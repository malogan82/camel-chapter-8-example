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

public class SpringTest {
	
	public static Logger LOGGER = LoggerFactory.getLogger(SpringTest.class);
	
	public static void main(String[] args) {
		AbstractXmlApplicationContext appContext = new ClassPathXmlApplicationContext("camel-context.xml");
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
			String body = "first message\nsecond message \nthird message\nfourth message";
			producerTemplate.sendBody("seda:a",body);
			LOGGER.info("FIRST MESSAGE SENT");
			String xmlBody = "<foos><foo><bar>First Message</bar></foo><foo><bar>Second Message</bar></foo><foo><bar>Third Message</bar></foo></foos>";
			producerTemplate.sendBody("seda:c",xmlBody);
			LOGGER.info("SECOND MESSAGE SENT");
			producerTemplate.sendBody("direct:parallel-custom-pool",xmlBody);
			LOGGER.info("THIRD MESSAGE SENT");
			producerTemplate.sendBody("direct:start",body);
			LOGGER.info("FOURTH MESSAGE SENT");
			String orders = "<orders>"
					+ "<order><name>Marco</name><surname>Longobardi</surname><amount>50</amount></order>"
			        + "<order><name>Marco</name><surname>Carletti</surname><amount>100</amount></order>"
					+ "<order><name>Nevia</name><surname>Roscigno</surname><amount>150</amount></order>"
			        +"</orders>";
			producerTemplate.sendBody("direct:inbox",orders);
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
