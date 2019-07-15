package it.marco.camel.pojo;

import org.apache.camel.Consume;
import org.apache.camel.DynamicRouter;
import org.apache.camel.language.XPath;

public class MyDynamicRouter {
	
	@Consume(uri = "activemq:foo")
    @DynamicRouter
    public String route(@XPath("/customer/id") String customerId) {
        if("endpoint1".equals(customerId)) {
        	return "direct:mock-a";
        }else if("endpoint2".equals(customerId)) {
        	return "direct:mock-b";
        }else if("endpoint3".equals(customerId)) {
        	return "direct:mock-c";
        }else {
        	return null;
        }
    }
}

