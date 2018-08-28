<%@page import="org.omg.PortableInterceptor.USER_EXCEPTION"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<body>
<!--[if lte IE 9]>
<p class="browsehappy">你正在使用<strong>过时</strong>的浏览器，Amaze UI 暂不支持。 请 <a href="http://browsehappy.com/" target="_blank">升级浏览器</a>
  以获得更好的体验！</p>
<![endif]-->

<header class="am-topbar admin-header">
  <div class="am-topbar-brand">

<strong>子午快线</strong> <small>位置可视化</small>
&nbsp;&nbsp;&nbsp;&nbsp;<span>当前场景：</span><strong><%=buildingAlias%></strong>
  </div>

  <button class="am-topbar-btn am-topbar-toggle am-btn am-btn-sm am-btn-success am-show-sm-only" data-am-collapse="{target: '#topbar-collapse'}"><span class="am-sr-only">导航切换</span> <span class="am-icon-bars"></span></button>

  <div class="am-collapse am-topbar-collapse" id="topbar-collapse">

    <ul class="am-nav am-nav-pills am-topbar-nav am-topbar-right admin-header-list">
      <li><a href="<%=basePath%>/buildings.jsp"><span class="am-icon-refresh"></span> <span>切换商场</span></a></li>
      <li><a href="javascript:;" id="admin-fullscreen"><span class="am-icon-arrows-alt"></span> <span class="admin-fullText">开启全屏</span></a></li>
      <li><a onclick="logout()"><span class="am-icon-sign-out"></span> <span>注销登陆</span></a></li>
    </ul>
  </div>
</header>

<script type="text/javascript">
function logout(){
	 /*   $.ajax({
	        //提交数据的类型 POST GET
	        type: "POST",
	        //提交的网址
	        url: basePath + "login",
	        data: {
	        	"data":"logout"
	        },
	        //返回数据的格式
	        dataType: "json",

	        //成功返回之后调用的函数             
	        success: function (data) {
	            
	        },
	        //调用出错执行的函数
	        error: function () {
			
	        }
	    }); */
	    $.ajax({
	    	type: "GET",
	    	url: basePath + "/auth/logout",
	    	dataType: "json",
	    	success: function (result) {
	    		layer.alert(result.message, {
					icon : 0
				});
	    		setTimeout(function(){ location.href = basePath + "/login.jsp";},1000)
		    },
		    error: function () {
				layer.alert("网络繁忙，请稍后再试！", {
					icon : 0
				});	
		    }
	    });
}

</script>