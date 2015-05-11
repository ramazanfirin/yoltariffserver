

import java.util.Iterator;
import java.util.List;

import com.yoltarif.model.Line;
import com.yoltarif.model.LineWrapper;
import com.yoltarif.model.StationDTO;
import com.yoltarif.util.RestUtil;
import com.yoltarif.util.XStreamUtil;


public class InsertDataRestApi {

	public static void main(String[] args) throws Exception {
		insertRest();
	}

public static void insertRest() throws Exception{
	
	LineWrapper lineWrapper = XStreamUtil.parseTest();
	List<Line> list = lineWrapper.getLineList() ;
	boolean fistIsComplated=false;
	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
		Line line = (Line) iterator.next();
		List<StationDTO> stationList = line.getSortedStationList();
		for (Iterator iterator2 = stationList.iterator(); iterator2.hasNext();) {
			StationDTO stationDTO = (StationDTO) iterator2.next();
			try {
				RestUtil.createStation(stationDTO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}



}
