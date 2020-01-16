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
			/**文件名*/
			fileName:"",
			
			/**当前页码 */
			currentPage:1,
			/**总页码 */
			totalPage:1,
			/**数据总量  */
			totalCount:"",
			/**全局排序 */
			globalSort:"update_time",
			/**全局排序方式 */
			globalOrder:"desc",
	};
	
	var pageDtoObj={
			/**排序字段,常用 create_time*/
			sort:"update_time",
			/**排序方式，一般用 DESC*/
			order:"desc",
			page:1,
			rows:15,
			
		
	};
	 
	contact={
			/**
			 * 根据机构Id更新
			 */
			updateAuth:function(checkContactId,orgUserData){
				var all=orgUserData.datagrid("getData");
				var allUserData=all.rows;
				console.log("all-->"+JSON.stringify(all));
				if(allUserData.length<=0){
					 $("#update_auth_dialog").dialog('close');
						contact.form_clear();
					return;
				}
				var checkObj=null;
				try {
					if(confirm("是否更新通讯录授权,未勾选的,通讯录不会授权给他")){
						checkObj=orgUserData.datagrid("getChecked");
//						if(checkObj.length==0){
//							common_tool.messager_show("请选择右侧用户");
//							return;
//						}
						
					}else{
						return;
					}
				} catch (e) {
					common_tool.messager_show("请选择右侧用户");
					 return;
				}
				
				$.ajax({
					method:"POST",
					 traditional: true,
		             url: getRootPath() + '/contact/auth',
		             dataType: 'json',
					data:{
						contactId:checkContactId,
						sysUserId:common_tool.getCurrUserId(),
//						listSysOrgPerStr:JSON.stringify(checkObj),
						listSysUserInOrg:JSON.stringify(checkObj),
						listSysUserInOrgAll:JSON.stringify(allUserData),
					},
					success:function(result){
						if(result.code==CONTACT_DELETE.pCode){
							common_tool.show.messager_show(CONTACT_DELETE.pMsg);
							return;
						}
						common_tool.messager_show("更新授权成功");
						 $("#update_auth_dialog").dialog('close');
						contact.refreshFile(1);
						contact.form_clear();
					}
						
				});
			},
			
			form_clear:function(){
				try {
					$("#update_auth_dialog table[id='auth_user_orgs']").treegrid("uncheckAll");
					$("#update_auth_dialog table[id='org_user']").datagrid("uncheckAll");
				} catch (e) {
				}
			},
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
//					common_tool.messager_show("没有找到企业通讯录");
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
					var createTime=obj.createTime;
					var updateTime=obj.updateTime;
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
					tr.append("<td class='tdpd'>"+common_tool.getMoth(createTime)+"</td>");
					tr.append("<td class='tdpd'>"+common_tool.getMoth(updateTime)+"</td>");
					tr.append("<td id="+i+" style='display: table-cell;'>" +
							/*"<input type='checkbox' name='ck' id='"+i+"'/>" +*/
							"<span class='contactTdDelete contactDelete' id='btn_delete_"+i+"'>删除</span>"+
							"<span class='contactDown' id='btn_simpleDown_"+i+"'>普通下载</span>"+
							"<span class='contactDown' id='btn_dec_down_"+i+"'>解密下载</span>"+
							"<span class='contactAuth contactDelete' id='btn_auth_"+i+"'>授权</span>"+
					"</td>");
					$("#tabFile").off("click","span[id=btn_delete_"+i+"]");
					$("#tabFile").off("click","span[id=btn_simpleDown_"+i+"]");
					$("#tabFile").off("click","span[id=btn_dec_down_"+i+"]");
					$("#tabFile").off("click","span[id=btn_auth_"+i+"]");
					
					$("#tabFile").on("click","span[id=btn_delete_"+i+"]",function(){
						var tagId=$(this).parent().attr("id");
						console.log("btn_delete_parent"+tagId);
						if(confirm("删除后文件不能恢复，是否删除")){
							var fileData=nameSpace.data.fileListData;
							 contact.deleteContact(fileData[tagId].id);
						 }
					});
					
					$("#tabFile").on("click","span[id=btn_simpleDown_"+i+"]",function(){
						console.log("btn_simpleDown_");
						  var tagId=$(this).parent().attr("id");
						  var fileData=nameSpace.data.fileListData;
						contact.download(fileData[tagId].id);
					});
					
					$("#tabFile").on("click","span[id=btn_dec_down_"+i+"]",function(){
						console.log("btn_dec_down_");
						var password=prompt("请输入同步密码");
						  if (password!=null && password!=""){
							  var tagId=$(this).parent().attr("id");
							  var fileData=nameSpace.data.fileListData;
								 contact.download(fileData[tagId].id,password);
						  }
					});
					
					$("#tabFile").on("click","span[id=btn_auth_"+i+"]",function(){
						console.log("btn_auth_");
						var tagId=$(this).parent().attr("id");
						var fileData=nameSpace.data.fileListData;
						var listUserId=fileData[tagId].listUserId;
						showAuthDialog(fileData[tagId].id,fileData[tagId].listOrgId,listUserId);
					});
					
					
					
//					$("#tabFile").off("change","input[id="+i+"]");
					tableBd.append(tr);
//					$("#tabFile").on("change","input[id="+i+"]",function(){
//						var checked=$(this).prop("checked");
//						if(checked){
//							var tagList=$("#tabFile input[type=checkbox]");
//							var tagId=$(this).attr("id");
//							for(var i=0;i<tagList.length;i++){
//									var aaaId=tagList[i].id;
//									if(tagId!=aaaId){
//										tagList[i].checked=false;
//									}else {
//										if(nameSpace.data.currState==0){		//  已授权
//											contact.refreshAuthUser(rows[i].id);
//										}else{		//  未授权
//											contact.refreshUnAuth(rows[i].id);
//										}
//									}
//							}
//						}
//					});
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
					tr.append("<td>" +
								"<input type='checkbox' name='ck' id='"+i+"'/>" +
							"</td>");
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
					            	contact.refreshFile(1);
					            	$("#contactContent input[id=inputSychPwd]").val("");
					            	$("#contactContent input[id=conSychPwd]").val("");
							 }else if(data.code=="30019"){
								 $("#contactContent input[id=clickFile]").css({"color":"red"});
								 $("#contactContent input[id=clickFile]").val(data.msg);
							 }else{
								 $("#contactContent input[id=clickFile]").css({"color":"red"});
								 $("#contactContent input[id=clickFile]").val(data.msg);
								 nameSpace.data.contactId="";
								 
							 }
							
						},
						
						error:function(data){
							console.log(data);
						},
					});
			},
			
			/**
			 * 刷新授权
			 */
			refreshAuth:function(){
				 var gloPer=common_tool.getGloPer();
					if(gloPer.indexOf("contact:insert")>-1){
						$(".contactInsert").show();
					}
					if(gloPer.indexOf("contact:select")>-1){
						$(".contactSelect").show();
					}
					if(gloPer.indexOf("contact:delete")>-1){
						$(".contactDelete").show();
					}
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
//					formData.append("file", decodeURIComponent(escape(window.atob(file))));
					formData.append("userId",userId);
//					if(nameSpace.data.fileName!=file.name){		//  解决两次文件上传，第一次上传失败，接着上传第二次，id号一致，不一致为正常，
//						
//					}
					formData.append("contactId",nameSpace.data.contactId);
					$.ajax({
				        url:getRootPath()+"/contact/upFile",
				        dataType:'json',
				        type:'POST',
				        async: false,
				        data: formData,
				        processData : false, // 使数据不做处理
				        dataType:"json",
//				        contentType : "multipart/form-data", // 不要设置Content-Type请求头
				        contentType : false, // 不要设置Content-Type请求头
				        success: function(data){
				            console.log(data);
				            if (data.code == '10000') {	
				            	var dto=data.data;
				            	var fileName=dto.fileName;
				            	nameSpace.data.fileName=fileName;
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
				            
				            	contact.refreshFile(1);
				            	
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
				 
			/**加载数据 刷新文件*/	 
			refreshFile:function(init){
				pageDtoObj.page=nameSpace.data.currentPage;
			
				if(init==1){
					$.ajaxSettings.async = false;
					var pageToolTag=$("#contactPageTool");
					if(pageToolTag!=null){
						pageToolTag.remove();
					}
					$.get("pageToolContact.html",function(data){	
						$("#contactPage").append(data);
					});
//					var url=getRootPath()+"/contact/list?userId="+common_tool.getCurrUserId()+"&pageDto="+JSON.stringify(pageDtoObj);
//					$.ajaxSettings.async = false;
				}
				var selectEdRows=$("#contactPageTool select[id=iEveryCount] option:selected").val();
				console.log("selectEdRows-->"+selectEdRows);
				if(selectEdRows==undefined){
					selectEdRows=1;
				}
		    	pageDtoObj.rows=selectEdRows;
				var dataObj={
						userId:common_tool.getCurrUserId(),
						pageDto:JSON.stringify(pageDtoObj),
				}
				 $.ajax({
					 url:getRootPath()+"/contact/list",
					 type:"POST",
					 data:dataObj,
					  traditional: true,
				        dataType: "json",
					 success:function(data){
						 if(init==1){		//  初始化页面数量
							 var dataCount=data.total;
							 if(typeof(dataCount)=="number"){
								 var selectEdRows=$("#contactPageTool select[id=iEveryCount] option:selected").val();
									var totalPage=dataCount%selectEdRows==0?dataCount/selectEdRows:Math.floor((dataCount/selectEdRows)+1);
									nameSpace.data.totalPage=totalPage;
									
									$("#contactPageTool span[id=currentPage]").text("当前1/"+totalPage+"页");
									nameSpace.data.totalCount=dataCount;
									var tagName=$("#contactPageTool span[id=totalPage]");
									console.log("标签"+tagName.attr("id"));
									$("#contactPageTool span[id=totalPage]").text("共"+dataCount+"条");
							 }
						 }else{
							 $("#contactPageTool span[id=currentPage]").text("当前"+pageDtoObj.page+"/"+nameSpace.data.totalPage+"页");
						 }
						 contact.fileData(data.rows);
						 contact.refreshAuth();
					 },
					 error:function(){
						 
					 },
					 
				 });
				 
			},
			
			/**刷新已授权用户*/
			refreshAuthUser:function(sysContactId){
//				if(sysContactId==undefined)return;
//				$.ajax({
//					url:getRootPath()+"/contact/listAuth?sysContactId="+sysContactId,
//					type:"GET",
//					success:function(data){
//						contact.fillAuth(data.rows);
////						console.log(data.rows);
//					},
//					error:function(data){
//						
//					},
//				});
			},
			
			refreshUnAuth:function(sysContactId){
//				if(sysContactId==undefined)return alert("请选择左侧文件");
//				var disUp=nameSpace.data.isLoadUp;
//				$.ajax({
//					url:getRootPath()+"/contact/listUnAuth?sysContactId="+sysContactId+"&sysOrgId="+common_tool.getHomeRoleOrgId()+"&disUp="+disUp,
//					type:"GET",
//					success:function(data){
//						contact.fillAuth(data.rows);
////						console.log(data.rows);
//					},
//				});
			},
			
			download:function(id,password){
				 if(id==undefined)return  alert("请选择文件");
				 if(password!=null && password.length>0){
					 $.ajax({
						 method:"GET",
						 url:getRootPath()+"/contact/download?contactId="+id+"&userId="+common_tool.getCurrUserId()+"&password="+password,
						 success:function(data){
							 if(data.code==10000){
								 window.location.href=getRootPath()+"/contact/download?contactId="+id+"&userId="+common_tool.getCurrUserId()+"&password="+password+"&passwordOk="+1;				 
							 }else{
								 if(data.msg!=null){
									 common_tool.messager_show(data.msg);
								 }else{
									 common_tool.messager_show(data.pMsg);
								 }
							 }
						 }
					 })
				 }else{
					 window.location.href=getRootPath()+"/contact/download?contactId="+id+"&userId="+common_tool.getCurrUserId();
				 }
				
			},
			deleteContact:function(id){
				$.ajax({
					url:getRootPath()+"/contact/deleteContact?userId="+common_tool.getCurrUserId()+"&contactId="+id,
					method:"GET",
					success:function(data){
						contact.refreshFile(1);
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
			 * 获得选中下标
			 */
			getFileCheckIndex:function(){
				var fileData=nameSpace.data.fileListData;
				var checkBoxs=$("#tabFile input[type=checkbox]");
				for(var i=0;i<checkBoxs.length;i++){
					var cb=checkBoxs[i];
					if(cb.checked){
						return i;
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
	
	/**
	 * 通话记录分页
	 */
	contactPageTool={
			   /**下一页*/
		    nextPage:function(){
		    	if( nameSpace.data.currentPage==nameSpace.data.totalPage)return;
			    nameSpace.data.currentPage++;
			    console.log("当前页码"+nameSpace.data.currentPage);
			    if(nameSpace.data.currentPage>=nameSpace.data.totalPage){
			    	nameSpace.data.currentPage=nameSpace.data.totalPage;
			    }
			    contact.refreshFile();
		    },
		    
		    /**当前页*/
//		    currPage:function(){
//		    	recordModult.refreshCallLog(NameSpace.data.get_optType(),NameSpace.data.get_orgIdUserId(),NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder(),8,-1);
//		    },
		    
		    /**上一页*/
		    prePage:function(){
		    	if( nameSpace.data.currentPage==1)return;
		    	 nameSpace.data.currentPage--;
				    if(nameSpace.data.currentPage<=1){
				    	nameSpace.data.currentPage=1;
				    }
				    contact.refreshFile();
		    },
		    /**修改每一页数量*/
		    changePages:function(){
		    	var rows=$("#contactPageTool select[id=iEveryCount] option:selected").val();
		    	var page=(nameSpace.data.totalCount%rows)==0?(nameSpace.data.totalCount/rows):(Math.floor((nameSpace.data.totalCount/rows))+1);
		    	nameSpace.data.totalPage=page;
		    	nameSpace.data.currentPage=1;
		    	 contact.refreshFile();
		    },
		    
		    /**跳转*/
		    jumpPage:function(){
		    	var page=$("#contactPageTool input[type=text]").val();
		    	console.log("jumpPage-->"+page);
		    	if(page!=null){
			    	if(typeof(parseInt(page))=="number"){
				    	if(page>nameSpace.data.totalPage){
				    		nameSpace.data.currentPage=nameSpace.data.totalPage;
				    	}else if(page<1){
				    		nameSpace.data.currentPage=1;
				    	}else{
				    		nameSpace.data.currentPage=page;
				    	}
				    	contact.refreshFile();
			    	}
		    	}
		    },
		    /**首页*/
		    firstPage:function(){
		    	nameSpace.data.currentPage=1;
		    	contact.refreshFile();
		    },
		    /**尾页*/
		    endPage:function(){
		    	nameSpace.data.currentPage=nameSpace.data.totalPage;
		    	contact.refreshFile();
		    },
	}
	
	 /**
	  * 显示授权dialog
	  */
	 function showAuthDialog(checkContactId,listOrgId,listUserId){
		 var orgUserData=null;
		 var orgsLeft=null;
		 var dataNet=null;
		$("#update_auth_dialog").dialog({
			  title: "通讯录授权",
	            iconCls: 'icon-save',
	            closable: true,
	            width: 1000,
	            height: 700,
	            cache: false,
	            modal: true,
	            resizable: false,
	           
	            'onOpen': function () {
	            	
	            	var params="?id="+common_tool.getCurrUserId();
	            	var time=null;
	            	 $("#update_auth_dialog table[id='auth_user_orgs']").treegrid({
                    		url:getRootPath()+"/organization/list"+params,
                			method:"GET",
                			
                			 "onCheck":function(rowData){
                 				if(rowData==null)return;
//                 				console.log("选中执行"+JSON.stringify(rowData));
                 				rowData.checkState=1;
                 				rowData.checked=true;
                 				console.log("rowData-children--->"+rowData.children.length);
                 					if(rowData.children.length!=0){	// 子节点全部选中
	                 					console.log("有子节点");
	                 					var children=orgsLeft.treegrid("getChildren",rowData.id);
//	                 					console.log(children);
	                 					for(var j=0;j<children.length;j++){
	                 						orgsLeft.treegrid("select",children[j].id);
	                 					}
//	                 					 var orgs=orgsLeft.treegrid("getSelected");
//	                 	                 console.log("机构集合"+JSON.stringify(orgs));
	                 				}
	                 				if(time!=null){
		           						clearTimeout(time);
		           						time=null;
	                 				}
                 				time=setTimeout(getOrgCheck, 300*2);
                 			},
                 			"onSelectAll":function(rowData){
                 				if(time!=null){
           						clearTimeout(time);
           						time=null;
           					}
                 				time=setTimeout(getOrgCheck, 200*2);
                 			},
                			"onUnselectAll":function(rowData){
                				console.log("取消全选");
                				for(var i=0;i<rowData.length;i++){
                					rowData[i].checkState=0;
                    				rowData[i].checked=false;
                					
                				}
                				if(time!=null){
           						clearTimeout(time);
           						time=null;
           					}
                 				time=setTimeout(getOrgCheck, 200*2);
                			},
                			"onSelectAll":function(rowData){
                				for(var i=0;i<rowData.length;i++){
                					rowData[i].checkState=1;
                    				rowData[i].checked=true;
                				}
                				if(time!=null){
           						clearTimeout(time);
           						time=null;
           					}
                 				time=setTimeout(getOrgCheck, 200*2);
                			},
                			"onUncheck":function(rowData){
                				rowData.checkState=0;
                				rowData.checked=false;
                				var parent= orgsLeft.treegrid("find",rowData.parentId);
                				if(parent!=null&&parent.checked==true){
                					rowData.checked=true;
                					rowData.checkState=1;
//                					$("#auth_user_orgs").treegrid("select",rowData.id);
                					orgsLeft.treegrid("unselect",rowData.parentId);
                					return;
                				}
                				if(rowData.children.length!=0){   //  是父节点
                					for(var j=0;j<rowData.children.length;j++){
                						orgsLeft.treegrid("unselect",rowData.children[j].id);
                					}
                					return;
                				}
                			
                				if(time!=null){
	           						clearTimeout(time);
	           						time=null;
                				}
                 				time=setTimeout(getOrgCheck, 200*2);
                			},
                			
       		            "onClickRow":function(row){
                				console.log("onClickRow-->"+row.parentId);
                				var parent=orgsLeft.treegrid("find",row.parentId);
                				console.log(parent);
                				if(parent!=null&&parent.checked==true){
                					row.checked=true;
                					row.checkState=1;
                					orgsLeft.treegrid("select",row.id);
                				}
                			},
                			"onLoadSuccess":function(){
                				console.log("onLoadSuccess加载成功");
                				 orgUserData =$("#update_auth_dialog table[id='org_user']");
                				 orgsLeft=$("#auth_user_orgs");
                				for(var i=0;i<listOrgId.length;i++){
                					console.log("checkOrgId-->"+listOrgId[i]);
                					if(listOrgId[i]!=1){
                						orgsLeft.treegrid("select",listOrgId[i]);
                					}
                				}
                			},
	            	 });
	            	 /**
	            	  * 获得选择的item,延时调用
	            	  */
	            	 function getOrgCheck(){
	            		 var cheecks=orgsLeft.treegrid("getChecked");
     	                 console.log("选择的数量"+JSON.stringify(cheecks));
     	                 var array=new Array();
     	                 for(var i=0;i<cheecks.length;i++){
     	                	 var orgId=cheecks[i].id;
     	                	 console.log("orgId-->"+orgId);
     	                	 if(orgId!=undefined){
     	                		 array.push(orgId);
     	                	 }
     	                 }
     	                 console.log("选中的集合"+array.toString());
     	                 try {
     	                	 orgUserData.datagrid("uncheckAll");
						} catch (e) {
							// TODO: handle exception
						}
     	                 if(array.length<=0){
     	                	console.log("选中的集合-->"+array.toString());
     	                	orgUserData.datagrid("loadData",{
     	                		 total:0,
     	                		 rows:[],
     	                	 });
     	                	 return;
     	                 }
     	                 
     	               if(dataNet!=null){
     	            	 dataNet.datagrid("reload",{
     	            		listOrgIds:array.toString(),
 	                		userId:common_tool.getCurrUserId(),
     	            	 });
     	            	 console.log("加载网络数据");
     	               }else{
     	            	  dataNet=orgUserData.datagrid({
      	                	 url:getRootPath()+"/organization/listOrgUser",
      	                	 method:"POST",
      	                	queryParams:{
      	                		listOrgIds:array.toString(),
      	                		userId:common_tool.getCurrUserId(),
      	                	},
      	                 	"onLoadSuccess":function(dataResult){
//      	                 		orgUserData.datagrid("checkAll");
      	                 		for(var i=0;i<listUserId.length;i++){
      	                 			var userId=listUserId[i];
      	                 			orgUserData.datagrid("selectRecord",userId);
      	                 		}
      	                 	}
      	                 
      	                 });
	            	 }
     	               }
     	               
     	           
	            	 
	            },
	            buttons: [
	                {
	                    text: '保存',
	                    width: 100,
	                    iconCls: 'icon-save',
	                    handler: function () {
	                    	contact.updateAuth(checkContactId,orgUserData);
	                    				
	                    }
	                },
	                {
	                    text: '取消',
	                    width: 100,
	                    iconCls: 'icon-add',
	                    handler: function () {
	                    	contact.form_clear();
	                        $("#update_auth_dialog").dialog('close');
	                    }
	                }
	          ],
		});
	 
	 }
	
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
//			 var contactId=contact.getFileCheck();
//			 if(contactId==undefined)return alert("请选择左侧文件");
//			 contact.changeColor(this);
////			 var tagList=$("#tabFile input[type=checkbox]");
//			 nameSpace.data.currState=0;
//			 var hasCheck=false;
//			 $("#authManage button[id=person]").text("解除授权");
//			 $("#authManage button[id=partment]").text("部门解除授权");
//			 contact.refreshAuthUser(contactId);
			 
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
//			 var contactId=contact.getFileCheck();
//			 if(contactId==undefined)return alert("请选择左侧文件");
//			 contact.changeColor(this);
//			 nameSpace.data.currState=1;
////			 var tagList=$("#tabFile input[type=checkbox]");
//			 var hasCheck=false;
//			 $("#authManage button[id=person]").text("授权使用");
//			 $("#authManage button[id=partment]").text("授权给部门");
//			
//			 
//			 contact.refreshUnAuth(contactId);
		 });
		 
		 

		 
		 
		 $("#contactAllTable button[id=btn_down_delete]").click(function(){
			 
			 var contactId=contact.getFileCheck();
			 if(contactId==undefined)return  alert("请选择文件");
			 if(confirm("删除后文件不能恢复，是否删除")){
				 contact.deleteContact(contactId);
			 }		
		 });
//			$("#testCheck").attr("checked",true);
		 $("#contactAllTable button[id=btn_down_file_enc]").click(function(){
			  var password=prompt("请输入同步密码");
			  if (password!=null && password!=""){
				  var contactId=contact.getFileCheck();
					 contact.download(contactId,password);
			  }
		 });
		 
		 $("#contactAllTable button[id=btn_down_file]").click(function(){
			 
			 var contactId=contact.getFileCheck();
			 contact.download(contactId);
		 });
		 
		
		 
		 $("#contactAllTable button[id=btn_update_auth]").click(function(){
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
		 contact.refreshFile(1);
//		 $("#contactAllTable span[id=auth]").click();
			 
		 
	 });
})();
 