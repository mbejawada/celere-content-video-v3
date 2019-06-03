package com.sorc.content.video.dao.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sorc.content.core.dao.IDataTransfer;
import com.wordnik.swagger.annotations.ApiModel;


/**
 * @author rakesh.moradiya
 *
 */
@ApiModel(value = "ElasticSearchVideo Description")
@XmlRootElement(name = "ElasticSearchVideo")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
public class ElasticSearchVideo  implements IDataTransfer<String>, Serializable {
	
	private static final long serialVersionUID = -5565632691117174004L;

	public ElasticSearchVideo(){}
	
	private String id;
	private Video video;
	private MetaMediaBasicFields metaMediaBasicFields;
	private Meta meta;
	private LiveEvents liveEvents;
	private List<String> person = new ArrayList<String>();
	private List<String> keywords = new ArrayList<String>();
	private List<VehicleTag> tags = new ArrayList<VehicleTag>();
	private String createdAt;
	private String updatedAt;
	private Integer hasSyndication;
	private AccessControl accessControl;
	private List<KalturaAdCuePointObj> adCuePoints = new ArrayList<KalturaAdCuePointObj>();
	private List<Link> links = new ArrayList<Link>();
	private List<Category> categories = new ArrayList<Category>();
	private List<Integer> websiteIds = new ArrayList<Integer>();
	private ClosedCaption closedCaption;
	private String show;
	private String season;
	private String mainCategory;
	private String showParentalRating;
	private String showGenry;
	private String seasonEpisodeShort;
	private Boolean isSlider;
	private Slider slider;
	private List<String> playlistIds = new ArrayList<String>();
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}
	public Video getVideo() {
		return video;
	}
	public void setVideo(Video video) {
		this.video = video;
	}
	public MetaMediaBasicFields getMetaMediaBasicFields() {
		return metaMediaBasicFields;
	}
	public void setMetaMediaBasicFields(MetaMediaBasicFields metaMediaBasicFields) {
		this.metaMediaBasicFields = metaMediaBasicFields;
	}
	public Meta getMeta() {
		return meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	public LiveEvents getLiveEvents() {
		return liveEvents;
	}
	public void setLiveEvents(LiveEvents liveEvents) {
		this.liveEvents = liveEvents;
	}
	public List<String> getPerson() {
		return person;
	}
	public void setPerson(List<String> person) {
		this.person = person;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	public List<VehicleTag> getTags() {
		return tags;
	}
	public void setTags(List<VehicleTag> tags) {
		this.tags = tags;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Integer getHasSyndication() {
		return hasSyndication;
	}
	public void setHasSyndication(Integer hasSyndication) {
		this.hasSyndication = hasSyndication;
	}
	public AccessControl getAccessControl() {
		return accessControl;
	}
	public void setAccessControl(AccessControl accessControl) {
		this.accessControl = accessControl;
	}
	public List<KalturaAdCuePointObj> getAdCuePoints() {
		return adCuePoints;
	}
	public void setAdCuePoints(List<KalturaAdCuePointObj> adCuePoints) {
		this.adCuePoints = adCuePoints;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public List<Integer> getWebsiteIds() {
		return websiteIds;
	}
	public void setWebsiteIds(List<Integer> websiteIds) {
		this.websiteIds = websiteIds;
	}

	public ClosedCaption getClosedCaption() {
		return closedCaption;
	}

	public void setClosedCaption(ClosedCaption closedCaption) {
		this.closedCaption = closedCaption;
	}

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getMainCategory() {
		return mainCategory;
	}

	public void setMainCategory(String mainCategory) {
		this.mainCategory = mainCategory;
	}

	public String getShowParentalRating() {
		return showParentalRating;
	}

	public void setShowParentalRating(String showParentalRating) {
		this.showParentalRating = showParentalRating;
	}

	public String getShowGenry() {
		return showGenry;
	}

	public void setShowGenry(String showGenry) {
		this.showGenry = showGenry;
	}

	public String getSeasonEpisodeShort() {
		return seasonEpisodeShort;
	}

	public void setSeasonEpisodeShort(String seasonEpisodeShort) {
		this.seasonEpisodeShort = seasonEpisodeShort;
	}

	public Boolean getIsSlider() {
		return isSlider;
	}

	public void setIsSlider(Boolean isSlider) {
		this.isSlider = isSlider;
	}

	public Slider getSlider() {
		return slider;
	}

	public void setSlider(Slider slider) {
		this.slider = slider;
	}

	public List<String> getPlaylistIds() {
		return playlistIds;
	}

	public void setPlaylistIds(List<String> playlistIds) {
		this.playlistIds = playlistIds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accessControl == null) ? 0 : accessControl.hashCode());
		result = prime * result
				+ ((adCuePoints == null) ? 0 : adCuePoints.hashCode());
		result = prime * result
				+ ((categories == null) ? 0 : categories.hashCode());
		result = prime * result
				+ ((closedCaption == null) ? 0 : closedCaption.hashCode());
		result = prime * result
				+ ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result
				+ ((hasSyndication == null) ? 0 : hasSyndication.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((isSlider == null) ? 0 : isSlider.hashCode());
		result = prime * result
				+ ((keywords == null) ? 0 : keywords.hashCode());
		result = prime * result + ((links == null) ? 0 : links.hashCode());
		result = prime * result
				+ ((liveEvents == null) ? 0 : liveEvents.hashCode());
		result = prime * result
				+ ((mainCategory == null) ? 0 : mainCategory.hashCode());
		result = prime * result + ((meta == null) ? 0 : meta.hashCode());
		result = prime
				* result
				+ ((metaMediaBasicFields == null) ? 0 : metaMediaBasicFields
						.hashCode());
		result = prime * result + ((person == null) ? 0 : person.hashCode());
		result = prime * result
				+ ((playlistIds == null) ? 0 : playlistIds.hashCode());
		result = prime * result + ((season == null) ? 0 : season.hashCode());
		result = prime
				* result
				+ ((seasonEpisodeShort == null) ? 0 : seasonEpisodeShort
						.hashCode());
		result = prime * result + ((show == null) ? 0 : show.hashCode());
		result = prime * result
				+ ((showGenry == null) ? 0 : showGenry.hashCode());
		result = prime
				* result
				+ ((showParentalRating == null) ? 0 : showParentalRating
						.hashCode());
		result = prime * result + ((slider == null) ? 0 : slider.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		result = prime * result
				+ ((updatedAt == null) ? 0 : updatedAt.hashCode());
		result = prime * result + ((video == null) ? 0 : video.hashCode());
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
		ElasticSearchVideo other = (ElasticSearchVideo) obj;
		if (accessControl == null) {
			if (other.accessControl != null)
				return false;
		} else if (!accessControl.equals(other.accessControl))
			return false;
		if (adCuePoints == null) {
			if (other.adCuePoints != null)
				return false;
		} else if (!adCuePoints.equals(other.adCuePoints))
			return false;
		if (categories == null) {
			if (other.categories != null)
				return false;
		} else if (!categories.equals(other.categories))
			return false;
		if (closedCaption == null) {
			if (other.closedCaption != null)
				return false;
		} else if (!closedCaption.equals(other.closedCaption))
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (hasSyndication == null) {
			if (other.hasSyndication != null)
				return false;
		} else if (!hasSyndication.equals(other.hasSyndication))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isSlider == null) {
			if (other.isSlider != null)
				return false;
		} else if (!isSlider.equals(other.isSlider))
			return false;
		if (keywords == null) {
			if (other.keywords != null)
				return false;
		} else if (!keywords.equals(other.keywords))
			return false;
		if (links == null) {
			if (other.links != null)
				return false;
		} else if (!links.equals(other.links))
			return false;
		if (liveEvents == null) {
			if (other.liveEvents != null)
				return false;
		} else if (!liveEvents.equals(other.liveEvents))
			return false;
		if (mainCategory == null) {
			if (other.mainCategory != null)
				return false;
		} else if (!mainCategory.equals(other.mainCategory))
			return false;
		if (meta == null) {
			if (other.meta != null)
				return false;
		} else if (!meta.equals(other.meta))
			return false;
		if (metaMediaBasicFields == null) {
			if (other.metaMediaBasicFields != null)
				return false;
		} else if (!metaMediaBasicFields.equals(other.metaMediaBasicFields))
			return false;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		if (playlistIds == null) {
			if (other.playlistIds != null)
				return false;
		} else if (!playlistIds.equals(other.playlistIds))
			return false;
		if (season == null) {
			if (other.season != null)
				return false;
		} else if (!season.equals(other.season))
			return false;
		if (seasonEpisodeShort == null) {
			if (other.seasonEpisodeShort != null)
				return false;
		} else if (!seasonEpisodeShort.equals(other.seasonEpisodeShort))
			return false;
		if (show == null) {
			if (other.show != null)
				return false;
		} else if (!show.equals(other.show))
			return false;
		if (showGenry == null) {
			if (other.showGenry != null)
				return false;
		} else if (!showGenry.equals(other.showGenry))
			return false;
		if (showParentalRating == null) {
			if (other.showParentalRating != null)
				return false;
		} else if (!showParentalRating.equals(other.showParentalRating))
			return false;
		if (slider == null) {
			if (other.slider != null)
				return false;
		} else if (!slider.equals(other.slider))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		if (updatedAt == null) {
			if (other.updatedAt != null)
				return false;
		} else if (!updatedAt.equals(other.updatedAt))
			return false;
		if (video == null) {
			if (other.video != null)
				return false;
		} else if (!video.equals(other.video))
			return false;
		if (websiteIds == null) {
			if (other.websiteIds != null)
				return false;
		} else if (!websiteIds.equals(other.websiteIds))
			return false;
		return true;
	}	
}
