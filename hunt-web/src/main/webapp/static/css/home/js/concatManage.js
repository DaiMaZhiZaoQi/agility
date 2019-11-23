/**
 * 联系人通讯录管理 
 */

(function(){
	
	var concatSpace=window.NameSpace||{};
	concatSpace.data={};
	
	concatOpt={
			/**
			 * 通讯录维护
			 */
			concatMaintail:function(){
				
			},
			/**
			 * 发布企业通讯录
			 */
			releaseConcat:function(){
				
			},
			
			/**
			 * 导入客户会员库
			 */
			importConcat:function(){
				
			},
	};
	
	$(document).ready(function(){	
		$("#div_contactManage_container div[id=releaseEnterprise]").click(function(){
			$("#div_contactManage_container div[id=concatOpt]").children().remove();
			$.get("contactMainTain.html",function(html){
				$("#div_contactManage_container div[id=concatOpt]").append(html);
			});
		});
		$("#div_contactManage_container div[id=releaseEnterprise]").click();
		
	});
	
})();



