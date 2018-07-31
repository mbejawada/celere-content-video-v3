/**
 * 
 */
package com.sorc.content.elasticsearch.core.util;

import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation.Entry;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
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
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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
		        		} /*else if(key != null && count < 4) {
		        			resultFacetList.add(Integer.valueOf(key));
		        			count++;
		        		}*/
	        		}
	        	}
	        }
		}
		return resultFacetList;
	}
	
	public static List<Object> getElasticSearchAppleumvFeedDetailFacetResult(String facet, String subAggFacet, BoolQueryBuilder filters, SearchResult result) throws Exception {
		List<Object> resultFacetList = new ArrayList<Object>();
		
		
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
		createUmcCatlogNode(streamWriter, eventFactory, "", "");
								
		if(result != null && result.getAggregations() != null && result.getAggregations().getTermsAggregation(facet) != null) 
		{
			
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
	
	private static void createUmcCatlogNode(XMLStreamWriter streamWriter, XMLEventFactory eventFactory, String teamId, String catalogId) throws XMLStreamException{		
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
	
	public static String isStringNull(String param) {
		if(param != null) {
			return param;
		}
		return "";
	}		
}
