user_tool = {
    form_clear: function () {
        $("#user_form").form('reset');
        $("#user_form").form('clear');
        $("#init_password_form").form('reset');
        $("#init_password_form").form('clear');
        $("#user-search-form").form('reset');
        $("#user-search-form").form('clear');
        
//        $("#user-permissions").datagrid("uncheckAll");
        $("#user_roles").datagrid("uncheckAll");
        $("#user_orgs").treegrid("uncheckAll");
        $("#user_in_orgs").treegrid("uncheckAll");
//        $("#user_orgs").treegrid("unselectAll");
//        $("#jobs").treegrid("uncheckAll");
        
        $("#user_grid").treegrid("uncheckAll");
        
        $("#save-permission-form").form('clear');
        $("#org-permissions").treegrid("uncheckAll");
        var pg=$("#permission-group");
        if(pg!=undefined){
        	pg.datagrid("uncheckAll");
        }
    },
    init_main_view: function () {
        var loginName = $("input[name='search-loginName']").val();
        var zhName = $("input[name='search-zhName']").val();
        var email = $("input[name='search-email']").val();
        var phone = $("input[name='search-phone']").val();
        var address = $("input[name='search-address']").val();
        $("#user_grid").datagrid({
            url: getRootPath() + '/user/list',
            method: 'get',
            idField: "id",
            fitColumns: true,
            toolbar: '#user-tool-bar',
            rownumbers: true,
            animate: true,
            singleSelect: true,
            fit: true,
            border: false,
            pagination: true,
            striped: true,
            pagePosition: "bottom",
            pageNumber: 1,
            pageSize: 15,
            pageList: [15, 30, 45, 60],
            queryParams: {
                loginName: loginName,
                zhName: zhName,
                email: email,
                phone: phone,
                address: address,
                userId:common_tool.getCurrUserId(),
            },
            columns: [[
                {title: "选择", field: "ck", checkbox: true},
                {title: "中文名", field: "zhName", width: 124, sortable: true},
                {title: "登录名", field: "loginName", width: 124, sortable: true},
                {title: "英文名", field: "enName", width: 124, sortable: true},
                {
                    title: "性别", width: 35, field: "sex", formatter: function (value, row, index) {
                    if (value == 1) {
                        return "男";
                    }
                    if (value == 2) {
                        return "女";
                    }
                }
                },
                {
                    title: "状态", field: "status", align: 'center', width: 87, formatter: function (value, row, index) {
                    if (value == 1) {
                        return "<input class='easyui-switchbutton status' checked />";
                    }
                    if (value == 3) {
                        return "<input class='easyui-switchbutton status' unchecked />";
                    }

                }
                },
                {title: "生日", field: "birth", width: 90},
                {title: "邮箱", field: "email", width: 130, sortable: true},
                {title: "电话", field: "phone", width: 130, sortable: true},
                {title: "地址", field: "address", width: 400},
                {
                    title: "可修改", field: "isFinal", formatter: function (value, row, index) {
                    if (value == 1) {
                        return "是";
                    }
                    if (value == 2) {
                        return "否";
                    }
                }, width: 50
                },
                {
                    title: "创建时间", field: "createTime", formatter: function (value, row, index) {
                    date = new Date(value);
                    timeStr = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes();
                    return timeStr;
                }, width: 200
                },
                {
                    title: "更新时间", field: "updateTime", formatter: function (value, row, index) {
                    return common_tool.timestampToDateTime(value);
                }, width: 200
                },
            ]],
            onLoadSuccess: function (data) {
                $(".status").switchbutton({
                    readonly: true,
                    onText: '已启用',
                    offText: '已禁用',
                    width: 80,
                })
            },
            onDblClickRow: function (index, row) {
                var users = $("#user_grid").datagrid('getChecked');
                if (users.length == 0) {
                    common_tool.messager_show("请至少选择一条记录");
                    return false;
                }
                $("#user_form").form('load', {
                    id: users[0].id,
                    loginName: users[0].loginName,
                    zhName: users[0].zhName,
                    enName: users[0].enName,
                    sex: users[0].sex,
                    birth: users[0].birth,
                    email: users[0].email,
                    phone: users[0].phone,
                    address: users[0].address,
                    password: '111111111111',
                });
                $("#user_edit_dialog").dialog({
                    title: "用户详情",
                    iconCls: 'icon-save',
                    closable: true,
                    width: 1200,
                    height: 700,
                    cache: false,
                    modal: true,
                    resizable: false,
                    'onBeforeOpen': function () {
                    	
                    },
                    'onOpen': function () {
                    	
                    	$.extend($.fn.validatebox.defaults.rules, {
                    		logName: {
                    			validator: function(value){
                    				var reg = new RegExp(/^[a-zA-Z0-9_-]{4,16}$/);
                    				if (!reg.test(value)) {
                    					return false;
                    				}
                    				return true;
                    			},
                    		}
                    	});
                    	
                    	var currSelectId=null;
                    	
                    	
                    	$("#user_roles").datagrid({
                    		 url:getRootPath()+"/role/list?userId="+common_tool.getCurrUserId(),
                    		 method:"GET",
                    		 "onLoadSuccess":function(){
                    			 var params="?id="+common_tool.getCurrUserId();
                    			 $("#user_orgs").treegrid({
                             		url:getRootPath()+"/organization/list"+params,
                         			method:"GET",
                         			"onCheck":function(rowData){
                         				if(rowData==null)return;
                         				rowData.checkState=1;
                         				rowData.checked=true;
                         				if(rowData.children.length!=0){	// 子节点全部选中
                         					var children=$("#user_orgs").treegrid("getChildren",rowData.id);
                         					console.log(children);
                         					for(var j=0;j<children.length;j++){
        	                 					$("#user_orgs").treegrid("select",children[j].id);
                         					}
                         					 var orgs=$("#user_edit_dialog table[id='user_orgs']").treegrid("getChecked");
                         				}
                         			},
                         			"onUnselectAll":function(rowData){
                         				for(var i=0;i<rowData.length;i++){
                         					rowData[i].checkState=0;
                             				rowData[i].checked=false;
                         					
                         				}
                         			},
                         			"onSelectAll":function(rowData){
                         				for(var i=0;i<rowData.length;i++){
                         					rowData[i].checkState=1;
                             				rowData[i].checked=true;
                         				}
                         			},
                         			"onUncheck":function(rowData){
                         				rowData.checkState=0;
                         				rowData.checked=false;
                         				var parent= $("#user_orgs").treegrid("find",rowData.parentId);
                         				if(parent!=null&&parent.checked==true){
                         					rowData.checked=true;
                         					rowData.checkState=1;
                         					$("#user_orgs").treegrid("unselect",rowData.parentId);
                         					return;
                         				}
                         				if(rowData.children.length!=0){   //  是父节点
                         					for(var j=0;j<rowData.children.length;j++){
                         						$("#user_orgs").treegrid("unselect",rowData.children[j].id);
                         					}
                         					return;
                         				}
                         			},
                         			
                         			"onClickRow":function(row){
                         				var parent= $("#user_orgs").treegrid("find",row.parentId);
                         				if(parent!=null&&parent.checked==true){
                         					row.checked=true;
                         					row.checkState=1;
                         					$("#user_orgs").treegrid("select",row.id);
                         				}
                         			},
                         		
                         		
                         			"onLoadSuccess":function(){
                                             var users = $("#user_grid").datagrid('getChecked')[0];
                                             for(var i=0;i<users.listUserRole.length;i++){ //  默认选中用户所处的角色
                                             	$("#user_roles").datagrid("selectRecord",users.listUserRole[i].sysRoleId);
                                             }
                                             for(var i=0;i<users.listUserOrg.length;i++){
                                             	$("#user_orgs").treegrid("select",users.listUserOrg[i].sysOrgId);
                                             }
                                         $("#user_edit_dialog table[id='user_in_orgs']").treegrid({
                                     		url:getRootPath()+"/organization/list"+params,
                                  			method:"GET",
                                  			"onLoadSuccess":function(){
                                                      var users = $("#user_grid").datagrid('getChecked')[0];
                                                  	$("#user_edit_dialog table[id='user_in_orgs']").treegrid("select",users.mSysUserInOrg.sysOrgId);
                                                  	
                                  			},
                                  			"onCheck":function(rowData){
                                  					if(rowData==null)return;
                                  					 if(currSelectId!=null){
                                  						 $("#user_orgs").treegrid("unselect",currSelectId);
                                  					 }
                                  					 currSelectId=rowData.id;
                                  					 $("#user_orgs").treegrid("select",currSelectId);
                                  			},
                                     	});
                             		},
                             	});
                    		 },
                    	});
                    	
                    },
                    'onClose': function () {
                        user_tool.form_clear();
                    },
                    
                    
                    buttons: [
                       
                        {
                            text: '取消',
                            width: 100,
                            iconCls: 'icon-add',
                            handler: function () {
                                user_tool.form_clear();
                                $("#user_edit_dialog").dialog('close');
                            }
                        }
                    ],
                });
                $("#user_edit_dialog").dialog("center");
            }
        });
    },
    // 新增用户  这边应该传一个机构代码过来，传机构代码之前，需新增机构， 
    init_edit_view: function (type) {
    	var titleName="新增用户";
    	if(type==2){
    		titleName="修改用户";
    	}
        $("#user_edit_dialog").dialog({
            title: titleName,
            iconCls: 'icon-save',
            closable: true,
            width: 1200,
            height: 700,
            cache: false,
            modal: true,
            resizable: false,
            'onBeforeOpen': function () {
            	
            },
            'onOpen': function () {
            	
            	$.extend($.fn.validatebox.defaults.rules, {
            		logName: {
            			validator: function(value){
            				var reg = new RegExp(/^[a-zA-Z0-9_-]{4,16}$/);
            				if (!reg.test(value)) {
            					return false;
            				}
            				return true;
            			},
            		}
            	});
            	
            	console.log("user打开");
            	var currSelectId=null;
            	
            	
            	$("#user_roles").datagrid({
            		 url:getRootPath()+"/role/list?userId="+common_tool.getCurrUserId(),
            		 method:"GET",
            		 "onLoadSuccess":function(){
            			 var params="?id="+common_tool.getCurrUserId();
            			 
            			 
            			 $("#user_orgs").treegrid({
                     		url:getRootPath()+"/organization/list"+params,
                 			method:"GET",
                 			"onCheck":function(rowData){
                 				if(rowData==null)return;
                 				console.log("选中执行");
                 				rowData.checkState=1;
                 				rowData.checked=true;
                 				if(rowData.children.length!=0){	// 子节点全部选中
                 					console.log("有子节点");
                 					var children=$("#user_orgs").treegrid("getChildren",rowData.id);
                 					console.log(children);
                 					for(var j=0;j<children.length;j++){
	                 					$("#user_orgs").treegrid("select",children[j].id);
                 					}
                 				}
                 				var orgs=$("#user_edit_dialog table[id='user_orgs']").treegrid("getChecked");
                 				console.log("机构集合"+JSON.stringify(orgs));
                 			},
                 			"onUnselectAll":function(rowData){
                 				console.log("取消全选");
                 				for(var i=0;i<rowData.length;i++){
                 					rowData[i].checkState=0;
                     				rowData[i].checked=false;
                 					
                 				}
                 			},
                 			"onSelectAll":function(rowData){
                 				for(var i=0;i<rowData.length;i++){
                 					rowData[i].checkState=1;
                     				rowData[i].checked=true;
                 				}
                 			},
                 			"onUncheck":function(rowData){
                 				rowData.checkState=0;
                 				rowData.checked=false;
                 				var parent= $("#user_orgs").treegrid("find",rowData.parentId);
                 				if(parent!=null&&parent.checked==true){
                 					rowData.checked=true;
                 					rowData.checkState=1;
//                 					$("#user_orgs").treegrid("select",rowData.id);
                 					$("#user_orgs").treegrid("unselect",rowData.parentId);
                 					return;
                 				}
                 				if(rowData.children.length!=0){   //  是父节点
                 					for(var j=0;j<rowData.children.length;j++){
                 						$("#user_orgs").treegrid("unselect",rowData.children[j].id);
                 					}
                 					return;
                 				}
                 			
                 				console.log(parent);
                 			},
                 			
                 			"onClickRow":function(row){
                 				console.log("onClickRow-->"+row.parentId);
                 				var parent= $("#user_orgs").treegrid("find",row.parentId);
                 				console.log(parent);
                 				if(parent!=null&&parent.checked==true){
                 					row.checked=true;
                 					row.checkState=1;
                 					$("#user_orgs").treegrid("select",row.id);
                 				}
                 			},
                 		
                 		
                 			"onLoadSuccess":function(){
                 				var users = $("#user_grid").datagrid('getChecked')[0];
                                 if (type == 2) {
                                     var json=JSON.stringify(users);
                                     console.log("user_grid"+json);
                                     for(var i=0;i<users.listUserRole.length;i++){ //  默认选中用户所处的角色
                                     	$("#user_roles").datagrid("selectRecord",users.listUserRole[i].sysRoleId);
                                     }
                                     for(var i=0;i<users.listUserOrg.length;i++){
                                    	 var isSelectOrg=$("#user_edit_dialog table[id='user_orgs']").treegrid("find",users.listUserOrg[i].sysOrgId);
                                    	 if(isSelectOrg!=undefined){
                                    		 $("#user_orgs").treegrid("select",users.listUserOrg[i].sysOrgId);
                                    	 }
                                      }
                                 }
                                 $("#user_edit_dialog table[id='user_in_orgs']").treegrid({
                             		url:getRootPath()+"/organization/list"+params,
                          			method:"GET",
                          			"onLoadSuccess":function(){
                          				 if (type == 2) {
//                                              var usersGrid = $("#user_grid").datagrid('getChecked')[0];
                                          	$("#user_edit_dialog table[id='user_in_orgs']").treegrid("select",users.mSysUserInOrg.sysOrgId);
                                          	
                          				 }
                          			},
                          			"onCheck":function(rowData){
                          					if(rowData==null)return;
                          					 if(currSelectId!=null){
                          						 $("#user_orgs").treegrid("unselect",currSelectId);
                          					 }
                          					 var selected=$("#user_orgs").treegrid("getSelected");
                          					 console.log("selected-->"+selected);
                          					 currSelectId=rowData.id;
                          					 $("#user_orgs").treegrid("select",currSelectId);
                          			},
                             	});
                     		},
                     	});
            		 },
            	});
            	
            },
            'onClose': function () {
                user_tool.form_clear();
            },
            
            
            buttons: [
                {
                    text: '保存',
                    width: 100,
                    iconCls: 'icon-save',
                    handler: function () {
                    	   var userInOrg=$("#user_edit_dialog table[id=user_in_orgs]").treegrid("getChecked");
                    	   
                           if(userInOrg.length>0){
                           		userInOrg=userInOrg[0];
                           		var isSelectTag=$("#user_edit_dialog table[id='user_orgs']").treegrid("find",userInOrg.id);
                           		if(isSelectTag!=null&&isSelectTag.checked==false){
                           			console.log("isSelectTag"+JSON.stringify(isSelectTag));
                           			$("#user_edit_dialog table[id='user_orgs']").treegrid("select",userInOrg.id);
                           			alert(isSelectTag.name+"为必选机构权限");
                           			return;
                           		}
                           }
                    	
                        if (type == 1) {
                        	
                            user_tool.save();
                        }
                        if (type == 2) {

                        	if(confirm("是否确定修改")){
                        		user_tool.update();
                        	}
                        }
                    }
                },
                {
                    text: '清除',
                    width: 100,
                    iconCls: 'icon-reload',
                    handler: function () {
                        user_tool.form_clear();
                    }
                },
                {
                    text: '取消',
                    width: 100,
                    iconCls: 'icon-add',
                    handler: function () {
                        user_tool.form_clear();
                        $("#user_edit_dialog").dialog('close');
                    }
                }
            ],
        });	
        $("#user_edit_dialog").dialog("center");
    },

    save: function () {
    
        var form_isValid = $("#user_form").form('validate');
    
        if (!form_isValid) {
            common_tool.messager_show("请输入必填参数")
        } else if ($("#user_in_orgs").treegrid("getChecked").length == 0) {
            common_tool.messager_show('请选择机构');
        }else if($("#user_roles").datagrid("getChecked").length==0){
        	 common_tool.messager_show('请选择角色');
        }else {
            var loginName = $('#user_edit_dialog input[id="loginName"]').val();
            var zhName = $('#user_edit_dialog input[id="zhName"]').val();
            var enName = $('#user_edit_dialog input[id="enName"]').val();
            var sex = $('#user_edit_dialog select[id="sex"]').combobox('getValue');
            var birth = $('#user_edit_dialog input[id="birth"]').datebox('getValue');
            var email = $('#user_edit_dialog input[id="email"]').val();
            var phone = $('#user_edit_dialog input[id="phone"]').val();
            var address = $('#user_edit_dialog input[id="address"]').val();
            var password = $('#user_edit_dialog input[id="password"]').val();
            var roles=$("#user_edit_dialog table[id='user_roles']").datagrid("getChecked");
            var roleIds=new Array();
            for(var i=0;i<roles.length;i++){
            	roleIds[i]=roles[i].id;
            }
            var userInOrg=$("#user_edit_dialog table[id=user_in_orgs]").treegrid("getChecked");
            if(userInOrg.length>0){
            	userInOrg=userInOrg[0];
//            	console.log("userInOrg--->"+userInOrg.id);
//            	$("#user_edit_dialog table[id='user_orgs']").treegrid("select",userInOrg.id);
            }
            
            var orgs=$("#user_edit_dialog table[id='user_orgs']").treegrid("getChecked");
//            var arrOrgs=new Array();
//            for(var i=0;i<orgs.length;i++){
//            	var orgName=orgs[i].name;
//            	if(orgName!=null&&orgName!=undefined){
//            		arrOrgs.push();
//            	}
//            }
//            console.log(arrOrgs);
//            return;
            var orgIds=new Array();
            for(var i=0;i<orgs.length;i++){
            	orgIds[i]=orgs[i].id;
            }
            if(!confirm("是否确定添加")){
        		return;
        	}
            $.ajax({
                data: {
                    loginName: loginName,
                    zhName: zhName,
                    enName: enName,
                    sex: sex,
                    birth: birth,
                    email: email,
                    phone: phone,
                    address: address,
                    password: password,
                    roleIds:roleIds.toString(),
                    sysUserInOrgStr:JSON.stringify(userInOrg),
                    listSysOrgPerStr:JSON.stringify(orgs)
                },
                traditional: true,
                method: 'post',
                url: getRootPath() + '/user/insert',
                async: false,
                dataType: 'json',
//                contentType:'application/json;charset=utf-8',
                success: function (result) {
                    if (result.code == 10000) {
                        $("#user_edit_dialog").dialog("close");
                        user_tool.form_clear();
//                        user_tool.init_main_view();
                        $("#user_grid").datagrid("reload");
                        common_tool.messager_show(result.msg);
                        return false;
                    }
                    else {
                        common_tool.messager_show(result.msg);
                    }
                },
            });

        }

    },
    update: function (data) {
        var form_isValid = $("#user_form").form('validate');
        var comUserId=common_tool.getCurrUserId();
        var loginName = $('#user_edit_dialog input[id="loginName"]').val();
        var superUser=false;
        if("1"==comUserId&&"super"==loginName){
        	superUser=true;
        }
        
        if (!form_isValid&&!superUser) {
            common_tool.messager_show("请输入必填参数")
        } else if ($("#user_in_orgs").treegrid("getChecked").length == 0&&!superUser) {
            common_tool.messager_show('请选择机构');
        }else if($("#user_roles").datagrid("getChecked").length==0&&!superUser){
        	 common_tool.messager_show('请选择角色');
        }else {
        	var id = $('#user_edit_dialog input[id="id"]').val();
        	var loginName = $('#user_edit_dialog input[id="loginName"]').val();
        	var zhName = $('#user_edit_dialog input[id="zhName"]').val();
        	var enName = $('#user_edit_dialog input[id="enName"]').val();
        	var sex = $('#user_edit_dialog select[id="sex"]').combobox('getValue');
        	var birth = $('#user_edit_dialog input[id="birth"]').datebox('getValue');
        	var email = $('#user_edit_dialog input[id="email"]').val();
        	var phone = $('#user_edit_dialog input[id="phone"]').val();
        	var address = $('#user_edit_dialog input[id="address"]').val();
        	var password = $('#user_edit_dialog input[id="password"]').val();
	       	 var roles=$("#user_edit_dialog table[id='user_roles']").datagrid("getChecked");
	         var roleIds=new Array();
	         for(var i=0;i<roles.length;i++){
	         	roleIds[i]=roles[i].id;
	         }
        	if(superUser){
        		   $.ajax({
                       data: {
                           id: id,
                           loginName: loginName,
                           zhName: zhName,
                           enName: enName,
                           sex: sex,
                           birth: birth,
                           email: email,
                           phone: phone,
                           address: address,
                           roleIds:roleIds.toString(),
                       },
                       traditional: true,
                       method: 'post',
                       url: getRootPath() + '/user/update',
                       async: false,
                       dataType: 'json',
                       success: function (result) {
                           if (result.code == 10000) {
                               $("#user_edit_dialog").dialog("close");
                               user_tool.form_clear();
                               $("#user_grid").datagrid("reload");
                               common_tool.messager_show(result.msg);
                               return false;
                           }
                           else {
                               common_tool.messager_show(result.msg);
                           }
                       },
                   });
        	}else{
        	
                 var userInOrg=$("#user_edit_dialog table[id=user_in_orgs]").treegrid("getChecked");
                 if(userInOrg.length>0){
                 	userInOrg=userInOrg[0];
                 }
                 var orgs=$("#user_edit_dialog table[id='user_orgs']").treegrid("getChecked");
                 console.log("机构集合"+JSON.stringify(orgs));
                 $.ajax({
                     data: {
                         id: id,
                         loginName: loginName,
                         zhName: zhName,
                         enName: enName,
                         sex: sex,
                         birth: birth,
                         email: email,
                         phone: phone,
                         address: address,
                         roleIds:roleIds.toString(),
                         sysUserInOrgStr:JSON.stringify(userInOrg),
                         listSysOrgPerStr:JSON.stringify(orgs)
                     },
                     traditional: true,
                     method: 'post',
                     url: getRootPath() + '/user/update',
                     async: false,
                     dataType: 'json',
                     success: function (result) {
                         if (result.code == 10000) {
                             $("#user_edit_dialog").dialog("close");
                             user_tool.form_clear();
                             $("#user_grid").datagrid("reload");
                             common_tool.messager_show(result.msg);
                             return false;
                         }
                         else {
                             common_tool.messager_show(result.msg);
                         }
                     },
                 });
        	}
         
            
           
            
            
         

        }
    },
    delete: function (id) {
        $.ajax({
            data: {
                id: id,
            },
            traditional: true,
            dataType: 'json',
            method: 'get',
            url: getRootPath() + '/user/delete',
            async: false,
            success: function (result) {
                if (result.code == 10000) {
                    user_tool.form_clear();
//                    user_tool.init_main_view();
                    $("#user_grid").datagrid("reload");
                    common_tool.messager_show(result.msg);
                    return false;
                }
                else {
                    common_tool.messager_show(result.msg);
                }
            },
        });
    },
    password_view: function (user) {
        $("#password_edit_dialog").dialog({
            title: '重置 ' + user.zhName + ' 密码',
            iconCls: 'icon-save',
            closable: true,
            width: 450,
            height: 350,
            cache: false,
            modal: true,
            resizable: false,
            'onClose': function () {
                user_tool.form_clear();
            },
            buttons: [
                {
                    text: '保存',
                    width: 100,
                    iconCls: 'icon-save',
                    handler: function () {
                        user_tool.update_password(user.id);
                    }
                },
                {
                    text: '清除',
                    width: 100,
                    iconCls: 'icon-reload',
                    handler: function () {
                        user_tool.form_clear();
                    }
                },
                {
                    text: '取消',
                    width: 100,
                    iconCls: 'icon-add',
                    handler: function () {
                        user_tool.form_clear();
                        $("#password_edit_dialog").dialog('close');
                    }
                }
            ],
        })
        $("#password_edit_dialog").dialog("center");
    },
    save_permission: function (userId,loginName) {
        if (!$("#permission_name").validatebox('isValid')) {
            common_tool.messager_show("请输入权限名称");
        } else if (!$("#permission_code").validatebox('isValid')) {
            common_tool.messager_show("请输入权限编码");
        }
        else if (!$("#permission_description").validatebox('isValid')) {
            common_tool.messager_show("请输入权限描述");
        }
        else if ($("#permission-group").treegrid("getChecked").length == 0) {
            common_tool.messager_show("请选择权限组");
        } else {
            var name = $("#permission_name").val();
            var code = $("#permission_code").val();
            var description = $("#permission_description").val();
            var groupId = $("#permission-group").treegrid("getChecked")[0].id;
            code=groupId+":"+userId+":"+loginName+":"+code;
//            console.log("save_permission"+name+"-->"+code+"-->"+description+"-->"+groupId+"-->"+userId);
            $.ajax({
                data: {
                    name: name,
                    code: code,
                    description: description,
                    groupId: groupId,
                },
                method: 'post',
                url: getRootPath() + '/permission/insert',
                async: false,
                dataType: 'json',
                success: function (result) {
                    if (result.code == 10000) {
                        $("#save-permission-dialog").dialog("close");
                        user_tool.form_clear();
                        common_tool.messager_show(result.msg);
                        return false;
                    }
                    else {
                        common_tool.messager_show(result.msg);
                    }
                },
            });
        }
    },
    add_user_permission: function (type,userId,loginName, groupId) {
    	var titleStr="";
    	if(type==1){
    		titleStr="新增权限";
    	}else{
    		titleStr="修改权限";
    	}
        $("#save-permission-dialog").dialog({
        	title:titleStr,
            iconCls: 'icon-save',
            closable: true,
            width: 700,
            height: 400,
            cache: false,
            modal: true,
            resizable: false,
            'onOpen': function () {
                if (groupId != null) {
                	
                    $("#permission-group").datagrid('selectRecord', groupId);
                }
           
            },
            buttons: [
                {
                    text: '保存',
                    width: 100,
                    iconCls: 'icon-save',
                    handler: function () {
                        if (type == 1) {						//  新增权限
                        	user_tool.save_permission(userId,loginName);
                        	
                        }
                        if (type == 2) {						//  修改权限
//                            permission_tool.update_permission();
                        }
                    }
                },
                {
                    text: '清除',
                    width: 100,
                    iconCls: 'icon-reload',
                    handler: function () {
                        permission_tool.form_clear();
                    }
                },
                {
                    text: '取消',
                    width: 100,
                    iconCls: 'icon-add',
                    handler: function () {
                        permission_tool.form_clear();
                        $("#save-permission-dialog").dialog('close');
                    }
                }
            ],
        });
        $("#save-permission-dialog").dialog("center");
    },
    update_password: function (id) {
        var newPassword = $("#newPassword").val();
        var repeatNewPassword = $("#repeatNewPassword").val();
        $.ajax({
            data: {
                id: id,
                newPassword: newPassword,
                repeatNewPassword: repeatNewPassword,
            },
            traditional: true,
            method: 'post',
            url: getRootPath() + '/user/updatePassword',
            async: false,
            dataType: 'json',
            success: function (result) {
                if (result.code == 10000) {
                    $("#password_edit_dialog").dialog("close");
                    user_tool.form_clear();
//                    user_tool.init_main_view();
                    $("#user_grid").datagrid("reload");
                    common_tool.messager_show(result.msg);
                    return false;
                }
                else {
                    common_tool.messager_show(result.msg);
                }
            },
        });
    },
    forbiddenUser: function (id) {
        $.ajax({
            data: {
                id: id,
            },
            traditional: true,
            method: 'get',
            url: getRootPath() + '/user/forbiddenUser',
            async: false,
            dataType: 'json',
            success: function (result) {
                if (result.code == 10000) {
                    user_tool.form_clear();
//                    user_tool.init_main_view();
                    $("#user_grid").datagrid("reload");
                    common_tool.messager_show(result.msg);
                    return false;
                }
                else {
                    common_tool.messager_show(result.msg);
                }
            },
        });
    },
    enableUser: function (id) {
        $.ajax({
            data: {
                id: id,
            },
            traditional: true,
            method: 'get',
            url: getRootPath() + '/user/enableUser',
            async: false,
            dataType: 'json',
            success: function (result) {
                if (result.code == 10000) {
                    user_tool.form_clear();
//                    user_tool.init_main_view();
                    $("#user_grid").datagrid("reload");
                    common_tool.messager_show(result.msg);
                    return false;
                }
                else {
                    common_tool.messager_show(result.msg);
                }
            },
        });
    },

};
$(document).ready(function () {
    user_tool.init_main_view();
    // 新增
    $("#user-save-btn").click(function () {
        user_tool.init_edit_view(1);
        $("#user_form").form('load', {
        	sex:"1",
        });
    });
    // 修改
    $("#user-update-btn").click(function () {
        var users = $("#user_grid").datagrid('getChecked');
        if (users.length == 0) {
            common_tool.messager_show("请至少选择一条记录");
            return false;
        }
        $("#user_form").form('load', {
            id: users[0].id,
            loginName: users[0].loginName,
            zhName: users[0].zhName,
            enName: users[0].enName,
            sex: users[0].sex,
            birth: users[0].birth,
            email: users[0].email,
            phone: users[0].phone,
            address: users[0].address,
            password: '111111111111',
        });
        user_tool.init_edit_view(2);
    });
    // 删除
    $("#user-delete-btn").click(function () {
        var users = $("#user_grid").datagrid('getChecked');
        if (users.length == 0) {
            common_tool.messager_show("请至少选择一条记录");
            return false;
        }
        $.messager.confirm('确认对话框', "您确认删除该条记录吗?", function (r) {
            if (r) {
                user_tool.delete(users[0].id);
            }
        });
    });
  
    $("#user-enable-btn").click(function () {
        var users = $("#user_grid").datagrid('getChecked');
        if (users.length == 0) {
            common_tool.messager_show("请至少选择一条记录");
            return false;
        }
        if (users[0].status == 1) {

            common_tool.messager_show("该账号已经处于启用状态");
            return false;
        }
        $.messager.confirm('确认对话框', "您确认启用 " + users[0].zhName + " 账号吗?", function (r) {
            if (r) {
                user_tool.enableUser(users[0].id)
            }
        });
    });
    $("#user-forbidden-btn").click(function () {
        var users = $("#user_grid").datagrid('getChecked');
        if (users.length == 0) {
            common_tool.messager_show("请至少选择一条记录");
            return false;
        }
        if (users[0].status == 3) {
            common_tool.messager_show("该账号已经处于禁用状态");
            return false;
        }
        $.messager.confirm('确认对话框', "您确认禁用 " + users[0].zhName + " 账号吗?", function (r) {
            if (r) {
                user_tool.forbiddenUser(users[0].id)
            }
        });
    });
    $("#user-flash-btn").click(function () {
        user_tool.form_clear();
//        user_tool.init_main_view();
        $("#user_grid").datagrid("reload");
    });
    $("#log-select-btn").click(function () {
        user_tool.init_main_view();
    });
    $("#user-password-btn").click(function () {
        var users = $("#user_grid").datagrid('getChecked');
        if (users.length == 0) {
            common_tool.messager_show("请至少选择一条记录");
            return false;
        }
        user_tool.password_view(users[0]);
    });
    
    /**添加用户权限*/
    $("#user-addpermission-btn").click(function() {
    	 var users = $("#user_grid").datagrid('getChecked');
         if (users.length == 0) {
             common_tool.messager_show("请至少选择一条记录");
             return false;
         }
         var userId=users[0].id;
         var loginName=users[0].loginName;
//         console.log("--->"+userId+"--enName-->"+loginName);
         user_tool.add_user_permission(1,userId,loginName);
	})
    
    
    
    
    
    
    
    
    
});