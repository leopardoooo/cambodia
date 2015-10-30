package com.ycsoft.business.component.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TCustColonyCfg;
import com.ycsoft.beans.config.TDeviceChangeReason;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.common.CDoneCodeDetail;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustDevice;
import com.ycsoft.beans.core.job.JUserStop;
import com.ycsoft.beans.core.prod.CProdOrderFee;
import com.ycsoft.beans.core.promotion.CPromFee;
import com.ycsoft.beans.core.promotion.CPromProdRefund;
import com.ycsoft.beans.core.user.CRejectRes;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserBroadband;
import com.ycsoft.beans.core.user.CUserDtv;
import com.ycsoft.beans.core.user.CUserHis;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.core.user.CUserStb;
import com.ycsoft.beans.device.RModemModel;
import com.ycsoft.beans.device.RStbModel;
import com.ycsoft.beans.prod.PPromFee;
import com.ycsoft.beans.prod.PPromFeeUser;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.beans.prod.PSpkgOpenbusifee;
import com.ycsoft.beans.prod.PSpkgOpenuser;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.component.config.ExpressionUtil;
import com.ycsoft.business.dao.config.TCustColonyCfgDao;
import com.ycsoft.business.dao.config.TDeviceChangeReasonDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.cust.CCustDeviceDao;
import com.ycsoft.business.dao.core.job.JUserStopDao;
import com.ycsoft.business.dao.core.prod.CProdOrderFeeDao;
import com.ycsoft.business.dao.core.promotion.CPromFeeDao;
import com.ycsoft.business.dao.core.promotion.CPromProdRefundDao;
import com.ycsoft.business.dao.core.promotion.CPromotionDao;
import com.ycsoft.business.dao.core.user.CRejectResDao;
import com.ycsoft.business.dao.core.user.CUserAtvDao;
import com.ycsoft.business.dao.core.user.CUserBroadbandDao;
import com.ycsoft.business.dao.core.user.CUserDtvDao;
import com.ycsoft.business.dao.core.user.CUserHisDao;
import com.ycsoft.business.dao.prod.PPromFeeDao;
import com.ycsoft.business.dao.prod.PPromFeeProdDao;
import com.ycsoft.business.dao.prod.PPromFeeUserDao;
import com.ycsoft.business.dao.prod.PSpkgDao;
import com.ycsoft.business.dao.prod.PSpkgOpenbusifeeDao;
import com.ycsoft.business.dao.prod.PSpkgOpenuserDao;
import com.ycsoft.business.dao.resource.device.RDeviceDao;
import com.ycsoft.business.dao.resource.device.RModemModelDao;
import com.ycsoft.business.dao.resource.device.RStbModelDao;
import com.ycsoft.business.dao.system.SOptrDao;
import com.ycsoft.business.dto.core.bill.UserBillDto;
import com.ycsoft.business.dto.core.prod.CPromotionDto;
import com.ycsoft.business.dto.core.prod.PromFeeProdDto;
import com.ycsoft.business.dto.core.user.ChangedUser;
import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.BeanHelper;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.prod.ResGroupDto;


/**
 * 用户组件
 */
@Component
public class UserComponent extends BaseBusiComponent {
	private CUserHisDao cUserHisDao;
	private CUserAtvDao cUserAtvDao;
	private CUserDtvDao cUserDtvDao;
	private CUserBroadbandDao cUserBroadbandDao;
	private SOptrDao sOptrDao;
	private CRejectResDao cRejectResDao;
	private JUserStopDao jUserStopDao;
	private CPromotionDao cPromotionDao;
	private CCustDao cCustDao;
	
	private PPromFeeDao pPromFeeDao;
	private PPromFeeUserDao pPromFeeUserDao;
	private PPromFeeProdDao pPromFeeProdDao;
	private CPromProdRefundDao cPromProdRefundDao;
	private RDeviceDao rDeviceDao;
	private CPromFeeDao cPromFeeDao;
	private TCustColonyCfgDao tCustColonyCfgDao;
	private ExpressionUtil expressionUtil ;
	@Autowired
	private RStbModelDao rStbModelDao;
	@Autowired
	private RModemModelDao rModemModelDao;
	@Autowired
	private CCustDeviceDao cCustDeviceDao;
	@Autowired
	private CProdOrderFeeDao cProdOrderFeeDao;
	@Autowired
	private TDeviceChangeReasonDao tDeviceChangeReasonDao;
	@Autowired
	private PSpkgDao pSpkgDao;
	@Autowired
	private PSpkgOpenuserDao pSpkgOpenuserDao;
	@Autowired
	private PSpkgOpenbusifeeDao pSpkgOpenbusifeeDao;
	
	public List<TDeviceChangeReason> queryDeviceChangeReason() throws Exception{
		return tDeviceChangeReasonDao.findAll();
	}

