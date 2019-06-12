package it.marco.camel.route.builder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.XPathBuilder;

public class MySplitRouteBuilder extends RouteBuilder {

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
		
		from("file:C:\\workspaceApacheCamelCookBook\\camel-chapter-8-example\\inbox\\split")
	    	.split().tokenize("\n", 10).streaming()
	        .to("activemq:queue:order");
		
		from("activemq:queue:order")
			.log("from activemq:queue:order ------> ${body}");

	}

}
