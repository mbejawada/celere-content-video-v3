package com.sorc.content.elasticsearch.core.util;

import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation.Entry;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sorc.content.elasticsearch.core.constant.AppleXmlFeedConstants;
import com.sorc.content.elasticsearch.core.constant.ElasticSearchVideoFieldConstants;
import com.sorc.content.video.dao.data.Category;
import com.sorc.content.video.dao.data.ElasticSearchVideo;
import com.sorc.content.video.dao.data.Meta;
import com.sorc.content.video.dao.data.Video;

/**
 * @author rakesh.moradiya
 *
 */
public class ElasticSearchVideoResultAssembler {

	private static Logger logger = LoggerFactory.getLogger(ElasticSearchVideoResultAssembler.class);			
	
	public static Map<String, Object> getElasticSearchVideoResult(SearchResult result) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ElasticSearchVideo> buyersGuideList = new ArrayList<ElasticSearchVideo>();
		if(result != null) {
			if(result.getTotal() != null) {
				resultMap.put(ElasticSearchVideoFieldConstants.TOTAL_COUNT, Long.valueOf(result.getTotal()));
			}
			if(result.isSucceeded() == true) {
				List<Hit<JSONObject, Void>> searchResultList = result.getHits(JSONObject.class);
				if(searchResultList != null && searchResultList.size() > 0) {
			        for (Hit<JSONObject, Void> hit : searchResultList) {
						 if(hit != null && hit.source != null) {
							ObjectMapper mapper = new ObjectMapper();
							ElasticSearchVideo elasticSearchVideo;
							try {
								elasticSearchVideo = mapper.readValue(hit.source.toString(), new TypeReference<ElasticSearchVideo>(){});
								buyersGuideList.add(elasticSearchVideo);
							} catch (IOException e) {
								logger.error(e.getMessage());
							}
						 }
					}
			        resultMap.put(ElasticSearchVideoFieldConstants.ELASTICSEARCH_VIDEO_LIST, buyersGuideList);
				}
			}
		}
		return resultMap;
	}
	
	public static List<Object> getElasticSearchVideoFacetResult(String facet, BoolQueryBuilder filters, SearchResult result) {
		List<Object> resultFacetList = new ArrayList<Object>();
		if(result != null && result.getAggregations() != null 
				&& result.getAggregations().getTermsAggregation(facet) != null) {
			TermsAggregation terms = null;
			if(filters != null) {
				terms = result.getAggregations().getFilterAggregation(facet).getAggregation(facet, TermsAggregation.class);
			} else {
				terms = result.getAggregations().getTermsAggregation(facet);	
			}
			
	        if(terms != null && terms.getBuckets() != null && terms.getBuckets().size() > 0) {
	        	//int count = 0;
	        	for(Entry bucket : terms.getBuckets()) {
	        		if(bucket != null && bucket.getKey() != null && bucket.getCount() != null) {
		        		String key = bucket.getKey();
		        		if(key != null) {
		        			resultFacetList.add(key);
		        		}
	        		}
	        	}
	        }
		}
		return resultFacetList;
	}
	
	public static List<Object> getElasticSearchAppleUmcFeedDetailFacetResult(String facet, String subAggFacet, BoolQueryBuilder filters, SearchResult result) throws Exception {
		List<Object> resultFacetList = new ArrayList<Object>();
		
		String showContentId = null;
		String seasonContentId = null;
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
		createUmcCatlogNode(streamWriter, eventFactory, PropertiesUtil.getProperty(AppleXmlFeedConstants.TEAM_ID), PropertiesUtil.getProperty(AppleXmlFeedConstants.CATALOG_ID));
								
		if(result != null && result.getAggregations() != null && result.getAggregations().getTermsAggregation(facet) != null) 
		{
			TermsAggregation terms = null;
			if(filters != null) {				
				terms = result.getAggregations().getFilterAggregation(facet).getAggregation(facet, TermsAggregation.class);
			} else {				
				terms = result.getAggregations().getTermsAggregation(facet);					
			}
			
			createNode(streamWriter, AppleXmlFeedConstants.TOTAL_ITEM_COUNT, AppleXmlFeedConstants.COUNT_PLACE_HOLDER);
			createNode(streamWriter, AppleXmlFeedConstants.LAST_BUILD_DATE, XmlFeedUtil.getLastBuildDate());				
			createNode(streamWriter, AppleXmlFeedConstants.TITLE, AppleXmlFeedConstants.ROOT_TITLE);
			createNode(streamWriter, AppleXmlFeedConstants.DESCRIPTION, AppleXmlFeedConstants.ROOT_DESCRIPTION);
			createNode(streamWriter, AppleXmlFeedConstants.DEFALUT_LOCALE, AppleXmlFeedConstants.ROOT_DEFALUT_LOCALE);
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
				        			seasonContentId = null;
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
												showContentId = createShowImformation(streamWriter, eventFactory, elasticSearchVideo, bucket.getKey());
												showNodeAdded = true;
												recordCount += 1;
											}
																						
											if(!seasonNodeAdded)
											{
												if(!subBucket.getKey().equals(AppleXmlFeedConstants.CATEGORY_TYPE_SEASON_PLACE_HOLDER))
												{
													seasonContentId = createSeasonwImformation(streamWriter, eventFactory, elasticSearchVideo, showContentId, subBucket.getKey());
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
												createEpisodeImformation(streamWriter, eventFactory, elasticSearchVideo, showContentId, seasonContentId);
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
		else
		{			
			createNode(streamWriter, AppleXmlFeedConstants.TOTAL_ITEM_COUNT, AppleXmlFeedConstants.COUNT_PLACE_HOLDER);
			createNode(streamWriter, AppleXmlFeedConstants.LAST_BUILD_DATE, XmlFeedUtil.getLastBuildDate());				
			createNode(streamWriter, AppleXmlFeedConstants.TITLE, AppleXmlFeedConstants.ROOT_TITLE);
			createNode(streamWriter, AppleXmlFeedConstants.DESCRIPTION, AppleXmlFeedConstants.ROOT_DESCRIPTION);
			createNode(streamWriter, AppleXmlFeedConstants.DEFALUT_LOCALE, AppleXmlFeedConstants.ROOT_DEFALUT_LOCALE);
		}
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeEndElement();;
					
		logger.info("Total Show: "+totalShow);
		logger.info("Total Season: "+totalSeason);
		logger.info("Total Episode: "+totalEpisode);
		resultFacetList.add(output.toString().replace(AppleXmlFeedConstants.TOTAL_ITME_COUNT_PLACEHOLDER, AppleXmlFeedConstants.TOTAL_ITEM_COUNT_START_ELEMENT+recordCount+AppleXmlFeedConstants.TOTAL_ITEM_COUNT_END_ELEMENT));		
		return resultFacetList;
	}
	
	private static void createUmcCatlogNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, 
			String teamId, String catalogId) throws XMLStreamException{		
		streamWriter.writeStartElement(AppleXmlFeedConstants.UMC_CATALOG);
		streamWriter.writeAttribute(AppleXmlFeedConstants.XMLNS_UMC, AppleXmlFeedConstants.XMLNS_UMC_VAL);
		streamWriter.writeAttribute(AppleXmlFeedConstants.VERSION, AppleXmlFeedConstants.VERSION_VAL);
		streamWriter.writeAttribute(AppleXmlFeedConstants.TEAM_ID, XmlFeedUtil.isStringNull(teamId));		
		streamWriter.writeAttribute(AppleXmlFeedConstants.CATALOG_ID, XmlFeedUtil.isStringNull(catalogId));			
	}
	
	private static void createNode(XMLStreamWriter streamWriter, String name, String value) throws XMLStreamException {
			
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(name);
		streamWriter.writeCharacters(value);
		streamWriter.writeEndElement();		
	}
	
	private static String createShowImformation(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, ElasticSearchVideo elasticSearchVideo, String showName) throws XMLStreamException
	{		
		String showContentId = null;
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
				
						createNode(streamWriter, AppleXmlFeedConstants.PUB_DATE, XmlFeedUtil.isStringNull(category.getCreatedAt()));		
						createNodeWithLocale(streamWriter, eventFactory, AppleXmlFeedConstants.TITLE, XmlFeedUtil.isStringNull(category.getDisplayName()));
						createNodeWithLocale(streamWriter, eventFactory, AppleXmlFeedConstants.DESCRIPTION, XmlFeedUtil.isStringNull(category.getDescription()));
						createNode(streamWriter, AppleXmlFeedConstants.GENRE, XmlFeedUtil.isStringNull(elasticSearchVideo.getShowGenry()));		
						
						//Populating Rating
						/*streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
						streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
						streamWriter.writeStartElement(AppleXmlFeedConstants.RATING);
						streamWriter.writeAttribute(AppleXmlFeedConstants.SYSTEM_CODE, AppleXmlFeedConstants.SYSTEM_CODE_VAL);		
						streamWriter.writeCharacters(AppleXmlFeedConstants.RATING_VAL_PREFIX+XmlFeedUtil.isStringNull(elasticSearchVideo.getShowParentalRating()));	
						streamWriter.writeEndElement();	*/
						
						createArtWorkNode(streamWriter, eventFactory, XmlFeedUtil.isStringNull(category.getAppleUmcUrl()), AppleXmlFeedConstants.ARTWORK_TYPE_COVERAGE_SQUARE);		
						createNode(streamWriter, AppleXmlFeedConstants.IS_ORIGINAL, AppleXmlFeedConstants.TRUE);
						createTvShowInfoNode(streamWriter, eventFactory,  XmlFeedUtil.isStringNull(category.getCreatedAt()));	
						
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
		
	private static void createNodeWithLocale(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, String element, String elementValue) throws XMLStreamException{		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(element);
		streamWriter.writeAttribute(AppleXmlFeedConstants.LOCALE, AppleXmlFeedConstants.LOCALE_VAL);		
		streamWriter.writeCharacters(elementValue);	
		streamWriter.writeEndElement();	
	}
	
	private static void createArtWorkNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, String url, String artWorkType) throws XMLStreamException{		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.ARTWORK);
		streamWriter.writeAttribute(AppleXmlFeedConstants.URL, url);
		streamWriter.writeAttribute(AppleXmlFeedConstants.TYPE, artWorkType);				
		streamWriter.writeEndElement();	
	}
	
	private static void createTvShowInfoNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, String createdAt) throws XMLStreamException{		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.TV_SHOWINFO);
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.TYPE);
		streamWriter.writeCharacters(AppleXmlFeedConstants.TV_SHOW_TYPE_VAL);	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.ORIGINAL_PREMIERE_DATE);
		streamWriter.writeCharacters(XmlFeedUtil.getYyyyMmDdStr(createdAt));	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.NETWORK);
		streamWriter.writeCharacters(AppleXmlFeedConstants.NETWORK_MOTORTREND);	
		streamWriter.writeEndElement();				
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
		streamWriter.writeEndElement();	
	}
	
	private static String createSeasonwImformation(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, ElasticSearchVideo elasticSearchVideo, String showContentId, String seasonName) throws XMLStreamException
	{			
		String seasonContentId = null;
		if(elasticSearchVideo != null && elasticSearchVideo.getCategories() != null && !elasticSearchVideo.getCategories().isEmpty())			
		{
			
			for(Category category : elasticSearchVideo.getCategories())
			{
				if(category.getType() != null && AppleXmlFeedConstants.CATEGORY_SEASON.equals(category.getType()))
				{
					if(category.getName() != null && category.getName().equals(seasonName))
					{
						seasonContentId = category.getId()==null?"":String.valueOf(category.getId());
						streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
						streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
						
						streamWriter.writeStartElement(AppleXmlFeedConstants.ITEM);
						streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_TYPE, AppleXmlFeedConstants.TV_SEASON);
						streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_ID, seasonContentId);
				
						createNode(streamWriter, AppleXmlFeedConstants.PUB_DATE, XmlFeedUtil.isStringNull(category.getCreatedAt()));		
						createNodeWithLocale(streamWriter, eventFactory, AppleXmlFeedConstants.TITLE, XmlFeedUtil.isStringNull(category.getDisplayName()));		
								
						createArtWorkNode(streamWriter, eventFactory, XmlFeedUtil.isStringNull(category.getAppleUmcUrl()), AppleXmlFeedConstants.ARTWORK_TYPE_COVERAGE_SQUARE);				
						createTvSeasonInfoNode(streamWriter, eventFactory, XmlFeedUtil.isStringNull(showContentId), (elasticSearchVideo.getMeta()==null?"":String.valueOf(elasticSearchVideo.getMeta().getSeason())), XmlFeedUtil.isStringNull(category.getCreatedAt()));		
						
						streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
						streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
						streamWriter.writeEndElement();
						break;
					}
				}
			}						
		}
		return seasonContentId;
	}
	
	private static void createTvSeasonInfoNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, String contentId, String seasonNumber, String createdAt) throws XMLStreamException{		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.TV_SEASONINFO);
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.SHOW_CONTENT_ID);
		streamWriter.writeCharacters(XmlFeedUtil.isStringNull(contentId));	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.SEASON_NUMBER);
		streamWriter.writeCharacters(XmlFeedUtil.isStringNull(seasonNumber));	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.ORIGINAL_PREMIERE_DATE);
		streamWriter.writeCharacters(XmlFeedUtil.getYyyyMmDdStr(createdAt));	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
		streamWriter.writeEndElement();	
	}
	
	private static void createEpisodeImformation(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, ElasticSearchVideo elasticSearchVideo, String showContentId, String seasonContentId) throws XMLStreamException 
	{	
		if(elasticSearchVideo != null)
		{
			streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
			streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
			
			streamWriter.writeStartElement(AppleXmlFeedConstants.ITEM);
			streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_TYPE, AppleXmlFeedConstants.TV_EPISODE);
			streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_ID, XmlFeedUtil.isStringNull(elasticSearchVideo.getVideo()!=null?elasticSearchVideo.getVideo().getVmsId():null));
	
			createNode(streamWriter, AppleXmlFeedConstants.PUB_DATE, XmlFeedUtil.isStringNull(elasticSearchVideo.getUpdatedAt()));		
			createNodeWithLocale(streamWriter, eventFactory, AppleXmlFeedConstants.TITLE, XmlFeedUtil.isStringNull(elasticSearchVideo.getVideo()!=null?elasticSearchVideo.getVideo().getName():null));
			createNodeWithLocale(streamWriter, eventFactory, AppleXmlFeedConstants.DESCRIPTION, XmlFeedUtil.isStringNull(elasticSearchVideo.getVideo()!=null?elasticSearchVideo.getVideo().getDescription():null));		
					
			createArtWorkNode(streamWriter, eventFactory, XmlFeedUtil.isStringNull(elasticSearchVideo.getVideo()!=null?elasticSearchVideo.getVideo().getAppleUmcThumbnailUrl():null), AppleXmlFeedConstants.ARTWORK_TYPE_TITLE);		
			
			if(elasticSearchVideo.getPerson() != null && !elasticSearchVideo.getPerson().isEmpty())
			{
				for(String personName : elasticSearchVideo.getPerson())
				{
					streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
					streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
					streamWriter.writeStartElement(AppleXmlFeedConstants.CREDIT);
					streamWriter.writeAttribute(AppleXmlFeedConstants.ROLE, AppleXmlFeedConstants.ROLE_CREATOR);		
					streamWriter.writeCharacters(personName);	
					streamWriter.writeEndElement();	
				}
			}
			else
			{
				streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
				streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
				streamWriter.writeStartElement(AppleXmlFeedConstants.CREDIT);
				streamWriter.writeAttribute(AppleXmlFeedConstants.ROLE, AppleXmlFeedConstants.ROLE_CREATOR);				
				streamWriter.writeEndElement();	
			}
			createTvEpisodeInfoNode(streamWriter, eventFactory, elasticSearchVideo.getVideo(), showContentId, seasonContentId, elasticSearchVideo.getMeta());
			
			streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
			streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
			streamWriter.writeEndElement();
		}		
	}
	
	private static void createTvEpisodeInfoNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, Video video, String showContentId, String seasonContentId, Meta meta) throws XMLStreamException
	{		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.TV_EPISODEINFO);
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.TYPE);
		streamWriter.writeCharacters(AppleXmlFeedConstants.STANDARD);	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.DURATION);
		streamWriter.writeCharacters(XmlFeedUtil.isStringNull(video == null?"":String.valueOf(video.getDuration())));	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.ORIGINAL_AIR_DATE);
		streamWriter.writeCharacters(XmlFeedUtil.isStringNull(video == null?"":XmlFeedUtil.getYyyyMmDdStr(video.getStartDate())));	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.SHOW_CONTENT_ID);
		streamWriter.writeCharacters(XmlFeedUtil.isStringNull(showContentId));	
		streamWriter.writeEndElement();		
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.SEASON_CONTENT_ID);
		streamWriter.writeCharacters(XmlFeedUtil.isStringNull(seasonContentId));	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.SEASON_NUMBER);
		streamWriter.writeCharacters(XmlFeedUtil.isStringNull(meta == null?"":String.valueOf(meta.getSeason())));	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.EPISODE_NUMBER);
		streamWriter.writeCharacters(XmlFeedUtil.isStringNull(meta == null?"":String.valueOf(meta.getEpisode())));	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
		streamWriter.writeEndElement();	
	}			
}
