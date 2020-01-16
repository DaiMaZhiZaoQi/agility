<%@page import="com.alibaba.druid.sql.ast.expr.SQLCaseExpr.Item"%>
<%@page import="com.hunt.controller.JsonUtils"%>
<%@page import="java.util.Date"%>
<%@page import="com.hunt.model.entity.SysDeviceRecord"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.hunt.model.dto.SysDeviceCallLogAndRecordDto" %>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--  通话记录操作 待删除 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/easyui.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/easyui/jquery.min.js"></script>
<script type="text/javascript"
            src="${pageContext.request.contextPath}/static/js/easyui/jquery.easyui.min.js"></script> --%>
  <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/js/easyui/jquery.easyui.min.js"></script>           
<style type="text/css">
th {
		height: 40px;
		line-height: 40px;
		width: auto;
		padding-top:5px;
		padding-bottom:5px;
		margin-right: 5px;
	}
#deviceCallLog td{
		height: 40px;
		line-height: 40px;
		padding-top:5px;
		padding-bottom:5px;
		height: 40px;
		line-height: 40px;
		text-align: center;
}
/* 	width: auto;   */
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/deviceCallLog.js"></script>
<script type="text/javascript">
	function deleteCallLog(callLog,obj,i){
		//var callLog=$("#deviceCallLog input[id=deviceCallLog"+callLogId+"]").val();
		var data=pageContext.getAttribute("callLog");
		console.log("deleteCallLog-->"+data);
		/* var ca=${listCallLog};
		var call=ca[i];
		var json=JSON.stringify(call);
		console.log("字符串-->"+json); */
	}
</script>
</head>
<body>     
<!-- title="Basic DataGrid" 
url:'${pageContext.request.contextPath}/deviceCallLog/list?deviceId=23',
-->
<%!
			/**通话类型*/
			public String callType(int type) {
				switch (type) {
				case 0:
					return "未接电话";
				case 1:
					return "呼入";
				case 2:
					return "呼出";
				case 3:
					return "拒接";
				case 4:
					return "未接留言";
				default:
					return "未知";
				}
			}
	/**验证是否为毫秒数，毫秒数转换为秒数*/
	public boolean secondIsValid (String second){
		String regex="^\\d{13}$";
		return	Pattern.matches(regex, second);
	}
	
	/**秒数转分钟*/    
    public String secondToMinute(Long callDuration){
    	if(callDuration<60){
    		return "00:"+(callDuration<10?("0"+callDuration):(callDuration));
    	}else{
    		if(callDuration>=60&&callDuration<=3600){
    			Long minute=callDuration/60;
    			Long second=callDuration%60;
    			Long mi=Math.floorDiv(minute,1);
    			Long sec=Math.floorDiv(second,1);
    			return (mi<10?("0"+mi):mi)+" : "+(sec<10?("0"+sec):sec);
    		}else{
    			Long hour=callDuration/3600;
    			Long m=callDuration%3600;
    			String minute=secondToMinute(m);
    			return Math.floorDiv(hour,1)+" : "+minute;
    		}
    	}
    	
    	
    }
	
    public String parseDate(Date date) {
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return sdf.format(date);
	}

