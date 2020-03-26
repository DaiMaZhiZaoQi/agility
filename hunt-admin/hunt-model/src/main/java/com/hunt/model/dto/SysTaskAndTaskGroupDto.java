package com.hunt.model.dto;

import java.util.List;

import com.hunt.model.entity.BaseEntity;
import com.hunt.model.entity.SysTask;
import com.hunt.model.entity.SysTaskGroup;
import com.hunt.model.entity.SysTaskWithBLOBs;

/**
 * 任务和任务组
 * @author williambai
 *
 */
public class SysTaskAndTaskGroupDto extends BaseEntity{
	
	private SysTaskGroup mSysTaskGroup;
	private List<SysTaskWithBLOBs> mSysTask;

	public SysTaskGroup getmSysTaskGroup() {
		return mSysTaskGroup;
	}
	public void setmSysTaskGroup(SysTaskGroup mSysTaskGroup) {
		this.mSysTaskGroup = mSysTaskGroup;
	}
	public List<SysTaskWithBLOBs> getmSysTask() {
		return mSysTask;
	}
	public void setmSysTask(List<SysTaskWithBLOBs> mSysTask) {
		this.mSysTask = mSysTask;
	}
	
	
}
