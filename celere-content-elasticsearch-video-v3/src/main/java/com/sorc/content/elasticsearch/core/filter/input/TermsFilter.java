package com.sorc.content.elasticsearch.core.filter.input;

import java.util.Collection;

public class TermsFilter implements IElasticSearchFilter {
	
	private String field;
	private Collection<?> terms;
	
	public TermsFilter(String field, Collection<?> terms) {
		this.field = field;
		this.terms = terms;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Collection<?> getTerms() {
		return terms;
	}
	public void setTerm(Collection<?> terms) {
		this.terms = terms;
	}
}
