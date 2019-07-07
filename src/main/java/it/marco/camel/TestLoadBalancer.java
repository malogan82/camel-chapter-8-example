package it.marco.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.marco.camel.route.builder.MyLoadBalancerRouteBuilder;
import it.marco.camel.runnable.MyRunnable;

public class TestLoadBalancer {
	
	public static Logger LOGGER = LoggerFactory.getLogger(TestLoadBalancer.class);

	public static void main(String[] args) {
		Main main = new Main();
		main.addRouteBuilder(new MyLoadBalancerRouteBuilder());
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
//		producerTemplate.sendBody("direct:start","TEST1");
//		producerTemplate.sendBody("direct:start","TEST2");
//		producerTemplate.sendBody("direct:start","TEST3");
//		producerTemplate.sendBody("direct:start","TEST4");
//		producerTemplate.sendBody("direct:start-random","TEST1");
//		producerTemplate.sendBody("direct:start-random","TEST2");
//		producerTemplate.sendBody("direct:start-random","TEST3");
//		producerTemplate.sendBody("direct:start-random","TEST4");
//		producerTemplate.sendBodyAndHeader("direct:start-sticky", "TEST1", "username", "Marco");
//		producerTemplate.sendBodyAndHeader("direct:start-sticky", "TEST2", "username", "Francesco");
//		producerTemplate.sendBodyAndHeader("direct:start-sticky", "TEST3", "username", "Antonio");
//		producerTemplate.sendBodyAndHeader("direct:start-sticky", "TEST4", "username", "Antonio");
//		producerTemplate.sendBodyAndHeader("direct:start-sticky", "TEST5", "username", "Marco");
//		producerTemplate.sendBodyAndHeader("direct:start-sticky", "TEST6", "username", "Francesco");
//		producerTemplate.sendBody("direct:start-topic","CICCIO PASTICCIO");
//		producerTemplate.sendBody("direct:start-failover","TEST1");
//		producerTemplate.sendBody("direct:start-failover-no-error-handler","TEST1");
//		producerTemplate.sendBody("direct:start-failover-no-exception","TEST2");
//		producerTemplate.sendBody("direct:foo","TEST3");
//		producerTemplate.sendBody("direct:start-failover-no-exception-roundrobin","TEST1");
//		producerTemplate.sendBody("direct:start-weighted","TEST1");
//		producerTemplate.sendBody("direct:start-weighted-default-delimiter","TEST1");
//		producerTemplate.sendBody("direct:start-custom-load-balancer","x");
		producerTemplate.sendBody("direct:start-circuit-breaker","TEST1");
		try {
			Thread.sleep(10000);
			main.stop();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		
	}
	
}
