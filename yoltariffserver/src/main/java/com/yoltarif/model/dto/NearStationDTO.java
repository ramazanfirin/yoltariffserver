package com.yoltarif.model.dto;

public class NearStationDTO {
Integer id ;
//String wkt;
Double lat;
Double lng;




public NearStationDTO(Integer _id,String _wkt) {
	super();
	String temp = _wkt.replace("POINT( ", "");
	temp = temp.replace(" )", "");		
	String[] result = temp.split(" ");
	lat=Double.parseDouble(result[1]);
	lng=Double.parseDouble(result[0]);
	
	id = _id;
}



public Integer getId() {
	return id;
}

public void setId(Integer id) {
	this.id = id;
}

public Double getLat() {
	return lat;
}

public void setLat(Double lat) {
	this.lat = lat;
}

public Double getLng() {
	return lng;
}

public void setLng(Double lng) {
	this.lng = lng;
}


}
