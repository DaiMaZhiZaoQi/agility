package com.hunt.service.impl;

import com.hunt.dao.SysOrganizationMapper;
import com.hunt.dao.SysRoleMapper;
import com.hunt.dao.SysRoleOrganizationMapper;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysRoleOrganizationTree;
import com.hunt.model.entity.SysOrganization;
import com.hunt.model.entity.SysRole;
import com.hunt.model.entity.SysRoleOrganization;
import com.hunt.model.entity.SysUser;
import com.hunt.service.SysRoleOrganizationService;

import org.apache.commons.logging.Log;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
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
 * @Date 2016/10/17/16:30
 * @Description
 */
@Service
@Transactional
public class SysRoleOrganizationServiceImpl implements SysRoleOrganizationService {
    private static Logger log = LoggerFactory.getLogger(SysRoleOrganizationServiceImpl.class);
    @Autowired
    private SysRoleOrganizationMapper roleOrganizationMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysOrganizationMapper sysOrganizationMapper;
    
    /**查询类型 0，查询该部门下的User 1, 把User对象转成Organization对象*/
    private int queryType;

    @Override
    public boolean isExistName(String name, long parentId) {
        return roleOrganizationMapper.isExistName(name, parentId);
    }

    @Override
    public long insertRoleOrganization(SysRoleOrganization roleOrganization) {
        Long id = roleOrganizationMapper.insert(roleOrganization);
        return id;
    }

    @Override
    public boolean isExistNameExcludeId(long id, String name, long parentId) {
        return roleOrganizationMapper.isExistNameExcludeId(id, name, parentId);
    }

    @Override
    public void updateRoleOrganization(SysRoleOrganization roleOrganization) {
        roleOrganizationMapper.update(roleOrganization);
    }

    @Override
    public SysRoleOrganization selectRoleOrganizationById(long id) {
        return roleOrganizationMapper.selectById(id);
    }

    @Override
    public PageInfo selectPage(int page, int rows, long id) {
    	log.debug("page-->"+page+"-->rows-->"+rows+"--id-->"+id);
        int counts = roleOrganizationMapper.selectCounts();
        SysRoleOrganizationTree tree = selectSysRoleOrganizationTree(id);
        List<SysRoleOrganizationTree> list = new ArrayList<>();
        list.add(tree);
        return new PageInfo(counts, list);
    }
    
	@Override
	public long selectOrganizationIdById(long id) {
		SysRoleOrganization sysRoleOrganization = roleOrganizationMapper.selectById(id);
		if(sysRoleOrganization!=null) {
			return sysRoleOrganization.getSysOrganizationId();
		}
		return 0;
	}

    //查询职位树形结构
    @Override
    public SysRoleOrganizationTree selectSysRoleOrganizationTree(long id) {
        SysRoleOrganizationTree tree = new SysRoleOrganizationTree();
        SysRoleOrganization roleOrganization = roleOrganizationMapper.selectById(id);
        log.debug("roleOrganization :{}", roleOrganization);
        BeanUtils.copyProperties(roleOrganization, tree);
        if (roleOrganization == null) {
            return null;
        }
        SysRole role = sysRoleMapper.selectById(roleOrganization.getSysRoleId());
        log.debug("role :{}", role);
        if (role != null) {
            tree.setSysRoleName(role.getName());
        }
        SysOrganization organization = sysOrganizationMapper.selectById(roleOrganization.getSysOrganizationId());
        if (organization != null) {
            tree.setSysOrganizationName(organization.getName());
        }
        List<SysRoleOrganizationTree> childrenList = selectSysRoleOrganizationTreeChildrenList(id);
        tree.setChildren(childrenList);
        for (int i = 0; i < childrenList.size(); i++) {
            tree.getChildren().set(i, selectSysRoleOrganizationTree(childrenList.get(i).getId()));
        }
        return tree;
    }

    //查询子目录
    @Override
    public List<SysRoleOrganizationTree> selectSysRoleOrganizationTreeChildrenList(long id) {
        List<SysRoleOrganization> childrenList = roleOrganizationMapper.selectChildren(id);
        List<SysRoleOrganizationTree> childrenTreeList = new ArrayList<>();
        for (SysRoleOrganization s : childrenList) {
            SysRoleOrganizationTree sysOrganizationTree = new SysRoleOrganizationTree();
            BeanUtils.copyProperties(s, sysOrganizationTree);
            childrenTreeList.add(sysOrganizationTree);
        }
        return childrenTreeList;
    }
    
    @Override
	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}
