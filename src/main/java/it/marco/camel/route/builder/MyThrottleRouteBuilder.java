package it.marco.camel.route.builder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.camel.builder.RouteBuilder;

public class MyThrottleRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
		
		from("seda:a").
			throttle(100).
			to("direct:mock-result");
		
		from("seda:b").
			throttle(3).
			timePeriodMillis(30000).
			to("direct:mock-result");
		
		from("seda:c").
			throttle(100).
			asyncDelayed().
			to("direct:mock-result");
		
		from("seda:d").
			throttle(100).
			timePeriodMillis(5000).
			maximumRequestsPerPeriod(10).
			asyncDelayed().
			callerRunsWhenRejected(true).
			executorService(scheduledExecutorService).
			to("direct:mock-result");
		
		from("direct:mock-result").
			log("from direct:mock-result ----------> ${body}");

	}

}
