<%@page import="com.hunt.model.entity.SysRoleOrganization"%>
<%@page import="com.hunt.model.dto.LoginInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
 <meta name="viewport" id="view1" content="width=device-width, initial-scale=0.8, maximum-scale=3.0, user-scalable=yes" />
<title>铱方录音管理系统</title>
<link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/static/image/favicon.ico">
    <link type="image/x-icon" rel="bookmark" href="${pageContext.request.contextPath}/static/image/favicon.ico">
    
	
	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/easyui.css"/>
	
	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/home/home.css" />
	<link rel="stylesheet" type="text/css" 
	href="${pageContext.request.contextPath}/static/css/home/homecss/commonLabel.css"/>
	
	<link rel="stylesheet" type="text/css" href="static/css/home/homecss/device-manage.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/home/csxAudio/csxAudio.css"/>
	 
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/easyui/jquery.min.js"></script>
	
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/laydate/laydate.js"></script>
	
	<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/audiojs/main.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/audiojs/audioplayer.js"></script> --%>
	<script type="text/javascript"
            src="${pageContext.request.contextPath}/static/js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/js/easyui/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/easyui/datagrid-groupview.js"></script>
           <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/system/common.js"></script>
           <script type="text/javascript">
	           <%
				LoginInfo lf=(LoginInfo)session.getAttribute("loginInfo");
				String logN=lf.getLoginName();
				Long userLoginId=lf.getId();
				%>
			var gloUserId="<%=userLoginId%>";
			console.log("登录成功保存用户id-->"+gloUserId);
			common_tool.setCurrUserId(gloUserId);
           </script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/home-device-list.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/csxAudio/csxAudio.js"></script>
