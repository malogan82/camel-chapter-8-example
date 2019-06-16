package it.marco.camel.processor;

import java.io.StringReader;

import javax.xml.xpath.XPathFactory;

import org.apache.camel.Exchange;
import org.apache.camel.language.XPath;
import org.apache.camel.processor.aggregate.UseOriginalAggregationStrategy;
import org.xml.sax.InputSource;

public class MyBeanProcessor {
	
	private UseOriginalAggregationStrategy useOriginalAggregationStrategy;
	
	public MyBeanProcessor(UseOriginalAggregationStrategy useOriginalAggregationStrategy) {
		this.useOriginalAggregationStrategy = useOriginalAggregationStrategy;
	}
	
	public void process(Exchange exchange, @XPath("/order/@number") String number) throws Exception {
		if(useOriginalAggregationStrategy.getOriginal()==null) {
			useOriginalAggregationStrategy.setOriginal(exchange);
		}else {
			javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
			String body = useOriginalAggregationStrategy.getOriginal().getIn().getBody(String.class);
			InputSource bodyXML = new InputSource(new StringReader(body));
			String result = xPath.evaluate("/order/@number", bodyXML);
	        if(!number.equals(result)) {
	        	useOriginalAggregationStrategy.setOriginal(exchange);
	        }
		}
	}

}
