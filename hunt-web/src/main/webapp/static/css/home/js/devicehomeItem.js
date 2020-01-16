/**
 * 
 */

(function(){
	

	var NameSpace = window.NameSpace || {};
	NameSpace.data = {
			/* 加载完成的数据 */
			 globalData:"",
			/* 全局deviceId */
			globalDeviceId:"",
			/* 当前页码 */
			currentPage:1,
			/* 总页码 */
			totalPage:1,
			/* 数据总量  */
			totalCount:"",
			/* 全局排序 */
			globalSort:"",
			/* 全局排序方式 */
			globalOrder:"",
			set_globalData:function(obj){this.globalData=obj;},
			get_globalData:function(obj){return this.globalData[obj];},
			
			set_globalDeviceId:function(obj){this.globalDeviceId=obj;},
			get_globalDeviceId:function(){return this.globalDeviceId;},
			
			add_currentPage:function(obj){this.currentPage++},
			sub_currentPage:function(obj){this.currentPage--},
			get_currentPage:function(){return this.currentPage; },
			
			set_totalPage:function(obj){this.totalPage=obj;},
			get_totalPage:function(){return this.totalPage;},
			
			set_totalCount:function(obj){this.totalCount=obj;},
			get_totalCount:function(){return this.totalCount; },
			
			set_globalSort:function(obj){this.globalSort=obj;},
			get_globalSort:function(){return this.globalSort;},
			
			set_globalOrder:function(obj){this.globalOrder=obj;},
			get_globalOrder:function(){return this.globalOrder;},
	};
	
	deviceCallLog={
			remark:function(callLogId){
					var str; //score变量，用来存储用户输入的成绩值。
					var textDescription=$("#description"+callLogId).text();
					str =prompt("修改备注信息",textDescription);
					if(str==null)return;
					var data={id:callLogId,callDescription:str};
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
			
			deleteLogAndRecord:function(callLogId,obj,i){
				if(confirm("是否删除该条通话记录及录音")){
					var objData=NameSpace.data.get_globalData(i);
					objData.status=2;
					var json=JSON.stringify(objData);
					console.log("deleteLogAndRecord-->"+json+"--i-->"+i);
					$.ajax({
						data:json,
						type:"POST",
						dataType:"json",
						url:getRootPath()+"/deviceCallLog/delete",
						traditional:true,
						contentType : 'application/json',
						success:function(data){
							if(data.code==10000){
								alert("操作成功");
								$(obj).parents("tr").remove();
							}else{
								alert(data.msg);
							}
						}
					})
					
				}else{
					
				}
			},
			
		fillData:function(data,deviceId){
			NameSpace.data.set_globalData(data.rows);
		/* 	$.each(data.rows,function(i, v){
				console.log("当前数据下标"+i+"--->"+v.callAddress);
				$("#deviceCallLog tbody tr").attr("index",i).html('<th>'+v.callAddress+'</th>');
	//			$("<tr>").attr("index",i).html('<td>'+v.callAddress+'</td>')..insertBefore("tr:last");
	
		
			}); */
			
			function getRow(row,i) {
				if(i%2==0){
				    var tr = $('<tr style="background-color: #ffffff;"></tr>');
				}else{
				    var tr = $('<tr style="background-color: #f3f3f3;"></tr>');
				}
			    var type=deviceCallLog.callType(row.callType);
			    var callLogId=row.id;
			    var callNumber=row.callNumber==""?"无":row.callNumber;
			    
			    var description=row.callDescription=(row.callDescription==null||row.callDescription=="")?"无":row.callDescription;
			    var callHasRecord=row.callHasRecord==-1?"无":"下载录音";
			    var callDate=deviceCallLog.getMoth(row.callDate);
			    var callDuration=row.callDuration==0?"无通话时间":row.callDuration;
			    var secondIsValid=deviceCallLog.secondIsValid(callDuration);
			    var callDuration=common_tool.secondToMinute(secondIsValid==true?(callDuration/1000):callDuration);
			    
			    var recoAudioLength=row.sysDeviceRecord==null?0:row.sysDeviceRecord.recoAudioLength;
				//var recoIsValid=deviceCallLog.secondIsValid(recoAudioLength);		    
				var recoAudioLength=common_tool.secondToMinute(Math.floor(recoAudioLength/1000));
				
			    tr.append('<td>'+type+'</td>');
			    tr.append('<td>'+callNumber+'</td>');
			    
			    tr.append('<td>'+callDuration+'</td>');
			    tr.append('<td>'+recoAudioLength+'</td>');
			 
			    tr.append('<td>'+callDate+'</td>');
			    tr.append('<td id=description'+callLogId+'>'+description+'</td>');
			    
			    var recoFilePath;
	  			if(callHasRecord=="下载录音"){
			    	if(row.sysDeviceRecord!=null){			//  可能会有多条录音，要求前端合成音频后上传。href='+recoFilePath+';  download='+recoFilePath+';   注意这个经典写法  <a onclick="deviceCallLog.playAudio('+fileType+');"><img src="static/image/play.png;" width="25px;" height="25px;" /></a>
			    		//recordId=row.sysDeviceRecord[0].id;
			    		recoFilePath=getRootPath()+row.sysDeviceRecord.recoFilePath;
			    		var record=getRootPath()+"/deviceRecord/audio?callLogId="+callLogId;
				    	tr.append('<td style="vertical-align: middle;"><audio style="width:260px;height: 32px;" id="audioPlay;"src='+record+' controls="controls">Your browser does not support the audio element.</audio></th>');
			    	}else{
			    		tr.append('<td>'+"无录音文件"+'</td>');
			    	}
			    }else{
				    tr.append('<td>'+callHasRecord+'</td>');
			    }
			    
			    tr.append('<td style="vertical-align: bottom; margin-top:30px;">'+
			    		'<img src="static/image/remark.png;" width="25px;" height="25px;" onclick="deviceCallLog.remark('+callLogId+');"/>'+
			    		'<img src="static/image/delete.png;" width="25px;" height="25px;" onclick="deviceCallLog.deleteLogAndRecord('+callLogId+',this,'+i+')"/></td>');
			  /*  
			  //'<a style="margin-left:7px;margin-right:5px;" href='+recoFilePath+'; download='+recoFilePath+';><img src="static/image/download.png;" width="25px;" height="25px;"/></a> '+
			  for(var i in row) {
			    	if(i=="callInPhone"){
			        	tr.append('<th>' + row[i] + '</th>');
			    	}else{
			    		tr.append('<th></th>');	
			    	}
			    	console.log("---->"+i+"--地址"+ row[i]);
			    } */
			    return tr;
			}
			var tbody = $('<tbody></tbody>');
			
			for(var i = 0; data.rows!=null&&i < data.rows.length; i ++ ){ 
			    tbody.append(getRow(data.rows[i],i));
			}
			
			
			$('#deviceCallLog'+deviceId+' tbody').replaceWith(tbody);
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
	    
	
	    
	    /**验证是否为毫秒数，毫秒数转换为秒数*/
	    secondIsValid:function(second){
	    	var regex=/^\d{13}$/;
	    	return regex.test(second);
	    	
	    },
	    
	 
	    clickChangeData:function(obj,sort){
	    	var path=$(obj).children("img").attr("src");
	    		NameSpace.data.set_globalSort(sort);
	    	if(path=="static/css/home/image/arrow_up.png"){
	    		$(obj).children("img").attr("src","static/css/home/image/arrow_down.png");
	    		NameSpace.data.set_globalOrder("desc");
	    		deviceCallLog.loadData(NameSpace.data.get_globalDeviceId(),sort,"desc",-1,1);
	    	}else{
	    		NameSpace.data.set_globalOrder("asc");
	    		deviceCallLog.loadData(NameSpace.data.get_globalDeviceId(),sort,"asc",-1,1);
	    		$(obj).children("img").attr("src","static/css/home/image/arrow_up.png");
	    	}
	    	
	    },
	   
	    /**加载数据 
	    * options 1,普通加载，排序，2，下一页 ，3. 上一页，4.跳转,5,修改每页加载数量 默认不处理 6,首页，7，尾页
	    */
	    loadData:function(deviceId,sort,order,definePage,options){
	   
	    	var selectEdRows=$("#idPageTool"+deviceId+" select[id=iEveryCount] option:selected").val();		//  每页加载条数
	    	if(options==2){
	    		NameSpace.data.add_currentPage();
	    		if(NameSpace.data.get_currentPage()>NameSpace.data.get_totalPage()){
		    		NameSpace.data.set_currentPage(NameSpace.data.get_totalPage());
		    		return;
	    		}
	    	}else if(options==3){
	    		NameSpace.data.sub_currentPage();
	    		if(NameSpace.data.get_currentPage()<1){
	    			NameSpace.data.set_currentPage(1);
	    			return;
	    		}
	    	}else if(options==4){
	    		if(NameSpace.data.get_totalPage()<definePage){
	    			NameSpace.data.set_currentPage(NameSpace.data.get_totalPage());
	    		}else if(definePage<1){
	    			NameSpace.data.set_currentPage(1);
	    		}else{
		    		NameSpace.data.set_currentPage(definePage);
	    		}
	    	}else if(options==6){
	    		NameSpace.data.set_currentPage(1);
	    	}else if(options==7){
	    		NameSpace.data.set_currentPage(NameSpace.data.get_totalPage());
	    	}else if(options==1){	
	    		NameSpace.data.set_currentPage(1);
	    	}
	    	$.ajax({
				type:"GET",
				url:getRootPath()+"/deviceCallLog/list?deviceId="+deviceId+"&sort="+sort+"&order="+order+"&page="+NameSpace.data.get_currentPage()+"&rows="+selectEdRows,
				success:function(data){
					NameSpace.data.set_globalDeviceId(deviceId);
					deviceCallLog.fillData(data,deviceId);
					$("#audioPlay").volume=0.5;
					$("#idPageTool"+deviceId+" span[id=currentPage]").text("当前"+(NameSpace.data.get_currentPage()<NameSpace.data.get_totalPage()?NameSpace.data.get_currentPage():NameSpace.data.get_totalPage())+"/"+NameSpace.data.get_totalPage()+"页");
				},
			})
	    },
	    /**下一页*/
	    nextPage:function(){
	    	
	    	alert("设备id-->"+NameSpace.data.get_globalDeviceId());
	    	this.deviceCallLog.loadData(NameSpace.data.get_globalDeviceId(),NameSpace.data.get_globalSort()==null?"call_date":NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder()==null?"desc":NameSpace.data.get_globalOrder(),-1,2);
	    },
	    /**上一页*/
	    prePage:function(){
	    	this.deviceCallLog.loadData(NameSpace.data.get_globalDeviceId(),NameSpace.data.get_globalSort()==null?"call_date":NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder()==null?"desc":NameSpace.data.get_globalOrder(),-1,3);
	    },
	    /**修改每一页数量*/
	    changePages:function(){
	    	var rows=$("#idPageTool"+NameSpace.data.get_globalDeviceId()+" select[id=iEveryCount] option:selected").val();
	    	var page=(NameSpace.data.get_totalCount()%rows)==0?(NameSpace.data.get_totalCount()/rows):(Math.floor((NameSpace.data.get_totalCount()/rows))+1);
			NameSpace.data.set_totalPage(page);
	    	deviceCallLog.loadData(NameSpace.data.get_globalDeviceId(),NameSpace.data.get_globalSort()==null?"call_date":NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder()==null?"desc":NameSpace.data.get_globalOrder(),-1,5);
	    },
	    
	    /**跳转*/
	    jumpPage:function(){
	    	var page=$("#idPageTool"+NameSpace.data.get_globalDeviceId()+" input[type=text]").val();
	    	deviceCallLog.loadData(NameSpace.data.get_globalDeviceId(),NameSpace.data.get_globalSort()==null?"call_date":NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder()==null?"desc":NameSpace.data.get_globalOrder(),page,4);
	    },
	    
	    firstPage:function(){
	    	deviceCallLog.loadData(NameSpace.data.get_globalDeviceId(),NameSpace.data.get_globalSort()==null?"call_date":NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder()==null?"desc":NameSpace.data.get_globalOrder(),-1,6);
	    },
	    
	    endPage:function(){
	    	deviceCallLog.loadData(NameSpace.data.get_globalDeviceId(),NameSpace.data.get_globalSort()==null?"call_date":NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder()==null?"desc":NameSpace.data.get_globalOrder(),-1,7);
	    }
	    
	},
	
	
		$(document).ready(function(){
			// 判断选择的哪个模块
			
			var deviceId=$('#div_alldevice_head input[id="deviceId"]').val();
			alert("deviceId-->"+deviceId);
			$(".ul_device_list div[id="+deviceId+"]").css("padding-bottom","0px")
			$("#deviceCallLog").attr("id","deviceCallLog"+deviceId);
			$("#pageTool").attr("id","idPageTool"+deviceId);
			$.ajax({   
				type:"GET",   
				url:getRootPath()+"/deviceCallLog/list?deviceId="+deviceId+"&sort=call_date&order=desc"+"&page="+1+"&rows="+3,
				success:function(data){
					NameSpace.data.set_globalDeviceId(deviceId);
					$("#idPageTool"+deviceId+" span[id=totalPage]").text("共"+data.total+"条");
					var selectEdRows=$("#idPageTool"+deviceId+" select[id=iEveryCount] option:selected").val();
					 NameSpace.data.set_totalCount(data.total);
					var page=(NameSpace.data.get_totalCount()%selectEdRows)==0?(NameSpace.data.get_totalCount()/selectEdRows):(Math.floor((NameSpace.data.get_totalCount()/selectEdRows))+1);
					NameSpace.data.set_totalPage(page);
					$("#idPageTool"+deviceId+" span[id=currentPage]").text("当前"+NameSpace.data.get_currentPage()+"/"+page+"页");
					deviceCallLog.fillData(data,deviceId);
					$("#audioPlay").volume=0.5;
				},
			})
			
		});
})();