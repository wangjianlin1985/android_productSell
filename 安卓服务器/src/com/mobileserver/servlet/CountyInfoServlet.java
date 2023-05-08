package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.CountyInfoDAO;
import com.mobileserver.domain.CountyInfo;

import org.json.JSONStringer;

public class CountyInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造县市信息业务层对象*/
	private CountyInfoDAO countyInfoDAO = new CountyInfoDAO();

	/*默认构造函数*/
	public CountyInfoServlet() {
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
			/*获取查询县市信息的参数信息*/
			String cityName = request.getParameter("cityName");
			cityName = cityName == null ? "" : new String(request.getParameter(
					"cityName").getBytes("iso-8859-1"), "UTF-8");

			/*调用业务逻辑层执行县市信息查询*/
			List<CountyInfo> countyInfoList = countyInfoDAO.QueryCountyInfo(cityName);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<CountyInfos>").append("\r\n");
			for (int i = 0; i < countyInfoList.size(); i++) {
				sb.append("	<CountyInfo>").append("\r\n")
				.append("		<cityId>")
				.append(countyInfoList.get(i).getCityId())
				.append("</cityId>").append("\r\n")
				.append("		<cityName>")
				.append(countyInfoList.get(i).getCityName())
				.append("</cityName>").append("\r\n")
				.append("	</CountyInfo>").append("\r\n");
			}
			sb.append("</CountyInfos>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(CountyInfo countyInfo: countyInfoList) {
				  stringer.object();
			  stringer.key("cityId").value(countyInfo.getCityId());
			  stringer.key("cityName").value(countyInfo.getCityName());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加县市信息：获取县市信息参数，参数保存到新建的县市信息对象 */ 
			CountyInfo countyInfo = new CountyInfo();
			int cityId = Integer.parseInt(request.getParameter("cityId"));
			countyInfo.setCityId(cityId);
			String cityName = new String(request.getParameter("cityName").getBytes("iso-8859-1"), "UTF-8");
			countyInfo.setCityName(cityName);

			/* 调用业务层执行添加操作 */
			String result = countyInfoDAO.AddCountyInfo(countyInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除县市信息：获取县市信息的县市编号*/
			int cityId = Integer.parseInt(request.getParameter("cityId"));
			/*调用业务逻辑层执行删除操作*/
			String result = countyInfoDAO.DeleteCountyInfo(cityId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新县市信息之前先根据cityId查询某个县市信息*/
			int cityId = Integer.parseInt(request.getParameter("cityId"));
			CountyInfo countyInfo = countyInfoDAO.GetCountyInfo(cityId);

			// 客户端查询的县市信息对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("cityId").value(countyInfo.getCityId());
			  stringer.key("cityName").value(countyInfo.getCityName());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新县市信息：获取县市信息参数，参数保存到新建的县市信息对象 */ 
			CountyInfo countyInfo = new CountyInfo();
			int cityId = Integer.parseInt(request.getParameter("cityId"));
			countyInfo.setCityId(cityId);
			String cityName = new String(request.getParameter("cityName").getBytes("iso-8859-1"), "UTF-8");
			countyInfo.setCityName(cityName);

			/* 调用业务层执行更新操作 */
			String result = countyInfoDAO.UpdateCountyInfo(countyInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
