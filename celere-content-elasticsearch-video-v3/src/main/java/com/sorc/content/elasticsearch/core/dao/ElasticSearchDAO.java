package com.sorc.content.elasticsearch.core.dao;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.cluster.Health;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.sort.Sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.transport.TransportSerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sorc.content.core.dao.IDataTransfer;
import com.sorc.content.core.dao.ValidationException;
import com.sorc.content.core.pagination.Pagination;
import com.sorc.content.core.sort.SortingMode;
import com.sorc.content.elasticsearch.core.constant.ElasticSearchVideoFieldConstants;
import com.sorc.content.elasticsearch.core.sort.IElasticSearchSorting;
import com.sorc.content.elasticsearch.core.util.AppleUmcAvailabilityResultAssembler;
import com.sorc.content.elasticsearch.core.util.ElasticSearchVideoResultAssembler;
import com.sorc.content.mongodb.core.dao.DBGlobalConfig;
import com.sorc.content.mongodb.core.dao.RetryStrategy;
import com.sorc.content.video.filter.input.CustomScriptFilter;

@Repository
public abstract class ElasticSearchDAO<T extends IDataTransfer<IDType>, IDType>
		extends ElasticSearchDataAccess {

	public int defaultNumberOfRetries;
	public long defaultWaitTime;
	JestClient jestClient = null;

	Logger logger = LoggerFactory.getLogger(ElasticSearchDAO.class);

	@Autowired
	private DBGlobalConfig esConfig;

	@PostConstruct
	public void init() {
		defaultNumberOfRetries = esConfig.getDefaultMaxNumberOfRetries();
		defaultWaitTime = esConfig.getDefaultRetryWaitTime();
	}

	@Autowired
	public ElasticSearchDAO(
			ElasticSearchClusterConfiguration clusterConfiguration) {

		String[] servers = clusterConfiguration.getServers();
		if(servers != null && servers.length > 0)
		{
			HttpClientConfig clientConfig = new HttpClientConfig.Builder(servers[0]).multiThreaded(true).readTimeout(1200000).build();
			JestClientFactory factory = new JestClientFactory();
			factory.setHttpClientConfig(clientConfig);
			jestClient = factory.getObject();
		}
	}
	
	@PreDestroy
	public void cleanUp() throws Exception {  
		if (jestClient != null) {
			jestClient.shutdownClient();
		}	
	}
	
	public Map<String, Object> search(Pagination pagination, String index,
			BoolQueryBuilder filters, List<IElasticSearchSorting> sorting, 
			CustomScriptFilter scriptFilter, boolean isRoot) throws Exception {

		RetryStrategy retry = new RetryStrategy(defaultNumberOfRetries, defaultWaitTime);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// build the search
		if (index != null) {
			Map<String, Object> source = new LinkedHashMap<String, Object>();	
			source.put(ElasticSearchVideoFieldConstants.FROM, pagination.getOffset());
			source.put(ElasticSearchVideoFieldConstants.SIZE, pagination.getSize());
			
			if(filters != null) {
				source.put(ElasticSearchVideoFieldConstants.QUERY, filters);
			}
			
			if(scriptFilter != null) {
				source.put(ElasticSearchVideoFieldConstants.SORT, getCustomScriptFilter(scriptFilter, sorting));
			}
			
	        Search search = new Search.Builder(source.toString())
	        .addIndex(index)
	        .addSort(addSorting(sorting))       
	        .build();

	        // execute the search
			SearchResult searchResult = null;
			do {
				try {
					searchResult  = jestClient.execute(search);
					break;
				} catch (NoNodeAvailableException e) {
					retry.errorOccured(e);
				} catch (TransportSerializationException e) {
					// transport serialization exceptions are caused by having
					// invalid indexes
					throw new ValidationException("Invalid query parameters");
				} catch (IndexNotFoundException e) {
					// transport serialization exceptions are caused by having
					// invalid indexes
					throw new ValidationException("Invalid query parameters: "
							+ e.getMessage() + " in search database");
				}
			} while (retry.shouldRetry());
			
			resultMap = getElasticSearchVideoResult(searchResult, isRoot);
		}
		return resultMap;
	}
	
	public List<Object> searchFacet(Pagination pagination, String index, String facet,
			BoolQueryBuilder filters, List<IElasticSearchSorting> sorting, boolean isMinMaxAgg) throws Exception {

		RetryStrategy retry = new RetryStrategy(defaultNumberOfRetries, defaultWaitTime);
		List<Object> resultList = null;

		// build the search
		if (index != null) {
			JSONObject source = new JSONObject();	
			source.put(ElasticSearchVideoFieldConstants.FROM, pagination.getOffset());
			source.put(ElasticSearchVideoFieldConstants.SIZE, pagination.getSize());
			
			if(facet != null && filters != null) {
				source.put(ElasticSearchVideoFieldConstants.AGGREGATIONS, setAggregateFilterBuilder(facet, filters, sorting, isMinMaxAgg));
			} else {
				source.put(ElasticSearchVideoFieldConstants.AGGREGATIONS, setAggregateBuilder(facet, sorting));
			}
	        Search search = new Search.Builder(source.toString())
	        .addIndex(index)
	        .addSort(addSorting(sorting))       
	        .build();
	        
			// execute the search
			SearchResult searchResult = null;
			do {
				try {
					searchResult  = jestClient.execute(search);
					break;
				} catch (NoNodeAvailableException e) {
					retry.errorOccured(e);
				} catch (TransportSerializationException e) {
					// transport serialization exceptions are caused by having
					// invalid indexes
					throw new ValidationException("Invalid query parameters");
				} catch (IndexNotFoundException e) {
					// transport serialization exceptions are caused by having
					// invalid indexes
					throw new ValidationException("Invalid query parameters: "
							+ e.getMessage() + " in search database");
				}
			} while (retry.shouldRetry());
						
			resultList = getElasticSEarchVideoFacetList(facet, filters, searchResult);
		}
		return resultList;
	}	
	
	public SearchResult searchDetailFacet(Pagination pagination, String index, String facet,
			BoolQueryBuilder filters, List<String> facetFields, List<IElasticSearchSorting> sorting, String callType) throws Exception {
		return searchDetailFacet(pagination, index, facet, filters, facetFields, null, sorting, null, callType);
	}
	
	public SearchResult searchDetailFacet(Pagination pagination, String index, String facet,
			BoolQueryBuilder filters, List<String> facetFields, List<String> additionalFacetColumns, List<IElasticSearchSorting> sorting, List<IElasticSearchSorting> aggDetailSorting, String callType) throws Exception {

		SearchResult searchResult = null;
		
		RetryStrategy retry = new RetryStrategy(defaultNumberOfRetries, defaultWaitTime);

		// build the search
		if (index != null) {
			JSONObject source = new JSONObject();	
			
			if(callType != null && ElasticSearchVideoFieldConstants.CALL_TYPE_SEARCH.equalsIgnoreCase(callType))
			{
				source.put(ElasticSearchVideoFieldConstants.FROM, pagination.getOffset());
				source.put(ElasticSearchVideoFieldConstants.SIZE, pagination.getSize());
				if(filters != null) {
					source.put(ElasticSearchVideoFieldConstants.QUERY, filters.toString());
				}
			}
			else
			{
				source.put(ElasticSearchVideoFieldConstants.FROM, 0);
				source.put(ElasticSearchVideoFieldConstants.SIZE, 0);
			}
			
			source.put(ElasticSearchVideoFieldConstants.AGGREGATIONS, 
					setAggregateDetailFacetFilterBuilder(facet, filters, facetFields, additionalFacetColumns, sorting, aggDetailSorting, pagination, callType));
			
	        Search search = new Search.Builder(source.toString())
	        .addIndex(index)
	        .addSort(addSorting(sorting))       
	        .build();
	        
			// execute the search			
			do {
				try {
					searchResult  = jestClient.execute(search);					
					break;
				} catch (NoNodeAvailableException e) {
					retry.errorOccured(e);
				} catch (TransportSerializationException e) {
					// transport serialization exceptions are caused by having
					// invalid indexes
					throw new ValidationException("Invalid query parameters");
				} catch (IndexNotFoundException e) {
					// transport serialization exceptions are caused by having
					// invalid indexes
					throw new ValidationException("Invalid query parameters: "
							+ e.getMessage() + " in search database");
				}
			} while (retry.shouldRetry());			
		}
		return searchResult;
	}	
	
	public List<Object> getElasticSearchAppleUmcFeedDetailFacetResult(Pagination pagination, String index, String facet,
			BoolQueryBuilder filters, List<String> facetFields, List<String> additionalFacetColumns, List<IElasticSearchSorting> sorting, List<IElasticSearchSorting> aggDetailSorting, String callType) throws Exception
	{
		SearchResult searchResult = searchDetailFacet(pagination, index, facet, filters, facetFields, additionalFacetColumns, sorting, aggDetailSorting, callType);		
		return ElasticSearchVideoResultAssembler.getElasticSearchAppleUmcFeedDetailFacetResult(facet, additionalFacetColumns.get(0), filters, searchResult);
	}
	
	public List<Object> getElasticSearchAppleUmcAvailabilityFeedDetailFacetResult(Pagination pagination, String index, String facet,
			BoolQueryBuilder filters, List<String> facetFields, List<String> additionalFacetColumns, List<IElasticSearchSorting> sorting, List<IElasticSearchSorting> aggDetailSorting, String callType) throws Exception
	{
		SearchResult searchResult = searchDetailFacet(pagination, index, facet, filters, facetFields, additionalFacetColumns, sorting, aggDetailSorting, callType);		
		return AppleUmcAvailabilityResultAssembler.getElasticSearchAppleUmcAvailabilityFeedDetailFacetResult(facet, additionalFacetColumns.get(0), filters, searchResult);
	}
		
	private JSONObject setAggregateBuilder(String facet,  List<IElasticSearchSorting> sorting ) {
		JSONObject aggFilter = new JSONObject();
		JSONObject termsFilter = new JSONObject();
		termsFilter.put(ElasticSearchVideoFieldConstants.FIELD, facet);
		termsFilter.put(ElasticSearchVideoFieldConstants.SIZE, Integer.MAX_VALUE);
		termsFilter.put(ElasticSearchVideoFieldConstants.ORDER, addSortingFacet(sorting));
		aggFilter.put(facet, new JSONObject().accumulate(ElasticSearchVideoFieldConstants.TERMS, 
				termsFilter));
		return aggFilter;
	}
	
	private Object setAggregateFilterBuilder(String facet, BoolQueryBuilder filters, List<IElasticSearchSorting> sorting, boolean isMinMaxAgg) {
				
		JSONObject filter = new JSONObject();
		JSONObject termFilter = new JSONObject();
		termFilter.put(ElasticSearchVideoFieldConstants.FILTER, filters.toString());
		if(filters != null) {
			termFilter.put(ElasticSearchVideoFieldConstants.AGGREGATIONS, setAggregateBuilder(facet, sorting));
		}
		filter.put(facet, termFilter);
		return filter;		
	}	
	
	private List<Sort> addSorting(List<IElasticSearchSorting> sorting) {
		List<Sort> sortingList = new ArrayList<Sort>();
		if(sorting != null && !sorting.isEmpty()) {
			for (IElasticSearchSorting iElasticSearchSorting : sorting) {
				sortingList.add(new Sort(iElasticSearchSorting.getFieldName(), 
						mapSortingOrder(iElasticSearchSorting.getMode())));
			}
		}
		return sortingList;
	}
	
	private String getSortingMode(List<IElasticSearchSorting> sorting) {
		if(sorting != null && !sorting.isEmpty()) {
			for (IElasticSearchSorting iElasticSearchSorting : sorting) {
				return mapSortingOrder(iElasticSearchSorting.getMode()).toString();
			}
		}
		return null;
	}
	
	private static String getFieldSortingMode(List<IElasticSearchSorting> sorting, String fieldName) {
		if(sorting != null && !sorting.isEmpty()) {
			for (IElasticSearchSorting iElasticSearchSorting : sorting) {
				if(iElasticSearchSorting.getFieldName()!=null && iElasticSearchSorting.getFieldName().equalsIgnoreCase(fieldName))
					return mapSortingOrder(iElasticSearchSorting.getMode()).toString();
			}
		}
		return null;
	}
	
	private Object setAggregateDetailFacetFilterBuilder(String facet, BoolQueryBuilder filters, 
			List<String> facetFields, List<String> additionalFacetColumns, List<IElasticSearchSorting> sorting, List<IElasticSearchSorting> aggDetailSorting, Pagination pagination, String callType) {
		JSONObject filter = new JSONObject();
		JSONObject termFilter = new JSONObject();
		termFilter.put(ElasticSearchVideoFieldConstants.FILTER, filters.toString());
		if(filters != null) {
			termFilter.put(ElasticSearchVideoFieldConstants.AGGREGATIONS, 
					setAggregateDetailFacetBuilder(facet, facetFields, additionalFacetColumns, sorting, aggDetailSorting, pagination, callType));
		}
		filter.put(facet, termFilter);
		return filter;
	}

	private JSONObject setAggregateDetailFacetBuilder(String facet, List<String> facetFields, List<String> additionalFacetColumns, 
			List<IElasticSearchSorting> sorting, List<IElasticSearchSorting> aggDetailSorting, Pagination pagination, String callType ) {
		JSONObject aggFilter = new JSONObject();
		JSONObject termsFilter = new JSONObject();
		termsFilter.put(ElasticSearchVideoFieldConstants.FIELD, facet);
		termsFilter.put(ElasticSearchVideoFieldConstants.SIZE, Integer.MAX_VALUE);
		termsFilter.put(ElasticSearchVideoFieldConstants.ORDER, addSortingFacet(sorting));
		
		if(additionalFacetColumns != null && additionalFacetColumns.size() > 0)
		{
			JSONObject firstSubAggFilter = new JSONObject();				
			JSONObject firstSubTermsFilter = new JSONObject();
			JSONObject secondSubTermsFilter = new JSONObject();
			for(int i =0; i < additionalFacetColumns.size(); i++)
			{					
				if(additionalFacetColumns.size() ==1)
				{
					firstSubTermsFilter.put(ElasticSearchVideoFieldConstants.FIELD, additionalFacetColumns.get(i));		
					firstSubTermsFilter.put(ElasticSearchVideoFieldConstants.SIZE, Integer.MAX_VALUE);
					firstSubAggFilter.put(additionalFacetColumns.get(i), new JSONObject().accumulate(ElasticSearchVideoFieldConstants.TERMS, firstSubTermsFilter).accumulate(ElasticSearchVideoFieldConstants.AGGREGATIONS, setAggregateDetailFilterBuilder(facet+additionalFacetColumns.get(i), facetFields, aggDetailSorting, pagination)));
				}
				else
				{
					JSONObject secondSubAggFilter = new JSONObject();
					//Support only two sub agg here
					if(i == 1)
					{
						secondSubTermsFilter.put(ElasticSearchVideoFieldConstants.FIELD, additionalFacetColumns.get(i));	
						secondSubTermsFilter.put(ElasticSearchVideoFieldConstants.SIZE, Integer.MAX_VALUE);
						secondSubAggFilter.put(additionalFacetColumns.get(i), new JSONObject().accumulate(ElasticSearchVideoFieldConstants.TERMS, secondSubTermsFilter).accumulate(ElasticSearchVideoFieldConstants.AGGREGATIONS, setAggregateDetailFilterBuilder(facet+additionalFacetColumns.get(i), facetFields, aggDetailSorting, pagination)));
						((JSONObject)firstSubAggFilter.get(additionalFacetColumns.get(0))).accumulate(ElasticSearchVideoFieldConstants.AGGREGATIONS, secondSubAggFilter);
					}
					else
					{
						firstSubTermsFilter.put(ElasticSearchVideoFieldConstants.FIELD, additionalFacetColumns.get(i));	
						firstSubTermsFilter.put(ElasticSearchVideoFieldConstants.SIZE, Integer.MAX_VALUE);
						firstSubAggFilter.put(additionalFacetColumns.get(0), new JSONObject().accumulate(ElasticSearchVideoFieldConstants.TERMS, firstSubTermsFilter));
					}
				}
				
			}
			aggFilter.put(facet, new JSONObject().accumulate(ElasticSearchVideoFieldConstants.TERMS, termsFilter)
					.accumulate(ElasticSearchVideoFieldConstants.AGGREGATIONS, firstSubAggFilter));
		}
		else			
			aggFilter.put(facet, new JSONObject().accumulate(ElasticSearchVideoFieldConstants.TERMS, termsFilter)
			.accumulate(ElasticSearchVideoFieldConstants.AGGREGATIONS, setAggregateDetailFilterBuilder(facet, facetFields, null, pagination)));
		
		return aggFilter;
	}
		
	private Object setAggregateDetailFilterBuilder(String facet, List<String> facetFields, List<IElasticSearchSorting> aggDetailSorting, Pagination pagination) {
		JSONObject aggFilter = new JSONObject();
		JSONObject hitsFilter = new JSONObject();
		JSONObject sourceFilter = new JSONObject();
		sourceFilter.put(ElasticSearchVideoFieldConstants.INCLUDES, facetFields);
		if(aggDetailSorting != null && aggDetailSorting.size() > 0)
			hitsFilter.put(ElasticSearchVideoFieldConstants.SIZE, Integer.MAX_VALUE);
		else 
			hitsFilter.put(ElasticSearchVideoFieldConstants.SIZE, pagination.getSize());
		hitsFilter.put(ElasticSearchVideoFieldConstants.SOURCE, sourceFilter);
		if(aggDetailSorting != null && aggDetailSorting.size() > 0)
		{
			hitsFilter.put(ElasticSearchVideoFieldConstants.SORT, addSortingDetailFacet(aggDetailSorting));
		}
		aggFilter.put(facet, new JSONObject().accumulate(ElasticSearchVideoFieldConstants.TOP_HITS, hitsFilter));
		return aggFilter;
	}
	
	private static JSONArray addSortingDetailFacet(List<IElasticSearchSorting> sorting) {
		JSONArray sortArrayObj = new JSONArray();
		if(sorting != null && !sorting.isEmpty()) {
			for (IElasticSearchSorting iElasticSearchSorting : sorting) {
				if(iElasticSearchSorting.getMode() != null) {
					JSONObject sortingObj = new JSONObject();
					sortingObj.put(iElasticSearchSorting.getFieldName(), mapSortingOrder(iElasticSearchSorting.getMode()));
					sortArrayObj.add(sortingObj);
				}
			}
		}
		return sortArrayObj;
	}
	
	private JSONObject addSortingFacet(List<IElasticSearchSorting> sorting) {
		JSONObject sortingObj = new JSONObject();
		if(sorting != null && !sorting.isEmpty()) {
			for (IElasticSearchSorting iElasticSearchSorting : sorting) {
				if(iElasticSearchSorting.getMode() != null) {
					sortingObj.put(ElasticSearchVideoFieldConstants.SORTING_TERM, mapSortingOrder(iElasticSearchSorting.getMode()));
				}
			}
		}
		return sortingObj;
	}

	public static JSONArray getCustomScriptFilter(CustomScriptFilter scriptFilter, List<IElasticSearchSorting> sorting) {
		JSONArray sortArrayObj = new JSONArray();		
		return sortArrayObj;
	}
	
	private static io.searchbox.core.search.sort.Sort.Sorting mapSortingOrder(SortingMode mode) {
		if (mode.equals(SortingMode.ASCENDING)) {
			return Sort.Sorting.ASC;
		} else {
			return Sort.Sorting.DESC;
		}
	}
	
	private Map<String, Object> getElasticSearchVideoResult(SearchResult result, boolean isRoot) {		
			return  ElasticSearchVideoResultAssembler.getElasticSearchVideoResult(result);		
	}	
	
	private List<Object> getElasticSEarchVideoFacetList(String facet, BoolQueryBuilder filters, SearchResult result) {
		return  ElasticSearchVideoResultAssembler.getElasticSearchVideoFacetResult(facet, filters, result);
	}
	
	public void setJestClient(JestClient jestClient) {
		this.jestClient = jestClient;
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (jestClient != null) {
			jestClient.shutdownClient();
		}
		super.finalize();
	}

	public boolean healthCheck() {
		 JestResult result;
		try {
			result = jestClient.execute(new Health.Builder().build());
			if (result.isSucceeded()) {
				return true;

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}