package com.hunt.controller;

import com.google.gson.Gson;
import com.hunt.system.security.geetest.GeetestLib;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.hunt.dao.SysDeviceMapper;
import com.hunt.dao.SysPermissionMapper;
import com.hunt.dao.SysUserRoleMapper;
import com.hunt.model.entity.SysPermission;
import com.hunt.model.entity.SysUser;
import com.hunt.service.SystemService;
import com.hunt.util.AesEncryptUtils;
import com.hunt.util.DateUtil;
import com.hunt.util.ResponseCode;
import com.hunt.util.Result;
import com.hunt.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础controller,方便统一异常处理
 *
 * @Author: ouyangan
 * @Date : 2016/10/8
 */


public class BaseController {
    public static final Logger l = LoggerFactory.getLogger(BaseController.class);
    @Autowired
    private SystemService systemService;
	@Autowired
	SysUserRoleMapper mSysUserRoleMapper;
	@Autowired
	SysPermissionMapper mSysPermissionMapper;
	
	@Autowired
	SysDeviceMapper mSysDeviceMapper;
	
	
	
    /** 
     * 极限验证码二次验证
     *
     * @param request
     * @return
     * @throws Exception
     */
    public boolean verifyCaptcha(HttpServletRequest request) throws Exception {
        l.debug("begin verifyCaptcha");
        int verifyResult = 0;
        GeetestLib gtSdk = new GeetestLib(systemService.selectDataItemByKey("geetest_id", 1L), systemService.selectDataItemByKey("geetest_key", 1L));
        l.debug(gtSdk.getCaptchaId());
        l.debug(gtSdk.getPrivateKey());
        String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
        String validate = request.getParameter(GeetestLib.fn_geetest_validate);
        String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);
        l.debug("challenge: {} ,validate: {} ,seccode: {}", challenge, validate, seccode);
        int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);
        l.debug("极限验证服务器状态 : {}", gt_server_status_code);
        if (gt_server_status_code == 1) {
            verifyResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode);
        } else {
            verifyResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
        }
        l.debug("极限验证结果 : {}", verifyResult);
        return verifyResult == 1;
    }

    //根据请求类型,响应不同类型
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException, ServletException {
        l.error("exception occur : \n {}", StringUtil.exceptionDetail(exception));
        if (request.getHeader("Accept").contains("application/json")) {
            l.debug("qingqiu");
            Result result = Result.error();
            if (exception instanceof IncorrectCredentialsException) {
                result = Result.instance(ResponseCode.password_incorrect.getCode(), ResponseCode.password_incorrect.getMsg());
                //账号不存在
            } else if (exception instanceof UnknownAccountException) {
                result = Result.instance(ResponseCode.unknown_account.getCode(), ResponseCode.unknown_account.getMsg());
                //未授权
            } else if (exception instanceof UnauthorizedException) {
                result = Result.instance(ResponseCode.unauthorized.getCode(), ResponseCode.unauthorized.getMsg());
                //未登录
            } else if (exception instanceof UnauthenticatedException) {
                result = Result.instance(ResponseCode.unauthenticated.getCode(), ResponseCode.unauthenticated.getMsg());
                //缺少参数
            } else if (exception instanceof MissingServletRequestParameterException) {
                result = Result.instance(ResponseCode.missing_parameter.getCode(), ResponseCode.missing_parameter.getMsg());
                //参数格式错误
            } else if ((exception instanceof MethodArgumentTypeMismatchException)) {
                result = Result.instance(ResponseCode.param_format_error.getCode(), ResponseCode.param_format_error.getMsg());
                //ip限制
            } else if (exception.getCause().getMessage().contains("system.exception.ForbiddenIpException")) {
                result = Result.instance(ResponseCode.forbidden_ip.getCode(), ResponseCode.forbidden_ip.getMsg());
                //其他错误
            }
            //调试时输出异常日志
            if (systemService.selectDataItemByKey("error_detail", 2L).equals("true")) {
                result.setData(StringUtil.exceptionDetail(exception));
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().append(new Gson().toJson(result));
            response.getWriter().flush();
            response.getWriter().close();
        } else {
            String basePath = systemService.selectDataItemByKey("basePath", 4L);
            String url = "/error/internalError";

            if (exception instanceof UnauthorizedException) {
                url = "/error/unAuthorization";
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.sendRedirect(basePath + url);
        }
    }
	/**
	 * 移动端判断用户是否有权限
	 * @param userId
	 * @param permission
	 * @return
	 */
	public boolean mobileHasPermission(Long userId,String permission) {
		//  查询用户角色 
		List<Long> listPermissionId = mSysUserRoleMapper.selectPerIdByUserId(userId);
		SysPermission sysPermission = mSysPermissionMapper.selectByCode(permission);
		return listPermissionId.contains(sysPermission.getId());
		
	}  
	
	
	public Map<String,String> decodParam(String args,String sign) throws Exception {
		Map<String,String> mapArgs=new HashMap<>();
		if(!StringUtils.isEmpty(args)) {  // 加密数据
			sign=sign.length()==0?AesEncryptUtils.KEY:sign;
			args=AesEncryptUtils.decrypt(args,sign);
			String[] split = args.split("&");
			for(String strSp:split) {
				String[] split2 = strSp.split("="); 
				if(split2.length>1) {
					if(isEmpty(split2[1])) {
						mapArgs.put(split2[0],"");
					}else {
						mapArgs.put(split2[0], split2[1]);
					}
				}else {
					mapArgs.put(split2[0], "");
				}
			}
		}
		return mapArgs;
	}
	
	public boolean isEmpty(String str) {
		return str==null||str.length()==0||str.equals("''")||str.equals("null")||str.equals("");
	}
	/**
	 * 更新心跳 
	 * @param request
	 * @param deviceSerial
	 * @return
	 */
	public Long updateHeart(HttpServletRequest request,String deviceSerial) {
		String ip = request.getRemoteAddr();
		return mSysDeviceMapper.updateDeviceTimeById(deviceSerial,ip);
	}
	
	/**
	 * 查询用户权限码
	 * @param userId
	 * @return
	 */
	public List<String> selectPerCodeByUserId(Long userId) {
		 return mSysUserRoleMapper.selectPerCodeByUserId(userId);
	}
	
	/**
	 * 创建一个 类似 webApp\loginName_userId\YYYYMM\dd\HHmmssSS.fileType 的文件夹
	 * @param fileTop  文件的上级目录   
	 * @param request
	 * @param fileType 文件类型
	 * @param generalRootPath  配置文件中的自定义文件根目录
	 * @return
	 */
	public File createFile(String fileTop, HttpServletRequest request, String fileType,String generalRootPath) {
		//  创建文件目录及路劲(创建文件目录及路径)
		String path=request.getServletContext().getRealPath("/");	//  http 项目目录的根路径
		Long currTime=System.currentTimeMillis();
		String createFilePath = DateUtil.createFilePath(currTime);
		String realPath=fileTop+createFilePath;
		// 文件夹的结构 登录名称_userId/    
		String realPathParent=null;
		if(!StringUtils.isEmpty(generalRootPath)) {
			realPathParent=generalRootPath+File.separator+realPath;
		} else {
			realPathParent=new File(path).getParent()+File.separator+realPath;
		}
		
		File file = new File(realPathParent);
		if(!file.exists()) {
			boolean mkdirs = file.mkdirs();			//   可能文件路径设置错误要重新配置
			if(!mkdirs) {
				return null;
			}
		}
		String timeFileName = DateUtil.getUniqueTime(currTime);
		String sdFileName=timeFileName+"."+fileType;
		File absoFile = new File(realPathParent,sdFileName);
		System.out.println("文件路径为-->"+absoFile.getAbsolutePath());
		return absoFile;
	}
	
	/**
	 * 密码验证
	 * @param sysUser
	 * @param password
	 * @return
	 */
	public Boolean checkPassWord(SysUser sysUser,String password) {
		String passwordSalt = sysUser.getPasswordSalt();
		String userDbPassWord = sysUser.getPassword();
		String createPassword = StringUtil.createPassword(password, passwordSalt, 2);
		if(!createPassword.equals(userDbPassWord)) { // 密码不正确
			return false;
		}	
		return true;
	}
}
