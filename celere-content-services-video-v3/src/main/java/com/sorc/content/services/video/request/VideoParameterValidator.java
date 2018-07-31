/**
 * 
 */
package com.sorc.content.services.video.request;

import java.util.Set;

import org.elasticsearch.index.query.BoolQueryBuilder;

import com.sorc.content.elasticsearch.video.filter.input.ElasticSearchVideoFilterQueryBuilder;
import com.sorc.content.video.filter.input.ElasticSearchVideoFilter;

/**
 * @author rakesh.moradiya
 *
 */
public class VideoParameterValidator {

	public static BoolQueryBuilder validateCustomParameters(Set<Integer> websiteIds) {
		ElasticSearchVideoFilter filter = new ElasticSearchVideoFilter();
		
		if(websiteIds != null && !websiteIds.isEmpty())
			filter.setWebsiteIds(websiteIds);
		
		ElasticSearchVideoFilterQueryBuilder qb = new ElasticSearchVideoFilterQueryBuilder(filter);
		return qb.buildQuery();
	}
}
