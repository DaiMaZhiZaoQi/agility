package com.hunt.dao;

import com.hunt.model.entity.SysUserRoleOrganization;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserRoleOrganizationMapper {
    //新增
    public Long insert(SysUserRoleOrganization SysUserRoleOrganization);

    //更新
    public void update(SysUserRoleOrganization SysUserRoleOrganization);

    //通过对象进行查询
    public SysUserRoleOrganization select(SysUserRoleOrganization SysUserRoleOrganization);

    //通过id进行查询
    public SysUserRoleOrganization selectById(@Param("id") Long id);

    //查询全部
    public List<SysUserRoleOrganization> selectAll();

    //查询数量
    public int selectCounts();

    void deleteUserId(@Param("userId") long userId);
    
    /**
     * 删除该用户的工作
     * @param userId
     * @param roleOrgId
     */
    void deleteByUserRoleOrgId(@Param("userId") long userId,@Param("roleOrgId") Long roleOrgId);

    List<SysUserRoleOrganization> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 查找该用户职位id sys_role_organization_id
     * @param userId
     * @return
     */
    List<Long> selectRoleOrgIdByUserId(@Param("userId") Long userId);

    List<Long> selectByRoleOrganizationId(@Param("roleOrganizationId") long roleOrganizationId);
    
    List<SysUserRoleOrganization> selectByRoleOrgId(@Param("roleOrganizationId") long roleOrganizationId);
    
    
    
    
}