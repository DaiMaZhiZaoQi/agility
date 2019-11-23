package com.hunt.dao;

import com.hunt.model.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysPermissionMapper {
    //新增
    public Long insert(SysPermission SysPermission);

    //更新
    public void update(SysPermission SysPermission);

    //通过对象进行查询
    public SysPermission select(SysPermission SysPermission);

    //通过id进行查询
    public SysPermission selectById(@Param("id") Long id);

    //查询全部
    public List<SysPermission> selectAll();

    //查询数量
    public int selectCounts();

    boolean isExistName(@Param("groupId") long groupId, @Param("name") String name);

    boolean isExistCode(@Param("groupId") long groupId, @Param("code") String code);

    boolean isExistNameExcludeId(@Param("id") long id, @Param("groupId") long groupId, @Param("name") String name);

    boolean isExistCodeExcludeId(@Param("id") long id, @Param("groupId") long groupId, @Param("code") String code);
    /**通过机构码查询permission的id*/
    List<SysPermission> selectIdByCode(List<String> list);
    /**用户插入默认权限*/
     Long inertDefaultPermission(List<SysPermission> list);
    
    int inertDefaultPermissionSingle(SysPermission sysPermission);
    
    /**
     * 修改权限状态
     * @param status
     * @param sysPgId
     * @return
     */
    Long updateStatus(@Param("status")Integer status,@Param("sysPgId") Long sysPgId);
    
    /**
     * 查询相关权限组的权限
     * @param sysPgId
     * @return
     */
    List<SysPermission> selectByPgId(@Param("sysPgId") Long sysPgId);
    /**
     * DONE deletPermissionByUserId 根据用户id假删除权限
     * @param userId  用户id
     */
    void deletPermissionByUserId(@Param("userId")long userId);
    
    /**
     * 通过权限码查找权限
     * @param code
     * @return
     */
    SysPermission selectByCode(@Param("code") String code);
    
}