package it.marco.camel;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import it.marco.camel.route.builder.MyResequencerRouteBuilder;
import it.marco.camel.runnable.MyRunnable;

public class TestResequencer {
	
	public static Logger LOGGER = LoggerFactory.getLogger(TestResequencer.class);

	public static void main(String[] args) {
		Main main = new Main();
		ActiveMQJMSConnectionFactory activeMQJMSConnectionFactory = 
				new ActiveMQJMSConnectionFactory("tcp://localhost:61616", "admin", "admin");
		JmsComponent jmsComponent = new JmsComponent();
		jmsComponent.setConnectionFactory(activeMQJMSConnectionFactory);
		jmsComponent.setMessageConverter(new SimpleMessageConverter());
		main.bind("jms", jmsComponent);
		main.addRouteBuilder(new MyResequencerRouteBuilder());
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
//		producerTemplate.sendBodyAndHeader("direct:start","Marco","TimeStamp",1);
//		producerTemplate.sendBodyAndHeader("direct:start","Antonio","TimeStamp",2);
//		producerTemplate.sendBodyAndHeader("direct:start","Giuseppe","TimeStamp",5);
//		producerTemplate.sendBodyAndHeader("direct:start","Vincenzo","TimeStamp",3);
//		producerTemplate.sendBodyAndHeader("direct:start","Francesco","TimeStamp",4);
//		producerTemplate.sendBodyAndHeader("direct:start-resequencer","Marco","TimeStamp",1);
//		producerTemplate.sendBodyAndHeader("direct:start-resequencer","Antonio","TimeStamp",2);
//		producerTemplate.sendBodyAndHeader("direct:start-resequencer","Giuseppe","TimeStamp",5);
//		producerTemplate.sendBodyAndHeader("direct:start-resequencer","Vincenzo","TimeStamp",3);
//		producerTemplate.sendBodyAndHeader("direct:start-resequencer","Francesco","TimeStamp",4);
//		producerTemplate.sendBodyAndHeader("direct:start-resequencer","Fabio","TimeStamp",5);
		producerTemplate.sendBody("jms:queue:foo","Marco");
		producerTemplate.sendBody("jms:queue:foo","Antonio");
		producerTemplate.sendBody("jms:queue:foo","Giuseppe");
		producerTemplate.sendBody("jms:queue:foo","Vincenzo");
		producerTemplate.sendBody("jms:queue:foo","Francesco");
		producerTemplate.sendBody("jms:queue:foo","Fabio");
		try {
			Thread.sleep(10000);
			main.stop();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		
	}
	
}
