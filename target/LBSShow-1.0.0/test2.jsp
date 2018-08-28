<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/public/head.jsp" %>
<%@ include file="/WEB-INF/public/top.jsp" %>
<%@ include file="/WEB-INF/public/foot.jsp" %>
<script>
$(function(){
	$.ajax({
		type : "GET",
		dataType : "json",
		url : basePath + "/test2?" + new Date(),
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data : {
			"type" : "ajax",
		},
		success : function(result) {
			console.log(result);
		},
		error : function() {
			layer.close(processLayout);
			layer.alert("请求出错...", {icon : 0});
		}
	});
});
</script>
