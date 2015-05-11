package com.yoltariffserver.rest;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.thoughtworks.xstream.XStream;
import com.yoltarif.model.Line;
import com.yoltarif.model.LineWrapper;
import com.yoltarif.model.dto.DisjktraDTO;
import com.yoltarif.model.dto.KeyValueDTO;
import com.yoltarif.service.StationService;
import com.yoltarif.util.Neo4jUtil;
import com.yoltariffserver.util.CityCurfTest;
import com.yoltariffserver.util.GCMUtil;
import com.yoltariffserver.util.Util;



// Plain old Java Object it does not extend as class or implements 
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/hello")
public class RestServer {

	@Autowired
	StationService stationService;
	
	  @Context
	  private ServletContext context=null; 
	
	
  public RestServer() {
		super();
		System.out.println("rest olustu");
	}
  
//  @Context
//  private ServletContext context=null; 

// This method is called if TEXT_PLAIN is request
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String sayPlainTextHello() {
    return "Hello Jersey";
  }

  // This method is called if XML is request
  @GET
  @Produces(MediaType.TEXT_XML)
  public String sayXMLHello() {
    return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
  }

  // This method is called if HTML is request
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String sayHtmlHello() {
    return "<html> " + "<title>" + "Hello Jersey" + "</title>"
        + "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
  }

  
    @GET
	@Path("/GetLines")
    @Produces(MediaType.APPLICATION_JSON)
	public LineWrapper getLines() throws Exception{
    	//BufferedReader br = new BufferedReader(new FileReader("D:\\calismalar\\spatial2\\YolTarif\\hatBilgi2.xml"));
    	BufferedReader br= new BufferedReader( new InputStreamReader(new FileInputStream("D:\\calismalar\\spatial2\\YolTarif\\hatBilgi4.xml"), "UTF8"));
    	//BufferedReader br = new BufferedReader( new InputStreamReader(new FileInputStream("/mnt/ebs1/hatBilgi4.xml"), "UTF8"));
    	StringBuffer buff = new StringBuffer();
    	String line;
    	while((line = br.readLine()) != null){
    	   buff.append(line);
    	}
    	
    	
    	XStream xstream = new XStream();
    	
    	LineWrapper lineWrapper = (LineWrapper)xstream.fromXML(buff.toString());
    	
    	return lineWrapper;
    	 
	}
    
    @GET
	@Path("/GetLines/findClosestNode/{param1}/{param2}")
    @Produces(MediaType.APPLICATION_JSON)
	public List<String> getLines(@PathParam("param1") String lat,@PathParam("param2") String lng) throws Exception{
    	
    	ServletContext  servletContext =(ServletContext) context;
    	BeanFactory context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    	StationService stationService= (StationService)context.getBean("stationService");
    	
    	List<String> result= stationService.findNearestLine(lat, lng, "0.1");
    	 return result;
	}
    
    
    
    @GET
	@Path("/GetLines/{param}")
    @Produces(MediaType.APPLICATION_JSON)
	public LineWrapper getLines(@PathParam("param") String name) throws Exception{
    	//BufferedReader br = new BufferedReader(new FileReader("D:\\calismalar\\spatial2\\YolTarif\\hatBilgi2.xml"));
    	BufferedReader br = new BufferedReader( new InputStreamReader(new FileInputStream("D:\\calismalar\\spatial2\\YolTarif\\hatBilgi4.xml"), "UTF8"));
    	//BufferedReader br = new BufferedReader( new InputStreamReader(new FileInputStream("/mnt/ebs1/hatBilgi4.xml"), "UTF8"));
    	StringBuffer buff = new StringBuffer();
    	String line;
    	while((line = br.readLine()) != null){
    	   buff.append(line);
    	}
    	
    	LineWrapper resultLineWrapper = new LineWrapper();
    	
    	XStream xstream = new XStream();
    	
    	LineWrapper lineWrapper = (LineWrapper)xstream.fromXML(buff.toString());
    	for (Iterator iterator = lineWrapper.getLineList().iterator(); iterator.hasNext();) {
			Line type = (Line) iterator.next();
			if(type.getName().contains(name)){
				resultLineWrapper.getLineList().add(type);
				break;
			}
		}
    	
    	
    	
    	return resultLineWrapper;
    	 
	}
    

    @GET
	@Path("/calculate/{param1}/{param2}/{param3}/{param4}")
    @Produces(MediaType.APPLICATION_JSON)
	public DisjktraDTO calculate(@PathParam("param1") String startLat,@PathParam("param2") String startLng,
			@PathParam("param3") String destLat,@PathParam("param3") String destLng) throws Exception{
    	
    	String sourceNodeId="";//Calculate it
    	String targetNodeId="";//Calculate it
    	
    	DisjktraDTO disjktaDTO = Neo4jUtil.calculateDisjktra(sourceNodeId, targetNodeId);
    	
    	return disjktaDTO;
    			
    			
    	
    
    	 
	}
    
    @GET
	@Path("/getIlceList")
    @Produces(MediaType.APPLICATION_JSON)
	public List<KeyValueDTO> getIlceList() throws Exception{
    	List<KeyValueDTO> result = new ArrayList<KeyValueDTO>();
    	
    	List<SelectItem> ilceList = CityCurfTest.getIlceList();
    	for (Iterator iterator = ilceList.iterator(); iterator.hasNext();) {
			SelectItem selectItem = (SelectItem) iterator.next();
			result.add(new KeyValueDTO(selectItem.getLabel(),selectItem.getValue().toString()));
		}
	
    	return result;
    }
    
