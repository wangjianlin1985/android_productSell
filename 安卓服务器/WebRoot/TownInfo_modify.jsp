<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.TownInfo" %>
<%@ page import="com.chengxusheji.domain.CountyInfo" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的CountyInfo信息
    List<CountyInfo> countyInfoList = (List<CountyInfo>)request.getAttribute("countyInfoList");
    TownInfo townInfo = (TownInfo)request.getAttribute("townInfo");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>修改乡镇信息</TITLE>
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
    var townName = document.getElementById("townInfo.townName").value;
    if(townName=="") {
        alert('请输入乡镇名称!');
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
    <TD align="left" vAlign=top ><s:form action="TownInfo/TownInfo_ModifyTownInfo.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>乡镇编号:</td>
    <td width=70%><input id="townInfo.townId" name="townInfo.townId" type="text" value="<%=townInfo.getTownId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>所在县市:</td>
    <td width=70%>
      <select name="townInfo.countyId.cityId">
      <%
        for(CountyInfo countyInfo:countyInfoList) {
          String selected = "";
          if(countyInfo.getCityId() == townInfo.getCountyId().getCityId())
            selected = "selected";
      %>
          <option value='<%=countyInfo.getCityId() %>' <%=selected %>><%=countyInfo.getCityName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>乡镇名称:</td>
    <td width=70%><input id="townInfo.townName" name="townInfo.townName" type="text" size="20" value='<%=townInfo.getTownName() %>'/></td>
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
