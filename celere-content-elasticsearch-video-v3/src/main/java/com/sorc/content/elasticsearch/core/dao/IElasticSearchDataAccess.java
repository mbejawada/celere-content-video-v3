package com.sorc.content.elasticsearch.core.dao;

import java.util.List;
import java.util.Map;

import com.sorc.content.core.dao.IDataAccess;
import com.sorc.content.elasticsearch.core.dto.ElasticSearchFilterDataTransfer;

@SuppressWarnings("rawtypes")
public interface IElasticSearchDataAccess extends IDataAccess {

	Map<String, Object> getRootDetailList(ElasticSearchFilterDataTransfer esfdt) throws Exception;
	
	Map<String, Object> getDetailList(ElasticSearchFilterDataTransfer esfdt) throws Exception;
	
	Map<String, Object> getCustomDetailList(ElasticSearchFilterDataTransfer esfdt) throws Exception;
	
	List<Object> getFacetList(ElasticSearchFilterDataTransfer esfdt) throws Exception;
	
	List<Object> getDetailFacetList(ElasticSearchFilterDataTransfer esfdt, String callType) throws Exception;	
	
	public List<Object> getMultiFacetList(ElasticSearchFilterDataTransfer esfdt) throws Exception;
	
	boolean healthCheck();
}
