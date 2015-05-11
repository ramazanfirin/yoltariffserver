package com.yoltarif.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class OnlineConverterUtil {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		//convert();
		convertPost("455862.00", "4287455.00");
	}
	
	public static void convert() throws ClientProtocolException, IOException{
	
		String url = "http://localhost/ArcGIS/rest/services/Geometry/GeometryServer/project";
		
		HttpClient client = new DefaultHttpClient();
		
		String encoded  ="?inSR=2322&outSR=4326&f=pjson&geometries={\"geometryType\" : \"esriGeometryPoint\",\"geometries\" : [ {\"x\" : 455862.00, \"y\" : 4287455.00}, {\"x\" : 4287455.00, \"y\" : 455862.00}]}";
		
		encoded = URLEncoder.encode(encoded, "UTF8");
		HttpGet request = new HttpGet(url+encoded);
//		client.getParams().setParameter("inSR", "2322");
//		client.getParams().setParameter("outSR", "4326");
//		client.getParams().setParameter("geometries", "{\"geometryType\" : \"esriGeometryPoint\",\"geometries\" : [ {\"x\" : 455862.00, \"y\" : 4287455.00}, {\"x\" : 4287455.00, \"y\" : 455862.00}]}");
//		client.getParams().setParameter("f", "pjson");
//		
	
		HttpResponse response = client.execute(request);
		
		BufferedReader rd = new BufferedReader(
	            new InputStreamReader(response.getEntity().getContent()));

	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
		result.append(line);
	}
		System.out.println(result);
	System.out.println("bitti");

	}
	
	public static String convertPost(String x,String y) throws ClientProtocolException, IOException{
		String url = "http://geoweb.hft-stuttgart.de/openwctsv2/result.php";
		//url = "http://localhost/Ulasim.aspx/JSDurakKordinatAdi";
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("search", "codes"));
		urlParameters.add(new BasicNameValuePair("source", "2322"));
		urlParameters.add(new BasicNameValuePair("target", "4326"));
		urlParameters.add(new BasicNameValuePair("source_name:", ""));
		urlParameters.add(new BasicNameValuePair("target_name", ""));
		urlParameters.add(new BasicNameValuePair("source_name:", ""));
		urlParameters.add(new BasicNameValuePair("coords", x+" "+ y));
		urlParameters.add(new BasicNameValuePair("source_name:", ""));
		urlParameters.add(new BasicNameValuePair("format", "api"));
		urlParameters.add(new BasicNameValuePair("submit", "1"));
		
		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		 
		HttpResponse response = client.execute(post);
		
		BufferedReader rd = new BufferedReader(
	            new InputStreamReader(response.getEntity().getContent()));
	
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		System.out.println(result);
		return result.toString();
	}
}
