/**
 * 我的任务
 */

(function() {
	var myTask=window.NameSpace||{};
	myTask.data={
		vTaskGroup:"",	
	};
	

	
	
	$(document).ready(function() {
		
	    /*$('#fb').filebox({
            buttonText: '发布任务',
            buttonAlign: 'right',
        })*/
        
//              $.ajaxSettings.async = false;
//        $.get("tasklist.html",function(html){
//			$("#div_task_container div[id=task_list]").append(html);
//			$("#div_task_container div[id=task_list]").html(html);
//		});
//    	$("#div_task_container div[id=task_list]").load(getRootPath()+"/tasklist.html");
//    	$("#div_task_container div[id=task_list]").load(getRootPath()+"/easyui-test.html");
    	$("#testtest").load(getRootPath()+"/easyui-test.html");
//        $.ajaxSettings.async = true;
//        $.parser.parse($("#div_task_container div[id=task_list]"));//重新渲染标签,解决按钮样式失效
		//  任务组加载详细子任务
//		$('#dg').datagrid({  
//			 method:"POST",
//		url:'http://localhost:8080/zicoo/task/TlistTaskGroup',
//		queryParams:{
// 	                		page:1,  
//							rows:15,
// 	                		userId:1,
// 	                	},
//		columns:[[
//			{field:'taskGroupName',title:'taskGroupName',width:100},
//			]]
//		});
		
	});
	
})();