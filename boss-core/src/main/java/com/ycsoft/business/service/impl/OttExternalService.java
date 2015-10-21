package com.ycsoft.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustLinkman;
import com.ycsoft.beans.core.cust.CCustPropChange;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.ott.OttAccount;
import com.ycsoft.beans.ott.OttProdTariff;
import com.ycsoft.beans.ott.OttUserOrder;
import com.ycsoft.beans.ott.OttUserProd;
import com.ycsoft.beans.ott.TServerOttauthDto;
import com.ycsoft.beans.ott.TServerOttauthFee;
import com.ycsoft.beans.ott.TServerOttauthProd;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.beans.prod.PPromotionEasyProd;
import com.ycsoft.boss.remoting.ott.OttClient;
import com.ycsoft.boss.remoting.ott.Result;
import com.ycsoft.business.component.core.OrderComponent;
import com.ycsoft.business.dao.config.TServerOttconfigProdDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.cust.CCustLinkmanDao;
import com.ycsoft.business.dao.core.cust.CCustPropChangeDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.prod.PProdTariffDao;
import com.ycsoft.business.dao.prod.PProdTariffDisctDao;
import com.ycsoft.business.dao.prod.PPromotionEasyProdDao;
import com.ycsoft.business.dto.core.prod.OrderProd;
import com.ycsoft.business.dto.core.prod.OrderProdPanel;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.NumericHelper;
import com.ycsoft.commons.helper.StringHelper;
/**
 * OTT调用BOSS接口实现
 * @author new
 *
 */
@Service
public class OttExternalService extends OrderService {
	@Autowired
	private CUserDao cUserDao;
	@Autowired
	private CAcctAcctitemDao cAcctAcctitemDao;
	@Autowired
	private CCustLinkmanDao cCustLinkmanDao;
	@Autowired
	private CCustDao cCustDao;
	@Autowired
	private CCustPropChangeDao cCustPropChangeDao;
	@Autowired
	private OttClient ottClient;
	@Autowired
	private CProdOrderDao cProdOrderDao;
	@Autowired
	private OrderComponent orderComponent;
	@Autowired
	private PProdTariffDao pProdTariffDao;
	@Autowired
	private PProdTariffDisctDao pProdTariffDisctDao;
	@Autowired
	private PPromotionEasyProdDao pPromotionEasyProdDao;
	@Autowired
	private TServerOttconfigProdDao tServerOttconfigProdDao;
	/**
	 * 获得用户账户
	 * @param login_name
	 */
	public OttAccount getAccountInfo(String loginName)throws Exception{
		LoggerHelper.debug(this.getClass(), "loginName="+loginName);
			OttAccount ottAcct=new OttAccount();
			CUser user=cUserDao.queryUserByLoginName(loginName);
			if(user==null||!user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE)){
				throw new ServicesException(ErrorCode.UserLoginNameIsNotExistsOrIsNotOttMobile);
			}
			CCust cust=custComponent.queryCustById(user.getCust_id());
			CCustLinkman linkman= custComponent.queryCustLinkmanById(cust.getCust_id());
			CAcctAcctitem acctItem=cAcctAcctitemDao.queryPublicAcctItem(cust.getCust_id());
			
