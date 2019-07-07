package it.marco.camel.route.builder;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.marco.camel.exception.MyCustomException;
import it.marco.camel.exception.MyOtherException;
import it.marco.camel.load.balancer.MyLoadBalancer;

public class MyLoadBalancerRouteBuilder extends RouteBuilder {
	
	public static Logger LOGGER = LoggerFactory.getLogger(MyLoadBalancerRouteBuilder.class);
	
	@Override
	public void configure() throws Exception {
		
		errorHandler(defaultErrorHandler().maximumRedeliveries(5));
		
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
		
		from("direct:start-failover")
			.loadBalance()
			.failover(IOException.class)
			.to("direct:failover-x",
				"direct:failover-y",
				"direct:failover-z");
		
		from("direct:start-failover-no-error-handler")
			.loadBalance()
			.failover(1,false,true,IOException.class)
			.to("direct:failover-x",
				"direct:failover-y",
				"direct:failover-z");
	
		from("direct:start-failover-no-exception")
			.loadBalance()
			.failover()
			.to("direct:failover-x",
				"direct:failover-y",
				"direct:failover-z");
		
		from("direct:start-failover-no-exception-roundrobin")
			.loadBalance()
			.failover(5,false,true)
			.to("direct:failover-x",
				"direct:failover-y",
				"direct:failover-z");
		
		from("direct:foo")
	    	.loadBalance()
	    	.failover(IOException.class, MyOtherException.class)
			.to("direct:a", "direct:b");
		
		from("direct:start-weighted")
			.loadBalance()
			.weighted(true, "4:2:1", ":")
			.to("direct:start-weighted-x", 
				"direct:start-weighted-y", 
				"direct:start-weighted-z");
		
		from("direct:start-weighted-default-delimiter")
			.loadBalance()
			.weighted(true, "4,2,1")
			.to("direct:start-weighted-x", 
				"direct:start-weighted-y", 
				"direct:start-weighted-z");
		
		from("direct:start-custom-load-balancer")
			.loadBalance(new MyLoadBalancer())
			.to("direct:custom-load-balancer-x",
				"direct:custom-load-balancer-y",
				"direct:custom-load-balancer-z");
		
		from("direct:start-circuit-breaker")
			.loadBalance()
			.circuitBreaker(2, 1000L, MyCustomException.class)
			.to("direct:start-circuit-breaker-result");
	
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
		
		from("direct:failover-x")
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					LOGGER.error("##########IOException");
					throw new IOException();
				}
			})
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					LOGGER.info("##########NEXT PROCESSOR");
				}
			})
			.log("from direct:failover-x ----------> ${body}");

		from("direct:failover-y")
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					LOGGER.error("##########IOException");
					throw new IOException();
				}
			})
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					LOGGER.info("##########NEXT PROCESSOR");
				}
			})
			.log("from direct:failover-y ----------> ${body}");
	
		from("direct:failover-z")
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					LOGGER.error("##########IOException");
					throw new IOException();
				}
			})
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					LOGGER.info("##########NEXT PROCESSOR");
				}
			})
			.log("from direct:failover-z ----------> ${body}");
		
		from("direct:a")
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					LOGGER.error("##########MyOtherException");
					throw new MyOtherException();
				}
			})
	    	.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					LOGGER.info("##########NEXT PROCESSOR");
				}
			})
			.log("from direct:a ----------> ${body}");
		
		from("direct:b")
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					LOGGER.error("##########MyOtherException");
					throw new MyOtherException();
				}
			})
	    	.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					LOGGER.info("##########NEXT PROCESSOR");
				}
			})
			.log("from direct:b ----------> ${body}");
		
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
		
		from("direct:start-weighted-x")
			.log("from direct:start-weighted-x ----------> ${body}");

		from("direct:start-weighted-y")
			.log("from direct:start-weighted-y ----------> ${body}");
	
		from("direct:start-weighted-z")
			.log("from direct:start-weighted-z ----------> ${body}");
		
		from("direct:custom-load-balancer-x")
			.log("from direct:custom-load-balancer-x ----------> ${body}");

		from("direct:custom-load-balancer-y")
			.log("from direct:custom-load-balancer-y ----------> ${body}");

		from("direct:custom-load-balancer-z")
			.log("from direct:custom-load-balancer-z ----------> ${body}");
		
		from("direct:start-circuit-breaker-result")
			.log("from direct:start-circuit-breaker-result ----------> ${body}");

	}

}
