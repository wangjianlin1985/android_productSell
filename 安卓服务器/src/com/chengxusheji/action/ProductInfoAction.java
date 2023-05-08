package com.chengxusheji.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import com.opensymphony.xwork2.ActionContext;
import com.chengxusheji.dao.ProductInfoDAO;
import com.chengxusheji.domain.ProductInfo;
import com.chengxusheji.dao.ProductClassDAO;
import com.chengxusheji.domain.ProductClass;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class ProductInfoAction extends BaseAction {

    /*�������Ҫ��ѯ������: ��Ʒ������*/
    private String barCode;
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
    public String getBarCode() {
        return this.barCode;
    }

    /*�������Ҫ��ѯ������: �������*/
    private ProductClass classId;
    public void setClassId(ProductClass classId) {
        this.classId = classId;
    }
    public ProductClass getClassId() {
        return this.classId;
    }

    /*�������Ҫ��ѯ������: ��Ʒ����*/
    private String productName;
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductName() {
        return this.productName;
    }

    /*�������Ҫ��ѯ������: ��������*/
    private String madeDate;
    public void setMadeDate(String madeDate) {
        this.madeDate = madeDate;
    }
    public String getMadeDate() {
        return this.madeDate;
    }

    /*��ǰ�ڼ�ҳ*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*һ������ҳ*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*��ǰ��ѯ���ܼ�¼��Ŀ*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*ҵ������*/
    @Resource ProductClassDAO productClassDAO;
    @Resource ProductInfoDAO productInfoDAO;

    /*��������ProductInfo����*/
    private ProductInfo productInfo;
    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }
    public ProductInfo getProductInfo() {
        return this.productInfo;
    }

    /*��ת�����ProductInfo��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�ProductClass��Ϣ*/
        List<ProductClass> productClassList = productClassDAO.QueryAllProductClassInfo();
        ctx.put("productClassList", productClassList);
        return "add_view";
    }

    /*���ProductInfo��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddProductInfo() {
        ActionContext ctx = ActionContext.getContext();
        /*��֤��Ʒ�������Ƿ��Ѿ�����*/
        String barCode = productInfo.getBarCode();
        ProductInfo db_productInfo = productInfoDAO.GetProductInfoByBarCode(barCode);
        if(null != db_productInfo) {
            ctx.put("error",  java.net.URLEncoder.encode("�ò�Ʒ�������Ѿ�����!"));
            return "error";
        }
        try {
            ProductClass classId = productClassDAO.GetProductClassByClassId(productInfo.getClassId().getClassId());
            productInfo.setClassId(classId);
            productInfoDAO.AddProductInfo(productInfo);
            ctx.put("message",  java.net.URLEncoder.encode("ProductInfo��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ProductInfo���ʧ��!"));
            return "error";
        }
    }

    /*��ѯProductInfo��Ϣ*/
    public String QueryProductInfo() {
        if(currentPage == 0) currentPage = 1;
        if(barCode == null) barCode = "";
        if(productName == null) productName = "";
        if(madeDate == null) madeDate = "";
        List<ProductInfo> productInfoList = productInfoDAO.QueryProductInfoInfo(barCode, classId, productName, madeDate, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        productInfoDAO.CalculateTotalPageAndRecordNumber(barCode, classId, productName, madeDate);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = productInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = productInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("productInfoList",  productInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("barCode", barCode);
        ctx.put("classId", classId);
        List<ProductClass> productClassList = productClassDAO.QueryAllProductClassInfo();
        ctx.put("productClassList", productClassList);
        ctx.put("productName", productName);
        ctx.put("madeDate", madeDate);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryProductInfoOutputToExcel() { 
        if(barCode == null) barCode = "";
        if(productName == null) productName = "";
        if(madeDate == null) madeDate = "";
        List<ProductInfo> productInfoList = productInfoDAO.QueryProductInfoInfo(barCode,classId,productName,madeDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "ProductInfo��Ϣ��¼"; 
        String[] headers = { "��Ʒ������","�������","��Ʒ����","��������"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<productInfoList.size();i++) {
        	ProductInfo productInfo = productInfoList.get(i); 
        	dataset.add(new String[]{productInfo.getBarCode(),productInfo.getClassId().getClassName(),
productInfo.getProductName(),new SimpleDateFormat("yyyy-MM-dd").format(productInfo.getMadeDate())});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"ProductInfo.xls");//filename�����ص�xls���������������Ӣ�� 
			response.setContentType("application/msexcel;charset=UTF-8");//�������� 
			response.setHeader("Pragma","No-cache");//����ͷ 
			response.setHeader("Cache-Control","no-cache");//����ͷ 
			response.setDateHeader("Expires", 0);//��������ͷ  
			String rootPath = ServletActionContext.getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
		return null;
    }
    /*ǰ̨��ѯProductInfo��Ϣ*/
    public String FrontQueryProductInfo() {
        if(currentPage == 0) currentPage = 1;
        if(barCode == null) barCode = "";
        if(productName == null) productName = "";
        if(madeDate == null) madeDate = "";
        List<ProductInfo> productInfoList = productInfoDAO.QueryProductInfoInfo(barCode, classId, productName, madeDate, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        productInfoDAO.CalculateTotalPageAndRecordNumber(barCode, classId, productName, madeDate);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = productInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = productInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("productInfoList",  productInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("barCode", barCode);
        ctx.put("classId", classId);
        List<ProductClass> productClassList = productClassDAO.QueryAllProductClassInfo();
        ctx.put("productClassList", productClassList);
        ctx.put("productName", productName);
        ctx.put("madeDate", madeDate);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�ProductInfo��Ϣ*/
    public String ModifyProductInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������barCode��ȡProductInfo����*/
        ProductInfo productInfo = productInfoDAO.GetProductInfoByBarCode(barCode);

        List<ProductClass> productClassList = productClassDAO.QueryAllProductClassInfo();
        ctx.put("productClassList", productClassList);
        ctx.put("productInfo",  productInfo);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�ProductInfo��Ϣ*/
    public String FrontShowProductInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������barCode��ȡProductInfo����*/
        ProductInfo productInfo = productInfoDAO.GetProductInfoByBarCode(barCode);

        List<ProductClass> productClassList = productClassDAO.QueryAllProductClassInfo();
        ctx.put("productClassList", productClassList);
        ctx.put("productInfo",  productInfo);
        return "front_show_view";
    }

    /*�����޸�ProductInfo��Ϣ*/
    public String ModifyProductInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            ProductClass classId = productClassDAO.GetProductClassByClassId(productInfo.getClassId().getClassId());
            productInfo.setClassId(classId);
            productInfoDAO.UpdateProductInfo(productInfo);
            ctx.put("message",  java.net.URLEncoder.encode("ProductInfo��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ProductInfo��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��ProductInfo��Ϣ*/
    public String DeleteProductInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            productInfoDAO.DeleteProductInfo(barCode);
            ctx.put("message",  java.net.URLEncoder.encode("ProductInfoɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ProductInfoɾ��ʧ��!"));
            return "error";
        }
    }

}
