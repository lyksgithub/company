<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/public/head.jsp" %>
<%@ include file="/WEB-INF/public/top.jsp" %>

<div class="am-cf admin-main">
<%@ include file="/WEB-INF/public/menu.jsp" %>
<style>
.ratings_bars{width:407px;height:27px;float: left;}
.ratings_bars #title0{width:25px;height:25px;text-align:center;border:1px solid #bfbebe;line-height:25px;font-family:Georgia, "Times New Roman", Times, serif;font-size:14px;float:left;color:#a0a0a0;margin-right:10px;background:#fff;}
.ratings_bars #title1{width:25px;height:25px;text-align:center;border:1px solid #bfbebe;line-height:25px;font-family:Georgia, "Times New Roman", Times, serif;font-size:14px;float:left;color:#a0a0a0;margin-right:10px;background:#fff;}
.ratings_bars #title2{width:25px;height:25px;text-align:center;border:1px solid #bfbebe;line-height:25px;font-family:Georgia, "Times New Roman", Times, serif;font-size:14px;float:left;color:#a0a0a0;margin-right:10px;background:#fff;}
.ratings_bars #title3{width:25px;height:25px;text-align:center;border:1px solid #bfbebe;line-height:25px;font-family:Georgia, "Times New Roman", Times, serif;font-size:14px;float:left;color:#a0a0a0;margin-right:10px;background:#fff;}
.ratings_bars #title4{width:25px;height:25px;text-align:center;border:1px solid #bfbebe;line-height:25px;font-family:Georgia, "Times New Roman", Times, serif;font-size:14px;float:left;color:#a0a0a0;margin-right:10px;background:#fff;}
.ratings_bars .bars_10{font-family:Georgia, "Times New Roman", Times, serif;font-size:18px;line-height:25px;float:left;color:#a0a0a0;}
.ratings_bars .scale{width:299px;height:13px;float:left;margin:7px 10px auto 10px;position:relative;background:url(<%=basePath%>/static/images/progress.png) 0 0 no-repeat;}
.ratings_bars .scale div{width:0px;position:absolute;width:0;left:0;height:13px;bottom:0;background:url(<%=basePath%>/static/images/progress.png) 0 -14px no-repeat;}
.ratings_bars .scale span{width:10px;height:26px;position:absolute;left:-2px;top:-7px;cursor:pointer;background:url(<%=basePath%>/static/images/bar.png) no-repeat;}
</style>
  <!-- content start -->
  <div class="admin-content" style="">
  	<div class="am-cf am-padding">
  		<div class="am-u-sm-12 am-text-left">
  			<select id="select_floor">
  			 <option value="0" selected>选择楼层</option>
	       </select>
	       <select class="select_duration">
  			 <option value="0" selected>选择时间区间</option>
  			 <option value="600" >十分钟内</option>
  			 <option value="1800">半小时内</option>
	         <option value="3600">一小时内</option>
	         <option value="21600">6小时内</option>
	         <option value="43200">12小时内</option>
	         <option value="86400">24小时内</option>
	         <option value="604800">1周内</option>
	         <option value="-1">自选区间</option>
	       </select>
	       <span class="custom_duration" style="display:none">
	       <input type="text" id="time_start" startTimestamp="" placeholder="YYYY-MM-DD hh:mm:ss" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})">
	       - 
	       <input type="text" id="time_end" endTimestamp="" placeholder="YYYY-MM-DD hh:mm:ss" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})">
	       </span>
	         <select class="step_size">
	       		<option value="0" selected>选择步长</option>
	       		<option value="600" >十分钟</option>
                <option value="1800">半小时</option>
                <option value="3600" >一小时</option>	      		
	          </select>
             <!--  <select class="play_speed">
	       		<option value="0" selected="selected">选择播放速度</option>
	       		<option value="100" >0.1s</option>
                <option value="500">0.5s</option>
                <option value="1000">1s</option>
                <option value="1500">1.5s</option>
                <option value="2000">2s</option>	      		
	          </select> -->
	      <button type="button" id="btnSearchTrackPlayback" class="am-btn am-btn-primary am-btn-xs"><span class="am-icon-search"></span> 查询多人轨迹</button>
	       <button type="button" id="btnAbortTrackPlayback" class="am-btn am-btn-primary am-btn-xs"><span class="am-icon-search"></span> 取消播放</button>
	      
	      
	      <span style="float: left;">设置播放速度:</span>
	      	<div class="ratings_bars">	      	
			<span id="title0">0</span>
			<span class="bars_10">0</span>
			<div class="scale" id="bar0">
				<div></div>
				<span id="btn0"></span>
			</div>
			<span class="bars_10">10</span>			
		</div>	      
		
	       <div style="float:right;display:none">
	       		<span>人数：<label id="person-value">0</label></span>&nbsp;&nbsp;
	       		<span>时间：<label id="time-value"></label></span>
	       </div>
  		</div>
    </div>
    <div class="am-progress am-margin-bottom-xs" style="height:1px;">
	 <div class="am-progress-bar" style="width: 0%" id="play_progress"></div>
	</div>
    <div class="am-cf am-padding" style="">
        <div class="am-panel am-panel-default">
          <div class="am-panel-hd am-cf" data-am-collapse="{target: '#collapse-panel-2'}">多人轨迹图<span class="am-icon-chevron-down am-fr" ></span></div>
          <div class="am-panel-bd am-collapse am-in" id="collapse-panel-2">
			<!-- <div style="overflow:auto;width:100%;background:none;">  -->
				<div id="trackWithHeatmap"><p align="center">No Available Data</p></div>
			<!--  </div>  -->
          </div>
        </div>
    </div>
  </div>
  <!-- content end -->

