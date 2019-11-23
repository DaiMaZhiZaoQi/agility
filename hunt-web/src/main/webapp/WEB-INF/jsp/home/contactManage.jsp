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
		<div style="float: left; width: 17%; height: 100%;border:0.5px solid #e6e6e6;display: inline-block;">
			<ul type="none">
				<li>
					<!-- <div class="device-item deviceManage" id="contactOptions">通讯录维护</div> -->
					<div class="device-item deviceManage" id="releaseEnterprise">发布企业通讯录</div>
					<!-- <div class="device-item deviceManage" id="importMember">导入客户会员库</div> -->
					
				</li>
			
			</ul>
		</div>
		<!-- border: 1px solid red;  -->
		<div style="float:right;width:82%;height: 100%;" id="concatOpt">
		
		</div>
	
	</div>
</body>
</html>