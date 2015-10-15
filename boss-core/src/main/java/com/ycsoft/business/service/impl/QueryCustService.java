
package com.ycsoft.business.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TProvince;
import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.bill.BBillWriteoff;
import com.ycsoft.beans.core.bill.BillDto;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustPropChange;
import com.ycsoft.beans.core.fee.CFeeUnprint;
import com.ycsoft.beans.core.job.JBandCommand;
import com.ycsoft.beans.core.job.JCaCommand;
import com.ycsoft.beans.core.job.JVodCommand;
import com.ycsoft.beans.core.promotion.CPromFee;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.abstracts.BaseService;
import com.ycsoft.business.component.core.AcctComponent;
import com.ycsoft.business.component.core.BillComponent;
import com.ycsoft.business.component.core.CustComponent;
import com.ycsoft.business.component.core.FeeComponent;
import com.ycsoft.business.component.core.JobComponent;
import com.ycsoft.business.component.core.UserComponent;
import com.ycsoft.business.component.core.UserProdComponent;
import com.ycsoft.business.dto.core.cust.CustDeviceDto;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.dto.core.cust.CustGeneralInfo;
import com.ycsoft.business.dto.core.fee.BBillPrintDto;
import com.ycsoft.business.dto.core.fee.BbillingcycleCfgDto;
import com.ycsoft.business.dto.core.fee.FeeDto;
import com.ycsoft.business.dto.core.fee.FeePayDto;
import com.ycsoft.business.dto.core.fee.QueryFeeInfo;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.JBandCommandDto;
import com.ycsoft.business.dto.core.prod.JCaCommandDto;
import com.ycsoft.business.dto.core.prod.JVodCommandDto;
import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.business.service.IQueryCustService;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

/**
 * 查询客户相关信息服务实现类
 *
 * @author YC-SOFT
 */

@Service
public class QueryCustService extends BaseService implements IQueryCustService {

	private CustComponent custComponent;
	private FeeComponent feeComponent;
	private UserComponent userComponent;
	private UserProdComponent userProdComponent;
	private JobComponent jobComponent;
	private AcctComponent acctComponent;
	private BillComponent billComponent;
	
	/**
	 * 查询所有账期
	 * @return
	 * @throws JDBCException
	 */
	public Pager<BbillingcycleCfgDto> queryAllBillingCycleCfg(String query, Integer start, Integer limit) throws Exception {
		return feeComponent.queryAllBillingCycleCfg(query, start, limit);
	}
	
	public Pager<CPromFee> queryPromFeeByCust(String custId, Integer start, Integer limit) throws Exception {
		return feeComponent.queryPromFeeByCust(custId, start, limit);
	}
	
	public List<CPromFee> queryCustPromFee(String custId) throws Exception {
		return feeComponent.queryCustPromFee(custId);
	}
	
	public BBillPrintDto queryBillPrint(String custId,String billingCycleId) throws Exception{
		BBillPrintDto bpDto = feeComponent.queryBillPrint(custId, billingCycleId);
		List<BillDto> billList = bpDto.getBillList();
		
		List<CUser> userList = userComponent.queryUserByCustId(custId);
		Map<String,CUser> userMap = CollectionHelper.converToMapSingle(userList, "user_id");
		
		for(BillDto bill : billList){
			String userId = bill.getUser_id();
			String acctItemId = bill.getAcctitem_id();
			if(StringHelper.isNotEmpty(userId)){
				CUser user = userMap.get(userId);
				if(null != user){
//					if(user.getUser_type().equals(SystemConstants.USER_TYPE_DTV)){
//						bill.setUser_name(user.getUser_name()+"("+user.getCard_id()+")");
//					}else{
						bill.setUser_name(user.getUser_name());
//					}
				}
			}
		}
		return bpDto;
	}
	
	
	public BBillPrintDto queryPromPrint(String custId,String promFeeSn) throws Exception{
		BBillPrintDto bpDto = feeComponent.queryPromPrint(custId, promFeeSn);
		List<BillDto> billList = bpDto.getBillList();
		
		List<CUser> userList = userComponent.queryUserByCustId(custId);
		Map<String,CUser> userMap = CollectionHelper.converToMapSingle(userList, "user_id");
		
		for(BillDto bill : billList){
			String userId = bill.getUser_id();
			String acctItemId = bill.getAcctitem_id();
			if(StringHelper.isNotEmpty(userId)){
				CUser user = userMap.get(userId);
				if(null != user){
//					if(user.getUser_type().equals(SystemConstants.USER_TYPE_DTV)){
//						bill.setUser_name(user.getUser_name()+"("+user.getCard_id()+")");
//					}else{
						bill.setUser_name(user.getUser_name());
//					}
				}
			}
		}
		return bpDto;
	}
	
