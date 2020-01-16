<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
     <meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>铱方云录音管理系统</title>
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/static/image/favicon.ico">
    <link type="image/x-icon" rel="bookmark" href="${pageContext.request.contextPath}/static/image/favicon.ico">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/icon.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/system/system_login.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/easyui/jquery.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/js/easyui/easyui-lang-zh_CN.js"></script>
    <%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/system/login.css"/> --%>
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/system/common.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/js/system/http_static.geetest.com_static_tools_gt.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/system/login.js"></script>
    
            <style type="text/css">
            .panel window{
            	font-size: 16px;
            }
            </style>
</head>
<body style="background-color: #f1f0f0" id="loginContainer">
	<div id="login_head" 
		style="height: 60px;width: 1366px;background-color: #ffffff;margin-left: auto;margin-right: auto;"align="center">
		<img alt="铱方科技" src="static/css/home/image/logo.png" style="
		height: 60px;
	    line-height: 60px;
	    float: left;">
	</div>
	<div id="login" align="center"  style="width: 1366px;
	    max-width: 1610px;
	    margin-left: auto;
		margin-top: 10px;
	    margin-right: auto;">	
	    <div style="background-image: url(${pageContext.request.contextPath}/static/css/home/image/background1.png);
	    	background-repeat: no-repeat;height: 675px;width: 1366px;
	    	 background-position:center;" align="center" >
	    	<div style="padding-top: 160px;
			    margin-left: 230px;
			    width: auto;
			    height: auto;
			" align="center">
	    	     <p style="font-size: 19px;color: #ffffff">账号&nbsp;
	    	     <input name="username" id="username" style="width: 200px;height: 35px;font-size: 18px;padding-left: 3px;" type="text" class="textbox"></p>
	        <p style="padding-top: 10px;
	        			color:#ffffff;
				        font-size: 19px;">密码&nbsp;
				        <input name="username" id="password"
	                                                                    style="width: 200px;height: 35px;font-size: 18px;padding-left: 3px;"
	                                                                    type="password" class="textbox"></p>
	        <!-- <div id="embed-captcha" style="width: 290px ;height: 188px;margin-top: 20px;"></div> -->
	    	<div>
				<input id="rempwd" type="checkbox" checked="checked" 
					style="background-color: rgba(255,255,255,0.7);
					font-family: Tahoma, Arial, Helvetica, sans-serif;
	    			font-size: 14px;
	    			width: 20px;
	    			height: 20px;
	    			margin-left: 166px;">
				<label id="lbl_remember_pwd" for="rempwd" style="margin-right: 8px;   
												 font-size: 17px;
												 vertical-align:super;
		    									color: #ffffff;">记住密码</label>
	    	</div>
				<button style="	height:45px;
								width:120px;
								color:white;
								font-size:20px;
								border-radius:5px;
								box-shadow:none;
								background: #6dcdf3;
								margin-top: 30px;
								margin-left: 50px;
								border-color: transparent;" id="btn_login">登录</button>
	    	</div>
	   
	
	  <!--       <div id="github" class="easyui-linkbutton">使用github登录</div> -->
	    </div>
		
	</div>
	<div id="login_head" 
		style="height: 35px;width: 1366px;background-color: #ffffff;margin-left: auto;margin-right: auto;visibility: hidden;">
		<a href="https://ir2.co/?page_id=19"  target="_blank" style="float: left;
			    margin-left: 20px;
			    margin-top: 5px;
			    font-size: 19px;
			    text-decoration:none
			    ">联系我们 &nbsp;></a>
		<span style="float: right;
			    margin-right: 20px;
			    margin-top:5px;
			    font-size: 18px;
			    color: #000000;">COPYRIGHT © 2016-2019 铱方科技（深圳）有限公司</span>
	</div>

</body>
<script type="text/javascript">
	$(document).ready(function(){
		var brower=getBrowser();
		console.log("浏览器"+brower);
		if(brower=="Chrome"){
//			document.getElementById('bodyContainer').setAttribute('content', 'width=device-width, initial-scale=0.8, maximum-scale=0.8, user-scalable=no');
			document.getElementById("loginContainer").style.zoom=0.9;
		}else if(brower=="Edge"){
			document.getElementById("loginContainer").style.zoom=0.9;
		}
		
	});
</script>
</html>
