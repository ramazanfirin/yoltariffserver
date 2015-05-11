package com.yoltarif.util;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.yoltarif.model.Line;
import com.yoltarif.model.LineWrapper;
import com.yoltarif.model.Station;
import com.yoltarif.model.StationDTO;

public class XStreamUtil {
public static void main(String[] args) throws Exception{
createXmlFile();
//parseTest();
}
public static void createXmlFile() throws Exception{
	LineWrapper lineWrapper = new LineWrapper();
	System.out.println(new Date());
	int i=0;
	List<String> hatList=Util.parseHatInformation();	
	for (Iterator iterator = hatList.iterator(); iterator.hasNext();) {
		try {
			i++;
			System.out.println(i+ " yapiliyor");
//			if(i<40)
//				continue;
				
			String hat = (String) iterator.next();
			String[] values = hat.split("@");
			String name = values[0];
			String id= values[1];

			Line line = new Line();
			line.setName(name);
//			if(!line.getName().contains("495")){
//				continue;
//			}else{
//				System.out.println("dikkat");
//			}
			
			lineWrapper.getLineList().add(line);
			List<String> stationList = Util.parseStationInformation(id);

			for (Iterator iterator2 = stationList.iterator(); iterator2.hasNext();) {
				try {
					String string = (String) iterator2.next();
					String[] values2 = string.split(",");
					String name2 = values2[0];
					String id2= values2[1];
					
					StationDTO station = new StationDTO();
					station.setName(name2);
					
					
					String coordinate = Util.getStationCoordinates(id2);
					coordinate = coordinate.replace("|","@");
					String[] coordinates = coordinate.split("@");
					String lng = coordinates[0].split(",")[0];
					String lat = coordinates[1].split(",")[0];
					
					String[] result = OnlineConverterUtil.convertPost(lng, lat).split(" ");
					Float lngFloat= Float.parseFloat(result[0]);
					Float latFloat= Float.parseFloat(result[1]);
					lngFloat = lngFloat-Float.parseFloat("0.000230");
					latFloat = latFloat-Float.parseFloat("0.000922");
					
					station.setWkt(lngFloat, latFloat);
					station.setOrijinalwkt(Float.parseFloat(lng), Float.parseFloat(lat));
					//line.getStationList().add(station);
					line.getSortedStationList().add(station);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
			}
			if(line.getName().contains("495")){
				System.out.println("geldi");
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			continue;
		}
	}
	
	
	
	XStream xstream = new XStream();
	
	String result = xstream.toXML(lineWrapper);
	System.out.println(result);
	System.out.println(new Date());
	
	ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("D:\\calismalar\\spatial2\\YolTarif\\hatBilgi5.xml"));
	out.writeObject(result);
	out.close();
}

public void test(){
LineWrapper lineWrapper = new LineWrapper();
	
	Line line = new Line();
	line.setName("E10");
	//lineService.save(line);
	
	Station station = new Station();
	station.setName("Durak1");
	
	Station station2 = new Station();
	station2.setName("Durak2");
	
	Station station3 = new Station();
	station3.setName("Durak3");
	
	line.getStationList().add(station);
	line.getStationList().add(station2);
	line.getStationList().add(station3);

	lineWrapper.getLineList().add(line);
	XStream xstream = new XStream();
	
	String result = xstream.toXML(lineWrapper);
	System.out.println(result);
}

public static LineWrapper parseTest() throws IOException{
	
	
	BufferedReader br = new BufferedReader(new FileReader("D:\\calismalar\\spatial2\\YolTarif\\hatBilgi5.xml"));
	StringBuffer buff = new StringBuffer();
	String line;
	while((line = br.readLine()) != null){
	   buff.append(line);
	}
	
	
	XStream xstream = new XStream(new DomDriver());
	
	LineWrapper lineWrapper = (LineWrapper)xstream.fromXML(buff.toString());
	
	System.out.println(lineWrapper.getLineList().size());
	
	return lineWrapper;
}
}
