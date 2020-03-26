<%@page import="com.hunt.model.entity.SysRoleOrganization"%>
<%@page import="com.hunt.model.dto.LoginInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>铱方云录音管理系统</title>
<link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/static/image/favicon.ico">
    <link type="image/x-icon" rel="bookmark" href="${pageContext.request.contextPath}/static/image/favicon.ico">
    
	
	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/easyui.css"/>
	
	
	
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/easyui/jquery.min.js"></script>
	
	
	
	<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/audiojs/main.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/audiojs/audioplayer.js"></script> --%>
	<script type="text/javascript"
            src="${pageContext.request.contextPath}/static/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/easyui/datagrid-groupview.js"></script>
           <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/system/common.js"></script>
          
</head>
<body id="bodyContainer">
	<div id="testtest"></div>
	
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/myTask.js"></script>
	<!-- <div style="margin-top:30px">
		发布任务 <input class="easyui-filebox" name="file1" data-options="prompt:'Choose a file...'" style="width:50%">
		<a href="javascript:void(0)" class="easyui-linkbutton" style="width:100%">确认</a>
	</div> -->
</body>
<script type="text/javascript">
	$(document).ready(function(){
		var brower=getBrowser();
		console.log("浏览器"+brower);
		if(brower=="Chrome"){
//			document.getElementById('bodyContainer').setAttribute('content', 'width=device-width, initial-scale=0.8, maximum-scale=0.8, user-scalable=no');
			document.getElementById('bodyContainer').style.zoom=0.9;
		}else if(brower=="Edge"){
			document.getElementById('bodyContainer').style.zoom=0.9;
		}
		
	});
</script>
</html>