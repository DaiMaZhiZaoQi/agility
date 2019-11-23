/**
 * 
 */
(function(){  
	var devManageSpace=window.NameSpace||{};
	devManageSpace.data={
			
	};
	devCommon={
			
	};
	
	
	
	$(document).ready(function(){
		
		$("#div_devmanage_container div[id=unregister]").click(function(){
			//   加載沒有绑定的设备  路由控制权掌握在前端手中   
//			alert("未绑定");
			$("#div_devmanage_container div[id=dev_list]").children().remove();
			$("#alDevice_manage_dialog").parent().remove();
			var divContainer=$("<div id=devUnRegister></div>");
			var divItem=$("<div class=device-item> </div>");		// 这个人的所有通话记录
			$("#div_devmanage_container div[id=dev_list]").append(divContainer);
			divContainer.append(divItem);
			$("#div_devmanage_container div[id=register]").css("border-left","5px solid #fafafa");
			$("#div_devmanage_container div[id=unregister]").css("border-left","5px solid #007ad5");
			$.get("noRegister.html",function(datahtml){
				divItem.append(datahtml);
			});
		});
		$("#div_devmanage_container div[id=register]").click(function(){
			//  加载已绑定的设备
//			alert("已绑定");
			$("#div_devmanage_container div[id=dev_list]").children().remove();
			$("#device_manage_dialog").parent().remove();
			var divContainer=$("<div id=devUnRegister></div>");
			var divItem=$("<div class=device-item> </div>");		// 这个人的所有通话记录
			$("#div_devmanage_container div[id=dev_list]").append(divContainer);
			divContainer.append(divItem);
			$("#div_devmanage_container div[id=register]").css("border-left","5px solid #007ad5");
			$("#div_devmanage_container div[id=unregister]").css("border-left","5px solid #fafafa");
			$.get("compRegister.html",function(datahtml){
				divItem.append(datahtml);
			});
		});
		$("#div_devmanage_container div[id=unregister]").click();
	});
	
	
	
})();