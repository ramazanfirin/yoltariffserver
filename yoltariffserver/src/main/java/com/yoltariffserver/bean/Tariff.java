package com.yoltariffserver.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yoltarif.model.Line;
import com.yoltarif.model.StationDTO;
import com.yoltarif.model.TempLine;
import com.yoltarif.model.TempStation;
import com.yoltarif.model.dto.Decision;
import com.yoltarif.model.dto.DisjktraDTO;
import com.yoltarif.model.dto.KeyValueDTO;
import com.yoltarif.model.dto.NearStationDTO;
import com.yoltarif.model.dto.RouteDTO;
import com.yoltarif.model.visual.VisualLine;
import com.yoltarif.service.CalculationService;
import com.yoltarif.service.LineService;
import com.yoltarif.service.StationService;
import com.yoltarif.util.DistanceUtil;
import com.yoltarif.util.FileUtil;
import com.yoltarif.util.Neo4jUtil;
import com.yoltarif.util.Util;

@ManagedBean
@SessionScoped
public class Tariff {

	MapModel emptyModel = new DefaultMapModel();
	
	List<Line> lineList  =new ArrayList<Line>() ;
	
	List<Line> selectedLines;
	
	List<VisualLine> visualLines = new ArrayList<VisualLine>();
	
	VisualLine selectedVisualLine = new VisualLine("");
	
	
	String startLat;
	String startLng;
	String endLat;
	String endLng;
	
	Marker startMarker;
	Marker endMarker;
	
	List<KeyValueDTO> keyValueDTOList = new ArrayList<KeyValueDTO>();
	
	@Autowired
	StationService stationService;
	
	@Autowired
	CalculationService calculationService;

	Decision decision;
	String selectedDemo="";
	
	public String showSelectedDemo() throws Exception{
		String tempStartLat="";
		String tempStartLng="";
		String tempEndLat="";
		String temoEndLng="";
		
		if(selectedDemo.equals("1")){
			tempStartLat = "38.71076055535748";
			tempStartLng ="35.45649714794922";
			tempEndLat = "38.72254685552562";
			temoEndLng = "35.48722407934565";
		}
		
		if(selectedDemo.equals("2")){
			tempStartLat = "38.735001321249165";
			tempStartLng ="35.522586778076175";
			tempEndLat = "38.675254807068065";
			temoEndLng = "35.49872539160151";
		}
		
		if(selectedDemo.equals("3")){
			tempStartLat = "38.740625074839734";
			tempStartLng ="35.43400950756836";
			tempEndLat = "38.68356332300966";
			temoEndLng = "35.453406788085886";
		}
		
		
		removeLines();
		showTemplateMarkers();
		startMarker.setLatlng(new LatLng(new Double(tempStartLat), new Double(tempStartLng)));
		endMarker.setLatlng(new LatLng(new Double(tempEndLat), new Double(temoEndLng)));
		
		startLat = tempStartLat;
		startLng=  tempStartLng;
		endLat = tempEndLat;
		endLng = temoEndLng;
		RequestContext.getCurrentInstance().execute("updateCoordinateForm("+tempStartLat+","+tempStartLng+","+tempEndLat+","+temoEndLng+")");
		//calculate();
		return "";
		
	}
	
