package com.yoltarif.model;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
@TypeAlias(value="Station")
public class Station extends AbstractEntity{

	String name;
	
	 
	@Indexed(indexType = IndexType.POINT, indexName = "location") 
	String wkt;
	
	
	String orijinalwkt;
	
	Boolean processed=false;


	@RelatedTo(type="contains",direction = Direction.INCOMING)
	private Set<Line> lineList = new LinkedHashSet<Line>();
	
	@RelatedToVia(type = "next",direction = Direction.OUTGOING)
	private Set<NextStation> stationList = new LinkedHashSet<NextStation>();
	
	@RelatedToVia(type = "next",direction = Direction.INCOMING)
	private Set<NextStation> preivousList = new LinkedHashSet<NextStation>();
	
	private List<Long> arriveTime = new ArrayList<Long>();
	
	private Long stationIndex; 
	
	

	public void setWkt(String wkt) {
		this.wkt = wkt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWkt() {
		return wkt;
	}

	public void setWkt(float lon, float lat) {
		this.wkt =String.format("POINT( %f %f )",lon,lat);
	}

	public String getOrijinalwkt() {
		return orijinalwkt;
	}

	public void setOrijinalwkt(float lon, float lat) {
		this.orijinalwkt = String.format("POINT( %.2f %.2f )",lon,lat);
	}

	

	public void setOrijinalwkt(String orijinalwkt) {
		this.orijinalwkt = orijinalwkt;
	}
	
	public String getLat(){
		if(wkt==null || wkt.equals(""))
			return 	"";
		
		String temp = wkt.replace("POINT( ", "");
		temp = temp.replace(" )", "");		
		String[] result = temp.split(" ");
		return result[1];
	}
	
	public String getLng(){
		if(wkt==null || wkt.equals(""))
			return 	"";
		
		String temp = wkt.replace("POINT( ", "");
		temp = temp.replace(" )", "");		
		String[] result = temp.split(" ");
		return result[0];
	}

	public Set<NextStation> getStationList() {
		return stationList;
	}

	public void setStationList(Set<NextStation> stationList) {
		this.stationList = stationList;
	}

	public Set<NextStation> getPreivousList() {
		return preivousList;
	}

	public void setPreivousList(Set<NextStation> preivousList) {
		this.preivousList = preivousList;
	}

	public Set<Line> getLineList() {
		return lineList;
	}

	public void setLineList(Set<Line> lineList) {
		this.lineList = lineList;
	}

	public Boolean getProcessed() {
		return processed;
	}

	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}

	public List<Long> getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(List<Long> arriveTime) {
		this.arriveTime = arriveTime;
	}

	public Long getStationIndex() {
		return stationIndex;
	}

	public void setStationIndex(Long stationIndex) {
		this.stationIndex = stationIndex;
	}

}
