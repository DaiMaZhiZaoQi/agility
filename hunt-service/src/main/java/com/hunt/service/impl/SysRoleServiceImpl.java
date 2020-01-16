package com.hunt.service.impl;

import com.hunt.dao.SysPermissionMapper;
import com.hunt.dao.SysRoleMapper;
import com.hunt.dao.SysRolePermissionMapper;
import com.hunt.dao.SysUserRoleMapper;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysRoleDto;
import com.hunt.model.entity.SysPermission;
import com.hunt.model.entity.SysRole;
import com.hunt.model.entity.SysRolePermission;
import com.hunt.model.entity.SysUserRole;
import com.hunt.service.SysRoleService;
import com.hunt.util.PermissionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ouyangan
 * @Date 2016/10/14/14:53
 * @Description
 */
@Service
@Transactional
public class SysRoleServiceImpl implements SysRoleService {
    private static Logger log = LoggerFactory.getLogger(SysRoleServiceImpl.class);
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private SysUserRoleMapper mSysUserRoleMapper;
    
    @Autowired
    private SysRolePermissionMapper mSysRolePermissionMapper;
    
    @Autowired
    private SysPermissionMapper mSysPermissionMapper;
    
    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public boolean isExsitRoleName(String name) {
        return sysRoleMapper.isExsitName(name);
    }

    @Override
    public long insertRole(SysRole sysRole, String permissionIds) {
        sysRoleMapper.insert(sysRole);
        String[] permissionIdsArray = permissionIds.split(",");
        for (int i = 0; i < permissionIdsArray.length; i++) {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setSysRoleId(sysRole.getId());
            sysRolePermission.setSysPermissionId(Long.valueOf(permissionIdsArray[i]));
            sysRolePermissionMapper.insert(sysRolePermission);
        }
        return sysRole.getId();
    }

    @Override
    public boolean isExsitRoleNameExcludeId(long id, String name) {
        return sysRoleMapper.isExsitNameExcludeId(id, name);
    }

    @Override
    public SysRole selectById(long id) {
        return sysRoleMapper.selectById(id);
    }

    @Override
    public PageInfo selectPage(int page, int row) {
        int counts = sysRoleMapper.selectCounts();
        List<SysRole> sysRoles = sysRoleMapper.selectAll();

        List<SysRoleDto> sysRoleDtoList = new ArrayList<>();
        for (int i = 0; i < sysRoles.size(); i++) {
            SysRoleDto sysRoleDto = new SysRoleDto();
            BeanUtils.copyProperties(sysRoles.get(i), sysRoleDto);
            List<SysRolePermission> sysRolePermissionList = sysRolePermissionMapper.selectByRoleId(sysRoles.get(i).getId());

            List<SysPermission> sysPermissionList = new ArrayList<>();
            for (int j = 0; j < sysRolePermissionList.size(); j++) {
            	Long sysPermissionId = sysRolePermissionList.get(j).getSysPermissionId();
                SysPermission sysPermission = sysPermissionMapper.selectById(sysRolePermissionList.get(j).getSysPermissionId());
                System.out.println("--->"+sysRoles.get(i).getId()+"sysPermissionId-->"+sysPermissionId+"-->"+sysPermission); 
                sysPermissionList.add(sysPermission);
            }
            sysRoleDto.setSysPermissions(sysPermissionList);
            sysRoleDtoList.add(sysRoleDto);
        }
        PageInfo pageInfo = new PageInfo(counts, sysRoleDtoList);
        return pageInfo;
    }
    
    
    /**
	 * 移动端判断用户是否有权限
	 * @param userId
	 * @param permission
	 * @return
	 */
	private boolean mobileHasPermission(Long userId,String permission) {
		//  查询用户角色 
		List<Long> listPermissionId = mSysUserRoleMapper.selectPerIdByUserId(userId);
		SysPermission sysPermission = mSysPermissionMapper.selectByCode(permission);
		return listPermissionId.contains(sysPermission.getId());
		
	}  

    @Override
	public PageInfo selectPageUserId(int page, int row, long userId) {
//    	Long existRole=0l;
//    	List<SysUserRole> listRole = mSysUserRoleMapper.selectByUserId(userId);
//    	for(SysUserRole role:listRole) {
//    		Long roleId = role.getId();
//    		List<SysRolePermission> listRolePermission = mSysRolePermissionMapper.selectByRoleId(roleId);
//    		for(SysRolePermission sysRolePermission:listRolePermission) {
//    			Long sysPermissionId = sysRolePermission.getSysPermissionId();
//    			SysPermission sysPermission = mSysPermissionMapper.selectById(sysPermissionId);
//    			String code= sysPermission.getCode();
//    			if(code.equals("system:manage")) {
//    				existRole=roleId;
//    			}
//    		}
//    	}
//    	
    	boolean mobileHasPermission = mobileHasPermission(userId,"system:manage");
    	
    	
    	
    	
    	 int counts = sysRoleMapper.selectCounts();
         List<SysRole> sysRoles = sysRoleMapper.selectAll();

         List<SysRoleDto> sysRoleDtoList = new ArrayList<>();
         for (int i = 0; i < sysRoles.size(); i++) {
             SysRoleDto sysRoleDto = new SysRoleDto();
             SysRole sysRole = sysRoles.get(i);
             String name = sysRole.getName();
             if("系统管理员".equals(name)&&!mobileHasPermission)continue;
             BeanUtils.copyProperties(sysRoles.get(i), sysRoleDto);
             List<SysRolePermission> sysRolePermissionList = sysRolePermissionMapper.selectByRoleId(sysRoles.get(i).getId());

             List<SysPermission> sysPermissionList = new ArrayList<>();
             for (int j = 0; j < sysRolePermissionList.size(); j++) {
             	Long sysPermissionId = sysRolePermissionList.get(j).getSysPermissionId();
                 SysPermission sysPermission = sysPermissionMapper.selectById(sysRolePermissionList.get(j).getSysPermissionId());
                 
                 System.out.println("--->"+sysRoles.get(i).getId()+"sysPermissionId-->"+sysPermissionId+"-->"+sysPermission); 
                 sysPermissionList.add(sysPermission);
                 
             }
             
             sysRoleDto.setSysPermissions(sysPermissionList);
             sysRoleDtoList.add(sysRoleDto);
         }
         PageInfo pageInfo = new PageInfo(counts, sysRoleDtoList);
         return pageInfo;
	}

	@Override
    public void deleteRole(SysRole sysRole) {
        sysRoleMapper.update(sysRole);
        sysRolePermissionMapper.deleteByRoleId(sysRole.getId());
    }

    @Override
    public void updateRole(SysRole sysRole, String permissionIds) {
        sysRoleMapper.update(sysRole);
        sysRolePermissionMapper.deleteByRoleId(sysRole.getId());
        String[] pIds = permissionIds.split(",");
        for (int i = 0; i < pIds.length; i++) {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setSysRoleId(sysRole.getId());
            sysRolePermission.setSysPermissionId(Long.valueOf(pIds[i]));
            sysRolePermissionMapper.insert(sysRolePermission);
        }
    }
}
