package it.marco.camel.strategy;

import org.apache.camel.Exchange;
import org.apache.camel.Service;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAggregationStrategy implements AggregationStrategy, Service {
	
	public static Logger LOGGER = LoggerFactory.getLogger(MyAggregationStrategy.class);

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
	public void start() throws Exception {
		LOGGER.info("--------------------> MyCompletionAwareAggregationStrategy.start");	
	}

	@Override
	public void stop() throws Exception {
		LOGGER.info("--------------------> MyCompletionAwareAggregationStrategy.stop");	
	}

}
