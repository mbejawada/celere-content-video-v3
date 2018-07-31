package com.sorc.content.elasticsearch.core.dto;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;

import com.sorc.content.core.pagination.Pagination;
import com.sorc.content.elasticsearch.core.sort.IElasticSearchSorting;
import com.sorc.content.video.filter.input.CustomScriptFilter;

public class ElasticSearchFilterDataTransfer {

	private Pagination pagination;
	private String index;
	private String facets;
	private List<IElasticSearchSorting> sorting;
	private BoolQueryBuilder filters;
	private List<String> facetFields;
	private CustomScriptFilter scriptFilter;
	private List<String> additionalFacetColumns;
	private List<IElasticSearchSorting> aggDetailSorting;

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getFacets() {
		return facets;
	}

	public void setFacets(String facets) {
		this.facets = facets;
	}

	public List<IElasticSearchSorting> getSorting() {
		return sorting;
	}

	public void setSorting(List<IElasticSearchSorting> sorting) {
		this.sorting = sorting;
	}

	public BoolQueryBuilder getFilters() {
		return filters;
	}

	public void setFilters(BoolQueryBuilder filters) {
		this.filters = filters;
	}

	public List<String> getFacetFields() {
		return facetFields;
	}

	public void setFacetFields(List<String> facetFields) {
		this.facetFields = facetFields;
	}

	public CustomScriptFilter getScriptFilter() {
		return scriptFilter;
	}

	public List<String> getAdditionalFacetColumns() {
		return additionalFacetColumns;
	}

	public void setAdditionalFacetColumns(List<String> additionalFacetColumns) {
		this.additionalFacetColumns = additionalFacetColumns;
	}

	public List<IElasticSearchSorting> getAggDetailSorting() {
		return aggDetailSorting;
	}

	public void setAggDetailSorting(List<IElasticSearchSorting> aggDetailSorting) {
		this.aggDetailSorting = aggDetailSorting;
	}

	public void setScriptAssembler(CustomScriptFilter scriptFilter) {
		this.scriptFilter = scriptFilter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + 
				((facets == null) ? 0 : facets.hashCode());
		result = prime * result + 
				((index == null) ? 0 : index.hashCode());
		result = prime * result
				+ ((pagination == null) ? 0 : pagination.hashCode());
		result = prime * result + ((sorting == null) ? 0 : sorting.hashCode());
		result = prime * result + ((filters == null) ? 0 : filters.hashCode());
		result = prime * result + ((facetFields == null) ? 0 : facetFields.hashCode());
		result = prime * result + ((scriptFilter == null) ? 0 : scriptFilter.hashCode());
		result = prime * result + ((additionalFacetColumns == null) ? 0 : additionalFacetColumns.hashCode());
		result = prime * result + ((aggDetailSorting == null) ? 0 : aggDetailSorting.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElasticSearchFilterDataTransfer other = (ElasticSearchFilterDataTransfer) obj;
		if (facets == null) {
			if (other.facets != null)
				return false;
		} else if (!facets.equals(other.facets))
			return false;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		if (pagination == null) {
			if (other.pagination != null)
				return false;
		} else if (!pagination.equals(other.pagination))
			return false;
		if (sorting == null) {
			if (other.sorting != null)
				return false;
		} else if (!sorting.equals(other.sorting))
			return false;
		if (filters == null) {
			if (other.filters != null)
				return false;
		} else if (!filters.equals(other.filters))
			return false;
		if (facetFields == null) {
			if (other.facetFields != null)
				return false;
		} else if (!facetFields.equals(other.facetFields))
			return false;
		if (scriptFilter == null) {
			if (other.scriptFilter != null)
				return false;
		} else if (!scriptFilter.equals(other.scriptFilter))
			return false;
		if (additionalFacetColumns == null) {
			if (other.additionalFacetColumns != null)
				return false;
		} else if (!additionalFacetColumns.equals(other.additionalFacetColumns))
			return false;
		if (aggDetailSorting == null) {
			if (other.aggDetailSorting != null)
				return false;
		} else if (!aggDetailSorting.equals(other.aggDetailSorting))
			return false;
		return true;
	}
}
