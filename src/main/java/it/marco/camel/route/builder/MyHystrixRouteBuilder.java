package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.HystrixConfigurationDefinition;

public class MyHystrixRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		HystrixConfigurationDefinition sharedConfig = new HystrixConfigurationDefinition();
		sharedConfig.setExecutionTimeoutInMilliseconds(5000);
		sharedConfig.setCircuitBreakerSleepWindowInMilliseconds(10000);
		
		from("direct:start")
			.hystrix()
				.to("http://fooservice.com/slow")
			.onFallback()
			    .transform().constant("Fallback message")
			.end()
			.to("direct:mock-result");
		
		from("direct:start-configuration")
			.hystrix()
				.hystrixConfiguration()
					.executionTimeoutInMilliseconds(5000).circuitBreakerSleepWindowInMilliseconds(10000)
				.end()
				.to("http://fooservice.com/slow")
				.onFallback()
		        .transform().constant("Fallback message")
		    .end()
		    .to("direct:mock-result");
		
		from("direct:start-global-configuration")
			.hystrix()
				.hystrixConfiguration(sharedConfig)
				.to("http://fooservice.com/slow")
				.onFallback()
					.transform().constant("Fallback message")
		    .end()
		    .to("direct:mock-result");
		
		from("direct:mock-result")
			.log("from direct:mock-result ----------> ${body}");
	}

}
