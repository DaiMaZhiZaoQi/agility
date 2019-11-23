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

/* 	 .mod_select{font-familY:Arial, Helvetica, sans-serif; display: inline-block;}
    .mod_select ul{margin:0;padding:0;}
    .mod_select ul li{list-style-type:none;float:left;margin-left:20px;height:24px;}
    .select_label{color:#982F4D;float:left;line-height:24px;padding-right:10px;font-size:12px;font-weight:700;}
    .select_box{float:left;border:solid 1px #828180;height: 30px;color:#444;position:relative;cursor:pointer;width: 125px;font-size:12px;}
    .selet_open{display:inline-block;border-left:solid 1px #E5E5E5;position:absolute;right:0;top:0;width:30px;height:24px;}
    .select_txt{display:inline-block;padding-left:10px;font-size: 18px;width: 125px;line-height: 30px;height: 30px;cursor:text;overflow:hidden;}
    .option{width:125px;;border:solid 1px #EDE7D6;position:absolute;top: 30px;left:-1px;z-index:2;overflow:hidden;display:none;}
    .option a{display:block;height:26px;line-height:26px;text-align:left;padding:0 10px;width:100%;background:#fff;padding-top: 5px;padding-bottom: 5px;}
    .option a:hover{background:#65b7f4;}
     */
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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/home/csxAudio/csxAudio.css"/>
<script type="text/javascript">
/* 	var searchWidth=$("#btn_blue_search").css("width").replace("px","");
	
	var searchHeight=$("#btn_blue_search").css("height").replace("px","");
	$("#btn_blue_search").mousemove(function(e){
		var x=e.offsetX;
		var y=e.offsetY;
		if(x>5&&y>5){
			var dx=searchWidth-x;
			var dy=searchHeight-y;
			if(dy>5&&dx>5){
				$("#tableCallLogSear ul[id=ulTime]").css("display", "unset");
			}
		}
		
	});
	$("#btn_blue_search").mouseout(function(){
		$("#tableCallLogSear ul[id=ulTime]").css("display", "none");
	}); */
	
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
	
  /*    $("#tableCallLogSear a[id=a_defin_time]").click(function(){
    	 $("#tableCallLogSear input[id=input_search]").attr("type","date");
    	   
         var time=comGetTime();
         $("#tableCallLogSear input[id=input_search]").val(time);
     }); */
     
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
	var bgTime=getFirstDayOfWeek();
	laydate.render({
		  elem: "#timeContainer input[id=beginTime]", 
		  type: 'datetime',
		  // format:"2019/10/24 18:58",
		  isInitValue: true,
		  value:new Date(getTimeStamp(bgTime)),
		});

	var edTime=getCurrTime();
	console.log("初始化加载时间-->"+edTime);
	laydate.render({
		  elem: "#timeContainer input[id=endTime]", 
		  type: 'datetime',
		  // format:"2019/10/24 18:58",
		  isInitValue: true,
		  value: new Date(getTimeStamp(edTime)),
		});
	
	
</script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/csxAudio/csxAudio.js"></script>
	
