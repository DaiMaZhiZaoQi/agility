//右下角显示消息
var homeRoleOrgId="";
//机构id
var homeClickOrgId="";
var orgPersonId="";
// 状态栏当前选中的状态  0,设备状态，1,通话记录。2,通讯录 3,设备管理
var homeStatusSelect="";
// 当前登录的用户id
var currUserId="";
common_tool = {
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

function getFirstDayOfWeek () {
	var date=new Date();
    var day = date.getDay() || 7; 
    day=date.getDate() + 1 - day;
    var fullyear=date.getFullYear();
	var mon=date.getMonth()+1;
	day=day<=9?("0"+day):day;
	mon=mon<=9?("0"+mon):mon;
    return fullyear+"-"+mon+"-"+day+"T00:00";
};
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
	return str==null||str.length==0||str.trim().length==0||!(regex.test(str)); 	
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




