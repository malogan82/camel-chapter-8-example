package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;

public class MyThrottleRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("seda:a").throttle(100).to("seda:b");

	}

}
