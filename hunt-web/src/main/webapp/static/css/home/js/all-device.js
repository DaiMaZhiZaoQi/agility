/**	弃用
 *	加载所有设备 
 */
var preDeviceId="";
device={
		/**搜索全部*/
		searchAll:function(searchContent){
//			var roleOrgId=common_tool.getHomeRoleOrgId();
			var roleOrgId=common_tool.get_orgPersonId();
			searchContent=searchContent.trim();
			var parametes=roleOrgId+"&isLoadAll=0&searchContent="+searchContent+"&searchType=0&userId="+common_tool.getCurrUserId();
			var href=getRootPath()+"/device/allDevice?"+parametes;
			console.log("searchAll-->"+href);
			var text=$(".tree-title").text();
//			 $(".main-container").load(href,function(){		//加载成功后回调
			$("#div_device_state_child").load(href,function(){		//加载成功后回调
				 var ttId=$("#tt").tree('getSelected');
				 $("#td_allDevice_title").text(ttId.name);
				 $("#searchContainer hr[id=hr_alldevice]").css("visibility","visible");
			 });
		},
		
		searchOnLine:function(){
//			var roleOrgId=common_tool.getHomeRoleOrgId();
			var roleOrgId=common_tool.get_orgPersonId();
			var parametes=roleOrgId+"&isLoadAll=0&searchContent=searchContent&searchType=1&userId="+common_tool.getCurrUserId();
			var href=getRootPath()+"/device/allDevice?"+parametes;
			var text=$(".tree-title").text();
//			 $(".main-container").load(href,function(){		//加载成功后回调
				 $("#div_device_state_child").load(href,function(){		//加载成功后回调
					 $("#searchContainer hr[id=hr_online]").css("visibility","visible");
				 var ttId=$("#tt").tree('getSelected');
				 $("#td_allDevice_title").text(ttId.name);
//				 console.log("titleNAME-->"+ttId.name);
			 });
		},
		
		searchOFFline:function(){
			var roleOrgId=common_tool.get_orgPersonId();
			var parametes=roleOrgId+"&isLoadAll=0&searchContent=searchContent&searchType=2&userId="+common_tool.getCurrUserId();
			var href=getRootPath()+"/device/allDevice?"+parametes;
//			 $(".main-container").load(href,function(){		//加载成功后回调
				 $("#div_device_state_child").load(href,function(){		//加载成功后回调
					 $("#searchContainer hr[id=hr_outline]").css("visibility","visible");
				 var ttId=$("#tt").tree('getSelected');
				 $("#td_allDevice_title").text(ttId.name);
			 });
		},
		
		
		remoManage:function(url){
			var remoUrl=url.split(".");
			url="http://"+url+":8080";
			if(remoUrl.length==4){
				window.open(url);
			}else{
				alert("地址错误");
			}
		},
		
		load:function (id,obj){//deviceCallLog/callLog
//		    $('#contoner'+id+' div').load(getRootPath()+"/WEB-INF/jsp/home/deviceCallLog.jsp");
//		    $('#contoner'+id+' div').html(getRootPath()+"/deviceCallLog.jsp");
			
			console.log("loadfun-->"+preDeviceId+"--->"+id);
			if(preDeviceId!=id){
					$("#contoner"+preDeviceId+" div").slideUp(300,function(){});
					$("#contoner"+preDeviceId+" div").children().remove();
			}
			$('#div_alldevice_head input[id="deviceId"]').val(id);
			if($("#contoner"+id+" div:has(table)").length>0){
				if($("#contoner"+id+" div").is(":visible")){		//   显示
					$("#contoner"+id+" div").slideUp(500,function(){});
				}else{	
					
					$("#contoner"+id+" div").slideDown(300,function(){});
				}
			}else{
				$('#contoner'+id+' div').load("deviceCallLog.html",function(){
					$("#contoner"+id+" div").slideDown(500,function(){});
				});
			}
			preDeviceId=id;
		},

		showRecord:function(sysDevice){
			alert("录音操作"+sysDevice);
		},
		
		loadAppend:function(id){
			console.log("loadAppend-->"+id);
			var trNode=$("#tr"+id);
			console.log(trNode.prop("nodeName"));
//			$("#tr"+id).after("<tr><td>张三</td></tr>");
			$("#tr"+id).after("<tr>"+
							"<td>" +
								"<div style='margin-top:30px;background-color:#ffffff;with:100%;' id='contoner"+id+"'>"+
									"<div></div>"+
								"</div>" +
//							"zhangsan"+
							"</td>"+
						"</tr>");
			
			
//			device.load(id);
		},
	
		
		loadRecord:function(deviceId){
			 $("#contonerDialog div").attr("id","record_container");
			 console.log("loadRecord--->"+deviceId);
			$("#recordDialog").dialog({
				title:'通话记录',
				 iconCls: 'icon-save',
		         closable: true,
		         width: 1200,
		         height: 700,
		         cache: false,
		         modal: true,
		         resizable: false,
		         'onOpen':function(){
		        	 $("#div_device_state_one div[id=recordDialog]").remove();
//		        	 $("#contonerDialog div").attr("id",deviceId);
		        	 console.log("callInit--->"+deviceId);
		        	 var i=0;
		        	 $("#contonerDialog div").load("deviceCallLog.html",function(){
//							$("#contoner"+1+" div").slideDown(500,function(){});
		        		 if(i==0){
		        			 deviceCallLog. callInit(deviceId);
		        		 }
		        		 i++;
						});
		 		
		         },
		         'onClose':function(){
		        	 console.log("关闭");
		        	 $("#deviceCallLogDevice").remove();
//		        	 $("#record_container").children().remove();
		         },
		         'buttons':[
		        	   {
		                    text: '关闭',
		                    width: 100,
		                    iconCls: 'icon-add',
		                    handler: function () {
		                    	console.log("handler-->");
		                        $("#recordDialog").dialog("close");
		                    }
		                }
		         ]
			});
			var recordDg=$(".panel div[id=recordDialog]");
			$(".panel div[id=recordDialog]").dialog("center");

		},
		
		optionBgColor:function(tag){
			$("#searchContainer hr").css("visibility","hidden");
		},
}

$(document).ready(function(){
	$("#searchContainer input[name=allDevice]").click(function() {  // #6dcdf3
		var searchContent=$("#searchContainer input[type=text]").val();
		device.optionBgColor();
		device.searchAll(searchContent);
		
	});
	
	$("#searchContainer input[name=alldevice_online]").click(function(){
		device.optionBgColor();
		device.searchOnLine();

	});
	
	$("#searchContainer input[name=alldevice_outline]").click(function(){
		device.optionBgColor();
		device.searchOFFline();
		
	});
	
	var gloPer=common_tool.getGloPer();
	console.log("gloPer-->"+JSON.stringify(gloPer));
	if(gloPer.indexOf("user:manage")>-1){
		$(".userManage").show();
	}
	
	if(gloPer.indexOf("device:manage")>-1){
		$(".deviceManage").show();
	}
	
	if(gloPer.indexOf("callLog:select")>-1){
		$(".callLogSelect").show();
	}
//	if(gloPer.indexOf("contact:insert")>-1){
//		$(".contactInsert").show();
//	}
//	if(gloPer.indexOf("contact:select")>-1){
//		$(".contactSelect").show();
//	}
//	if(gloPer.indexOf("contact:delete")>-1){
//		$(".contactDelete").show();
//	}
	
	
	
//	glpPer.forEach(function(item,index) {
//		
//	});
});


