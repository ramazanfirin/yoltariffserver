package com.yoltarif.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.yoltarif.model.dto.DisjktraDTO;



public class Neo4jUtil {
	
public static void main(String[] args) {
	calculateDisjktra("11368", "11365");
}
	
public static DisjktraDTO 	calculateDisjktra(String source,String destination){
	long startDate = (new Date()).getTime();
	String sql=callDisjktra(source,destination);
	DisjktraDTO result = new DisjktraDTO();
	
	Object obj=JSONValue.parse(sql);
	JSONArray array=(JSONArray)obj;
	
	JSONObject object=(JSONObject) array.get(0);
		
	Double weigth = (Double)object.get("weight");
	result.setWeigth(weigth);
		
	String start = (String)object.get("start");
	result.setStartNodeId(parseId(start));
	
	JSONArray arrayNode =(JSONArray)object.get("nodes");
	for (int i = 0; i < arrayNode.size(); i++) {
		//System.out.println(arrayNode.get(i));
		result.getNodeIdList().add(parseId(arrayNode.get(i).toString()));
	}
	Long length =(Long)object.get("length");
	JSONArray arrayrelationships =(JSONArray)object.get("relationships");
	for (int i = 0; i < arrayrelationships.size(); i++) {
		//System.out.println(arrayrelationships.get(i));
		result.getRelationIdList().add(parseId(arrayrelationships.get(i).toString()));
	}
	String end = (String)object.get("end");
	result.setEndNodeId(parseId(end));
	long endDate = (new Date()).getTime();
	System.out.println("disjkta convert = "+(endDate-startDate));
	
	return result;
}
	
public static String parseId(String input){
	
	String[] result = input.split("/");
	return result[result.length - 1];
	
}

public static String callDisjktra(String source,String destination){
	try {
		long startDate = (new Date()).getTime();
		String sourceId=source;
		String destId=destination;
		String url = "http://localhost:7474/db/data/node/";
		//url = "http://localhost/Ulasim.aspx/JSDurakKordinatAdi";

		HttpClient client = new DefaultHttpClient();

		HttpPost post = new HttpPost(url+sourceId+"/paths");
		
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\"to\":\""+url+destId+ "\",");
		buffer.append("\"cost_property\":"+"\"duration\""+",");
		buffer.append("\"relationships\":{");
		buffer.append("\"type\" : \"next\",");
		buffer.append("\"direction\" : \"out\"");
		buffer.append("},");
		buffer.append("\"algorithm\" : \"dijkstra\"");
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
		
//		System.out.println(result);
//		System.out.println(new Date()+" batti");
		long endDate = (new Date()).getTime();
		System.out.println("disjkta hesaplama = "+(endDate-startDate));
		return result.toString();
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return "";
}

}
