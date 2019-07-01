package it.marco.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import it.marco.camel.runnable.MyRunnable;

public class SpringTestThrottle {
	
	public static Logger LOGGER = LoggerFactory.getLogger(SpringTestThrottle.class);
	
	public static void main(String[] args) {
		AbstractXmlApplicationContext appContext = new ClassPathXmlApplicationContext("camel-context-throttle.xml");
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
