<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%--我的任务 --%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<style type="text/css"> 
	.myTask{
		padding: 20px 0px 20px 8px;
		font-size: 20px;
	}
	
	.datagrid-row{
		height: 48px;
		font-size: 14px;
	}
	
	.datagrid .datagrid-editable-input{
		font-size: 14px;
		height: 35px;
	}
	.textbox .textbox-text{
		font-size: 14px;
	}

	.combobox-item-selected{
		background:#c4e4ff;
	}
	.panel-header, .panel-body{
		border-style: none;
	}
</style>
</head>
<body>


<div id="div_task_container" class="taskSelect" style="margin-top: 30px;">
		
		<div style="float: left; width: 17%; height: 100%;">
			<div class="taskInsert userManage" style="margin-bottom:20px; border:0px">
				<button id="btn_title_dispatch" class="btn-green-allDevice" style="font-size: 18px;width: 100%;height: 40px;margin: 0px;">发布&nbsp;/&nbsp;更新任务</button>
			</div>
			<table id="taskGroup"></table>
			<!--  <table id="taskGroup" style="border: none;" class="easyui-datagrid" data-options="
              singleSelect:'true',
              idField:'id',
         	   fitColumns: 'true',
                ">
              
                <thead frozen="true">
	                <tr style="padding-bottom: 20px;padding-top: 20px;">
	                    <th data-options="field:'taskGroupName',width:'100%',align:'center'" >我的任务组</th>
	                </tr>
                </thead>
            </table> -->
		</div>
		
		
		<div style="float:right;width:82%;height: 100%;" id="task_list">
			<div  style="position: relative; height: 105px;"class="clearfix"">
		 		<div id="task_group" style="float: left;position: absolute;">
		 			<table style="display:inline-block;">
						<tr>
							<td rowspan="3">
								<img alt="我的任务"  class="deviceHeadImg"  src="${pageContext.request.contextPath}/static/css/home/image/task_gray.png">
							</td>	
							<td id="td_group_title" style="padding-left: 20px; font-size: 24px;">
								<span id="span_group">任务</span>
								<button id="btn_dispatch_task" class="btn-green-allDevice taskDelete userManage" style="font-size: 16px;width: 90px;height: 28px;">分派任务</button>
							</td>
							
						</tr>
						<tr><td></td></tr>
						<tr>
							<td id="sp_group_detail" style="padding-left: 20px; color: #8A8A8A;font-size: 16px;"></td>
						</tr>
					</table>
				</div>
				
				<div style="float: right;position: absolute;top: 2%;right: 10px;" id="div_search_module">
				<!-- 发布任务 <input class="easyui-filebox" name="file1"/> -->
					<!-- <form action="javascript:void(0)" method="post" name="form" enctype="multipart/form-data" style="font-size: 14px;" id="form_task">
				  		<input id="fb" name="taskFile" type="text" style="width:300px;height: 40px;">
				  		<button id="btn_confirm_taskUp" class="btn-green-allDevice" style="font-size: 18px;width: 110px;height: 38px;">发布&nbsp;/&nbsp;更新</button>
			 		</form> -->
			 		<!-- <input type="text" class="search" style="height: 38px;" placeholder="输入电话号码，人名"/>
			 		<input type="button" name="allDevice" id="btn_blue_searchtask" class="alldevice" value="搜索"/> -->
			 		<div style="margin-top: 50px; margin-right: 40px;" class="taskDelete">
		    			<input type="checkbox" checked="checked" style="width: 22px;height: 22px;cursor: pointer;margin: 0px;vertical-align: middle;display: none" id="cbDispatch">
						<label for="selectStatus" style="font-size: 16px;vertical-align: middle;
						 ">选择任务状态：</label>
						  <input id="selectStatus" class="taskDelete" style="font-size: 15px;" name="dept" value="aa">
						<!--   <select id="selectStatus">
						  	<option>已分派的任务</option>
						  	<option>未分派的任务</option>
						  	<option>已完成的任务</option>
						  	<option>未完成的任务</option>
						  </select> -->
			 		</div>
			 		
				</div>
				
		 	</div>
			<!-- 任务列表 -->
		   <table id="task_grid" style="margin-top: 10px;height:250px;    border-style: none;"></table>
		   
		   <div id="taskPageTool" class="myPage" align="center" style="margin: 80px 0px 40px 0px; height: 30px;line-height: 30px;">
				<select id="iEveryCount" style="height: 40px;margin-right: 10px;font-size: 20px;" onchange="taskFun.changePages('taskPageTool')">
					<option>20</option>
					<option>30</option>
					<option>40</option>
				</select>
				<span class="pageDisplay" id="currentPage">当前 1/2 页</span>
				<span class="pageDisplay" id="totalPage">共21条</span>
				&nbsp;&nbsp;
				<a class="pageOption" href="javascript:void(0)" onclick="taskFun.firstPage('taskPageTool')">首页</a>
				&nbsp;
				<a class="pageOption" href="javascript:void(0)" id="ipreviousPage" onclick="taskFun.prePage('taskPageTool')">上一页</a>
				<a class="pageOption" href="javascript:void(0)" id="inextPage" onclick="taskFun.nextPage('taskPageTool')">下一页</a>
				&nbsp;
				<a class="pageOption" href="javascript:void(0)" onclick="taskFun.endPage('taskPageTool')">尾页</a>
				&nbsp;&nbsp;
				<span class="pageDisplay">转到</span>
				&nbsp;
				<input type="text" maxlength="4" size="1" style="height: 30px;font-size: 20px;">
				&nbsp;
				<span class="pageDisplay">页</span>				
				<a class="pageOption" href="javascript:void(0)" onclick="taskFun.jumpPage('taskPageTool')">跳转</a>
			</div>
		   
		   
		   
		 <!-- 任务发布dialog  -->
		 <div id="dialog_uptask" style="display: none; margin: 0 auto;text-align: center;">
		 	<div style="margin-top: 20px;" id="curCheckTask">当前任务</div>
		 	<form action="javascript:void(0)" method="post" name="form" enctype="multipart/form-data" style="font-size: 14px;margin: 0 auto;text-align: center;margin-top: 45px;" id="form_task">
		 		<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
				 <input id="fb" name="taskFile" type="text" style="width:300px;height: 40px;">
				  <button id="btn_confirm_taskUp" class="btn-green-allDevice" style="font-size: 16px;width: 100px;height: 35px;margin-top: 50px;">确定</button>
				  <button id="btn_delete_taskGroup" class="btn-red-allDevice" style="font-size: 16px;width: 120px;height: 35px;margin-top: 50px;">移除当前任务</button>
			 </form>
		
		 </div>
		 
		 <%-- 任务分发dialog --%>
		 
 		 <div id="dialog_dispatch_task" style="display: none;">
				<div style="float:left; width:28%;height: 100%;">
					<div style="margin-top: 10px;margin-bottom:10px; text-align: center;" title="一键分派，选中任务接收人后，确认分派，任务被平均分派">
						<div style="display: inline-block;margin-bottom:10px;">
							<input type="checkbox" style="width: 22px;height: 22px;cursor: pointer;margin: 0px;vertical-align: middle;" id="autoDispatch">
							<label for="autoDispatch" style="font-size: 18px;vertical-align: middle;cursor: pointer;
							  color: #0092ff;">开启一键分派</label>
							  <span id="spanUnDispatch" style="font-size: 14px;margin-left: 8px; vertical-align: middle;"></span>
						</div>
						 </br>
						<span style="color: red;font-size: 14px; margin: 0px auto;">开启一键分派,不再显示任务详情,</br>选中任务接收人,确认分派,任务被平均分派</span>
					</div>
		            <table id="dialog_org_user"></table>
		        </div>
		         <div style="float:right;width:72%;height: 100%;">
		           <table id="dialog_task_grid"></table>
		         </div>
		 </div>
		 
		 <!-- 通话记录 dialog-->
		 <div id="dialog_callRecord_task" style="display: none;">
		  	<!-- <table id="callRecord_grid" style="margin-top: 10px;height:250px"></table> -->
		  	
		  	 <table id="callRecord_grid" style="height:auto;" class="easyui-datagrid" data-options="
              singleSelect:'true',
              idField:'callId',
         	   fitColumns: 'true',
                ">
              
              <!--   <thead frozen="true">
	                <tr style="padding-bottom: 20px;padding-top: 20px;">
	                    <th data-options="field:'orgName',width:'10%',align:'center'" >联系人</th>
	                    <th data-options="field:'devSerial',width:'10%',align:'center'" >通话类型</th>
	                    <th data-options="field:'callDuration',width:'10%',align:'center'" >通话时长</th>
	                    <th data-options="field:'callDate',width:'10%',align:'center'" >通话时间</th>
	                </tr>
                </thead> -->
                
            </table>
		  	
		  	 
		   <div id="dialogRecordPageTool" class="myPage" align="center" style="margin: 80px 0px 40px 0px; height: 30px;line-height: 30px;">
				<select id="iEveryCount" style="height: 40px;margin-right: 10px;font-size: 20px;" onchange="taskFun.changePages('dialogRecordPageTool')">
					<option>10</option>
					<option>20</option>
					<option>30</option>
				</select>
				<span class="pageDisplay" id="currentPage">当前 1/2 页</span>
				<span class="pageDisplay" id="totalPage">共21条</span>
				&nbsp;&nbsp;
				<a class="pageOption" href="javascript:void(0)" onclick="taskFun.firstPage('dialogRecordPageTool')">首页</a>
				&nbsp;
				<a class="pageOption" href="javascript:void(0)" id="ipreviousPage" onclick="taskFun.prePage('dialogRecordPageTool')">上一页</a>
				<a class="pageOption" href="javascript:void(0)" id="inextPage" onclick="taskFun.nextPage('dialogRecordPageTool')">下一页</a>
				&nbsp;
				<a class="pageOption" href="javascript:void(0)" onclick="taskFun.endPage('dialogRecordPageTool')">尾页</a>
				&nbsp;&nbsp;
				<span class="pageDisplay">转到</span>
				&nbsp;
				<input type="text" maxlength="4" size="1" style="height: 30px;font-size: 20px;">
				&nbsp;
				<span class="pageDisplay">页</span>				
				<a class="pageOption" href="javascript:void(0)" onclick="taskFun.jumpPage('dialogRecordPageTool')">跳转</a>
			</div>
		   
		  	
		 </div>
		 <!-- <table id="dg"></table> -->
	</div>
	
</div>	
	
	
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/tasklist.js"></script>
	<script type="text/javascript">
			window.addEventListener('error',function(e){
		        alert(e.filename+'\nLine '+e.lineno+'\n'+e.message);
		    });
	</script>
	<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/test.js"></script> --%>
	<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/myTask.js"></script> --%>
</body>
</html>