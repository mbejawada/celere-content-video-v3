package com.sorc.content.services.video.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sorc.content.core.pagination.Pagination;
import com.sorc.content.core.sort.SortingMode;
import com.sorc.content.elasticsearch.core.constant.ElasticSearchVideoFieldConstants;
import com.sorc.content.elasticsearch.core.dao.IElasticSearchDataAccess;
import com.sorc.content.elasticsearch.core.dto.ElasticSearchFilterDataTransfer;
import com.sorc.content.elasticsearch.core.sort.IElasticSearchSorting;
import com.sorc.content.elasticsearch.video.sort.ElasticSearchVideoSorting;
import com.sorc.content.services.documentation.ServicesCommonDocumentation;
import com.sorc.content.services.request.IHttpRequestHandler;
import com.sorc.content.services.request.QueryParameters;
import com.sorc.content.services.request.QueryParametersPaginationSorting;
import com.sorc.content.services.response.Result;
import com.sorc.content.services.validation.NotEmpty;
import com.sorc.content.services.video.documentation.constants.VideoConstants;
import com.sorc.content.services.video.documentation.constants.VideoDocumentationParameters;
import com.sorc.content.services.video.request.VideoParameterValidator;
import com.sorc.content.services.video.request.VideoQueryParameters;
import com.sorc.content.services.video.util.AppleUmcLiveEventAvailabilityResultAssembler;
import com.sorc.content.services.video.util.AppleUmcLiveEventCatalogResultAssembler;
import com.sorc.content.video.dao.data.ElasticSearchVideo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * @author rakesh.moradiya
 *
 */
@Path("/")
@Api(value = "/1.0", description = "Video Resources 3.0")
@Service
public class VideoResource3_0 {

	Logger logger = LoggerFactory.getLogger(getClass());

	private static final String INDEX = "mtod_video";

	@Autowired
	private IElasticSearchDataAccess dao;
	
	@Autowired
	private IHttpRequestHandler httpRequestHandler;

	public IElasticSearchDataAccess getDao() {
		return dao;
	}

	public void setDao(IElasticSearchDataAccess dao) {
		this.dao = dao;
	}

	public IHttpRequestHandler getHttpRequestHandler() {
		return httpRequestHandler;
	}

	public void setHttpRequestHandler(IHttpRequestHandler httpRequestHandler) {
		this.httpRequestHandler = httpRequestHandler;
	}
		
