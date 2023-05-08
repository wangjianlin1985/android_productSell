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

    /*�������Ҫ��ѯ������: �ֻ���*/
    private String telephone;
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getTelephone() {
        return this.telephone;
    }

    /*�������Ҫ��ѯ������: ����*/
    private String name;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    /*�������Ҫ��ѯ������: ��������*/
    private CountyInfo countyId;
    public void setCountyId(CountyInfo countyId) {
        this.countyId = countyId;
    }
    public CountyInfo getCountyId() {
        return this.countyId;
    }

    /*�������Ҫ��ѯ������: ��������*/
    private TownInfo townId;
    public void setTownId(TownInfo townId) {
        this.townId = townId;
    }
    public TownInfo getTownId() {
        return this.townId;
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
    @Resource CountyInfoDAO countyInfoDAO;
    @Resource TownInfoDAO townInfoDAO;
    @Resource SellPersonDAO sellPersonDAO;

    /*��������SellPerson����*/
    private SellPerson sellPerson;
    public void setSellPerson(SellPerson sellPerson) {
        this.sellPerson = sellPerson;
    }
    public SellPerson getSellPerson() {
        return this.sellPerson;
    }

    /*��ת�����SellPerson��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�CountyInfo��Ϣ*/
        List<CountyInfo> countyInfoList = countyInfoDAO.QueryAllCountyInfoInfo();
        ctx.put("countyInfoList", countyInfoList);
        /*��ѯ���е�TownInfo��Ϣ*/
        List<TownInfo> townInfoList = townInfoDAO.QueryAllTownInfoInfo();
        ctx.put("townInfoList", townInfoList);
        return "add_view";
    }

    /*���SellPerson��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddSellPerson() {
        ActionContext ctx = ActionContext.getContext();
        /*��֤�ֻ����Ƿ��Ѿ�����*/
        String telephone = sellPerson.getTelephone();
        SellPerson db_sellPerson = sellPersonDAO.GetSellPersonByTelephone(telephone);
        if(null != db_sellPerson) {
            ctx.put("error",  java.net.URLEncoder.encode("���ֻ����Ѿ�����!"));
            return "error";
        }
        try {
            CountyInfo countyId = countyInfoDAO.GetCountyInfoByCityId(sellPerson.getCountyId().getCityId());
            sellPerson.setCountyId(countyId);
            TownInfo townId = townInfoDAO.GetTownInfoByTownId(sellPerson.getTownId().getTownId());
            sellPerson.setTownId(townId);
            sellPersonDAO.AddSellPerson(sellPerson);
            ctx.put("message",  java.net.URLEncoder.encode("SellPerson��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SellPerson���ʧ��!"));
            return "error";
        }
    }

    /*��ѯSellPerson��Ϣ*/
    public String QuerySellPerson() {
        if(currentPage == 0) currentPage = 1;
        if(telephone == null) telephone = "";
        if(name == null) name = "";
        List<SellPerson> sellPersonList = sellPersonDAO.QuerySellPersonInfo(telephone, name, countyId, townId, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        sellPersonDAO.CalculateTotalPageAndRecordNumber(telephone, name, countyId, townId);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = sellPersonDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��̨������excel*/
    public String QuerySellPersonOutputToExcel() { 
        if(telephone == null) telephone = "";
        if(name == null) name = "";
        List<SellPerson> sellPersonList = sellPersonDAO.QuerySellPersonInfo(telephone,name,countyId,townId);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "SellPerson��Ϣ��¼"; 
        String[] headers = { "�ֻ���","����","��������","��������"};
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
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"SellPerson.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯSellPerson��Ϣ*/
    public String FrontQuerySellPerson() {
        if(currentPage == 0) currentPage = 1;
        if(telephone == null) telephone = "";
        if(name == null) name = "";
        List<SellPerson> sellPersonList = sellPersonDAO.QuerySellPersonInfo(telephone, name, countyId, townId, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        sellPersonDAO.CalculateTotalPageAndRecordNumber(telephone, name, countyId, townId);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = sellPersonDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��ѯҪ�޸ĵ�SellPerson��Ϣ*/
    public String ModifySellPersonQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������telephone��ȡSellPerson����*/
        SellPerson sellPerson = sellPersonDAO.GetSellPersonByTelephone(telephone);

        List<CountyInfo> countyInfoList = countyInfoDAO.QueryAllCountyInfoInfo();
        ctx.put("countyInfoList", countyInfoList);
        List<TownInfo> townInfoList = townInfoDAO.QueryAllTownInfoInfo();
        ctx.put("townInfoList", townInfoList);
        ctx.put("sellPerson",  sellPerson);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�SellPerson��Ϣ*/
    public String FrontShowSellPersonQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������telephone��ȡSellPerson����*/
        SellPerson sellPerson = sellPersonDAO.GetSellPersonByTelephone(telephone);

        List<CountyInfo> countyInfoList = countyInfoDAO.QueryAllCountyInfoInfo();
        ctx.put("countyInfoList", countyInfoList);
        List<TownInfo> townInfoList = townInfoDAO.QueryAllTownInfoInfo();
        ctx.put("townInfoList", townInfoList);
        ctx.put("sellPerson",  sellPerson);
        return "front_show_view";
    }

    /*�����޸�SellPerson��Ϣ*/
    public String ModifySellPerson() {
        ActionContext ctx = ActionContext.getContext();
        try {
            CountyInfo countyId = countyInfoDAO.GetCountyInfoByCityId(sellPerson.getCountyId().getCityId());
            sellPerson.setCountyId(countyId);
            TownInfo townId = townInfoDAO.GetTownInfoByTownId(sellPerson.getTownId().getTownId());
            sellPerson.setTownId(townId);
            sellPersonDAO.UpdateSellPerson(sellPerson);
            ctx.put("message",  java.net.URLEncoder.encode("SellPerson��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SellPerson��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��SellPerson��Ϣ*/
    public String DeleteSellPerson() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            sellPersonDAO.DeleteSellPerson(telephone);
            ctx.put("message",  java.net.URLEncoder.encode("SellPersonɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SellPersonɾ��ʧ��!"));
            return "error";
        }
    }

}
