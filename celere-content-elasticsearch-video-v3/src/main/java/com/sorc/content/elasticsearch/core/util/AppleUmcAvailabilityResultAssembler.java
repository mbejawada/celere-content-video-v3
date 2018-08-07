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
	
	
		
	public static List<Object> getElasticSearchAppleUmcAvailabilityFeedDetailFacetResult(String facet, String subAggFacet, BoolQueryBuilder filters, SearchResult result) throws Exception {
		List<Object> resultFacetList = new ArrayList<Object>();
		
		
		resultFacetList.add("");		
		return resultFacetList;
	}
	
}
