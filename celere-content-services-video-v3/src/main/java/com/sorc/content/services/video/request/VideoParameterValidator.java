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

	public static BoolQueryBuilder validateCustomParameters(Set<Integer> websiteIds, String mainCategory, Set<String> mainCategoryNotIn, Integer videoDuration,
			String countryCode, String videoId) {
		ElasticSearchVideoFilter filter = new ElasticSearchVideoFilter();
		
		if(websiteIds != null && !websiteIds.isEmpty())
			filter.setWebsiteIds(websiteIds);
		
		if(mainCategory != null)
			filter.setMainCategory(mainCategory);
		
		if(mainCategoryNotIn != null && !mainCategoryNotIn.isEmpty())
			filter.setMainCategoryNotIn(mainCategoryNotIn);
		
		if(videoDuration != null)
			filter.setVideoDuration(videoDuration);
		
		if(countryCode != null)
			filter.setCountryCode(countryCode);
		
		if(videoId != null)
			filter.setVideoId(videoId);
		
		ElasticSearchVideoFilterQueryBuilder qb = new ElasticSearchVideoFilterQueryBuilder(filter);
		return qb.buildQuery();
	}
}
