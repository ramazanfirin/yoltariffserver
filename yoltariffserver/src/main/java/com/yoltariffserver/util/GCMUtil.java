package com.yoltariffserver.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class GCMUtil {
public static void sendMessage(String stopName,String distance,String lat,String lng,String speed,double bearing){
	String key ="AIzaSyA5vr74kwURbtKibIJUcKIe0Y019xj2gwE";
	//String id  = "APA91bEYx2JyCTvy7KDp-QytAj1jVtBM1VUome9J9i9ef6qTjqqLEyjgj8cdbxGsCvpY1pl4eY2hgD3oUTN8BG7knoBiKwjzHxOE3KVcjLfbCgJywHMoRp5osuFjI4ipLg55qpT-GZYlBv_eD6Xgrzhzo21nVu34pA";
	String id = "APA91bHZMGBbTJFmNdm47uoNfMJUL7IkmwGU-DimbJq1KB7OZoozGv3OuerDVJbHOzeRJsadI1RByo4ad3bMdNjd-0twgGlArt8ptya9j8lOsfcWNMBKGCakRIcHQOedOh6T-83R2EHJj2S7-dqcMranxNYbBm6OkOBS_-Sp3LL8-p5cOnPK6NaBQgV-2wow_J81x_LnFw57";
	String samsung = "APA91bEr7tTY1FvzkxzLc8oqBwGV6ftRyoc13QRdIc8PtfPECIrAV3etHtidVp8L9a2WJn9mW5qr47Qxs2P3f8Yr4zuZQ9HApSEyG4HWo2PLADNwNzkqns5CmP9s5l5OhDEK3k5dfoYzYql-IRwzuTHUYEMXr0GF_q2KwkF6nrCDbGxqHM0obc_tt9AitSQQ1T6B_pEomw0-";
	String samsung1 = "APA91bGcEz4xnBungl7NZuhTVhp0tDejwO1TVADwD6TtnY83m_MDFpKDg4xKKtOq1UtJSLLOo8cGESwSXLx5fbvncHk9AJ3TO7_U39YZLwZjlZzLS4aic5GFlHzMO4MGQosoqv3OwjbOqTOBxIOGnDsGtvFexlNO9OKzoFvpT28gbbSz37CBebW7Bv5BTz8TRWSCc6J7OP8_";
	
	List<String> idList = new ArrayList<String>();
	idList.add(id);
	idList.add(samsung);
	idList.add(samsung1);	
	
	Message message2 = new Message.Builder()
    .collapseKey("deneme")
    .timeToLive(0)
    .delayWhileIdle(false)
    //.dryRun(true)
    //.restrictedPackageName("com.example.bletesst1")
    .addData("programName", "yolTariff")
    .addData("stopName", stopName)
    .addData("distance", distance)
    .addData("lat", lat)
    .addData("lng", lng)
    .addData("speed", speed)
    .addData("bearing", String.valueOf(bearing))
    .build();
	
     Sender sender= new Sender(key);
	
	try {
		//Result result = sender.send(message2, id, 10);
		sender.send(message2,idList,  10);
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
