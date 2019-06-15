package it.marco.camel.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyExceptionHandler {
	
	public static Logger LOGGER = LoggerFactory.getLogger(MyExceptionHandler.class);
	
	public void messageFailed(String body) {
		LOGGER.error(body);
	}

}
