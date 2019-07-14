package it.marco.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.marco.camel.route.builder.MyDynamicRouterRouteBuilder;
import it.marco.camel.route.builder.MyLoopRouteBuilder;
import it.marco.camel.runnable.MyRunnable;

public class TestDynamicRouter {
	
	public static Logger LOGGER = LoggerFactory.getLogger(TestDynamicRouter.class);

	public static void main(String[] args) {
		Main main = new Main();
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
		producerTemplate.sendBody("direct:start","TEST");
		try {
			main.stop();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		LOGGER.info("MAIN STOPPED");
		
	}
	
}
