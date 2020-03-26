//右下角显示消息
var homeRoleOrgId="";
//机构id
var homeClickOrgId="";
var orgPersonId="";
// 状态栏当前选中的状态  0,设备状态，1,通话记录。2,通讯录 3,设备管理
var homeStatusSelect="";
// 当前登录的用户id
var currUserId="";




var USER_MANAGE={
		 pCode:-100,
		 pName:"user:manage", 
		 pMsg:"您没有用户管理权限"
};
var ROLE_MANAGE={
		pCode:-101,
		pName:"role:manage", 
		pMsg:"您没有角色管理权限"
};

var ORG_MANAGE={
		pCode:-102,
		pName:"org:manage", 
		pMsg:"您没有机构管理权限"
};

var SYSTEM_MANAGE={
		pCode:-103,
		pName:"system:manage", 
		pMsg:"您没有系统管理权限"
};


var DEVICE_INSERT={
		pCode:-104,
		pName:"device:insert", 
		pMsg:"您没有设备注册权限"
};
var DEVICE_SELECT={
		pCode:-105,
		pName:"device:select", 
		pMsg:"您没有设备查询权限"
};
var DEVICE_MANAGE={
		pCode:-106,
		pName:"device:manage", 
		pMsg:"您没有设备维护权限"
};

var CALLLOG_INSERT={
		pCode:-107,
		pName:"callLog:insert", 
		pMsg:"您没有添加通话记录权限"
};
var CALLLOG_SELECT={
		pCode:-108,
		pName:"callLog:select", 
		pMsg:"您没有通话记录查询权限"
};
var CALLLOG_DELETE={
		pCode:-109,
		pName:"callLog:delete", 
		pMsg:"您没有通话记录维护权限"
};


var RECORD_INSERT={
		pCode:-110,
		pName:"record:insert", 
		pMsg:"您没有添加录音权限"
};
var RECORD_SELECT={
		pCode:-111,
		pName:"record:select", 
		pMsg:"您没有录音查询权限"
};
var RECORD_DELETE={
		pCode:-112,
		pName:"record:delete", 
		pMsg:"您没有录音维护权限"
};


var CONTACT_INSERT={
		pCode:-113,
		pName:"contact:insert", 
		pMsg:"您没有通讯录发布权限"
};
var CONTACT_SELECT={
		pCode:-114,
		pName:"contact:select", 
		pMsg:"您没有通讯录查询权限"
};
var CONTACT_DELETE={
		pCode:-115,
		pName:"contact:delete", 
		pMsg:"您没有通讯录维护权限"
};