	public void showMarkesForRoute(Decision decision){
		long startDate = (new Date()).getTime();
		List<Polyline> polyLineList = new ArrayList<Polyline>();
		Map<String,String> colorMap = new HashMap<String,String>();
		String color =Util.getRandomColor();
		
		for (Iterator iterator = decision.getRoutes().iterator(); iterator.hasNext();) {
			RouteDTO routeDTO = (RouteDTO)
					iterator.next();
			
			if(!routeDTO.getStartNodeLineName().equals("TempLine")){
			color =colorMap.get(routeDTO.getStartNodeLineName());
			if(color==null){
				color  =Util.getRandomColor();
				colorMap.put(routeDTO.getStartNodeLineName(),color);
			}
			}
			double latStart = Double.parseDouble(Util.getLat(routeDTO.getStartNodeWtk()));
			double lngStart = Double.parseDouble(Util.getLng(routeDTO.getStartNodeWtk()));
			
			Marker markerStart = new Marker(new LatLng(latStart, lngStart), routeDTO.getStartNodeLineName()+"-"+routeDTO.getStartNodeName(),"","http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|"+color);
			emptyModel.addOverlay(markerStart);	
			
			double latEnd = Double.parseDouble(Util.getLat(routeDTO.getEndNodeWkt()));
			double lngEnd = Double.parseDouble(Util.getLng(routeDTO.getEndNodeWkt()));
		
			Marker markerEnd = new Marker(new LatLng(latEnd, lngEnd), routeDTO.getEndNodeLineName()+"-"+routeDTO.getEndNodeName(),"","http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|"+color);
			emptyModel.addOverlay(markerEnd);	
			
			
			
			List<KeyValueDTO> data  = new ArrayList<KeyValueDTO>();
			//data.add(e)
			Polyline polyline = new Polyline();
			polyline.setStrokeWeight(5);
	        polyline.setStrokeColor("#"+color);
	        if(routeDTO.getTravelType().equals("WALKING"))
	        	polyline.setStrokeColor("#3170ce");
	        polyline.setStrokeOpacity(0.7);
			
			polyline.getPaths().add(new LatLng(latStart, lngStart));
			polyline.getPaths().add(new LatLng(latEnd, lngEnd));
			polyline.setData(routeDTO.getDistance()+" "+routeDTO.getDuration()+" "+routeDTO.getTravelType());
			polyLineList.add(polyline);
		}
		
		for (Iterator iterator = polyLineList.iterator(); iterator.hasNext();) {
			Polyline polyline2 = (Polyline) iterator.next();
			emptyModel.addOverlay(polyline2);
		}
		
		
		long endDate = (new Date()).getTime();
		System.out.println("show route = "+(endDate-startDate));
		
	}
	
	 public void onMarkerSelect(OverlaySelectEvent event) {
	    if(event.getOverlay() instanceof Polyline){
	        Polyline polyline = (Polyline)event.getOverlay();
	       LatLng start = polyline.getPaths().get(0);
	       LatLng end = polyline.getPaths().get(1);
	       double infoWindowLat = (start.getLat()+end.getLat())/2; 
	       double infoWindowLng = (start.getLng()+end.getLng())/2;
	       
	       String data = (String)polyline.getData();
	       String[] datas = data.split(" ");
	       String message = "Uzaklik="+datas[0]+" m <br> Sure="+datas[1]+" dk <br> Tip="+datas[2];
	       
	       RequestContext.getCurrentInstance().execute("openInfoWindow(\""+message+"\","+infoWindowLat+","+infoWindowLng+")");
	   }
	   }
	
