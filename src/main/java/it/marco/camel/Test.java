package it.marco.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.marco.camel.route.builder.MySplitRouteBuilder;
import it.marco.camel.runnable.MyRunnable;

public class Test {
	
	public static Logger LOGGER = LoggerFactory.getLogger(Test.class);

	public static void main(String[] args) {
		Main main = new Main();
		main.addRouteBuilder(new MySplitRouteBuilder());
		//main.addRouteBuilder(new MyRouteBuilder());
		//main.bind("myAggregationStrategy", new MyAggregationStrategy());
		//main.bind("myAggregationStrategyPojo", new MyAggregationStrategyPojo());
		//main.bind("myPreparePojo", new MyPrepareProcessor());
		MyRunnable runnable = new MyRunnable(main);
		Thread thread = new Thread(runnable);
		thread.run();
		while(!main.isStarted()) {
			if(main.isStarted()) {
				break;
			}
		}
		LOGGER.info("MAIN STARTED");
		CamelContext camelContext = main.getCamelContexts().get(0);
		ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
		String body = "first message\nsecond message \nthird message\nfourth message";
		producerTemplate.sendBody("seda:a",body);
		LOGGER.info("MESSAGE SENT");
		//producerTemplate.sendBodyAndHeader("direct:start", "Hello World", "recipientList", "seda:a,seda:b,seda:c");
		//LOGGER.info("FIRST MESSAGE SENT");
		//Object response = producerTemplate.requestBodyAndHeader("direct:start", "Hello World", "recipientList", "direct:a,direct:b,direct:c,xxx:d",String.class);
		//LOGGER.info("SECOND MESSAGE SENT");
		//LOGGER.info(String.format("RESPONSE ----------> %s",response));
		try {
			main.stop();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		LOGGER.info("MAIN STOPPED");
		
	}
	
}
