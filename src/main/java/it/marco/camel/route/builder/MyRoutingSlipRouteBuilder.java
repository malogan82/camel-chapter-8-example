package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyRoutingSlipRouteBuilder extends RouteBuilder {
	
	public static Logger LOGGER = LoggerFactory.getLogger(MyRoutingSlipRouteBuilder.class);

	@Override
	public void configure() throws Exception {
		
		from("direct:b").
			routingSlip("aRoutingSlipHeader");
		
		from("direct:c").
			routingSlip("aRoutingSlipHeader","#");
		
		from("direct:first").
			log("from direct:first ----------> ${body}").
			transform(simple("${body}+FIRST"));
		
		from("direct:second").
			log("from direct:second ----------> ${body}").
			transform(simple("${body}+SECOND"));
		
		from("direct:third").
			log("from direct:third ----------> ${body}").
			transform(simple("${body}+THIRD"));

	}

}
