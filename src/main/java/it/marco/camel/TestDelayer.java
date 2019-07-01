package it.marco.camel;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import it.marco.camel.pojo.SomeBean;
import it.marco.camel.route.builder.MyDelayerRouteBuilder;
import it.marco.camel.runnable.MyRunnable;

public class TestDelayer {
	
	public static Logger LOGGER = LoggerFactory.getLogger(TestDelayer.class);

	public static void main(String[] args) {
		Main main = new Main();
		ActiveMQJMSConnectionFactory activeMQJMSConnectionFactory = 
				new ActiveMQJMSConnectionFactory("tcp://localhost:61616", "admin", "admin");
		ActiveMQComponent activeMQComponent = new ActiveMQComponent();
		activeMQComponent.setConnectionFactory(activeMQJMSConnectionFactory);
		activeMQComponent.setMessageConverter(new SimpleMessageConverter());
		main.bind("activemq", activeMQComponent);
		main.bind("someBean", new SomeBean());
		main.addRouteBuilder(new MyDelayerRouteBuilder());
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
		producerTemplate.sendBody("direct:start","TEST");
		producerTemplate.sendBody("activemq:foo","TEST");
		producerTemplate.sendBody("activemq:queue:foo-queue","TEST");
		try {
			Thread.sleep(10000);
			main.stop();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		
	}
	
}
