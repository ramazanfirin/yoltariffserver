package com.yoltarif.model.dto;

import com.yoltarif.util.Util;

public class DistanceCalculateDTO {
Integer relId;
double startLat;
double startLng;
double destLat;
double destLng;

String type;

String startWkt;
String endWkt;

public Integer getRelId() {
	return relId;
}
public void setRelId(Integer relId) {
	this.relId = relId;
}

public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getStartWkt() {
	return startWkt;
}
public void setStartWkt(String startWkt) {
	this.startWkt = startWkt;
}
public String getEndWkt() {
	return endWkt;
}
public void setEndWkt(String endWkt) {
	this.endWkt = endWkt;
}

public double getStartLat() {
	return Double.parseDouble(Util.getLat(startWkt));
}
public void setStartLat(double startLat) {
	this.startLat = startLat;
}
public double getStartLng() {
	return Double.parseDouble(Util.getLng(startWkt));
}
public void setStartLng(double startLng) {
	this.startLng = startLng;
}
public double getDestLat() {
	return Double.parseDouble(Util.getLat(this.endWkt));
}
public void setDestLat(double destLat) {
	this.destLat = destLat;
}
public double getDestLng() {
	return Double.parseDouble(Util.getLng(this.endWkt));
}
public void setDestLng(double destLng) {
	this.destLng = destLng;
}
}