</div>
<%@ include file="/WEB-INF/public/foot.jsp" %>
<script>
var page="customertrack	";
$(function(){
	$("#"+page).attr("class","current");
	$('#btnSearchTrackPlayback').click(function() {
		var data = {
			"building" : building,
		};
		if(!getQueryTimeDuration(this,data)){
			return;
		}
		var floor = $('#select_floor option:selected').val();
		if (floor == '0') {
			layer.alert("请选择楼层", {
				icon : 0
			});
			return;
		}
		data['floor'] = floor;
		/* var playIndex = document.querySelector('.play_speed').selectedIndex;
		var playSpeedElem = document.querySelector('.play_speed');
		var playSpeed = playSpeedElem.options[playIndex].value;
		if(playSpeed == 0){
			playSpeed = 10;
		} */
		var stepSize = document.querySelector(".step_size");
		var sindex = stepSize.selectedIndex;
		var svalue = stepSize.options[sindex].value;		
		
		if(svalue == 0){
			svalue = 3600;
		}
	//	playSpeed = parseInt(playSpeed);
		svalue = parseInt(svalue);
		data['stepSize'] = svalue;
		//console.log("stepSize:"+svalue +";"+"playSpeed:"+playSpeed);
    	getTrackPlayback(data,drawSpeed);
		
	});
	
	$('#btnAbortTrackPlayback').click(function() {
		abortTrackPlayback();
	});
		
});

/**
 * 滑动杆相关
 */
scale = function (btn, bar, title) {
	this.btn = document.getElementById(btn);
	this.bar = document.getElementById(bar);
	this.title = document.getElementById(title);
	this.step = this.bar.getElementsByTagName("DIV")[0];
	this.init();
	var rate = (drawSpeed-10.0)/(1000-10);
	var pos = Math.ceil(100*rate)
	var x = Math.ceil(this.bar.clientWidth*rate);
	this.ondrag(pos,x);
	this.btn.style.left = x + 'px';
	console.log(drawSpeed)
};
scale.prototype = {
		init: function () {
			var f = this, g = document, b = window, m = Math;
			f.btn.onmousedown = function (e) {
				var x = (e || b.event).clientX;
			//	console.log("e"+e+"b"+b.event)
				var l = this.offsetLeft;
				var max = f.bar.offsetWidth - this.offsetWidth;
				g.onmousemove = function (e) {
					var thisX = (e || b.event).clientX;
					var to = m.min(max, m.max(-2, l + (thisX - x)));
					f.btn.style.left = to + 'px';
					f.ondrag(m.round(m.max(0, to / max) * 100), to);
					b.getSelection ? b.getSelection().removeAllRanges() : g.selection.empty();
				};
				g.onmouseup = new Function('this.onmousemove=null');
			};
		},
		ondrag: function (pos, x) {
			this.step.style.width = Math.max(0, x) + 'px';
			if(x<0){
				x=0;
			}
			var speed = 10+(1000-10)*(x*1.0/this.bar.clientWidth);
			drawSpeed = Math.ceil(speed);
			//console.log(drawSpeed);
			this.title.innerHTML = pos / 10 + '';
		}
	}
new scale('btn0', 'bar0', 'title0');
</script>