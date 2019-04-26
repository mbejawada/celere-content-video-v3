package com.sorc.content.services.video.resources;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sorc.content.core.dao.NotFoundException;
import com.sorc.content.core.dao.ValidationException;
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
import com.sorc.content.services.validation.NotEmpty;
import com.sorc.content.services.video.documentation.constants.VideoConstants;
import com.sorc.content.services.video.documentation.constants.VideoDocumentationParameters;
import com.sorc.content.services.video.request.VideoParameterValidator;
import com.sorc.content.services.video.request.VideoQueryParameters;
import com.sorc.content.services.video.response.Result;
import com.sorc.content.services.video.response.status.RestrictedAssetResource;
import com.sorc.content.services.video.response.status.RestrictedResource;
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
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_TEXT, required = false) @QueryParam(VideoQueryParameters.QUERY_PARAM_TEXT) String text,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_COUNTRY_CODE, required = false) @QueryParam(VideoQueryParameters.QUERY_PARAM_COUNTRY_CODE) String countryCode,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_STATUS, required = false) @QueryParam(VideoQueryParameters.QUERY_PARAM_STAUTS) String status,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_MEDIA_TYPE, required = false) @QueryParam(VideoQueryParameters.QUERY_PARAM_MEDIA_TYPE) String mediaType,
			@ApiParam(value = VideoDocumentationParameters.SORT_BY, required = false) @DefaultValue(VideoQueryParameters.DEFAULT_SORTING_START_DATE) @QueryParam(QueryParametersPaginationSorting.QUERY_PARAM_SORT_BY) String sortBy,
			@ApiParam(value = ServicesCommonDocumentation.SORTING_MODE, required = false) @DefaultValue(QueryParametersPaginationSorting.DESCENDING_SORTING_MODE) @QueryParam(QueryParametersPaginationSorting.QUERY_SORTING_MODE) SortingMode sortingMode,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_PAGE, required = false) @DefaultValue(VideoQueryParameters.DEFAULT_QUERY_PARAM_PAGE) @QueryParam(VideoQueryParameters.QUERY_PARAM_PAGE_INDEX) int pageIndex,			
			@ApiParam(value = ServicesCommonDocumentation.PAGINATION, required = false) @DefaultValue(QueryParametersPaginationSorting.DEFAULT_PAGINATION) @QueryParam(VideoQueryParameters.QUERY_PARAM_PAGE_SIZE) int pageSize)
			throws JsonParseException, JsonMappingException, IOException,
			Exception {
		
		if(status == null || status.trim().length() == 0)		
			status = VideoConstants.STATUS_READY;
		
		if(StringUtils.isNotEmpty(text))
			text = getDecodedString(text);
		
		ElasticSearchFilterDataTransfer esfdt = new ElasticSearchFilterDataTransfer();
		esfdt.setPagination(new Pagination(pageSize, (pageIndex-1)*pageSize));
		esfdt.setIndex(INDEX);
		esfdt.setFilters(VideoParameterValidator.validateCustomParameters(websiteIds, null, null, null, countryCode, null, status, text, null, null, null, null, null, null, null, null, null, mediaType, null, null, null));
				
		List<IElasticSearchSorting> sorting = new ArrayList<IElasticSearchSorting>();
		if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.DEFAULT_SORTING_EPISODE_NUM))
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_EPISODE_NO, sortingMode));
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_START_DATE))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_START_DATE, sortingMode));
		}
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_VIEWS))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_VIEWS, sortingMode));
		}
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_SORT_DATE))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_SORT_DATE, sortingMode));
		}
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
		
		return new Result<ElasticSearchVideo>(totalCount, videoList, pageIndex, pageSize, httpRequestHandler.getCorrelationId());
	}	
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/{" + VideoQueryParameters.QUERY_PARAM_VIDEO_ID + "}")
	@ApiOperation(value = "Find videos associated with country", notes = "Returns the video associated with country", response = ElasticSearchVideo.class, position = 2, httpMethod="GET")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Missing website ID"), @ApiResponse(code = 400, message = "Missing Action Name"), @ApiResponse(code = 404, message = "Resource not found")})
	@Produces({ MediaType.APPLICATION_JSON })
	public Object getElasticSearchVideo(
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_VIDEO_ID, required = true) @PathParam(VideoQueryParameters.QUERY_PARAM_VIDEO_ID) final String videoId,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_STATUS, required = false) @QueryParam(VideoQueryParameters.QUERY_PARAM_STAUTS) String status,
			@ApiParam(value = ServicesCommonDocumentation.WEBSITEID, required = true) @NotEmpty(QueryParameters.WEBSITE_IDS) @QueryParam(QueryParameters.WEBSITE_IDS) final Set<Integer> websiteIds,			
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_COUNTRY_CODE, required = true) @QueryParam(VideoQueryParameters.QUERY_PARAM_COUNTRY_CODE) String countryCode)
			throws JsonParseException, JsonMappingException, IOException,
			Exception {
		
		if(status == null || status.trim().length() == 0)		
			status = VideoConstants.STATUS_READY;
		
		ElasticSearchFilterDataTransfer esfdt = new ElasticSearchFilterDataTransfer();
		esfdt.setPagination(new Pagination(1, 0));
		esfdt.setIndex(INDEX);
		esfdt.setFilters(VideoParameterValidator.validateCustomParameters(websiteIds, null, null, null, null, videoId, status, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
		
		List<IElasticSearchSorting> sorting = new ArrayList<IElasticSearchSorting>();
		sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_UPDATED_AT, SortingMode.DESCENDING));		
		esfdt.setSorting(sorting);
		
		Map<String, Object> resultMap = dao.getDetailList(esfdt);
		
		List<ElasticSearchVideo> videoList = new ArrayList<ElasticSearchVideo>();
		if(resultMap != null && !resultMap.isEmpty()) {
			if(resultMap.get(ElasticSearchVideoFieldConstants.ELASTICSEARCH_VIDEO_LIST) != null) {				
				videoList = (List<ElasticSearchVideo>) resultMap.get(ElasticSearchVideoFieldConstants.ELASTICSEARCH_VIDEO_LIST);
				if(videoList.size() > 0)
				{
					if(videoList.get(0).getAccessControl() != null)
					{
						if(!videoList.get(0).getAccessControl().getAllow())
						{
							if(videoList.get(0).getAccessControl().getValues().contains(countryCode.toUpperCase()))
								return new RestrictedResource(videoList.get(0).getAccessControl().getMessage());
						}
						else
						{
							if(!videoList.get(0).getAccessControl().getValues().contains(countryCode.toUpperCase()))
								return new RestrictedResource(videoList.get(0).getAccessControl().getMessage());
						}		
					}
				}
			}
		}
		
		if(videoList != null && videoList.size() > 0)
			return videoList.get(0);		
		else throw new NotFoundException("video ["+videoId+"] is not found"); 
		
	}
	
	@GET
	@Path("/up_next")
	@ApiOperation(value = "Find next possible video belongs to show and season", notes = "Returns the list of video belongs to show and season", response = ElasticSearchVideo.class, position = 3, httpMethod="GET")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Missing website ID"), @ApiResponse(code = 400, message = "Missing Action Name"), @ApiResponse(code = 404, message = "Resource not found")})
	@Produces({ MediaType.APPLICATION_JSON })
	public Object getUpNextVideoFeedData(
			@ApiParam(value = ServicesCommonDocumentation.WEBSITEID, required = true) @NotEmpty(QueryParameters.WEBSITE_IDS) @QueryParam(QueryParameters.WEBSITE_IDS) final Set<Integer> websiteIds,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_VIDEO_ID, required = true) @QueryParam(VideoQueryParameters.QUERY_PARAM_VIDEO_ID) final String videoId,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_SHOW_CATEGORY_ID, required = false) @QueryParam(VideoQueryParameters.QUERY_PARAM_SHOW_CATEGORY_ID) Integer showCategoryId,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_SEASON_CATEGORY_ID, required = false) @QueryParam(VideoQueryParameters.QUERY_PARAM_SEASON_CATEGORY_ID) Integer seasonCategoryId,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_SHOW_CATEGORY, required = false) @QueryParam(VideoQueryParameters.QUERY_PARAM_SHOW_CATEGORY) String showCategory,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_COUNTRY_CODE, required = false) @QueryParam(VideoQueryParameters.QUERY_PARAM_COUNTRY_CODE) String countryCode,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_STATUS, required = false) @DefaultValue(VideoConstants.STATUS_READY) @QueryParam(VideoQueryParameters.QUERY_PARAM_STAUTS) String status,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_MEDIA_TYPE, required = false) @QueryParam(VideoQueryParameters.QUERY_PARAM_MEDIA_TYPE) String mediaType,
			@ApiParam(value = VideoDocumentationParameters.SORT_BY, required = false) @DefaultValue(VideoQueryParameters.DEFAULT_SORTING_START_DATE) @QueryParam(QueryParametersPaginationSorting.QUERY_PARAM_SORT_BY) String sortBy,
			@ApiParam(value = ServicesCommonDocumentation.SORTING_MODE, required = false) @DefaultValue(QueryParametersPaginationSorting.DEFAULT_SORTING_MODE) @QueryParam(QueryParametersPaginationSorting.QUERY_SORTING_MODE) SortingMode sortingMode,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_PAGE, required = false) @DefaultValue(VideoQueryParameters.DEFAULT_QUERY_PARAM_PAGE) @QueryParam(VideoQueryParameters.QUERY_PARAM_PAGE_INDEX) int pageIndex,			
			@ApiParam(value = ServicesCommonDocumentation.PAGINATION, required = false) @DefaultValue(QueryParametersPaginationSorting.DEFAULT_PAGINATION) @QueryParam(VideoQueryParameters.QUERY_PARAM_PAGE_SIZE) int pageSize
			) throws JsonParseException, JsonMappingException, IOException,Exception 
	{
		if(websiteIds == null || websiteIds.isEmpty())		
			throw new ValidationException("website_ids is required field");						
		
		ElasticSearchFilterDataTransfer esfdt = new ElasticSearchFilterDataTransfer();
				
		esfdt.setPagination(new Pagination(pageSize, 0));
		esfdt.setIndex(INDEX);		
		esfdt.setFilters(VideoParameterValidator.validateCustomParameters(websiteIds, null, null, null, null, videoId, status, null, showCategoryId, seasonCategoryId, null, null, null, showCategory, null, null, null, mediaType, null, null, null));
		
		List<IElasticSearchSorting> sorting = new ArrayList<IElasticSearchSorting>();
		if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.DEFAULT_SORTING_EPISODE_NUM))
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_EPISODE_NO, sortingMode));
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_START_DATE))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_START_DATE, sortingMode));
		}
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_VIEWS))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_VIEWS, sortingMode));
		}
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_SORT_DATE))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_SORT_DATE, sortingMode));
		}
		esfdt.setSorting(sorting);
		
		List<ElasticSearchVideo> videoList = new ArrayList<ElasticSearchVideo>();
		ElasticSearchVideo esVideo = dao.getVideoDetail(esfdt);
		
		Long totalCount = 0L;
		Map<String, Object> resultMap = null;		
		String nextShow = null;
		
		if(esVideo != null)
		{
			if(countryCode != null && countryCode.trim().length() > 0)
			{
				if(esVideo.getAccessControl() != null)
				{
					if(!esVideo.getAccessControl().getAllow())
					{
						if(esVideo.getAccessControl().getValues().contains(countryCode.toUpperCase()))
							return new com.sorc.content.services.response.Result<ElasticSearchVideo>(totalCount, videoList, httpRequestHandler.getCorrelationId());
					}
					else
					{
						if(!esVideo.getAccessControl().getValues().contains(countryCode.toUpperCase()))
							return new com.sorc.content.services.response.Result<ElasticSearchVideo>(totalCount, videoList, httpRequestHandler.getCorrelationId());
					}		
				}
			}
			
			//fetch next episodes list based on current show
			esfdt = new ElasticSearchFilterDataTransfer();
			
			esfdt.setPagination(new Pagination(pageSize, (pageIndex-1)*pageSize));
			esfdt.setIndex(INDEX);			
			esfdt.setFilters(VideoParameterValidator.validateCustomParameters(websiteIds, null, null, null, countryCode, null, status, null, null, null, esVideo.getShow(), null, null, showCategory, null, null, null, mediaType, null, null, esVideo.getVideo().getStartDate()));
			esfdt.setSorting(sorting);
			
			resultMap = dao.getDetailList(esfdt);
			if(resultMap != null && !resultMap.isEmpty()) {
				if(resultMap.get(ElasticSearchVideoFieldConstants.TOTAL_COUNT) != null 
						&& resultMap.get(ElasticSearchVideoFieldConstants.ELASTICSEARCH_VIDEO_LIST) != null) {
					totalCount = (Long) resultMap.get(ElasticSearchVideoFieldConstants.TOTAL_COUNT);
					videoList = (List<ElasticSearchVideo>) resultMap.get(ElasticSearchVideoFieldConstants.ELASTICSEARCH_VIDEO_LIST);					
				}
			}
			
			if(videoList == null || videoList.size() == 0)
			{				
				totalCount = 0L;
				
				//get next show based on alphabatical order 
				esfdt = new ElasticSearchFilterDataTransfer();
				esfdt.setPagination(new Pagination(0, 0));
				esfdt.setIndex(INDEX);	
				esfdt.setFacets(VideoConstants.FACET_SHOW);
				esfdt.setFilters(VideoParameterValidator.validateCustomParameters(websiteIds, esVideo.getMainCategory(), null, null, null, null, status, null, null, null, null, null, null, showCategory, null, null, null, null, null, null, null));
				esfdt.setSorting(sorting);
				List<IElasticSearchSorting> aggDetailSorting = new ArrayList<IElasticSearchSorting>();
				aggDetailSorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_SHOW, SortingMode.ASCENDING));				
				esfdt.setAggDetailSorting(aggDetailSorting);
				
				nextShow = dao.getNextShow(esfdt, esVideo.getShow());
				
				if(nextShow != null && nextShow.trim().length() > 0)
				{														
					//fetch the next episodes based on next show
					esfdt = new ElasticSearchFilterDataTransfer();
					
					esfdt.setPagination(new Pagination(pageSize, (pageIndex-1)*pageSize));
					esfdt.setIndex(INDEX);			
					esfdt.setFilters(VideoParameterValidator.validateCustomParameters(websiteIds, null, null, null, countryCode, null, status, null, null, null, nextShow, null, null, showCategory, null, null, null, mediaType, null, null, null));
					esfdt.setSorting(sorting);
					
					resultMap = dao.getDetailList(esfdt);
					if(resultMap != null && !resultMap.isEmpty()) {
						if(resultMap.get(ElasticSearchVideoFieldConstants.TOTAL_COUNT) != null 
								&& resultMap.get(ElasticSearchVideoFieldConstants.ELASTICSEARCH_VIDEO_LIST) != null) {
							totalCount = (Long) resultMap.get(ElasticSearchVideoFieldConstants.TOTAL_COUNT);
							videoList = (List<ElasticSearchVideo>) resultMap.get(ElasticSearchVideoFieldConstants.ELASTICSEARCH_VIDEO_LIST);					
						}
					}					
				}				
			}
		}
		
		return new Result<ElasticSearchVideo>(totalCount, videoList, pageIndex, pageSize, httpRequestHandler.getCorrelationId());				
	}	
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/by_season")
	@ApiOperation(value = "Find list of Videos", notes = "Returns the list of video", response = ElasticSearchVideo.class, position = 4, httpMethod="GET")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Missing website ID"), @ApiResponse(code = 400, message = "Missing Action Name"), @ApiResponse(code = 404, message = "Resource not found")})
	@Produces({ MediaType.APPLICATION_JSON })
	public Result<Object> getEpisodesBySeason(
			@ApiParam(value = ServicesCommonDocumentation.WEBSITEID, required = true) @NotEmpty(QueryParameters.WEBSITE_IDS) @QueryParam(QueryParameters.WEBSITE_IDS) final Set<Integer> websiteIds,			
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_SEASON_CATEGORY_ID, required = false) @QueryParam(VideoQueryParameters.QUERY_PARAM_SEASON_CATEGORY_ID) Integer seasonCategoryId,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_SEASON_NO, required = false) @QueryParam(VideoQueryParameters.QUERY_PARAM_SEASON_NUM) Integer seasonNo,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_SHOW_CATEGORY_ID, required = false) @QueryParam(VideoQueryParameters.QUERY_PARAM_SHOW_CATEGORY_ID) Integer showCategoryId,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_COUNTRY_CODE, required = false) @QueryParam(VideoQueryParameters.QUERY_PARAM_COUNTRY_CODE) String countryCode,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_PAGE, required = false) @DefaultValue(VideoQueryParameters.DEFAULT_QUERY_PARAM_PAGE) @QueryParam(VideoQueryParameters.QUERY_PARAM_PAGE_INDEX) int pageIndex,			
			@ApiParam(value = ServicesCommonDocumentation.PAGINATION, required = false) @DefaultValue(QueryParametersPaginationSorting.DEFAULT_PAGINATION) @QueryParam(VideoQueryParameters.QUERY_PARAM_PAGE_SIZE) int pageSize,
			@ApiParam(value = VideoDocumentationParameters.SORT_BY, required = false) @DefaultValue(VideoQueryParameters.DEFAULT_SORTING_EPISODE_NUM) @QueryParam(QueryParametersPaginationSorting.QUERY_PARAM_SORT_BY) String sortBy,
			@ApiParam(value = ServicesCommonDocumentation.SORTING_MODE, required = false) @DefaultValue(QueryParametersPaginationSorting.DESCENDING_SORTING_MODE) @QueryParam(QueryParametersPaginationSorting.QUERY_SORTING_MODE) SortingMode sortingMode)
			throws JsonParseException, JsonMappingException, IOException,
			Exception {
		
		String status = VideoConstants.STATUS_READY;			
		
		if(seasonCategoryId == null && showCategoryId == null)
		{
			throw new ValidationException("Either season_category_id or show_category_id are required");
		}
		
		if(countryCode == null || countryCode.trim().length() == 0)
			countryCode = VideoConstants.COUNTRY_US;
		
		ElasticSearchFilterDataTransfer esfdt = new ElasticSearchFilterDataTransfer();
		esfdt.setPagination(new Pagination(pageSize, (pageIndex-1)*pageSize));
		esfdt.setIndex(INDEX);
		esfdt.setFilters(VideoParameterValidator.validateCustomParameters(websiteIds, null, null, null, null, null, status, null, showCategoryId, seasonCategoryId, null, null, null, null, seasonNo, null, null, null, null, null, null));
		
		List<IElasticSearchSorting> sorting = new ArrayList<IElasticSearchSorting>();
		if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.DEFAULT_SORTING_EPISODE_NUM))
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_EPISODE_NO, sortingMode));
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_START_DATE))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_START_DATE, sortingMode));
		}
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_VIEWS))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_VIEWS, sortingMode));
		}
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_SORT_DATE))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_SORT_DATE, sortingMode));
		}
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
		
		List<Object> restrictedVideoList = new ArrayList<Object>();
		if(videoList != null && !videoList.isEmpty())
		{
			for(ElasticSearchVideo esVideo : videoList)
			{
				if(esVideo.getAccessControl() != null)
				{
					if(!esVideo.getAccessControl().getAllow())
					{
						if(esVideo.getAccessControl().getValues().contains(countryCode.toUpperCase()))
							restrictedVideoList.add(new RestrictedAssetResource(esVideo.getId(), esVideo.getAccessControl().getMessage()));
						else
							restrictedVideoList.add(esVideo);
					}
					else
					{
						if(!esVideo.getAccessControl().getValues().contains(countryCode.toUpperCase()))
							restrictedVideoList.add(new RestrictedAssetResource(esVideo.getId(), esVideo.getAccessControl().getMessage()));
						else
							restrictedVideoList.add(esVideo);
					}		
				}
				else
					restrictedVideoList.add(esVideo);
			}
		}
		
		return new Result<Object>(totalCount, restrictedVideoList, pageIndex, pageSize, httpRequestHandler.getCorrelationId());
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/by_tag")
	@ApiOperation(value = "Find list of Videos", notes = "Returns the list of video", response = ElasticSearchVideo.class, position = 5, httpMethod="GET")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Missing website ID"), @ApiResponse(code = 400, message = "Missing Action Name"), @ApiResponse(code = 404, message = "Resource not found")})
	@Produces({ MediaType.APPLICATION_JSON })
	public Result<Object> getVideoListByTag(
			@ApiParam(value = ServicesCommonDocumentation.WEBSITEID, required = true) @NotEmpty(QueryParameters.WEBSITE_IDS) @QueryParam(QueryParameters.WEBSITE_IDS) final Set<Integer> websiteIds,				
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_TAGS_IN, required = true) @QueryParam(VideoQueryParameters.QUERY_PARAM_TAGS_IN) Set<String> tagsIn,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_COUNTRY_CODE, required = false) @QueryParam(VideoQueryParameters.QUERY_PARAM_COUNTRY_CODE) String countryCode,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_PAGE, required = false) @DefaultValue(VideoQueryParameters.DEFAULT_QUERY_PARAM_PAGE) @QueryParam(VideoQueryParameters.QUERY_PARAM_PAGE_INDEX) int pageIndex,			
			@ApiParam(value = ServicesCommonDocumentation.PAGINATION, required = false) @DefaultValue(QueryParametersPaginationSorting.DEFAULT_PAGINATION) @QueryParam(VideoQueryParameters.QUERY_PARAM_PAGE_SIZE) int pageSize,
			@ApiParam(value = VideoDocumentationParameters.SORT_BY, required = false) @DefaultValue(VideoQueryParameters.DEFAULT_SORTING_START_DATE) @QueryParam(QueryParametersPaginationSorting.QUERY_PARAM_SORT_BY) String sortBy,
			@ApiParam(value = ServicesCommonDocumentation.SORTING_MODE, required = false) @DefaultValue(QueryParametersPaginationSorting.DESCENDING_SORTING_MODE) @QueryParam(QueryParametersPaginationSorting.QUERY_SORTING_MODE) SortingMode sortingMode)
			throws JsonParseException, JsonMappingException, IOException,
			Exception {
			
		if(tagsIn == null || tagsIn.isEmpty())
		{
			throw new ValidationException("tags_in is required");
		}
		
		if(countryCode == null || countryCode.trim().length() == 0)
			countryCode = VideoConstants.COUNTRY_US;
		
		String status = VideoConstants.STATUS_READY;		
		
		ElasticSearchFilterDataTransfer esfdt = new ElasticSearchFilterDataTransfer();
		esfdt.setPagination(new Pagination(pageSize, (pageIndex-1)*pageSize));
		esfdt.setIndex(INDEX);
		esfdt.setFilters(VideoParameterValidator.validateCustomParameters(websiteIds, null, null, null, null, null, status, null, null, null, null, null, null, null, null, tagsIn, null, null, null, null, null));
		
		List<IElasticSearchSorting> sorting = new ArrayList<IElasticSearchSorting>();
		if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.DEFAULT_SORTING_EPISODE_NUM))
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_EPISODE_NO, sortingMode));
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_START_DATE))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_START_DATE, sortingMode));
		}
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_VIEWS))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_VIEWS, sortingMode));
		}	
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_SORT_DATE))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_SORT_DATE, sortingMode));
		}
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
		
		List<Object> restrictedVideoList = new ArrayList<Object>();
		if(videoList != null && !videoList.isEmpty())
		{
			for(ElasticSearchVideo esVideo : videoList)
			{
				if(esVideo.getAccessControl() != null)
				{
					if(!esVideo.getAccessControl().getAllow())
					{
						if(esVideo.getAccessControl().getValues().contains(countryCode.toUpperCase()))
							restrictedVideoList.add(new RestrictedAssetResource(esVideo.getId(), esVideo.getAccessControl().getMessage()));
						else
							restrictedVideoList.add(esVideo);
					}
					else
					{
						if(!esVideo.getAccessControl().getValues().contains(countryCode.toUpperCase()))
							restrictedVideoList.add(new RestrictedAssetResource(esVideo.getId(), esVideo.getAccessControl().getMessage()));
						else
							restrictedVideoList.add(esVideo);
					}		
				}
				else
					restrictedVideoList.add(esVideo);
			}
		}
		
		return new Result<Object>(totalCount, restrictedVideoList, pageIndex, pageSize, httpRequestHandler.getCorrelationId());
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/assets")
	@ApiOperation(value = "Find list of Videos by mentioned assets along with country code", notes = "Returns the list of asset video", response = ElasticSearchVideo.class, position = 6, httpMethod="GET")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Missing website ID"), @ApiResponse(code = 400, message = "Missing Action Name"), @ApiResponse(code = 404, message = "Resource not found")})
	@Produces({ MediaType.APPLICATION_JSON })
	public com.sorc.content.services.response.Result<Object> getVideoListByAsset(
			@ApiParam(value = ServicesCommonDocumentation.WEBSITEID, required = true) @NotEmpty(QueryParameters.WEBSITE_IDS) @QueryParam(QueryParameters.WEBSITE_IDS) final Set<Integer> websiteIds,				
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_ASSET_IN, required = true) @QueryParam(VideoQueryParameters.QUERY_PARAM_ASSET_IN) Set<String> assetIn,			
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_COUNTRY_CODE, required = true) @QueryParam(VideoQueryParameters.QUERY_PARAM_COUNTRY_CODE) String countryCode)
			throws JsonParseException, JsonMappingException, IOException,
			Exception {
			
		if(assetIn == null || assetIn.isEmpty())
		{
			throw new ValidationException("assets_in is required");
		}
		String status = VideoConstants.STATUS_READY;		
		
		ElasticSearchFilterDataTransfer esfdt = new ElasticSearchFilterDataTransfer();
		esfdt.setPagination(new Pagination(assetIn.size(), 0));
		esfdt.setIndex(INDEX);
		esfdt.setFilters(VideoParameterValidator.validateCustomParameters(websiteIds, null, null, null, null, null, status, null, null, null, null, null, null, null, null, null, assetIn, null, null, null, null));
		
		List<IElasticSearchSorting> sorting = new ArrayList<IElasticSearchSorting>();
		sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_START_DATE, SortingMode.DESCENDING));	
		
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
		
		List<Object> restrictedVideoList = new ArrayList<Object>();
		if(videoList != null && !videoList.isEmpty())
		{
			for(ElasticSearchVideo esVideo : videoList)
			{
				if(esVideo.getAccessControl() != null)
				{
					if(!esVideo.getAccessControl().getAllow())
					{
						if(esVideo.getAccessControl().getValues().contains(countryCode.toUpperCase()))
							restrictedVideoList.add(new RestrictedAssetResource(esVideo.getId(), esVideo.getAccessControl().getMessage()));
						else
							restrictedVideoList.add(esVideo);
					}
					else
					{
						if(!esVideo.getAccessControl().getValues().contains(countryCode.toUpperCase()))
							restrictedVideoList.add(new RestrictedAssetResource(esVideo.getId(), esVideo.getAccessControl().getMessage()));
						else
							restrictedVideoList.add(esVideo);
					}		
				}
				else
					restrictedVideoList.add(esVideo);
			}
		}
		return new com.sorc.content.services.response.Result<Object>(totalCount, restrictedVideoList, httpRequestHandler.getCorrelationId());
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/slider")
	@ApiOperation(value = "Find list of slider Videos", notes = "Returns the list of slider video", response = ElasticSearchVideo.class, position = 7, httpMethod="GET")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Missing website ID"), @ApiResponse(code = 400, message = "Missing Action Name"), @ApiResponse(code = 404, message = "Resource not found")})
	@Produces({ MediaType.APPLICATION_JSON })
	public Result<ElasticSearchVideo> getSliderVideoList(
			@ApiParam(value = ServicesCommonDocumentation.WEBSITEID, required = true) @NotEmpty(QueryParameters.WEBSITE_IDS) @QueryParam(QueryParameters.WEBSITE_IDS) final Set<Integer> websiteIds,				
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_COUNTRY_CODE, required = true) @QueryParam(VideoQueryParameters.QUERY_PARAM_COUNTRY_CODE) String countryCode,			
			@ApiParam(value = VideoDocumentationParameters.SORT_BY, required = false) @DefaultValue(VideoQueryParameters.DEFAULT_SORTING_START_DATE) @QueryParam(QueryParametersPaginationSorting.QUERY_PARAM_SORT_BY) String sortBy,
			@ApiParam(value = ServicesCommonDocumentation.SORTING_MODE, required = false) @DefaultValue(QueryParametersPaginationSorting.DESCENDING_SORTING_MODE) @QueryParam(QueryParametersPaginationSorting.QUERY_SORTING_MODE) SortingMode sortingMode,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_PAGE, required = false) @DefaultValue(VideoQueryParameters.DEFAULT_QUERY_PARAM_PAGE) @QueryParam(VideoQueryParameters.QUERY_PARAM_PAGE_INDEX) int pageIndex,			
			@ApiParam(value = ServicesCommonDocumentation.PAGINATION, required = false) @DefaultValue(QueryParametersPaginationSorting.DEFAULT_PAGINATION) @QueryParam(VideoQueryParameters.QUERY_PARAM_PAGE_SIZE) int pageSize)
			throws JsonParseException, JsonMappingException, IOException,
			Exception {
					
		if(websiteIds == null || websiteIds.isEmpty() || countryCode == null || countryCode.trim().length() == 0)		
			throw new ValidationException("website_ids and country_code are required fields");
		
		String status = VideoConstants.STATUS_READY;				
		boolean isSlider = true;
		
		ElasticSearchFilterDataTransfer esfdt = new ElasticSearchFilterDataTransfer();
		esfdt.setPagination(new Pagination(pageSize, (pageIndex-1)*pageSize));
		esfdt.setIndex(INDEX);
		esfdt.setFilters(VideoParameterValidator.validateCustomParameters(websiteIds, null, null, null, countryCode, null, status, null, null, null, null, null, null, null, null, null, null, null, isSlider, null, null));
		
		List<IElasticSearchSorting> sorting = new ArrayList<IElasticSearchSorting>();
		if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.DEFAULT_SORTING_EPISODE_NUM))
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_EPISODE_NO, sortingMode));
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_START_DATE))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_START_DATE, sortingMode));
		}
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_VIEWS))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_VIEWS, sortingMode));
		}
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_SORT_DATE))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_SORT_DATE, sortingMode));
		}
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
		
		return new Result<ElasticSearchVideo>(totalCount, videoList, pageIndex, pageSize, httpRequestHandler.getCorrelationId());
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/live")
	@ApiOperation(value = "Find list of slider Videos", notes = "Returns the list of slider video", response = ElasticSearchVideo.class, position = 8, httpMethod="GET")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Missing website ID"), @ApiResponse(code = 400, message = "Missing Action Name"), @ApiResponse(code = 404, message = "Resource not found")})
	@Produces({ MediaType.APPLICATION_JSON })
	public Result<ElasticSearchVideo> getLiveVideoList(
			@ApiParam(value = ServicesCommonDocumentation.WEBSITEID, required = true) @NotEmpty(QueryParameters.WEBSITE_IDS) @QueryParam(QueryParameters.WEBSITE_IDS) final Set<Integer> websiteIds,				
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_COUNTRY_CODE, required = true) @QueryParam(VideoQueryParameters.QUERY_PARAM_COUNTRY_CODE) String countryCode,			
			@ApiParam(value = VideoDocumentationParameters.SORT_BY, required = false) @DefaultValue(VideoQueryParameters.DEFAULT_SORTING_START_DATE) @QueryParam(QueryParametersPaginationSorting.QUERY_PARAM_SORT_BY) String sortBy,
			@ApiParam(value = ServicesCommonDocumentation.SORTING_MODE, required = false) @DefaultValue(QueryParametersPaginationSorting.DESCENDING_SORTING_MODE) @QueryParam(QueryParametersPaginationSorting.QUERY_SORTING_MODE) SortingMode sortingMode,
			@ApiParam(value = VideoDocumentationParameters.DOC_PARAM_PAGE, required = false) @DefaultValue(VideoQueryParameters.DEFAULT_QUERY_PARAM_PAGE) @QueryParam(VideoQueryParameters.QUERY_PARAM_PAGE_INDEX) int pageIndex,			
			@ApiParam(value = ServicesCommonDocumentation.PAGINATION, required = false) @DefaultValue(QueryParametersPaginationSorting.DEFAULT_PAGINATION) @QueryParam(VideoQueryParameters.QUERY_PARAM_PAGE_SIZE) int pageSize)
			throws JsonParseException, JsonMappingException, IOException,
			Exception {
					
		if(websiteIds == null || websiteIds.isEmpty() || countryCode == null || countryCode.trim().length() == 0)		
			throw new ValidationException("website_ids and country_code are required fields");
		
		String status = VideoConstants.STATUS_READY;				
		boolean isLiveEvent = true;
		
		ElasticSearchFilterDataTransfer esfdt = new ElasticSearchFilterDataTransfer();
		esfdt.setPagination(new Pagination(pageSize, (pageIndex-1)*pageSize));
		esfdt.setIndex(INDEX);
		esfdt.setFilters(VideoParameterValidator.validateCustomParameters(websiteIds, null, null, null, countryCode, null, status, null, null, null, null, null, null, null, null, null, null, null, null, isLiveEvent, null));
		
		List<IElasticSearchSorting> sorting = new ArrayList<IElasticSearchSorting>();
		if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.DEFAULT_SORTING_EPISODE_NUM))
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_EPISODE_NO, sortingMode));
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_START_DATE))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_START_DATE, sortingMode));
		}
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_VIEWS))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_VIEWS, sortingMode));
		}
		else if(sortBy != null && sortBy.equalsIgnoreCase(VideoQueryParameters.SORTING_SORT_DATE))
		{
			sorting.add(new ElasticSearchVideoSorting(VideoConstants.SORT_SORT_DATE, sortingMode));
		}
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
		
		return new Result<ElasticSearchVideo>(totalCount, videoList, pageIndex, pageSize, httpRequestHandler.getCorrelationId());
	}
	
	public String getDecodedString(String string) throws UnsupportedEncodingException{
		return URLDecoder.decode(string, "UTF-8");
	}
	
	public Set<String> getDecodedStrings(Set<String> strings) throws UnsupportedEncodingException{
		Set<String> decodedStrings = new TreeSet<String>();
		for(String string : strings ){
			decodedStrings.add(URLDecoder.decode(string, "UTF-8"));
		}
		return decodedStrings;
	}
}
