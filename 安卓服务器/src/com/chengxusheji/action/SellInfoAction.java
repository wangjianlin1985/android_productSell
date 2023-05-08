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
import com.chengxusheji.dao.SellInfoDAO;
import com.chengxusheji.domain.SellInfo;
import com.chengxusheji.dao.ProductInfoDAO;
import com.chengxusheji.domain.ProductInfo;
import com.chengxusheji.dao.SellPersonDAO;
import com.chengxusheji.domain.SellPerson;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class SellInfoAction extends BaseAction {

    /*�������Ҫ��ѯ������: ��Ʒ����*/
    private ProductInfo productBarCode;
    public void setProductBarCode(ProductInfo productBarCode) {
        this.productBarCode = productBarCode;
    }
    public ProductInfo getProductBarCode() {
        return this.productBarCode;
    }

    /*�������Ҫ��ѯ������: ������Ա*/
    private SellPerson sellPerson;
    public void setSellPerson(SellPerson sellPerson) {
        this.sellPerson = sellPerson;
    }
    public SellPerson getSellPerson() {
        return this.sellPerson;
    }

    /*�������Ҫ��ѯ������: ��������*/
    private String sellDate;
    public void setSellDate(String sellDate) {
        this.sellDate = sellDate;
    }
    public String getSellDate() {
        return this.sellDate;
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

    private int sellId;
    public void setSellId(int sellId) {
        this.sellId = sellId;
    }
    public int getSellId() {
        return sellId;
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
    @Resource ProductInfoDAO productInfoDAO;
    @Resource SellPersonDAO sellPersonDAO;
    @Resource SellInfoDAO sellInfoDAO;

    /*��������SellInfo����*/
    private SellInfo sellInfo;
    public void setSellInfo(SellInfo sellInfo) {
        this.sellInfo = sellInfo;
    }
    public SellInfo getSellInfo() {
        return this.sellInfo;
    }

    /*��ת�����SellInfo��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�ProductInfo��Ϣ*/
        List<ProductInfo> productInfoList = productInfoDAO.QueryAllProductInfoInfo();
        ctx.put("productInfoList", productInfoList);
        /*��ѯ���е�SellPerson��Ϣ*/
        List<SellPerson> sellPersonList = sellPersonDAO.QueryAllSellPersonInfo();
        ctx.put("sellPersonList", sellPersonList);
        return "add_view";
    }

    /*���SellInfo��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddSellInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            ProductInfo productBarCode = productInfoDAO.GetProductInfoByBarCode(sellInfo.getProductBarCode().getBarCode());
            sellInfo.setProductBarCode(productBarCode);
            SellPerson sellPerson = sellPersonDAO.GetSellPersonByTelephone(sellInfo.getSellPerson().getTelephone());
            sellInfo.setSellPerson(sellPerson);
            sellInfoDAO.AddSellInfo(sellInfo);
            ctx.put("message",  java.net.URLEncoder.encode("SellInfo��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SellInfo���ʧ��!"));
            return "error";
        }
    }

    /*��ѯSellInfo��Ϣ*/
    public String QuerySellInfo() {
        if(currentPage == 0) currentPage = 1;
        if(sellDate == null) sellDate = "";
        List<SellInfo> sellInfoList = sellInfoDAO.QuerySellInfoInfo(productBarCode, sellPerson, sellDate, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        sellInfoDAO.CalculateTotalPageAndRecordNumber(productBarCode, sellPerson, sellDate);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = sellInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = sellInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("sellInfoList",  sellInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("productBarCode", productBarCode);
        List<ProductInfo> productInfoList = productInfoDAO.QueryAllProductInfoInfo();
        ctx.put("productInfoList", productInfoList);
        ctx.put("sellPerson", sellPerson);
        List<SellPerson> sellPersonList = sellPersonDAO.QueryAllSellPersonInfo();
        ctx.put("sellPersonList", sellPersonList);
        ctx.put("sellDate", sellDate);
        return "query_view";
    }

    /*��̨������excel*/
    public String QuerySellInfoOutputToExcel() { 
        if(sellDate == null) sellDate = "";
        List<SellInfo> sellInfoList = sellInfoDAO.QuerySellInfoInfo(productBarCode,sellPerson,sellDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "SellInfo��Ϣ��¼"; 
        String[] headers = { "��Ʒ����","������Ա","��������","��������"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<sellInfoList.size();i++) {
        	SellInfo sellInfo = sellInfoList.get(i); 
        	dataset.add(new String[]{sellInfo.getProductBarCode().getProductName(),
sellInfo.getSellPerson().getName(),
sellInfo.getSellCount() + "",new SimpleDateFormat("yyyy-MM-dd").format(sellInfo.getSellDate())});
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
			response.setHeader("Content-disposition","attachment; filename="+"SellInfo.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯSellInfo��Ϣ*/
    public String FrontQuerySellInfo() {
        if(currentPage == 0) currentPage = 1;
        if(sellDate == null) sellDate = "";
        List<SellInfo> sellInfoList = sellInfoDAO.QuerySellInfoInfo(productBarCode, sellPerson, sellDate, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        sellInfoDAO.CalculateTotalPageAndRecordNumber(productBarCode, sellPerson, sellDate);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = sellInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = sellInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("sellInfoList",  sellInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("productBarCode", productBarCode);
        List<ProductInfo> productInfoList = productInfoDAO.QueryAllProductInfoInfo();
        ctx.put("productInfoList", productInfoList);
        ctx.put("sellPerson", sellPerson);
        List<SellPerson> sellPersonList = sellPersonDAO.QueryAllSellPersonInfo();
        ctx.put("sellPersonList", sellPersonList);
        ctx.put("sellDate", sellDate);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�SellInfo��Ϣ*/
    public String ModifySellInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������sellId��ȡSellInfo����*/
        SellInfo sellInfo = sellInfoDAO.GetSellInfoBySellId(sellId);

        List<ProductInfo> productInfoList = productInfoDAO.QueryAllProductInfoInfo();
        ctx.put("productInfoList", productInfoList);
        List<SellPerson> sellPersonList = sellPersonDAO.QueryAllSellPersonInfo();
        ctx.put("sellPersonList", sellPersonList);
        ctx.put("sellInfo",  sellInfo);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�SellInfo��Ϣ*/
    public String FrontShowSellInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������sellId��ȡSellInfo����*/
        SellInfo sellInfo = sellInfoDAO.GetSellInfoBySellId(sellId);

        List<ProductInfo> productInfoList = productInfoDAO.QueryAllProductInfoInfo();
        ctx.put("productInfoList", productInfoList);
        List<SellPerson> sellPersonList = sellPersonDAO.QueryAllSellPersonInfo();
        ctx.put("sellPersonList", sellPersonList);
        ctx.put("sellInfo",  sellInfo);
        return "front_show_view";
    }

    /*�����޸�SellInfo��Ϣ*/
    public String ModifySellInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            ProductInfo productBarCode = productInfoDAO.GetProductInfoByBarCode(sellInfo.getProductBarCode().getBarCode());
            sellInfo.setProductBarCode(productBarCode);
            SellPerson sellPerson = sellPersonDAO.GetSellPersonByTelephone(sellInfo.getSellPerson().getTelephone());
            sellInfo.setSellPerson(sellPerson);
            sellInfoDAO.UpdateSellInfo(sellInfo);
            ctx.put("message",  java.net.URLEncoder.encode("SellInfo��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SellInfo��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��SellInfo��Ϣ*/
    public String DeleteSellInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            sellInfoDAO.DeleteSellInfo(sellId);
            ctx.put("message",  java.net.URLEncoder.encode("SellInfoɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SellInfoɾ��ʧ��!"));
            return "error";
        }
    }

}
