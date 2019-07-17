package com.sorc.content.elasticsearch.core.filter.input;

public class DateRangeFilter implements IElasticSearchFilter {

	private String field;
	private String from;
	private String to;
	private boolean includeBoundry;

	public DateRangeFilter(String field, String from, String to, boolean includeBoundry) {
		this.field = field;
		this.to = to;
		this.from = from;
		this.includeBoundry = includeBoundry;
	}
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public boolean isIncludeBoundry() {
		return includeBoundry;
	}

	public void setIncludeBoundry(boolean includeBoundry) {
		this.includeBoundry = includeBoundry;
	}
}