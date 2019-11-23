package com.hunt.dao;

import com.hunt.model.entity.SysPermissionGroup;
import com.hunt.model.entity.SysUser;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysPermissionGroupMapper {
    //新增
    public Long insert(SysPermissionGroup SysPermissionGroup);

    
    
    //更新
    public void update(SysPermissionGroup SysPermissionGroup);
    
    /**
     * 假删除的权限组
     * @param sysUser
     */
    public void updatePermissionGroup(SysUser sysUser);
    
    

    //通过对象进行查询
    public SysPermissionGroup select(SysPermissionGroup SysPermissionGroup);

    //通过id进行查询
    public SysPermissionGroup selectById(@Param("id") Long id);

    //查询全部
    public List<SysPermissionGroup> selectAll();

    //查询数量
    public int selectCounts();

    boolean isExistGroupName(@Param("name") String name);
    /**查询权限组id*/
    public int selectIdByGroupName(@Param("name") String name);
    
    /**
     * 修改通讯录状态
     * @param id  sys_permission_group id
     * @param status 1,正常  2,删除
     * @return
     */
    Long updateStatus(@Param("id") Long id,@Param("status") Integer status);
    
    SysPermissionGroup selectByGroupName(@Param("permissionGroupName")String permissionGroupName);
}