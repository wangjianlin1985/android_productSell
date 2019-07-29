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
import com.chengxusheji.domain.SellPerson;

@Service @Transactional
public class SellPersonDAO {

	@Resource SessionFactory factory;
    /*每页显示记录数目*/
    private final int PAGE_SIZE = 10;

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加图书信息*/
    public void AddSellPerson(SellPerson sellPerson) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(sellPerson);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<SellPerson> QuerySellPersonInfo(String telephone,String name,CountyInfo countyId,TownInfo townId,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From SellPerson sellPerson where 1=1";
    	if(!telephone.equals("")) hql = hql + " and sellPerson.telephone like '%" + telephone + "%'";
    	if(!name.equals("")) hql = hql + " and sellPerson.name like '%" + name + "%'";
    	if(null != countyId && countyId.getCityId()!=0) hql += " and sellPerson.countyId.cityId=" + countyId.getCityId();
    	if(null != townId && townId.getTownId()!=0) hql += " and sellPerson.townId.townId=" + townId.getTownId();
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List sellPersonList = q.list();
    	return (ArrayList<SellPerson>) sellPersonList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<SellPerson> QuerySellPersonInfo(String telephone,String name,CountyInfo countyId,TownInfo townId) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From SellPerson sellPerson where 1=1";
    	if(!telephone.equals("")) hql = hql + " and sellPerson.telephone like '%" + telephone + "%'";
    	if(!name.equals("")) hql = hql + " and sellPerson.name like '%" + name + "%'";
    	if(null != countyId && countyId.getCityId()!=0) hql += " and sellPerson.countyId.cityId=" + countyId.getCityId();
    	if(null != townId && townId.getTownId()!=0) hql += " and sellPerson.townId.townId=" + townId.getTownId();
    	Query q = s.createQuery(hql);
    	List sellPersonList = q.list();
    	return (ArrayList<SellPerson>) sellPersonList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<SellPerson> QueryAllSellPersonInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From SellPerson";
        Query q = s.createQuery(hql);
        List sellPersonList = q.list();
        return (ArrayList<SellPerson>) sellPersonList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String telephone,String name,CountyInfo countyId,TownInfo townId) {
        Session s = factory.getCurrentSession();
        String hql = "From SellPerson sellPerson where 1=1";
        if(!telephone.equals("")) hql = hql + " and sellPerson.telephone like '%" + telephone + "%'";
        if(!name.equals("")) hql = hql + " and sellPerson.name like '%" + name + "%'";
        if(null != countyId && countyId.getCityId()!=0) hql += " and sellPerson.countyId.cityId=" + countyId.getCityId();
        if(null != townId && townId.getTownId()!=0) hql += " and sellPerson.townId.townId=" + townId.getTownId();
        Query q = s.createQuery(hql);
        List sellPersonList = q.list();
        recordNumber = sellPersonList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public SellPerson GetSellPersonByTelephone(String telephone) {
        Session s = factory.getCurrentSession();
        SellPerson sellPerson = (SellPerson)s.get(SellPerson.class, telephone);
        return sellPerson;
    }

    /*更新SellPerson信息*/
    public void UpdateSellPerson(SellPerson sellPerson) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(sellPerson);
    }

    /*删除SellPerson信息*/
    public void DeleteSellPerson (String telephone) throws Exception {
        Session s = factory.getCurrentSession();
        Object sellPerson = s.load(SellPerson.class, telephone);
        s.delete(sellPerson);
    }

}
