
package com.sorc.content.elasticsearch.core.dao;

import java.util.List;

import com.sorc.content.core.dao.IDataTransfer;
import com.sorc.content.core.dao.NotFoundException;
import com.sorc.content.core.dao.ValidationException;
import com.sorc.content.core.dto.IFilterIdDataTransfer;
import com.sorc.content.core.dto.IFiltersDataTransfer;
import com.sorc.content.core.dto.IUpdatableDataTransfer;
import com.sorc.content.core.dto.TraceInfo;
import com.sorc.content.core.filter.InvalidFilterException;
import com.sorc.content.core.filter.combination.FilterCombinationNotImplementedException;
import com.sorc.content.core.sort.InvalidSortingTypeException;

public abstract class ElasticSearchDataAccess implements IElasticSearchDataAccess{
	
	@SuppressWarnings("rawtypes")
	public IDataTransfer getObj(IFiltersDataTransfer filterDataTransfer)
			throws NotFoundException, InvalidFilterException,
			FilterCombinationNotImplementedException {
		
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Object save(IDataTransfer t) throws ValidationException {
		
		return null;
	}
	@SuppressWarnings("rawtypes")
	public void delete(IFilterIdDataTransfer idFilterDataTransfer) {
		
		
	}

	public long count(TraceInfo traceInfo) {
		
		return 0;
	}
	@SuppressWarnings("rawtypes")
	public void update(IFilterIdDataTransfer assetId,
			IUpdatableDataTransfer updateDataTransfer)
			throws InvalidFilterException,
			FilterCombinationNotImplementedException {
		
		
	}
	@SuppressWarnings("rawtypes")
	public void update(IFiltersDataTransfer filterDataTransfer,
			IUpdatableDataTransfer updateDataTransfer)
			throws InvalidFilterException,
			FilterCombinationNotImplementedException {
		
		
	}
	@SuppressWarnings("rawtypes")
	public long count(IFiltersDataTransfer filterDataTransfer)
			throws InvalidFilterException,
			FilterCombinationNotImplementedException,
			InvalidSortingTypeException {
		
		return 0;
	}
	@SuppressWarnings("rawtypes")
	public List getList(IFiltersDataTransfer filterDataTransfer)
			throws InvalidFilterException,
			FilterCombinationNotImplementedException,
			InvalidSortingTypeException {
		
		return null;
	}
	@SuppressWarnings("rawtypes")
	public IDataTransfer getObj(IFilterIdDataTransfer idFilterDataTransfer)
			throws NotFoundException {
		
		return null;
	}
	public abstract boolean healthCheck();
}

