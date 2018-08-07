/**
 * 
 */
package com.sorc.content.elasticsearch.core.util;

import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation.Entry;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sorc.content.elasticsearch.core.constant.AppleXmlFeedConstants;
import com.sorc.content.video.dao.data.AccessControl;
import com.sorc.content.video.dao.data.Category;
import com.sorc.content.video.dao.data.ElasticSearchVideo;

/**
 * @author rakesh.moradiya
 *
 */
public class AppleUmcAvailabilityResultAssembler {

	private static Logger logger = LoggerFactory.getLogger(AppleUmcAvailabilityResultAssembler.class);
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");	
	
		
	public static List<Object> getElasticSearchAppleUmcAvailabilityFeedDetailFacetResult(String facet, String subAggFacet, BoolQueryBuilder filters, SearchResult result) throws Exception {
		List<Object> resultFacetList = new ArrayList<Object>();
		
		String catalogId = PropertiesUtil.getProperty(AppleXmlFeedConstants.CATALOG_ID);
		String tvShowLocatorWebUrl = PropertiesUtil.getProperty(AppleXmlFeedConstants.TV_SHOW_LOCATOR_WEB_URL);
		String tvSeasonLocatorWebUrl = PropertiesUtil.getProperty(AppleXmlFeedConstants.TV_SEASON_LOCATOR_WEB_URL);
		String episodeLocatorWebUrl = PropertiesUtil.getProperty(AppleXmlFeedConstants.EPISODE_LOCATOR_WEB_URL);
		String tvShowLocatorOtherUrl = PropertiesUtil.getProperty(AppleXmlFeedConstants.TV_SHOW_LOCATOR_OTHER_URL);
		String tvSeasonLocatorOtherUrl = PropertiesUtil.getProperty(AppleXmlFeedConstants.TV_SEASON_LOCATOR_OTHER_URL);
		String episodeLocatorOtherUrl = PropertiesUtil.getProperty(AppleXmlFeedConstants.EPISODE_LOCATOR_OTHER_URL);
		String restrictionFormat = PropertiesUtil.getProperty(AppleXmlFeedConstants.RESTRICTION_FORMAT);
		String showContentId = null;		
		boolean showNodeAdded = false;
		boolean seasonNodeAdded = false;
		int totalShow = 0;
		int totalSeason = 0;
		int totalEpisode = 0;
		long showDocCount = 0;
		long seasonDocCount = 0;		
		
		int recordCount = 0;
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
		createNode(streamWriter, AppleXmlFeedConstants.LAST_BUILD_DATE, getLastBuildDate());	
		createServiceNode(streamWriter, eventFactory, PropertiesUtil.getProperty(AppleXmlFeedConstants.TEAM_ID), PropertiesUtil.getProperty(AppleXmlFeedConstants.SERVICE_ID));
		createNode(streamWriter, AppleXmlFeedConstants.SERVICE_TYPE, AppleXmlFeedConstants.UMC_AVAILABILITY_SERVICE_TYPE_VAL);
		createNode(streamWriter, AppleXmlFeedConstants.TOTAL_ITEM_COUNT, AppleXmlFeedConstants.COUNT_PLACE_HOLDER);						
		
		if(result != null && result.getAggregations() != null && result.getAggregations().getTermsAggregation(facet) != null) 
		{
			TermsAggregation terms = null;
			if(filters != null) {				
				terms = result.getAggregations().getFilterAggregation(facet).getAggregation(facet, TermsAggregation.class);
			} else {				
				terms = result.getAggregations().getTermsAggregation(facet);					
			}
											
	        if(terms != null && terms.getBuckets() != null && terms.getBuckets().size() > 0) 
	        {	        		        	    
	        	totalShow = terms.getBuckets().size();
	        	for(Entry bucket : terms.getBuckets()) {		        		
	        		showNodeAdded = false;
	        		seasonNodeAdded = false;	 
	        		showContentId = null;
	        		showDocCount = bucket.getCount();
	        		seasonDocCount = 0;
	        		
	        		if(bucket != null && bucket.getKey() != null && bucket.getCount() != null) {	 	        				        				        		        			
	        			TermsAggregation subTerms = bucket.getAggregation(subAggFacet, TermsAggregation.class);
	        			if(subTerms != null && subTerms.getBuckets() != null && subTerms.getBuckets().size() > 0) {
	        				//recordCount += subTerms.getBuckets().size();	   
	        				totalSeason += subTerms.getBuckets().size();
	        				for(Entry subBucket : subTerms.getBuckets()) {
	        					seasonDocCount += subBucket.getCount();        						
	        					seasonNodeAdded = false;
	        					if(subBucket != null && subBucket.getKey() != null && subBucket.getCount() != null) {		        						
				        			List<String> topHitsList = subBucket.getTopHitsAggregation(facet+subAggFacet).getSourceAsStringList();				        							        			
				        			recordCount += topHitsList.size();		
				        			totalEpisode += topHitsList.size();				        			
				        			for(String sourceStr : topHitsList)
				        			{				        				
				    					ObjectMapper mapper = new ObjectMapper();
										ElasticSearchVideo elasticSearchVideo = null;
										try 
										{											
											elasticSearchVideo = mapper.readValue(sourceStr, new TypeReference<ElasticSearchVideo>(){});																							
													
											if(!showNodeAdded)
											{
												showContentId = createShowImformation(streamWriter, eventFactory, elasticSearchVideo, bucket.getKey(), catalogId, tvShowLocatorWebUrl, tvShowLocatorOtherUrl);
												showNodeAdded = true;
												recordCount += 1;
											}
																						
											if(!seasonNodeAdded)
											{
												if(!subBucket.getKey().equals(AppleXmlFeedConstants.CATEGORY_TYPE_SEASON_PLACE_HOLDER))
												{
													createSeasonwImformation(streamWriter, eventFactory, elasticSearchVideo, catalogId, showContentId, bucket.getKey(), subBucket.getKey(), tvSeasonLocatorWebUrl, tvSeasonLocatorOtherUrl);
													seasonNodeAdded = true;
													recordCount += 1;
												}
												else
												{
													totalSeason -= 1;													
												}
												seasonNodeAdded = true;
											}											
											
											if(!subBucket.getKey().equals(AppleXmlFeedConstants.CATEGORY_TYPE_SEASON_PLACE_HOLDER))
												createEpisodeImformation(streamWriter, eventFactory, elasticSearchVideo, catalogId, episodeLocatorWebUrl, episodeLocatorOtherUrl, restrictionFormat);
											else
											{
												totalEpisode -= 1;
												recordCount -= 1;
											}
											
										} catch (Exception e) {
											logger.error(e.getMessage());
											e.printStackTrace();
										}																				
				        			}				        					        		
	        					}
	        					else
	        					{
	        						logger.info("No episode found for :"+subBucket.getKey());
	        					}
	        				}
	        				
	        				if(showDocCount != seasonDocCount)
	        					logger.info("Document not matched for Show:"+ bucket.getKey() + " show docCount: "+showDocCount + " season docCount: "+seasonDocCount);
	        			}	
	        			else
	        			{
	        				logger.info("No Season Found for	:"+bucket.getKey());
	        				logger.info("Missing Docs:	"+bucket.getCount());
	        			}
	        		}  
	        		else
	        			logger.info("No season found for :"+bucket.getKey());
	        	}	        	
	        }
		}		

		//End Service
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeEndElement();;
		
		//End umcAvailability
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeEndElement();;
					
		logger.info("Total Show: "+totalShow);
		logger.info("Total Season: "+totalSeason);
		logger.info("Total Episode: "+totalEpisode);
		resultFacetList.add(output.toString().replace(AppleXmlFeedConstants.TOTAL_ITME_COUNT_PLACEHOLDER, AppleXmlFeedConstants.TOTAL_ITEM_COUNT_START_ELEMENT+recordCount+AppleXmlFeedConstants.TOTAL_ITEM_COUNT_END_ELEMENT));		
		return resultFacetList;
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
		streamWriter.writeAttribute(AppleXmlFeedConstants.TEAM_ID, isStringNull(teamId));		
		streamWriter.writeAttribute(AppleXmlFeedConstants.SERVICE_ID, isStringNull(serviceId));		
	}
		
