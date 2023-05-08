package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.TownInfoDAO;
import com.mobileserver.domain.TownInfo;

import org.json.JSONStringer;

public class TownInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造乡镇信息业务层对象*/
	private TownInfoDAO townInfoDAO = new TownInfoDAO();

	/*默认构造函数*/
	public TownInfoServlet() {
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
			/*获取查询乡镇信息的参数信息*/
			int countyId = 0;
			if (request.getParameter("countyId") != null)
				countyId = Integer.parseInt(request.getParameter("countyId"));
			String townName = request.getParameter("townName");
			townName = townName == null ? "" : new String(request.getParameter(
					"townName").getBytes("iso-8859-1"), "UTF-8");

			/*调用业务逻辑层执行乡镇信息查询*/
			List<TownInfo> townInfoList = townInfoDAO.QueryTownInfo(countyId,townName);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<TownInfos>").append("\r\n");
			for (int i = 0; i < townInfoList.size(); i++) {
				sb.append("	<TownInfo>").append("\r\n")
				.append("		<townId>")
				.append(townInfoList.get(i).getTownId())
				.append("</townId>").append("\r\n")
				.append("		<countyId>")
				.append(townInfoList.get(i).getCountyId())
				.append("</countyId>").append("\r\n")
				.append("		<townName>")
				.append(townInfoList.get(i).getTownName())
				.append("</townName>").append("\r\n")
				.append("	</TownInfo>").append("\r\n");
			}
			sb.append("</TownInfos>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(TownInfo townInfo: townInfoList) {
				  stringer.object();
			  stringer.key("townId").value(townInfo.getTownId());
			  stringer.key("countyId").value(townInfo.getCountyId());
			  stringer.key("townName").value(townInfo.getTownName());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加乡镇信息：获取乡镇信息参数，参数保存到新建的乡镇信息对象 */ 
			TownInfo townInfo = new TownInfo();
			int townId = Integer.parseInt(request.getParameter("townId"));
			townInfo.setTownId(townId);
			int countyId = Integer.parseInt(request.getParameter("countyId"));
			townInfo.setCountyId(countyId);
			String townName = new String(request.getParameter("townName").getBytes("iso-8859-1"), "UTF-8");
			townInfo.setTownName(townName);

			/* 调用业务层执行添加操作 */
			String result = townInfoDAO.AddTownInfo(townInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除乡镇信息：获取乡镇信息的乡镇编号*/
			int townId = Integer.parseInt(request.getParameter("townId"));
			/*调用业务逻辑层执行删除操作*/
			String result = townInfoDAO.DeleteTownInfo(townId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新乡镇信息之前先根据townId查询某个乡镇信息*/
			int townId = Integer.parseInt(request.getParameter("townId"));
			TownInfo townInfo = townInfoDAO.GetTownInfo(townId);

			// 客户端查询的乡镇信息对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("townId").value(townInfo.getTownId());
			  stringer.key("countyId").value(townInfo.getCountyId());
			  stringer.key("townName").value(townInfo.getTownName());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新乡镇信息：获取乡镇信息参数，参数保存到新建的乡镇信息对象 */ 
			TownInfo townInfo = new TownInfo();
			int townId = Integer.parseInt(request.getParameter("townId"));
			townInfo.setTownId(townId);
			int countyId = Integer.parseInt(request.getParameter("countyId"));
			townInfo.setCountyId(countyId);
			String townName = new String(request.getParameter("townName").getBytes("iso-8859-1"), "UTF-8");
			townInfo.setTownName(townName);

			/* 调用业务层执行更新操作 */
			String result = townInfoDAO.UpdateTownInfo(townInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
