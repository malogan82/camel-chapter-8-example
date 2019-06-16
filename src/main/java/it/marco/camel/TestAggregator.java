package it.marco.camel;

import java.util.HashMap;
import java.util.Map;

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
//		producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE1", "id", "A");
//		producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE2", "id", "A");
//		producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE1", "id", "B");
//		producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE2", "id", "B");
//		producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE3", "id", "A");
//		producerTemplate.sendBodyAndHeader("direct:aggregate","MESSAGE3", "id", "B");
		String order1 = "<order number=\"1\"><name>Marco</name><surname>Longobardi</surname><amount>50</amount></order>";
		String order2 = "<order number=\"2\"><name>Marco</name><surname>Carletti</surname><amount>100</amount></order>";
		String order3 = "<order number=\"2\"><name>Nevia</name><surname>Roscigno</surname><amount>150</amount></order>";
		producerTemplate.sendBody("direct:aggregateXPath",order1);
		producerTemplate.sendBody("direct:aggregateXPath",order2);
		producerTemplate.sendBody("direct:aggregateXPath",order3);
//		Map<String,Object> headers = new HashMap<>();
//		headers.put("MsgType","OK");
//		headers.put("mySize", 3);
//		producerTemplate.sendBodyAndHeaders("direct:aggregateXPath",order1,headers);
//		headers.put("MsgType","ALERT");	
//		producerTemplate.sendBodyAndHeaders("direct:aggregateXPath",order2,headers);
//		headers.put("MsgType","OK");
//		producerTemplate.sendBodyAndHeaders("direct:aggregateXPath",order3,headers);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
	
}
