package it.marco.camel;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import it.marco.camel.route.builder.MyDynamicRouterRouteBuilder;
import it.marco.camel.route.builder.MyLoopRouteBuilder;
import it.marco.camel.runnable.MyRunnable;

public class TestDynamicRouter {
	
	public static Logger LOGGER = LoggerFactory.getLogger(TestDynamicRouter.class);

	public static void main(String[] args) {
		ActiveMQJMSConnectionFactory activeMQJMSConnectionFactory = 
				new ActiveMQJMSConnectionFactory("tcp://localhost:61616", "admin", "admin");
		ActiveMQComponent activeMQComponent = new ActiveMQComponent();
		activeMQComponent.setConnectionFactory(activeMQJMSConnectionFactory);
		activeMQComponent.setMessageConverter(new SimpleMessageConverter());
		Main main = new Main();
		main.bind("activemq", activeMQComponent);
		main.addRouteBuilder(new MyDynamicRouterRouteBuilder());
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
		String xmlBody = "<customer><id>endpoint1</id></customer>";
		producerTemplate.sendBody("activemq:foo",xmlBody);
		xmlBody = "<customer><id>endpoint2</id></customer>";
		producerTemplate.sendBody("activemq:foo",xmlBody);
		xmlBody = "<customer><id>endpoint3</id></customer>";
		producerTemplate.sendBody("activemq:foo",xmlBody);
		try {
			main.stop();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		LOGGER.info("MAIN STOPPED");
		
	}
	
}
