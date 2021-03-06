package com.hunt.util;

/**
 * @Author ouyangan
 * @Date 2016/10/8/13:41
 * @Description
 */
public enum ResponseCode {
    success(10000, "操作成功"),
    error(20000, "服务器错误"),
    unknown_account(20001, "账户不存在,或密码不存在"),
    forbidden_account(20002, "账户已禁用"),
    password_incorrect(30015, "用户名不存在或密码错误"),
    verify_captcha_error(20004, "验证码错误,请重新刷新并滑动验证码!"),
    unauthorized(20005, "无操作权限"),
    can_not_edit(20006, "该条记录无法编辑"), 
    unauthenticated(20007, "未登录"),
    forbidden_ip(20008, "非法请求"), 
    not_found_url(20009, "url不存在"),
    param_format_error(30001, "参数格式错误"),
    missing_parameter(30002, "缺少参数"),
    name_already_exist(30003, "该名称已存在"),
    data_not_exist(30004, "该记录不存在"),
    login_name_already_exist(30005, "该登录名已存在"),
    code_already_exist(30006, "该编码已存在"),
    fullname_already_exist(30007, "该全称已存在"),
    
	
	encode_fail(30008,"编码格式错误，支持UTF-8"),
	missing_file_version(30009,"文件缺少版本号,请重新选择文件"),
	file_sych_err(30010,"通讯录同步密码错误,请输入至少4位字符,字母,数字,下划线"),
	file_exist_err(30011,"该文件版本已存在同步密码不匹配，请检查"),
	file_exist_code(30012,"该文件版本应大于之前的版本或者版本号不符合yyyyMMdd，请检查"),
	no_permission(30013,"无修改权限"),
	file_not_exist(30014,"文件不存在，请重新上传"),
	    
	name_no_exist(30015,"用户名不存在或密码错误"),
	user_no_in_org(30016,"用户不在部门中,请把用户添加到部门"),
	device_not_exist(30017,"设备不存在"),
	device_not_bind(30018,"设备没绑定用户"),
	
	csv_password_incorrect(30019,"密码错误"),
	
	decode_err(30020,"参数解密错误"),
	
	task_exist(30021,"任务已存在或没有更新权限"),
	task_num_exist(30022,"通话号码重复,列名不符合规则,请检查文件"),
	task_num_exist2(30023,"通话号码重复,请检查文件"),
	
	task_num_0(30024,"没有未完成的任务"),
	
	file_config_fail(30025,"文件路径配置错误，请检查");
	
    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResponseCode{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
