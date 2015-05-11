package com.yoltarif.util;

public class DistanceUtil {

	/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::                                                                         :*/
	/*::  This routine calculates the distance between two points (given the     :*/
	/*::  latitude/longitude of those points). It is being used to calculate     :*/
	/*::  the distance between two locations using GeoDataSource (TM) prodducts  :*/
	/*::                                                                         :*/
	/*::  Definitions:                                                           :*/
	/*::    South latitudes are negative, east longitudes are positive           :*/
	/*::                                                                         :*/
	/*::  Passed to function:                                                    :*/
	/*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
	/*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
	/*::    unit = the unit you desire for results                               :*/
	/*::           where: 'M' is statute miles                                   :*/
	/*::                  'K' is kilometers (default)                            :*/
	/*::                  'N' is nautical miles                                  :*/
	/*::  Worldwide cities and other features databases with latitude longitude  :*/
	/*::  are available at http://www.geodatasource.com                          :*/
	/*::                                                                         :*/
	/*::  For enquiries, please contact sales@geodatasource.com                  :*/
	/*::                                                                         :*/
	/*::  Official Web site: http://www.geodatasource.com                        :*/
	/*::                                                                         :*/
	/*::           GeoDataSource.com (C) All Rights Reserved 2014                :*/
	/*::                                                                         :*/
	/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

	public static int distance(String _lat1, String _lon1, String _lat2, String _lon2, String unit) {
		double lat1= Double.parseDouble(_lat1);
		double lon1 = Double.parseDouble(_lon1);
		double lat2 = Double.parseDouble(_lat2);
		double lon2 = Double.parseDouble(_lon2);
		
		
		double theta = lon1 - lon2;
	  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	  dist = Math.acos(dist);
	  dist = rad2deg(dist);
	  dist = dist * 60 * 1.1515;
	  if (unit.equals("K")) {
	    dist = dist * 1.609344;
	    dist = dist *1000;
	  } else if (unit.equals("N")) {
	  	dist = dist * 0.8684;
	    }
	  return (int)dist;
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts decimal degrees to radians             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static  double deg2rad(double deg) {
	  return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts radians to decimal degrees             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double rad2deg(double rad) {
	  return (rad * 180 / Math.PI);
	}
	
	public static void main(String[] args) {
		int i =distance("38.73063256421581", "35.48267550793457","38.72269752381757","35.48703096029658", "K");
		//int i = (int)d;
		System.out.println(i);
		double walking = calculateWalkingTime(new Long(i));
		System.out.println(walking);
	}

	public static double calculateWalkingTime(int _distance){
		double distance = (double)_distance;
		double result = distance / 60;
		return result;
		
	}
	
	public static double calculateWalkingTime(Long _distance){
		double distance = (double)_distance;
		double result = distance / 60;
		return result;
		
	}
	
	public static double calculateDriving(int _distance){
		double distance = (double)_distance;
		double result = distance / 400;
		return result;
		
	}
	
}
