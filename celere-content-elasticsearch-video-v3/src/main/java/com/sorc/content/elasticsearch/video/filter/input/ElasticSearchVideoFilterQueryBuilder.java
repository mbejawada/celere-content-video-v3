/**
 * 
 */
package com.sorc.content.elasticsearch.video.filter.input;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.sorc.content.elasticsearch.core.filter.input.CustomFilterBuilder;
import com.sorc.content.elasticsearch.core.filter.input.IElasticSearchFilter;
import com.sorc.content.elasticsearch.core.filter.input.IElasticSearchQueryBuilder;
import com.sorc.content.elasticsearch.core.filter.input.TermsFilter;
import com.sorc.content.video.filter.input.ElasticSearchVideoFilter;

/**
 * @author rakesh.moradiya
 *
 */
public class ElasticSearchVideoFilterQueryBuilder implements IElasticSearchQueryBuilder, IElasticSearchFilter {

	private ElasticSearchVideoFilter filter;
	private BoolQueryBuilder boolQueryBuilder;
	private CustomFilterBuilder fb;
	
	public ElasticSearchVideoFilterQueryBuilder(ElasticSearchVideoFilter filter) {
		this.filter = filter;
		fb = new CustomFilterBuilder();
	}
	
	
	@Override
	public BoolQueryBuilder buildQuery() {	
		
		if (filter.getWebsiteIds() != null) {
			buildBoolQueryFilter(new TermsFilter("websiteIds", filter.getWebsiteIds()));
		}
		return boolQueryBuilder;
	}

	private void buildBoolQueryFilter(IElasticSearchFilter currFilter) {
		buildBoolQueryFilter(currFilter, false);
	}
	
	private void buildBoolQueryFilter(IElasticSearchFilter currFilter, boolean mustNotFilter) {
		if (boolQueryBuilder == null) {
			boolQueryBuilder = QueryBuilders.boolQuery();		
			if(mustNotFilter)
				boolQueryBuilder.mustNot(fb.createFilter(currFilter));
			else
				boolQueryBuilder.filter(fb.createFilter(currFilter));
		} else {
			if(mustNotFilter)
				boolQueryBuilder.mustNot(fb.createFilter(currFilter));
			else
				boolQueryBuilder.filter(fb.createFilter(currFilter));
		}
	}
}