/**
 * 
 */
package com.sorc.content.services.video.response;

/**
 * @author rakesh.moradiya
 *
 */
public class MetaData {
	private Long totalCount;
	private Integer resultCount;
	private Integer pageIndex;
	private Integer pageSize;
	private String correlationId;

	public MetaData() {
	}

	public MetaData(Long totalCount, Integer resultCount, Integer pageIndex, Integer pageSize) {
		this.totalCount = totalCount;
		this.resultCount = resultCount;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}

	public MetaData(Long totalCount, Integer resultCount, Integer pageIndex, Integer pageSize, String correlationId) {
		this.totalCount = totalCount;
		this.resultCount = resultCount;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.correlationId = correlationId;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getResultCount() {
		return resultCount;
	}

	public void setResultCount(Integer resultCount) {
		this.resultCount = resultCount;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	@Override
	public String toString() {
		return "MetaData [totalCount=" + totalCount + ", resultCount="
				+ resultCount + ", pageIndex=" + pageIndex + ", pageSize="
				+ pageSize + ", correlationId=" + correlationId + "]";
	}

}