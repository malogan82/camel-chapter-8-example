package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;

public class MySplitRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("seda:a")
			.split(bodyAs(String.class).tokenize("\n"))
			.to("seda:b");
		
		from("seda:b")
			.log("${body}");

	}

}
