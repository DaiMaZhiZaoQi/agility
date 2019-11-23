package com.hunt.controller;

import com.github.pagehelper.StringUtil;
import com.hunt.controller.BaseController;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.entity.SysOrganization;
import com.hunt.service.SysOrganizationService;
import com.hunt.service.SysRoleOrganizationService;
import com.hunt.service.impl.SysOrganizationServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    private SysOrganizationServiceImpl mSysOrganizationServiceImpl;
    
    @ApiOperation(value = "跳转至组织机构", httpMethod = "GET", produces = "text/html")
    @RequiresPermissions("organization:list")
    @RequestMapping(value = "organization", method = RequestMethod.GET)
    public String organization() {
    	System.out.println("list--organization");
    	
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
    @RequiresPermissions("organization:insert")
    @ResponseBody
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Result insert(@RequestParam String name,
                         @RequestParam String description,
                         @RequestParam String fullName,
                         @RequestParam long parentId,
                         @RequestParam String parentOrgId,
                         @RequestParam(defaultValue = "1") int isFinal) {
        boolean isExistFullName = sysOrganizationService.isExistFullName(fullName);
        if (isExistFullName) {
            return Result.error(ResponseCode.fullname_already_exist.getMsg());
        }
        SysOrganization organization = new SysOrganization();
        organization.setFullName(fullName);
        organization.setName(name);
        organization.setDescription(description);
        organization.setParentId(parentId);
        organization.setIsFinal(isFinal);
        log.debug("新增机构"+parentOrgId+"---"+parentId); 
        String orgCode = getOrgCode(parentId,parentOrgId);		//  插入机构码 
        organization.setOrgCode(orgCode);
        
        long i = sysOrganizationService.insertOrganization(organization);
        return Result.success();
    }
    
    /**
     * 设置机构码，根据上级机构码得到该级别的机构码，如果大机构分支较多，则需要调整大机构的机构码，依次累加
     * 如果该级的机构较多则依次递增
     * @param parentId			上一级id
     * @param orgId				上一级机构码    TODO 机构码为3位数
     * @return
     */
    private String getOrgCode(long parentId,String parentOrgCode) {
    	String orgCode = sysOrganizationService.selectOrgCodeByParentId(parentId);
    	if(parentOrgCode.equals("000")) {		//  大机构
    		if(StringUtil.isNotEmpty(orgCode)) {
    			int parseInt = Integer.parseInt(orgCode);
    			parseInt++;
    			log.debug("getOrgCode大集团--->"+""+parseInt);
    			return parseInt+"";
    		}else {
    			return "100";
    		}
    	}else {							//  小机构
    		if(StringUtil.isNotEmpty(orgCode)) {
    			int parseInt = Integer.parseInt(orgCode);
    			parseInt++;
    			log.debug("getOrgCode集团子公司--->"+""+parseInt);
    			return parseInt+"";
    		}else {
    			return parentOrgCode+"000";
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
    @RequiresPermissions("organization:delete")
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
     * @param id          id
     * @param name        名称
     * @param description 描述
     * @param fullName    全称
     * @param parentId    父级id
     * @return
     */
    @ApiOperation(value = "更新机构", httpMethod = "POST", produces = "application/json", response = Result.class)
    @RequiresPermissions("organization:update")
    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestParam long id,
                         @RequestParam String name,
                         @RequestParam String fullName,
                         @RequestParam String description,
                         @RequestParam long parentId) {
        SysOrganization sysOrganization = sysOrganizationService.selectOrganization(id);
        if (sysOrganization == null) {
            return Result.error(ResponseCode.data_not_exist.getMsg());
        }
        if (sysOrganization.getIsFinal() == 2) {
            return Result.error(ResponseCode.can_not_edit.getMsg());
        }
        if (sysOrganization.getId() == parentId) {
            return Result.error("上级机构不能选择自己,请选择其他组织机构!");
        }
        boolean isExistFullNameExcludeId = sysOrganizationService.isExistFullNameExcludeId(id, fullName);
        if (isExistFullNameExcludeId) {
            return Result.error(ResponseCode.fullname_already_exist.getMsg());
        }
        SysOrganization organization = new SysOrganization();
        organization.setId(id);
        organization.setFullName(fullName);
        organization.setName(name);
        organization.setDescription(description);
        organization.setParentId(parentId);
        sysOrganizationService.updateOrganization(organization);
        return Result.success();
    }

    /**
     * 查询机构列表，只查询相关的机构,  设备管理页面时，当前部门下需要加载个人设备
     *
     * @param page 起始页码
     * @param row  分页大小
     * @param id   顶级id  sys_organization   系统机构表的id
     * @return
     */
    @ApiOperation(value = "查询机构列表", httpMethod = "GET", produces = "application/json", response = PageInfo.class)
    @RequiresPermissions("organization:list")
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public PageInfo list(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "row", defaultValue = "15") int row,
                           @RequestParam(value = "id", defaultValue = "1") long id,
                           @RequestParam(value="queryType",defaultValue="0") int queryType) {
    	System.out.println("organization:list--"+page+"--"+row+"---"+id);
    	long sysOrganizationId=sysRoleOrganizationService.selectOrganizationIdById(id);
//    	mSysOrganizationServiceImpl.setQueryType(queryType);
    	mSysOrganizationServiceImpl.setQueryType(0);
        PageInfo pageInfo = sysOrganizationService.selectPage(page, row, sysOrganizationId==0?1:sysOrganizationId);;
        return pageInfo;
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
