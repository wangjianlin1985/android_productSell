<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%>
<%@ page import="com.chengxusheji.domain.CountyInfo" %>
<%@ page import="com.chengxusheji.domain.TownInfo" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的CountyInfo信息
    List<CountyInfo> countyInfoList = (List<CountyInfo>)request.getAttribute("countyInfoList");
    //获取所有的TownInfo信息
    List<TownInfo> townInfoList = (List<TownInfo>)request.getAttribute("townInfoList");
    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>添加销售人员</TITLE> 
<STYLE type=text/css>
BODY {
    	MARGIN-LEFT: 0px; BACKGROUND-COLOR: #ffffff
}
.STYLE1 {color: #ECE9D8}
.label {font-style.:italic; }
.errorLabel {font-style.:italic;  color:red; }
.errorMessage {font-weight:bold; color:red; }
</STYLE>
 <script src="<%=basePath %>calendar.js"></script>
<script language="javascript">
/*验证表单*/
function checkForm() {
    var telephone = document.getElementById("sellPerson.telephone").value;
    if(telephone=="") {
        alert('请输入手机号!');
        return false;
    }
    var password = document.getElementById("sellPerson.password").value;
    if(password=="") {
        alert('请输入登录密码!');
        return false;
    }
    var name = document.getElementById("sellPerson.name").value;
    if(name=="") {
        alert('请输入姓名!');
        return false;
    }
    return true; 
}
 </script>
</HEAD>

<BODY background="<%=basePath %>images/adminBg.jpg">
<s:fielderror cssStyle="color:red" />
<TABLE align="center" height="100%" cellSpacing=0 cellPadding=0 width="80%" border=0>
  <TBODY>
  <TR>
    <TD align="left" vAlign=top >
    <s:form action="SellPerson/SellPerson_AddSellPerson.action" method="post" id="sellPersonAddForm" onsubmit="return checkForm();"  enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">

  <tr>
    <td width=30%>手机号:</td>
    <td width=70%><input id="sellPerson.telephone" name="sellPerson.telephone" type="text" /></td>
  </tr>

  <tr>
    <td width=30%>登录密码:</td>
    <td width=70%><input id="sellPerson.password" name="sellPerson.password" type="text" size="20" /></td>
  </tr>

  <tr>
    <td width=30%>姓名:</td>
    <td width=70%><input id="sellPerson.name" name="sellPerson.name" type="text" size="10" /></td>
  </tr>

  <tr>
    <td width=30%>所在县市:</td>
    <td width=70%>
      <select name="sellPerson.countyId.cityId">
      <%
        for(CountyInfo countyInfo:countyInfoList) {
      %>
          <option value='<%=countyInfo.getCityId() %>'><%=countyInfo.getCityName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>所在乡镇:</td>
    <td width=70%>
      <select name="sellPerson.townId.townId">
      <%
        for(TownInfo townInfo:townInfoList) {
      %>
          <option value='<%=townInfo.getTownId() %>'><%=townInfo.getTownName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr bgcolor='#FFFFFF'>
      <td colspan="4" align="center">
        <input type='submit' name='button' value='保存' >
        &nbsp;&nbsp;
        <input type="reset" value='重写' />
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
