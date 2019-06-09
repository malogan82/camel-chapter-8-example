package it.marco.camel.pojo;

public class MyAggregationStrategyPojo {
	
	public String aggregate(String oldBody, String newBody) {
		return String.format("%s+%s",oldBody,newBody);
	}

}
