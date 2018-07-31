package com.sorc.content.elasticsearch.core.sort;

import com.sorc.content.core.sort.SortingMode;

public interface IElasticSearchSorting {

	public String getFieldName();
	public void setMode(SortingMode mode);
	public SortingMode getMode();
}
