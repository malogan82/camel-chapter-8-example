package it.marco.camel;

import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.marco.camel.route.builder.MyResequencerRouteBuilder;
import it.marco.camel.runnable.MyRunnable;

public class TestResequencer {
	
	public static Logger LOGGER = LoggerFactory.getLogger(TestResequencer.class);

	public static void main(String[] args) {
		Main main = new Main();
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
		producerTemplate.sendBodyAndHeader("direct:start","Marco","TimeStamp",1);
		producerTemplate.sendBodyAndHeader("direct:start","Antonio","TimeStamp",2);
		producerTemplate.sendBodyAndHeader("direct:start","Giuseppe","TimeStamp",5);
		producerTemplate.sendBodyAndHeader("direct:start","Vincenzo","TimeStamp",3);
		producerTemplate.sendBodyAndHeader("direct:start","Francesco","TimeStamp",4);
		try {
			Thread.sleep(10000);
			main.stop();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		
	}
	
}
