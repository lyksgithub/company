<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/public/head.jsp" %>
<%@ include file="/WEB-INF/public/top.jsp" %>

<style>
    .mallLevelDisplay{
        width: 100%;
        height:300px;
    }
    .mallSexRatio{
        position: absolute;
        height:300px;
        width: 500px;
        padding-left: 100px;
        /*background: whitesmoke;*/
    }
    .phoneBand{
        position: absolute;
        height: 300px;
        width: 60%;
        /*background:indianred;*/
        margin-left: 501px;
    }
    .maleFemaleRate{
        height: 250px;
        padding-top: 50px;
        margin-right: -70px;
        float: left;
    }
    .mallSexRate{
        height: 250px;
        padding-top: 120px;
        float: left;
    }
    .Line{
        border: solid;
        margin: 20px;
        color: whitesmoke;
    }
    .border{
        position: relative;
        /*border: solid;
        color: #6495ED;*/
        height: 40%;
        width: 26%;
        margin-left: 50px;
        margin-bottom: 50px;
        float: left;
    }
    .oldCustomerImg,.shoppingIndexImg,.InshopTimeIndexImg,.avgAgeImg{
        position: absolute;
        margin-top: 50px;
        margin-left:  30px;
        color: #272636;
        float:left;
    }
    .oldCustomerImg{
        position: absolute;
        margin-top: 120px;
        margin-left:  30px;
        color: #272636;
        float:left;
    }
    #oldCustomerNumber,#avgAgeNumber,#shoppingIndexNumber,#InshopTimeNumber{
        position:absolute;
        margin-left: -30px;
    }
    #mallMale{
        font-family:"arial black" ;
        color:rgb(39, 157, 179);
    }
    #mallFemale{
        font-family:"arial black" ;
        color:rgb(229, 89, 80);
    }
</style>

<div class="am-cf admin-main">
    <%@ include file="/WEB-INF/public/menu.jsp" %>
    <!-- content start -->
    <div class="admin-content" style="min-height:800px;height:800px;">
        <div class="am-cf am-padding am-u-sm-12" style="height:800px;overflow:auto">

            <div class="mallLevelDisplay">
			<strong>商场级别 </strong>
                <div class="mallSexRatio">
                    <div class="mallSexRate" ><span id="mallMale" style="font-size:300%;">0%</span></div>
                    <div class="maleFemaleRate">
                        <img width="70%" src= "static/images/maleFemale.png"/>
                    </div>
                    <div class="mallSexRate"><span id="mallFemale" style="font-size:300%;">0%</span></div>
                </div>

                <div class="phoneBand">
                    <div id="mallPhoneBand" style="width:100%; height:100%;margin-left: 90px;margin-right: 50px;"></div>
                </div>

			
           
            </div>
	

            <div class="Line"></div>
            
            <div><strong>商铺级别 </strong></div>
            <select  id="shopList"  >
            </select>

            <div class="shopLevelDisplay" style="overflow: hidden;height: 65%">
            <div class="pieCharts" style="width: 100%;height: 50%">
                <!-- <div class="border" id="shopSexRatio" style="width: 45%;float:left;"> -->
                    <div id="shopSexRate" style="width:45%; height:100%;float:left;"></div>
            <!--     </div> -->
           <!--      <div class="border" id="shopKidRatio" style="width:45%;float:left;"> -->
                    <div id="shopKidRate" style="width:45%; height:100%;float:left;"></div>
             <!--    </div> -->
                <div style="clear: both"></div>
          </div>
          
          <div class="PSImage" style="width: 100%;height: 50%;overflow: hidden;margin-left: 50px; margin-top: 20px;" >       
          <!-- 老顾客比例-->
   			 <div class="border" id="shopOldcustomerRate" style="float: left;width:20%;height: 100%">
   			 	<span id="oldCustomerNumber"  style="font-size: 22px;white-space: nowrap;">店铺老顾客比例：无数据</span>
       		 	<div class="oldCustomerImg" style="margin-top: 40px">
            	<img src="static/images/oldCustomer.png" />
        		</div>       		
   			 </div>  
			<div class="border" id="shopAvgAge" style="float: left;width: 20%;height: 100%">
				
					<span id="avgAgeNumber"  style="font-size: 22px;white-space: nowrap;">店铺平均年龄指数：无数据</span>
	
				<div class="avgAgeImg" style="margin-top: 40px">
					<img src="static/images/avgAge.png" />
				</div>
				
			</div>
			<div class="border" id="shopShoppingIndex" style="float: left;width: 20%;height: 100%">
				
					<span id="shoppingIndexNumber"  style="font-size: 22px;white-space: nowrap;">店铺平均消费指数：无数据</span>
		
				<div class="shoppingIndexImg" style="margin-top: 40px">
					<img src="static/images/shoppingIndex.png" />
				</div>
				
			</div>

			<div class="border" id="shopAvgInshopTime" style="float: left;width:20%;height: 100%">
			
					<span id="InshopTimeNumber" style="font-size: 22px;white-space: nowrap;">平均驻店时间：无数据</span>
	
				<div class="InshopTimeIndexImg" style="margin-top: 40px">
					<img src="static/images/inshopTime.png" />
				</div>				
			</div>
		</div>    
                 </div>           
            </div>
        </div>
    </div>
    <!-- content end -->
</div>

<%@ include file="/WEB-INF/public/foot.jsp" %>