	public Tariff() {
		super();
		startMarker = new Marker(new LatLng(38.720940, 35.482933), "Baslangic Noktasi","","http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|ABCDEF");
		startMarker.setZindex(100);
		endMarker = new Marker(new LatLng(38.722279, 35.489799), "Bitis Noktasi","","http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|ABCDEF");
		endMarker.setZindex(100);
		startMarker.setDraggable(true);
		endMarker.setDraggable(true);
		try {
			getLines() ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String showTemplateMarkers(){
		emptyModel.addOverlay(startMarker);
		emptyModel.addOverlay(endMarker);
		return "";
	}
	
	public String removeTemplateMarkers(){
		emptyModel.getMarkers().remove(startMarker);
		emptyModel.getMarkers().remove(endMarker);
		return "";
	}
	
	public void onMarkerDrag(MarkerDragEvent event) {
       Marker marker = event.getMarker();
       if(marker.getTitle().contains("Baslangic")){
    	   startLat = String.valueOf(marker.getLatlng().getLat());
    	   startLng = String.valueOf(marker.getLatlng().getLng());
       }else{
    	   endLat = String.valueOf(marker.getLatlng().getLat());
    	   endLng = String.valueOf(marker.getLatlng().getLng());
       }
    }
	
	public LineService getLineService() throws Exception{
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		BeanFactory context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		LineService serviceProvider= (LineService)context.getBean("lineService");
		return serviceProvider;
	}
	
	public StationService getStationService() throws Exception{
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		BeanFactory context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		StationService serviceProvider= (StationService)context.getBean("stationService");
		return serviceProvider;
	}
	
	public CalculationService getCalculationService() throws Exception{
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		BeanFactory context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		CalculationService serviceProvider= (CalculationService)context.getBean("calculationService");
		return serviceProvider;
	}
	
	public String calculate() throws Exception{
		long startDate = (new Date()).getTime();
		TempStation startStation = new TempStation();
		TempStation endStation = new TempStation();
		TempLine tempLine = new TempLine();
		
		try {
			List<NearStationDTO> startStationList= getStationService().findNearestStations(startLat, startLng, "0.9");
			if(startStationList.size()==0){
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage( FacesMessage.SEVERITY_ERROR,"baslangic a 1km mesafede durak bulunamadi","baslangic bulunamadi"));
				return "";
			}
			List<NearStationDTO> endStationList = getStationService().findNearestStations(endLat, endLng, "0.9");
			if(endStationList.size()==0){
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage( FacesMessage.SEVERITY_ERROR,"hedefe 1km mesafede durak bulunamadi","baslangic bulunamadi"));
				return "";
			}
			
			long endDate2 = (new Date()).getTime();
			System.out.println("first queyries = "+(endDate2-startDate)+ " "+ startStationList.size()+ " "+ endStationList.size());
			
			tempLine.setName("TempLine");
			tempLine.setSortedStationList(null);
			tempLine =(TempLine) getLineService().save(tempLine);
			
			startStation.setName("Baslangic");
			startStation.setWkt(Float.parseFloat(startLng), Float.parseFloat(startLat));
			startStation.getLineList().add(tempLine);
			startStation = (TempStation)getStationService().save(startStation);
			
			for (Iterator iterator = startStationList.iterator(); iterator.hasNext();) {
				NearStationDTO nearStationDTO = (NearStationDTO) iterator.next();
				int distance = DistanceUtil.distance(startStation.getLat(), startStation.getLng(), String.valueOf(nearStationDTO.getLat()), String.valueOf(nearStationDTO.getLng()), "K");
				Double duration = DistanceUtil.calculateWalkingTime(distance);
				getStationService().createWalkingRelationshipforStartNode(startStation.getId(), new Long(nearStationDTO.getId()), duration, new Long(distance));
			}
			
			long endDate3 = (new Date()).getTime();
			System.out.println("start temp stations = "+(endDate3-startDate));

			endStation.setName("Bitis");
			endStation.setWkt(Float.parseFloat(endLng), Float.parseFloat(endLat));
			endStation.getLineList().add(tempLine);
			endStation = (TempStation)getStationService().save(endStation);
			
			for (Iterator iterator = endStationList.iterator(); iterator.hasNext();) {
				NearStationDTO nearStationDTO = (NearStationDTO) iterator.next();
				int distance = DistanceUtil.distance(endStation.getLat(), endStation.getLng(), String.valueOf(nearStationDTO.getLat()), String.valueOf(nearStationDTO.getLng()), "K");
				Double duration = DistanceUtil.calculateWalkingTime(distance);
				getStationService().createWalkingRelationshipforEndNode(endStation.getId(), new Long(nearStationDTO.getId()), duration, new Long(distance));
			}	
			
			long endDate4 = (new Date()).getTime();
			System.out.println("finish temp stations = "+(endDate4-startDate));
						
			DisjktraDTO disjktraDTO= Neo4jUtil.calculateDisjktra(startStation.getId().toString(), endStation.getId().toString());
			decision=getCalculationService().calculateRoute(disjktraDTO.getRelationshipAsString());
			showMarkesForRoute(decision);

			String message = "Toplam Mesafe="+decision.getTotalDistance()+
							"m <br>Toplam Süre="+ 	decision.getTotalDuration()+
							" dk <br>Toplam Hat Sayisi="+decision.getLineCount()+
							"<br>Toplam yürüme mesafesi="+decision.getTotalWalkingTime()+" dk"+
							"<br>Baslangic noktasina yakin durak sayisi="+startStationList.size()+
							"<br>Bitis noktasina yakin durak sayisi="+endStationList.size();
		       
		    RequestContext.getCurrentInstance().execute("openInfoWindow(\""+message+"\","+startLat+","+startLng+")");
			
			long endDate = (new Date()).getTime();
			System.out.println("genel tamamlama = "+(endDate-startDate));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage( FacesMessage.SEVERITY_ERROR,"Hata Olustu"+ e.getMessage(),"baslangic bulunamadi"));
			e.printStackTrace();
		}finally{
			//delete nodes
			if(startStation!=null && startStation.getId()!=null)
				getStationService().deleteNodeAndRelationships(startStation.getId());
			
			if(endStation!=null && endStation.getId()!=null)
				getStationService().deleteNodeAndRelationships(endStation.getId());
			if(tempLine!=null && tempLine.getId()!=null)
				getStationService().deleteNodeAndRelationships(tempLine.getId());
			
		}
		
		
		return "";
	}
	
	
	public String removeLines(){
		emptyModel.getMarkers().clear();
	emptyModel.getPolylines().clear();
	return "";
	}
	
	public String drawLine() {
		drawVisualLine(selectedVisualLine);
		return "";
	}
	
	public void drawVisualLine(VisualLine line){
		for (Iterator iterator = line.getMarkers().iterator(); iterator.hasNext();) {
			Marker marker = (Marker)iterator.next();
			emptyModel.addOverlay(marker);
		}
		emptyModel.addOverlay(line.getPolyline());
	}
	
	
	public String getLines() throws Exception{
		ServletContext servletContext = (ServletContext) FacesContext
			    .getCurrentInstance().getExternalContext().getContext();
		setLineList(FileUtil.getLines(servletContext).getLineList());
		visualLines.clear();
		List<Line> lines = FileUtil.getLines(servletContext).getLineList();
		for (Iterator iterator = lines.iterator(); iterator.hasNext();) {
			Line line = (Line) iterator.next();
			VisualLine visualLine = new VisualLine(line.getName());
			System.out.println("bitti");
			for (Iterator iterator2 = line.sortedStationList.iterator(); iterator2.hasNext();) {
				StationDTO station = (StationDTO) iterator2.next();
				visualLine.addMarker(station.getLat(),station.getLng(),line.getName()+"-"+ station.getName());
			}
			visualLines.add(visualLine);
		}
		
		
		return "";
	}
	
	public void drawLines(){
		
		emptyModel.getMarkers().clear();
		emptyModel.getPolylines().clear();
		for (Iterator iterator = selectedLines.iterator(); iterator.hasNext();) {
			Line type = (Line) iterator.next();
			VisualLine visualLine = new VisualLine(type.getName());
			System.out.println("bitti");
			for (Iterator iterator2 = type.sortedStationList.iterator(); iterator2.hasNext();) {
				StationDTO station = (StationDTO) iterator2.next();
				visualLine.addMarker(station.getLat(),station.getLng(),type.getName()+"-"+ station.getName());
			}
			visualLines.add(visualLine);
		}
		
		drawOnMap();
	}
	
	public void drawOnMap(){
		for (Iterator iterator = visualLines.iterator(); iterator.hasNext();) {
			VisualLine visualLine = (VisualLine) iterator.next();
			for (Iterator iterator2 = visualLine.getMarkers().iterator(); iterator2	.hasNext();) {
				Marker marker = (Marker) iterator2.next();
				emptyModel.addOverlay(marker);
			}
			emptyModel.addOverlay(visualLine.getPolyline());
						
		}
	}

	
	
	
	public MapModel getEmptyModel() {
		return emptyModel;
	}

	public void setEmptyModel(MapModel emptyModel) {
		this.emptyModel = emptyModel;
	}

	public List<Line> getLineList() {
		return lineList;
	}

	public void setLineList(List<Line> lineList) {
		this.lineList = lineList;
	}


	public List<Line> getSelectedLines() {
		return selectedLines;
	}


	public void setSelectedLines(List<Line> selectedLines) {
		this.selectedLines = selectedLines;
	}

	public List<VisualLine> getVisualLines() {
		return visualLines;
	}

	public void setVisualLines(List<VisualLine> visualLines) {
		this.visualLines = visualLines;
	}

	public VisualLine getSelectedVisualLine() {
		return selectedVisualLine;
	}

	public void setSelectedVisualLine(VisualLine selectedVisualLine) {
		this.selectedVisualLine = selectedVisualLine;
	}

	public String getStartLat() {
		return startLat;
	}

	public void setStartLat(String startLat) {
		this.startLat = startLat;
	}

	public String getStartLng() {
		return startLng;
	}

	public void setStartLng(String startLng) {
		this.startLng = startLng;
	}

	public String getEndLat() {
		return endLat;
	}

	public void setEndLat(String endLat) {
		this.endLat = endLat;
	}

	public String getEndLng() {
		return endLng;
	}

	public void setEndLng(String endLng) {
		this.endLng = endLng;
	}

	public List<KeyValueDTO> getKeyValueDTOList() {
		return keyValueDTOList;
	}

	public void setKeyValueDTOList(List<KeyValueDTO> keyValueDTOList) {
		this.keyValueDTOList = keyValueDTOList;
	}

	public Decision getDecision() {
		return decision;
	}

	public void setDecision(Decision decision) {
		this.decision = decision;
	}

	public String getSelectedDemo() {
		return selectedDemo;
	}

	public void setSelectedDemo(String selectedDemo) {
		this.selectedDemo = selectedDemo;
	}
	
}
