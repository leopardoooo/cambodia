/**
 *
 */
package com.ycsoft.business.component.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.bill.TvOrder;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.job.JProdNextTariff;
import com.ycsoft.beans.core.job.JProdNextTariffHis;
import com.ycsoft.beans.core.job.JProdPreopen;
import com.ycsoft.beans.core.job.JProdPreopenHis;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdHis;
import com.ycsoft.beans.core.prod.CProdInvalidTariff;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.prod.CProdPropPat;
import com.ycsoft.beans.core.prod.CProdResourceAcct;
import com.ycsoft.beans.core.prod.CProdRsc;
import com.ycsoft.beans.core.prod.CProdSyn;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.core.bill.TvOrderDao;
import com.ycsoft.business.dao.core.job.JProdNextTariffDao;
import com.ycsoft.business.dao.core.job.JProdNextTariffHisDao;
import com.ycsoft.business.dao.core.job.JProdPreopenDao;
import com.ycsoft.business.dao.core.job.JProdPreopenHisDao;
import com.ycsoft.business.dao.core.prod.CProdHisDao;
import com.ycsoft.business.dao.core.prod.CProdPropChangeDao;
import com.ycsoft.business.dao.core.prod.CProdPropPatDao;
import com.ycsoft.business.dao.core.prod.CProdResourceAcctDao;
import com.ycsoft.business.dao.core.prod.CProdRscDao;
import com.ycsoft.business.dao.core.prod.CProdSynDao;
import com.ycsoft.business.dao.core.promotion.CPromProdRefundDao;
import com.ycsoft.business.dao.core.user.CRejectResDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.prod.PProdTariffDisctDao;
import com.ycsoft.business.dao.prod.PResDao;
import com.ycsoft.business.dto.core.prod.CProdBacthDto;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.CustProdDto;
import com.ycsoft.business.dto.core.prod.PProdDto;
import com.ycsoft.business.dto.core.prod.ProdResDto;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
import com.ycsoft.business.dto.core.prod.PromFeeProdDto;
import com.ycsoft.business.dto.core.prod.ResGroupDto;
import com.ycsoft.business.dto.core.prod.UserProdDto;
import com.ycsoft.business.dto.core.user.UserProdRscDto;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.store.TemplateConfig.Template;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

/**
 * 用户产品组件
 * @author sheng
 * Jun 25, 2010 4:41:35 PM
 */
@Component
public class UserProdComponent extends BaseBusiComponent {
    private CProdHisDao cProdHisDao;
    private CProdRscDao cProdRscDao;
    private CProdSynDao cProdSynDao;
    private CProdResourceAcctDao cProdResourceAcctDao;
    private PResDao pResDao;
	private PProdTariffDisctDao pProdTariffDisctDao;
	private CRejectResDao cRejectResDao;
	private JProdNextTariffHisDao jProdNextTariffHisDao;
	private TvOrderDao tvOrderDao;
	
	private JProdPreopenDao jProdPreopenDao;
	private JProdPreopenHisDao jProdPreopenHisDao;
	private JobComponent jobComponent;
	private CUserDao cUserDao;
	private CPromProdRefundDao cPromProdRefundDao;
	private CProdPropPatDao cProdPropPatDao;
	
	/**
	 * 查找套餐缴费中产品对应的已存在prod_sn
	 */
	public Map<String, Map<String, PromFeeProdDto>> queryPromFeeProdSn(String custId,String countyId,List<PromFeeProdDto> prodList)throws Exception{
		List<CProdDto> list=cProdDao.queryAllProdAcct(custId,countyId);
		
		Map<String,Map<String,CProdDto>> prodsnMap=new HashMap<String,Map<String,CProdDto>>();
		for(CProdDto o:list){
			if(!prodsnMap.containsKey(o.getUser_id())){
				Map<String,CProdDto> tmap=new HashMap<String,CProdDto>();
				prodsnMap.put(o.getUser_id(), tmap);
			}
			if("BAND".equals(o.getServ_id())){
				//宽带产品自动匹配，一个用户下要求只有一个宽带产品
				prodsnMap.get(o.getUser_id()).put("BAND", o);
			}
			prodsnMap.get(o.getUser_id()).put(o.getProd_id(), o);
		}
		
		Map<String,Map<String,PromFeeProdDto>> promMap=new HashMap<String,Map<String,PromFeeProdDto>>();
		for(PromFeeProdDto o:prodList){
			
			if(prodsnMap.containsKey(o.getUser_id())
					&&prodsnMap.get(o.getUser_id()).containsKey(o.getProd_id())){
				CProdDto cp=prodsnMap.get(o.getUser_id()).get(o.getProd_id());
				o.setProd_sn(cp.getProd_sn());
				o.setTariff_id(cp.getTariff_id());
				o.setRent(cp.getTariff_rent());
				o.setBilling_cycle(cp.getBilling_cycle());
				o.setShould_pay(o.getRent()*o.getMonths()/o.getBilling_cycle());
				o.setProd_id(cp.getProd_id());
				o.setServ_id(cp.getServ_id());
			}
			if(!promMap.containsKey(o.getUser_id())){
				Map<String,PromFeeProdDto> tmap=new HashMap<String,PromFeeProdDto>();
				promMap.put(o.getUser_id(), tmap);
			}
			promMap.get(o.getUser_id()).put(o.getProd_id(), o);
			
			if(o.getProd_id().equals("BAND")){
				throw new ComponentException("Error:宽带用户未订基础产品.");
			}
		}
		return promMap;
	}

	public List<CProdDto> queryAllProdAcct(String custId, String countyId) throws Exception {
		return cProdDao.queryAllProdAcct(custId,countyId);
	}
	
	public List<PProdDto> queryProdByCountyId(String countyId, String prodStatus,
			String tariffStatus, String ruleId, String tariffType)
			throws Exception {
		return pProdDao.queryProdByCountyId(countyId,
				prodStatus, tariffStatus, ruleId, tariffType);
	}
	
	/**
	 * @param prodId
	 * @param countyId
	 * @return
	 */
	public List<PProdDto> queryProdByCountyId(String prodId, String countyId) throws JDBCException {
		return pProdDao.queryProdByCountyId(prodId,countyId);
	}

	/**
	 * 查询多个资费的折扣信息
	 * @param tariffIds
	 * @return
	 * @throws Exception
	 */
	public List<ProdTariffDto> queryTariffByTariffIds(String[] tariffIds) throws Exception{
		List<ProdTariffDto> list = new ArrayList<ProdTariffDto>();
		List<PProdTariff> ptList = pProdTariffDao.queryPTariffByIds(tariffIds);
		for(PProdTariff pt: ptList){
			ProdTariffDto ptdto = new ProdTariffDto();
			PropertyUtils.copyProperties(ptdto,pt);
			list.add(ptdto);
		}
		return list;
	}

	/**
	 * 查询折扣
	 * @param tariffIds
	 * @return
	 */
	public List<PProdTariffDisct> queryDisctByTariffIds(String[] tariffIds) throws Exception {
		return pProdTariffDisctDao.queryDisctByTariffIds(tariffIds, getOptr().getCounty_id());
	}
	
	public PProdTariffDisct queryDistById(String disctId) throws Exception{
		return pProdTariffDisctDao.findByKey(disctId);
	}

