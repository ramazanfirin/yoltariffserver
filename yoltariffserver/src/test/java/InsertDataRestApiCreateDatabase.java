

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.yoltarif.model.Station;
import com.yoltarif.service.LineService;
import com.yoltarif.service.StationService;






//@ContextConfiguration(locations = "classpath:hellowordContext.xml")
@ContextConfiguration(locations = "file:///D:/calismalar/spatial2/yoltariffserver/src/main/webapp/WEB-INF/applicationContext_local.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class InsertDataRestApiCreateDatabase {

	

@Autowired
StationService stationService;

@Autowired
LineService lineService;
	
	
	@Rollback(false)
	@Test
	public void addData() throws IOException{
	
	Station station = new Station();
	station.setName("test");
	station.setWkt("POINT( 35.442211 38.699169 )");
	
	stationService.save(station);
	stationService.deleteStation(station);
	}
}
