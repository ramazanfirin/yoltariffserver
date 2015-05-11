package com.yoltarif.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;

import com.yoltarif.model.Line;
import com.yoltarif.model.Station;
import com.yoltarif.model.dto.DistanceCalculateDTO;
import com.yoltarif.repository.LineRepository;

@Service
public class LineService {
	
	@Autowired
    private Neo4jTemplate template;
	
	@Autowired(required=true)
	public LineRepository lineRepository;
	
	public Object save(Line object){
		return lineRepository.save(object);
	}

	public void clearDatabase(){
		lineRepository.deleteAllNodes();
		lineRepository.deleteAllRelations();
	}

	public Result<Line> findAll(){
		return lineRepository.findAll();
	}
	
	public List<Line> getAllLines(){
		return lineRepository.getAllLines();
	}
	
	public List<Station> getStationList(Long id){
		return lineRepository.getStationList(id);
	}
	
	public Line getLineByName(String name){
		return lineRepository.getLineByName(name);
	}

	public List<DistanceCalculateDTO> findEmptyDuration(){
	    List<DistanceCalculateDTO> resultList = new ArrayList<DistanceCalculateDTO>();
		Result<Map<String,Object>> result =  template.query("match ()-[r:next]-() where r.duration is null return ID(r) as id,startNode(r).wkt as startWkt,endNode(r).wkt as endWkt,r.travelType as type", null);
		//Map<String,Object> map=result.single();
		for (Iterator iterator = result.iterator(); iterator.hasNext();) {
			Map<String,Object> map= (Map<String,Object>) iterator.next();
			DistanceCalculateDTO calculateDTO = new DistanceCalculateDTO();
			calculateDTO.setRelId((Integer)map.get("id")); 
			calculateDTO.setStartWkt((String)map.get("startWkt")); 
			calculateDTO.setEndWkt((String)map.get("endWkt")); 
			calculateDTO.setType((String)map.get("type"));
			resultList.add(calculateDTO);
			
		}
		return resultList;
	}


}
