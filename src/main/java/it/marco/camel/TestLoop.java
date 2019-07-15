package it.marco.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.marco.camel.route.builder.MyLoopRouteBuilder;
import it.marco.camel.runnable.MyRunnable;

public class TestLoop {
	
	public static Logger LOGGER = LoggerFactory.getLogger(TestLoop.class);

	public static void main(String[] args) {
		Main main = new Main();
		main.addRouteBuilder(new MyLoopRouteBuilder());
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
		producerTemplate.sendBody("direct:start","TEST1");
		producerTemplate.sendBodyAndHeader("direct:start-header","TEST2","loop",9);
		String xmlBody = "<hello times=\"10\">TEST3</hello>";
		producerTemplate.sendBody("direct:start-xpath",xmlBody);
		producerTemplate.sendBody("direct:start-copy","TEST4");
		producerTemplate.sendBody("direct:start-no-copy","TEST5");
		producerTemplate.sendBody("direct:start-do-while","A");
		try {
			main.stop();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		LOGGER.info("MAIN STOPPED");
		
	}
	
}