	public Map<String,CUser> queryUserMap(String cust_id) throws Exception{
		return CollectionHelper.converToMapSingle(cUserDao.queryUserByCustId(cust_id), "user_id");
	}
	/**
	 * 创建用户
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public CUser createUser(CUser user) throws Exception {
		//DTT,OTT用户没有机顶盒号、宽带用户没有MAC
		if (((user.getUser_type().equals(SystemConstants.USER_TYPE_DTT)
				|| user.getUser_type().equals(SystemConstants.USER_TYPE_OTT)) && StringHelper.isEmpty(user.getStb_id()))
				|| (user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)
						&& StringHelper.isEmpty(user.getModem_mac())))
			user.setStatus(StatusConstants.INSTALL);
		else
			user.setStatus(StatusConstants.ACTIVE);
		user.setIs_rstop_fee(isStopFee());
		setBaseInfo(user);
		cUserDao.save(user);
	
		return user;
	}

	private void decideFreeUser(CUserDtv dtv) throws JDBCException {
		/*
		// 客户允许有2个免费副机
		List<CUser> users = queryUserByCustId(dtv.getCust_id());
		if (dtv.getTerminal_type().equals(
				SystemConstants.USER_TERMINAL_TYPE_FZD)) {
			int freenum = 0;
			for (CUser u : users) {
				if (u.getUser_type().equals(PROD_SERV_ID_DTV)
						&& "T".equals(u.getStr19()))
					freenum++;
			}
			if ( "T".equals(dtv.getStr19()) && freenum > 1)
				dtv.setStr19("F");
			
		} else {
			dtv.setStr19("F");
		}
		*/
	}

	public String queryLastStatus(String userId) throws Exception{
		CUserPropChange upc =  cUserPropChangeDao.queryLastStatus(userId, getOptr().getCounty_id());
		if(null == upc)
			return null;
		return upc.getOld_value();
	}

	/**
	 * 删除用户信息
	 * @param doneCode
	 * @param user
	 * @throws Exception
	 */
	public void removeUserWithHis( Integer doneCode ,CUser user) throws Exception{

		CUserHis userHis = new CUserHis();
		userHis.setDone_code(doneCode);
		BeanUtils.copyProperties(user, userHis);
		userHis.setStatus(StatusConstants.INVALID);
		userHis.setStatus_date(DateHelper.now());
//		if (user.getUser_type().equals(SystemConstants.USER_TYPE_DTV))
//			userHis.setNeed_check("T");
//		else 
			userHis.setNeed_check("F");
		cUserHisDao.save(userHis);
		
//		if (user.getUser_type().equals(SystemConstants.USER_TYPE_ATV)){
//			CUserAtv atv = cUserAtvDao.findByKey(user.getUser_id());
//			CUserAtvHis atvHis = new CUserAtvHis();
//			BeanUtils.copyProperties(atv, atvHis);
//			atvHis.setDone_code(doneCode);
//			cUserAtvHisDao.save(atvHis);
//		} else if (user.getUser_type().equals(SystemConstants.USER_TYPE_DTV)){
//			CUserDtv dtv = cUserDtvDao.findByKey(user.getUser_id());
//			CUserDtvHis dtvHis = new CUserDtvHis();
//			BeanUtils.copyProperties(dtv, dtvHis);
//			dtvHis.setDone_code(doneCode);
//			cUserDtvHisDao.save(dtvHis);
//		} else if (user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
//			CUserBroadband band = cUserBroadbandDao.findByKey(user.getUser_id());
//			CUserBroadbandHis bandHis = new CUserBroadbandHis();
//			BeanUtils.copyProperties(band, bandHis);
//			bandHis.setDone_code(doneCode);
//			cUserBroadbandHisDao.save(bandHis);
//		} 
		
		removeUserWithoutHis(user.getUser_id());
	}

	public void removeUserWithoutHis(String userId) throws Exception{
		cUserDao.remove(userId);
		cUserAtvDao.remove(userId);
		cUserDtvDao.remove(userId);
		cUserBroadbandDao.remove(userId);
	}
	
	/**
	 * @param cardId
	 */
	public void updateUserCheckFlag(String cardId) throws Exception{
		this.cUserHisDao.updateCheckFlag(cardId);
		
	}

	/**
	 * 根据客户ID，获取符合条件用户的集合
	 * @param cust
	 */
	public List<CUser> queryUserByCustId(String custId) throws JDBCException {
		List<CUser> users= cUserDao.queryUserByCustId(custId);
		fillUserName(users);
		return users;
	}
	/**
	 * 查询正常和施工中的用户清单
	 * @param custId
	 * @return
	 */
	public List<CUser> queryCanSelectByCustId(String custId)throws JDBCException {
		List<CUser> users=cUserDao.queryCanSelectUserByCustId(custId);
		fillUserName(users);
		return users;
	}
	
	/**
	 * @param custId
	 * @return
	 */
	public List<CUserStb> queryUserStbByCustId(String custId) throws JDBCException, ServicesException {
		return cUserDao.queryUserStbByCustId(custId);
	}
	
	/**
	 * @param userId
	 * @return
	 */
	public CUserStb queryUserStbByUserId(String userId) throws JDBCException {
		return cUserDao.queryUserStbByUserId(userId);
	}
	
	public List<CUser> queryUserByCustIds(String[] custIds) throws Exception{
		return cUserDao.queryUserByCustIds(custIds);
	}
	
	public List<CUser> queryUserHisByCustId(String custId) throws JDBCException {
		List<CUser> users= new ArrayList<CUser>();
		users.addAll(cUserAtvDao.queryAtvHisByCustId(custId));
		users.addAll(cUserDtvDao.queryDtvHisByCustId(custId));
		users.addAll(cUserBroadbandDao.queryBandHisByCustId(custId));
		fillUserName(users);
		return users;
	}

	/**
	 * 根据客户ID，获取符合条件用户的集合
	 * @param cust
	 */
	public List<UserDto> queryUser(String custId) throws Exception {
		List<UserDto> result = new ArrayList<UserDto>();
		List<CUser> users = queryUserByCustId(custId);
		List<JUserStop> stopList = jUserStopDao.findAll();
		Map<String,List<JUserStop>> stopmap = CollectionHelper.converToMap(stopList, "user_id");
		for (CUser user :users){
			UserDto userdto = new UserDto();
			BeanUtils.copyProperties(user, userdto);
			List<JUserStop> stoplist = stopmap.get(user.getUser_id());
			if(stoplist!=null){
				userdto.setStop_date(stoplist.get(0).getStop_date());
			}
			if(userdto.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
				if(StringHelper.isNotEmpty(userdto.getModem_mac())){
					RModemModel modemModel = rModemModelDao.queryByModemMac(userdto.getModem_mac());
					if(modemModel != null){
						userdto.setDevice_model(modemModel.getDevice_model());
						userdto.setDevice_model_text(modemModel.getModel_name());
					}
					
					CCustDevice custDevice = cCustDeviceDao.queryCustDeviceByDeviceCode(userdto.getCust_id(), userdto.getModem_mac());
					if(custDevice != null){
						userdto.setBuy_model(custDevice.getBuy_mode());
						userdto.setBuy_model_text(custDevice.getBuy_mode_text());
					}
				}
			}else{
				if(StringHelper.isNotEmpty(userdto.getStb_id())){
					RStbModel stbModel = rStbModelDao.queryByStbId(userdto.getStb_id());
					if(stbModel != null){
						userdto.setDevice_model(stbModel.getDevice_model());
						userdto.setDevice_model_text(stbModel.getModel_name());
					}
					
					CCustDevice custDevice = cCustDeviceDao.queryCustDeviceByDeviceCode(userdto.getCust_id(), userdto.getStb_id());
					if(custDevice != null){
						userdto.setBuy_model(custDevice.getBuy_mode());
						userdto.setBuy_model_text(custDevice.getBuy_mode_text());
					}
				}
			}
			if(!user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE)){
				if(StringHelper.isEmpty(userdto.getDevice_model()) && StringHelper.isNotEmpty(user.getStr3())){
					userdto.setDevice_model(user.getStr3());
					if(user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
						userdto.setDevice_model_text( MemoryDict.getDictName(DictKey.MODEM_MODEL, user.getStr3()) );
					}else{
						userdto.setDevice_model_text( MemoryDict.getDictName(DictKey.STB_MODEL, user.getStr3()) );
					}
				}
				if(StringHelper.isEmpty(userdto.getBuy_model()) && StringHelper.isNotEmpty(user.getStr10())){
					userdto.setBuy_model(user.getStr10());
					userdto.setBuy_model_text( MemoryDict.getDictName(DictKey.DEVICE_BUY_MODE, user.getStr10()) );
				}
			}
			
			result.add(userdto);
		}
		return result;
	}
	
	/**
	 * 根据客户ID，获取符合条件销户用户的集合
	 * @param cust
	 */
	public List<UserDto> queryUserHis(String custId) throws JDBCException {
		List<UserDto> result = new ArrayList<UserDto>();
		List<CUser> users = queryUserHisByCustId(custId);
		for (CUser user :users){
			UserDto userdto = new UserDto();
			BeanUtils.copyProperties(user, userdto);
			result.add(userdto);
		}
		return result;
	}
	
	public void saveCancelOpenInteractive(String userId,Integer doneCode) throws Exception {
		/*
		CUser user = cUserDao.findByKey(userId);
		CUserDtv dtv = cUserDtvDao.findByKey(userId);
		List<CUserPropChange> changeList = new ArrayList<CUserPropChange>();
		
		RDevice modem = rDeviceDao.findByDeviceCode(user.getModem_mac());

		if (StringHelper.isNotEmpty(user.getModem_mac())){
			CUserPropChange change = new CUserPropChange();
			change.setColumn_name("modem_mac");
			change.setOld_value(user.getModem_mac());
			change.setNew_value("");
			setBaseInfo(change);
			change.setUser_id(userId);
			change.setDone_code(doneCode);
			change.setChange_time(DateHelper.now());
			changeList.add(change);
		}
		if (StringHelper.isNotEmpty(user.getNet_type())){
			CUserPropChange change = new CUserPropChange();
			change.setColumn_name("net_type");
			change.setOld_value(user.getNet_type());
			change.setNew_value("");
			setBaseInfo(change);
			change.setUser_id(userId);
			change.setDone_code(doneCode);
			change.setChange_time(DateHelper.now());
			changeList.add(change);
		}
		//双向用户类型
		if (StringHelper.isNotEmpty(user.getStr11())){
			CUserPropChange change = new CUserPropChange();
			change.setColumn_name("str11");
			change.setOld_value(user.getStr11());
			change.setNew_value("");
			setBaseInfo(change);
			change.setUser_id(userId);
			change.setDone_code(doneCode);
			change.setChange_time(DateHelper.now());
			changeList.add(change);
		}
		if (StringHelper.isNotEmpty(dtv.getPassword())){
			CUserPropChange change = new CUserPropChange();
			change.setColumn_name("password");
			change.setOld_value(dtv.getPassword());
			change.setNew_value("");
			setBaseInfo(change);
			change.setUser_id(userId);
			change.setDone_code(doneCode);
			change.setChange_time(DateHelper.now());
			changeList.add(change);
		}
		if (dtv.getServ_type().equals(SystemConstants.DTV_SERV_TYPE_DOUBLE)){
			CUserPropChange change = new CUserPropChange();
			change.setColumn_name("serv_type");
			change.setOld_value(dtv.getServ_type());
			change.setNew_value(SystemConstants.DTV_SERV_TYPE_SINGLE);
			setBaseInfo(change);
			change.setUser_id(userId);
			change.setDone_code(doneCode);
			change.setChange_time(DateHelper.now());
			changeList.add(change);
		}
		
		user.setNet_type("");
		user.setStr11("");
		if(modem!= null && SystemConstants.BOOLEAN_FALSE.equals(modem.getIs_virtual())){
			//不是虚拟设备
			user.setModem_mac("");
		}
		dtv.setPassword("");
		dtv.setServ_type(SystemConstants.DTV_SERV_TYPE_SINGLE);
		
		cUserDao.update(user);
		cUserDtvDao.update(dtv);
		cUserPropChangeDao.save(changeList.toArray(new CUserPropChange[changeList.size()]));
		*/
	}
	
	
	
	/**
	 * 返回userdto
	 * @param userId
	 * @return
	 * @throws JDBCException
	 */
	public CUser queryUserById(String userId) throws JDBCException {
		return cUserDao.findByKey(userId);
	}
		

	/**
	 * 根据用户ID， 查询用户异动信息
	 * @param userId
	 */
	public List<CUserPropChange> queryUserPropChange(String userId,String userType) throws Exception{
		 List<CUserPropChange> propChangelist =  cUserPropChangeDao.queryByUserId(userId,userType, getOptr().getCounty_id());
		 for(CUserPropChange upc :propChangelist){
			 upc.setBusi_name( MemoryDict.getTransData( upc.getBusi_name() ) );
			 upc.setColumn_name_text( MemoryDict.getTransData(upc.getColumn_name_text()) );
			 if (StringHelper.isNotEmpty(upc.getParam_name())){
				upc.setOld_value_text(MemoryDict.getDictName( upc.getParam_name(), upc.getOld_value()));
				upc.setNew_value_text(MemoryDict.getDictName( upc.getParam_name(), upc.getNew_value()));
			 }else {
				upc.setOld_value_text( MemoryDict.getTransData( upc.getOld_value() ));
				upc.setNew_value_text( MemoryDict.getTransData( upc.getNew_value() ));
			 }
		 }
		 return propChangelist;
	}
	
	public Pager<CProdOrderFee> queryOrderFeeDetail(String orderSn, Integer start, Integer limit) throws Exception {
		return cProdOrderFeeDao.queryOrderFeeDetail(orderSn, start, limit);
	}

	/**
	 * @param userId
	 * @param doneCode
	 * @return
	 */
	public List<CUserPropChange> queryPropChangeByDoneCode(String userId,Integer doneCode) throws Exception{
		return cUserPropChangeDao.queryByDoneCode(userId, doneCode,getOptr().getCounty_id());
	}

	/**
	 * 修改用户设备信息
	 * @param userId
	 */
	public void updateDevice(Integer doneCode,CUser user) throws Exception {
		CUser oldUser = cUserDao.findByKey(user.getUser_id());
		String[] propNames = {"stb_id","card_id","modem_mac", "str10"};
		List<CUserPropChange> upcList = new ArrayList<CUserPropChange>();
		for (String propName:propNames){
			String oldValue = BeanHelper.getPropertyString(oldUser, propName);
			String newValue = BeanHelper.getPropertyString(user, propName);
			oldValue = (oldValue == null) ? "" : oldValue;
			newValue = (newValue == null) ? "" : newValue;
			if (!oldValue.equals(newValue)){
				CUserPropChange upc = new CUserPropChange();
				upc.setUser_id(user.getUser_id());
				upc.setDone_code(doneCode);
				upc.setColumn_name(propName);
				upc.setOld_value(BeanHelper.getPropertyString(oldUser, propName));
				upc.setNew_value(BeanHelper.getPropertyString(user, propName));
				setBaseInfo(upc);
				upcList.add(upc);
			}
		}
		//修改用户信息
		cUserDao.update(user);
		//记录异动
		cUserPropChangeDao.save(upcList.toArray(new CUserPropChange[upcList.size()]));
	}

	/**
	 * 设置用户名信息
	 * 模拟用户不需要设置
	 * 数字用户为终端类型
	 * 宽带用户为宽带帐号
	 * @param records
	 */
	public void fillUserName(List<CUser> records) {
		for( CUser user :records){
			user.setUser_name(getFillUserName(user));
		}
	}


	/**
	 * 根据设备号查询用户
	 * @param deviceType
	 * @param deviceCode
	 * @return
	 */
	public List<CUser> queryUserByDevice(String deviceType, String deviceCode)
			throws JDBCException, ComponentException {
		List<CUser> user = null;
		if (deviceType.equals(SystemConstants.DEVICE_TYPE_STB))
			user = cUserDao.queryUserByStbId(deviceCode);
		else if (deviceType.equals(SystemConstants.DEVICE_TYPE_CARD))
			user = cUserDao.queryUserByCardId(deviceCode);
		else if (deviceType.equals(SystemConstants.DEVICE_TYPE_MODEM))
			user = cUserDao.queryUserByModemId(deviceCode);
		else
			throw new ComponentException("无效的设备类型" + deviceType);
		return user;
	}

	/**
	 * 更新用户设备信息
	 *
	 * @param userId
	 * @param deviceType
	 * @param deviceCode
	 * @throws JDBCException
	 * @throws ComponentException
	 */
	public void updateDevice(String userId, String deviceType, String deviceCode) throws JDBCException, ComponentException {
		if (deviceType.equals(SystemConstants.DEVICE_TYPE_STB))
			cUserDao.updateDevice(userId, "", null, null);
		else if (deviceType.equals(SystemConstants.DEVICE_TYPE_CARD))
			cUserDao.updateDevice(userId, null,"", null);
		else if (deviceType.equals(SystemConstants.DEVICE_TYPE_MODEM))
			cUserDao.updateDevice(userId, null, null,"");
		else
			throw new ComponentException("无效的设备类型" + deviceType);
	}

	/**
	 * @param userId
	 * @param doneCode
	 */
	public void removeUserPropChange(String userId, Integer doneCode) throws Exception{
		cUserPropChangeDao.removeByDoneCode( userId, doneCode,getOptr().getCounty_id());

	}
	
	/**
	 * 保存用户排斥的资源
	 * @param userId
	 * @param custId
	 * @param resIds
	 * @throws Exception
	 */
	public void saveRejectRes(String userId,String custId,String resIds) throws Exception {
		cRejectResDao.deleteByUserIdAndCustId(userId, custId);
		
		if(StringHelper.isNotEmpty(resIds)){
			String[] resIdsArr = resIds.split(",");
			List<CRejectRes> list = new ArrayList<CRejectRes>();
			for(String resId : resIdsArr){
				CRejectRes res = new CRejectRes();
				res.setUser_id(userId);
				res.setCust_id(custId);
				res.setRes_id(resId);
				list.add(res);
			}
			cRejectResDao.save(list.toArray(new CRejectRes[list.size()]));
		}
	}

	/**
	 * 获取用户id
	 */
	public String gUserId() throws Exception {
		return cUserDao.findSequence().toString();
	}
	
	/**
	 * 部门下的是所有操作员
	 * @param deptId
	 * @return
	 * @throws JDBCException
	 */
	public List<SOptr> getByDeptId(String deptId) throws JDBCException {
		return sOptrDao.getByDeptId(deptId);
	}
	
	public CUserBroadband queryBandByDeviceId(String deviceCode) throws JDBCException{
		return cUserBroadbandDao.queryBandByDeviceId(deviceCode);
	}
	
	public UserDto queryUserByDeviceId(String deviceId) throws JDBCException{
		List<CUser> users= new ArrayList<CUser>();
		CUser dtv = cUserDtvDao.queryDtvByDeviceId(deviceId);
		if (dtv!=null) users.add(dtv);
			CUser broadband = cUserBroadbandDao.queryBandByDeviceId(deviceId);
		if (broadband!=null) users.add(broadband);
		fillUserName(users);

		UserDto userdto = null;
		if (users.size()>0){
			userdto = new UserDto();
			BeanUtils.copyProperties(users.get(0), userdto);
		}
		return userdto;
 	}
	
	/**
	 * @param loginName
	 * @param countyId
	 * @return
	 */
	public CUser queryUserByLoginName(String loginName) throws Exception {
		return cUserDao.queryUserByLoginName(loginName);
	}


	
	/**
	 * @param userId
	 * @return
	 */
	public List<CPromotionDto> queryUserPromotion(String userId) throws Exception {
		return cPromotionDao.queryUserPromotion(userId,getOptr().getCounty_id());
	}
	
	public List<CPromotionDto> queryPromotionCanCancel(String userId, String prodId) throws Exception {
		List<CPromotionDto> list = new ArrayList<CPromotionDto>();
		List<CPromotionDto> result = new ArrayList<CPromotionDto>();
		
		/*
		list = cPromotionDao.queryPromotionCanCancel(userId,getOptr().getCounty_id());
		
		Date now = new Date();
		Map<String, List<CPromotionDto>> mapKeyByProdId = CollectionHelper.converToMap(list, "user_id");
		List<CPromotionDto> list2 = mapKeyByProdId.get(prodId);
		
		if(!CollectionHelper.isEmpty(list2)){
			CPromotionDto promotion = list2.get(0);
			if(promotion.getStatus().equals(StatusConstants.ACTIVE)){
				String promotionId = promotion.getPromotion_id();
				Map<String, List<CPromotionDto>> mapKeyByPromotion = CollectionHelper.converToMap(list, "promotion_id");
				List<CPromotionDto> list3 = mapKeyByPromotion.get(promotionId);
				
				for(CPromotionDto dto:list3){
					Date createDate = dto.getCreate_time();
					if(createDate.getYear() == now.getYear() && createDate.getMonth() == now.getMonth()){
						result.add(dto);
					}
				}
			}
		}
		*/
		String [] regs = new String [] {"sumPayFee(\"" + prodId + "\""
				,"ACCTITEM_"+prodId
				,"acctitem("+prodId
				,"(acctitem_id=\""+prodId+"\""
				,"payMonthsOfToday(\""+prodId+"\""}; 
		
		//查找本身是触发促销的条件的,
//		if(CollectionHelper.isEmpty(result)){
			list = cPromotionDao.queryPromotionCanCancelAsCondition(userId,prodId,getOptr().getCounty_id());
			Map<String, List<CPromotionDto>> mapKeyByRuleStr = CollectionHelper.converToMap(list, "promotion_sn");// rule_id的假名
			//数目不多,一个嵌套循环问题不大
			for(String ruleId:mapKeyByRuleStr.keySet()){
				List<CPromotionDto> list3 = mapKeyByRuleStr.get(ruleId);
				String ruleStr = null;
				if(CollectionHelper.isNotEmpty(list3)){
					ruleStr = list3.get(0).getIs_necessary();
					for(String reg:regs){
						if(ruleStr.indexOf(reg)>=0){
							result.clear();
							result.addAll(list3);
						}
					}
				}
			}
//		}
		
		return result;
	}
	
	/**
	 * @param countyId
	 * @return
	 */
	public List<ChangedUser> queryChangedUserInfo(String beginDate, String endDate,String countyId) throws JDBCException {
		List<ChangedUser> userList = new ArrayList<ChangedUser>();
		if(StringHelper.isNotEmpty(beginDate)){
			if(StringHelper.isEmpty(endDate)){
				endDate = DateHelper.formatNow();
			}
//			List<ChangedUser> addedUsers = cUserDao.queryAddedUsers(beginDate,endDate,countyId);
			List<ChangedUser> modifiedUsers = cUserDao.queryModifiedUsers(beginDate,endDate,countyId);
//			List<ChangedUser> deletedUsers = cUserDao.queryDeletedUsers(beginDate,endDate,countyId);
//			userList.addAll(addedUsers);
			userList.addAll(modifiedUsers);
//			userList.addAll(deletedUsers);
		}/*else{
			userList = cUserDao.queryChangedUserInfo(countyId);
		}*/
		return userList;
	}
	
	/**
	 * @param deviceId
	 * @param returnTvRecordCount
	 * @param returnVodRecordCount
	 * @return
	 */
	public List<UserBillDto> queryUserBill(String deviceId,
			Integer returnTvRecordCount, Integer returnVodRecordCount) throws Exception {
		return cUserDao.queryUserBill(deviceId,returnTvRecordCount,returnVodRecordCount,getOptr().getCounty_id());
	}

	public List<CUser> queryUserByCustNo(String custNo) throws Exception{
		CCust cust = cCustDao.queryCustFullInfo("CUST_NO", custNo, "1=1");
		List<CUser> userList = queryUserByCustId(cust.getCust_id());
		fillUserName(userList);
		return userList;
	}
	
	public List<CUser> queryUserByUserIds(List<String> userIds) throws Exception{
		return cUserDao.queryUserByUserIds(userIds.toArray(new String[userIds.size()]));
	}
	
	public List<CUser> queryAllUserByUserIds(String[] userIds) throws JDBCException {
		List<CUser> users = cUserDao.queryUserByUserIds(userIds);
		fillUserName(users);
		return users;
	}
	
	public List<CUser> queryAllUserHisByUserIds(String[] userIds) throws JDBCException {
		List<CUser> users = cUserHisDao.queryAllUserHisByUserIds(userIds);
		fillUserName(users);
		return users;
	}
	
	public void updateUserStatus(List<CUser> userList,
			List<CUserPropChange> upcList, List<CDoneCode> dcList,
			List<CDoneCodeDetail> dcdList) throws Exception {
		cUserDao.update(userList.toArray(new CUser[userList.size()]));
		cUserPropChangeDao.save(upcList.toArray(new CUserPropChange[upcList.size()]));
		cDoneCodeDao.save(dcList.toArray(new CDoneCode[dcList.size()]));
		cDoneCodeDetailDao.save(dcdList.toArray(new CDoneCodeDetail[dcdList.size()]));
	}
	
	
	/**
	 * @param doneCode
	 * @param busiCode
	 * @param oldUser
	 * @param newCustId
	 * @param newUserId
	 * @param forceAsZZD 强制将所有的设置为主终端.
	 * @throws Exception
	 */
	public void updateUser(Integer doneCode, String busiCode, CUser oldUser,
			String newCustId, String newUserId,boolean forceAsZZD) throws Exception {
		
//		CUser newUser = new CUser();
//		BeanUtils.copyProperties(oldUser, newUser);
//		newUser.setUser_id(newUserId);
//		newUser.setCust_id(newCustId);
//		newUser.setUser_name(forceAsZZD?"主终端":oldUser.getUser_name());
//		cUserDao.save(newUser);
//		
//		List<CUserPropChange> changeList = new ArrayList<CUserPropChange>();
//		CUserPropChange change = new CUserPropChange();
//		
//		String oldUserId = oldUser.getUser_id();
//		String oldCustId = oldUser.getCust_id();
//		String userType = oldUser.getUser_type();
//		if(userType.equals(SystemConstants.USER_TYPE_DTV)){
//			CUserDtv oldUserDtv = cUserDtvDao.findByKey(oldUserId);
//			CUserDtv newUserDtv = new CUserDtv();
//			BeanUtils.copyProperties(oldUserDtv, newUserDtv);
//			newUserDtv.setUser_id(newUserId);
//			
//			if(!oldUserDtv.getTerminal_type().equals(SystemConstants.USER_TERMINAL_TYPE_ZZD) && forceAsZZD ){//
//				change = new CUserPropChange();
//				change.setDone_code(doneCode);
//				change.setUser_id(newUserId);
//				change.setColumn_name("terminal_type");
//				change.setOld_value(oldUserDtv.getTerminal_type());
//				change.setNew_value(SystemConstants.USER_TERMINAL_TYPE_ZZD);
//				change.setBusi_code(busiCode);
//				setBaseInfo(change);
//				changeList.add(change);
//			}
//			if(forceAsZZD){
//				newUserDtv.setTerminal_type(SystemConstants.USER_TERMINAL_TYPE_ZZD);
//			}
//			cUserDtvDao.save(newUserDtv);
//		} else if(userType.equals(SystemConstants.USER_TYPE_ATV)){
//			CUserAtv oldUserAtv = cUserAtvDao.findByKey(oldUserId);
//			CUserAtv newUserAtv = new CUserAtv();
//			BeanUtils.copyProperties(oldUserAtv, newUserAtv);
//			newUserAtv.setUser_id(newUserId);
//			newUserAtv.setTerminal_type(SystemConstants.USER_TERMINAL_TYPE_ZZD);
//			cUserAtvDao.save(newUserAtv);
//		} else if(userType.equals(SystemConstants.USER_TYPE_BAND)){
//			CUserBroadband oldUserBand = cUserBroadbandDao.findByKey(oldUserId);
//			CUserBroadband newUserBand = new CUserBroadband();
//			BeanUtils.copyProperties(oldUserBand, newUserBand);
//			newUserBand.setUser_id(newUserId);
//			cUserBroadbandDao.save(newUserBand);
//		}
//		
//		change.setDone_code(doneCode);
//		change.setUser_id(newUserId);
//		change.setColumn_name("user_id");
//		change.setOld_value(oldUserId);
//		change.setNew_value(newUserId);
//		change.setBusi_code(busiCode);
//		setBaseInfo(change);
//		changeList.add(change);
//		
//		change = new CUserPropChange();
//		change.setDone_code(doneCode);
//		change.setUser_id(newUserId);
//		change.setColumn_name("cust_id");
//		change.setOld_value(oldCustId);
//		change.setNew_value(newCustId);
//		change.setBusi_code(busiCode);
//		setBaseInfo(change);
//		changeList.add(change);
//		
//		cUserPropChangeDao.save(changeList.toArray(new CUserPropChange[changeList.size()]));
//		
//		jUserStopDao.updateByUserId(oldUserId, newUserId);
//		
//		cUserDao.updateProdInclude(oldUserId, oldCustId, newUserId, newCustId);
//		
//		this.removeUserWithHis(doneCode, oldUser);
	}
	
	public void updateUser(CUser user) throws Exception {
		cUserDao.update(user);
	}
	
	/**
	 * 加一点说明,以前的要求就是所有的都修改为主终端.
	 * @param doneCode
	 * @param busiCode
	 * @param oldUser
	 * @param newCustId
	 * @param newUserId
	 * @throws Exception
	 */
	public void updateUser(Integer doneCode, String busiCode, CUser oldUser,
			String newCustId, String newUserId) throws Exception {
		updateUser(doneCode, busiCode, oldUser, newCustId, newUserId,true);
	}
	
	public void renewUser(Integer doneCode, String userId) throws Exception {
		CUser user = cUserDao.findByKey(userId);
		CUserPropChange upc = cUserPropChangeDao.queryLastStatus(userId, getOptr().getCounty_id());
		//恢复用户状态，取最近状态异动：如果最近状态异动为报停，则新状态为报停，否则均为正常
		String newValue = (upc != null && upc.getOld_value().equals(StatusConstants.REQSTOP)) ? upc.getOld_value() : StatusConstants.ACTIVE;
		
		List<CUserPropChange> upcList = new ArrayList<CUserPropChange>();
		upc = new CUserPropChange();
		upc.setUser_id(userId);
		upc.setDone_code(doneCode);
		upc.setColumn_name("status");
		upc.setOld_value(user.getStatus());
		upc.setNew_value(newValue);
		setBaseInfo(upc);
		upcList.add(upc);
		
		Date date = new Date();
		upc = new CUserPropChange();
		upc.setUser_id(userId);
		upc.setDone_code(doneCode);
		upc.setColumn_name("status_date");
		upc.setOld_value(DateHelper.dateToStr(user.getStatus_date()));
		upc.setNew_value(DateHelper.dateToStr(date));
		setBaseInfo(upc);
		upcList.add(upc);
		
		editUser(doneCode, userId, upcList);
	}
	
	/**
	 * @param userId
	 * @return
	 */
	public String queryUserLastStatus(String userId) throws Exception {
		return cUserPropChangeDao.queryUserLastStatus(userId,getOptr().getCounty_id());
	}


	/**
	 * 查询本地区可用的套餐
	 * @return
	 */
	public List<PPromFee> querySelectablePromPay() throws Exception {
		return pPromFeeDao.querySelectablePromPay(getOptr().getCounty_id());
	}
	
	/**
	 * 查询套餐缴费的基本信息.
	 * @param promfeeId
	 * @return
	 * @throws Exception
	 */
	public PPromFee queryPromFeeSimpleInfo(String promfeeId) throws Exception{
		return pPromFeeDao.findByKey(promfeeId);
	}
	
	/**
	 * 查找上月和当月的有效套餐缴费
	 * @param userId
	 * @param prodId
	 * @return
	 * @throws JDBCException
	 */
	public List<PPromFee> queryIsPromFee(String userId,String prodId) throws JDBCException {
		return pPromFeeDao.queryIsPromFee(userId,prodId);
	}
	
	/**
	 * 查看该产品属进行的套餐缴费列
	 * @param userId
	 * @param prodSn
	 * @return
	 * @throws Exception
	 */
	public List<CPromProdRefund> querySelectPromFee(String userId,String prodSn) throws Exception {
		return cPromProdRefundDao.querySelectPromFee(userId,prodSn,getOptr().getCounty_id());
	}

	/**
	 * 根据套餐编号查询套餐
	 * @param promFeeId
	 * @return
	 * @throws Exception
	 */
	public List<PPromFeeUser> queryPromFeeUser(String promFeeId) throws Exception{
		return pPromFeeUserDao.queryPromFeeUser(promFeeId);
	}
	
	/**
	 * 根据多个套餐编号查询套餐规则用户
	 * @param promFeeIds
	 * @return
	 * @throws Exception
	 */
	public List<PPromFeeUser> queryPromFeeUser(String[] promFeeIds) throws Exception{
		return pPromFeeUserDao.queryPromFeeUser(promFeeIds);
	}
	
	/**
	 * 查询多个套餐信息
	 * @param promFeeIds
	 * @param custId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CPromFee> queryPromFee(String[] promFeeIds,String custId,String countyId) throws Exception{
		return cPromFeeDao.queryPromFee(promFeeIds,custId,countyId);
	}
	
	/**
	 * 根据套餐编号查套餐信息
	 * @param promFeeId
	 * @return
	 * @throws Exception
	 */
	public List<PromFeeProdDto> queryPromFeeProds(String promFeeId) throws Exception{
		return pPromFeeProdDao.queryPromFeeProds(promFeeId);
	}

	/**
	 * 根据多个产品编号查询动态资源组信息
	 * @param ProdIds
	 * @return
	 * @throws Exception
	 */
	public List<ResGroupDto> queryGroupByProdIds(String[] ProdIds)throws Exception {
		return pPromFeeProdDao.queryGroupByProdIds(ProdIds);
	}
	
	/**
	 * 根据多个资源组编号查询资源信息
	 * @param resGroupId
	 * @return
	 * @throws Exception
	 */
	public List<PRes> queryResByGroupId(String[] resGroupId)throws Exception {
		return pPromFeeProdDao.queryResByGroupId(resGroupId);
	}
	/**
	 * 调用存储过程，批量销用户
	 * @param userIds
	 */
	public void batchLogoffUser(Integer doneCode,String remark,List<String> userIds,String isReclaimDevice,String deviceStatus) throws Exception {
		cUserDao.batchLogoffUser(doneCode,remark,userIds,isReclaimDevice,deviceStatus,getOptr());
	}
	
	public Pager<UserDto> queryUserInfoToCallCenter(Pager<Map<String ,Object>> p) throws Exception{
		Pager<UserDto> pager = cUserDao.queryUserInfoToCallCenter(p,getOptr().getCounty_id());
		List<UserDto> userList = pager.getRecords();
		for( UserDto user :userList){
			if("DTV".equals(user.getUser_type())){
				if (StringHelper.isEmpty(user.getUser_name()))
					user.setUser_name(MemoryDict.getDictName(DictKey.TERMINAL_TYPE,user.getTerminal_type()));
			}else if("BAND".equals(user.getUser_type())){
				user.setUser_name(user.getLogin_name());
			}else if("ATV".equals(user.getUser_type())){
				if (StringHelper.isEmpty(user.getUser_name()))
					user.setUser_name("模拟终端");
			}
		}
		return pager;
	}
	
	public void addUserCfg(CUser user) throws Exception{
		CCust cust = cCustDao.findByKey(user.getCust_id());
		String year = String.valueOf(DateHelper.getCurrYear());
		List<TCustColonyCfg> cfgList = tCustColonyCfgDao.queryCfg(year,true,true, cust.getCust_colony(),cust.getCust_class(), cust.getCounty_id());
		boolean key = false;
		TCustColonyCfg cfg = new TCustColonyCfg();
		for(TCustColonyCfg dto:cfgList){
			int num = tCustColonyCfgDao.queryUserNum(year, dto.getCust_colony(), dto.getCust_class(), cust.getCounty_id());
			if(dto.getUser_num() == 0 || dto.getUser_num()+1> num){
				continue;
			}else{
				key = true;
				cfg = dto;
				break;
			}
		}
		if(key){
			throw new ComponentException(year+"年份的["+cfg.getCust_colony_text()+"]["+cfg.getCust_class_text()+"]的用户开户限额:["+cfg.getUser_num()+"]已满!");
		}
	}
	
	public String queryMinUserId(String custId) throws Exception {
		return cUserDao.queryMinUserId(custId);
	}
	
	public void changeCust(String userId, String toCustId,Integer doneCode ,String busiCode) throws ServicesException{
		this.cUserDao.callChangeCust(userId, toCustId, doneCode, busiCode,this.getOptr());
	}
	
	public void setCUserAtvDao(CUserAtvDao userAtvDao) {
		cUserAtvDao = userAtvDao;
	}

	public void setCUserDtvDao(CUserDtvDao userDtvDao) {
		cUserDtvDao = userDtvDao;
	}

	/**
	 * @param userBroadbandDao the cUserBroadbandDao to set
	 */
	public void setCUserBroadbandDao(CUserBroadbandDao userBroadbandDao) {
		cUserBroadbandDao = userBroadbandDao;
	}

	public void setCUserHisDao(CUserHisDao userHisDao) {
		cUserHisDao = userHisDao;
	}

	public void setSOptrDao(SOptrDao optrDao) {
		sOptrDao = optrDao;
	}

	public void setCRejectResDao(CRejectResDao rejectResDao) {
		cRejectResDao = rejectResDao;
	}

	public void setJUserStopDao(JUserStopDao userStopDao){
		jUserStopDao = userStopDao;
	}

	public void setCPromotionDao(CPromotionDao promotionDao) {
		cPromotionDao = promotionDao;
	}

	public void setCCustDao(CCustDao custDao) {
		cCustDao = custDao;
	}

	public void setPPromFeeDao(PPromFeeDao pPromFeeDao) {
		this.pPromFeeDao = pPromFeeDao;
	}

	public void setPPromFeeUserDao(PPromFeeUserDao pPromFeeUserDao) {
		this.pPromFeeUserDao = pPromFeeUserDao;
	}
	public void setPPromFeeProdDao(PPromFeeProdDao pPromFeeProdDao) {
		this.pPromFeeProdDao = pPromFeeProdDao;
	}
	
	public void setRDeviceDao(RDeviceDao deviceDao) {
		rDeviceDao = deviceDao;
	}
	public void setCPromFeeDao(CPromFeeDao cPromFeeDao) {
		this.cPromFeeDao = cPromFeeDao;
	}

	public void setTCustColonyCfgDao(TCustColonyCfgDao custColonyCfgDao) {
		tCustColonyCfgDao = custColonyCfgDao;
	}

	public void setCPromProdRefundDao(CPromProdRefundDao promProdRefundDao) {
		cPromProdRefundDao = promProdRefundDao;
	}

	/**
	 * 查找用户数量
	 * @param custId
	 * @return
	 */
	public Integer queryUserCount(String custId) throws Exception {
		return cUserDao.queryUserCount(custId);
	}
	
	public TDeviceChangeReason queryChangeReasonByType(String reasonType) throws Exception {
		return tDeviceChangeReasonDao.queryChangeReasonByType(reasonType);
	}
	

	/**
	 * @param optr_id
	 * @return
	 */
	public SOptr queryOptrById(String optr_id) throws Exception{
		return sOptrDao.findByKey(optr_id);
	}
	
	public List<PSpkgOpenuser> querySpkgUser(String spkgSn) throws Exception {
		return pSpkgOpenuserDao.querySpkgUser(spkgSn);
	}
	
	public List<PSpkgOpenbusifee> querySpkgOpenFee(String spkgSn) throws Exception {
		return pSpkgOpenbusifeeDao.querySpkgOpenFee(spkgSn);
	}
	
	public void updateOpenUserDoneCode(String spkgSn, Integer doneCode) throws Exception{
		pSpkgOpenuserDao.updateOpenUserDoneCode(spkgSn, doneCode);
	}
	
	/**
	 * ".用户开户和批量用户开户修改
		开宽带用户时，自动生成的宽带账号。密码默认 c_cust.password
		       生成规则 第一个宽带  cust_no+01+@+域名
		                     第二个宽带  cust_no+02+@+域名
		域名提取方法跟  cust_no的序列号一致。
		OTT、OTT_MOBILE用户 账号生成规则  : 第一个OTT cust_no+61
		                                     第二个OTT cust_no+62"

	 */
	public int queryMaxNumByLoginName(String custId, String custNo, String userType) throws Exception {
		List<String> list = cUserDao.queryLoginNameByUserType(custId, userType);
		List<Integer> loginNameList = new ArrayList<Integer>();
		boolean flag = userType.equals(SystemConstants.USER_TYPE_BAND);
		for(String loginName : list){
			if(flag && loginName.indexOf("@") > 0){
				loginName = loginName.substring(0, loginName.indexOf("@"));
			}
			if(loginName.length() > custNo.length()){
				loginName = loginName.substring(custNo.length());
				String regex=".*[a-zA-Z]+.*";
				Matcher m=Pattern.compile(regex).matcher(loginName);
				if(!m.matches())
					loginNameList.add( Integer.parseInt(loginName) );
			}
		}
		if(loginNameList.size() == 0) return 0;
		return Collections.max(loginNameList);
		
	}
	
	public boolean validAccount(String name) throws Exception{
		return cUserDao.validAccount(name);
	}
	

	public void setExpressionUtil(ExpressionUtil expressionUtil) {
		this.expressionUtil = expressionUtil;
	}

	/**
	 * 变更用户名,需要 设备号 和新的用户名.
	 * @param user
	 * @param custId 
	 * @return 
	 * @throws Exception 
	 */
	public int updateUserNameByDeviceCode(CUser user, String custId) throws Exception {
		return cUserDao.updateUserNameByDeviceCode(user,custId);
	}

	public List<CUser> queryAllUserAndHisByUserIds(String[] userIds) throws Exception {
		List<CUser> list = new ArrayList<CUser>();
		List<CUser> userList = cUserDao.queryUserByUserIds(userIds);
		if(userList.size()>0){
			list.addAll(userList);
		}
		if(list.size()!=userIds.length){
			List<String> userHis = new ArrayList<String>();
			List<String> userIdList = CollectionHelper.converValueToList(list, "user_id");
			for(int i = 0 ;i<userIds.length;i++){
				if(!userIdList.contains(userIds[i])){
					userHis.add(userIds[i]);
				}
			}
			if(userHis.size()>0){
				List<CUser> userHisList = cUserHisDao.queryAllUserHisByUserIds(userIds);
				if(userHisList.size()>0){
					list.addAll(userHisList);
				}
			}
		}
		return list;
	}
	
	public List<CUser> queryUserByTaskId(String taskId) throws Exception {
		return cUserDao.queryTaskUser(taskId);
	}
	
	public int countLikeLoginName(String loginNamePrefix) throws Exception {
		return cUserDao.countLikeLoginName(loginNamePrefix);
	}
	
}
