package com.ycsoft.business.service;


import java.util.Date;
import java.util.List;

import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.config.TDeviceChangeReason;
import com.ycsoft.beans.core.prod.CancelUserDto;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.prod.PPromotionAcct;
import com.ycsoft.beans.prod.PSpkg;
import com.ycsoft.beans.prod.PSpkgOpenbusifee;
import com.ycsoft.beans.prod.PSpkgOpenuser;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.abstracts.IBaseService;
import com.ycsoft.business.dto.core.fee.FeeInfoDto;
import com.ycsoft.business.dto.core.prod.DisctFeeDto;
import com.ycsoft.business.dto.core.prod.PromotionDto;
import com.ycsoft.business.dto.core.user.UserInfo;
import com.ycsoft.business.dto.core.user.UserRes;
import com.ycsoft.daos.core.JDBCException;

/**
 * 用户组件
 * @author YC-SOFT
 *
 */
public interface IUserService extends IBaseService{

	/**
	 * 用户开户
	 * @param user
	 */
	public void createUser(CUser user,String deviceBuyMode,FeeInfoDto deviceFee) throws Exception;
	
	/**
	 * supernet 使用
	 * @param user
	 * @param deviceId
	 * @param deviceType
	 * @param deviceModel
	 * @param deviceBuyMode
	 * @param deviceFee
	 * @throws Exception
	 */
	public void createUser(CUser user, String deviceId, String deviceType, String deviceModel, String deviceBuyMode,
			FeeInfoDto deviceFee)  throws Exception;
	
	public void createUserBatch(List<UserInfo> userList, String stopType, String isHand) throws Exception;
	
//	public void saveChangeDevice(String userId,String deviceId,String devcieBuyMode,FeeInfoDto deviceFee, String changeReason, boolean reclaim)  throws Exception;
	public void saveChangeDevice(String userId, String deviceCode, String changeReason, String deviceBuyMode, FeeInfoDto deviceFee) throws Exception;

	/**
	 * 修改用户信息
	 * @param propChangeList	变化的属性集合
	 * @throws Exception
	 */
	public void editUser(List<CUserPropChange> propChangeList) throws Exception;
	
	/**
	 * 第二终端转副机
	 * @param propChangeList
	 * @param prodSn
	 * @throws Exception
	 */
	public void saveEzdtoFzd(List<CUserPropChange> propChangeList,String prodSn,String newTariffId) throws Exception;
	
	/**
	 * 修改机顶盒号
	 * @param stbId
	 * @throws Exception
	 */
	public void editStb(String stbId,String cardId) throws Exception;

	/**
	 * 用户销户(支持多用户)
	 * @param logoffUserDto
	 */
	public void saveRemoveUser(String userId, Integer cancelFee, Integer refundFee) throws Exception;

	/**
	 * 开通双向
	 * @param netType		 双向接入方式
	 * @param modemMac		 modem mac地址
	 * @param vodUserType	双向用户类型
	 * @param password 
	 * @param remainReplacoverDate 是否保留原设备(机顶盒)的保修期.
	 * @throws Exception
	 */
	public void saveOpenInteractive(String netType,String modemMac,String password,String vodUserType,String remainReplacoverDate) throws Exception;

	/**
	 * 模拟转数字
	 * @param user
	 * @param curMonthFee    本月模拟费用
	 * @param payFee		 缴费金额
	 * @throws Exception
	 */
	public void saveAtvToDtv(CUser user,int curMonthFee,int payFee) throws Exception;
	
	/**
	 * 检查用户能报停
	 * @throws Exception
	 */
	public void checkStopUser(String[] userIds) throws Exception;
	
	public void untuckUsers() throws Exception;

	/**
	 * 报停(支持多用户)
	 * @param effectiveDate 报停时间
	 * @throws Exception
	 */
	public void saveStop(String effectiveDat,int tjFee) throws Exception;

	/**
	 * 取消预报停(支持多用户)
	 * @throws Exception
	 */
	public void cancelStopUser() throws Exception;
	
	/**
	 * 报开(支持多用户)
	 * @param datas
	 */
	public void saveOpen(String stbId,String cardId,String modemMac,int tjFee) throws Exception;

	/**
	 * 数字电视用户指令重发
	 * @throws Excpetion
	 */
	public void saveResendCa() throws Exception;

	
	/**
	 * 数字电视用户指令刷新
	 * @throws Excpetion
	 */
	public void saveRefreshCa(String refreshType) throws Exception;
	
	/**
	 * 宽带用户修改上网密码
	 * @param newPwd   新密码
	 * @throws Exception
	 */
	public void saveEditPwd(String loginName, String newPwd) throws Exception;
	
	/**
	 * 修改连接数
	 * @param maxConn
	 * @throws Exception
	 */
	public void saveEditConnect(int maxConn) throws Exception;
	
	/**
	 * 清除绑定
	 * @throws Exception
	 */
	public void saveClearBind() throws Exception;
	
	/**
	 * 强制下线
	 * @throws Exception
	 */
	public void saveOffLine() throws Exception;
	/**
	 * 保存促销信息
	 * @param promotionId
	 * @param feeList
	 * @throws Exception
	 */
	public void savePromotion(int times,String promotionId,List<DisctFeeDto> feeList,List<PPromotionAcct> acctList) throws Exception;

