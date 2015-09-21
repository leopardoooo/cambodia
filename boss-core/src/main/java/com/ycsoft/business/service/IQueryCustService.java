package com.ycsoft.business.service;

import java.util.List;
import java.util.Map;

import com.ycsoft.beans.config.TProvince;
import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.bill.BBillWriteoff;
import com.ycsoft.beans.core.bill.BillDto;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustPropChange;
import com.ycsoft.beans.core.job.JBandCommand;
import com.ycsoft.beans.core.job.JCaCommand;
import com.ycsoft.beans.core.promotion.CPromFee;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.abstracts.IBaseService;
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
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

/**
 *
 * 客户数据查询
 * @author hh
 * @date Feb 8, 2010 8:03:48 PM
 */
public interface IQueryCustService extends IBaseService{
	
	/**
	 * 查询所有账期
	 * @return
	 * @throws JDBCException
	 */
	public Pager<BbillingcycleCfgDto> queryAllBillingCycleCfg(String query, Integer start, Integer limit) throws Exception;
	
	/**套餐账单
	 * @param custId
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<CPromFee> queryPromFeeByCust(String custId, Integer start, Integer limit) throws Exception;
	
	/**
	 * 根据客户编号查询套餐信息
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<CPromFee> queryCustPromFee(String custId) throws Exception;
	
	/**
	 * 账单打印
	 * @param custId
	 * @param billingCycleId
	 * @return
	 * @throws Exception
	 */
	public BBillPrintDto queryBillPrint(String custId,String billingCycleId) throws Exception;
	
	
	public BBillPrintDto queryPromPrint(String custId,String billingCycleId) throws Exception;

	/**
	 *	查找客户，web搜索框
	 * @param search_type
	 * @param search_value
	 * @return
	 * @throws Exception
	 */
	Pager<CCust> searchCust(String search_type,String search_value,Integer start,Integer limit)throws Exception;
	
	/**
	 * 查询可迁移的客户.暂时全部都是按照客户名字搜索.
	 * @param search_type
	 * @param search_value
	 * @param start
	 * @param limit
	 * @return
	 * @throws ServicesException
	 */
	Pager<CCust> searchTransportableCust(String search_value,Integer start,Integer limit)throws ServicesException;

	/**
	 * 查询客户,包含所有账目是否有欠费.
	 * kye说明:</br>
	 * <ul>
	 * 	<li>fullInfo : 客户的全完信息.跟 searchCustById 方法返回.</li>
	 * <li>oweFeeInfo : 客户的的欠费信息(所有账目),0没有欠费,1有欠费.</li>
	 * <li>userNotActive : boolean 类型,是否有非正常状态的客户.</li>
	 * <li>prodNotActive : boolean 类型,是否有非正常状态的产品.</li>
	 * </ul>
	 * @param custId
	 * @return
	 * @throws ServicesException
	 */
	public Map<String, Object> queryCustWithOweFeeInfo(String custId) throws ServicesException;
	/**
	 * 根据客户ID查询客户
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public CustFullInfoDto searchCustById(String custId) throws Exception;
	
	/**
	 * 查找客户，接口搜索
	 * @param searchType  CUST_NO或CARD_ID
	 * @param searchValue	搜索值
	 * @return
	 * @throws Exception
	 */
	public CCust searchCust(String searchType,String searchValue ) throws Exception ;
	/**
	 * 根据客户名称和地址模糊查询单位客户
	 * @param unitName
	 * @return
	 * @throws Exception
	 */
	List<CCust> queryUnitByNameAndAddr(String unitName,String addr)throws Exception;
	
	/**
	 * 根据客户名称和地址模糊查询模拟大客户
	 * @param unitName
	 * @return
	 * @throws Exception
	 */
	public List<CCust> queryMnCustByNameAndAddr(String mnCustName,String addr) throws Exception;
	
	/**
	 * 根据客户id查找客户所属的单位信息
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	List<CCust> queryResidentUnit(String custId)throws Exception;

	/**
	 * 查询客户业务费用（受理费+设备销售）
	 * @param custId
	 * @param queryFeeInfo	条件过滤对象
	 * @param limit 
	 * @param start 
	 * @return
	 */
	Pager<FeeDto> queryBusiPayFee(String custId, QueryFeeInfo queryFeeInfo, Integer start, Integer limit) throws Exception;

	/**
	 * 查询客户预存费用
	 * @param custId
	 * @param queryFeeInfo	条件过滤对象
	 * @param limit 
	 * @param start 
	 * @return
	 */
	Pager<FeeDto> queryAcctPayFee(String custId, QueryFeeInfo queryFeeInfo, Integer start, Integer limit) throws Exception;

	/**
	 * 查询销户客户预存费用
	 * @param custId
	 * @param limit 
	 * @param start 
	 * @return
	 */
	Pager<FeeDto> queryAcctPayFeeHis(String custId, QueryFeeInfo queryFeeInfo,Integer start,Integer limit) throws Exception;
	
	/**
	 * 批量查询客户预存费用
	 * @param beginOptrateDate
	 * @param endOptrateDate
	 * @param beginAcctDate
	 * @param endAcctDate
	 * @param optrId
	 * @param feeType
	 * @param deptId
	 * @return
	 * @throws Exception
	 */
	public List<FeeDto> queryBatchAcctPayFee(String beginOptrateDate,
			String endOptrateDate, String beginAcctDate, String endAcctDate,
			String optrId, String feeType, String deptId, String custNo,
			String beginInvoice, String endInvoice, String countyId)
			throws Exception;
	
