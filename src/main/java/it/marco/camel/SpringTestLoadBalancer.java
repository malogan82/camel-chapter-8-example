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

public class SpringTestLoadBalancer {
	
	public static Logger LOGGER = LoggerFactory.getLogger(SpringTestLoadBalancer.class);
	
	public static void main(String[] args) {
		AbstractXmlApplicationContext appContext = new ClassPathXmlApplicationContext("camel-context-load-balancer.xml");
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
			producerTemplate.sendBody("direct:start","TEST1");
			producerTemplate.sendBody("direct:start","TEST2");
			producerTemplate.sendBody("direct:start","TEST3");
			producerTemplate.sendBody("direct:start","TEST4");
			producerTemplate.sendBody("direct:start-random","TEST1");
			producerTemplate.sendBody("direct:start-random","TEST2");
			producerTemplate.sendBody("direct:start-random","TEST3");
			producerTemplate.sendBody("direct:start-random","TEST4");
			try {
				Thread.sleep(10000);
				main.stop();
			} catch (Exception e) {
				LOGGER.error(e.getMessage(),e);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
	}

}