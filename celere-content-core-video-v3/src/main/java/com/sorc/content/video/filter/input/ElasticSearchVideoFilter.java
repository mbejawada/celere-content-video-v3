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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((countryCode == null) ? 0 : countryCode.hashCode());
		result = prime * result
				+ ((mainCategory == null) ? 0 : mainCategory.hashCode());
		result = prime
				* result
				+ ((mainCategoryNotIn == null) ? 0 : mainCategoryNotIn
						.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		if (countryCode == null) {
			if (other.countryCode != null)
				return false;
		} else if (!countryCode.equals(other.countryCode))
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
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
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
