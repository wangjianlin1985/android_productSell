package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.ProductInfoDAO;
import com.mobileserver.domain.ProductInfo;

import org.json.JSONStringer;

public class ProductInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造产品信息业务层对象*/
	private ProductInfoDAO productInfoDAO = new ProductInfoDAO();

	/*默认构造函数*/
	public ProductInfoServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*获取action参数，根据action的值执行不同的业务处理*/
		String action = request.getParameter("action");
		if (action.equals("query")) {
			/*获取查询产品信息的参数信息*/
			String barCode = request.getParameter("barCode");
			barCode = barCode == null ? "" : new String(request.getParameter(
					"barCode").getBytes("iso-8859-1"), "UTF-8");
			int classId = 0;
			if (request.getParameter("classId") != null)
				classId = Integer.parseInt(request.getParameter("classId"));
			String productName = request.getParameter("productName");
			productName = productName == null ? "" : new String(request.getParameter(
					"productName").getBytes("iso-8859-1"), "UTF-8");
			Timestamp madeDate = null;
			if (request.getParameter("madeDate") != null)
				madeDate = Timestamp.valueOf(request.getParameter("madeDate"));

			/*调用业务逻辑层执行产品信息查询*/
			List<ProductInfo> productInfoList = productInfoDAO.QueryProductInfo(barCode,classId,productName,madeDate);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<ProductInfos>").append("\r\n");
			for (int i = 0; i < productInfoList.size(); i++) {
				sb.append("	<ProductInfo>").append("\r\n")
				.append("		<barCode>")
				.append(productInfoList.get(i).getBarCode())
				.append("</barCode>").append("\r\n")
				.append("		<classId>")
				.append(productInfoList.get(i).getClassId())
				.append("</classId>").append("\r\n")
				.append("		<productName>")
				.append(productInfoList.get(i).getProductName())
				.append("</productName>").append("\r\n")
				.append("		<madeDate>")
				.append(productInfoList.get(i).getMadeDate())
				.append("</madeDate>").append("\r\n")
				.append("		<firstBeizhu>")
				.append(productInfoList.get(i).getFirstBeizhu())
				.append("</firstBeizhu>").append("\r\n")
				.append("		<secondBeizhu>")
				.append(productInfoList.get(i).getSecondBeizhu())
				.append("</secondBeizhu>").append("\r\n")
				.append("		<thirdBeizhu>")
				.append(productInfoList.get(i).getThirdBeizhu())
				.append("</thirdBeizhu>").append("\r\n")
				.append("	</ProductInfo>").append("\r\n");
			}
			sb.append("</ProductInfos>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(ProductInfo productInfo: productInfoList) {
				  stringer.object();
			  stringer.key("barCode").value(productInfo.getBarCode());
			  stringer.key("classId").value(productInfo.getClassId());
			  stringer.key("productName").value(productInfo.getProductName());
			  stringer.key("madeDate").value(productInfo.getMadeDate());
			  stringer.key("firstBeizhu").value(productInfo.getFirstBeizhu());
			  stringer.key("secondBeizhu").value(productInfo.getSecondBeizhu());
			  stringer.key("thirdBeizhu").value(productInfo.getThirdBeizhu());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加产品信息：获取产品信息参数，参数保存到新建的产品信息对象 */ 
			ProductInfo productInfo = new ProductInfo();
			String barCode = new String(request.getParameter("barCode").getBytes("iso-8859-1"), "UTF-8");
			productInfo.setBarCode(barCode);
			int classId = Integer.parseInt(request.getParameter("classId"));
			productInfo.setClassId(classId);
			String productName = new String(request.getParameter("productName").getBytes("iso-8859-1"), "UTF-8");
			productInfo.setProductName(productName);
			Timestamp madeDate = Timestamp.valueOf(request.getParameter("madeDate"));
			productInfo.setMadeDate(madeDate);
			String firstBeizhu = new String(request.getParameter("firstBeizhu").getBytes("iso-8859-1"), "UTF-8");
			productInfo.setFirstBeizhu(firstBeizhu);
			String secondBeizhu = new String(request.getParameter("secondBeizhu").getBytes("iso-8859-1"), "UTF-8");
			productInfo.setSecondBeizhu(secondBeizhu);
			String thirdBeizhu = new String(request.getParameter("thirdBeizhu").getBytes("iso-8859-1"), "UTF-8");
			productInfo.setThirdBeizhu(thirdBeizhu);

			/* 调用业务层执行添加操作 */
			String result = productInfoDAO.AddProductInfo(productInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除产品信息：获取产品信息的产品条形码*/
			String barCode = new String(request.getParameter("barCode").getBytes("iso-8859-1"), "UTF-8");
			/*调用业务逻辑层执行删除操作*/
			String result = productInfoDAO.DeleteProductInfo(barCode);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新产品信息之前先根据barCode查询某个产品信息*/
			String barCode = new String(request.getParameter("barCode").getBytes("iso-8859-1"), "UTF-8");
			ProductInfo productInfo = productInfoDAO.GetProductInfo(barCode);

			// 客户端查询的产品信息对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("barCode").value(productInfo.getBarCode());
			  stringer.key("classId").value(productInfo.getClassId());
			  stringer.key("productName").value(productInfo.getProductName());
			  stringer.key("madeDate").value(productInfo.getMadeDate());
			  stringer.key("firstBeizhu").value(productInfo.getFirstBeizhu());
			  stringer.key("secondBeizhu").value(productInfo.getSecondBeizhu());
			  stringer.key("thirdBeizhu").value(productInfo.getThirdBeizhu());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新产品信息：获取产品信息参数，参数保存到新建的产品信息对象 */ 
			ProductInfo productInfo = new ProductInfo();
			String barCode = new String(request.getParameter("barCode").getBytes("iso-8859-1"), "UTF-8");
			productInfo.setBarCode(barCode);
			int classId = Integer.parseInt(request.getParameter("classId"));
			productInfo.setClassId(classId);
			String productName = new String(request.getParameter("productName").getBytes("iso-8859-1"), "UTF-8");
			productInfo.setProductName(productName);
			Timestamp madeDate = Timestamp.valueOf(request.getParameter("madeDate"));
			productInfo.setMadeDate(madeDate);
			String firstBeizhu = new String(request.getParameter("firstBeizhu").getBytes("iso-8859-1"), "UTF-8");
			productInfo.setFirstBeizhu(firstBeizhu);
			String secondBeizhu = new String(request.getParameter("secondBeizhu").getBytes("iso-8859-1"), "UTF-8");
			productInfo.setSecondBeizhu(secondBeizhu);
			String thirdBeizhu = new String(request.getParameter("thirdBeizhu").getBytes("iso-8859-1"), "UTF-8");
			productInfo.setThirdBeizhu(thirdBeizhu);

			/* 调用业务层执行更新操作 */
			String result = productInfoDAO.UpdateProductInfo(productInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
