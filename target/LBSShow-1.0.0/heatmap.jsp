<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/public/head.jsp"%>
<%@ include file="/WEB-INF/public/top.jsp"%>
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
<div class="am-cf admin-main">
	<%@ include file="/WEB-INF/public/menu.jsp"%>

	<!-- content start -->
	<div class="admin-content" style="overflow: auto;">
		<div class="am-cf am-padding">
			<div class="am-text-left" style="float: left;">
				<select id="select_floor">
					<option value="0" selected>选择楼层</option>
				</select> 
				<input type="text" id="start" class="laydate-icon"
					placeholder="选择开始时间"> - <input type="text" id="end"
					class="laydate-icon" placeholder="选择结束时间"> 
				<select
					class="select_duration">
					<option value="0">选择范围</option>
					<option value="10">十分钟</option>
					<option value="30" >半小时</option>
					<option value="60" selected>一小时</option>
				</select>
				<button type="button" id="btnSearchHistoryHeatmap"
					class="am-btn am-btn-primary am-btn-xs" data-toggle="tooltip"
					title="只能查询一天的历史热力图" data-placement="top" onclick="searchMap(1000,1)">
					<span class="am-icon-search"></span> 查询历史热力图
				</button>
				<button type="button" id="btnAbortTrackPlayback"
					class="am-btn am-btn-primary am-btn-xs">
					<span class="am-icon-search"></span> 取消播放
				</button>
			</div>			
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
			
		</div>
	<!-- 	<span style="position: relative;left: 61.4%;top: 90px;">快</span><span style="position: relative;left: 76%;top: 90px;">慢</span> -->
		<div class="am-progress am-margin-bottom-xs" style="height: 1px;">
			<div class="am-progress-bar" style="width: 0%" id="play_progress"></div>
		</div>
		<div id="process">当前时段:</div>
		<div id="heatmap"
			style="overflow: auto; width: 100%; height: 700px; background: none;"></div>
	</div>
	<!-- content end -->
</div>
<%@ include file="/WEB-INF/public/foot.jsp"%>
<script>
var page="heatmap_history";
var markValue = 0;
$(function(){
	$("#"+page).attr("class","current");
	var start = {  
		    elem: '#start',  
		    format: 'YYYY/MM/DD hh:mm:ss',  
		    max: laydate.now(-1), //最大日期  
		    istime: true,  
		    istoday: false,  
		    choose: function(datas){  
		         end.max = datas; //开始日选好后，重置结束日的最小日期  
		         end.min = datas //将结束日的初始值设定为开始日  
		    }  
		};  
		var end = {  
		    elem: '#end',  
		    format: 'YYYY/MM/DD hh:mm:ss',   
		    max: laydate.now(-1), 
		    istime: true,  
		    istoday: false,  
		    choose: function(datas){  
		    	start.min = datas;
		        start.max = datas; //结束日选好后，重置开始日的最大日期  
		    }  
		};  
		laydate(start);  
		laydate(end);   
		
    	searchMap(drawSpeed,markValue);
});

 //点击查询
function searchMap(time_duration,value){
	clearInterval(handle1);
	document.getElementById("play_progress").style.width = 0;;
	var floor = $('#select_floor option:selected').val();
	var start_time = $("#start").val();
	var end_time = $("#end").val();		
	start_time =  Date.parse(new Date(start_time));
	end_time = Date.parse(new Date(end_time));
	var duration = $('.select_duration option:selected').val();
	var data = {};
	if(value == 0){
		floor ='F1';
		var data = new Date();
		var month = data.getMonth()+1;
		var day = data.getDate()-1;
		if(month<10){
			month = "0"+month;
		}
		if(day < 10){
			day = "0"+day;
		}
		start_time = '2017/'+month+'/'+day+' '+'09:00:00';
		end_time = '2017/'+month+'/'+day+' '+'22:00:00';	
		start_time =  Date.parse(new Date(start_time));
		end_time = Date.parse(new Date(end_time));		
	}else{
		markValue = 1;
	 	if (floor == '0' || duration == '0' || isNaN(start_time) || isNaN(end_time)) {
		 	layer.alert("请设置正确的查询条件", {
				icon : 0
			});
			return ;
		}
	}
	
//	start_time='1510070400000';
//	end_time='1510156740000';	
	data['start_time'] = start_time;
	data['end_time'] = end_time;
	data['minutes'] = duration;
	data[' buildingId'] = building;		
	data['floor'] = floor;
    getMap(floor);
	var img = new Image();
	img.src = imgSrcHead  + json[posBuilding].floors[posFloor].mapUrl; // 设置地图
	if(img.complete){
		getHistoryHeatmap(data,img,time_duration);
	}else{
		img.onload = function() {
			getHistoryHeatmap(data,img,time_duration);
		}
	} 
}
 
//点击取消播放
$('#btnAbortTrackPlayback').click(function(){
     clearInterval(handle1);
});

