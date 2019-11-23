package com.hunt.model.dto;

import com.hunt.model.entity.BaseEntity;

/**
 * 分页参数实体类
 * @author williambai
 *
 */
public class PageDto extends BaseEntity{
	
	/**排序字段,常用 create_time*/
	private String sort;
	/**排序方式，一般用 DESC*/
	private String order;
	private Integer page;
	private Integer rows;
	
	
	
	public PageDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PageDto(String sort, String order, Integer page, Integer rows) {
		super();
		this.sort = sort;
		this.order = order;
		this.page = page;
		this.rows = rows;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	
	
	

}
