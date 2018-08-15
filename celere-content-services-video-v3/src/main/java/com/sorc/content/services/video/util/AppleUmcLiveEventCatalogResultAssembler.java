/**
 * 
 */
package com.sorc.content.services.video.util;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sorc.content.elasticsearch.core.constant.AppleXmlFeedConstants;
import com.sorc.content.elasticsearch.core.util.PropertiesUtil;
import com.sorc.content.elasticsearch.core.util.XmlFeedUtil;
import com.sorc.content.video.dao.data.ElasticSearchVideo;

/**
 * @author rakesh.moradiya
 *
 */
public class AppleUmcLiveEventCatalogResultAssembler {

	private static Logger logger = LoggerFactory.getLogger(AppleUmcLiveEventCatalogResultAssembler.class);	
	
	public static String getAppleUmcLiveEventCatalogFeedData(List<ElasticSearchVideo> videoList, Long totalItemCount) throws Exception
	{		
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
		createUmcCatalogNode(streamWriter, eventFactory);
		createNode(streamWriter, AppleXmlFeedConstants.TOTAL_ITEM_COUNT, totalItemCount == null?"0":String.valueOf(totalItemCount));
		createNode(streamWriter, AppleXmlFeedConstants.LAST_BUILD_DATE, XmlFeedUtil.getLastBuildDate());	
		createNode(streamWriter, AppleXmlFeedConstants.TITLE, AppleXmlFeedConstants.LIVE_EVENT_CATALOG_ROOT_TITLE);
		createNode(streamWriter, AppleXmlFeedConstants.DESCRIPTION, AppleXmlFeedConstants.LIVE_EVENT_CATALOG_ROOT_DESCRIPTION);
		createNode(streamWriter, AppleXmlFeedConstants.DEFALUT_LOCALE, AppleXmlFeedConstants.ROOT_DEFALUT_LOCALE);
		
		if(videoList != null && videoList.size() > 0)
		{
			for(ElasticSearchVideo esVideo : videoList)
			{
				if(esVideo.getVideo() != null)
				{
					//Start Item Node
					createItemNode(streamWriter, eventFactory, esVideo.getVideo().getVmsId());
					
					createNode(streamWriter, AppleXmlFeedConstants.PUB_DATE, XmlFeedUtil.isStringNull(esVideo.getUpdatedAt()));	
					createNodeWithLocale(streamWriter, eventFactory, AppleXmlFeedConstants.TITLE, XmlFeedUtil.isStringNull(esVideo.getVideo().getName()));
					createNodeWithLocale(streamWriter, eventFactory, AppleXmlFeedConstants.DESCRIPTION, XmlFeedUtil.isStringNull(esVideo.getVideo().getDescription()));
					createSportingEventInfoNode(streamWriter, eventFactory, esVideo);
					//End Item Node					
					streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
					streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
					streamWriter.writeEndElement();;
				}
			}
		}
		
		//End umcCatalog
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeEndElement();
		
		return output.toString();
	}
	
	private static void createUmcCatalogNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory) throws XMLStreamException{		
		streamWriter.writeStartElement(AppleXmlFeedConstants.UMC_CATALOG);
		streamWriter.writeAttribute(AppleXmlFeedConstants.XMLNS_UMC, AppleXmlFeedConstants.XMLNS_UMC_VAL);
		streamWriter.writeAttribute(AppleXmlFeedConstants.VERSION, AppleXmlFeedConstants.VERSION_VAL);
		streamWriter.writeAttribute(AppleXmlFeedConstants.TEAM_ID, XmlFeedUtil.isStringNull(PropertiesUtil.getProperty(AppleXmlFeedConstants.TEAM_ID)));	
		streamWriter.writeAttribute(AppleXmlFeedConstants.CATALOG_ID, XmlFeedUtil.isStringNull(PropertiesUtil.getProperty(AppleXmlFeedConstants.LIVE_EVENT_CATALOG_ID)));	
	}
	
	private static void createNode(XMLStreamWriter streamWriter, String name, String value) throws XMLStreamException {
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(name);
		streamWriter.writeCharacters(XmlFeedUtil.isStringNull(value));
		streamWriter.writeEndElement();		
	}		
	
	private static void createItemNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, String contentId) throws XMLStreamException{		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.ITEM);
		streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_TYPE, AppleXmlFeedConstants.SPORTING_EVENT);
		streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_ID, XmlFeedUtil.isStringNull(contentId));			
	}
	
	private static void createNodeWithLocale(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, String element, String elementValue) throws XMLStreamException{		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(element);
		streamWriter.writeAttribute(AppleXmlFeedConstants.LOCALE, AppleXmlFeedConstants.LOCALE_VAL);		
		streamWriter.writeCharacters(elementValue);	
		streamWriter.writeEndElement();	
	}
	
	private static void createSportingEventInfoNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, ElasticSearchVideo esVideo) throws XMLStreamException{		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.SPORTING_EVENT_INFO);
		
		if(esVideo.getLiveEvents() != null)
		{
			createNode(streamWriter, AppleXmlFeedConstants.START_DATE, XmlFeedUtil.isStringNull(esVideo.getLiveEvents().getEventStartDate()));
			createNode(streamWriter, AppleXmlFeedConstants.SPORT_NAME, XmlFeedUtil.isStringNull(esVideo.getLiveEvents().getSportName()));
		}
		else
		{
			createNode(streamWriter, AppleXmlFeedConstants.START_DATE, "");
			createNode(streamWriter, AppleXmlFeedConstants.SPORT_NAME, "");
		}	
		
		createLeagueNode(streamWriter, eventFactory, esVideo);
		createVenueNode(streamWriter, eventFactory, esVideo);
		
		if(esVideo.getLiveEvents() != null)
			createNode(streamWriter, AppleXmlFeedConstants.IS_TBD, String.valueOf(esVideo.getLiveEvents().getEventTimeTBD()));
		else
			createNode(streamWriter, AppleXmlFeedConstants.IS_TBD, "false");
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
		streamWriter.writeEndElement();	
	}
	
	private static void createLeagueNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, ElasticSearchVideo esVideo) throws XMLStreamException{		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.LEAGE);
		
		if(esVideo.getLiveEvents() != null)
		{
			createNode(streamWriter, AppleXmlFeedConstants.ABBREVIATION, XmlFeedUtil.isStringNull(esVideo.getLiveEvents().getSportingLeague()));			
		}
		else
		{
			createNode(streamWriter, AppleXmlFeedConstants.ABBREVIATION, "");			
		}			
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
		streamWriter.writeEndElement();	
	}
	
	private static void createVenueNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, ElasticSearchVideo esVideo) throws XMLStreamException{		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.VENUE);
		
		if(esVideo.getMeta() != null)
		{
			createNode(streamWriter, AppleXmlFeedConstants.NAME, XmlFeedUtil.isStringNull(esVideo.getMeta().getCircuit()));
			createNodeWithLocale(streamWriter, eventFactory, AppleXmlFeedConstants.CITY, XmlFeedUtil.isStringNull(esVideo.getMeta().getCity()));					
			createNode(streamWriter, AppleXmlFeedConstants.COUNTRY, XmlFeedUtil.isStringNull(esVideo.getMeta().getCountry()));		
		}
		else
		{
			createNode(streamWriter, AppleXmlFeedConstants.NAME, "");
			createNodeWithLocale(streamWriter, eventFactory, AppleXmlFeedConstants.CITY, "");
			createNode(streamWriter, AppleXmlFeedConstants.COUNTRY, "");	
		}			
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
		streamWriter.writeEndElement();	
	}
}
