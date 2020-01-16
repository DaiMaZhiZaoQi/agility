<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/system/user.js"></script>
<div id="user-tool-bar" style="padding: 10px 10px 0 10px">
    <form id="user-search-form">
        <div class="easyui-linkbutton " id="user-flash-btn" data-options="iconCls:'icon-reload'" style="width:70px">刷新
        </div>
        <div class="easyui-linkbutton " id="user-save-btn" data-options="iconCls:'icon-add'" style="width:70px">新增</div>
        <div class="easyui-linkbutton " id="user-update-btn" data-options="iconCls:'icon-edit'" style="width:70px">修改
        </div>
        <div class="easyui-linkbutton " id="user-delete-btn" data-options="iconCls:'icon-remove'" style="width:70px">删除
        </div>
        <div class="easyui-linkbutton " id="user-enable-btn" data-options="iconCls:'icon-add'" style="width:70px">启用
        </div>
        <div class="easyui-linkbutton " id="user-forbidden-btn" data-options="iconCls:'icon-remove'" style="width:70px">
            禁用
        </div>
        <div class="easyui-linkbutton " id="user-password-btn" data-options="iconCls:'icon-remove'"
             style="width:120px">重置密码
        </div>
        <!-- <div class="easyui-linkbutton" id="user-addpermission-btn" data-options="iconCls:'icon-add'" style="width:120px;">添加用户权限</div> -->
        
        <span style="line-height: 26px; " class="user-l-btn-text">登录名:<input name="search-loginName" class="easyui-textbox" width=""
                                                     style="line-height: 26px; "></span>
        <span style="line-height: 26px; " class="user-l-btn-text">中文名:<input name="search-zhName" class="easyui-textbox"
                                                     style="line-height: 26px; "></span>
        <!-- <span style="line-height: 26px; " class="l-btn-text">邮箱:<input name="search-email" class="easyui-textbox"
                                                    style="line-height: 26px; "></span> -->
        <span style="line-height: 26px; " class="user-l-btn-text">电话:<input name="search-phone" class="easyui-textbox"
                                                    style="line-height: 26px; "></span>&nbsp;
       <!--  <span style="line-height: 26px; ">地址:<input name="search-address" class="easyui-textbox"
                                                    style="line-height: 26px; "></span>&nbsp; -->
        <div class="easyui-linkbutton " id="log-select-btn" data-options="iconCls:'icon-search'" style="width:70px">搜索
        </div>
    </form>
</div>
<div id="user_grid" style="padding: 10px">

