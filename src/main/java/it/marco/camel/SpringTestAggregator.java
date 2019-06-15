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

public class SpringTestAggregator {
	
	public static Logger LOGGER = LoggerFactory.getLogger(SpringTestAggregator.class);
	
	public static void main(String[] args) {
		AbstractXmlApplicationContext appContext = new ClassPathXmlApplicationContext("camel-context-aggregator.xml");
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
			producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE1", "id", "A");
			producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE2", "id", "A");
			producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE1", "id", "B");
			producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE2", "id", "B");
			producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE3", "id", "A");
			producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE3", "id", "B");
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
