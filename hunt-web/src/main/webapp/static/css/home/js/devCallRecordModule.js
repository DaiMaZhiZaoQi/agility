/**
 * 查询通话记录
 */
(function() {
//	//我们强烈推荐你在代码最外层把需要用到的模块先加载
//	layui.use(['layer', 'form', 'element'], function(){
//	  var layer = layui.layer
//	  ,form = layui.form
//	  ,element = layui.element
//	 
//	  //……
//	  //你的代码都应该写在这里面
//	});
	var NameSpace = window.NameSpace || {};
	NameSpace.data = {
			/* 加载完成的数据 */
			 globalData:"",
			/* 全局deviceId */
//			globalDeviceId:"",
			
			/*机构id或用户id*/
			orgIdUserId:"",
			 
			/* 当前页码 */
			currentPage:1,
			/* 总页码 */
			totalPage:1,
			/* 数据总量  */
			totalCount:"",
			/* 全局排序 */
			globalSort:"call_date",
			/* 全局排序方式 */
			globalOrder:"desc",
			/*操作类型*/
			optType:"",
			
			sTimeType:"2",
	    	sContent:"",
	    	/**开始时间*/
	    	beginTime:"0",
	    	
	    	/**结束时间*/
	    	endTime:"0",
	    	
	    	callIsHaveRecord:0,
	    	
	    	set_sContent:function(obj){this.sContent=obj},
	    	get_sContent:function(){return this.sContent},
	    	
	    	set_sTimeType:function(obj){this.sTimeType=obj},
	    	get_sTimeType:function(obj){return this.sTimeType},
	    	
	    	set_beginTime:function(obj){this.beginTime=obj},
	    	get_beginTime:function(){return this.beginTime},
	    	
			set_globalData:function(obj){this.globalData=obj;},
			get_globalDataAll:function(){return this.globalData;},
			get_globalData:function(obj){return this.globalData[obj];},
			
//			set_globalDeviceId:function(obj){this.globalDeviceId=obj;},
//			get_globalDeviceId:function(){return this.globalDeviceId;},
			set_orgIdUserId:function(obj){this.orgIdUserId=obj},
			get_orgIdUserId:function(){return this.orgIdUserId},
			
			add_currentPage:function(obj){this.currentPage++},
			sub_currentPage:function(obj){this.currentPage--},
			get_currentPage:function(){return this.currentPage; },
			set_currentPage:function(obj){this.currentPage=obj},
			
			set_totalPage:function(obj){this.totalPage=obj;},
			get_totalPage:function(){return this.totalPage;},
			
			set_totalCount:function(obj){this.totalCount=obj;},
			get_totalCount:function(){return this.totalCount; },
			
			set_globalSort:function(obj){this.globalSort=obj;},
			get_globalSort:function(){return this.globalSort;},
			
			set_globalOrder:function(obj){this.globalOrder=obj;},
			get_globalOrder:function(){return this.globalOrder;},
			
			set_optType:function(obj){this.optType=obj},
			get_optType:function(){return this.optType},
			
			
	};

recordCommon={
		testFunTest:function(){
			alert("方法调用成功");
		},
		/**毫秒数转时间*/
		getMoth:function(str){  
			var dateTime=new Date(str);
			var year=dateTime.getFullYear();
			var month=dateTime.getMonth() + 1;
			month=month<10?("0"+month):month;
			var day=dateTime.getDate();
			day=day<10?("0"+day):day;
			var hour=dateTime.getHours();
			hour=hour<10?("0"+hour):hour;
			var minute=dateTime.getMinutes();
			minute=minute<10?("0"+minute):minute;
			var second=dateTime.getSeconds(); 
			second=second<10?("0"+second):second;
			return year+'-'+month+'-'+day+' '+hour+':'+minute+':'+second;
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
	    /**验证是否为毫秒数，毫秒数转换为秒数*/
	    secondIsValid:function(second){
	    	var regex=/^\d{13}$/;
	    	return regex.test(second);
	    	
	    },
		/**秒数转分钟*/    
	    secondToMinute:function(callDuration){
	    	
	    	if(callDuration<60){
	    		return "00:"+(callDuration<10?("0"+callDuration):(callDuration));
	    	}else{
	    		if(callDuration>60&&callDuration<3600){
	    			var minute=callDuration/60;
	    			var second=callDuration%60;
	    			var mi=Math.floor(minute);
	    			var sec=Math.floor(second);
	    			return (mi<10?("0"+mi):mi)+":"+(sec<10?("0"+sec):sec);
	    		}else{
	    			var hour=callDuration/3600;
	    			var m=callDuration%3600;
	    			var minute=recordCommon.secondToMinute(m);
	    			return Math.floor(hour)+":"+minute;
	    		}
	    	}
	    },
	    /* sor : call_duration, reco_audio_length，call_date*/
	    clickChangeData:function(obj,sort){
	    	var path=$(obj).children("img").attr("src");
	    		NameSpace.data.set_globalSort(sort);
	    	if(path=="static/css/home/image/arrow_up.png"){
	    		$(obj).children("img").attr("src","static/css/home/image/arrow_down.png");
	    		NameSpace.data.set_globalOrder("desc");
//	    		deviceCallLog.loadData(NameSpace.data.get_globalDeviceId(),sort,"desc",-1,1);
	    		recordModult.refreshCallLog(NameSpace.data.get_optType(),NameSpace.data.get_orgIdUserId(),NameSpace.data.get_globalSort(),"desc",1,-1);
	    	}else{
	    		NameSpace.data.set_globalOrder("asc");
//	    		deviceCallLog.loadData(NameSpace.data.get_globalDeviceId(),sort,"asc",-1,1);
	    		recordModult.refreshCallLog(NameSpace.data.get_optType(),NameSpace.data.get_orgIdUserId(),NameSpace.data.get_globalSort(),"asc",1,-1);
	    		$(obj).children("img").attr("src","static/css/home/image/arrow_up.png");
	    	}
	    	
	    },
	    
	    
	    /**下一页*/
	    nextPage:function(){
	    	
	    	/* refreshCallLog:function(optType,id,sort,order,options,definePage)   */
	    	recordModult.refreshCallLog(NameSpace.data.get_optType(),NameSpace.data.get_orgIdUserId(),NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder(),2,-1);
	    },
	    
	    /**当前页*/
	    currPage:function(){
	    	recordModult.refreshCallLog(NameSpace.data.get_optType(),NameSpace.data.get_orgIdUserId(),NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder(),8,-1);
	    },
	    
	    /**上一页*/
	    prePage:function(){
//	    	this.deviceCallLog.loadData(NameSpace.data.get_globalDeviceId(),NameSpace.data.get_globalSort()==null?"call_date":NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder()==null?"desc":NameSpace.data.get_globalOrder(),-1,3);
	    	recordModult.refreshCallLog(NameSpace.data.get_optType(),NameSpace.data.get_orgIdUserId(),NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder(),3,-1);
	    },
	    /**修改每一页数量*/
	    changePages:function(){
	    	var rows=$("#pageTool select[id=iEveryCount] option:selected").val();
	    	var page=(NameSpace.data.get_totalCount()%rows)==0?(NameSpace.data.get_totalCount()/rows):(Math.floor((NameSpace.data.get_totalCount()/rows))+1);
			NameSpace.data.set_totalPage(page);
//	    	deviceCallLog.loadData(NameSpace.data.get_globalDeviceId(),NameSpace.data.get_globalSort()==null?"call_date":NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder()==null?"desc":NameSpace.data.get_globalOrder(),-1,5);
	    	recordModult.refreshCallLog(NameSpace.data.get_optType(),NameSpace.data.get_orgIdUserId(),NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder(),5,-1);
	    },
	    
	    /**跳转*/
	    jumpPage:function(){
	    	var page=$("#pageTool input[type=text]").val();
//	    	deviceCallLog.loadData(NameSpace.data.get_globalDeviceId(),NameSpace.data.get_globalSort()==null?"call_date":NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder()==null?"desc":NameSpace.data.get_globalOrder(),page,4);
	    	recordModult.refreshCallLog(NameSpace.data.get_optType(),NameSpace.data.get_orgIdUserId(),NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder(),4,page);
	    },
	    /**首页*/
	    firstPage:function(){
//	    	deviceCallLog.loadData(NameSpace.data.get_globalDeviceId(),NameSpace.data.get_globalSort()==null?"call_date":NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder()==null?"desc":NameSpace.data.get_globalOrder(),-1,6);
	    	recordModult.refreshCallLog(NameSpace.data.get_optType(),NameSpace.data.get_orgIdUserId(),NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder(),6,-1);
	    },
	    /**尾页*/
	    endPage:function(){
	    	recordModult.refreshCallLog(NameSpace.data.get_optType(),NameSpace.data.get_orgIdUserId(),NameSpace.data.get_globalSort(),NameSpace.data.get_globalOrder(),7,-1);
	    },
	 
	    /**
		 *	搜索时间段 
		 *sTimeType
		 */
		selectCallDate:function(){
			var sContent=$("#tableCallLogSear input[id=input_search]").val();
			var optType=NameSpace.data.get_optType();
			var optIdUserId=NameSpace.data.get_orgIdUserId();
			NameSpace.data.set_sContent(undefined!=sContent?sContent:"");
//			NameSpace.data.set_sTimeType(undefined!=sTimeType?sTimeType:"");
			$.ajaxSettings.async = false;
			recordModult.refreshPage(optType,optIdUserId);	
			$.ajaxSettings.async = false;
			recordModult.refreshTotal(optType,optIdUserId);
			$.ajaxSettings.async = false;
			recordModult.refreshCallLog(optType,optIdUserId,"call_date","DESC",1,-1);
		}
},
recordModult={
		test:function (h){
	            document.getElementById("test").style.height = h+"px"
	        },
		   // 设置iframe高度
	    setIframeHeight:function(iframe,orgId,optionType) {
	    	if (iframe) {
//		    	var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;	网页为静态时可以调用此方法动态改变页面高度
//		    	if (iframeWin.document.body) {
//		    		iframe.height = iframeWin.document.documentElement.scrollHeight || iframeWin.document.body.scrollHeight;
//		    	}
		    	
//		    	   if (iframe.attachEvent){//IE
//		               iframe.attachEvent("onload", function(){
//		                   //调整高度
//		                      iframe.height = $(iframe).contents().find("body").height() + 50;
//		               });
//		           }else{//其他浏览器
//		               iframe.onload = function(){
//		                   iframe.height = $(iframe).contents().find("body").height() + 50;
//		               };
//		           }  
//		    	
		    	var id=$(iframe).attr("id");
		    	var param="orgId="+orgId+"&optionType="+optionType;
		    	document.getElementById(id).contentWindow.update(param,orgId);
	    	}
	    },
	    
		initContainer:function(data,id,optionType){
			console.log("正在加载");
			if(data.rows==null)return;
			var divContainer=$("<div id=callRecord></div>");			
			var divItem=$("<div class=device-item> </div>");		// 这个人的所有通话记录
			$.ajaxSettings.async = false;
			$.get("recordHead.html",function(datahtml){
				divItem.append(datahtml);
			});
			divContainer.append(divItem);
			$("#div_container_callRecord").append(divContainer);
			
			$.ajaxSettings.async = false;
			$.get("pageTool.html",function(data){
				divItem.append(data);
				$.ajax({
					type:"GET",
					url:getRootPath()+"/callRecord/callTotal?id="+id+"&optType="+optionType,
					success:function(dataSource){
						var dataCount=dataSource.data;
						var selectEdRows=$("#pageTool select[id=iEveryCount] option:selected").val();
						var totalPage=dataCount%selectEdRows==0?dataCount/selectEdRows:Math.floor((dataCount/selectEdRows)+1);
						NameSpace.data.set_totalPage(totalPage);
						$("#pageTool span[id=currentPage]").text("当前1/"+totalPage);
						 NameSpace.data.set_totalCount(dataCount);
						$("#pageTool span[id=totalPage]").text("共"+dataCount+"条");
					},
				});
			});
			var table= recordModult.fillTable(data.rows);
			$("#deviceCallLog").append(table);
			
//			   var audio=$("audio");
//			    if(audio!=null){
//			    	console.log("audio-->"+audio.prop("nodeName"));
//			    	audio.audioPlayer();
//			    }
		},
	    
		fillData:function(data){
			if(data.rows==null)return;
//			var divContainer=$("<div id=callRecord></div>");			
//			var divItem=$("<div class=device-item> </div>");		// 这个人的所有通话记录
//			$.ajaxSettings.async = false;
//			$.get("recordHead.html",function(datahtml){
//				divItem.append(datahtml);
//			});
//			divContainer.append(divItem);
//			$("#div_container_callRecord").append(divContainer);
//			var table= recordModult.fillTable(data.rows);
//			$("#deviceCallLog").append(table);
			
			$("#deviceCallLog tbody").remove();
			
			var table= recordModult.fillTable(data.rows);
			$("#deviceCallLog").append(table);
			csxAudio.init("audiosx");
		/*	function getTitleName(rows){
				var orgName="";
				var orgList=rows.mOrganization;
				var total=rows.listDeviceTotal;
				var callLogCount=total.callLogCount;
				var callRecordCount=total.callRecordCount;
				var callAlreadyAcceptCount=total.callAlreadyAcceptCount;
				return orgList.name+"</br>"+"通话数量:"+callLogCount+"</br>"+"通话录音数量:"+callRecordCount;
			
			}*/

		},
		
		/**
		 * 统计通话记录
		 */
		callTotal:function(data){
			var optType=data.data;
			console.log("optType"+JSON.stringify(optType));
			var str= "<span class='titleChild'>通话数量 : "+optType.callLogCount+"</span>&nbsp;&nbsp;"
			+"<span class='titleChild'>录音数量 : "+optType.callRecordCount+"</span></br>\n"
			+"<span class='titleChild'>已接 : "+optType.callAlreadyAcceptCount
			+"</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='titleChild'>未接 : "+optType.callNoAccept
			+"</span>&nbsp;&nbsp;&nbsp;&nbsp;<span class='titleChild'>拨出 : "+optType.callCallOut+"</span>";
			$("#div_head_callRecord td[id=head_callRecord_child]").append(str);
		},
		
		
		
	
		fillTable:function(data){
			var currPage=NameSpace.data.get_currentPage();
			var totaPage=NameSpace.data.get_totalPage();
			console.log("totalPage-->"+totalPage+"-->"+currPage);
			NameSpace.data.set_globalData(data);
			var index=1;
			if(currPage==totaPage){
				var selectEdRows=$("#pageTool select[id=iEveryCount] option:selected").val();
				index=(totaPage-1)*selectEdRows+1;
			}else{
				var selectEdRows=$("#pageTool select[id=iEveryCount] option:selected").val();
				index=(currPage*selectEdRows)-data.length+1;
			}
			function getRow(callLog,i) {
				var row=callLog.sysDeviceRecord;
				var orgName=callLog.orgName;
				var callName=row.callName;
				var devSerial=callLog.devSerial;
				var tr=null;
				if(i%2==0){
					tr = $("<tr style='background-color: #ffffff;' id='tr"+i+"'></tr>");
				}else{
				     tr = $("<tr style='background-color: #f3f3f3;' id='tr"+i+"'></tr>");
				}
			    var type=recordCommon.callType(row.callType);
			    var callLogId=row.id;
			    
			    var callNumber=row.callNumber==""?"无":row.callNumber;
			    
			    var callHasRecord=row.callHasRecord==1?"下载录音":"无";
			    var callDate=recordCommon.getMoth(row.callDate);
			    var callDuration=row.callDuration==0?"0":row.callDuration;
			    console.log("填充表格"+recoAudioLength);
			    var secondIsValid=recordCommon.secondIsValid(callDuration);
			    var callDuration=recordCommon.secondToMinute(secondIsValid==true?(callDuration/1000):callDuration);
			    
			    var recoAudioLength=row.recoAudioLength==null?0:row.recoAudioLength;
				var recoAudioLength=recordCommon.secondToMinute(Math.floor(recoAudioLength/1000));
				callName=callName==""?"无":callName;
				tr.append("<td style='padding:0px;'>"+(index++)+"</td>");
				tr.append("<td style='padding:3px;' class='changeTd' title="+orgName+">"+orgName+"</td>");
			    tr.append('<td>'+type+'</td>');
			    tr.append("<td class='changeTd' title='"+callName+"'>"+callName+"</td>");
			    tr.append("<td class='changeTd' title='"+callNumber+"'>"+callNumber+"</td>");
			    
			    tr.append("<td class='changeTd' title='"+callDuration+"'>"+callDuration+"</td>");
			    tr.append("<td class='changeTd' title='"+recoAudioLength+"'>"+recoAudioLength+"</td>");
			    tr.append("<td style='padding:3px;'>"+callDate+"</td>");
			 
//			    tr.append('<td id=description'+callLogId+'>'+description+'</td>');
			    
			    tr.append("<td style='padding:3px;' class='bigTd' title='"+devSerial+"'>"+devSerial+"</td>");
			    var recoFilePath;
	  			if(callHasRecord=="下载录音"){
//			    	if(row.sysDeviceRecord!=null){			//  可能会有多条录音，要求前端合成音频后上传。href='+recoFilePath+';  download='+recoFilePath+';   注意这个经典写法  <a onclick="deviceCallLog.playAudio('+fileType+');"><img src="static/image/play.png;" width="25px;" height="25px;" /></a>
			    		var record=getRootPath()+"/deviceRecord/audio?callLogId="+callLogId;
				    	tr.append("<td class='audio bigTd'>"+
					    	"<div class='audiosx' id='audiosx"+callLogId+"'>"+
								"<button class='play' id='play'></button>"+
								"<div class='scroll' id='scrollBar'></div>"+
								"<div class='bar'></div>"+ 
								"<div class='mask'></div>"+ 
								"<span class='currTime' id='currTime'>00:00&nbsp;/&nbsp;</span>"+
								"<span class='time' id='time'>00:00</span>"+
								"<button class='down' id='down'></button>"+
								"<audio id='audioPlay' src='"+record+"' style='visibility: hidden;' ></audio>"+
							"</div>"+
				    	 /*"<audio style='width:260px;height: 32px;' id='audioPlay' src='"+record+"' controls='controls'>Your browser does not support the audio element.</audio>*/
				    	   "</td>");
			    }else{
				    tr.append("<td class='bigTd'>"+callHasRecord+"</td>");
			    }
			    tr.append('<td style="vertical-align: middle;">'+
			    		/*'<img src="static/image/remark.png;" width="25px;" height="25px;" onclick="deviceCallLog.remark('+callLogId+');"/>'+*/
			    		'<img src="static/image/delete.png;" width="25px;" height="25px;" onclick="recordModult.deleteLogAndRecord('+callLogId+',this,'+i+')"/></td>');
			  
			    return tr;
			}
			var tbody = $('<tbody></tbody>');
			for(var i = 0; data!=null&&i < data.length; i ++ ){ 
			    tbody.append(getRow(data[i],i));
			 
			}
			return tbody;
		},
		/**
		 * 删除通话记录
		 */
		deleteLogAndRecord:function(callLogId,obj,index){
			console.log("deleteLogAnd-->"+callLogId+"-->"+obj+"--->"+index);
			if(confirm("是否删除该条通话记录及录音")){
				var data=NameSpace.data.get_globalDataAll();
				var dataObj=data[index];
				callLogId=dataObj.callId;
				var deviceId=dataObj.sysDeviceRecord.deviceId;
				var callType=dataObj.sysDeviceRecord.callType;
				var callDuration=dataObj.sysDeviceRecord.callDuration;
				var status=dataObj.sysDeviceRecord.status;
				var createTime=dataObj.sysDeviceRecord.createTime;
				var data={id:callLogId,deviceId:deviceId,callType:callType,callDuration:callDuration,status:status,createTime:createTime};
				data=JSON.stringify(data);
//				$("#deviceCallLog tr[id=tr"+index+"]").remove();
//				recordCommon.currPage();
				$.ajax({
					data:data,
					type:"POST",
					dataType:"json",
					url:getRootPath()+"/deviceCallLog/delete",
					traditional:true,
					contentType : 'application/json',
					success:function(data){
						if(data.code==10000){
							common_tool.messager_show("操作成功");
//							$("#deviceCallLog tr[id=tr"+index+"]").remove();
							recordCommon.currPage();
						}else{
							common_tool.messager_show(data.msg);
						}
					}
				});
				
			}else{
				
			}
			
		},
	/*	
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
		*/
		
		
		/**
		 * 刷新通话记录
		 * optType 0 查询部门 ,1 查询机构
		 * options 1,普通加载，排序，2，下一页 ，3. 上一页，4.跳转,5,修改每页加载数量 默认不处理 6,首页，7，尾页
		 * sTimeType 1,近三天  2，近 七天 3，近半月 4，近一月 5，近三月 6,近半年 7 今年内 8 一年前 如今年是2019 就查询去年2018年，整年数据  默认查询七天内的
		 */
		 refreshCallLog:function(optType,id,sort,order,options,definePage,sTimeType,sContent){
		    	var selectEdRows=$("#pageTool select[id=iEveryCount] option:selected").val();
		    	var searchType=$("#tableCallLogSear input[name=radio]:checked").val();
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
		    	}else if(options==8){
		    		NameSpace.data.set_currentPage(NameSpace.data.get_currentPage());
		    	}
		    	
			$.ajax({
				type:"GET",
				url:getRootPath()+"/callRecord/list?id="+id+"&optType="+optType+"&page="+NameSpace.data.get_currentPage()+"&row="+selectEdRows+"&sort="+sort+"&order="+order+"&beginTime="+NameSpace.data.beginTime+"&endTime="+NameSpace.data.endTime+"&sContent="+NameSpace.data.sContent+"&searchType="+searchType+"&callIsHaveRecord="+NameSpace.data.callIsHaveRecord,
				success:function(data){
					recordModult.fillData(data);
					$("#audioPlay").volume=0.5;
					$("#pageTool span[id=currentPage]").text("当前"+(NameSpace.data.get_currentPage()<NameSpace.data.get_totalPage()?NameSpace.data.get_currentPage():NameSpace.data.get_totalPage())+"/"+NameSpace.data.get_totalPage()+"页");
					console.log("刷新通话数据");
				},
			});
		},
		
		/**
		 * 刷新通话统计
		 */
		refreshTotal:function(optType,id){
			var searchType=$("#tableCallLogSear input[name=radio]:checked").val();
			$.ajax({
				type:"GET",
				url:getRootPath()+"/callRecord/deviceTotal?id="+id+"&optType="+optType+"&beginTime="+NameSpace.data.beginTime+"&endTime="+NameSpace.data.endTime+"&sContent="+NameSpace.data.sContent+"&searchType="+searchType,
				success:function(data){
//					console.log("设备统计-->"+JSON.stringify(data));
					$("#head_callRecord_child").empty();
					recordModult.callTotal(data);
					
				}
			})
		},
		
		initSearContent:function(){
			NameSpace.data.sContent="";
			$("#tableCallLogSear input[id=input_search]").val("");
		},
		
		/**
		 * 刷新页面数量
		 */
		refreshPage:function(optType,id){
			NameSpace.data.set_currentPage(1);
			NameSpace.data.set_optType(optType);
			NameSpace.data.set_orgIdUserId(id);
			NameSpace.data.set_globalOrder("desc");
			var searchType=$("#tableCallLogSear input[name=radio]:checked").val();
			$.ajax({
				type:"GET",
				url:getRootPath()+"/callRecord/callTotal?id="+id+"&optType="+optType+"&beginTime="+NameSpace.data.beginTime+"&endTime="+NameSpace.data.endTime+"&sContent="+NameSpace.data.sContent+"&searchType="+searchType+"&callIsHaveRecord="+NameSpace.data.callIsHaveRecord,
				success:function(data){
					var dataCount=data.data;
//					var selectEdRows=$("#pageTool select[id=iEveryCount] option:selected").val();
////					var selectEdRows=15;
//					console.log("selectEdRows-->"+selectEdRows);
//					var totalPage=dataCount%selectEdRows==0?dataCount/selectEdRows:Math.floor((dataCount/selectEdRows)+1);
//					$("#pageTool span[id=currentPage]").text("当前1/"+totalPage);
//					$("#pageTool span[id=totalPage]").text("共"+dataCount+"条");
					
					var selectEdRows=$("#pageTool select[id=iEveryCount] option:selected").val();
					var totalPage=dataCount%selectEdRows==0?dataCount/selectEdRows:Math.floor((dataCount/selectEdRows)+1);
					NameSpace.data.set_totalPage(totalPage);
					
					$("#pageTool span[id=currentPage]").text("当前1/"+totalPage);
					 NameSpace.data.set_totalCount(dataCount);
					$("#pageTool span[id=totalPage]").text("共"+dataCount+"条");
					
				},
			});
		},
		
		
		
},



$(document).ready(function() {
	var id=homeDevic.getGlobalId();
	var optionType=homeDevic.getGloOptType();
	NameSpace.data.set_orgIdUserId(id);
	NameSpace.data.set_optType(optionType);
	NameSpace.data.set_globalSort("call_date");
	NameSpace.data.set_globalOrder("desc");  
	var bgTime=getFirstDayOfWeek();
	NameSpace.data.beginTime=getTimeStamp(bgTime);
	
	var edTime=getCurrTime();
	NameSpace.data.endTime=getTimeStamp(edTime);

	
	/**
	 * 确定搜索时间
	 */
	$("#timeContainer button[id=confirmTime]").click(function(){
		var beginTime=$("#timeContainer input[id=beginTime]").val();
		NameSpace.data.beginTime=getTimeStamp(beginTime);
		var endTime=$("#timeContainer input[id=endTime]").val();
		NameSpace.data.endTime=getTimeStamp(endTime);
		recordCommon.selectCallDate();
	});
	$("#timeContainer input[id=cbHaveRecord]").change(function(){
		var check=$(this).prop("checked");
		NameSpace.data.callIsHaveRecord=check==true?1:0;
		recordCommon.selectCallDate();
	});
	console.log("初始化刷新");
		$.ajax({
			type:"GET",
			url:getRootPath()+"/callRecord/list?id="+id+"&optType="+optionType+"&page=1&row=15",
			success:function(data){
				recordModult.initContainer(data,id,optionType);
			},
		});
		
		$.ajax({
			type:"GET",
			url:getRootPath()+"/callRecord/deviceTotal?id="+id+"&optType="+optionType,
			success:function(data){
				recordModult.callTotal(data);
			}
		});
		/**搜索*/
		$("#tableCallLogSear button[class=alldevice]").click(function(){
			recordCommon.selectCallDate();
		});
		
		var the_timeout=setTimeout(function(){
			csxAudio.init("audiosx");
		},30); 
});
})();


      