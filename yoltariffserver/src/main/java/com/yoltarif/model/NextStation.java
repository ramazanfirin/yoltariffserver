package com.yoltarif.model;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type = "MEMBER_OF")
public class NextStation {

	public enum TRAVEL_TYPE {
	    DRIVING,WALKING 
	}
	
	@GraphId 
	private Long id;
	
	@Fetch @StartNode 
	private Station startNode;
	
	@Fetch @EndNode 
	private Station endNode;
	
	private String travelType;
	
	private Double duration;
	
	private Long distance;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Station getStartNode() {
		return startNode;
	}

	public void setStartNode(Station startNode) {
		this.startNode = startNode;
	}

	public Station getEndNode() {
		return endNode;
	}

	public void setEndNode(Station endNode) {
		this.endNode = endNode;
	}

	public String getTravelType() {
		return travelType;
	}

	public void setTravelType(String travelType) {
		this.travelType = travelType;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}
	
}
