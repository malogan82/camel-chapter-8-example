package it.marco.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MyMultiCastProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.getIn().setBody(String.format("%s - %s",
				 exchange.getIn().getBody(String.class), 
				 exchange.getIn().getHeader("Bid",String.class)));
	}

}
