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

	/*�����Ʒ��Ϣҵ������*/
	private ProductInfoDAO productInfoDAO = new ProductInfoDAO();

	/*Ĭ�Ϲ��캯��*/
	public ProductInfoServlet() {
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
			/*��ȡ��ѯ��Ʒ��Ϣ�Ĳ�����Ϣ*/
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

			/*����ҵ���߼���ִ�в�Ʒ��Ϣ��ѯ*/
			List<ProductInfo> productInfoList = productInfoDAO.QueryProductInfo(barCode,classId,productName,madeDate);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ��Ӳ�Ʒ��Ϣ����ȡ��Ʒ��Ϣ�������������浽�½��Ĳ�Ʒ��Ϣ���� */ 
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

			/* ����ҵ���ִ����Ӳ��� */
			String result = productInfoDAO.AddProductInfo(productInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ����Ʒ��Ϣ����ȡ��Ʒ��Ϣ�Ĳ�Ʒ������*/
			String barCode = new String(request.getParameter("barCode").getBytes("iso-8859-1"), "UTF-8");
			/*����ҵ���߼���ִ��ɾ������*/
			String result = productInfoDAO.DeleteProductInfo(barCode);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*���²�Ʒ��Ϣ֮ǰ�ȸ���barCode��ѯĳ����Ʒ��Ϣ*/
			String barCode = new String(request.getParameter("barCode").getBytes("iso-8859-1"), "UTF-8");
			ProductInfo productInfo = productInfoDAO.GetProductInfo(barCode);

			// �ͻ��˲�ѯ�Ĳ�Ʒ��Ϣ���󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ���²�Ʒ��Ϣ����ȡ��Ʒ��Ϣ�������������浽�½��Ĳ�Ʒ��Ϣ���� */ 
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

			/* ����ҵ���ִ�и��²��� */
			String result = productInfoDAO.UpdateProductInfo(productInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
