/**
 * 加载设备列表
 */
(function(){
	var comSpace=window.NameSpace||{};
	comSpace.data={
			currDisplay:"",
			titleName:"",
	};
	
	var deviceOrgId="";
	/**当前点击的按钮*/
	var clickObj=null;
	/**当前选择的机构id*/
	var currHref=null;
	
	/**全局id*/
	var glolbalId="";
	/**全局操作类型 0,查询机构类型。1，查询个人类型*/
	var globOptType="";
	
	homeDevic={
			init:function (){
		        $(".tree-icon,.tree-file").removeClass("tree-icon tree-file");
		        $(".tree-icon,.tree-folder").removeClass("tree-icon tree-folder tree-folder-open tree-folder-closed"); 
		    },
		    
		    load:function(href){
		    	$('.device-item').datalist({
		    	    url:href,
		    	    fit:true,
		    	    plain:true,
		    	    textFormatter:function(value,row,index){
		    	    	console.log("value-->"+value);
		    	    },
	//	    	    columns:[[
	//	    			{field:'code',title:'Code',width:100},
	//	    			{field:'name',title:'Name',width:100},
	//	    			{field:'price',title:'Price',width:100,align:'right'}
	//	    	    ]]
		    	});
		    },
		    
		    
	  			
		    /**
		     * 加载该角色下的所有设备
		     */
		    loadAllDevice:function(){
		    	
		    },
		    
		    /**菜单栏点击，获取设备状态*/
		    menuDeviceStateClick:function(){
		    	if(currHref!=null){
		    		$("#div_device_manage_state div[id=div_device_state_child]").load(currHref,function(){ 	//加载失败时的处理，性能可以优化，放easyui tabs标签的使用
//		    			 $("#searchContainer hr[id='hr_alldevice']").css("visibility","visible");
		    		});	//加载失败时的处理，性能可以优化，放easyui tabs标签的使用
		    	}
		    	var clickDeviceState=$("#menu ul[id=iDeviceState]");
		    	homeDevic.setCssDefault(clickDeviceState);
		    	homeDevic.displayManage("div_device_state");
		    	comSpace.data.currDisplay="";
		    },
		    
		    /**显示管理*/
		    displayManage:function(objId){
		    	var childrens=$("#div_home_content").children();
		    	for(var i=0;i<childrens.length;i++){
		    		var childId=childrens[i].id;
		    		var childHidden=$("#div_home_content div[id="+childId+"]").is(":hidden");
		    		if(!childHidden){
		    			if(objId!=undefined&&objId!=childId){
		    				$("#div_home_content div[id="+childId+"]").hide();
		    				
		    			}
		    		}
		    	}
		    	console.log("没有audio控件");
		    	try {
		    		var audioStop=$(".audiosx audio[id=audioPlay]");
		    		console.log("audioStop-->"+audioStop.length);
		    		for(var i=0;i<audioStop.length;i++){
		    			audioStop[i].pause();
		    		}
				} catch (e) {
					console.log("没有audio控件");
				}
		    	for(var i=0;i<childrens.length;i++){
		    		var childId=childrens[i].id;
		    		var childHidden=$("#div_home_content div[id="+childId+"]").is(":hidden");
		    		if(childHidden){
		    			if(objId!=undefined&&objId==childId){
		    				$("#div_home_content div[id="+objId+"]").show();
		    			}
		    		}
		    	}
		    },
		    
		    /**设备状态管理*/
		    displayStateManage:function(objId){
		    	$("#div_device_manage_state div[id=abcTemp]").show();
		    	var childrens=$("#div_device_statechild").children();
		    	for(var i=0;i<childrens.length;i++){
		    		var childId=childrens[i].id;
		    		if(childId=="abcTemp"){
		    			continue;  
		    		}
		    		var childHidden=$("#div_device_manage_state div[id="+childId+"]").is(":hidden");
		    		if(!childHidden){
		    			if(objId!=undefined&&objId!=childId){
		    				$("#div_device_manage_state div[id="+childId+"]").hide();
		    			}
		    		}
		    	}
		    	
		    	for(var i=0;i<childrens.length;i++){
		    		var childId=childrens[i].id;
		    		if(childId=="abcTemp"){
		    			continue;  
		    		}
		    		var childHidden=$("#div_device_manage_state div[id="+childId+"]").is(":hidden");
		    		if(childHidden){
		    			if(objId!=undefined&&objId==childId){
		    				$("#div_device_manage_state div[id="+objId+"]").show();
		    			}
		    		}
		    	}
		    },
		    
		    /**
		     *	加载所有通话记录 
		     */
		    loadAllRecord:function(){
		    	var href=getRootPath()+"/callRecord/callRecord";
		    	console.log("正在加载中");
		    	$("#div_device_manage_state div[id=div_call_record]").load(href);
	//	    	$(".main-container").html(href);
	//	    	$(".main-container").html("<iframe scrolling='true' frameborder='0'  src="+href +" style='width:100%;height:100%;'></iframe>");
		    
		    },
		    
		    /**
		     * 加载设备管理
		     */
		    loadDeviceManage:function(){
		    	var href=getRootPath()+"/devManager/devManager";
		    	$("#div_device_manage").load(href);
		    },
		    	
		    /**
		     * 加载任务组
		     */
		    loadMyTask:function(){
		    	var href=getRootPath()+"/task/myTask";
		    	$("#div_myTask").load(href);
		    },
		    
		    
		    /**
		     * 加载设备管理
		     */
		    loadContactManage:function(){
		    	var href=getRootPath()+"/contact/contact";
		    	$("#div_contact_manage").load(href);
		    },
		    
		    
	
		    setCssDefault:function(node){
		    	node.css("background","#49a1e2");
		    	if(clickObj!=null&&clickObj.attr("id")!=node.attr("id")){
//		    		alert("clickObj-->"+ toString.call(clickObj)+"--->"+toString.call(node));
//		    		alert("clickObj-->"+ clickObj.constructor+"--->"+clickObj.constructor);
//		    		alert("clickObj-->"+ clickObj.attr("id")+"--->"+node.constructor);
		    		clickObj.css("background","transparent");
		    	}
		    	clickObj=node;
		    },
		    
		    getGlobalId:function(){
		    	return glolbalId;
		    },
		    
		    getGloOptType:function(){
		    	return globOptType;
		    },
		    
		    /**
		     * 修改密码
		     */
		    update_password: function (id) {
		    	let firstPass=$("#modify_password_form input[id=nPassword]").val();
            	let secondPass=$("#modify_password_form input[id=rNewPassword]").val();
		        $.ajax({
		            data: {
		                id: id,
		                newPassword: firstPass,
		                repeatNewPassword: secondPass,
		            },
		            traditional: true,
		            method: 'post',
		            url: getRootPath() + '/user/updatePassword',
		            async: false,
		            dataType: 'json',
		            success: function (result) {
		                if (result.code == 10000) {
		                	common_tool.messager_show(result.msg);
		                    $("#dialogModifyPass").dialog('close');
//		                    user_tool.init_main_view();
//		                    $("#user_grid").datagrid("reload");
		                    window.location.href=getRootPath();
		                    return false;
		                }
		                else {
		                    common_tool.messager_show(result.msg);
		                }
		            },
		        });
		    },
	}
	
	
	
	$(document).ready(function(){
		
			homeDevic.menuDeviceStateClick();
			var clickId=0;
			var text=$(".sysOrgId").text();							  //  处理身兼多职的问题  
			var userId=common_tool.getCurrUserId();
			console.log("userId-->"+userId);
			console.log("text-->"+text);
			$("#tt").tree({
				method:"GET",
				id:"id",
				url:getRootPath() + '/organization/list?id='+userId+'&queryType=0',    //  不同id显示内容不同，在登录时注意接收不同的id
				loadFilter: function(data){
				
				 return data.rows;
		   	   },
				formatter:function(node){							  // 子节点加载成功后加载该部门下的成员设备
					return node.name;
				},
		  		onLoadSuccess:function(obj,data){						  // 加载成员设备
		  			homeDevic.init();
		  			common_tool.setHomeRoleOrgId(data[0].id);
		  			common_tool.set_orgPersonId("orgId="+data[0].id);
		  			console.log("onLoadSuccess-->"+data[0].id+"---userId-->"+userId);
		  			deviceOrgId=data[0].id;
		  			globOptType=0;
		  			glolbalId=deviceOrgId;
		  			var href=getRootPath()+"/device/allDevice?orgId="+data[0].id+"&isLoadAll=1&userId="+userId;
		  			currHref=href;
					 $("#div_device_state_child").load(href);
	//				 $(".main-container").html("<iframe scrolling='true' frameborder='0'  src="+href +" style='width:100%;height:100%;'></iframe>");
					 $("#tt li:eq(0)").find("#_easyui_tree_1").addClass("tree-node-selected");
					 
		  		},
		  		
		  		
				onClick:function(node){
					var id=node.id;
					common_tool.set_homeClickOrgId(id);
					glolbalId=id;
					var orgType=node.orgType;
					globOptType=orgType;
					var nodeName=node.name;
					comSpace.data.titleName=nodeName;
					console.log("node-->"+id+"node-->"+nodeName);
					if(clickId!=id){
						var selectStatus=common_tool.getHomeSelectStatus();
						
						var parametes="";
						var selected = $('#tt').tree('getSelected');
						if(orgType==0){					//  机构类型
							parametes="orgId="+id;
							if(selected.target.id=="_easyui_tree_1"){   // TODO  修改此处，点击即可全部更新
								parametes=parametes+"&isLoadAll=1";
							}
						}else if(orgType==1){			//   个人类型
							parametes="personalId="+id;
						}
						common_tool.set_orgPersonId(parametes);
						var href=getRootPath()+"/device/allDevice?"+parametes+"&userId="+userId;
						currHref=href;
						
						if(1==selectStatus){				// 刷新通话语音记录
							var optType=orgType==0?0:1;
							console.log("selectStatus--->"+optType+"--id-->"+id);
							recordModult.initSearContent();
							var result=recordModult.refreshCallLog(optType,id,"call_date","DESC",1,-1); // 刷新数据列表
							$("#td_allDevice_title span[id=span_title]").text(nodeName+"通话记录");
							 clickId=id;
							if(result==0){
								return
							}
							recordModult.refreshPage(optType,id);	// 刷新数据总数页面数量
							recordModult.refreshTotal(optType,id);	// 刷新通话数量总数							
						}else{								//	查询设备
							
							var text=$(".tree-title").text();
							 $("#div_device_state_child").load(href,function(){		//   加载成功后回调
								 $("#td_allDevice_title").text(nodeName);
							 });
							 clickId=id;
						}
						
					}
						
					
				}
			});
			
			$("#menu ul[id=iDeviceState]").click(function() {
				$("#idHomeTable a[id=aOnLineDevice]").attr("name","firstLoad");
				homeDevic.displayManage("div_device_state");
				homeDevic.displayStateManage("div_device_state_child");
//				var stateTree= $("#div_device_manage_state div[id=div_device_state_one]");
				var isHidden=$("#div_device_manage_state div[id=div_device_state_child]").is(":hidden");
				
		    	if(!isHidden){
		    		common_tool.setSelectStatus(0);
		    	}
//				if(stateTree.prop("nodeName")==undefined){
					homeDevic.menuDeviceStateClick();
//				}else{
					var clickDeviceState=$("#menu ul[id=iDeviceState]");
			    	homeDevic.setCssDefault(clickDeviceState);
//				}
			    	
			    var ttChecked=$("#div_device_manage_state ul[id=tt]").tree("getSelected");
			    console.log(ttChecked);
				 $("#td_allDevice_title").text(ttChecked.name);
			});
			
			/**
			 * 查看所有通话记录
			 */
			$("#menu ul[id=iCallRecord]").click(function() {  
				console.log("点击事件执行中");
				$("#div_device_manage_state div[id=div_device_state_child]").children().remove();
				homeDevic.displayManage("div_device_state");   
				homeDevic.displayStateManage("div_call_record");   
//				var record=$("#div_device_manage_state div[id=div_container_callRecord]");
				var callRecord= $("#menu ul[id=iCallRecord]");
		    	homeDevic.setCssDefault(callRecord);
		    	var isHidden=$("#div_device_manage_state div[id=div_call_record]").is(":hidden");
		    	
		    	if(!isHidden){
		    		common_tool.setSelectStatus(1);
		    	}
		    	
		    	$.ajaxSettings.async = false;
//				if(record.prop("nodeName")==undefined){
					homeDevic.loadAllRecord();
//				}
				var tag=$("#td_allDevice_title span[id=span_title]");
				tag.text(comSpace.data.titleName+"通话记录");
			});
			
			/**
			 * 通讯录
			 */
			$("#menu ul[id=tdContact]").click(function(){
				$("#div_device_manage_state div[id=div_device_state_child]").hide();
				$("#div_device_manage_state div[id=div_call_record]").hide();
				$("#div_device_manage_state div[id=abcTemp]").hide();
				homeDevic.displayManage("div_contact_manage");
				var divContactCont=$("#div_contact_manage div[id=div_contactManage_container]");
				
				var tdContact= $("#menu ul[id=tdContact]");
				homeDevic.setCssDefault(tdContact);
				if(divContactCont.prop("nodeName")==undefined){
					homeDevic.loadContactManage();
				}else{
					homeDevic.loadContactManage();
				}
//				$("#inscribe").css({ 'position': 'absolute', 'bottom': '15px', 'padding-top':'0px','padding-bottom':'0px'});
			});
			
			/**
			 * 设备管理
			 */
			$("#menu ul[id=tdDeviceManage]").click(function(){
				$("#div_device_manage_state div[id=div_device_state_child]").hide();
				$("#div_device_manage_state div[id=div_call_record]").hide();
				$("#div_device_manage_state div[id=abcTemp]").hide();
				homeDevic.displayManage("div_device_manage"); 
				var tdDeviceManage= $("#menu ul[id=tdDeviceManage]");
				homeDevic.setCssDefault(tdDeviceManage);
				var divDevice=$("#div_device_manage div[id=div_devmanage_container]");
				if(divDevice.prop("nodeName")==undefined){
					homeDevic.loadDeviceManage();
				}
			});
			
			/**
			 * 我的任务
			 */
			$("#menu li[id=tdMyTask]").click(function(){
				$("#div_device_manage_state div[id=div_device_state_child]").hide();
				$("#div_device_manage_state div[id=div_call_record]").hide();
				$("#div_device_manage_state div[id=abcTemp]").hide();
				homeDevic.displayManage("div_myTask"); 
				var tdMyTask= $("#menu li[id=tdMyTask]");
				homeDevic.setCssDefault(tdMyTask);
				var divDevice=$("#div_myTask div[id=div_task_container]");
				if(divDevice.prop("nodeName")==undefined){
					homeDevic.loadMyTask();
				}else{
					homeDevic.loadMyTask();
					console.log("我的任务");
				}
			});
			
			/**
			 * 去管理中心
			 */
			$("#idHomeTable a[id=goCenter]").click(function(){
//				window.location.href=getRootPath()+"/system/welcome";
				window.open (getRootPath()+"/system/welcome");
			});
			
			/**
			 * 修改管理密码
			 */
			$("#idHomeTable a[id=modifyPass]").click(function(){
				$("#dialogModifyPass").dialog({
				    title: '修改登录密码',
				    width: 500,
				    height: 400,
				    closed: false,
				    cache: false,
//				    href: 'get_content.php',
				    modal: true,
				    onClose:function(){
				        $("#modify_password_form").form('reset');
				        $("#modify_password_form").form('clear');
				        
				    },
				    onOpen:function(){
				    	console.log("修改密码对话框打开");
//				    	$("#modify_password_form input[id=nPassword]").textbox("setValue"," ");
//                    	$("#modify_password_form input[id=rNewPassword]").textbox("setValue"," ");
				    },
					buttons:[
						   {
	                            text: '确认修改',
	                            width: 100,
//	                            iconCls: 'icon-add',
	                            handler: function () {
	                            	var firstPass=$("#modify_password_form input[id=nPassword]").val();
	                            	var secondPass=$("#modify_password_form input[id=rNewPassword]").val();
	                            	 let form_isValid = $("#modify_password_form").form('validate');
	                            	    
	                                 if (!form_isValid) {
	                                     common_tool.messager_show("请输入必填参数");
	                                     return;
	                                 }
	                            	if(firstPass==secondPass&&!strIsEmpty(secondPass)){
	                            		console.log("firstPass"+firstPass);
	                            		homeDevic.update_password(common_tool.getCurrUserId());
	                            		return;
	                            	}else{
	                            		common_tool.messager_show("密码不一致，请重新输入");
	                            	}
	                            }
	                        },
						   {
	                            text: '取消',
	                            width: 100,
//	                            iconCls: 'icon-add',
	                            handler: function () {
	                                $("#dialogModifyPass").dialog('close');
	                            }
	                        }
					]
				
				});
			});
			
			$("#idHomeTable a[id=goBack]").click(function(){
				window.location.href=getRootPath();
//				window.open (getRootPath());
			});
	})
})();