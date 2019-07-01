package it.marco.camel.route.builder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyDelayerRouteBuilder extends RouteBuilder {
	
	public static Logger LOGGER = LoggerFactory.getLogger(MyDelayerRouteBuilder.class);
	
	private int count = 0;
	
	@Override
	public void configure() throws Exception {
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
		
		onException(Exception.class)
			.maximumRedeliveries(2)
			.backOffMultiplier(1.5)
			.handled(true)
			.delay(1000)
				.log("Halting for some time")
				.to("direct:mock-halt")
			.end()
		.end();
		
		from("seda:a").
			delay(2000).
			to("direct:mock-result");
		
		from("seda:b").
			delay(header("MyDelay")).
			to("direct:mock-result");
		
		from("direct:start").
			process(new Processor() {

				@Override
				public void process(Exchange exchange) throws Exception {
					if(count<2) {
					//if(count<3) {
						count++;
						LOGGER.error("##########EXCEPTION");
						throw new Exception();
					}
				}
				
			})
			.to("direct:mock-result");
		
		from("activemq:foo").
		  	delay().method("someBean", "computeDelay").
		  	to("activemq:bar");
		
		from("activemq:bar").
			log("from activemq:bar ----------> ${body}");
		
		from("activemq:queue:foo-queue")
		    .delay(1000)
		    .asyncDelayed()
			.callerRunsWhenRejected(true)
			.executorService(scheduledExecutorService)
		    .to("activemq:aDelayedQueue");
		
		from("activemq:aDelayedQueue").
			log("from activemq:aDelayedQueue ----------> ${body}");
		
		from("direct:mock-halt").
			log("from direct:mock-halt ----------> ${body}");
		
		from("direct:mock-result").
			log("from direct:mock-result ----------> ${body}");

	}

}
