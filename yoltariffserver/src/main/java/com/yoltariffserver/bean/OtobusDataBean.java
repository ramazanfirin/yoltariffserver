package com.yoltariffserver.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.yoltariffserver.rest.OtobusData;
@ManagedBean
@SessionScoped
public class OtobusDataBean {

	
	List<OtobusData> dataList = new ArrayList<OtobusData>();

	String lat;
	String lng;

	String refLat;
	String refLng;
	
	public List<OtobusData> getDataList() {
		
		return dataList;
	}

	public void setDataList(List<OtobusData> dataList) {
		this.dataList = dataList;
	}
	
	public String showData(){
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		if(servletContext.getAttribute("otobusDataList")!=null)
			dataList  =(List<OtobusData>)servletContext.getAttribute("otobusDataList");
		
		return "";
	}
	
	public String changeDurakCoordinates(){
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		servletContext.setAttribute("lat",lat);
		servletContext.setAttribute("lng",lng);
		
		servletContext.setAttribute("refLat",refLat);
		servletContext.setAttribute("refLng",refLng);
		
		return "";
	}
	
	public String showDurakCoordinates(){
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		if(servletContext.getAttribute("lat")!=null)
		  lat = (String)servletContext.getAttribute("lat");
		
		if(servletContext.getAttribute("lng")!=null)
			  lng = (String)servletContext.getAttribute("lng");
		
		if(servletContext.getAttribute("refLat")!=null)
			  refLat = (String)servletContext.getAttribute("refLat");
			
			if(servletContext.getAttribute("refLat")!=null)
				  refLng = (String)servletContext.getAttribute("refLng");
		return "";
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getRefLat() {
		return refLat;
	}

	public void setRefLat(String refLat) {
		this.refLat = refLat;
	}

	public String getRefLng() {
		return refLng;
	}

	public void setRefLng(String refLng) {
		this.refLng = refLng;
	}
}
