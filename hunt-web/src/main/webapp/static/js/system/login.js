$(document).ready(function () {
    var captcha;
    handlerEmbed = function (captchaObj) {
        captchaObj.appendTo("#embed-captcha");
        captcha = captchaObj;
    };
  /*  $.ajax({
        url: getRootPath() + "/system/captcha?t=" + (new Date()).getTime(), // 加随机数防止缓存
        type: "get",
        dataType: "json",
        success: function (data) {
            initGeetest({
                gt: data.gt,
                challenge: data.challenge,
                product: "embed",
                offline: !data.success
            }, handlerEmbed);
        }
    });*/
    $("#username").val(localStorage.getItem("userName"));
    $("#password").val(localStorage.getItem("password"));
 /*   $("#login input[id=rempwd]").change(function() {
    	localStorage.setItem("userName", $("#username").val());
    	var checked=$("#login input[id=rempwd]").is(":checked");
    	if(checked){
    		localStorage.setItem("password",$("password").val());
    	}else{
    		localStorage.removeItem("password");
    	}
		
	});*/
    
    $("#login button[id=btn_login]").click(function(){

        if (!$("#username").validatebox("isValid")) {
            $("#username").focus();
        } else if (!$("#password").validatebox("isValid")) {
            $("#password").focus();
        } else if (captcha!=undefined&&!captcha.getValidate()) {
            common_tool.messager_show("请刷新并滑动验证码!")
        } else {
        	var userName=$("#username").val();
        	 var password=$("#password").val();
        	 var rootPath=getRootPath();
            $.ajax({
                url: rootPath+ "/system/login",
                type: "post",
                dataType: "json",
                data: {
                    loginName:userName ,
                    password:password,
                    loginModule:captcha==undefined?"0":"0",//"1",
                    platform: 1,
                    geetest_challenge: $("input[name='geetest_challenge']").val(),
                    geetest_validate: $("input[name='geetest_validate']").val(),
                    geetest_seccode: $("input[name='geetest_seccode']").val(),
                },
                success: function (data) {
                    if (data.code == 10000) {
                    	localStorage.setItem("userName", $("#username").val());
                    	var checked=$("#login input[id=rempwd]").is(":checked");
                    	if(checked){
                    		localStorage.setItem("password",$("#password").val());
                    	}else{
                    		localStorage.removeItem("password");
                    	}
//                        location.href = getRootPath() + "/system/welcome";   //  TODO  跳转到login.jsp页面
                        location.href = getRootPath() + "/home.jsp";   //  TODO  跳转到login.jsp页面
                    } else if (data.code == 20001) {
                        common_tool.messager_show(data.msg);
                        $("#username").focus();
                    } else if (data.code == 20003) {
                        common_tool.messager_show(data.msg);
                        $("#password").focus();
                    }
                    else {
                        common_tool.messager_show(data.msg);
                    }
                }
            })
        }
    
    });
    
 /*   
    $("#login").dialog({

        title: '云录音后台登录',
        closable: false,
        width: 1600,
        height: 800,
        cache: false,
        modal: true,
        resizable: false,
        draggable: false,
        buttons: [
            {
                text: '登录',
                width: 120,
                height:40,
                handler: function () {
                    if (!$("#username").validatebox("isValid")) {
                        $("#username").focus();
                    } else if (!$("#password").validatebox("isValid")) {
                        $("#password").focus();
                    } else if (captcha!=undefined&&!captcha.getValidate()) {
                        common_tool.messager_show("请刷新并滑动验证码!")
                    } else {
                        $.ajax({
                            url: getRootPath() + "/system/login",
                            type: "post",
                            dataType: "json",
                            data: {
                                loginName: $("#username").val(),
                                password: $("#password").val(),
                                loginModule:captcha==undefined?"0":"1",
                                platform: 1,
                                geetest_challenge: $("input[name='geetest_challenge']").val(),
                                geetest_validate: $("input[name='geetest_validate']").val(),
                                geetest_seccode: $("input[name='geetest_seccode']").val(),
                            },
                            success: function (data) {
                                if (data.code == 10000) {
//                                    location.href = getRootPath() + "/system/welcome";   //  TODO  跳转到login.jsp页面
                                    location.href = getRootPath() + "/home.jsp";   //  TODO  跳转到login.jsp页面
                                } else if (data.code == 20001) {
                                    common_tool.messager_show(data.msg)
                                    $("#username").focus();
                                } else if (data.code == 20003) {
                                    common_tool.messager_show(data.msg)
                                    $("#password").focus();
                                }
                                else {
                                    common_tool.messager_show(data.msg)
                                }
                            }
                        })
                    }
                }
            },
        ],
    });*/

    $('#username').validatebox({
        required: true,
        missingMessage: '请输入账号',
    });
    $('#password').validatebox({
        required: true,
        validType: 'length[6, 30]',
        missingMessage: '请输入密码',
        invalidMessage: '请输入6-30位密码',
    });
    if (!$("#username").validatebox("isValid")) {
        $("#username").focus();
    } else if (!$("#password").validatebox("isValid")) {
        $("#password").focus();
    }
    $("#github").click(function () {
        location = "https://github.com/login/oauth/authorize?client_id=f4b35940357e82596645&state=hunt_admin&redirect_uri=http://127.0.0.1:8080/oauth/github";
    });
});