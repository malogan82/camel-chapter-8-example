package it.marco.camel.pojo;

import java.util.List;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.marco.camel.route.builder.MySplitRouteBuilder;

public class MyOrderService {
	
	public static Logger LOGGER = LoggerFactory.getLogger(MySplitRouteBuilder.class);
	
	public void handleOrder(Exchange exchange) {
		exchange.getIn().setBody(String.format("%s+TRANSFORMED", exchange.getIn().getBody(String.class)));
	}
	
	public void buildCombinedResponse(String body) {
		LOGGER.info("buildCombinedResponse method");
		LOGGER.info(String.format("buildCombinedResponse ----------> %s", body));
	}

}
