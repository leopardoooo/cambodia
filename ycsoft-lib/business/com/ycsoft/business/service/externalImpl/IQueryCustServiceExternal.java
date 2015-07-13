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
import com.ycsoft.daos.core.Pager;

/**
 * @author liujiaqi
 * 
 */
public interface IQueryCustServiceExternal {

	CCust searchCust(BusiParameter p, String searchType, String searchValue)
			throws Exception;

	List<CCust> searchCustToCallCenter(BusiParameter p, String cust_no,
			String cust_name, String address, String card_id,
			String telOrMobile, String modem_mac, String stb_id,
			String band_login_name) throws Exception;

	CustFullInfoDto searchCustById(BusiParameter p, String custId)
			throws Exception;

	List<FeeDto> queryAcctPayFee(BusiParameter p, String custId)
			throws Exception;

	Pager<JCaCommandDto> queryCaCommandByCardId(String cardId, Integer start,
			Integer limit) throws Exception;
	
	CustGeneralInfo SearchCustGeneralInfo(BusiParameter p,String custId) throws Exception;
	
	List<CustDeviceDto> queryCustDevices(BusiParameter p,String custId) throws Exception;
	
	Pager<JVodCommandDto> queryVodCommandByCardId(String cardId, Integer start,
			Integer limit) throws Exception;
	
	Pager<JBandCommandDto> queryBandCommandByParam(Map<String,String> param, Integer start,
			Integer limit) throws Exception;
	List<FeeDto> queryBusiPayFee(BusiParameter p,String custId) throws Exception ;
	
	String syncServerTime() throws Exception ;
	
	List<BillDto> queryCustOweBill(String custId)throws Exception;

}
