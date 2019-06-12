package it.marco.camel;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import it.marco.camel.pojo.MyOrderService;
import it.marco.camel.pojo.MySplitterBean;
import it.marco.camel.route.builder.MySplitRouteBuilder;
import it.marco.camel.runnable.MyRunnable;

public class Test {
	
	public static Logger LOGGER = LoggerFactory.getLogger(Test.class);

	public static void main(String[] args) {
		Main main = new Main();
		ActiveMQJMSConnectionFactory activeMQJMSConnectionFactory = 
				new ActiveMQJMSConnectionFactory("tcp://localhost:61616", "admin", "admin");
		ActiveMQComponent activeMQComponent = new ActiveMQComponent();
		activeMQComponent.setConnectionFactory(activeMQJMSConnectionFactory);
		activeMQComponent.setMessageConverter(new SimpleMessageConverter());
		main.bind("activemq", activeMQComponent);
		main.bind("mySplitterBean", new MySplitterBean(new DefaultCamelContext()));
		main.bind("MyOrderService", new MyOrderService());
		main.addRouteBuilder(new MySplitRouteBuilder());
		//main.addRouteBuilder(new MyRouteBuilder());
		//main.bind("myAggregationStrategy", new MyAggregationStrategy());
		//main.bind("myAggregationStrategyPojo", new MyAggregationStrategyPojo());
		//main.bind("myPreparePojo", new MyPrepareProcessor());
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
		String body = "first message\nsecond message \nthird message\nfourth message";
		producerTemplate.sendBody("seda:a",body);
		LOGGER.info("FIRST MESSAGE SENT");
		String xmlBody = "<foos><foo><bar>First Message</bar></foo><foo><bar>Second Message</bar></foo><foo><bar>Third Message</bar></foo></foos>";
		producerTemplate.sendBody("seda:c",xmlBody);
		LOGGER.info("SECOND MESSAGE SENT");
		producerTemplate.sendBody("activemq:my.queue",xmlBody);
		LOGGER.info("THIRD MESSAGE SENT");
		producerTemplate.sendBody("activemq:my.queue.executor.service",xmlBody);
		LOGGER.info("FOURTH MESSAGE SENT");
		producerTemplate.sendBody("direct:start",body);
		LOGGER.info("FIFTH MESSAGE SENT");
		producerTemplate.sendBody("direct:body",body);
		LOGGER.info("SIXTH MESSAGE SENT");
		String user = "Marco,Luca,Paolo,Antonio";
		producerTemplate.sendBodyAndHeader("direct:message", body, "user", user);
		body = body.replace("\n", "@");
		producerTemplate.sendBody("direct:start2",body);
		//producerTemplate.sendBodyAndHeader("direct:start", "Hello World", "recipientList", "seda:a,seda:b,seda:c");
		//LOGGER.info("FIRST MESSAGE SENT");
		//Object response = producerTemplate.requestBodyAndHeader("direct:start", "Hello World", "recipientList", "direct:a,direct:b,direct:c,xxx:d",String.class);
		//LOGGER.info("SECOND MESSAGE SENT");
		//LOGGER.info(String.format("RESPONSE ----------> %s",response));
		try {
			main.stop();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		LOGGER.info("MAIN STOPPED");
		
	}
	
}
