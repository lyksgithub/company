<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/public/head.jsp"%>
<%@ include file="/WEB-INF/public/top.jsp"%>

<div class="am-cf admin-main">
	<%@ include file="/WEB-INF/public/menu.jsp"%>

	<!-- content start -->
	<div class="admin-content" style="overflow: auto; z-index: 999">
		<div class="am-cf am-padding">
			<select id="select_floor">
				<option value="0" selected>选择楼层</option>
			</select>
			<button type="button" id="btnChangePersonsFloor"
				class="am-btn am-btn-primary am-btn-xs">
				<span class="am-icon-refresh"></span> 切换
			</button>
			<button type="button" id="btnPlusMap"
				class="am-btn am-btn-primary am-btn-xs">
				<span class="am-icon-search-plus"></span> 放大
			</button>
			<button type="button" id="btnMinusMap"
				class="am-btn am-btn-primary am-btn-xs">
				<span class="am-icon-search-minus"></span> 缩小
			</button>
		</div>
		<div class="am-progress am-mar	gin-bottom-xs" style="height: 1px;">&nbsp;</div>
		<div class="heatmap_list"
			style="overflow: auto; width: 100%; height: 700px; background: none;"></div>
	</div>
	<!-- content end -->

</div>
<%@ include file="/WEB-INF/public/foot.jsp"%>
<script>
	page = "real_heatmap";
	var handle;
	$(function() {
		$("#" + page).attr("class", "current");
		var floor = $('#select_floor option:selected').val();																		
		$(".heatmap_list").append('<p id="realfloor">当前楼层：'+ floor+ '</p><div class="heatmap_real" id="heatmap_real"><canvas id="canvas">您的浏览器不支持canvas!</canvas></div>');
		var data = {
			"building" : building,
			"floor" : floor
		};	
		
		clearInterval(handle);
		//默认加载F1热力图
		loadOneFloorMapByRecursion(floor);			
		drawRealHeatMap(floor);
	//点击切换楼层		
		$('#btnChangePersonsFloor').click(function() {
			floor = $('#select_floor option:selected').val();			
			$('#realfloor').html("当前楼层：" + floor);
			if (floor == '0') {
				layer.alert("请选择楼层", {
					icon : 0
				});
				return;
			}
			data = {
				"building" : building,
				"floor" : floor
			};
			clearInterval(handle);
			loadOneFloorMapByRecursion(floor);
			drawRealHeatMap(floor);
		});
	});

	function loadOneFloorMapByRecursion(curFloor){
	    getMap(curFloor);
	    var img = new Image();
	    img.src = imgSrcHead + json[posBuilding].floors[posFloor].mapUrl;// 设置地图
	    var data = {
	        "building" : building,
	        "floor" :curFloor
	    };
	    img.onload = function(){
	        var width = img.width;
	        var height = img.height;
	        var maxWidth = $('.heatmap_real').width();
	        if(width>maxWidth){
	            mapScale[curFloor] = maxWidth*1.0/width;
	        }else{
	            mapScale[curFloor] = 1.0;
	        }
	        width *= mapScale[curFloor];
	        height *= mapScale[curFloor];
	        $("#heatmap_real").css("background","url("+img.src+") no-repeat");
	        $("#heatmap_real").css("width",width*canvasScale);
	        $("#heatmap_real").css("height",height*canvasScale);
	        //关键设置，不然图片不会等比例缩放
	        $("#heatmap_real").css("background-size","contain");
	        getRealtimeHeatmap(data,img,"heatmap_real");
	    }
	}
	
	/**
	 * 
	 * @param requestData
	 * @param img
	 * @param container
	 *            容器ID
	 */
	function getRealtimeHeatmap(requestData,img,containerId){
	    $.ajax({
	        type : "GET",
	        dataType : "json",
	        url : basePath + "/heatmap/get_heatmap_realtime",
	        contentType : "application/x-www-form-urlencoded; charset=UTF-8",
	        data : {    
	            "building" : requestData.building,
	            "floor"	   : requestData.floor
	        },
	        success : function(result){
	        if (result.status !== 200){
	                clearInterval(heatmapHandles[requestData.floor]);
	                layer.alert("暂无实时数据", {
	                    icon : 0
	                });
	            } else { 
	                $('#'+containerId).empty();
	                var heatmap = h337.create({
	                    container : document.getElementById(containerId)
	                });
	                var data = result.data;
	                var count = 0;
	                var heatmapData = [];
	                for ( var i in data){
	                	heatmapData.push({
	                        x : parseInt(data[i].corx*mapScale[requestData.floor]*canvasScale),
	                        y : parseInt(data[i].cory*mapScale[requestData.floor]*canvasScale),
	                        value : 3,
				            radius : 90
	                    });
	                    count++;
	                }
	                heatmap.setData({
	                    max : count,
	                    data : heatmapData
	                });
	          }
	        },
	        error : function(){
	            clearInterval(heatmapHandles[requestData.floor]);
	            layer.alert("网络繁忙，请稍后再试！", {
	                icon : 0
	            });
	        }
	    });
	}

	function drawRealHeatMap(floor){
		handle = setInterval(function(){loadOneFloorMapByRecursion(floor)}, 10000)
	}
</script>