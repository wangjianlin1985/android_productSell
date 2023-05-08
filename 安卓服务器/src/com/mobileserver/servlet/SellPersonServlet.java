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

	/*����������Աҵ������*/
	private SellPersonDAO sellPersonDAO = new SellPersonDAO();

	/*Ĭ�Ϲ��캯��*/
	public SellPersonServlet() {
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
			/*��ȡ��ѯ������Ա�Ĳ�����Ϣ*/
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

			/*����ҵ���߼���ִ��������Ա��ѯ*/
			List<SellPerson> sellPersonList = sellPersonDAO.QuerySellPerson(telephone,name,countyId,townId);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ���������Ա����ȡ������Ա�������������浽�½���������Ա���� */ 
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

			/* ����ҵ���ִ����Ӳ��� */
			String result = sellPersonDAO.AddSellPerson(sellPerson);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ��������Ա����ȡ������Ա���ֻ���*/
			String telephone = new String(request.getParameter("telephone").getBytes("iso-8859-1"), "UTF-8");
			/*����ҵ���߼���ִ��ɾ������*/
			String result = sellPersonDAO.DeleteSellPerson(telephone);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*����������Ա֮ǰ�ȸ���telephone��ѯĳ��������Ա*/
			String telephone = new String(request.getParameter("telephone").getBytes("iso-8859-1"), "UTF-8");
			SellPerson sellPerson = sellPersonDAO.GetSellPerson(telephone);

			// �ͻ��˲�ѯ��������Ա���󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ����������Ա����ȡ������Ա�������������浽�½���������Ա���� */ 
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

			/* ����ҵ���ִ�и��²��� */
			String result = sellPersonDAO.UpdateSellPerson(sellPerson);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
