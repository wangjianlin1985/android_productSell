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

    /*�������Ҫ��ѯ������: ��������*/
    private String cityName;
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getCityName() {
        return this.cityName;
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

    private int cityId;
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
    public int getCityId() {
        return cityId;
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

    /*��������CountyInfo����*/
    private CountyInfo countyInfo;
    public void setCountyInfo(CountyInfo countyInfo) {
        this.countyInfo = countyInfo;
    }
    public CountyInfo getCountyInfo() {
        return this.countyInfo;
    }

    /*��ת�����CountyInfo��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*���CountyInfo��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddCountyInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            countyInfoDAO.AddCountyInfo(countyInfo);
            ctx.put("message",  java.net.URLEncoder.encode("CountyInfo��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("CountyInfo���ʧ��!"));
            return "error";
        }
    }

    /*��ѯCountyInfo��Ϣ*/
    public String QueryCountyInfo() {
        if(currentPage == 0) currentPage = 1;
        if(cityName == null) cityName = "";
        List<CountyInfo> countyInfoList = countyInfoDAO.QueryCountyInfoInfo(cityName, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        countyInfoDAO.CalculateTotalPageAndRecordNumber(cityName);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = countyInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = countyInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("countyInfoList",  countyInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("cityName", cityName);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryCountyInfoOutputToExcel() { 
        if(cityName == null) cityName = "";
        List<CountyInfo> countyInfoList = countyInfoDAO.QueryCountyInfoInfo(cityName);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "CountyInfo��Ϣ��¼"; 
        String[] headers = { "���б��","��������"};
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
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"CountyInfo.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯCountyInfo��Ϣ*/
    public String FrontQueryCountyInfo() {
        if(currentPage == 0) currentPage = 1;
        if(cityName == null) cityName = "";
        List<CountyInfo> countyInfoList = countyInfoDAO.QueryCountyInfoInfo(cityName, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        countyInfoDAO.CalculateTotalPageAndRecordNumber(cityName);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = countyInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = countyInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("countyInfoList",  countyInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("cityName", cityName);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�CountyInfo��Ϣ*/
    public String ModifyCountyInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������cityId��ȡCountyInfo����*/
        CountyInfo countyInfo = countyInfoDAO.GetCountyInfoByCityId(cityId);

        ctx.put("countyInfo",  countyInfo);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�CountyInfo��Ϣ*/
    public String FrontShowCountyInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������cityId��ȡCountyInfo����*/
        CountyInfo countyInfo = countyInfoDAO.GetCountyInfoByCityId(cityId);

        ctx.put("countyInfo",  countyInfo);
        return "front_show_view";
    }

    /*�����޸�CountyInfo��Ϣ*/
    public String ModifyCountyInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            countyInfoDAO.UpdateCountyInfo(countyInfo);
            ctx.put("message",  java.net.URLEncoder.encode("CountyInfo��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("CountyInfo��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��CountyInfo��Ϣ*/
    public String DeleteCountyInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            countyInfoDAO.DeleteCountyInfo(cityId);
            ctx.put("message",  java.net.URLEncoder.encode("CountyInfoɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("CountyInfoɾ��ʧ��!"));
            return "error";
        }
    }

}
