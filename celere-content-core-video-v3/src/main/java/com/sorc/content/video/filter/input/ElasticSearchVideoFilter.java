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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mainCategory == null) ? 0 : mainCategory.hashCode());
		result = prime
				* result
				+ ((mainCategoryNotIn == null) ? 0 : mainCategoryNotIn
						.hashCode());
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
		if (websiteIds == null) {
			if (other.websiteIds != null)
				return false;
		} else if (!websiteIds.equals(other.websiteIds))
			return false;
		return true;
	}
	
}
