<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%> <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<title>双鱼林基于安卓Android销售管理系统-首页</title>
<link href="<%=basePath %>css/index.css" rel="stylesheet" type="text/css" />
 </head>
<body>
<div id="container">
	<div id="banner"><img src="<%=basePath %>images/logo.gif" /></div>
	<div id="globallink">
		<ul>
			<li><a href="<%=basePath %>index.jsp">首页</a></li>
			<li><a href="<%=basePath %>CountyInfo/CountyInfo_FrontQueryCountyInfo.action" target="OfficeMain">县市信息</a></li> 
			<li><a href="<%=basePath %>TownInfo/TownInfo_FrontQueryTownInfo.action" target="OfficeMain">乡镇信息</a></li> 
			<li><a href="<%=basePath %>ProductClass/ProductClass_FrontQueryProductClass.action" target="OfficeMain">产品类别</a></li> 
			<li><a href="<%=basePath %>ProductInfo/ProductInfo_FrontQueryProductInfo.action" target="OfficeMain">产品信息</a></li> 
			<li><a href="<%=basePath %>SellPerson/SellPerson_FrontQuerySellPerson.action" target="OfficeMain">销售人员</a></li> 
			<li><a href="<%=basePath %>SellInfo/SellInfo_FrontQuerySellInfo.action" target="OfficeMain">销售信息</a></li> 
		</ul>
		<br />
	</div> 
	<div id="main">
	 <iframe id="frame1" src="<%=basePath %>desk.jsp" name="OfficeMain" width="100%" height="100%" scrolling="yes" marginwidth=0 marginheight=0 frameborder=0 vspace=0 hspace=0 >
	 </iframe>
	</div>
	<div id="footer">
		<p>双鱼林设计 QQ:287307421或254540457 &copy;版权所有 <a href="http://www.shuangyulin.com" target="_blank">双鱼林设计网</a>&nbsp;&nbsp;<a href="<%=basePath%>login/login_view.action"><font color=red>后台登陆</font></a></p>
	</div>
</div>
</body>
</html>
