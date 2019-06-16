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
//			producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE1", "id", "A");
//			producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE2", "id", "A");
//			producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE1", "id", "B");
//			producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE2", "id", "B");
//			producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE3", "id", "A");
//			producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE3", "id", "B");
			String order1 = "<order number=\"1\"><name>Marco</name><surname>Longobardi</surname><amount>50</amount></order>";
			String order2 = "<order number=\"2\"><name>Marco</name><surname>Carletti</surname><amount>100</amount></order>";
			String order3 = "<order number=\"2\"><name>Nevia</name><surname>Roscigno</surname><amount>150</amount></order>";
			producerTemplate.sendBody("direct:aggregateXPath",order1);
			producerTemplate.sendBody("direct:aggregateXPath",order2);
			producerTemplate.sendBody("direct:aggregateXPath",order3);
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