	public Pager<BillDto> queryCustBill(String custId, QueryFeeInfo queryFeeInfo, Integer start, Integer limit) throws Exception{
		return billComponent.queryCustBill(custId, queryFeeInfo, start, limit);
	}
	
	/**
	 * 查询账单销帐记录.
	 * @param billSn
	 */
	public List<BBillWriteoff> queryCustBillWriteOff(String billSn) throws Exception{
		 return billComponent.queryWriteOffByBill(billSn);
	}
	
	/**
	 * 查询操作员下未打印发票的客户
	 */
	public String queryUnPrintCustByOptr() throws Exception {
		
		String optrId = getOptr().getOptr_id();
		/**
		//先找未支付，然后找未打印
		List<CDoneCodeUnpay> unPays=doneCodeComponent.queryUnPayByOptr(optrId);
		if(unPays!=null&&unPays.size()>0){
			CCust cust = custComponent.queryCustById(unPays.get(0).getCust_id());
			return cust.getCust_no();
		}
		**/
		List<CFeeUnprint> unPrints=feeComponent.queryUnPrintByOptr(optrId);
		if(unPrints!=null&&unPrints.size()>0){
			CCust cust = custComponent.queryCustById(unPrints.get(0).getCust_id());
			return cust.getCust_no();
		}

		return null;
	}
	
	public String queryUnRefundByOptr() throws Exception {
		String custNo = null;
		List<Object[]> custRefund = acctComponent.queryUnRefundByOptr();
		if(null!= custRefund && custRefund.size()>0){
			for(int i=0;i<custRefund.size();i++){
				Object[] custObject = custRefund.get(i);
				String custId=(String) custObject[0];
				Integer doneCode=Integer.parseInt((String)custObject[1]);
				//查询当前操作员是否为最后一个调账的人,如果是，maxDoneCode 为null 
				String maxDoneCode = acctComponent.queryUnRefundMaxDoneCode(custId, doneCode);
				
				if(StringHelper.isNotEmpty(custId) && StringHelper.isNotEmpty(maxDoneCode) && maxDoneCode.equals("-1")){
					CCust cust = custComponent.queryCustById(custId);
					if(null != cust){
						custNo = cust.getCust_no();
						return custNo;
					}
				}
			}
		}
		return custNo;
	}
	
//	public List<String> queryUnPrintCustByOptr() throws Exception {
//		return feeComponent.queryUnPrintCustByOptr();
//	}
	
	public Pager<CCust> searchCust(String searchType,String searchValue, Integer start,Integer limit) throws Exception {
		Map<String, Object> cond  = new HashMap<String,Object>();
		cond.put(searchType, searchValue);
		return custComponent.queryCust(new Pager<Map<String ,Object>>(cond , start, limit));
	}
	
	public Pager<CCust> searchTransportableCust(String search_value,Integer start,Integer limit)throws ServicesException{
		try {
			//暂时全部都是按照客户名字搜索.
			return custComponent.searchTransportableCust(search_value,start,limit);
		} catch (Exception e) {
			throw new ServicesException(e);
		}
	}
	
