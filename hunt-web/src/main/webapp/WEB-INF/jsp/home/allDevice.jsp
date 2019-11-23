<%@page import="javax.servlet.jsp.jstl.core.LoopTagStatus"%>
<%@page import="com.hunt.model.dto.LoginInfo"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.hunt.model.entity.SysUser"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Timestamp" %>
<%@page import="java.util.List" %>
<%@page import="com.hunt.model.dto.SysDeviceCallLogAndRecordDto" %>
<%@page import="com.hunt.model.entity.SysDeviceRecord" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.hunt.model.dto.SysDeviceAndCallDto" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!-- 所有设备  待删除  -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
	<%-- <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/js/easyui/jquery.easyui.min.js"></script> --%>
            <style type="text/css">
	            .deviceCallLog th{
					text-align: center;	            	
	            }
            
            </style>
</head>
<body>
	<div id="div_device_state_one">
	
	
	<!-- 头部  -->
	<div id="div_alldevice_head">
		<input type="hidden" id="orgId" name="hiddenyu" value="${orgId}"/>
		<input type="hidden" id="deviceId" name="deviceId" value=""/>
		<input type="hidden" id="requestType" name="hiddenyu" value="${requestType}"/>
		<input type="hidden" id="isLoadAll" name="isLoadAll" value="${isLoadAll}">
		<table>
			<tr>
				<td rowspan="2">
					<img alt="所有设备" class="deviceHeadImg" src="${pageContext.request.contextPath}/static/css/home/image/home.png">
				</td>	
				<td id="td_allDevice_title" style="padding-left: 20px; font-size: 24px;">所有设备</td>
			</tr>
			<tr>
				<td style="padding-left: 20px; color: #8A8A8A;font-size: 16px;">所有设备全部状态</td>
			</tr>
		</table>
		<hr class="horLine"/>
		<!-- 搜索  -->
		<div class="container" align="left" id="searchContainer" >
		    <form action="" class="parent">
		    	<table>
		    		<tr>
		    			<td>
			    			<input type="text" class="search" placeholder="搜索用户名，设备ID" value="${searchContent}" >	
		    			</td>
		    			<td>
		    			 	<input type="button" name="allDevice" id="btn_blue_alldevice" class="alldevice" value="搜索">
		    			</td>
		    			
		    			<td>
		    			 	<input type="button" name="alldevice_online" id="btn_green_alldevice" class="alldevice_online" value="在线">
		    			</td>
		    			
		    			<td>
		    			 	<input type="button" name="alldevice_outline" id="btn_red_alldevice" class="alldevice_outline" value="离线">
		    			</td>
		    		</tr>
		    	</table>
		    </form>
		</div>
	</div>

	<!-- 列表  用JSTL循环列表-->
	<div>
	<c:if test="${!empty listDevice}"> 
		
		
		<%
			Integer isLoadAll=(Integer)request.getAttribute("isLoadAll");
			Integer allDeviceCount=0;
			Integer onLineDeviceCount=0;
			String loginName="";
			Long userId=0l;
			HashSet<Long> userIdSet=null;
			if(isLoadAll!=null&&isLoadAll==1){		//	刷新统计部门所有设备的状态
				List<SysDeviceAndCallDto> listUserAndDevice=(List<SysDeviceAndCallDto>) request.getAttribute("listDevice");
				
				if(listUserAndDevice!=null&&listUserAndDevice.size()>0){
					SysDeviceAndCallDto dto= listUserAndDevice.get(0);
					SysUser sysUser=dto.getSysUser();
					userId=sysUser.getId();
					System.out.println("用户id-->"+userId);
					loginName=sysUser.getLoginName();
					userIdSet=new HashSet<>();
				}
				allDeviceCount=listUserAndDevice.size();
			}else{
				userIdSet=null;
			}
		 %>
		 
		  <table id="deviceStatus" class="deviceCallLog"
			style="border:0; width:100%;overflow-x:scroll; background-color: #ffffff; margin-top: 12px;">
			<thead >
				<tr style="background-color: #f3f3f3;" >
					<th>序号</th>
					<th>部门/人员</th>
					<th>在线状态</th>
					<th>设备序列号</th>
					<th>来电</th>
					<th>去电</th>
					<!-- <th onclick='recordCommon.clickChangeData(this,"call_duration")'>通话时长<img alt="正序排列" src="static/css/home/image/arrow_down.png" style="width:15px;height: 15px;margin-top: 10px;margin-left: 5px;"/></th>
					<th onclick='recordCommon.clickChangeData(this,"reco_audio_length")'>录音时长<img alt="正序排列" src="static/css/home/image/arrow_down.png" style="width:15px;height: 15px;margin-top: 10px;margin-left: 5px;"/></th>
					<th onclick="recordCommon.clickChangeData(this,'call_date')">通话时间<img alt="正序排列" src="static/css/home/image/arrow_down.png" style="width:15px;height: 15px;margin-top: 10px;margin-left: 5px;"/></th> -->
					<th>未接</th>
					<th>访问时间</th>
					<th>通话记录</th>
					<th>话机管理</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${listDevice}" var="device" begin="0" step="1" varStatus="index">
					<c:set value="${device.setSysOrganization}" var="listSysOrg"/>
					<c:set var="sysOrgName" value=""/>
					<c:forEach items="${listSysOrg}" var="sysOrg">
						<c:set var="sysOrgName" value="${sysOrgName}${sysOrg.name}/"/>	
					</c:forEach>
					
					<c:set value="${device.setSysRoleOrganization}" var="listSysRoleOrg"/>
					<c:set var="sysRoleOrgName" value=""/>
					<c:forEach items="${listSysRoleOrg}" var="sysRoleOrg">
						<c:set var="sysRoleOrgName" value="${sysRoleOrgName}${sysRoleOrg.name }/"/>
					</c:forEach>
					<% LoopTagStatus obj=(LoopTagStatus)pageContext.getAttribute("index"); 
						System.out.println("pageContext-->"+obj.getIndex());
						String sty="";
						if(obj.getIndex()%2==0){
							sty="background-color: #ffffff;";
						}else{
							sty="background-color: #f3f3f3;";
						}
					%> 
					<tr id="tr${device.sysDevice.id}" style="<%=sty%>"> 
					
						<td style="padding:0px;">${index.index+1}</td>
						<td>${sysOrgName}${sysRoleOrgName}${device.sysUser.zhName}</td>
						<%!String style="";%>
							<%!String onStr="OFF"; %>
							<%!String deviceTime="";%>
							<%
								SysDeviceAndCallDto sysDevice=(SysDeviceAndCallDto)pageContext.getAttribute("device");
								
								Date deviceDate=sysDevice.getSysDevice().getDeviceTime();
								boolean isOnLine=false;
								SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:mm");
								SysUser sysUser=sysDevice.getSysUser();
								if(sysUser!=null && userIdSet!=null){
									Long userid2=sysUser.getId();
									userIdSet.add(userid2);
								}
								if(deviceDate!=null){
									Long time=deviceDate.getTime();
									deviceTime=sdf.format(deviceDate);
									isOnLine=System.currentTimeMillis()-time<(5*60*1000)?true:false;
									System.out.println("deviceDate-->"+isOnLine+"time-->"+time+"-currTime-->");
								}else{
									deviceTime="";
								}
								pageContext.setAttribute("isOnLine", isOnLine);
							%>
					
							<c:choose>
								<c:when test="${isOnLine}">
									<%style="background-color"+":#5abc71;color"+":white"; 
										onStr="ON";
										if(allDeviceCount!=0){
											onLineDeviceCount++;
										}
									%>
								</c:when>
								<c:otherwise>
									<%
										style="background-color"+":c0c0c0"; 
										onStr="OFF";
									%>
								</c:otherwise>
							</c:choose>
							<td style=<%=style%>><%=onStr%></td>
							<td>${device.sysDevice.deviceSerial}</td>
							<td>${device.sysDeviceTotal.callAlreadyAcceptCount==null?0:device.sysDeviceTotal.callAlreadyAcceptCount}</td>
							<td>${device.sysDeviceTotal.callCallOut==null?0:device.sysDeviceTotal.callCallOut}</td>
							<td>${device.sysDeviceTotal.callNoAccept==null?0:device.sysDeviceTotal.callNoAccept}</td>
							<td><%=deviceTime==""?'无':deviceTime%></td>
							<td style="cursor: pointer; color: #007ad5" id="testtest${device.sysDevice.id}" onclick="device.loadRecord('${device.sysDevice.id}')"> 
								更多
							</td>
							<td style="cursor: pointer; color: #007ad5" id="remoe${device.sysDevice.id}" onclick="device.remoManage('${device.sysDevice.deviceMsg}')">
								<%if(sysDevice.getSysDevice().getDeviceMsg()==null){%>
										无
									<%}else{%>
										管理&nbsp;${device.sysDevice.deviceMsg}
									<%}%>
							
						<%-- 		管理&nbsp;${device.sysDevice.deviceMsg==null?'无':device.sysDevice.deviceMsg} --%>
							</td>
