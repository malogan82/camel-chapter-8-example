package it.marco.camel.strategy;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.TimeoutAwareAggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyTimeoutAwareAggregationStrategy implements TimeoutAwareAggregationStrategy {
	
	public static Logger LOGGER = LoggerFactory.getLogger(MyTimeoutAwareAggregationStrategy.class);

	@Override
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

	@Override
	public void timeout(Exchange oldExchange, int index, int total, long timeout) {
		LOGGER.info("--------------------> MyTimeoutAwareAggregationStrategy.timeout");

	}

}