	public Map<String, Object> queryCustWithOweFeeInfo(String custId) throws ServicesException{
		try {
			//暂时全部都是按照客户名字搜索.
			Map<String, Object> map = new HashMap<String, Object>();
			CustFullInfoDto fullInfo = custComponent.searchCustInfoById(custId);
			map.put("fullInfo", fullInfo);
			String flag = acctComponent.queryWhetherCustOwnfee(custId);
			map.put("oweFeeInfo", flag);
			List<UserDto> users = userComponent.queryUser(custId);
			Map<String, List<UserDto>> userStatusMap = CollectionHelper.converToMap(users, "status");
			boolean userNotActive = true;//有用户不是正常状态
			if ((userStatusMap.size() == 0)//没有用户
					|| (userStatusMap.size() == 1 && // 或者 所有的用户都是一种状态,且是ACTIVE的
							userStatusMap.get(StatusConstants.ACTIVE) != null)) {
				userNotActive = false;
			}
			map.put("userNotActive", userNotActive);
			List<CProdDto> allProds = userProdComponent.queryProdByCustId(custId);
			Map<String, List<CProdDto>> prodStatusMap = CollectionHelper.converToMap(allProds, "status");
			boolean prodNotActive = true;//有产品不是正常状态
			if ((prodStatusMap.size() == 0)//没有用户
					|| (prodStatusMap.size() == 1 && // 或者 所有的用户都是一种状态,且是ACTIVE的
							prodStatusMap.get(StatusConstants.ACTIVE) != null)) {
				prodNotActive = false;
			}
			map.put("prodNotActive", prodNotActive);
			return map;
		} catch (Exception e) {
			throw new ServicesException(e);
		}
	}
	
	/**
	 * 根据客户ID查询客户
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public CustFullInfoDto searchCustById(String custId) throws Exception{
		return custComponent.searchCustInfoById(custId);
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IQueryCustService#multipleSearchCust(com.ycsoft.beans.core.cust.CCust)
	 */
	public Pager<CCust> complexSearchCust(CCust cust, Integer start, Integer limit) throws Exception {
		return custComponent.complexSearchCust(cust,start,limit);
	}

	/**
	 * 根据客户地址，模糊查找没加入单位的居民客户
	 */
	public List<CCust> searchResidentCust(CCust cust) throws Exception {
		return custComponent.searchResidentCust(cust);
	}


	public CCust searchCust(String searchType,String searchValue ) throws Exception {
		return custComponent.queryCust(searchType,searchValue );
	}

	public List<CCust> queryMnCustByNameAndAddr(String mnCustName,String addr) throws Exception {
		return custComponent.queryMnCustByNameAndAddr(mnCustName,addr);
	}
	
	public List<CCust> queryUnitByNameAndAddr(String unitName,String addr) throws Exception {
		return custComponent.queryUnitByNameAndAddr(unitName,addr);
	}

	public List<CCust> queryResidentUnit(String custId) throws Exception {
		return custComponent.queryUnitByResident(custId);
	}

	public List<CCustPropChange> queryCustPropChange(String custId,String custType)
			throws Exception {

		return custComponent.queryPropChangeByCustID(custId,custType);
	}

	public List<CustDeviceDto> queryCustDevices(String custId) throws Exception {
		return custComponent.queryCustDevices(custId);
	}

	public List<CProdDto> queryCustPackage(String custId)
			throws Exception {
		return userProdComponent.queryCustPackage(custId);
	}

	public Pager<FeeDto> queryBusiPayFee(String custId, QueryFeeInfo queryFeeInfo, Integer start, Integer limit) throws Exception {
		return feeComponent.queryBusiPayFee(custId, queryFeeInfo, start, limit);
	}

	public Pager<FeeDto> queryAcctPayFee(String custId, QueryFeeInfo queryFeeInfo, Integer start, Integer limit) throws Exception {
		Map<String,CUser> users =CollectionHelper.converToMapSingle(userComponent.queryUserByCustId(custId), "user_id") ;
		Pager<FeeDto> acctPayFee = feeComponent.queryAcctPayFee(custId, queryFeeInfo, start, limit);
		List<FeeDto> feeList = acctPayFee.getRecords();
		
		for (FeeDto pf :feeList){
			if (StringHelper.isNotEmpty(pf.getUser_id())){
				CUser user = users.get(pf.getUser_id());
				if (user!=null){
					pf.setUser_name(user.getUser_name());
					pf.setUser_type_text(user.getUser_type_text());
					if(user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
						pf.setDevice_code(user.getModem_mac());
					}else{
						pf.setDevice_code(user.getStb_id());
					}
				}
				/*else{
					pf.setIs_logoff("T");
				}*/
			}
		}
		return acctPayFee;
	}
	
	
	

