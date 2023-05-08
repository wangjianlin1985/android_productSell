<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%>
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
    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>���������Ϣ</TITLE> 
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
    <s:form action="SellInfo/SellInfo_AddSellInfo.action" method="post" id="sellInfoAddForm" onsubmit="return checkForm();"  enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">

  <tr>
    <td width=30%>��Ʒ����:</td>
    <td width=70%>
      <select name="sellInfo.productBarCode.barCode">
      <%
        for(ProductInfo productInfo:productInfoList) {
      %>
          <option value='<%=productInfo.getBarCode() %>'><%=productInfo.getProductName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>������Ա:</td>
    <td width=70%>
      <select name="sellInfo.sellPerson.telephone">
      <%
        for(SellPerson sellPerson:sellPersonList) {
      %>
          <option value='<%=sellPerson.getTelephone() %>'><%=sellPerson.getName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>��������:</td>
    <td width=70%><input id="sellInfo.sellCount" name="sellInfo.sellCount" type="text" size="8" /></td>
  </tr>

  <tr>
    <td width=30%>��������:</td>
    <td width=70%><input type="text" readonly id="sellInfo.sellDate"  name="sellInfo.sellDate" onclick="setDay(this);"/></td>
  </tr>

  <tr>
    <td width=30%>��ע1:</td>
    <td width=70%><textarea id="sellInfo.firstBeizhu" name="sellInfo.firstBeizhu" rows="5" cols="50"></textarea></td>
  </tr>

  <tr>
    <td width=30%>��ע2:</td>
    <td width=70%><textarea id="sellInfo.secondBeizhu" name="sellInfo.secondBeizhu" rows="5" cols="50"></textarea></td>
  </tr>

  <tr>
    <td width=30%>��ע3:</td>
    <td width=70%><textarea id="sellInfo.thirdBeizhu" name="sellInfo.thirdBeizhu" rows="5" cols="50"></textarea></td>
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
