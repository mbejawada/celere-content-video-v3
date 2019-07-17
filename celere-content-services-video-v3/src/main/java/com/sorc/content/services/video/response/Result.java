package com.sorc.content.services.video.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author rakesh.moradiya
 *
 */
@XmlRootElement
public class Result<T> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1943731978083142557L;
	private MetaData metaData;
	private List<T> items = new ArrayList<T>();
	

	public Result() {
	}

	public Result(Long totalCount, Integer resultCount, Integer pageIndex, Integer pageSize, String correlationId) {
		metaData = new MetaData(totalCount, resultCount, pageIndex, pageSize, correlationId);
	}

	public Result(Long totalCount, List<T> items, Integer pageIndex, Integer pageSize, String correlationId) {
		this.items = items;
		metaData = new MetaData(totalCount, items.size(), pageIndex, pageSize, correlationId);
	}
	

	public Result(T item, Integer pageIndex, Integer pageSize, String correlationId) {
		this.items.add(item);
		metaData = new MetaData(1l, items.size(), pageIndex, pageSize, correlationId);
	}
	
	public Result(Long totalCount, Integer resultCount, Integer pageIndex, Integer pageSize, String correlationId,List<T> items) {
		metaData = new MetaData(totalCount, resultCount, pageIndex, pageSize, correlationId);
	}
	
	public MetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}
		
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	
	
	
}