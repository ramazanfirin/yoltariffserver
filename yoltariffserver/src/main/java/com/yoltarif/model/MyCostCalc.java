package com.yoltarif.model;

import org.neo4j.graphalgo.CostEvaluator;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Relationship;

public class MyCostCalc implements CostEvaluator {
	 
	  public Double getCost(Relationship relationship, Direction direction) {
	       Double a = Double.valueOf(relationship.getProperty("distance").toString());
	       return a;
	  }
	 
	}