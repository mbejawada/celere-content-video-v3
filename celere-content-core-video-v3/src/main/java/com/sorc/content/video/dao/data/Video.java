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

	
}