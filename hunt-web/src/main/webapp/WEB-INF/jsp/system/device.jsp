<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/system/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/system/device.js"></script>
	<div id="device-tool-bar" style="padding: 10px">
		<form id="device-search-form">
		    <div class="easyui-linkbutton " id="device-select-btn" data-options="iconCls:'icon-reload'" style="width:70px">刷新</div>
		    <div class="easyui-linkbutton " id="device-save-btn" data-options="iconCls:'icon-add'" style="width:70px">新增</div>
			<div class="easyui-linkbutton " id="device-update-btn" data-options="iconCls:'icon-edit'" style="width:70px">修改</div>
			<div class="easyui-linkbutton " id="device-delete-btn" data-options="iconCls:'icon-remove'" style="width:70px">删除</div>
			<span style="line-height: 26px; ">设备名:<input name="search-deviceName" class="easyui-textbox" id="search-deviceName"
		                                                     style="line-height: 26px; "></span>
		    <span style="line-height: 26px; ">设备序列号:<input name="search-deviceSerial" class="easyui-textbox" id="search-deviceSerial"
		                                                    style="line-height: 26px; "></span>&nbsp;
			 <div class="easyui-linkbutton " id="device-search-btn" data-options="iconCls:'icon-search'" style="width:70px">搜索
		        </div>                                                    
		</form>
	</div>

<div id="device_grid" style="padding: 10px">
	<div>${sessionScope.get("loginInfo").jobs}</div>
</div>

<!-- 添加或修改设备的对话框   必填参数  userId roleOrgId(这两个参数在绑定人员时可以拿到) deviceName deviceSerial description createBy -->
<div id="device_edit_dialog">
	<form id="device_edit_form">
		<div style="float: left;width:270px;">
            <input type="hidden" name="id" id="iddevice">
            <div style="padding: 5px">
           	<div> &nbsp;设备名称:</div>
            <input name="deviceName" id="deviceName"
                 style="width: 300px;height: 35px;margin:5px 0px 0px 10px;"
                 data-options="required:true"
                 class="easyui-textbox easyui-validatebox">
           </div>
                                                          
              <div style="padding: 5px">
              	<div>&nbsp;设备序列号:</div>
              	<input name="deviceSerial" id="deviceSerial"
                      style="width: 300px;height:35px;margin:5px 0px 0px 10px;"
                      data-options="required:true"
                      class="easyui-textbox easyui-validatebox">
             </div>                                           
            <div style="padding: 5px;"> 
	           	<div>&nbsp;设备描述:</div> 
	            <input name="description" id="description"
	             	style="width: 300px;height: 130px;margin:5px 0px 0px 10px;"
	                data-options="multiline:true"
	                class="easyui-textbox easyui-validatebox">
             </div>
        </div>
        
          <!--   view:groupview,
                groupField:'sysPermissionGroupId',
                groupFormatter: function (value, rows) {
                return rows[0].sysPermissionGroupName;
                }, -->
          <div style="float: right;width: 300px;height: 100%;">
            <table id="deviceJobs" class="easyui-treegrid"
                   data-options="
                url:'${pageContext.request.contextPath}/job/list',
                method:'get',
                idField: 'id',
                nodeId:'id',
                treeField: 'name',
                rownumbers: true,
                fit: true,
                fitColumns: false,
                singleSelect:false,
            ">
                <thead frozen="true">
                <tr>
                    <th data-options="field:'ck', checkbox: true">选择</th>
                </tr>
                </thead>
                <thead>
                <tr>
                    <th data-options="field:'name',width:260">职位</th>
                </tr>
                </thead>
            </table>
        </div>
        
        <div style="float: right; width:300px;height: 100%;">
            <table id="device_user_list" class="easyui-datagrid" data-options="
                url:'${pageContext.request.contextPath}/user/list',
                method:'get',
                idField:'id',
            	singleSelect: true,
                fitColumns: true,
                rownumbers: true,
                fit: true,
                ">
                <thead frozen="true">
                <tr>
                    <th data-options="field:'ck', checkbox: true">选择</th>
                </tr>
                </thead>
                <thead>
                <tr>
                    <th data-options="field:'loginName',width:200">使用人员</th>
                </tr>
                </thead>
            </table>
        </div>
      
	</form>

</div>
