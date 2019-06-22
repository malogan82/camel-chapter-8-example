package it.marco.camel.route.builder;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hawtdb.HawtDBAggregationRepository;
import org.apache.camel.language.tokenizer.TokenizeLanguage;
import org.apache.camel.processor.aggregate.AggregateController;
import org.apache.camel.processor.aggregate.DefaultAggregateController;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.apache.camel.processor.aggregate.UseOriginalAggregationStrategy;
import org.apache.camel.spi.AggregationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.marco.camel.strategy.MyAggregationStrategy;
import it.marco.camel.strategy.MySimpleAggregationStrategy;

public class MyAggregatorRouteBuilder extends RouteBuilder {
	
	public static Logger LOGGER = LoggerFactory.getLogger(MyAggregatorRouteBuilder.class);
	
	private UseOriginalAggregationStrategy useOriginalAggregationStrategy = new UseOriginalAggregationStrategy();
	
	private AggregateController controller = new DefaultAggregateController();
	
	@Override
	public void configure() throws Exception {
		AggregationRepository repo = new HawtDBAggregationRepository("repo1", "target/data/hawtdb.dat");
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 16, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
		
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
			.closeCorrelationKeyOnCompletion(10000)
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
		
		from("direct:tokenizeXml")
			.log("from direct:tokenizeXml ----------> ${body}")
			.process(new Processor() {

				@Override
				public void process(Exchange exchange) throws Exception {
					Expression exp = TokenizeLanguage.tokenizeXML("${header.foo}", null);
					List<String> names = exp.evaluate(exchange, List.class);
					for(String name:names) {
						LOGGER.info(String.format("direct:tokenizeXml ----------> %s",name));
					}
					exchange.getIn().setBody(names);
				}
				
			});
		
		from("direct:start")
			.aggregate(header("StockSymbol"))
			 .completionTimeout(3000)
		        .groupExchanges()
		    .to("direct:result");
		
		from("direct:result")
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				LOGGER.info("process method");
				List<Exchange> names = exchange.getIn().getBody(List.class);
				for(Exchange name:names) {
					LOGGER.info(String.format("direct:result ----------> %s",name.getIn().getBody(String.class)));
				}
			}
			
		});
		
		from("direct:start-hawtdb")
	        .aggregate(header("id"), new MySimpleAggregationStrategy())
	            .completionTimeout(3000)
	            .aggregationRepository(repo)
	        .to("direct:aggregated-hawtdb");
		
		from("direct:aggregated-hawtdb")
			.log("from direct:aggregated-hawtdb ----------> ${body}");
		
//		from("direct:start-parallel-processing")
//		    .aggregate(header("id"), new UseLatestAggregationStrategy())
//		        .completionTimeout(3000)
//		        .parallelProcessing()
//		    .to("direct:aggregated-parallel-processing");
		
		from("direct:start-parallel-processing")
		    .aggregate(header("id"), new UseLatestAggregationStrategy())
		        .completionTimeout(3000)
		        .executorService(threadPoolExecutor)
		    .to("direct:aggregated-parallel-processing");
		
		from("direct:aggregated-parallel-processing")
			.log("from direct:aggregated-parallel-processing ----------> ${body}");
		
	}

}
