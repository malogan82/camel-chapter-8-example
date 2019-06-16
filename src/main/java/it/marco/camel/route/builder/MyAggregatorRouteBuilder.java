package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregateController;
import org.apache.camel.processor.aggregate.DefaultAggregateController;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.apache.camel.processor.aggregate.UseOriginalAggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.marco.camel.strategy.MyAggregationStrategy;

public class MyAggregatorRouteBuilder extends RouteBuilder {
	
	public static Logger LOGGER = LoggerFactory.getLogger(MyAggregatorRouteBuilder.class);
	
	private UseOriginalAggregationStrategy useOriginalAggregationStrategy = new UseOriginalAggregationStrategy();
	
	private AggregateController controller = new DefaultAggregateController();
	
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
			.aggregate(xpath("/order/@number"), new MyAggregationStrategy(controller))
			//.completionSize(header("mySize"))
			//.completionPredicate(header("MsgType").isEqualTo("ALERT"))
			//.eagerCheckCompletion()
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
