package com.sorc.content.elasticsearch.core.filter.input;

public class ExistsFilter implements IElasticSearchFilter {

	private String field;
	
	public ExistsFilter(String field)
	{
		this.field = field;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
}
