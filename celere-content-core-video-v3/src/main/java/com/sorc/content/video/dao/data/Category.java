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
@ApiModel(value = "Category Description")
@XmlRootElement(name = "Category")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
public class Category implements Serializable {
		
	private static final long serialVersionUID = 9183872315578446046L;	

	private String createdAt;
	private String displayName;
	private Integer id;
	private String vmsUrl;
	private String brandLogoUrl;
	private String appleUmcUrl;
	private String webUrl;
	private String featureUrl;
	private String name;	
	private String type;
	private String description;	
	private String featureVertUrl;
	private String showVertUrl;
	
	public Category(){}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVmsUrl() {
		return vmsUrl;
	}

	public void setVmsUrl(String vmsUrl) {
		this.vmsUrl = vmsUrl;
	}

	public String getBrandLogoUrl() {
		return brandLogoUrl;
	}

	public void setBrandLogoUrl(String brandLogoUrl) {
		this.brandLogoUrl = brandLogoUrl;
	}

	public String getAppleUmcUrl() {
		return appleUmcUrl;
	}

	public void setAppleUmcUrl(String appleUmcUrl) {
		this.appleUmcUrl = appleUmcUrl;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getFeatureUrl() {
		return featureUrl;
	}

	public void setFeatureUrl(String featureUrl) {
		this.featureUrl = featureUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFeatureVertUrl() {
		return featureVertUrl;
	}

	public void setFeatureVertUrl(String featureVertUrl) {
		this.featureVertUrl = featureVertUrl;
	}

	public String getShowVertUrl() {
		return showVertUrl;
	}

	public void setShowVertUrl(String showVertUrl) {
		this.showVertUrl = showVertUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((appleUmcUrl == null) ? 0 : appleUmcUrl.hashCode());
		result = prime * result
				+ ((brandLogoUrl == null) ? 0 : brandLogoUrl.hashCode());
		result = prime * result
				+ ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result
				+ ((featureUrl == null) ? 0 : featureUrl.hashCode());
		result = prime * result
				+ ((featureVertUrl == null) ? 0 : featureVertUrl.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((showVertUrl == null) ? 0 : showVertUrl.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((vmsUrl == null) ? 0 : vmsUrl.hashCode());
		result = prime * result + ((webUrl == null) ? 0 : webUrl.hashCode());
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
		Category other = (Category) obj;
		if (appleUmcUrl == null) {
			if (other.appleUmcUrl != null)
				return false;
		} else if (!appleUmcUrl.equals(other.appleUmcUrl))
			return false;
		if (brandLogoUrl == null) {
			if (other.brandLogoUrl != null)
				return false;
		} else if (!brandLogoUrl.equals(other.brandLogoUrl))
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (featureUrl == null) {
			if (other.featureUrl != null)
				return false;
		} else if (!featureUrl.equals(other.featureUrl))
			return false;
		if (featureVertUrl == null) {
			if (other.featureVertUrl != null)
				return false;
		} else if (!featureVertUrl.equals(other.featureVertUrl))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (showVertUrl == null) {
			if (other.showVertUrl != null)
				return false;
		} else if (!showVertUrl.equals(other.showVertUrl))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (vmsUrl == null) {
			if (other.vmsUrl != null)
				return false;
		} else if (!vmsUrl.equals(other.vmsUrl))
			return false;
		if (webUrl == null) {
			if (other.webUrl != null)
				return false;
		} else if (!webUrl.equals(other.webUrl))
			return false;
		return true;
	}	
}
