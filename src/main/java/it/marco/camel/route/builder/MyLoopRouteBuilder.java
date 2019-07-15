package it.marco.camel.route.builder;

import org.apache.camel.builder.RouteBuilder;

public class MyLoopRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from("direct:start")
			.loop(8)
			.to("direct:mock-result");
		
		from("direct:start-header")
			.loop(header("loop"))
			.to("direct:mock-result");
		
		from("direct:start-copy")
			 .loop(3).copy()
		         .transform(body().append("B"))
		         .to("direct:mock-loop")
		     .end()
		     .to("direct:mock-result");
		
		from("direct:start-no-copy")
			 .loop(3)
		         .transform(body().append("B"))
		         .to("direct:mock-loop")
		     .end()
		     .to("direct:mock-result");
		
		from("direct:start-do-while")
		    .loopDoWhile(simple("${body.length} <= 5"))
		        .to("direct:mock-loop")
		        .transform(body().append("A"))
		    .end()
		    .to("direct:mock-result");
		
		from("direct:start-xpath")
			.loop()
			.xpath("/hello/@times")
			.to("direct:mock-result");
		
		from("direct:mock-loop")
			.log("from direct:mock-loop ----------> ${body}");
			
		from("direct:mock-result")
			.log("from direct:mock-result ----------> ${body}");

	}

}
