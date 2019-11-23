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
			var parametes=roleOrgId+"&isLoadAll=0&searchContent="+searchContent+"&searchType=0";
			var href=getRootPath()+"/device/allDevice?"+parametes;
			console.log("searchAll-->"+href);
			var text=$(".tree-title").text();
			 $(".main-container").load(href,function(){		//加载成功后回调
				 var ttId=$("#tt").tree('getSelected');
				 $("#td_allDevice_title").text(ttId.name);
			 });
		},
		
		searchOnLine:function(){
//			var roleOrgId=common_tool.getHomeRoleOrgId();
			var roleOrgId=common_tool.get_orgPersonId();
			var parametes=roleOrgId+"&isLoadAll=0&searchContent=searchContent&searchType=1";
			var href=getRootPath()+"/device/allDevice?"+parametes;
			var text=$(".tree-title").text();
			 $(".main-container").load(href,function(){		//加载成功后回调
				 var ttId=$("#tt").tree('getSelected');
				 $("#td_allDevice_title").text(ttId.name);
//				 console.log("titleNAME-->"+ttId.name);
			 });
		},
		
		searchOFFline:function(){
//			var roleOrgId=common_tool.getHomeRoleOrgId();
			var roleOrgId=common_tool.get_orgPersonId();
			var parametes=roleOrgId+"&isLoadAll=0&searchContent=searchContent&searchType=2";
			var href=getRootPath()+"/device/allDevice?"+parametes;
//			var text=$(".tree-title").text();
			 $(".main-container").load(href,function(){		//加载成功后回调
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
				}else{		//
					
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
			 $('#contoner'+1+' div').attr("id",deviceId);
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
		        	 $('#contoner'+1+' div').attr("id",deviceId);
		        	 console.log("callInit--->");
		        	 var i=0;
		        	 $('#contoner'+1+' div').load("deviceCallLog.html",function(){
//							$("#contoner"+1+" div").slideDown(500,function(){});
		        		 if(i==0){
		        			 deviceCallLog. callInit();
		        		 }
		        		 i++;
						});
		 		
		         },
		         'onClose':function(){
		        	 console.log("关闭");
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
		showdialog:function(deviceId){
			$('#contoner'+1+' div').attr("id",deviceId);
			console.log("loadRecord--->"+deviceId);
			$("#recordDialog").dialog({
				title:'通话记录',
				iconCls: 'icon-save',
				closable: true,
				width: 1200,
				height: 500,
				cache: false,
				modal: true,
				resizable: false,
				'onOpen':function(){
					$('#contoner'+1+' div').attr("id",deviceId);
					console.log("callInit--->");
					var i=0;
					$('#contoner'+1+' div').load("deviceCallLog.html",function(){
//							$("#contoner"+1+" div").slideDown(500,function(){});
						if(i==0){
							deviceCallLog. callInit();
						}
						i++;
					});
					
				},
				'onClose':function(){
					console.log("关闭");
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
		}
}

$(document).ready(function(){
//	$("#showDialog").click(function(){
//		device.showdialog();
//	});
//	$("#testtest7").click(function(){
//		var deviceId="7";
//		 $('#contoner'+1+' div').attr("id",deviceId);
//	     
//			$("#recordDialog").dialog({
//				title:'通话记录',
//				 iconCls: 'icon-save',
//		         closable: true,
//		         width: 1200,
//		         height: 500,
//		         cache: false,
//		         modal: true,
//		         resizable: false,
//		         'onOpen':function(){
////		        	 device.load(1, "obj");
//		        	 $('#contoner'+1+' div').attr("id",deviceId);
//		        	 $('#contoner'+1+' div').load("deviceCallLog.html",function(){
////							$("#contoner"+1+" div").slideDown(500,function(){});
//		        		 deviceCallLog. callInit();
//						});
//		        	 
//		         },
//		         'onClose':function(){
//		        	 console.log("关闭");
//		         },
//		         buttons:[
//		        	   {
//		                    text: '关闭',
//		                    width: 100,
//		                    iconCls: 'icon-add',
//		                    handler: function () {
//		                        $("#recordDialog").window("close");
//		                    }
//		                }
//		         ]
//			});
//	});
	$("#searchContainer input[name=allDevice]").click(function() {
		var searchContent=$("#searchContainer input[type=text]").val();
		if(searchContent.length>0){
			device.searchAll(searchContent);
		}else{
			alert("请输入搜索内容");
		}
	});
	
	$("#searchContainer input[name=alldevice_online]").click(function(){
		device.searchOnLine();
	});
	
	$("#searchContainer input[name=alldevice_outline]").click(function(){
		device.searchOFFline();
	});
});


