<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.SellPerson" %>
<%@ page import="com.chengxusheji.domain.CountyInfo" %>
<%@ page import="com.chengxusheji.domain.TownInfo" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //��ȡ���е�CountyInfo��Ϣ
    List<CountyInfo> countyInfoList = (List<CountyInfo>)request.getAttribute("countyInfoList");
    //��ȡ���е�TownInfo��Ϣ
    List<TownInfo> townInfoList = (List<TownInfo>)request.getAttribute("townInfoList");
    SellPerson sellPerson = (SellPerson)request.getAttribute("sellPerson");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>�޸�������Ա</TITLE>
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
/*��֤��*/
function checkForm() {
    var telephone = document.getElementById("sellPerson.telephone").value;
    if(telephone=="") {
        alert('�������ֻ���!');
        return false;
    }
    var password = document.getElementById("sellPerson.password").value;
    if(password=="") {
        alert('�������¼����!');
        return false;
    }
    var name = document.getElementById("sellPerson.name").value;
    if(name=="") {
        alert('����������!');
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
    <TD align="left" vAlign=top ><s:form action="SellPerson/SellPerson_ModifySellPerson.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>�ֻ���:</td>
    <td width=70%><input id="sellPerson.telephone" name="sellPerson.telephone" type="text" value="<%=sellPerson.getTelephone() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>��¼����:</td>
    <td width=70%><input id="sellPerson.password" name="sellPerson.password" type="text" size="20" value='<%=sellPerson.getPassword() %>'/></td>
  </tr>

  <tr>
    <td width=30%>����:</td>
    <td width=70%><input id="sellPerson.name" name="sellPerson.name" type="text" size="10" value='<%=sellPerson.getName() %>'/></td>
  </tr>

  <tr>
    <td width=30%>��������:</td>
    <td width=70%>
      <select name="sellPerson.countyId.cityId">
      <%
        for(CountyInfo countyInfo:countyInfoList) {
          String selected = "";
          if(countyInfo.getCityId() == sellPerson.getCountyId().getCityId())
            selected = "selected";
      %>
          <option value='<%=countyInfo.getCityId() %>' <%=selected %>><%=countyInfo.getCityName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>��������:</td>
    <td width=70%>
      <select name="sellPerson.townId.townId">
      <%
        for(TownInfo townInfo:townInfoList) {
          String selected = "";
          if(townInfo.getTownId() == sellPerson.getTownId().getTownId())
            selected = "selected";
      %>
          <option value='<%=townInfo.getTownId() %>' <%=selected %>><%=townInfo.getTownName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr bgcolor='#FFFFFF'>
      <td colspan="4" align="center">
        <input type='submit' name='button' value='����' >
        &nbsp;&nbsp;
        <input type="reset" value='��д' />
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
