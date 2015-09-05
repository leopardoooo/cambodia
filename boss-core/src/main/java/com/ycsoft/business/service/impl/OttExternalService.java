package com.ycsoft.business.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustLinkman;
import com.ycsoft.beans.core.cust.CCustPropChange;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.ott.OttAccount;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.cust.CCustLinkmanDao;
import com.ycsoft.business.dao.core.cust.CCustPropChangeDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.NumericHelper;
import com.ycsoft.commons.helper.StringHelper;
/**
 * OTT调用BOSS接口实现
 * @author new
 *
 */
@Service
public class OttExternalService extends BaseBusiService {
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
	/**
	 * 获得用户账户
	 * @param login_name
	 */
	public OttAccount getAccountInfo(String loginName)throws Exception{

			OttAccount ottAcct=new OttAccount();
			CUser user=cUserDao.queryUserByLoginName(loginName);
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
	 * 初始化业务参数
	 * @param busiCode
	 * @throws Exception 
	 */
	private Integer initBusiParam(String busiCode,String  custId) throws Exception{
		
		BusiParameter param=new BusiParameter();
		
		SOptr optr=new SOptr();
		optr.setOptr_id("0");
		optr.setDept_id("4501");
		optr.setLogin_name("admin");
		optr.setArea_id("4500");
		optr.setCounty_id("4501");
		
		CustFullInfoDto custFullInfo = new CustFullInfoDto();
		if(custId!=null){
			custFullInfo.setCust(custComponent.queryCustById(custId));
		}
		param.setOptr(optr);
		param.setBusiCode(busiCode);
		param.setCustFullInfo(custFullInfo);
		param.setService_channel(SystemConstants.SERVICE_CHANNEL_MOBILE);
		param.setDoneCode(doneCodeComponent.gDoneCode());

		this.setParam(param);
		
		return param.getDoneCode();
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
	
			CUser user=cUserDao.queryUserByLoginName(loginName);
			Integer doneCode=this.initBusiParam(BusiCodeConstants.CUST_EDIT, user.getCust_id());
			
			if(user_passwd!=null&&!user_passwd.equals("")){
				CUser editUser=new CUser();
				editUser.setUser_id(user.getUser_id());
				editUser.setPassword(user_passwd);
				cUserDao.update(editUser);
			}
			if("1".equals(user_rank)||"0".equals(user_rank)){
				CCust cust=new CCust();
				cust.setCust_id(user.getCust_id());
				if("1".equals(user_rank)){
					cust.setCust_level(SystemConstants.CUST_LEVEL_VIP);
				}else{
					cust.setCust_level(SystemConstants.CUST_LEVEL_YBKH);
				}
				cCustDao.update(cust);
				
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
				CCustLinkman linkman=new CCustLinkman();
				linkman.setCust_id(user.getCust_id());
				if(StringHelper.isNotEmpty(telephone)){
					linkman.setTel(telephone);
				}
				if(StringHelper.isNotEmpty(user_name)){
					linkman.setLinkman_name(user_name);
				}
				cCustLinkmanDao.update(linkman);
				
			}
			//业务保存
			this.saveAllPublic(doneCode, this.getBusiParam());
					
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
		
		Integer doneCode=this.initBusiParam(BusiCodeConstants.CUST_OPEN, null);
		
		if(userComponent.queryUserByLoginName(loginName)!=null){
			throw new ServicesException(ErrorCode.E40009);
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

		
		CCustLinkman linkMan=new CCustLinkman();
		linkMan.setLinkman_name(user_name);
		linkMan.setTel(telephone);
		linkMan.setEmail(email);
		
		custComponent.createCust(cust, linkMan, SystemConstants.OTT_MOBILE_CUSTCODE);
		this.getBusiParam().getCustFullInfo().setCust(cust);
		
		Map<String,String> result=new HashMap<>();
		result.put("customercode", cust.getCust_no());
		this.saveAllPublic(doneCode, this.getBusiParam());
		return result;
	}
	
	/**
	 * 用户验证接口
	 * @param user 可输入用户ID或昵称(user_id, user_name)
	 * @throws Exception 
	 */
	public Map<String,String> queryUserValidate(String loginName) throws Exception{
		CUser user=userComponent.queryUserByLoginName(loginName);
		Map<String,String> result=new HashMap<>();
		if(user==null){
			result.put("validate_result", "0");
		}else{
			result.put("validate_result", "1");
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
	public String getOrderedProductList(String loginName) throws Exception{
		CUser user=userComponent.queryUserByLoginName(loginName);
		
		Map<String,Date> resDateMap=authComponent.getUserResExpDate(user.getUser_id());
		
		
		return "";
	}
	
}
