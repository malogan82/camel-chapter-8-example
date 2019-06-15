package it.marco.camel;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import it.marco.camel.route.builder.MyAggregatorRouteBuilder;
import it.marco.camel.runnable.MyRunnable;

public class TestAggregator {
	
	public static Logger LOGGER = LoggerFactory.getLogger(TestAggregator.class);

	public static void main(String[] args) {
		Main main = new Main();
		ActiveMQJMSConnectionFactory activeMQJMSConnectionFactory = 
				new ActiveMQJMSConnectionFactory("tcp://localhost:61616", "admin", "admin");
		ActiveMQComponent activeMQComponent = new ActiveMQComponent();
		activeMQComponent.setConnectionFactory(activeMQJMSConnectionFactory);
		activeMQComponent.setMessageConverter(new SimpleMessageConverter());
		main.bind("activemq", activeMQComponent);
		main.addRouteBuilder(new MyAggregatorRouteBuilder());
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
	}
	
}