	public Pager<FeeDto> queryAcctPayFeeHis(String custId, QueryFeeInfo queryFeeInfo,Integer start,Integer limit) throws Exception {
		Map<String,CUser> users =CollectionHelper.converToMapSingle(userComponent.queryUserHisByCustId(custId), "user_id") ;
		Pager<FeeDto> acctPayFee  = feeComponent.queryAcctPayFeeHis(custId, queryFeeInfo,start,limit);
		List<FeeDto> feeList = acctPayFee.getRecords();
		
		for (FeeDto pf :feeList){
			if (StringHelper.isNotEmpty(pf.getUser_id())){
				CUser user = users.get(pf.getUser_id());
				if (user!=null){
					pf.setUser_name(user.getUser_name());
					pf.setUser_type_text(user.getUser_type_text());
					pf.setDevice_code(user.getCard_id());
				}else{
					pf.setIs_logoff("T");
				}
			}
		}
		return acctPayFee;
	}
	
	/**
	 * 查询客户预存费用(批量)
	 * @param custId
	 * @return
	 */
	public List<FeeDto> queryBatchAcctPayFee(String beginOptrateDate,
			String endOptrateDate, String beginAcctDate, String endAcctDate,
			String optrId, String feeType, String deptId, String custNo,
			String beginInvoice, String endInvoice, String countyId)
			throws Exception {
		
		List<FeeDto> acctPayFee = feeComponent.queryBatchAcctPayFee(beginOptrateDate,
				endOptrateDate, beginAcctDate, endAcctDate,
				optrId, feeType, deptId,custNo,beginInvoice,endInvoice,countyId);
		
		for(FeeDto fee : acctPayFee){
			fee.setUser_type_text(MemoryDict.getDictName(DictKey.USER_TYPE, fee.getUser_type_text()));
		}
		
		return acctPayFee;
	}
	
	/**
	 * 查询客户业务费用(批量)
	 * @param custId
	 * @return
	 */
	public List<FeeDto> queryBatchBusiPayFee(String beginOptrateDate,
			String endOptrateDate, String beginAcctDate, String endAcctDate,
			String optrId, String feeType, String deptId, String custNo,
			String beginInvoice, String endInvoice, String countyId)
			throws Exception {
		
		return feeComponent.queryBatchBusiPayFee(beginOptrateDate,
				endOptrateDate, beginAcctDate, endAcctDate,
				optrId, feeType, deptId,custNo,beginInvoice,endInvoice,countyId);
	}

	public Pager<JCaCommand> queryCaCommand(String type,String custId,Integer start,Integer limit) throws Exception {
		return jobComponent.queryCaCommand(type,custId,start,limit);
	}
	
	public Pager<JCaCommandDto> queryCaCommandByCardId(String cardId,Integer start,Integer limit) throws Exception {
		return jobComponent.queryCaCommandByCardId(cardId,start,limit);
	}
	
	public Pager<JVodCommand> queryVodCommand(String type,String custId,Integer start,Integer limit) throws Exception {
		return jobComponent.queryVodCommand(type,custId,start,limit);
	}

	public Pager<JBandCommand> queryBandCommand(String custId,Integer start,Integer limit) throws Exception {
		return jobComponent.queryBandCommand(custId,start,limit);
	}
	
	public CAcctAcctitem getOweFee(String custId) throws Exception {
		CAcctAcctitem acctPayFee = acctComponent.queryOweFee(custId);
		return acctPayFee;
	}
	public List<CCust> searchCustToCallCenter(String cust_no, String cust_name, String address, String card_id, String telOrMobile, String modem_mac, String stb_id, String band_login_name) throws Exception {
		return custComponent.queryCustToCallCenter(cust_no, cust_name, address, card_id, telOrMobile, modem_mac,stb_id, band_login_name);
	}
	
	public CustGeneralInfo SearchCustGeneralInfo(String custId) throws Exception{
		return custComponent.SearchCustGeneralInfo(custId);
	}

