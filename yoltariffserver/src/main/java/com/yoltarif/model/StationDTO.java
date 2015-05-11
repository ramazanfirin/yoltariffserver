package com.yoltarif.model;


import org.neo4j.graphdb.Direction;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
@TypeAlias(value="StationDTO")
public class StationDTO extends AbstractEntity{

	String name;
	
	 
	@Indexed(indexType = IndexType.POINT, indexName = "location") 
	String wkt;
	
	
	String orijinalwkt;


	@RelatedTo(type="contains",direction = Direction.INCOMING)
	Line line;
	
	@RelatedTo(type="next",direction = Direction.OUTGOING)
	StationDTO nextStation;
	
	@RelatedTo(type="next",direction = Direction.INCOMING)
	StationDTO previousStation;
	
	public StationDTO getNextStation() {
		return nextStation;
	}

	public void setNextStation(StationDTO nextStation) {
		this.nextStation = nextStation;
	}

	
	public StationDTO getPreviousStation() {
		return previousStation;
	}

	public void setPreviousStation(StationDTO previousStation) {
		this.previousStation = previousStation;
	}

	
	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public void setWkt(String wkt) {
		this.wkt = wkt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWkt() {
		return wkt;
	}

	public void setWkt(float lon, float lat) {
		this.wkt =String.format("POINT( %f %f )",lon,lat);
	}

	public String getOrijinalwkt() {
		return orijinalwkt;
	}

	public void setOrijinalwkt(float lon, float lat) {
		this.orijinalwkt = String.format("POINT( %.2f %.2f )",lon,lat);
	}

	public String getLat(){
		if(wkt==null || wkt.equals(""))
			return 	"";
		
		String temp = wkt.replace("POINT( ", "");
		temp = temp.replace(" )", "");		
		String[] result = temp.split(" ");
		return result[1];
	}
	
	public String getLng(){
		if(wkt==null || wkt.equals(""))
			return 	"";
		
		String temp = wkt.replace("POINT( ", "");
		temp = temp.replace(" )", "");		
		String[] result = temp.split(" ");
		return result[0];
	}
}
