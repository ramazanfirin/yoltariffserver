package com.yoltarif.model;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
@TypeAlias(value="Line")
public class Line extends AbstractEntity{

	String name;
	
	@RelatedTo(type="startNode",direction = Direction.OUTGOING)
	private Station startStation;
	
	@RelatedTo(type="endNode",direction = Direction.OUTGOING)
	private Station endStation = new Station();
	
	@RelatedTo(type="contains",direction = Direction.OUTGOING)
	private Set<Station> stationList = new LinkedHashSet<Station>();

	public List<StationDTO> sortedStationList = new ArrayList<StationDTO>();

	public List<Long> startTime = new ArrayList<Long>();
	
    public List<StationDTO> getSortedStationList() {
		return sortedStationList;
	}


	public void setSortedStationList(List<StationDTO> sortedStationList) {
		this.sortedStationList = sortedStationList;
	}


	public void addStation(Station station){
		
    	
    }


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Station getStartStation() {
		return startStation;
	}


	public void setStartStation(Station startStation) {
		this.startStation = startStation;
	}


	public Station getEndStation() {
		return endStation;
	}


	public void setEndStation(Station endStation) {
		this.endStation = endStation;
	}


	public Set<Station> getStationList() {
		return stationList;
	}


	public void setStationList(Set<Station> stationList) {
		this.stationList = stationList;
	}


	public List<Long> getStartTime() {
		return startTime;
	}


	public void setStartTime(List<Long> startTime) {
		this.startTime = startTime;
	}


	
}



