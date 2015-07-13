/**
 *
 */
package com.ycsoft.business.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.core.job.JCaCommand;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.business.commons.abstracts.IBaseService;
import com.ycsoft.business.dto.core.prod.CProdBacthDto;
import com.ycsoft.business.dto.core.prod.PProdDto;
import com.ycsoft.business.dto.core.prod.ProdDictDto;
import com.ycsoft.business.dto.core.prod.ProdResDto;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
import com.ycsoft.business.dto.core.user.UserProdRscDto;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.daos.core.Pager;

/**
 * @author YC-SOFT
 *
 */
public interface IUserProdService extends IBaseService{

	/**
	 * 查询资费的折扣信息
	 * @param tariffIds 资费编号
	 * @return
	 * @throws Exception
	 */
	public List<ProdTariffDto> queryTariffByTariffIds(String[] tariffIds,String[] userIds,String custId) throws Exception;
	
	public List<ProdTariffDto> queryBatchTariffByTariffId(String[] tariffIds) throws Exception;

	/**
	 * 查找用户可以订购的产品
	 * 可以直接订购的产品包括基本产品和用户套餐
	 * 产品状态为正常并且不在用户已经订购的产品中
	 * @param userIds	用户编号
	 * @param userType	用户类型
	 * @return
	 */
	public List<ProdDictDto> queryCanOrderProd(String[] userIds,String userType,String servType) throws Exception;

	/**
	 * 查找宽带用户可以订购的产品
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<PProd> queryCanOrderBandProd(String userId) throws Exception;
	/**
	 * 批量订购
	 * @param userIds
	 * @param userType
	 * @return
	 * @throws Exception
	 */
	public List<ProdDictDto> queryBatchCanOrderProd(String userType) throws Exception;
	/**
	 * 呼叫中心 查询用户可订购产品
	 * @param userIds
	 * @param userType
	 * @param servType
	 * @return
	 * @throws Exception
	 */
	public List<PProdDto> queryCanOrderProdToCallCenter(String[] userIds, String userType,String servType) throws Exception;
	
	/**
	 * 根据客户编号，查找可以订购的产品
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<ProdDictDto> queryCanOrderPkg(String custId) throws Exception;

	/**
	 * 查找产品对应的资源信息，包括动态资源和静态资源
	 * @param prodId	产品编号
	 * @return
	 */
	public List<ProdResDto> queryProdRes(String prodId) throws Exception;

	/**
	 * 查找产品对应的子产品
	 * @param prodId	产品编号
	 * @return
	 */
	public List<PProd> querySubProds(String prodId) throws Exception;


	/**
	 * 查找产品可以选择的资费
	 * @param prodId
	 * @param custDto
	 * @param tariffId 
	 * @return
	 * @throws Exception
	 */
	public List<ProdTariffDto> queryProdTariff(String[] userIds,String prodId,String tariffId) throws Exception;
	
	
	/**
	 * 修改免费终端，查询基本产品资费
	 * @param userIds
	 * @param type
	 * @param prodId
	 * @param tariffId
	 * @return
	 * @throws Exception
	 */
	public List<ProdTariffDto> queryFreeTariff(String[] userIds,String type,String prodId,String tariffId) throws Exception;

	/**
	 * 查找产品可以选择的资费,包含不同周期的资费
	 * @param prodId
	 * @param custDto
	 * @param tariffId 
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> queryEditProdTariff(String[] userIds,String prodId,String tariffId) throws Exception;
	
	/**
	 * 批量订购产品可选择的资费
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public List<ProdTariffDto> queryBatchProdTariff(String prodId) throws Exception;
	
	public List<ProdTariffDto> queryAllProdTariff(String[] userIds, String prodId,String tariffId) throws Exception;

	/**
	 * 第二终端转副机的时候基本包可以选择的资费
	 * @param custId
	 * @param userId
	 * @param prodId
	 * @param tariffId
	 * @return
	 * @throws Exception
	 */
	public List<ProdTariffDto> queryTariffForEzdToFzd(String custId,String userId,String prodId,String tariffId)
		throws Exception;
	
	/**
	 * 保存产品订购
	 * @param prodId		产品id
	 * @param tariffId		资费id
	 * @param feeDate		开始计费日期
	 * @param dynamicRscList	动态资源信息
	 * @throws Exception
	 */
	public void saveOrder(String prodId,String tariffId,String feeDate, List<UserProdRscDto> dynamicRscList, String expDate, Date preOpenTime,String isBankPay)throws Exception;
	/**
	 * 保存产品订购
	 * @param prodId		产品id
	 * @param tariffId		资费id
	 * @param feeDate		开始计费日期
	 * @param dynamicRscList	动态资源信息
	 * @throws Exception
	 */
	public void saveOrder(String prodId,String tariffId,String feeDate, List<UserProdRscDto> dynamicRscList, String expDate,String publicAcctItemType)throws Exception;
	
	/**
	 * 批量订购产品
	 * @param userIds
	 * @param prodId
	 * @param tariffId
	 * @param feeDate
	 * @param dynamicRscList
	 * @param expDate
	 * @param publicAcctItemType
	 * @throws Exception
	 */
	public void saveBatchOrder(List<String> userIdList, String prodId,
			String tariffId, String feeDate,
			List<UserProdRscDto> dynamicRscList, String expDate) throws Exception;
	
	/**
	 * 批量退订产品
	 * @param userIdList
	 * @param prodId
	 * @throws Exception
	 */
	public void saveBatchCancel(List<String> userIdList, String prodId) throws Exception;

	
	/**
	 * 保存批量订购产品
	 * @param pordLists
	 * @throws Exception
	 */
	public void saveOrderList(String pordLists)throws Exception;

	
	/**
	 * 保存客户套餐订购
	 * @param prodId
	 * @param tariffId
	 * @param feeDate
	 * @param stopType 
	 * @param prodSns
	 * @param preOpenDate 预开通时间.
	 * @throws Exception
	 */
	public void saveOrderCustPkg(String prodId, String tariffId, String feeDate,String stopType, String[] prodSns) throws Exception;
	
