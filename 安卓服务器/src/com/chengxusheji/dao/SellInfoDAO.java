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
import com.chengxusheji.domain.ProductInfo;
import com.chengxusheji.domain.SellPerson;
import com.chengxusheji.domain.SellInfo;

@Service @Transactional
public class SellInfoDAO {

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
    public void AddSellInfo(SellInfo sellInfo) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(sellInfo);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<SellInfo> QuerySellInfoInfo(ProductInfo productBarCode,SellPerson sellPerson,String sellDate,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From SellInfo sellInfo where 1=1";
    	if(null != productBarCode && !productBarCode.getBarCode().equals("")) hql += " and sellInfo.productBarCode.barCode='" + productBarCode.getBarCode() + "'";
    	if(null != sellPerson && !sellPerson.getTelephone().equals("")) hql += " and sellInfo.sellPerson.telephone='" + sellPerson.getTelephone() + "'";
    	if(!sellDate.equals("")) hql = hql + " and sellInfo.sellDate like '%" + sellDate + "%'";
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List sellInfoList = q.list();
    	return (ArrayList<SellInfo>) sellInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<SellInfo> QuerySellInfoInfo(ProductInfo productBarCode,SellPerson sellPerson,String sellDate) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From SellInfo sellInfo where 1=1";
    	if(null != productBarCode && !productBarCode.getBarCode().equals("")) hql += " and sellInfo.productBarCode.barCode='" + productBarCode.getBarCode() + "'";
    	if(null != sellPerson && !sellPerson.getTelephone().equals("")) hql += " and sellInfo.sellPerson.telephone='" + sellPerson.getTelephone() + "'";
    	if(!sellDate.equals("")) hql = hql + " and sellInfo.sellDate like '%" + sellDate + "%'";
    	Query q = s.createQuery(hql);
    	List sellInfoList = q.list();
    	return (ArrayList<SellInfo>) sellInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<SellInfo> QueryAllSellInfoInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From SellInfo";
        Query q = s.createQuery(hql);
        List sellInfoList = q.list();
        return (ArrayList<SellInfo>) sellInfoList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(ProductInfo productBarCode,SellPerson sellPerson,String sellDate) {
        Session s = factory.getCurrentSession();
        String hql = "From SellInfo sellInfo where 1=1";
        if(null != productBarCode && !productBarCode.getBarCode().equals("")) hql += " and sellInfo.productBarCode.barCode='" + productBarCode.getBarCode() + "'";
        if(null != sellPerson && !sellPerson.getTelephone().equals("")) hql += " and sellInfo.sellPerson.telephone='" + sellPerson.getTelephone() + "'";
        if(!sellDate.equals("")) hql = hql + " and sellInfo.sellDate like '%" + sellDate + "%'";
        Query q = s.createQuery(hql);
        List sellInfoList = q.list();
        recordNumber = sellInfoList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public SellInfo GetSellInfoBySellId(int sellId) {
        Session s = factory.getCurrentSession();
        SellInfo sellInfo = (SellInfo)s.get(SellInfo.class, sellId);
        return sellInfo;
    }

    /*更新SellInfo信息*/
    public void UpdateSellInfo(SellInfo sellInfo) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(sellInfo);
    }

    /*删除SellInfo信息*/
    public void DeleteSellInfo (int sellId) throws Exception {
        Session s = factory.getCurrentSession();
        Object sellInfo = s.load(SellInfo.class, sellId);
        s.delete(sellInfo);
    }

}
