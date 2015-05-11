

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
import com.yoltarif.model.Station;
import com.yoltarif.service.LineService;
import com.yoltarif.service.StationService;
import com.yoltarif.util.DistanceUtil;






//@ContextConfiguration(locations = "classpath:hellowordContext.xml")
@ContextConfiguration(locations = "file:///D:/calismalar/spatial2/yoltariffserver/src/main/webapp/WEB-INF/applicationContext_local.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class InsertWalkingData2 {

	

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
		long startDate=0;
		List<Station> stations=stationService.getUnprocessedStations();
			for (Iterator iterator = stations.iterator(); iterator.hasNext();) {
				try {
				station = (Station) iterator.next();
				
				count++;
//				if(count>1000)
//					break;
				
//				int tempCount = count++;
//				System.out.println(tempCount + " basladi");
				startDate  = (new Date()).getTime();
				//System.out.println(startDate);
				Set<Line> lineList =station.getLineList();
				
				String idList="";
				for (Iterator iterator2 = lineList.iterator(); iterator2.hasNext();) {
					Line line = (Line) iterator2.next();
					idList=idList+line.getId()+",";
				}
				//System.out.println((new Date()).getTime()-startDate);
				
				idList=idList.substring(0, idList.length()-1);
				List<String> list=stationService.findNearestStationListReturnCoordinate(station.getLat(), station.getLng(), "0.2", idList);
				//System.out.println("1."+((new Date()).getTime()-startDate));
				for (Iterator iterator2 = list.iterator(); iterator2.hasNext();) {
					String temp = (String) iterator2.next();
					String[] values = temp.split(",");
					Long id = new Long(values[0]);
					String wkt = values[1];
					String lat = com.yoltarif.util.Util.getLat(wkt);
					String lng = com.yoltarif.util.Util.getLng(wkt);
					
		    		int distance = DistanceUtil.distance(station.getLat(), station.getLng(),lat ,lng, "K");	
		    		double duration=0;
		    		if(distance==0){
	    			distance=1;
		    		}
		    		
		    			duration = DistanceUtil.calculateWalkingTime(distance);

					stationService.createWalkingRelationshipforStartNode(station.getId(),id,duration,new Long(distance));
					//List<NextStation> reStations= stationService.findWalkingRelationship();
				} 
//				station.setProcessed(true);
//				stationService.save(station);
				
				if((count % 10) == 0)
					System.out.println((count)+" bitti "+((new Date()).getTime()-startDate));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
	
	}
	
	
	
}
