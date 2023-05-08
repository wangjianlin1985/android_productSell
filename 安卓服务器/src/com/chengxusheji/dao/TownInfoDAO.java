package com.chengxusheji.dao;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.chengxusheji.domain.CountyInfo;
import com.chengxusheji.domain.TownInfo;

@Service @Transactional
public class TownInfoDAO {

	@Resource SessionFactory factory;
    /*ÿҳ��ʾ��¼��Ŀ*/
    private final int PAGE_SIZE = 10;

    /*�����ѯ���ܵ�ҳ��*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*�����ѯ�����ܼ�¼��*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*���ͼ����Ϣ*/
    public void AddTownInfo(TownInfo townInfo) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(townInfo);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<TownInfo> QueryTownInfoInfo(CountyInfo countyId,String townName,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From TownInfo townInfo where 1=1";
    	if(null != countyId && countyId.getCityId()!=0) hql += " and townInfo.countyId.cityId=" + countyId.getCityId();
    	if(!townName.equals("")) hql = hql + " and townInfo.townName like '%" + townName + "%'";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List townInfoList = q.list();
    	return (ArrayList<TownInfo>) townInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<TownInfo> QueryTownInfoInfo(CountyInfo countyId,String townName) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From TownInfo townInfo where 1=1";
    	if(null != countyId && countyId.getCityId()!=0) hql += " and townInfo.countyId.cityId=" + countyId.getCityId();
    	if(!townName.equals("")) hql = hql + " and townInfo.townName like '%" + townName + "%'";
    	Query q = s.createQuery(hql);
    	List townInfoList = q.list();
    	return (ArrayList<TownInfo>) townInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<TownInfo> QueryAllTownInfoInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From TownInfo";
        Query q = s.createQuery(hql);
        List townInfoList = q.list();
        return (ArrayList<TownInfo>) townInfoList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(CountyInfo countyId,String townName) {
        Session s = factory.getCurrentSession();
        String hql = "From TownInfo townInfo where 1=1";
        if(null != countyId && countyId.getCityId()!=0) hql += " and townInfo.countyId.cityId=" + countyId.getCityId();
        if(!townName.equals("")) hql = hql + " and townInfo.townName like '%" + townName + "%'";
        Query q = s.createQuery(hql);
        List townInfoList = q.list();
        recordNumber = townInfoList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public TownInfo GetTownInfoByTownId(int townId) {
        Session s = factory.getCurrentSession();
        TownInfo townInfo = (TownInfo)s.get(TownInfo.class, townId);
        return townInfo;
    }

    /*����TownInfo��Ϣ*/
    public void UpdateTownInfo(TownInfo townInfo) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(townInfo);
    }

    /*ɾ��TownInfo��Ϣ*/
    public void DeleteTownInfo (int townId) throws Exception {
        Session s = factory.getCurrentSession();
        Object townInfo = s.load(TownInfo.class, townId);
        s.delete(townInfo);
    }

}
