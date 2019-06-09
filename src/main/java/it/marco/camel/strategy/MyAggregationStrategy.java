package it.marco.camel.strategy;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class MyAggregationStrategy implements AggregationStrategy {

	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if(oldExchange == null) {
			return newExchange;
		}else {
			oldExchange.getIn().setBody(String.format("%s+%s", 
													  oldExchange.getIn().getBody(String.class),
													  newExchange.getIn().getBody(String.class)));
			return oldExchange;
		}
	}

}