</div>
<div id="user_edit_dialog">
    <form id="user_form">
        <input type="hidden" name="id" id="id">
	        <!-- <input type="text"  name="loginName" style="display:none; width: 0px;height: 0px;" autocomplete="new-password" />
			<input type="password" name="password" style="display:none; width: 0px;height: 0px;" /> -->
        <div style="float: left;height: 280px;width:20%">
            <p style="padding: 10px;margin: 0px;">&nbsp;登录名:&nbsp;<input name="loginName" id="loginName" type="text" style="width: 190px;height: 35px;"
                                                       data-options="required:true,validType:'logName',invalidMessage:'登录名必须字母或数字,4到16位'"
                                                       class="easyui-textbox easyui-validatebox" autocomplete="new-password"></p>
            <p style="padding: 10px;margin: 0px;">&nbsp;&nbsp;密码:&nbsp;<input name="password" type="password" id="password"
                                                             style="width: 190px;height: 35px"
                                                             data-options="required:true,validType:['length[6,20]']"
                                                             class="easyui-textbox easyui-validatebox" autocomplete="new-password"></p>
            <p style="padding:10px;margin: 0px;">&nbsp;中文名:&nbsp;<input name="zhName" id="zhName"
                                                        style="width: 190px;height: 35px"
                                                        data-options="required:true"
                                                        class="easyui-textbox easyui-validatebox"></p>
            <p style="padding:10px;margin:0px;">&nbsp;英文名:&nbsp;<input name="enName" id="enName"
                                                        style="width: 190px;height: 35px"
                                                        data-options="required:false"
                                                        class="easyui-textbox easyui-validatebox"></p>
            <p style="padding:10px;margin: 0px;">&nbsp;&nbsp;性别:&nbsp;<select name="sex" id="sex"
                                                              style="width: 190px;height: 35px"
                                                              data-options="required:true"
                                                              class="easyui-combobox easyui-validatebox">
                <option value="1" selected="selected">男</option>
                <option value="2">女</option>
            </select>
            </p>
            <p style="padding:10px;margin: 0px;">&nbsp;&nbsp;生日:&nbsp;<input name="birth" class="easyui-datebox" id="birth"
                                                             style="width: 190px;height: 35px" type="text"
                                                             data-options="required:false,editable:false"
                                                             class="easyui-textbox easyui-validatebox"></p>
            <p style="padding:10px;margin: 0px;">&nbsp;&nbsp;邮箱:&nbsp;<input name="email" id="email"
                                                             style="width: 190px;height: 35px"
                                                             data-options="required:false,validType:['email']"
                                                             class="easyui-textbox easyui-validatebox"></p>
            <p style="padding:10px;margin: 0px;">&nbsp;&nbsp;电话:&nbsp;<input name="phone" id="phone"
                                                             style="width: 190px;height: 35px"
                                                             data-options="required:false,validType:['length[11,11]'],invalidMessage:'请输入11位手机号'"
                                                             class="easyui-textbox easyui-validatebox"></p>
            <p style="padding:10px;margin: 0px;">&nbsp;&nbsp;地址:&nbsp;<input name="address" id="address"
                                                             style="width: 190px;height: 35px"
                                                             data-options="required:false"
                                                             class="easyui-textbox easyui-validatebox"></p>
        </div>
        
        <!-- 我的组织 -->
         <div  style="float: left;width: 30%;height: 100%;">
        	<table id="user_in_orgs" class="easyui-treegrid"
                   data-options="
            <%--     url:'${pageContext.request.contextPath}/organization/list',
                method:'get', --%>
                idField: 'id',
                treeField: 'name',
                border: true,
                rownumbers: true,
                fit: true,
                fitColumns: true,
                singleSelect:true,
                checkOnSelect:true,
                required:true,">
                <thead frozen="true">
	                <tr>
	                    <th data-options="field:'ck', checkbox: true">选择</th>
	                </tr>
                </thead>
                <thead>
	                <tr>
	                    <th data-options="field:'name',width:260">我的部门</th>
	                </tr>
                </thead>
            </table>
        </div>
        
        
        
        <!-- 用户角色  -->
        <div style="float:left;width:20%; height: 100%;">
            <table id="user_roles" class="easyui-datagrid"
                   data-options="
          <%--       url:'${pageContext.request.contextPath}/role/list',
                method:'get', --%>
                idField: 'id',
                nodeId:'id',
                treeField: 'name',
                border: true,
                rownumbers: true,
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
	                    <th data-options="field:'name',width:260">用户角色</th>
	                </tr>
                </thead>
            </table>
        </div>
        
        <!-- 权限  -->
       <%--  <div style="float: right;width:320px; height: 100%;">
            <table id="user-permissions" class="easyui-datagrid" data-options="
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
                    <th data-options="field:'name',width:200">权限</th>
                </tr>
                </thead>
            </table>
        </div> --%>
        
         <!-- 角色机构权限 -->
        <!--  <div style="float: right; width: 25%;height: 100%;">
            <table id="org-permissions" class="easyui-datagrid" data-options="
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
        </div> -->
        
        <!--  singleSelect:false, selectOnCheck:false, -->
        
        <!-- 机构权限 -->
        <div  style="float: right;width: 30%;height: 100%;">
        	<table id="user_orgs" class="easyui-treegrid"
                   data-options="
            <%--     url:'${pageContext.request.contextPath}/organization/list',
                method:'get', --%>
                idField: 'id',
                treeField: 'name',
                border: true,
                rownumbers: true,
                fit: true,
                fitColumns: true,
                singleSelect:false,
               
                checkOnSelect:true,
                required:true,">
                <thead frozen="true">
	                <tr>
	                    <th data-options="field:'ck', checkbox: true">选择</th>
	                </tr>
                </thead>
                <thead>
	                <tr>
	                    <th data-options="field:'name',width:260">部门权限</th>
	                </tr>
                </thead>
            </table>
        </div>
        
        <!-- 职位  -->
       <%--  <div style="float: right;width: 360px;height: 100%;">
            <table id="jobs" class="easyui-treegrid"
                   data-options="
                url:'${pageContext.request.contextPath}/job/list',
                method:'get',
                idField: 'id',
                nodeId:'id',
                treeField: 'name',
                rownumbers: true,
                fit: true,
                fitColumns: true,
                singleSelect:true,">
                <thead frozen="true">
                <tr>
                    <th data-options="field:'ck', checkbox: true">选择</th>
                </tr>
                </thead>
                <thead>
                <tr>
                    <th data-options="field:'name',width:320">职位</th>
                </tr>
                </thead>
            </table>
        </div> --%>

    </form>
