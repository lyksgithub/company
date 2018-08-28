<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/public/head.jsp" %>
<%@ include file="/WEB-INF/public/top.jsp" %>

    <div class="am-cf am-padding am-u-sm-12" style="overflow:auto;">
    	<p style="font-size:40px" align="center"><strong>LBS可视化</strong></p>
    	<div align="center">
    		<select id="select_building">
    			<option value="-1">请选择商场</option>
    		</select>
    		<button type="button" class="am-btn am-btn-primary" id="btnChangeBuilding"><span class="am-icon-check"></span> 确 定</button>
    	</div>
    	<div style="position:fixed;bottom:10px;width:100%">
    	<%@ include file="/WEB-INF/public/foot.jsp" %>
    	</div>
    </div>
<script>
$(function(){
	$('#btnChangeBuilding').click(function(){
		var building = $('#select_building option:selected').val();
		if(building=='-1'){
			layer.msg('请选择商场');
		}else{
			switchBuilding(building);
		}
	});
	getAllBuildings();
});

function getAllBuildings(){
	$.ajax({
		type : "GET",
		dataType : "json",
		url : basePath + "/building/get_all_building",
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data : {
		},
		success : function(result) {
			if (result.status !== 200) {
				layer.alert(result.message, {icon : 0});
			} else {
				var list = result.data;
				var html = '<option value="-1">请选择商场</option>';
				console.log(list);
				for(var i =0;i<list.length;i++){
					html+='<option value="'+list[i].buildingCode+'">'+list[i].buildingName+'</option>';
				}
				 $('#select_building').html(html);
			}
		},
		error : function() {
			layer.alert("网络繁忙，请稍后再试！", {
				icon : 0
			});
		}
	});
}

function switchBuilding(requestData){
	$.ajax({
		type : "POST",
		dataType : "json",
		url : basePath + "/building/switch_building",
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data : {
			"building" : requestData
		},
		success : function(result) {
			if (result.status !== 200) {
				layer.alert(result.message, {icon : 0});
			} else {
				var data = result.data;
				console.log(data);
				window.location.href= basePath + "/home.jsp";			
			}
		},
		error : function() {
			layer.alert("网络繁忙，请稍后再试！", {
				icon : 0
			});
		}
	});
}


</script>
<style>
select{
	width:300px;
	height:40px;
	line-height:40px;
}
</style>