function handle(data){
   $('#heatmap').html('');
	draw(data);
}
function draw(data){
var count = 0;
var heatmapData = [];
var heatmap = h337.create({
	container : document.getElementById('heatmap')
});
for ( var i in data){
	var value = data[i].value;
	// value = parseInt(Math.sqrt(value));
	heatmapData.push({
		x : data[i].x*hisScale,
		y : data[i].y*hisScale,
		value : value,
		radius : 100
	});
	count++;
}
heatmap.setData({
	max : count,
	data : heatmapData
});
}

var value;
var hisScale=1.0;
var handle1;
function getHistoryHeatmap(requestData,img,time_duration){	
	$.ajax({
		type : "post",
		dataType : "json",
		url : basePath + "/heatmap/get_history_heatmap",
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data : {
			"building" : building,
			"floor":requestData.floor,
			"start":requestData.start_time,
			"end":requestData.end_time,
			"minutes":requestData.minutes,
		},
		beforeSend : function(){
			processLayout = layer.load(1, {
				shade : false
			}); // 0代表加载的风格，支持0-2
		},
		success : function(result){
			layer.close(processLayout);
			if (result.status !== 200){
				layer.alert(result.message, {
					icon : 0
				});
			} else {
			//	var data = '[[{ "value": 14,"x": 260, "y": 300 },{ "value": 14, "x": 230,"y": 690},{"value": 13, "x": 180, "y": 520}],[{"value": 13,"x": 180,"y": 220},{"value": 13,"x": 180,"y": 720} ,{"value": 13,"x": 180,"y": 720} ],[{"value": 13,"x": 180,"y": 720},{"value": 13,"x": 180,"y": 720} ,{"value": 13,"x": 180,"y": 320}]]';	
			//	data = eval("("+data+")");			     
				data = result.data;						
				var width = img.width;
				var height = img.height;
				$('#heatmap').css("background","url("+img.src+") no-repeat");
				$('#heatmap').css("width",width);
				$('#heatmap').css("height",height);
				$("#heatmap").css("background-size","contain");
				$('#heatmap').html('');
				var index =1;
				var html ="当前时段:";
				document.getElementById("process").innerHTML = html;
				draw(data[0]);
				handle1 = setInterval(function(){				
				    html = "当前时段: "+index;				
					document.getElementById("process").innerHTML = html;
					var leng = document.getElementById("play_progress").style.width;
					leng = leng.substring(0);
					var len = parseInt(leng)+ parseInt(100/data.length);
					document.getElementById("play_progress").style.width = len+"%";
					if(index == data.length -1){						
						document.getElementById("play_progress").style.width = "100%";	
						 layer.alert("绘制完毕", {
			                    icon : 0
			                });	
						clearInterval(handle1);
						return false;
					}else{
						handle(data[index]);
						index++;
					}
				},time_duration);
			}
		},
		error : function(){
			layer.close(processLayout);
			layer.alert("网络繁忙，请稍后再试！", {
				icon : 0
			});
			return 
			
		}
	});
//	var data = '[[{ "value": 14,"x": 260, "y": 300 },{ "value": 14, "x": 230,"y": 690},{"value": 13, "x": 180, "y": 520}],[{"value": 13,"x": 180,"y": 220},{"value": 13,"x": 180,"y": 720} ,{"value": 13,"x": 180,"y": 720} ],[{"value": 13,"x": 180,"y": 720},{"value": 13,"x": 180,"y": 720} ,{"value": 13,"x": 180,"y": 320}]]';	
//	data = eval("("+data+")");
}


/**
 * 滑动杆相关
 */
 var documentflag = false;
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
};
scale.prototype = {
		init: function () {
			var f = this, g = document, b = window, m = Math;
			f.btn.onmousedown = function (e) {
				documentflag = true;
				var x = (e || b.event).clientX;
				var l = this.offsetLeft;
				var max = f.bar.offsetWidth - this.offsetWidth;
				g.onmousemove = function (e) {
					var thisX = (e || b.event).clientX;
					var to = m.min(max, m.max(-2, l + (thisX - x)));
					f.btn.style.left = to + 'px';
					f.ondrag(m.round(m.max(0, to / max) * 100), to);
					b.getSelection ? b.getSelection().removeAllRanges() : g.selection.empty();
				};
				g.onmouseup = new Function('this.onmousemove=null;if(documentflag){searchMap(drawSpeed,markValue)};documentflag=false');
			};
		},
		ondrag: function (pos, x) {
			this.step.style.width = Math.max(0, x) + 'px';
			if(x<0){
				x=0;
			}
			var speed = 10+(1000-10)*(x*1.0/this.bar.clientWidth);
			drawSpeed = Math.ceil(speed);
			this.title.innerHTML = pos / 10 + '';
		}
	}
new scale('btn0', 'bar0', 'title0');
</script>
