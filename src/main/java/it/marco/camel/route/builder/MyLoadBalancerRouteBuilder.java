package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyLoadBalancerRouteBuilder extends RouteBuilder {
	
	public static Logger LOGGER = LoggerFactory.getLogger(MyLoadBalancerRouteBuilder.class);
	
	@Override
	public void configure() throws Exception {
		
		from("direct:start")
			.loadBalance()
			.roundRobin()
			.to("direct:mock-x", "direct:mock-y", "direct:mock-z");
		
		from("direct:start-random")
			.loadBalance()
			.random()
			.to("direct:mock-random-x","direct:mock-random-y","direct:mock-random-z");
		
		from("direct:start-sticky")
			.loadBalance()
			.sticky(header("username"))
			.to("direct:mock-sticky-x","direct:mock-sticky-y","direct:mock-sticky-z");
		
		from("direct:start-topic")
			.loadBalance()
			.topic()
			.to("direct:mock-topic-x","direct:mock-topic-y","direct:mock-topic-z");
			
		from("direct:mock-x")
			.log("from direct:mock-x ----------> ${body}");
		
		from("direct:mock-y")
			.log("from direct:mock-y ----------> ${body}");
		
		from("direct:mock-z")
			.log("from direct:mock-z ----------> ${body}");
		
		from("direct:mock-random-x")
			.log("from direct:mock-random-x ----------> ${body}");
	
		from("direct:mock-random-y")
			.log("from direct:mock-random-y ----------> ${body}");
		
		from("direct:mock-random-z")
			.log("from direct:mock-random-z ----------> ${body}");
		
		from("direct:mock-sticky-x")
			.log("from direct:mock-sticky-x ----------> ${body}");
	
		from("direct:mock-sticky-y")
			.log("from direct:mock-sticky-y ----------> ${body}");
		
		from("direct:mock-sticky-z")
			.log("from direct:mock-sticky-z ----------> ${body}");
		
		from("direct:mock-topic-x")
			.log("from direct:mock-topic-x ----------> ${body}");
	
		from("direct:mock-topic-y")
			.log("from direct:mock-topic-y ----------> ${body}");
		
		from("direct:mock-topic-z")
			.log("from direct:mock-topic-z ----------> ${body}");

	}

}
