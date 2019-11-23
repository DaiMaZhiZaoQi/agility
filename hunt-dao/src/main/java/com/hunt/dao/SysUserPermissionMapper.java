package com.hunt.dao;

import com.hunt.model.entity.SysPermission;
import com.hunt.model.entity.SysUserPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserPermissionMapper {
    //新增
    public Long insert(SysUserPermission sysUserPermission);

    //更新
    public void update(SysUserPermission sysUserPermission);
    
    /**
     * 更新用的万能方法
     * @param sysUserPermission
     */
    void updateDelete(@Param("sysUserPermission")SysUserPermission sysUserPermission,@Param("updateStatus")Integer updateStatus);

    //通过对象进行查询
    public SysUserPermission select(@Param("sysUserPermission")SysUserPermission sysUserPermission);

    //通过id进行查询
    public SysUserPermission selectById(@Param("id") Long id);

    //查询全部
    public List<SysUserPermission> selectAll();

    //查询数量
    public int selectCounts();

    void deleteByUserId(@Param("userId") long userId);
    
    /**
     * 删除
     * @param userId 用户id
     * @param sysPermissionId 权限id
     */
    void deleteByUserIdPid(@Param("userId") Long userId,@Param("sysPermissionId") Long sysPermissionId);
    
    /**
     * 查询该用户下的权限id
     * @param userId
     * @return
     */
    List<Long> selectPdByUserId(@Param("sysUserId") Long sysUserId);

    List<SysUserPermission> selectByUserId(Long userId);
    
    /**
     * 查找权限id
     * @param userId
     * @return
     */
    List<Long> selectPerIdByUserId(@Param("userId")Long userId);
    
    /**
     * 修改状态
     * @param list
     * @param status 1，正常 2，删除
     * @return
     */
    Long updateByPermissionId(@Param("list")List<Long> list,@Param("status") Integer status);
    
    
    
    /**批量插入权限*/
//    int inertDefaultPermission(List<SysPermission> listPermission,int permiGroupId);
}