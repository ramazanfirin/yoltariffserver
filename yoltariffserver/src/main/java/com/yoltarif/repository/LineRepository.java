package com.yoltarif.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.yoltarif.model.Line;
import com.yoltarif.model.Station;

public interface LineRepository extends GraphRepository<Line>{

	@Query("START n = node(*) DELETE n")
    public void deleteAllNodes();
	
	@Query("START n = rel(*)  DELETE n")
    public void deleteAllRelations();
	
	@Query("match (l:Line)-[r:contains]->(s:Station)-[x:next]-(k:Station) where ID(l)={0} return ID(x),s.wkt,k.wkt,x.travelType")
    public void deleteAllRelations(Long lined);

	@Query("match (l:Line) where l.name={0} return l limit 1")
	public Line getLineByName(String name);
	
	@Query("match (l:Line) return l")
	public List<Line> getAllLines();
	
	@Query("match (s:Station)<-[r:contains]-(l:Line) where ID(l)={0} return s order by s.stationIndex")
	public List<Station> getStationList(Long id);
}