<%-- 							<td style="cursor: pointer; color: #007ad5" onclick="device.loadAppend('${device.sysDevice.id}')"> 
								更多
							</td>
 --%>						
					</tr>
					
				</c:forEach>
			</tbody>
		</table>
		      
	<%-- <c:forEach items="${listDevice}" var="device">
	
			<div class="device-item" id="${device.sysDevice.id}">
					<c:set value="${device.setSysOrganization}" var="listSysOrg"/>
					<c:set var="sysOrgName" value=""/>
					<c:forEach items="${listSysOrg}" var="sysOrg">
						<c:set var="sysOrgName" value="${sysOrgName}${sysOrg.name}/"/>	
					</c:forEach>
					
					<c:set value="${device.setSysRoleOrganization}" var="listSysRoleOrg"/>
					<c:set var="sysRoleOrgName" value=""/>
					<c:forEach items="${listSysRoleOrg}" var="sysRoleOrg">
						<c:set var="sysRoleOrgName" value="${sysRoleOrgName}${sysRoleOrg.name }/"/>
					</c:forEach>
					
						<div class="device-item_title_child">
							<span id="sp_device_title">${sysOrgName}${sysRoleOrgName}${device.sysUser.zhName}</span>
							<!-- <span id="sp_device_title">'+orgName+roleOrgName+list[i].sysUser.loginName+'</span> -->
							<%!String style="";%>
							<%!String onStr="OFF"; %>
							<%!String deviceTime="";%>
							<%
								SysDeviceAndCallDto sysDevice=(SysDeviceAndCallDto)pageContext.getAttribute("device");
								
								Date deviceDate=sysDevice.getSysDevice().getDeviceTime();
								boolean isOnLine=false;
								SimpleDateFormat sdf=new SimpleDateFormat("MM/dd HH:mm");
								SysUser sysUser=sysDevice.getSysUser();
								if(sysUser!=null && userIdSet!=null){
									Long userid2=sysUser.getId();
									userIdSet.add(userid2);
								}
								if(deviceDate!=null){
									Long time=deviceDate.getTime();
									deviceTime=sdf.format(deviceDate);
									isOnLine=System.currentTimeMillis()-time<(5*60*1000)?true:false;
									System.out.println("deviceDate-->"+isOnLine+"time-->"+time+"-currTime-->");
								}else{
									deviceTime="";
								}
								pageContext.setAttribute("isOnLine", isOnLine);
							%>
					
							<c:choose>
								<c:when test="${isOnLine}">
									<%style="background-color"+":#5abc71;color"+":white"; 
										onStr="ON";
										if(allDeviceCount!=0){
											onLineDeviceCount++;
										}
									%>
								</c:when>
								<c:otherwise>
									<%
										style="background-color"+":c0c0c0"; 
										onStr="OFF";
									%>
								</c:otherwise>
							</c:choose>
							
							<span id="sp_device_state" style=<%=style%>><%=onStr%></span>	
						</div>
						<div class="div_item_child">
							<span id="sp_device_id" class="gray_text" style="width: 20px;">设备ID&nbsp;:&nbsp;${device.sysDevice.deviceSerial}</span>
							<span id="sp_device_deviceTime" class="gray_text" style="width: 120px;">设备时间&nbsp;:&nbsp;<%=deviceTime%></span>
						</div>
						<div class="div_item_child">
						<table class="table-options" style="width: 100%;">
								<tr>
									<td style="width: 500px;">
										<span id="sp_device_version" class="gray_text">软件版本&nbsp;:&nbsp;</span>
										<span id="sp_device_ip" class="gray_text" style="margin-left: 50px;">设备IP&nbsp;:&nbsp;</span>
									</td>
									<td style="margin-left: 100px" align="right">
										<input type="button" name="alldevice_online" id="btn_green_alldevice" class="alldevice_online" style="padding-left: 10px;padding-right: 10px;font-size: 18px;width: 110px;" onclick="device.load(${device.sysDevice.id})" value="通话与录音">
									</td>
								
								</tr>
							</table>
						</div>
						<div class="div_item_child">
							<span id="sp_device_callIn" class="gray_text">来电&nbsp;:&nbsp;${device.sysDeviceTotal.callAlreadyAcceptCount==null?0:device.sysDeviceTotal.callAlreadyAcceptCount}</span>
							<span id="sp_device_callOut" class="gray_text">去电&nbsp;:&nbsp;${device.sysDeviceTotal.callCallOut==null?0:device.sysDeviceTotal.callCallOut}</span>
							<span id="sp_device_noAccept" class="gray_text">未接&nbsp;:&nbsp;${device.sysDeviceTotal.callNoAccept==null?0:device.sysDeviceTotal.callNoAccept}</span>
						</div>
						
						<div style="margin-top:30px;background-color:#ffffff;with:100%;"id="contoner${device.sysDevice.id}">
							<div></div>
						</div>
						
						
					</div>
		</c:forEach> --%>
			
		<script type="text/javascript">
		
			var allDeviceCount="<%=allDeviceCount%>";
			if(allDeviceCount>0){
			
				var deviceOnLine="<%=onLineDeviceCount%>";
				$("#idHomeTable a[id=aOnLineDevice]").text("在线设备 "+deviceOnLine+"/"+allDeviceCount);
				
				<%if(userIdSet!=null){%>
						var userCount="<%=userIdSet.size()%>";
						$("#idHomeTable a[id=userCount]").text("用户数量 "+userCount);
					<%}else{%>
					$("#idHomeTable a[id=userCount]").text("用户数量 "+1);
				<%}%>
			}
			
			
		</script>
	 </c:if>
			<!-- <div  id="showDialog">显示对话框</div> -->
	
 			<div id="recordDialog" class="easyui-dialog">
				<div style="margin-top:30px;background-color:#ffffff;with:100%;"id="contoner1">
					<div id="deviceId"></div>
				</div>
			</div>
	

	
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/all-device.js"></script>
	<script type="text/javascript">
		//var hasP="${hasP}";
		//console.log("权限-->"+hasP);
		var reco=$(".panel").children("#recordDialog");
		if(reco!=null){
			$(".panel").remove();
		}
		console.log("权限详情");
		console.log("${hasP}");
		if("${hasP}"==1){
			$("#idHomeTable a[id=goCenter]").show();
		}else{
			$("#idHomeTable a[id=goCenter]").hide();
		}
		
		<%
			LoginInfo lf=(LoginInfo)session.getAttribute("loginInfo");
			String logN=lf.getLoginName();
			Long userLoginId=lf.getId();
		%>
			var gloUserId="<%=userLoginId%>";
			common_tool.setCurrUserId(gloUserId);
		$("#idHomeTable a[id=loginName]").text("欢迎，"+"<%=logN%>");
		
		<%
		String promptMsg= (String)request.getAttribute("promptMsg");
		if(null!=promptMsg&&promptMsg.length()>0){%>
			var proStr="<%=promptMsg%>";
			common_tool.messager_show(proStr);
			$("#deviceStatus").remove();
		<%}%>
	</script>
</html>