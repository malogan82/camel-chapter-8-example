package it.marco.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.marco.camel.route.builder.MyRoutingSlipRouteBuilder;
import it.marco.camel.runnable.MyRunnable;

public class TestRoutingSlip {
	
	public static Logger LOGGER = LoggerFactory.getLogger(TestRoutingSlip.class);

	public static void main(String[] args) {
		Main main = new Main();
		main.addRouteBuilder(new MyRoutingSlipRouteBuilder());
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
		
	}
	
}
