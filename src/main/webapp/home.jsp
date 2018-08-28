<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/public/head.jsp" %>
<%@ include file="/WEB-INF/public/top.jsp" %>

<div class="am-cf admin-main">
<%@ include file="/WEB-INF/public/menu.jsp" %>

  <!-- content start -->
  <div class="admin-content" style="min-height:800px;height:800px;">
    <div class="am-cf am-padding am-u-sm-12" style="height:800px;overflow:auto">
    	<div id="realtime_customer_count" style="float:left;width:40%;height:500px;"></div>
    	<div id="realtime_customer_line" style="float:left;width:60%;height:500px;"></div>
    	<div id="shop_list">
    		<table class="am-table am-table-bordered">
			    <thead>
			        <tr>
			            <th>商家名称</th>
			            <th>所在楼层</th>
			            <th>活跃人数</th>
			        </tr>
			    </thead>
			    <tbody>
			        <tr>
			            <td colspan=3><p align="center">No Available Data</p></td>
			        </tr>
			    </tbody>
			</table>
    	</div>
    	
    </div>
  </div>
  <!-- content end -->
</div>
<%@ include file="/WEB-INF/public/foot.jsp" %>
<script src="<%=basePath%>/static/script/common/charts.js"></script>
<script>
var page="home.jsp";
var chartHandle = null;
$(function(){
	$("#"+page).attr("class","current");
	showRealtimeCustomerCount();
	getRealtimeCustomerShopData(building);
	chartHandle = setInterval(function() {
		getRealtimeCustomerShopData(building);
	}, 5000);
	setTimeout(showDynamicLine,1000);
});

$(document).ready(function(){
//  直接调用 参数代表分钟数,可以有一位小数;
     timeUserFun(30);
 });
</script>