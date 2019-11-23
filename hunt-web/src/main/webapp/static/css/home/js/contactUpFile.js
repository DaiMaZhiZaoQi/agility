/**
 *  企业通讯录处理
 */

(function(){
	var nameSpace=window.NameSpace||{};
	nameSpace.data={
			contactId:0,
			upStatus:0,
			/**文件数据*/
			fileListData:"",
			/**当前状态 0,已授权 1,未授权*/
			currState:0,
			/**授权或是未授权数据*/
			authStateData:"",
			/**是否显示上级 0,默认不显示 1,显示上级*/
			isLoadUp:0,
	};
	
	 
	contact={
			fileDefault:function(){
				var tbody=$("<tbody></tbody>");
				var tr=$("<tr></tr>");
				tr.css("background-color","#f3f3f3");
				tr.append("<td>"+0+"</td>");
				tr.append("<td> </td>");
				tr.append("<td> </td>");
				tr.append("<td> </td>");
				tr.append("<td> </td>");
				tr.append("<td> </td>");
				tbody.append(tr);
				$("#contactAllTable table[id=tabFile]").append(tbody);
			},
			fileData:function(rows){// style='overflow-y: scroll;display: block;height: 200px;width:100%'
				$("#contactAllTable table[id=tabFile] tbody").remove();
				if(rows.length<=0){
//					contact.fileDefault();
					common_tool.messager_show("没有找到企业通讯录");
					return;
				}
//				console.log(tag);
//				$("#contactAllTable table tbody").remove();
				var tableBd=$("<tbody></tbody>");
				
				nameSpace.data.fileListData=rows;
				for(var i=0;i<rows.length;i++){
					var obj=rows[i];
					var orgName=obj.orgName;
					var objId=obj.id;
					var oriFileName=obj.oriFileName;
					var authName=obj.authName;
					var contactCode=obj.contactCode;
					var tr=$("<tr></tr>");
					if(i%2==1){
						tr.css("background-color","#f3f3f3");
					}else {
						tr.css("background-color","#ffffff");
					}
					tr.append("<td>"+(i+1)+"</td>");
					tr.append("<td class='tdpd'>"+(orgName==null?"无":orgName)+"</td>");
					tr.append("<td class='tdpd'>"+(oriFileName==null?"无":oriFileName)+"</td>");
					tr.append("<td class='tdpd'>"+(contactCode==null?"无":contactCode)+"</td>");
					tr.append("<td class='tdpd'>"+(authName==null?"无":authName)+"</td>");
					tr.append("<td><input type='checkbox' name='ck' id='"+i+"'/></td>");
					$("#tabFile").off("change","input[id="+i+"]");
					tableBd.append(tr);
					$("#tabFile").on("change","input[id="+i+"]",function(){
						var checked=$(this).prop("checked");
						if(checked){
							var tagList=$("#tabFile input[type=checkbox]");
							var tagId=$(this).attr("id");
							for(var i=0;i<tagList.length;i++){
									var aaaId=tagList[i].id;
									if(tagId!=aaaId){
										tagList[i].checked=false;
									}else {
										if(nameSpace.data.currState==0){		//  已授权
											contact.refreshAuthUser(rows[i].id);
										}else{		//  未授权
											contact.refreshUnAuth(rows[i].id);
										}
									}
							}
						}
					});
				}
				$("#contactAllTable table[id=tabFile]").append(tableBd);
				$("#tabFile input[id=0]").attr("checked",true);
				console.log("通讯录授权状态"+nameSpace.data.currState);
				if(nameSpace.data.currState==0){
					 $("#contactAllTable span[id=auth]").click();
				}else{
					contact.refreshUnAuth(rows[0].id);
				}
				
			},
			
			fillAuth:function(rows){
				nameSpace.data.authStateData=rows;
				$("#contactAllTable table[id=userOrg] tbody").remove();
				if(rows.length<=0){
//					contact.fileDefault();
					return;
				}
				var tbody=$("<tbody></tbody>");
				for(var i=0;i<rows.length;i++){
					var row=rows[i];
					var orgName=row.orgName;
					var loginName=row.loginName;
					var zhName=row.zhName;
					var tr=$("<tr></tr>");
					if(i%2==1){
						tr.css("background-color","#f3f3f3");
					}else{
						tr.css("background-color","#ffffff");
					}
					tr.append("<td>"+(i+1)+"</td>");
					tr.append("<td class='tdpd'>"+orgName+"</td>");
					tr.append("<td class='tdpd'>"+loginName+"</td>");
					tr.append("<td class='tdpd'>"+zhName+"</td>");
					tr.append("<td><input type='checkbox' name='ck' id='"+i+"'/></td>");
					$("#userOrg").off("change","input[id="+i+"]");
					tbody.append(tr);
					$("#userOrg").on("change","input[id="+i+"]",function(){
						var checked=$(this).prop("checked");
						if(checked){
							var id=$(this).attr("id");
							var rowData=rows[id];
							if(rowData.isAuth==1){
								alert("该选项是文件的作者，默认持有文件权限");
								$(this).attr("checked",false);
								return;
							}
						}
					});
				}
				$("#contactAllTable table[id=userOrg]").append(tbody);
			},
			
			
			/**改变点击时的颜色*/
			changeColor:function(obj){
				var curId=$(obj).attr("id");
				console.log(curId);
				
				if(curId=="noAuth"){
					var upOrg=$("#authManage span[id=upOrg]");
					upOrg.show();
				}else{
					var upOrg=$("#authManage span[id=upOrg]");
					upOrg.hide();
				}
				
				$(obj).siblings().css("background-color","#007ad5");
				$(obj).css("background-color","#3395de");
			},
			
			/**确认上传文件，并授权*/
			confirmUp:function(){
					if(!contact.sychPassword()){
						return;
					}
					if(nameSpace.data.upStatus==1){
//						alert("已上传成功");
					}
						var userId=common_tool.getCurrUserId();
						var synPwd=$("#contactContent input[id=inputSychPwd]").val();
						var objDate={
								userId:userId,
							   contactSychPassword:synPwd,
							   contactId:nameSpace.data.contactId,
						};
					$.ajax({
						url:getRootPath()+"/contact/upContact",
						dataType:"json",
						data:objDate,
						type:"POST",
						success:function(data){
							console.log(data);
							 if (data.code == '10000') {	
								 $("#contactContent input[id=clickFile]").css({"color":"black"});
					            	$("#contactContent input[id=clickFile]").val(data.msg);
//					            	$("#contactContent button[id=btn_confirm_up]").text("已激活");
					            	nameSpace.data.upStatus=1;
					            	nameSpace.data.contactId="";
					            	contact.refreshFile();
					            	$("#contactContent input[id=inputSychPwd]").val("");
					            	$("#contactContent input[id=conSychPwd]").val("");
							 }else{
								 $("#contactContent input[id=clickFile]").css({"color":"red"});
					            	$("#contactContent input[id=clickFile]").val(data.msg);
//					            	$("#contactContent button[id=btn_confirm_up]").text("激活");
							 }
							
						},
						
						error:function(data){
							console.log(data);
						},
					});
			},
			
			sychPassword:function(){
				var inputSychPwd=$("#contactContent input[id=inputSychPwd]");
				var pwd1=inputSychPwd.val();
				var conSychPwd=$("#contactContent input[id=conSychPwd]");
				var pwd2=conSychPwd.val();
				if(pwd1==null||pwd1.length==0){
					alert("请输入同步密码");
					return false;
				}
				if(pwd1.length<4){
					alert("请输入同步密码,密码位数大于4");
					return false;
				}
				if(strIsEngNum(pwd1)){
					alert("请输入字母/数字同步密码");
					return false;
				}
				
				if(pwd2==null||pwd2.length==0){
					alert("请再次输入同步密码");
					return false;
				}
				if(pwd2.length<4){
					alert("请再次输入同步密码,密码位数大于4");
					return false;
				}
				if(strIsEngNum(pwd2)){
					alert("请再次输入字母/数字同步密码");
					return false;
				}
				if(pwd2!=pwd1){
					alert("两次输入密码不同，请重新输入")
					return false;
				}
				return true;
			},
			
			/**选择文件并上传*/
			getFilePath:function (){
					// var filePath=$("#contactContent input[type=file]").text();
					// alert("filePath-->"+filePath);
					var file=$("#contactContent input[type=file]")[0].files[0];
					var formData = new FormData();
					var userId=common_tool.getCurrUserId();
					formData.append("file",file);
					formData.append("userId",userId);
					formData.append("contactId",nameSpace.data.contactId);
					$.ajax({
				        url:getRootPath()+"/contact/upFile",
				        dataType:'json',
				        type:'POST',
				        async: false,
				        data: formData,
				        processData : false, // 使数据不做处理
				        contentType : false, // 不要设置Content-Type请求头
				        success: function(data){
				            console.log(data);
				            if (data.code == '10000') {	
				            	var dto=data.data;
				            	var fileName=dto.fileName;
				            	nameSpace.data.contactId=dto.id;
				            	var status=dto.status;
				            	if(status==1){
//				            		$("#contactContent button[id=btn_confirm_up]").text("已激活");
				            		$("#contactContent input[id=clickFile]").css({"color":"black"});
//					            	$("#contactContent input[id=clickFile]").val("上传成功");
					            	$("#contactContent input[id=clickFile]").val(fileName);
				            	}else{
//				            		$("#contactContent button[id=btn_confirm_up]").text("立即激活");
				            		$("#contactContent input[id=clickFile]").css({"color":"black"});
					            	$("#contactContent input[id=clickFile]").val(fileName);
				            	}
				            
				            	contact.refreshFile();
				            }else{
				            	$("#contactContent input[id=clickFile]").css({"color":"red"});
				            	$("#contactContent input[id=clickFile]").val(data.msg);
				            }
				           
				        },
				        error:function(response){
				        	console.log(data);
				        	$("#contactContent input[id=clickFile]").text(response.msg);
				        }
				    });
				 },
				 
			/**刷新文件*/	 
			refreshFile:function(){
				 $.ajax({
					 url:getRootPath()+"/contact/list?userId="+common_tool.getCurrUserId(),
					 type:"GET",
					 success:function(data){
						 contact.fileData(data.rows);
					 },
					 error:function(){
						 
					 },
					 
				 });
			},
			
			/**刷新已授权用户*/
			refreshAuthUser:function(sysContactId){
				if(sysContactId==undefined)return;
				$.ajax({
					url:getRootPath()+"/contact/listAuth?sysContactId="+sysContactId,
					type:"GET",
					success:function(data){
						contact.fillAuth(data.rows);
//						console.log(data.rows);
					},
					error:function(data){
						
					},
				});
			},
			
			refreshUnAuth:function(sysContactId){
				if(sysContactId==undefined)return alert("请选择左侧文件");
				var disUp=nameSpace.data.isLoadUp;
				$.ajax({
					url:getRootPath()+"/contact/listUnAuth?sysContactId="+sysContactId+"&sysOrgId="+common_tool.getHomeRoleOrgId()+"&disUp="+disUp,
					type:"GET",
					success:function(data){
						contact.fillAuth(data.rows);
//						console.log(data.rows);
					},
				});
			},
			
			download:function(id){
				 if(id==undefined)return  alert("请选择文件");
				window.location.href=getRootPath()+"/contact/download?contactId="+id;
			},
			deleteContact:function(id){
				$.ajax({
					url:getRootPath()+"/contact/deleteContact?userId="+common_tool.getCurrUserId()+"&contactId="+id,
					method:"GET",
					success:function(data){
						contact.refreshFile();
						alert(data.msg);
					},
					error:function(data){
						
					},
				});
			},
			/**
			 * 授权给选中用户
			 */
			auth:function(sysUserOrgDtoArr,contactId){
				if(sysUserOrgDtoArr.length<=2||contactId==undefined)return alert("请选择修改项");
				var data={sysUserOrgDto:sysUserOrgDtoArr,
						contactId:contactId};
				console.log(data);
				$.ajax({
					url:getRootPath()+"/contact/auth",
					type:"POST",
					data:data,
					traditional:true,
//					 contentType: "application/json;charset=utf-8",
//					dataType:"json",
					success:function(data){
						if(data.code==10000){
							common_tool.messager_show("操作成功");
							contact.refreshUnAuth(contactId);
						}
						console.log(data);
					}
				});
			},
			
			/**
			 * 授权给部门用户
			 */
			authOrg:function(sysUserOrgDtoArr,contactId){
				if(sysUserOrgDtoArr.length<=2||contactId==undefined)return alert("请选择修改项");
				if(confirm("点击确定，通讯录将授权到所选择的整个部门")){
					var data={sysUserOrgDto:sysUserOrgDtoArr,
							contactId:contactId};
					console.log(data);
					$.ajax({
						url:getRootPath()+"/contact/authOrg",
						type:"POST",
						data:data,
						traditional:true,
//						 contentType: "application/json;charset=utf-8",
//						dataType:"json",
						success:function(data){
							if(data.code==10000){
								common_tool.messager_show("操作成功");
								contact.refreshUnAuth(contactId);
							}
							console.log(data);
						}
					});
				}
				
			},
			
			/**
			 * 解绑选中的个人授权
			 */
			unAuth:function(sysUserOrgDtoArr,contactId){
				if(sysUserOrgDtoArr.length<=2||contactId==undefined)return alert("请选择修改项");
				var data={sysUserOrgDto:sysUserOrgDtoArr,
						contactId:contactId};
				console.log(data);
				$.ajax({
					url:getRootPath()+"/contact/unAuth",
					type:"POST",
					data:data,
					traditional:true,
//					 contentType: "application/json;charset=utf-8",
//					dataType:"json",
					success:function(data){
						if(data.code==10000){
							common_tool.messager_show("操作成功");
							contact.refreshFile();
							$.ajaxSettings.async = false;
							contact.refreshAuthUser(contactId);
						}else {
							alert(data.msg);
						}
					}
				});
			},
			
			/**
			 * 解绑选中部门的授权
			 */
			unAuthOrg:function(sysUserOrgDtoArr,contactId){
				if(sysUserOrgDtoArr.length<=2||contactId==undefined)return alert("请选择修改项");
				if(confirm("点击确定，整个部门不在有权限访问该通讯录")){
					var data={sysUserOrgDto:sysUserOrgDtoArr,
							contactId:contactId};
					console.log(data);
					$.ajax({
						url:getRootPath()+"/contact/unAuthOrg",
						type:"POST",
						data:data,
						traditional:true,
//						 contentType: "application/json;charset=utf-8",
//						dataType:"json",
						success:function(data){
							if(data.code==10000){
								common_tool.messager_show("操作成功");
								contact.refreshAuthUser(contactId);
							}else{
								alert(data.msg);
							}
						}
					});
				}
				
			},
			
			/**
			 * 获取文件列表的选中项contactId，仅限单选
			 */
			getFileCheck:function(){
				 var fileData=nameSpace.data.fileListData;
				 var checkBoxs=$("#tabFile input[type=checkbox]");
				 for(var i=0;i<checkBoxs.length;i++){
					 var cb=checkBoxs[i];
					 if(cb.checked){
						var checkId=cb.id;
						return fileData[checkId].id;
					 }
				 }
			},
			
			/**
			 * 已选中的的用户 return Array
			 */
			getUserCheck:function(){
				 var tagList=$("#userOrg input[type=checkbox]");
				 var data=nameSpace.data.authStateData;
				 var authData=new Array();
				 for(var i=0;i<tagList.length;i++){
					 if(tagList[i].checked){
						 var checkId=tagList[i].id;
						 if(data[checkId].isAuth==0){
							 authData.push(data[checkId]);	// 获取已选中的用户
						 }
					 }
				 }
				 return authData;
			}
		};
	
	$(document).ready(function(){
		
		 $("#contactHead").load("contactHead.html");
	     $("#contactContent button[id=btnClick]").click(function(){
	    	 $("#contactContent input[type=file]").val("");
	    	 $("#contactContent input[id=clickFile]").val("");
	    	 $("#contactContent input[type=file]").click();
//	    	 $("#contactContent button[id=btn_confirm_up]").text("激活");
	     });
		 
		 $("#contactContent button[id=btn_confirm_up]").click(function(){
			contact.confirmUp();
		 });
		 
		 $("#contactAllTable span[id=auth]").click(function(){
			 var contactId=contact.getFileCheck();
			 if(contactId==undefined)return alert("请选择左侧文件");
			 contact.changeColor(this);
//			 var tagList=$("#tabFile input[type=checkbox]");
			 nameSpace.data.currState=0;
			 var hasCheck=false;
			 $("#authManage button[id=person]").text("解除授权");
			 $("#authManage button[id=partment]").text("部门解除授权");
			 contact.refreshAuthUser(contactId);
			 
		 });
		 var dispUp=0;
		 $("#authManage span[id=upOrg]").click(function() {
			 if(dispUp%2==0){
				 $("#authManage span[id=upOrg]").text("隐藏上级");
				 nameSpace.data.isLoadUp=1;
				 contact.refreshUnAuth(contact.getFileCheck());
			 }else {
				 $("#authManage span[id=upOrg]").text("显示上级");
				 nameSpace.data.isLoadUp=0;
				 contact.refreshUnAuth(contact.getFileCheck());
			}
			dispUp++;
		});
		 
		 $("#authManage button[id=person]").click(function(){
			 var contactId=contact.getFileCheck();
			 if(contactId==undefined)return  alert("请选中左侧文件");
			 var authData=contact.getUserCheck();
			 console.log(authData);
			 if(nameSpace.data.currState==0){			//已授权中解除授权
				 var str=JSON.stringify(authData);
				 console.log("解除已授权-->"+str);
				 contact.unAuth(str,contactId);
				 
			 }else{										//未授权执行授权
				 var str=JSON.stringify(authData);
				 console.log("待授权-->"+str);
				 contact.auth(str,contactId);
			 }
		 });
		 
		 $("#authManage button[id=partment]").click(function(){
			 var contactId=contact.getFileCheck();
			 if(contactId==undefined)return  alert("请选中左侧文件");
			 var authData=contact.getUserCheck();
			 console.log(authData);
			 if(nameSpace.data.currState==0){			//已授权中解除授权
				 var str=JSON.stringify(authData);
				 console.log("解除已授权-->"+str);
				 contact.unAuthOrg(str,contactId);
				 
			 }else{										//未授权执行授权
				 var str=JSON.stringify(authData);
				 console.log("待授权-->"+str);
				 contact.authOrg(str,contactId);
			 }
			 
		 });
		 
		 $("#contactAllTable span[id=noAuth]").click(function(){
			 var contactId=contact.getFileCheck();
			 if(contactId==undefined)return alert("请选择左侧文件");
			 contact.changeColor(this);
			 nameSpace.data.currState=1;
//			 var tagList=$("#tabFile input[type=checkbox]");
			 var hasCheck=false;
			 $("#authManage button[id=person]").text("授权使用");
			 $("#authManage button[id=partment]").text("授权给部门");
			
			 
			 contact.refreshUnAuth(contactId);
		 });
		 
		 

		 
		 
		 $("#contactAllTable button[id=btn_down_delete]").click(function(){
			 
			 var contactId=contact.getFileCheck();
			 if(contactId==undefined)return  alert("请选择文件");
			 if(confirm("删除后文件不能恢复，是否删除")){
				 contact.deleteContact(contactId);
			 }		
		 });
//			$("#testCheck").attr("checked",true);
		 $("#contactAllTable button[id=btn_down_file]").click(function(){
			 var contactId=contact.getFileCheck();
			 contact.download(contactId);
		 });
		
		 
	
		 
		 $("#contactAllTable table[id=contactList]").datagrid({
			  url:getRootPath()+"/contact/list?userId="+common_tool.getCurrUserId(),
			  method:"GET",
			  idField:'id',
         	  singleSelect: true,
             fitColumns: true,
             rownumbers: true,
             columns:[[
            	 {field:'id',title:'选择',width:100,align:'left',checkbox: "true",formatter:function(value,row,index){
            		 return row.id;
            	 }},
         		{field:'oriFileName',title:'通讯录',width:100,align:'left',formatter:function(value,row,index){
         			return row.oriFileName;
         		}}
             ]],
		 });
		 
		 $("#contactAllTable ul[id=orgList]").tree({
				method:"GET",
				checkbox:true,
				url:getRootPath() + '/organization/listAuthContact?id='+(common_tool.getHomeRoleOrgId())+'&queryType=1',    //  不同id显示内容不同，在登录时注意接收不同的id
				loadFilter: function(data){
					return data.rows;
				},
				formatter:function(node){							  // 子节点加载成功后加载该部门下的成员设备
					return node.name;
				},
		  		onLoadSuccess:function(obj,data){						  // 加载成员设备
		  			homeDevic.init();
		  			common_tool.setHomeRoleOrgId(data[0].id);
		  			deviceOrgId=data[0].id;
		  			globOptType=0;
		  			glolbalId=deviceOrgId;
		  			var href=getRootPath()+"/device/allDevice?orgId="+data[0].id+"&isLoadAll=1";
		  			currHref=href;
//					 $(".main-container").load(href);
					 $("#tt li:eq(0)").find("#_easyui_tree_1").addClass("tree-node-selected");
		  		},
		  		
				/*onClick:function(node){
					var id=node.id;
					glolbalId=id;
					var orgType=node.orgType;
					globOptType=orgType;
					var nodeName=node.name;
					console.log("node-->"+id+"node-->"+nodeName);
					if(clickId!=id){
						var selectStatus=common_tool.getHomeSelectStatus();
						if(1==selectStatus){
							var optType=orgType==0?0:1;
							console.log("selectStatus--->"+optType+"--id-->"+id);
							recordModult.initSearContent();
							recordModult.refreshPage(optType,id);	
							recordModult.refreshTotal(optType,id);
							recordModult.refreshCallLog(optType,id,"call_date","DESC",1,-1);
							$("#td_allDevice_title span[id=span_title]").text(nodeName+"通话记录");
						}else{
							var parametes="";
							var selected = $('#tt').tree('getSelected');
							if(orgType==0){					//  机构类型
								parametes="orgId="+id;
								if(selected.target.id=="_easyui_tree_1"){
									parametes=parametes+"&isLoadAll=1";
								}
							}else if(orgType==1){			//   个人类型
								parametes="personalId="+id;
							}
							var href=getRootPath()+"/device/allDevice?"+parametes;
							currHref=href;
							var text=$(".tree-title").text();
							 $(".main-container").load(href,function(){		//   加载成功后回调
								 $("#td_allDevice_title").text(nodeName);
							 });
							 clickId=id;
						}
					}
				}*/
			});
		 contact.refreshFile();
//		 $("#contactAllTable span[id=auth]").click();
	 });
})();
 