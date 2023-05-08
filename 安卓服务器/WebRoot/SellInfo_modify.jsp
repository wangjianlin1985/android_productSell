<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.SellInfo" %>
<%@ page import="com.chengxusheji.domain.ProductInfo" %>
<%@ page import="com.chengxusheji.domain.SellPerson" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的ProductInfo信息
    List<ProductInfo> productInfoList = (List<ProductInfo>)request.getAttribute("productInfoList");
    //获取所有的SellPerson信息
    List<SellPerson> sellPersonList = (List<SellPerson>)request.getAttribute("sellPersonList");
    SellInfo sellInfo = (SellInfo)request.getAttribute("sellInfo");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>修改销售信息</TITLE>
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
    return true; 
}
 </script>
</HEAD>
<BODY background="<%=basePath %>images/adminBg.jpg">
<s:fielderror cssStyle="color:red" />
<TABLE align="center" height="100%" cellSpacing=0 cellPadding=0 width="80%" border=0>
  <TBODY>
  <TR>
    <TD align="left" vAlign=top ><s:form action="SellInfo/SellInfo_ModifySellInfo.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>销售编号:</td>
    <td width=70%><input id="sellInfo.sellId" name="sellInfo.sellId" type="text" value="<%=sellInfo.getSellId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>产品名称:</td>
    <td width=70%>
      <select name="sellInfo.productBarCode.barCode">
      <%
        for(ProductInfo productInfo:productInfoList) {
          String selected = "";
          if(productInfo.getBarCode().equals(sellInfo.getProductBarCode().getBarCode()))
            selected = "selected";
      %>
          <option value='<%=productInfo.getBarCode() %>' <%=selected %>><%=productInfo.getProductName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>销售人员:</td>
    <td width=70%>
      <select name="sellInfo.sellPerson.telephone">
      <%
        for(SellPerson sellPerson:sellPersonList) {
          String selected = "";
          if(sellPerson.getTelephone().equals(sellInfo.getSellPerson().getTelephone()))
            selected = "selected";
      %>
          <option value='<%=sellPerson.getTelephone() %>' <%=selected %>><%=sellPerson.getName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>销售数量:</td>
    <td width=70%><input id="sellInfo.sellCount" name="sellInfo.sellCount" type="text" size="8" value='<%=sellInfo.getSellCount() %>'/></td>
  </tr>

  <tr>
    <td width=30%>销售日期:</td>
    <% DateFormat sellDateSDF = new SimpleDateFormat("yyyy-MM-dd");  %>
    <td width=70%><input type="text" readonly  id="sellInfo.sellDate"  name="sellInfo.sellDate" onclick="setDay(this);" value='<%=sellDateSDF.format(sellInfo.getSellDate()) %>'/></td>
  </tr>

  <tr>
    <td width=30%>备注1:</td>
    <td width=70%><textarea id="sellInfo.firstBeizhu" name="sellInfo.firstBeizhu" rows=5 cols=50><%=sellInfo.getFirstBeizhu() %></textarea></td>
  </tr>

  <tr>
    <td width=30%>备注2:</td>
    <td width=70%><textarea id="sellInfo.secondBeizhu" name="sellInfo.secondBeizhu" rows=5 cols=50><%=sellInfo.getSecondBeizhu() %></textarea></td>
  </tr>

  <tr>
    <td width=30%>备注3:</td>
    <td width=70%><textarea id="sellInfo.thirdBeizhu" name="sellInfo.thirdBeizhu" rows=5 cols=50><%=sellInfo.getThirdBeizhu() %></textarea></td>
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