<script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/devCallRecordModule.js"></script>
</head>
<body>
	<div id="div_container_callRecord">
		 <div id="div_head_callRecord">
			<table style="display:inline-block;">
				<tr>
					<td rowspan="3">
						<img alt="所有通话记录"  class="deviceHeadImg"  src="${pageContext.request.contextPath}/static/css/home/image/record-gray.png">
						
					</td>	
					<td id="td_allDevice_title" style="padding-left: 20px; font-size: 24px;">
						<span id="span_title">通话记录</span>
					<!-- <div class="mod_select" id="divYearSelect" style="display: none;">
							    <ul style="display: none;">
							        <li>
							            <div class="select_box">
							                <span class="select_txt" name="2">近七天记录</span>
							                <a class="selet_open">
							               		 <img alt="正序排列" src="static/css/home/image/arrow_down.png" style="width:15px;height: 15px;margin-top: 7px;margin-left: 10px;">
							                <b></b></a>
							                <div class="option">
							                    <a name="1">近三天记录</a>
							                    <a name="2">近七天记录</a>
							                    <a name="3">近半月记录</a>
							                    <a name="4">近一月记录</a>
							                    <a name="5">近三月记录</a>
							                    <a name="6">近半年记录</a>
							                    <a name="7">今年内记录</a>
							                    <a id="a_preyear" name="8"></a>
							                    <a id="a_defin_time">自定义时间</a>
							                </div>
							            </div>
							            <br clear="all" />
							        </li>
							    </ul>
   					 			<input type="hidden" id="select_value" />
							</div> -->
						<!-- 	<span>
								<img alt="刷新" src="">
							</span> -->
					</td>
				</tr>
				
				<tr><td></td></tr>
				<tr>
					<td id="head_callRecord_child" style="padding-left: 20px; color: #8A8A8A;font-size: 16px;"></td>
				</tr>
			</table>
			
			<table align="right" style="margin-right: 60px;  " id="tableCallLogSear">
		    		<tr>
		    			<td rowspan="1"  >
				    		<input type="text" id="input_search" class="search" style="width: 360px;    background-position: 335px;" placeholder="号码/联系人/设备号"  >	
		    			</td>
		    			<td colspan="1"> 
							<!--<input type="button" name="allDevice" id="btn_blue_alldevice" class="alldevice" value="全部"> -->
							<a name="allDevice" >
								<button class="alldevice" style="background: #007ad5;margin-left: 0px;"id="btn_blue_search">查找</button>
							</a>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td style="padding-top: 10px;">
		    				<ul style="display:inline; padding-left: 0px;margin-top: 5px;">
		    						<li class="radio" style="display: inline; font-size: 20px;margin-right: 0px;">
		    							<input id="searNum" type="radio" name="radio" style="width: 18px;
										    height: 18px;
										    margin: 0px;
										    padding: 0px;" value="0" checked="checked">
		    						 	<label for="searNum" class="radio-label">查询号码</label>	
									</li> 
		    						<li class="radio" style="display: inline; font-size: 20px;margin-right: 0px;">
			    						<input id="searPerson" type="radio" name="radio" style="width: 18px;
										    height: 18px;
										    margin: 0px;
										    padding: 0px;" value="1">
			    						<label for="searPerson" class="radio-label">查询联系人</label> 
		    						</li>
		    						<li class="radio" style="display: inline;font-size: 20px;margin-right: 0px;">
			    						<input id="searSer" type="radio" name="radio" style="width: 18px;
										    height: 18px;
										    margin: 0px;
										    padding: 0px;"value="2">
		    							<label for="searSer" class="radio-label">查询序列号</label> 
		    						</li>
		    				</ul>
		    			</td>
		    		</tr>
		    		
		    		<tr>
		    	
		    		</tr>
		    		
		    	</table>
		    	<div style="margin-left: 6px;" class="clearfix" id="timeContainer">
		    		<div style="float: left;    padding-top: 9px;">
						<!-- <input id="beginTime" type="datetime-local" class="inputDate"  value=""> -->
						<input type="text" class="layui-input inputDate" id="beginTime" placeholder="yyyy-MM-dd HH:mm:ss">
						<span style="font-size: 26px;">到</span>
						<!-- <input id="endTime" type="datetime-local" class="inputDate" value="" > -->
						<input type="text" class="layui-input inputDate" id="endTime" placeholder="yyyy-MM-dd HH:mm:ss">
						<button class="alldevice" style="height: 40px;" id="confirmTime">查找</button>
		    		</div>
					
					<div  style="text-align: right;
							    margin-right: 50px;
							     position: absolute;
							    right: 30px;
							    margin-top: 5px;
							   float: right;">
						<input type="checkbox" style="width: 22px;height: 22px;float: left;margin-top: 4px;" id="cbHaveRecord">
						<label for="cbHaveRecord" style="font-size: 22px;margin-left:3px;margin-right:5px;
						    color: #0092ff;">只查录音</label>
					</div>
				</div>
				<div style="clear: both"></div>
			<hr style="height:0.05px; background-color:	#CCCCCC; margin-right: 30px;"/>
		 </div>	
		
	</div>


</body>
</html>