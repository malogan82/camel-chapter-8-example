package it.marco.camel.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;

public class MySplitterBean {
	
	private CamelContext camelContext;
	
	public MySplitterBean(CamelContext camelContext) {
		this.camelContext = camelContext;
	}
	
	public List<String> splitBody(String body){
		String[] bodySplitted = body.split("\n");
		return Arrays.asList(bodySplitted);
	}
	
	public List<Message> splitMessage(@Header(value = "user") String header, @Body String body) {
        List<Message> answer = new ArrayList<Message>();
        String[] parts = header.split(",");
        for (String part : parts) {
            DefaultMessage message = new DefaultMessage(camelContext);
            message.setHeader("user", part);
            message.setBody(body);
            answer.add(message);
        }
        return answer;
    }

}
