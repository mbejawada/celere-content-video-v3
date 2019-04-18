/**
 * 
 */
package com.sorc.content.video.dao.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wordnik.swagger.annotations.ApiModel;

/**
 * @author rakesh.moradiya
 *
 */
@ApiModel(value = "Slider Information")
@XmlRootElement(name = "Slider")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
public class Slider implements Serializable {
	
	private static final long serialVersionUID = -2672848360597889977L;

	private String template;
	private String title;
	private String subTitle;
	private String ctaText;
	private String ctaType;
	private String appCtaLink;
	private String webCtaLink;
	private List<String> ctaLinkOption  = new ArrayList<String>();
	private String categoryText;
	private String webCategoryLink;
	private String appCategoryLink;
	private List<String> categoryLinkOption  = new ArrayList<String>();
	private List<String> platformCheck  = new ArrayList<String>();
	private List<String> entitlementCheck  = new ArrayList<String>();
	private String moreInfo;
	private String campaign;

	public Slider(){}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getCtaText() {
		return ctaText;
	}

	public void setCtaText(String ctaText) {
		this.ctaText = ctaText;
	}

	public String getCtaType() {
		return ctaType;
	}

	public void setCtaType(String ctaType) {
		this.ctaType = ctaType;
	}

	public String getAppCtaLink() {
		return appCtaLink;
	}

	public void setAppCtaLink(String appCtaLink) {
		this.appCtaLink = appCtaLink;
	}

	public String getWebCtaLink() {
		return webCtaLink;
	}

	public void setWebCtaLink(String webCtaLink) {
		this.webCtaLink = webCtaLink;
	}

	public List<String> getCtaLinkOption() {
		return ctaLinkOption;
	}

	public void setCtaLinkOption(List<String> ctaLinkOption) {
		this.ctaLinkOption = ctaLinkOption;
	}

	public String getCategoryText() {
		return categoryText;
	}

	public void setCategoryText(String categoryText) {
		this.categoryText = categoryText;
	}

	public String getWebCategoryLink() {
		return webCategoryLink;
	}

	public void setWebCategoryLink(String webCategoryLink) {
		this.webCategoryLink = webCategoryLink;
	}

	public String getAppCategoryLink() {
		return appCategoryLink;
	}

	public void setAppCategoryLink(String appCategoryLink) {
		this.appCategoryLink = appCategoryLink;
	}

	public List<String> getCategoryLinkOption() {
		return categoryLinkOption;
	}

	public void setCategoryLinkOption(List<String> categoryLinkOption) {
		this.categoryLinkOption = categoryLinkOption;
	}

	public List<String> getPlatformCheck() {
		return platformCheck;
	}

	public void setPlatformCheck(List<String> platformCheck) {
		this.platformCheck = platformCheck;
	}

	public List<String> getEntitlementCheck() {
		return entitlementCheck;
	}

	public void setEntitlementCheck(List<String> entitlementCheck) {
		this.entitlementCheck = entitlementCheck;
	}

	public String getMoreInfo() {
		return moreInfo;
	}

	public void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}

	public String getCampaign() {
		return campaign;
	}

	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((appCategoryLink == null) ? 0 : appCategoryLink.hashCode());
		result = prime * result
				+ ((appCtaLink == null) ? 0 : appCtaLink.hashCode());
		result = prime * result
				+ ((campaign == null) ? 0 : campaign.hashCode());
		result = prime
				* result
				+ ((categoryLinkOption == null) ? 0 : categoryLinkOption
						.hashCode());
		result = prime * result
				+ ((categoryText == null) ? 0 : categoryText.hashCode());
		result = prime * result
				+ ((ctaLinkOption == null) ? 0 : ctaLinkOption.hashCode());
		result = prime * result + ((ctaText == null) ? 0 : ctaText.hashCode());
		result = prime * result + ((ctaType == null) ? 0 : ctaType.hashCode());
		result = prime
				* result
				+ ((entitlementCheck == null) ? 0 : entitlementCheck.hashCode());
		result = prime * result
				+ ((moreInfo == null) ? 0 : moreInfo.hashCode());
		result = prime * result
				+ ((platformCheck == null) ? 0 : platformCheck.hashCode());
		result = prime * result
				+ ((subTitle == null) ? 0 : subTitle.hashCode());
		result = prime * result
				+ ((template == null) ? 0 : template.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result
				+ ((webCategoryLink == null) ? 0 : webCategoryLink.hashCode());
		result = prime * result
				+ ((webCtaLink == null) ? 0 : webCtaLink.hashCode());
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
		Slider other = (Slider) obj;
		if (appCategoryLink == null) {
			if (other.appCategoryLink != null)
				return false;
		} else if (!appCategoryLink.equals(other.appCategoryLink))
			return false;
		if (appCtaLink == null) {
			if (other.appCtaLink != null)
				return false;
		} else if (!appCtaLink.equals(other.appCtaLink))
			return false;
		if (campaign == null) {
			if (other.campaign != null)
				return false;
		} else if (!campaign.equals(other.campaign))
			return false;
		if (categoryLinkOption == null) {
			if (other.categoryLinkOption != null)
				return false;
		} else if (!categoryLinkOption.equals(other.categoryLinkOption))
			return false;
		if (categoryText == null) {
			if (other.categoryText != null)
				return false;
		} else if (!categoryText.equals(other.categoryText))
			return false;
		if (ctaLinkOption == null) {
			if (other.ctaLinkOption != null)
				return false;
		} else if (!ctaLinkOption.equals(other.ctaLinkOption))
			return false;
		if (ctaText == null) {
			if (other.ctaText != null)
				return false;
		} else if (!ctaText.equals(other.ctaText))
			return false;
		if (ctaType == null) {
			if (other.ctaType != null)
				return false;
		} else if (!ctaType.equals(other.ctaType))
			return false;
		if (entitlementCheck == null) {
			if (other.entitlementCheck != null)
				return false;
		} else if (!entitlementCheck.equals(other.entitlementCheck))
			return false;
		if (moreInfo == null) {
			if (other.moreInfo != null)
				return false;
		} else if (!moreInfo.equals(other.moreInfo))
			return false;
		if (platformCheck == null) {
			if (other.platformCheck != null)
				return false;
		} else if (!platformCheck.equals(other.platformCheck))
			return false;
		if (subTitle == null) {
			if (other.subTitle != null)
				return false;
		} else if (!subTitle.equals(other.subTitle))
			return false;
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (!template.equals(other.template))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (webCategoryLink == null) {
			if (other.webCategoryLink != null)
				return false;
		} else if (!webCategoryLink.equals(other.webCategoryLink))
			return false;
		if (webCtaLink == null) {
			if (other.webCtaLink != null)
				return false;
		} else if (!webCtaLink.equals(other.webCtaLink))
			return false;
		return true;
	}
	
}
