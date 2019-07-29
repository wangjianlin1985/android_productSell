package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.SellInfoDAO;
import com.mobileserver.domain.SellInfo;

import org.json.JSONStringer;

public class SellInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造销售信息业务层对象*/
	private SellInfoDAO sellInfoDAO = new SellInfoDAO();

	/*默认构造函数*/
	public SellInfoServlet() {
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
			/*获取查询销售信息的参数信息*/
			String productBarCode = "";
			if (request.getParameter("productBarCode") != null)
				productBarCode = request.getParameter("productBarCode");
			String sellPerson = "";
			if (request.getParameter("sellPerson") != null)
				sellPerson = request.getParameter("sellPerson");
			Timestamp sellDate = null;
			if (request.getParameter("sellDate") != null)
				sellDate = Timestamp.valueOf(request.getParameter("sellDate"));

			/*调用业务逻辑层执行销售信息查询*/
			List<SellInfo> sellInfoList = sellInfoDAO.QuerySellInfo(productBarCode,sellPerson,sellDate);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<SellInfos>").append("\r\n");
			for (int i = 0; i < sellInfoList.size(); i++) {
				sb.append("	<SellInfo>").append("\r\n")
				.append("		<sellId>")
				.append(sellInfoList.get(i).getSellId())
				.append("</sellId>").append("\r\n")
				.append("		<productBarCode>")
				.append(sellInfoList.get(i).getProductBarCode())
				.append("</productBarCode>").append("\r\n")
				.append("		<sellPerson>")
				.append(sellInfoList.get(i).getSellPerson())
				.append("</sellPerson>").append("\r\n")
				.append("		<sellCount>")
				.append(sellInfoList.get(i).getSellCount())
				.append("</sellCount>").append("\r\n")
				.append("		<sellDate>")
				.append(sellInfoList.get(i).getSellDate())
				.append("</sellDate>").append("\r\n")
				.append("		<firstBeizhu>")
				.append(sellInfoList.get(i).getFirstBeizhu())
				.append("</firstBeizhu>").append("\r\n")
				.append("		<secondBeizhu>")
				.append(sellInfoList.get(i).getSecondBeizhu())
				.append("</secondBeizhu>").append("\r\n")
				.append("		<thirdBeizhu>")
				.append(sellInfoList.get(i).getThirdBeizhu())
				.append("</thirdBeizhu>").append("\r\n")
				.append("	</SellInfo>").append("\r\n");
			}
			sb.append("</SellInfos>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(SellInfo sellInfo: sellInfoList) {
				  stringer.object();
			  stringer.key("sellId").value(sellInfo.getSellId());
			  stringer.key("productBarCode").value(sellInfo.getProductBarCode());
			  stringer.key("sellPerson").value(sellInfo.getSellPerson());
			  stringer.key("sellCount").value(sellInfo.getSellCount());
			  stringer.key("sellDate").value(sellInfo.getSellDate());
			  stringer.key("firstBeizhu").value(sellInfo.getFirstBeizhu());
			  stringer.key("secondBeizhu").value(sellInfo.getSecondBeizhu());
			  stringer.key("thirdBeizhu").value(sellInfo.getThirdBeizhu());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加销售信息：获取销售信息参数，参数保存到新建的销售信息对象 */ 
			SellInfo sellInfo = new SellInfo();
			int sellId = Integer.parseInt(request.getParameter("sellId"));
			sellInfo.setSellId(sellId);
			String productBarCode = new String(request.getParameter("productBarCode").getBytes("iso-8859-1"), "UTF-8");
			sellInfo.setProductBarCode(productBarCode);
			String sellPerson = new String(request.getParameter("sellPerson").getBytes("iso-8859-1"), "UTF-8");
			sellInfo.setSellPerson(sellPerson);
			int sellCount = Integer.parseInt(request.getParameter("sellCount"));
			sellInfo.setSellCount(sellCount);
			Timestamp sellDate = Timestamp.valueOf(request.getParameter("sellDate"));
			sellInfo.setSellDate(sellDate);
			String firstBeizhu = new String(request.getParameter("firstBeizhu").getBytes("iso-8859-1"), "UTF-8");
			sellInfo.setFirstBeizhu(firstBeizhu);
			String secondBeizhu = new String(request.getParameter("secondBeizhu").getBytes("iso-8859-1"), "UTF-8");
			sellInfo.setSecondBeizhu(secondBeizhu);
			String thirdBeizhu = new String(request.getParameter("thirdBeizhu").getBytes("iso-8859-1"), "UTF-8");
			sellInfo.setThirdBeizhu(thirdBeizhu);

			/* 调用业务层执行添加操作 */
			String result = sellInfoDAO.AddSellInfo(sellInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除销售信息：获取销售信息的销售编号*/
			int sellId = Integer.parseInt(request.getParameter("sellId"));
			/*调用业务逻辑层执行删除操作*/
			String result = sellInfoDAO.DeleteSellInfo(sellId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新销售信息之前先根据sellId查询某个销售信息*/
			int sellId = Integer.parseInt(request.getParameter("sellId"));
			SellInfo sellInfo = sellInfoDAO.GetSellInfo(sellId);

			// 客户端查询的销售信息对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("sellId").value(sellInfo.getSellId());
			  stringer.key("productBarCode").value(sellInfo.getProductBarCode());
			  stringer.key("sellPerson").value(sellInfo.getSellPerson());
			  stringer.key("sellCount").value(sellInfo.getSellCount());
			  stringer.key("sellDate").value(sellInfo.getSellDate());
			  stringer.key("firstBeizhu").value(sellInfo.getFirstBeizhu());
			  stringer.key("secondBeizhu").value(sellInfo.getSecondBeizhu());
			  stringer.key("thirdBeizhu").value(sellInfo.getThirdBeizhu());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新销售信息：获取销售信息参数，参数保存到新建的销售信息对象 */ 
			SellInfo sellInfo = new SellInfo();
			int sellId = Integer.parseInt(request.getParameter("sellId"));
			sellInfo.setSellId(sellId);
			String productBarCode = new String(request.getParameter("productBarCode").getBytes("iso-8859-1"), "UTF-8");
			sellInfo.setProductBarCode(productBarCode);
			String sellPerson = new String(request.getParameter("sellPerson").getBytes("iso-8859-1"), "UTF-8");
			sellInfo.setSellPerson(sellPerson);
			int sellCount = Integer.parseInt(request.getParameter("sellCount"));
			sellInfo.setSellCount(sellCount);
			Timestamp sellDate = Timestamp.valueOf(request.getParameter("sellDate"));
			sellInfo.setSellDate(sellDate);
			String firstBeizhu = new String(request.getParameter("firstBeizhu").getBytes("iso-8859-1"), "UTF-8");
			sellInfo.setFirstBeizhu(firstBeizhu);
			String secondBeizhu = new String(request.getParameter("secondBeizhu").getBytes("iso-8859-1"), "UTF-8");
			sellInfo.setSecondBeizhu(secondBeizhu);
			String thirdBeizhu = new String(request.getParameter("thirdBeizhu").getBytes("iso-8859-1"), "UTF-8");
			sellInfo.setThirdBeizhu(thirdBeizhu);

			/* 调用业务层执行更新操作 */
			String result = sellInfoDAO.UpdateSellInfo(sellInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
