package com.sorc.content.elasticsearch.core.dao;

import java.util.ArrayList;
import java.util.List;

public class ElasticSearchResult {
	private long totalCount;
	private int pageNumber;
	private List<Object> items = new ArrayList<>();
		
	public ElasticSearchResult() {
		super();
	}

	public ElasticSearchResult(long totalCount, List<Object> items) {
		super();
		this.totalCount = totalCount;
		this.items = items;
	}
	
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public List<Object> getItems() {
		return items;
	}
	public void setItems(List<Object> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "SearchResult [totalCount=" + totalCount + ", items=" + items + "]";
	}
	
	
}