	@SuppressWarnings("unchecked")
	@GET
	@Path("/")
	@ApiOperation(value = "Find list of Videos", notes = "Returns the list of video", response = ElasticSearchVideo.class, position = 1, httpMethod="GET")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Missing website ID"), @ApiResponse(code = 400, message = "Missing Action Name"), @ApiResponse(code = 404, message = "Resource not found")})
	@Produces({ MediaType.APPLICATION_JSON })
	public Result<ElasticSearchVideo> getElasticSearchVideoList(
			@ApiParam(value = ServicesCommonDocumentation.WEBSITEID, required = true) @NotEmpty(QueryParameters.WEBSITE_IDS) @QueryParam(QueryParameters.WEBSITE_IDS) final Set<Integer> websiteIds,			
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_PAGE, required = false) @DefaultValue(VideoQueryParameters.DEFAULT_QUERY_PARAM_PAGE) @QueryParam(VideoQueryParameters.QUERY_PARAM_PAGE) int page,
			@ApiParam(value = ServicesCommonDocumentation.PAGINATION, required = false) @DefaultValue(QueryParametersPaginationSorting.DEFAULT_PAGINATION) @QueryParam(QueryParametersPaginationSorting.QUERY_PARAM_SIZE) int size)
			throws JsonParseException, JsonMappingException, IOException,
			Exception {
		
		ElasticSearchFilterDataTransfer esfdt = new ElasticSearchFilterDataTransfer();
		esfdt.setPagination(new Pagination(size, (page-1)*size));
		esfdt.setIndex(INDEX);
		esfdt.setFilters(VideoParameterValidator.validateCustomParameters(websiteIds, null, null));
		
		List<IElasticSearchSorting> sorting = new ArrayList<IElasticSearchSorting>();
		sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_UPDATED_AT, SortingMode.DESCENDING));		
		esfdt.setSorting(sorting);
		
		Map<String, Object> resultMap = dao.getDetailList(esfdt);
		Long totalCount = 0L;
		List<ElasticSearchVideo> videoList = new ArrayList<ElasticSearchVideo>();
		if(resultMap != null && !resultMap.isEmpty()) {
			if(resultMap.get(ElasticSearchVideoFieldConstants.TOTAL_COUNT) != null 
					&& resultMap.get(ElasticSearchVideoFieldConstants.ELASTICSEARCH_VIDEO_LIST) != null) {
				totalCount = (Long) resultMap.get(ElasticSearchVideoFieldConstants.TOTAL_COUNT);
				videoList = (List<ElasticSearchVideo>) resultMap.get(ElasticSearchVideoFieldConstants.ELASTICSEARCH_VIDEO_LIST);
			}
		}
		
		return new Result<ElasticSearchVideo>(totalCount, videoList, httpRequestHandler.getCorrelationId());
	}
	
	@GET
	@Path("/appleUmc/catalog")
	@ApiOperation(value = "Find Unique Show", notes = "Returns the list of video with unique show and season", response = ElasticSearchVideo.class, position = 2, httpMethod="GET")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Missing website ID"), @ApiResponse(code = 400, message = "Missing Action Name"), @ApiResponse(code = 404, message = "Resource not found")})
	@Produces({ MediaType.TEXT_XML })
	public Object getAppleUmcCatlogFeedData(
			@ApiParam(value = ServicesCommonDocumentation.WEBSITEID, required = true) @NotEmpty(QueryParameters.WEBSITE_IDS) @QueryParam(QueryParameters.WEBSITE_IDS) final Set<Integer> websiteIds
			) throws JsonParseException, JsonMappingException, IOException,Exception 
	{
		Set<String> mainCategoryNotIn = new HashSet<String>();
		mainCategoryNotIn.add(VideoConstants.CATEGORY_MOTORSPORTS);
		
		ElasticSearchFilterDataTransfer esfdt = new ElasticSearchFilterDataTransfer();
		List<String> facetFieldsList = new ArrayList<String>();
		facetFieldsList.add(VideoConstants.SHOW);
		facetFieldsList.add(VideoConstants.SEASON);
		facetFieldsList.add(VideoConstants.UPDATED_AT);
		facetFieldsList.add(VideoConstants.CATEGORIES);
		facetFieldsList.add(VideoConstants.MAIN_CATEGORY);
		facetFieldsList.add(VideoConstants.META);
		facetFieldsList.add(VideoConstants.VIDEO);		
		facetFieldsList.add(VideoConstants.PERSON);
		facetFieldsList.add(VideoConstants.SHOW_PARENTAL_RATING);	
		facetFieldsList.add(VideoConstants.SHOW_GENRY);	
		esfdt.setFacetFields(facetFieldsList);
		
		esfdt.setFacets(VideoConstants.FACET_SHOW);
		esfdt.setPagination(new Pagination(0, 0));
		esfdt.setIndex(INDEX);
		esfdt.setFilters(VideoParameterValidator.validateCustomParameters(websiteIds, null, mainCategoryNotIn));
		
		List<String> additionalFacetColumns = new ArrayList<String>();
		additionalFacetColumns.add(VideoConstants.FACET_SEASON);
		esfdt.setAdditionalFacetColumns(additionalFacetColumns);
		
		List<IElasticSearchSorting> aggDetailSorting = new ArrayList<IElasticSearchSorting>();
		aggDetailSorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_SHOW, SortingMode.ASCENDING));
		aggDetailSorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_SEASON, SortingMode.ASCENDING));
		esfdt.setAggDetailSorting(aggDetailSorting);
		
		List<IElasticSearchSorting> sorting = new ArrayList<IElasticSearchSorting>();
		sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_SHOW, SortingMode.ASCENDING));
		esfdt.setSorting(sorting);
		
		List<Object> modelsHeaderList = dao.getDetailFacetList(esfdt, VideoConstants.CALL_TYPE_APPLEUMC_CATALOG);
		return modelsHeaderList.get(0);
	}
	
	@GET
	@Path("/appleUmc/availability")
	@ApiOperation(value = "Find Unique Show", notes = "Returns the list of video with unique show and season", response = ElasticSearchVideo.class, position = 3, httpMethod="GET")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Missing website ID"), @ApiResponse(code = 400, message = "Missing Action Name"), @ApiResponse(code = 404, message = "Resource not found")})
	@Produces({ MediaType.TEXT_XML })
	public Object getAppleUmcAvailabilityFeedData(
			@ApiParam(value = ServicesCommonDocumentation.WEBSITEID, required = true) @NotEmpty(QueryParameters.WEBSITE_IDS) @QueryParam(QueryParameters.WEBSITE_IDS) final Set<Integer> websiteIds
			) throws JsonParseException, JsonMappingException, IOException,Exception 
	{
		Set<String> mainCategoryNotIn = new HashSet<String>();
		mainCategoryNotIn.add(VideoConstants.CATEGORY_MOTORSPORTS);
		
		ElasticSearchFilterDataTransfer esfdt = new ElasticSearchFilterDataTransfer();
		List<String> facetFieldsList = new ArrayList<String>();
		
		facetFieldsList.add(VideoConstants.VIDEO);	
		facetFieldsList.add(VideoConstants.UPDATED_AT);
		facetFieldsList.add(VideoConstants.CATEGORIES);
		facetFieldsList.add(VideoConstants.META);
		facetFieldsList.add(VideoConstants.SHOW);
		facetFieldsList.add(VideoConstants.SEASON);		
		facetFieldsList.add(VideoConstants.MAIN_CATEGORY);					
		facetFieldsList.add(VideoConstants.ACCESS_CONTROL);
		facetFieldsList.add(VideoConstants.CLOSED_CAPTION);			
		esfdt.setFacetFields(facetFieldsList);
		
		esfdt.setFacets(VideoConstants.FACET_SHOW);
		esfdt.setPagination(new Pagination(0, 0));
		esfdt.setIndex(INDEX);
		esfdt.setFilters(VideoParameterValidator.validateCustomParameters(websiteIds, null, mainCategoryNotIn));
		
		List<String> additionalFacetColumns = new ArrayList<String>();
		additionalFacetColumns.add(VideoConstants.FACET_SEASON);
		esfdt.setAdditionalFacetColumns(additionalFacetColumns);
		
		List<IElasticSearchSorting> aggDetailSorting = new ArrayList<IElasticSearchSorting>();
		aggDetailSorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_SHOW, SortingMode.ASCENDING));
		aggDetailSorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_SEASON, SortingMode.ASCENDING));
		esfdt.setAggDetailSorting(aggDetailSorting);
		
		List<IElasticSearchSorting> sorting = new ArrayList<IElasticSearchSorting>();
		sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_SHOW, SortingMode.ASCENDING));
		esfdt.setSorting(sorting);
		
		List<Object> modelsHeaderList = dao.getDetailFacetList(esfdt, VideoConstants.CALL_TYPE_APPLEUMC_AVAILABILITY);
		return modelsHeaderList.get(0);
	}
		
	@GET
	@Path("/appleUmc/liveEventCatalog")
	@ApiOperation(value = "Find list of Live Events", notes = "Returns the list of Live Events", response = ElasticSearchVideo.class, position = 4, httpMethod="GET")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Missing website ID"), @ApiResponse(code = 400, message = "Missing Action Name"), @ApiResponse(code = 404, message = "Resource not found")})
	@Produces({ MediaType.TEXT_XML })
	public Object getAppleUmcLiveEventCatalogFeedData(
			@ApiParam(value = ServicesCommonDocumentation.WEBSITEID, required = true) @NotEmpty(QueryParameters.WEBSITE_IDS) @QueryParam(QueryParameters.WEBSITE_IDS) final Set<Integer> websiteIds
			)  throws JsonParseException, JsonMappingException, IOException,Exception 
	{	
		ElasticSearchFilterDataTransfer esfdt = new ElasticSearchFilterDataTransfer();
		esfdt.setPagination(new Pagination(VideoConstants.MAX_RESULT_SIZE, 0));
		esfdt.setIndex(INDEX);
		esfdt.setFilters(VideoParameterValidator.validateCustomParameters(websiteIds, VideoConstants.CATEGORY_WATCH_LIVE, null));
		
		List<IElasticSearchSorting> sorting = new ArrayList<IElasticSearchSorting>();
		sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_UPDATED_AT, SortingMode.DESCENDING));		
		esfdt.setSorting(sorting);
		
		Map<String, Object> resultMap = dao.getDetailList(esfdt);
		Long totalCount = 0L;
		List<ElasticSearchVideo> videoList = new ArrayList<ElasticSearchVideo>();
		if(resultMap != null && !resultMap.isEmpty()) {
			if(resultMap.get(ElasticSearchVideoFieldConstants.TOTAL_COUNT) != null 
					&& resultMap.get(ElasticSearchVideoFieldConstants.ELASTICSEARCH_VIDEO_LIST) != null) {
				totalCount = (Long) resultMap.get(ElasticSearchVideoFieldConstants.TOTAL_COUNT);
				videoList = (List<ElasticSearchVideo>) resultMap.get(ElasticSearchVideoFieldConstants.ELASTICSEARCH_VIDEO_LIST);
			}
		}
		
		return AppleUmcLiveEventCatalogResultAssembler.getAppleUmcLiveEventCatalogFeedData(videoList, totalCount);
	}
	
	@GET
	@Path("/appleUmc/liveEventAvailability")
	@ApiOperation(value = "Find list of Live Events", notes = "Returns the list of Live Events", response = ElasticSearchVideo.class, position = 4, httpMethod="GET")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Missing website ID"), @ApiResponse(code = 400, message = "Missing Action Name"), @ApiResponse(code = 404, message = "Resource not found")})
	@Produces({ MediaType.TEXT_XML })
	public Object getAppleUmcLiveEventAvailabilityFeedData(
			@ApiParam(value = ServicesCommonDocumentation.WEBSITEID, required = true) @NotEmpty(QueryParameters.WEBSITE_IDS) @QueryParam(QueryParameters.WEBSITE_IDS) final Set<Integer> websiteIds
			)  throws JsonParseException, JsonMappingException, IOException,Exception 
	{	
		ElasticSearchFilterDataTransfer esfdt = new ElasticSearchFilterDataTransfer();
		esfdt.setPagination(new Pagination(VideoConstants.MAX_RESULT_SIZE, 0));
		esfdt.setIndex(INDEX);
		esfdt.setFilters(VideoParameterValidator.validateCustomParameters(websiteIds, VideoConstants.CATEGORY_WATCH_LIVE, null));
		
		List<IElasticSearchSorting> sorting = new ArrayList<IElasticSearchSorting>();
		sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_UPDATED_AT, SortingMode.DESCENDING));		
		esfdt.setSorting(sorting);
		
		Map<String, Object> resultMap = dao.getDetailList(esfdt);
		Long totalCount = 0L;
		List<ElasticSearchVideo> videoList = new ArrayList<ElasticSearchVideo>();
		if(resultMap != null && !resultMap.isEmpty()) {
			if(resultMap.get(ElasticSearchVideoFieldConstants.TOTAL_COUNT) != null 
					&& resultMap.get(ElasticSearchVideoFieldConstants.ELASTICSEARCH_VIDEO_LIST) != null) {
				totalCount = (Long) resultMap.get(ElasticSearchVideoFieldConstants.TOTAL_COUNT);
				videoList = (List<ElasticSearchVideo>) resultMap.get(ElasticSearchVideoFieldConstants.ELASTICSEARCH_VIDEO_LIST);
			}
		}
		
		return AppleUmcLiveEventAvailabilityResultAssembler.getAppleUmcLiveEventAvailabilityFeedData(videoList, totalCount);
	}
}
