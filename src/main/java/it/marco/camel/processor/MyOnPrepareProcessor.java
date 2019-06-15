package it.marco.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MyOnPrepareProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.getIn().setBody(String.format("PREPARED_%S", exchange.getIn().getBody(String.class)));
	}

}
