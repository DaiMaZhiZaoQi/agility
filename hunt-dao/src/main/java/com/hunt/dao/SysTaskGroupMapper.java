package com.hunt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hunt.model.dto.PageDto;
import com.hunt.model.entity.SysTaskGroup;

/**
 * 任务组 
 * @author williambai
 *
 */
public interface SysTaskGroupMapper {   

	Long insert(SysTaskGroup sysTaskGroup);
	
	Long updateById(@Param("sysTaskGroup")SysTaskGroup sysTaskGroup,@Param("id")Long id);
	
	/**
	 * 删除
	 * @param sysTaskGroup
	 * @return
	 */
	Long deleteBy(@Param("sysTaskGroup")SysTaskGroup sysTaskGroup);
	
	List<SysTaskGroup> selectById();
	
	/**
	 * 查询任务组详情
	 * @param userId
	 * @param id
	 * @return
	 */
	SysTaskGroup selectByIdUserId(@Param("userId") Long userId,@Param("id")Long id);
	
	/**
	 * 判断用户是否为作者
	 * @param userId
	 * @param id
	 * @return
	 */
	SysTaskGroup selectByIdUserIdAuth(@Param("userId") Long userId,@Param("id")Long id);
	
	/**
	 * 查询任务组
	 * @param pageDto
	 * @return
	 */
	List<SysTaskGroup> selectByPageDto(@Param("pageDto")PageDto pageDto);
	 
	/**
	 * 查询任务组
	 * @param userId
	 * @return 
	 */
	List<SysTaskGroup> selectTaskGroupByUserId(@Param("userId")Long userId); 
	
	/**
	 * 查询任务组
	 * @param taskGroupName
	 * @return
	 */
	SysTaskGroup selectTaskGroupByGroupName(@Param("taskGroupName") String taskGroupName);
}
