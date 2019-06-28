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

public class SpringTestRoutingSlip {
	
	public static Logger LOGGER = LoggerFactory.getLogger(SpringTestRoutingSlip.class);
	
	public static void main(String[] args) {
		AbstractXmlApplicationContext appContext = new ClassPathXmlApplicationContext("camel-context-routingslip.xml");
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
			String body = "TEST";
			String aRoutingSlipHeader = "direct:first,direct:second,direct:third";
			String aRoutingSlipHeaderCustomSeparator = "direct:first#direct:second#direct:third";
			//String response = producerTemplate.requestBodyAndHeader("direct:b",body,"aRoutingSlipHeader",aRoutingSlipHeader,String.class);
			String response = producerTemplate.requestBodyAndHeader("direct:c",body,"aRoutingSlipHeader",aRoutingSlipHeaderCustomSeparator,String.class);
			LOGGER.info(String.format("RESPONSE ----------> %s",response));
			try {
				Thread.sleep(10000);
				main.stop();
			} catch (Exception e) {
				LOGGER.error(e.getMessage(),e);
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage(),e);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
	}

}
