package com.sorc.content.elasticsearch.core.filter.input;

public class TermFilter implements IElasticSearchFilter {
	
	private String field;
	private Object term;
	
	public TermFilter(String field, Object term) {
		this.field = field;
		this.term = term;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Object getTerm() {
		return term;
	}
	public void setTerm(Object term) {
		this.term = term;
	}
}
