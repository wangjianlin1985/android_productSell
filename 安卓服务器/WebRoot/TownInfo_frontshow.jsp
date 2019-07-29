<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.TownInfo" %>
<%@ page import="com.chengxusheji.domain.CountyInfo" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的CountyInfo信息
    List<CountyInfo> countyInfoList = (List<CountyInfo>)request.getAttribute("countyInfoList");
    TownInfo townInfo = (TownInfo)request.getAttribute("townInfo");

%>
<HTML><HEAD><TITLE>查看乡镇信息</TITLE>
<STYLE type=text/css>
body{margin:0px; font-size:12px; background-image:url(<%=basePath%>images/bg.jpg); background-position:bottom; background-repeat:repeat-x; background-color:#A2D5F0;}
.STYLE1 {color: #ECE9D8}
.label {font-style.:italic; }
.errorLabel {font-style.:italic;  color:red; }
.errorMessage {font-weight:bold; color:red; }
</STYLE>
 <script src="<%=basePath %>calendar.js"></script>
</HEAD>
<BODY><br/><br/>
<s:fielderror cssStyle="color:red" />
<TABLE align="center" height="100%" cellSpacing=0 cellPadding=0 width="80%" border=0>
  <TBODY>
  <TR>
    <TD align="left" vAlign=top ><s:form action="" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3'  class="tablewidth">
  <tr>
    <td width=30%>乡镇编号:</td>
    <td width=70%><%=townInfo.getTownId() %></td>
  </tr>

  <tr>
    <td width=30%>所在县市:</td>
    <td width=70%>
      <%=townInfo.getCountyId().getCityName() %>
    </td>
  </tr>

  <tr>
    <td width=30%>乡镇名称:</td>
    <td width=70%><%=townInfo.getTownName() %></td>
  </tr>

  <tr>
      <td colspan="4" align="center">
        <input type="button" value="返回" onclick="history.back();"/>
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
