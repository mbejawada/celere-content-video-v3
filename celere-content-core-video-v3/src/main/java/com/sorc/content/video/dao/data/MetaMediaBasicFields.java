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
@ApiModel(value = "MetaMediaBasicFields Description")
@XmlRootElement(name = "MetaMediaBasicFields")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
public class MetaMediaBasicFields implements Serializable {

	private static final long serialVersionUID = 8973097842295836688L;

	public MetaMediaBasicFields(){}
	
	private String mediaType;
	private String watchPermissionRule;
	private String geoBlockRule;
	
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	public String getWatchPermissionRule() {
		return watchPermissionRule;
	}
	public void setWatchPermissionRule(String watchPermissionRule) {
		this.watchPermissionRule = watchPermissionRule;
	}
	public String getGeoBlockRule() {
		return geoBlockRule;
	}
	public void setGeoBlockRule(String geoBlockRule) {
		this.geoBlockRule = geoBlockRule;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((geoBlockRule == null) ? 0 : geoBlockRule.hashCode());
		result = prime * result
				+ ((mediaType == null) ? 0 : mediaType.hashCode());
		result = prime
				* result
				+ ((watchPermissionRule == null) ? 0 : watchPermissionRule
						.hashCode());
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
		MetaMediaBasicFields other = (MetaMediaBasicFields) obj;
		if (geoBlockRule == null) {
			if (other.geoBlockRule != null)
				return false;
		} else if (!geoBlockRule.equals(other.geoBlockRule))
			return false;
		if (mediaType == null) {
			if (other.mediaType != null)
				return false;
		} else if (!mediaType.equals(other.mediaType))
			return false;
		if (watchPermissionRule == null) {
			if (other.watchPermissionRule != null)
				return false;
		} else if (!watchPermissionRule.equals(other.watchPermissionRule))
			return false;
		return true;
	}	
	
}
