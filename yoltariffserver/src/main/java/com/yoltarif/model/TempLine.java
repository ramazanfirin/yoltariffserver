package com.yoltarif.model;


import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
@TypeAlias(value="TempLine")
public class TempLine extends Line{

	
}



