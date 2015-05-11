package com.yoltarif.model.visual;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

import com.yoltarif.util.Util;

public class VisualLine {

	String name;
	List<Marker> markers = new ArrayList<Marker>();
	Polyline polyline = new Polyline();
	String color;
	String iconUrl="http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|";
	Boolean show;
	
	
	public VisualLine(String _name) {
		super();
		name=_name;
		color = Util.getRandomColor();
		polyline.setStrokeColor("#"+color);
		iconUrl = iconUrl+color;
		//System.out.println(iconUrl);
	}

	public void addMarker(String lat,String lng,String title){
		Marker marker = new Marker(new LatLng(Double.parseDouble(lat),Double.parseDouble(lng)), title,"",iconUrl);
		markers.add(marker);
		polyline.getPaths().add(marker.getLatlng());
	}
	
	public List<Marker> getMarkers() {
		return markers;
	}
	public void setMarkers(List<Marker> markers) {
		this.markers = markers;
	}
	public Polyline getPolyline() {
		return polyline;
	}
	public void setPolyline(Polyline polyline) {
		this.polyline = polyline;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}
}
