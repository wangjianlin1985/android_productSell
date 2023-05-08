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

@Service @Transactional
public class CountyInfoDAO {

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
    public void AddCountyInfo(CountyInfo countyInfo) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(countyInfo);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<CountyInfo> QueryCountyInfoInfo(String cityName,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From CountyInfo countyInfo where 1=1";
    	if(!cityName.equals("")) hql = hql + " and countyInfo.cityName like '%" + cityName + "%'";
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List countyInfoList = q.list();
    	return (ArrayList<CountyInfo>) countyInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<CountyInfo> QueryCountyInfoInfo(String cityName) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From CountyInfo countyInfo where 1=1";
    	if(!cityName.equals("")) hql = hql + " and countyInfo.cityName like '%" + cityName + "%'";
    	Query q = s.createQuery(hql);
    	List countyInfoList = q.list();
    	return (ArrayList<CountyInfo>) countyInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<CountyInfo> QueryAllCountyInfoInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From CountyInfo";
        Query q = s.createQuery(hql);
        List countyInfoList = q.list();
        return (ArrayList<CountyInfo>) countyInfoList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String cityName) {
        Session s = factory.getCurrentSession();
        String hql = "From CountyInfo countyInfo where 1=1";
        if(!cityName.equals("")) hql = hql + " and countyInfo.cityName like '%" + cityName + "%'";
        Query q = s.createQuery(hql);
        List countyInfoList = q.list();
        recordNumber = countyInfoList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public CountyInfo GetCountyInfoByCityId(int cityId) {
        Session s = factory.getCurrentSession();
        CountyInfo countyInfo = (CountyInfo)s.get(CountyInfo.class, cityId);
        return countyInfo;
    }

    /*更新CountyInfo信息*/
    public void UpdateCountyInfo(CountyInfo countyInfo) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(countyInfo);
    }

    /*删除CountyInfo信息*/
    public void DeleteCountyInfo (int cityId) throws Exception {
        Session s = factory.getCurrentSession();
        Object countyInfo = s.load(CountyInfo.class, cityId);
        s.delete(countyInfo);
    }

}
