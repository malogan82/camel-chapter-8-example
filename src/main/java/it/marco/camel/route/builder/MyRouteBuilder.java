package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;

//import it.marco.camel.strategy.MyAggregationStrategy;

public class MyRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
//		from("direct:start").
//			recipientList(header("recipientList")).
//			aggregationStrategy(new MyAggregationStrategy()).
//			delimiter(",");
		
//		from("direct:start").
//			recipientList(header("recipientList")).
//			aggregationStrategyRef("myAggregationStrategy").
//			delimiter(",");
		
		from("direct:start").
			recipientList(header("recipientList")).
			aggregationStrategyRef("myAggregationStrategyPojo").
			aggregationStrategyMethodName("aggregate").
			aggregationStrategyMethodAllowNull().
			parallelProcessing().
			parallelAggregate().
			stopOnException().
			ignoreInvalidEndpoints().
			streaming().
			timeout(3000).
			onPrepareRef("myPreparePojo").
			shareUnitOfWork().
			cacheSize(-1).
			delimiter(",");
			
		
		from("seda:a").
		    log("----------> seda a ----------> ${body}");
		
		from("seda:b").
			log("----------> seda b ----------> ${body}");
		
		from("seda:c").
			log("----------> seda c ----------> ${body}");
		
		from("direct:a").
	    	log("----------> direct a ----------> ${body}").
	    	transform(simple("${body} direct:a"));
	
		from("direct:b").
			log("----------> direct b ----------> ${body}").
	    	transform(simple("${body} direct:b"));
		
		from("direct:c").
			log("----------> direct c ----------> ${body}").
	    	transform(simple("${body} direct:c"));
	}

}
