package it.marco.camel.route.builder;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.GroupedExchangeAggregationStrategy;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.apache.camel.processor.aggregate.UseOriginalAggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.marco.camel.strategy.MyAggregationStrategy;
import it.marco.camel.strategy.MyCompletionAwareAggregationStrategy;

public class MyAggregatorRouteBuilder extends RouteBuilder {
	
	public static Logger LOGGER = LoggerFactory.getLogger(MyAggregatorRouteBuilder.class);
	
	private UseOriginalAggregationStrategy useOriginalAggregationStrategy = new UseOriginalAggregationStrategy();
	
	@Override
	public void configure() throws Exception {
		from("direct:aggregate")
			.aggregate(header("id"), new UseLatestAggregationStrategy())
				.completionTimeout(3000)
			.to("direct:aggregated");
		
		from("direct:aggregated")
			.log("from direct:aggregated ----------> ${body} for id ${header.id}");
		
		from("direct:aggregateXPath")
			.log("from direct:aggregateXPath ----------> ${body}")
			//.aggregate(xpath("/order/@number"),new MyCompletionAwareAggregationStrategy())
			//.aggregate(xpath("/order/@number"),new GroupedExchangeAggregationStrategy())
			//.bean(new MyBeanProcessor(useOriginalAggregationStrategy))
			//.aggregate(xpath("/order/@number"),useOriginalAggregationStrategy)
			.aggregate(xpath("/order/@number"), new MyAggregationStrategy())
			//.aggregate(xpath("/order/@number"))
				//.aggregationStrategy(new MyAggregationStrategy())
			.ignoreInvalidCorrelationKeys()
			.completionTimeout(3000)
			.to("direct:aggregatedXPath");
		
		from("direct:aggregatedXPath")
			.log("from direct:aggregatedXPath ----------> ${body}");
//			.process(new Processor() {
//				@Override
//				public void process(Exchange exchange) throws Exception {
//					List<Exchange> aggregatedList = exchange.getProperty(Exchange.GROUPED_EXCHANGE,List.class);
//					for(Exchange aggregated:aggregatedList) {
//						LOGGER.info(String.format("from direct:aggregatedXPath ---------->  %s", aggregated.getIn().getBody(String.class)));
//					}
//				}
//			});

	}

}
