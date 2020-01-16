<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.util.*" %>
<%@page import="com.hunt.model.dto.SysDeviceAndCallDto" %>
<%@page import="com.hunt.model.dto.PageInfo" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/static/laydate/laydate.js"></script> --%>


<style type="text/css">
	.inputDate{
		    width: 220px;
		    height: 30px;
		    font-size: 18px;
		    padding-left: 5px;
		    margin-left: 5px;
	}
		.radio {
		  margin: 0.5rem;
		}
		.radio input[type="radio"] {
		  position: absolute;
		  opacity: 0;
		}
		.radio input[type="radio"] + .radio-label:before {
		  content: '';
		  background: #f4f4f4;
		  border-radius: 100%;
		  border: 1px solid #b4b4b4;
		  display: inline-block;
		  width: 1.4em;
		  height: 1.4em;
		  position: relative;
		  top: -0.2em;
		  margin-right: 2px;
		  vertical-align: top;
		  cursor: pointer;
		  text-align: center;
		  -webkit-transition: all 250ms ease;
		  transition: all 250ms ease;
		}
		.radio input[type="radio"]:checked + .radio-label:before {
		  background-color: #3197EE;
		  box-shadow: inset 0 0 0 4px #f4f4f4;
		}
		.radio input[type="radio"]:focus + .radio-label:before {
		  outline: none;
		  border-color: #3197EE;
		}
		.radio input[type="radio"]:disabled + .radio-label:before {
		  box-shadow: inset 0 0 0 4px #f4f4f4;
		  border-color: #b4b4b4;
		  background: #b4b4b4;
		}
		.radio input[type="radio"] + .radio-label:empty:before {
		  margin-right: 0;
		}
</style>
<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/home/csxAudio/csxAudio.css"/> --%>
<script type="text/javascript">
	
	 $(document).ready(function(){
    
         /*赋值给文本框*/
         $(".option a").click(function(){
             var value=$(this).text();
             var name=$(this).attr("name");
             $(this).parent().siblings(".select_txt").text(value);
             $("#select_value").val(name)
         	$("#divYearSelect div[class=option]").hide();   
             recordCommon.selectCallDate($("#select_value").val());
          })
          $("#divYearSelect a[id=a_preyear]").text(getYear(1)+"记录");
         
     })
	
 
	$("#divYearSelect div[class=select_box]").mouseenter(function(e){
	    event.stopPropagation();
        $(this).find(".option").toggle();
        $(this).parent().siblings().find(".option").hide();
	});
	
	$("#divYearSelect div[class=select_box]").mouseleave(function(e){
        $("#divYearSelect div[class=option]").hide();                                      
	});
	
	function getYear(obj){
		var date=new Date();
		return date.getFullYear()-obj;
	}
	  var bgTime=null;
	var seBeginTime=common_tool.get_beginTime();
	if(seBeginTime!=null&&seBeginTime.length>0){
		bgTime=seBeginTime;
		console.log("setBeginTime-->"+seBeginTime+"-bgTime-->"+bgTime);
		laydate.render({
			  elem: "#timeContainer input[id=beginTime]", 
			  type: 'datetime',
			  isInitValue: true,
			   value:new Date(getTimeStamp(bgTime)),
			});
	 
	}else{
	//	bgTime=getFirstDayOfWeek();
		laydate.render({
			  elem: "#timeContainer input[id=beginTime]", 
			  type: 'datetime',
			  isInitValue: true,
		});
	}
	
	var edTime=getCurrTime();
	console.log("初始化加载时间-->"+edTime);
	laydate.render({
		  elem: "#timeContainer input[id=endTime]", 
		  type: 'datetime',
		  isInitValue: true,
		  value: new Date(getTimeStamp(edTime)),
		});
	
	
</script>
	
	<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/csxAudio/csxAudio.js"></script> --%>
	
<script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/devCallRecordModule.js"></script>
</head>
<body>
	<div id="div_container_callRecord">
		 <div id="div_head_callRecord" class="callLogSelect" style="max-width: 2000px;min-width: 1016px;height: 120px;line-height: 25px;">
			<table style="display:inline-block;">
				<tr>
					<td rowspan="3">
						<img alt="所有通话记录"  class="deviceHeadImg"  src="${pageContext.request.contextPath}/static/css/home/image/record-gray.png">
						
					</td>	
					<td id="td_allDevice_title" style="padding-left: 20px; font-size: 24px;">
						<span id="span_title">通话记录</span>
					</td>
				</tr>
				
				<tr><td></td></tr>
				<tr>
					<td id="head_callRecord_child" style="padding-left: 20px; color: #8A8A8A;font-size: 16px;"></td>
				</tr>
			</table>
		    	<table align="right" style="margin-right: 60px; " cellpadding="8" id="searchTable">
		    		<tr class="titleChild">
		    			<td>通话号码:</td>
		    			<td style="padding: 1px;position: relative;">
		    				<input style="height: 30px; width:200px;padding-left: 5px; font-size: 18px;" data-options="buttonIcon:'${pageContext.request.contextPath}/static/css/home/icons/cancel.png'" class="easyui-textbox" type="text" name="number"></input>
		    				<img alt="删除" src="${pageContext.request.contextPath}/static/css/icons/cancel.png" id="deleteCallNum" style="position: absolute;right: 10px;top: 12px;display: none;">
		    			</td>
		    			<td align="right">部门:</td>
		    			<td style="padding: 1px;position: relative;">
		    				<input style="height: 30px;width:200px; padding-left: 5px;font-size: 18px;" class="easyui-textbox"; type="text"; name="orgName";></input>
		    				<img alt="删除" src="${pageContext.request.contextPath}/static/css/icons/cancel.png" id="deleteOrg" style="position: absolute;right: 10px;top: 12px;display: none;">
		    			</td>
		    			
		    		</tr>
		    	
		    		<tr class="titleChild" id="timeContainer">
			    				<td>开始时间:</td>
								<td style="padding: 1px;"><input type="text" class="layui-input inputDate" style=" width: 200px;margin-left: 0px;" id="beginTime" placeholder="选择开始时间"></td>
								<!-- <span style="font-size: 26px;">到</span> -->
								<td>结束时间:</td>
								<td style="padding: 1px;"><input type="text" class="layui-input inputDate" style=" width: 200px;margin-left: 0px;" id="endTime" placeholder="选择结束时间"></td>
		    			
		    		</tr>
		    		<tr class="titleChild">
		    			<td colspan="2" align="right" style="cursor: pointer;">
		    				<input type="checkbox" checked="checked" style="width: 22px;height: 22px;cursor: pointer;margin: 0px;vertical-align: middle;" id="cbHaveRecord">
							<label for="cbHaveRecord" style="font-size: 20px;vertical-align: middle;cursor: pointer;
						    color: #0092ff;">只查录音</label>
		    			</td>
		    			
						<td colspan="2" align="right"><button class="alldevice" style="height: 40px;margin-left: 10px;" id="confirmTime">查找</button></td>
		    		</tr>
		    
		    	</table>
		
				<div style="clear: both"></div>
			<hr style="height:0.05px; background-color:	#CCCCCC; margin-right: 30px;"/>
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
</html>