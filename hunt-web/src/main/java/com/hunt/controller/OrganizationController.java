package com.hunt.controller;

import com.github.pagehelper.StringUtil;
import com.hunt.controller.BaseController;
import com.hunt.dao.SysOrganizationMapper;
import com.hunt.dao.SysUserInOrgMapper;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.entity.SysOrganization;
import com.hunt.model.entity.SysUserInOrg;
import com.hunt.model.entity.SysUserOrganization;
import com.hunt.properties.PropertiesUtil;
import com.hunt.service.SysOrganizationService;
import com.hunt.service.SysRoleOrganizationService;
import com.hunt.service.impl.SysOrganizationServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hunt.util.PermissionCode;
import com.hunt.util.ResponseCode;
import com.hunt.util.Result;

/**
 * 组织机构模块
 */
@Api(value = "组织机构模块")
@Controller
@RequestMapping("organization")
public class OrganizationController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(OrganizationController.class);

    @Autowired
    private SysOrganizationService sysOrganizationService;
    
    @Autowired
    private SysRoleOrganizationService sysRoleOrganizationService;
    @Autowired
    private SysOrganizationMapper mSysOrganizationMapper;
    @Autowired
    private SysUserInOrgMapper mSysUserInOrgMapper;
    
    @Autowired
    private PropertiesUtil mProperUtil;
   
    
    @Autowired
    private SysOrganizationServiceImpl mSysOrganizationServiceImpl;
    
    @ApiOperation(value = "跳转至组织机构", httpMethod = "GET", produces = "text/html")
    @RequiresPermissions("org:manage")
    @RequestMapping(value = "organization", method = RequestMethod.GET)
    public String organization() {
    	System.out.println("list--organization");
    	log.info("录音存储路径"+mProperUtil.getRecordFilePath());
        return "system/organization";
    }

    /**
     * 
     * 12/5 新增字段 机构代码
     * 新增机构
     *
     * @param name        名称
     * @param description 描述
     * @param fullName    全称
     * @param parentId    父级id
     * @param isFinal     是否可修改
     * @return
     */
    @ApiOperation(value = "新增机构", httpMethod = "POST", produces = "application/json", response = Result.class)
    @RequiresPermissions("org:manage")
    @ResponseBody
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Result insert(@RequestParam String name,
                         @RequestParam String description,
                         @RequestParam String fullName,
                         @RequestParam long parentId,
                         @RequestParam String parentOrgCode,
                         @RequestParam(defaultValue = "1") int isFinal) {
//        boolean isExistFullName = sysOrganizationService.isExistFullName(fullName);
        boolean isExistFullName = sysOrganizationService.isExistOrgName(name, parentId);
        if (isExistFullName) {
            return Result.error(ResponseCode.name_already_exist.getMsg());
        }
        SysOrganization organization = new SysOrganization();
        organization.setFullName(fullName);
        organization.setName(name);
        organization.setDescription(description);
        organization.setParentId(parentId);
        organization.setIsFinal(isFinal);
        log.debug("新增机构"+parentOrgCode+"---"+parentId); 
        String orgCode = getOrgCode(parentId,parentOrgCode);		//  插入机构码 
        organization.setOrgCode(orgCode);
        
        long i = sysOrganizationService.insertOrganization(organization);
        return Result.success();
    }
    
    /**
     * 设置机构码，根据上级机构码得到该级别的机构码，如果大机构分支较多，则需要调整大机构的机构码，依次累加
     * 如果该级的机构较多则依次递增
     * @param parentId			上一级id
     * @param parentOrgCode				上一级机构码    TODO 机构码为3位数
     * @return
     */
    private String getOrgCode(long parentId,String parentOrgCode) {
    	String orgCode = sysOrganizationService.selectOrgCodeByParentId(parentId);
    	if(parentOrgCode.equals("00")) {		//  大机构
    		if(StringUtil.isNotEmpty(orgCode)) {
    			int parseInt = Integer.parseInt(orgCode);
    			parseInt++;
    			log.debug("getOrgCode大集团--->"+""+parseInt);
    			if(orgCode.startsWith("0")) {
    				return "0"+parseInt;
    			}
    			return parseInt+"";
    		}else {
    			return "01";
    		}
    	}else {							//  小机构
    		if(StringUtil.isNotEmpty(orgCode)) {
    			
    			int parseInt = Integer.parseInt(orgCode);
    			parseInt++;
    			log.debug("getOrgCode集团子公司--->"+""+parseInt);
    			if(orgCode.startsWith("0")) {
    				return "0"+parseInt;
    			}
    			return parseInt+"";
    		}else {
    			return parentOrgCode+"00";
    		}
    	}
    	//  得到 parentid==0的机构  
    }
    

    /**
     * 删除机构
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除机构", httpMethod = "GET", produces = "application/json", response = Result.class)
    @RequiresPermissions("org:manage")
    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public Result delete(@RequestParam long id) {
        SysOrganization sysOrganization = sysOrganizationService.selectOrganization(id);
        if (sysOrganization == null) {
            return Result.error(ResponseCode.data_not_exist.getMsg());
        }
        if (sysOrganization.getIsFinal() == 2) {
            return Result.error(ResponseCode.can_not_edit.getMsg());
        }
        int i = sysOrganizationService.deleteOrganization(id);
        return Result.success();
    }

    /**
     * 更新机构
     *
     * @param id          机构id
     * @param name        名称
     * @param description 描述
     * @param fullName    全称
     * @param parentId    父级id
     * @return
     */
    @ApiOperation(value = "更新机构", httpMethod = "POST", produces = "application/json", response = Result.class)
    @RequiresPermissions("org:manage")
    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestParam long id,
                         @RequestParam String name,
                         @RequestParam String fullName,
                         @RequestParam String description,
                         @RequestParam(name="parentId",required=false,defaultValue="0") long parentId) {
        SysOrganization sysOrganization = sysOrganizationService.selectOrganization(id);
        if (sysOrganization == null) {
            return Result.error(ResponseCode.data_not_exist.getMsg());
        }
        if(parentId==0l) {
        	parentId=sysOrganization.getParentId();
        }
        if (sysOrganization.getIsFinal() == 2) {
            return Result.error(ResponseCode.can_not_edit.getMsg());
        }
        if (sysOrganization.getId() == parentId) {
            return Result.error("上级机构不能选择自己,请选择其他组织机构!");
        }
//        boolean isExistFullNameExcludeId = sysOrganizationService.isExistFullNameExcludeId(id, fullName);
        if(!StringUtils.isEmpty(name)&&!name.equals(sysOrganization.getName())) {	// 更改部门名称
        	boolean isExistOrgName = sysOrganizationService.isExistOrgName(name,parentId);
        	if (isExistOrgName) {
        		return Result.error(ResponseCode.name_already_exist.getMsg());
        	}
        }
        SysOrganization organization = new SysOrganization();
        organization.setId(id);
        organization.setFullName(fullName);
        organization.setName(name);
        organization.setDescription(description);
        organization.setParentId(parentId);
//        sysOrganizationService.updateOrganization(organization);
        sysOrganizationService.updateOrganChangeOrgCode(organization);
        return Result.success();
    }

    /**
     * 查询机构列表，只查询相关的机构,  设备管理页面时，当前部门下需要加载个人设备
     * 
     * @param page 起始页码
     * @param row  分页大小
     * @param id   userId 查询用户所在的机构
     * @param queryType 查询类型 0，查询机构，1，查询个人然后转换成机构
     * @return
     */
    @ApiOperation(value = "查询机构列表", httpMethod = "GET", produces = "application/json", response = PageInfo.class)
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public PageInfo list(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "row", defaultValue = "15") int row,
                           @RequestParam(value = "id", defaultValue = "1") long userId,
                           @RequestParam(value="queryType",defaultValue="0") int queryType,
                           @RequestParam(value="isDisplaySuper",defaultValue="0",required=false) int isDisplaySuper) {
    	System.out.println("organization:list--"+page+"--"+row+"---"+userId);
//    	long sysOrganizationId=sysRoleOrganizationService.selectOrganizationIdById(id);
    	SysUserInOrg sysUserInOrg = mSysUserInOrgMapper.selectByUserId(userId); 
    	Long orgId=sysUserInOrg.getSysOrgId();
    	if(orgId==1l) {				//  super账户
    		List<SysOrganization> listSysOrg = mSysOrganizationMapper.selectChildren(sysUserInOrg.getSysOrgId());  // 查询该用户下的自购
    		
    		if(listSysOrg.size()<=0) {
    			listSysOrg=mSysOrganizationMapper.selectChildren(0l);
    		}else if(isDisplaySuper==1) {
    			listSysOrg=mSysOrganizationMapper.selectChildren(0l);
    		}else {
    			return sysOrganizationService.selectPageListSimple(page, row,userId);
    		}
    		List<Long> listOrgId=new ArrayList<Long>();
    		for(SysOrganization sysOrg:listSysOrg) {
    			listOrgId.add(sysOrg.getId());
    		}
    		mSysOrganizationServiceImpl.setQueryType(queryType);
    		PageInfo pageInfo = sysOrganizationService.selectPageList(page, row, listOrgId);
    		return pageInfo;
    	}else {
    		return sysOrganizationService.selectPageListSimple(page, row,userId);
    	}
    }  
    
    @ApiOperation(value="查询该机构下的用户",httpMethod="POST",produces="application/json",response=PageInfo.class)
    @ResponseBody
    @RequestMapping(value="listOrgUser",method=RequestMethod.POST)
    public PageInfo listOrgUser(@RequestParam(value="listOrgIds",defaultValue="[]")String orgIds,
    							@RequestParam(value="userId",defaultValue="0",required=false)Long userId) {
    	if(!mobileHasPermission(userId, PermissionCode.CALLLOG_DELETE.pName)) {
    		return new PageInfo(PermissionCode.CALLLOG_DELETE.pCode, PermissionCode.CALLLOG_DELETE.pMsg);
    	}
    	return sysOrganizationService.selectOrgUser(orgIds);   
    }
    
    
    
    
    
  
    
    @ApiOperation(value="查询机构持有的权限",httpMethod="GET",produces="application/json",response=PageInfo.class)
    @ResponseBody
    @RequestMapping(value="listUserOrg",method=RequestMethod.GET)
    public PageInfo listUserOrg(@RequestParam(value="orgIds",required=true,defaultValue="10")String orgIds,
    							@RequestParam(value="userId",required=true)String userId) {
    	System.out.println("listOrgPermission-->"+orgIds+"userId"+userId);
    	List<SysUserOrganization> listUserOrg = sysOrganizationService.selectUserOrg(orgIds,Long.valueOf(userId));
    	
    	return new PageInfo(listUserOrg.size(), listUserOrg);
    }
    
    
    
    /**
     * 查询机构列表，只查询相关的机构,  设备管理页面时，当前部门下需要加载个人设备
     *
     * @param page 起始页码
     * @param row  分页大小
     * @param id   顶级id  sys_organization   系统机构表的id
     * @return
     */
    @ApiOperation(value = "查询已绑定通讯录的机构", httpMethod = "GET", produces = "application/json", response = PageInfo.class)
