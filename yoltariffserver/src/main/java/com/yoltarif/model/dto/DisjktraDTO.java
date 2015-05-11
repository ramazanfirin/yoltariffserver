package com.yoltarif.model.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yoltarif.model.NextStation;
import com.yoltarif.model.Station;

public class DisjktraDTO {

	String startNodeId;
	String endNodeId;
	Double weigth;
	
	List<String> nodeIdList= new ArrayList<String>();
	List<String> relationIdList= new ArrayList<String>();
	
	List<Station> stationList  = new ArrayList<Station>();
	List<NextStation> relationshipList = new ArrayList<NextStation>();
	
	Double totalDistance;
	Double totalDuration;
	
	
	public String getRelationshipAsString(){
		String result="";
		for (Iterator iterator = relationIdList.iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			result = result+type+",";
			
			
		}
		result = result.substring(0,result.length()-1);
		return result;
	}
	
	public String getNodesAsString(){
		String result="";
		for (Iterator iterator = nodeIdList.iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			result = result+type+",";
			
			
		}
		result = result.substring(0,result.length()-1);
		return result;
	}
	
	
	public String getStartNodeId() {
		return startNodeId;
	}
	public void setStartNodeId(String startNodeId) {
		this.startNodeId = startNodeId;
	}
	public String getEndNodeId() {
		return endNodeId;
	}
	public void setEndNodeId(String endNodeId) {
		this.endNodeId = endNodeId;
	}
	
	
	public List<String> getRelationIdList() {
		return relationIdList;
	}
	public void setRelationIdList(List<String> relationIdList) {
		this.relationIdList = relationIdList;
	}
	public Double getWeigth() {
		return weigth;
	}
	public void setWeigth(Double weigth) {
		this.weigth = weigth;
	}
	public List<String> getNodeIdList() {
		return nodeIdList;
	}
	public void setNodeIdList(List<String> nodeIdList) {
		this.nodeIdList = nodeIdList;
	}
	public List<Station> getStationList() {
		return stationList;
	}
	public void setStationList(List<Station> stationList) {
		this.stationList = stationList;
	}
	public List<NextStation> getRelationshipList() {
		return relationshipList;
	}
	public void setRelationshipList(List<NextStation> relationshipList) {
		this.relationshipList = relationshipList;
	}
	public Double getTotalDistance() {
		return totalDistance;
	}
	public void setTotalDistance(Double totalDistance) {
		this.totalDistance = totalDistance;
	}
	public Double getTotalDuration() {
		return totalDuration;
	}
	public void setTotalDuration(Double totalDuration) {
		this.totalDuration = totalDuration;
	}
}
