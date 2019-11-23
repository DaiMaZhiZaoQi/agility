/**
 * 设备管理js
 */

device_tool={
		
	/**
	 * 清除
	 */
	form_clear:function(){
		$("#device_edit_form").form('reset');
		$("#device_edit_form").form('clear');
		$("#device_user_list").datagrid('uncheckAll');
		$("#device_grid").datagrid('uncheckAll');
		$("#deviceJobs").datagrid("uncheckAll");
		
	    $("#device-search-form").form('reset');
        $("#device-search-form").form('clear');
//		$("#device-tool-bar input[name='search-deviceSerial']").textbox("reset");
//		$("#device-tool-bar input[name='search-deviceSerial']").textbox("clear");
//		
//		$("#device-tool-bar input[name='search-deviceName']").textbox("reset");
//		$("#device-tool-bar input[name='search-deviceName']").textbox("clear");
	},
		
	
	/**
	 * 加载设备列表
	 */
	init_device_view:function(){
		console.log("加载数据--init_device_view");
		var deviceName=$("#device-tool-bar input[name='search-deviceName']").val();
		var deviceSerial=$("#device-tool-bar input[name='search-deviceSerial']").val();
			$("#device_grid").datagrid({
				url:getRootPath()+"/systemdevice/list",
				method:'post',
				idField:"id",
				treeField:"deviceName",
				fitColumns:true,
				toolbar:"#device-tool-bar",
				rownumbers:true,
				animate:true,
				singleSelect:true,
				fit:true,
				border:false,
				pagination:true,
				striped:true,
				pagePosition:"bottom",
				pageNumber:1,
				pageSize:15,
				pageList:[15,30,45,60],
				  queryParams: {
		                deviceName: deviceName,
		                deviceSerial: deviceSerial,
		            },
				columns:[[
					 {title: "选择", field: "ck", checkbox: true},
					 {title: "设备名称", field: "deviceName", width: 200},
					 {title: "设备序列号", field: "deviceSerial", width: 200},
					 {title: "说明", field: "description", width: 300},
			         {
		                    title: "是否可修改", field: "isFinal", formatter: function (value, row, index) {
		                    if (value == 1) {
		                        return "是";
		                    }
		                    if (value == 2) {
		                        return "否";
		                    }
		                }, width: 60
		                },
		                {
		                    title: "创建时间", field: "createTime", formatter: function (value, row, index) {
		                    return common_tool.timestampToDateTime(value);
		                }, width: 300
		                },
		                {
		                    title: "更新时间", field: "updateTime", formatter: function (value, row, index) {
		                    return common_tool.timestampToDateTime(value);
		                }, width: 300
		                },
				]],
			})
	},
		
	init_main_view:function(){		
		$("#device_grid").datagrid({
			url: getRootPath() + "/role/list",
            method: 'get',
            idField: "id",
            treeField: 'name',
            fitColumns: true,
            toolbar: '#role-tool-bar',
            rownumbers: true,
            animate: true,
            singleSelect: true,
            fit: true,
            border: false,
            pagination: false,
            striped: true,
            pagePosition: "bottom",
            pageNumber: 1,
            pageSize: 15,
            pageList: [15, 30, 45, 60],
            columns: [[
                {title: "选择", field: "ck", checkbox: true},
                {title: "名称", field: "name", width: 300},
                {title: "说明", field: "description", width: 400},
                {
                    title: "是否可修改", field: "isFinal", formatter: function (value, row, index) {
                    if (value == 1) {
                        return "是";
                    }
                    if (value == 2) { 
                        return "否";
                    }
                }, width: 60
                },
                {
                    title: "创建时间", field: "createTime", formatter: function (value, row, index) {
                    return common_tool.timestampToDateTime(value);
                }, width: 100
                },
                {
                    title: "更新时间", field: "updateTime", formatter: function (value, row, index) {
                    return common_tool.timestampToDateTime(value);
                }, width: 100
                },
            ]],
           
		})
		
	},
	
	/**
	 * type 1,新增   2,删除
	 */
	init_device_dialog:function(type,sysDeviceRoleOrg){			//  添加设备初始化
		$("#device_edit_dialog").dialog({
			title:'添加设备',
			 iconCls: 'icon-save',
	         closable: true,
	         width: 950,
	         height: 500,
	         cache: false,
	         modal: true,
	         resizable: false,
	         'onOpen': function () {
	        	 if(type==2){
		        	 var device = $("#device_grid").datagrid("getChecked")[0];
		        	 var json=JSON.stringify(device);
	        		 console.log("字符串-->"+json);
		        	 if(device.sysDeviceRoleOrg!=null&&device.sysDeviceRoleOrg.length!=0){
		        		 console.log("选中的行"+device.sysDeviceRoleOrg[0].sysUserId);
		        		 $("#device_user_list").datagrid("selectRecord",device.sysDeviceRoleOrg[0].sysUserId);
		        	 }else{
		        		 $("#device_user_list").datagrid("uncheckAll");
		        	 }
	        	 }
	        	 
	        	 	var isShow;
	        		var users=$("#device_edit_form table[id='device_user_list']").datagrid("getChecked");
	        	 for (var i = 0; users.length>0&&i < users[0].userRoleOrganizations.length; i++) {
                     $("#deviceJobs").treegrid("select", users[0].userRoleOrganizations[i].sysRoleOrganizationId);
                     isShow=true;
                 }
	        	 isShow=false;
	        
	        	 $('#device_user_list').datagrid({
	        		 'onLoadSuccess':function(data){
	        			 var strData=JSON.stringify(data);
//	        			 console.log("onLoadSuccess"+strData);
	        			 var s = $("#deviceJobs").datagrid('getPanel');
	        			 var rows = s.find('tr.datagrid-row');
	        			 var rows1 = s.find('tr.datagrid-row td[field!=ck]');
	        			 rows1.unbind('click').bind('click', function(e) {
	        				 return false;
	        			 });
	        			 var rows2 = s.find('tr.datagrid-row td[field=ck]');
	        			 rows2.unbind('click').bind('click', function(e) {
	        				 return false;
	        			 });
	        			 var rows3=s.find('tr.datagrid-header-row td[field=ck]');
	        			 rows3.unbind('click').bind('click', function(e) {
	        				 return false;
	        			 });
	        		 },
	        		
	    			 'onCheck':function(rowIndex,rowData){
	    				 if(!isShow){
	    					 $("#deviceJobs").datagrid("uncheckAll");
	    	        	  var userRoleOrgs=rowData.userRoleOrganizations;
	    	        	  var str=JSON.stringify(userRoleOrgs);
	    	        	  console.log("已选中-->"+str);
	    	        	  for (var i = 0; userRoleOrgs.length>0&&i < userRoleOrgs.length; i++) {
	    	                     $("#deviceJobs").treegrid("select", userRoleOrgs[i].sysRoleOrganizationId);
	    	                 }
	    				}
	    				
	    	          },
	    		});
	        	 $("#deviceJobs").treegrid({
		        	 'rowStyler':function(row){
	     		        return 'background-color:#e3e3e3;color:#000000;font-weight:bold;';
	     		    },
	        	 });
	          },
	       
	          'onClose':function(){
	        		 device_tool.form_clear();
	          },
	         buttons:[
	        	 {
	                    text: '保存',
	                    width: 100,
	                    iconCls: 'icon-save',
	                    handler: function () {
	                    	if(type==1){
	                    		device_tool.save();
	                    		
	                    	}else if(type==2){
	                    		device_tool.update(sysDeviceRoleOrg);
	                    	}
	                    }
	                },
	                {
	                    text: '清除',
	                    width: 100,
	                    iconCls: 'icon-reload',
	                    handler: function () {
	                    	
	                    }
	                },
	                {
	                    text: '取消',
	                    width: 100,
	                    iconCls: 'icon-add',
	                    handler: function () {
	                    	
	                        $("#device_edit_dialog").dialog('close');
	                    }
	                }
	         ],
		});
	},
	
	/**
	 * 保存新增
	 */
	save:function(){
		if(!$("#device_edit_form input[id='deviceName']").validatebox('isValid')){
			common_tool.messager_show("请输入设备名称");
		}else if(!$("#device_edit_form input[id='deviceSerial']").validatebox('isValid')){
			common_tool.messager_show("请输入设备序列号");
		}else if(!$("#device_edit_form input[id='description']").validatebox('isValid')){
			common_tool.messager_show("请输入设备描述");
		}else {				//   添加设备
			var deviceName=$("#device_edit_form input[id='deviceName']").val();
			var deviceSerial=$("#device_edit_form input[id='deviceSerial']").val();
			var description=$("#device_edit_form input[id='description']").val();
			var sysOrg=$("#device_edit_form table[id=deviceJobs]").datagrid("getChecked");
			/*var str=JSON.stringify(sysOrg);
			var sysOrgId=sysOrg[0].sysOrganizationId;
			alert("sysOrgId-->"+sysOrgId+"--->"+str);
			return;*/
			var user=$("#device_edit_form table[id='device_user_list']").datagrid("getChecked");
			/*	var arr=new Array();
			if(user.length>0){
				for(var i=0;user.length>0&&i<user[0].userRoleOrganizations.length;i++){
					arr[i]=user[0].userRoleOrganizations[i].sysRoleOrganizationId;
				}
			}*/
			
			$.ajax({
				
				data:{ 
					deviceName:deviceName,
					deviceSerial:deviceSerial,
					description:description,
					sysOrgId:sysOrg[0].sysOrganizationId,
					userId:user!=null&&user.length>0?user[0].id:0,
				},
				method: 'post',
				url: getRootPath() + '/systemdevice/insert',
				dataType: 'json',
				 traditional:true,				//防止深度序列化
				success:function(data){
					if(data.code==10000){		//   操作成功
						$("#device_edit_dialog").dialog("close");
						 common_tool.messager_show(data.msg);
						 device_tool.form_clear();
						 device_tool.init_device_view();
	                        return false;
					}else{
						 common_tool.messager_show(data.msg);
	                        return false;
					}
				}
			});
		}
	},
	
	update:function(sysDeviceRoleOrg){
		if(!$("#device_edit_form input[id='deviceName']").validatebox('isValid')){
			common_tool.messager_show("请输入设备名称");
		}else if(!$("#device_edit_form input[id='deviceSerial']").validatebox('isValid')){
			common_tool.messager_show("请输入设备序列号");
		}else if(!$("#device_edit_form input[id='description']").validatebox('isValid')){
			common_tool.messager_show("请输入设备描述");
		}else{
			var deviceName=$("#device_edit_form input[id='deviceName']").val();
			var deviceId=$("#device_edit_form input[id='iddevice']").val();
			var deviceSerial=$("#device_edit_form input[id='deviceSerial']").val();
			var description=$("#device_edit_form input[id='description']").val();
			
			var sysOrg=$("#device_edit_form table[id=deviceJobs]").datagrid("getChecked");
			
			var user=$("#device_edit_form table[id='device_user_list']").datagrid("getChecked");
			$.ajax({
			
				data:{ 
					deviceId:deviceId,
					deviceName:deviceName,
					deviceSerial:deviceSerial,
					description:description,
					sysOrgId:sysOrg[0].sysOrganizationId,
					userId:user!=null&&user.length>0?user[0].id:0,
				},
				method: 'post',
				url: getRootPath() + '/systemdevice/update',
//				contentType : 'application/json',
				dataType: 'json',
				 traditional:true,				//防止深度序列化
				success:function(data){
					if(data.code==10000){		//   操作成功
						$("#device_edit_dialog").dialog("close");
						 common_tool.messager_show(data.msg);
						 device_tool.form_clear();
						 device_tool.init_device_view();
	                        return false;
					}else{
						 common_tool.messager_show(data.msg);
	                        return false;
					}
				}
			});
			
		}
	},
	
	/**
	 * 上传修改后的值
	 */
	convertBody:function(){
		
	}
}

$(document).ready(function() {
//	device_tool.init_main_view();
	device_tool.init_device_view();
	$("#device-save-btn").click(function() {		//  新增
		device_tool.init_device_dialog(1,null);
	});
	
	$("#device-update-btn").click(function(){		//	修改
		var device=$("#device_grid").treegrid('getChecked')[0];
		var updateDevice=JSON.stringify(device);
		console.log("device-update-btn"+updateDevice);
		if(device==null){
			 common_tool.messager_show("请选择一条记录");
	         return false;
		}
		$("#device_edit_form").form('load',{
			"id":device.id,
			"deviceName":device.deviceName,
			"deviceSerial":device.deviceSerial,
			"description":device.description
			
		});
		device_tool.init_device_dialog(2,device.sysDeviceRoleOrg);
	});
	
	$("#device-select-btn").click(function(){		//	刷新
		device_tool.form_clear();
		device_tool.init_device_view();
	});
	
	 $("#device-search-btn").click(function () {
		 console.log("查询设备");
		 device_tool.init_device_view();
	    });
})