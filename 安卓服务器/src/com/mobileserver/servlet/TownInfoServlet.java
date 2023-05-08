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

	/*����������Ϣҵ������*/
	private TownInfoDAO townInfoDAO = new TownInfoDAO();

	/*Ĭ�Ϲ��캯��*/
	public TownInfoServlet() {
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
			int countyId = 0;
			if (request.getParameter("countyId") != null)
				countyId = Integer.parseInt(request.getParameter("countyId"));
			String townName = request.getParameter("townName");
			townName = townName == null ? "" : new String(request.getParameter(
					"townName").getBytes("iso-8859-1"), "UTF-8");

			/*����ҵ���߼���ִ��������Ϣ��ѯ*/
			List<TownInfo> townInfoList = townInfoDAO.QueryTownInfo(countyId,townName);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ���������Ϣ����ȡ������Ϣ�������������浽�½���������Ϣ���� */ 
			TownInfo townInfo = new TownInfo();
			int townId = Integer.parseInt(request.getParameter("townId"));
			townInfo.setTownId(townId);
			int countyId = Integer.parseInt(request.getParameter("countyId"));
			townInfo.setCountyId(countyId);
			String townName = new String(request.getParameter("townName").getBytes("iso-8859-1"), "UTF-8");
			townInfo.setTownName(townName);

			/* ����ҵ���ִ����Ӳ��� */
			String result = townInfoDAO.AddTownInfo(townInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ��������Ϣ����ȡ������Ϣ��������*/
			int townId = Integer.parseInt(request.getParameter("townId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = townInfoDAO.DeleteTownInfo(townId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*����������Ϣ֮ǰ�ȸ���townId��ѯĳ��������Ϣ*/
			int townId = Integer.parseInt(request.getParameter("townId"));
			TownInfo townInfo = townInfoDAO.GetTownInfo(townId);

			// �ͻ��˲�ѯ��������Ϣ���󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ����������Ϣ����ȡ������Ϣ�������������浽�½���������Ϣ���� */ 
			TownInfo townInfo = new TownInfo();
			int townId = Integer.parseInt(request.getParameter("townId"));
			townInfo.setTownId(townId);
			int countyId = Integer.parseInt(request.getParameter("countyId"));
			townInfo.setCountyId(countyId);
			String townName = new String(request.getParameter("townName").getBytes("iso-8859-1"), "UTF-8");
			townInfo.setTownName(townName);

			/* ����ҵ���ִ�и��²��� */
			String result = townInfoDAO.UpdateTownInfo(townInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
