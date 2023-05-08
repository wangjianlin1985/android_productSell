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

	/*����������Ϣҵ������*/
	private SellInfoDAO sellInfoDAO = new SellInfoDAO();

	/*Ĭ�Ϲ��캯��*/
	public SellInfoServlet() {
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
			String productBarCode = "";
			if (request.getParameter("productBarCode") != null)
				productBarCode = request.getParameter("productBarCode");
			String sellPerson = "";
			if (request.getParameter("sellPerson") != null)
				sellPerson = request.getParameter("sellPerson");
			Timestamp sellDate = null;
			if (request.getParameter("sellDate") != null)
				sellDate = Timestamp.valueOf(request.getParameter("sellDate"));

			/*����ҵ���߼���ִ��������Ϣ��ѯ*/
			List<SellInfo> sellInfoList = sellInfoDAO.QuerySellInfo(productBarCode,sellPerson,sellDate);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ���������Ϣ����ȡ������Ϣ�������������浽�½���������Ϣ���� */ 
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

			/* ����ҵ���ִ����Ӳ��� */
			String result = sellInfoDAO.AddSellInfo(sellInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ��������Ϣ����ȡ������Ϣ�����۱��*/
			int sellId = Integer.parseInt(request.getParameter("sellId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = sellInfoDAO.DeleteSellInfo(sellId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*����������Ϣ֮ǰ�ȸ���sellId��ѯĳ��������Ϣ*/
			int sellId = Integer.parseInt(request.getParameter("sellId"));
			SellInfo sellInfo = sellInfoDAO.GetSellInfo(sellId);

			// �ͻ��˲�ѯ��������Ϣ���󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ����������Ϣ����ȡ������Ϣ�������������浽�½���������Ϣ���� */ 
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

			/* ����ҵ���ִ�и��²��� */
			String result = sellInfoDAO.UpdateSellInfo(sellInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
