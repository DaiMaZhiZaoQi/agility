package com.hunt.controller;

import com.hunt.model.dto.LoginInfo;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.dto.SysUserOrgDto;
import com.hunt.model.entity.SysOrganization;
import com.hunt.model.entity.SysUser;
import com.hunt.model.entity.SysUserInOrg;
import com.hunt.model.entity.SysUserOrganization;
import com.hunt.service.SysUserService;
import com.hunt.service.SystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.hunt.util.ResponseCode;
import com.hunt.util.Result;
import com.hunt.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @Author ouyangan
 * @Date 2016/10/8/13:37
 * @Description
 */
@Api(value = "用户管理模块")
@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SystemService systemService;
    
    
    @ApiOperation(value = "跳转至用户管理模块", httpMethod = "GET", produces = "text/html")
//    @RequiresPermissions("user:list")
    @RequiresPermissions("user:manage")
    @RequestMapping(value = "user", method = RequestMethod.GET)
    public String user() {
        return "system/user";
    }

    /**
     * 新增用户
     * TODO createBy
     * @param loginName     登录名
     * @param zhName        中文名
     * @param enName        英文名
     * @param sex           性别
     * @param birth         生日
     * @param email         邮箱
     * @param phone         电话
     * @param address       地址
     * @param password      密码
     * @param isFinal       是否可修改
     * @param roleIds       角色ids
     * @param orgIds		所在机构
     * @param orgIdPermiss  机构权限
     * @return
     */
    @ApiOperation(value = "新增用户", httpMethod = "POST", produces = "application/json", response = Result.class)
    @RequiresPermissions("user:manage")
    @ResponseBody
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Result insert(@RequestParam String loginName,
                         @RequestParam String zhName,
                         @RequestParam(defaultValue = "", required = false) String enName,
                         @RequestParam int sex,
                         @RequestParam(defaultValue = "", required = false) String birth,
                         @RequestParam(defaultValue = "", required = false) String email,
                         @RequestParam(defaultValue = "", required = false) String phone,
                         @RequestParam(defaultValue = "", required = false) String address,
                         @RequestParam String password,
                         @RequestParam(defaultValue = "1") int isFinal,
                         @RequestParam(defaultValue="",required=false) String roleIds,   
                         @RequestParam(value="sysUserInOrgStr",required=true,defaultValue="") String sysUserInOrgStr,
                         @RequestParam(value="listSysOrgPerStr",required=true,defaultValue="")String listSysOrgPerStr
    					) {	 // 通过角色查询到权限
        boolean isExistLoginName = sysUserService.isExistLoginName(loginName);
        if (isExistLoginName) {
            return Result.error(ResponseCode.name_already_exist.getMsg());
        }   
        if ((!StringUtils.hasText(password)) && password.length() < 6) {
            return Result.error("请设置密码长度大于等于6");
        }
     
        SysOrganization sysUserInOrg=JsonUtils.readValue(sysUserInOrgStr, SysOrganization.class);
        System.out.println("listSysOrgPerStr--->"+listSysOrgPerStr);
        
        System.out.println("sysUserInOrg--->"+sysUserInOrg.getFullName());
        List<SysOrganization> listSysOrgPer=Arrays.asList(JsonUtils.readValue(listSysOrgPerStr, SysOrganization[].class));
        SysUser user = new SysUser();
        user.setLoginName(loginName);
        user.setZhName(zhName);
        user.setEnName(enName);
        user.setSex(sex);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setBirth(birth);
        user.setIsFinal(isFinal);
        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        user.setPasswordSalt(salt);
        user.setPassword(StringUtil.createPassword(password, salt, 2));
//        long id = sysUserService.insertUser(user, jobIds, permissionIds);
        long id = sysUserService.insertUserOrgRole(user, roleIds, sysUserInOrg,listSysOrgPer);
        System.out.println("新增用户--userId"+user.getId()); 
        systemService.clearAuthorizationInfoCacheByUserId(user.getId());
//        sysUserService.insertUserPermission(user);
        return Result.success(id);
    }
    
  
    
    
    /**
     * 新增用户给每个用户赋予机构代码
     */
    public void insertN() {
    	
    }

    @ApiOperation(value = "删除用户", httpMethod = "GET", produces = "application/json", response = Result.class)
    @RequiresPermissions("user:manage")
    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public Result delete(@RequestParam long id) {
        SysUser user = sysUserService.selectById(id);
        if (user == null) {
            return Result.error(ResponseCode.data_not_exist.getMsg());
        }
        if (user.getIsFinal() == 2) {
            return Result.error(ResponseCode.can_not_edit.getMsg());
        }
        sysUserService.deleteById(user);
        systemService.forceLogout(id);
        return Result.success();
    }

    /**
     * 更新用户
     *
     * @param id			用户id
     * @param loginName     登录名
     * @param zhName        中文名
     * @param enName        英文名
     * @param sex           性别
     * @param birth         生日
     * @param email         邮箱
     * @param phone         电话
     * @param address       地址
     * @param password      密码
     * @param jobIds        职位ids
     * @param permissionIds 权限ids
     * @return
     */
    @ApiOperation(value = "更新用户", httpMethod = "POST", produces = "application/json", response = Result.class)
    @RequiresPermissions("user:manage")
    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestParam long id,
                         @RequestParam String loginName,
                         @RequestParam String zhName,
                         @RequestParam String enName,
                         @RequestParam int sex,
                         @RequestParam(defaultValue = "", required = false) String birth,
                         @RequestParam(defaultValue = "", required = false) String email,
                         @RequestParam(defaultValue = "", required = false) String phone,
                         @RequestParam(defaultValue = "", required = false) String address,
                         @RequestParam(defaultValue="",required=false) String roleIds,   
                         @RequestParam(value="sysUserInOrgStr",required=true,defaultValue="") String sysUserInOrgStr,
                         @RequestParam(value="listSysOrgPerStr",required=true,defaultValue="")String listSysOrgPerStr
                         ) {
        boolean isExistLoginNameExlcudeId = sysUserService.isExistLoginNameExcludeId(id, loginName);
        if (isExistLoginNameExlcudeId) {
            return Result.error(ResponseCode.name_already_exist.getMsg());
        }
        
        SysUser user = sysUserService.selectById(id);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        String loginName2 = loginInfo.getLoginName(); 
        if (user.getIsFinal() == 2&&!"super".equals(loginName2)) { 
            return Result.error(ResponseCode.can_not_edit.getMsg());
        }
        user.setLoginName(loginName); 
        user.setZhName(zhName);
        user.setEnName(enName);
        user.setSex(sex);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);  
        user.setBirth(birth);
        
        if("super".equals(loginName)) {		//super 账户
        	sysUserService.updateSuperUser(user, roleIds);
        }else {
        	SysOrganization sysUserInOrg=JsonUtils.readValue(sysUserInOrgStr, SysOrganization.class);
        	List<SysOrganization> listSysOrgPer=Arrays.asList(JsonUtils.readValue(listSysOrgPerStr, SysOrganization[].class));
//        sysUserService.updateUser(user, jobIds, permissionIds);
        	sysUserService.updateUserDetail(user, roleIds,sysUserInOrg,listSysOrgPer);
        }
        
   
        systemService.clearAuthorizationInfoCacheByUserId(id);
        return Result.success();
    }

    /**
     * 查询用户列表
     *
     * @param page      起始页码
     * @param rows      分页大小
     * @param sort      排序字段
     * @param order     排序规则
     * @param loginName 登录名
     * @param zhName    中文名
     * @param email     邮箱
     * @param phone     电话
     * @param address   地址
     * @return
     */
    @ApiOperation(value = "查询用户列表", httpMethod = "GET", produces = "application/json", response = PageInfo.class)
    @RequiresPermissions("user:manage")
    @ResponseBody 
    @RequestMapping(value = "list", method = RequestMethod.GET) 
    public PageInfo list(@RequestParam(defaultValue = "1") int page,   
                           @RequestParam(defaultValue = "30") int rows,
                           @RequestParam(defaultValue = "zhName") String sort,
                           @RequestParam(defaultValue = "asc") String order,
                           @RequestParam(defaultValue = "", required = false) String loginName,
                           @RequestParam(defaultValue = "", required = false) String zhName,
                           @RequestParam(defaultValue = "", required = false) String email,
                           @RequestParam(defaultValue = "", required = false) String phone,
                           @RequestParam(defaultValue = "", required = false) String address,
                           @RequestParam(value="userId",defaultValue="0",required=false) long userId) {
    	if(userId==0l) {
    		return new PageInfo(ResponseCode.missing_parameter.getCode(), "参数错误,请重新登录");
    	}
    	if(userId==1l) {
    		 return sysUserService.selectPage(page, rows, StringUtil.camelToUnderline(sort), order, loginName, zhName, email, phone, address);
    	}
    	//  TODO  查询该机构下的所有用户
        PageInfo pageInfo = sysUserService.selectPage(page, rows, StringUtil.camelToUnderline(sort), order, loginName, zhName, email, phone, address,userId);
        return pageInfo;
    }
    
    @ApiOperation(value="查询该用户下的用户",httpMethod="GET",produces="application/json",response=PageInfo.class)
    @ResponseBody
    @RequestMapping(value="listUserByOrg",method=RequestMethod.GET)
    public PageInfo listUserByUserOrg(@RequestParam("orgId") Long orgId) {
    	
    	return sysUserService.selectUserByOrgId(orgId);
    }
    
    /**
     * 更新密码
     *
     * @param id                id
     * @param repeatNewPassword
     * @param newPassword       新密码
     * @return
     */
    @ApiOperation(value = "更新密码", httpMethod = "POST", produces = "application/json", response = Result.class)
    @RequiresPermissions("user:manage")
    @ResponseBody
    @RequestMapping(value = "updatePassword", method = RequestMethod.POST)
    public Result updatePassword(@RequestParam long id,
                                 @RequestParam String newPassword,
                                 @RequestParam String repeatNewPassword) {
        if ((!StringUtils.hasText(newPassword)) && newPassword.length() < 6) {
            return Result.error("请设置密码长度大于等于6");
        }
        if (!newPassword.equals(repeatNewPassword)) {
            return Result.error("两次输入的密码不一致!");
        }
        SysUser user = sysUserService.selectById(id);
        if (user.getIsFinal() == 2) {
            return Result.error(ResponseCode.can_not_edit.getMsg());
        }
        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        user.setPasswordSalt(salt);
        user.setPassword(StringUtil.createPassword(newPassword, salt, 2));
        sysUserService.updateUser(user);
        return Result.success();
    }

    /**
     * 禁用账户
     *
     * @param id id
     * @return
     */

    @ApiOperation(value = "禁用账户", httpMethod = "GET", produces = "application/json", response = Result.class)
    @RequiresPermissions("user:manage")
    @ResponseBody
    @RequestMapping(value = "forbiddenUser", method = RequestMethod.GET)
    public Result forbiddenUser(@RequestParam long id) {
        SysUser sysUser = sysUserService.selectById(id);
        if (sysUser.getIsFinal() == 2) {
            return Result.error(ResponseCode.can_not_edit.getMsg());
        }
        sysUser.setStatus(3);
        sysUserService.updateUser(sysUser);
        systemService.forceLogout(sysUser.getId());
        return Result.success();
    }

    /**
     * 启用账户
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "启用账户", httpMethod = "GET", produces = "application/json", response = Result.class)
    @RequiresPermissions("user:manage")
    @ResponseBody
    @RequestMapping(value = "enableUser", method = RequestMethod.GET)
    public Result enableUser(@RequestParam long id) {
        SysUser sysUser = sysUserService.selectById(id);
        if (sysUser.getIsFinal() == 2) {
            return Result.error(ResponseCode.can_not_edit.getMsg());
        }
        sysUser.setStatus(1);
        sysUserService.updateUser(sysUser);
        systemService.clearAuthorizationInfoCacheByUserId(sysUser.getId());
        return Result.success();
    }

}
