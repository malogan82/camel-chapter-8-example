package it.marco.camel.pojo;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyOrderStrategy implements AggregationStrategy {
	
	public static Logger LOGGER = LoggerFactory.getLogger(MyOrderStrategy.class);

    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            return newExchange;
        }

        String orders = oldExchange.getIn().getBody(String.class);
        String newLine = newExchange.getIn().getBody(String.class);

        LOGGER.debug("Aggregate old orders: " + orders);
        LOGGER.debug("Aggregate new order: " + newLine);

        orders = orders + ";" + newLine;
        oldExchange.getIn().setBody(orders);

        return oldExchange;
    }
}