package it.marco.camel;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.marco.camel.route.builder.MyThrottleRouteBuilder;
import it.marco.camel.runnable.MyRunnable;

public class TestThrottle {
	
	public static Logger LOGGER = LoggerFactory.getLogger(TestThrottle.class);

	public static void main(String[] args) {
		Main main = new Main();
		main.addRouteBuilder(new MyThrottleRouteBuilder());
		main.bind("executorService", new ThreadPoolExecutor(8, 16, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue()));
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
		for(int i=0;i<=100;i++) {
			String body = "TEST"+(i+1);
			producerTemplate.sendBody("seda:d",body);
		}
		try {
			Thread.sleep(10000);
			main.stop();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		
	}
	
}