			ottAcct.setCustomer_code(cust.getCust_no());
			ottAcct.setUser_id(user.getLogin_name());
			ottAcct.setUser_name(linkman.getLinkman_name());
			ottAcct.setMoney(NumericHelper.changeF2Y(acctItem.getActive_balance().toString()));
			if(SystemConstants.CUST_LEVEL_VIP.equals(cust.getCust_level())){
				ottAcct.setUser_rank("1");
			}
			if(StringHelper.isNotEmpty(linkman.getTel())){
				ottAcct.setTelephone(linkman.getTel());
			}else{
				ottAcct.setTelephone(linkman.getMobile());
			}
			return ottAcct;

	}
	
	/**
	 * 修改用户帐户信息接口
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_passwd 用户密码
	 * @param user_name 用户名称
	 * @param user_rank 用户等级
	 * @param telephone 手机号码
	 */
	public void  modifyAccountInfo(String loginName,String user_passwd,String user_rank,String user_name,String telephone) throws Exception{
		LoggerHelper.debug(this.getClass(), "loginName="+loginName+" user_passwd="+user_passwd+" user_rank="+user_rank+" telephone="+telephone+" user_name="+user_name);
			CUser user=cUserDao.queryUserByLoginName(loginName);
			if(user==null||!user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE)){
				throw new ServicesException(ErrorCode.UserLoginNameIsNotExistsOrIsNotOttMobile);
			}
			Integer doneCode=this.initExternalBusiParam(BusiCodeConstants.CUST_EDIT, user.getCust_id());
			CCust cust=this.getBusiParam().getCust();
			CCustLinkman linkman=cCustLinkmanDao.findByKey(user.getCust_id());
			
			String new_user_passwd=user.getPassword();
			String new_user_name=linkman.getLinkman_name();
			String new_telephone=linkman.getTel();
			
			if(user_passwd!=null&&!user_passwd.equals("")){
				CUser editUser=new CUser();
				editUser.setUser_id(user.getUser_id());
				editUser.setPassword(user_passwd);
				cUserDao.update(editUser);
				//调用接口修改密码
				new_user_passwd= user_passwd;
			}
			if("1".equals(user_rank)||"0".equals(user_rank)){
				CCust upatecust=new CCust();
				upatecust.setCust_id(user.getCust_id());
				if("1".equals(user_rank)){
					upatecust.setCust_level(SystemConstants.CUST_LEVEL_VIP);
				}else{
					upatecust.setCust_level(SystemConstants.CUST_LEVEL_YBKH);
				}
				cCustDao.update(upatecust);
				//changeOttMap.put("password", user_passwd);
				
				
				CCustPropChange change=new CCustPropChange();
				change.setColumn_name("cust_level");
				change.setDone_code(doneCode);
				change.setOld_value(this.getBusiParam().getCust().getCust_level());
				change.setNew_value(cust.getCust_level());
				change.setCreate_time(new Date());
				change.setCust_id(user.getCust_id());
				change.setArea_id(user.getArea_id());
				change.setCounty_id(user.getCounty_id());
				
				cCustPropChangeDao.save(change);
			}
			if(StringHelper.isNotEmpty(user_name)||StringHelper.isNotEmpty(telephone)){
				CCustLinkman updatelinkman=new CCustLinkman();
				updatelinkman.setCust_id(user.getCust_id());
				if(StringHelper.isNotEmpty(telephone)&&!telephone.equals(linkman.getTel())){
					updatelinkman.setTel(telephone);
					new_telephone=telephone;
					
					CCustPropChange change=new CCustPropChange();
					change.setColumn_name("tel");
					change.setDone_code(doneCode);
					change.setOld_value(linkman.getTel());
					change.setNew_value(telephone);
					change.setCreate_time(new Date());
					change.setCust_id(user.getCust_id());
					change.setArea_id(user.getArea_id());
					change.setCounty_id(user.getCounty_id());
					
					cCustPropChangeDao.save(change);
					
				}
				if(StringHelper.isNotEmpty(user_name)&&!user_name.equals(linkman.getLinkman_name())){
					updatelinkman.setLinkman_name(user_name);
					new_user_name=user_name;
					
					CCustPropChange change=new CCustPropChange();
					change.setColumn_name("linkman_name");
					change.setDone_code(doneCode);
					change.setOld_value(linkman.getLinkman_name());
					change.setNew_value(user_name);
					change.setCreate_time(new Date());
					change.setCust_id(user.getCust_id());
					change.setArea_id(user.getArea_id());
					change.setCounty_id(user.getCounty_id());
					
					cCustPropChangeDao.save(change);
				}
				cCustLinkmanDao.update(updatelinkman);
				
			}
			
			//业务保存
			this.saveAllPublic(doneCode, this.getBusiParam());
			//调用接口修改
			Result ottResult=
					ottClient.editUser(user.getLogin_name(),new_user_passwd ,
		    		new_user_passwd, cust.getAddress(), linkman.getEmail(), new_telephone
		    		,user.getStb_id() , user.getModem_mac(), user.getStatus());
		    if(!ottResult.isSuccess()){
		    	if(ottResult.isConnectionError())
		    		throw new ServicesException(ErrorCode.E20003);
		    	else
		    		throw new ServicesException(ErrorCode.E40009);
		    }
	}
	
	/**
	 * 用户注册接口，通过手机注册用户，BOSS后台将进行客户开户和基本包订购，生成用户
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_passwd 用户密码
	 * @param user_name 用户名称
	 * @param user_rank 用户等级
	 * @param telephone 手机号码
	 * @param email 邮箱
	 * @throws Exception 
	 */
	public Map<String,String> RegisterAccount(String loginName,String user_passwd,
			String user_name,String user_rank,String telephone,String email) throws Exception{
		LoggerHelper.debug(this.getClass(), "loginName="+loginName+" user_passwd="+user_passwd+" user_rank="+user_rank+" telephone="+telephone+" user_name="+user_name+" email="+email);
		
		if(StringHelper.isEmpty(loginName)||StringHelper.isEmpty(user_passwd)){
			throw new ServicesException(ErrorCode.E40006);
		}
		Integer doneCode=this.initExternalBusiParam(BusiCodeConstants.CUST_OPEN, null);
		
		if(userComponent.queryUserByLoginName(loginName)!=null){
			throw new ServicesException(ErrorCode.E40009);
		}
		
		if(StringHelper.isEmpty(user_name)){
			user_name=loginName;
		}
		
		CCust cust=new CCust();
		cust.setCust_name(loginName);
		cust.setStatus(StatusConstants.ACTIVE);
		cust.setNet_type("CITY");
		cust.setCust_class(SystemConstants.CUST_CLASS_YBKH);
		cust.setCust_colony(SystemConstants.CUST_CLASS_YBKH);
		if("1".equals(user_rank)){
			cust.setCust_level(SystemConstants.CUST_LEVEL_VIP);
		}else{
			cust.setCust_level(SystemConstants.CUST_LEVEL_YBKH);
		}
		cust.setAddress(SystemConstants.OTT_MOBILE_ADDRESS);
		cust.setAddr_id(SystemConstants.OTT_MOBILE_ADDR_ID);
		cust.setCust_type(SystemConstants.CUST_TYPE_RESIDENT);
		cust.setPassword(user_passwd);
		
		CCustLinkman linkMan=new CCustLinkman();
		linkMan.setLinkman_name(user_name);
		linkMan.setTel(telephone);
		linkMan.setEmail(email);
		
		custComponent.createCust(cust, linkMan, SystemConstants.OTT_MOBILE_CUSTCODE);
		this.getBusiParam().getCustFullInfo().setCust(cust);
		//为客户创建公用账户
		acctComponent.createAcct(cust.getCust_id(),null, SystemConstants.ACCT_TYPE_PUBLIC, null);
		//保存用户
		CUser user=new CUser();
		user.setUser_id(cUserDao.findSequence().toString());
		user.setCust_id(cust.getCust_id());
		user.setUser_type(SystemConstants.USER_TYPE_OTT_MOBILE);
		user.setAcct_id(acctComponent.gAcctId());
		user.setStatus(StatusConstants.ACTIVE);
		user.setStatus_date(new Date());
		user.setOpen_time(new Date());
		user.setTerminal_type(SystemConstants.USER_TERMINAL_TYPE_ZZD);
		user.setIs_rstop_fee("F");
		user.setArea_id(cust.getArea_id());
		user.setCounty_id(cust.getCounty_id());
		user.setLogin_name(loginName);
		user.setPassword(user_passwd);
		
		cUserDao.save(user);
		
		Map<String,String> resultMap=new HashMap<>();
		resultMap.put("customer_code", cust.getCust_no());
		
		
		 //注册免费送3天节目
		for(PPromotionEasyProd easyProd: pPromotionEasyProdDao.queryPromotionByType(SystemConstants.PROMOTION_TYPE_OTT_MOBILE_EXTERNAL)){
			OrderProd orderProd=getOttMobileOrderProd(user,easyProd.getProd_id(),easyProd.getTariff_id(),easyProd.getOrder_cycles(),null,null,null);
			saveOrderProd(orderProd, this.getBusiParam().getBusiCode(), this.getBusiParam().getDoneCode());
		}
		
		this.saveAllPublic(doneCode, this.getBusiParam());
		//调用接口
		Result ottResult=ottClient.editUser(loginName,user_passwd ,
	    		user_name, cust.getAddress(), linkMan.getEmail(), linkMan.getTel()
	    		,user.getStb_id() , user.getModem_mac(), user.getStatus());
	    if(!ottResult.isSuccess()){
	    	if(ottResult.getStatus().equals("40006"))
	    		throw new ServicesException(ErrorCode.E40006);
	    	else if(ottResult.isConnectionError())
	    		throw new ServicesException(ErrorCode.E20003);
	    	else
	    		throw new ServicesException(ErrorCode.E40009);
	    }
	    //产品授权
	    Map<String,Date> userResMap = authComponent.getUserResExpDate(user.getUser_id());
		for(String externalResId: userResMap.keySet()){
			String resDate=DateHelper.format( userResMap.get(externalResId), DateHelper.FORMAT_TIME_END);
			Result resutl=ottClient.openUserProduct(user.getLogin_name(), externalResId,resDate);
			if(!resutl.isSuccess()){
				throw new ServicesException(ErrorCode.E40009);
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 用户验证接口
	 * @param user 可输入用户ID或昵称(user_id, user_name)
	 * @throws Exception 
	 */
	public Map<String,String> queryUserValidate(String loginName) throws Exception{
		LoggerHelper.debug(this.getClass(), "loginName="+loginName);
		CUser user=userComponent.queryUserByLoginName(loginName);
		Map<String,String> result=new HashMap<>();
		if(user==null){
			result.put("validate_result", "1");
		}else{
			result.put("validate_result", "0");
		}
		return result;
	}
	/**
	 * 根据用户ID获取用户的已订购列表
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_ip 来源IP
	 * @throws Exception 
	 */
	public List<OttUserProd> getOrderedProductList(String loginName) throws Exception{
		LoggerHelper.debug(this.getClass(), "loginName="+loginName);
		CUser user=userComponent.queryUserByLoginName(loginName);
		if(user==null||!user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE)){
			throw new ServicesException(ErrorCode.UserLoginNameIsNotExistsOrIsNotOttMobile);
		}
		//map<prod_id,CProdOrderDto>
		Map<String,CProdOrderDto> unExpOrderMap=new HashMap<>();
		for(CProdOrderDto order: cProdOrderDao.queryProdOrderDtoByUserId(user.getUser_id())){
			if(!order.getExp_date().before(DateHelper.today())){
				CProdOrderDto _prodOrder= unExpOrderMap.get(order.getProd_id());
				if(_prodOrder==null){
					unExpOrderMap.put(order.getProd_id(), order);
				}else{
					if(order.getExp_date().before(_prodOrder.getExp_date())){
						order.setExp_date(_prodOrder.getExp_date());
					}
					if(order.getEff_date().after(_prodOrder.getEff_date())){
						order.setEff_date(_prodOrder.getEff_date());
					}
				}
			}
		}
		List<OttUserProd> resutls=new ArrayList<>();
		for(CProdOrderDto order: unExpOrderMap.values()){
			OttUserProd re=new OttUserProd();
			re.setId(order.getProd_id());
			re.setName(order.getProd_name());
			re.setContinue_buy("1");
			re.setBegin_time( DateHelper.format(order.getEff_date(),DateHelper.FORMAT_TIME_START));
			re.setEnd_time( DateHelper.format(order.getExp_date(),DateHelper.FORMAT_TIME_END));
			resutls.add(re);
		}
		return resutls;
	}
	
	/**
	 * 生成订购产品的数据结构OrderProd
	 * @param user
	 * @param product_id
	 * @param product_fee_id
	 * @param amount
	 * @param file_name
	 * @param boss_data
	 * @param ott_date
	 * @return
	 * @throws Exception
	 */
	private OrderProd getOttMobileOrderProd(CUser user,String product_id,String product_fee_id,Integer amount,String file_name,String boss_data,String ott_date) throws Exception{
		OrderProd orderProd=new OrderProd();
		orderProd.setCust_id(user.getCust_id());
		orderProd.setUser_id(user.getUser_id());
		orderProd.setProd_id(product_id);
		orderProd.setTariff_id(product_fee_id);
		orderProd.setTransfer_fee(0);
		orderProd.setOrder_fee_type(SystemConstants.ORDER_FEE_TYPE_ACCT);
		
		List<CProdOrder> orderAlllist=cProdOrderDao.queryNotExpAllOrderByProd(user.getUser_id(),product_id);
		Date eff_date=DateHelper.today();
		String last_order_sn=null;
		if(orderAlllist.size()>0){
			last_order_sn=orderAlllist.get(orderAlllist.size()-1).getOrder_sn();
			CProdOrderDto dto=cProdOrderDao.queryCProdOrderDtoByKey(last_order_sn);
			eff_date=DateHelper.addDate(dto.getExp_date(), 1);	
		}
		String[] tmpTariff=orderProd.getTariff_id().split("_");
		PProdTariff tariff=pProdTariffDao.findByKey(tmpTariff[0]);
		int billing_cycle=0;
		int rent=0;
		if(tmpTariff.length==2){
			PProdTariffDisct disct= pProdTariffDisctDao.findByKey(tmpTariff[1]);
			billing_cycle=disct.getBilling_cycle();
			rent=disct.getDisct_rent();
		}else{
			billing_cycle=tariff.getBilling_cycle();
			rent=tariff.getRent();
		}
		//订购月数 order_months
		float order_months=0f;
		// 实际支付金额（小计金额）pay_fee
		int pay_fee=0;
		//失效日期exp_date
		Date exp_date=null;
		if(tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_DAY)){
			pay_fee=rent*amount/billing_cycle;
			order_months=amount*1.0f/30;
			exp_date=DateHelper.addDate(eff_date, amount-1);
		}else{
			if(billing_cycle==12){
				pay_fee=rent*amount;
				order_months=billing_cycle*amount;
				exp_date=DateHelper.getNextMonthPreviousDay(eff_date, billing_cycle*amount);
			}else{
				pay_fee=rent*amount/billing_cycle;
				order_months=billing_cycle*amount/billing_cycle;
				exp_date=DateHelper.getNextMonthPreviousDay(eff_date, billing_cycle*amount/billing_cycle);
			}
		}
		orderProd.setEff_date(eff_date);
		orderProd.setPay_fee(pay_fee);
		orderProd.setOrder_months(order_months);
		orderProd.setExp_date(exp_date);
		orderProd.setLast_order_sn(last_order_sn);
		
		return orderProd;
	}
	/**
	 * OTT_MOBILE用户购买产品，购买指定产品包下的指定资费。
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param product_id 产品ID	
	 * @param product_fee_id 产品资费ID	
	 * @param amount 订购数量（仅对周期性产品有效）
	 * @param film_name 影片名称,可以为空，针对单片有效
	 * @param boss_data BOSS扩展参数,从可订购产品列表中获取，订购产品透传BOSS
	 * @param ott_data OTT扩展参数,同步产品授权时回传CMS
	 * @throws Exception 
	 */
	public void saveOttMobileBuyProduct(String loginName,String product_id,String product_fee_id,Integer amount,String file_name,String boss_data,String ott_date) throws Exception{
		LoggerHelper.debug(this.getClass(), "loginName="+loginName+" product_id="+product_id+" product_fee_id="+product_fee_id+" amount="+amount);
		if(StringHelper.isEmpty(loginName)||
			StringHelper.isEmpty(product_id)||
			StringHelper.isEmpty(product_fee_id)||
			amount==null){
			throw new ServicesException(ErrorCode.E40006);
		}
		CUser user=userComponent.queryUserByLoginName(loginName);
		
		if(user==null||!user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE)){
			throw new ServicesException(ErrorCode.UserLoginNameIsNotExistsOrIsNotOttMobile);
		}
		String busi_code=BusiCodeConstants.PROD_SINGLE_ORDER;
		this.initExternalBusiParam(busi_code, user.getCust_id());
		doneCodeComponent.lockCust(user.getCust_id());
		
		OrderProd orderProd=getOttMobileOrderProd(user,product_id,product_fee_id,amount,file_name,boss_data,ott_date);
		
		try{
		   saveOrderProd(orderProd, busi_code, this.getBusiParam().getDoneCode());
		}catch(ServicesException e){
			if(ErrorCode.AcctFeeNotEnough.equals(e.getErrorCode())){
				throw new ServicesException(ErrorCode.E20005);
			}else{
				throw e;
			}
		}
		this.saveAllPublic(this.getBusiParam().getDoneCode(), this.getBusiParam());
		//处理实时指令
		Map<String,Date> userResMap = authComponent.getUserResExpDate(user.getUser_id());
		for(String externalResId: userResMap.keySet()){
			String resDate=DateHelper.format( userResMap.get(externalResId), DateHelper.FORMAT_TIME_END);
			Result resutl=ottClient.openUserProduct(user.getLogin_name(), externalResId,resDate);
			if(!resutl.isSuccess()){
				throw new ServicesException(ErrorCode.E40009);
			}
		}
	}
	/**
	 * 根据用户ID获取用户购买产品记录
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_ip 来源IP
	 * @throws Exception 
	 */
	public List<OttUserOrder> getBuyProductHistory(String loginName) throws Exception{
		LoggerHelper.debug(this.getClass(), "loginName="+loginName);
		CUser user=userComponent.queryUserByLoginName(loginName);
		if(user==null||!user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE)){
			throw new ServicesException(ErrorCode.UserLoginNameIsNotExistsOrIsNotOttMobile);
		}
		//map<prod_id,CProdOrderDto>
		List<OttUserOrder> resutls=new ArrayList<>();
		for(CProdOrderDto order: cProdOrderDao.queryProdOrderDtoByUserId(user.getUser_id())){
			OttUserOrder re=new OttUserOrder();
			re.setMoney(NumericHelper.changeF2Y(order.getOrder_fee().toString()));
			re.setProduct_id(order.getProd_id());
			re.setProduct_name(order.getProd_name());
			re.setProduct_fee_id(order.getTariff_id());
			re.setTime(DateHelper.format(order.getOrder_time(), DateHelper.FORMAT_TIME));
			resutls.add(re);
		}
		return resutls;
	}
	
	/**
	 * OTT_MOBILE接口使用
	 * 根据用户ID获取用户的可订购产品列表；产品ID列表不为空时，
	 * 返回包含该产品列表的组合产品列表
	 * 
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_ip 来源IP
	 * @param product_ids 产品ID列表, 可以为空,多产品以逗号分隔
	 * @throws Exception 
	 */
	public List<OttProdTariff> getProductList(String loginName,String product_ids) throws Exception{
		LoggerHelper.debug(this.getClass(), "loginName="+loginName+" product_ids="+product_ids);
		CUser user=userComponent.queryUserByLoginName(loginName);
		if(user==null||!user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE)){
			throw new ServicesException(ErrorCode.UserLoginNameIsNotExistsOrIsNotOttMobile);
		}
		List<OttProdTariff> prodTariffList=new ArrayList<>();
		
		OrderProdPanel prodPanel=orderComponent.queryOrderableProd(BusiCodeConstants.PROD_SINGLE_ORDER,user.getCust_id(),user.getUser_id(),null);
		
		List<PProd> prodList=new ArrayList<>();
		if(StringHelper.isEmpty(product_ids)){
			prodList=prodPanel.getProdList();
		}else{
			Set<String> _set=new HashSet<String>();
			for(String product_id:product_ids.split(",")){
				_set.add(product_id);
			}
			for(PProd prod:prodPanel.getProdList()){
				if(_set.contains(prod.getProd_id())){
					prodList.add(prod);
				}
			}
		}
		
		for(PProd prod:prodList){
			for(PProdTariffDisct _d:prodPanel.getTariffMap().get(prod.getProd_id())){
				OttProdTariff tf=new OttProdTariff();
				prodTariffList.add(tf);
				
				tf.setId(prod.getProd_id());
				tf.setName(prod.getProd_name());
				tf.setType("0");
				tf.setProduct_fee_id(_d.getTariff_id());
				tf.setPrice(NumericHelper.changeF2Y(_d.getDisct_rent().toString()));
				if(_d.getBilling_type().equals(SystemConstants.BILLING_TYPE_DAY)){
					//按天
					tf.setUnit("day");
					tf.setAmount(_d.getBilling_cycle().toString());
				}else{
					if(_d.getBilling_cycle().equals(12)){
						tf.setUnit("year");
						tf.setAmount("1");
					}else{
						tf.setUnit("month");
						tf.setAmount(_d.getBilling_cycle().toString());
					}
				}
			}
		}
		
		return prodTariffList;
	}
	
	/**
	 * 根据用户ID获取用户的可升级产品列表；产品ID列表不为空时，
	 * 返回包含该产品列表的组合产品列表
	 * 
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_ip 来源IP
	 * @param product_ids 产品ID列表, 可以为空,多产品以逗号分隔
	 */
	public List<Object> getProductListByUpdate(){
		//没有升级业务
		return new ArrayList<Object>();
	}
	
	/**
	 * 把指定产品包升级到另外一个产品包，实现产品包的换购
	 * 
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_ip 来源IP
	 * @param product_id 升级之前的产品ID
	 * @param update_product_id 升级之后的产品ID
	 * @param price 升级所需价格
	 * @param currency_type RMB：人民币, USD：美元
	 * @throws Exception 
	 */
	public void updateProduct() throws Exception{
		//没有升级业务
		throw new ServicesException(ErrorCode.E40009);
	}
	/**
	 * 同步产品
	 * @throws Exception
	 */
	public void saveSyncProd()throws Exception{
		
		List<TServerOttauthDto> list=new ArrayList<>();
		
		for(TServerOttauthProd prod:tServerOttconfigProdDao.queryNeedSyncDto()){
			List<TServerOttauthFee> fees=tServerOttconfigProdDao.queryFee(prod.getId());
			if(fees!=null&&fees.size()>0){
				TServerOttauthDto dto=new TServerOttauthDto();
				dto.setId(prod.getId());
				dto.setName(prod.getName());
				dto.setStatus(prod.getStatus());
				dto.setFee(fees);
				if(StringHelper.isNotEmpty(prod.getDomain())){
					Map<String,String> map=new HashMap<>();
					map.put("domain", prod.getDomain());
					dto.setExtension(map);
				}
				list.add(dto);
			}
		}

		for(TServerOttauthDto dto:list){
			String prodFeeInfo =new Gson().toJson(dto);
			LoggerHelper.debug(this.getClass(), prodFeeInfo);
			
			Result resutl=ottClient.addOrUpdateProduct(prodFeeInfo);
			if(!resutl.isSuccess()){
				throw new ServicesException(resutl.getStatus()+resutl.getErr()+resutl.getReason());
			}
			tServerOttconfigProdDao.updateSync(dto.getId());
			
		}
	}
	
}
