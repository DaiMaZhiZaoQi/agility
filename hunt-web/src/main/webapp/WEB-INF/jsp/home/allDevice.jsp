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
	
            <style type="text/css">
	            .deviceCallLog th{
					text-align: center;	            	
	            }
            	.hrindicate{
            	    height: 1.2px;
				    width: 80px;
				    padding: 0px;
				    border: 0px;
				    border-spacing: 0px;
				    background-color: #fa0101;
				    margin-left: 20px;
				    visibility: visible;
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
					<td id="td_allDevice_title" style="padding-left: 20px; font-size: 22px;">所有设备</td>
				</tr>
				<tr>
					<td style="padding-left: 20px; color: #8A8A8A;font-size: 16px;">
						<span id="curUserCount">在线用户数&nbsp;:</span><br/>
						<span id="curdevideCount">在线设备数&nbsp;:</span>
					</td>
				</tr>
			</table>
			<hr class="horLine"/>
			<!-- 搜索  -->
			<div class="container" align="left" id="searchContainer" style="margin-bottom: 25px;" >
			    <form action="" class="parent">
			    	<table>
			    		<tr>
			    			<td>
				    			<input type="text" class="search" style="margin-bottom: 20px;" placeholder="搜索中文名，序列号" value="${searchContent}"/>	
			    			</td> 
			    			<td>
			    			 	<input type="button" name="allDevice" id="btn_blue_alldevice" class="alldevice" value="搜索"/>
			    			 	<hr id="hr_alldevice" style="visibility:hidden;" class="hrindicate"/>
			    			</td>
			    			
			    			<td>
			    			 	<input type="button" name="alldevice_online" id="btn_green_alldevice" class="alldevice_online" value="在线"/>
			    			 	<hr id="hr_online" style="visibility:hidden;" class="hrindicate"/>
			    			</td>
			    			
			    			<td>
			    			 	<input type="button" name="alldevice_outline" id="btn_red_alldevice" class="alldevice_outline" value="离线"/>
			    			 	<hr id="hr_outline" style="visibility:hidden;" class="hrindicate"/>
			    			</td>
			    		</tr>
			    	</table>
			    </form>
			</div>
		</div>
	
		<!-- 列表  用JSTL循环列表-->
		<div>
			<c:choose>
				<c:when test="${!empty listUserPerCode}">
					<%-- <c:if test="${!empty listUserPerCode}"> --%>
					<%
						List<String> listUserPerCode=(List<String>)request.getAttribute("listUserPerCode");
						for(String str:listUserPerCode){
							System.out.println("权限列表-->"+str);
						}
					%>
					<script type="text/javascript">
						var perArr="<%=listUserPerCode%>";
						common_tool.setGloPer(perArr);
					</script>
					<%-- </c:if> --%>
					<c:if test="${!empty listDevice}"> 
					<%
						Integer isLoadAll=(Integer)request.getAttribute("isLoadAll");
						Integer allDeviceCount=0;
						Integer onLineDeviceCount=0;
						String loginName="";
						Long userId=0l;
						HashSet<Long> userIdSet=null;
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
						/* if(isLoadAll!=null&&isLoadAll==1){		//	刷新统计部门所有设备的状态
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
						} */
					 %>
					 
					  <table id="deviceStatus" class="deviceCallLog"
						style="border:0; width:100%;overflow-x:scroll; background-color: #ffffff; margin-top: 12px;margin-bottom: 40px;">
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
								<th class="callLogSelect">通话记录</th>
								<th class="deviceManage">话机管理</th>
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
									<td title="${sysOrgName}${sysRoleOrgName}${device.sysUser.zhName}">${sysOrgName}${sysRoleOrgName}${device.sysUser.zhName}</td>
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
										<td title="${device.sysDevice.deviceSerial}">${device.sysDevice.deviceSerial}</td>
										<td>${device.sysDeviceTotal.callAlreadyAcceptCount==null?0:device.sysDeviceTotal.callAlreadyAcceptCount}</td>
										<td>${device.sysDeviceTotal.callCallOut==null?0:device.sysDeviceTotal.callCallOut}</td>
										<td>${device.sysDeviceTotal.callNoAccept==null?0:device.sysDeviceTotal.callNoAccept}</td>
										<td><%=deviceTime==""?'无':deviceTime%></td>
										<td class="callLogSelect" style="cursor: pointer; color: #007ad5" id="testtest${device.sysDevice.id}" onclick="device.loadRecord('${device.sysDevice.id}')"> 
											更多
										</td>
										<td class="deviceManage" style="cursor: pointer; color: #007ad5" id="remoe${device.sysDevice.id}" onclick="device.remoManage('${device.sysDevice.deviceMsg}')">
											<%if(sysDevice.getSysDevice().getDeviceMsg()==null){%>
													无
												<%}else{%>
													管理&nbsp;${device.sysDevice.deviceMsg}
												<%}%>
										</td>
								</tr>
								
							</c:forEach>
						</tbody>
					</table>
					      
				
						
					<script type="text/javascript">
						var deviceOnLine=<%=onLineDeviceCount%>;
						var allDeviceCount=<%=allDeviceCount%>;
						<%int searchType= (int)request.getAttribute("searchType");%>
					<%switch(searchType){
						case 0:%>
							let firstLoad=$("#idHomeTable a[id=aOnLineDevice]").attr("name");
							console.log("name"+firstLoad);
							if("firstLoad"==firstLoad){
								$("#idHomeTable a[id=aOnLineDevice]").text("在线设备 "+deviceOnLine+"/"+allDeviceCount);
							}
							
						
							<%if(userIdSet!=null){%>
								var userCount="<%=userIdSet.size()%>";
								if("firstLoad"==firstLoad){
									$("#idHomeTable a[id=userCount]").text("用户数量 "+userCount);
								}
								$("#curUserCount").text("用户数: "+userCount);
							<%}else{%>
								$("#idHomeTable a[id=userCount]").text("用户数量 "+1);
							<%}%>
							$("#curdevideCount").text("设备数: "+allDeviceCount);
							$("#idHomeTable a[id=aOnLineDevice]").attr("name","seCondLoad");
							<%break;
						case 1:%>
							<%if(userIdSet!=null){%>
								var userCount="<%=userIdSet.size()%>";
								$("#curUserCount").text("在线用户数: "+userCount);
								var userCount="<%=userIdSet.size()%>";
								$("#curdevideCount").text("在线设备数: "+deviceOnLine);	
							
							<%}else{%>
								$("#curUserCount").text("在线用户数: "+0);
								var userCount="<%=userIdSet.size()%>";
								$("#curdevideCount").text("在线设备数: "+0);	
							<%}
							break;
						case 2:%>
								
								<%if(userIdSet!=null){%>
									var userCount="<%=userIdSet.size()%>";
									$("#curdevideCount").text("离线用户数: "+userCount);    
								<%}else{%>
									$("#curdevideCount").text("离线用户数: "+0);
								<%}%>
								var allDeviceCount="<%=allDeviceCount%>";
								$("#curUserCount").text("离线设备数: "+allDeviceCount);
							<%break;
						case 3:%>
								<%if(userIdSet!=null){%>
									var userCount="<%=userIdSet.size()%>";
									$("#curUserCount").text("查询用户数: "+userCount);
								<%}%>
								$("#curdevideCount").text("查询设备数: "+allDeviceCount);
							<%break;
					}%>
						
						
					</script>
				 </c:if>
				 <c:if test="${empty listDevice}">
				 	<script type="text/javascript">
					 	/* $("#idHomeTable a[id=aOnLineDevice]").text("在线设备 "+0+"/"+0);
						$("#idHomeTable a[id=userCount]").text("用户数量 "+0); */
						$("#curUserCount").text("用户数: "+0);
						$("#curdevideCount").text("设备数: "+0);
					</script>
				 </c:if>
				</c:when>
			</c:choose>
			 
	 			
		
		</div>
		
		
</div>
	
		
		<!-- <div id="inscribe" 
			style="width:100%;background-color: #ffffff;display:inline-block;position: absolute;bottom: 0px;left: 0%;"  class="clearfix inscribe">
			<br style="margin-top: 20px;">
			<div style="margin-top: 20px;height: 75%;" class="clearfix">&nbsp;&nbsp;&nbsp;</div>
			<div align="center" class="clearfix"><a href="http://ir2.co/" class="clearfix" target="view">Copyright © 2020 铱方科技（深圳）有限公司&nbsp;版权所有</a></div>
			<div></div>
			
			<div align="center" class="clearfix">Copyright © 2020 铱方科技（深圳）有限公司&nbsp;版权所有</div>
				    <br>
			<div align="center">技术支持:&nbsp;&nbsp;天津市慧然网络科技有限公司</div>
			<div align="center"><a href="http://www.tjhrwl.com/" target="view">技术支持:&nbsp;&nbsp;天津市慧然网络科技有限公司</a></div>
				    <br>
			<div align="center">联系电话:&nbsp;&nbsp;13662125100&nbsp;&nbsp;邮箱地址：153465518@qq.com</div>
				    
		</div> -->

</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/all-device.js"></script>
	<script type="text/javascript">
	
		console.log("权限详情");
	
		
		<%
			LoginInfo lf=(LoginInfo)session.getAttribute("loginInfo");
			String logN=lf.getZhName();
			Long userLoginId=lf.getId();
		%>
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