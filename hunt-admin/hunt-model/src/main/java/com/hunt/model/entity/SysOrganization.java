package com.hunt.model.entity;

import java.util.List;

/**
 * @Author: ouyangan
 * @Date: 2016-10-12 14:21
 * @Description:
 */
public class SysOrganization {

    // id :
    private Long id;

    // name :名称
    private String name;

    // fullName :全称
    private String fullName;

    // description :描述
    private String description;

    // is_final :是否可删除
    private Integer isFinal;

    // parent_id :
    private Long parentId;

    // rank :排序
    private Long rank;

    // create_time :创建时间
    private java.util.Date createTime;

    // update_time :更新时间
    private java.util.Date updateTime;

    // create_by :创建人id
    private Long createBy;

    // update_by :更新人id
    private Long updateBy;

    // status :数据状态,1:正常,2:删除
    private Long status;
    
    //  系统  10
    //机构码 2位代表一个机构   ，xxx集团 11，
    //xxx集团xa分公司 1100，                       xxx集团xb分公司                 1101.
    // xx集团xa分公司xa部门   110000  xxx集团xa分公司xb部门  110001  xxx集团xb分公司xa部门110100
    private String orgCode;

    /**结构类型 0：机构，1：个人*/
    private int orgType;
    
    private List<SysOrganization> children;
    private String state;
    private String open;
    private int checkState;
    private Boolean checked;
  

	@Override
	public String toString() {
		return "SysOrganization [id=" + id + ", name=" + name + ", fullName=" + fullName + ", description="
				+ description + ", isFinal=" + isFinal + ", parentId=" + parentId + ", rank=" + rank + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", createBy=" + createBy + ", updateBy=" + updateBy
				+ ", status=" + status + ", orgCode=" + orgCode + ", orgType=" + orgType + "]";
	}

	/**
     * get
     *
     * @return Long
     */
    public Long getId() {
        return id;
    }

    /**
     * set
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * get 名称
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * set 名称
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * get 描述
     *
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * set 描述
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get 是否可删除
     *
     * @return Integer
     */
    public Integer getIsFinal() {
        return isFinal;
    }

    /**
     * set 是否可删除
     *
     * @param isFinal
     */
    public void setIsFinal(Integer isFinal) {
        this.isFinal = isFinal;
    }

    /**
     * get
     *
     * @return Long
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * set
     *
     * @param parentId
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * get 排序
     *
     * @return Long
     */
    public Long getRank() {
        return rank;
    }

    /**
     * set 排序
     *
     * @param rank
     */
    public void setRank(Long rank) {
        this.rank = rank;
    }

    /**
     * get 创建时间
     *
     * @return java.util.Date
     */
    public java.util.Date getCreateTime() {
        return createTime;
    }

    /**
     * set 创建时间
     *
     * @param createTime
     */
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    /**
     * get 更新时间
     *
     * @return java.util.Date
     */
    public java.util.Date getUpdateTime() {
        return updateTime;
    }

    /**
     * set 更新时间
     *
     * @param updateTime
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * get 创建人id
     *
     * @return Long
     */
    public Long getCreateBy() {
        return createBy;
    }

    /**
     * set 创建人id
     *
     * @param createBy
     */
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    /**
     * get 更新人id
     *
     * @return Long
     */
    public Long getUpdateBy() {
        return updateBy;
    }

    /**
     * set 更新人id
     *
     * @param updateBy
     */
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * get 数据状态,1:正常,2:删除
     *
     * @return Integer
     */
    public Long getStatus() {
        return status;
    }

    /**
     * set 数据状态,1:正常,2:删除
     *
     * @param status
     */
    public void setStatus(Long status) {
        this.status = status;
    }

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public int getOrgType() {
		return orgType;
	}

	public void setOrgType(int orgType) {
		this.orgType = orgType;
	}

	public List<SysOrganization> getChildren() {
		return children;
	}

	public void setChildren(List<SysOrganization> children) {
		this.children = children;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	
	
	
	
	public int getCheckState() {
		return checkState;
	}

	public void setCheckState(int checkState) {
		this.checkState = checkState;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	
    
    
}
