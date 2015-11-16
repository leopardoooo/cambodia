/**
 *
 */
package com.ycsoft.business.commons.abstracts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TConfigTemplate;
import com.ycsoft.beans.config.TPayType;
import com.ycsoft.beans.config.TTabDefine;
import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.bill.BBill;
import com.ycsoft.beans.core.job.JProdNextTariff;
import com.ycsoft.beans.core.job.JRecordChange;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.prod.CProdStatusChange;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.system.SCounty;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dao.config.TPayTypeDao;
import com.ycsoft.business.dao.config.TTabDefineDao;
import com.ycsoft.business.dao.config.TTemplateCountyDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemDao;
import com.ycsoft.business.dao.core.bill.BBillDao;
import com.ycsoft.business.dao.core.common.CDoneCodeDetailDao;
import com.ycsoft.business.dao.core.job.JProdNextTariffDao;
import com.ycsoft.business.dao.core.job.JRecordChangeDao;
import com.ycsoft.business.dao.core.prod.CProdDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.prod.CProdPropChangeDao;
import com.ycsoft.business.dao.core.prod.CProdStatusChangeDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.core.user.CUserPropChangeDao;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.business.dao.prod.PProdTariffDao;
import com.ycsoft.business.dao.system.SAgentDao;
import com.ycsoft.business.dao.system.SCountyDao;
import com.ycsoft.business.dto.config.TemplateConfigDto;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.BeanHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.pojo.UserTypeDto;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.store.TemplateConfig;
import com.ycsoft.commons.store.TemplateConfig.Template;
import com.ycsoft.daos.core.JDBCException;

/**
 * 扩展commons包的<code>BaseComponent</code>
 *
 * @author hh
 * @date Dec 30, 2009 10:10:56 AM
 */
@Component
public class BaseBusiComponent extends BaseComponent{

	private TTemplateCountyDao tTemplateCountyDao;
	private TTabDefineDao tTabDefineDao;
	private TPayTypeDao tPayTypeDao;
	private SCountyDao sCountyDao;
	protected CDoneCodeDetailDao cDoneCodeDetailDao;
	private JRecordChangeDao jRecordChangeDao;
	
	protected PProdDao pProdDao;
	protected PProdTariffDao pProdTariffDao;
	protected CProdDao cProdDao;
	protected CProdPropChangeDao cProdPropChangeDao;
	@Autowired
	protected CProdOrderDao cProdOrderDao;
	@Autowired
	protected CProdStatusChangeDao cProdStatusChangeDao;
	protected CAcctAcctitemDao cAcctAcctitemDao;
	protected BBillDao bBillDao;
	protected JProdNextTariffDao jProdNextTariffDao;
	@Autowired
	protected SAgentDao sAgentDao;
	
	@Autowired
	protected CUserDao cUserDao;
	@Autowired
	protected CUserPropChangeDao cUserPropChangeDao;

	public void setCAcctAcctitemDao(CAcctAcctitemDao acctAcctitemDao) {
		cAcctAcctitemDao = acctAcctitemDao;
	}
	
	/**
	 * 获得终端用户名
	 * @param user
	 */
	public String getFillUserName(CUser user){
		String userName = "";
		if(user.getUser_type().equals(SystemConstants.USER_TYPE_DTT)){
			if(StringHelper.isNotEmpty(user.getUser_name())){
				userName = user.getUser_name() +" ("+user.getStb_id() +")";
			}else if(StringHelper.isNotEmpty(user.getStb_id())){
				userName=user.getStb_id();
			}else{
				userName = MemoryDict.getDictName( DictKey.TERMINAL_TYPE, SystemConstants.USER_TERMINAL_TYPE_ZZD );
			}
		}else if(user.getUser_type().equals(SystemConstants.USER_TYPE_OTT)){
			if(StringHelper.isNotEmpty(user.getUser_name())){
				userName = user.getUser_name() +" ("+user.getLogin_name() +")";
			}else if(StringHelper.isNotEmpty(user.getTerminal_type())){
				userName = MemoryDict.getDictName( DictKey.TERMINAL_TYPE, user.getTerminal_type() )+" ("+user.getLogin_name()+")";
			}else{
				userName=user.getLogin_name();
			}
		}else{
			if(StringHelper.isNotEmpty(user.getUser_name())){
				userName = user.getUser_name() +" ("+user.getLogin_name() +")";
			}else{
				if(StringHelper.isEmpty(user.getLogin_name())){
					userName=user.getUser_type();
				}else{
					userName = user.getLogin_name();
				}
			}
		}
		
		/*if(StringHelper.isEmpty(user.getUser_name())){
			if(StringHelper.isEmpty(user.getLogin_name())){
				return user.getUser_type();
			}else{
				return user.getLogin_name();
			}
		}*/
		return userName;
	}
	/**
	 * 创建包多月产品的按月账单
	 * @param acctItem
	 * @param invalidDate
	 * @param all_balance
	 * @param months
	 * @param tariff
	 */
	private void createInvalidMuchMonthBill(Integer doneCode,CProd cprod,Date invalidDate,int months,PProdTariff tariff) throws Exception{
		
		int bill_rent=(int)(tariff.getRent()*1.0/tariff.getBilling_cycle());
		int allbill_fee=months*tariff.getRent()/tariff.getBilling_cycle();
		
		//除不尽差额
		int first_chubujinge=allbill_fee-bill_rent*months;
		int bill_month_index=0;
		while(allbill_fee>0){
			int bill_fee=bill_rent;
			if(bill_month_index==0){
				bill_fee=first_chubujinge+bill_fee;
			}
			if(bill_fee>=allbill_fee){
				bill_fee=allbill_fee;
				allbill_fee=0;
			}else{
				allbill_fee=allbill_fee-bill_fee;
			}
			//按到期日来判断开始账单的账期
			String billingCycle = DateHelper.format(
					DateHelper.getNextMonthByNum(invalidDate,bill_month_index)
					, DateHelper.FORMAT_YM);
			this.saveMuchMonthBill(cprod,doneCode,billingCycle,bill_fee,bill_fee);
			bill_month_index++;
		}
		//账户欠费金额修改
		this.cAcctAcctitemDao.changeOwefee(false,cprod.getAcct_id(), cprod.getProd_id(), months*tariff.getRent()/tariff.getBilling_cycle(),getOptr().getCounty_id());
	}
	