	/**
	 * 保存促销取消
	 * @param promotionSn
	 * @throws Exception
	 */
	public void saveCancelPromotion(String promotionSn) throws Exception;
	
	/**
	 * 查询用户可以选择的促销
	 * @return
	 * @throws Exception
	 */
	public List<PromotionDto> querySelectableProm() throws Exception;

	/**
	 * 根据促销ID查询促销详细信息
	 * @param promotionId 促销ID
	 * @return
	 * @throws Exception
	 */
	public PromotionDto queryPromInfoById(String custId,String userId,String promotionId) throws Exception;
	
	/**
	 * 批量临时授权.
	 * @throws Exception
	 */
	public void saveOpenTempBatch() throws Exception;

	/**
	 * 用户临时授权
	 * @param userId
	 * @throws Exception
	 */
	public void saveOpenTemp() throws Exception ;
	
	/**
	 * 保存用户排斥的资源
	 * @param userId
	 * @param custId
	 * @param resIds
	 * @throws Exception
	 */
	public void saveRejectRes(String userId, String custId, String resIds) throws Exception;
	
	/**
	 * 查询用户有效资源
	 * @param userIds
	 * @return
	 * @throws Exception
	 */
	public List<UserRes> queryValidRes(String userId) throws Exception;
	
	/**
	 * 部门下的是所有操作员
	 * @param deptId
	 * @return
	 * @throws JDBCException
	 */
	public List<SOptr> getByDeptId(String deptId) throws JDBCException;
	
	/**查询预报停的信息,如果有,返回信息串
	 * @param userLists
	 * @return
	 * @throws Exception
	 */
	public Object queryStopByUsers(String userLists) throws Exception;
	
	/**
	 * 取消双向
	 * @throws Exception
	 */
	public void saveCancelOpenInteractive() throws Exception;
	
	/**
	 * 修改接入方式
	 * @param netType
	 * @param modemMac
	 * @throws Exception
	 */
	public void saveEditNetType(String netType,String modemMac) throws Exception;
	
	/**
	 * 查询租赁费
	 * @return
	 * @throws Exception
	 */
	public TBusiFee queryZlFeeById() throws Exception;
	
	/**
	 * 保存租赁费用
	 * @param fee_Id
	 * @param amount
	 * @throws Exception
	 */
	public void saveLeaseFee(String fee_Id,String amount) throws Exception;
	
	
	/**
	 * 模拟剪线
	 * @throws Exception
	 */
	public void saveAtvCustLine() throws Exception;
	
	/**
	 * 模拟恢复
	 * @throws Exception
	 */
	public void saveAtvActive() throws Exception ;
	
	/**续报停
	 * @throws Exception
	 */
	public void editUserStop() throws Exception;

	/**
	 * 验证宽带登录账号不能重复
	 * @param loginName
	 */
	public void checkLoginName(String loginName)throws Exception;

	/**
	 * 一体机转换
	 * @param newCardId
	 * @param str4
	 * @param str5
	 * @param reclaim
	 * @param deviceStatus
	 */
	public void saveToSingleCard(String newCardId, String str4, String str5,
			boolean reclaim, String deviceStatus) throws Exception;
	
	/**
	 * 充值卡充值
	 * @param icCard	智能卡号
	 * @param rechargeCard	充值卡密码
	 * @throws Exception
	 */
	@Deprecated
	public void saveRechargeCard(String icCard,String rechargeCard) throws Exception;
	
	/**
	 * 获取宽带登录账号
	 * @param loginName
	 * @param county_id
	 * @return
	 */
	public Object createLoginName(String loginName, String county_id);


	/**
	 * 重发开户指令
	 */
	public void saveResendUserCmd() throws Exception;
	

	
	/**
	 * 批量修改用户状态
	 * @param userIds
	 * @param userStatus
	 * @throws Exception
	 */
	public void updateUserStatus(List<String> userIds,String userStatus) throws Exception;
	
	/**
	 * 恢复用户状态
	 * @return
	 * @throws Exception
	 */
	public void renewUser(String userId) throws Exception;
	
	/**
	 * 更换促销
	 * @param times
	 * @param promotionSn
	 * @param promotionId
	 * @param acctList
	 * @throws Exception
	 */
	public void saveChangePromotion(int times, String promotionSn,
			String promotionId, List<PPromotionAcct> acctList) throws Exception;
	
	/**
	 * 取消授权
	 * @throws Excpetion
	 */
	public void saveCancelCaAuth() throws Exception;
	/**
	 * 重算到期日期
	 * @throws Exception
	 */
	public void saveUserInvalid()throws Exception;

	/**
	 * @param userIdList
	 */
	public void batchLogoffUser(List<CancelUserDto> cancelUserList) throws Exception;

	public void editFreeUser(String userId, String prodId, String tariffId,String type,Date tariffStartDate )throws Exception;
	public void transferUsers(String toCustId) throws Exception  ;
	public void validAccount(String name) throws Exception;
	public List<TDeviceChangeReason> queryDeviceChangeReason() throws Exception;

	List<PSpkgOpenuser> querySpkgUser(String spkgSn,String custId) throws Exception;

	List<PSpkgOpenbusifee> querySpkgOpenFee(String spkgSn) throws Exception;
	
	public void cancelInstallTask(String taskId) throws Exception;

}
