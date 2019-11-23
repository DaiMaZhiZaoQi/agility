package com.hunt.dao;
/**
 * 测试Mapper
 * @author williambai
 *
 */

import java.util.List;

import com.hunt.model.entity.SysPermissionGroupTest;

public interface SysPermissionGroupTestMapper {

	public Long insert(SysPermissionGroupTest sysPermissionGroupTest);
	
	public List<Long> insertBatch(List<SysPermissionGroupTest> list);
}
