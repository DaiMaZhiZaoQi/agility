/**
 *	通话记录javaScript 
 */

deviceCallLog={
		remark:function(callLogId){
				var str; //score变量，用来存储用户输入的成绩值。
				var textDescription=$("#description"+callLogId).text();
				str =prompt("修改备注信息",textDescription);
				if(str==null)return;
				var data={id:callLogId,description:str};
				$.ajax({
					type:"POST",
					data:data,
					dataType:"json",
					url:getRootPath()+"/deviceCallLog/update",
					success:function(data){			//    修改成功，刷新备注信息
						if(data.code==10000){
							$("#description"+callLogId).text(str);
						}else{
							alert(data.msg);
						}
					},
					error:function(XMLHttpRequest,textStatus,errThrown){
						alert("请求错误，请检查参数");
					}
				});
		},
		
		deleteLogAndRecord:function(id,deviceId,callType,callDuration,index,status,createTime){
			if(confirm("是否删除该条通话记录及录音")){
				var data={id:id,deviceId:deviceId,callType:callType,callDuration:callDuration,status:status,createTime:createTime};
				data=JSON.stringify(data);
				console.log("deleteLogAndRecord-->"+data);
				$.ajax({
					data:data,
					type:"POST",
					dataType:"json",
					url:getRootPath()+"/deviceCallLog/delete",
					traditional:true,
					contentType : 'application/json',
					success:function(data){
						if(data.code==10000){
//							alert("操作成功");
							$("#deviceCallLog tr[id=tr"+index+"]").remove();
						}else{
							alert(data.msg);
						}
					}
				})
				
			}else{
				
			}
		},
		
	
		
	fillData:function(data,deviceId){
//		$("#deviceCallLog tbody"+deviceId).lo
	},
	
	
	
	callType:function(type){
		switch (type) {
		case 0:
			return "未接电话";
		case 1:
			return "呼入";
		case 2:
			return "呼出";
		case 3:
			return "拒接";
		case 4:
			return "未接留言";
		default:
			return "未知";
		}
	},
	
	
	/**毫秒数转时间*/
	getMoth:function(str){  
		var dateTime=new Date(str);
		var year=dateTime.getFullYear();
		var month=dateTime.getMonth() + 1;
		var day=dateTime.getDate();
		var hour=dateTime.getHours();
		var minute=dateTime.getMinutes();
		var second=dateTime.getSeconds(); 
		return year+'-'+month+'-'+day+' '+hour+':'+minute+':'+second;
    },  
    
	/**秒数转分钟*/    
    secondToMinute:function(callDuration){
    	if(callDuration<60){
    		return "00:"+(callDuration<10?("0"+callDuration):(callDuration));
    	}else{
    		if(callDuration>=60&&callDuration<=3600){
    			var minute=callDuration/60;
    			var second=callDuration%60;
    			var mi=Math.floor(minute);
    			var sec=Math.floor(second);
    			return (mi<10?("0"+mi):mi)+":"+(sec<10?("0"+sec):sec);
    		}else{
    			var hour=callDuration/3600;
    			var m=callDuration%3600;
    			var minute=deviceCallLog.secondToMinute(m);
    			return Math.floor(hour)+":"+minute;
    		}
    	}
    },
    
    /**验证是否为毫秒数，毫秒数转换为秒数*/
    secondIsValid:function(second){
    	var regex=/^\d{13}$/;
    	return regex.test(second);
    	
    },
    
 
    clickChangeData:function(obj,sort,deviceId){
    	if(deviceId==0)return;
    	var path=$(obj).children("img").attr("src");
    	if(path=="static/css/home/image/arrow_up.png"){
    		$(obj).children("img").attr("src","static/css/home/image/arrow_down.png");
    		deviceCallLog.loadData(deviceId,sort,"desc");
    	}else{
    		deviceCallLog.loadData(deviceId,sort,"asc");
    		$(obj).children("img").attr("src","static/css/home/image/arrow_up.png");
    	}
    	
    },
   
    loadData:function(deviceId,sort,order){
    	$.ajax({
			type:"GET",
			url:getRootPath()+"/deviceCallLog/list?deviceId="+deviceId+"&sort="+sort+"&order="+order,
			success:function(data){
				globalDeviceId=deviceId;
				deviceCallLog.fillData(data,deviceId);
				$("#audioPlay").volume=0.5;
			},
		})
    }
    
    
    
   

}