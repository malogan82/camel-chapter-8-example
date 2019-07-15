package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;

public class MyServiceCallRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from("direct:start")
			.serviceCall("foo?beer=yes")
			.to("direct:mock-result");
		
		from("direct:start-context-path")
			.serviceCall("foo/beverage?beer=yes");
		
		from("direct:start-controlled-resolved-uri-1")
			.serviceCall("myService", "http:myService.host:myService.port/foo")
			.to("direct:mock-result");
		
		from("direct:start-controlled-resolved-uri-2")
			.serviceCall("myService", "netty4:tcp:myService?connectTimeout=1000")
			.to("direct:mock-result");
		
		from("direct:mock-result")
			.log("from direct:mock-result ----------> ${body}");

	}

}
