<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/public/head.jsp" %>
<%@ include file="/WEB-INF/public/top.jsp" %>

<div class="am-cf admin-main">
  <%@ include file="/WEB-INF/public/menu.jsp" %>
   <!-- content start --> 
  <div class="admin-content">
 
  <div class="mapshow" style="clear: both;">
  	  <div class="am-cf am-padding am-u-sm-12"> 
  		   <select id="select_floor">
  			 <option value="0" selected>选择楼层</option>
	       </select>
	       <button type="button" id="btnChangePersonsFloor" class="am-btn am-btn-primary am-btn-xs"><span class="am-icon-refresh"></span> 切换</button>
  		 </div>
   <div class="am-progress am-margin-bottom-xs" style="height:1px;">&nbsp;</div>
   <div id="floormap" class="am-u-md-9" style="width:100%; background:none;"></div>	 
  </div>
  
   <div class="am-progress am-margin-bottom-xs" style="height:1px;clear: both;">&nbsp;</div>  	 
   <div class="chosedate" style="clear: both;">
    <div id="floorcharts" class="am-u-md-9" style="float: left;"></div>
    <div id="select_datetime" class="am-u-md-9" style="float: left;">
       选择日期：<input type="text" id="datepicker">
   <!-- <span class="custom_duration"><input type="text" id="selectTime" placeholder="YYYY-MM-DD" onclick="laydate({istime: true, format: 'YYYY-MM-DD'})"></span> -->
   <button type="button" id="btnSearchCount" class="am-btn am-btn-primary am-btn-xs"><span class="am-icon-search"></span>查询</button></div>
    </div>
   
    <div id ="charts" style="clear: both;">  
   <div class="am-cf am-padding am-margin-sx">
	    <div class="apChart" id="main" style="width:100%;"></div>
    </div>
    </div>
  </div>

  <!-- content end -->
</div>

<%@ include file="/WEB-INF/public/foot.jsp" %>
 

<script>

    page="ap_"+page;
    var realtimeHandle;
 	var mapScale = {};
	var mapWidth = {};
	var mapHeight = {};
	var floor,time ;
	var myDate,month,day;
	
	$(function(){
		
		var realtimeHandle = null;
		$("#" + page).attr("class", "current");
		
		//加载楼层地图
		floor = $('#select_floor option:selected').val();
		$("#floormap").append('<p id="realfloor">当前楼层：'+floor+'</p><div class="custumermap" id="customer_map"><canvas id="canvas">您的浏览器不支持canvas!</canvas></div>');		
		$("#floorcharts").append('<p id="realfloor">当前楼层：'+floor+'</p>');			    
		
		var requestData = {
				"building" : building,
				"floor" : floor
			};
	    showAPStatus(floor,requestData);
	
	    //绘制ap状态图表
	    $( "#datepicker" ).datepicker();
	     myDate = new Date();
	     month = myDate.getMonth()+1;
	     day = myDate.getDate();
	    if(month<10){
	        month = '0'+month;
	    }
	    if(day < 10){
	    	day = '0'+ day;
	    }
	    time = (month+'/'+day+'/'+myDate.getFullYear());
	    
	    var data = {
	    	"building": building,
	    	"floor" : floor,
	    	"time" :time
	    }
	    getAPchartsdata(data);
	    

	});
	
