/**
 * 
 */
package com.sorc.content.video.filter.input;

import java.util.Set;

import com.sorc.content.core.filter.IFilter;
import com.sorc.content.video.dao.data.ElasticSearchVideo;

/**
 * @author rakesh.moradiya
 *
 */
public class ElasticSearchVideoFilter implements IFilter<ElasticSearchVideo, String>{

	private Set<Integer> websiteIds;
	private String mainCategory;
	private Set<String> mainCategoryNotIn;
	private Integer videoDuration;
	private String countryCode;
	private String videoId;
	private String status;
	private String text;
	private String showName;
	private String seasonName;
	private Integer showId;
	private Integer seasonId;
	private Integer episodeNum;
	private String showCategory;
	private Integer seasonNum;
	private Set<String> tagsIn;
	private Set<String> assetIn;
	private String mediaType;
	private Boolean isSlider;
	private Boolean isLiveEvent;
	private String startDate;
	private String sortDate;
	private Boolean isHls;
	private String sliderTemplate;
	private String episodeStartDateRange;
	private String episodeEndDateRange;
	private Set<String> liveStatusIn;
	private String playlistId;
	private Boolean isFree;
	
	public Set<Integer> getWebsiteIds() {
		return websiteIds;
	}

	public void setWebsiteIds(Set<Integer> websiteIds) {
		this.websiteIds = websiteIds;
	}

	public String getMainCategory() {
		return mainCategory;
	}

	public void setMainCategory(String mainCategory) {
		this.mainCategory = mainCategory;
	}

	public Set<String> getMainCategoryNotIn() {
		return mainCategoryNotIn;
	}

	public void setMainCategoryNotIn(Set<String> mainCategoryNotIn) {
		this.mainCategoryNotIn = mainCategoryNotIn;
	}

	public Integer getVideoDuration() {
		return videoDuration;
	}

