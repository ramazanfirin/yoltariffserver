package com.yoltarif.util;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.Duration;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;


public class Util {
	private static  GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyA5vr74kwURbtKibIJUcKIe0Y019xj2gwE");
public static List<String> parseHatInformation() {
		
		List<String> resultList = new ArrayList<String>();
		BufferedReader br = null;
		StringBuilder fileContents = new StringBuilder();
		 
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("D:\\calismalar\\spatial2\\YolTarif\\hatBilgisi"));
			
			br = new BufferedReader( new InputStreamReader(new FileInputStream("D:\\calismalar\\spatial2\\YolTarif\\hatBilgisi"), "UTF8"));
			 

			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				fileContents.append(sCurrentLine);
			}

			String result = fileContents.toString();
			//result = result.replace("{\"value\":\".*\"},", "");
			result = result.replace("\",\"","@" );
			
			String[] list=result.split(",");
			for (int i = 0; i < list.length; i++) {
				if(list[i].startsWith("{\"text\"")){
					String line = list[i];
					line = line.replace("{\"text\":\"", "");
					line = line.replace("value\":\"", "");
					line = line.replace("\"}", "");
					//System.out.println(line);
					
					//String[] values  = line.split("@");
					resultList.add(line);
				
				
				}
			}
			
			//System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return resultList;
	}
	

public static List<String> parseStationInformation(String hotId) throws Exception{
	String url = "http://cbs.kayseri.bel.tr/UCGuzergah.aspx?NO="+hotId;
	List<String> resultList = new ArrayList<String>();
	String result= Util.getUrl(url);
	
	Document doc = Jsoup.parse(result);
	Elements impress = doc.getElementsByAttribute("onclick");
	impress = doc.getElementsByAttributeValue("class", "link");
	for (Iterator iterator = impress.iterator(); iterator.hasNext();) {
		Element element = (Element) iterator.next();
		
		String onclick=element.attr("onclick");
		if(!onclick.startsWith("JSDurakKorinat"))
			continue;
		
		String name =element.html();
		String coordinate  = onclick.replace("JSDurakKorinataGir(\"", "").replace("\")", "");
		resultList.add(name+","+coordinate);	
	}
	System.out.println("bitti");
	return resultList;
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

public static String getStationCoordinates(String cbNo) throws Exception{
	String url = "http://cbs.kayseri.bel.tr/Ulasim.aspx/JSDurakKordinatAdi";
	//url = "http://localhost/Ulasim.aspx/JSDurakKordinatAdi";
	String y="\""+"\"";
	String x= "\""+cbNo+"\"";
	HttpClient client = new DefaultHttpClient();
	HttpPost post = new HttpPost(url);
	
	StringEntity input = new StringEntity("{\"cbNO\":"+x+",\"no\":"+y+"}");
	input.setContentType("application/json");
	post.setEntity(input);

	post.setHeader("Accept", "*/*");
	post.setHeader("Accept-Encoding", "gzip,deflate");
	post.setHeader("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.6,en;q=0.4");
	post.setHeader("Connection", "keep-alive");
	post.setHeader("Content-Type", "application/json; charset=UTF-8");
	post.setHeader("Host", "cbs.kayseri.bel.tr");
	post.setHeader("X-Requested-With", "XMLHttpRequest");
	post.setHeader("Referer", "http://cbs.kayseri.bel.tr/Ulasim.aspx");
	post.setHeader("Cookie", "ASP.NET_SessionId=klflqsxnogvmw5knoabxvu25");
	post.setHeader("Origin","http://cbs.kayseri.bel.tr");
	post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.3");
	HttpResponse response = client.execute(post);
	
	BufferedReader rd = new BufferedReader(
            new InputStreamReader(response.getEntity().getContent()));

	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
		result.append(line);
	}
	
	
	
	String returnValue = result.toString(); 
	returnValue = returnValue.replace("{\"d\":\"","");
	returnValue = returnValue.replace("\"}","");
	//System.out.println(returnValue);
	return returnValue;

}

public static String getRandomColor(){
	int red = (int) (( Math.random()*255)+1);
	int green = (int) (( Math.random()*255)+1);
	int blue = (int) (( Math.random()*255)+1);

	 
	Color RandomC = new Color(red,green,blue);
	
	
	
	int RandomRGB = (RandomC.getRGB());
	String RandomRGB2Hex = Integer.toHexString(RandomRGB);
	if(RandomRGB2Hex.length()>6)
		RandomRGB2Hex = RandomRGB2Hex.substring(0, 6);
 
	 return RandomRGB2Hex;
}

public static String getLat(String wkt){
	String temp = wkt.replace("POINT( ", "");
	temp = temp.replace(" )", "");		
	String[] result = temp.split(" ");
	return result[1];
}

public static String getLng(String wkt){
	String temp = wkt.replace("POINT( ", "");
	temp = temp.replace(" )", "");		
	String[] result = temp.split(" ");
	return result[0];
}

public static void main(String[] args) throws Exception{
//	List<String> list = parseStationInformation(82);
//		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
//			String string = (String) iterator.next();
//			System.out.println(string);
//		}
	//getStationCoordinates("1479");
	int red = (int) (( Math.random()*255)+1);
	int green = (int) (( Math.random()*255)+1);
	int blue = (int) (( Math.random()*255)+1);

	 
	Color RandomC = new Color(red,green,blue);
	
	
	
	int RandomRGB = (RandomC.getRGB());
	String RandomRGB2Hex = Integer.toHexString(RandomRGB);
	if(RandomRGB2Hex.length()>6)
		RandomRGB2Hex = RandomRGB2Hex.substring(0, 6);
	 int EndRGB = RandomRGB2Hex.hashCode();
	 
	 
	System.out.println(RandomRGB2Hex);
}

public static String getMaxrixData(double sourceLat,double sourceLng,double destinationLat,double destinationLng, TravelMode mode){
	DistanceMatrix matrix =DistanceMatrixApi.newRequest(context)
	        .origins(new LatLng(sourceLat, sourceLng))
	        .destinations(new LatLng(destinationLat, destinationLng))
	         .mode(mode.DRIVING)
	         .language("tr-TR")
	        .awaitIgnoreError();
			
			DistanceMatrixRow row =matrix.rows[0];
		    DistanceMatrixElement[] elements = row.elements;
		    Distance distance  = elements[0].distance;
		    Duration duration = elements[0].duration;
		    
		    
		    
		    BigDecimal b = new BigDecimal(duration.inSeconds);  
		    BigDecimal c = new BigDecimal(60);
		    BigDecimal d=new BigDecimal(b.doubleValue()/c.doubleValue());
		    
		    d = d.setScale(2, BigDecimal.ROUND_FLOOR);  
		    
//		    System.out.println(distance.inMeters);
//		    System.out.println(d.doubleValue());
//		    
		    return distance.inMeters+","+d.doubleValue();
	
}

public static DistanceMatrix getMaxrixData(LatLng[] sourceLatLng,LatLng[] destinationLatLng,TravelMode mode){
	DistanceMatrix matrix =DistanceMatrixApi.newRequest(context)
	        .origins(sourceLatLng)
	        .destinations(destinationLatLng)
	         .mode(mode.DRIVING)
	         .language("tr-TR")
	        .awaitIgnoreError();
	return matrix;
}

}
