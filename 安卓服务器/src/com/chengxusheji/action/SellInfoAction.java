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

    /*界面层需要查询的属性: 产品名称*/
    private ProductInfo productBarCode;
    public void setProductBarCode(ProductInfo productBarCode) {
        this.productBarCode = productBarCode;
    }
    public ProductInfo getProductBarCode() {
        return this.productBarCode;
    }

    /*界面层需要查询的属性: 销售人员*/
    private SellPerson sellPerson;
    public void setSellPerson(SellPerson sellPerson) {
        this.sellPerson = sellPerson;
    }
    public SellPerson getSellPerson() {
        return this.sellPerson;
    }

    /*界面层需要查询的属性: 销售日期*/
    private String sellDate;
    public void setSellDate(String sellDate) {
        this.sellDate = sellDate;
    }
    public String getSellDate() {
        return this.sellDate;
    }

    /*当前第几页*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*一共多少页*/
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

    /*当前查询的总记录数目*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*业务层对象*/
    @Resource ProductInfoDAO productInfoDAO;
    @Resource SellPersonDAO sellPersonDAO;
    @Resource SellInfoDAO sellInfoDAO;

    /*待操作的SellInfo对象*/
    private SellInfo sellInfo;
    public void setSellInfo(SellInfo sellInfo) {
        this.sellInfo = sellInfo;
    }
    public SellInfo getSellInfo() {
        return this.sellInfo;
    }

    /*跳转到添加SellInfo视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的ProductInfo信息*/
        List<ProductInfo> productInfoList = productInfoDAO.QueryAllProductInfoInfo();
        ctx.put("productInfoList", productInfoList);
        /*查询所有的SellPerson信息*/
        List<SellPerson> sellPersonList = sellPersonDAO.QueryAllSellPersonInfo();
        ctx.put("sellPersonList", sellPersonList);
        return "add_view";
    }

    /*添加SellInfo信息*/
    @SuppressWarnings("deprecation")
    public String AddSellInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            ProductInfo productBarCode = productInfoDAO.GetProductInfoByBarCode(sellInfo.getProductBarCode().getBarCode());
            sellInfo.setProductBarCode(productBarCode);
            SellPerson sellPerson = sellPersonDAO.GetSellPersonByTelephone(sellInfo.getSellPerson().getTelephone());
            sellInfo.setSellPerson(sellPerson);
            sellInfoDAO.AddSellInfo(sellInfo);
            ctx.put("message",  java.net.URLEncoder.encode("SellInfo添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SellInfo添加失败!"));
            return "error";
        }
    }

    /*查询SellInfo信息*/
    public String QuerySellInfo() {
        if(currentPage == 0) currentPage = 1;
        if(sellDate == null) sellDate = "";
        List<SellInfo> sellInfoList = sellInfoDAO.QuerySellInfoInfo(productBarCode, sellPerson, sellDate, currentPage);
        /*计算总的页数和总的记录数*/
        sellInfoDAO.CalculateTotalPageAndRecordNumber(productBarCode, sellPerson, sellDate);
        /*获取到总的页码数目*/
        totalPage = sellInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*后台导出到excel*/
    public String QuerySellInfoOutputToExcel() { 
        if(sellDate == null) sellDate = "";
        List<SellInfo> sellInfoList = sellInfoDAO.QuerySellInfoInfo(productBarCode,sellPerson,sellDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "SellInfo信息记录"; 
        String[] headers = { "产品名称","销售人员","销售数量","销售日期"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"SellInfo.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
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
    /*前台查询SellInfo信息*/
    public String FrontQuerySellInfo() {
        if(currentPage == 0) currentPage = 1;
        if(sellDate == null) sellDate = "";
        List<SellInfo> sellInfoList = sellInfoDAO.QuerySellInfoInfo(productBarCode, sellPerson, sellDate, currentPage);
        /*计算总的页数和总的记录数*/
        sellInfoDAO.CalculateTotalPageAndRecordNumber(productBarCode, sellPerson, sellDate);
        /*获取到总的页码数目*/
        totalPage = sellInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*查询要修改的SellInfo信息*/
    public String ModifySellInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键sellId获取SellInfo对象*/
        SellInfo sellInfo = sellInfoDAO.GetSellInfoBySellId(sellId);

        List<ProductInfo> productInfoList = productInfoDAO.QueryAllProductInfoInfo();
        ctx.put("productInfoList", productInfoList);
        List<SellPerson> sellPersonList = sellPersonDAO.QueryAllSellPersonInfo();
        ctx.put("sellPersonList", sellPersonList);
        ctx.put("sellInfo",  sellInfo);
        return "modify_view";
    }

    /*查询要修改的SellInfo信息*/
    public String FrontShowSellInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键sellId获取SellInfo对象*/
        SellInfo sellInfo = sellInfoDAO.GetSellInfoBySellId(sellId);

        List<ProductInfo> productInfoList = productInfoDAO.QueryAllProductInfoInfo();
        ctx.put("productInfoList", productInfoList);
        List<SellPerson> sellPersonList = sellPersonDAO.QueryAllSellPersonInfo();
        ctx.put("sellPersonList", sellPersonList);
        ctx.put("sellInfo",  sellInfo);
        return "front_show_view";
    }

    /*更新修改SellInfo信息*/
    public String ModifySellInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            ProductInfo productBarCode = productInfoDAO.GetProductInfoByBarCode(sellInfo.getProductBarCode().getBarCode());
            sellInfo.setProductBarCode(productBarCode);
            SellPerson sellPerson = sellPersonDAO.GetSellPersonByTelephone(sellInfo.getSellPerson().getTelephone());
            sellInfo.setSellPerson(sellPerson);
            sellInfoDAO.UpdateSellInfo(sellInfo);
            ctx.put("message",  java.net.URLEncoder.encode("SellInfo信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SellInfo信息更新失败!"));
            return "error";
       }
   }

    /*删除SellInfo信息*/
    public String DeleteSellInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            sellInfoDAO.DeleteSellInfo(sellId);
            ctx.put("message",  java.net.URLEncoder.encode("SellInfo删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SellInfo删除失败!"));
            return "error";
        }
    }

}
