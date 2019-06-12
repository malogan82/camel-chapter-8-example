package it.marco.camel.route.builder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.XPathBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.marco.camel.Test;
import it.marco.camel.pojo.MyOrderStrategy;

public class MySplitRouteBuilder extends RouteBuilder {
	
	public static Logger LOGGER = LoggerFactory.getLogger(MySplitRouteBuilder.class);

	@Override
	public void configure() throws Exception {
		XPathBuilder xPathBuilder = new XPathBuilder("//foo/bar/text()");
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 16, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
		from("activemq:my.queue.executor.service")
		  .split(xPathBuilder)
		  .parallelProcessing()
		  .executorService(threadPoolExecutor)
		  .to("activemq:my.parts.executor.service");
		
		from("activemq:my.queue")
			.split(xPathBuilder)
			.parallelProcessing()
			.to("activemq:my.parts");
		
		from("activemq:my.parts.executor.service")
			.log("from activemq:my.parts.executor.service -> ${body}");
		
		from("activemq:my.parts")
			.log("from activemq:my.parts -> ${body}");
		
		from("seda:a")
			.split(bodyAs(String.class).tokenize("\n"))
			.to("seda:b");
		
		from("seda:b")
			.log("${body}");
		
		from("seda:c")
		  .split(xpath("//foo/bar/text()"))
		  .to("seda:d");
		
		from("seda:d")
			.log("${body}");
		
		from("file:C:\\workspaceApacheCamelCookBook\\camel-chapter-8-example\\inbox\\split?noop=true")
	    	.split().tokenize("\n", 10).streaming()
	        .to("activemq:queue:order");
		
		from("activemq:queue:order")
			.log("from activemq:queue:order ------> ${body}");
		
		from("direct:start")
		 	.split().tokenize("\n", 3, true).streaming()
		    .to("direct:group");
		
		from("direct:group")
			.log("from direct:group ----------> ${body}");
		
		from("direct:body")
			.split()
			.method("mySplitterBean", "splitBody")
			.to("direct:result");
		
		from("direct:message")
			.split()
			.method("mySplitterBean", "splitMessage")
			.to("direct:result");
		
		from("direct:result")
			.log("from direct:result ----------> ${header.user} - ${body}");
		
		from("direct:start2")
			.split(body().tokenize("@"), new MyOrderStrategy())
				.to("bean:MyOrderService?method=handleOrder")
			.end()
			.to("bean:MyOrderService?method=buildCombinedResponse");
		
		from("direct:streaming")
		  .split(body().tokenize(","), new MyOrderStrategy())
		    .parallelProcessing()
		    .streaming()
		    .to("activemq:my.parts")
		  .end()
		  .to("activemq:all.parts");
		
		from("activemq:my.parts")
			.log("from activemq:my.parts ----------> ${body}");
		
		from("activemq:all.parts")
			.log("from activemq:all.parts ----------> ${body}");

	}

}