	public void setVideoDuration(Integer videoDuration) {
		this.videoDuration = videoDuration;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getSeasonName() {
		return seasonName;
	}

	public void setSeasonName(String seasonName) {
		this.seasonName = seasonName;
	}

	public Integer getShowId() {
		return showId;
	}

	public void setShowId(Integer showId) {
		this.showId = showId;
	}

	public Integer getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(Integer seasonId) {
		this.seasonId = seasonId;
	}

	public Integer getEpisodeNum() {
		return episodeNum;
	}

	public void setEpisodeNum(Integer episodeNum) {
		this.episodeNum = episodeNum;
	}

	public String getShowCategory() {
		return showCategory;
	}

	public void setShowCategory(String showCategory) {
		this.showCategory = showCategory;
	}

	public Integer getSeasonNum() {
		return seasonNum;
	}

	public Set<String> getTagsIn() {
		return tagsIn;
	}

	public void setTagsIn(Set<String> tagsIn) {
		this.tagsIn = tagsIn;
	}

	public void setSeasonNum(Integer seasonNum) {
		this.seasonNum = seasonNum;
	}

	public Set<String> getAssetIn() {
		return assetIn;
	}

	public void setAssetIn(Set<String> assetIn) {
		this.assetIn = assetIn;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public Boolean getIsSlider() {
		return isSlider;
	}

	public void setIsSlider(Boolean isSlider) {
		this.isSlider = isSlider;
	}

	public Boolean getIsLiveEvent() {
		return isLiveEvent;
	}

	public void setIsLiveEvent(Boolean isLiveEvent) {
		this.isLiveEvent = isLiveEvent;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getSortDate() {
		return sortDate;
	}

	public void setSortDate(String sortDate) {
		this.sortDate = sortDate;
	}

	public Boolean getIsHls() {
		return isHls;
	}

	public void setIsHls(Boolean isHls) {
		this.isHls = isHls;
	}

	public String getSliderTemplate() {
		return sliderTemplate;
	}

	public void setSliderTemplate(String sliderTemplate) {
		this.sliderTemplate = sliderTemplate;
	}

	public String getEpisodeStartDateRange() {
		return episodeStartDateRange;
	}

	public void setEpisodeStartDateRange(String episodeStartDateRange) {
		this.episodeStartDateRange = episodeStartDateRange;
	}

	public String getEpisodeEndDateRange() {
		return episodeEndDateRange;
	}

	public void setEpisodeEndDateRange(String episodeEndDateRange) {
		this.episodeEndDateRange = episodeEndDateRange;
	}

	public Set<String> getLiveStatusIn() {
		return liveStatusIn;
	}

	public void setLiveStatusIn(Set<String> liveStatusIn) {
		this.liveStatusIn = liveStatusIn;
	}

	public String getPlaylistId() {
		return playlistId;
	}

	public void setPlaylistId(String playlistId) {
		this.playlistId = playlistId;
	}

	public Boolean getIsFree() {
		return isFree;
	}

	public void setIsFree(Boolean isFree) {
		this.isFree = isFree;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assetIn == null) ? 0 : assetIn.hashCode());
		result = prime * result
				+ ((countryCode == null) ? 0 : countryCode.hashCode());
		result = prime
				* result
				+ ((episodeEndDateRange == null) ? 0 : episodeEndDateRange
						.hashCode());
		result = prime * result
				+ ((episodeNum == null) ? 0 : episodeNum.hashCode());
		result = prime
				* result
				+ ((episodeStartDateRange == null) ? 0 : episodeStartDateRange
						.hashCode());
		result = prime * result + ((isFree == null) ? 0 : isFree.hashCode());
		result = prime * result + ((isHls == null) ? 0 : isHls.hashCode());
		result = prime * result
				+ ((isLiveEvent == null) ? 0 : isLiveEvent.hashCode());
		result = prime * result
				+ ((isSlider == null) ? 0 : isSlider.hashCode());
		result = prime * result
				+ ((liveStatusIn == null) ? 0 : liveStatusIn.hashCode());
		result = prime * result
				+ ((mainCategory == null) ? 0 : mainCategory.hashCode());
		result = prime
				* result
				+ ((mainCategoryNotIn == null) ? 0 : mainCategoryNotIn
						.hashCode());
		result = prime * result
				+ ((mediaType == null) ? 0 : mediaType.hashCode());
		result = prime * result
				+ ((playlistId == null) ? 0 : playlistId.hashCode());
		result = prime * result
				+ ((seasonId == null) ? 0 : seasonId.hashCode());
		result = prime * result
				+ ((seasonName == null) ? 0 : seasonName.hashCode());
		result = prime * result
				+ ((seasonNum == null) ? 0 : seasonNum.hashCode());
		result = prime * result
				+ ((showCategory == null) ? 0 : showCategory.hashCode());
		result = prime * result + ((showId == null) ? 0 : showId.hashCode());
		result = prime * result
				+ ((showName == null) ? 0 : showName.hashCode());
		result = prime * result
				+ ((sliderTemplate == null) ? 0 : sliderTemplate.hashCode());
		result = prime * result
				+ ((sortDate == null) ? 0 : sortDate.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tagsIn == null) ? 0 : tagsIn.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result
				+ ((videoDuration == null) ? 0 : videoDuration.hashCode());
		result = prime * result + ((videoId == null) ? 0 : videoId.hashCode());
		result = prime * result
				+ ((websiteIds == null) ? 0 : websiteIds.hashCode());
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
		ElasticSearchVideoFilter other = (ElasticSearchVideoFilter) obj;
		if (assetIn == null) {
			if (other.assetIn != null)
				return false;
		} else if (!assetIn.equals(other.assetIn))
			return false;
		if (countryCode == null) {
			if (other.countryCode != null)
				return false;
		} else if (!countryCode.equals(other.countryCode))
			return false;
		if (episodeEndDateRange == null) {
			if (other.episodeEndDateRange != null)
				return false;
		} else if (!episodeEndDateRange.equals(other.episodeEndDateRange))
			return false;
		if (episodeNum == null) {
			if (other.episodeNum != null)
				return false;
		} else if (!episodeNum.equals(other.episodeNum))
			return false;
		if (episodeStartDateRange == null) {
			if (other.episodeStartDateRange != null)
				return false;
		} else if (!episodeStartDateRange.equals(other.episodeStartDateRange))
			return false;
		if (isFree == null) {
			if (other.isFree != null)
				return false;
		} else if (!isFree.equals(other.isFree))
			return false;
		if (isHls == null) {
			if (other.isHls != null)
				return false;
		} else if (!isHls.equals(other.isHls))
			return false;
		if (isLiveEvent == null) {
			if (other.isLiveEvent != null)
				return false;
		} else if (!isLiveEvent.equals(other.isLiveEvent))
			return false;
		if (isSlider == null) {
			if (other.isSlider != null)
				return false;
		} else if (!isSlider.equals(other.isSlider))
			return false;
		if (liveStatusIn == null) {
			if (other.liveStatusIn != null)
				return false;
		} else if (!liveStatusIn.equals(other.liveStatusIn))
			return false;
		if (mainCategory == null) {
			if (other.mainCategory != null)
				return false;
		} else if (!mainCategory.equals(other.mainCategory))
			return false;
		if (mainCategoryNotIn == null) {
			if (other.mainCategoryNotIn != null)
				return false;
		} else if (!mainCategoryNotIn.equals(other.mainCategoryNotIn))
			return false;
		if (mediaType == null) {
			if (other.mediaType != null)
				return false;
		} else if (!mediaType.equals(other.mediaType))
			return false;
		if (playlistId == null) {
			if (other.playlistId != null)
				return false;
		} else if (!playlistId.equals(other.playlistId))
			return false;
		if (seasonId == null) {
			if (other.seasonId != null)
				return false;
		} else if (!seasonId.equals(other.seasonId))
			return false;
		if (seasonName == null) {
			if (other.seasonName != null)
				return false;
		} else if (!seasonName.equals(other.seasonName))
			return false;
		if (seasonNum == null) {
			if (other.seasonNum != null)
				return false;
		} else if (!seasonNum.equals(other.seasonNum))
			return false;
		if (showCategory == null) {
			if (other.showCategory != null)
				return false;
		} else if (!showCategory.equals(other.showCategory))
			return false;
		if (showId == null) {
			if (other.showId != null)
				return false;
		} else if (!showId.equals(other.showId))
			return false;
		if (showName == null) {
			if (other.showName != null)
				return false;
		} else if (!showName.equals(other.showName))
			return false;
		if (sliderTemplate == null) {
			if (other.sliderTemplate != null)
				return false;
		} else if (!sliderTemplate.equals(other.sliderTemplate))
			return false;
		if (sortDate == null) {
			if (other.sortDate != null)
				return false;
		} else if (!sortDate.equals(other.sortDate))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (tagsIn == null) {
			if (other.tagsIn != null)
				return false;
		} else if (!tagsIn.equals(other.tagsIn))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (videoDuration == null) {
			if (other.videoDuration != null)
				return false;
		} else if (!videoDuration.equals(other.videoDuration))
			return false;
		if (videoId == null) {
			if (other.videoId != null)
				return false;
		} else if (!videoId.equals(other.videoId))
			return false;
		if (websiteIds == null) {
			if (other.websiteIds != null)
				return false;
		} else if (!websiteIds.equals(other.websiteIds))
			return false;
		return true;
	}
	
}
