organization_tool = {
    //重置表单
		
    form_clear: function () {
    	console.log("清除数据-formclear");
        $("#organization_form").form("clear");
        $("#organization").treegrid("uncheckAll");
        $("#organization_save_right").treegrid("uncheckAll");
    },
    //初始化数据
    init_data: function () {
        var data_json = '';
        $.ajax({
            method: 'get',
            url: getRootPath() + '/organization/list',
            async: false,
            dataType: 'json',
            success: function (result) {
            	console.log("请求到的json数据"+result);
                if (result.code == 10000) {
                    data_json = result.data;
                    
                }
                else {
                    common_tool.messager_show(result.msg);
                }
                
            },
            
        });
        return data_json;
    },
    //校验参数
    checkValidatebox: function () {
        if (!$('#organization_form input[id="name"]').validatebox('isValid')) {
            common_tool.messager_show('请输入名称');
         
            return false;
        } else if (!$('#organization_form input[id="fullName"]').validatebox('isValid')) {
            common_tool.messager_show('请输入全称');
            return false;
        } else if (!$('#organization_form input[id="org_description"]').validatebox('isValid')) {
            common_tool.messager_show('请输入描述');
            return false;
        } else if ($('#organization_form table[id="organization_save_right"]').treegrid("getChecked").length == 0) {
            common_tool.messager_show('请选择上级组织机构');
            return false;
        }
        return true;
    },
    //jquery 框架 填充数据 初始化页面+加载数据
    init_mian_view: function () {
    	
        $("#organization").treegrid({
            // data: organization_tool.init_data(),
        	
            url: getRootPath() + '/organization/list?id='+common_tool.getCurrUserId(),
            method: 'get',
            idField: "id",
            nodeId: 'id',
            treeField: 'name',
            fitColumns: true,
            fit: true,
            toolbar: '#tool-bar',
            rownumbers: true,
            animate: true,
            border: false,
            pagePosition: "bottom",
            pageNumber: 1,
            pageSize: 15,
            pageList: [15, 30, 45, 60],
            pagination: false,
            striped: true,
            columns: [[
                {title: "选择", field: "ck", checkbox: true, width: 20},
                {title: "简称", field: "name", width: 300},
                {title: "全称", field: "fullName", width: 300},
                {title: "说明", field: "description", width: 300},
                {
                    title: "是否可修改", field: "isFinal", formatter: function (value, row, index) {
                    if (value == 1) {
                        return "是";
                    }
                    if (value == 2) {
                        return "否";
                    }
                }, width: 70
                },
                {
                    title: "创建时间", field: "createTime", formatter: function (value, row, index) {
                    return common_tool.timestampToDateTime(value);
                }, width: 150
                },
                {
                    title: "更新时间", field: "updateTime", formatter: function (value, row, index) {
                    return common_tool.timestampToDateTime(value);
                }, width: 150
                },
            ]],
            //  双击节点时触发            
            onDblClickRow: function (index, row) {
                var organizationArray = $("#organization").treegrid('getChecked');
              
                $("#organization_form").form('load', {
                    "id": organizationArray[0].id,
                    "name": organizationArray[0].name,
                    "fullName": organizationArray[0].fullName,
                    "description": organizationArray[0].description,
                })
                $("#organization_save").dialog({
                    title: '机构详情',
                    iconCls: 'icon-save',
                    closable: true,
                    width: 900,
                    height: 550,
                    cache: false,
                    modal: true,
                    resizable: false,
                    'onOpen': function () {
                    	$("#organization_save_right").treegrid("reload");
                    	  console.log("organizationArray--string"+organizationArray[0].parentId);
                        $("#organization_save_right").treegrid('select', organizationArray[0].parentId);
                    },
                    'onClose': function () {
                        organization_tool.form_clear();
                    },
                    buttons: [
                    	 {
                             text: '取消',
                             width: 100,
                             iconCls: 'icon-add',
                             handler: function () {
                                 $("#organization_save").dialog('close');
                                 organization_tool.form_clear();
                             }
                         }
                    ],
                });
                $("#organization_save").dialog("center");
            },
        });
        console.log("treegrid列表在处理");
    },
    init_edit_view: function (data, parentId) {
    	var titleName="新增部门";
    	if(data==2){
    		titleName="修改部门信息";
    	}
        $("#organization_save").dialog({
            title: titleName,
            iconCls: 'icon-save',
            closable: true,
            width: 900,
            height:500,
            cache: false,
            modal: true,
            resizable: false,
            'onOpen': function () {
            	console.log("添加机构展开"+data+"parentId"+parentId);
            	$("#organization_save_right").treegrid("reload");
                if (parentId != null) {
                    $("#organization_save_right").treegrid('select', parentId);
                }
            },
            'onClose': function () {
                organization_tool.form_clear();
            },
            buttons: [
                {
                    text: '保存',
                    width: 100,
                    iconCls: 'icon-save',
                    handler: function () {
                        if (data == 1) {
                            organization_tool.save();
                        }
                        if (data == 2) {
                        	if(confirm("是否修改部门数据")){
                        		organization_tool.update();
                        	}
                        }
                    }
                },
                {
                    text: '清除',
                    width: 100,
                    iconCls: 'icon-reload',
                    handler: function () {
                        organization_tool.form_clear();
                    }
                },
                {
                    text: '取消',
                    width: 100,
                    iconCls: 'icon-add',
                    handler: function () {
                        $("#organization_save").dialog('close');
                        organization_tool.form_clear();
                    }
                }
            ],
        });
        $("#organization_save").dialog("center");
    },
    delete: function (id) {
        $.ajax({
            data: {
                id: id,
            },
            method: 'get',
            url: getRootPath() + '/organization/delete',
            async: false,
            dataType: 'json',
            success: function (result) {
                if (result.code == 10000) {
//                    organization_tool.init_mian_view();
                	  $("#organization").treegrid("reload");
                }
                else {
                    common_tool.messager_show(result.msg);
                }
            },
        });
    },
    save: function () {
        if (!organization_tool.checkValidatebox()) {
            return false;
        }
        var name = $('#organization_form input[id="name"]').val();
        var fullName = $('#organization_form input[id="fullName"]').val();
        var description = $('#organization_form input[id="org_description"]').val();
        var isFinal = 1;
        var parentId = $('#organization_form table[id="organization_save_right"]').treegrid("getChecked")[0].id;
        // TODO  新增 机构代码recoCode
//        var orgCode = $('#organization_form table[id="organization_save_right"]').treegrid("getChecked")[0].orgCode;
        var orgCode = $('#organization_form table[id="organization_save_right"]').treegrid("getChecked")[0].orgCode;
        console.log("机构代码"+orgCode);
        console.log("treegrid列表在保存");
        $.ajax({
            data: {
                name: name,
                fullName: fullName,
                description: description,
                isFinal: isFinal,
                parentId: parentId,
                parentOrgCode:orgCode,
            },
            method: 'post',
            url: getRootPath() + '/organization/insert',
            async: false,
            dataType: 'json',
            success: function (result) {
                if (result.code == 10000) {
                    $("#organization_save").dialog("close");
                    organization_tool.form_clear();
                    $("#organization").treegrid("reload");
//                    organization_tool.init_mian_view();
                }else {
                    common_tool.messager_show(result.msg);
                }
            },
        });
    },
    update: function () {
        if (!organization_tool.checkValidatebox()) {
            return false;
        }
        var id = $('#organization_form input[id="org_id"]').val();
        var name = $('#organization_form input[id="name"]').val();
        var fullName = $('#organization_form input[id="fullName"]').val();
        var description = $('#organization_form input[id="org_description"]').val();
        var parentId = $('#organization_form table[id="organization_save_right"]').treegrid("getChecked")[0].id;
        $.ajax({
            data: {
                id: id,
                name: name,
                fullName: fullName,
                description: description,
                parentId: parentId,
            },
            method: 'post',
            url: getRootPath() + '/organization/update',
            async: false,
            dataType: 'json',
            success: function (result) {
                if (result.code == 10000) {
                    $("#organization_save").dialog("close");
                    organization_tool.form_clear();
                    $("#organization").treegrid("reload");
//                    organization_tool.init_mian_view();
                }
                else {
                    common_tool.messager_show(result.msg);
                }
            },
        });
    }
};
$(document).ready(function () {
    organization_tool.init_mian_view();
    $(".save-btn").click(function () {
    	console.log("save-btn点击了");
        organization_tool.init_edit_view(1, null);
    });
    $(".update-btn").click(function () {
        var organizationArray = $("#organization").treegrid('getChecked');
        if (organizationArray.length == 0) {
            common_tool.messager_show("请选择一条记录");
            return false;
        }
        $("#organization_form").form('load', {
            "id": organizationArray[0].id,
            "name": organizationArray[0].name,
            "fullName": organizationArray[0].fullName,
            "description": organizationArray[0].description,
        })
        organization_tool.init_edit_view(2, organizationArray[0].parentId);
    });
    $(".delete-btn").click(function () {
        var organizationArray = $("#organization").treegrid('getChecked');
        if (organizationArray.length == 0) {
            common_tool.messager_show("请至少选择一条记录");
            return false;
        }
        $.messager.confirm('确认对话框', "您确认删除该条记录吗?", function (r) {
            if (r) {
                organization_tool.delete(organizationArray[0].id);
            }
        });
    });
    $(".select-btn").click(function () {
        organization_tool.form_clear();
//        organization_tool.init_mian_view();
        $("#organization").treegrid("reload");
    });
});

