/**
 * 
 */
package com.sorc.content.services.video.util;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sorc.content.elasticsearch.core.constant.AppleXmlFeedConstants;
import com.sorc.content.elasticsearch.core.util.PropertiesUtil;
import com.sorc.content.elasticsearch.core.util.XmlFeedUtil;
import com.sorc.content.video.dao.data.AccessControl;
import com.sorc.content.video.dao.data.ElasticSearchVideo;

/**
 * @author rakesh.moradiya
 *
 */
public class AppleUmcLiveEventAvailabilityResultAssembler {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");	
	public static String getAppleUmcLiveEventAvailabilityFeedData(List<ElasticSearchVideo> videoList, Long totalItemCount) throws Exception
	{		
		String catalogId = PropertiesUtil.getProperty(AppleXmlFeedConstants.LIVE_EVENT_CATALOG_ID);
		String liveEventLocatorWebUrl = PropertiesUtil.getProperty(AppleXmlFeedConstants.LIVE_EVENT_LOCATOR_WEB_URL);
		String liveEventLocatorOtherUrl = PropertiesUtil.getProperty(AppleXmlFeedConstants.LIVE_EVENT_LOCATOR_OTHER_URL);
		String restrictionFormat = PropertiesUtil.getProperty(AppleXmlFeedConstants.RESTRICTION_FORMAT);
		
		String vmsId = null;
		String startDate = null;
		String endDate = null;
		
		// create a XMLOutputFactory
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		
		// create a StringWriter to write response
		StringWriter output = new StringWriter();
		
		// create XMLStreamWriter
		XMLStreamWriter streamWriter = outputFactory.createXMLStreamWriter(output);

		// create a EventFactory
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();		
		
		// create and write Start Tag		
		streamWriter.writeStartDocument("UTF-8", "1.0");
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
		createUmcAvailabilityNode(streamWriter, eventFactory);
		createNode(streamWriter, AppleXmlFeedConstants.LAST_BUILD_DATE, XmlFeedUtil.getLastBuildDate());	
		createServiceNode(streamWriter, eventFactory, PropertiesUtil.getProperty(AppleXmlFeedConstants.TEAM_ID), PropertiesUtil.getProperty(AppleXmlFeedConstants.LIVE_EVENT_AVAILABILITY_SERVICE_ID));
		createNode(streamWriter, AppleXmlFeedConstants.SERVICE_TYPE, AppleXmlFeedConstants.UMC_AVAILABILITY_SERVICE_TYPE_VAL);
		createNode(streamWriter, AppleXmlFeedConstants.TOTAL_ITEM_COUNT, totalItemCount == null?"0":String.valueOf(totalItemCount));		
		
		if(videoList != null && videoList.size() > 0)
		{
			for(ElasticSearchVideo esVideo : videoList)
			{
				vmsId = XmlFeedUtil.isStringNull(esVideo.getVideo()!=null?esVideo.getVideo().getVmsId():null);
				streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
				streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
				
				streamWriter.writeStartElement(AppleXmlFeedConstants.ITEM);
				streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_TYPE, AppleXmlFeedConstants.SPORTING_EVENT);
				streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_ID, vmsId);
				streamWriter.writeAttribute(AppleXmlFeedConstants.CATALOG_ID, XmlFeedUtil.isStringNull(catalogId));
		
				createNode(streamWriter, AppleXmlFeedConstants.PUB_DATE, XmlFeedUtil.isStringNull(esVideo.getUpdatedAt()));	
				
				liveEventLocatorWebUrl = liveEventLocatorWebUrl.replace(AppleXmlFeedConstants.VMS_ID_INDICATOR, vmsId);						
				liveEventLocatorOtherUrl = liveEventLocatorOtherUrl.replace(AppleXmlFeedConstants.VMS_ID_INDICATOR, vmsId);			
				
				createLocatorsNode(streamWriter, eventFactory, liveEventLocatorWebUrl, liveEventLocatorOtherUrl);	
				
				createPlayablePropertiesNode(streamWriter, eventFactory, (esVideo.getClosedCaption() == null ? null: esVideo.getClosedCaption().getLanguageCode()));
						
				if(esVideo.getLiveEvents() != null)
				{
					startDate = esVideo.getLiveEvents().getEventStartDate();
					endDate = esVideo.getLiveEvents().getEventEndDate();
				}
				
				createOfferesNode(streamWriter, eventFactory, startDate, endDate, (esVideo.getMeta() == null ? false:(esVideo.getMeta().getIsLiveEvent()==null?false:esVideo.getMeta().getIsLiveEvent())), esVideo.getAccessControl(), restrictionFormat);
				
				streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
				streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
				streamWriter.writeEndElement();
			}
		}
		
		//End Service
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeEndElement();;
		
		//End umcAvailability
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeEndElement();
		
		return output.toString();
	}
	
	private static void createUmcAvailabilityNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory) throws XMLStreamException{		
		streamWriter.writeStartElement(AppleXmlFeedConstants.UMC_AVAILABILITY);
		streamWriter.writeAttribute(AppleXmlFeedConstants.XMLNS_UMC, AppleXmlFeedConstants.XMLNS_UMC_AVAILABILITY_VAL);
		streamWriter.writeAttribute(AppleXmlFeedConstants.VERSION, AppleXmlFeedConstants.VERSION_VAL);			
	}
	
	private static void createServiceNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, String teamId, String serviceId) throws XMLStreamException{
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.SERVICE);
		streamWriter.writeAttribute(AppleXmlFeedConstants.TEAM_ID, XmlFeedUtil.isStringNull(teamId));		
		streamWriter.writeAttribute(AppleXmlFeedConstants.SERVICE_ID, XmlFeedUtil.isStringNull(serviceId));		
	}
	
	private static void createNode(XMLStreamWriter streamWriter, String name, String value) throws XMLStreamException {
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(name);
		streamWriter.writeCharacters(XmlFeedUtil.isStringNull(value));
		streamWriter.writeEndElement();		
	}	
	
	private static void createLocatorsNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, String locatorWebUrl, String locatorOtherUrl) throws XMLStreamException{		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.LOCATORS);
		
		createLocatorNode(streamWriter, eventFactory, AppleXmlFeedConstants.PLATFORM_WEB, AppleXmlFeedConstants.ACTION_OPEN, locatorWebUrl);
		createLocatorNode(streamWriter, eventFactory, AppleXmlFeedConstants.PLATFORM_WEB, AppleXmlFeedConstants.ACTION_PLAY, locatorWebUrl);
		
		createLocatorNode(streamWriter, eventFactory, AppleXmlFeedConstants.PLATFORM_ATV, AppleXmlFeedConstants.ACTION_OPEN, locatorOtherUrl);
		createLocatorNode(streamWriter, eventFactory, AppleXmlFeedConstants.PLATFORM_ATV, AppleXmlFeedConstants.ACTION_PLAY, locatorOtherUrl);
		
		createLocatorNode(streamWriter, eventFactory, AppleXmlFeedConstants.PLATFORM_IOS, AppleXmlFeedConstants.ACTION_OPEN, locatorOtherUrl);
		createLocatorNode(streamWriter, eventFactory, AppleXmlFeedConstants.PLATFORM_IOS, AppleXmlFeedConstants.ACTION_PLAY, locatorOtherUrl);
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
		streamWriter.writeEndElement();	
	}
	
	private static void createLocatorNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, String platform, String action, String url) throws XMLStreamException{		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.LOCATOR);
		streamWriter.writeAttribute(AppleXmlFeedConstants.PLATFORM, XmlFeedUtil.isStringNull(platform));
		streamWriter.writeAttribute(AppleXmlFeedConstants.ACTION, XmlFeedUtil.isStringNull(action));
		streamWriter.writeAttribute(AppleXmlFeedConstants.URL, url);				
		streamWriter.writeEndElement();	
	}
	
	private static void createPlayablePropertiesNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, String closedCaptioning) throws XMLStreamException{		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.PLAYBLE_PROPERTIES);
		
		createNode(streamWriter, AppleXmlFeedConstants.CLOSED_CAPATIONING, XmlFeedUtil.isStringNull(closedCaptioning).toLowerCase()+AppleXmlFeedConstants.HYPHEN_US);	
		createNode(streamWriter, AppleXmlFeedConstants.VIDEO_QUALITY,  AppleXmlFeedConstants.VIDEO_QUALITY_HD);	
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
		streamWriter.writeEndElement();	
	}
	
	private static void createOfferesNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, String startDate, String endDate, boolean isLive, AccessControl accessControl, String restrictionFormat) throws XMLStreamException{		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.OFFERS);
		
		createOfferNode(streamWriter, eventFactory, startDate, endDate, isLive, accessControl, restrictionFormat);
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
		//End Offers
		streamWriter.writeEndElement();	
	}
	
	private static void createOfferNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, String startDate, String endDate, boolean isLive, AccessControl accessControl, String restrictionFormat) throws XMLStreamException
	{					
		if(endDate == null || endDate.trim().length() ==0)
		{
			endDate = getYearEndDate(startDate);
		}
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.OFFER);
		
		createNode(streamWriter, AppleXmlFeedConstants.OFFERING_TYPE, AppleXmlFeedConstants.OFFERING_TYPE_SUBSCRIPTION);
		createNode(streamWriter, AppleXmlFeedConstants.WINDOW_START, XmlFeedUtil.isStringNull(startDate));
		createNode(streamWriter, AppleXmlFeedConstants.WINDOW_END, XmlFeedUtil.isStringNull(endDate));		
		createNode(streamWriter, AppleXmlFeedConstants.IS_LIVE, String.valueOf(isLive));
				
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		//End Offer
		streamWriter.writeEndElement();	
	}
	
	public static String getYearEndDate(String startDate)
	{
		if(startDate != null && startDate.trim().length() >0)
		{
			try
			{
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(sdf.parse(startDate));
				return String.valueOf(calendar.get(Calendar.YEAR)+1)+AppleXmlFeedConstants.YEAR_END_STR;
			}
			catch(Exception e)
			{
				
			}
		}
		
		return "";
	}
}
