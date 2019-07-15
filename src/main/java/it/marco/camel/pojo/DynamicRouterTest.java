package it.marco.camel.pojo;

import java.util.ArrayList;
import java.util.List;

public class DynamicRouterTest {
	
	private int invoked;
	
	private List<String> bodies = new ArrayList<>();
	
	public String slip(String body) {
	    bodies.add(body);
	    invoked++;
	    if (invoked == 1) {
	        return "direct:mock-a";
	    } else if (invoked == 2) {
	        return "direct:mock-b,direct:mock-c";
	    } else if (invoked == 3) {
	        return "direct:foo";
	    } else if (invoked == 4) {
	        return "direct:mock-result";
	    }
	    return null;
	}

}
