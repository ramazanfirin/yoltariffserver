

import java.io.IOException;
import java.util.Date;
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
import com.yoltarif.model.LineWrapper;
import com.yoltarif.model.NextStation;
import com.yoltarif.model.Station;
import com.yoltarif.model.StationDTO;
import com.yoltarif.service.LineService;
import com.yoltarif.service.StationService;
import com.yoltarif.util.DistanceUtil;
import com.yoltarif.util.XStreamUtil;






//@ContextConfiguration(locations = "classpath:hellowordContext.xml")
@ContextConfiguration(locations = "file:///D:/calismalar/spatial2/yoltariffserver/src/main/webapp/WEB-INF/applicationContext_local.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class InsertTest {

	

@Autowired
StationService stationService;

@Autowired
LineService lineService;
	
	
	@Rollback(false)
	@Test
	public void addData() throws IOException{
		long startDate = (new Date()).getTime();
		try {
			
			//stationService.deleteDatabase();
//			Station tempStation = new Station();
//			tempStation.setName("");
//			tempStation.setWkt("POINT( 35.689442 38.685810 )");
//			stationService.save(tempStation);
//			stationService.deleteStation(tempStation);
			
			
			
			LineWrapper lineWrapper = XStreamUtil.parseTest();
			List<Line> list = lineWrapper.getLineList() ;
			boolean fistIsComplated=false;
			int lineCount=0;
			int stationCount=0;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				
				Line line = (Line) iterator.next();
				if(line.getName().contains("320") || (line.getName().contains("544")) || (line.getName().contains("496")))
					continue;
//				
				if((!(line.getName().contains("495") || line.getName().contains("323") )))
					continue;
				
//				if((!(line.getName().contains("500"))))
//					continue;
				
				if(line.getName().contains("323")){
					if(fistIsComplated)
						continue;
				}
//				xsasu89<xzzzzzzzzzwsax dfm
//				if(line.getName().contains("323"))
//					fistIsComplated=true;
				
				Line temp = lineService.getLineByName(line.getName());
				if(temp!=null){
					
					System.out.println(line.getName() +" hat bulundu");
					continue;
				}
				++lineCount;
				System.out.println(lineCount + " " +line.getName() +" devam ediyor");
				
				if(lineCount>50)
					break;
				
				List<StationDTO> stationList = line.getSortedStationList();
				Station station=null; 
				int index=0;
				for (Iterator iterator2 = stationList.iterator(); iterator2.hasNext();) {
					StationDTO stationDto = (StationDTO) iterator2.next();
				
//					Long id =stationService.findNearestStationId(stationDto.getLat(), stationDto.getLng(), "0.02");
//					if(id!=null)
//						station = stationService.findOne(id);
//					else{
						station = new Station();
						station.setName(stationDto.getName());
						station.setWkt(stationDto.getWkt());
						stationService.save(station);
						station.setProcessed(false);
						index++;
						station.setStationIndex(new Long(index));
//					}
					addToLine(line, station);
					stationService.save(station);
				}
				line.setSortedStationList(null);
				lineService.save(line);
				Line temp2 = lineService.getLineByName(line.getName());
				//calculateWalkingDataforLine(line);
				int a=0;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("hata");
		}
		long endDate = (new Date()).getTime();
		System.out.println("zaman =" +(endDate-startDate));
	
	}
	
	
	public void calculateWalkingDataforLine(Line line){
		for (Iterator iterator = line.getStationList().iterator();iterator.hasNext();) {
			Station type = (Station) iterator.next();
			checkWalkingNearStation(type,line);
			
		}
	}
	
	public Station checkWalkingNearStation(Station station,Line line){
		
		if(station.getName().contains("14"))
			System.out.println("geldi.");
		
		List<Long> stationList = stationService.findNearestStationList(station.getLat(), station.getLng(), "0.5",String.valueOf(line.getId()));
		for (Iterator iterator = stationList.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			Station targetStation = stationService.findOne(id);
			if(targetStation.getId().longValue() == station.getId().longValue())
				continue;
			NextStation nextStation = new NextStation();
    		nextStation.setEndNode(targetStation);
    		nextStation.setStartNode(station);
    		
//    		String data= Util.getMaxrixData(Double.parseDouble(station.getLat()), Double.parseDouble(station.getLng()), 
//    				Double.parseDouble(targetStation.getLat()), Double.parseDouble(targetStation.getLng()), TravelMode.WALKING);
    		String data = "10,10";
    		String[] datas = data.split(",");
    		
//    		nextStation.setDistance(new Long(datas[0]));
//    		nextStation.setDuration(new Double(datas[1]));
    		
    		
    		nextStation.setTravelType(NextStation.TRAVEL_TYPE.WALKING.toString());
    		station.getPreivousList().add(nextStation);
    		
//    		station = stationService.save(station);
//    		
//    		station = stationService.findOne(station.getId());
//    		targetStation = stationService.findOne(targetStation.getId());
    		
    		NextStation nextStation2 = new NextStation();
    		nextStation2.setEndNode(station);
    		nextStation2.setStartNode(targetStation);
    		
    		
//    		nextStation2.setDistance(new Long(datas[0]));
//    		nextStation2.setDuration(new Double(datas[1]));
    		nextStation2.setTravelType(NextStation.TRAVEL_TYPE.WALKING.toString());
    		station.getStationList().add(nextStation2);
    		
    		stationService.save(station);
			
		}
		return station;
	}
	
	public void addToLine(Line line,Station station) throws Exception{
		try {
			System.out.println(station.getName()+" ekliniyor");
			if(station.getName().contains("kurukopru"))
				System.out.println("geldi.");
			
			line.getStationList().add(station);
			if(line.getStationList().size()==1){
				line.setStartStation(station);
				line.setEndStation(station);
			}else{
			   		
				NextStation nextStation = new NextStation();
				nextStation.setEndNode(station);
				nextStation.setStartNode(line.getEndStation());
				
//    		String data= Util.getMaxrixData(Double.parseDouble(line.getEndStation().getLat()), Double.parseDouble(line.getEndStation().getLng()), 
//    				Double.parseDouble(station.getLat()), Double.parseDouble(station.getLng()), TravelMode.DRIVING);
//				String data ="10,10";
//				String[] datas = data.split(",");
				
				int distance = DistanceUtil.distance(station.getLat(), station.getLng(),line.getEndStation().getLat() ,line.getEndStation().getLng(), "K");	
				double duration = DistanceUtil.calculateDriving(distance);
				
				
    		nextStation.setDistance(new Long(distance));
    		nextStation.setDuration(new Double(duration));
				nextStation.setTravelType(NextStation.TRAVEL_TYPE.DRIVING.toString());
				
				station.getPreivousList().add(nextStation);
			}
			
			
			
			
			line.setEndStation(station);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
		
	}
	
	
	public void clearDataBase(){
		lineService.clearDatabase();
	}
	
}
