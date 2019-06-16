package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;

import it.marco.camel.strategy.MyAggregationStrategy;

public class MyAggregatorRouteBuilder extends RouteBuilder {

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
			.aggregate(xpath("/order/@number"), new MyAggregationStrategy())
				.ignoreInvalidCorrelationKeys()
				.completionTimeout(3000)
			.to("direct:aggregatedXPath");
		
		from("direct:aggregatedXPath")
			.log("from direct:aggregatedXPath ----------> ${body}");

	}

}
