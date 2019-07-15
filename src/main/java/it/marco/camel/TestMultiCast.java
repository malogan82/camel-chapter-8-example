package it.marco.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.marco.camel.route.builder.MyMultiCastRouteBuilder;
import it.marco.camel.runnable.MyRunnable;

public class TestMultiCast {
	
	public static Logger LOGGER = LoggerFactory.getLogger(TestMultiCast.class);

	public static void main(String[] args) {
		Main main = new Main();
		main.addRouteBuilder(new MyMultiCastRouteBuilder());
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
		long startTime1 = System.currentTimeMillis();
		String response1 = producerTemplate.requestBody("direct:start-offer","TEST",String.class);
		long endTime1 = System.currentTimeMillis();
		LOGGER.info(String.format("TIME ELAPSED ----------> %s", endTime1-startTime1));
		LOGGER.info(String.format("RESPONSE ----------> %s", response1));
		long startTime2 = System.currentTimeMillis();
		String response2 = producerTemplate.requestBody("direct:start-offer-parallel","TEST",String.class);
		long endTime2 = System.currentTimeMillis();
		LOGGER.info(String.format("TIME ELAPSED ----------> %s", endTime2-startTime2));
		LOGGER.info(String.format("RESPONSE ----------> %s", response2));
		long startTime3 = System.currentTimeMillis();
		String response3 = producerTemplate.requestBody("direct:start-offer-executor","TEST",String.class);
		long endTime3 = System.currentTimeMillis();
		LOGGER.info(String.format("TIME ELAPSED ----------> %s", endTime3-startTime3));
		LOGGER.info(String.format("RESPONSE ----------> %s", response3));
		try {
			main.stop();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		LOGGER.info("MAIN STOPPED");
		
	}
	
}
