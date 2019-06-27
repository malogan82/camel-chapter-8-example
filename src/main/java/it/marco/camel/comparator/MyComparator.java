package it.marco.camel.comparator;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.processor.resequencer.ExpressionResultComparator;

public class MyComparator implements ExpressionResultComparator {

	@Override
	public boolean predecessor(Exchange o1, Exchange o2) {
		return compare(o1,o2)>0;
	}

	@Override
	public boolean successor(Exchange o1, Exchange o2) {
		return compare(o1,o2)<0;
	}

	@Override
	public boolean isValid(Exchange o1) {
		return o1.getIn().getHeader("seqnum") instanceof String;
	}

	@Override
	public int compare(Exchange o1, Exchange o2) {
		return o1.getIn().getHeader("seqnum",String.class).compareTo(o2.getIn().getHeader("seqnum",String.class));
	}

	@Override
	public void setExpression(Expression expression) {

	}

}
