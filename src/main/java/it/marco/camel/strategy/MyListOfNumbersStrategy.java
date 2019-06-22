package it.marco.camel.strategy;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AbstractListAggregationStrategy;

public class MyListOfNumbersStrategy extends AbstractListAggregationStrategy<Integer> {
	
	@Override
    public Integer getValue(Exchange exchange) {
        return exchange.getIn().getBody(Integer.class);
    }

}
