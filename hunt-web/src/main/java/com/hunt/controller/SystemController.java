package com.hunt.controller;

import com.hunt.system.security.geetest.GeetestConfig;
import com.hunt.system.security.geetest.GeetestLib;
import com.hunt.dao.SysLoginStatusMapper;
import com.hunt.model.dto.LoginInfo;
import com.hunt.model.dto.PageInfo;
import com.hunt.model.entity.SysDataGroup;
import com.hunt.model.entity.SysDataItem;
import com.hunt.model.entity.SysIpForbidden;
import com.hunt.model.entity.SysLoginStatus;
import com.hunt.model.entity.SysUser;
import com.hunt.service.SysUserService;
import com.hunt.service.SystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.Subject;
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
import com.hunt.util.StringUtil;
//import com.sun.tools.classfile.InnerClasses_attribute.Info;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 系统功能模块
 */
@Api(value = "系统功能模块")
@Controller
@RequestMapping("system")
public class SystemController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(SystemController.class);
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private SysLoginStatusMapper mSysLoginStatusMapper;
    /**
     * 引导页
     *
     * @return
     */
    @ApiOperation(value = "跳转至引导页", httpMethod = "GET", produces = "text/html")
    @RequestMapping(value = "welcome", method = RequestMethod.GET)
    public String index() {
        return "system/index";
    }

    /**
     * 登录
     *
     * @param loginName 登录名
     * @param password  密码
     * @param platform  终端类型
     * @param loginModule 登录模式，0 浏览器没网络时，使用局域网 无需要证码登录
     * @return
     */
    @ApiOperation(value = "登录", httpMethod = "POST", produces = "application/json", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result login(@RequestParam String loginName,
                        @RequestParam String password,
                        @RequestParam int platform,
                        @RequestParam(value="loginModule",required=false,defaultValue="0") Integer loginModule,
                        HttpServletRequest request) throws Exception {
        //极限验证二次服务验证
        if (loginModule==1&&!verifyCaptcha(request)) { 
            return Result.instance(ResponseCode.verify_captcha_error.getCode(), ResponseCode.verify_captcha_error.getMsg());
        }
        SysUser user = sysUserService.selectByLoginName(loginName);		//  查找数据库用户名 
        if (user == null) {
//        	Result result = sysUserService.init(loginName,password);
//        	int code = result.getCode();
//        	if(code==ResponseCode.success.getCode()) {
//        		return Result.instance(ResponseCode.success.getCode(), "初始化成功请重新登录");
//        	}
            return Result.instance(ResponseCode.unknown_account.getCode(), ResponseCode.unknown_account.getMsg());
        }
        if (user.getStatus() == 3) {									//  账号已禁用
            return Result.instance(ResponseCode.forbidden_account.getCode(), ResponseCode.forbidden_account.getMsg());
        }
        Subject subject = SecurityUtils.getSubject();				    //  shiro  权限验证  生成session
        Session session = subject.getSession();
        SecurityManager securityManager = SecurityUtils.getSecurityManager();
        SysLoginStatus sysLoginStatus = mSysLoginStatusMapper.selectByLoginName(loginName);
        if(sysLoginStatus!=null) {
        	try {
        		Session session1 = securityManager.getSession(new DefaultSessionKey(sysLoginStatus.getSessionId()));
        		session=session1;
        	}catch (Exception e) {
        		log.info("账号异常登录-->"+e.getMessage());
			}
        }
        
        subject.login(new UsernamePasswordToken(loginName, password));	//  获取session id  可能会有异常，还需要根据不同异常来处理，账号不存在
        LoginInfo loginInfo = sysUserService.login(user, session.getId(), platform);
        subject.getSession().setAttribute("loginInfo", loginInfo);
        log.debug("登录成功"+loginInfo); 
        
        return Result.success(loginInfo);
    }
    
    /**
     * 移动端登录  免验证码	
     */
    @ApiOperation(value = "移动端登录", httpMethod = "POST", produces = "application/json", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "loginByMobile", method = RequestMethod.POST)
    public Result loginByMobile(@RequestParam String loginName,
    							@RequestParam String password,
    							@RequestParam int platform,
    							HttpServletRequest request) {
    	   SysUser user = sysUserService.selectByLoginName(loginName);		//  查找数据库用户名 
           if (user == null) {
               return Result.instance(ResponseCode.unknown_account.getCode(), ResponseCode.unknown_account.getMsg());
           }
           if (user.getStatus() == 3) {									//  账号已禁用
               return Result.instance(ResponseCode.forbidden_account.getCode(), ResponseCode.forbidden_account.getMsg());
           }
           Subject subject = SecurityUtils.getSubject();				    //  shiro  权限验证  生成session
           
           Session session=subject.getSession();
           SecurityManager securityManager = SecurityUtils.getSecurityManager();
           SysLoginStatus sysLoginStatus = mSysLoginStatusMapper.selectByLoginName(loginName);
           if(sysLoginStatus!=null) {
        	   try {
        		   Session session1 = securityManager.getSession(new DefaultSessionKey(sysLoginStatus.getSessionId()));
        		   session=session1;
			} catch (Exception e) {
				log.info("exception-->"+e.getMessage()); 
			}
           }
           
           subject.login(new UsernamePasswordToken(loginName, password));	//  获取session id  可能会有异常，还需要根据不同异常来处理，账号不存在
           LoginInfo loginInfo = sysUserService.login(user, session.getId(), platform);
           subject.getSession().setAttribute("loginInfo", loginInfo);
		return Result.success(loginInfo);
    	
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation(value = "退出", httpMethod = "GET", produces = "application/json", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.success();
    }

    /**
     * 极限验证
     * @param request
     * @return
     */
    @ApiOperation(value = "极限验证", httpMethod = "GET", produces = "application/json", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "captcha", method = RequestMethod.GET)
    public String captcha(HttpServletRequest request) {
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key());
        int gtServerStatus = gtSdk.preProcess();
        request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
        return gtSdk.getResponseStr();
    }

    /**
     * 用户状态
     *
     * @return
     */
    @ApiOperation(value = "跳转至用户状态模块", httpMethod = "GET", produces = "text/html")
    @RequiresPermissions("user:manage")
    @RequestMapping(value = "online", method = RequestMethod.GET)
    public String online() {
        return "system/online";
    }

    /**
     * 在线用户列表
     *
     * @param page 起始页码
     * @param rows 分页大小
     * @return
     */
    @ApiOperation(value = "在线用户列表", httpMethod = "GET", produces = "application/json", response = PageInfo.class)
    @RequiresPermissions("user:manage")
    @ResponseBody
    @RequestMapping(value = "online/list", method = RequestMethod.GET)
    public PageInfo onlineList(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "30") int rows) {
        PageInfo pageInfo = systemService.selectLogStatus(page, rows);
        return pageInfo;
    }

    /**
     * 强制用户下线
     *
     * @param userIds 用户ids
     * @return
     */
    @ApiOperation(value = "强制用户下线", httpMethod = "GET", produces = "application/json", response = Result.class)
    @RequiresPermissions("user:manage")
    @ResponseBody
    @RequestMapping(value = "forceLogout", method = RequestMethod.GET)
    public Result forceLogout(@RequestParam String userIds) {
        System.out.println("userIds = [" + userIds + "]");
        String[] ids = userIds.split(",");
        for (String id : ids) {
            systemService.forceLogout(Long.parseLong(id));
        }
        return Result.success();
    }

    /**
     * 日志页面
     *
     * @return
     */
    @ApiOperation(value = "跳转至日志页面", httpMethod = "GET", produces = "text/html")
    @RequiresPermissions("system:manage")
    @RequestMapping(value = "log")
    public String log() {
        return "system/log";
    }

    /**
     * 查询日志列表
     *
     * @param page   起始页码
     * @param rows   分页大小
     * @param sort   排序字段
     * @param order  排序规则
     * @param method 请求执行方法
     * @param url    请求url
     * @param param  请求参数
     * @param result 请求响应内容
     * @return
     */
    @ApiOperation(value = "查询日志列表", httpMethod = "GET", produces = "application/json", response = PageInfo.class)
    @RequiresPermissions("system:manage")
    @ResponseBody
    @RequestMapping(value = "log/list", method = RequestMethod.GET)
    public PageInfo logList(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "30") int rows,
                            @RequestParam(defaultValue = "id") String sort,
                            @RequestParam(defaultValue = "desc") String order,
                            @RequestParam(defaultValue = "", required = false) String method,
                            @RequestParam(defaultValue = "", required = false) String url,
                            @RequestParam(defaultValue = "", required = false) String param,
                            @RequestParam(defaultValue = "", required = false) String result) {
        PageInfo pageInfo = systemService.selectLog(page, rows, StringUtil.camelToUnderline(sort), order, method, url, param, result);
        return pageInfo;
    }

    /**
     * 新增字典组
     *
     * @param name        名称
     * @param description 描述
     * @return
     */
    @ApiOperation(value = "新增字典组", httpMethod = "POST", produces = "application/json", response = Result.class)
    @RequiresPermissions("system:manage")
    @ResponseBody
    @RequestMapping(value = "dataGroup/insert", method = RequestMethod.POST)
    public Result dataGroupInsert(@RequestParam String name,
                                  @RequestParam String description) {
        boolean isExistName = systemService.isExistDataGroupName(name);
        if (isExistName) {
            return Result.error(ResponseCode.name_already_exist.getMsg());
        }
        SysDataGroup sysDataGroup = new SysDataGroup();
        sysDataGroup.setName(name);
        sysDataGroup.setDescription(description);
        sysDataGroup.setParentId(0L);
        sysDataGroup.setIsFinal(2);
        Long id = systemService.insertSysDataGroup(sysDataGroup);
        return Result.success(id);
    }

    /**
     * 字典组列表
     *
     * @return
     */
    @ApiOperation(value = "字典组列表", httpMethod = "GET", produces = "application/json", response = Result.class)
    @RequiresPermissions("system:manage")
    @ResponseBody
    @RequestMapping(value = "dataGroup/list", method = RequestMethod.GET)
    public List<SysDataGroup> dataGroupList() {
        List<SysDataGroup> list = systemService.selectDataGroupList();
        return list;
    }

    @ApiOperation(value = "跳转至字典页面", httpMethod = "GET", produces = "text/html")
    @RequestMapping(value = "data", method = RequestMethod.GET)
    public String data() {
        return "system/data";
    }

    /**
     * 新增数据字典
     *
     * @param key         key
     * @param value       value
     * @param description 描述
     * @param groupId     字典组
     * @return
     */
    @ApiOperation(value = "新增数据字典", httpMethod = "POST", produces = "application/json", response = Result.class)
    @RequiresPermissions("system:manage")
    @ResponseBody
    @RequestMapping(value = "data/insert", method = RequestMethod.POST)
    public Result dataInsert(@RequestParam String key,
                             @RequestParam String value,
                             @RequestParam String description,
                             @RequestParam long groupId) {
        boolean isExistName = systemService.isExistDataItemKeyName(key, groupId);
        if (isExistName) {
            return Result.error(ResponseCode.name_already_exist.getMsg());
        }
        SysDataItem sysDataItem = new SysDataItem();
        sysDataItem.setKeyName(key);
        sysDataItem.setKeyValue(value);
        sysDataItem.setSysDataGroupId(groupId);
        sysDataItem.setDescription(description);
        long id = systemService.insertSysDataItem(sysDataItem);
        return Result.success(id);
    }


    /**
     * 删除字典
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除字典", httpMethod = "GET", produces = "application/json", response = Result.class)
    @RequiresPermissions("system:manage")
    @ResponseBody
    @RequestMapping(value = "data/delete", method = RequestMethod.GET)
    public Result dataDelete(@RequestParam Long id) {
        systemService.deleteDataItemById(id);
        return Result.success();
    }

    /**
     * 更新字典
     *
     * @param id          id
     * @param key         key
     * @param value       value
     * @param description 描述
     * @param groupId     字典组
     * @return
     */
    @ApiOperation(value = "更新字典", httpMethod = "POST", produces = "application/json", response = Result.class)
    @RequiresPermissions("system:manage")
    @ResponseBody
    @RequestMapping(value = "data/update", method = RequestMethod.POST)
    public Result dataUpdate(@RequestParam Long id,
                             @RequestParam String key,
                             @RequestParam String value,
                             @RequestParam String description,
                             @RequestParam long groupId) {
        SysDataItem sysDataItem = systemService.selectDataItemById(id);
        if (sysDataItem == null) {
            return Result.error(ResponseCode.data_not_exist.getMsg());
        }
        if (sysDataItem.getIsFinal() == 2) {
            return Result.error(ResponseCode.can_not_edit.getMsg());
        }
        boolean isExistDataItemNameExcludeId = systemService.isExistDataItemNameExcludeId(id, key, groupId);
        if (isExistDataItemNameExcludeId) {
            return Result.error(ResponseCode.name_already_exist.getMsg());
        }
        sysDataItem.setKeyName(key);
        sysDataItem.setKeyValue(value);
        sysDataItem.setSysDataGroupId(groupId);
        sysDataItem.setDescription(description);
        systemService.updateDateItem(sysDataItem);

        return Result.success();
    }

    /**
     * 查询字典详情
     *
     * @return
     */
    @ApiOperation(value = "查询字典详情", httpMethod = "GET", produces = "application/json", response = Result.class)
    @RequiresPermissions("system:manage")
    @ResponseBody
    @RequestMapping(value = "data/select", method = RequestMethod.GET)
    public Result dataSelect() {
        return Result.success();
    }

    /**
     * 字典列表
     *
     * @param page 起始页码
     * @param rows 分页大小
     * @return
     */
    @ApiOperation(value = "字典列表", httpMethod = "GET", produces = "application/json", response = PageInfo.class)
    @RequiresPermissions("system:manage")
    @ResponseBody
    @RequestMapping(value = "data/list", method = RequestMethod.GET)
    public PageInfo dataList(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "30") int rows) {
        PageInfo pageInfo = systemService.selectDataItemPage(page, rows);
        return pageInfo;
    }

    /**
     * ip页面跳转
     *
     * @return
     */
    @ApiOperation(value = "跳转至ip模块", httpMethod = "GET", produces = "text/html")
    @RequiresPermissions("system:manage")
    @RequestMapping(value = "ip", method = RequestMethod.GET)
    public String ip() {
        return "system/ip";
    }

    /**
     * 插入ip
     *
     * @param ip          ip地址
     * @param expireTime  过期时间
     * @param description 说明
     * @return
     */
    @ApiOperation(value = "插入ip", httpMethod = "POST", produces = "application/json", response = Result.class)
    @RequiresPermissions("system:manage")
    @ResponseBody
    @RequestMapping(value = "ip/insert", method = RequestMethod.POST)
    public Result ipInsert(@RequestParam String ip,
                           @RequestParam String expireTime,
                           @RequestParam String description) throws ParseException {
        boolean isExistIp = systemService.isExistIp(ip);
        if (isExistIp) {
            return Result.error(ResponseCode.name_already_exist.getMsg());
        }
        SysIpForbidden sysIpForbidden = new SysIpForbidden();
        sysIpForbidden.setIp(ip);
        sysIpForbidden.setExpireTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(expireTime));
        sysIpForbidden.setDescription(description);
        Long id = systemService.insertIp(sysIpForbidden);
        // 激活ip拦截
        if(id>0) {
        	systemService.insertIpIntercept();
        }
        return Result.success(id);
    }

    /**
     * 删除ip
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除ip", httpMethod = "GET", produces = "application/json", response = Result.class)
    @RequiresPermissions("system:manage")
    @ResponseBody
    @RequestMapping(value = "ip/delete", method = RequestMethod.GET)
    public Result ipDelete(@RequestParam long id) {
        systemService.deleteIp(id);
        return Result.success();
    }

    /**
     * 更新ip
     *
     * @param id,         id,
     * @param ip          ip地址
     * @param expireTime  过期时间
     * @param description 说明
     * @return
     */
    @ApiOperation(value = "更新ip", httpMethod = "POST", produces = "application/json", response = Result.class)
    @RequiresPermissions("system:manage")
    @ResponseBody
    @RequestMapping(value = "ip/update", method = RequestMethod.POST)
    public Result ipUpdate(@RequestParam long id,
                           @RequestParam String ip,
                           @RequestParam String expireTime,
                           @RequestParam String description) throws ParseException {
        boolean isExistIpExcludeId = systemService.isExistIpExcludeId(ip, id);
        if (isExistIpExcludeId) {
            return Result.error(ResponseCode.name_already_exist.getMsg());
        }
        SysIpForbidden sysIpForbidden = new SysIpForbidden();
        sysIpForbidden.setId(id);
        sysIpForbidden.setIp(ip);
        sysIpForbidden.setExpireTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(expireTime));
        sysIpForbidden.setDescription(description);
        systemService.updateIp(sysIpForbidden);
        systemService.insertIpIntercept();
        return Result.success();
    }

    /**
     * 查询ip列表
     *
     * @param page
     * @param rows
     * @return
     */
    @ApiOperation(value = "查询ip列表", httpMethod = "GET", produces = "application/json", response = PageInfo.class)
    @RequiresPermissions("system:manage")
    @ResponseBody
    @RequestMapping(value = "ip/list", method = RequestMethod.GET)
    public PageInfo ipSelect(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "30") int rows) {
        PageInfo pageInfo = systemService.selectIp(page, rows);
        return pageInfo;
    }

    @ApiOperation(value = "ip拦截开关", httpMethod = "GET", produces = "application/json", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "ip/intercept", method = RequestMethod.GET)
    public Result intercept(@RequestParam boolean open) {
        //启用
        if (open == true) {
            systemService.openIpIntercept();
        }
        //禁用
        if (open == false) {
            systemService.closeIpIntercept();
        }
        return Result.success();
    }

    @ApiOperation(value = "ip拦截开关状态", httpMethod = "GET", produces = "application/json", response = Result.class)
    @ResponseBody
    @RequestMapping(value = "ip/intercept/status")
    public Result interceptStatus() {
        boolean ip_forbidden = systemService.selectIPForbiddenStatus();
        return Result.success(ip_forbidden);
    }

}
