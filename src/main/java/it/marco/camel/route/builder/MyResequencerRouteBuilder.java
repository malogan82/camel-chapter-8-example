package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyResequencerRouteBuilder extends RouteBuilder {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MyResequencerRouteBuilder.class);

	@Override
	public void configure() throws Exception {
		from("direct:start").
			resequence(header("TimeStamp")).
			to("direct:mock-result");
		
		from("direct:mock-result").
			log("from direct:mock-result ----------> ${body}");
		
	}

	

}
