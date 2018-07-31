/**
 * 
 */
package com.sorc.content.services.video.resources;

import static org.mockito.Matchers.anyObject;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.sorc.content.core.dto.IFiltersDataTransfer;
import com.sorc.content.core.dto.TraceInfo;
import com.sorc.content.elasticsearch.core.dao.IElasticSearchDataAccess;
import com.sorc.content.elasticsearch.core.dto.ElasticSearchFilterDataTransfer;
import com.sorc.content.services.request.IHttpRequestHandler;
import com.sorc.content.services.response.Result;
import com.sorc.content.video.dao.data.ElasticSearchVideo;

/**
 * @author rakesh.moradiya
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ VideoResource3_0.class })
public class VideoResourceTest {

	private IElasticSearchDataAccess daoMock;
	private ElasticSearchFilterDataTransfer filtersDataTransferMock;
	
	private VideoResource3_0 videoResourcer;
	private TraceInfo traceInfoMock;
	@SuppressWarnings("rawtypes")
	private Result resultMock;
	private HttpServletRequest httpServletRequestMock;
	private IHttpRequestHandler httpRequestHandlerMock;
	private ElasticSearchVideo resultsVideoDetail;
	private final String correlationId = "45874-54455";

	@SuppressWarnings("unchecked")
	@Before
	public void initMock() throws Exception {
		daoMock = mock(IElasticSearchDataAccess.class);
		httpServletRequestMock = mock(HttpServletRequest.class);
		filtersDataTransferMock = mock(ElasticSearchFilterDataTransfer.class);
		httpRequestHandlerMock = mock(IHttpRequestHandler.class);

		resultMock = mock(Result.class);
		traceInfoMock = mock(TraceInfo.class);

		resultsVideoDetail = new ElasticSearchVideo();
		videoResourcer = new VideoResource3_0();
		videoResourcer.setDao(daoMock);
		
		videoResourcer.setHttpRequestHandler(httpRequestHandlerMock);
		when(httpRequestHandlerMock.getCorrelationId()).thenReturn(
				correlationId);
		
		// Return a new BuyersGuide object when getObj is called instead of
		// returning an object from the DAO
		when(
				daoMock.getObj((IFiltersDataTransfer<ElasticSearchVideo, String>) anyObject()))
				.thenReturn(resultsVideoDetail);
		when(httpServletRequestMock.getAttribute("partner")).thenReturn(null);
		when(httpServletRequestMock.getHeader("User-Agent")).thenReturn(
				"{\"correlationId\":\"" + correlationId + "\"}");

		whenNew(TraceInfo.class).withAnyArguments().thenReturn(traceInfoMock);
		whenNew(ElasticSearchFilterDataTransfer.class).withAnyArguments().thenReturn(
				filtersDataTransferMock);
		whenNew(Result.class).withAnyArguments().thenReturn(resultMock);

	}

	@Test
	public void testListings() throws Exception {
		HashSet<Integer> websiteIds = new HashSet<>();
		websiteIds.add(70005);
		/*videoResourcer.getBuyersGuideList(websiteIds, null, null, null, null, null, null, null,
				Integer.parseInt(QueryParametersPaginationSorting.DEFAULT_OFFSET),
				Integer.parseInt(QueryParametersPaginationSorting.DEFAULT_PAGINATION));
		*/
		// Mockito verify
		//Mockito.verify(filtersDataTransferMock).setPagination(new Pagination(Integer.parseInt(QueryParametersPaginationSorting.DEFAULT_PAGINATION), Integer.parseInt(QueryParametersPaginationSorting.DEFAULT_OFFSET)));
	}
}
