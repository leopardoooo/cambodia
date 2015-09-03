/**
 *
 */
package com.ycsoft.business.service;

import java.util.List;
import java.util.Map;

import com.ycsoft.beans.config.TServerRes;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdInvalidTariff;
import com.ycsoft.beans.core.prod.CProdOrderFee;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.promotion.CPromProdRefund;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PPromFee;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.abstracts.IBaseService;
import com.ycsoft.business.dto.core.prod.CProdBacthDto;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.CPromotionAcctDto;
import com.ycsoft.business.dto.core.prod.CPromotionDto;
import com.ycsoft.business.dto.core.prod.PProdDto;
import com.ycsoft.business.dto.core.prod.ProdResDto;
import com.ycsoft.business.dto.core.prod.PromTreeDto;
import com.ycsoft.business.dto.core.prod.UserProdDto;
import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.daos.core.Pager;



/**
 * @author sheng
 * May 17, 2010 3:10:51 PM
 */
public interface IQueryUserService extends IBaseService{
	
	/**
	 * 查询用户未排斥资源
	 * @param userId
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<PRes> queryUnRejectRes(String userId,String custId) throws Exception;
	
	/**
	 * 查询用户排斥资源
	 * @param userId
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<PRes> queryRejectRes(String userId,String custId) throws Exception;

	/**
	 * 查询用户产品资源
	 * @param prodSn
	 * @return
	 * @throws Exception
	 */
	public List<PRes> queryUserProdRes(String prodSn) throws Exception;
	
	/**
	 * 查询订购的产品已选择动态资源和该产品所有动态资源
	 * @param prodSn
	 * @return
	 * @throws Exception
	 */
	public ProdResDto queryDynResByProdSn(String prodSn) throws Exception;

	/**
	 * 查询当前产品的状态异动信息
	 * @param prodId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public Pager<CProdPropChange> querProdPropChange(String prodId, Integer start, Integer limit) throws Exception;

	/**
	 * 查询当前产品的资费变更信息
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public Pager<CProdInvalidTariff> queryTariffChange(String prodId,Integer start,Integer limit) throws Exception;

	/**
	 * 根据资费ID 查询产品资费信息
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public PProdTariff queryProdTariffById(String tariffId) throws Exception;

	/**
	 * 根据客户ID查询对应用户信息
	 * @param custId
	 * @return
	 */
	List<UserDto> queryUser(String custId) throws Exception;
	
	/**
	 * 根据客户ID查询对应用户历史信息
	 * @param custId
	 * @return
	 */
	List<UserDto> queryUserHis(String custId) throws Exception;

	/**
	 * 根据用户ID查询用户受理记录
	 * @param userId
	 * @return
	 */
	List<CDoneCode> queryUserDoneCode(String userId) throws Exception;

	/**
	 * 根据用户id查询用户异动信息
	 * @param userId
	 * @return
	 */
	List<CUserPropChange> queryUserPropChange(String userId,String userType) throws Exception;
	
	Pager<CProdOrderFee> queryOrderFeeDetail(String orderSn, Integer start, Integer limit) throws Exception;
	
	/**
	 * 根据用户id查询用户促销信息
	 * @param userId
	 * @return
	 */
	public List<CPromotionDto> queryUserPromotion(String userId) throws Exception;
	
	/**
	 * 查询用户可以回退的促销.
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<CPromotionDto> queryPromotionCanCancel(String userId, String prodId) throws Exception;

	/**
	 * 查询客户下所有用户的产品,按userId 封装
	 * @param custId
	 * @return
	 */
	public Map<String,List<CProdDto>> queryAllProd(String custId) throws Exception;
	
	public List<CProdDto> queryProdByCustId(String custId) throws Exception;
	
	/**
	 * 呼叫中心用户已订购产品
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public List<UserProdDto> queryUserProdToCallCenter(Map<String,Object> p) throws Exception;
	
	/**
	 * 呼叫中心用户退订产品
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public List<UserProdDto> queryUserProdHisToCallCenter(Map<String,Object> p) throws Exception;
	
	/**
	 * 根据编号号数组查询产品信息
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<CProdBacthDto> queryProdByIds(String[] ids,String type,String prodId) throws Exception;
	
	
	public List<CProd> queryBaseProdByIds(String[] ids,String type) throws Exception;
	
	
	/**
	 * 查询客户下所有用户的产品历史,按userId 封装
	 * @param custId
	 * @return
	 */
	public Map<String,List<CProdDto>> queryAllProdHis(String custId) throws Exception;

	/**
	 * 查询用户下订购的所有产品
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<CProdDto> querProdByUserId(String userId) throws Exception;

	/**
	 * 查询所有模拟产品
	 * @return
	 * @throws Exception 
	 */
	public List<PProd> queryAtvProds() throws Exception;
	/**
	 * 根据客户编号查询用户信息
	 * @return
	 * @throws Exception 
	 */
	public List<CUser> queryUserByCustId(String custId) throws Exception;
	/**
	 * 根据用户编号查询用户信息
	 * @return
	 * @throws Exception 
	 */
	public CUser queryUserById(String userId) throws Exception;
	/**
	 * 根据设备编号查询用户信息
	 * @return
	 * @throws Exception 
	 */
	public UserDto queryUserByDeviceId(String deviceId) throws Exception;
	
	/**
	 * 根据county_id查询所有产品信息
	 * @return
	 * @throws Exception
	 */
	public List<PProdDto> queryProdByCountyId(String countyId, String prodStatus,
			String tariffStatus, String ruleId, String tariffType)throws Exception;
	
	/**
	 * 根据客户编号和用户状态查询用户信息
	 * @param custNo
	 * @return
	 * @throws Exception
	 */
	public List<CUser> queryUserByCustNoAndStatus(String custNo,String userStatus) throws Exception;
	
	/**
	 * 根据促销标识查询促销中的产品信息
	 * @param promotionSn	促销唯一标识
	 * @param promotionId	促销编号
	 * @return
	 * @throws Exception
	 */
	public List<CPromotionAcctDto> queryPromotionProdBySn(String promotionSn,String promotionId) throws Exception;

	/**
	 *  查询本地区适用套餐
	 * @param custId
	 * @param optr
	 * @return
	 * @throws Exception
	 */
	public List<PPromFee> querySelectablePromPay(String custId,SOptr optr) throws Exception;
	
	
	/**
	 * 查看该产品属进行的套餐缴费列
	 * @param userId
	 * @param prodSn
	 * @return
	 * @throws Exception
	 */
	public List<CPromProdRefund> querySelectPromFee(String userId,String prodSn) throws Exception;

	/**
	 * 生成套餐树，以及可以使用的终端信息
	 * @param custId
	 * @param promFeeId
	 * @return
	 * @throws Exception
	 */
	public PromTreeDto querySelectUserProm(String custId, String promFeeId) throws Exception;
	
	public Pager<UserDto> queryUserInfoToCallCenter(Map<String ,Object> params, Integer start, Integer limit) throws Exception;
	
	/**
	 * 中兴宽带资源
	 * @return
	 * @throws Exception
	 */
	public List<TServerRes> queryZteBandRes() throws Exception;
}
