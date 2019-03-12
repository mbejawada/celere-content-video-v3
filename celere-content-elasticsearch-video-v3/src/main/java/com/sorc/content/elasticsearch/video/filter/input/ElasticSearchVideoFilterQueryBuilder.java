/**
 * 
 */
package com.sorc.content.elasticsearch.video.filter.input;

import java.util.Arrays;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;

import com.sorc.content.elasticsearch.core.constant.ElasticSearchVideoFieldConstants;
import com.sorc.content.elasticsearch.core.filter.input.CustomFilterBuilder;
import com.sorc.content.elasticsearch.core.filter.input.ExistsFilter;
import com.sorc.content.elasticsearch.core.filter.input.IElasticSearchFilter;
import com.sorc.content.elasticsearch.core.filter.input.IElasticSearchQueryBuilder;
import com.sorc.content.elasticsearch.core.filter.input.NumericRangeFilter;
import com.sorc.content.elasticsearch.core.filter.input.RangeFilter;
import com.sorc.content.elasticsearch.core.filter.input.TermFilter;
import com.sorc.content.elasticsearch.core.filter.input.TermsFilter;
import com.sorc.content.elasticsearch.core.util.PropertiesUtil;
import com.sorc.content.video.filter.input.ElasticSearchVideoFilter;

/**
 * @author rakesh.moradiya
 *
 */
public class ElasticSearchVideoFilterQueryBuilder implements IElasticSearchQueryBuilder, IElasticSearchFilter {

	private ElasticSearchVideoFilter filter;
	private BoolQueryBuilder boolQueryBuilder;
	private CustomFilterBuilder fb;
	private final String lowercase_analyzer = "keyword_lowercase";
	private static String searchableFields = PropertiesUtil.getProperty(ElasticSearchVideoFieldConstants.SEARCHABLE_FIELDS);
	private static String[] searchableFieldsArray = getSearchableFieldArray(searchableFields);
	
	public ElasticSearchVideoFilterQueryBuilder(ElasticSearchVideoFilter filter) {
		this.filter = filter;
		fb = new CustomFilterBuilder();
	}
	
	
	@Override
	public BoolQueryBuilder buildQuery() {	
		
		if (filter.getWebsiteIds() != null) {
			buildBoolQueryFilter(new TermsFilter("websiteIds", filter.getWebsiteIds()));
		}
		
		if(filter.getStatus() != null) {
			buildBoolQueryFilter(new TermFilter("status.keyword", filter.getStatus()));
		}
		
		if(filter.getMainCategory() != null) {
			buildBoolQueryFilter(new TermFilter("mainCategory.keyword", filter.getMainCategory()));
		}
		
		if (filter.getMainCategoryNotIn() != null) {
			buildBoolQueryFilter(new TermsFilter("mainCategory.keyword", filter.getMainCategoryNotIn()), true);
		}
		
		if(filter.getVideoDuration() != null) {			
			buildBoolQueryFilter(new RangeFilter("video.duration", filter.getVideoDuration(), null));
		}
		
		if(filter.getCountryCode() != null) {		
			
			BoolQueryBuilder countryGlobalBoolQueryBuilder = QueryBuilders.boolQuery();	
			countryGlobalBoolQueryBuilder.mustNot(fb.createFilter(new ExistsFilter("accessControl")));
			
			BoolQueryBuilder countryAllowBoolQueryBuilder = QueryBuilders.boolQuery();	
			countryAllowBoolQueryBuilder.filter(fb.createFilter(new TermFilter("accessControl.values.keyword", filter.getCountryCode())));
			countryAllowBoolQueryBuilder.filter(fb.createFilter(new TermFilter("accessControl.allow", true)));
			
			BoolQueryBuilder countryNotAllowBoolQueryBuilder = QueryBuilders.boolQuery();	
			countryNotAllowBoolQueryBuilder.mustNot(fb.createFilter(new TermFilter("accessControl.values.keyword", filter.getCountryCode())));
			countryNotAllowBoolQueryBuilder.filter(fb.createFilter(new TermFilter("accessControl.allow", false)));
				
			if(boolQueryBuilder == null)
				boolQueryBuilder = QueryBuilders.boolQuery();		
			boolQueryBuilder.should(countryGlobalBoolQueryBuilder);
			boolQueryBuilder.should(countryAllowBoolQueryBuilder);
			boolQueryBuilder.should(countryNotAllowBoolQueryBuilder);
		}
		
		if(filter.getVideoId()!= null) {			
			buildBoolQueryFilter(new TermFilter("id.keyword", filter.getVideoId()));
		}
		
		if(filter.getShowId() != null) {
			buildBoolQueryFilter(new TermFilter("categories.id", filter.getShowId()));
		}
		
		if(filter.getSeasonId() != null) {
			buildBoolQueryFilter(new TermFilter("categories.id", filter.getSeasonId()));
		}
		
		if(filter.getShowName() != null) {
			buildBoolQueryFilter(new TermFilter("show.keyword", filter.getShowName()));
		}
		else if(filter.getShowCategory() != null) {
			buildBoolQueryFilter(new TermFilter("show.keyword", filter.getShowCategory()));
		}
		
		if(filter.getSeasonName() != null) {
			buildBoolQueryFilter(new TermFilter("season.keyword", filter.getSeasonName()));
		}
		
		if(filter.getEpisodeNum() != null) {
			buildBoolQueryFilter(new NumericRangeFilter("meta.episode", filter.getEpisodeNum(), null));
		}				
		
		if(filter.getText() != null)
		{			
			if(isExactMatchAnalyzer(filter.getText()))
				boolQueryBuilder.filter(new MultiMatchQueryBuilder(filter.getText().replaceAll("\"", ""), searchableFieldsArray).type(Type.PHRASE_PREFIX).operator(Operator.AND).analyzer(lowercase_analyzer));
			else
			{				
				boolQueryBuilder.filter(new MultiMatchQueryBuilder(filter.getText(), searchableFieldsArray).type(Type.PHRASE_PREFIX).slop(50).operator(Operator.AND).analyzer(lowercase_analyzer));				
			}
		}
		
		return boolQueryBuilder;
	}

	private void buildBoolQueryFilter(IElasticSearchFilter currFilter) {
		buildBoolQueryFilter(currFilter, false);
	}
	
	private void buildBoolQueryFilter(IElasticSearchFilter currFilter, boolean mustNotFilter) {
		if (boolQueryBuilder == null) {
			boolQueryBuilder = QueryBuilders.boolQuery();		
			if(mustNotFilter)
				boolQueryBuilder.mustNot(fb.createFilter(currFilter));
			else
				boolQueryBuilder.filter(fb.createFilter(currFilter));
		} else {
			if(mustNotFilter)
				boolQueryBuilder.mustNot(fb.createFilter(currFilter));
			else
				boolQueryBuilder.filter(fb.createFilter(currFilter));
		}
	}
	
	private boolean isExactMatchAnalyzer(String queryText) {
		if(queryText.startsWith("\"") && queryText.endsWith("\"")){
			return true;
		}
		return false;
	}
	
	private static String[] getSearchableFieldArray(String fields)
	{		
		if(fields != null)
		{
			return fields.split(",");
		}
		return null;
	}
	
	private List<String> getSearchableTextList(String text)
	{		
		if(text != null)
		{
			return Arrays.asList(text.split(" "));
		}
		return null;
	}
}