<script type="text/javascript">
    $(function(){
        getMarketData();
    });

    function getMarketData() {
        $.ajax({
            type : "POST",
            url : basePath + "/UserPortrait?" + new Date(),
            data : {
                "data" : "marketData",
                "building": building
            },
            contentType : "application/x-www-form-urlencoded; charset=UTF-8",
            dataType : "json",
            success: function (result) {

                //显示商场级别性别比
                $("#mallMale").html(Math.round(result.maleProportion*100.00) + "%");
                $("#mallFemale").html((100 - Math.round(result.maleProportion*100.00))+ "%");

                var phoneBrand = [];
                var phoneNumber = [];
                
                var phonebrand = result.phoneBrand;
                
                for(var i in phonebrand){
                	 phoneBrand.push(i);
                     phoneNumber.push(phonebrand[i]);
                }
                
                mallPhone(phoneBrand,phoneNumber);

                var shopIdAndShopName = result.shopIdAndShopName;
                for(var i in shopIdAndShopName){
                	
                	 $("#shopList").append("<option value = "+i+" >"+shopIdAndShopName[i]+"</option>");
                }
                  
                getShopData($("#shopList").val());
   
                
            },
            //调用出错执行的函数
            error : function () {
                console.log("调用出错");
            }
        });
    }


    function mallPhone(phoneBand,phoneNumber){
        var phoneBandChart = echarts.init(document.getElementById('mallPhoneBand'));
        var d = new Date();
        var date = d.getFullYear()+"年"+(d.getMonth()+1)+"月"+d.getDate()+"日";
        option = {
            title: {
                text: '手机品牌分布',
                subtext: building
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            legend: {
                data: [date]
            },
            grid: {
                left: '6%',
                right: '6%',
                bottom: '1%',
                containLabel: true
            },
            xAxis: {
                type: 'value',
                boundaryGap: [0, 0.01]
            },
            yAxis: {
                type: 'category',
                data: ['Apple','Samsung','Xiaomi','HTC','HUAWEI','OPPO','VIVO','Sony']
            },
            series: [
                {
                    name: date,
                    type: 'bar',
                    data: [113, 22,34, 52, 152, 35,40,3]
                }
            ]
        };
        option.yAxis.data = phoneBand;
        option.series[0].data = phoneNumber;
        phoneBandChart.setOption(option, true);   //为echarts对象加载数据
        window.addEventListener("resize",function(){
            phoneBandChart.resize();
        });
    }
    mallPhone();


    function getShopData(shopId) {
        $.ajax({
            type : "POST",
            url : basePath + "/UserPortrait?" + new Date(),
            data : {
                "data" : "GANT",
                "shopId": shopId
            },
            contentType : "application/x-www-form-urlencoded; charset=UTF-8",
            dataType : "json",
            success: function (result) {

                $("#oldCustomerNumber").html("店铺老顾客比例："+Math.round(result.repeat_customer_ratio)  + "%");
                $("#avgAgeNumber").html("顾客平均年龄指数："+result.average_age);
                $("#shoppingIndexNumber").html("店铺平均消费指数："+result.average_consumption);
                $("#InshopTimeNumber").html("平均驻留时间："+result.average_residence_time + "s");

                var maleValue = Math.round(result.gender_ratio*100) ;
                var femaleValue = 100 - maleValue;
           
                shopSexRate(maleValue,femaleValue);
                     					          
                var haveBaby = Math.round(result.havingbaby_ratio*100);
                var notHaveBaby = 100 - haveBaby;
              
                shopKidRate(haveBaby,notHaveBaby)

            },
            //调用出错执行的函数
            error : function () {
                console.log("调用出错");
                shopSexRate(0,0);

                shopKidRate(0,0);
            }
        });
    }



    function shopSexRate(maleValue,femaleValue){//店铺性别比例
        var phone = echarts.init(document.getElementById('shopSexRate'));
        option = {
            title : {
                text: '性别比例',
                subtext: '店铺',
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} %"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: ['男','女']
            },

            series : [
                {
                    name: '访问来源',
                    type: 'pie',
                    radius : '55%',//饼图的大小
                    center: ['50%', '60%'],
                    data:[
                        {value:335, name:'男'},
                        {value:310, name:'女'},

                    ],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        option.series[0].data[0].value = maleValue;
        option.series[0].data[1].value = femaleValue;
        phone.setOption(option, true);   //为echarts对象加载数据
        window.addEventListener("resize",function(){
            phone.resize();
        });
    }
    shopSexRate();

    function shopKidRate(haveBaby,notHaveBaby){//店铺有无小孩比例
        var phoneBand = echarts.init(document.getElementById('shopKidRate'));
        option = {
            title : {
                text: '有无小孩比例',
                subtext: '店铺',
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c}%"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: ['有小孩','不确定']
            },

            series : [
                {
                    name: '访问来源',
                    type: 'pie',
                    radius : '55%',//饼图的大小
                    center: ['50%', '60%'],
                    data:[
                        {value:135, name:'有小孩'},
                        {value:310, name:'不确定'},

                    ],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        option.series[0].data[0].value = haveBaby;
        option.series[0].data[1].value = notHaveBaby;
        phoneBand.setOption(option, true);   //为echarts对象加载数据
        window.addEventListener("resize",function(){
            phoneBand.resize();
        });
    }
    shopKidRate();

    
    
    $(document).ready(function(){
    //  直接调用 参数代表分钟数,可以有一位小数;
         timeUserFun(30);
     });
    
    
    $("#shopList").change(
    	function(){
    		getShopData($(this).val())
    		
    	}		
    )
    
</script>