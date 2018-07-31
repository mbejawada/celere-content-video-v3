package com.sorc.content.elasticsearch.core.filter.input;

public class NotInFilter implements IElasticSearchFilter {
	
	private Object term;
	
	public NotInFilter(Object term) {
		this.term = term;
	}
	public Object getTerm() {
		return term;
	}
	public void setTerm(Object term) {
		this.term = term;
	}
}
