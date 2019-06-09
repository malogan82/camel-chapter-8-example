package it.marco.camel.pojo;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MyPrepareProcessor implements Processor{
	
	public void process(Exchange exchange) throws Exception {
		String copy = String.format("%s_copied", exchange.getIn().getBody());
		exchange.getIn().setBody(copy);
	}

}
