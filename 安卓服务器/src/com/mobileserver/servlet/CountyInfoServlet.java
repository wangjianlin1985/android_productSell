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

	/*����������Ϣҵ������*/
	private CountyInfoDAO countyInfoDAO = new CountyInfoDAO();

	/*Ĭ�Ϲ��캯��*/
	public CountyInfoServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*��ȡaction����������action��ִֵ�в�ͬ��ҵ����*/
		String action = request.getParameter("action");
		if (action.equals("query")) {
			/*��ȡ��ѯ������Ϣ�Ĳ�����Ϣ*/
			String cityName = request.getParameter("cityName");
			cityName = cityName == null ? "" : new String(request.getParameter(
					"cityName").getBytes("iso-8859-1"), "UTF-8");

			/*����ҵ���߼���ִ��������Ϣ��ѯ*/
			List<CountyInfo> countyInfoList = countyInfoDAO.QueryCountyInfo(cityName);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ���������Ϣ����ȡ������Ϣ�������������浽�½���������Ϣ���� */ 
			CountyInfo countyInfo = new CountyInfo();
			int cityId = Integer.parseInt(request.getParameter("cityId"));
			countyInfo.setCityId(cityId);
			String cityName = new String(request.getParameter("cityName").getBytes("iso-8859-1"), "UTF-8");
			countyInfo.setCityName(cityName);

			/* ����ҵ���ִ����Ӳ��� */
			String result = countyInfoDAO.AddCountyInfo(countyInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ��������Ϣ����ȡ������Ϣ�����б��*/
			int cityId = Integer.parseInt(request.getParameter("cityId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = countyInfoDAO.DeleteCountyInfo(cityId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*����������Ϣ֮ǰ�ȸ���cityId��ѯĳ��������Ϣ*/
			int cityId = Integer.parseInt(request.getParameter("cityId"));
			CountyInfo countyInfo = countyInfoDAO.GetCountyInfo(cityId);

			// �ͻ��˲�ѯ��������Ϣ���󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ����������Ϣ����ȡ������Ϣ�������������浽�½���������Ϣ���� */ 
			CountyInfo countyInfo = new CountyInfo();
			int cityId = Integer.parseInt(request.getParameter("cityId"));
			countyInfo.setCityId(cityId);
			String cityName = new String(request.getParameter("cityName").getBytes("iso-8859-1"), "UTF-8");
			countyInfo.setCityName(cityName);

			/* ����ҵ���ִ�и��²��� */
			String result = countyInfoDAO.UpdateCountyInfo(countyInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
