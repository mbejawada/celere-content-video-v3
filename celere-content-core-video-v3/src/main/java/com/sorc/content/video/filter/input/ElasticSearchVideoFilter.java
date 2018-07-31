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

	public Set<Integer> getWebsiteIds() {
		return websiteIds;
	}

	public void setWebsiteIds(Set<Integer> websiteIds) {
		this.websiteIds = websiteIds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (websiteIds == null) {
			if (other.websiteIds != null)
				return false;
		} else if (!websiteIds.equals(other.websiteIds))
			return false;
		return true;
	}
	
}
