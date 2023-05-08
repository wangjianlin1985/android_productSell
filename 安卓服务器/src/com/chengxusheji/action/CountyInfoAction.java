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
import com.chengxusheji.dao.CountyInfoDAO;
import com.chengxusheji.domain.CountyInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class CountyInfoAction extends BaseAction {

    /*界面层需要查询的属性: 县市名称*/
    private String cityName;
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getCityName() {
        return this.cityName;
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

    private int cityId;
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
    public int getCityId() {
        return cityId;
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
    @Resource CountyInfoDAO countyInfoDAO;

    /*待操作的CountyInfo对象*/
    private CountyInfo countyInfo;
    public void setCountyInfo(CountyInfo countyInfo) {
        this.countyInfo = countyInfo;
    }
    public CountyInfo getCountyInfo() {
        return this.countyInfo;
    }

    /*跳转到添加CountyInfo视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*添加CountyInfo信息*/
    @SuppressWarnings("deprecation")
    public String AddCountyInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            countyInfoDAO.AddCountyInfo(countyInfo);
            ctx.put("message",  java.net.URLEncoder.encode("CountyInfo添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("CountyInfo添加失败!"));
            return "error";
        }
    }

    /*查询CountyInfo信息*/
    public String QueryCountyInfo() {
        if(currentPage == 0) currentPage = 1;
        if(cityName == null) cityName = "";
        List<CountyInfo> countyInfoList = countyInfoDAO.QueryCountyInfoInfo(cityName, currentPage);
        /*计算总的页数和总的记录数*/
        countyInfoDAO.CalculateTotalPageAndRecordNumber(cityName);
        /*获取到总的页码数目*/
        totalPage = countyInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = countyInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("countyInfoList",  countyInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("cityName", cityName);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryCountyInfoOutputToExcel() { 
        if(cityName == null) cityName = "";
        List<CountyInfo> countyInfoList = countyInfoDAO.QueryCountyInfoInfo(cityName);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "CountyInfo信息记录"; 
        String[] headers = { "县市编号","县市名称"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<countyInfoList.size();i++) {
        	CountyInfo countyInfo = countyInfoList.get(i); 
        	dataset.add(new String[]{countyInfo.getCityId() + "",countyInfo.getCityName()});
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
			response.setHeader("Content-disposition","attachment; filename="+"CountyInfo.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询CountyInfo信息*/
    public String FrontQueryCountyInfo() {
        if(currentPage == 0) currentPage = 1;
        if(cityName == null) cityName = "";
        List<CountyInfo> countyInfoList = countyInfoDAO.QueryCountyInfoInfo(cityName, currentPage);
        /*计算总的页数和总的记录数*/
        countyInfoDAO.CalculateTotalPageAndRecordNumber(cityName);
        /*获取到总的页码数目*/
        totalPage = countyInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = countyInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("countyInfoList",  countyInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("cityName", cityName);
        return "front_query_view";
    }

    /*查询要修改的CountyInfo信息*/
    public String ModifyCountyInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键cityId获取CountyInfo对象*/
        CountyInfo countyInfo = countyInfoDAO.GetCountyInfoByCityId(cityId);

        ctx.put("countyInfo",  countyInfo);
        return "modify_view";
    }

    /*查询要修改的CountyInfo信息*/
    public String FrontShowCountyInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键cityId获取CountyInfo对象*/
        CountyInfo countyInfo = countyInfoDAO.GetCountyInfoByCityId(cityId);

        ctx.put("countyInfo",  countyInfo);
        return "front_show_view";
    }

    /*更新修改CountyInfo信息*/
    public String ModifyCountyInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            countyInfoDAO.UpdateCountyInfo(countyInfo);
            ctx.put("message",  java.net.URLEncoder.encode("CountyInfo信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("CountyInfo信息更新失败!"));
            return "error";
       }
   }

    /*删除CountyInfo信息*/
    public String DeleteCountyInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            countyInfoDAO.DeleteCountyInfo(cityId);
            ctx.put("message",  java.net.URLEncoder.encode("CountyInfo删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("CountyInfo删除失败!"));
            return "error";
        }
    }

}
