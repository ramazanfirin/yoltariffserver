

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.yoltarif.model.Line;
import com.yoltarif.model.NextStation;
import com.yoltarif.model.Station;
import com.yoltarif.service.LineService;
import com.yoltarif.service.StationService;
import com.yoltarif.util.DistanceUtil;






//@ContextConfiguration(locations = "classpath:hellowordContext.xml")
@ContextConfiguration(locations = "file:///D:/calismalar/spatial2/yoltariffserver/src/main/webapp/WEB-INF/applicationContext_local.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class InsertWalkingData {

	

@Autowired
StationService stationService;

@Autowired
LineService lineService;

@Rollback(false)
@Test
public void dijkstraTest(){
	
}
	
//@Rollback(false)
//@Test
public void insertWalking(){
	List<Station> list = stationService.getAllStations();
	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
		//Station station = (Station) iterator.next();
		System.out.println("bitti");
	}
}

@Rollback(false)
	@Test
	public void addData() throws IOException{

		int count = 0;
		Station station;
			//Result<Station> stations=stationService.findAllStations();
			List<Station> stations=stationService.getUnprocessedStations();
			for (Iterator iterator = stations.iterator(); iterator.hasNext();) {
				try {
				station = (Station) iterator.next();
				
//				int tempCount = count++;
//				System.out.println(tempCount + " basladi");
				long startDate  = (new Date()).getTime();
				//System.out.println(startDate);
				Set<Line> lineList =station.getLineList();
				
				String idList="";
				for (Iterator iterator2 = lineList.iterator(); iterator2.hasNext();) {
					Line line = (Line) iterator2.next();
					idList=idList+line.getId()+",";
				}
				//System.out.println((new Date()).getTime()-startDate);
				
				idList=idList.substring(0, idList.length()-1);
				List<Long> list=stationService.findNearestStationList(station.getLat(), station.getLng(), "0.2", idList);
				//System.out.println("1."+((new Date()).getTime()-startDate));
				for (Iterator iterator2 = list.iterator(); iterator2.hasNext();) {
					Long id = (Long) iterator2.next();
					//System.out.println("2."+((new Date()).getTime()-startDate));
					Station station2 = stationService.findOne(id);
					//System.out.println("3."+((new Date()).getTime()-startDate));
					NextStation nextStation = new NextStation();
		    		nextStation.setEndNode(station2);
		    		nextStation.setStartNode(station);
		    		
//	    		String data= Util.getMaxrixData(Double.parseDouble(station2.getLat()), Double.parseDouble(station2.getLng()), 
//	    				Double.parseDouble(station.getLat()), Double.parseDouble(station.getLng()), TravelMode.WALKING);
//	    		
//	    		String[] datas = data.split(",");
		    		
		    		int distance = DistanceUtil.distance(station.getLat(), station.getLng(),station2.getLat() ,station2.getLng(), "K");	
					double duration = DistanceUtil.calculateWalkingTime(distance);
		    		
		    		nextStation.setDistance(new Long(distance));
		    		nextStation.setDuration(new Double(duration));
		    		
		    		nextStation.setTravelType(NextStation.TRAVEL_TYPE.WALKING.toString());
		    		station2.getPreivousList().add(nextStation);
		    		stationService.save(station2);
		    		System.out.println((++count)+" bitti "+((new Date()).getTime()-startDate));
		    		//System.out.println("bitti");
				}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
	
	}
	
	
	
}
