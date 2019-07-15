package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;

public class MyHystrixRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:start-hystrix")
		    .hystrix()
		        .to("http://fooservice.com/slow")
		    .onFallback()
		        .transform().constant("Fallback message")
		    .end()
		    .to("direct:mock-result");
		
		from("direct:mock-result")
			.log("from direct:mock-result ----------> ${body}");
	}

}
