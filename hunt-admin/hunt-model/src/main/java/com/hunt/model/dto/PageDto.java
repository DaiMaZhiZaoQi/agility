package com.hunt.model.dto;

import com.hunt.model.entity.BaseEntity;

/**
 * 分页参数实体类，通用
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
	
	/**通用id*/
	private Long id;
	private Long userId;
	/**开始时间 毫秒数*/
	private Long beginTime;
	/**结束时间 毫秒数*/
	private Long endTime;
	/**电话号码*/
	private String callNumber;
	/**部门*/
	private String orgName;
	/**是否只查录音*/
	private Integer callIsHaveRecord;
	
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public String getCallNumber() {
		return callNumber;
	}
	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Integer getCallIsHaveRecord() {
		return callIsHaveRecord;
	}
	public void setCallIsHaveRecord(Integer callIsHaveRecord) {  
		this.callIsHaveRecord = callIsHaveRecord;
	}
	
	
	

}
