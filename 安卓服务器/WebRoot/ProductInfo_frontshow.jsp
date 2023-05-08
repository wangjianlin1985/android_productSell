<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.ProductInfo" %>
<%@ page import="com.chengxusheji.domain.ProductClass" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //��ȡ���е�ProductClass��Ϣ
    List<ProductClass> productClassList = (List<ProductClass>)request.getAttribute("productClassList");
    ProductInfo productInfo = (ProductInfo)request.getAttribute("productInfo");

%>
<HTML><HEAD><TITLE>�鿴��Ʒ��Ϣ</TITLE>
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
    <td width=30%>��Ʒ������:</td>
    <td width=70%><%=productInfo.getBarCode() %></td>
  </tr>

  <tr>
    <td width=30%>�������:</td>
    <td width=70%>
      <%=productInfo.getClassId().getClassName() %>
    </td>
  </tr>

  <tr>
    <td width=30%>��Ʒ����:</td>
    <td width=70%><%=productInfo.getProductName() %></td>
  </tr>

  <tr>
    <td width=30%>��������:</td>
        <% java.text.DateFormat madeDateSDF = new java.text.SimpleDateFormat("yyyy-MM-dd");  %>
    <td width=70%><%=madeDateSDF.format(productInfo.getMadeDate()) %></td>
  </tr>

  <tr>
    <td width=30%>��ע1:</td>
    <td width=70%><%=productInfo.getFirstBeizhu() %></td>
  </tr>

  <tr>
    <td width=30%>��ע2:</td>
    <td width=70%><%=productInfo.getSecondBeizhu() %></td>
  </tr>

  <tr>
    <td width=30%>��ע3:</td>
    <td width=70%><%=productInfo.getThirdBeizhu() %></td>
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
