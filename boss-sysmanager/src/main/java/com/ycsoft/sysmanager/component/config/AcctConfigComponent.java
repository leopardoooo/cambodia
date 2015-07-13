package com.ycsoft.sysmanager.component.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TCountyAcct;
import com.ycsoft.beans.config.TCountyAcctChange;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dao.config.TCountyAcctChangeDao;
import com.ycsoft.business.dao.config.TCountyAcctDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

@Component
public class AcctConfigComponent extends BaseComponent {
	private TCountyAcctDao tCountyAcctDao;
	private TCountyAcctChangeDao tCountyAcctChangeDao;
	
	public Pager<TCountyAcct> queryAcctConfig(String countyId,String colony, Integer start, Integer limit) throws JDBCException{
		return tCountyAcctDao.queryAcctConfig(countyId,colony,start,limit);
	}
	

	public List<TCountyAcct> queryAcctConfigForAdd(String countyId,String colony) throws JDBCException {
		return tCountyAcctDao.queryAcctConfigForAdd(countyId,colony);
	}
	

	public void saveAcctConfig(List<TCountyAcct> acctList,SOptr optr) throws JDBCException {
		if(acctList.size() > 0){
			String optrId = optr.getOptr_id();
			String acctId = acctList.get(0).getT_acct_id();
			//如果不为空，表示修改
			if(StringHelper.isNotEmpty(acctId)){
				List<TCountyAcctChange> acctChangeList = new ArrayList<TCountyAcctChange>();
				for(TCountyAcct acct : acctList){
					TCountyAcctChange change = new TCountyAcctChange();
					BeanUtils.copyProperties(acct, change);
					change.setOptr_id(optrId);
					acctChangeList.add(change);
				}
				tCountyAcctDao.update(acctList.toArray(new TCountyAcct[acctList.size()]));
				tCountyAcctChangeDao.save(acctChangeList.toArray(new TCountyAcctChange[acctChangeList.size()]));
			}else{
				for(TCountyAcct acct : acctList){
					acct.setT_acct_id(tCountyAcctDao.findSequence().toString());
					acct.setInitamount(acct.getBalance());
					acct.setOptr_id(optrId);
				}
				
				tCountyAcctDao.save(acctList.toArray(new TCountyAcct[acctList.size()]));
			}
		}
	}
	
	public void deleteAcctConfig(String[] acctIds,String optrId) throws JDBCException {
		if(null != acctIds && acctIds.length > 0){
			tCountyAcctDao.deleteAcctConfig(acctIds,optrId);
		}
		
	}
	

	/**
	 * 查询账户明细
	 * @param acctId
	 * @param start
	 * @param limit
	 * @return
	 * @throws JDBCException 
	 */
	public Pager<TCountyAcctChange> queryAcctDetail(String acctId, Integer start, Integer limit) throws JDBCException {
		return tCountyAcctChangeDao.queryAcctDetail(acctId,start,limit);
	}


	
	public List<SItemvalue> findViewDict(String itemKey) throws JDBCException {
		return sItemvalueDao.findViewDict(itemKey);
	}




	
	
	public void setTCountyAcctDao(TCountyAcctDao tCountyAcctDao) {
		this.tCountyAcctDao = tCountyAcctDao;
	}
	public void setTCountyAcctChangeDao(TCountyAcctChangeDao tCountyAcctChangeDao) {
		this.tCountyAcctChangeDao = tCountyAcctChangeDao;
	}


}
