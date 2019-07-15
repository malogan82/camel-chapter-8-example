package it.marco.camel.route.builder;

import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.RouteBuilder;

public class MySampleRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from("direct:sample")
		    .sample()
		    .to("direct:mock-result");
		
		from("direct:sample-configured")
	    	.sample(1, TimeUnit.SECONDS)
	    	.to("direct:mock-result");
		
		from("direct:sample-configured-via-dsl")
		    .sample().samplePeriod(1).timeUnits(TimeUnit.SECONDS)
		    .to("direct:mock-result");
		
		from("direct:sample-messageFrequency")
		    .sample(10)
		    .to("direct:mock-result");

		from("direct:sample-messageFrequency-via-dsl")
		    .sample().sampleMessageFrequency(5)
		    .to("direct:mock-result");
		
		from("direct:mock-result")
			.log("from direct:mock-result ----------> ${body}");
	}

}
