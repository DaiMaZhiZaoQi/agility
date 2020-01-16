package com.hunt.dao;

import com.hunt.model.entity.SysOrganization;
import com.hunt.model.entity.SysUser;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysOrganizationMapper {
    //新增
    public Long insert(SysOrganization SysOrganization);

    //更新
    public void update(SysOrganization SysOrganization);

    //通过对象进行查询
    public SysOrganization select(SysOrganization SysOrganization);

    //通过id进行查询
    public SysOrganization selectById(@Param("id") Long id);

    //查询全部
    public List<SysOrganization> selectAll();

    /**查询数量*/
    public int selectCounts();

    List<SysOrganization> selectChildren(@Param("parentId") long parentId);

    boolean isExistFullName(@Param("fullName") String fullName);
    
    /**
     * 查询同一机构下是否存在相同机构名
     * @param name
     * @param parentId
     * @return
     */
    boolean isExistOrgName(@Param("name") String name,@Param("parentId") Long parentId);

    boolean isExistFullNameExcludeId(@Param("id") long id, @Param("fullName") String fullName);
    
    boolean isExistNameExcludeParentId(@Param("parentId")Long parId  ,@Param("name") String name);
    
    /**
     * 查询机构码集合
     * @param orgCode
     * @return
     */
    String selectOrgCodeByParentId(@Param("parentId") long parentId);
    
    SysOrganization selectIdByUserId(@Param("sysUserId") Long sysUserId);

    /**
     * 查询以orgCode开头的机构
     * @param orgCode
     * @return
     */
    List<SysOrganization> selectByOrgCode(@Param("orgCode")String orgCode);
    
    
}