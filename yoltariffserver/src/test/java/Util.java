import java.math.BigDecimal;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.Duration;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;


public class Util {
	private static  GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyA5vr74kwURbtKibIJUcKIe0Y019xj2gwE");
	

	public static void main(String[] args) {
	
		
		
		
		
		//List<NameValuePair> actualParams = parseQueryParamsFromRequestLine(server.takeRequest().getRequestLine());
	System.out.println("bitti");
	}
	
	public static String getMaxrixData(double sourceLat,double sourceLng,double destinationLat,double destinationLng, TravelMode mode){
		DistanceMatrix matrix =DistanceMatrixApi.newRequest(context)
		        .origins(new LatLng(sourceLat, sourceLng))
		        .destinations(new LatLng(destinationLat, destinationLng))
		         .mode(mode.DRIVING)
		         .language("tr-TR")
		        .awaitIgnoreError();
				
				DistanceMatrixRow row =matrix.rows[0];
			    DistanceMatrixElement[] elements = row.elements;
			    Distance distance  = elements[0].distance;
			    Duration duration = elements[0].duration;
			    
			    
			    
			    BigDecimal b = new BigDecimal(duration.inSeconds);  
			    BigDecimal c = new BigDecimal(60);
			    BigDecimal d=new BigDecimal(b.doubleValue()/c.doubleValue());
			    
			    d = d.setScale(2, BigDecimal.ROUND_FLOOR);  
			    
//			    System.out.println(distance.inMeters);
//			    System.out.println(d.doubleValue());
//			    
			    return distance.inMeters+","+d.doubleValue();
			    
				
				
	}
	
	public static DistanceMatrix getMaxrixData(LatLng[] sourceLatLng,LatLng[] destinationLatLng,TravelMode mode){
		DistanceMatrix matrix =DistanceMatrixApi.newRequest(context)
		        .origins(sourceLatLng)
		        .destinations(destinationLatLng)
		         .mode(mode.DRIVING)
		         .language("tr-TR")
		        .awaitIgnoreError();
		return matrix;
	}
}
