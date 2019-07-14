package it.marco.camel.route.builder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import it.marco.camel.processor.MyMultiCastProcessor;
import it.marco.camel.processor.MyOnPrepareProcessor;
import it.marco.camel.strategy.HighestBidAggregationStrategy;

public class MyMultiCastRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		ThreadPoolExecutor executor = 
				new ThreadPoolExecutor(8,16,0,TimeUnit.MILLISECONDS,new LinkedBlockingQueue());
		
		from("direct:start-offer")
			.multicast(new HighestBidAggregationStrategy())
			    .to("direct:buyer1", "direct:buyer2", "direct:buyer3")
			.end()
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					exchange.getIn().setBody(String.format("%s - %s",
											 exchange.getIn().getBody(String.class), 
											 exchange.getIn().getHeader("Bid",String.class)));
				}
			});
		
		from("direct:start-offer-parallel")
			.multicast(new HighestBidAggregationStrategy())
				.parallelProcessing()
				.timeout(500)
			    .to("direct:buyer1", "direct:buyer2", "direct:buyer3")
			.end()
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					exchange.getIn().setBody(String.format("%s - %s",
											 exchange.getIn().getBody(String.class), 
											 exchange.getIn().getHeader("Bid",String.class)));
				}
			});
		
		from("direct:start-offer-executor")
			.multicast(new HighestBidAggregationStrategy())
				.executorService(executor)
			    .to("direct:buyer1", "direct:buyer2", "direct:buyer3")
			.end()
			.process(new MyMultiCastProcessor());
		
		from("direct:start-offer-on-prepare")
			.multicast(new HighestBidAggregationStrategy())
				.onPrepare(new MyOnPrepareProcessor())
			    .to("direct:buyer1", "direct:buyer2", "direct:buyer3")
			.end()
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					exchange.getIn().setBody(String.format("%s - %s",
											 exchange.getIn().getBody(String.class), 
											 exchange.getIn().getHeader("Bid",String.class)));
				}
			});
		
		from("direct:buyer1")
			.setHeader("Bid",constant(new Float(5000)))
			.log("from direct:buyer1 ----------> ${header.Bid}");
		
		from("direct:buyer2")
			.setHeader("Bid",constant(new Float(6000)))
			.log("from direct:buyer2 ----------> ${header.Bid}");
		
		from("direct:buyer3")
			.setHeader("Bid",constant(new Float(7000)))
			.log("from direct:buyer3 ----------> ${header.Bid}");

	}

}