    @GET
	@Path("/getMahalleList/{param1}/")
    @Produces(MediaType.APPLICATION_JSON)
	public List<KeyValueDTO> getMahalleist(@PathParam("param1") String ilceName) throws Exception{
    	List<KeyValueDTO> result = new ArrayList<KeyValueDTO>();
    	
    	List<SelectItem> list = CityCurfTest.getMahalleList(ilceName);
    	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			SelectItem selectItem = (SelectItem) iterator.next();
			result.add(new KeyValueDTO(selectItem.getLabel(),selectItem.getValue().toString()));
		}
	
    	return result;
    }
   
    @GET
	@Path("/getSokakList/{param1}/")
    @Produces(MediaType.APPLICATION_JSON)
	public List<KeyValueDTO> getLSokakList(@PathParam("param1") String name) throws Exception{
    	List<KeyValueDTO> result = new ArrayList<KeyValueDTO>();
    	
    	List<SelectItem> list = CityCurfTest.getSokakList(name);
    	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			SelectItem selectItem = (SelectItem) iterator.next();
			result.add(new KeyValueDTO(selectItem.getLabel(),selectItem.getValue().toString()));
		}
	
    	return result;
    }
    
    @GET
	@Path("/getBinaList/{param1}/")
    @Produces(MediaType.APPLICATION_JSON)
	public List<KeyValueDTO> getBinaList(@PathParam("param1") String name) throws Exception{
    	List<KeyValueDTO> result = new ArrayList<KeyValueDTO>();
    	
    	List<SelectItem> list = CityCurfTest.getBinaList(name);
    	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			SelectItem selectItem = (SelectItem) iterator.next();
			result.add(new KeyValueDTO(selectItem.getLabel(),selectItem.getValue().toString()));
		}
	
    	return result;
    }
    
    @GET
	@Path("/getCoordinate/{param1}/")
    @Produces(MediaType.APPLICATION_JSON)
	public List<KeyValueDTO> getCoordinate(@PathParam("param1") String binaNo) throws Exception{
    	List<KeyValueDTO> result = new ArrayList<KeyValueDTO>();
    	
    	List<String> list = CityCurfTest.getCoordinate(binaNo);
    	result.add(new KeyValueDTO("lat",list.get(0)));
    	result.add(new KeyValueDTO("lng",list.get(1)));
    	
	
    	return result;
    }
    
    @POST
	@Path("/coordinateInformation")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response  getGPSData(JSONObject inputJsonObj) throws Exception{
    	double targetLat =41.021306 ;
    	double targetLng =  29.227854;
    	double refLat = 41d;
    	double refLng = 29d;
    	
    	
    	if(context.getAttribute("lat")!=null){
    		targetLat = Double.parseDouble((String)context.getAttribute("lat"));
    	}
    	
    	if(context.getAttribute("lng")!=null){
    		targetLng = Double.parseDouble((String)context.getAttribute("lng"));
    	}
    	
    	if(context.getAttribute("refLat")!=null){
    		refLat = Double.parseDouble((String)context.getAttribute("refLat"));
    	}
    	
    	if(context.getAttribute("refLng")!=null){
    		refLng = Double.parseDouble((String)context.getAttribute("refLng"));
    	}
    	
    	System.out.println(inputJsonObj.toJSONString());
    	context.getAttribute("");
    	
    	OtobusData data = new OtobusData();
    	data.setStopName((String)inputJsonObj.get("StopName"));
    	data.setSpeed((String)inputJsonObj.get("Speed"));
    	data.setVehicleName((String)inputJsonObj.get("VehicleName"));
    	data.setDistance((String)inputJsonObj.get("Distance"));
    	data.setLatitude((String)inputJsonObj.get("Latitude"));
    	data.setLongitude((String)inputJsonObj.get("Longitude"));
    	data.setTimestamp((String)inputJsonObj.get("Timestamp"));
    	
    	
    	
    	if(context.getAttribute("otobusDataList")==null){
    		context.setAttribute("otobusDataList", new ArrayList<OtobusData>());
    	}
    	
    	List<OtobusData> list = (List<OtobusData>)context.getAttribute("otobusDataList");
    	if(list.size()>480)
    		list.clear();
    	
    	list.add(data);	
    	
    	
    	double distance = Util.distance(targetLat, targetLng, Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()), "M");
    	double bearing  =Util.calculateBearing(targetLat, targetLng, refLat, refLng);
    	if(distance<200){
    		GCMUtil.sendMessage("Durak", String.valueOf(distance),data.getLatitude(),data.getLongitude(),data.getSpeed(),bearing);
    	}
    	return Response.status(200).build();

    }
    
    @GET
	@Path("/getStationInformation")
    @Consumes(MediaType.APPLICATION_JSON)
	public String  getBusCoordinate() throws Exception{
    	String result = "";
    	
    	if(context.getAttribute("lat")!=null && context.getAttribute("lng")!=null){
    		result =(String)context.getAttribute("lat")+" "+(String)context.getAttribute("lng");
    	}
    	
    	
    	return result;
    }
    
} 