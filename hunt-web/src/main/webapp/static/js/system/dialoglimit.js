/**
 * 限制dialog 不能超出范围
 * PS:是下面这段JS里面的宽和高 不是界面上的。还有尽量不要修改Panel的移动事件，因为很多其他控件都是从他衍生，改了的话很容易导致整个框架瘫痪
 */

 var easyuiPanelOnMove = function (left, top) {
	 alert("正在移动");
    var parentObj = $(this).panel('panel').parent();
    if (left < 0) {
        $(this).window('move', {
            left: 1
        });
    }
    if (top < 0) {
        $(this).window('move', {
            top: 1
        });
    }
    var width = $(this).panel('options').width;
    var height = $(this).panel('options').height;
    var right = left + width;
    var buttom = top + height;
    var parentWidth = parentObj.width();
    var parentHeight = parentObj.height();
    if (parentObj.css("overflow") == "hidden") {
        if (left > parentWidth - width) {
            $(this).window('move', {
                "left": parentWidth - width
            });
        }
        if (top > parentHeight - height) {
            $(this).window('move', {
                "top": parentHeight - height
            });
        }
    }
};
$.fn.panel.defaults.onMove = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;
$.fn.dialog.defaults.onMove = easyuiPanelOnMove;

