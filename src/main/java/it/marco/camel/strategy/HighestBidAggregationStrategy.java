package it.marco.camel.strategy;

import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.Exchange;

public class HighestBidAggregationStrategy implements AggregationStrategy {
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
    	if(oldExchange!=null) {
	        float oldBid = oldExchange.getIn().getHeader("Bid", Float.class);
	        float newBid = newExchange.getIn().getHeader("Bid", Float.class);
	        return (newBid > oldBid) ? newExchange : oldExchange;
    	}else {
    		return newExchange;
    	}
    }
}