<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>


<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/system/organization.js"></script>

<div id="tool-bar" style="padding: 10px">
    <div class="easyui-linkbutton select-btn " data-options="iconCls:'icon-reload'" style="width:70px">刷新</div>
    <div class="easyui-linkbutton save-btn" data-options="iconCls:'icon-add'" style="width:70px">新增</div>
    <div class="easyui-linkbutton update-btn" data-options="iconCls:'icon-edit'" style="width:70px">修改</div>
    <div class="easyui-linkbutton delete-btn" data-options="iconCls:'icon-remove'" style="width:70px">删除</div>
</div>
<div id="organization" style="padding: 10px">

</div>

<div id="organization_save">
    <form id="organization_form">
        <input type="hidden" name="id" id="org_id">
        <div id="organization_save_left" style="float: left;height: 100%;">
            <p style="padding: 10px;">&nbsp;&nbsp;部门简称:&nbsp;<input name="name" id="name" style="width: 300px;height: 35px"
                                                            data-options="required:true"
                                                            class="easyui-textbox easyui-validatebox"></p>
            <p style="padding: 10px ;">&nbsp;&nbsp;部门全称:&nbsp;<input name="fullName" id="fullName"
                                                             style="width: 300px;height: 35px"
                                                             data-options="required:true"
                                                             class="easyui-textbox easyui-validatebox"></p>
            <p style="padding: 10px;"> &nbsp;&nbsp;部门描述:&nbsp;<input name="description" id="org_description"
                                                             style="width: 300px;height: 130px"
                                                             data-options="required:false,multiline:true"
                                                             class="easyui-textbox easyui-validatebox">
            </p>
        </div>
        <div style="float: right;width:50%;height: 100%;">
            <table id="organization_save_right" class="easyui-treegrid easyui-validatebox"
                   data-options="
                url:'${pageContext.request.contextPath}/organization/list?isDisplaySuper=1&id='+common_tool.getCurrUserId(),
                method:'get',
                idField: 'id',
                treeField: 'name',
                border: true,
                rownumbers: true,
                fit: true,
                fitColumns: true,
                checkOnSelect:true,
                required:true,
            ">
                <thead frozen="true">
                <tr>
                    <th data-options="field:'ck', checkbox: true">选择</th>
                </tr>
                </thead>
                <thead>
                <tr>
                    <th data-options="field:'name',width:260">选择部门所属的上级部门</th>
                </tr>
                </thead>
            </table>
        </div>
    </form>
</div>
</div>
