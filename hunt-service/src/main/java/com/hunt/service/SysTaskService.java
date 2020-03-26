package com.hunt.service;
/**
 * 我的任务
 * @author williambai
 *
 */

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.hunt.model.dto.PageDto;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.entity.SysTask;
import com.hunt.model.entity.SysTaskGroup;
import com.hunt.model.entity.SysTaskWithBLOBs;
import com.hunt.model.entity.SysUser;
import com.hunt.util.Result;


public interface SysTaskService {
	
	/**
	 * 查询任务组详情
	 * @param userId
	 * @param sysTaskGroupId
	 * @return
	 */
	SysTaskGroup selectGroupDetail(Long userId,Long sysTaskGroupId);
	
	/**
	 * 查询任务列表
	 * @param pageDto
	 * @return
	 */
	PageInfo selectTaskList(PageDto pageDto);
	
	/**
	 * 查询未分配的任务
	 * @param userId
	 * @param taskGroupId
	 * @param status
	 * @return
	 */
	PageInfo selectUnDisPatch(Long userId,Long taskGroupId,Integer status);
	
	/**
	 * 一键派发任务
	 * @param userId
	 * @param listTaskId
	 * @param listUserId
	 * @return
	 */
	Result oneKeyDisPatch(Long taskGroupId,Long userId,List<String> listTaskId,List<SysUser> listUser);
	
	
	
	/**
	 * 派发任务 两集合比较,更新状态，或添加新的
	 * @param taskGroupId	任务组id
	 * @param sysUser		用户
	 * @param listTaskSelectId	任务id   选中的id
	 * @param listTaskAllId	全部id
	 * @return
	 */
	Result updateTask(Long taskGroupId,SysUser sysUser,List<String> listTaskSelectId,List<String> listTaskAllId);
	
	/**
	 * 更新任务
	 * @param sysTaskWithBLOBs
	 * @param taskGroupId
	 * @return
	 */
	Result updateTaskMsg( SysTaskWithBLOBs sysTaskWithBLOBs,Long taskGroupId);
//	Result updateCompleteTask(String telPhone,Integer callType,);

	/**
	 * 查询任务组
	 * @param pageDto
	 * @return
	 */
	PageInfo selectTaskGroup(PageDto pageDto);
	/**
	 * 文件转码UTF-8
	 * @param fileTask  上传后的文件
	 * @return
	 * @throws IOException 
	 */
	File convertUtf8(File fileTask) throws IOException;
	
	Result insertTask(File newFileTask, SysUser sysUser);

	/**
	 * 删除任务组
	 * @param taskGroupId
	 * @param userId
	 * @return
	 */
	Result deleteTaskGroup(Long taskGroupId, Long userId);
	
}
