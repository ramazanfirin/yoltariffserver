package com.yoltariffserver.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CityCurfTest {

	public static void main(String[] args) {
		try {
			//parseIlce();
			//getMahalleList("NO=124&AD=KEYKUBAT MAH.");
			//parseSokak();
			//getBinaList("NO=257690&AD=");
			getCoordinate("225960");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getUrl(String url) throws Exception{
		HttpClient client = new DefaultHttpClient();
		
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		
		BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
		result.append(line);
	}

	
	return result.toString();
	}
	
	public static List<SelectItem> getIlceList() throws Exception{
		String url = "http://cbs.kayseri.bel.tr/KIlce.aspx";
		List  <SelectItem> selectItemList = new ArrayList<SelectItem>();
		try {
			String result= getUrl(url);
			
			Document doc = Jsoup.parse(result);
			Elements impress = doc.getElementsByClass("imgcursor");
			
			
			for (Iterator iterator = impress.iterator(); iterator.hasNext();) {
				Element element = (Element) iterator.next();
//			if(element.attributes().size()>0)
//				continue;

				String ilce  = new String(element.attr("title").getBytes(), "UTF-8");
				String onclick = element.attr("onclick");
				onclick = onclick.replace("JsMahGoster", "");
				onclick = onclick.replace("(", "");
				onclick = onclick.replace(")", "");
				onclick = onclick.replace("this.id,", "");
				onclick = onclick.replace(",this.title", "");
				onclick = onclick.replace(";", "");
			    
				//System.console().writer().println(ilce + " "+onclick);
				SelectItem item = new SelectItem("NO="+onclick+"&AD=",ilce);
				selectItemList.add(item);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return selectItemList;
	}
	
	public static List <SelectItem>  getMahalleList(String url1) throws Exception{
		String urlEncoded = URLEncoder.encode(url1, "UTF-8");
		String url = "http://cbs.kayseri.bel.tr/KMahalle.aspx?"+url1;
		List  <SelectItem> selectItemList = new ArrayList<SelectItem>();
		try {
			String result= getUrl(url);
			
			Document doc = Jsoup.parse(result);
			Elements impress = doc.getElementsByAttributeValue("style", "border-width: 0px");
			for (Iterator iterator = impress.iterator(); iterator.hasNext();) {
				Element element = (Element) iterator.next();
				String mahalle  = new String(element.attr("title").getBytes(), "UTF-8");
				
				
			
				String onclick = element.attr("onclick");
				onclick = onclick.replace("JScsbmGetir", "");
				onclick = onclick.replace("(", "");
				onclick = onclick.replace(")", "");
				onclick = onclick.replace("this.id,", "");
				onclick = onclick.replace(",this.title", "");
				onclick = onclick.replace(";", "");
				
				System.out.println(mahalle+" "+onclick);
				SelectItem item = new SelectItem("NO="+onclick+"&AD=",mahalle);
				selectItemList.add(item);
			}
			
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return selectItemList;
	}
	

	public static List <SelectItem>  getSokakList(String url1) throws Exception{
		List  <SelectItem> selectItemList = new ArrayList<SelectItem>();
		String urlEncoded = URLEncoder.encode(url1, "UTF-8");
		String url = "http://cbs.kayseri.bel.tr/Kcsbm.aspx?"+url1;
		try {
			String result= getUrl(url);
			
			Document doc = Jsoup.parse(result);
			Elements impress = doc.getElementsByClass("cursorhand");
			for (Iterator iterator = impress.iterator(); iterator.hasNext();) {
				Element element = (Element) iterator.next();
				String sokak  = new String(element.attr("title").getBytes(), "UTF-8");
				
				
			
				String onclick = element.attr("onclick");
				onclick = onclick.replace("JSKapiGetir", "");
				onclick = onclick.replace("(", "");
				onclick = onclick.replace(")", "");
				onclick = onclick.replace("this.id,", "");
				onclick = onclick.replace(",this.title", "");
				onclick = onclick.replace(";", "");
				
				if(!sokak.equals("") && !(sokak==null)){
					SelectItem item = new SelectItem("NO="+onclick+"&AD=",sokak);
					selectItemList.add(item);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return selectItemList;
	}
	
	public static List <SelectItem> getBinaList(String url1) throws Exception{
		String urlEncoded = URLEncoder.encode(url1, "UTF-8");
		String url = "http://cbs.kayseri.bel.tr/KBina.aspx?"+url1;
		List  <SelectItem> selectItemList = new ArrayList<SelectItem>();
		try {
			String result= getUrl(url);
			Document doc = Jsoup.parse(result);
			Elements impress = doc.getElementsByClass("trAlternative");
			Elements impress2 = doc.getElementsByClass("trNormal");
			impress.addAll(impress2);
			
			for (Iterator iterator = impress.iterator(); iterator.hasNext();) {
				Element element = (Element) iterator.next();
				Elements elementList  =element.getElementsByAttributeValue("align", "left");
				
				//System.out.println(elementList.get(0).html());
				
				Elements onclickList =elementList.get(3).getElementsByAttribute("onclick");
				if(onclickList.size()==0)
					continue ;
							
				String onclik=onclickList.get(0).attr("onclick");
				
				onclik = onclik.replace("JSBinaCiz", "");
				onclik = onclik.replace("(", "");
				onclik = onclik.replace(")", "");
				onclik = onclik.replace("\"", "");
				
				String bina = elementList.get(0).html();
				
				SelectItem item = new SelectItem(onclik,bina);
				selectItemList.add(item);
			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		System.out.println(selectItemList.size());
		return selectItemList;
	}
	
	public static List<String> getCoordinate(String binaNo) throws Exception{
		List<String> returnList= new ArrayList<String>();
		
		String url = "http://cbs.kayseri.bel.tr/Rehber.aspx/GetKapiBina";
		//url = "http://localhost/Rehber.aspx/GetKapiBina";
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		
		StringEntity input = new StringEntity("{\"binaNO\":"+binaNo+"}");
		input.setContentType("application/json");
		post.setEntity(input);
		
		post.setHeader("Accept", "*/*");
		post.setHeader("Accept-Encoding", "gzip,deflate");
		post.setHeader("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.6,en;q=0.4");
		post.setHeader("Connection", "keep-alive");
		post.setHeader("Content-Type", "application/json; charset=UTF-8");
		post.setHeader("Host", "cbs.kayseri.bel.tr");
		post.setHeader("X-Requested-With", "XMLHttpRequest");
		post.setHeader("Referer", "http://cbs.kayseri.bel.tr/Rehber.aspx");
		HttpResponse response = client.execute(post);
		
		BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		String returnValue="";
		returnValue = result.toString().replace("{\"d\":{\"__type\":\"Rehber+sobje\",\"deger\":\"", "");
		returnValue = returnValue.replace("\"}}", "");
		returnValue = returnValue.replace("||", "&");
		returnValue = returnValue.replace("|", "@");
		
		String[] values= returnValue.split("&");
		
		String[] temp  =values[0].split("@");
		String lat=temp[0];
		String lng=temp[1];
		String[]  coordinates = convertCoordinate( lat,lng).split(" ");
	    Float lngFloat= Float.parseFloat(coordinates[0]);
		Float latFloat= Float.parseFloat(coordinates[1]);
		lngFloat = lngFloat-Float.parseFloat("0.000230");
		latFloat = latFloat-Float.parseFloat("0.000922");
		returnList.add(String.valueOf(latFloat));
		returnList.add(String.valueOf(lngFloat));
		
//		for (int i = 0; i < values.length; i++) {
//			String[] temp  =values[i].split("@");
//			returnList.add(temp[0]);
//			returnList.add(temp[1]);
//		}
		
		return returnList;
	}
	
	
	public static String convertCoordinate(String x,String y) throws ClientProtocolException, IOException{
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
	
//	public static void getKapiNo() throws Exception{
//		
////		List<String> a =CityCurfUtil.getKapiNo("225960");
////		System.out.println("bitti");
//	}
}
