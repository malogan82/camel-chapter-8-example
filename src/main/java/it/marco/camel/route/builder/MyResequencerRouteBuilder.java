package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.config.BatchResequencerConfig;
import org.apache.camel.model.config.StreamResequencerConfig;
import org.apache.camel.processor.resequencer.ExpressionResultComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.marco.camel.comparator.MyComparator;

public class MyResequencerRouteBuilder extends RouteBuilder {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MyResequencerRouteBuilder.class);

	@Override
	public void configure() throws Exception {
		ExpressionResultComparator comparator = new MyComparator();
		
		from("direct:start").
			resequence(header("TimeStamp")).
			to("direct:mock-result");
		
		from("direct:mock-result").
			log("from direct:mock-result ----------> ${body}");
		
		from("direct:start-resequencer").
			resequence(header("TimeStamp")).
			batch(new BatchResequencerConfig(300,4000L)).
			allowDuplicates().
			reverse().
			to("direct:mock-result");
		
		from("direct:start-stream").
			resequence(header("seqnum")).
			stream().
			to("direct:mock-result");
		
		from("direct:start-stream-resequencer").
			resequence(header("seqnum")).
			stream(new StreamResequencerConfig(5000, 4000L)).
			ignoreInvalidExchanges().
			rejectOld().
			to("direct:mock-result");
		
		from("direct:start-stream-resequencer-comparator").
			resequence(header("seqnum")).
			stream(new StreamResequencerConfig(5000, 4000L, comparator)).
			to("direct:mock-result");
		
		from("jms:queue:foo").
			resequence(header("JMSPriority")).
			batch().
			timeout(3000).
			allowDuplicates().
			reverse().
			to("direct:mock-result");
		
	}

	

}
