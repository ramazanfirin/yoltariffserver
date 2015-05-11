package com.yoltarif.model.dto;

import java.util.ArrayList;
import java.util.List;

public class Decision {

	List<RouteDTO> routes = new ArrayList<RouteDTO>() ;
	
	Integer totalDistance;
	
	Double totalDuration;
	
	Integer lineCount;
	
	Double totalWalkingTime;

	

	
	public List<RouteDTO> getRoutes() {
		return routes;
	}

	public void setRoutes(List<RouteDTO> routes) {
		this.routes = routes;
	}

	public Integer getLineCount() {
		return lineCount;
	}

	public void setLineCount(Integer lineCount) {
		this.lineCount = lineCount;
	}

	public Integer getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(Integer totalDistance) {
		this.totalDistance = totalDistance;
	}

	public Double getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(Double totalDuration) {
		this.totalDuration = totalDuration;
	}

	public Double getTotalWalkingTime() {
		return totalWalkingTime;
	}

	public void setTotalWalkingTime(Double totalWalkingTime) {
		this.totalWalkingTime = totalWalkingTime;
	}
	
}