//    @RequiresPermissions("organization:list")
    @ResponseBody
    @RequestMapping(value = "listAuthContact", method = RequestMethod.GET) 
    public PageInfo listAuthContact(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "row", defaultValue = "15") int row,
                           @RequestParam(value = "id") long id,
                           @RequestParam(value="queryType",defaultValue="0") int queryType) {
    	System.out.println("organization:list--"+page+"--"+row+"---"+id);
//    	long sysOrganizationId=sysRoleOrganizationService.selectOrganizationIdById(id);
    	mSysOrganizationServiceImpl.setQueryType(queryType);
        PageInfo pageInfo = sysOrganizationService.selectPage(page, row, id);
        return pageInfo;
    }
    
    
    
    /**
     * 获得机构下的人员，先获得该机构下的所有机构，然后获得该机构下的人员，
     * 
     * @param id   机构顶级id    
     * @return
     */
    @ResponseBody
    @RequestMapping(value="listOrgUnderPerson",method=RequestMethod.GET)
    public PageInfo listOrganizationUnderPerson(@RequestParam(value="id") long id) {
    	System.out.println("listOrganizationUnderPerson-->"+id);
    	return  sysRoleOrganizationService.selectOrganizationUnderPerson(id);
    }
    
    
    /**
     * 
     * @return
     */
    @ApiOperation(value="Client 查询机构列表",httpMethod="GET",produces="application/json",response=PageInfo.class)
    @ResponseBody
    @RequestMapping(value="listC",method=RequestMethod.GET)
    public PageInfo listC(@RequestParam(value="id",defaultValue="1") long id) {
    	sysOrganizationService.selectPageC(id);
    	return new PageInfo(0, 0);
    }

}
