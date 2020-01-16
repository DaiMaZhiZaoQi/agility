<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/concatManage.js"></script>
<title>Insert title here</title>
<style type="text/css"> 
	.deviceManage{
		padding: 20px 0px 20px 8px;
		font-size: 1.2em;
	}
	
	.clearfix:before,.clearfix:after{
		display: table;
		content:"";
	}
	.clearfix:after{
		clear:both;
	}
	.clearfix{
		zoom:1;
	}
	
</style>
</head>
<body>
<div id="div_contactManage_container">
		<div style="float: left; width: 17%; height: 100%;border:0.5px solid #e6e6e6;display: inline-block;display: none;">
			<ul type="none">
				<li>
					<!-- <div class="device-item deviceManage" id="contactOptions">通讯录维护</div> -->
					<div class="device-item deviceManage" id="releaseEnterprise">发布企业通讯录</div>
					<!-- <div class="device-item deviceManage" id="importMember">导入客户会员库</div> -->
					
				</li>
			
			</ul>
		</div>
		<!-- border: 1px solid red;  -->
		<div style="float:right;width:100%;height: 100%;" id="concatOpt">
		
		</div>
		<div id="update_auth_dialog" style="display: none;">
		 <!-- 机构权限 -->
	        <div  style="float: left;width: 50%;height: 100%;">
	        	<table id="auth_user_orgs" class="easyui-treegrid"
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
		                    <th data-options="field:'name',width:260">部门权限全选</th>
		                </tr>
	                </thead>
	            </table>
	        </div>
	        
	         <!-- 角色权限  
	              <%-- url:'${pageContext.request.contextPath}/organization/listOrgUser',
                 <!-- view:groupview, -->
                method:'get', --%>
	         
	         	var len=rows.length;
                	console.log('长度'+len);
                	for(var i=0;i&lt;len;i++){
                		console.log(rows[i].groupOrgName);
                		if(rows[i].length!=0){
			               	 return rows[i].groupOrgName;
                		}
                	}
	         -->
        <div style="float: right; width:50%;height: 100%;">
            <table id="org_user" class="easyui-datagrid" data-options="
                idField:'id',
              	view:groupview,
                groupField:'groupOrgId',
                groupFormatter: function (value, rows) {
                	return rows[0].groupOrgName;
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
                    <th data-options="field:'zhName',width:200">个人权限全选</th>
                </tr>
                </thead>
            </table>
        </div>
        </div>
	
	</div>
	
	<!-- <div id="inscribe" 
			style="width:100%;background-color: #ffffff;display:inline-block;position:fixed;bottom: 0px;left: 0%;"  class="clearfix inscribe">
			<br style="margin-top: 20px;">
			<div style="margin-top: 20px;height: 75%;" class="clearfix">&nbsp;&nbsp;&nbsp;</div>
			<div align="center" class="clearfix"><a href="http://ir2.co/" class="clearfix" target="view">Copyright © 2020 铱方科技（深圳）有限公司&nbsp;版权所有</a></div>
			<div></div>
			
			<div align="center" class="clearfix">Copyright © 2020 铱方科技（深圳）有限公司&nbsp;版权所有</div>
				    <br>
			<div align="center">技术支持:&nbsp;&nbsp;天津市慧然网络科技有限公司</div>
			<div align="center"><a href="http://www.tjhrwl.com/" target="view">技术支持:&nbsp;&nbsp;天津市慧然网络科技有限公司</a></div>
				    <br>
			<div align="center">联系电话:&nbsp;&nbsp;13662125100&nbsp;&nbsp;邮箱地址：153465518@qq.com</div>
				    
		</div> -->
</body>
</html>