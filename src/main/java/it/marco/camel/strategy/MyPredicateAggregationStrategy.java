package it.marco.camel.strategy;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyPredicateAggregationStrategy implements Predicate {
	
	public static Logger LOGGER = LoggerFactory.getLogger(MyPredicateAggregationStrategy.class);

	@Override
	public boolean matches(Exchange exchange) {
		return exchange.getIn().getBody(String.class).contains("Carletti");
	}
	
	
}
