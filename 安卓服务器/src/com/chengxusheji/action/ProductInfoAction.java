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

    /*界面层需要查询的属性: 产品条形码*/
    private String barCode;
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
    public String getBarCode() {
        return this.barCode;
    }

    /*界面层需要查询的属性: 所属类别*/
    private ProductClass classId;
    public void setClassId(ProductClass classId) {
        this.classId = classId;
    }
    public ProductClass getClassId() {
        return this.classId;
    }

    /*界面层需要查询的属性: 产品名称*/
    private String productName;
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductName() {
        return this.productName;
    }

    /*界面层需要查询的属性: 生产日期*/
    private String madeDate;
    public void setMadeDate(String madeDate) {
        this.madeDate = madeDate;
    }
    public String getMadeDate() {
        return this.madeDate;
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

    /*当前查询的总记录数目*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*业务层对象*/
    @Resource ProductClassDAO productClassDAO;
    @Resource ProductInfoDAO productInfoDAO;

    /*待操作的ProductInfo对象*/
    private ProductInfo productInfo;
    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }
    public ProductInfo getProductInfo() {
        return this.productInfo;
    }

    /*跳转到添加ProductInfo视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的ProductClass信息*/
        List<ProductClass> productClassList = productClassDAO.QueryAllProductClassInfo();
        ctx.put("productClassList", productClassList);
        return "add_view";
    }

    /*添加ProductInfo信息*/
    @SuppressWarnings("deprecation")
    public String AddProductInfo() {
        ActionContext ctx = ActionContext.getContext();
        /*验证产品条形码是否已经存在*/
        String barCode = productInfo.getBarCode();
        ProductInfo db_productInfo = productInfoDAO.GetProductInfoByBarCode(barCode);
        if(null != db_productInfo) {
            ctx.put("error",  java.net.URLEncoder.encode("该产品条形码已经存在!"));
            return "error";
        }
        try {
            ProductClass classId = productClassDAO.GetProductClassByClassId(productInfo.getClassId().getClassId());
            productInfo.setClassId(classId);
            productInfoDAO.AddProductInfo(productInfo);
            ctx.put("message",  java.net.URLEncoder.encode("ProductInfo添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ProductInfo添加失败!"));
            return "error";
        }
    }

    /*查询ProductInfo信息*/
    public String QueryProductInfo() {
        if(currentPage == 0) currentPage = 1;
        if(barCode == null) barCode = "";
        if(productName == null) productName = "";
        if(madeDate == null) madeDate = "";
        List<ProductInfo> productInfoList = productInfoDAO.QueryProductInfoInfo(barCode, classId, productName, madeDate, currentPage);
        /*计算总的页数和总的记录数*/
        productInfoDAO.CalculateTotalPageAndRecordNumber(barCode, classId, productName, madeDate);
        /*获取到总的页码数目*/
        totalPage = productInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*后台导出到excel*/
    public String QueryProductInfoOutputToExcel() { 
        if(barCode == null) barCode = "";
        if(productName == null) productName = "";
        if(madeDate == null) madeDate = "";
        List<ProductInfo> productInfoList = productInfoDAO.QueryProductInfoInfo(barCode,classId,productName,madeDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "ProductInfo信息记录"; 
        String[] headers = { "产品条形码","所属类别","产品名称","生产日期"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"ProductInfo.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询ProductInfo信息*/
    public String FrontQueryProductInfo() {
        if(currentPage == 0) currentPage = 1;
        if(barCode == null) barCode = "";
        if(productName == null) productName = "";
        if(madeDate == null) madeDate = "";
        List<ProductInfo> productInfoList = productInfoDAO.QueryProductInfoInfo(barCode, classId, productName, madeDate, currentPage);
        /*计算总的页数和总的记录数*/
        productInfoDAO.CalculateTotalPageAndRecordNumber(barCode, classId, productName, madeDate);
        /*获取到总的页码数目*/
        totalPage = productInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*查询要修改的ProductInfo信息*/
    public String ModifyProductInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键barCode获取ProductInfo对象*/
        ProductInfo productInfo = productInfoDAO.GetProductInfoByBarCode(barCode);

        List<ProductClass> productClassList = productClassDAO.QueryAllProductClassInfo();
        ctx.put("productClassList", productClassList);
        ctx.put("productInfo",  productInfo);
        return "modify_view";
    }

    /*查询要修改的ProductInfo信息*/
    public String FrontShowProductInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键barCode获取ProductInfo对象*/
        ProductInfo productInfo = productInfoDAO.GetProductInfoByBarCode(barCode);

        List<ProductClass> productClassList = productClassDAO.QueryAllProductClassInfo();
        ctx.put("productClassList", productClassList);
        ctx.put("productInfo",  productInfo);
        return "front_show_view";
    }

    /*更新修改ProductInfo信息*/
    public String ModifyProductInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            ProductClass classId = productClassDAO.GetProductClassByClassId(productInfo.getClassId().getClassId());
            productInfo.setClassId(classId);
            productInfoDAO.UpdateProductInfo(productInfo);
            ctx.put("message",  java.net.URLEncoder.encode("ProductInfo信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ProductInfo信息更新失败!"));
            return "error";
       }
   }

    /*删除ProductInfo信息*/
    public String DeleteProductInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            productInfoDAO.DeleteProductInfo(barCode);
            ctx.put("message",  java.net.URLEncoder.encode("ProductInfo删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ProductInfo删除失败!"));
            return "error";
        }
    }

}
