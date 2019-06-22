package it.marco.camel.strategy;

import org.apache.camel.Exchange;
import org.apache.camel.Service;
import org.apache.camel.processor.aggregate.AggregateController;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.processor.aggregate.DefaultAggregateController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAggregationStrategy implements AggregationStrategy, Service {
	
	public static Logger LOGGER = LoggerFactory.getLogger(MyAggregationStrategy.class);
	
	private AggregateController controller;
	
	public MyAggregationStrategy(AggregateController controller) {
		this.controller = controller;
	}

	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if(oldExchange == null) {
			return newExchange;
		}else {
			int groups = controller.forceCompletionOfGroup("2");
			LOGGER.info("COMPLETED GROUPS ----------> "+groups);
//			if(oldExchange.getIn().getBody(String.class).contains("Carletti")) {
//				oldExchange.setProperty(Exchange.AGGREGATION_COMPLETE_CURRENT_GROUP, true);
//			}else {
				oldExchange.getIn().setBody(String.format("%s+%s", 
													  	  oldExchange.getIn().getBody(String.class),
													      newExchange.getIn().getBody(String.class)));
//			}
			return oldExchange;
		}
	}
	
	@Override
	public void start() throws Exception {
		LOGGER.info("--------------------> MyAggregationStrategy.start");	
	}

	@Override
	public void stop() throws Exception {
		LOGGER.info("--------------------> MyAggregationStrategy.stop");	
	}

}