	private static void createNode(XMLStreamWriter streamWriter, String name, String value) throws XMLStreamException {
			
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(name);
		streamWriter.writeCharacters(value);
		streamWriter.writeEndElement();		
	}
	
	private static String createShowImformation(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, ElasticSearchVideo elasticSearchVideo, String showName, String catalogId, String tvShowLocatorWebUrl, String tvShowLocatorOtherUrl) throws XMLStreamException
	{		
		String showContentId = null;
		String locatorWebUrl = tvShowLocatorWebUrl;
		String locatorOtherUrl = tvShowLocatorOtherUrl;
		
		if(elasticSearchVideo != null && elasticSearchVideo.getCategories() != null && !elasticSearchVideo.getCategories().isEmpty())			
		{
			for(Category category : elasticSearchVideo.getCategories())
			{
				if(category.getType() != null && AppleXmlFeedConstants.CATEGORY_SHOW.equals(category.getType()))
				{			
					if(category.getName() != null && category.getName().equals(showName))
					{
						showContentId = (category.getId()==null?"":String.valueOf(category.getId()));
						streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
						streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
						
						streamWriter.writeStartElement(AppleXmlFeedConstants.ITEM);
						streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_TYPE, AppleXmlFeedConstants.TV_SHOW);
						streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_ID, showContentId);
						streamWriter.writeAttribute(AppleXmlFeedConstants.CATALOG_ID, isStringNull(catalogId));
				
						createNode(streamWriter, AppleXmlFeedConstants.PUB_DATE, isStringNull(category.getCreatedAt()));																		
							
						locatorWebUrl = locatorWebUrl.replace(AppleXmlFeedConstants.DISPLAY_NAME_INDICATOR, SlugGeneratorUtil.generateSlug(category.getName()));
						locatorWebUrl = locatorWebUrl.replace(AppleXmlFeedConstants.CATETORY_ID_INDICATOR, showContentId);
						
						locatorOtherUrl = locatorOtherUrl.replace(AppleXmlFeedConstants.DISPLAY_NAME_INDICATOR, SlugGeneratorUtil.generateSlug(category.getName()));
						locatorOtherUrl = locatorOtherUrl.replace(AppleXmlFeedConstants.CATETORY_ID_INDICATOR, showContentId);
						
						createLocatorsNode(streamWriter, eventFactory, locatorWebUrl, locatorOtherUrl);	
						
						streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
						streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
						streamWriter.writeEndElement();
						break;
					}
				}
			}
		}
		
		return showContentId;
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
		streamWriter.writeAttribute(AppleXmlFeedConstants.PLATFORM, isStringNull(platform));
		streamWriter.writeAttribute(AppleXmlFeedConstants.ACTION, isStringNull(action));
		streamWriter.writeAttribute(AppleXmlFeedConstants.URL, url);				
		streamWriter.writeEndElement();	
	}
		
	private static void createPlayablePropertiesNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, String closedCaptioning) throws XMLStreamException{		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.PLAYBLE_PROPERTIES);
		
		createNode(streamWriter, AppleXmlFeedConstants.CLOSED_CAPATIONING, isStringNull(closedCaptioning).toLowerCase()+AppleXmlFeedConstants.HYPHEN_US);	
		createNode(streamWriter, AppleXmlFeedConstants.VIDEO_QUALITY,  AppleXmlFeedConstants.VIDEO_QUALITY_HD);	
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
		streamWriter.writeEndElement();	
	}	
	
	private static void createSeasonwImformation(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, ElasticSearchVideo elasticSearchVideo, String catalogId, String showContentId, String showName, String seasonName, String tvSeasonLocatorWebUrl, String tvSeasonLocatorOtherUrl) throws XMLStreamException
	{			
		String seasonContentId = null;
		String locatorWebUrl = tvSeasonLocatorWebUrl;
		String locatorOtherUrl = tvSeasonLocatorOtherUrl;
		
		if(elasticSearchVideo != null && elasticSearchVideo.getCategories() != null && !elasticSearchVideo.getCategories().isEmpty())			
		{
			
			for(Category category : elasticSearchVideo.getCategories())
			{
				if(category.getType() != null && AppleXmlFeedConstants.CATEGORY_SEASON.equals(category.getType()))
				{
					if(category.getName() != null && category.getName().equals(seasonName))
					{
						seasonContentId = (category.getId()==null?"":String.valueOf(category.getId()));
						streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
						streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
						
						streamWriter.writeStartElement(AppleXmlFeedConstants.ITEM);
						streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_TYPE, AppleXmlFeedConstants.TV_SEASON);
						streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_ID, seasonContentId);
						streamWriter.writeAttribute(AppleXmlFeedConstants.CATALOG_ID, isStringNull(catalogId));
				
						createNode(streamWriter, AppleXmlFeedConstants.PUB_DATE, isStringNull(category.getCreatedAt()));																		
							
						locatorWebUrl = locatorWebUrl.replace(AppleXmlFeedConstants.DISPLAY_NAME_INDICATOR, SlugGeneratorUtil.generateSlug(showName));
						locatorWebUrl = locatorWebUrl.replace(AppleXmlFeedConstants.PARENT_CATETORY_ID_INDICATOR, showContentId);
						
						locatorOtherUrl = locatorOtherUrl.replace(AppleXmlFeedConstants.DISPLAY_NAME_INDICATOR, SlugGeneratorUtil.generateSlug(showName));
						locatorOtherUrl = locatorOtherUrl.replace(AppleXmlFeedConstants.PARENT_CATETORY_ID_INDICATOR, showContentId);
						
						createLocatorsNode(streamWriter, eventFactory, locatorWebUrl, locatorOtherUrl);	
						
						streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
						streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
						streamWriter.writeEndElement();
						break;
					}
				}
			}						
		}		
	}
	
	private static void createEpisodeImformation(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, ElasticSearchVideo elasticSearchVideo, String catalogId, String episodeLocatorWebUrl, String episodeLocatorOtherUrl, String restrictionFormat) throws XMLStreamException 
	{	
		String locatorWebUrl = episodeLocatorWebUrl;
		String locatorOtherUrl = episodeLocatorOtherUrl;
		String vmsId = null;
		String startDate = null;
		String endDate = null;
		if(elasticSearchVideo != null)
		{
			vmsId = isStringNull(elasticSearchVideo.getVideo()!=null?elasticSearchVideo.getVideo().getVmsId():null);
			streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
			streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
			
			streamWriter.writeStartElement(AppleXmlFeedConstants.ITEM);
			streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_TYPE, AppleXmlFeedConstants.TV_EPISODE);
			streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_ID, vmsId);
			streamWriter.writeAttribute(AppleXmlFeedConstants.CATALOG_ID, isStringNull(catalogId));
	
			createNode(streamWriter, AppleXmlFeedConstants.PUB_DATE, isStringNull(elasticSearchVideo.getUpdatedAt()));	
			
			locatorWebUrl = locatorWebUrl.replace(AppleXmlFeedConstants.VMS_ID_INDICATOR, vmsId);						
			locatorOtherUrl = locatorOtherUrl.replace(AppleXmlFeedConstants.VMS_ID_INDICATOR, vmsId);			
			
			createLocatorsNode(streamWriter, eventFactory, locatorWebUrl, locatorOtherUrl);	
			
			createPlayablePropertiesNode(streamWriter, eventFactory, (elasticSearchVideo.getClosedCaption() == null ? null: elasticSearchVideo.getClosedCaption().getLanguageCode()));
					
			if(elasticSearchVideo.getVideo() != null)
			{
				startDate = elasticSearchVideo.getVideo().getStartDate();
				endDate = elasticSearchVideo.getVideo().getEndDate();
			}
			
			createOfferesNode(streamWriter, eventFactory, startDate, endDate, (elasticSearchVideo.getMeta() == null ? false:(elasticSearchVideo.getMeta().getIsLiveEvent()==null?false:elasticSearchVideo.getMeta().getIsLiveEvent())), elasticSearchVideo.getAccessControl(), restrictionFormat);
			
			streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
			streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
			streamWriter.writeEndElement();
		}		
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
	
	private static void createOfferNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, String startDate, String endDate, boolean isLive, AccessControl accessControl, String restrictionFormat) throws XMLStreamException{		
		
		boolean allow = false;
		if(endDate == null || endDate.trim().length() ==0)
		{
			endDate = getYearEndDate(startDate);
		}
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.OFFER);
		
		createNode(streamWriter, AppleXmlFeedConstants.OFFERING_TYPE, AppleXmlFeedConstants.OFFERING_TYPE_SUBSCRIPTION);
		createNode(streamWriter, AppleXmlFeedConstants.WINDOW_START, isStringNull(startDate));
		createNode(streamWriter, AppleXmlFeedConstants.WINDOW_END, isStringNull(endDate));		
		createNode(streamWriter, AppleXmlFeedConstants.IS_LIVE, String.valueOf(isLive));
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.RESTRICTIONS);
		
		if(accessControl != null && accessControl.getValues() != null && !accessControl.getValues().isEmpty())
		{
			allow = accessControl.getAllow()==null?false:accessControl.getAllow();
			
			for(String str : accessControl.getValues())
			{
				streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
				streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
				streamWriter.writeStartElement(AppleXmlFeedConstants.RESTRICTION);
				
				streamWriter.writeAttribute(AppleXmlFeedConstants.TYPE, AppleXmlFeedConstants.TYPE_DEVICE_LOCATION);
				streamWriter.writeAttribute(AppleXmlFeedConstants.RELATIONSHIP, allow?AppleXmlFeedConstants.RELATIONSHIP_ALLOW:AppleXmlFeedConstants.RELATIONSHIP_DENY);
				streamWriter.writeCharacters(restrictionFormat+str);
				
				//End Restriction
				streamWriter.writeEndElement();	
			}
			streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
			streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		}
		
		
		//End Restrictions
		streamWriter.writeEndElement();	
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
				return String.valueOf(calendar.get(Calendar.YEAR))+AppleXmlFeedConstants.YEAR_END_STR;
			}
			catch(Exception e)
			{
				
			}
		}
		
		return "";
	}
	public static String isStringNull(String param) {
		if(param != null) {
			return param;
		}
		return "";
	}	
	
	private static String getLastBuildDate()
	{
		try
		{
			return sdf.format(new Date());
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
