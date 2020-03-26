package com.hunt.model.entity;

import java.sql.Timestamp;
import java.util.Date;

public class SysTask extends BaseEntity{
    /**
     * 任务id
     * 表字段 : sys_task.id
     * 
     */
    private Long id;
    /**
     * 任务名称
     * 表字段 : sys_task.task_name
     * 
     */
    private String taskName;

    /**
     * 任务号码
     * 表字段 : sys_task.task_number
     * 
     */
    private String taskNumber;
    
 

    /**
     * 任务所属任务组id
     * 表字段 : sys_task.task_group_id
     * 
     */
    private Long taskGroupId;

    /**
     * 任务所属的用户id
     * 表字段 : sys_task.task_user_id
     * 
     */
    private Long taskUserId;

    /**
     * 任务所属用户名称
     * 表字段 : sys_task.task_user_name
     * 
     */
    private String taskUserName;
    
    private Long taskCount;

    /**
     * 任务发布时间
     * 表字段 : sys_task.create_time
     * 
     */
    private Date createTime;

    /**
     * 任务更新时间
     * 表字段 : sys_task.update_time
     * 
     */
    private Date updateTime;

    /**
     * 任务状态(-1，任务未分配，0，未开始，1，正在进行，2，已完成，3，任务已重新分配)
     * 表字段 : sys_task.status
     * 
     */
    private Byte status;

    /**
     * 获取任务id
     * @return id 任务id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置任务id
     * @param id 任务id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取任务名称
     * @return task_name 任务名称
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * 设置任务名称
     * @param taskName 任务名称
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    /**
     * 获取任务号码
     * @return task_number 任务号码
     */
    public String getTaskNumber() {
        return taskNumber;
    }

    /**
     * 设置任务号码
     * @param taskNumber 任务号码
     */
    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber == null ? null : taskNumber.trim();
    }
    
   
	/**
     * 获取任务所属任务组id
     * @return task_group_id 任务所属任务组id
     */
    public Long getTaskGroupId() {
        return taskGroupId;
    }

    /**
     * 设置任务所属任务组id
     * @param taskGroupId 任务所属任务组id
     */
    public void setTaskGroupId(Long taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    /**
     * 获取任务所属的用户id
     * @return task_user_id 任务所属的用户id
     */
    public Long getTaskUserId() {
        return taskUserId;
    }

    /**
     * 设置任务所属的用户id
     * @param taskUserId 任务所属的用户id
     */
    public void setTaskUserId(Long taskUserId) {
        this.taskUserId = taskUserId;
    }

    /**
     * 获取任务所属用户名称
     * @return task_user_name 任务所属用户名称
     */
    public String getTaskUserName() {
        return taskUserName;
    }

    /**
     * 设置任务所属用户名称
     * @param taskUserName 任务所属用户名称
     */
    public void setTaskUserName(String taskUserName) {
        this.taskUserName = taskUserName == null ? null : taskUserName.trim();
    }

    
    
    public Long getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(Long taskCount) {
		this.taskCount = taskCount;
	}

	/**
     * 获取任务发布时间
     * @return create_time 任务发布时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置任务发布时间
     * @param createTime 任务发布时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取任务更新时间
     * @return update_time 任务更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置任务更新时间
     * @param updateTime 任务更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取任务状态(-1，任务未分配，0，未开始，1，正在进行，2，已完成，3，任务已重新分配)
     * @return status 任务状态(-1，任务未分配，0，未开始，1，正在进行，2，已完成，3，任务已重新分配)
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置任务状态(-1，任务未分配，0，未开始，1，正在进行，2，已完成，3，任务已重新分配)
     * @param status 任务状态(-1，任务未分配，0，未开始，1，正在进行，2，已完成，3，任务已重新分配)
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}