function showAPStatus(floor,requestData){
	 var imgMarker = new Image();
     imgMarker.src = basePath + "/static/images/marker.png"// 设置覆盖物
     imgMarker.onload = function(){		   
	 			loadAloneMap(floor,imgMarker,requestData);	
	     }
}
	function loadAloneMap(curFloor,imgMarker,requestData){
		 getMap(curFloor);
		    var img = new Image();
		    img.src = imgSrcHead + json[posBuilding].floors[posFloor].mapUrl;// 设置地图
		img.onload = function(){
			var width = img.width;
			var height = img.height;
			var maxWidth = $('#floormap').width();
			if(width>maxWidth){
				mapScale[curFloor] = maxWidth*1.0/width;
			}else{
				mapScale[curFloor] = 1.0;
			}
		
			width *= mapScale[curFloor];
			height *= mapScale[curFloor];
			
			mapWidth[curFloor] = width;
			mapHeight[curFloor] =height;
			
			$("#customer_map").css("background","url("+img.src+") no-repeat");
			$("#customer_map").css("width",width);
			$("#customer_map").css("height",height);
			//关键设置，不然图片不会等比例缩放
			$("#customer_map").css("background-size","contain");				   
				getAllApPosition(requestData,mapWidth,mapHeight,imgMarker,mapScale);	
				 clearInterval(realtimeHandle);
				realtimeHandle = setInterval(function(){
					getAllApPosition(requestData,mapWidth,mapHeight,imgMarker,mapScale);
				}, 3000);	
		}		
	}
	
	$('#btnChangePersonsFloor').click(function(){
		clearInterval(handle);
		floor = $('#select_floor option:selected').val();
		time = (month+'/'+day+'/'+myDate.getFullYear());
		$('#datepicker').val('');
		$('#realfloor').html("当前楼层："+floor);
		$("#floorcharts").html("当前楼层："+floor);	
			if (floor == '0'){
				layer.alert("请选择楼层", {
					icon : 0
				});
				return;
			}
			requestData = {
        		 "building" : building,
        		 "floor" : floor
         };        
			var data = {
			    	"building": building,
			    	"floor" : floor,
			    	"time" :time
			    }			
         showAPStatus(floor,requestData);
	     getAPchartsdata(data);
  
         
	});
	
	$('#btnSearchCount').click(function(){
		time = $('#datepicker').val();
		if(time == ''){
			alert("请选择正确的查询日期！");
			return;
		}
		 var data = {
			    	"building": building,
			    	"floor" : floor,
			    	"time" :time
			    }
		 getAPchartsdata(data);
       
	});

function getAPchartsdata(data){	
	 $.ajax({
		type : "GET",
		dataType : "json",
		url : basePath + "/ap/get_ap_echarts",
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data : {
			"building" : data.building,
			"floor"    : data.floor,
	    	"time"     : data.time
		},
		success : function(result){
			if (result.status !== 200){
				layer.alert(result.message, {
					icon : 0
				});
			} else {	
				
			var data2 = result.data;
			var APs, hours,datas,num,max;
			max = data2.max;
			APs = data2.aps;
			hours = data2.hours;
			datas = data2.data;
			num = data2.num;			
			document.getElementById("main").style.height = (num*110)+'px';
		    var app = echarts.init(document.getElementById('main'));
		    window.onresize =function(){ app.resize()}; 
			apStatuscharts(hours,APs,datas,num,max,app);
		}
		},
		error : function(){
			layer.alert("网络繁忙，请稍后再试！", {
				icon : 0
			});
		}
	}); 
}
	
	
function apStatuscharts(hours,APs,data,num,max,app){  	
	//ap的echarts图表
	app.resize();
	option = {
		    tooltip: {
		        position: 'top'
		    },
		    title: [],
		    singleAxis: [],
		    series: [],
		   
		};

		echarts.util.each(APs, function (day, idx) {
			option.title.push({
		        textBaseline: 'middle',
		        top: 30+((idx)*100),
		        text: day
		    });
		    option.singleAxis.push({
		        left: '350',
		        type: 'category',
		        boundaryGap: false,
		        data: hours,
		        top: ((idx)*100),
		        height:30,
		        axisLabel: {
		            interval:20
		        }
		    });
		    option.series.push({
		        singleAxisIndex: idx,
		        coordinateSystem: 'singleAxis',
		        type: 'scatter',
		        data: [],
		        symbolSize: function (dataItem) {	        	
		        	return Math.log(dataItem[1]) * 8;    //此处控制气泡半径大小
		        }
		    }); 
		});

		echarts.util.each(data, function (dataItem) {
		    option.series[dataItem[0]].data.push([dataItem[1], dataItem[2]]);
		});
		app.setOption(option, true);
		
		
}

$(document).ready(function(){
//  直接调用 参数代表分钟数,可以有一位小数;
     timeUserFun(30);
 });
</script>