	/**
     * 保存用户套餐
	 * @param cust_id
	 * @param acct_id
	 * @param user_id
	 * @param prodId
	 * @param prodOrderTypeOrder
	 * @param feeDate
	 * @param tariff
	 * @return
	 */
	public String addPackage(Integer doneCode,String custId,String acctId,String userId,
			String prodId, String prodType,String orderType, String feeDate, String expDate,
			String stopType,PProdTariff tariff,List<PPackageProd> packageProdList,
			List<UserProdRscDto> dynamicRscs,String stopByInvalidDate,String isBase,Date preOpenTime,String isBankPay) throws Exception{
		CProd prod = new CProd();
		prod.setProd_sn(gUserProdId());
		prod.setDone_code(doneCode);
		prod.setCust_id(custId);
		prod.setUser_id(userId);
		prod.setAcct_id(acctId);
		prod.setProd_id(prodId);
		prod.setPre_open_time(preOpenTime);
		prod.setIs_bank_pay(isBankPay);

		prod.setTariff_id(tariff.getTariff_id());
		prod.setOrder_type(orderType);
		//如果产品资费是非包月的，
		if(tariff.getBilling_cycle() > 1 && SystemConstants.BILLING_TYPE_MONTH.equals(tariff.getBilling_type())){
			prod.setPublic_acctitem_type(SystemConstants.PUBLIC_ACCTITEM_TYPE_NONE);
		}
		//如果是订购，公用账目可用类型为都可以用
		else if(SystemConstants.PROD_ORDER_TYPE_ORDER.equals(orderType)){
			//潜江地区不用公用
			if(SystemConstants.COUNTY_9005.equals(getOptr().getCounty_id())){
				prod.setPublic_acctitem_type(SystemConstants.PUBLIC_ACCTITEM_TYPE_SPEC_ONLY);
			}else{
				prod.setPublic_acctitem_type(SystemConstants.PUBLIC_ACCTITEM_TYPE_ALL);
			}
		}else{
			prod.setPublic_acctitem_type(SystemConstants.PUBLIC_ACCTITEM_TYPE_NONE);
		}
		
		prod.setProd_type(prodType);
		if(null == preOpenTime){
			prod.setStatus(StatusConstants.ACTIVE);
		}else{
			prod.setStatus(StatusConstants.PREAUTHOR);
		}
		prod.setStatus_date(new Date());
		prod.setInvalid_date(DateHelper.strToDate(feeDate));
		prod.setStop_by_invalid_date(stopByInvalidDate);
		prod.setBillinfo_eff_date(DateHelper.strToDate(feeDate));
		//next_bill_date已作废
//		prod.setNext_bill_date(getNextBillDate(tariff, feeDate));
		prod.setCounty_id(getOptr().getCounty_id());
		prod.setArea_id(getOptr().getArea_id());
		setProdStopType(prod, prodType, isBase, stopType,false);
		cProdDao.save(prod);
		createRecordChange( "C_PROD", "INS", prod.getProd_sn());
		
		for (PPackageProd p : packageProdList){
//			addProd(doneCode,custId, acctId, userId, prod.getProd_sn(), prodId, p
//					.getProd_id(), p.getProd().getProd_type(),SystemConstants.PROD_ORDER_TYPE_ORDER,
//					feeDate, expDate, prod.getStop_type(), p.getProdTariff(), dynamicRscs,stopByInvalidDate,isBase,preOpenTime,isBankPay);
		}
		return prod.getProd_sn();
	}
    /**
     * 用户新增产品（基础产品或者客户套餐）及动态资源信息
     * @param custId	客户编号
     * @param acctId	账户编号
     * @param userId	用户编号
     * @param packageSn	所属套餐SN
     * @param prodId	产品id
     * @param orderType	订购方式
     * @param effDate	开始计费日期
     * @param dynamicRscs	动态资源
     * @param tariff	资费信息
     * @param preOpenTime 预开通日期.</br>&nbsp;&nbsp;&nbsp;&nbsp;如果预开通不为空，则产品状态设置为 预开通 ，
     * 	不发授权开通指令（数字和宽带），并生成预开通JOB等待执行.
     * @return
     * @throws Exception
     */
	public String addProd(Integer doneCode,String custId,String acctId,String userId,String packageSn,
			String packageId,String prodId,String prodType,String orderType,String feeDate,String expDate,
			String stopType,PProdTariff tariff, List<UserProdRscDto> dynamicRscList,
			String stopByInvalidDate,String isBase,Date preOpenTime,String isBankPay) throws Exception{
		CProd prod = new CProd();
		prod.setProd_sn(gUserProdId());
		prod.setDone_code(doneCode);
		prod.setCust_id(custId);
		prod.setUser_id(userId);
		prod.setAcct_id(acctId);
		prod.setProd_id(prodId);
		prod.setProd_type(prodType);
		prod.setPackage_id(packageId);
		prod.setPackage_sn(packageSn);
		prod.setPre_open_time(preOpenTime);
		prod.setIs_bank_pay(isBankPay);
		
		if(null != tariff){
			prod.setTariff_id(tariff.getTariff_id());	
		}
		prod.setOrder_type(orderType);
		
		//如果产品资费是非包月的，
		if(null !=tariff && tariff.getBilling_cycle() > 1 && SystemConstants.BILLING_TYPE_MONTH.equals(tariff.getBilling_type())){
			prod.setPublic_acctitem_type(SystemConstants.PUBLIC_ACCTITEM_TYPE_NONE);
		}
		//如果是订购，公用账目可用类型为都可以用
		else if(SystemConstants.PROD_ORDER_TYPE_ORDER.equals(orderType)){
			//潜江地区不用公用
			if(SystemConstants.COUNTY_9005.equals(getOptr().getCounty_id())){
				prod.setPublic_acctitem_type(SystemConstants.PUBLIC_ACCTITEM_TYPE_SPEC_ONLY);
			}else{
				prod.setPublic_acctitem_type(SystemConstants.PUBLIC_ACCTITEM_TYPE_ALL);
			}
		}else{
			prod.setPublic_acctitem_type(SystemConstants.PUBLIC_ACCTITEM_TYPE_NONE);
		}
		if(preOpenTime !=null){
			prod.setStatus(StatusConstants.PREAUTHOR);
		}else{
			prod.setStatus(StatusConstants.ACTIVE);
		}
		prod.setStatus_date(new Date());
		prod.setInvalid_date(DateHelper.strToDate(feeDate));
		if(StringHelper.isNotEmpty(expDate)){
			prod.setExp_date(DateHelper.strToDate(expDate));
		}
		setProdStopType(prod, prodType, isBase, stopType,StringHelper.isNotEmpty(packageSn)?true:false);
		prod.setStop_by_invalid_date(stopByInvalidDate);
		//next_bill_date已作废
//		prod.setNext_bill_date(getNextBillDate(tariff, feeDate));
		/**
		 * 0资费基本包时，开始计费日应该是订购当天，不应该是到期日前一天.
		 */
		if(prodType.equals(SystemConstants.PROD_TYPE_BASE) && tariff.getRent() == 0){
			prod.setBillinfo_eff_date(DateHelper.now());
		}else{
			prod.setBillinfo_eff_date(DateHelper.strToDate(feeDate));
		}
		prod.setCounty_id(getOptr().getCounty_id());
		prod.setArea_id(getOptr().getArea_id());
		cProdDao.save(prod);
		createRecordChange( "C_PROD", "INS", prod.getProd_sn());
		
		//产品存在动态资源信息
		PRes[] dynamicRscs = getDynamicRsc(prodId,dynamicRscList);
		if(dynamicRscs != null){
			for (PRes rsc : dynamicRscs) {
				addUserProdres(prod.getProd_sn(), rsc.getRes_id());
			}
		}
		return prod.getProd_sn();
	}

	/**
	 * 设置用户产品的催停标志
	 * @param prod
	 * @param prodType
	 * @param isBase
	 * @param stopType
	 */
	private void setProdStopType(CProd prod,String prodType,String isBase,String stopType,boolean isChildProd){
		if (StringHelper.isEmpty(stopType))
			prod.setStop_type(SystemConstants.STOP_TYPE_KCKT);
		if (prodType.equals(SystemConstants.PROD_TYPE_CUSTPKG) || isChildProd){
			prod.setStop_type(stopType);
		} else {
			if (SystemConstants.BOOLEAN_TRUE.equals(isBase)){
				prod.setStop_type(stopType);
			} else {
				prod.setStop_type(SystemConstants.STOP_TYPE_KCKT);
			}
		}
		
	}
	public void addUserProdres(String prodSn, String  resId) throws Exception {
		CProdRsc userProdRsc = new CProdRsc();
		userProdRsc.setProd_sn(prodSn);
		userProdRsc.setRes_id(resId);
		userProdRsc.setCounty_id(getOptr().getCounty_id());
		userProdRsc.setArea_id(getOptr().getArea_id());
		List<CProdRsc> userProdResList = cProdRscDao.findByEntity(userProdRsc);
		if (userProdResList == null || userProdResList.size()==0)
			cProdRscDao.save(userProdRsc);
	}

	/**
	 * 增加产品资源账户
	 */
	public void addProdRscAcct(Integer doneCode,String prodSn,String feeUnit,int amount) throws Exception{
		CProdResourceAcct rscAcct = new CProdResourceAcct();
		rscAcct.setProd_sn(prodSn);
		rscAcct.setDone_code(doneCode);
		rscAcct.setFree_unit(feeUnit);
		rscAcct.setAmount(amount);
		setBaseInfo(rscAcct);
		cProdResourceAcctDao.save(rscAcct);

	}

