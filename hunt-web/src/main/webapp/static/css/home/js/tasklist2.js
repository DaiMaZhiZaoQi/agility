/**
 * 任务列表 taskList
 */

common_task={
		fa:function(){
			 console.log("fsdfdsafdsafsdaf");
		 },
}

var taskList=(
	
	function() {
	/**
	 * 这种写法什么意思？？？
	 */
	var tl=window.NameSpace||{};
	tl.data={
			/**上传任务文件*/
			btnFileUp:"",
			form_task:"",
			table_task_group:"",
			taskGroupId:"0",
			view_task_group:null,
			view_task_list:null,
			view_dialog_taskgrid:null,
			btn_dispatch_task:null,
			
			listAllSelectId:new Array(),
	};
	
	/**
	 * 分页请求参数
	 */
	groupPageDto={
		page:"1",
		rows:"15",
		userId:"",
	};
	
	
	
	
	telPageDto={
			/**排序字段,常用 create_time*/
			sort:"update_time",
			/**默认查询已分派*/
			others:"0",
			/**排序方式，一般用 DESC*/
			order:"DESC",
			page:"1",
			rows:"15",
			callNumber:"",
			userId:common_tool.getCurrUserId(),
	};
//	/**
//	 * 任务列表分页
//	 */
	tlPageDto={
			/**排序字段,常用 create_time*/
			sort:"update_time",
			/**默认查询已分派*/
			others:"0",
			/**排序方式，一般用 DESC*/
			order:"DESC",
			page:"1",
			rows:"1",
			/**通用id*/
			id:11,
			userId:common_tool.getCurrUserId(),
			selectStatus:2,
			currentPage:1,
			totalPage:1,
			totalCount:0,
	};

	taskFun={
			

		    
		    /* sor : call_duration, reco_audio_length，call_date*/
		    clickChangeData:function(obj,sort){
		    	var path=$(obj).children("img").attr("src");
		    		NameSpace.data.set_globalSort(sort);
		    	if(path=="static/css/home/image/arrow_up.png"){
		    		$(obj).children("img").attr("src","static/css/home/image/arrow_down.png");
		    		NameSpace.data.set_globalOrder("desc");
		    		
		    		recordModult.refreshCallLog(NameSpace.data.get_optType(),NameSpace.data.get_orgIdUserId(),NameSpace.data.get_globalSort(),"desc",1,-1);
		    	}else{
		    		NameSpace.data.set_globalOrder("asc");
		    		recordModult.refreshCallLog(NameSpace.data.get_optType(),NameSpace.data.get_orgIdUserId(),NameSpace.data.get_globalSort(),"asc",1,-1);
		    		$(obj).children("img").attr("src","static/css/home/image/arrow_up.png");
		    	}
		    	
		    },
		    
		    
		    /**下一页*/
		    nextPage:function(){
		    	tlPageDto.page++;
		    	/* refreshCallLog:function(optType,id,sort,order,options,definePage)   */
		    	taskFun.loadTaskColumn();
		    },
		    
		    /**当前页*/
		    currPage:function(){
		    	taskFun.loadTaskColumn();
		    },
		    
		    /**上一页*/
		    prePage:function(){
		    	tlPageDto.page--;
		    	taskFun.loadTaskColumn();
		    },
		    /**修改每一页数量*/
		    changePages:function(){
		    	tlPageDto.page=1;
		    	tlPageDto.rows=$("#taskPageTool select[id=iEveryCount] option:selected").val();
		    	taskFun.loadTaskColumn();
		    },
		    
		    /**跳转*/
		    jumpPage:function(){
		    	let jumpPage=$("#taskPageTool input[type=text]").val();
		    	jumpPage=parseInt(jumpPage);
		    	if(typeof(jumpPage)=="number"&&!isNaN(jumpPage)){		//  isNaN(obj)
		    		if(jumpPage==tlPageDto.page)return;
		    		if(jumpPage>tlPageDto.totalPage||jumpPage<1){
		    			alert("Enter illegal");
		    			return;
		    		}
		    	}else {
					alert("Enter illegal");
					return;
				}
		    	tlPageDto.page=jumpPage;
		    	taskFun.loadTaskColumn();
		    },
		    
		    /**首页*/
		    firstPage:function(){
		    	if(tlPageDto.page==1)return;
		    	tlPageDto.page=1;
		    	taskFun.loadTaskColumn();
		    },
		    
		    /**尾页*/
		    endPage:function(){
		    	if(tlPageDto.page==tlPageDto.totalPage)return;
		    	tlPageDto.page=tlPageDto.totalPage;
		    	taskFun.loadTaskColumn();
		    },
		    
		    /**
			 * 分页加载任务
			 */
			loadTaskColumn:function(){
				
				if(tlPageDto.page>tlPageDto.totalPage){
					tlPageDto.page=tlPageDto.totalPage;
					return;
				}
				if (tlPageDto.page<1) {
					 tlPageDto.page=1;
					 return;
				}
				$.ajax({
					type:"POST",
					url:getRootPath()+"/task/taskList",
					contentType:"application/json",
					data:JSON.stringify(tlPageDto),
					success:function(data){
						if(tlPageDto.page=="1"){		//  更新页面数据总数  TODO
							$("#taskPageTool span[id=totalPage]").text("共"+data.total+"条");
							tlPageDto.totalCount=data.total;
							let selectEdRows=$("#taskPageTool select[id=iEveryCount] option:selected").val();
							
							let totalPage=tlPageDto.totalCount%selectEdRows==0?tlPageDto.totalCount/selectEdRows:Math.floor((tlPageDto.totalCount/selectEdRows)+1);
							tlPageDto.totalPage=totalPage;
							$("#taskPageTool span[id=currentPage]").text("当前1/"+totalPage+"页");
						}else {
							$("#taskPageTool span[id=currentPage]").text("当前"+tlPageDto.page+"/"+tlPageDto.totalPage+"页");
						}
						var aarData=new Array();
						for(var i=0;i<data.rows.length;i++){
							var taskData=JSON.parse(data.rows[i].taskMsg);
							taskData.id=data.rows[i].id;
							taskData.updateTime=data.rows[i].updateTime;
							taskData.status=data.rows[i].status;
							taskData.taskCount=data.rows[i].taskCount;
							aarData.push(taskData);
							taskData.record="更多";
						}
						if(tl.data.view_task_list!=null){
							tl.data.view_task_list.datagrid("loadData",aarData);
							return;
						}
					}
				});
			},
			
			
			
			/**任务数量*/
			form_clear: function () {
				$("#dialog_dispatch_task table[id=dialog_org_user]").datagrid("uncheckAll");
				$("#dialog_dispatch_task table[id=dialog_task_grid]").datagrid("uncheckAll");
				
			},
			
			/**
			 * 确认分派任务
			 */
			confirmDispatch:function(){
				var checkUser=$("#dialog_dispatch_task table[id=dialog_org_user]").datagrid("getChecked");
				console.log(checkUser);
				console.log(tl.data.listAllSelectId);
				var checkData=tl.data.view_task_group.datagrid("getChecked");
				console.log(checkData);
				var listSelectId=$("#dialog_dispatch_task table[id=dialog_task_grid]").datagrid("getChecked");
				var arrSelectId=new Array();
				for(let i=0;i<listSelectId.length;i++){
					arrSelectId.push(listSelectId[i].id);
				}
				console.log(listSelectId);
				console.log(common_tool.getCurrUserId());
//				return;
				$.ajax({
					type:"POST",
					url:getRootPath()+"/task/dispatchTask",
					contentType:"application/x-www-form-urlencoded",
					data:{
						userId:common_tool.getCurrUserId(),
						taskGroupId:checkData[0].id,
						taskUserId:checkUser[0].id,
						zhName:checkUser[0].zhName,
						strTaskSelectId:arrSelectId.toString(),
						strTaskAllId:tl.data.listAllSelectId.toString(),
					},
					success:function(data){
						console.log(data);
						common_tool.messager_show(data.msg);
                        
					},
					error:function(data){
						common_tool.messager_show(data.msg);
					},
				
				})
			},
			
		/**
		 * 分派任务dialog 刷新
		 */
		dispathTask:function(){
			let dialogDatagridUser=null;
			let unDisPathchSize=0;
			let taskId=new Array();
			let isClic=false;
			$("#dialog_dispatch_task").dialog({
				 title: '分派任务',
				 width: 1200,
				 height: 600,
				 closable:true ,
				 cache: false,
				  modal: true,
				  resizable: false,
				 onOpen:function(){
					 console.log("分派任务对话框打开");
					 $("#dialog_dispatch_task input[id=autoDispatch]").on("change",function() {
						 let cbChecked=$(this).prop("checked");
						 //  判断任务是否满足分派条件
						 let datagridUserOptions=dialogDatagridUser.datagrid("options");
						 if(cbChecked){
							 console.log("一键分派开启");
							 datagridUserOptions.singleSelect=false;
							 $("#dialog_dispatch_task table[id=dialog_task_grid]").datagrid({columns:[]});
							$("#dialog_dispatch_task table[id=dialog_task_grid]").datagrid("loadData", { total: 0, rows: [] });
							let checkData=tl.data.view_task_group.datagrid("getChecked");
							let taskGroupId=checkData[0].id;
							$.ajax({
								type:"POST",
								url:getRootPath()+"/task/listUnPatch",
//								contentType:"application/json",
								data:{
									userId:common_tool.getCurrUserId(),
									taskGroupId:taskGroupId,
									status:-1,
								},
//								data:JSON.stringify(tlPageDto),
								success:function(data){
									console.log(data);
									for (let i = 0; i < data.rows.length; i++) {
										taskId.push(data.rows[i].id);
									}
									unDisPathchSize=data.total;
									$("#dialog_dispatch_task span[id=spanUnDispatch]").text("共"+data.total+"个任务")
								},
							});
								return;
						 }else {
							 console.log("一键分派关闭");
							 if(dialogDatagridUser!=null){
								 datagridUserOptions.singleSelect=true;
								 dialogDatagridUser.datagrid("reload");
								 $("#dialog_dispatch_task span[id=spanUnDispatch]").text("");
								 return;
							 }
						}
					});
					 let curChecked=$("#dialog_dispatch_task input[id=autoDispatch]").prop("checked");
					 console.log("当前选中状态--->"+curChecked);
					 dialogDatagridUser=$("#dialog_dispatch_task table[id=dialog_org_user]").datagrid({
						 	url: getRootPath() + '/user/list',
				            method: 'get',
				            idField: "id",
				            fitColumns: true,
				            rownumbers: true,
				            animate: true,
				            singleSelect: true,
				            fit: true,
				            border: false,
				            striped: true,
				            queryParams: {
				                userId:common_tool.getCurrUserId(),
				            },
				            columns: [[
				                {title: "选择", field: "ck", checkbox: true},
				                {title: "任务接收人", field: "zhName", width: 130, sortable: true},
				            ]],
				        	onCheck:function(rowIndex,rowData){
				        		console.log("选择用户--->"+rowIndex+"--->"+JSON.stringify(rowData));
//				        		datagrid("selectRecord",rowData.rows[0].id);
				        		 let cbChecked=$("#dialog_dispatch_task input[id=autoDispatch]").prop("checked");
								 if(!cbChecked){
									 taskFun.loadDialogTask(rowData.id);
								 }
				        	},
				            onLoadSuccess:function(data){
				            	$("#dialog_dispatch_task table[id=dialog_org_user]").datagrid("selectRecord",data.rows[0].id);
				            },
				            onLoadError:function(data){
				            	console.log(data);
				            }
					 });
					 
				 },
				 onClose:function(){
					 $("#dialog_dispatch_task input[id=autoDispatch]").prop("checked",false)
					  $("#dialog_dispatch_task span[id=spanUnDispatch]").text("");
					 $("#dialog_dispatch_task input[id=autoDispatch]").off();
					 taskFun.form_clear();
					 if(isClic){
						   $("#cbDispatch").prop("checked",true);
                           taskFun.initTaskGroup();
					 }
					 
				 },
				 
				 buttons: [
					  {
	                         text: '更新分派',
	                         width: 100,
	                         handler: function () {
	                        	 let cbChecked=$("#dialog_dispatch_task input[id=autoDispatch]").prop("checked");
	                        	 if(cbChecked){		//  开启一键分派
	                        		 let checkUser=dialogDatagridUser.datagrid("getChecked");
									 console.log("--->"+checkUser.length,checkUser,taskId.toString());
									 if(unDisPathchSize==0){
										 common_tool.messager_show("0个未完成的任务");
										 return;
									 }
									 if(unDisPathchSize<checkUser.length){
										 if(confirm("接收人大于任务数，末位几个不会被分到任务，是否继续分派....")){
											 console.log("继续");
										 }else{
											 return;
										 }
									 }
									 let checkData=tl.data.view_task_group.datagrid("getChecked");
										let taskGroupId=checkData[0].id;
									 $.ajax({
										 url:getRootPath()+"/task/oneKey",
										 type:"POST",
										 data:{
											 taskGroupId:taskGroupId,
											 userId:common_tool.getCurrUserId(),
											 listTaskId:taskId.toString(),
											 listUser:JSON.stringify(checkUser),
											 
										 },
										 success:function(data){
											 if(data.code==10000){
												 $("#dialog_dispatch_task").dialog('close');
						                         common_tool.messager_show(data.msg);
						                         $("#cbDispatch").prop("checked",true);
						                         taskFun.initTaskGroup();
											 }
										 },
									 });
								 
	                        	 }else{
	                        		 isClic=true;
	                        		 taskFun.confirmDispatch();
	                        	 }
	      
	                         }
	                     },
                     {
                         text: '取消',
                         width: 100,
//                         iconCls: 'icon-add',
                         handler: function () {
                             $("#dialog_dispatch_task").dialog('close');
                         }
                     }
                 ],
			});
			 $("#dialog_dispatch_task").dialog("center");
			 
		},
//		
//		
//		/**
//		 * 加载dialog中的任务列表
//		 */
	loadDialogTask:function(userId){
			var checkData=tl.data.view_task_group.datagrid("getChecked");
			var taskGroupId=checkData[0].id;
			console.log(checkData);
			if(tl.data.view_dialog_taskgrid!=null){
				tl.data.view_dialog_taskgrid.datagrid("clearChecked");
			}
    		//  加载已分配给我的和没有分配的任务
    	$.ajax({
    		type:"POST",
			contentType:"application/x-www-form-urlencoded",
			url:getRootPath()+"/task/taskColumn",
			data:{"userId":common_tool.getCurrUserId(),"sysTaskGroupId":taskGroupId},
			success:function(data){
				console.log(data);
				var arrsColumn=tlFun.creteDialogColumn(data.msg);
				if(arrsColumn.length==0){
					$("#dialog_dispatch_task table[id=dialog_task_grid]").datagrid({columns:[]});
					$("#dialog_dispatch_task table[id=dialog_task_grid]").datagrid("loadData", { total: 0, rows: [] });
					return;
				}
				tlPageDto.selectStatus=1;
				tlPageDto.others=1;
				tlPageDto.curUserId=userId;
				$.ajax({
					type:"POST",
					url:getRootPath()+"/task/taskList",
					contentType:"application/json",
					data:JSON.stringify(tlPageDto),
					success:function(data){
						if(tlPageDto.page=="1"){		//  更新页面数据总数
							$("#taskPageTool span[id=totalPage]").text("共"+tlPageDto.page+"页");
						}
						var aarDialogData=new Array();
						for(var i=0;i<data.rows.length;i++){
									
							var taskData=JSON.parse(data.rows[i].taskMsg);
							taskData.id=data.rows[i].id;
							taskData.status=data.rows[i].status;
							aarDialogData.push(taskData);
						}
						tlPageDto.curUserId=null;		
						console.log("arrsColumn-->"+JSON.stringify(arrsColumn));
						console.log("data-->"+JSON.stringify(aarDialogData));
						tl.data.view_dialog_taskgrid=$("#dialog_dispatch_task table[id=dialog_task_grid]").datagrid({
							idField: "id",
							title:"任务详情(勾选的为已分派的任务)",
							fitColumns: true,
							rownumbers: true,
							singleSelect: false,
							border: false,
							striped: true,
							animate: true,
							columns:[arrsColumn],
							data:aarDialogData,
							onLoadSuccess:function(data){
								console.log(data);
								var aarRows=data.rows;
								tl.data.listAllSelectId.length=0;
								if(Array.isArray(aarRows)){
									for(let i=0;i<aarRows.length;i++){
										if(aarRows[i].status>-1){
											tl.data.listAllSelectId.push(aarRows[i].id);
											$("#dialog_dispatch_task table[id=dialog_task_grid]").datagrid("selectRecord",aarRows[i].id);
										}
									}
//									aarRows.forEach((item,index,aar)=>{
//										if (item.status>-1) {
//											tl.data.listAllSelectId.push(item.id);
//											$("#dialog_dispatch_task table[id=dialog_task_grid]").datagrid("selectRecord",item.id);
//										}
//									});
								}
							},
							onCheck:function(rowIndex,rowData){
								
							},
							
							onLoadError:function(){
								console.log("错误"); 
							},
									
						});
					},
					error:function(data){
						tlPageDto.curUserId=null;			
					}
				});
					
		},
		error:function(data){
			console.log("err-->"+data);
		},
				
    });

		},
		
		/**
		 * 初始化任务组
		 */
		initTaskGroup:function(){
			var checkIndex=0;
			
			if(tl.data.view_task_group!=null){
				tl.data.view_task_group.datagrid("reload");
				console.log("重新加载");
				return;
			}
			tl.data.view_task_group=tl.data.table_task_group.datagrid({
				 idField: "id",
				 fitColumns: true,
				 rownumbers: false,
				 height:500,
				 singleSelect: true,
		         border: false,
		         animate: true,
		         columns:[[{field:'taskGroupName',title:'我的任务组',width:'100%',align:'center',
		        	 formatter:function(value, row, index) {
						return "<div style='height:45px;line-height: 45px;' title='任务版本 : " +row.taskCode+ "'>" + value + "</div>";
					 }
		         }]],
				loader:function(param, success, error) {
	                
	                $.ajax({
	                    type : "POST",
	                    url : getRootPath()+"/task/listTaskGroup",
	                    contentType : 'application/json;charset=utf-8', // 设置请求头信息
	                    data : JSON.stringify(groupPageDto),
	                    success : function(result) {       
	                    	success(result);
	                    }
	                });
				},
				
				
               	onLoadSuccess:function(dataResult){
               		console.log(dataResult);
               		if(dataResult.rows.length==0){
               			common_tool.messager_show("没有查询到任务组");
               			$("#div_search_module").hide();
               			return;
               		}else{
               			$("#div_search_module").show();
               		}
               		tl.data.table_task_group.datagrid("selectRecord",dataResult.rows[0].id);
               		tl.data.taskGroupId=dataResult.rows[0].id;
               		tlFun.initTaskColumn();
               	},
               	onClickRow:function(rowIndex,rowData){
               		console.log("onClickRow");
               		if(checkIndex==rowIndex){
               			checkIndex=rowIndex;
               			console.log("选中了--->"+rowIndex+"数据--->"+JSON.stringify(rowData));
               			tl.data.view_task_group.datagrid("selectRecord",rowData.id);
               			return;
               		}
               		checkIndex=rowIndex;
               		if(rowData!=undefined){
						tl.data.taskGroupId=rowData.id;
							
						console.log("选中了taskGroup"+JSON.stringify(rowData));
               			tlFun.initTaskColumn();
					}
//               	var parent=tl.data.view_task_group.datagrid("getColumnOption");
               	},
               	onCheck:function(rowIndex,rowData){
               		console.log("onCheck");
               	},
				onSelect:function(index,row){
					console.log("onSelect");
				},
			});
		}
	}
	
	
	/**
	 * 任务列表功能
	 */
	tlFun={
			
			/**
			 * 创建对方框任务列
			 */
			creteDialogColumn:function(str){
				var arrsColumn=new Array();
				var strEmpty=strIsEmpty(str);
				if(strEmpty)return arrsColumn;
				console.log("createDialogColumn--->"+strEmpty+"--->"+typeof str);
				var objJson=JSON.parse(str);
				console.log("str--->"+objJson.id);
				var taskColumn=objJson.taskColumn;
				if(taskColumn.length>0){
					var taskCo=JSON.parse(taskColumn);
					var ck={title: "选择", field: "ck", checkbox: true};
					arrsColumn.push(ck);
					var column=0;
					for (var i in taskCo) {
						column++;
						if(column<=4){
							var oj={field:i,title:taskCo[i],width:100,editor:'text',align:'center',formatter:function (value) {
								return "<span title='" + value + "'>" + value + "</span>"}};
							arrsColumn.push(oj);
						}
					}
				
					var statusColumn={field:"status",title:"状态",width:100,align:"center",
						formatter: function(value,row,index){
							console.log("formatter--->"+value);
							return tlFun.parseStatus(row.status)
						}};
					arrsColumn.push(statusColumn);
				}
				return arrsColumn;
			},
			
			/**
			 * 创建表头
			 */
			createColumn:function(str){
				let arrsColumn=new Array();
				var objJson=JSON.parse(str);
				console.log("str--->"+objJson.id);
				var taskColumn=objJson.taskColumn;
				var statusData = [
				    {id:1,name:'正在进行'},
				    {id:2,name:'已完成'},
				];
				if(taskColumn.length>0){
					var taskCo=JSON.parse(taskColumn);
					for (var i in taskCo) {
						if(i=="xm"){
							let xm={field:i,title:taskCo[i],width:60,sortable: true,editor:'text',align:'center',formatter:function (value) {
								return "<span title='" + value + "'>" + value + "</span>"}};
							arrsColumn.push(xm);
						}else if(i=="dhhm"){
							let dhhm={field:i,title:taskCo[i],width:60,editor:'text',align:'center',formatter:function (value) {
								return "<span title='" + value + "'>" + value + "</span>"}};
							arrsColumn.push(dhhm);
						}else{
							let oj={field:i,title:taskCo[i],width:70,editor:'text',align:'center',formatter:function (value) {
								return "<span title='" + value + "'>" + value + "</span>"}};
							arrsColumn.push(oj);
						}
					}
					var statusColumn={field:"status",title:"状态",sortable: true,width:60,align:"center",
							editor:{
								type:'combobox',
								options:{
									valueField:'id',
									textField:'name',
									data:statusData,
									required:true,
									onSelect:function(data){
										console.log("editor-->",data);
									},
									panelHeight:"auto",
									editable:false
								},
								
							},
							formatter:function(value,row,index){
//								if(value=="已完成"){
//									return "已完成";
//								}
								var statusVal=tlFun.parseStatus(value);
								console.log("value--->"+value+"--row-->"+JSON.stringify(row)+"--index--"+index);
//								return "<a href='javascript:void(0)' style='color:#007ad5;font-size:18px;'>"+statusVal+"</a>";
								return statusVal;
							},
					};
					arrsColumn.push(statusColumn);    
					var taskCount={field:"taskCount",title:"通话次数",sortable: true,width:60,align:"center"};
					arrsColumn.push(taskCount);
					var telRecord={field:"record",title:"通话记录",width:60,align:"center",
							formatter:function(value,row,index){ 
								return "<a href='javascript:void(0)' style='color:#007ad5;font-size:18px;text-decoration: underline;' onclick='tlFun.phoneRecord(this)'>"+value+"</a>";
							},
							
					};
					arrsColumn.push(telRecord);
					let updateTime={field:"updateTime",title:"更新时间",sortable: true,width:70,align:"center",
						formatter:function(value,row,index){
							return "<span style='font-size:16px;'>"+ common_tool.getSimMoth(value,1)+"</span>";
						}};
					arrsColumn.push(updateTime);
					var option={field:'action',title:'维护',width:70,align:'center',
							formatter:function(value,row,index){
								if (row.editing){
									var s = "<a href='javascript:void(0)'  style='color:#007ad5;text-decoration: underline;font-size: 18px;' onclick='tlFun.saverow(this)'>保存</a> ";
									var c = "<a href='javascript:void(0)' style='color:#007ad5;text-decoration: underline;font-size: 18px;' onclick='tlFun.cancelrow(this)'>取消</a>";
									return s+c;
								} else {
									var e = "<a href='javascript:void(0)' style='color:#007ad5;text-decoration: underline;font-size: 18px;' onclick='tlFun.editrow(this)'>编辑</a>";
//									var d = '<a href="javascript:void(0)" onclick="tlFun.deleterow(this)">删除</a>';
//									return e+d;
									return e;
								}
							}
						}
					arrsColumn.push(option);
				}
				return arrsColumn;
			},

			/**
			 * 创建项目组信息标题
			 */
			createGroupMsg:function(msg){
				$("#task_group td[id=sp_group_detail]").children().remove();
				var taskGroup=JSON.parse(msg);
				$("#task_group span[id=span_group]").text(taskGroup.taskGroupName);
				tlFun.groupTotal(taskGroup);
			},
			
			/**
			 * 统计通话记录
			 */
			groupTotal:function(optType){
				console.log("optType"+JSON.stringify(optType));
				let groupChecked=tl.data.table_task_group.datagrid("getChecked");
				console.log(groupChecked);
				let creTime=common_tool.getSimMoth(groupChecked[0].createTime,2);
				
				var str= "<span class='titleChild' style='margin-right:10px;'>发布时间 : "+creTime+"</span></br>\n<span class='titleChild' style='margin-right:10px;'>任务总数 : "+optType.taskSize+"</span>"
				+"<span class='titleChild' style='margin-right:10px;'>已完成 : "+optType.taskFinish+"</span>"
				+"<span class='titleChild'>占比: "+optType.taskRate+"%</span>";
				$("#task_group td[id=sp_group_detail]").append(str);
			},
			
			/**
			 * 解析状态   任务状态(-1，任务未分派，0，未开始，1，正在进行，2，已完成，3，任务已重新分配,4,任务已删除)
			 */
			parseStatus:function(status){
				switch (status) {
					case -1:return "<span style='color:#f11919;font-size:18px;'>未分派</span>";
					case 0:return "<span style='color:#007ad5;font-size:18px;'>未开始</span>";
					case 1:return "<span style='color:#007ad5;font-size:18px;'>正在进行</span>";
					case 2:return "<span style='color:#5abb71;font-size:18px;'>已完成</span>";
					case 3:return "已重新分派";
					case 4:return "已删除";
					default:return "未知";
				}
			},
			
			
			
			/**
			 * 初始化任务列表
			 */
			initTaskColumn:function(){
				var check=	$("#cbDispatch").prop("checked");
				if(check){		//  已分派
					tlPageDto.others=0;
					tlPageDto.selectStatus=2
				}else {			//  未分派
					tlPageDto.selectStatus=0
					tlPageDto.others=-1;
				}
				$.ajax({
					type:"POST",
					contentType:"application/x-www-form-urlencoded",
					url:getRootPath()+"/task/taskColumn",
					data:{"userId":common_tool.getCurrUserId(),"sysTaskGroupId":tl.data.taskGroupId},
					success:function(data){
						console.log(data);
						var strEmpty=strIsEmpty(data.msg);
						if(strEmpty){
							$("#task_group td[id=sp_group_detail]").children().remove();
							$("#task_grid").datagrid({columns:[]});
							 $("#task_grid").datagrid("loadData", { total: 0, rows: [] });
							 return;
						}
						tlFun.createGroupMsg(data.msg);
						var arrsColumn=tlFun.createColumn(data.msg);
						if(arrsColumn.length==0){
							$("#task_grid").datagrid({columns:[]});
							 $("#task_grid").datagrid("loadData", { total: 0, rows: [] });
							return;
						}
						$.ajax({
							type:"POST",
							url:getRootPath()+"/task/taskList",
							contentType:"application/json",
							data:JSON.stringify(tlPageDto),
							success:function(data){
								if(tlPageDto.page=="1"){		//  更新页面数据总数  TODO
									$("#taskPageTool span[id=totalPage]").text("共"+data.total+"条");
									tlPageDto.totalCount=data.total;
									let selectEdRows=$("#taskPageTool select[id=iEveryCount] option:selected").val();
									
									let totalPage=tlPageDto.totalCount%selectEdRows==0?tlPageDto.totalCount/selectEdRows:Math.floor((tlPageDto.totalCount/selectEdRows)+1);
									tlPageDto.totalPage=totalPage;
									$("#taskPageTool span[id=currentPage]").text("当前1/"+totalPage+"页");
								}else {
									$("#taskPageTool span[id=currentPage]").text("当前"+tlPageDto.page+"/"+tlPageDto.totalPage+"页");
								}
								var aarData=new Array();
								for(var i=0;i<data.rows.length;i++){
									var taskData=JSON.parse(data.rows[i].taskMsg);
									taskData.id=data.rows[i].id;
									taskData.updateTime=data.rows[i].updateTime;
									taskData.status=data.rows[i].status;
									taskData.taskCount=data.rows[i].taskCount;
									aarData.push(taskData);
									taskData.record="更多";
								}
								if(tl.data.view_task_list!=null){
									tl.data.view_task_list.datagrid({columns:[arrsColumn]});
									tl.data.view_task_list.datagrid("reload", JSON.stringify(tlPageDto));
									return;
								}
							let IsCheckFlag = true; //标示是否是勾选复选框选中行的，true - 是 , false - 否
								tl.data.view_task_list= $("#task_grid").datagrid({
									 idField: "id",
									 fitColumns: true,
									 rownumbers: true,
									 singleSelect: true,
							         border: false,
							         striped: true,				
							         animate: true,
							         loader:function(param, success, error) {
							                $.ajax({
							                	type:"POST",
												url:getRootPath()+"/task/taskList",
												contentType:"application/json",
							                    data : JSON.stringify(tlPageDto),
							                    success : function(result) {
							                    	if(tlPageDto.page=="1"){		//  更新页面数据总数  TODO
														$("#taskPageTool span[id=totalPage]").text("共"+data.total+"条");
														tlPageDto.totalCount=data.total;
														let selectEdRows=$("#taskPageTool select[id=iEveryCount] option:selected").val();
														
														let totalPage=tlPageDto.totalCount%selectEdRows==0?tlPageDto.totalCount/selectEdRows:Math.floor((tlPageDto.totalCount/selectEdRows)+1);
														tlPageDto.totalPage=totalPage;
														$("#taskPageTool span[id=currentPage]").text("当前1/"+totalPage+"页");
													}else {
														$("#taskPageTool span[id=currentPage]").text("当前"+tlPageDto.page+"/"+tlPageDto.totalPage+"页");
													}
													let aarData=new Array();
													for(let i=0;i<result.rows.length;i++){
														let taskData=JSON.parse(result.rows[i].taskMsg);
														taskData.id=result.rows[i].id;
														taskData.updateTime=result.rows[i].updateTime;
														taskData.status=result.rows[i].status;
														taskData.taskCount=result.rows[i].taskCount;
														aarData.push(taskData);
														taskData.record="更多";
													}
							                    	success(aarData);
							                    }
							                });
										},
							         columns:[arrsColumn],
									 remoteSort:false,
									 onSortColumn:function(sort,order){
								    	 switch(sort){
								    	 	case "xm":
								    	 		sort="task_name";
								    	 		break;
								    	 	case "status":
								    	 		sort="status";
								    	 		break;
								    	 	case "taskCount":
								    	 		sort="task_count";
								    	 		break;
								    	 	default:
								    	 		sort="update_time";
								    	 		break;
								    	 		
								    	 }
								    	 tlPageDto.sort=sort;		
								    	 tlPageDto.order=order;
								    	console.log(sort,order); 
								     },
									 onClickCell:function(rowIndex,field,value){
										 IsCheckFlag=false;
									 },
									 onSelect: function (rowIndex, rowData) {
								         if (!IsCheckFlag) {
								             IsCheckFlag = true;
								             $("#task_grid").datagrid("unselectRow", rowIndex);
								         }
								     },                    
								     onUnselect: function (rowIndex, rowData) {
								         if (!IsCheckFlag) {
								             IsCheckFlag = true;
								             $("#task_grid").datagrid("selectRow", rowIndex);
								         }
								     },
									 onLoadSuccess:function(data){
//										 var e = $("#task_grid").datagrid('getColumnOption', 'productid');
//										  e.editor={};		//  禁止某一行可编辑
									 },
									 onBeforeEdit:function(index,row){
										 let options = $("#task_grid").datagrid("options");
									      options.tempeditor = options.tempeditor || {};
							                let getColumnOption = $("#task_grid").datagrid('getColumnOption', 'status');
							                let getdhhmColumnOption = $("#task_grid").datagrid('getColumnOption', 'dhhm');
							                
							                if (!options.tempeditor["status"]) {
							                    options.tempeditor["status"] = getColumnOption.editor;
							                }
							                if (!options.tempeditor["dhhm"]) {
							                	options.tempeditor["dhhm"] = getdhhmColumnOption.editor;
							                }
							                if (row.status==0) {
							                    getColumnOption.editor = undefined;
							                    getdhhmColumnOption.editor=options.tempeditor["dhhm"];
							                } else {
							                    getColumnOption.editor = options.tempeditor["status"];
							                    getdhhmColumnOption.editor=undefined;
							                }
										 
										 
										 
											row.editing = true;
											tlFun.updateActions(index);
										},
									onAfterEdit:function(index,row){
//										$("#task_grid").datagrid('beginEdit',index);
//										console.log("onAfterEdit",row);
											$.ajax({
												type:"post",
												url:getRootPath()+"/task/updateTaskMsg",
												contentType:"application/x-www-form-urlencoded",
												data:{taskMsg:"{map:"+JSON.stringify(row)+"}"},
												success:function(data){
													if(data.code==10000){
														row.editing = false;
														tlFun.updateActions(index);
														common_tool.messager_show(data.msg);
													}else{
														common_tool.messager_show("保存失败");
														$("#task_grid").datagrid('beginEdit',index);
													}
													console.log(data);
												}
												
											});
									},
									onCancelEdit:function(index,row){
											row.editing = false;
											tlFun.updateActions(index);
									}
								 });
								console.log(data);
							},
							error:function(data){
							}
						});
					},
					error:function(data){
						console.log("err-->"+data);
					},
				});
			},
			
			/**
			 * 行编辑
			 */
			updateActions:function(index){
				$("#task_grid").datagrid('refreshRow', index);
			},
			
			
			getRowIndex:function(target){
				var tr = $(target).closest('tr.datagrid-row');
				return parseInt(tr.attr('datagrid-row-index'));
			},
			editrow:function(target){
				$("#task_grid").datagrid('beginEdit', tlFun.getRowIndex(target));
			},
			deleterow:function(target){
				$.messager.confirm('Confirm','确定删除?',function(r){
					if (r){
						$("#task_grid").datagrid('deleteRow', tlFun.getRowIndex(target));
					}
				});
			},
			saverow:function(target){
				$("#task_grid").datagrid('endEdit', tlFun.getRowIndex(target));
				
			
			},
			cancelrow:function(target){
				$("#task_grid").datagrid('cancelEdit', tlFun.getRowIndex(target));
			},
			
			/**
			 * 任务通话记录修改备注
			 */
			remarkRecord:function(target){
				let rowIndex=tlFun.getRowIndex(target);
				let allData=$("#dialog_callRecord_task table[id=callRecord_grid]").datagrid('getData');
				let rowData=allData.rows[rowIndex];
				let desc=$(target).text();
				let callLogId=rowData.callId;
				
				var str; //score变量，用来存储用户输入的成绩值。
				str =prompt("修改备注信息",desc);
				if(str==null||str==desc)return;
				var data={id:callLogId,callDescription:str};
				$.ajax({
					type:"POST",
					data:data,
					dataType:"json",
					url:getRootPath()+"/deviceCallLog/update",
					success:function(data){			//    修改成功，刷新备注信息
						if(data.code==10000){
							$(target).text(str);
							$(target).attr("title",str);
						}else{
							alert(data.msg);
						}
					},
					error:function(XMLHttpRequest,textStatus,errThrown){
						alert("请求错误，请检查参数");
					}
				});
				
			},
			
			/**
			 * 查询通话记录 
			 */
			phoneRecord:function(target){
				let rowIndex=tlFun.getRowIndex(target);
				let allData=$("#task_grid").datagrid('getData');
				let rowData=allData.rows[rowIndex];
			
				console.log("selectRecord-->"+target+"RowIndex--->"+rowIndex+"---->"+rowData.dhhm);
				telPageDto.callNumber=rowData.dhhm;
				
				$("#dialog_callRecord_task").dialog({	//  发布成功dialog 关闭   
				    title: rowData.dhhm+"的通话记录与录音",
				    width: 1250,
				    height: 750,
				    closable:true ,
					cache: false,
					modal: true,
					resizable: false,
					onOpen:function(){
						let IsCheckFlag = true; //标示是否是勾选复选框选中行的，true - 是 , false - 否
						$("#dialog_callRecord_task table[id=callRecord_grid]").datagrid({
							
							loader:function(param, success, error) {
				                
				                $.ajax({
				                    type : "POST",
				                    url : getRootPath()+"/deviceCallLog/listByNum",
				                    contentType : 'application/json;charset=utf-8', // 设置请求头信息
				                    data : JSON.stringify(telPageDto),
				                    success : function(result) {       
				                    	success(result);
				                    }
				                });
							},
							 idField: "callId",
							 fitColumns: true,
							 rownumbers: true,
					         striped: true,
					         animate: true,
					         columns:[[
					        	  {title: "联系人", field: "callName", width: 75, align:'center',formatter: function (value, row, index) {
					        		  console.log("当前行--->"+value+"--row-->"+JSON.stringify(row)+"--index-->"+index);
					        		  	return row.sysDeviceRecord.callName;
					        		  },
					        	  },
					        	  {title: "任务操作员", field: "userName",sortable: true, width: 75, align:'center',formatter: function (value, row, index) {
					        		  console.log("当前行--->"+value+"--row-->"+JSON.stringify(row)+"--index-->"+index);
						        		  if(strIsEmpty(row.userName)){
						        			  return "无";
						        		  }else{
						        		  	return row.userName;
						        		  }
					        		  },
					        	  },
					        	  {title: "通话类型", field: "callType",align:'center', width: 65,
						        		formatter:function(value, row, index){
						        			var callType=row.sysDeviceRecord.callType
						        			 var type=common_tool.callType(callType);
						        			var tagEle=null;
						        			if(callType==2){	//  呼出
						        				tagEle="<a><img style='vertical-align:middle;' width='18px' height='18px' src='static/image/out_phone.png'/><span style='vertical-align:middle;'>&nbsp;"+type+"</span></a>";
						    				}else if(callType==1){	// 呼入
						    					tagEle="<a><img style='vertical-align:middle;' width='18px' height='18px' src='static/image/in_phone.png'/><span style='vertical-align:middle;'>&nbsp;"+type+"</span></a>";
						    				}else if(callType==3){	// 拒接
						    					tagEle="<a><img style='vertical-align:middle;' width='18px' height='18px' src='static/image/no_accept.png'/><span style='vertical-align:middle;'>&nbsp;"+type+"</span></a>";
						    				}else if(callType==5||callType==4){	// 1,未接  4,未接留言
						    					tagEle="<a><img style='vertical-align:middle;' width='18px' height='18px' src='static/image/resu_accept.png'/><span style='vertical-align:middle;'>&nbsp;"+type+"</span></a>";
						    				}else{
						    					tagEle="<a><span>"+type+"<span></a>";
						    				}
						        			return tagEle;
						        		},
						          },
					        	 
					        	  {title: "通话时长", field: "callDuration",align:'center', width: 70, sortable: true,
					        		  formatter:function(value, row, index){
					        			var secondIsValid=common_tool.secondIsValid((row.sysDeviceRecord.callDuration)+500);
					      			    var callDuration=common_tool.secondToMinute(secondIsValid==true?(row.sysDeviceRecord.callDuration/1000):row.sysDeviceRecord.callDuration);
						        		return callDuration;	
					        		  },  
					        	  },
					        	  {title: "录音时长", field: "callRecordMs",align:'center', width: 70, sortable: true,
					        		  formatter:function(value, row, index){
					        			   var recoAudioLength=row.sysDeviceRecord.callRecordMs==null?0:row.sysDeviceRecord.callRecordMs;
						      				 recoAudioLength=common_tool.secondToMinute(recoAudioLength/1000);
					        			  return recoAudioLength;	
					        		  },  
					        	  },
					        	  {title: "通话时间", field: "callDate",align:'center', width: 105, sortable: true,
					        		formatter:function(value,row,index){
					        			return common_tool.getMoth((row.sysDeviceRecord.callDate)+500);
					        		},  
					        	  },
					        	  {title: "序列号", field: "devSerial",align:'center', width: 105,
						        		formatter:function(value, row, index){
						        			return "<span title="+value+">"+value+"</span>";
						        		},
					        	  },
					        	  {title: "录音", field: "id",align:'center', width: 110,
						        		formatter:function(value, row, index){
						        			
						        			if(1==row.sysDeviceRecord.callHasRecord){
						        				var record=getRootPath()+"/deviceRecord/audio?callLogId="+row.sysDeviceRecord.id;
						        				let callRecord="<div class='audio bigTd recordSelect' style='max-width: 110px;'>"+
						        				"<div class='audiosx' style='height:35px;margin-left:5px;' name='audioTask' id='audioTask"+(row.sysDeviceRecord.id)+"'>"+
						        				"<button class='play' id='play'></button>"+
						        				"<div class='scroll' id='scrollBar'></div>"+
						        				"<div class='bar'></div>"+ 
						        				"<div class='mask'></div>"+ 
						        				"<span class='currTime' id='currTime'>00:00&nbsp;/&nbsp;</span>"+
						        				"<span class='time' id='time'>00:00</span>"+
						        				"<button class='down' id='down'></button>"+
						        				"<audio id='audioPlay' src='"+record+"' style='visibility: hidden;' controls='controls' ></audio>"+
						        				"</div>"+
						        				"</div>";
						        				return callRecord;
						        			}else {
												return "<span>无</span>";
											}
						        		},
					        	  },
					        	  {title: "修改备注", field: "callDescription",align:'center', width: 75,
						        		formatter:function(value, row, index){
						        			let callDes= row.sysDeviceRecord.callDescription;
						        			if(strIsEmpty(callDes)){
						        				return "<span style='cursor:pointer;color:#007ad5;font-size:15px;text-decoration: underline;' title='点击修改备注' onclick='tlFun.remarkRecord(this)'>无</span>";
						        			}else {
						        				return "<span style='cursor:pointer;color:#007ad5;font-size:15px;' title="+callDes+" onclick='tlFun.remarkRecord(this)'>"+callDes+"</span>";
											}
						        		},
					        	  },
					         ]],
					         onClickCell:function(rowIndex,field,value){
								 IsCheckFlag=false;
							 },
							 onSelect: function (rowIndex, rowData) {
						         if (!IsCheckFlag) {
						             IsCheckFlag = true;
						             $("#dialog_callRecord_task table[id=callRecord_grid]").datagrid("unselectRow", rowIndex);
						         }
						     },                    
						     onUnselect: function (rowIndex, rowData) {
						         if (!IsCheckFlag) {
						             IsCheckFlag = true;
						             $("#dialog_callRecord_task table[id=callRecord_grid]").datagrid("selectRow", rowIndex);
						         }
						     },
						     onSortColumn:function(sort,order){
						    	 switch(sort){
						    	 	case "callDate":
						    	 		sort="call_date";
						    	 		break;
						    	 	case "callDuration":
						    	 		sort="call_duration";
						    	 		break;
						    	 	case "callRecordMs":
						    	 		sort="call_record_ms";
						    	 		break;
						    	 	default:
						    	 		sort="update_time";
						    	 		break;
						    	 		
						    	 }
						    	 telPageDto.sort=sort;
						    	 telPageDto.order=order;
						    	console.log(sort,order); 
						     },
							 onLoadSuccess:function(data){
								 console.log("onLoadSuccess--->");
								 var gloPer=common_tool.getGloPer();
								 if(gloPer.indexOf("record:select")>-1){
										$(".recordSelect").show();
									}
								 var the_timeout=setTimeout(function(){
										csxAudio.init("audioTask");
									},30); 
							 },
						});
					},
					onClose:function(){  
						try {
				    		var audioStop=$(".audiosx audio[id=audioPlay]");
				    		console.log("audioStop-->"+audioStop.length);
				    		for(var i=0;i<audioStop.length;i++){
				    			audioStop[i].pause();
				    		}
						} catch (e) {
							console.log("没有audio控件");
						}
					},
				    buttons:[
						  {
		                         text: '确认',
		                         width: 90,
		                         handler: function () {
		                        	 
		                         }
		                  },
		                  {
		                      text: '取消',
		                      width: 90,
		                      handler: function () {
		                          $("#dialog_callRecord_task").dialog('close');
		                      }
		                  }
	              ],
				});
				 $("#dialog_callRecord_task").dialog("center");
			},
			
			
			
			
			/**
			 * 初始化任务列表
			 */
			initTaskList:function(){
				$.ajax({
					type:"POST",
					url:getRootPath()+"/task/taskList",
					contentType:"application/json",
					data:JSON.stringify(tlPageDto),
					success:function(data){
						var aarData=new Array();
						for(var i=0;i<data.rows.length;i++){
							var taskData=JSON.parse(data.rows[i].taskMsg);
							aarData.push(taskData);
						}
					},
					error:function(data){
						
					}
				});
			},
			
			/**
			 * 上传任务文件
			 */
			upTaskFile:function(){
				var formData = new FormData();
				var temp=$("#fb").filebox("getValue");
				var temp1=$("#form_task input[type=file]")[0].files[0];
				if(temp1==undefined){
					alert("请点击选择文件");
					return;
				}
				var isCsv=temp1.name.lastIndexOf("csv");
				if(isCsv==-1){
					alert("文件不符规则，请上传csv文件");
					return;
				}
				formData.append('taskFile', $("#form_task input[type=file]")[0].files[0]);
				formData.append('userId',common_tool.getCurrUserId());
				 $.ajax({  
					    type: "POST",  
					    url:getRootPath()+"/task/upTF",  
					    data:formData,
					    cache: false,
					    processData: false,
				        contentType: false,
					    async: false,  
					    error: function(request) {  
					        alert("Connection error");  
					    },  
					    success: function(data) {  
					    	console.log(data);
					    	common_tool.messager_show(data.msg);
					    	if(data.code==10000){
					    		$("#dialog_uptask").dialog("close");
					    		taskFun.initTaskGroup();
					    	}
					    }  
					  })
			},
			
			/**
			 * 刷新授权
			 */
			refreshAuth:function(){
				 var gloPer=common_tool.getGloPer();
					if(gloPer.indexOf("task:insert")>-1){
						$(".taskInsert").show();
					}
					if(gloPer.indexOf("task:select")>-1){
						$(".taskSelect").show();
					}
					if(gloPer.indexOf("task:delete")>-1){
						$(".taskDelete").show();
					}
					if(gloPer.indexOf("record:select")>-1){
						$(".recordSelect").show();
					}
			},
			
	};
	$.extend($.fn.datagrid.defaults.editors.combobox, {
	        getValue: function (target) {
	        	var comValue=$(target).combobox("getValue");
	        	console.log("getValue-->"+$(target).combobox("getValue"));
	        	if(comValue=="2"){
	        		return 2;
	        	}else if(comValue=="1"){
					return 1;
				}
	        },
	        setValue: function (target, value) {
	        	console.log("setValue--->"+value);
	        	 $(target).combobox("setValue", value);  
	        },
	    });
	
	$(document).ready(function(){
		tl.data.btnFileUp=$("#form_task button[id=btn_confirm_taskUp]");
		tl.data.table_task_group=$("#div_task_container table[id=taskGroup]");
		tl.data.btn_dispatch_task=$("#btn_dispatch_task");
		//初始化userId
		groupPageDto.userId=common_tool.getCurrUserId();
	
		tl.data.form_task=$("#form_task");
		tl.data.btnFileUp.click(function() {
//			tlFun.upTaskFile();
		});
		$("#fb").filebox({
            buttonText: '选择csv文件',
            buttonAlign: 'left',
            accept:".csv",
        });
		tlFun.refreshAuth();
		//  加载任务组
		taskFun.initTaskGroup();
		tl.data.btn_dispatch_task.click(function() {
			taskFun.dispathTask();
		});
		$("#btn_title_dispatch").click(function() {
			$("#dialog_uptask").dialog({	//  发布成功dialog 关闭   
			    title: '发布/更新任务',
			    width: 450,
			    height: 400,
			    closable:true ,
				cache: false,
				modal: true,
				resizable: false,
				onClose:function(){  
					$("#fb").filebox("clear");
				},
				onOpen:function(){
					console.log("对话框打开");
				},
			    buttons:[
					  {
	                         text: '确认',
	                         width: 90,
//	                         iconCls: 'icon-add',
	                         handler: function () {
//	                        	 gfds{}
//	                        	 alert("确认分派任务");
//	                        	 taskFun.confirmDispatch();
	                        	 if(confirm("发布任务后,任务将会更新，是否发布?")){
	                        		 tlFun.upTaskFile();
	                        	 }
	                         }
	                     },
                  {
                      text: '取消',
                      width: 90,
//                      iconCls: 'icon-add',
                      handler: function () {
                          $("#dialog_uptask").dialog('close');
                      }
                  }
              ],
			});
			 $("#dialog_uptask").dialog("center");
		});
		
		/**
		 * 已分派任务，未分派任务checkBox切换
		 */
		$("#cbDispatch").change(function() {
			tlFun.initTaskColumn();
//			alert(check);
		});
		var heightY=$(window).height();
		var y = $("#inscribe").offset().top;
		console.log("底部标签位置"+y+"---->"+heightY);
		//  底部标签位置535.0972290039062---->937
		var marginValue=(heightY-y)/2;
		$("#task_list").css("margin-bottom",marginValue);
		
		
	});
		
	
	
	
})();