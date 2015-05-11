package com.yoltarif.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;

import com.yoltarif.model.dto.Decision;
import com.yoltarif.model.dto.DisjktraDTO;
import com.yoltarif.model.dto.RouteDTO;
import com.yoltarif.util.Neo4jUtil;



@Service
public class CalculationService {


	@Autowired
	private Neo4jTemplate template;
	
	
	public DisjktraDTO calcDisjktraDTO(String sourceNodeId,String destNodeId){
		
		DisjktraDTO disjktraDTO =Neo4jUtil.calculateDisjktra(sourceNodeId, destNodeId);
		//List<RouteDTO> listRoute = calculateRoute(disjktraDTO.getRelationshipAsString());
		
	
		return disjktraDTO;
	}
	
	
	public Decision calculateRoute(String idList){
		long startDate = (new Date()).getTime();
		Decision decision = new Decision();
		//List<RouteDTO> resultList = new ArrayList<RouteDTO>();
		String query  ="match (startLine)-[:contains]->(start)-[rels:next]->(dest)<-[:contains]-(targetLine) "+ 
						" where ID(rels) in ["+idList+"]" +
						" return ID(rels) as relID,"+
						"ID(start) as startID,start.wkt as startPoint,startLine.name as lineName ,start.name as startName,"+
						"ID(dest) as destID,dest.wkt as destPoint, targetLine.name as targetLineName,dest.name as destName,"+
						"rels.duration as duration,rels.distance as distance ,rels.travelType as travelType";
						;
		
		Result<Map<String,Object>> result =  template.query(query, null);
		//Map<String,Object> map=result.single();
		Integer totalDistance=0;
		Double totalDuration=new Double(0);
		Double totalWalkingTime = new Double(0);
		int lineCount;
		Map<String,String> lineMap= new HashMap<String,String>();
		
		Map<Integer,RouteDTO> tempMap = new HashMap<Integer, RouteDTO>();
		for (Iterator iterator = result.iterator(); iterator.hasNext();) {
            RouteDTO routeDTO = new RouteDTO();
			Map<String,Object> map= (Map<String,Object>) iterator.next();
            //System.out.println("");
			Integer relID=(Integer)map.get("relID");
            routeDTO.setStartNodeId(String.valueOf(map.get("startID")));
            routeDTO.setStartNodeWtk((String)map.get("startPoint"));
            routeDTO.setStartNodeLineName((String)map.get("lineName"));
            routeDTO.setStartNodeName((String)map.get("startName"));
            
            routeDTO.setEndNodeId(String.valueOf(map.get("destID")));
            routeDTO.setEndNodeWkt((String)map.get("destPoint"));
            routeDTO.setEndNodeLineName((String)map.get("targetLineName"));
            routeDTO.setEndNodeName((String)map.get("destName"));
            
            routeDTO.setDistance(String.valueOf(map.get("distance")));
            routeDTO.setDuration(String.valueOf(map.get("duration")));
            routeDTO.setTravelType((String)map.get("travelType"));
            
            if("WALKING".equals((String)map.get("travelType"))){
            	totalWalkingTime = totalWalkingTime+ (Double)map.get("duration");
            }
            
            //resultList.add(routeDTO);
            tempMap.put(relID, routeDTO);
            totalDistance = totalDistance+(Integer)map.get("distance");
            totalDuration = totalDuration+(Double)map.get("duration");
            lineMap.put((String)map.get("lineName"), (String)map.get("lineName"));
		}
		
		decision.setTotalDistance(totalDistance);
		decision.setTotalDuration(totalDuration);
		decision.setLineCount(new Integer(lineMap.keySet().size()));
		decision.setTotalWalkingTime(totalWalkingTime);
		
		String[] list = idList.split(",");
		for (int i = 0; i < list.length; i++) {
			decision.getRoutes().add(tempMap.get(new Integer(list[i])));
		}	
		
		long endDate = (new Date()).getTime();
		System.out.println("calculate route = "+(endDate-startDate));
		return decision;	
		
	}
	
}
