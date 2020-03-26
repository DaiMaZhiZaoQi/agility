package com.hunt.model.entity;

import com.hunt.model.entity.SysTask;

public class SysTaskWithBLOBs extends SysTask {
    /**
     * 任务信息
     * 表字段 : sys_task.task_msg
     * 
     */
    private String taskMsg;

    /**
     * 任务更新时间链
     * 表字段 : sys_task.task_time_chain
     * 
     */
    private String taskTimeChain;

    /**
     * 获取任务信息
     * @return task_msg 任务信息
     */
    public String getTaskMsg() {
        return taskMsg;
    }

    /**
     * 设置任务信息
     * @param taskMsg 任务信息
     */
    public void setTaskMsg(String taskMsg) {
        this.taskMsg = taskMsg == null ? null : taskMsg.trim();
    }

    /**
     * 获取任务更新时间链
     * @return task_time_chain 任务更新时间链
     */
    public String getTaskTimeChain() {
        return taskTimeChain;
    }

    /**
     * 设置任务更新时间链
     * @param taskTimeChain 任务更新时间链
     */
    public void setTaskTimeChain(String taskTimeChain) {
        this.taskTimeChain = taskTimeChain == null ? null : taskTimeChain.trim();
    }
}