package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.apache.camel.processor.aggregate.UseOriginalAggregationStrategy;

import it.marco.camel.processor.MyBeanProcessor;

public class MyAggregatorRouteBuilder extends RouteBuilder {
	
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
			.bean(new MyBeanProcessor(useOriginalAggregationStrategy))
			.aggregate(xpath("/order/@number"),useOriginalAggregationStrategy)
			//.aggregate(xpath("/order/@number"), new MyAggregationStrategy())
			//.aggregate(xpath("/order/@number"))
				//.aggregationStrategy(new MyAggregationStrategy())
			.ignoreInvalidCorrelationKeys()
			.completionTimeout(3000)
			.to("direct:aggregatedXPath");
		
		from("direct:aggregatedXPath")
			.log("from direct:aggregatedXPath ----------> ${body}");

	}

}
