package com.ycsoft.business.service;

import java.util.List;

import com.ycsoft.beans.core.bill.BillDto;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.valuable.CValuableCard;
import com.ycsoft.beans.core.valuable.CValuableCardFee;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.system.SCounty;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.abstracts.IBaseService;
import com.ycsoft.business.dto.core.acct.AcctitemDto;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
import com.ycsoft.business.dto.device.ValuableCardDto;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

public interface IValuableCardService extends IBaseService{

	/**
	 * 验证客户密码（客户查询需要调用）
	 * @param custId
	 * @param password
	 * @return
	 */
	public CCust validCustByPassword(String custId,String password) throws Exception;
	
	/**
	 * 根据智能卡号查询用户（指令重发需要调用）
	 * @param cardNo
	 * @return
	 */
	public CUser queryUserByCard(String cardNo) throws Exception;
	
	/**
	 * 查询客户月账单
	 * @param custId 客户编号
	 * @param month 月份 格式YYYYMM
	 * @return
	 * @throws Exception
	 */
	public List<BillDto> queryBillByCustId(String custId,String month) throws Exception;
	
	/**
	 * 获取缴费需要的数据，用于账户充值
	 * @param custId
	 * @param acctId
	 * @param acctItemId
	 * @param fee
	 * @throws Exception
	 */
	public List<PayDto> savePayAcct(String acctId,String acctItemId,int fee) throws Exception;
	
	/**
	 * 查询用户的所有账目信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<AcctitemDto> queryAcctItemByUserId(String userId) throws Exception;
	
	/**
	 * 查询所有数字产品
	 * @param areaId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<PProd> queryAllDtvProds(String servId ,String areaId,String countyId) throws Exception;
	/**
	 * 查询所有数字产品资费
	 * @param areaId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public  List<ProdTariffDto> queryProdTariffByProdId(String prod_id ) throws Exception;
	/**
	 * 查询所有县市
	 * @return
	 * @throws JDBCException
	 */
	public List<SCounty> queryAllCounty() throws JDBCException;
	
	/**
	 * @throws Exception 
	 * 
	 */
	public SOptr queryOptrByLoginName(String loginName,String pwd) throws Exception;
	
	public void saveCvaluableCardFee(CValuableCardFee cValuableCardFee ) throws JDBCException;
	
	public Pager<ValuableCardDto> queryValuableAllCard(Integer start, Integer limit, String query,String queryItem) throws Exception;
	
	/**
	 * 保存充值卡
	 * @param records
	 * @param optr
	 * @throws Exception
	 */
	public String saveValuableCard(Integer amount,String records,SOptr optr) throws Exception;
	
	/**
	 * 删除充值卡
	 * @param deviceCode
	 * @param optr
	 * @throws Exception
	 */
	public String removeValuableCard(String[] valuableId,SOptr optr) throws Exception;
	
	/**
	 * 查询充值卡
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 */
	public CValuableCard queryValuableCard(String deviceCode) throws Exception;
	
	public void editValuableCard(String doneCode,String custName) throws Exception;

}