	public List<CCust> queryUnitMember(String custId) throws Exception {
		return custComponent.queryUnitMember(custId);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IQueryCustService#queryAddressAll(java.lang.String, com.ycsoft.beans.system.SOptr)
	 */
	public int queryAddressAll(String addrId,SOptr optr) throws Exception{
		List<CCust> list = custComponent.queryAddressAll(addrId, optr); 
		return list== null?0:list.size();
	}
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IQueryCustService#queryCustAddress(com.ycsoft.beans.core.cust.CCust, com.ycsoft.beans.system.SOptr, java.lang.Integer, java.lang.Integer)
	 */
	public Pager<CCust> queryCustAddress(CCust cust,SOptr optr,Integer start,Integer limit) throws Exception{
		return custComponent.queryCustAddress(cust, optr,start,limit);
	}
	
	public Pager<CCust> queryCustAddrByCustIds(String[] custIds,SOptr optr,Integer start,Integer limit) throws Exception{
		return custComponent.queryCustAddrByCustIds(custIds, optr,start,limit);
	}
	
	public Object queryImportanceCustNum() throws Exception{
		return custComponent.queryImportanceCustNum();
	}

	public Pager<CCust> queryImportanceCust(Integer start, Integer limit) throws Exception {
		return custComponent.queryImportanceCust(start,limit);
	}

	public List<BillDto> queryCustOweBill(String custId)throws Exception{
		return billComponent.queryCustOweBill(custId);
	}
	
	public Pager<FeePayDto> queryFeePay(String custId,QueryFeeInfo queryFeeInfo, Integer start,Integer limit) throws Exception {
		return feeComponent.queryFeePay(custId, queryFeeInfo, start, limit);
	}
	
	public List<FeeDto> queryFeePayDetail(String paySn) throws Exception {
		return feeComponent.queryFeePayDetail(paySn);
	}
	
	public List<TProvince> queryProvince() throws Exception {
		return custComponent.queryProvince();
	}
	
	/**
	 * @param custComponent the custComponent to set
	 */
	public void setCustComponent(CustComponent custComponent) {
		this.custComponent = custComponent;
	}

	/**
	 * @param feeComponent the feeComponent to set
	 */
	public void setFeeComponent(FeeComponent feeComponent) {
		this.feeComponent = feeComponent;
	}

	/**
	 * @param userComponent the userComponent to set
	 */
	public void setUserComponent(UserComponent userComponent) {
		this.userComponent = userComponent;
	}

	/**
	 * @param userProdComponent the userProdComponent to set
	 */
	public void setUserProdComponent(UserProdComponent userProdComponent) {
		this.userProdComponent = userProdComponent;
	}

	/**
	 * @param jobComponent the jobComponent to set
	 */
	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}

	/**
	 * @param acctComponent the acctComponent to set
	 */
	public void setAcctComponent(AcctComponent acctComponent) {
		this.acctComponent = acctComponent;
	}

	/**
	 * @param billComponent the billComponent to set
	 */
	public void setBillComponent(BillComponent billComponent) {
		this.billComponent = billComponent;
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IQueryCustService#test()
	 */
	public void test(String optrId) throws Exception {
		custComponent.test(optrId);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IQueryCustService#queryBandCommandByParam(java.util.Map, java.lang.Integer, java.lang.Integer)
	 */
	public Pager<JBandCommandDto> queryBandCommandByParam(
			Map<String, String> param, Integer start, Integer limit)
			throws Exception {
		// TODO Auto-generated method stub
		return jobComponent.queryBandCommandByParam(param,start,limit);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IQueryCustService#queryVodCommandByCardId(java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public Pager<JVodCommandDto> queryVodCommandByCardId(String cardId,
			Integer start, Integer limit) throws Exception {
		// TODO Auto-generated method stub
		return jobComponent.queryVodCommandByCardId(cardId,start,limit);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IQueryCustService#syncServerTime()
	 */
	public String syncServerTime() throws JDBCException, Exception {
		// TODO Auto-generated method stub
		return jobComponent.syncServerTime();
	}

	
}
