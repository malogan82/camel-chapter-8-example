package it.marco.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.marco.camel.route.builder.MySampleRouteBuilder;
import it.marco.camel.runnable.MyRunnable;

public class TestSample {
	
	public static Logger LOGGER = LoggerFactory.getLogger(TestSample.class);

	public static void main(String[] args) {
		Main main = new Main();
		main.addRouteBuilder(new MySampleRouteBuilder());
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
		producerTemplate.sendBody("direct:sample","TEST1");
		producerTemplate.sendBody("direct:sample","TEST2");
		producerTemplate.sendBody("direct:sample","TEST3");
		producerTemplate.sendBody("direct:sample","TEST4");
		producerTemplate.sendBody("direct:sample","TEST5");
		producerTemplate.sendBody("direct:sample","TEST6");
		producerTemplate.sendBody("direct:sample","TEST7");
		producerTemplate.sendBody("direct:sample","TEST8");
		producerTemplate.sendBody("direct:sample","TEST9");
		producerTemplate.sendBody("direct:sample","TEST10");
		producerTemplate.sendBody("direct:sample-configured","TEST1");
		producerTemplate.sendBody("direct:sample-configured","TEST2");
		producerTemplate.sendBody("direct:sample-configured","TEST3");
		producerTemplate.sendBody("direct:sample-configured","TEST4");
		producerTemplate.sendBody("direct:sample-configured","TEST5");
		producerTemplate.sendBody("direct:sample-configured","TEST6");
		producerTemplate.sendBody("direct:sample-configured","TEST7");
		producerTemplate.sendBody("direct:sample-configured","TEST8");
		producerTemplate.sendBody("direct:sample-configured","TEST9");
		producerTemplate.sendBody("direct:sample-configured","TEST10");
		producerTemplate.sendBody("direct:sample-configured-via-dsl","TEST1");
		producerTemplate.sendBody("direct:sample-configured-via-dsl","TEST2");
		producerTemplate.sendBody("direct:sample-configured-via-dsl","TEST3");
		producerTemplate.sendBody("direct:sample-configured-via-dsl","TEST4");
		producerTemplate.sendBody("direct:sample-configured-via-dsl","TEST5");
		producerTemplate.sendBody("direct:sample-configured-via-dsl","TEST6");
		producerTemplate.sendBody("direct:sample-configured-via-dsl","TEST7");
		producerTemplate.sendBody("direct:sample-configured-via-dsl","TEST8");
		producerTemplate.sendBody("direct:sample-configured-via-dsl","TEST9");
		producerTemplate.sendBody("direct:sample-configured-via-dsl","TEST10");
		producerTemplate.sendBody("direct:sample-messageFrequency","TEST1");
		producerTemplate.sendBody("direct:sample-messageFrequency","TEST2");
		producerTemplate.sendBody("direct:sample-messageFrequency","TEST3");
		producerTemplate.sendBody("direct:sample-messageFrequency","TEST4");
		producerTemplate.sendBody("direct:sample-messageFrequency","TEST5");
		producerTemplate.sendBody("direct:sample-messageFrequency","TEST6");
		producerTemplate.sendBody("direct:sample-messageFrequency","TEST7");
		producerTemplate.sendBody("direct:sample-messageFrequency","TEST8");
		producerTemplate.sendBody("direct:sample-messageFrequency","TEST9");
		producerTemplate.sendBody("direct:sample-messageFrequency","TEST10");
		producerTemplate.sendBody("direct:sample-messageFrequency-via-dsl","TEST1");
		producerTemplate.sendBody("direct:sample-messageFrequency-via-dsl","TEST2");
		producerTemplate.sendBody("direct:sample-messageFrequency-via-dsl","TEST3");
		producerTemplate.sendBody("direct:sample-messageFrequency-via-dsl","TEST4");
		producerTemplate.sendBody("direct:sample-messageFrequency-via-dsl","TEST5");
		producerTemplate.sendBody("direct:sample-messageFrequency-via-dsl","TEST6");
		producerTemplate.sendBody("direct:sample-messageFrequency-via-dsl","TEST7");
		producerTemplate.sendBody("direct:sample-messageFrequency-via-dsl","TEST8");
		producerTemplate.sendBody("direct:sample-messageFrequency-via-dsl","TEST9");
		producerTemplate.sendBody("direct:sample-messageFrequency-via-dsl","TEST10");
		try {
			main.stop();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		LOGGER.info("MAIN STOPPED");
		
	}
	
}
