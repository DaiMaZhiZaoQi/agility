package com.hunt.service;



import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysOrganizationTree;
import com.hunt.model.dto.SysPermissionOrgDto;
import com.hunt.model.entity.SysOrganization;
import com.hunt.model.entity.SysUserOrganization;

import java.sql.Date;
import java.util.List;

/**
 * @Author: ouyangan
 * @Date : 2016/10/10
 */
public interface SysOrganizationService {
	
    Long insertOrganization(SysOrganization sysOrganization);
    
    
    
    int deleteOrganization(long id);

    void updateOrganization(SysOrganization organization);
    
    /**
     * 修改机构，子机构，机构码
     * @param organization
     */
    void updateOrganChangeOrgCode(SysOrganization organization);
    
    /**
     * 
     * @param page  页面
     * @param row	行数
     * @param id	机构id
     * @return
     */
    PageInfo selectPage(int page, int row, long id);
    
    /**
     * 查询一个机构下的子机构
     * @param page
     * @param row
     * @param listOrgId
     * @return
     */
    PageInfo selectPageList(int page, int row, List<Long> listOrgId);
    
    /**
     * 查询机构下的用户
     * @param orgIds
     * @return
     */
    PageInfo selectOrgUser(String orgIds);
    
    /**
     * 查询用户的机构权限
     * @param page
     * @param row
     * @param userId
     * @return
     */
    PageInfo selectPageListSimple(int page,int row,long userId);
    
    /**
     * 查询该用户下的机构再查询该机构下的用户
     * @param userId
     * @return
     */
    List<Long> selectUserIdByUserId(Long userId);
    
    /**
     * 查询该部门下的所有
     * @param page
     * @param row
     * @param id
     * @return
     */
    PageInfo selectPageNoTree(int page, int row, long id);
    
    
    /**
     * 查询机构及其机构下的子机构
     * @param page	查询页码
     * @param row	查询行数
     * @param id	机构id
     * @return
     */
    public PageInfo selectOrgFromOrgId(Integer page,Integer row,Long id);
    
    /**
     * 从机构id来查找设备
     * @param page	查询页码
     * @param row	查询行数
     * @param id	机构id
     * @return
     */
    public PageInfo selectDeviceFromOrg(int page,int row,Long id);
    
    /**
     * 查询通话记录
     * @param page
     * @param row
     * @param id	机构id
     * @return
     */
    public PageInfo selectDeviceCallRecordFromOrg(int page,int row,Long id);
    
    /**
     * 通过机构id查询通话记录
     * @param id orgId
     * @param sort
     * @param order
     * @param page
     * @param rows
     * @return
     */
    public PageInfo selectDeviceCallbyOrg(Long id,String sort,String order,Integer page,Integer rows);
    
    /**
     * 查询机构下的所有设备
     * @param id  机构id
     * @param sort
     * @param order
     * @param page
     * @param rows
 	 * @param sType 0 默认查询，1，查询 通话号码，2，查询设备号  3,查询时间段
	 * @param sTimeType 1,近三天  2，近 七天 3，近半月 4，近一月 5，近三月 6,近半年 7 今年内 8 一年前
	 * @param sContent	搜索内容
     * @return
     */
    public PageInfo selectAllCallLogByOrg(Long id,String sort,String order,Integer page,Integer rows,String sType,Long beginTime,Long endTime,String sContent,Integer callIsHaveRecord);
    
    /**
     * 2019/06/26  
     * 查询机构列表非树形结构,用于全部设备的展现
     * @param id  机构表顶级id
     * @return
     */
    public List<SysOrganizationTree> selectSysOrganizationList(long id,List<SysOrganizationTree> list);
    
    /**
     * Client，查询所属机构
     * @param id
     * @return
     */
    PageInfo selectPageC(long id);

    public SysOrganizationTree selectSysOrganizationTree(long id);

    /**
     * 查询一个机构下的子机构  比如    xxx事业部-->技术部-->技术经理-->员工    
     * @param id  机构id
     * @return
     */
    public List<SysOrganizationTree> selectChildrenTreeList(long id);

    boolean isExistFullName(String fullName);
    
    /**
     * 是否存在相同机构名称
     * @param orgName
     * @param parentId
     * @return
     */
    boolean isExistOrgName(String orgName,Long parentId);

    SysOrganization selectOrganization(long id);

    boolean isExistFullNameExcludeId(long id, String fullName);
    
    /**
     * 查询该机构下的所有机构的最大机构代码
     * @param orgCode  机构代码
     * @return
     */
    String selectOrgCodeByParentId(long parentId);
    
    /**
     * 查询机构列表及其子机构
     * @param 机构顶级id
     * @param 机构列表
     * @param 加载子项列表
     */
    public List<SysOrganization> selectSysOrgList(Long id ,List<SysOrganization> list,boolean loadChild);
    
    
    /**
     * 查询机构下的机构id
     * @param id 机构id
     * @param listOrgId
     * @param loadChild
     * @return
     */
    public List<Long> selectSysOrgIdList(Long id,List<Long> listOrgId,boolean loadChild);
    /**
     * 查询上级部门
     * @param orgId
     * @param list
     * @return
     */
    public List<SysOrganization> selectUpSysOrgList(Long orgId,List<SysOrganization> list);
    
    /**
     * 向上查询机构id
     * @param orgId
     * @param list
     * @return
     */
    public List<Long> selectUpSysOrgIdList(Long orgId,List<Long> list);
    
    
    
    public List<SysUserOrganization> selectUserOrg(String orgIds,Long userId);
    
    
}
