

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.yoltarif.service.CalculationService;
import com.yoltarif.service.LineService;
import com.yoltarif.service.StationService;






//@ContextConfiguration(locations = "classpath:hellowordContext.xml")
@ContextConfiguration(locations = "file:///D:/calismalar/spatial2/yoltariffserver/src/main/webapp/WEB-INF/applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class Disjktra2 {

	

@Autowired
StationService stationService;

@Autowired
LineService lineService;

@Autowired
CalculationService calculationService;

private final SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

@Rollback(false)
@Test
public void dijkstraTest(){
	System.out.println("basladi dis "+sdf.format(new Date()));
	
	String source="11368";
    String destination = "11365";
	//DisjktraDTO disjktraDTO = Neo4jUtil.calculateDisjktra(source, destination);
	
	calculationService.calcDisjktraDTO(source, destination);
	//List<NextStation> relationshipList = stationService.findRelationsByIdList(disjktraDTO.getRelationshipAsString());
	
	System.out.println("bitti dis "  +sdf.format(new Date()));
	
}
}
	
	


	
