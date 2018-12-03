package com.sorc.content.video.dao.data;

import java.io.Serializable;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wordnik.swagger.annotations.ApiModel;

/**
 * @author rakesh.moradiya
 *
 */
@ApiModel(value = "Video Description")
@XmlRootElement(name = "Video")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
public class Video implements Serializable {

	private static final long serialVersionUID = 1608674604602879291L;

	public Video(){}
	
	private String dataUrl;
	private Integer width;
	private Integer height;
	private Integer duration;
	private Integer msDuration;
	private String minDuration;
	private String vmsId;
	private String name;
	private String slug;
	private String description;
	private String webThumbnailUrl;
	private String appleUmcThumbnailUrl;
	private String iOSThumbnailUrl;
	private String androidThumbnailUrl;
	private String rokuThumbnailUrl;
	private String startDate;
	private String endDate;
	private String referenceId;
	private Integer plays;
	private Integer views;
	private String lastPlayedAt;
	private Double rank;
	private Integer totalRank;
	private Integer votes;
	private String hlsUrl;
	private String mpegdashUrl;

	public String getDataUrl() {
		return dataUrl;
	}
	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Integer getMsDuration() {
		return msDuration;
	}
	public void setMsDuration(Integer msDuration) {
		this.msDuration = msDuration;
	}
	public String getMinDuration() {
		return minDuration;
	}
	public void setMinDuration(String minDuration) {
		this.minDuration = minDuration;
	}
	public String getVmsId() {
		return vmsId;
	}
	public void setVmsId(String vmsId) {
		this.vmsId = vmsId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getWebThumbnailUrl() {
		return webThumbnailUrl;
	}
	public void setWebThumbnailUrl(String webThumbnailUrl) {
		this.webThumbnailUrl = webThumbnailUrl;
	}
	public String getAppleUmcThumbnailUrl() {
		return appleUmcThumbnailUrl;
	}
	public void setAppleUmcThumbnailUrl(String appleUmcThumbnailUrl) {
		this.appleUmcThumbnailUrl = appleUmcThumbnailUrl;
	}
	public String getIOSThumbnailUrl() {
		return iOSThumbnailUrl;
	}
	public void setIOSThumbnailUrl(String iOSThumbnailUrl) {
		this.iOSThumbnailUrl = iOSThumbnailUrl;
	}
	public String getAndroidThumbnailUrl() {
		return androidThumbnailUrl;
	}
	public void setAndroidThumbnailUrl(String androidThumbnailUrl) {
		this.androidThumbnailUrl = androidThumbnailUrl;
	}
	public String getRokuThumbnailUrl() {
		return rokuThumbnailUrl;
	}
	public void setRokuThumbnailUrl(String rokuThumbnailUrl) {
		this.rokuThumbnailUrl = rokuThumbnailUrl;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public Integer getPlays() {
		return plays;
	}
	public void setPlays(Integer plays) {
		this.plays = plays;
	}
	public Integer getViews() {
		return views;
	}
	public void setViews(Integer views) {
		this.views = views;
	}
	public String getLastPlayedAt() {
		return lastPlayedAt;
	}
	public void setLastPlayedAt(String lastPlayedAt) {
		this.lastPlayedAt = lastPlayedAt;
	}
	public Double getRank() {
		return rank;
	}
	public void setRank(Double rank) {
		this.rank = rank;
	}
	public Integer getTotalRank() {
		return totalRank;
	}
	public void setTotalRank(Integer totalRank) {
		this.totalRank = totalRank;
	}
	public Integer getVotes() {
		return votes;
	}
	public void setVotes(Integer votes) {
		this.votes = votes;
	}
	public String getHlsUrl() {
		return hlsUrl;
	}
	public void setHlsUrl(String hlsUrl) {
		this.hlsUrl = hlsUrl;
	}
	public String getMpegdashUrl() {
		return mpegdashUrl;
	}
	public void setMpegdashUrl(String mpegdashUrl) {
		this.mpegdashUrl = mpegdashUrl;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((androidThumbnailUrl == null) ? 0 : androidThumbnailUrl
						.hashCode());
		result = prime
				* result
				+ ((appleUmcThumbnailUrl == null) ? 0 : appleUmcThumbnailUrl
						.hashCode());
		result = prime * result + ((dataUrl == null) ? 0 : dataUrl.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((height == null) ? 0 : height.hashCode());
		result = prime * result + ((hlsUrl == null) ? 0 : hlsUrl.hashCode());
		result = prime * result
				+ ((iOSThumbnailUrl == null) ? 0 : iOSThumbnailUrl.hashCode());
		result = prime * result
				+ ((lastPlayedAt == null) ? 0 : lastPlayedAt.hashCode());
		result = prime * result
				+ ((minDuration == null) ? 0 : minDuration.hashCode());
		result = prime * result
				+ ((mpegdashUrl == null) ? 0 : mpegdashUrl.hashCode());
		result = prime * result
				+ ((msDuration == null) ? 0 : msDuration.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((plays == null) ? 0 : plays.hashCode());
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		result = prime * result
				+ ((referenceId == null) ? 0 : referenceId.hashCode());
		result = prime
				* result
				+ ((rokuThumbnailUrl == null) ? 0 : rokuThumbnailUrl.hashCode());
		result = prime * result + ((slug == null) ? 0 : slug.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result
				+ ((totalRank == null) ? 0 : totalRank.hashCode());
		result = prime * result + ((views == null) ? 0 : views.hashCode());
		result = prime * result + ((vmsId == null) ? 0 : vmsId.hashCode());
		result = prime * result + ((votes == null) ? 0 : votes.hashCode());
		result = prime * result
				+ ((webThumbnailUrl == null) ? 0 : webThumbnailUrl.hashCode());
		result = prime * result + ((width == null) ? 0 : width.hashCode());
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
		Video other = (Video) obj;
		if (androidThumbnailUrl == null) {
			if (other.androidThumbnailUrl != null)
				return false;
		} else if (!androidThumbnailUrl.equals(other.androidThumbnailUrl))
			return false;
		if (appleUmcThumbnailUrl == null) {
			if (other.appleUmcThumbnailUrl != null)
				return false;
		} else if (!appleUmcThumbnailUrl.equals(other.appleUmcThumbnailUrl))
			return false;
		if (dataUrl == null) {
			if (other.dataUrl != null)
				return false;
		} else if (!dataUrl.equals(other.dataUrl))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (height == null) {
			if (other.height != null)
				return false;
		} else if (!height.equals(other.height))
			return false;
		if (hlsUrl == null) {
			if (other.hlsUrl != null)
				return false;
		} else if (!hlsUrl.equals(other.hlsUrl))
			return false;
		if (iOSThumbnailUrl == null) {
			if (other.iOSThumbnailUrl != null)
				return false;
		} else if (!iOSThumbnailUrl.equals(other.iOSThumbnailUrl))
			return false;
		if (lastPlayedAt == null) {
			if (other.lastPlayedAt != null)
				return false;
		} else if (!lastPlayedAt.equals(other.lastPlayedAt))
			return false;
		if (minDuration == null) {
			if (other.minDuration != null)
				return false;
		} else if (!minDuration.equals(other.minDuration))
			return false;
		if (mpegdashUrl == null) {
			if (other.mpegdashUrl != null)
				return false;
		} else if (!mpegdashUrl.equals(other.mpegdashUrl))
			return false;
		if (msDuration == null) {
			if (other.msDuration != null)
				return false;
		} else if (!msDuration.equals(other.msDuration))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (plays == null) {
			if (other.plays != null)
				return false;
		} else if (!plays.equals(other.plays))
			return false;
		if (rank == null) {
			if (other.rank != null)
				return false;
		} else if (!rank.equals(other.rank))
			return false;
		if (referenceId == null) {
			if (other.referenceId != null)
				return false;
		} else if (!referenceId.equals(other.referenceId))
			return false;
		if (rokuThumbnailUrl == null) {
			if (other.rokuThumbnailUrl != null)
				return false;
		} else if (!rokuThumbnailUrl.equals(other.rokuThumbnailUrl))
			return false;
		if (slug == null) {
			if (other.slug != null)
				return false;
		} else if (!slug.equals(other.slug))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (totalRank == null) {
			if (other.totalRank != null)
				return false;
		} else if (!totalRank.equals(other.totalRank))
			return false;
		if (views == null) {
			if (other.views != null)
				return false;
		} else if (!views.equals(other.views))
			return false;
		if (vmsId == null) {
			if (other.vmsId != null)
				return false;
		} else if (!vmsId.equals(other.vmsId))
			return false;
		if (votes == null) {
			if (other.votes != null)
				return false;
		} else if (!votes.equals(other.votes))
			return false;
		if (webThumbnailUrl == null) {
			if (other.webThumbnailUrl != null)
				return false;
		} else if (!webThumbnailUrl.equals(other.webThumbnailUrl))
			return false;
		if (width == null) {
			if (other.width != null)
				return false;
		} else if (!width.equals(other.width))
			return false;
		return true;
	}	
}