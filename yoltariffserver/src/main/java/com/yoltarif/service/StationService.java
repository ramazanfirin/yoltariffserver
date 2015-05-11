package com.yoltarif.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.rest.graphdb.entity.RestNode;
import org.neo4j.rest.graphdb.entity.RestRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Shape;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;

import com.yoltarif.model.NextStation;
import com.yoltarif.model.Station;
import com.yoltarif.model.dto.NearStationDTO;
import com.yoltarif.repository.StationRepository;

@Service
public class StationService {
	
	@Autowired(required=true)
	public StationRepository stationRepository;
	
	@Autowired
    private Neo4jTemplate template;
	
	//final String GET_NEAREST_NODES_QUERY = ""
	
	
	
	
//	public List<String> findNearestNode(String lat,String lng,String distance){
//		String query = "withinDistance:["+lat+","+ lng+","+ distance+"]";
//		return stationRepository.findNearestNode(query);
//	}
	
	public void setRelationProperties(Integer id,Double duration,Double distance){
		stationRepository.setRelationProperties(id, duration, distance);
	}
	
	public void dijkstraTest(){
		
		
//		PathFinder pathFinder = GraphAlgoFactory.dijkstra(new MeExpander(), new MyCostCalc());
//	    WeightedPath path = pathFinder.findSinglePath(template.getNode(0),template.getNode(9));
//	    if (path != null) {
//	        for (Node n : path.nodes()) {
//	              System.out.print(" " + n.getProperty("name"));
//	        }
//	    }
//	    System.out.println("");
	 }
		
	public void dijkstra(Long start){
		String sql  ="START p=node(11368), s=node(11365)"+
					"MATCH path =shortestPath(p-[rels:next*]->(s))"+
					"return extract(x IN nodes(path) | ID(x)) as nodes";
	}
	
	public void deleteNodeAndRelationships(Long nodeId){
		stationRepository.deleteNodeAndRelationships(nodeId);
		
	}
	
	public Station findOne(Long id){
		return stationRepository.findOne(id);
	}
	
	public void createWalkingRelationshipforStartNode(Long startId,Long endId,Double duration,Long distance) throws Exception{
		
		stationRepository.createWalkingRelationshipforStartNode(startId, endId, duration, distance);
	}
	
	public List<NextStation> findWalkingRelationship(){
		return stationRepository.findWalkingRelationship();
	}
	
public void createWalkingRelationshipforEndNode(Long startId,Long endId,Double duration,Long distance){
		
		stationRepository.createWalkingRelationshipforEndNode(startId, endId, duration, distance);
	}
	
public List<Station> getUnprocessedStations(){
	return stationRepository.getUnprocessedStations();
}

	public List<NearStationDTO> findNearestStations(String lat,String lng,String distance){
		List<NearStationDTO> resultList = new ArrayList<NearStationDTO>();
		String query = "START n=node:location ('withinDistance:["+lat+","+ lng+","+ distance+"]') return ID(n) as id,n.wkt as wkt";
		Result<Map<String,Object>> result =  template.query(query, null);
		//Map<String,Object> map=result.single();
		for (Iterator iterator = result.iterator(); iterator.hasNext();) {
			Map<String,Object> map= (Map<String,Object>) iterator.next();
			Integer id = null;
			if(map.get("id") instanceof Long){
				Long idLong = (Long)map.get("id");
				id = new Integer(idLong.intValue());
			}else{
			 id = (Integer)map.get("id");
			}String wkt=(String)map.get("wkt");
			NearStationDTO dto = new NearStationDTO(id, wkt);
			resultList.add(dto);
		}
		return resultList;
	}
	
	public List<NextStation> findRelationsByIdList(String idList){
		//return stationRepository.findRelationsByIdList(idList);
		
		List<NextStation> resultList = new ArrayList<NextStation>();
		String query  ="match (n)-[rels:next]->() where ID(rels) in ["+idList+"] return rels";
		Result<Map<String,Object>> result =  template.query(query, null);
		//Map<String,Object> map=result.single();
		for (Iterator iterator = result.iterator(); iterator.hasNext();) {
			Map<String,Object> map= (Map<String,Object>) iterator.next();map.get("rels");
			RestRelationship temp=(RestRelationship)map.get("rels");
			NextStation nextStation=template.convert(temp, NextStation.class);
			resultList.add(nextStation);
		}
		return resultList;
		
	}
	
	
	public List<String> findNearestLine(String lat,String lng,String distance){
		List<String> resultList = new ArrayList<String>();
		Result<Map<String,Object>> result =  template.query("START n=node:location ('withinDistance:["+lat+","+ lng+","+ distance+"]'),h=node(*)match n<-[:contains]-h return h.name", null);
		//Map<String,Object> map=result.single();
		for (Iterator iterator = result.iterator(); iterator.hasNext();) {
			Map<String,Object> map= (Map<String,Object>) iterator.next();
			String temp=(String)map.get("h.name");
			resultList.add(temp);
		}
		return resultList;
	}
	
