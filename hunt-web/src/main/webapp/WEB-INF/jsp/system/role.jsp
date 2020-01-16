<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/system/role.js"></script>
<div id="role-tool-bar" style="padding: 10px">
    <div class="easyui-linkbutton " id="role-select-btn" data-options="iconCls:'icon-reload'" style="width:70px">刷新
    </div>
    <div class="easyui-linkbutton " id="role-save-btn" data-options="iconCls:'icon-add'" style="width:70px">新增</div>
    <div class="easyui-linkbutton " id="role-update-btn" data-options="iconCls:'icon-edit'" style="width:70px">修改</div>
    <div class="easyui-linkbutton " id="role-delete-btn" data-options="iconCls:'icon-remove'" style="width:70px">删除
    </div>

</div>
<div id="role_grid" style="padding: 10px">

</div>

<div id="role_edit_dialog">
    <form id="role_edit_form">
        <div style="float: left;width: 50%;">
            <input type="hidden" name="role_id" id="role_id">
            <p style="padding: 5px">&nbsp;&nbsp;角色名称:&nbsp;<input name="role_name" id="role_name"
                                                          style="width: 250px;height: 35px"
                                                          data-options="required:true"
                                                          class="easyui-textbox easyui-validatebox"></p>
            <p style="padding: 5px;"> &nbsp;&nbsp;角色描述:&nbsp;<input name="description" id="role_description"
                                                            style="width: 250px;height: 130px"
                                                            data-options="multiline:true"
                                                            class="easyui-textbox easyui-validatebox">
        </div>
        <!-- 角色权限  -->
        <div style="float: left; width:49%;height: 100%;">
            <table id="role-permissions" class="easyui-datagrid" data-options="
                url:'${pageContext.request.contextPath}/permission/list',
                method:'get',
                idField:'id',
                view:groupview,
                groupField:'sysPermissionGroupId',
                groupFormatter: function (value, rows) {
                return rows[0].sysPermissionGroupName;
                },
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
                    <th data-options="field:'name',width:200">角色权限全选</th>
                </tr>
                </thead>
            </table>
        </div>
        
         <!-- 角色机构权限 -->
       <%--   <div style="float: right; width: 25%;height: 100%;">
            <table id="role-org-permissions" class="easyui-datagrid" data-options="
                idField:'id',
                view:groupview,
                groupField:'perGroupId',
                groupFormatter: function (value, rows) {
                	var len=rows.length;
                	for(var i=0;i&lt;len;i++){
                		console.log(rows[i].permGroupName);
                		if(rows[i].length!=0){
                			
			               	 return rows[i].permGroupName;
                		}
                	}
                },
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
                    <th data-options="field:'name',width:200">组织权限</th>
                </tr>
                </thead>
            </table>
        </div>
        
        
          <!-- 机构 -->
        <div  style="float: right; width: 25%;;height: 100%;">
        	<table id="role_orgs" class="easyui-treegrid"
                   data-options="
                url:'${pageContext.request.contextPath}/organization/list',
                method:'get',
                idField: 'id',
                treeField: 'name',
                border: true,
                rownumbers: true,
                 singleSelect:false,
                fit: true,
                fitColumns: true,
                checkOnSelect:true,
                required:true,">
                <thead frozen="true">
	                <tr>
	                    <th data-options="field:'ck', checkbox: true">选择</th>
	                </tr>
                </thead>
                <thead>
	                <tr>
	                    <th data-options="field:'name',width:260">组织名称</th>
	                </tr>
                </thead>
            </table>
        </div> --%>
       
    </form>
</div>