	/**
	 * 修改产品资费
	 * @param prodSn
	 * @param tariffId
	 * @throws JDBCException
	 */
	public void updateProdTariff(Integer doneCode, String prodSn,String tariffId) throws Exception{
		PProdTariff tariff = this.pProdTariffDao.findByKey(tariffId);
//		CProd prod = cProdDao.findByKey(prodSn);
//		PProdTariff oldTariff = pProdTariffDao.findByKey(prod.getTariff_id());
		//单月资费改为多月资费
		/*if (oldTariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH)
				&& tariff.getBilling_cycle().intValue() > 1) {
			cProdDao.updateNextBillDate(prodSn);
		}*/
		
		String stopByInvlaidDate = SystemConstants.BOOLEAN_FALSE;
		String publicType = SystemConstants.PUBLIC_ACCTITEM_TYPE_ALL;
		if ( tariff.getBilling_cycle()>1 
				|| (tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH) 
						&& tariff.getRent() ==0)){
			stopByInvlaidDate = SystemConstants.BOOLEAN_TRUE;
			publicType = SystemConstants.PUBLIC_ACCTITEM_TYPE_NONE;
		}
		cProdDao.updateProdTariff(doneCode, prodSn, tariffId,stopByInvlaidDate, publicType);
		createRecordChange("C_PROD", "MOD", prodSn);
	}
	
	

	/**
	 * 修改产品未生效资费
	 * @param prodSn
	 * @param nextTariffId
	 * @param countyId
	 * @throws Exception
	 */
	public void updateNextTariff(String prodSn,String nextTariffId) throws Exception{
		cProdDao.updateNextTariff(prodSn, nextTariffId, getOptr().getCounty_id());
	}

	/**
	 * 修改产品状态
	 * @param doneCode
	 * @param prod_sn
	 * @param status
	 * @param active
	 */
	public void updateProdStatus(Integer doneCode, String prodSn,
			String oldStatus, String newStatus)  throws Exception{
		CProd prod = cProdDao.findByKey(prodSn);
		List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
		CProdPropChange propChange = new CProdPropChange();
		propChange.setColumn_name("status");
		propChange.setOld_value(oldStatus);
		propChange.setNew_value(newStatus);

		changeList.add(propChange);
		
		propChange = new CProdPropChange();
		propChange.setColumn_name("status_date");
		propChange.setOld_value(DateHelper.dateToStr(prod.getStatus_date()));
		propChange.setNew_value(DateHelper.formatNow());
		
		changeList.add(propChange);
		
		editProd(doneCode, prodSn, changeList);
	}

	/**
	 * 修改产品对应的套餐
	 * @param prodSn
	 * @param pkgSn
	 * @param pkgId
	 * @throws Exception
	 */
	public void updateProdPkg(String prodSn,String pkgSn,String pkgId) throws Exception{
		cProdDao.updateProdPkg(prodSn, pkgSn,pkgId, getOptr().getCounty_id());
		createRecordChange("C_PROD", "MOD", prodSn);
	}

	/**
	 * 更新产品的出帐日期
	 * @param userId
	 * @param days
	 * @throws Exception
	 */
	public void updateNextBillDate(String userId,String stopDate) throws Exception {
		cProdDao.updateNextBillDate(userId, stopDate, getOptr().getCounty_id());
	}


	/**
	 * @param doneCode
	 * @param busiCode
	 * @param oldUserId
	 * @param newCustId
	 * @param newUserId
	 * @param newAcctId
	 * @throws Exception
	 */
	public void updateProd(Integer doneCode, String busiCode, String oldUserId, 
			String newCustId, String newUserId, String newAcctId) throws Exception {
		List<CProd> prodList = cProdDao.queryByUserId(oldUserId);
		
		CUser newUser = cUserDao.findByKey(newUserId);
		for(CProd prod : prodList){
			List<CProdPropChange> propChangeList = new ArrayList<CProdPropChange>();
			CProdPropChange change = new CProdPropChange();
			change.setProd_sn(prod.getProd_sn());
			change.setColumn_name("cust_id");
			change.setOld_value(prod.getCust_id());
			change.setNew_value(newCustId);
			change.setBusi_code(busiCode);
			setBaseInfo(change);
			propChangeList.add(change);
			
			change = new CProdPropChange();
			change.setProd_sn(prod.getProd_sn());
			change.setColumn_name("user_id");
			change.setOld_value(prod.getUser_id());
			change.setNew_value(newUserId);
			change.setBusi_code(busiCode);
			setBaseInfo(change);
			propChangeList.add(change);
			
			change = new CProdPropChange();
			change.setProd_sn(prod.getProd_sn());
			change.setColumn_name("acct_id");
			change.setOld_value(prod.getAcct_id());
			change.setNew_value(newAcctId);
			change.setBusi_code(busiCode);
			setBaseInfo(change);
			propChangeList.add(change);
			
			this.editProd(doneCode, prod.getProd_sn(), propChangeList);
			
			jobComponent.createBusiCmdJob(doneCode,
					BusiCmdConstants.ACCTIVATE_PROD, newUser.getCust_id(),
					newUser.getUser_id(), newUser.getStb_id(), newUser.getCard_id(),
					newUser.getModem_mac(), prod.getProd_sn(), prod.getProd_id());
		}
	}
	
	public List<CProdDto> queryChildProdByPkgsn(String pkgSn) throws Exception {
		return cProdDao.queryChildProdByPkgsn(pkgSn, getOptr().getCounty_id());
	}
	
	/**
	 * 删除用户产品
	 * @param doneCode
	 * @param prodSn
	 * @throws Exception
	 */
	public void removeProdWithHis(Integer doneCode,CProd prod) throws Exception{
		//保存产品历史
		CProdHis prodHis = new CProdHis();
		PropertyUtils.copyProperties(prodHis, prod);
		prodHis.setDone_code(doneCode);
		prodHis.setOrder_done_code(prod.getDone_code());
		prodHis.setStatus(StatusConstants.INVALID);
		cProdHisDao.save(prodHis);
		//删除产品
		cProdDao.remove(prod.getProd_sn());
		createRecordChange("C_PROD", "DEL", prod.getProd_sn());
		//cProdRscDao.removeByProdSn(prod.getProd_sn());不能删除产品的动态资源,否则指令就会找不到要发的资源
		cProdResourceAcctDao.removeByProdSn(prod.getProd_sn());
		//如果是套餐，删除子产品
		if (!prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
			List<CProdDto> childList = cProdDao.queryChildProdByPkgsn(prod.getProd_sn(), getOptr().getCounty_id());
			for (CProdDto childProd:childList){
				CProdHis childProdHis = new CProdHis();
				PropertyUtils.copyProperties(childProdHis, childProd);
				childProdHis.setOrder_done_code(childProd.getDone_code());
				childProdHis.setDone_code(doneCode);
				cProdHisDao.save(childProdHis);
				//删除产品
				cProdDao.remove(childProd.getProd_sn());
//				cProdRscDao.removeByProdSn(childProd.getProd_sn());不能删除产品的动态资源,否则指令就会找不到要发的资源
				createRecordChange("C_PROD", "DEL", prod.getProd_sn());
			}
		}
	}

	/**
	 * 删除产品不记录历史
	 * @param doneCode
	 * @param prodSn
	 * @throws Exception
	 */
	public void removeProdWithoutHis(Integer doneCode,String prodSn) throws Exception{
		//删除产品
		cProdDao.remove(prodSn);
		createRecordChange("C_PROD", "DEL", prodSn);
		cProdResourceAcctDao.removeByProdSn(prodSn);
		cProdResourceAcctDao.removeByProdSn(prodSn);
		 
		//如果是套餐，删除子产品
		List<CProdDto> childList = cProdDao.queryChildProdByPkgsn(prodSn, getOptr().getCounty_id());
		if (childList != null){
			for (CProdDto childProd:childList){
				cProdDao.remove(childProd.getProd_sn());
				cProdRscDao.removeByProdSn(childProd.getProd_sn());
				createRecordChange("C_PROD", "DEL", prodSn);
			}
		}
	}
	
	public void removeProdRes(String prodSn,String[] resIds) throws Exception{
		if (resIds != null && resIds.length>0){
			cProdRscDao.removeByProdRes(prodSn, resIds);
		}
	}
	/**
	 * 增加一条用户产品历史记录
	 * @param doneCode
	 * @param prod
	 * @throws Exception
	 */
	public void addHis(Integer doneCode,CProd prod) throws Exception{
		//保存产品历史
		CProdHis prodHis = new CProdHis();
		PropertyUtils.copyProperties(prodHis, prod);
		prodHis.setDone_code(doneCode);
		prodHis.setOrder_done_code(prod.getDone_code());
		prodHis.setInvalid_date(new Date());
		cProdHisDao.save(prodHis);
	}
	
	public void saveProdSyn(int doneCode,String custId,String sourceUserId,String orderUserId,
			String prodId,String isNewProd,Date oldInvalidDate,Date newInvalidDate,int fee) throws Exception {
		CProdSyn syn = new CProdSyn();
		syn.setDone_code(doneCode);
		syn.setCust_id(custId);
		syn.setSoruce_user_id(sourceUserId);
		syn.setOrder_user_id(orderUserId);
		syn.setProd_id(prodId);
		syn.setIs_new_prod(isNewProd);
		syn.setOld_invalid_date(oldInvalidDate);
		syn.setNew_invalid_date(newInvalidDate);
		syn.setFee(fee);
		syn.setArea_id(this.getOptr().getArea_id());
		syn.setCounty_id(this.getOptr().getCounty_id());
		
		this.cProdSynDao.save(syn);
	}
	
	public void changeProdDynRes(String prodSn,List<UserProdRscDto> dyResList) throws Exception {
		cProdRscDao.removeByProdSn(prodSn);
		
		
		List<CProdRsc> prList = new ArrayList<CProdRsc>();
		for(UserProdRscDto upRsc : dyResList){
			List<PRes> resList = upRsc.getRscList();
			for(PRes pres : resList){
				CProdRsc pr = new CProdRsc();
				pr.setProd_sn(prodSn);
				pr.setRes_id(pres.getRes_id());
				pr.setCounty_id(getOptr().getCounty_id());
				pr.setArea_id(getOptr().getArea_id());
				prList.add(pr);
			}
		}
		cProdRscDao.save(prList.toArray(new CProdRsc[prList.size()]));
		createRecordChange( "C_PROD", "INS", prodSn);
	}
	/**
	 * 保存套餐缴费中产品的动态资源
	 * @param prodSn
	 * @param resList
	 */
	public void savePromFeeProdDynRes(String prodSn,List<PRes> resList)throws Exception {
		cProdRscDao.removeByProdSn(prodSn);
		List<CProdRsc> prList = new ArrayList<CProdRsc>();
		Map<String,String> prMap=new HashMap<String,String>();//去掉重复的资源
		for(PRes pres : resList){
			if(!prMap.containsKey(pres.getRes_id())){
				CProdRsc pr = new CProdRsc();
				pr.setProd_sn(prodSn);
				pr.setRes_id(pres.getRes_id());
				pr.setCounty_id(getOptr().getCounty_id());
				pr.setArea_id(getOptr().getArea_id());
				prList.add(pr);
				prMap.put(pres.getRes_id(), pres.getRes_id());
			}
		}
		cProdRscDao.save(prList.toArray(new CProdRsc[prList.size()]));
	}

	/**
	 * 获取产品的下次出帐日期
	 * @param tariff
	 * @param effDate
	 * @return
	 */
	private Date getNextBillDate(PProdTariff tariff,String effDate){
		if (null != tariff && tariff.getBilling_cycle()>1){
			return DateHelper.strToDate(effDate);
		} else {
			return null;
		}
	}

	/**
	 * 根据流水查找到期日变更的异动记录,算出2个日期的相差天数
	 * @param doneCode
	 * @return
	 * @throws Exception
	 */
	public int getDate(int doneCode) throws Exception {
		CProdPropChange change = cProdPropChangeDao.queryOldDate(doneCode, getOptr().getCounty_id());
		if (change == null)
			return 0;
		return DateHelper.getDifferDays(change.getOld_value(),change.getNew_value());
	}
	
	/**
	 * 0资费产品冲正，根据冲正的流水找到原先交费的天数，目前的到期日期减去这个天数
	 * @param doneCode
	 * @param oldDoneCode
	 * @param prodSn
	 * @return
	 * @throws Exception
	 */
	public Date updateInvalidDateByDoneCode(int doneCode,int oldDoneCode, String prodSn) throws Exception {
		if(StringHelper.isNotEmpty(prodSn)){
			CProd prod = queryByProdSn(prodSn);
			if (prod != null ){
				int num = getDate(oldDoneCode);
				Date invalidDate = getDate(prod.getInvalid_date(), 0, -num);
					//如果本产品是用户套餐产品就查询所有子产品				
					List<CProd> prodList = queryByPkgSn(prodSn);
					//添加本产品的c_prod到list
					prodList.add(prod);
					for(CProd dto:prodList){
						List<CProdPropChange> List = new ArrayList<CProdPropChange>();
						List.add(new CProdPropChange("invalid_date",
								DateHelper.dateToStr(dto.getInvalid_date()),DateHelper.dateToStr(invalidDate)));
						editProd(doneCode, dto.getProd_sn(), List);
					}
					return invalidDate;
			}
		}
		return null;
	}
	
	/**
	 * lxr:账务模式修改计算方法 注销原始方法
	 
	public Date updateInvalidDate(int doneCode, String prodSn,int freeDays,
			int changeFee, CAcctAcctitem acctItem) throws Exception {
		if(StringHelper.isNotEmpty(prodSn)){
			CProd prod = queryByProdSn(prodSn);
			if (prod != null ){
				//判断产品资费的租金是否为0，如果是0则不计算
				//保存用户到期日异动信息				
				PProdTariff tariff = this.queryProdTariffById(prod.getTariff_id());
				if (tariff.getRent()>0){
					int balance = acctItem.getActive_balance() + acctItem.getInactive_balance();
					
					//包多月的产品,账单费用
					int billFee = 0;
					if(tariff.getBilling_cycle() > 1){
						billFee = cProdDao.queryBillFeeByProdSn(prodSn);
					}
					
					Date invalidDate = getInvalidDate(prod.getInvalid_date(), prod.getTariff_id(),							
							acctItem.getOwe_fee(),balance,acctItem.getReal_fee(),changeFee,freeDays,billFee);					
					//如果本产品是用户套餐产品就查询所有子产品				
					List<CProd> prodList = queryByPkgSn(prodSn);
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
							
							//如果是包多月产品，
							if(SystemConstants.BOOLEAN_TRUE.equals(dto.getStop_by_invalid_date())
									&& dto.getNext_bill_date() != null){
								Date nextBillDate = dto.getNext_bill_date();
								Date today = DateHelper.strToDate(DateHelper.formatNow());
								if(nextBillDate.before(DateHelper.strToDate(DateHelper.formatNow()))){
									nextBillDate = today;
								}
								
								//如果新的到期日比下次出账日期小，而且已经出过账（"已经出账"为了排除那些因第一次免费观看，导致出账在到期日之后的情况）
								if(!today.equals(invalidDate) && invalidDate.before(nextBillDate) && null !=dto.getLast_bill_date()){
									nextBillDate = invalidDate;
								}
								
								if(!dto.getNext_bill_date().equals(nextBillDate)){
									List.add(new CProdPropChange("next_bill_date",
											DateHelper.dateToStr(dto.getNext_bill_date()),DateHelper.dateToStr(nextBillDate)));
								}
							}
							
							editProd(doneCode, dto.getProd_sn(), List);
						}else{
							List<CProdPropChange> List = new ArrayList<CProdPropChange>();
							List.add(new CProdPropChange("invalid_date",
									DateHelper.dateToStr(dto.getInvalid_date()),DateHelper.dateToStr(invalidDate)));
							
							//如果是包多月产品，
							if(SystemConstants.BOOLEAN_TRUE.equals(dto.getStop_by_invalid_date())
									&& dto.getNext_bill_date() != null){
								Date nextBillDate = dto.getNext_bill_date();
								Date today = DateHelper.strToDate(DateHelper.formatNow());
								if(nextBillDate.before(DateHelper.strToDate(DateHelper.formatNow()))){
									nextBillDate = today;
								}
								
								//如果新的到期日比下次出账日期小，而且已经出过账（"已经出账"为了排除那些因第一次免费观看，导致出账在到期日之后的情况）
								if(!today.equals(invalidDate) && invalidDate.before(nextBillDate) && null !=dto.getLast_bill_date()){
									nextBillDate = invalidDate;
								}
								
								if(!dto.getNext_bill_date().equals(nextBillDate)){
									List.add(new CProdPropChange("next_bill_date",
											DateHelper.dateToStr(dto.getNext_bill_date()),DateHelper.dateToStr(nextBillDate)));
								}
							}
							
							editProd(doneCode, dto.getProd_sn(), List);
						}
					}
					
					return invalidDate;
				}
			}
		}
		return null;
	}

	public Date updateInvalidDateByTariff(int doneCode, String prodSn, CAcctAcctitem acctItem) throws Exception {
		if(StringHelper.isNotEmpty(prodSn)){
			CProd prod = queryByProdSn(prodSn);
			if (prod != null ){
				//判断产品资费的租金是否为0，如果是0则不计算
				//保存用户到期日异动信息				
				PProdTariff tariff = this.queryProdTariffById(prod.getTariff_id());
				if (tariff.getRent()>0){
					int balance = acctItem.getActive_balance() + acctItem.getInactive_balance();
					Date invalidDate = getInvalidDateByTarrif(DateHelper.now(), prod.getTariff_id(),							
							acctItem.getOwe_fee(),balance,acctItem.getReal_fee());					
					//如果本产品是用户套餐产品就查询所有子产品				
					List<CProd> prodList = queryByPkgSn(prodSn);
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
							
							//如果是包多月产品，
							if(SystemConstants.BOOLEAN_TRUE.equals(dto.getStop_by_invalid_date())
									&& dto.getNext_bill_date() != null){
								Date nextBillDate = dto.getNext_bill_date();
								Date today = DateHelper.strToDate(DateHelper.formatNow());
								if(nextBillDate.before(DateHelper.strToDate(DateHelper.formatNow()))){
									nextBillDate = today;
								}
								
								//如果新的到期日比下次出账日期小，而且已经出过账（"已经出账"为了排除那些因第一次免费观看，导致出账在到期日之后的情况）
								if(!today.equals(invalidDate) && invalidDate.before(nextBillDate) && null !=dto.getLast_bill_date()){
									nextBillDate = invalidDate;
								}
								
								if(!dto.getNext_bill_date().equals(nextBillDate)){
									List.add(new CProdPropChange("next_bill_date",
											DateHelper.dateToStr(dto.getNext_bill_date()),DateHelper.dateToStr(nextBillDate)));
								}
							}
							
							editProd(doneCode, dto.getProd_sn(), List);
						}else{
							List<CProdPropChange> List = new ArrayList<CProdPropChange>();
							List.add(new CProdPropChange("invalid_date",
									DateHelper.dateToStr(dto.getInvalid_date()),DateHelper.dateToStr(invalidDate)));
							
							//如果是包多月产品，
							if(SystemConstants.BOOLEAN_TRUE.equals(dto.getStop_by_invalid_date())
									&& dto.getNext_bill_date() != null){
								Date nextBillDate = dto.getNext_bill_date();
								Date today = DateHelper.strToDate(DateHelper.formatNow());
								if(nextBillDate.before(DateHelper.strToDate(DateHelper.formatNow()))){
									nextBillDate = today;
								}
								
								//如果新的到期日比下次出账日期小，而且已经出过账（"已经出账"为了排除那些因第一次免费观看，导致出账在到期日之后的情况）
								if(!today.equals(invalidDate) && invalidDate.before(nextBillDate) && null !=dto.getLast_bill_date()){
									nextBillDate = invalidDate;
								}
								
								if(!dto.getNext_bill_date().equals(nextBillDate)){
									List.add(new CProdPropChange("next_bill_date",
											DateHelper.dateToStr(dto.getNext_bill_date()),DateHelper.dateToStr(nextBillDate)));
								}
							}
							
							editProd(doneCode, dto.getProd_sn(), List);
						}
					}
					
					return invalidDate;
				}
			}
		}
		return null;
	}
	*/
	
