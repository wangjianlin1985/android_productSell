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
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
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

    /*�����ܵ�ҳ���ͼ�¼��*/
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

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public SellInfo GetSellInfoBySellId(int sellId) {
        Session s = factory.getCurrentSession();
        SellInfo sellInfo = (SellInfo)s.get(SellInfo.class, sellId);
        return sellInfo;
    }

    /*����SellInfo��Ϣ*/
    public void UpdateSellInfo(SellInfo sellInfo) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(sellInfo);
    }

    /*ɾ��SellInfo��Ϣ*/
    public void DeleteSellInfo (int sellId) throws Exception {
        Session s = factory.getCurrentSession();
        Object sellInfo = s.load(SellInfo.class, sellId);
        s.delete(sellInfo);
    }

}
