/**
 * 
 */
package com.ycsoft.business.service.externalImpl;

import java.util.List;
import java.util.Map;

import com.ycsoft.beans.core.bill.BillDto;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dto.core.cust.CustDeviceDto;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.dto.core.cust.CustGeneralInfo;
import com.ycsoft.business.dto.core.fee.FeeDto;
import com.ycsoft.business.dto.core.prod.JBandCommandDto;
import com.ycsoft.business.dto.core.prod.JCaCommandDto;
import com.ycsoft.business.dto.core.prod.JVodCommandDto;
import com.ycsoft.business.service.impl.QueryCustService;
import com.ycsoft.daos.core.Pager;

/**
 * @author liujiaqi
 * 
 */
public class QueryCustServiceExternal extends ParentService implements
		IQueryCustServiceExternal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IQueryCustServiceExternal#queryAcctPayFee(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String)
	 */
	public List<FeeDto> queryAcctPayFee(BusiParameter p, String custId)
			throws Exception {
		QueryCustService queryCustService = (QueryCustService) getBean(
				QueryCustService.class, p);
		return queryCustService.queryAcctPayFee(custId, null,0,10000).getRecords();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IQueryCustServiceExternal#searchCust(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String, java.lang.String)
	 */
	public CCust searchCust(BusiParameter p, String searchType,
			String searchValue) throws Exception {
		QueryCustService queryCustService = (QueryCustService) getBean(
				QueryCustService.class, p);
		return queryCustService.searchCust(searchType, searchValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IQueryCustServiceExternal#searchCustById(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String)
	 */
	public CustFullInfoDto searchCustById(BusiParameter p, String custId)
			throws Exception {
		QueryCustService queryCustService = (QueryCustService) getBean(
				QueryCustService.class, p);
		return queryCustService.searchCustById(custId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IQueryCustServiceExternal#searchCustToCallCenter(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.util.Map, java.lang.Integer, java.lang.Integer)
	 */
	public List<CCust> searchCustToCallCenter(BusiParameter p, String cust_no,
			String cust_name, String address, String card_id,
			String telOrMobile, String modem_mac, String stb_id,
			String band_login_name) throws Exception {
		QueryCustService queryCustService = (QueryCustService) getBean(
				QueryCustService.class, p);
		return queryCustService.searchCustToCallCenter(cust_no, cust_name,
				address, card_id, telOrMobile, modem_mac, stb_id,
				band_login_name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IQueryCustServiceExternal#queryCaCommandByCardId(java.lang.String,
	 *      java.lang.Integer, java.lang.Integer)
	 */
	public Pager<JCaCommandDto> queryCaCommandByCardId(String cardId,
			Integer start, Integer limit) throws Exception {
		QueryCustService queryCustService = (QueryCustService) getBean(
				QueryCustService.class, null);
		return queryCustService.queryCaCommandByCardId(cardId, start, limit);
	}
	
	public CustGeneralInfo SearchCustGeneralInfo(BusiParameter p,String custId) throws Exception{
		QueryCustService queryCustService = (QueryCustService) getBean(
				QueryCustService.class, p);
		return queryCustService.SearchCustGeneralInfo(custId);
	}
	
	public List<CustDeviceDto> queryCustDevices(BusiParameter p,String custId) throws Exception{
		QueryCustService queryCustService = (QueryCustService) getBean(
				QueryCustService.class, p);
		return queryCustService.queryCustDevices(custId);
	}
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IQueryCustServiceExternal#queryVodCommandByCardId(java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public Pager<JVodCommandDto> queryVodCommandByCardId(String cardId,
			Integer start, Integer limit) throws Exception {
		// TODO Auto-generated method stub
		QueryCustService queryCustService = (QueryCustService) getBean(
				QueryCustService.class, null);
		return queryCustService.queryVodCommandByCardId(cardId, start, limit);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IQueryCustServiceExternal#queryBandCommandByParam(java.util.Map, java.lang.Integer, java.lang.Integer)
	 */
	public Pager<JBandCommandDto> queryBandCommandByParam(
			Map<String, String> param, Integer start, Integer limit)
			throws Exception {
		// TODO Auto-generated method stub
		QueryCustService queryCustService = (QueryCustService) getBean(
				QueryCustService.class, null);
		return queryCustService.queryBandCommandByParam(param, start, limit);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IQueryCustServiceExternal#queryBusiPayFee(java.lang.String)
	 */
	public List<FeeDto> queryBusiPayFee(BusiParameter p,String custId) throws Exception {
		// TODO Auto-generated method stub
		QueryCustService queryCustService = (QueryCustService) getBean(
				QueryCustService.class, p);
		return queryCustService.queryBusiPayFee(custId, null, 0, 10000).getRecords();
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IQueryCustServiceExternal#syncServerTime()
	 */
	public String syncServerTime() throws Exception {
		// TODO Auto-generated method stub
		QueryCustService queryCustService = (QueryCustService) getBean(
				QueryCustService.class,null);
		return queryCustService.syncServerTime();
	}
	
	
	public List<BillDto> queryCustOweBill(String custId)throws Exception{
		// TODO Auto-generated method stub
		QueryCustService queryCustService = (QueryCustService) getBean(
				QueryCustService.class,null);
		return queryCustService.queryCustOweBill(custId);
	}
 

}
