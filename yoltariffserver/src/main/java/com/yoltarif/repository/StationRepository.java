package com.yoltarif.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.SpatialRepository;

import com.yoltarif.model.NextStation;
import com.yoltarif.model.Station;



public interface StationRepository extends GraphRepository<Station>,SpatialRepository<Station>{


	
	@Query("start n=node(*) where n.name={0} return n")
    public List<Station> searchByName(String serviceName);
	
	
	@Query("START n=node:location({0}), h=node(*) match n<-[:contains]-h ;return h.name")
    public List<String> findNearestNode(String params);	

	@Query("match (n)-[rels:next]->() where ID(rels) in [{0}] return rels")
    public List<NextStation> findRelationsByIdList(String idList);	
	
	@Query("START n=node({0}) optional MATCH n-[r]-() DELETE r, n")
	public void deleteNodeAndRelationships(Long nodeId);
	
	@Query("START n=node(*) optional MATCH n-[r]-() DELETE r, n")
	public void deleteDatabase();

	@Query("start startNode=node({0}),endNode=node({1})  CREATE (startNode)-[:next {duration: {2},distance:{3},travelType:\"WALKING\",__type__:\"NextStation\"}]->(endNode)") 
	public void createWalkingRelationshipforStartNode(Long startId,Long endId,Double duration,Long distance) throws Exception;
	
	@Query("start startNode=node({0}),endNode=node({1})  CREATE (startNode)<-[:next {duration: {2},distance:{3},travelType:\"WALKING\",__type__:\"NextStation\"}]-(endNode)") 
	public void createWalkingRelationshipforEndNode(Long startId,Long endId,Double duration,Long distance);
	
	@Query("match ()-[r:next]-() where ID(r) = {0} SET r.duration={1} ,r.distance={2}")
	public void setRelationProperties(Integer id,Double duration,Double distance);
	
	@Query("match (n:Station) where n.processed=false return n")
	public List<Station> getUnprocessedStations();
	
	@Query("match ()-[r:next{travelType:\"WALKING\"}]-() return r")
	public List<NextStation> findWalkingRelationship();
	
	@Query("match (n:Station) where ")
	public List<NextStation> setProcessedFalse();
}