	/**
	 * 批量查询客户业务费用
	 * @param beginOptrateDate
	 * @param endOptrateDate
	 * @param beginAcctDate
	 * @param endAcctDate
	 * @param optrId
	 * @param feeType
	 * @param deptId
	 * @return
	 * @throws Exception
	 */
	public List<FeeDto> queryBatchBusiPayFee(String beginOptrateDate,
			String endOptrateDate, String beginAcctDate, String endAcctDate,
			String optrId, String feeType, String deptId, String custNo,
			String beginInvoice, String endInvoice, String countyId)
			throws Exception;

	/**
	 * 根据客户id查找客户属性的异动信息
	 * @param CustId
	 * @return
	 * @throws Exception
	 */
	List<CCustPropChange> queryCustPropChange(String custId,String custType) throws Exception;


	/**
	 * 查询客户下的所有设备信息
	 * @param custId 客户编号
	 * @return
	 */
	List<CustDeviceDto> queryCustDevices(String custId)throws Exception;

	/**
	 * 查询客户套餐，根据客户编号
	 * @param custId 客户编号
	 * @return
	 */
	List<CProdDto> queryCustPackage(String custId)throws Exception;
	/**
	 * 查询往月欠费
	 */
	CAcctAcctitem getOweFee(String custId)throws Exception;
	/**
	 * 查询客户的指令信息
	 * @return
	 * @throws Exception
	 */
	public Pager<JCaCommand> queryCaCommand(String type,String custId,Integer start,Integer limit) throws Exception;

	
	/**
	 * 按卡号查询客户的指令信息
	 * @param cardId
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<JCaCommandDto> queryCaCommandByCardId(String cardId,Integer start,Integer limit) throws Exception ;
	
	
	/**
	 * 查询单位客户的成员
	 * @param residentCustId
	 * @return
	 * @throws Exception
	 */
	List<CCust> queryUnitMember(String residentCustId) throws Exception;
	/**
	 *
	 * 呼叫中心查找客户，接口搜索, 多个条件组合查询
	 * @param params
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	List<CCust> searchCustToCallCenter(String cust_no, String cust_name, String address, String card_id, String telOrMobile, String modem_mac, String stb_id, String band_login_name) throws Exception;
	
	CustGeneralInfo SearchCustGeneralInfo(String custId) throws Exception;

	Pager<?> queryVodCommand(String type,String custId,Integer start,Integer limit) throws Exception;
	
	Pager<JBandCommand> queryBandCommand(String custId,Integer start,Integer limit) throws Exception;

	/**
	 * 多条件查询客户
	 * @param cust
	 * @param limit 
	 * @param start 
	 * @return
	 * @throws JDBCException 
	 * @throws Exception 
	 */
	Pager<CCust> complexSearchCust(CCust cust, Integer start, Integer limit) throws JDBCException, Exception;

	/**
	 *查询当前操作员，上线后未打印发票的客户编号
	 * @return
	 * @throws Exception
	 */
	String queryUnPrintCustByOptr()throws Exception;
//	List<String> queryUnPrintCustByOptr()throws Exception;
	
	public String queryUnRefundByOptr() throws Exception ;
	
	/**
	 * 查询居民客户
	 * @param query
	 * @return
	 * @throws Exception
	 */
	List<CCust> searchResidentCust(CCust cust) throws Exception;
	
	/**
	 * 通过cust的小区，号，楼，单元来查询客户信息
	 * @param cust
	 * @param optr
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<CCust> queryCustAddress(CCust cust,SOptr optr,Integer start,Integer limit) throws Exception;
	
	/**
	 * 根据多个客户编号查询客户信息
	 * @param custIds
	 * @param optr
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<CCust> queryCustAddrByCustIds(String[] custIds,SOptr optr,Integer start,Integer limit) throws Exception;
	
	/**
	 * 根据地址编号返回客户数量
	 * @param addrId
	 * @param optr
	 * @return
	 * @throws Exception
	 */
	public int queryAddressAll(String addrId,SOptr optr) throws Exception;
	
	/**
	 * 根据操作员数据规则查询重要客户数量
	 * @return
	 * @throws Exception
	 */
	public Object queryImportanceCustNum() throws Exception;
	
	/**
	 * 根据操作员数据规则分页查询重要客户
	 * @param start
	 * @param limit
	 * @return
	 * @throws JDBCException
	 * @throws Exception
	 */
	Pager<CCust> queryImportanceCust(Integer start, Integer limit) throws JDBCException, Exception;

	/**
	 * 查询欠费账单.
	 * @param custId
	 * @param queryFeeInfo
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<BillDto> queryCustBill(String custId, QueryFeeInfo queryFeeInfo, Integer start, Integer limit) throws Exception;
	
	/**
	 * 查询账单销帐记录.
	 * @param billSn
	 * @return
	 * @throws Exception
	 */
	public List<BBillWriteoff> queryCustBillWriteOff(String billSn) throws Exception;
	
	
	/**
	 * 查看VOD指令
	 * @param cardId
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<JVodCommandDto> queryVodCommandByCardId(String cardId,Integer start,Integer limit) throws Exception ;
	/**
	 * 查看Band指令
	 * @param param
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<JBandCommandDto> queryBandCommandByParam(Map<String,String> param,Integer start,Integer limit) throws Exception ; 
	/**
	 * 查询数据库时间
	 * @return
	 */
	public String syncServerTime() throws JDBCException, Exception;

	public Pager<FeePayDto> queryFeePay(String residentCustId,QueryFeeInfo queryFeeInfo, Integer start,
			Integer limit)throws Exception ;

	public List<FeeDto> queryFeePayDetail(String paySn) throws Exception;

	public List<TProvince> queryProvince()throws Exception;

}