<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.SellInfo" %>
<%@ page import="com.chengxusheji.domain.ProductInfo" %>
<%@ page import="com.chengxusheji.domain.SellPerson" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //��ȡ���е�ProductInfo��Ϣ
    List<ProductInfo> productInfoList = (List<ProductInfo>)request.getAttribute("productInfoList");
    //��ȡ���е�SellPerson��Ϣ
    List<SellPerson> sellPersonList = (List<SellPerson>)request.getAttribute("sellPersonList");
    SellInfo sellInfo = (SellInfo)request.getAttribute("sellInfo");

%>
<HTML><HEAD><TITLE>�鿴������Ϣ</TITLE>
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
    <td width=30%>���۱��:</td>
    <td width=70%><%=sellInfo.getSellId() %></td>
  </tr>

  <tr>
    <td width=30%>��Ʒ����:</td>
    <td width=70%>
      <%=sellInfo.getProductBarCode().getProductName() %>
    </td>
  </tr>

  <tr>
    <td width=30%>������Ա:</td>
    <td width=70%>
      <%=sellInfo.getSellPerson().getName() %>
    </td>
  </tr>

  <tr>
    <td width=30%>��������:</td>
    <td width=70%><%=sellInfo.getSellCount() %></td>
  </tr>

  <tr>
    <td width=30%>��������:</td>
        <% java.text.DateFormat sellDateSDF = new java.text.SimpleDateFormat("yyyy-MM-dd");  %>
    <td width=70%><%=sellDateSDF.format(sellInfo.getSellDate()) %></td>
  </tr>

  <tr>
    <td width=30%>��ע1:</td>
    <td width=70%><%=sellInfo.getFirstBeizhu() %></td>
  </tr>

  <tr>
    <td width=30%>��ע2:</td>
    <td width=70%><%=sellInfo.getSecondBeizhu() %></td>
  </tr>

  <tr>
    <td width=30%>��ע3:</td>
    <td width=70%><%=sellInfo.getThirdBeizhu() %></td>
  </tr>

  <tr>
      <td colspan="4" align="center">
        <input type="button" value="����" onclick="history.back();"/>
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
