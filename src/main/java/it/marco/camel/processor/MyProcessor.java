package it.marco.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MyProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		if(exchange.getIn().getBody(String.class).contains("Marco")){
			throw new Exception("TEST EXCEPTION");
		}

	}

}
