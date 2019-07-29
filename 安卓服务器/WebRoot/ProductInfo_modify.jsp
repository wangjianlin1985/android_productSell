<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.ProductInfo" %>
<%@ page import="com.chengxusheji.domain.ProductClass" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的ProductClass信息
    List<ProductClass> productClassList = (List<ProductClass>)request.getAttribute("productClassList");
    ProductInfo productInfo = (ProductInfo)request.getAttribute("productInfo");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>修改产品信息</TITLE>
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
    var barCode = document.getElementById("productInfo.barCode").value;
    if(barCode=="") {
        alert('请输入产品条形码!');
        return false;
    }
    var productName = document.getElementById("productInfo.productName").value;
    if(productName=="") {
        alert('请输入产品名称!');
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
    <TD align="left" vAlign=top ><s:form action="ProductInfo/ProductInfo_ModifyProductInfo.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>产品条形码:</td>
    <td width=70%><input id="productInfo.barCode" name="productInfo.barCode" type="text" value="<%=productInfo.getBarCode() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>所属类别:</td>
    <td width=70%>
      <select name="productInfo.classId.classId">
      <%
        for(ProductClass productClass:productClassList) {
          String selected = "";
          if(productClass.getClassId() == productInfo.getClassId().getClassId())
            selected = "selected";
      %>
          <option value='<%=productClass.getClassId() %>' <%=selected %>><%=productClass.getClassName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>产品名称:</td>
    <td width=70%><input id="productInfo.productName" name="productInfo.productName" type="text" size="40" value='<%=productInfo.getProductName() %>'/></td>
  </tr>

  <tr>
    <td width=30%>生产日期:</td>
    <% DateFormat madeDateSDF = new SimpleDateFormat("yyyy-MM-dd");  %>
    <td width=70%><input type="text" readonly  id="productInfo.madeDate"  name="productInfo.madeDate" onclick="setDay(this);" value='<%=madeDateSDF.format(productInfo.getMadeDate()) %>'/></td>
  </tr>

  <tr>
    <td width=30%>备注1:</td>
    <td width=70%><textarea id="productInfo.firstBeizhu" name="productInfo.firstBeizhu" rows=5 cols=50><%=productInfo.getFirstBeizhu() %></textarea></td>
  </tr>

  <tr>
    <td width=30%>备注2:</td>
    <td width=70%><textarea id="productInfo.secondBeizhu" name="productInfo.secondBeizhu" rows=5 cols=50><%=productInfo.getSecondBeizhu() %></textarea></td>
  </tr>

  <tr>
    <td width=30%>备注3:</td>
    <td width=70%><textarea id="productInfo.thirdBeizhu" name="productInfo.thirdBeizhu" rows=5 cols=50><%=productInfo.getThirdBeizhu() %></textarea></td>
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
