<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/css/home/js/devManageJs.js"></script>
<style type="text/css"> 
	.deviceManage{
		padding: 20px 0px 20px 8px;
		font-size: 20px;
		 /* border-left: 5px solid #007ad5;  */
	}

</style>
</head>
<body>
	<div id="div_devmanage_container">
		<div style="float: left; width: 17%; height: 100%;border:0.5px solid #e6e6e6;display: inline-block;">
			<ul type="none">
				<li>
					<div class="device-item deviceManage" id="unregister">未绑定的设备</div>
					<div class="device-item deviceManage" id="register">已绑定的设备</div>
					
				</li>
			
			</ul>
		</div>
		<!-- border: 1px solid red;  -->
		<div style="float:right;width:82%;height: 100%;display: inline-block;" id="dev_list">
		
		</div>
	
	</div>
</body>
</html>