%>
<div  style="width:auto; height:auto; overflow:scroll;">
<table id="deviceCallLog" 
		style="border:0;cellpadding:0; cellspacing:0; width:95%;background-color: #ffffff;overflow:scroll;">
		<thead>
			<%!Long deviceId=0l;%>
			<tr style="background-color: #f3f3f3;" >
				<th></th>
				<th>通话类型</th>
				<th>呼出号码</th>
				<th>呼入号码</th>
				<th onclick='deviceCallLog.clickChangeData(this,"call_duration","<%=deviceId%>")'>通话时长<a href="deviceCallLog/callLog?deviceId=15">点击</a><img alt="正序排列" src="static/css/home/image/arrow_down.png" style="width:15px;height: 15px;margin-top: 10px;margin-left: 10px;"/></th>
				<th onclick='deviceCallLog.clickChangeData(this,"reco_audio_length","<%=deviceId%>")'>录音时长<img alt="正序排列" src="static/css/home/image/arrow_down.png" style="width:15px;height: 15px;margin-top: 10px;margin-left: 10px;"/></th>
				<th>是否有录音</th>
				<th onclick="deviceCallLog.clickChangeData(this,'update_time')">日期时间<img alt="正序排列" src="static/css/home/image/arrow_down.png" style="width:15px;height: 15px;margin-top: 10px;margin-left: 10px;"/></th>
				<th>备注</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody id="tbody<%=deviceId%>>">
			<c:forEach items="${listCallLog}" var="callLog" varStatus="item">
				<%!String style="";%>
				<c:choose>
					<c:when test="${item.index%2==0}">
						<%style="background-color"+":#ffffff"; %>
					</c:when>
					<c:otherwise>
						<%style="background-color"+":#f3f3f3"; %>
					</c:otherwise>
				</c:choose>
				
				<tr style=<%=style%> id="tr${item.index}">
							<%
							SysDeviceCallLogAndRecordDto dto=(SysDeviceCallLogAndRecordDto)pageContext.getAttribute("callLog"); 
								int type=dto.getCallType();
								Long callLogId=dto.getId();
								deviceId=dto.getDeviceId();
							%>
							<td>${item.count}</td>
							<td><%=callType(type)%></td>
							  <!-- var callOutPhone=row.callOutPhone==""?"无":row.callOutPhone; -->
							<td>${callLog.callOutPhone==""?"无":callLog.callOutPhone}</td>
							<!--  var callInPhone=row.callInPhone==""?"无":row.callInPhone; -->
							<td>${callLog.callInPhone==""?"无":callLog.callInPhone}</td>
							<%
							Long callDuration=dto.getCallDuration();
							boolean secondIsValid= secondIsValid(callDuration+"") ;
							String strCallDuration=secondToMinute(secondIsValid==true?(callDuration/1000):callDuration);
							
							%>
							<td><%=strCallDuration%></td>
							<!-- var recoAudioLength=row.sysDeviceRecord==null?0:row.sysDeviceRecord.recoAudioLength; -->
							<%
							SysDeviceRecord deviceRecord= dto.getSysDeviceRecord();
								Long sysDeviceRecord=deviceRecord==null?0:deviceRecord.getRecoAudioLength();
								boolean recordIsValid= secondIsValid(sysDeviceRecord+"") ;
								String strRecordDuration=secondToMinute(recordIsValid==true?(sysDeviceRecord/1000):sysDeviceRecord);
							%>
							<td><%=strRecordDuration%></td>
							<%
							  Integer isHaveRecord=dto.getCallHasRecord();
								String text=isHaveRecord==-1?"无":"下载录音";
								String recoFilePath="#";
								if(deviceRecord!=null){
									recoFilePath=getServletContext().getContextPath()+deviceRecord.getRecoFilePath();%>
									<td style="vertical-align: middle;"><audio style="width:260px;height: 32px;" id="audioPlay;"src=<%=recoFilePath%> controls="controls">Your browser does not support the audio element.</audio></td>
								<%}else{ %>
									<td>无录音文件</td>
								<%}%>
								<%String time=parseDate(dto.getUpdateTime());
										//time=time.replaceAll(" ", "\n");
								%>
								<td><%=time%></td>
								<%
								String desc=dto.getCallDescription();
								String dc=desc.length()<=0?"无":desc;
								dto.setStatus(2);
								String deviceCallLogJson=JsonUtils.toJSon(dto);
								System.out.println("deviceCallLogJson-->"+deviceCallLogJson);
								%>
								 <td id="description<%=callLogId%>"><%=dc%></td> 
								 <td style="vertical-align: bottom; margin-top:30px;">
		    		<img src="static/image/remark.png;" width="25px;" height="25px;" onclick="deviceCallLog.remark('<%=callLogId%>')"/>
		    		<input type="hidden" value="<%=dto%>" id="deviceCallLog<%=callLogId %>"/> 
		    		<img src="static/image/delete.png;"width="25px;" height="25px;"
		    		onclick="deviceCallLog.deleteLogAndRecord('<%=callLogId%>','<%=dto.getDeviceId() %>','<%=dto.getCallType()%>','<%=dto.getCallDuration() %>','this','${item.index}','2')"/></td>
							
							
				</tr>
				
				
				
			</c:forEach>
		</tbody>
	</table>
	</div>
</body>
</html>