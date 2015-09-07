package com.ycsoft.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustLinkman;
import com.ycsoft.beans.core.cust.CCustPropChange;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.ott.OttAccount;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.boss.remoting.ott.OttClient;
import com.ycsoft.boss.remoting.ott.Result;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.component.core.OrderComponent;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.cust.CCustLinkmanDao;
import com.ycsoft.business.dao.core.cust.CCustPropChangeDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.DateHelper;
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
	@Autowired
	private OttClient ottClient;
	@Autowired
	private CProdOrderDao cProdOrderDao;
	@Autowired
	private OrderComponent orderComponent;
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
		return resultMap;
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
	public List<Map<String, String>> getOrderedProductList(String loginName) throws Exception{
		CUser user=userComponent.queryUserByLoginName(loginName);
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
		List<Map<String,String>> resutls=new ArrayList<>();
		for(CProdOrderDto order: unExpOrderMap.values()){
			Map<String,String> re=new HashMap<>();
			re.put("id", order.getProd_id());
			re.put("name", order.getProd_name());
			re.put("continue_buy", "1");
			re.put("update", "0");
			re.put("begin_time", DateHelper.format(order.getEff_date(),DateHelper.FORMAT_TIME));
			re.put("end_time", DateHelper.format(order.getExp_date(),DateHelper.FORMAT_TIME));
			resutls.add(re);
		}
		return resutls;
	}
	
	
	/**
	 * 根据用户ID获取用户的可订购产品列表；产品ID列表不为空时，
	 * 返回包含该产品列表的组合产品列表
	 * 
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_ip 来源IP
	 * @param product_ids 产品ID列表, 可以为空,多产品以逗号分隔
	 */
	public String getProductList(){
		return "";
	}
	
	/**
	 * 用户购买产品，购买指定产品包下的指定资费。
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param product_id 产品ID	
	 * @param product_fee_id 产品资费ID	
	 * @param amount 订购数量（仅对周期性产品有效）
	 * @param film_name 影片名称,可以为空，针对单片有效
	 * @param boss_data BOSS扩展参数,从可订购产品列表中获取，订购产品透传BOSS
	 * @param ott_data OTT扩展参数,同步产品授权时回传CMS
	 */
	public void buyProduct(){
		// TODO buy_product
		
	}
	
	/**
	 * 根据用户ID获取用户购买产品记录
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_ip 来源IP
	 */
	public String getBuyProductHistory(){
		// TODO get_buy_product_history
		
		return "";
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
	public List getProductListByUpdate(){
		// TODO 没有升级业务
		return new ArrayList();
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
	
}