</div>
<%-- 
<!-- 添加用户权限 -->
<div id="save-permission-dialog">
<!--    -->
    <form action="#" id="save-permission-form">
     <!--    <input type="hidden" id="id" name="id"> -->
        <div style="float: left;height: 300px;">
            <p style="padding: 10px;">&nbsp;&nbsp;名称:<input name="permission_name" id="permission_name"
                                                            style="width: 300px;height: 35px"
                                                            data-options="required:true"
                                                            class="easyui-textbox easyui-validatebox"></p>
            <p style="padding: 10px ;">&nbsp;&nbsp;编码:<input name="permission_code" id="permission_code"
                                                             style="width: 300px;height: 35px"
                                                             data-options="required:true"
                                                             class="easyui-textbox easyui-validatebox"
                                                            ></p>
            <p style="padding: 10px;"> &nbsp;&nbsp;描述:<input name="permission_description" id="permission_description"
                                                             style="width: 300px;height: 130px"
                                                             data-options="required:true,multiline:true"
                                                             class="easyui-textbox easyui-validatebox">
            </p>
        </div>
        <div style="float: right; width: 40%; height:326px;">
            <table class="easyui-datagrid" id="permission-group" data-options="
                   url:'${pageContext.request.contextPath}/permission/group/list',
                   idField: 'id',
                   method:'get',
                   fitColumns:true,
                   fit:true,
                   singleSelect:true,
                ">
                <thead frozen="true">
                <tr>
                    <th data-options="field:'ck', checkbox: true">选择</th>
                </tr>
                </thead>
                <thead>
                <tr>
                    <th data-options="field:'name',width:200">权限组名称</th>
                </tr>
                </thead>
            </table>
        </div>
    </form>
</div>
 --%>


<div id="password_edit_dialog">
    <form id="init_password_form" style="padding: 20px 0 0 0;">
        <p style="padding: 10px 0 10px 23px ;">&nbsp;&nbsp;密码:<input name="newPassword" type="password" id="newPassword"
                                                                     style="width: 300px;height: 35px"
                                                                     data-options="required:true,validType:['length[6,20]']"
                                                                     class="easyui-textbox easyui-validatebox"></p>
        <p style="padding: 10px 0 10px 23px ;">重复密码:<input name="repeatNewPassword" type="password"
                                                           id="repeatNewPassword"
                                                           style="width: 300px;height: 35px"
                                                           data-options="required:true,validType:['length[6,20]']"
                                                           class="easyui-textbox easyui-validatebox"></p>
    </form>
</div>










