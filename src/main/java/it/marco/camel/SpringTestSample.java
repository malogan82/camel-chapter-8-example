package it.marco.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import it.marco.camel.runnable.MyRunnable;

public class SpringTestSample {
	
	public static Logger LOGGER = LoggerFactory.getLogger(SpringTestSample.class);

	public static void main(String[] args) {
		AbstractXmlApplicationContext appContext = new ClassPathXmlApplicationContext("camel-context-sample.xml");
		try {
			CamelContext camelContext = SpringCamelContext.springCamelContext(appContext);
			Main main = new Main();
			main.getCamelContexts().add(camelContext);
			MyRunnable runnable = new MyRunnable(main);
			Thread thread = new Thread(runnable);
			thread.run();
			while(!main.isStarted()) {
				if(main.isStarted()) {
					break;
				}
			}
			LOGGER.info("MAIN STARTED");
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
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		
	}
	
}
