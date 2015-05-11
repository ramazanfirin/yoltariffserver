package mydisjktra;



import java.io.IOException;
import java.util.Iterator;
import java.util.List;

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






//@ContextConfiguration(locations = "classpath:hellowordContext.xml")
@ContextConfiguration(locations = "file:///D:/calismalar/spatial2/yoltariffserver/src/main/webapp/WEB-INF/applicationContext_local.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class InsertStartTime {

	

@Autowired
StationService stationService;

@Autowired
LineService lineService;

@Rollback(false)
@Test
public void dijkstraTest(){
	
}
	
//
//@Rollback(false)
//	@Test
	public void addDataToLine() throws IOException{

		int count = 0;
		//Station station;
		long startDate=0;
		List<Line> lineList=lineService.getAllLines();
		for (Iterator iterator = lineList.iterator(); iterator.hasNext();) {
			Line line = (Line) iterator.next();
			for(int i=360;i<1140;i=i+30){
				line.getStartTime().add(new Long(i));
			}
			line.setSortedStationList(null);
			lineService.save(line);
			
		}	

		System.out.println("line bitti");
		//addDataToStation();
	}

@Rollback(false)
@Test
public void addDataToStation() throws IOException{
	int i=0;
	List<Line> lineList=lineService.getAllLines();
	for (Iterator iterator = lineList.iterator(); iterator.hasNext();) {
		Line line = (Line) iterator.next();
		List<Long> startTimeList = line.getStartTime();
		for (Iterator iterator2 = startTimeList.iterator(); iterator2.hasNext();) {
			Long long1 = (Long) iterator2.next();
			List<Station> statinList = lineService.getStationList(line.getId());
			i=0;
			for (Iterator iterator3 = statinList.iterator(); iterator3
					.hasNext();) {
				i++;
				Station station = (Station) iterator3.next();
				station.getArriveTime().add(long1+(i*3));
				stationService.save(station);
			}
			
		}	
			
			
			
	}
	System.out.println("station bitti");
}
}