	/**
	 * 变更到期日
	 * @param prodSn
	 * @param invalidDate
	 * @throws Exception
	 */
	public void editInvalidDate(String prodSn, String invalidDate) throws Exception;
	/**
	 * 修改用户产品的资费
	 * @param prodSn
	 * @param newTariffId
	 * @param effDate
	 * @param expDate 
	 * @throws Exception
	 */
	public void changeTariff(String prodSn,String newTariffId,String effDate, String expDate,boolean isUpdate) throws Exception;
	
	public void bacthChangeTariff(List<CProdBacthDto> pordList,String newTariffId,boolean isUpdate) throws Exception;

	/**
	 * 修改失效日期
	 * @param prodSn
	 * @param expDate
	 * @throws Exception
	 */
	public void changeExpDate(String prodSn,String expDate) throws Exception;
	
	/**
	 * 取消资费生效
	 * @param prodSn
	 * @param tariffId
	 * @throws Exception
	 */
	public void removeByProdSn(String prodSn, String tariffId) throws Exception;
	
	/**
	 * 查询用户产品的的资源信息
	 * @param prodSn
	 * @return
	 * @throws JDBCException
	 */
	public List<PRes> queryProdResByUserProdSn(String prodSn) throws Exception ;

	/**
	 * 终止产品
	 * @param prodSns
	 * @param banlanceDealType
	 * @throws Exception
	 */
	public void saveTerminate(String[] prodSn,String banlanceDealType,String transAcctId,String transAcctItemId,String promFeeSn) throws Exception;

	/**
	 * 更换宽带产品
	 * @param prodId
	 * @param tariffId
	 * @param feeDate
	 * @param expDate
	 * @param oldProdSn
	 * @param present_fee
	 * @throws Exception
	 */
	public void changeBandProd(String prodId,String tariffId,String feeDate,String expDate,String oldProdSn,int present_fee) throws Exception;
	
	/**
	 * 更换产品动态资源
	 * @param prodSn
	 * @param dyResList
	 * @throws Exception
	 */
	public void changeProdDynRes(String prodSn,List<UserProdRscDto> dyResList) throws Exception;
	
	/**
	 * 终止套餐
	 * @param prodSn
	 * @param banlanceDealType
	 * @param transAcctId
	 * @param transAcctItemId
	 * @throws Exception
	 */
	public void saveTerminatePkg(String prodSn,String banlanceDealType,String transAcctId,String transAcctItemId) throws Exception;
	
	/**
	 * 同步产品
	 * @throws Exception
	 */
	public void saveProdSyn(String[] prodSns,String[] userIds) throws Exception;

	
	/**
	 * 修改客户套餐对应的产品
	 * @param pkgSn
	 * @param stopType 
	 * @param prodSns
	 * @throws Exception
	 */
	public void saveEditCustPkg(String pkgSn,String stopType, String[] prodSns) throws Exception;
	
	/**
	 * 重设用户产品对应的资源
	 * @throws Exception
	 */
	public void resetUserProdRes() throws Exception;
	
	/**
	 * 根据用户Id和产品Id查询已订购产品
	 * @param userId
	 * @param prodId
	 * @return
	 */
	public CProd queryOrderdProdByUserId(String userId,String prodId) throws Exception;
	
	
	/**
	 * 修改产品
	 * @param propChangeList
	 * @throws Exception
	 */
	public void saveEditProd(String prodSn , List<CProdPropChange> propChangeList) throws Exception;
	
	
	//产品暂停
	public void pauseProd(String prodSn,String userId) throws Exception;
	
	//产品恢复
	public void resumeProd(String prodSn,String userId) throws Exception;

	/**
	 * 修改产品的预开通时间.
	 * @param prodSn
	 * @param countyId
	 * @param newPreOpenDate 新的预开通时间.
	 */
	public void updateProdPreOpenDate(String prodSn, String countyId,Date newPreOpenDate, Date feeDate) throws ServicesException;

	/**
	 * 修改产品 公用账目适用类型 .
	 * @param prodSn
	 * @param countyId
	 * @param publicAcctitemType
	 */
	public void updatePublicAcctItemType(String prodSn, String countyId,String publicAcctitemType) throws ServicesException;
	
	
	/**
	 * modify cust class and zzd base prod's tariff is YBZF
	 * @throws Exception
	 */
	public void resumeCustClass()throws Exception;
	
	
//	public Date getInvalidDateByFee(long fee,int balance,int oweFee,int realFee,int rent,String rentType,Date beginFeeDate)throws Exception;
//	
//	
//	public long getFeeByInvalidDate(int balance,int oweFee,int realFee,int rent,String rentType,Date invaidDate,Date beginFeeDate) throws Exception;

	public Date getInvalidDateByFee(String prodSn,int payFee)throws Exception;
	
	
	public long getFeeByInvalidDate(String prodSn,Date invalidDate) throws Exception;

	public void saveBusiCmdCard(String cardId)throws Exception;

	/**
	 * @param cardId
	 * @param start
	 * @param limit
	 * @return
	 */
	public Pager<JCaCommand> queryCaCommandByCardId(String cardId, Integer start,Integer limit)throws Exception;

	/**
	 * 变更银行扣费
	 * @param prodSn
	 */
	public void changeCprodBank(String prodSn)throws Exception;

	/**
	 * 重算到期日.`
	 * @param prodSn
	 * @return
	 */
	public Date reCalcInvalidDate(String prodSn) throws Exception;
}
