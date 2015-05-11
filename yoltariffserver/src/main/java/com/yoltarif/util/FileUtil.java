package com.yoltarif.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.yoltarif.model.LineWrapper;

public class FileUtil {

	public static LineWrapper getLines(ServletContext servletContext) throws Exception{
    	//BufferedReader br = new BufferedReader(new FileReader("D:\\calismalar\\spatial2\\YolTarif\\hatBilgi2.xml"));
    	//BufferedReader br= new BufferedReader( new InputStreamReader(new FileInputStream("D:\\calismalar\\spatial2\\YolTarif\\hatBilgi5.xml"), "UTF8"));
    	//BufferedReader br= new BufferedReader( new InputStreamReader(new FileInputStream("c:\\hatBilgi5.xml"), "UTF8"));
    	//BufferedReader br = new BufferedReader( new InputStreamReader(new FileInputStream("/mnt/ebs1/hatBilgi4.xml"), "UTF8"));
    	BufferedReader br= new BufferedReader( new InputStreamReader(servletContext.getResourceAsStream("/hatBilgisi5.xml"), "UTF8"));
    	
    	StringBuffer buff = new StringBuffer();
    	String line;
    	while((line = br.readLine()) != null){
    	   buff.append(line);
    	}
    	
    	
    	XStream xstream = new XStream(new DomDriver());
    	
    	LineWrapper lineWrapper = (LineWrapper)xstream.fromXML(buff.toString());
    	
    	return lineWrapper;
    	 
	}
	
}
