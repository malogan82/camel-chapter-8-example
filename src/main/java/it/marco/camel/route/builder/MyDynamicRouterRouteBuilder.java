package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;

import it.marco.camel.pojo.DynamicRouterTest;

public class MyDynamicRouterRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:start")
	    	.dynamicRouter(bean(DynamicRouterTest.class, "slip"));
		
		from("direct:mock-a")
			.log("from direct:mock-a ----------> ${body}");
		
		from("direct:mock-b")
			.log("from direct:mock-b ----------> ${body}");
		
		from("direct:mock-c")
			.log("from direct:mock-c ----------> ${body}");
		
		from("direct:mock-result")
			.log("from direct:mock-result ----------> ${body}");
		
		from("direct:foo")
			.log("from direct:foo ----------> ${body}");


	}

}
