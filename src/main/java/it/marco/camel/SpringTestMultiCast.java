package it.marco.camel;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import it.marco.camel.runnable.MyRunnable;

public class SpringTestMultiCast {
	
	public static Logger LOGGER = LoggerFactory.getLogger(SpringTestMultiCast.class);
	
	public static void main(String[] args) {
		AbstractXmlApplicationContext appContext = new ClassPathXmlApplicationContext("camel-context-multicast.xml");
		try {
			CamelContext camelContext = SpringCamelContext.springCamelContext(appContext);
			Main main = new Main();
			main.getCamelContexts().add(camelContext);
			MyRunnable runnable = new MyRunnable(main);
			Thread thread = new Thread(runnable);
			thread.run();
			while(!main.isStarted()) {
				if(main.isStarted()) {
					break;
				}
			}
			LOGGER.info("MAIN STARTED");
			ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
			long startTime1 = System.currentTimeMillis();
			String response1 = producerTemplate.requestBody("direct:start-offer","TEST",String.class);
			long endTime1 = System.currentTimeMillis();
			LOGGER.info(String.format("TIME ELAPSED ----------> %s", endTime1-startTime1));
			LOGGER.info(String.format("RESPONSE ----------> %s", response1));
			try {
				main.stop();
			} catch (Exception e) {
				LOGGER.error(e.getMessage(),e);
			}
			LOGGER.info("MAIN STOPPED");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
	}

}
