<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/public/head.jsp" %>
<%@ include file="/WEB-INF/public/top.jsp" %>
   
<center>
 <fieldset>
        <legend>用户登录</legend> <br />
        <!-- form 表单的action 属性值要和配置在web.xml文件中的servlet的url-pattern相同 -->
        <!-- <form action="login" method="post" id="login"> -->
            邮箱：<input id="email" type="text" name="email"  /> <br /> <br /> 
            密码：<input id="password" type="password" name="password"  /> <br /> <br /> 
            <input type="submit" value="登录"  onclick="getData()"/>
      <!--  </form> -->
    </fieldset>
</center>

<%@ include file="/WEB-INF/public/foot.jsp" %>


<script>



var modulus = null;
var exponent = null;

$(document).ready(function () {
    $.ajax({
        //提交数据的类型 POST GET
        type: "GET",
        //提交的网址
      
        url: "http://location.gene-point.com/platform/netdeveloper/netlbsshowlogin?action=get_rsa_public_keypair",
        //返回数据的格式
        dataType: "jsonp",

        //成功返回之后调用的函数             
        success: function (data) {          
            modulus = data.modulus;
            exponent = data.exponent; 
        },
        //调用出错执行的函数
        error: function () {
           
        }
    });
});

function getData() {
    var email = document.getElementById("email").value;
    var password = document.getElementById("password").value;
    var key = RSAUtils.getKeyPair(exponent, '', modulus);
    var encryptedPwd = RSAUtils.encryptedString(key, password);
    var loginData = {"email": email, "encryptedPwd": encryptedPwd};
    $.ajax({
        //提交数据的类型 POST GET
        type: "POST",
        //提交的网址
        url: basePath + "/auth/login",
        data: loginData,
        //返回数据的格式
        dataType: "json",

        //成功返回之后调用的函数             
        success: function (data) {
        	if (data.status !== 200) {
        		layer.alert(data.message, {
                    icon: 0
                });
            } else {
            	 location.href = basePath + "/buildings.jsp";
            }           
        },
        //调用出错执行的函数
        error: function () {

        }
    });
}


</script>
