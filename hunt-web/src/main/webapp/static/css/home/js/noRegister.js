/**
 *   未绑定设备的
 */

(function(){
	var noSpace=window.NameSpace||{};
	noSpace.data={
		sort:"create_time",
		order:"desc",
		page:"1",
		rows:"10",
	};
	
	/**
	 * 分页插件
	 */
	pageTool={
			dataCount:0,
			totalPage:"",
			page:1,
			rows:15,
			sort:"create_time",
			order:"desc",
			netTotalCount:function(){
				$.ajax({
					url:getRootPath()+"/devManager/unRegisCount",
					type:"GET",
					success:function(data){
						pageTool.net();
						pageTool.dataCount=data.data;
						pageTool.initPageTool(pageTool.dataCount);
					},
				});
			},
			
			net:function(fun){
				$.ajax({
					url:getRootPath()+"/devManager/listUnRegis?"+noRegister.param(),
					type:"GET",
					success:function(data){
						noRegister.fillData(data.rows);
						if(fun!=null){
							fun("10000");
						}
						$("#noRegisPageTool span[id=currentPage]").text("当前"+(pageTool.page)+"/"+(pageTool.totalPage));
					}
				});
			},
			
			/**
			 *获得页面数量 
			 */
			getPage:function(dataCount,selectRows){
				return dataCount%selectRows==0?(dataCount/selectRows):Math.floor((dataCount/selectRows)+1);
			},
			
			initPageTool:function(dataCount){
				var selectRows=$("#noRegisPageTool select[id=iEveryCount] option:selected").val();
				var totalPage=pageTool.getPage(dataCount,selectRows);
				this.totalPage=totalPage;
				$("#noRegisPageTool span[id=currentPage]").text("当前1/"+totalPage);
				$("#noRegisPageTool span[id=totalPage]").text("共"+dataCount+"条");
			},
			
		    /**下一页*/
		    nextPage:function(){
		    	this.page++;
		    	if(this.page>this.totalPage){
		    		this.page=this.totalPage;
		    		return;
		    	}
		    	
		    	pageTool.net(function(data){
		    		if(data!=10000){
		    			pageTool.page--;
		    		}
		    	});
		    },
		    
		    /**上一页*/
		    prePage:function(){
		    	this.page--;
		    	if(this.page<1){
		    		this.page=1;
		    		return;	
		    	}
		    	pageTool.net(function(data){
		    		if(data!=10000){
		    			pageTool.page++;
		    		}
		    	});
		    },
		    /**修改每一页数量*/
		    changePages:function(){
		    	var rows=$("#noRegisPageTool select[id=iEveryCount] option:selected").val();
		    	var totalPage=pageTool.getPage(this.dataCount,rows);
		    	console.log("totalPage-->"+totalPage+"--->"+this.dataCount);
				this.totalPage=totalPage;
				this.rows=rows;
				pageTool.net(function(data) {
				});
		    },
		    clickChangeData:function(obj,sort){
		    	var path=$(obj).children("img").attr("src");
		    	pageTool.sort=sort;
		    	if(path=="static/css/home/image/arrow_up.png"){
		    		$(obj).children("img").attr("src","static/css/home/image/arrow_down.png");
		    		pageTool.order="desc";
		    		pageTool.net(function(data) {
					});
		    	}else{
		    		pageTool.order="asc";
		    		pageTool.net(function(data) {
					});
		    		$(obj).children("img").attr("src","static/css/home/image/arrow_up.png");
		    	}
		    	
		    },
		    /**跳转*/
		    jumpPage:function(){
		    	var page=$("#noRegisPageTool input[type=text]").val();
		    	if(page>this.totalPage){
		    		alert("超出范围");
		    		return;
		    	}
		    	if(page<1){
		    		alert("超出范围");
		    		return;
		    	}
		    	this.page=page;
		    	pageTool.net(function(data) {
				});
		    },
		    /**首页*/
		    firstPage:function(){
		    	this.page=1;
		    	pageTool.net(function(data) {
				});
		    },
		    /**尾页*/
		    endPage:function(){
		    	this.page=this.totalPage;
		    	pageTool.net(function(data) {
				});
		    },
		    
		    
		    updateMsg:function(obj){
		    	alert(toString.call(obj));
		    },
		    
		    /**
			 * type 1,新增   2,删除
			 */
			init_device_dialog:function(type,sysDevice){			//  添加设备初始化
				var currRowIndex="-1";
				var hasPermission=true;
				$("#device_manage_dialog").dialog({
					title:'添加设备',
					 iconCls: 'icon-save',
			         closable: true,
			         width: 1000,
			         height: 500,
			         cache: false,
			         modal: true,
			         resizable: false,
			         /*maximizable:true,*/
			         
			         'onOpen': function () {
			        	 var deviceName=sysDevice.deviceName;
			        	 var deviceSerial=sysDevice.deviceSerial;
			        	 var description=sysDevice.description;
			        	 deviceName=deviceName==null?"":deviceName;
			        	 deviceSerial=deviceSerial==null?"":deviceSerial;
			        	 description=description==null?"":description;
			        	 $("#device_manage_dialog input[id=deviceName]").val(deviceName);
			        	 $("#device_manage_dialog input[id=deviceSerial]").val(deviceSerial);
			        	 $("#device_manage_dialog textarea[id=description]").val(description);
			        	 
			        	 
			        	 $('#device_manage_dialog table[id=device_user_list]').datagrid({
			        		 url:getRootPath()+"/user/listUserByOrg?orgId="+common_tool.getHomeRoleOrgId(),
			        		 method:'get',
			                 idField:'id',
			             	singleSelect: true,
			                 fitColumns: true,
			                 rownumbers: true,
			                 columns:[[
			                	 {field:'id',title:'选择',width:100,align:'left',checkbox: "true",formatter:function(value,row,index){
			                		 return row.id;
			                	 }},
			             		{field:'loginName',title:'使用人员',width:100,align:'left',formatter:function(value,row,index){
			             			return row.loginName;
			             		}}
			                 ]],
			                 
			        		 onLoadSuccess:function(data){
			        			console.log("datagrid加载成功"); 
			        			 var params="?id="+common_tool.getCurrUserId();
			        			 $("#device_manage_dialog table[id=deviceJobs]").treegrid({
//					        		 url:getRootPath()+"/job/list",
			        				 url:getRootPath()+"/organization/list"+params,
					        		 method:'get',
					                 idField:'id',
					                 treeField:"name",
					             	singleSelect: true,
					                 fitColumns: true,
					                 rownumbers: true,
					                 columns:[[
					                	 {field:'id',title:'选择',width:100,checkbox: "true"}/*,formatter:function(value,row,index){
					                		 return row.id;
					                	 }}*/,
					             		{field:'name',title:'职位',width:100}/*,formatter:function(value,row,index){
					             			console.log("name-->"+JSON.stringify(row));
					             			return row.name;
					             		}}*/
					                 ]],
					                 onLoadSuccess:function(row,data){
					                	 console.log("deviceJobs 加载成功");
					                	 if(data.code==20005){
					                		 hasPermission=false;
					    	        		  common_tool.messager_show(data.msg);
					    	        	  }
					                 },
					                 rowStyler:function(row){
						     		        return 'background-color:#e3e3e3;color:#000000;font-weight:bold;';
						     		 },
					    		});
			        		 },
			                 
			    			 onCheck:function(rowIndex,rowData){
			    					 $("#device_manage_dialog table[id=deviceJobs]").datagrid("uncheckAll");
			    					 if(currRowIndex==rowIndex){
			    						 $("#device_manage_dialog table[id=device_user_list]").datagrid('uncheckAll');
			    						 currRowIndex="-1";
			    						 return;
			    					 }
			    					 $("#device_manage_dialog table[id=deviceJobs]").treegrid("select", rowData.sysOrgId);
			    	        	  currRowIndex=rowIndex;
			    	          },
			    	          
			    	         
			    		});
			        	
			        	 
			          },
			       
			          'onClose':function(){
			        	  noRegister.formClear();
			          },
			         buttons:[
			        	 {
			        		 	
			                    text: '保存',
			                    width: 100,
			                    iconCls: 'icon-save',
			                    handler: function () {
			                    	console.log("点击保存");
			                    	if(hasPermission){
			                    		var devId=sysDevice.id;
			                    		noRegister.update(devId);
			                    	}
			                    }
			                },
			             /*   {
			                    text: '清除',
			                    width: 100,
			                    iconCls: 'icon-reload',
			                    handler: function () {
			                    	console.log("点击清除");
			                    	
			                    }
			                },*/
			                {
			                    text: '取消',
			                    width: 100,
			                    iconCls: 'icon-add',
			                    handler: function () {
			                        $("#device_manage_dialog").dialog("close");
			                    }
			                }
			         ],
				});
			},
	}
	
	noRegister={
		formClear:function(){		 //  初始化所有已选中的，form表单
      	  $("#device_manage_dialog form[id=device_edit_form]").form("reset");
      	  $("#device_manage_dialog form[id=device_edit_form]").form("clear");
      	  
      	  $("#device_manage_dialog table[id=device_user_list]").datagrid('uncheckAll');
      	  $("#device_manage_dialog table[id=deviceJobs]").datagrid('uncheckAll');
		},
			
		/**
		 * 获得参数
		 */
		param:function(){
			return "sort="+pageTool.sort+"&order="+pageTool.order+"&page="+pageTool.page+"&rows="+pageTool.rows;
		},
		
	
		
		fillData:function(listData){
			$("#noRegisdevice tbody").remove();
			
			var tbody=$("<tbody></tbody>");
			$("#noRegisdevice").append(tbody);
			for(var i=0;i<listData.length;i++){
				var tableTr=null;
				if(i%2==0){
					tableTr=$("<tr style='background-color: #ffffff;'></tr>");
				}else{
					tableTr=$("<tr style='background-color: #f3f3f3;'></tr>");
				}
				
				var sysDevice=listData[i];
				var sysDeviceId=sysDevice.id;
				var devName=sysDevice.deviceName;
				var deviceSerial=sysDevice.deviceSerial;
				var createTime=sysDevice.createTime;
				createTime=common_tool.getMoth(createTime);
				var description=sysDevice.description==null?"无":sysDevice.description;
				tableTr.append("<td>"+(i+1)+"</td>");
				tableTr.append("<td>"+deviceSerial+"</td>");
				tableTr.append("<td>"+devName+"</td>");
				tableTr.append("<td>"+createTime+"</td>");
				tableTr.append("<td>"+description+"</td>");
				tableTr.append("<td style='vertical-align: bottom; margin-top:30px;'>"+
				    		"<img src='static/image/remark.png' width='25px' height='25px' id='"+i+"'/></td>");
				$("#noRegisdevice").off("click","img[id="+i+"]");
				$("#noRegisdevice").on("click","img[id="+i+"]",function(){	// 用append添加html时 绑定点击事件的最佳方式
					var sysDev=listData[$(this).attr("id")];
					pageTool.init_device_dialog(1,sysDev);
					
				});
				tbody.append(tableTr);
			}
		},
		update:function(devId){
			var devName=$("#device_edit_form input[id='deviceName']").val();
			var deviceSerial=$("#device_edit_form input[id='deviceSerial']").val();
			if(strIsEmpty(devName)){
				common_tool.messager_show("设备名称错误");
			}else if(strIsEmpty(deviceSerial)){
				common_tool.messager_show("序列号输入错误");
			}else{
				var deviceName=$("#device_edit_form input[id='deviceName']").val();
				var deviceId=devId;
				var deviceSerial=$("#device_edit_form input[id='deviceSerial']").val();
				var description=$("#device_edit_form textarea[id='description']").val();
				var user=$("#device_edit_form table[id='device_user_list']").datagrid("getChecked");
				var sysOrg=$("#device_edit_form table[id=deviceJobs]").datagrid("getChecked");
//				var arr=new Array();
				if(user.length>0){
//					for(var i=0;user.length>0&&i<user[0].userRoleOrganizations.length;i++){
//						arr[i]=user[0].userRoleOrganizations[i].sysRoleOrganizationId;
//					}
				}else {
					common_tool.messager_show("请绑定用户");
					return;
				}
				
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
					dataType: 'json',
					 traditional:true,				//防止深度序列化
					success:function(data){
						if(data.code==10000){		//   操作成功
							$("#device_manage_dialog").dialog("close");
							noRegister.formClear();
							common_tool.messager_show(data.msg);
							pageTool.netTotalCount();
		                        return false;
						}else{
							 common_tool.messager_show(data.msg);
		                        return false;
						}
					}
				});
			}
		},
	};
	
	$(function(){
		pageTool.netTotalCount();
	});
	
	
	
	
})();