	/**
	 * 包多月产品的额外账单
	 * @param prod
	 * @param doneCode
	 * @param billingCycle
	 * @param billFee
	 * @param oweFee
	 * @param comeFrom
	 * @return
	 * @throws Exception
	 */
	public void saveMuchMonthBill(CProd prod,int doneCode,String billingCycle,int billFee,int oweFee) throws Exception{
		String billSn = bBillDao.findSequence().toString();
		BBill bill = new BBill();
		BeanUtils.copyProperties(prod, bill);
		bill.setAcctitem_id(prod.getProd_id());
		bill.setBill_sn(billSn);
		bill.setBilling_cycle_id(billingCycle);
		bill.setStatus("1");
		bill.setCome_from("6");//包多月缴费的额外账单设置成6
		bill.setBill_done_code(""+doneCode);
		bill.setFee_flag("ZC");
		bill.setBill_type("1");
		bill.setBill_date(new Date());
		bill.setEff_date(new Date());
		bill.setRent_month_fee(billFee);
		bill.setUse_fee(0);
		bill.setFinal_month_fee(billFee);
		bill.setFinal_use_fee(0);
		bill.setFinal_bill_fee(billFee);
		bill.setOwe_fee(oweFee);
		bBillDao.save(bill);
		createRecordChange("B_BILL", "ADD", billSn);
	}
	
	/**
	 * 账务改造：到期日模式 lxr
	 * 产品信息
	 * lxr 包多月产品到期日计算时，直接出账
	 * @param cprod
	 * @param tariff
	 * @param oweFee
	 * @param balance
	 * @param realFee
	 * @param changeBalance
	 * @param changeFreeDays
	 * @param billFee
	 * @return
	 * @throws Exception
	 */
	private Date getInvalidDate(Integer doneCode,CProd cprod,CAcctAcctitem acctItem,PProdTariff tariff
			,int changeBalance,int changeFreeDays) throws Exception{
		
		Date invalidDate=new Date(cprod.getInvalid_date().getTime());
		if(changeBalance==0||tariff.getRent() == 0||!tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH)){
			invalidDate=DateHelper.addDate(invalidDate, changeFreeDays);
			return invalidDate;
		}
		PProd prod = pProdDao.findByKey(tariff.getProd_id());
		
