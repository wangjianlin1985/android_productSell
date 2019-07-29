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
import com.chengxusheji.dao.TownInfoDAO;
import com.chengxusheji.domain.TownInfo;
import com.chengxusheji.dao.CountyInfoDAO;
import com.chengxusheji.domain.CountyInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class TownInfoAction extends BaseAction {

    /*界面层需要查询的属性: 所在县市*/
    private CountyInfo countyId;
    public void setCountyId(CountyInfo countyId) {
        this.countyId = countyId;
    }
    public CountyInfo getCountyId() {
        return this.countyId;
    }

    /*界面层需要查询的属性: 乡镇名称*/
    private String townName;
    public void setTownName(String townName) {
        this.townName = townName;
    }
    public String getTownName() {
        return this.townName;
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

    private int townId;
    public void setTownId(int townId) {
        this.townId = townId;
    }
    public int getTownId() {
        return townId;
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
    @Resource TownInfoDAO townInfoDAO;

    /*待操作的TownInfo对象*/
    private TownInfo townInfo;
    public void setTownInfo(TownInfo townInfo) {
        this.townInfo = townInfo;
    }
    public TownInfo getTownInfo() {
        return this.townInfo;
    }

    /*跳转到添加TownInfo视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的CountyInfo信息*/
        List<CountyInfo> countyInfoList = countyInfoDAO.QueryAllCountyInfoInfo();
        ctx.put("countyInfoList", countyInfoList);
        return "add_view";
    }

    /*添加TownInfo信息*/
    @SuppressWarnings("deprecation")
    public String AddTownInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            CountyInfo countyId = countyInfoDAO.GetCountyInfoByCityId(townInfo.getCountyId().getCityId());
            townInfo.setCountyId(countyId);
            townInfoDAO.AddTownInfo(townInfo);
            ctx.put("message",  java.net.URLEncoder.encode("TownInfo添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("TownInfo添加失败!"));
            return "error";
        }
    }

    /*查询TownInfo信息*/
    public String QueryTownInfo() {
        if(currentPage == 0) currentPage = 1;
        if(townName == null) townName = "";
        List<TownInfo> townInfoList = townInfoDAO.QueryTownInfoInfo(countyId, townName, currentPage);
        /*计算总的页数和总的记录数*/
        townInfoDAO.CalculateTotalPageAndRecordNumber(countyId, townName);
        /*获取到总的页码数目*/
        totalPage = townInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = townInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("townInfoList",  townInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("countyId", countyId);
        List<CountyInfo> countyInfoList = countyInfoDAO.QueryAllCountyInfoInfo();
        ctx.put("countyInfoList", countyInfoList);
        ctx.put("townName", townName);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryTownInfoOutputToExcel() { 
        if(townName == null) townName = "";
        List<TownInfo> townInfoList = townInfoDAO.QueryTownInfoInfo(countyId,townName);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "TownInfo信息记录"; 
        String[] headers = { "乡镇编号","所在县市","乡镇名称"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<townInfoList.size();i++) {
        	TownInfo townInfo = townInfoList.get(i); 
        	dataset.add(new String[]{townInfo.getTownId() + "",townInfo.getCountyId().getCityName(),
townInfo.getTownName()});
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
			response.setHeader("Content-disposition","attachment; filename="+"TownInfo.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询TownInfo信息*/
    public String FrontQueryTownInfo() {
        if(currentPage == 0) currentPage = 1;
        if(townName == null) townName = "";
        List<TownInfo> townInfoList = townInfoDAO.QueryTownInfoInfo(countyId, townName, currentPage);
        /*计算总的页数和总的记录数*/
        townInfoDAO.CalculateTotalPageAndRecordNumber(countyId, townName);
        /*获取到总的页码数目*/
        totalPage = townInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = townInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("townInfoList",  townInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("countyId", countyId);
        List<CountyInfo> countyInfoList = countyInfoDAO.QueryAllCountyInfoInfo();
        ctx.put("countyInfoList", countyInfoList);
        ctx.put("townName", townName);
        return "front_query_view";
    }

    /*查询要修改的TownInfo信息*/
    public String ModifyTownInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键townId获取TownInfo对象*/
        TownInfo townInfo = townInfoDAO.GetTownInfoByTownId(townId);

        List<CountyInfo> countyInfoList = countyInfoDAO.QueryAllCountyInfoInfo();
        ctx.put("countyInfoList", countyInfoList);
        ctx.put("townInfo",  townInfo);
        return "modify_view";
    }

    /*查询要修改的TownInfo信息*/
    public String FrontShowTownInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键townId获取TownInfo对象*/
        TownInfo townInfo = townInfoDAO.GetTownInfoByTownId(townId);

        List<CountyInfo> countyInfoList = countyInfoDAO.QueryAllCountyInfoInfo();
        ctx.put("countyInfoList", countyInfoList);
        ctx.put("townInfo",  townInfo);
        return "front_show_view";
    }

    /*更新修改TownInfo信息*/
    public String ModifyTownInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            CountyInfo countyId = countyInfoDAO.GetCountyInfoByCityId(townInfo.getCountyId().getCityId());
            townInfo.setCountyId(countyId);
            townInfoDAO.UpdateTownInfo(townInfo);
            ctx.put("message",  java.net.URLEncoder.encode("TownInfo信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("TownInfo信息更新失败!"));
            return "error";
       }
   }

    /*删除TownInfo信息*/
    public String DeleteTownInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            townInfoDAO.DeleteTownInfo(townId);
            ctx.put("message",  java.net.URLEncoder.encode("TownInfo删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("TownInfo删除失败!"));
            return "error";
        }
    }

}
