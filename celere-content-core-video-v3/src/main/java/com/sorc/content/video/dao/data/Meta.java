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
@ApiModel(value = "Meta Description")
@XmlRootElement(name = "Meta")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
public class Meta implements Serializable {		

	private static final long serialVersionUID = -1644705762364806018L;

	public Meta(){}
	
	private Integer episode;
	private Integer season;
	private String parentalRating;
	private String city;
	private String country;
	private String circuit;
	private Boolean isFree;
	private Boolean isLiveEvent;
	private String entitlement;
	private String genre;	
	private String sortDate;
	private Boolean hasCuePoint;
	private Boolean hasNewsRoom;
	
	public Integer getEpisode() {
		return episode;
	}
	public void setEpisode(Integer episode) {
		this.episode = episode;
	}
	public Integer getSeason() {
		return season;
	}
	public void setSeason(Integer season) {
		this.season = season;
	}
	public String getParentalRating() {
		return parentalRating;
	}
	public void setParentalRating(String parentalRating) {
		this.parentalRating = parentalRating;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCircuit() {
		return circuit;
	}
	public void setCircuit(String circuit) {
		this.circuit = circuit;
	}
	public Boolean getIsFree() {
		return isFree;
	}
	public void setIsFree(Boolean isFree) {
		this.isFree = isFree;
	}
	public Boolean getIsLiveEvent() {
		return isLiveEvent;
	}
	public void setIsLiveEvent(Boolean isLiveEvent) {
		this.isLiveEvent = isLiveEvent;
	}
	public String getEntitlement() {
		return entitlement;
	}
	public void setEntitlement(String entitlement) {
		this.entitlement = entitlement;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getSortDate() {
		return sortDate;
	}
	public void setSortDate(String sortDate) {
		this.sortDate = sortDate;
	}
	public Boolean getHasCuePoint() {
		return hasCuePoint;
	}
	public void setHasCuePoint(Boolean hasCuePoint) {
		this.hasCuePoint = hasCuePoint;
	}
	public Boolean getHasNewsRoom() {
		return hasNewsRoom;
	}
	public void setHasNewsRoom(Boolean hasNewsRoom) {
		this.hasNewsRoom = hasNewsRoom;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((circuit == null) ? 0 : circuit.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((entitlement == null) ? 0 : entitlement.hashCode());
		result = prime * result + ((episode == null) ? 0 : episode.hashCode());
		result = prime * result + ((genre == null) ? 0 : genre.hashCode());
		result = prime * result
				+ ((hasCuePoint == null) ? 0 : hasCuePoint.hashCode());
		result = prime * result
				+ ((hasNewsRoom == null) ? 0 : hasNewsRoom.hashCode());
		result = prime * result + ((isFree == null) ? 0 : isFree.hashCode());
		result = prime * result
				+ ((isLiveEvent == null) ? 0 : isLiveEvent.hashCode());
		result = prime * result
				+ ((parentalRating == null) ? 0 : parentalRating.hashCode());
		result = prime * result + ((season == null) ? 0 : season.hashCode());
		result = prime * result
				+ ((sortDate == null) ? 0 : sortDate.hashCode());
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
		Meta other = (Meta) obj;
		if (circuit == null) {
			if (other.circuit != null)
				return false;
		} else if (!circuit.equals(other.circuit))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (entitlement == null) {
			if (other.entitlement != null)
				return false;
		} else if (!entitlement.equals(other.entitlement))
			return false;
		if (episode == null) {
			if (other.episode != null)
				return false;
		} else if (!episode.equals(other.episode))
			return false;
		if (genre == null) {
			if (other.genre != null)
				return false;
		} else if (!genre.equals(other.genre))
			return false;
		if (hasCuePoint == null) {
			if (other.hasCuePoint != null)
				return false;
		} else if (!hasCuePoint.equals(other.hasCuePoint))
			return false;
		if (hasNewsRoom == null) {
			if (other.hasNewsRoom != null)
				return false;
		} else if (!hasNewsRoom.equals(other.hasNewsRoom))
			return false;
		if (isFree == null) {
			if (other.isFree != null)
				return false;
		} else if (!isFree.equals(other.isFree))
			return false;
		if (isLiveEvent == null) {
			if (other.isLiveEvent != null)
				return false;
		} else if (!isLiveEvent.equals(other.isLiveEvent))
			return false;
		if (parentalRating == null) {
			if (other.parentalRating != null)
				return false;
		} else if (!parentalRating.equals(other.parentalRating))
			return false;
		if (season == null) {
			if (other.season != null)
				return false;
		} else if (!season.equals(other.season))
			return false;
		if (sortDate == null) {
			if (other.sortDate != null)
				return false;
		} else if (!sortDate.equals(other.sortDate))
			return false;
		return true;
	}
	
}