		//非模拟节目的包多月产品处理
		if(!SystemConstants.PROD_SERV_ID_ATV.equals(prod.getServ_id())&&tariff.getBilling_cycle()>1){
			
			if(changeBalance>=0){
				if(invalidDate.before(DateHelper.today())){
					invalidDate=DateHelper.today();
				}
				if(cprod.getBillinfo_eff_date()!=null&&cprod.getBillinfo_eff_date().after(invalidDate)){
					invalidDate=new Date(cprod.getBillinfo_eff_date().getTime());
				}
				//充值情况,按实时余额判断增加到期日
				if(changeFreeDays>0){
					invalidDate=DateHelper.addDate(invalidDate, changeFreeDays);
				}
				//实时余额
				int all_balance=acctItem.getActive_balance()+acctItem.getInactive_balance()-acctItem.getOwe_fee();
				//实时余额可看周期数
				int month_cycles=all_balance/tariff.getRent();
				if(month_cycles>0){					
					//插入账单
					this.createInvalidMuchMonthBill(doneCode, cprod, invalidDate, month_cycles*tariff.getBilling_cycle(), tariff);
					//修改到期日
					invalidDate=DateHelper.getNextMonthByNum(invalidDate, month_cycles*tariff.getBilling_cycle());
				}
			}
			/**
			 * jpdan
			 * 包多月退款不计算到期日，作废账单前已计算正确到期日
			 */
			/*else{
				//退款情况 按变化量计算到期日
				//计算退款月数
				int month_cycles=changeBalance/tariff.getRent();
				invalidDate=DateHelper.getNextMonthByNum(invalidDate, month_cycles*tariff.getBilling_cycle());
				if(invalidDate.before(DateHelper.today())){
					invalidDate=DateHelper.today();
				}
			}*/
		}else if(!SystemConstants.PROD_SERV_ID_ATV.equals(prod.getServ_id())&&tariff.getBilling_cycle()==1&&
				cprod.getStop_by_invalid_date().equals(SystemConstants.BOOLEAN_FALSE)){
			//非模拟，包月按账务计费的产品按实时余额计算到期日
			//模拟产品或者（包月产品，计费模式为到期日，）
			
			if (acctItem.getActive_balance()+acctItem.getInactive_balance() - acctItem.getOwe_fee() - acctItem.getReal_fee()<0){
				invalidDate=DateHelper.today();
				
				if(cprod.getBillinfo_eff_date()!=null&&cprod.getBillinfo_eff_date().after(invalidDate)){
					invalidDate=new Date(cprod.getBillinfo_eff_date().getTime());
				}
				//充值情况,按实时余额判断增加到期日
				if(changeFreeDays>0){
					invalidDate=DateHelper.addDate(invalidDate, changeFreeDays);
				}
				
				//实时余额
				int all_balance=acctItem.getActive_balance()+acctItem.getOrder_balance()+acctItem.getInactive_balance()
									-acctItem.getOwe_fee()-acctItem.getReal_fee();
				
				if(all_balance!=0){
					invalidDate=getInvalidDateByAcctmode(invalidDate, all_balance, tariff.getRent(), tariff.getBilling_cycle());
				}
			} else {
				invalidDate = getInvalidDateByFeePro(cprod.getProd_sn(),0);
			}
			
		}else{
			//按余额变化量计算到期日
			invalidDate=getInvalidDateByAcctmode(invalidDate, changeBalance, tariff.getRent(), tariff.getBilling_cycle());
			
			//退款时，当实时余额<=0时，到期日改成当天
//			if(!SystemConstants.PROD_SERV_ID_ATV.equals(prod.getServ_id())&&changeBalance<0){
//				int all_balance=acctItem.getActive_balance()+acctItem.getOrder_balance()+acctItem.getInactive_balance()
//				-acctItem.getOwe_fee()-acctItem.getReal_fee();
//				if(all_balance<=0){
//					invalidDate=DateHelper.today();
//				}
//			}
			
		}
		return invalidDate;
	}

	/**
	 * @param cprod
	 * @param acctItem
	 * @param tariff
	 * @return
	 * @throws Exception
	 * @throws JDBCException
	 */
	public Date getInvalidDateByFeePro(String prodSn,long payFee) throws Exception, JDBCException {
		Date invalidDate;
		CProd cprod = this.cProdDao.findByKey(prodSn);
		boolean stopped = StatusConstants.OWESTOP.equals(cprod.getStatus());//是否已经停机
		CAcctAcctitem acctItem = this.cAcctAcctitemDao.queryByAcctItemId(cprod.getAcct_id(), cprod.getProd_id());
		PProdTariff tariff = this.pProdTariffDao.findByKey(cprod.getTariff_id());
		//判断产品是否有未生效的资费
		JProdNextTariff nextTariff = null;
		long fee =0;
		try{
			nextTariff = this.jProdNextTariffDao.queryByProdSn(cprod.getProd_sn(), cprod.getNext_tariff_id(), cprod.getCounty_id());
		} catch (Exception e){
			e.printStackTrace();
		}
		
		Integer real_fee = acctItem.getReal_fee();
		Integer rent = tariff.getRent();
		String month_rent_cal_type = tariff.getMonth_rent_cal_type();
		Date billinfo_eff_date = cprod.getBillinfo_eff_date();
		int balance = acctItem.getActive_balance()+acctItem.getInactive_balance();
		Integer owe_fee = acctItem.getOwe_fee();
		PProdTariff nt = null;
		if(nextTariff != null ){
			nt = this.pProdTariffDao.findByKey(nextTariff.getTariff_id());
		}
		//新的计算到期日算法里，会按照未生效的资费计算，如果未生效的资费是0资费，将会陷入死循环,所以这里判断下
		if (nextTariff != null && ( nt != null && nt.getRent() > 0 ) ){
			nt = this.pProdTariffDao.findByKey(nextTariff.getTariff_id());
			fee = this.getFeeByInvalidDate(0, owe_fee,real_fee, 
					rent, month_rent_cal_type,nextTariff.getEff_date(), billinfo_eff_date);
			if (fee>balance + payFee){
				invalidDate = this.getInvalidDateByFee(stopped,payFee, balance, owe_fee,
						real_fee, rent, month_rent_cal_type, billinfo_eff_date);
			} else {
				invalidDate = this.getInvalidDateByFee(stopped,payFee, balance-(int)fee, 0,
						real_fee, nt.getRent(), nt.getMonth_rent_cal_type(), nextTariff.getEff_date());
			}
		} else {
			invalidDate = this.getInvalidDateByFee(stopped,payFee, balance, owe_fee,
				real_fee, rent, month_rent_cal_type, billinfo_eff_date);
		}
		return invalidDate;
	}
	
	public long getFeeByInvalidDatePro(String prodSn,Date invalidDate) throws Exception, JDBCException {
		CProd cprod = this.cProdDao.findByKey(prodSn);
		CAcctAcctitem acctItem = this.cAcctAcctitemDao.queryByAcctItemId(cprod.getAcct_id(), cprod.getProd_id());
		PProdTariff tariff = this.pProdTariffDao.findByKey(cprod.getTariff_id());
		//判断产品是否有未生效的资费
		JProdNextTariff nextTariff = null;
		long fee =0;
		try{
			nextTariff = this.jProdNextTariffDao.queryByProdSn(cprod.getProd_sn(), cprod.getNext_tariff_id(), cprod.getCounty_id());
		} catch (Exception e){
			e.printStackTrace();
		}
		
		if (nextTariff != null){
			if (invalidDate.compareTo(nextTariff.getEff_date())<=0){
				fee = this.getFeeByInvalidDate(acctItem.getActive_balance(), acctItem.getOwe_fee(),acctItem.getReal_fee(),
						tariff.getRent(), tariff.getMonth_rent_cal_type(),invalidDate, cprod.getBillinfo_eff_date());
			} else {
				PProdTariff nt = this.pProdTariffDao.findByKey(nextTariff.getTariff_id());
				fee = this.getFeeByInvalidDate(acctItem.getActive_balance(), acctItem.getOwe_fee(),acctItem.getReal_fee(),
						tariff.getRent(), tariff.getMonth_rent_cal_type(),nextTariff.getEff_date(), cprod.getBillinfo_eff_date()) +
					  this.getFeeByInvalidDate(0,0,0,
						nt.getRent(), nt.getMonth_rent_cal_type(),invalidDate, nextTariff.getEff_date()) ;
			}
			
		} else {
			fee = this.getFeeByInvalidDate(acctItem.getActive_balance(), acctItem.getOwe_fee(),acctItem.getReal_fee(),
					tariff.getRent(), tariff.getMonth_rent_cal_type(),invalidDate, cprod.getBillinfo_eff_date());
		}
		return fee;
	}
	
	
	/**
	 *	账务模式 到期日 lxr
	 *  包月产品计算到期日时，余额使用变化余额
	 *  包多月产品计算到期日时，余额只使用活动余额
	 * @param doneCode
	 * @param prodSn
	 * @param freeDays
	 * @param changeFee
	 * @param acctItem
	 * @return
	 * @throws Exception
	 */
	public Date updateInvalidDate(int doneCode, String prodSn,int freeDays,
			int changeFee, CAcctAcctitem acctItem) throws Exception {
		if(StringHelper.isNotEmpty(prodSn)){
			CProd prod = cProdDao.findByKey(prodSn);
			if (prod != null ){
				//判断产品资费的租金是否为0，如果是0则不计算
				//保存用户到期日异动信息				
				PProdTariff tariff = pProdTariffDao.findByKey(prod.getTariff_id());
				PProdTariff newTariff = pProdTariffDao.findByKey(prod.getNext_tariff_id());
				//当前非零资费、或者有未生效资费，别且这个未生效资费不是
				if (tariff.getRent()>0 || (newTariff != null && newTariff.getRent() > 0) ){
					//计算到期日时，重新获得(变化后的)账目
					Date invalidDate = getInvalidDate(doneCode,prod, cAcctAcctitemDao.queryByAcctItemId(prod.getAcct_id(),prod.getProd_id()),tariff,changeFee,freeDays);
					if(tariff.getRent() == 0 && newTariff != null){//当前令资费，有未生效的非零资费
						invalidDate = getInvalidDateByFeePro(prod.getProd_sn(), changeFee);
					}
					//如果本产品是用户套餐产品就查询所有子产品				
					List<CProd> prodList = cProdDao.queryByPkgSn(prodSn, getOptr().getCounty_id());
					//添加本产品的c_prod到list
					prodList.add(prod);
					//新增到期日
					int day = DateHelper.getDiffDays(prod.getInvalid_date(), invalidDate);
					if(day != 0){
						for(CProd dto:prodList){
							//套餐子产品到期日：原来的到期日加上新增的到期日
							if(StringHelper.isNotEmpty(dto.getPackage_sn())){
								List<CProdPropChange> List = new ArrayList<CProdPropChange>();
								Date newInvalidDate = DateHelper.addDate(dto.getInvalid_date().before(new Date()) ? new Date() : dto.getInvalid_date(), day);
								List.add(new CProdPropChange("invalid_date",
										DateHelper.dateToStr(dto.getInvalid_date()),DateHelper.dateToStr(newInvalidDate)));
								editProd(doneCode, dto.getProd_sn(), List);
							}else{
								List<CProdPropChange> List = new ArrayList<CProdPropChange>();
								List.add(new CProdPropChange("invalid_date",
										DateHelper.dateToStr(dto.getInvalid_date()),DateHelper.dateToStr(invalidDate)));
								editProd(doneCode, dto.getProd_sn(), List);
							}
						}
					}
					return invalidDate;
				}
			}
		}
		return null;
	}
	/**
	 * 资费变更时或套餐子产品移除时的到期日计算
	 * @param doneCode
	 * @param prodSn
	 * @param acctItem
	 * @return
	 * @throws Exception
	 */
	public Date updateInvalidDateByTariff(int doneCode, String prodSn, CAcctAcctitem acctItem) throws Exception {
		if(StringHelper.isNotEmpty(prodSn)){
			CProd prod = cProdDao.findByKey(prodSn);
			if (prod != null ){
				//判断产品资费的租金是否为0，如果是0则不计算
				//保存用户到期日异动信息				
				PProdTariff tariff = pProdTariffDao.findByKey(prod.getTariff_id());
				if (tariff.getRent()>0){
					int balance = acctItem.getActive_balance() + acctItem.getInactive_balance();
					
					Date invalidDate = getInvalidDateByTarrif(prod,tariff,acctItem.getOwe_fee(),balance,acctItem.getReal_fee());					
					//如果本产品是用户套餐产品就查询所有子产品				
					List<CProd> prodList = cProdDao.queryByPkgSn(prodSn ,getOptr().getCounty_id());
					//添加本产品的c_prod到list
					prodList.add(prod);
					
					//新增到期日
					int day = DateHelper.getDiffDays(prod.getInvalid_date(), invalidDate);
					
					for(CProd dto:prodList){
						//套餐子产品到期日：原来的到期日加上新增的到期日
						if(StringHelper.isNotEmpty(dto.getPackage_sn())){
							List<CProdPropChange> List = new ArrayList<CProdPropChange>();
							Date newInvalidDate = DateHelper.addDate(dto.getInvalid_date().before(new Date()) ? new Date() : dto.getInvalid_date(), day);
							List.add(new CProdPropChange("invalid_date",
									DateHelper.dateToStr(dto.getInvalid_date()),DateHelper.dateToStr(newInvalidDate)));		
							editProd(doneCode, dto.getProd_sn(), List);
						}else{
							List<CProdPropChange> List = new ArrayList<CProdPropChange>();
							List.add(new CProdPropChange("invalid_date",
									DateHelper.dateToStr(dto.getInvalid_date()),DateHelper.dateToStr(invalidDate)));
							editProd(doneCode, dto.getProd_sn(), List);
						}
					}
					return invalidDate;
				}
			}
		}
		return null;
	}
	
	/**
	 * 账务模式 到期日计算
	 * 现用于资费变更生效时计算到期日，扣除所有欠费后的剩余金额>0时，重新计算到期日
	 */
	private Date getInvalidDateByTarrif(CProd cprod,PProdTariff tariff,int oweFee,int balance,int realFee) throws Exception{
		
		Date invalidDate=DateHelper.today();
		if (tariff.getRent() == 0)
			return invalidDate;
		int changfee=balance-oweFee-realFee;
		if(changfee>0){
			invalidDate = this.getInvalidDateByAcctmode(invalidDate, changfee, tariff.getRent(), tariff.getBilling_cycle());
		}
		return invalidDate;
	}
	
	/**
	 * 修改用户信息
	 * 
	 * @param doneCode
	 * @param userId
	 * @param propChangeList
	 * @throws Exception
	 */
	public void editUser(Integer doneCode,String userId,List<CUserPropChange> propChangeList) throws Exception{
		if(propChangeList == null || propChangeList.size() == 0) return ;
		CUser user = new CUser();
		user.setUser_id(userId);
		for (CUserPropChange change:propChangeList){
			try{
				String newValue = StringHelper.isEmpty(change.getNew_value()) ? ""
						: change.getNew_value();
				BeanHelper.setPropertyString(user, change.getColumn_name(), newValue);
				if (change.getColumn_name().equalsIgnoreCase("status")){
					user.setStatus_date(new Date());
				}
				
				if (change.getColumn_name().equalsIgnoreCase("user_class")){
					//获取操作员的原始信息
					SOptr optr1 = sOptrDao.findByKey(getOptr().getOptr_id());
					if (!getOptr().getCounty_id().equals(optr1.getCounty_id())){
						user.setUser_class_area(optr1.getCounty_id());
					}
				}
				if (change.getColumn_name().equalsIgnoreCase("stop_type")){
					//更新产品的催停标记
					cProdDao.updateStopType(userId,newValue);
				}
			} catch(Exception e){

			}
			setBaseInfo(change);
			change.setUser_id(userId);
			change.setDone_code(doneCode);
			change.setChange_time(DateHelper.now());
		}
		//保存信息修改
		cUserDao.update(user);
		//保存异动信息
		cUserPropChangeDao.save(propChangeList.toArray(new CUserPropChange[propChangeList.size()]));

	}
	
	/**
	 * 修改产品信息
	 * @param doneCode
	 * @param prodSn
	 * @param propChangeList
	 * @throws Exception
	 */
	public void editProd(Integer doneCode,String orderSn,List<CProdPropChange> propChangeList) throws Exception{
		if(propChangeList == null || propChangeList.size() == 0) return ;
		CProdOrder order = new CProdOrder();
		order.setOrder_sn(orderSn);
		
		CProdStatusChange statusChange =null;
		for (CProdPropChange change:propChangeList){
			BeanHelper.setPropertyString(order, change.getColumn_name(), change
					.getNew_value());

			change.setProd_sn(orderSn);
			change.setDone_code(doneCode);
			change.setArea_id(getOptr().getArea_id());
			change.setCounty_id(getOptr().getCounty_id());
			
			if (change.getColumn_name().equals("status")){
				statusChange = new CProdStatusChange();
				statusChange.setOrder_sn(orderSn);
				statusChange.setDone_code(doneCode);
				statusChange.setStatus_date(new Date());
				statusChange.setStatus(change.getNew_value());
			}
		}
		//修改产品信息
		cProdOrderDao.update(order);
		
		
		//保存产品异动信息
		cProdPropChangeDao.save(propChangeList.toArray(new CProdPropChange[propChangeList.size()]));
		//单独保存产品状态异动
		if (statusChange != null){
			cProdStatusChangeDao.save(statusChange);
		}
	}
	
	/**
	 * 设置系统当前的操作员
	 */
	final public SOptr getOptr() throws ServicesException {
		return BaseService.getOptr();
	}
	
	/**
	 * 设置用户信息。如果用户数大于一个，则只设置用户类型信息
	 * @param user 用户信息列表
	 * @param target <code>BaseUserType</code>的子类
	 */
	protected void setUserType(List<CUser> userList, UserTypeDto target ){
		if(userList == null || userList.size() ==0 ) return ;
		CUser user = userList.get(0);
		String userType =user.getUser_type();
		target.setUser_type( userType );
		target.setNet_type(user.getNet_type());
//		if (userList.size() ==1){
//			if (userType.equals(SystemConstants.USER_TYPE_DTV)){
//				CUserDtv dtv = (CUserDtv)user;
//				target.setServ_type(dtv.getServ_type());
//				target.setTerminal_type(dtv.getTerminal_type());
//			} else if (userType.equals(SystemConstants.USER_TYPE_ATV)){
//				CUserAtv atv = (CUserAtv)user;
//				target.setServ_type(atv.getServ_type());
//				target.setTerminal_type(atv.getTerminal_type());
//			}
//		}
	}
	

	/**
	 * 插入变更记录，触发后台程序
	 * @param tableName
	 * @param changeType
	 * @param recId
	 * @throws Exception
	 */
	protected void createRecordChange(String tableName,String changeType,String recId) throws Exception{
		JRecordChange change = new JRecordChange();
		change.setTable_name(tableName);
		change.setRec_id(recId);
		change.setChange_type(changeType);
		change.setArea_id(getOptr().getArea_id());
		change.setCounty_id(getOptr().getCounty_id());
		this.jRecordChangeDao.save(change);
	}
	
	/**
	 * 设置公共的信息信息
	 * @param obj
	 */
	protected void setBaseInfo(Serializable obj) throws ServicesException {
		if (obj == null)
			return;
		setBeanValue(obj, "area_id", getOptr().getArea_id());
		setBeanValue(obj, "county_id", getOptr().getCounty_id());
		setBeanValue(obj, "dept_id", getOptr().getDept_id());
		setBeanValue(obj, "optr_id", getOptr().getOptr_id());
	}

	private void setBeanValue(Serializable obj,String name,String value){
		try {
			BeanHelper.setPropertyString(obj, name, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  获取模板编号
	 * @param template_type 模板类型
	 * @return
	 * @throws JDBCException
	 */
	protected String queryTemplateId(String templateType) throws Exception {
		String result = tTemplateCountyDao.getTemplateIdByCounty(getOptr()
				.getCounty_id(), templateType);
		return result;
	}
	
	/**
	 * 查找可以选择的付款方式
	 */
	public List<TPayType> queryPayType() throws Exception{
		String dataRight = this.queryDataRightCon(getOptr(), DataRight.PAY_TYPE.toString());
		return this.tPayTypeDao.queryPayType(dataRight);
	}
	
	public List<SItemvalue> queryItemValues(String dataType,String itemKey) throws Exception{
		String dataRight = this.queryDataRightCon(getOptr(), dataType);
		return this.sItemvalueDao.queryItemValues(itemKey,dataRight);
	}
	
	/**
	 * 根据付款方式获取资金类型
	 * @param payType
	 * @return
	 * @throws Exception
	 */
	public String getFeeType(String payType) throws Exception{
		return this.tPayTypeDao.findByKey(payType).getAcct_feetype();
	}
	
	/**
	 * 
	 */
	public List<SCounty> querySwitchCounty() throws Exception{
		String dataRight = this.queryDataRightCon(getOptr(), DataRight.CHANGE_COUNTY.toString());
		return this.sCountyDao.querySwitchCounty(dataRight);
	}
	/**
	 * 根据余额计算到期日
	 * @param stopped 是否已经停机.
	 * @param fee 缴费金额
	 * @param balance 余额
	 * @param realFee 实时费用
	 * @param rent 日租
	 * @param rentType 日租的计算方法
	 * @param beginFeeDate 开始计费日期
	 * @return
	 */
	public Date getInvalidDateByFee(boolean stopped, long fee,int balance,int oweFee,int realFee,int rent,String rentType,Date beginFeeDate) throws Exception{
		fee = fee+balance-oweFee;
		Date invalidDate = null;
		float dayRent = rent /30f;//计算日租
		if (fee<0)
			return null;
		
		if (beginFeeDate.compareTo(new Date())<0)
			beginFeeDate = new Date();
		if (fee<dayRent)
			return beginFeeDate;
		Date nextMonth = DateHelper.getNextMonth(beginFeeDate);
		long tempFee=0;
		while(true){
			tempFee = this.getFeeByInvalidDate(0, 0, realFee, rent, rentType, nextMonth, beginFeeDate);
			if (tempFee>=fee)
				break;
			nextMonth = DateHelper.getNextMonth(nextMonth);
		}
		Date yesterday = nextMonth;//DateHelper.addNumDate(nextMonth, -1, DateHelper.DAY);
		if (fee != tempFee){
			while(true){
				tempFee = this.getFeeByInvalidDate(0, 0, realFee, rent, rentType, yesterday, beginFeeDate);
				if(tempFee<=fee)
					break;
				yesterday = DateHelper.addNumDate(yesterday, -1, DateHelper.DAY);	
			}
		}
		/*
		if(stopped){
			tempFee += realFee;//1 .加上实时费用.
		}*/
		if (tempFee == fee){
			invalidDate = yesterday;
		} else {
			if (rentType.equals("AT")){
				invalidDate=  DateHelper.addNumDate(yesterday, 1, DateHelper.DAY);
			}
			else if (rentType.equals("BY")){
				invalidDate = DateHelper.addNumDate(yesterday, Math.round((fee-tempFee)/dayRent), DateHelper.DAY);
			}
		}
		if(invalidDate.before(new Date())){
			invalidDate = new Date();
		}
		return invalidDate;
	}

	/**
	 * 根据到期日计算缴费金额
	 * @param balance //余额，含未返还的冻结金额
	 * @param realFee //本月实时费用
	 * @param rent    //月租费
	 * @param billingType //计费方式
	 * @param invaidDate  //到期日
	 * @return
	 * 
	 * 收费金额= (本月费用 + 到期日 和当前日期月数 * 月租费 + 到期日当月的费用) - 余额 + 欠费
	 */
	public long getFeeByInvalidDate(int balance,int oweFee,int realFee,int rent,String rentType,Date invaidDate,Date beginFeeDate) throws Exception{
		long fee=0;
		if (beginFeeDate.compareTo(new Date())<0)
			beginFeeDate = new Date();
		int months = DateHelper.getDiffMonth(beginFeeDate, invaidDate);//到期日 和 下个月一日 之间的月数
		
		if (months==0){
			//到期日为当前月的一个日期
			fee = getMonthFee(realFee,invaidDate,beginFeeDate,rentType,rent) - balance + oweFee;
		} else {
			fee = getMonthFee(realFee,DateHelper.parseDate(DateHelper.getLastDateInCurrentMonth(beginFeeDate),"yyyy-MM-dd")
					,beginFeeDate,rentType,rent) + 	(months-1) * rent +
					getMonthFee(realFee,invaidDate,beginFeeDate,rentType,rent)  - balance + oweFee;
		}
		return fee;
	}
	/**
	 * 
	 * @param rentFee 实时费用
	 * @param date 当月缴费的截止日期
	 * @param BeginFeeDate 开始计费日期
	 * @param billingType 计费方式
	 * @param rent 月租费
	 * @return
	 * 
	 * 日租 = 月租/30 四舍五入
	 */
	public long getMonthFee(long realFee,Date date,Date BeginFeeDate,String rentType,int rent) throws Exception{
		long monthFee=0;
		float dayRent = rent /30f;//计算日租
		long useDays = Math.round(realFee /dayRent);//本月收看天数
		
		Date firstDay= DateHelper.parseDate(DateHelper.getFirstDateInCurrentMonth(date),"yyyy-MM-dd");//传入日期所在月的第一天
		Date lastDay = DateHelper.parseDate(DateHelper.getLastDateInCurrentMonth(BeginFeeDate),"yyyy-MM-dd");//当前月的最后一天
		long monthDays = DateHelper.getDiffDays(firstDay, DateHelper.getNextMonth(date));//传入月的当月总天数
		
		long totalDays;
		if (date.compareTo(lastDay)<=0){
			//是计算当前月的可以使用天数
			if (BeginFeeDate.compareTo(date) ==0) 
				totalDays=useDays;
			else
				totalDays = useDays + DateHelper.getDiffDays(BeginFeeDate, date)+1;
		}			
		else //是计算结束月的可以使用天数
			totalDays = DateHelper.getDiffDays(firstDay, date) ;
		if (totalDays>=30 || totalDays== monthDays){
			monthFee = rent;
		} else if (totalDays>0) {
			if (rentType.equals("AT")){//按天计费
				monthFee = Math.round(dayRent*totalDays);
			}  else if (rentType.equals("BY")){
				totalDays++;
				if (totalDays<=16){
					monthFee = rent/2;
				} else {
					monthFee = rent;
				}
			}	
		}
		//System.out.println(monthFee);
		return monthFee;
	}
	
	/**
	 * 根据表名查询所有的columns.
	 * @param tableNameLike
	 * @return
	 * @throws Exception
	 */
	public Map<String, TTabDefine> queryTableDefine(String tableNameLike) throws Exception{
		Map<String, TTabDefine> map = new HashMap<String, TTabDefine>();
		List<TTabDefine> list = tTabDefineDao.findTableAllColumns(tableNameLike);
		for(TTabDefine tdf :list){
			map.put(tdf.getColumn_name(), tdf);
		}
		return map;
	}
	
	/**
	 * 
	 * 获取配置模板值
	 * @param key 键值
	 * @return
	 */
	public String queryTemplateConfig(String key) throws Exception {
		TemplateConfigDto t = (TemplateConfigDto) TemplateConfig.loadConfig(
				Template.CONFIG, getOptr().getCounty_id());
		TConfigTemplate template = t.get(key);
		if (template == null)
			return null;
		return template.getConfig_value();
	}
	
	/**
	 * 查询报停是否计费
	 * @return
	 */
	protected String isStopFee(){
		String isRstopFee = SystemConstants.BOOLEAN_FALSE;
		try {
			isRstopFee = this.queryTemplateConfig(TemplateConfigDto.Config.IS_RSTOP_FEE.toString());
			isRstopFee = isRstopFee==null ?SystemConstants.BOOLEAN_FALSE:isRstopFee;
		} catch (Exception e){
			
		}
		return isRstopFee;
	}
	/**
	 * 
	 * @param templateCountyDao
	 */
	public List<SCounty> queryAllCounty() throws JDBCException{
		return sCountyDao.queryAllCounty();
	}

	public void setTTemplateCountyDao(TTemplateCountyDao templateCountyDao) {
		tTemplateCountyDao = templateCountyDao;
	}

	public void setTPayTypeDao(TPayTypeDao payTypeDao) {
		tPayTypeDao = payTypeDao;
	}

	public void setSCountyDao(SCountyDao countyDao) {
		sCountyDao = countyDao;
	}

	/**
	 * @param doneCodeDetailDao the cDoneCodeDetailDao to set
	 */
	public void setCDoneCodeDetailDao(CDoneCodeDetailDao doneCodeDetailDao) {
		cDoneCodeDetailDao = doneCodeDetailDao;
	}

	/**
	 * @param recordChangeDao the jRecordChangeDao to set
	 */
	public void setJRecordChangeDao(JRecordChangeDao recordChangeDao) {
		jRecordChangeDao = recordChangeDao;
	}

	public void setPProdDao(PProdDao prodDao) {
		pProdDao = prodDao;
	}

	public void setPProdTariffDao(PProdTariffDao prodTariffDao) {
		pProdTariffDao = prodTariffDao;
	}
	
	public void setCProdDao(CProdDao prodDao) {
		cProdDao = prodDao;
	}

	public void setCProdPropChangeDao(CProdPropChangeDao prodPropChangeDao) {
		cProdPropChangeDao = prodPropChangeDao;
	}
	public BBillDao getBBillDao() {
		return bBillDao;
	}
	public void setBBillDao(BBillDao billDao) {
		bBillDao = billDao;
	}
	
	
	
	public void setJProdNextTariffDao(JProdNextTariffDao jProdNextTariffDao) {
		this.jProdNextTariffDao = jProdNextTariffDao;
	}
	
	public void setTTabDefineDao(TTabDefineDao tabDefineDao) {
		tTabDefineDao = tabDefineDao;
	}
	
}