common_tool = {
		// 全局用户角色权限
	 gloPer:"",
	 /**搜索的开始时间*/
	 searchBeginTime:null,
	 
	 set_beginTime:function(searchBeginTime){
		this.searchBeginTime=searchBeginTime;
	 },
	 
	 get_beginTime:function(){
		return this.searchBeginTime; 
	 },
	 
    messager_show: function (msg) {
        $.messager.show({
            title: '系统提示',
            msg: msg,
            timeout: 3000,
            width:400,
            height:200,
            showType: 'slide'
        });
    },
    
    /**
     * 设置全局权限
     */
    setGloPer:function(gloPer){
    	this.gloPer=gloPer;
    },
    
    getGloPer:function(){
    	return this.gloPer;
    },
    
    timestampToDateTime: function (value) {
        date = new Date(value);
        timeStr = date.getFullYear() + "-" + (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + "-" + date.getDate() + " " + (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ":" + (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ":" + (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
        return timeStr;
    },
    
    setCurrUserId:function(userId){
//    	alert("当前用户id"+userId);
    	currUserId=userId;
    },
    getCurrUserId:function(){
    	return currUserId;
    },
    
    setHomeRoleOrgId:function(roleOrgId){
    	homeRoleOrgId=roleOrgId;
    },
    getHomeRoleOrgId:function(){
    	return homeRoleOrgId;
    },
    
    set_orgPersonId:function(obj){
    	orgPersonId=obj;
    },
    
    get_orgPersonId:function(){
    	return orgPersonId;
    },
    
    set_homeClickOrgId:function(obj){
    	homeClickOrgId=obj;
    },
    
    get_homeClickOrgId:function(){
    	return homeClickOrgId;
    },
    
    setSelectStatus:function(homeStatusSelect){
    	this.homeStatusSelect=homeStatusSelect;
    },
    
    getHomeSelectStatus:function(){
    	return this.homeStatusSelect;
    },

    /**验证是否为毫秒数，毫秒数转换为秒数*/
    secondIsValid:function(second){
    	var regex=/^\d{13}$/;
    	return regex.test(second);
    	
    },
  
    /**秒数转分钟*/    
    secondToMinute:function(callDuration){
//    	callDuration=callDuration+0.3;
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
	   			var minute=common_tool.secondToMinute(m);
	   			return Math.floor(hour)+":"+minute;
	   		}
	   	}
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
   
   /**毫秒数转时间*/
	getSimMoth:function(str,leve){  
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
		if(leve==1){
			return month+'-'+day+' '+hour+':'+minute+':'+second;
		}else if (leve==2) {
			return month+'-'+day+' '+hour+':'+minute;
		}
  },
   
   
   /**验证是否为毫秒数，毫秒数转换为秒数*/
   secondIsValid:function(second){
   	var regex=/^\d{13}$/;
   	return regex.test(second);
   	
   },
    /**毫秒数转时间*/
	/*getMoth:function(str){  
		var dateTime=new Date(str);
		var year=dateTime.getFullYear();
		var month=dateTime.getMonth() + 1;
		var day=dateTime.getDate();
		var hour=dateTime.getHours();
		var minute=dateTime.getMinutes();
		var second=dateTime.getSeconds(); 
		minute=minute<10?("0"+minute):minute;
		return year+'-'+month+'-'+day+' '+hour+':'+minute+':'+second;
    },*/
    
    /**
     * 通话类型 
     */
    callType:function(type){
		switch (type) {
		case 0:
			return "未接电话";
		case 1:
			return "呼入";
		case 2:
			return "呼出";
		case 3:
			return "未接";
		case 4:
			return "未接留言";
		case 5:
			return "拒接";
		default:
			return "未知";
		}
	},
}

function getBrowser2() {
    var ua = window.navigator.userAgent;
    //var isIE = window.ActiveXObject != undefined && ua.indexOf("MSIE") != -1;
    var isIE = !!window.ActiveXObject || "ActiveXObject" in window;
    var isFirefox = ua.indexOf("Firefox") != -1;
    var isOpera = window.opr != undefined;
    var isChrome = ua.indexOf("Chrome") && window.chrome;
    var isSafari = ua.indexOf("Safari") != -1 && ua.indexOf("Version") != -1;
    if (isIE) {
        return "IE";
    } else if (isFirefox) {
        return "Firefox";
    } else if (isOpera) {
        return "Opera";
    } else if (isChrome) {
        return "Chrome";
    } else if (isSafari) {
        return "Safari";
    } else {
        return "Unkown";
    }
}

function getBrowser() {
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var isOpera = userAgent.indexOf("Opera") > -1; //判断是否Opera浏览器
    var isIE = userAgent.indexOf("compatible") > -1
            && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器
    var isEdge = userAgent.indexOf("Edge") > -1; //判断是否IE的Edge浏览器
    var isFF = userAgent.indexOf("Firefox") > -1; //判断是否Firefox浏览器
    var isSafari = userAgent.indexOf("Safari") > -1
            && userAgent.indexOf("Chrome") == -1; //判断是否Safari浏览器
    var isChrome = userAgent.indexOf("Chrome") > -1
            && userAgent.indexOf("Safari") > -1; //判断Chrome浏览器

    if (isIE) {
        var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
        reIE.test(userAgent);
        var fIEVersion = parseFloat(RegExp["$1"]);
        if (fIEVersion == 7) {
            return "IE7";
        } else if (fIEVersion == 8) {
            return "IE8";
        } else if (fIEVersion == 9) {
            return "IE9";
        } else if (fIEVersion == 10) {
            return "IE10";
        } else if (fIEVersion == 11) {
            return "IE11";
        } else {
            return "0";
        }//IE版本过低
        return "IE";
    }
    if (isOpera) {
        return "Opera";
    }
    if (isEdge) {
        return "Edge";
    }
    if (isFF) {
        return "FF";
    }
    if (isSafari) {
        return "Safari";
    }
    if (isChrome) {
        return "Chrome";
    }
    
}

/**
 * 
 * @returns 当前时间
 */
function comGetTime (){
	var date=new Date(); 
	var fullyear=date.getFullYear();
	var mon=date.getMonth()+1;
	mon=mon<=9?("0"+mon):mon;
	var da=date.getDate()<=9?("0"+date.getDate()):date.getDate();
	return fullyear+"-"+mon+"-"+da;
}

function getCurrTime() {
	var dateTime=new Date(); 
	var year=dateTime.getFullYear();
	var month=dateTime.getMonth() + 1;
	var day=dateTime.getDate();
	var hour=dateTime.getHours();
	var minute=dateTime.getMinutes();
	var second=dateTime.getSeconds(); 
	month=month<=9?("0"+month):month;
	day=day<=9?("0"+day):day;
	hour=hour<=9?("0"+hour):hour;
	minute=minute<=9?("0"+minute):minute;
	second=second<=9?("0"+second):second;
	var result= year+"-"+month+"-"+day+"T"+hour+":"+minute+":"+second;
	return result;
}


/**
 * 获得这个月第一天
 * @returns
 */
function getCurrMonth(){
	var date=new Date(); 
	var fullyear=date.getFullYear();
	var mon=date.getMonth()+1;
	mon=mon<=9?("0"+mon):mon;
//	var da=date.getDate()<=9?("0"+date.getDate()):date.getDate();
	return fullyear+"-"+mon+"-"+"01"+"T00:00";
}

//function getFirstDayOfWeek () {
//	var date=new Date();
//    var day = date.getDay() || 7; 
//    console.log("getFirstDayOfWeek"+day);
//    day=date.getDate() + 1 - day;
//    var fullyear=date.getFullYear();
//	var mon=date.getMonth()+1;
//	day=day<=9?("0"+day):day;
//	mon=mon<=9?("0"+mon):mon;
//    return fullyear+"-"+mon+"-"+day+"T00:00";
//};

function getFirstDayOfWeek(){
	
	var date=new Date();
	var day=date.getDay();
	console.log("day-->"+day);		// 星期
	var time=date.getTime();
	var hours=date.getHours();
	console.log("hours-->"+hours);
	var minutes=date.getMinutes();
	console.log("minutes-->"+minutes);
	var miSeconds=date.getSeconds();
	console.log("miSeconds-->"+miSeconds)
	var timeDiff=60*60*24*1000*(day-1)+(60*60*hours*1000)+(60*minutes*1000)+(miSeconds*1000);
	var timeResult=time-timeDiff;
	var dateTime=new Date(timeResult);
	
	var year=dateTime.getFullYear();
	var month=dateTime.getMonth() + 1;
	var day=dateTime.getDate();
	var hour=dateTime.getHours();
	var minute=dateTime.getMinutes();
	var second=dateTime.getSeconds(); 
	month=month<=9?("0"+month):month;
	day=day<=9?("0"+day):day;
	hour=hour<=9?("0"+hour):hour;
	minute=minute<=9?("0"+minute):minute;
	second=second<=9?("0"+second):second;
	return year+"-"+month+"-"+day+"T"+hour+":"+minute+":"+second;
}


/**
 * 判断是否为数字串
 * @param obj
 * @returns
 */
function isNumbers(obj){
	var regex=/^\d+$/;
	return regex.test(obj);
	
}
function getRootPath() {
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPatht = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1)
    return (localhostPatht + projectName);
}
/**
 * 判断字符串为空
 * @param str
 * @returns
 */
function strIsEmpty(str){
	var regex=/^[\u4E00-\u9FA5A-Za-z0-9_]+$/;
	return str==null||str.length==0||str.trim().length==0||str.toLowerCase()=="null"||!(regex.test(str)); 	
}

/**
 * 字母或数字有效
 * @param str
 * @returns
 */
function strIsEngNum(str){
	var regex=/^[A-Za-z0-9_]+$/;
	return str==null||str.length==0||str.trim().length==0||!(regex.test(str));
}

function getTimeStamp(date){
//	alert("date"+date);
	if(date.indexOf(" ")){
		date=date.replace(" ","T");
	}
	var time=new Date(date).getTime();
	return time;
}

/**
 * 判断字符串是否为null
 * @param str
 * @returns
 */
function strIsEmpty(str){
	return str==null||str.length==0||str=="null"||str==undefined;
}




