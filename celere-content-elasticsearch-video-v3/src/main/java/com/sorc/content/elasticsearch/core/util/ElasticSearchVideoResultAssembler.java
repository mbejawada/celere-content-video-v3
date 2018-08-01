package com.sorc.content.elasticsearch.core.util;

import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation.Entry;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private static SimpleDateFormat sdf_yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd");
	private static TimeZone utc = TimeZone.getTimeZone("UTC");
	
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
	
	public static List<Object> getElasticSearchAppleumvFeedDetailFacetResult(String facet, String subAggFacet, BoolQueryBuilder filters, SearchResult result) throws Exception {
		List<Object> resultFacetList = new ArrayList<Object>();
		
		String showContentId = null;
		String seasonContentId = null;
		boolean showNodeAdded = false;
		boolean seasonNodeAdded = false;
		
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
						
			createNode(streamWriter, AppleXmlFeedConstants.TITLE, AppleXmlFeedConstants.ROOT_TITLE);
			createNode(streamWriter, AppleXmlFeedConstants.DESCRIPTION, AppleXmlFeedConstants.ROOT_DESCRIPTION);
			createNode(streamWriter, AppleXmlFeedConstants.DEFALUT_LOCALE, AppleXmlFeedConstants.ROOT_DEFALUT_LOCALE);
	        if(terms != null && terms.getBuckets() != null && terms.getBuckets().size() > 0) 
	        {	        		        	        	
	        	for(Entry bucket : terms.getBuckets()) {		        		
	        		showNodeAdded = false;
	        		seasonNodeAdded = false;	        		
	        		if(bucket != null && bucket.getKey() != null && bucket.getCount() != null) {	 	        				        				        		        			
	        			TermsAggregation subTerms = bucket.getAggregation(subAggFacet, TermsAggregation.class);
	        			if(subTerms != null && subTerms.getBuckets() != null && subTerms.getBuckets().size() > 0) {
	        				//recordCount += subTerms.getBuckets().size();	        				
	        				for(Entry subBucket : subTerms.getBuckets()) {
	        					seasonNodeAdded = false;
	        					if(subBucket != null && subBucket.getKey() != null && subBucket.getCount() != null) {	 
				        			List<String> topHitsList = subBucket.getTopHitsAggregation(facet+subAggFacet).getSourceAsStringList();
				        			showContentId = null;
				        			seasonContentId = null;
				        			recordCount += topHitsList.size();				        			
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
												seasonContentId = createSeasonwImformation(streamWriter, eventFactory, elasticSearchVideo, showContentId, subBucket.getKey());
												seasonNodeAdded = true;
												recordCount += 1;
											}
											
											createEpisodeImformation(streamWriter, eventFactory, elasticSearchVideo, showContentId, seasonContentId);											
											
										} catch (Exception e) {
											logger.error(e.getMessage());
											e.printStackTrace();
										}																				
				        			}				        					        		
	        					}
	        					else
	        					{
	        						//System.out.println("No episode found for :"+subBucket.getKey());
	        					}
	        				}
	        			}	
	        			else
	        			{
	        				//System.out.println("No Season Found for	:"+bucket.getKey());
	        				//System.out.println("Missing Docs:	"+bucket.getCount());
	        			}
	        		}        			
	        	}
	        	//System.out.println("TOtal Records:"+j);
	        }
		}
		else
		{
			createNode(streamWriter, AppleXmlFeedConstants.TOTAL_ITEM_COUNT, "0");
			createNode(streamWriter, AppleXmlFeedConstants.TITLE, AppleXmlFeedConstants.ROOT_TITLE);
			createNode(streamWriter, AppleXmlFeedConstants.DESCRIPTION, AppleXmlFeedConstants.ROOT_DESCRIPTION);
			createNode(streamWriter, AppleXmlFeedConstants.DEFALUT_LOCALE, AppleXmlFeedConstants.ROOT_DEFALUT_LOCALE);
		}
		createNode(streamWriter, AppleXmlFeedConstants.TOTAL_ITEM_COUNT, String.valueOf(recordCount));
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeEndElement();;
		
		resultFacetList.add(output.toString());		
		return resultFacetList;
	}
	
	private static void createUmcCatlogNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, 
			String teamId, String catalogId) throws XMLStreamException{		
		streamWriter.writeStartElement(AppleXmlFeedConstants.UMC_CATALOG);
		streamWriter.writeAttribute(AppleXmlFeedConstants.XMLNS_UMC, AppleXmlFeedConstants.XMLNS_UMC_VAL);
		streamWriter.writeAttribute(AppleXmlFeedConstants.VERSION, AppleXmlFeedConstants.VERSION_VAL);
		streamWriter.writeAttribute(AppleXmlFeedConstants.TEAM_ID, isStringNull(teamId));		
		streamWriter.writeAttribute(AppleXmlFeedConstants.CATALOG_ID, isStringNull(catalogId));			
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
					if(category.getDisplayName() != null && category.getDisplayName().equals(showName))
					{
						showContentId = (category.getId()==null?"":String.valueOf(category.getId()));
						streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
						streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
						
						streamWriter.writeStartElement(AppleXmlFeedConstants.ITEM);
						streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_TYPE, AppleXmlFeedConstants.TV_SHOW);
						streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_ID, showContentId);
				
						createNode(streamWriter, AppleXmlFeedConstants.PUB_DATE, isStringNull(category.getCreatedAt()));		
						createNodeWithLocale(streamWriter, eventFactory, AppleXmlFeedConstants.TITLE, isStringNull(category.getDisplayName()));
						createNodeWithLocale(streamWriter, eventFactory, AppleXmlFeedConstants.DESCRIPTION, isStringNull(category.getDescription()));
						createNode(streamWriter, AppleXmlFeedConstants.GENRE, getGenre(elasticSearchVideo));		
						
						//Populating Rating
						streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
						streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
						streamWriter.writeStartElement(AppleXmlFeedConstants.RATING);
						streamWriter.writeAttribute(AppleXmlFeedConstants.SYSTEM_CODE, AppleXmlFeedConstants.SYSTEM_CODE_VAL);		
						streamWriter.writeCharacters(AppleXmlFeedConstants.RATING_VAL_PREFIX+isStringNull(elasticSearchVideo.getMeta()==null?"":elasticSearchVideo.getMeta().getParentalRating()));	
						streamWriter.writeEndElement();	
						
						createArtWorkNode(streamWriter, eventFactory, isStringNull(category.getAppleUmcUrl()), AppleXmlFeedConstants.ARTWORK_TYPE_COVERAGE_SQUARE);		
						createNode(streamWriter, AppleXmlFeedConstants.IS_ORIGINAL, AppleXmlFeedConstants.TRUE);
						createTvShowInfoNode(streamWriter, eventFactory,  isStringNull(category.getCreatedAt()));	
						
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
		streamWriter.writeCharacters(isStringNull(createdAt));	
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
					if(category.getDisplayName() != null && category.getDisplayName().equals(seasonName))
					{
						seasonContentId = category.getId()==null?"":String.valueOf(category.getId());
						streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
						streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
						
						streamWriter.writeStartElement(AppleXmlFeedConstants.ITEM);
						streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_TYPE, AppleXmlFeedConstants.TV_SEASON);
						streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_ID, seasonContentId);
				
						createNode(streamWriter, AppleXmlFeedConstants.PUB_DATE, isStringNull(category.getCreatedAt()));		
						createNodeWithLocale(streamWriter, eventFactory, AppleXmlFeedConstants.TITLE, isStringNull(category.getDisplayName()));		
								
						createArtWorkNode(streamWriter, eventFactory, isStringNull(category.getAppleUmcUrl()), AppleXmlFeedConstants.ARTWORK_TYPE_COVERAGE_SQUARE);				
						createTvSeasonInfoNode(streamWriter, eventFactory, isStringNull(showContentId), (elasticSearchVideo.getMeta()==null?"":String.valueOf(elasticSearchVideo.getMeta().getSeason())), isStringNull(category.getCreatedAt()));		
						
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
		streamWriter.writeCharacters(isStringNull(contentId));	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.SEASON_NUMBER);
		streamWriter.writeCharacters(isStringNull(seasonNumber));	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.ORIGINAL_PREMIERE_DATE);
		streamWriter.writeCharacters(isStringNull(createdAt));	
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
			streamWriter.writeAttribute(AppleXmlFeedConstants.CONTENT_ID, isStringNull(elasticSearchVideo.getVideo()!=null?elasticSearchVideo.getVideo().getVmsId():null));
	
			createNode(streamWriter, AppleXmlFeedConstants.PUB_DATE, isStringNull(elasticSearchVideo.getUpdatedAt()));		
			createNodeWithLocale(streamWriter, eventFactory, AppleXmlFeedConstants.TITLE, isStringNull(elasticSearchVideo.getVideo()!=null?elasticSearchVideo.getVideo().getName():null));
			createNodeWithLocale(streamWriter, eventFactory, AppleXmlFeedConstants.DESCRIPTION, isStringNull(elasticSearchVideo.getVideo()!=null?elasticSearchVideo.getVideo().getDescription():null));		
					
			createArtWorkNode(streamWriter, eventFactory, isStringNull(elasticSearchVideo.getVideo()!=null?elasticSearchVideo.getVideo().getAppleUmcThumbnailUrl():null), AppleXmlFeedConstants.ARTWORK_TYPE_TITLE);		
			
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
		streamWriter.writeCharacters(isStringNull(video == null?"":String.valueOf(video.getDuration())));	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.ORIGINAL_AIR_DATE);
		streamWriter.writeCharacters(isStringNull(video == null?"":getYyyyMmDdStr(video.getStartDate())));	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.SHOW_CONTENT_ID);
		streamWriter.writeCharacters(isStringNull(showContentId));	
		streamWriter.writeEndElement();		
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.SEASON_CONTENT_ID);
		streamWriter.writeCharacters(isStringNull(seasonContentId));	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.SEASON_NUMBER);
		streamWriter.writeCharacters(isStringNull(meta == null?"":String.valueOf(meta.getSeason())));	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);	
		streamWriter.writeStartElement(AppleXmlFeedConstants.EPISODE_NUMBER);
		streamWriter.writeCharacters(isStringNull(meta == null?"":String.valueOf(meta.getEpisode())));	
		streamWriter.writeEndElement();
		
		streamWriter.writeDTD(AppleXmlFeedConstants.TAB);
		streamWriter.writeDTD(AppleXmlFeedConstants.NEW_LINE);
		streamWriter.writeEndElement();	
	}
	
	private static String getYyyyMmDdStr(String dateStr)
	{
		try
		{
			return sdf_yyyy_mm_dd.format(sdf_yyyy_mm_dd.parse(dateStr));
		}
		catch(Exception e)
		{
			return "";
		}
	}
	public static String isStringNull(String param) {
		if(param != null) {
			return param;
		}
		return "";
	}
	
	private static String getGenre(ElasticSearchVideo elasticSearchVideo)
	{
		if(elasticSearchVideo != null && elasticSearchVideo.getMainCategory() !=null && elasticSearchVideo.getMainCategory().trim().equals(AppleXmlFeedConstants.CATEGORY_MOTORSPORTS))			
		{
			return AppleXmlFeedConstants.GENRE_SPORTS;
		}
		
		return AppleXmlFeedConstants.GENRE_REALITY;
	}
}