//    @RequiresPermissions(value="job:insert") 
	@Override
	public PageInfo selectOrganizationUnderPerson(long id) {
		List<Long> listRoleOrgId = roleOrganizationMapper.selectIdByOrgId(id);
		System.out.println("-->selectOrganizationUnderPerson--->"+listRoleOrgId.toString());
		return selectUserIdByRoleOrgId(listRoleOrgId);
	}
	
	
	@Override
	public List<SysUser> selectSysUserByOrganization(Long id) {
		List<Long> listRoleOrgId = roleOrganizationMapper.selectIdByOrgId(id);							//  查询用户角色id
		List<Long> listUserId=new ArrayList<>();
	  for(Long roleOrgId:listRoleOrgId) {
		  
		List<Long> listSelectIdByOrgId = roleOrganizationMapper.selectUserIdByRoleOrgId(roleOrgId); 	//  一个部门下的用户id
		if(listSelectIdByOrgId!=null) {
			listUserId.addAll(listSelectIdByOrgId);
		}
	  }
	  if(listUserId.size()>0) {
		  List<SysUser> listUser=new ArrayList<>();
		  for(Long userId:listUserId) {
				//   TODO 嵌入查询权限，没有查询权限在该处开启过滤，对当前操作的主体进行权限规则判断，有这个数据权限，则添加
//				boolean hasDataPermission = hasDataPermission(userId,"device:"+userId+":list");
//				if(hasDataPermission) {
//					SysUser sysUser= roleOrganizationMapper.selectUserById(userId);
					SysUser sysUser= roleOrganizationMapper.selectUserOnLineById(userId);
					if(sysUser!=null) {
						listUser.add(sysUser);
					}
//				}
			}
			if(listUser.size()>0) {
				return listUser;
			}
	  }
		return null;
	}

	/**
	 * 根据用户机构id,查询用户id
	 * @param id
	 * @return
	 */
	@Override
	public PageInfo selectUserIdByRoleOrgId(List<Long> listRoleOrgId){
		List<Long> listUserId=new ArrayList<>();
			System.out.println("listRoleOrgId-->"+listRoleOrgId.toString()); 
		  for(Long roleOrgId:listRoleOrgId) {
			  
			List<Long> listSelectIdByOrgId = roleOrganizationMapper.selectUserIdByRoleOrgId(roleOrgId); 	//  一个部门下的用户id
			if(listSelectIdByOrgId!=null) {
				listUserId.addAll(listSelectIdByOrgId);
			}
		  }
		  if(listUserId.size()>0) {
			  return selectUserById(listUserId);
		  }
		  return null;
	}
	
	@Override
	public PageInfo selectUserById(List<Long> listUserId) {
		List listData=null;
		if(queryType==0) {
			List<SysUser> listUser=new ArrayList<>();
			listData=listUser;
		}else if(queryType==1) {
			List<SysOrganization> listOrg=new ArrayList<>();
			listData=listOrg;
		}
		for(Long userId:listUserId) {
			
			//   TODO 嵌入查询权限，没有查询权限在该处开启过滤，对当前操作的主体进行权限规则判断，有这个数据权限，则添加
			boolean hasDataPermission = hasDataPermission(userId,"device:"+userId+":list");
			if(hasDataPermission) {
//				SysUser sysUser= roleOrganizationMapper.selectUserById(userId);
				SysUser sysUser= roleOrganizationMapper.selectUserOnLineById(userId);
				if(sysUser==null)continue;
				if(queryType==1) { 
					SysOrganization sysOrg = sysUserConvertSysOrg(sysUser);
					listData.add(sysOrg);
				}else {
					listData.add(sysUser);
				}
			}
		}
		if(listData!=null) {
			return new PageInfo(listData.size(), listData);
		}
		return null;
	}
	
	/**
	 * 是否有该数据权限
	 * @param targetUserId  目标用户id
	 * @param permission  	权限码   (device:targetUserId:list) 该用户的设备信息展示，(device:targetUserId:updateXXX) 修改XXX
	 * TODO hasDataPermission 查询有权限的用户的 
	 * @return
	 */
	public boolean hasDataPermission(Long targetUserId,String permission) {
		//  权限在添加用户的初期会默认插入几条权限，   list, update。当前用户默认就有该权限
		// 查询该权限对应的权限id
		// 查询当前用户的所拥有的权限id，所拥有的的权限id，应该在一登录的时候就保存了。工具方法直接获取
		Subject subject = SecurityUtils.getSubject();
		boolean permitted = subject.isPermitted(permission);
		System.out.println("hasDataPermission-->"+permitted);
		return permitted;
	}
	
	
	/**
	 * 用户转变成机构类型    解决user表与SysOrganization  实体不一致问题
	 * @param sysUser
	 * @return
	 */
	private SysOrganization sysUserConvertSysOrg(SysUser sysUser) {
		SysOrganization sysOrganization=new SysOrganization();
//		String loginName=sysUser.getLoginName();
		String loginName=sysUser.getZhName();
		long id=sysUser.getId();
		sysOrganization.setName(loginName);
		sysOrganization.setId(id);
		sysOrganization.setOrgType(1);
		return sysOrganization;
	}
	
	
	
	



}