/*	*//**
	 * 得到下一个到期日
	 * @param invalidDate
	 * @param tariffId
	 * @param changeFee
	 * @param acctItem
	 * @return
	 * @throws Exception
	 *//*
	public Date getNextInvalidDate(Date invalidDate,String tariffId,int changeFee, CAcctAcctitem acctItem) throws Exception{
		int balance = acctItem.getActive_balance() + acctItem.getInactive_balance();
		return getInvalidDate(invalidDate, tariffId,
				acctItem.getOwe_fee(),balance,acctItem.getReal_fee(),changeFee, 0);
	}*/
	
	
	/**
	 * 修改到期日,如果非正常状态变成正常状态
	 * @param doneCode
	 * @param prod
	 * @param invalidDate
	 * @throws Exception
	 */
	public void editInvalidDate(int doneCode, CProd prod,Date invalidDate) throws Exception {
		//保存用户产品订购方式和到期日异动信息
		List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
		changeList.add(new CProdPropChange("invalid_date",
				DateHelper.dateToStr(prod.getInvalid_date()),DateHelper.dateToStr(invalidDate)));
		
		if(!prod.getStatus().equals(StatusConstants.ACTIVE)){
			changeList.add(new CProdPropChange("status",prod.getStatus(),StatusConstants.ACTIVE));
			//next_bill_date已作废
//			changeList.add(new CProdPropChange("next_bill_date",DateHelper.dateToStr(prod.getNext_bill_date()),
//					DateHelper.dateToStr(invalidDate)));
		}
		//新增到期日
		//next_bill_date已作废
//		int day = DateHelper.getDiffDays(prod.getInvalid_date(), invalidDate);
//		Date newBillDate = DateHelper.addDate(prod.getNext_bill_date().before(new Date()) ? new Date() : prod.getNext_bill_date(), day);
		
//		changeList.add(new CProdPropChange("next_bill_date",DateHelper.dateToStr(prod.getNext_bill_date()),
//				DateHelper.dateToStr(newBillDate)));
		
		editProd(doneCode, prod.getProd_sn(), changeList);
		
		List<CProd> prodList = queryByPkgSn(prod.getProd_sn());
		for(CProd cprod : prodList){
			if(StringHelper.isNotEmpty(cprod.getPackage_sn())){
				List<CProdPropChange> cpcList = new ArrayList<CProdPropChange>();
				cpcList.add(new CProdPropChange("invalid_date",
						DateHelper.dateToStr(cprod.getInvalid_date()),DateHelper.dateToStr(invalidDate)));
				editProd(doneCode, cprod.getProd_sn(), cpcList);
			}
		}
	}
	

	/**
	 * 协议缴费，修改到期日，产品状态由非正常，改为正常，并发加授权
	 * 20130416 by wqy  临时授权，修改到期日，状态改为临时开通，并发加授权 
	 * @param doneCode
	 * @param prodSn
	 * @param invalidDate
	 * @throws Exception
	 */
	public void updateInvalidDateStatus(int doneCode, String prodSn,Date invalidDate,String busiCode) throws Exception {
		if(StringHelper.isNotEmpty(prodSn)){
			CProd prod = queryByProdSn(prodSn);
			//保存用户产品订购方式和到期日异动信息
			List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
			changeList.add(new CProdPropChange("invalid_date",
					DateHelper.dateToStr(prod.getInvalid_date()),DateHelper.dateToStr(invalidDate)));
			if(!prod.getStatus().equals(StatusConstants.ACTIVE)){
				if(busiCode.equals(BusiCodeConstants.ACCT_PAY_ZERO)){
					changeList.add(new CProdPropChange("status",prod.getStatus(),StatusConstants.ACTIVE));
				}else if(busiCode.equals(BusiCodeConstants.USER_OPEN_TEMP)){
					changeList.add(new CProdPropChange("status",prod.getStatus(),StatusConstants.TMPOPEN));
				}
				//发送指令
				CUser user = cUserDao.findByKey(prod.getUser_id());
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD,
						user.getCust_id(), user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), prodSn,prod.getProd_id());

			}
			editProd(doneCode, prod.getProd_sn(), changeList);
		}
	}

	public void updateInvalidDate(int doneCode, String prodSn,Date invalidDate) throws Exception {
		if(StringHelper.isNotEmpty(prodSn)){
			CProd prod = queryByProdSn(prodSn);
			if(!DateHelper.dateToStr(prod.getInvalid_date()).equals(DateHelper.dateToStr(invalidDate))){
				//保存用户产品订购方式和到期日异动信息
				List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
				changeList.add(new CProdPropChange("invalid_date",
						DateHelper.dateToStr(prod.getInvalid_date()),DateHelper.dateToStr(invalidDate)));
				editProd(doneCode, prod.getProd_sn(), changeList);
			}
		}
	}
	
	/**
	 * 修改失效日期
	 * @param doneCode
	 * @param prodSn
	 * @param newExpDate
	 * @throws Exception
	 */
	public void updateExpDate(int doneCode, String prodSn,String newExpDate) throws Exception {
		if(StringHelper.isNotEmpty(prodSn)){
			CProd prod = queryByProdSn(prodSn);
			List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
			String time = null;
			if(StringHelper.isNotEmpty(newExpDate)){
				time = newExpDate;
			}
			changeList.add(new CProdPropChange("exp_date",
					DateHelper.dateToStr(prod.getExp_date()),time));
			editProd(doneCode, prod.getProd_sn(), changeList);
		}
	}
	
	public void removeByProdSn(String prodSn, String tariffId) throws Exception {
		JProdNextTariff jProdNextTariff = jProdNextTariffDao.queryByProdSn(
				prodSn, tariffId, getOptr().getCounty_id());
		JProdNextTariffHis jProdNextTariffHis = new JProdNextTariffHis();
		BeanUtils.copyProperties(jProdNextTariff, jProdNextTariffHis);
		jProdNextTariffHisDao.save(jProdNextTariffHis);
		jProdNextTariffDao.removeByProdSn(prodSn, tariffId, getOptr().getCounty_id());
		this.updateNextTariff(prodSn, null);
	}
	
	
	public void removeNextByProdSn(String prodSn) throws Exception {
		List<JProdNextTariff> jProdNextTariffList = jProdNextTariffDao.queryNextByProdSn(
				prodSn, getOptr().getCounty_id());
		for(JProdNextTariff dto :jProdNextTariffList){
			JProdNextTariffHis jProdNextTariffHis = new JProdNextTariffHis();
			BeanUtils.copyProperties(dto, jProdNextTariffHis);
			jProdNextTariffHisDao.save(jProdNextTariffHis);
		}
		jProdNextTariffDao.removeByProdSn(prodSn, getOptr().getCounty_id());
		this.updateNextTariff(prodSn, null);
	}
	
	/**
	 * 日期计算
	 * @param beginDate
	 * @param addMonths
	 * @param addDays
	 * @return
	 * @throws Exception
	 */
	public Date getDate(Date beginDate,int addMonths,int addDays) throws Exception{
		return cProdDao.getDate(beginDate, addMonths, addDays);
	}
	
	/**
	 * lxr:账务模式到期日 该方法失效
	 * 产品信息
	 * @param prod
	 * @param changeBalance 余额变化量
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	private Date getInvalidDate(Date invalidDate,String tariffId,int oweFee,int balance,int realFee,int changeBalance,int changeFreeDays,int billFee) throws Exception{
		PProdTariff tariff = pProdTariffDao.findByKey(tariffId);
		PProd prod = pProdDao.findByKey(tariff.getProd_id());
		if (tariff.getRent() == 0)
			return invalidDate;
		int addMonths=0;
		int addDays =changeFreeDays;
		if (changeBalance!=0){
			if (!prod.getServ_id().equals(SystemConstants.PROD_SERV_ID_ATV)){
				
				changeBalance = changeBalance - (balance>oweFee?0:oweFee-balance);
				if (invalidDate.before(DateHelper.today())){
					changeBalance = changeBalance + balance - realFee;
//					if ((int)Math.round(tariff.getRent()*1.0/30*(DateHelper.getCurrDAY()-1)) == realFee) {
//						//invalidDate = DateHelper.strToDate(DateHelper.getFirstDateInCurrentMonth());
//					} else {
					invalidDate = new Date();
//					}
					 
				}
			}
			
			//计费方式包月
			if (tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH)){
				if (tariff.getBilling_cycle()>1){
					//如果变化金额+扣除账单后剩余的金额小于0，到期日恢复到当天
					if((changeBalance+balance-billFee)<0){
						invalidDate = new Date();
					}else{
						addMonths = ((changeBalance+balance-billFee)/tariff.getRent() - (balance-billFee)/tariff.getRent()) *tariff.getBilling_cycle();
					}
				} else {
					addMonths = changeBalance/tariff.getRent();
					addDays += (changeBalance - tariff.getRent()*addMonths)/(tariff.getRent()/30);
				}
			}
		}
		invalidDate = getDate(invalidDate, addMonths, addDays);
		if(LoggerHelper.isDebugEnabled(UserProdComponent.class)){
			StringBuffer msg= new StringBuffer(100);
			msg.append("invalidDate=").append(invalidDate);
			msg.append("tariffId=").append(tariffId);
			msg.append("oweFee=").append(oweFee);
			msg.append("balance=").append(balance);
			msg.append("realFee=").append(realFee);
			msg.append("changeBalance=").append(changeBalance);
			msg.append("changeFreeDays=").append(changeFreeDays);
			msg.append("addMonths=").append(addMonths);
			msg.append("addDays=").append(addDays);
			msg.append("result invalidDate=").append(invalidDate);
			
			LoggerHelper.debug(UserProdComponent.class, msg.toString());
		}
		return invalidDate;
	}
	
	/**
	 * 账务模式 到期日 该方法失效
	 * 现用于资费变更生效时计算到期日，扣除所有欠费后的剩余金额>0时，重新计算到期日
	 * @param invalidDate 	原到期日
	 * @param tariffId 		新资费
	 * @param oweFee		往月欠费
	 * @param balance		账户金额
	 * @param realFee		实时费用
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	private Date getInvalidDateByTarrif(Date invalidDate,String tariffId,int oweFee,int balance,int realFee) throws Exception{
		PProdTariff tariff = pProdTariffDao.findByKey(tariffId);
		if (tariff.getRent() == 0)
			return invalidDate;
		int addMonths=0;
		int addDays =0;
		int changeBalance = 0;
			changeBalance = balance>(oweFee+realFee)?balance-(oweFee+realFee):0;
			if(changeBalance>0){
				//计费方式包月
				if (tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH)){
					if (tariff.getBilling_cycle()>1){
						addMonths = (changeBalance/tariff.getRent()) *tariff.getBilling_cycle();
					} else {
						addMonths = changeBalance/tariff.getRent();
						addDays += (changeBalance - tariff.getRent()*addMonths)/(tariff.getRent()/30);
					}
				}
				//包月周期>1时，剩余金额<资费金额，那么到期日还是原到期日
				if(addMonths>0||addDays>0){
					invalidDate = new Date();
				}
			}
		invalidDate = getDate(invalidDate, addMonths, addDays);

		return invalidDate;
	}
	
	
	public int getFeeByNextInvaildDate(Date nextInvalidDate,Date invalidDate,String tariffId,CAcctAcctitem acctItem) throws JDBCException{
		int fee = 0;
		PProdTariff tariff = pProdTariffDao.findByKey(tariffId);
		//零资费
		if (tariff.getRent() == 0){
			return fee;
		}
		
		int balance = acctItem.getActive_balance() + acctItem.getInactive_balance();
		//余额<=0
		if(balance <= 0){
			fee = acctItem.getOwe_fee();
		}
		
		PProd prod = pProdDao.findByKey(tariff.getProd_id());
		//服务类型不是模拟
		if (!prod.getServ_id().equals(SystemConstants.PROD_SERV_ID_ATV)){
			if (invalidDate.before(new Date())){
				if ((int)Math.round(tariff.getRent()*1.0/30*(DateHelper.getCurrDAY()-1)) == acctItem.getReal_fee()) {
					invalidDate = DateHelper.strToDate(DateHelper.getFirstDateInCurrentMonth());
				} else {
					invalidDate = new Date();
//					changeBalance = changeBalance + balance - realFee;
					fee = fee + acctItem.getReal_fee() - balance;
				}
			}
		}	
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(nextInvalidDate);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(invalidDate);
		
		int months = (cal.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12 + (cal.get(Calendar.MONTH) - cal2.get(Calendar.MONTH));
		int days = cal.get(Calendar.DAY_OF_MONTH) - cal2.get(Calendar.DAY_OF_MONTH);
		
		//计费方式包月
		if (tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH)){
			if (tariff.getBilling_cycle()>1){
				fee = fee + months * tariff.getRent()/tariff.getBilling_cycle();
			} else {
				fee = fee + months * tariff.getRent() + days * tariff.getRent() / 30;
			}
		}
		
		
		return fee;
	}
	
	/**
	 * 查询所有模拟产品
	 * @return
	 * @throws Exception
	 */
	public List<PProd> queryAllAtvProds() throws Exception{
		return pProdDao.queryCanOrderUserProd(SystemConstants.PROD_SERV_ID_ATV, getOptr().getArea_id(), getOptr().getCounty_id(), SystemConstants.DEFAULT_DATA_RIGHT);
	}
	
	/**
	 * 查询所有数字产品
	 * @param areaId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<PProd> queryAllDtvProds(String servId ,String areaId,String countyId) throws Exception{
		return pProdDao.queryCanOrderUserProd(servId, areaId, countyId, "1=1");
	}
	
	public List<PProd> queryCanOrderUserProd(String servId) throws Exception {
		String dataRight = this.queryDataRightCon(getOptr(), DataRight.PROD_ORDER.toString());
		return pProdDao.queryCanOrderUserProd(servId,getOptr().getArea_id(),getOptr().getCounty_id(),dataRight);
	}
	
	/**
	 * 获得用户可以订购的产品
	 * @param userIds
	 * @param servId 产品服务
	 * @return
	 */
	public List<PProd> queryCanOrderProds(String[] userIds, String servId) throws Exception {
		//读取操作员的数据权限
		String dataRight = this.queryDataRightCon(getOptr(), DataRight.PROD_ORDER.toString());
		List<PProd> prods = pProdDao.queryCanOrderUserProd(servId,getOptr().getArea_id(),getOptr().getCounty_id(),dataRight);
		List<PProd> result = new ArrayList<PProd>();
		List<CProdDto> ordered = queryByUserIds(userIds);
		String needBaseProd = queryTemplateConfig(Template.NEED_BASE_PROD.toString());
		
		Map<String,List<CProdDto>> map = null;
		if(userIds.length > 1){
			//如果该县市需要基本包
			if(SystemConstants.BOOLEAN_TRUE.equals(needBaseProd)){
				map = CollectionHelper.converToMap(ordered, "user_id");
				//不相等说明有用户没订购基本包
				if(map.size() != 0 && map.size() != userIds.length){
					throw new ComponentException("有用户没订购基本包，无法批量订购");
				}
			}
		}
		
		
		for (PProd p : prods) {
			boolean isfind = false;
			for (CProd up : ordered) {
				if (p.getProd_id().equals(up.getProd_id())) {
					isfind = true;
					break;
				}
			}
			//如果该县市需要基本包，且用户没有订购任何产品，则至返回基本包产品
			if (SystemConstants.BOOLEAN_TRUE.equals(needBaseProd)){
				//没订购产品，或订购了一个非基本包节目（按次点播）
				if (ordered == null || ordered.size()==0
						|| (ordered.size() == 1 && ordered.get(0).getIs_base().equals(SystemConstants.BOOLEAN_FALSE))){
					if (p.getIs_base().equals(SystemConstants.BOOLEAN_FALSE))
						isfind = true;
				}
			}
			if (!isfind){
				result.add(p);
			}
		}
		return result;
	}

	/**
	 * @param custId
	 * @return
	 */
	public List<PProd> queryCanOrderPkgs(String custId) throws Exception{
		//读取操作员的数据权限
		String dataRight = this.queryDataRightCon(getOptr(), DataRight.PROD_ORDER.toString());
		List<PProd> prods = pProdDao.queryCanOrderPkg(getOptr().getCounty_id(),dataRight);
		List<PProd> result = new ArrayList<PProd>();
		List<CProdDto> ordered = cProdDao.queryPkgByCustId(custId,getOptr().getCounty_id());
		for (PProd p : prods) {
			boolean isfind = false;
			for (CProdDto up : ordered) {
				if (p.getProd_id().equals(up.getProd_id())) {
					isfind = true;
					break;
				}
			}
			if (!isfind){
				result.add(p);
			}
		}
		return result;
	}

	/**
	 * 查找客户下所有产品
	 * @param custId
	 * @return
	 */
	public List<CProdDto> queryProdByCustId(String custId) throws Exception {
		String dataRight = "";
		try {
			dataRight = this.queryDataRightCon(getOptr(), DataRight.PROD_PAUSE.toString());
		} catch (Exception e) {
		}
		List<CProdDto> prodList = cProdDao.queryProdByCustId(custId, getOptr().getCounty_id());
		if(StringHelper.isNotEmpty(dataRight)){
			for(CProdDto prod : prodList){
				//规则表达式包含要暂停的产品ID，则该产品可暂停
				if(dataRight.indexOf(prod.getProd_id()) > -1){
					prod.setIs_pause(SystemConstants.BOOLEAN_TRUE);
				}else{
					prod.setIs_pause(SystemConstants.BOOLEAN_FALSE);
				}
			}
		}
		return prodList;
	}
	
	private void setUserName(List<UserProdDto> list){
		for( UserProdDto userProd :list){
			if("DTV".equals(userProd.getUser_type())){
				if (StringHelper.isEmpty(userProd.getUser_name()))
					userProd.setUser_name(MemoryDict.getDictName(DictKey.TERMINAL_TYPE,userProd.getTerminal_type()));
			}else if("BAND".equals(userProd.getUser_type())){
				userProd.setUser_name(userProd.getLogin_name());
			}else if("ATV".equals(userProd.getUser_type())){
				if (StringHelper.isEmpty(userProd.getUser_name()))
					userProd.setUser_name("模拟终端");
			}
		}
	}
	
	public List<CProdDto> queryProdBalanceByCustId(String custId) throws Exception {
		return cProdDao.queryProdBalanceByCustId(custId, getOptr().getCounty_id());
	}
	
	public List<UserProdDto> queryUserProdToCallCenter(Map<String,Object> p) throws Exception {
		List<UserProdDto> list = cProdDao.queryUserProdToCallCenter(p, getOptr().getCounty_id());
		setUserName(list);
		return list;
	}
	
	public List<UserProdDto> queryUserProdHisToCallCenter(Map<String,Object> p) throws Exception {
		List<UserProdDto> list = cProdHisDao.queryUserProdHisToCallCenter(p, getOptr().getCounty_id());
		setUserName(list);
		return list;
	}
	
	public List<CProdDto> queryProdHisByCustId(String custId) throws Exception {
		return cProdDao.queryProdHisByCustId(custId, getOptr().getCounty_id());
	}

	/**
	 * 根据数组编号查询产品
	 * @param ids 	编号数组
	 * @param key	判断是客户编号数组"CUST"还是用户编号数组"USER"
	 * @return
	 * @throws Exception
	 */
	public List<CProdBacthDto> queryProdByIds(String[] ids,String key,String prodId) throws Exception {
		List<CProdBacthDto> list = cProdDao.queryProdByIds(ids, getOptr().getCounty_id(),key,prodId);
		Date now = DateHelper.strToDate(DateHelper.formatNow());
		for(CProdBacthDto dto : list){
			if(dto.getIs_invalid_tariff().equals(SystemConstants.BOOLEAN_TRUE)
					||dto.getIs_zero_tariff().equals(SystemConstants.BOOLEAN_TRUE)
					||dto.getTariff_rent() == 0){
				dto.setEff_date(now);
				dto.setIsEdit(false);
			}
			//next_bill_date已作废
//			else if(dto.getNext_bill_date()!=null){
			else if(dto.getBilling_cycle()>1){
				dto.setEff_date(DateHelper.addDate(dto.getInvalid_date(), 1));
				dto.setIsEdit(false);
			}else if(DateHelper.strToDate(DateHelper.dateToStr(dto.getOrder_date())).compareTo(now)==0){
				dto.setEff_date(now);
				dto.setIsEdit(false);
			}else{
				Date nextnow = DateHelper.strToDate(DateHelper.formatNow());
				nextnow.setDate(1);
				nextnow.setMonth(nextnow.getMonth()+1);
				dto.setEff_date(nextnow);
				dto.setIsEdit(true);
			}
			
		}
		return list;
	}
	
	public List<CProd> queryBaseProdByIds(String[] ids,String key) throws Exception {
		return cProdDao.queryBaseProdByIds(ids,key);
	}
	
	/**
	 * 查询用户产品
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<CProdDto> queryByUserId(String userId) throws Exception{
		return cProdDao.queryUserProd(userId, getOptr().getCounty_id());
	}
	public List<CProdDto> queryByCustId(String custId) throws Exception{
		return cProdDao.queryProdByCustId(custId, getOptr().getCounty_id());
	}
	
	/**
	 * 查询用户产品,包括套餐子产品
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<CProdDto> queryAllProdsByUserId(String userId) throws Exception{
		return cProdDao.queryUserAllProds(userId, getOptr().getCounty_id());
	}
	
	/**
	 * 查询用户产品
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<CProdDto> queryByUserIds(String[] userIds) throws Exception{
		return cProdDao.queryUserProdByUserIds(userIds, getOptr().getCounty_id());
	}

	/**
	 * 查找用户数字电视基本产品
	 * @param user_id
	 * @return
	 */
	public List<CProdDto> queryUserDtvBaseProd(String userId)  throws Exception{
		return cProdDao.queryUserDtvBaseProd(userId, getOptr().getCounty_id());
	}

	/**
	 * 查找客户套餐
	 * @param custId
	 * @return
	 */
	public List<CProdDto> queryCustPackage(String custId)  throws Exception{
		String dataRight = "";
		try {
			dataRight = this.queryDataRightCon(getOptr(), DataRight.PROD_PAUSE.toString());
		} catch (Exception e) {
		}
		List<CProdDto> prodList = cProdDao.queryPkgByCustId(custId, getOptr().getCounty_id());
		if(StringHelper.isNotEmpty(dataRight)){
			for(CProdDto prod : prodList){
				//规则表达式包含要暂停的产品ID，则该产品可暂停
				if(dataRight.indexOf(prod.getProd_id()) > -1){
					prod.setIs_pause(SystemConstants.BOOLEAN_TRUE);
				}else{
					prod.setIs_pause(SystemConstants.BOOLEAN_FALSE);
				}
			}
		}
		return prodList;
	}


	/**
	 * 查找可以加入套餐的客户产品
	 * @param custId
	 * @param custType
	 * @param pkgId
	 * @return
	 */
	public List<CustProdDto> queryCustProdForPkg(String custId,
			String custType, String pkgId,String pkgTarrifId) throws Exception{
		List<CustProdDto> prodList= null;
		if (custType.equals(SystemConstants.CUST_TYPE_UNIT)){
			prodList = cProdDao.queryUnitProdForPkg(custId,pkgId,pkgTarrifId, getOptr().getCounty_id());
		} else {
			prodList = cProdDao.queryCustProdForPkg(custId,pkgId,pkgTarrifId, getOptr().getCounty_id());
		}
		return prodList;
	}

	/**
	 * @param custId
	 * @param pkgId
	 * @return
	 */
	public List<CustProdDto> queryProdsOfPkg(String custId, String pkgId) throws Exception {
		return cProdDao.queryProdsOfPkg(custId,pkgId, getOptr().getCounty_id());
	}

	/**
	 * 根据sn获取产品信息
	 * @param prodSn
	 * @return
	 * @throws Exception
	 */
	public CProd queryByProdSn(String prodSn) throws Exception{
		return cProdDao.findByKey(prodSn);
	}
	/**
	 * 根据套餐sn获取产品信息
	 * @param prodSn
	 * @return
	 * @throws Exception
	 */
	public List<CProd> queryByPkgSn(String pkgSn) throws Exception{
		return cProdDao.queryByPkgSn(pkgSn,getOptr().getCounty_id());
	}



	public CProd queryByAcctItem(String acctId,String acctItemId) throws Exception{
		List<CProd> prodList = cProdDao.queryByAcctItem(acctId,acctItemId, getOptr().getCounty_id());
		if (prodList != null && prodList.size()>0)
			return prodList.get(0);
		else
			return null;
	}
	
	/**
	 * @param userId
	 * @param acctitem_id
	 * @return
	 */
	public CProd queryByProdId(String userId, String acctItemId) throws JDBCException{
		List<CProd> prodList = cProdDao.queryByProdId(userId,acctItemId);
		if (prodList != null && prodList.size()>0)
			return prodList.get(0);
		else
			return null;
	}

	/**
	 * @param doneCode
	 * @return
	 */
	public List<CProd> queryProdByDoneCode(Integer doneCode) throws Exception{
		return cProdDao.queryByDoneCode(doneCode,getOptr().getCounty_id());
	}
	/**
	 * 查询用户产品的的资源信息
	 * @param prodSn
	 * @return
	 * @throws JDBCException
	 */
	public List<PRes> queryResByProdSn(String prodSn) throws Exception {
		List<PRes> presList = new ArrayList<PRes>();
		CProd cProd = cProdDao.findByKey(prodSn);
		if (cProd == null)
			return presList;
		//动态资源
		List<PRes> pdycresList =new ArrayList<PRes>();
		if(!SystemConstants.PROD_TYPE_BASE.equals(cProd.getProd_type())){
			List<CProd> prods = cProdDao.queryByPkgSn(cProd.getProd_sn(), cProd.getCounty_id());
			
			for(CProd prod : prods){
				PProd p = pProdDao.queryProdById(prod.getProd_id());
				List<PRes> tempPresList = new ArrayList<PRes>();
				//静态资源
				if(p.getServ_id().equals("DTV"))
				{
					//数字电视静态资源
					tempPresList = pResDao.queryDTVControlIdByProdSn(prod.getProd_id(),prod.getProd_sn(),prod.getCounty_id());	
					for(PRes pRes : tempPresList){
						presList.add(pRes);
					}
					//数字电视动态资源
					pdycresList = pResDao.queryControlIdByUserProdSn(prod.getProd_sn(),getOptr().getCounty_id());
					for(PRes pRes : pdycresList){
						presList.add(pRes);
					}
				}
				else
				{	
					//vod,宽带静态资源			
					tempPresList = pResDao.queryITVBANDControlIdByProdId(prod.getProd_id(),prod.getCounty_id());
					for(PRes pRes : tempPresList){
						presList.add(pRes);
					}
					//vod,宽带动态资源
					pdycresList = pResDao.queryITVBANDControlIdByUserProdSn(prod.getProd_sn(),getOptr().getCounty_id());
					for(PRes pRes : pdycresList){
						presList.add(pRes);
					}
				}
				 
			}
		}else{
			PProd p = pProdDao.queryProdById(cProd.getProd_id());				
			//静态资源
			if(p.getServ_id().equals("DTV")){
				//数字电视静态资源
				presList = pResDao.queryDTVControlIdByProdSn(cProd.getProd_id(),cProd.getProd_sn(),cProd.getCounty_id());	
				//数字电视动态资源
				pdycresList = pResDao.queryControlIdByUserProdSn(prodSn,getOptr().getCounty_id());
				for(PRes pRes : pdycresList){
					presList.add(pRes);
				}
			}
			else
			{
				//vod,宽带静态资源
				presList = pResDao.queryITVBANDControlIdByProdId(cProd.getProd_id(),cProd.getCounty_id());
				//vod,宽带动态资源
				pdycresList = pResDao.queryITVBANDControlIdByUserProdSn(prodSn,getOptr().getCounty_id());
				for(PRes pRes : pdycresList){
					presList.add(pRes);
				}
			}	
		}
		
		return presList;
	}
	
	public ProdResDto queryDynResByProdSn(String prodSn) throws Exception {
		ProdResDto prodRes = new ProdResDto();
		//该订购的产品选择的资源
		List<String> prodRscList = cProdRscDao.queryUserProdRes(prodSn);
		
		CProd prod = cProdDao.findByKey(prodSn);
		List<ResGroupDto> allResGroup = pProdDao.queryResGroup(prod.getProd_id());
		List<ResGroupDto> oldResGroupList = new ArrayList<ResGroupDto>();
		for(ResGroupDto resGroup : allResGroup){
			List<PRes> resList = pProdDao.queryResByGroupId(resGroup.getGroup_id());
			resGroup.setResList(resList);
			
			ResGroupDto oldResGroup = new ResGroupDto();
			BeanUtils.copyProperties(resGroup, oldResGroup);
			List<PRes> oldResList = new ArrayList<PRes>();
			oldResGroup.setResList(oldResList);
			for(PRes res : resList){
				if(prodRscList.contains(res.getRes_id())){
					oldResList.add(res);
				}
			}
			oldResGroupList.add(oldResGroup);
		}
		prodRes.setDynamicResList(allResGroup);
		prodRes.setOldDynamicResList(oldResGroupList);
		return prodRes;
	}

	/**
	 * 查询当前产品的状态异动信息
	 * @param prodId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public Pager<CProdPropChange> queryChangeByProd(String prodId, Integer start, Integer limit) throws Exception{
		return cProdDao.queryChangeByProd(prodId,getOptr().getCounty_id(),start,limit);
	}
	
	/**
	 * 查询当前产品的状态异动信息
	 * @param prodId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CProdPropChange> queryChangeByDoneCode(int doneCode) throws Exception{
		return cProdDao.queryChangeByDoneCode(doneCode,getOptr().getCounty_id());
	}

	/**
	 * 查询当前产品变更信息
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public Pager<CProdInvalidTariff> queryTariffChange(String prodId,Integer start,Integer limit) throws Exception {
		return cProdDao.queryTariffChange(prodId,start,limit);
	}

	/**
	 * 查询产品资费信息
	 *
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public PProdTariff queryProdTariffById(String tariffId) throws Exception {
		return pProdTariffDao.findByKey(tariffId);
	}

	/**
	 * 查找产品变更前的状态
	 * @param prod_sn
	 * @return
	 */
	public String queryLastStatus(String prodSn) throws Exception {
		CProdPropChange change = cProdPropChangeDao.queryLastStatus(prodSn,
				getOptr().getCounty_id());
		if (change == null)
			return null;
		return change.getOld_value();
	}
	
	/**
	 * 保存接口订购信息
	 * @param userId
	 * @param prodId
	 * @param tariffId
	 * @param fee
	 */
	public void saveOrderInfo(String userId, String prodId, String tariffId,
			int fee) throws Exception {
		TvOrder orderInfo = new TvOrder(userId,prodId,tariffId,fee,getOptr().getCounty_id());
		tvOrderDao.save(orderInfo);
	}

	


	/**
	 * 获取用户id
	 */
	private String gUserProdId() throws Exception {
		return cProdDao.findSequence().toString();
	}

	private PRes[] getDynamicRsc(String prodId,List<UserProdRscDto> dynamicRscList){
		PRes[] rscs = null;
		List<PRes > rscList =new ArrayList<PRes>();
		if (dynamicRscList!=null){
			for (UserProdRscDto userProdRsc:dynamicRscList){
				if (prodId.equals(userProdRsc.getProdId())){
					rscList.addAll(userProdRsc.getRscList());
				}
			}
		}
		rscs = rscList.toArray(new PRes[rscList.size()]);
		return rscs;
	}
	
	/**
	 * 查询用户未排斥资源
	 * @param userId
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<PRes> queryUnRejectRes(String userId,String custId) throws Exception {
		return cRejectResDao.queryUnRejectRes(userId, custId);
	}
	
	/**
	 * 查询用户排斥资源
	 * @param userId
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<PRes> queryRejectRes(String userId,String custId) throws Exception {
		return cRejectResDao.queryRejectRes(userId, custId);
	}

	public List<String> queryUserProdRes(String prodSn) throws Exception {
		return cProdRscDao.queryUserProdRes(prodSn);
	}
	public PProd queryByProdId(String prodId) throws Exception{
		return pProdDao.findByKey(prodId);
	}
	
	public List<String> queryResByUserId(String userId,String prodId) throws Exception {
		return cProdDao.queryResByUserId(userId, prodId, getOptr().getCounty_id());
	}
	
	/**
	 * 删除产品异动
	 * @param prodSn
	 * @param doneCode
	 * @throws Exception
	 */
	public void removePropChange(String prodSn, Integer doneCode) throws Exception{
		cProdPropChangeDao.removeByDoneCode( prodSn, doneCode,getOptr().getCounty_id());

	}
	
	/**
	 * 查找套餐缴费生成的零资费产品异动
	 * @param doneCode
	 * @return
	 */
	public List<CProdPropChange> queryPromPayChange(Integer doneCode,String countyId) throws Exception{
		return cProdPropChangeDao.queryPromPayChange(doneCode,countyId);
	}
	
	public CProdRscDao getCUserProdRscDao() {
		return cProdRscDao;
	}
	public void setCUserProdRscDao(CProdRscDao userProdRscDao) {
		cProdRscDao = userProdRscDao;
	}
	public void setCProdResourceAcctDao(CProdResourceAcctDao prodResourceAcctDao) {
		cProdResourceAcctDao = prodResourceAcctDao;
	}
	/**
	 * @param resDao the pResDao to set
	 */
	public void setPResDao(PResDao resDao) {
		pResDao = resDao;
	}
	public void setPProdTariffDisctDao(PProdTariffDisctDao prodTariffDisctDao) {
		pProdTariffDisctDao = prodTariffDisctDao;
	}

	public void setCProdHisDao(CProdHisDao prodHisDao) {
		cProdHisDao = prodHisDao;
	}

	/**
	 * @param prodRscDao the cProdRscDao to set
	 */
	public void setCProdRscDao(CProdRscDao prodRscDao) {
		cProdRscDao = prodRscDao;
	}

	/**
	 * 查询属于单位的客户产品
	 * 
	 * @param custId
	 * @param unitIds
	 */
	public List<CProd> queryUnitProdByUnitIds(String custId, String[] unitIds)
			throws JDBCException {
		return cProdDao.queryUnitProdByUnitIds(custId, unitIds);
	}
	
	/**
	 * 修改产品的预开通时间.
	 * @param prodSn
	 * @param countyId
	 * @param newPreOpenDate 新的预开通时间.
	 */
	public CProd updateProdPreOpenDate(Integer doneCode ,String prodSn, String countyId, Date newPreOpenDate, Date feeDate) throws Exception{
		List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
		Date now  = DateHelper.today();
		
		boolean newDateIsToday = now.equals(newPreOpenDate);//修改后的预开通日期是今天?
		
		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put("prod_sn", prodSn);
		params.put("county_id", countyId);
		List<CProd> prods = cProdDao.findByMap(params);
		if(prods ==null || prods.size() != 1){
			throw new ComponentException("未能找到需要修改预开通的产品 ");
		}
		CProd cprod = prods.get(0);
		
		Date oldFeeDate = cprod.getBillinfo_eff_date();
		
		CUser user = cUserDao.findByKey(cprod.getUser_id());
		
		List<JProdPreopen> jobs = jProdPreopenDao.findByMap(params);
		
		if(jobs ==null || jobs.size() != 1){
			throw new ComponentException("未能找到需要修改预开通的排程 ");
		}
		JProdPreopen job = jobs.get(0);
		
		if( oldFeeDate.getTime() - feeDate.getTime() != 0 ){
			
			String stopByInvalidDate = cprod.getStop_by_invalid_date();//
			if(stopByInvalidDate.equals("T") ){//0资费或者包多月的,包多月的产品和 0资费产品 涉及到到期日修改，根据 开始计费时间变化
				//预开通日期变了,到期日跟着变化
				Date oldInvalidDate = cprod.getInvalid_date();
				
				CProdPropChange invalidDateChange = new CProdPropChange();
				invalidDateChange.setColumn_name("invalid_date");
				invalidDateChange.setOld_value(DateHelper.format(oldInvalidDate, DateHelper.FORMAT_YMD));//先去老的值
				//缴费等费用业务后，计算新旧生效日期差额，生成新到期日
				int days = DateHelper.getDiffDays(cprod.getPre_open_time(), newPreOpenDate);
				Date newInvalidDate = DateHelper.addDate(oldInvalidDate, days);
				
				cprod.setInvalid_date(newInvalidDate);//填充新的值
				invalidDateChange.setNew_value(DateHelper.dateToStr(newInvalidDate));
				changeList.add(invalidDateChange);
				if(cprod.getBillinfo_eff_date().getTime() - feeDate.getTime() !=0){
					//包多月产品修改已出账账单账期
					int changemonths= DateHelper.compareToMonthByDate(cprod.getBillinfo_eff_date(), feeDate);
					if(changemonths!=0){
						bBillDao.updateMuchProdBillByChangeBillinfoEffDate(prodSn, 
								DateHelper.format(cprod.getBillinfo_eff_date(),DateHelper.FORMAT_YM), countyId, changemonths);
					}
				}
				
				//next_bill_date已作废
				/*Date oldNextBillDate = cprod.getNext_bill_date();
				//年包
				if(oldNextBillDate != null && oldNextBillDate.getTime() - feeDate.getTime() != 0){
					CProdPropChange nextBillDateChange = new CProdPropChange();
					nextBillDateChange.setColumn_name("next_bill_date");
					nextBillDateChange.setOld_value(DateHelper.format(oldNextBillDate, DateHelper.FORMAT_YMD));//先去老的值
					cprod.setNext_bill_date(feeDate);//填充新的值
					nextBillDateChange.setNew_value(DateHelper.format(feeDate, DateHelper.FORMAT_YMD));
					changeList.add(nextBillDateChange);
				}*/
			}
			
			CProdPropChange feeDateChange = new CProdPropChange();
			feeDateChange.setColumn_name("billinfo_eff_date");
			feeDateChange.setOld_value(DateHelper.format(oldFeeDate, DateHelper.FORMAT_YMD));//先去老的值
			cprod.setBillinfo_eff_date(feeDate);//填充新的值
			feeDateChange.setNew_value(DateHelper.format(feeDate, DateHelper.FORMAT_YMD));
			changeList.add(feeDateChange);
			
		}
		
		CProdPropChange propChange = new CProdPropChange();
		propChange.setColumn_name("pre_open_time");
		propChange.setOld_value(DateHelper.format(cprod.getPre_open_time(), DateHelper.FORMAT_YMD));//先去老的值
		cprod.setPre_open_time(newPreOpenDate);//填充新的值
		propChange.setNew_value(DateHelper.format(newPreOpenDate, DateHelper.FORMAT_YMD));
		changeList.add(propChange);
		
		if(newDateIsToday){//如果信的预开通日期是今天,设置产品状态为 ACTIVE，删除 J_PROD_PREOPEN,添加历史记录
			CProdPropChange statusChange = new CProdPropChange();
			statusChange.setColumn_name("status");
			statusChange.setOld_value(StatusConstants.PREAUTHOR);
			cprod.setStatus(StatusConstants.ACTIVE);
			statusChange.setNew_value(StatusConstants.ACTIVE);
			changeList.add(statusChange);
			
			CProdPropChange preChange = new CProdPropChange();
			preChange.setColumn_name("pre_open_time");
			preChange.setOld_value(DateHelper.dateToStr(cprod.getPre_open_time()));
			cprod.setPre_open_time(null);
			preChange.setNew_value("");
			changeList.add(preChange);
			
			JProdPreopenHis his = new JProdPreopenHis();
			BeanUtils.copyProperties(job, his);
			his.setIs_success("T");
			his.setRemark("修改预开通时间到今日 : " + DateHelper.format(newPreOpenDate, DateHelper.FORMAT_YMD) + ",删除预开通job,插入指令job");
			
			jProdPreopenDao.remove(job.getJob_id());
			jProdPreopenHisDao.save(his);
			
			//发送指令
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD,
					user.getCust_id(), user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), prodSn,cprod.getProd_id());
			jobComponent.createAcctModeCalJob(doneCode, user.getCust_id());
		}else{
			job.setPre_open_time(newPreOpenDate);
			jProdPreopenDao.update(job);
		}
		
		editProd(doneCode, prodSn, changeList);
		
		//子产品和套餐保持一致
		CProd parentProd = cProdDao.findByKey(prodSn);
		List<CProdDto> childProds = cProdDao.queryChildProdByPkgsn(prodSn, getOptr().getCounty_id());
		for(CProdDto prodDto : childProds){
			CProd childProd = new CProd();
			BeanUtils.copyProperties(prodDto, childProd);
			childProd.setPre_open_time(parentProd.getPre_open_time());
			childProd.setStatus(parentProd.getStatus());
			childProd.setInvalid_date(parentProd.getInvalid_date());
			childProd.setBillinfo_eff_date(parentProd.getBillinfo_eff_date());
			//next_bill_date已作废
//			if(childProd.getNext_bill_date() != null)
//				childProd.setNext_bill_date(parentProd.getNext_bill_date());
			cProdDao.update(childProd);
		}
		return parentProd;
	}
	

	/**
	 * 修改产品公用账目使用类型.
	 * @param doneCode
	 * @param prodSn
	 * @param countyId
	 * @param publicAcctitemType
	 */
	public void updatePublicAcctItemType(Integer doneCode, String prodSn,CCust cust, String publicAcctitemType) throws Exception{
		List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put("prod_sn", prodSn);
		params.put("county_id", cust.getCounty_id());
		List<CProd> prods = cProdDao.findByMap(params);
		if(prods ==null || prods.size() != 1){
			throw new ComponentException("未能找到需要修改 产品公用账目使用类型 的产品 ");
		}
		CProd prod = prods.get(0);
		String oldValue = prod.getPublic_acctitem_type();
		
		CProdPropChange feeDateChange = new CProdPropChange();
		feeDateChange.setColumn_name("public_acctitem_type");
		feeDateChange.setOld_value(prod.getPublic_acctitem_type());//先去老的值
		prod.setPublic_acctitem_type(publicAcctitemType);
		feeDateChange.setNew_value(publicAcctitemType);
		changeList.add(feeDateChange);
		
		editProd(doneCode, prodSn, changeList);
		
		//http://ljq.vicp.net:82/mantis/view.php?id=1263
		if(publicAcctitemType.equals("ALL") || publicAcctitemType.equals("NONE") || 
				publicAcctitemType.equals("SPEC_ONLY") || publicAcctitemType.equals("PUBLIC_ONLY") ){
			CProdPropPat cpp = new CProdPropPat();
			cpp.setArea_id(cust.getArea_id());
			cpp.setProd_sn(prodSn);
			cpp.setOld_value(oldValue);
			cpp.setNew_value(publicAcctitemType);
			cpp.setDone_code(doneCode);
			cpp.setChange_time(new Date());
			cpp.setCounty_id(cust.getCounty_id());
			cpp.setStatus(StatusConstants.ACTIVE);
			cpp.setCust_id(cust.getCust_id());
			cProdPropPatDao.save(cpp);
		}else{//系统默认,设置c_prod_prop_pat 中该产品的所有有效变更记录的状态为失效，
			CProdPropPat cpp = new CProdPropPat();
			cpp.setProd_sn(prodSn);
			String sql = "update c_prod_prop_pat set status = ? where prod_sn = ? ";
			cProdPropPatDao.executeUpdate(sql, StatusConstants.INVALID,prodSn);
		}
	}
	
	public PProdTariff queryTariffByAcctId(String acctId, String acctItemId) throws Exception {
		return this.pProdTariffDao.queryTariffByAcctId(acctId, acctItemId, getOptr().getCounty_id());
	}
	
	/**
	 * 退款时候记录套餐缴费信息
	 * @param promFeeSn
	 * @param prodSns
	 * @param doneCode
	 */
	public void savePromProdRefund(String promFeeSn, String[] prodSns,Integer doneCode,Integer refund) throws Exception {
		cPromProdRefundDao.savePromProdRefund(promFeeSn, prodSns, doneCode,refund*-1);
	}
	
	public List<CProd> queryProdByPackageProd(String custId, String packageId) throws Exception {
		return cProdDao.queryProdByPackageProd(custId, packageId);
	}

	public void setCRejectResDao(CRejectResDao rejectResDao) {
		cRejectResDao = rejectResDao;
	}

	public void setJProdNextTariffHisDao(JProdNextTariffHisDao prodNextTariffHisDao) {
		jProdNextTariffHisDao = prodNextTariffHisDao;
	}

	/**
	 * @param tvOrderDao the tvOrderDao to set
	 */
	public void setTvOrderDao(TvOrderDao tvOrderDao) {
		this.tvOrderDao = tvOrderDao;
	}

	/**
	 * @param prodSynDao the cProdSynDao to set
	 */
	public void setCProdSynDao(CProdSynDao prodSynDao) {
		cProdSynDao = prodSynDao;
	}

	public void setJProdPreopenDao(JProdPreopenDao jProdPreopenDao) {
		this.jProdPreopenDao = jProdPreopenDao;
	}

	public void setJProdPreopenHisDao(JProdPreopenHisDao jProdPreopenHisDao) {
		this.jProdPreopenHisDao = jProdPreopenHisDao;
	}

	public void setCUserDao(CUserDao cUserDao) {
		this.cUserDao = cUserDao;
	}

	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}

	public void setCPromProdRefundDao(CPromProdRefundDao promProdRefundDao) {
		cPromProdRefundDao = promProdRefundDao;
	}

	public void setCProdPropPatDao(CProdPropPatDao cProdPropPatDao) {
		this.cProdPropPatDao = cProdPropPatDao;
	}

	/**
	 * @param cust_id
	 * @return
	 */
	public CProd queryBaseProdByCustId(String cust_id)throws Exception {
		return cProdDao.queryBaseProdByCustId(cust_id, getOptr().getCounty_id());
	}
	
	public boolean isProdOpen(String status) throws Exception {
		return cProdDao.isProdOpen(status);
	}
}
