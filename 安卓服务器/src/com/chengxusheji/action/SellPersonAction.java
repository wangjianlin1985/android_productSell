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
import com.chengxusheji.dao.SellPersonDAO;
import com.chengxusheji.domain.SellPerson;
import com.chengxusheji.dao.CountyInfoDAO;
import com.chengxusheji.domain.CountyInfo;
import com.chengxusheji.dao.TownInfoDAO;
import com.chengxusheji.domain.TownInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class SellPersonAction extends BaseAction {

    /*界面层需要查询的属性: 手机号*/
    private String telephone;
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getTelephone() {
        return this.telephone;
    }

    /*界面层需要查询的属性: 姓名*/
    private String name;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    /*界面层需要查询的属性: 所在县市*/
    private CountyInfo countyId;
    public void setCountyId(CountyInfo countyId) {
        this.countyId = countyId;
    }
    public CountyInfo getCountyId() {
        return this.countyId;
    }

    /*界面层需要查询的属性: 所在乡镇*/
    private TownInfo townId;
    public void setTownId(TownInfo townId) {
        this.townId = townId;
    }
    public TownInfo getTownId() {
        return this.townId;
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
    @Resource CountyInfoDAO countyInfoDAO;
    @Resource TownInfoDAO townInfoDAO;
    @Resource SellPersonDAO sellPersonDAO;

    /*待操作的SellPerson对象*/
    private SellPerson sellPerson;
    public void setSellPerson(SellPerson sellPerson) {
        this.sellPerson = sellPerson;
    }
    public SellPerson getSellPerson() {
        return this.sellPerson;
    }

    /*跳转到添加SellPerson视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的CountyInfo信息*/
        List<CountyInfo> countyInfoList = countyInfoDAO.QueryAllCountyInfoInfo();
        ctx.put("countyInfoList", countyInfoList);
        /*查询所有的TownInfo信息*/
        List<TownInfo> townInfoList = townInfoDAO.QueryAllTownInfoInfo();
        ctx.put("townInfoList", townInfoList);
        return "add_view";
    }

    /*添加SellPerson信息*/
    @SuppressWarnings("deprecation")
    public String AddSellPerson() {
        ActionContext ctx = ActionContext.getContext();
        /*验证手机号是否已经存在*/
        String telephone = sellPerson.getTelephone();
        SellPerson db_sellPerson = sellPersonDAO.GetSellPersonByTelephone(telephone);
        if(null != db_sellPerson) {
            ctx.put("error",  java.net.URLEncoder.encode("该手机号已经存在!"));
            return "error";
        }
        try {
            CountyInfo countyId = countyInfoDAO.GetCountyInfoByCityId(sellPerson.getCountyId().getCityId());
            sellPerson.setCountyId(countyId);
            TownInfo townId = townInfoDAO.GetTownInfoByTownId(sellPerson.getTownId().getTownId());
            sellPerson.setTownId(townId);
            sellPersonDAO.AddSellPerson(sellPerson);
            ctx.put("message",  java.net.URLEncoder.encode("SellPerson添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SellPerson添加失败!"));
            return "error";
        }
    }

    /*查询SellPerson信息*/
    public String QuerySellPerson() {
        if(currentPage == 0) currentPage = 1;
        if(telephone == null) telephone = "";
        if(name == null) name = "";
        List<SellPerson> sellPersonList = sellPersonDAO.QuerySellPersonInfo(telephone, name, countyId, townId, currentPage);
        /*计算总的页数和总的记录数*/
        sellPersonDAO.CalculateTotalPageAndRecordNumber(telephone, name, countyId, townId);
        /*获取到总的页码数目*/
        totalPage = sellPersonDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = sellPersonDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("sellPersonList",  sellPersonList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("telephone", telephone);
        ctx.put("name", name);
        ctx.put("countyId", countyId);
        List<CountyInfo> countyInfoList = countyInfoDAO.QueryAllCountyInfoInfo();
        ctx.put("countyInfoList", countyInfoList);
        ctx.put("townId", townId);
        List<TownInfo> townInfoList = townInfoDAO.QueryAllTownInfoInfo();
        ctx.put("townInfoList", townInfoList);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QuerySellPersonOutputToExcel() { 
        if(telephone == null) telephone = "";
        if(name == null) name = "";
        List<SellPerson> sellPersonList = sellPersonDAO.QuerySellPersonInfo(telephone,name,countyId,townId);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "SellPerson信息记录"; 
        String[] headers = { "手机号","姓名","所在县市","所在乡镇"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<sellPersonList.size();i++) {
        	SellPerson sellPerson = sellPersonList.get(i); 
        	dataset.add(new String[]{sellPerson.getTelephone(),sellPerson.getName(),sellPerson.getCountyId().getCityName(),
sellPerson.getTownId().getTownName()
});
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
			response.setHeader("Content-disposition","attachment; filename="+"SellPerson.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询SellPerson信息*/
    public String FrontQuerySellPerson() {
        if(currentPage == 0) currentPage = 1;
        if(telephone == null) telephone = "";
        if(name == null) name = "";
        List<SellPerson> sellPersonList = sellPersonDAO.QuerySellPersonInfo(telephone, name, countyId, townId, currentPage);
        /*计算总的页数和总的记录数*/
        sellPersonDAO.CalculateTotalPageAndRecordNumber(telephone, name, countyId, townId);
        /*获取到总的页码数目*/
        totalPage = sellPersonDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = sellPersonDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("sellPersonList",  sellPersonList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("telephone", telephone);
        ctx.put("name", name);
        ctx.put("countyId", countyId);
        List<CountyInfo> countyInfoList = countyInfoDAO.QueryAllCountyInfoInfo();
        ctx.put("countyInfoList", countyInfoList);
        ctx.put("townId", townId);
        List<TownInfo> townInfoList = townInfoDAO.QueryAllTownInfoInfo();
        ctx.put("townInfoList", townInfoList);
        return "front_query_view";
    }

    /*查询要修改的SellPerson信息*/
    public String ModifySellPersonQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键telephone获取SellPerson对象*/
        SellPerson sellPerson = sellPersonDAO.GetSellPersonByTelephone(telephone);

        List<CountyInfo> countyInfoList = countyInfoDAO.QueryAllCountyInfoInfo();
        ctx.put("countyInfoList", countyInfoList);
        List<TownInfo> townInfoList = townInfoDAO.QueryAllTownInfoInfo();
        ctx.put("townInfoList", townInfoList);
        ctx.put("sellPerson",  sellPerson);
        return "modify_view";
    }

    /*查询要修改的SellPerson信息*/
    public String FrontShowSellPersonQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键telephone获取SellPerson对象*/
        SellPerson sellPerson = sellPersonDAO.GetSellPersonByTelephone(telephone);

        List<CountyInfo> countyInfoList = countyInfoDAO.QueryAllCountyInfoInfo();
        ctx.put("countyInfoList", countyInfoList);
        List<TownInfo> townInfoList = townInfoDAO.QueryAllTownInfoInfo();
        ctx.put("townInfoList", townInfoList);
        ctx.put("sellPerson",  sellPerson);
        return "front_show_view";
    }

    /*更新修改SellPerson信息*/
    public String ModifySellPerson() {
        ActionContext ctx = ActionContext.getContext();
        try {
            CountyInfo countyId = countyInfoDAO.GetCountyInfoByCityId(sellPerson.getCountyId().getCityId());
            sellPerson.setCountyId(countyId);
            TownInfo townId = townInfoDAO.GetTownInfoByTownId(sellPerson.getTownId().getTownId());
            sellPerson.setTownId(townId);
            sellPersonDAO.UpdateSellPerson(sellPerson);
            ctx.put("message",  java.net.URLEncoder.encode("SellPerson信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SellPerson信息更新失败!"));
            return "error";
       }
   }

    /*删除SellPerson信息*/
    public String DeleteSellPerson() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            sellPersonDAO.DeleteSellPerson(telephone);
            ctx.put("message",  java.net.URLEncoder.encode("SellPerson删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SellPerson删除失败!"));
            return "error";
        }
    }

}
