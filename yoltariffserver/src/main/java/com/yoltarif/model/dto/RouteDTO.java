package com.yoltarif.model.dto;

public class RouteDTO {

	
	String startNodeId;
	String startNodeWtk;
	String startNodeLineName;
	String startNodeName;
	
	String endNodeId;
	String endNodeWkt;
	String endNodeLineName;
	String endNodeName;
	
	String duration;
	String distance;
	String travelType;
	
	public String getStartNodeId() {
		return startNodeId;
	}
	public void setStartNodeId(String startNodeId) {
		this.startNodeId = startNodeId;
	}
	public String getStartNodeWtk() {
		return startNodeWtk;
	}
	public void setStartNodeWtk(String startNodeWtk) {
		this.startNodeWtk = startNodeWtk;
	}
	public String getStartNodeLineName() {
		return startNodeLineName;
	}
	public void setStartNodeLineName(String startNodeLineName) {
		this.startNodeLineName = startNodeLineName;
	}
	public String getStartNodeName() {
		return startNodeName;
	}
	public void setStartNodeName(String startNodeName) {
		this.startNodeName = startNodeName;
	}
	public String getEndNodeId() {
		return endNodeId;
	}
	public void setEndNodeId(String endNodeId) {
		this.endNodeId = endNodeId;
	}
	public String getEndNodeWkt() {
		return endNodeWkt;
	}
	public void setEndNodeWkt(String endNodeWkt) {
		this.endNodeWkt = endNodeWkt;
	}
	public String getEndNodeLineName() {
		return endNodeLineName;
	}
	public void setEndNodeLineName(String endNodeLineName) {
		this.endNodeLineName = endNodeLineName;
	}
	public String getEndNodeName() {
		return endNodeName;
	}
	public void setEndNodeName(String endNodeName) {
		this.endNodeName = endNodeName;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getTravelType() {
		return travelType;
	}
	public void setTravelType(String travelType) {
		this.travelType = travelType;
	}
	
	
}
