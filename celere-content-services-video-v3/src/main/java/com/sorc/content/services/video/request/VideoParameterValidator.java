/**
 * 
 */
package com.sorc.content.services.video.request;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.elasticsearch.index.query.BoolQueryBuilder;

import com.sorc.content.elasticsearch.video.filter.input.ElasticSearchVideoFilterQueryBuilder;
import com.sorc.content.services.video.documentation.constants.VideoConstants;
import com.sorc.content.video.filter.input.ElasticSearchVideoFilter;

/**
 * @author rakesh.moradiya
 *
 */
public class VideoParameterValidator {

	public static BoolQueryBuilder validateCustomParameters(Set<Integer> websiteIds, String mainCategory, Set<String> mainCategoryNotIn, Integer videoDuration,
			String countryCode, String videoId, String status, String text,
			Integer showCategoryId, Integer seasonCategoryId, String showName, String seasonName, 
			Integer episodeNum, String showCategory, Integer seasonNo,
			Set<String> tagsIn, Set<String> assetIn, String mediaType, Boolean isSlider,
			Boolean isLiveEvent, String startDate, String sortDate, Boolean isHls,
			String sliderTemplate, String episodeStartDateRange, String episodeEndDateRange, Set<String> liveStatusIn,
			String playlistId) {
		ElasticSearchVideoFilter filter = new ElasticSearchVideoFilter();
		
		if(websiteIds != null && !websiteIds.isEmpty())
			filter.setWebsiteIds(websiteIds);
		
		if(mainCategory != null)
		{
			filter.setMainCategory(mainCategory);			
		}
		
		if(liveStatusIn != null && !liveStatusIn.isEmpty())
			filter.setLiveStatusIn(liveStatusIn);		
		
		if(mainCategoryNotIn != null && !mainCategoryNotIn.isEmpty())
			filter.setMainCategoryNotIn(mainCategoryNotIn);
		
		if(videoDuration != null)
			filter.setVideoDuration(videoDuration);
		
		if(countryCode != null)
			filter.setCountryCode(countryCode);
		
		if(videoId != null)
			filter.setVideoId(videoId);
		
		if(status != null)
			filter.setStatus(status.trim().toUpperCase());
		
		if(text != null && text.trim().length() > 0)
			filter.setText(text);
		
		if(showCategoryId != null)
			filter.setShowId(showCategoryId);
		
		if(seasonCategoryId != null)
			filter.setSeasonId(seasonCategoryId);
		
		if(showName != null)
			filter.setShowName(showName);
		
		if(seasonName != null)
			filter.setSeasonName(seasonName);
		
		if(episodeNum != null)
			filter.setEpisodeNum(episodeNum);
		
		if(showCategory != null)
			filter.setShowCategory(showCategory);
		
		if(seasonNo != null)
			filter.setSeasonNum(seasonNo);
		
		if(tagsIn != null && !tagsIn.isEmpty()) {
			filter.setTagsIn(tagsIn);
		}
		
		if(assetIn != null && !assetIn.isEmpty()) {
			filter.setAssetIn(assetIn);
		}
		
		if(mediaType != null)
			filter.setMediaType(mediaType);
		
		if(isSlider != null)
			filter.setIsSlider(isSlider);
		
		if(isLiveEvent != null)
			filter.setIsLiveEvent(isLiveEvent);
		
		if(startDate != null)
			filter.setStartDate(startDate);
		
		if(sortDate != null)
			filter.setSortDate(sortDate);
		
		if(isHls != null && isHls)
			filter.setIsHls(true);
		
		if(sliderTemplate != null)
			filter.setSliderTemplate(sliderTemplate);
		
		if(episodeStartDateRange != null)
			filter.setEpisodeStartDateRange(episodeStartDateRange);
		
		if(episodeEndDateRange != null)
			filter.setEpisodeEndDateRange(episodeEndDateRange);
		
		if(playlistId != null)
			filter.setPlaylistId(playlistId);
		
		ElasticSearchVideoFilterQueryBuilder qb = new ElasticSearchVideoFilterQueryBuilder(filter);
		return qb.buildQuery();
	}
	
	@SuppressWarnings("unused")
	private static List<String> getSearchableTextList(String text)
	{
		List<String> textList = null;
		if(text != null)		
			textList = Arrays.asList(text.split(" "));			
		else
			return null;
		
		return textList;
	}
}
