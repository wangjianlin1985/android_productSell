package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.SellPersonDAO;
import com.mobileserver.domain.SellPerson;

import org.json.JSONStringer;

public class SellPersonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造销售人员业务层对象*/
	private SellPersonDAO sellPersonDAO = new SellPersonDAO();

	/*默认构造函数*/
	public SellPersonServlet() {
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
			/*获取查询销售人员的参数信息*/
			String telephone = request.getParameter("telephone");
			telephone = telephone == null ? "" : new String(request.getParameter(
					"telephone").getBytes("iso-8859-1"), "UTF-8");
			String name = request.getParameter("name");
			name = name == null ? "" : new String(request.getParameter(
					"name").getBytes("iso-8859-1"), "UTF-8");
			int countyId = 0;
			if (request.getParameter("countyId") != null)
				countyId = Integer.parseInt(request.getParameter("countyId"));
			int townId = 0;
			if (request.getParameter("townId") != null)
				townId = Integer.parseInt(request.getParameter("townId"));

			/*调用业务逻辑层执行销售人员查询*/
			List<SellPerson> sellPersonList = sellPersonDAO.QuerySellPerson(telephone,name,countyId,townId);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<SellPersons>").append("\r\n");
			for (int i = 0; i < sellPersonList.size(); i++) {
				sb.append("	<SellPerson>").append("\r\n")
				.append("		<telephone>")
				.append(sellPersonList.get(i).getTelephone())
				.append("</telephone>").append("\r\n")
				.append("		<password>")
				.append(sellPersonList.get(i).getPassword())
				.append("</password>").append("\r\n")
				.append("		<name>")
				.append(sellPersonList.get(i).getName())
				.append("</name>").append("\r\n")
				.append("		<countyId>")
				.append(sellPersonList.get(i).getCountyId())
				.append("</countyId>").append("\r\n")
				.append("		<townId>")
				.append(sellPersonList.get(i).getTownId())
				.append("</townId>").append("\r\n")
				.append("	</SellPerson>").append("\r\n");
			}
			sb.append("</SellPersons>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(SellPerson sellPerson: sellPersonList) {
				  stringer.object();
			  stringer.key("telephone").value(sellPerson.getTelephone());
			  stringer.key("password").value(sellPerson.getPassword());
			  stringer.key("name").value(sellPerson.getName());
			  stringer.key("countyId").value(sellPerson.getCountyId());
			  stringer.key("townId").value(sellPerson.getTownId());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加销售人员：获取销售人员参数，参数保存到新建的销售人员对象 */ 
			SellPerson sellPerson = new SellPerson();
			String telephone = new String(request.getParameter("telephone").getBytes("iso-8859-1"), "UTF-8");
			sellPerson.setTelephone(telephone);
			String password = new String(request.getParameter("password").getBytes("iso-8859-1"), "UTF-8");
			sellPerson.setPassword(password);
			String name = new String(request.getParameter("name").getBytes("iso-8859-1"), "UTF-8");
			sellPerson.setName(name);
			int countyId = Integer.parseInt(request.getParameter("countyId"));
			sellPerson.setCountyId(countyId);
			int townId = Integer.parseInt(request.getParameter("townId"));
			sellPerson.setTownId(townId);

			/* 调用业务层执行添加操作 */
			String result = sellPersonDAO.AddSellPerson(sellPerson);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除销售人员：获取销售人员的手机号*/
			String telephone = new String(request.getParameter("telephone").getBytes("iso-8859-1"), "UTF-8");
			/*调用业务逻辑层执行删除操作*/
			String result = sellPersonDAO.DeleteSellPerson(telephone);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新销售人员之前先根据telephone查询某个销售人员*/
			String telephone = new String(request.getParameter("telephone").getBytes("iso-8859-1"), "UTF-8");
			SellPerson sellPerson = sellPersonDAO.GetSellPerson(telephone);

			// 客户端查询的销售人员对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("telephone").value(sellPerson.getTelephone());
			  stringer.key("password").value(sellPerson.getPassword());
			  stringer.key("name").value(sellPerson.getName());
			  stringer.key("countyId").value(sellPerson.getCountyId());
			  stringer.key("townId").value(sellPerson.getTownId());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新销售人员：获取销售人员参数，参数保存到新建的销售人员对象 */ 
			SellPerson sellPerson = new SellPerson();
			String telephone = new String(request.getParameter("telephone").getBytes("iso-8859-1"), "UTF-8");
			sellPerson.setTelephone(telephone);
			String password = new String(request.getParameter("password").getBytes("iso-8859-1"), "UTF-8");
			sellPerson.setPassword(password);
			String name = new String(request.getParameter("name").getBytes("iso-8859-1"), "UTF-8");
			sellPerson.setName(name);
			int countyId = Integer.parseInt(request.getParameter("countyId"));
			sellPerson.setCountyId(countyId);
			int townId = Integer.parseInt(request.getParameter("townId"));
			sellPerson.setTownId(townId);

			/* 调用业务层执行更新操作 */
			String result = sellPersonDAO.UpdateSellPerson(sellPerson);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
