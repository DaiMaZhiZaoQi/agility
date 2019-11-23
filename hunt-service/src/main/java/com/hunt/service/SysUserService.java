package com.hunt.service;

import com.hunt.model.dto.LoginInfo;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.PerFeatureDto;
import com.hunt.model.dto.SysCallLogDeviceRecoDto;
import com.hunt.model.dto.SysDeviceAndCallDto;
import com.hunt.model.dto.SysDeviceCallLogAndRecordDto;
import com.hunt.model.entity.SysPermissionGroup;
import com.hunt.model.entity.SysUser;
import com.hunt.util.Result;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ouyangan
 * @Date : 2016/10/7
 */
public interface SysUserService {
	
	Result init(String loginName,String password);

    long insertUser(SysUser user, String jobIds, String permissionIds);

    /**插入用户权限 权限码格式  device:targetUserId:list
     * @param 用户user
     * @return id
     * */
    long insertUserPermission(SysUser user);
    
    /**
     * 首次授权时添加默认权限
     * @param userId 添加权限的用户id
     * @param obj	需要控制的权限表
     * @return
     */
    Long insertObjPermission(SysPermissionGroup sysPermissionGroup,Long userId,PerFeatureDto preFeature);
    
    /**
     * 弃用
     * @param userId
     * @return
     */
    @Deprecated
    int insertUserPermissionSingle(long userId);
    
    
    boolean isExistLoginName(String loginName);

    void deleteById(SysUser user);

    SysUser selectById(long id);
    
    /**
     * 查询用户及设备
     * @param id 用户id
     * @param page	页码
     * @param row	条数
     * @return
     */
    List<SysDeviceAndCallDto> listUserAndDevice(Long id);
    /**
     * 设置当前机构Id
     * @param curOrgId
     */
    void setCurOrgId(Long curOrgId);
    /**
     * 查询通话记录通过用户id
     * @param id	用户id
     * @return
     */
    List<SysCallLogDeviceRecoDto> listCallDevByUserId(Long id,String sort,String order,Integer page,Integer row,String sType,Long beginTime,Long endTime,String sContent,Integer callIsHaveRecord);
    
    /**
     * 查询当前用户所属通话记录
     * @param id userId
     * @return
     */
    List<SysDeviceAndCallDto> listUserCallRecord(Long id);
    
    /**
     * 查询通话记录及语音信息
     * @param roleOrgId
     * @param sort
     * @param order
     * @param page
     * @param rows
     */
    List<SysDeviceCallLogAndRecordDto> listCallLogRecord(Long roleOrgId,String sort,String order,Integer page,Integer rows);
    
    

    boolean isExistLoginNameExcludeId(long id, String loginName);

    void updateUser(SysUser user, String jobIds, String permissionIds);

    PageInfo selectPage(int page, int rows, String sort, String order, String loginName, String zhName, String email, String phone, String address);

    /**
     * 查询当前用户所在的机构的下有哪些用户包括权限
     * @return
     */
    PageInfo selectUserByOrgId(Long orgId);
    
    /**
     * 查询该机构下的用户
     * @param orgId
     * @return
     */
    List<SysUser> selectSysUserByOrgId(Long orgId);
    
    void updateUser(SysUser user);

    SysUser selectByLoginName(String loginName);

    LoginInfo login(SysUser user, Serializable id, int platform);


}
