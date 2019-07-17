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
@ApiModel(value = "LiveEvents Description")
@XmlRootElement(name = "LiveEvents")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
public class LiveEvents implements Serializable {

	private static final long serialVersionUID = -3127471993377150094L;

	public LiveEvents(){}
	
	private String vod;
	private String cdn;
	private String playerType;
	private String catalogStartDate;
	private String catalogEndDate;
	private String createPageDate;
	private String endPageDate;
	private String eventStartDate;
	private String eventEndDate;
	private String cdnMobile;
	private Boolean eventTimeTBD;
	private String sportName;
	private String sportingLeague;
	private boolean isHls;
	private String liveStatus;
	
	public String getVod() {
		return vod;
	}
	public void setVod(String vod) {
		this.vod = vod;
	}
	public String getCdn() {
		return cdn;
	}
	public void setCdn(String cdn) {
		this.cdn = cdn;
	}
	public String getPlayerType() {
		return playerType;
	}
	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}
	public String getCatalogStartDate() {
		return catalogStartDate;
	}
	public void setCatalogStartDate(String catalogStartDate) {
		this.catalogStartDate = catalogStartDate;
	}
	public String getCatalogEndDate() {
		return catalogEndDate;
	}
	public void setCatalogEndDate(String catalogEndDate) {
		this.catalogEndDate = catalogEndDate;
	}
	public String getCreatePageDate() {
		return createPageDate;
	}
	public void setCreatePageDate(String createPageDate) {
		this.createPageDate = createPageDate;
	}
	public String getEndPageDate() {
		return endPageDate;
	}
	public void setEndPageDate(String endPageDate) {
		this.endPageDate = endPageDate;
	}
	public String getEventStartDate() {
		return eventStartDate;
	}
	public void setEventStartDate(String eventStartDate) {
		this.eventStartDate = eventStartDate;
	}
	public String getEventEndDate() {
		return eventEndDate;
	}
	public void setEventEndDate(String eventEndDate) {
		this.eventEndDate = eventEndDate;
	}
	public String getCdnMobile() {
		return cdnMobile;
	}
	public void setCdnMobile(String cdnMobile) {
		this.cdnMobile = cdnMobile;
	}
	public Boolean getEventTimeTBD() {
		return eventTimeTBD;
	}
	public void setEventTimeTBD(Boolean eventTimeTBD) {
		this.eventTimeTBD = eventTimeTBD;
	}
	
	public String getSportName() {
		return sportName;
	}
	public void setSportName(String sportName) {
		this.sportName = sportName;
	}
	public String getSportingLeague() {
		return sportingLeague;
	}
	public void setSportingLeague(String sportingLeague) {
		this.sportingLeague = sportingLeague;
	}
	public boolean getIsHls() {
		return isHls;
	}
	public void setIsHls(boolean isHls) {
		this.isHls = isHls;
	}
	public String getLiveStatus() {
		return liveStatus;
	}
	public void setLiveStatus(String liveStatus) {
		this.liveStatus = liveStatus;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((catalogEndDate == null) ? 0 : catalogEndDate.hashCode());
		result = prime
				* result
				+ ((catalogStartDate == null) ? 0 : catalogStartDate.hashCode());
		result = prime * result + ((cdn == null) ? 0 : cdn.hashCode());
		result = prime * result
				+ ((cdnMobile == null) ? 0 : cdnMobile.hashCode());
		result = prime * result
				+ ((createPageDate == null) ? 0 : createPageDate.hashCode());
		result = prime * result
				+ ((endPageDate == null) ? 0 : endPageDate.hashCode());
		result = prime * result
				+ ((eventEndDate == null) ? 0 : eventEndDate.hashCode());
		result = prime * result
				+ ((eventStartDate == null) ? 0 : eventStartDate.hashCode());
		result = prime * result
				+ ((eventTimeTBD == null) ? 0 : eventTimeTBD.hashCode());
		result = prime * result + (isHls ? 1231 : 1237);
		result = prime * result
				+ ((liveStatus == null) ? 0 : liveStatus.hashCode());
		result = prime * result
				+ ((playerType == null) ? 0 : playerType.hashCode());
		result = prime * result
				+ ((sportName == null) ? 0 : sportName.hashCode());
		result = prime * result
				+ ((sportingLeague == null) ? 0 : sportingLeague.hashCode());
		result = prime * result + ((vod == null) ? 0 : vod.hashCode());
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
		LiveEvents other = (LiveEvents) obj;
		if (catalogEndDate == null) {
			if (other.catalogEndDate != null)
				return false;
		} else if (!catalogEndDate.equals(other.catalogEndDate))
			return false;
		if (catalogStartDate == null) {
			if (other.catalogStartDate != null)
				return false;
		} else if (!catalogStartDate.equals(other.catalogStartDate))
			return false;
		if (cdn == null) {
			if (other.cdn != null)
				return false;
		} else if (!cdn.equals(other.cdn))
			return false;
		if (cdnMobile == null) {
			if (other.cdnMobile != null)
				return false;
		} else if (!cdnMobile.equals(other.cdnMobile))
			return false;
		if (createPageDate == null) {
			if (other.createPageDate != null)
				return false;
		} else if (!createPageDate.equals(other.createPageDate))
			return false;
		if (endPageDate == null) {
			if (other.endPageDate != null)
				return false;
		} else if (!endPageDate.equals(other.endPageDate))
			return false;
		if (eventEndDate == null) {
			if (other.eventEndDate != null)
				return false;
		} else if (!eventEndDate.equals(other.eventEndDate))
			return false;
		if (eventStartDate == null) {
			if (other.eventStartDate != null)
				return false;
		} else if (!eventStartDate.equals(other.eventStartDate))
			return false;
		if (eventTimeTBD == null) {
			if (other.eventTimeTBD != null)
				return false;
		} else if (!eventTimeTBD.equals(other.eventTimeTBD))
			return false;
		if (isHls != other.isHls)
			return false;
		if (liveStatus == null) {
			if (other.liveStatus != null)
				return false;
		} else if (!liveStatus.equals(other.liveStatus))
			return false;
		if (playerType == null) {
			if (other.playerType != null)
				return false;
		} else if (!playerType.equals(other.playerType))
			return false;
		if (sportName == null) {
			if (other.sportName != null)
				return false;
		} else if (!sportName.equals(other.sportName))
			return false;
		if (sportingLeague == null) {
			if (other.sportingLeague != null)
				return false;
		} else if (!sportingLeague.equals(other.sportingLeague))
			return false;
		if (vod == null) {
			if (other.vod != null)
				return false;
		} else if (!vod.equals(other.vod))
			return false;
		return true;
	}
	
}
