package com.ycsoft.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustLinkman;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.business.dto.core.cust.CustOTTMobile;
import com.ycsoft.business.dto.core.prod.OrderProd;
import com.ycsoft.business.dto.core.user.UserLoginPwd;
import com.ycsoft.business.service.IAccountService;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;

@Service
public class AccountService extends OrderService implements IAccountService {
	@Autowired
	private CUserDao cUserDao;
	@Autowired
	private PProdDao pProdDao;
	
	public List<UserLoginPwd> createOttMobile(CustOTTMobile custOtt) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		BusiParameter busiParamter = getBusiParam();
		String custNamePrefix = custOtt.getCust_name_prefix();
		int custNum = custOtt.getCust_number();	//开客户数量
		int userCount = userComponent.countLikeLoginName(custNamePrefix) + 1;	//前缀开头的账号数
		//名称前缀后跟数字以0开头，计算最大数
		int userDigit = String.valueOf(userCount + custNum).length();
		int zeroNum = String.valueOf(custNum).length();
		//需要的最大数和开户数 位数比较
		int sumNum = ( (zeroNum > userDigit) ? zeroNum : userDigit ) + 1;
		
		List<UserLoginPwd> list = new ArrayList<UserLoginPwd>();
		for(int i=0; i < custNum; i++){
			
			UserLoginPwd userLoginPwd = new UserLoginPwd();
			//不足补零
			String loginName = custNamePrefix + StringHelper.leftWithZero( String.valueOf(userCount), sumNum );
			userCount += 1;	//后缀跟数字自增1
			if(userComponent.queryUserByLoginName(loginName) != null){
//				throw new ServicesException("账号已存在!");
				custNum += 1;	//账号存在，后缀+1继续生成，直到不存在为止
				continue;
			}
			String password = String.valueOf( 100000 + new Random(System.currentTimeMillis()).nextInt(900000) );
			userLoginPwd.setLogin_name(loginName);
			userLoginPwd.setPassword( password );
			list.add(userLoginPwd);
			
			CCust cust = new CCust();
			cust.setCust_name( loginName );
			cust.setCust_type(SystemConstants.CUST_TYPE_RESIDENT);
			cust.setStatus(StatusConstants.ACTIVE);
			cust.setAddr_id(custOtt.getAddr_id());
			cust.setAddress(custOtt.getAddress());
			cust.setNote(custOtt.getNote());
			cust.setNet_type("CITY");
			cust.setCust_level(SystemConstants.CUST_LEVEL_YBKH);
			cust.setIs_black(SystemConstants.BOOLEAN_FALSE);
			cust.setPassword( password );
			
			CCustLinkman linkman = new CCustLinkman();
			linkman.setMail_address(custOtt.getAddress());
			
			custComponent.createCust(cust, linkman, SystemConstants.OTT_MOBILE_CUSTCODE);
			
			String custId = cust.getCust_id();
			acctComponent.createAcct(custId, null, SystemConstants.ACCT_TYPE_PUBLIC, null);
			
			String userId = userComponent.gUserId();
			CUser user = new CUser();
			user.setCust_id(custId);
			user.setUser_id(userId);
			user.setUser_type(SystemConstants.USER_TYPE_OTT_MOBILE);
			user.setAcct_id(acctComponent.gAcctId());
			user.setStatus(StatusConstants.ACTIVE);
			user.setStatus_date(new Date());
			user.setOpen_time(new Date());
			user.setTerminal_type(SystemConstants.USER_TERMINAL_TYPE_ZZD);
			user.setIs_rstop_fee(SystemConstants.BOOLEAN_FALSE);
			user.setArea_id(cust.getArea_id());
			user.setCounty_id(cust.getCounty_id());
			user.setLogin_name(loginName);
			user.setPassword(cust.getPassword());
			cUserDao.save(user);
			createUserJob(user, custId, doneCode);
			
			OrderProd orderProd = new OrderProd();
			orderProd.setCust_id(custId);
			orderProd.setUser_id(userId);
			orderProd.setProd_id(custOtt.getProd_id());
			orderProd.setTariff_id(custOtt.getTariff_id());
			orderProd.setEff_date(DateHelper.parseDate(DateHelper.formatNow(), "yyyy-MM-dd"));
			orderProd.setExp_date(custOtt.getInvalid_date());
			orderProd.setPay_fee(0);
			orderProd.setTransfer_fee(0);
			
			Date effDate = orderProd.getEff_date();
			Date expDate = DateHelper.addDate(orderProd.getExp_date(), 1);
			int diffMonths =  DateHelper.getDiffMonth(effDate, expDate);
			float days = DateHelper.getDiffDays(effDate, DateHelper.addTypeDate(expDate, "MONTH", diffMonths*-1));
			float months = (float)diffMonths;
			float num = days/30f + months;
			float orderMonths = (float)(Math.round(num*Math.pow(10,2))/Math.pow(10,2));
			
			orderProd.setOrder_months(orderMonths);
			
			saveOrderProd(orderProd, busiParamter.getBusiCode(), doneCode);
			
			List<String> userIdList = new ArrayList<String>();
			userIdList.add(userId);
			doneCodeComponent.saveDoneCodeDetail(doneCode, custId, userIdList);
		}
		doneCodeComponent.saveDoneCode(doneCode, busiParamter.getBusiCode(), custOtt.getAddr_id());
		return list;
	}
	
	public List<PProd> queryOTTMobileFreeProd() throws Exception {
		return pProdDao.queryFeeOrderUserProd(SystemConstants.USER_TYPE_OTT_MOBILE);
	}

}
