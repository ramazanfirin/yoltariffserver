package com.yoltariffserver.util;

public class Util {

	public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		  double theta = lon1 - lon2;
		  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		  dist = Math.acos(dist);
		  dist = rad2deg(dist);
		  dist = dist * 60 * 1.1515;
		  if (unit.equals("K")) {
		    dist = dist * 1.609344;
		  } else if (unit.equals("N")) {
		  	dist = dist * 0.8684;
		  }else if (unit.equals("M")) {
			  dist = dist * 1.609344;
			  dist = dist*1000;
			  }
		  return (dist);
		}
	
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts decimal degrees to radians             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	public static double deg2rad(double deg) {
	  return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts radians to decimal degrees             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	public static double rad2deg(double rad) {
	  return (rad * 180 / Math.PI);
	}
	
	public static void main(String[] args) {
		//41.021306, 29.227854 kose
		//41.023320, 29.225761
		double a = distance(41.021306d,29.227854d,41.023320d,29.225761d,"M");
		System.out.println(a);
	}
	
	public static double calculateBearing(double lat1, double lon1, double lat2, double lon2){
		  double longitude1 = lon1;
		  double longitude2 = lon2;
		  double latitude1 = Math.toRadians(lat1);
		  double latitude2 = Math.toRadians(lat2);
		  double longDiff= Math.toRadians(longitude2-longitude1);
		  double y= Math.sin(longDiff)*Math.cos(latitude2);
		  double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

		  return (Math.toDegrees(Math.atan2(y, x))+360)%360;
		}
	
}
