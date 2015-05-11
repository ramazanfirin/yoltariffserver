package com.yoltarif.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.yoltarif.model.StationDTO;

public class RestUtil {

	static String serverAddress="http://www.kaliteyazilim.com:8484/";
	
	public static void main(String[] args) throws Exception {
		getLayer("a");
	}
	
	
	public static JSONObject createStation(StationDTO dto) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("name", dto.getName());
		map.put("wkt",dto.getWkt());
		
		JSONObject result = createNode(map);
		addLabel((String)result.get("labels"),"Station");
		addToLocationIndex((String)result.get("self"));
		//addToIndex((String)result.get("self"));
		
		String[] datas= ((String)result.get("self")).split("/");
		String id = datas[datas.length-1];
		
		//updateGeometryFromWKT(dto.getWkt(),new Long(id));
		
		return result;
		
	}
	
	public static void updateGeometryFromWKT(String point,Long id) throws Exception{
		HttpClient client = new DefaultHttpClient();

		String url = serverAddress+"db/data/ext/SpatialPlugin/graphdb/updateGeometryFromWKT";
		HttpPost post = new HttpPost(url);
		
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\"layer\":\""+"location"+ "\",");
		buffer.append("\"geometry\":\""+point+ "\",");
		buffer.append("\"geometryNodeId\":"+id+ "");
		
		buffer.append("}");
		
		StringEntity input = new StringEntity(buffer.toString());
		
		
		input.setContentType("application/json");
		post.setEntity(input);

		post.setHeader("Accept", "*/*");
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Accept", "application/json; charset=UTF-8");



		HttpResponse response = client.execute(post);
		
		
		BufferedReader rd = new BufferedReader(
		        new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		System.out.println(result);
		if(!(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==204))
			throw new Exception("hata");
	}
	
	public static  JSONObject createNode(Map<String, Object> map) throws Exception{
		HttpClient client = new DefaultHttpClient();

		String url = serverAddress+"db/data/node";
		HttpPost post = new HttpPost(url);
		
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
			String  name = (String ) iterator.next();
			Object value = map.get(name);
			
			buffer.append("\""+name+"\":\""+value+ "\",");
		}
		
		String inputData = buffer.toString();
		inputData = inputData.substring(0, inputData.length()-1);
		inputData = inputData+"}";
		
		StringEntity input = new StringEntity(inputData);
		
		
		input.setContentType("application/json");
		post.setEntity(input);

		post.setHeader("Accept", "*/*");
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Accept", "application/json; charset=UTF-8");



		HttpResponse response = client.execute(post);
		
		BufferedReader rd = new BufferedReader(
		        new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
	System.out.println(result);
	if(!(response.getStatusLine().getStatusCode()==201 || response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==204))
		throw new Exception("hata");
		
		Object obj=JSONValue.parse(result.toString());
		JSONObject array=(JSONObject)obj;
		
		System.out.println(new Date()+" batti");		
		//return result.toString();
		return array;
	}
	
	public static  void addToIndex(String nodeUrl) throws Exception{
		HttpClient client = new DefaultHttpClient();

		String url = serverAddress+"db/data/ext/SpatialPlugin/graphdb/addNodeToLayer";
		HttpPost post = new HttpPost(url);
		
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\"layer\":\""+"location"+ "\",");
		buffer.append("\"node\":\""+nodeUrl+ "\"");
		
		buffer.append("}");
		
		StringEntity input = new StringEntity(buffer.toString());
		
		
		input.setContentType("application/json");
		post.setEntity(input);

		post.setHeader("Accept", "*/*");
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Accept", "application/json; charset=UTF-8");



		HttpResponse response = client.execute(post);
		
		
		BufferedReader rd = new BufferedReader(
		        new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		System.out.println(result);
		if(!(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==204))
			throw new Exception("hata");
		//		System.out.println(new Date()+" batti");
		//return result.toString();
	}
	
	public static void addLabel(String url,String label) throws Exception{
		
		HttpClient client = new DefaultHttpClient();

		
		HttpPost post = new HttpPost(url);
		
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("\""+label+"\"");
		StringEntity input = new StringEntity(buffer.toString());
		
		
		input.setContentType("application/json");
		post.setEntity(input);

		post.setHeader("Accept", "*/*");
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Accept", "application/json; charset=UTF-8");



		HttpResponse response = client.execute(post);
		
		
			
//		BufferedReader rd = new BufferedReader(
//		        new InputStreamReader(response.getEntity().getContent()));
//
//		StringBuffer result = new StringBuffer();
//		String line = "";
//		while ((line = rd.readLine()) != null) {
//			result.append(line);
//		}
//		
//		System.out.println(result);
		if(!(response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==204))
			throw new Exception("hata");		
	}
	
	public static void  getLayer(String layer) throws Exception{
HttpClient client = new DefaultHttpClient();

		
		HttpPost post = new HttpPost(serverAddress+"/db/data/ext/SpatialPlugin/graphdb/getLayer");
		
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\"layer\":\""+"location"+ "\"");
		
		
		buffer.append("}");
		
		
		
		StringEntity input = new StringEntity(buffer.toString());
		
		
		input.setContentType("application/json");
		post.setEntity(input);

		post.setHeader("Accept", "*/*");
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Accept", "application/json; charset=UTF-8");
		HttpResponse response = client.execute(post);
		
		BufferedReader rd = new BufferedReader(
        new InputStreamReader(response.getEntity().getContent()));

	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
		result.append(line);
	}
System.out.println(result);
	}
	
	
	public static void addToLocationIndex(String node) throws Exception{
		HttpClient client = new DefaultHttpClient();

		String url = serverAddress+"db/data/index/node/location";
		HttpPost post = new HttpPost(url);
		
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\"value\":\""+"dummy"+ "\",");
		buffer.append("\"key\":\""+"dummy"+ "\",");
		buffer.append("\"uri\":\""+node+ "\"");
		buffer.append("}");
		
		StringEntity input = new StringEntity(buffer.toString());
		
		
		input.setContentType("application/json");
		post.setEntity(input);

		post.setHeader("Accept", "*/*");
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Accept", "application/json; charset=UTF-8");



		HttpResponse response = client.execute(post);
		
		
//		BufferedReader rd = new BufferedReader(
//		        new InputStreamReader(response.getEntity().getContent()));
//
//		StringBuffer result = new StringBuffer();
//		String line = "";
//		while ((line = rd.readLine()) != null) {
//			result.append(line);
//		}
		
		//System.out.println(result);
		if(!(response.getStatusLine().getStatusCode()==201 || response.getStatusLine().getStatusCode()==200 || response.getStatusLine().getStatusCode()==204))
			throw new Exception("hata");
		//		System.out.println(new Date()+" batti");
		//return result.toString();
	}
}
