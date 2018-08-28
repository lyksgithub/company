<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/public/head.jsp" %>
<%@ include file="/WEB-INF/public/top.jsp" %>

<div class="am-cf admin-main">
  <%@ include file="/WEB-INF/public/menu.jsp" %>
 
  <!-- content start -->
  <div class="admin-content" style="overflow:auto;z-index:999">
  	<div class="am-cf am-padding">
  		<div class="am-u-sm-3 am-text-left">
  		   <select id="select_floor">
  			 <option value="0" selected>选择楼层</option>
	       </select>
	       <button type="button" id="btnChangePersonsFloor" class="am-btn am-btn-primary am-btn-xs"><span class="am-icon-refresh"></span> 切换</button>
	       <button type="button" id="btnPlusMap" class="am-btn am-btn-primary am-btn-xs"><span class="am-icon-search-plus"></span> 放大</button>
	       <button type="button" id="btnMinusMap" class="am-btn am-btn-primary am-btn-xs"><span class="am-icon-search-minus"></span> 缩小</button>
  		</div>
  		<div id ="select_style"  class="am-u-sm-3 am-text-left" >
  		   <select id="select_mac" ></select>
	       <input type="text" id="user-mac" placeholder="用户MAC,多个MAC之间用逗号分隔" style="display:none;width:150px;">
	       <button type="button" id="btnSearchMacTrack" class="am-btn am-btn-primary am-btn-xs"><span class="am-icon-search"></span> 查询</button>
  		</div>
    </div>
    <div class="am-progress am-margin-bottom-xs" style="height:1px;">&nbsp;</div>
    <div class="am-cf am-padding am-margin-sx">
    	<div id="floormap" class="am-u-md-9" style="overflow:auto;width:100%;background:none;">
    		<!-- <canvas id="canvas">您的浏览器不支持canvas!</canvas> -->
    		
	    </div>
    </div>
  </div>
  <!-- content end -->

</div>
<%@ include file="/WEB-INF/public/foot.jsp" %>
<script>
	page="customer_"+page;
	
	$(function(){
		$("#"+page).attr("class","current");	
		var floor = $('#select_floor option:selected').val();
		$("#floormap").append('<p id="realfloor">当前楼层：'+floor+'</p><div class="custumermap" id="customer_map"><canvas id="canvas">您的浏览器不支持canvas!</canvas></div>');		
		var data = {
				"building" : building,
				"floor" : floor
			};
		getAllPersonRealtimePosition(data);
		clearInterval(handle);
		handle = setInterval(function(){
			getAllPersonRealtimePosition(data);
		}, 2000);
			
		$('#btnChangePersonsFloor').click(function(){
			clearInterval(handle);
			floor = $('#select_floor option:selected').val();
			$('#realfloor').html("当前楼层："+floor);
				if (floor == '0'){
					layer.alert("请选择楼层", {
						icon : 0
					});
					return;
				}
             data = {
            		 "building" : building,
            		 "floor" : floor
             };
				getAllPersonRealtimePosition(data);
	    		handle = setInterval(function(){
		     	getAllPersonRealtimePosition(data);
		     }, 2000);	
		});
		
		var oImg = document.getElementById('customer_map');

	});
	
	function addEvent(obj, sType, fn) {
	    if (obj.addEventListener) {
	        obj.addEventListener(sType, fn, false);
	    } else {
	        obj.attachEvent('on' + sType, fn);
	    }
	};
	function removeEvent(obj, sType, fn) {
	    if (obj.removeEventListener) {
	        obj.removeEventListener(sType, fn, false);
	    } else {
	        obj.detachEvent('on' + sType, fn);
	    }
	};
	function prEvent(ev) {
	    var oEvent = ev || window.event;
	    if (oEvent.preventDefault) {
	        oEvent.preventDefault();
	    }
	    return oEvent;
	}

	/*添加滑轮事件*/
	function addWheelEvent(obj, callback) {
	    if (window.navigator.userAgent.toLowerCase().indexOf('firefox') != -1) {
	        addEvent(obj, 'DOMMouseScroll', wheel);
	    } else {
	        addEvent(obj, 'mousewheel', wheel);
	    }
	    function wheel(ev) {
	        var oEvent = prEvent(ev),
	        delta = oEvent.detail ? oEvent.detail > 0 : oEvent.wheelDelta < 0;
	        callback && callback.call(oEvent, delta);
	        return false;
	    }
	};
	
//	$('#select_style').hide();
	var macData = [];
	var selectedValue = '';
	var queryMac = '';
	$('#select_mac').change(function(){
		macData = [];
		selectedValue = $("#select_mac option:selected").val();
		if($(this).children('option:selected').val()=='-1'){	
			$("#user-mac").show();
		}else{
			$("#user-mac").hide();
		}
	});
	var mac;
	$('#btnSearchMacTrack').click(function(){	
		macData = [];
		queryMac = $('#user-mac').val();	
	    macData = queryMac.split(",");
		//0：用户选择   -1：输入mac
		if(selectedValue != "0" && selectedValue != "-1"){
			macData = [];
			queryMac=selectedValue;
			macData.push(queryMac);
		}
	});
	
	
	$(document).ready(function(){
	//  直接调用 参数代表分钟数,可以有一位小数;
	     timeUserFun(30);
	 });
</script>