/**
 * 
 */
package com.sorc.content.elasticsearch.video.sort;

import com.sorc.content.core.sort.SortingMode;
import com.sorc.content.elasticsearch.core.sort.IElasticSearchSorting;

/**
 * @author rakesh.moradiya
 *
 */
public class ElasticSearchVideoSorting implements IElasticSearchSorting{

	private String fieldName;
	private SortingMode mode;
	
	public ElasticSearchVideoSorting(String fieldName, SortingMode mode) {
		this.fieldName = fieldName;
		this.mode = mode;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Override
	public String getFieldName() {
		return this.fieldName;
	}

	public SortingMode getMode() {
		return mode;
	}

	public void setMode(SortingMode mode) {
		this.mode = mode;
	}

}