<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/system/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/system/job.js"></script>

<div id="job-tool-bar" style="padding: 10px">
    <div class="easyui-linkbutton  " id="job-select-btn" data-options="iconCls:'icon-reload'" style="width:70px">刷新
    </div>
    <div class="easyui-linkbutton " id="job-save-btn" data-options="iconCls:'icon-add'" style="width:70px">新增</div>
    <div class="easyui-linkbutton " id="job-update-btn" data-options="iconCls:'icon-edit'" style="width:70px">修改</div>
    <div class="easyui-linkbutton " id="job-delete-btn" data-options="iconCls:'icon-remove'" style="width:70px">删除</div>
</div>
<div id="job_grid" style="padding: 10px">

</div>

<div id="job_dialog">
    <form id="job_form">
        <input type="hidden" name="id" id="job_id">
        <div style="float: left;height: 250px;width: 200px;">
            <p style="padding: 10px;">&nbsp;&nbsp;职位简称:&nbsp;<input name="name" id="job_name" style="width: 300px;height: 35px"
                                                            data-options="required:true"
                                                            class="easyui-textbox easyui-validatebox"></p>
            <p style="padding: 10px ;">&nbsp;&nbsp;职位全称:&nbsp;<input name="fullName" id="job_fullName"
                                                             style="width: 300px;height: 35px"
                                                             data-options="required:true"
                                                             class="easyui-textbox easyui-validatebox"></p>
            <p style="padding: 10px;"> &nbsp;&nbsp;职位描述:&nbsp;<input name="description" id="job_description"
                                                             style="width: 300px;height: 130px"
                                                             data-options="multiline:true"
                                                             class="easyui-textbox easyui-validatebox">
            </p>
        </div>
        <div style="float: right;width: 360px;;height: 100%;">
            <table id="job_dialog_parent_tree" class="easyui-treegrid"
                   data-options="
                url:'${pageContext.request.contextPath}/job/list',
                method:'get',
                idField: 'id',
                nodeId:'id',
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
                    <th data-options="field:'name',width:260">上级职位</th>
                </tr>
                </thead>
            </table>
        </div>
        <div style="float: right;width: 220px;height: 100%">
            <table id="job_dialog_role" class="easyui-datagrid"
                   data-options="
                url:'${pageContext.request.contextPath}/role/list',
                method:'get',
                idField: 'id',
                nodeId:'id',
                treeField: 'name',
                border: true,
                rownumbers: true,
                fit: true,
                fitColumns: true,
                checkOnSelect:true,
                singleSelect:true,
                required:true,
            ">
                <thead frozen="true">
                <tr>
                    <th data-options="field:'ck', checkbox: true">选择</th>
                </tr>
                </thead>
                <thead>
                <tr>
                    <th data-options="field:'name',width:260">系统角色</th>
                </tr>
                </thead>
            </table>
        </div>
        <div style="float: right;width: 370px;height: 100%">
            <table id="job_dialog_organization" class="easyui-treegrid"
                   data-options="
                url:'${pageContext.request.contextPath}/organization/list',
                method:'get',
                idField: 'id',
                nodeId:'id',
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
                    <th data-options="field:'name',width:260">组织机构</th>
                </tr>
                </thead>
            </table>
        </div>
    </form>
</div>
</div>
