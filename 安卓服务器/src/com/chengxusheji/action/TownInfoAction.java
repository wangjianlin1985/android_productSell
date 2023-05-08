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

    /*�������Ҫ��ѯ������: ��������*/
    private CountyInfo countyId;
    public void setCountyId(CountyInfo countyId) {
        this.countyId = countyId;
    }
    public CountyInfo getCountyId() {
        return this.countyId;
    }

    /*�������Ҫ��ѯ������: ��������*/
    private String townName;
    public void setTownName(String townName) {
        this.townName = townName;
    }
    public String getTownName() {
        return this.townName;
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

    private int townId;
    public void setTownId(int townId) {
        this.townId = townId;
    }
    public int getTownId() {
        return townId;
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
    @Resource CountyInfoDAO countyInfoDAO;
    @Resource TownInfoDAO townInfoDAO;

    /*��������TownInfo����*/
    private TownInfo townInfo;
    public void setTownInfo(TownInfo townInfo) {
        this.townInfo = townInfo;
    }
    public TownInfo getTownInfo() {
        return this.townInfo;
    }

    /*��ת�����TownInfo��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�CountyInfo��Ϣ*/
        List<CountyInfo> countyInfoList = countyInfoDAO.QueryAllCountyInfoInfo();
        ctx.put("countyInfoList", countyInfoList);
        return "add_view";
    }

    /*���TownInfo��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddTownInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            CountyInfo countyId = countyInfoDAO.GetCountyInfoByCityId(townInfo.getCountyId().getCityId());
            townInfo.setCountyId(countyId);
            townInfoDAO.AddTownInfo(townInfo);
            ctx.put("message",  java.net.URLEncoder.encode("TownInfo��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("TownInfo���ʧ��!"));
            return "error";
        }
    }

    /*��ѯTownInfo��Ϣ*/
    public String QueryTownInfo() {
        if(currentPage == 0) currentPage = 1;
        if(townName == null) townName = "";
        List<TownInfo> townInfoList = townInfoDAO.QueryTownInfoInfo(countyId, townName, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        townInfoDAO.CalculateTotalPageAndRecordNumber(countyId, townName);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = townInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��̨������excel*/
    public String QueryTownInfoOutputToExcel() { 
        if(townName == null) townName = "";
        List<TownInfo> townInfoList = townInfoDAO.QueryTownInfoInfo(countyId,townName);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "TownInfo��Ϣ��¼"; 
        String[] headers = { "������","��������","��������"};
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
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"TownInfo.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯTownInfo��Ϣ*/
    public String FrontQueryTownInfo() {
        if(currentPage == 0) currentPage = 1;
        if(townName == null) townName = "";
        List<TownInfo> townInfoList = townInfoDAO.QueryTownInfoInfo(countyId, townName, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        townInfoDAO.CalculateTotalPageAndRecordNumber(countyId, townName);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = townInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��ѯҪ�޸ĵ�TownInfo��Ϣ*/
    public String ModifyTownInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������townId��ȡTownInfo����*/
        TownInfo townInfo = townInfoDAO.GetTownInfoByTownId(townId);

        List<CountyInfo> countyInfoList = countyInfoDAO.QueryAllCountyInfoInfo();
        ctx.put("countyInfoList", countyInfoList);
        ctx.put("townInfo",  townInfo);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�TownInfo��Ϣ*/
    public String FrontShowTownInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������townId��ȡTownInfo����*/
        TownInfo townInfo = townInfoDAO.GetTownInfoByTownId(townId);

        List<CountyInfo> countyInfoList = countyInfoDAO.QueryAllCountyInfoInfo();
        ctx.put("countyInfoList", countyInfoList);
        ctx.put("townInfo",  townInfo);
        return "front_show_view";
    }

    /*�����޸�TownInfo��Ϣ*/
    public String ModifyTownInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            CountyInfo countyId = countyInfoDAO.GetCountyInfoByCityId(townInfo.getCountyId().getCityId());
            townInfo.setCountyId(countyId);
            townInfoDAO.UpdateTownInfo(townInfo);
            ctx.put("message",  java.net.URLEncoder.encode("TownInfo��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("TownInfo��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��TownInfo��Ϣ*/
    public String DeleteTownInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            townInfoDAO.DeleteTownInfo(townId);
            ctx.put("message",  java.net.URLEncoder.encode("TownInfoɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("TownInfoɾ��ʧ��!"));
            return "error";
        }
    }

}
