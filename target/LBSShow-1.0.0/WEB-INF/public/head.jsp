<%@ page language="java" import="java.util.*,com.genepoint.custom.*" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%
	String path = request.getContextPath();
    String basePath = path;
	HttpSession httpSession = request.getSession();
	String webPage = (String)request.getAttribute("page");
	String building = (String)httpSession.getAttribute("buildingCode");
	String buildingAlias = (String)httpSession.getAttribute("buildingName");
	String floorList = (String)httpSession.getAttribute("floorList");
	String userinfo = (String)httpSession.getAttribute("userinfo");
	if(building==null){
		buildingAlias="N/A";
	}
%>
<!DOCTYPE html>
<html lang="zh-CN" class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
<% 
	out.println("<title>子午快线</title>");
%>
  <meta name="description" content="LBS">
  <meta name="keywords" content="LBS">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <link rel="icon" type="image/png" href="<%=basePath%>/static/assets/i/favicon.png">
  <link rel="apple-touch-icon-precomposed" href="<%=basePath%>/static/assets/i/app-icon72x72@2x.png">
  <meta name="apple-mobile-web-app-title" content="Amaze UI" />
  <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/assets/css/amazeui.min.css"/>
  <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/assets/css/admin.css">
  <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/script/jquery-ui/jquery-ui.min.css">
  <!--  
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
  <link rel="stylesheet" type="text/css" href="<%=basePath%>static/assets/css/progress.css">
  -->
	<script>
	var path="<%=path%>";
	var basePath="<%=basePath%>";
	var page = "<%=webPage%>";
	var building = "<%=building%>";
	var floorConfig = "<%=floorList%>"; 
	var userinfo = <%=userinfo%>;	
	</script>
</head>
