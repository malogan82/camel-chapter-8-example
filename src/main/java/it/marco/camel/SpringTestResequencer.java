package it.marco.camel;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import it.marco.camel.runnable.MyRunnable;

public class SpringTestResequencer {
	
	public static Logger LOGGER = LoggerFactory.getLogger(SpringTestResequencer.class);
	
	public static void main(String[] args) {
		AbstractXmlApplicationContext appContext = new ClassPathXmlApplicationContext("camel-context-resequencer.xml");
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
			producerTemplate.sendBodyAndHeader("direct:start","Marco","TimeStamp",1);
			producerTemplate.sendBodyAndHeader("direct:start","Antonio","TimeStamp",2);
			producerTemplate.sendBodyAndHeader("direct:start","Giuseppe","TimeStamp",5);
			producerTemplate.sendBodyAndHeader("direct:start","Vincenzo","TimeStamp",3);
			producerTemplate.sendBodyAndHeader("direct:start","Francesco","TimeStamp",4);
			producerTemplate.sendBodyAndHeader("direct:start","Fabio","TimeStamp",6);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage(),e);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
	}

}