</head>
<body id="bodyContainer">
	<!-- <div id="testtest"></div> -->
	<div class="mediaContainer">
	
		<!-- 头部 --> 
		<div>
		
			<c:forEach items="${sessionScope.get('loginInfo').jobs}" var="SysUserRoleOrganization">
		
			<font class="sysOrgId" color="red" style="display: none;">${SysUserRoleOrganization.sysRoleOrganizationId}</font>
			</c:forEach>
			<table width="100%" id="idHomeTable">
				<tr>
					<!-- LOGO -->
					<td width="70%" style="white-space: nowrap;padding-left: 30px;">
						<img id="img_head_left" alt="铱方科技" src="static/css/home/image/logo.png"/>
						<!-- <span style="display: block;font-size: 20px;padding-top: 30px;">天津市蓟州区市场监督管理局录音系统</span> -->
					</td>
				<%--  基础数据展示   使用EL表达式取值 --%>
					<td width="30%" style="white-space: nowrap; padding-right: 50px;">
						
						<a href="javascript:void(0)" id="aOnLineDevice" name="firstLoad">在线设备&nbsp;0/0</a>
						&nbsp;&nbsp;
						<a href="javascript:void(0)" id="userCount">用户数量&nbsp;1</a>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a href="javascript:void(0)" id="loginName">欢迎，</a>
						<a href="javascript:void(0)" id="modifyPass" style="margin-left: 20px;color: #007ad5;font-weight: bold;"> 修改密码</a>
						<a href="javascript:void(0)" id="goCenter" class="userManage" style="margin-left: 20px;color: #007ad5;font-weight: bold;">管理中心&nbsp;></a>
						<a href="javascript:void(0)" id="goBack" style="margin-left: 20px;color: #007ad5;font-weight: bold;">退出&nbsp;></a>
					</td>

				</tr>
			</table>
		</div>


		<!--菜单栏 Menu -->
		<div id="menu" style="overflow: hidden;
		min-width: 1080px;max-width: 2000px;background-color: #007ad5;height: 50px;
		padding-right:50px;
    	line-height: 50px;" class="clearfix">
			<ul class="nav" >
				<li id="tdMyTask">
					<img alt="我的任务" src="static/css/home/image/ntask.png"><span>我的任务</span>
				</li>
			    <!-- <a href="javascript:void(0)">
					<img alt="我的任务" src="static/css/home/image/ntask.png">我的任务
				</a> -->
			 <!--    <ul class="plat" style="position: absolute; top: 140px;z-index:9999;">
			        <li><a href="javascript:void(0)">发布任务</a></li>
			        <li><a href="javascript:void(0)">分派任务</a></li>
			        
			    </ul> -->
			</ul>
			<ul class="nav" id="tdContact">
			   <li>
			   		<img alt="通讯录" src="static/css/home/image/ncontacts.png"/><span>企业通讯录</span>
			   </li>
			  <!-- <a href="javascript:void(0)">
			  	<img alt="通讯录" src="static/css/home/image/ncontacts.png"/>企业通讯录
				
			  </a> -->
			</ul>
			<ul class="nav" id="iCallRecord">
				<li>
						<img alt="通话记录" src="static/css/home/image/nRecord.png"><span>录音管理</span>
				</li>
			   <!-- <a href="javascript:void(0)">
					<img alt="通话记录" src="static/css/home/image/nRecord.png">录音管理
			   </a> -->
			</ul>
			<ul class="nav" id="iDeviceState" >
				<li >
					<img  alt="设备状态" src="static/css/home/image/nHome.png"><span>设备管理</span>
				</li>
				<!-- <a href="javascript:void(0)"> 
					<img  alt="设备状态" src="static/css/home/image/nHome.png">设备管理 
				</a> -->
			</ul>
		<!-- 	<ul class="nav" id="tdDeviceManage">
			   <a href="javascript:void(0)" style="height: 20px; line-height: 20px;">
					<img alt="设备管理" src="static/css/home/image/ndevice.png">设备管理
				</a>
			</ul> -->
		
		
			<!-- <table width="100%" height="100%" bgcolor="#007ad5" style="padding-top: 0px;padding-bottom: 0px;border-spacing:0px;">
				<tr align="center">
					<td width="60%" height="100%" class="tdAuto"></td>
					<td id="iDeviceState" align="center" class="tdAuto" style="padding: 15px 5px 15px 5px;" >
						<a href="javascript:void(0)" style="height: 20px; line-height: 20px;"> 
							<img  alt="设备状态" src="static/css/home/image/nHome.png">设备管理
						</a>
					</td>
					<td  align="center" id="iCallRecord" class="tdAuto">
						<a href="javascript:void(0)"  style="height: 20px; line-height: 20px;">
							<img alt="通话记录" src="static/css/home/image/nRecord.png">录音管理
						</a>
					</td>
					<td  align="center" id="tdContact" class="tdAuto">
						<a href="javascript:void(0)"  style="height: 20px; line-height: 20px;">
							<img alt="通讯录" src="static/css/home/image/ncontacts.png"/>企业通讯录</a>
					</td>
					<td  align="center" id="tdMyTask" class="tdAuto">
						<select>
							<option>
								<a href="javascript:void(0)" style="height: 20px; line-height: 20px;">
									<img alt="我的任务" src="static/css/home/image/ntask.png">我的任务
								</a>
							</option>
							<option>发布任务</option>
							<option>任务分配</option>
						</select>
					</td>
					<td  align="center" id="tdDeviceManage" class="tdAuto">
							<a href="javascript:void(0)" style="height: 20px; line-height: 20px;">
								<img alt="设备管理" src="static/css/home/image/ndevice.png">设备管理
							</a>
					</td>
					<td width="5%" class="tdAuto"></td>
				</tr>
			</table> -->
		
		</div>
	
		<!-- 内容 -->
		<div id="div_home_content">
			<div id="div_device_state" style="display: none;    min-height: 633px;">
				
		
				<!-- 设备列表  采用第三方框架 easy-ui  -->
				<div id="div_device_manage_state">
					<!-- 部门  easy-ui -->
					<div  style="float:left;width:18%; height: 100%" id="abcTemp">
					
						<ul id="tt" class="easyui-tree" 
						style="
							height: auto;
						    margin-top: 30px;
						    padding-top: 20px;
						    padding-bottom: 20px;
						    border-bottom: solid 0.5px #e6e6e6;
						    border-left: solid 0.5px #e6e6e6;
						    border-right: solid 0.5px #e6e6e6;
						    border-top: solid 0.5px #e6e6e6;">			    
						</ul>
					
					</div>
					<div style="float: right;width:82%;" id="div_device_statechild">
						<div class="main-container" id="div_device_state_child" style=" margin-top: 20px;margin-left: 10px;">
							欢迎使用铱方云录音
						</div>
						
						<div class="main-container" id="div_call_record" style=" margin-top: 20px;margin-left: 10px;display: none;">
							欢迎使用铱方云录音
						</div>
					
					</div>
					
				</div>
			</div>
			<div id="div_contact_manage" style="display: none;"> 
			</div>
				<!-- 设备管理 -->
			<div id="div_device_manage" style="display: none;">
				设备管理
			</div>
			
			<!-- 设备管理 -->
			<div id="div_myTask" style="display: none;">
				我的任务
			</div>
		</div>
				<div id="recordDialog" style="display: none;">
					<div style="margin-top:30px;background-color:#ffffff;with:100%;"id="contonerDialog">
						<div id="record_container"></div>
					</div>
				</div>
		
	</div>
	<!--  height: auto;  -->
	<div id="inscribe" 
			style="width:100%;background-color: #ffffff;display:inline-block;  height: 22%; "  class="clearfix inscribe">
			<br style="margin-top: 20px;">
			<div style="margin-top: 20px;height: 75%;" class="clearfix">&nbsp;&nbsp;&nbsp;</div>
			<div align="center" class="clearfix"><a href="http://ir2.co/" class="clearfix" target="view">Copyright © 2020 铱方科技（深圳）有限公司&nbsp;版权所有</a></div>
				    <br/>
				    <br/>
	<!-- 		<div align="center"><a href="http://www.tjhrwl.com/" target="view">技术支持:&nbsp;&nbsp;天津市慧然网络科技有限公司</a></div>
				    <br>
			<div align="center" style="margin-bottom: 25px;">联系电话:&nbsp;&nbsp;13662125100&nbsp;&nbsp;邮箱地址：153465518@qq.com</div> -->
				    
		</div>
		
		<div id="mm" style="width:150px;display: none;">

		</div>
		
	
			<div id="dialogModifyPass" style="display: none;">、
				<!--   data-options="required:true,validType:['length[6,20]']" -->
				    <form id="modify_password_form" style="padding: 20px 0 0 0;">
				        <p style="padding: 10px 0 10px 23px ;">&nbsp;&nbsp;密码:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="newPassword" type="password" id="nPassword"
				                                                                     style="width: 300px;height: 35px"
				                                                                     autocomplete="false"
				                                                                     data-options="required:true,prompt:'密码长度6~20个字符',validType:['length[6,20]']"
				                                                                     class="easyui-textbox easyui-validatebox"></p>
				        <p style="padding: 10px 0 10px 23px ;">重复密码 : <input name="repeatNewPassword" type="password"
				                                                           id="rNewPassword"
				                                                           autocomplete="false"
				                                                           style="width: 300px;height: 35px"
				                                                           data-options="required:true,prompt:'密码长度6~20个字符',validType:['length[6,20]']"
                                                           class="easyui-textbox easyui-validatebox"></p>
   					 </form>
			
			
			
			</div>
		<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/myTask.js"></script> --%>
	<!-- <div style="margin-top:30px">
		发布任务 <input class="easyui-filebox" name="file1" data-options="prompt:'Choose a file...'" style="width:50%">
		<a href="javascript:void(0)" class="easyui-linkbutton" style="width:100%">确认</a>
	</div> -->
</body>
<script type="text/javascript">
	$(document).ready(function(){
		var brower=getBrowser();
		console.log("浏览器"+brower);
		if(brower=="Chrome"){		// TODO  对手动拖拉调节宽度有影响
//			document.getElementById('bodyContainer').setAttribute('content', 'width=device-width, initial-scale=0.8, maximum-scale=0.8, user-scalable=no');
//			document.getElementById('bodyContainer').style.zoom=0.9;
		}else if(brower=="Edge"){
		//	document.getElementById('bodyContainer').style.zoom=0.9;
		}
		/* 
		 $("#tdMyTask").menubutton({
			 menu: '#mm'
		}); */
	});
</script>
</html>