	public Station findNearestStation(String lat,String lng,String distance){
		
		Result<Map<String,Object>> result =  template.query("START n=node:location ('withinDistance:["+lat+","+ lng+","+ distance+"]') return n limit 1", null);
		//Map<String,Object> map=result.single();
		for (Iterator iterator = result.iterator(); iterator.hasNext();) {
			Map<String,Object> map= (Map<String,Object>) iterator.next();
			if(map.get("n") !=null){
				RestNode node=(RestNode)map.get("n");node.getId();
				//template.createEntityFrom(node.,Station.class,null);
				
				Station temp=template.convert(node, Station.class);
				return temp;
			}
		}
		return null;
	}
	
	
	
public Long findNearestStationId(String lat,String lng,String distance){
		
		Result<Map<String,Object>> result =  template.query("START n=node:location ('withinDistance:["+lat+","+ lng+","+ distance+"]') return ID(n) as id limit 1", null);
		//Map<String,Object> map=result.single();
		for (Iterator iterator = result.iterator(); iterator.hasNext();) {
			Map<String,Object> map= (Map<String,Object>) iterator.next();
			if(map.get("id") !=null){
				Long node=(Long)map.get("id");
				//template.createEntityFrom(node.,Station.class,null);
				
				
				return node;
			}
		}
		return null;
	}

public void deleteDatabase(){
	stationRepository.deleteDatabase();
}

public void deleteStation(Station station){
	stationRepository.delete(station);;
}
	
public List<Station> findNearestStationList(String lat,String lng,String distance,Long lineId){
	    List<Station> resultList = new ArrayList<Station>();
		Result<Map<String,Object>> result =  template.query("START n=node:location ('withinDistance:["+lat+","+ lng+","+ distance+"]')  match n-[]-(l:Line) where ID(l)<>"+lineId+" return n", null);
		//Map<String,Object> map=result.single();
		for (Iterator iterator = result.iterator(); iterator.hasNext();) {
			Map<String,Object> map= (Map<String,Object>) iterator.next();
			if(map.get("n") !=null){
				RestNode node=(RestNode)map.get("n");node.getId(); 
				Station temp=template.convert(node, Station.class);
				resultList.add(temp);
			}
		}
		return resultList;
	}

public List<Long> findNearestStationList(String lat,String lng,String distance,String lineIdList){
    List<Long> resultList = new ArrayList<Long>();
	Result<Map<String,Object>> result =  template.query("START n=node:location ('withinDistance:["+lat+","+ lng+","+ distance+"]')  match n-[]-(l:Line) where not (ID(l) in["+lineIdList +"]) return ID(n) as id", null);
	//Map<String,Object> map=result.single();
	for (Iterator iterator = result.iterator(); iterator.hasNext();) {
		Map<String,Object> map= (Map<String,Object>) iterator.next();
		if(map.get("id") !=null){
			Long id  =(Long) map.get("id");
			resultList.add(id);
		}
	}
	return resultList;
}

public List<String> findNearestStationListReturnCoordinate(String lat,String lng,String distance,String lineIdList){
    List<String> resultList = new ArrayList<String>();
	Result<Map<String,Object>> result =  template.query("START n=node:location ('withinDistance:["+lat+","+ lng+","+ distance+"]')  match n-[]-(l:Line) where not (ID(l) in["+lineIdList +"]) return ID(n) as id,n.wkt as wkt", null);
	//Map<String,Object> map=result.single();
	for (Iterator iterator = result.iterator(); iterator.hasNext();) {
		Map<String,Object> map= (Map<String,Object>) iterator.next();
		
			Long id  =(Long) map.get("id");
			String wkt = (String ) map.get("wkt");
			resultList.add(id+","+wkt);
	}
	return resultList;
}
	
	public Station save(Station object){
		return stationRepository.save(object);
	}
	
	public void findWithinBoundingBox(String indexName,Box box){
		stationRepository.findWithinBoundingBox(indexName, box);
	}

	public Result<Station> findWithinDistance(String indexName,double lat,double lng,double distanceKM){
		return stationRepository.findWithinDistance(indexName, lat, lng, distanceKM);
	}
	
	public void findWithinShape(String indexName,Shape shape){
		stationRepository.findWithinShape(indexName, shape);
	}

	public void findWithinWellKnownText(String indexName,String wellKnownText){
		stationRepository.findWithinWellKnownText(indexName, wellKnownText);
	}
	
	public Result<Station> findAllStations(){
		Result<Station> stations= stationRepository.findAll();
		return stations;
	}
	
	public List<Station> getAllStations(){
		List<Station> list = new ArrayList<Station>();
		Result<Map<String,Object>> result =  template.query("MATCH (n:Station)<-[:contains]-(l:Line)  RETURN n ", null);
		//Map<String,Object> map=result.single();
		for (Iterator iterator = result.iterator(); iterator.hasNext();) {
			Map<String,Object> map= (Map<String,Object>) iterator.next();
			RestNode node=(RestNode)map.get("n");
			//template.createEntityFrom(node.,Station.class,null);
			System.out.println(node.getProperty("name", "no_Data"));
			Station temp=template.convert(node, Station.class);
			//list.add(null;
		}
		return list;
	}
}

