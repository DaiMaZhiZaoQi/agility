
var csxAudioSp=window.NameSpace||{};

csxAudioSp.data={
				playpg:getRootPath()+"/static/css/home/csxAudio/play.png",
				stoppg:getRootPath()+"/static/css/home/csxAudio/stop.png",
				playBtn:null,
				autoplay:false,
		};
csxAudio={
		
		
	playReady:function(id){
		var audiosx=document.getElementById(id);
		// var curTime=document.getElementById("currTime");	// 4
		if(audiosx==null){
			console.log("please add label");
			return;
		}
		var curTime=audiosx.children[4];				// 4
		// var str=document.getElementById("str");
		
		// var play=document.getElementById("play");	// 0
		var play=audiosx.children[0];	// 0
		// var down=document.getElementById("down");		// 6
		var down=audiosx.children[6];		// 6
		var audio=audiosx.children[7];			//7
		if(audio==undefined){
			return;
		}
		audio.load();
		// var audio=document.getElementById("audio");			//7
		// var time=document.getElementById("time");		// 5
		var time=audiosx.children[5];		// 5
		
		var scrollBar=audiosx.children[1];			// 1
		var bar=audiosx.children[2]; 				// 2
		var mask=audiosx.children[3];				// 3
		var barWidth=bar.offsetWidth ;
		if(barWidth>7){
			barWidth=7;
		}
		console.log("barWidth-->"+barWidth);
		var scrollBarWidth=scrollBar.offsetWidth ;
		if(scrollBarWidth>90){
			scrollBarWidth=90;
		}
		console.log("scrollBarWidth-->"+scrollBarWidth);
		var maskOfLeft=mask.offsetLeft;
		console.log("maskOfLeft-->"+maskOfLeft);
		//  鼠标按下bar 滚动到鼠标按下位置
		bar.onmousedown=function(event){ 
			var event=event||window.event;
			var leftVal=event.clientX-this.offsetLeft;
			console.log("offsetLeft-->"+this.offsetLeft+"leftVal"+leftVal);
			var that=this;
			 
			document.onmousemove=function(event){ 
				var event=event||window.event; 
				var left=event.clientX-leftVal;  //scrollBar.offsetLeft-
				if(audio!=null&&csxAudioSp.data.autoplay){
					var timeRate=((left-maskOfLeft)/scrollBarWidth);
					console.log("left-->"+left+"scrollBarWidth-->"+scrollBarWidth);
					console.log("timeRate-->"+timeRate+"-->"+audio.duration);
					audio.currentTime=(timeRate*audio.duration);
					console.log("timeRate-->"+timeRate+"-->"+audio.duration+"--当前播放->"+audio.currentTime);
					changTime(audio.currentTime);
					step(left,bar);
					return;
				}
				return;
				// step(left,that);
			} 
			document.onmouseup=function(event){
				document.onmousemove=null;
			} 
			
		}
		function step(left,that){
			console.log("step-->"+left);
			bar.style.left=left+"px";	 
			var le=parseInt(bar.style.left);
			
			if(le>=((scrollBarWidth+maskOfLeft)-barWidth)){
				bar.style.left=(scrollBarWidth+maskOfLeft)-barWidth+"px";
			}else if(le<scrollBar.offsetLeft){
				bar.style.left=maskOfLeft+"px";
			}
			var maskWidth=parseInt(that.style.left)-parseInt(maskOfLeft);
			mask.style.width=maskWidth+"px";  
			var inValue=parseInt(maskWidth);
			var inScB=parseInt(scrollBarWidth-barWidth);
			// str.innerHTML="当前，完成了"+parseInt((inValue/inScB)*100)+"%";
			 window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
		}
		
		function changTime(timeV){
			curTime.innerHTML=secondToMinute(timeV)+"/&nbsp;";
		}
		var changeV=0;
		function changeColor(barLable){
			if(changeV%2==0){
				barLable.style.backgroundColor= "rgb(69, 171, 232)";
			}else{
				barLable.style.backgroundColor= "rgb(52, 224, 89)";
			}
			changeV++;
		}
		
	
		
		/**秒数转分钟*/    
	    function secondToMinute(callDuration){
//	    	callDuration=callDuration+0.5;
	    	if(callDuration<60){
	    		callDuration=Math.round(callDuration);
	    		return "00:"+(callDuration<10?("0"+callDuration):callDuration);
	    	}else{
	    		if(callDuration>=60&&callDuration<=3600){
	    			var minute=callDuration/60;
	    			var second=callDuration%60;
	    			var mi=Math.floor(minute);
	    			var sec=Math.round(second);
	    			return (mi<10?("0"+mi):mi)+":"+(sec<10?("0"+sec):sec);
	    		}else{
	    			var hour=callDuration/3600;
	    			var m=callDuration%3600;
	    			var minute=secondToMinute(m);
	    			return Math.floor(hour)+":"+minute;
	    		}
	    	}
	    }
		    
		
			// 没有资源
			audio.onerror=function(){
				audiosx.style.backgroundColor="#CCCCCC";
				play.disabled="disabled";
				bar.onclick="disabled";
			}
		
			var audioDuration=0;
			audio.oncanplay=function(){ 	//  音频长度
				audioDuration=audio.duration;
				var minute=secondToMinute(audioDuration);
				if(audioDuration>=3600){
					time.style.right="29px";
				}else{
					
				}
				time.innerHTML=minute; 
				if(audioDuration>0){
					csxAudioSp.data.autoplay=true;
				}
			}	
		
		
			audio.ontimeupdate=function(){
				// if(fromUser)return;
//				if(csxAudioSp.data.playBtn==null)return;
				var curTimeV=audio.currentTime;			//   音频不能拖放原因
				console.log("curTime"+curTimeV);
				if(audioDuration<=0)return;
				var rate=(curTimeV/audioDuration);
				console.log("rate-->"+rate);
				var stepTime=(rate*100);
				console.log("正在播放"+stepTime+"%");
				var curProgress=((rate*scrollBarWidth)+maskOfLeft);
				changeColor(bar);
				changTime(curTimeV);
				step(curProgress,bar);
			}
			audio.onended=function(){
				play.click();
			}
			var cCount=0;
			play.onclick=function(){	 // 播放控制
				if(audio!=null&&csxAudioSp.data.autoplay){
					if(isNaN(audio.duration)){
						alert("沒找到资源")
						return;
					}
					if(cCount%2==0){		// 暂停
//						play.style['background-image']="url(stop.png)";
						play.style['background-image']="url("+csxAudioSp.data.stoppg+")";
						play.style["background-repeat"]="no-repeat";
						play.style["background-position"]="-2px -1px";
						if(csxAudioSp.data.playBtn!=null&&csxAudioSp.data.playBtn!=this){
							csxAudioSp.data.playBtn.click();
						}
						csxAudioSp.data.playBtn=this;
						audio.play();
					}else{					// 播放
//						play.style['background-image']="url(play.png)";
						play.style['background-image']="url("+csxAudioSp.data.playpg+")";
						play.style["background-repeat"]="no-repeat";
						play.style["background-position"]="-2px -1px";
						audio.pause();
						csxAudioSp.data.playBtn=null;
					}
					cCount++;
				}else{
					alert("没找到资源");
				}
			}
		
			down.onclick=function(){
				if(audio!=null&&csxAudioSp.data.autoplay){
					if(isNaN(audio.duration)){
						alert("沒找到资源")
						return;
					}
					window.location.href=audio.src;
				}else{
					alert("没找到资源");
				}
			}
		
	},
	
	testFun:function(arg){
		console.log(arg);
	},
	
	init:function(parentId){
//		var audioTag=document.getElementsByClassName("audiosx");
		var audioTag=document.getElementsByName(parentId);
		if(audioTag==undefined||audioTag.length<=0){
			console.log("没有找到资源");
			return;
		}
		var tag=audioTag[0].id;
		console.log("parentId-->"+tag);
		for(var i=0;i<audioTag.length;i++){
			console.log("当前id-->"+audioTag[i].id);
			csxAudio.playReady(audioTag[i].id);
		}
		
		/*window.onload=function(){ 
		
		}*/
	},
}


