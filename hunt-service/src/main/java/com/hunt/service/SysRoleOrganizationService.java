package com.hunt.service;


import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysRoleOrganizationTree;
import com.hunt.model.entity.SysRoleOrganization;
import com.hunt.model.entity.SysUser;

import java.util.List;

/**
 * @Author ouyangan
 * @Date 2016/10/17/16:30
 * @Description
 */
public interface SysRoleOrganizationService {
    boolean isExistName(String name, long parentId);

    long insertRoleOrganization(SysRoleOrganization roleOrganization);

    boolean isExistNameExcludeId(long id, String name, long parentId);

    void updateRoleOrganization(SysRoleOrganization roleOrganization);

    SysRoleOrganization selectRoleOrganizationById(long id);

    PageInfo selectPage(int page, int rows, long id);
    public void setQueryType(int queryType);
    /**
     * 查询机构id
     * @param id	顶级id
     * @return
     */
    long selectOrganizationIdById(long id);
    
    /**
     * 查看当前机构下的设备人员
     * @param id   机构表顶级id
     * @return
     */
    PageInfo selectOrganizationUnderPerson(long id);
    PageInfo selectUserIdByRoleOrgId(List<Long> listRoleOrgId);
    PageInfo selectUserById(List<Long> listUserId);
    
    /**
     * 查询当前机构下所有用户
     * @param id 机构id
     * @return
     */
    List<SysUser> selectSysUserByOrganization(Long id);
    
    
    public SysRoleOrganizationTree selectSysRoleOrganizationTree(long id);

    public List<SysRoleOrganizationTree> selectSysRoleOrganizationTreeChildrenList(long id);
}
