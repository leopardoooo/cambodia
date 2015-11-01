package com.ycsoft.business.component.core;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TAddress;
import com.ycsoft.beans.config.TCustColonyCfg;
import com.ycsoft.beans.config.TDeviceBuyMode;
import com.ycsoft.beans.config.TDistrict;
import com.ycsoft.beans.config.TNonresCustApproval;
import com.ycsoft.beans.config.TProvince;
import com.ycsoft.beans.config.TTabDefine;
import com.ycsoft.beans.core.acct.CAcctBank;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.common.CDoneCodeDetail;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustAddr;
import com.ycsoft.beans.core.cust.CCustAddrHis;
import com.ycsoft.beans.core.cust.CCustBonuspoint;
import com.ycsoft.beans.core.cust.CCustDevice;
import com.ycsoft.beans.core.cust.CCustDeviceBuymodeProp;
import com.ycsoft.beans.core.cust.CCustDeviceChange;
import com.ycsoft.beans.core.cust.CCustDeviceHis;
import com.ycsoft.beans.core.cust.CCustHis;
import com.ycsoft.beans.core.cust.CCustLinkman;
import com.ycsoft.beans.core.cust.CCustLinkmanHis;
import com.ycsoft.beans.core.cust.CCustPropChange;
import com.ycsoft.beans.core.cust.CCustUnitToResident;
import com.ycsoft.beans.prod.PSpkg;
import com.ycsoft.beans.system.SAgent;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.config.TAddressDao;
import com.ycsoft.business.dao.config.TCustColonyCfgDao;
import com.ycsoft.business.dao.config.TDeviceBuyModeDao;
import com.ycsoft.business.dao.config.TDistrictDao;
import com.ycsoft.business.dao.config.TNonresCustApprovalDao;
import com.ycsoft.business.dao.config.TProvinceDao;
import com.ycsoft.business.dao.core.acct.CAcctBankDao;
import com.ycsoft.business.dao.core.cust.CCustAddrDao;
import com.ycsoft.business.dao.core.cust.CCustAddrHisDao;
import com.ycsoft.business.dao.core.cust.CCustBonuspointDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.cust.CCustDeviceBuymodePropDao;
import com.ycsoft.business.dao.core.cust.CCustDeviceChangeDao;
import com.ycsoft.business.dao.core.cust.CCustDeviceDao;
import com.ycsoft.business.dao.core.cust.CCustDeviceHisDao;
import com.ycsoft.business.dao.core.cust.CCustHisDao;
import com.ycsoft.business.dao.core.cust.CCustLinkmanDao;
import com.ycsoft.business.dao.core.cust.CCustLinkmanHisDao;
import com.ycsoft.business.dao.core.cust.CCustPropChangeDao;
import com.ycsoft.business.dao.core.cust.CCustUnitToResidentDao;
import com.ycsoft.business.dao.prod.PSpkgDao;
import com.ycsoft.business.dto.config.TAddressDto;
import com.ycsoft.business.dto.core.cust.CustDeviceDto;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.dto.core.cust.CustGeneralInfo;
import com.ycsoft.business.dto.device.DeviceDto;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.BeanHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

@Component
public class CustComponent extends BaseBusiComponent {
	private CCustDao cCustDao;
	private CCustHisDao cCustHisDao;
	private CCustLinkmanDao cCustLinkmanDao;
	private CCustLinkmanHisDao cCustLinkmanHisDao;
	private CCustAddrDao cCustAddrDao;
	private CCustAddrHisDao cCustAddrHisDao;
	private CCustUnitToResidentDao cCustUnitToResidentDao;
	private CCustPropChangeDao cCustPropChangeDao;
	private CCustDeviceDao cCustDeviceDao;
	private CCustDeviceHisDao cCustDeviceHisDao;
	private CCustDeviceChangeDao cCustDeviceChangeDao;
	private TDeviceBuyModeDao tDeviceBuyModeDao;
	private CCustBonuspointDao cCustBonuspointDao;
	private TAddressDao tAddressDao;
	private CAcctBankDao cAcctBankDao;
	private TCustColonyCfgDao tCustColonyCfgDao;
	private CCustDeviceBuymodePropDao cCustDeviceBuymodePropDao;
	private TNonresCustApprovalDao tNonresCustApprovalDao;
	private TDistrictDao tDistrictDao;
	private TProvinceDao tProvinceDao;
	
	@Autowired
	private PSpkgDao pSpkgDao;
	
	/**
	 * 查询变更产权的设备销售方式
	 * @return
	 * @throws Exception
	 */
	public List<TDeviceBuyMode> queryDeviceBuyModeByOwnership(String flag) throws Exception {
		String dataRight = this.queryDataRightCon(getOptr(), DataRight.BUY_MODE.toString());
		return tDeviceBuyModeDao.queryDeviceBuyModeByOwnership(flag,dataRight);
	}

	public List<TDeviceBuyMode> queryDeviceCanFee() throws Exception {
		String dataRight = this.queryDataRightCon(getOptr(), DataRight.BUY_MODE.toString());
		return tDeviceBuyModeDao.queryDeviceCanFee(dataRight);
	}
	
	/**
	 * 创建客户
	 *
	 * @param cust 客户基本信息
	 * @param linkMan 联系人
	 * @param custCode 
	 * @param resident 居民信息
	 * @throws Exception
	 */
	public String createCust(CCust cust,CCustLinkman linkMan, String custCode) throws Exception{

		//检查协议号能否被客户使用
		this.checkCustUseSpkgSn(cust, cust.getSpkg_sn());
		
		//保存客户基本信息
		cust.setCust_id(gCustId());
		cust.setCust_no(gCustNoByAddr(cust.getAddr_id(),custCode));

		
		if(StringHelper.isEmpty(cust.getStatus())){
			cust.setStatus(StatusConstants.ACTIVE);
		}
		
		cust.setIs_black(SystemConstants.BOOLEAN_FALSE);
		if(StringHelper.isEmpty(cust.getCust_class())){
			cust.setCust_class(SystemConstants.CUST_CLASS_YBKH);
		}
		cust.setArea_id(getOptr().getArea_id());
		cust.setCounty_id(getOptr().getCounty_id());
		cust.setDept_id(getOptr().getDept_id());
		cust.setOptr_id(getOptr().getOptr_id());		
		cCustDao.save(cust);

		//保存联系人信息
		linkMan.setCust_id(cust.getCust_id());
		linkMan.setArea_id(getOptr().getArea_id());
		linkMan.setCounty_id(getOptr().getCounty_id());
		cCustLinkmanDao.save(linkMan);
		//保存客户地址详细信息
		CCustAddr custAddr = new CCustAddr();
		BeanUtils.copyProperties(cust, custAddr);
		cCustAddrDao.save(custAddr);
		//同步客户开户数量限制
//		addCustColony(cust.getCust_id());
		
		return cust.getCust_id();
	}

	/**
	 * 检查协议号能否被使用
	 * @param cust
	 * @param skkg_sn
	 * @throws Exception
	 */
	public void checkCustUseSpkgSn(CCust cust,String skkg_sn) throws Exception{
		if(StringHelper.isNotEmpty(skkg_sn)){
			PSpkg spkg = pSpkgDao.querySpkgBySn(skkg_sn);
			if(spkg != null){
				if(!spkg.getStatus().equals(StatusConstants.IDLE) || cCustDao.queryBySpkgSn(spkg.getSpkg_sn()) != null)
					throw new ComponentException(ErrorCode.SpkgIsUsed);
				spkg.setApply_optr_id(getOptr().getOptr_id());
				spkg.setApply_date(new Date());
				spkg.setStatus(StatusConstants.UNCONFIRM);
				pSpkgDao.update(spkg);
			}else{
				throw new ComponentException(ErrorCode.SpkgIsError);
			}
		}
	}
	
	public void checkCustSpkgSnConfirm(String custId,String skkg_sn) throws Exception{
		PSpkg spkg = pSpkgDao.querySpkgBySn(skkg_sn);
		if(spkg == null||!spkg.getStatus().equals(StatusConstants.CONFIRM)){
			throw new ComponentException(ErrorCode.SpkgHasNotCONFIRM);
		}
		CCust cust=cCustDao.findByKey(custId);
		if(cust.getSpkg_sn()==null||!cust.getSpkg_sn().equals(skkg_sn)){
			throw new ComponentException(ErrorCode.SpkgIsNotTrueCust);
		}
	}
	/**
	 * 修改客户信息
	 * @param doneCode	流水号
	 * @param custId	客户编号
	 * @param propChangeList	客户变化信息
	 * @throws Exception
	 */
	public void editCust(Integer doneCode,String custId,List<CCustPropChange> propChangeList) throws Exception{
		if(propChangeList == null || propChangeList.size() == 0) return ;
		CCust cust = new CCust();
		CCustLinkman linkMan = new CCustLinkman();
		cust.setCust_id(custId);
		linkMan.setCust_id(custId);
		
		Map<String, TTabDefine> tabDefine = queryTableDefine("CCUST");
		
		for (CCustPropChange change:propChangeList){
			//验证协议号是否使用
			if(change.getColumn_name().equals("spkg_sn")){
				this.checkCustUseSpkgSn(cCustDao.findByKey(custId), change.getNew_value());
			}
			
			if(change.getColumn_name().equals("cust_count")){
				int custCount = Integer.parseInt(change.getNew_value());
				int count = cCustUnitToResidentDao.getMnCustCountByCustId(custId);//现已有数量
				if(custCount < count){
					throw new ComponentException("该模拟大客户已有 "+count+" 个客户");
				}
			}
			BeanHelper.setPropertyString(cust, change.getColumn_name(), change
					.getNew_value());
			BeanHelper.setPropertyString(linkMan, change.getColumn_name(),
					change.getNew_value());
			change.setCust_id(custId);
			change.setDone_code(doneCode);
			change.setArea_id(getOptr().getArea_id());
			change.setCounty_id(getOptr().getCounty_id());
			change.setColumn_name_text(tabDefine.get(change.getColumn_name()).getComments());
		}
		//修改客户信息
		cCustDao.update(cust);
		cCustLinkmanDao.update(linkMan);
		CCustAddr custaddr = new CCustAddr();
		BeanUtils.copyProperties(cust, custaddr);
		cCustAddrDao.update(custaddr);
		//同步客户群体数量限制
		changeCustColony(cust.getCust_id(),propChangeList);

		//保存客户异动信息
		cCustPropChangeDao.save(propChangeList.toArray(new CCustPropChange[propChangeList.size()]));
	}

	public void addCustColony(String custId) throws Exception{
		CCust cust = cCustDao.findByKey(custId);
		String year = String.valueOf(DateHelper.getCurrYear());
		TCustColonyCfg cfg = tCustColonyCfgDao.getCustCfg(year, cust.getCust_colony(),cust.getCust_class(), cust.getCounty_id());
		if(cfg != null){
			throw new ComponentException(cfg.getYear_date()+"年份的["+cfg.getCust_colony_text()+"]["+cfg.getCust_class_text()+"]的限额:["+cfg.getCust_num()+"]已满!");
		}else{
			tCustColonyCfgDao.add(year,true,true, cust.getCust_colony(),cust.getCust_class(), cust.getCounty_id());
		}
	}
	
	public void removeCustColony(String custId) throws Exception{
		CCust cust = cCustDao.findByKey(custId);
		String year = String.valueOf(DateHelper.getCurrYear(cust.getOpen_time()));
		CCustPropChange change = cCustPropChangeDao.queryPropByCustIdAndColumn(cust.getCust_id(),cust.getCounty_id(),"cust_colony");
		CCustPropChange changeClass = cCustPropChangeDao.queryPropByCustIdAndColumn(cust.getCust_id(),cust.getCounty_id(),"cust_class");
		if(change!=null){
			year = String.valueOf(DateHelper.getCurrYear(change.getChange_time()));
			if(changeClass!=null && changeClass.getChange_time().compareTo(change.getChange_time())>=0){
				year = String.valueOf(DateHelper.getCurrYear(changeClass.getChange_time()));
			}
		}else if(changeClass!=null){
			year = String.valueOf(DateHelper.getCurrYear(changeClass.getChange_time()));
		}
		List<TCustColonyCfg> cfgList = tCustColonyCfgDao.queryCfg(year, true,true,cust.getCust_colony(),cust.getCust_class(), cust.getCounty_id());
		
		for(TCustColonyCfg list : cfgList){
			//(开户时间或者异动时间 )大于等于客户群体开户数量限制配置创建时间的，已使用量减1
			if(cust.getOpen_time().compareTo(list.getCreate_time())>=0 ||
					(change != null && cust.getCust_colony().equals(change.getNew_value()) && 
							change.getChange_time().compareTo(list.getCreate_time())>=0) ||
							(changeClass != null && cust.getCust_class().equals(changeClass.getNew_value()) && 
									changeClass.getChange_time().compareTo(list.getCreate_time())>=0)){
				tCustColonyCfgDao.removeNum(year, list.getCust_colony(), list.getCust_class(),cust.getCounty_id());
			}
			
		}

	}
	
	
	/**
	 * 如果修改客户的客户群体或客户优惠类型，客户开户限制相应变更
	 * @param custId
	 * @param propChangeList
	 * @throws Exception
	 */
	public void changeCustColony(String custId,List<CCustPropChange> propChangeList) throws Exception{
		boolean isChange = false;
		boolean is_colony = false;
		boolean is_class = false;
		CCust cust = cCustDao.findByKey(custId);
		String cColony = cust.getCust_colony();
		String cClass = cust.getCust_class();
		for (CCustPropChange pChange:propChangeList){
			if(pChange.getColumn_name().equals("cust_colony") ){
				isChange = true;
				is_colony = true;
				cColony = pChange.getOld_value();
			}
			if(pChange.getColumn_name().equals("cust_class")){
				isChange = true;
				is_class = true;
				cClass = pChange.getOld_value();
			}
		}
		
		if(isChange){
			String year = String.valueOf(DateHelper.getCurrYear(cust.getOpen_time()));
			CCustPropChange change = cCustPropChangeDao.queryPropByCustIdAndColumn(cust.getCust_id(), cust.getCounty_id(),"cust_colony");
			CCustPropChange changeClass = cCustPropChangeDao.queryPropByCustIdAndColumn(cust.getCust_id(),cust.getCounty_id(),"cust_class");
			if(change!=null){
				year = String.valueOf(DateHelper.getCurrYear(change.getChange_time()));
				if(changeClass!=null && changeClass.getChange_time().compareTo(change.getChange_time())>=0){
					year = String.valueOf(DateHelper.getCurrYear(changeClass.getChange_time()));
				}
			}else if(changeClass!=null){
				year = String.valueOf(DateHelper.getCurrYear(changeClass.getChange_time()));
			}
			
			//变更前的客户群体今年是否有配置限制
			List<TCustColonyCfg> oldCfgList = tCustColonyCfgDao.queryCfg(year, is_colony,is_class,cColony,cClass, cust.getCounty_id());
			for(TCustColonyCfg list : oldCfgList){
				//(开户时间或者异动时间 )大于等于客户群体开户数量限制配置创建时间的，已使用量减1
				if(cust.getOpen_time().compareTo(list.getCreate_time())>=0 || 
						(change != null && list.getCust_colony().equals(change.getNew_value()) && 
								change.getChange_time().compareTo(list.getCreate_time())>=0 )||
								(changeClass != null && list.getCust_class().equals(changeClass.getNew_value()) && 
										changeClass.getChange_time().compareTo(list.getCreate_time())>=0 )	
								){
					tCustColonyCfgDao.removeNum(year, list.getCust_colony(),list.getCust_class(), cust.getCounty_id());
				}
			}
			

			List<TCustColonyCfg> cfgList = tCustColonyCfgDao.queryCfg(year,is_colony,is_class, cust.getCust_colony(),cust.getCust_class(), cust.getCounty_id());
			boolean userKey = false;
			boolean custKey = false;
			TCustColonyCfg cfg = new TCustColonyCfg();
			for(TCustColonyCfg dto:cfgList){
				if(dto.getCust_num()>0 && dto.getUse_num()>=dto.getCust_num()){
					custKey = true;
					cfg = dto;
					break;
				}
				int num = tCustColonyCfgDao.queryUserNum(year, dto.getCust_colony(), dto.getCust_class(), cust.getCounty_id());
				if(dto.getUser_num() == 0 || dto.getUser_num()+1> num){
					continue;
				}else{
					userKey = true;
					cfg = dto;
					break;
				}
			}
			if(userKey){
				throw new ComponentException(year+"年份的["+cfg.getCust_colony_text()+"]["+cfg.getCust_class_text()+"]的用户开户限额:["+cfg.getUser_num()+"]已满!");
			}
			if(custKey){
				throw new ComponentException(cfg.getYear_date()+"年份的["+cfg.getCust_colony_text()+"]["+cfg.getCust_class_text()+"]的客户开户限额:["+cfg.getCust_num()+"]已满!");
			}else{
				tCustColonyCfgDao.add(year,is_colony,is_class, cust.getCust_colony(),cust.getCust_class(), cust.getCounty_id());
			}
		}
	}
	
	
	/**
	 * 保存单位客户和个人客户的关系
	 * @param unitToResident
	 * @throws Exception
	 */
	public void custJoinUnit(String unitId,String custId) throws Exception {
		CCustUnitToResident unitToResident = new CCustUnitToResident();
		unitToResident.setUnit_cust_id(unitId);
		unitToResident.setResident_cust_id(custId);
		cCustUnitToResidentDao.save(unitToResident);
	}
	
	public void saveBankStop(String custId, int doneCode) throws Exception {
		CAcctBank acctbank = cAcctBankDao.findByCustId(custId);
		if (acctbank == null || acctbank.getStatus().equals(StatusConstants.INVALID))
			throw new ComponentException("客户未和银行签约或已经取消！");
		if(acctbank.getStatus().equals(StatusConstants.STOP))
			throw new ComponentException("银行卡扣已暂停，不能暂停！");
		
		SOptr optr = getOptr();
		CCustPropChange cpc = new CCustPropChange();
		cpc.setArea_id(optr.getArea_id());
		cpc.setCounty_id(optr.getCounty_id());
		cpc.setCust_id(custId);
		cpc.setColumn_name("bank_status");
		cpc.setOld_value(StatusConstants.ACTIVE);
		cpc.setNew_value(StatusConstants.STOP);
		cpc.setDone_code(doneCode);
		
		List<CCustPropChange> cpcList = new ArrayList<CCustPropChange>();
		cpcList.add(cpc);
		cCustPropChangeDao.save(cpcList.toArray(new CCustPropChange[cpcList.size()]));
		cAcctBankDao.updateBank(custId, StatusConstants.STOP);
	}
	
	public void saveBankResume(String custId, int doneCode) throws Exception {
		CAcctBank acctbank = cAcctBankDao.findByCustId(custId);
		if (acctbank == null || acctbank.getStatus().equals(StatusConstants.INVALID))
			throw new ComponentException("客户未和银行签约或已经取消！");
		if(acctbank.getStatus().equals(StatusConstants.ACTIVE))
			throw new ComponentException("银行卡扣已正常，不能恢复！");

		SOptr optr = getOptr();
		CCustPropChange cpc = new CCustPropChange();
		cpc.setArea_id(optr.getArea_id());
		cpc.setCounty_id(optr.getCounty_id());
		cpc.setCust_id(custId);
		cpc.setColumn_name("bank_status");
		cpc.setOld_value(StatusConstants.STOP);
		cpc.setNew_value(StatusConstants.ACTIVE);
		cpc.setDone_code(doneCode);
		
		List<CCustPropChange> cpcList = new ArrayList<CCustPropChange>();
		cpcList.add(cpc);
		cCustPropChangeDao.save(cpcList.toArray(new CCustPropChange[cpcList.size()]));
		cAcctBankDao.updateBank(custId, StatusConstants.ACTIVE);
	}
	
	/**
	 * 删除客户
	 * @param doneCode
	 * @param cust
	 * @param linkMan
	 * @param transfer 是否是客户迁移,根据这个改变客户状态.其他跟之前一样.
	 * @return
	 * @throws Exception
	 */
	public void removeCustWithHis(Integer doneCode,CCust cust,CCustLinkman linkMan,boolean transfer) throws Exception{
		CCustHis custHis = new CCustHis();
		CCustLinkmanHis custLimkManHis = new CCustLinkmanHis();
		CCustAddrHis custAddrHis = new CCustAddrHis();

		PropertyUtils.copyProperties(custHis,cust);
		custHis.setStatus(StatusConstants.INVALID);
		if(transfer){
			if(custHis.getStatus().equals(StatusConstants.RELOCATE)){
				custHis.setStatus(StatusConstants.RELOCATE_LOGOFF);
			}else{
				custHis.setStatus(StatusConstants.TRANSFER_LOGOFF);
			}
		}
		custHis.setDone_code(doneCode);
		cCustHisDao.save(custHis);
		PropertyUtils.copyProperties(custLimkManHis,linkMan);
		custLimkManHis.setDone_code(doneCode);
		cCustLinkmanHisDao.save(custLimkManHis);

		CCustAddr custAddr = cCustAddrDao.findByKey(cust.getCust_id());
		if(custAddr != null){
			BeanUtils.copyProperties(custAddr, custAddrHis);
			custAddrHis.setDone_code(doneCode);
			cCustAddrHisDao.save(custAddrHis);
		}
		
		//同步客户群体数量限制
		removeCustColony(cust.getCust_id());
		
		removeCustWithOutHis(cust.getCust_id());
	}

	public void removeCustWithOutHis(String custId) throws Exception{
		cCustDao.remove(custId);
		cCustLinkmanDao.remove(custId);
		cCustAddrDao.remove(custId);
	}

	/**
	 * 删除流水号对应的客户异动记录
	 * @param custId
	 * @param doneCode
	 * @throws Exception
	 */
	public void removeCustPropChange(String custId,Integer doneCode) throws Exception{
		cCustPropChangeDao.removeByDoneCode(custId, doneCode, getOptr().getCounty_id());
	}
	/**
	 * 退出单位
	 * @param custIds
	 * @throws Exception
	 */
	public void custQuitUnit(String[] unitId, String custId,Integer doneCode) throws Exception {
		cCustUnitToResidentDao.insertToHis(unitId,custId,doneCode);
		cCustUnitToResidentDao.deleteCustUnit(unitId,custId);
	}
	
	/**
	 *
	 * 保存客户变更信息
	 *
	 * @param newCust
	 * 变更后的信息 user_id不能为空，为null的表示不变更，为空字符串的表示变更为空
	 *
	 */
	public void saveCustPropChange(Integer doneCode,
			CCust newCust) throws Exception {
		CCust oldCust = cCustDao.findByKey(newCust.getCust_id());
		Field[] fields = oldCust.getClass().getDeclaredFields();
		List<CCustPropChange> cpcList = new ArrayList<CCustPropChange>();
		for (Field field : fields) {
			String newvalue = BeanHelper.getPropertyString(newCust, field
					.getName());
			if (newvalue != null) {
				String oldvalue = BeanHelper.getPropertyString(oldCust,
						field.getName());
				if (!newvalue.equals(oldvalue)) {
					CCustPropChange cpc = new CCustPropChange();
					cpc.setArea_id(oldCust.getArea_id());
					cpc.setCounty_id(oldCust.getCounty_id());
					cpc.setCust_id(oldCust.getCust_id());
					cpc.setColumn_name(field.getName());
					cpc.setOld_value(oldvalue);
					cpc.setNew_value(newvalue);
					cpc.setDone_code(doneCode);
					cpcList.add(cpc);
				}
			}
		}

		cCustPropChangeDao.save(cpcList.toArray(new CCustPropChange[cpcList.size()]));
	}
	/**
	 * 购买设备
	 * @param custId		客户编号
	 * @param deviceId		设备id
	 * @param deviceType	设备类型
	 * @param deviceCode	设备编号
	 * @param pairCardId	配对的卡号
	 * @param buyMode		购买方式
	 * @throws JDBCException
	 */
	public void addDevice(Integer doneCode,String custId, String deviceId,String deviceType,String deviceCode,
			String pairCardId,String pairCardCode,String pairModemId,String pairModemCode,String buyMode) throws Exception {
		CCustDevice custDevice = new CCustDevice ();

		setBaseInfo(custDevice);
		custDevice.setCust_id(custId);
		custDevice.setDevice_id(deviceId);
		custDevice.setDevice_type(deviceType);
		custDevice.setDevice_code(deviceCode);
		custDevice.setPair_card_id(pairCardId);
		custDevice.setPair_card_code(pairCardCode);
		custDevice.setPair_modem_id(pairModemId);
		custDevice.setPair_modem_code(pairModemCode);
		custDevice.setStatus_date(DateHelper.now());
		Calendar now = Calendar.getInstance();
		now.add(Calendar.YEAR, 3);
		custDevice.setReplacover_date(now.getTime());
		custDevice.setBuy_mode(buyMode);
		custDevice.setBuy_time(DateHelper.now());
		custDevice.setStatus(StatusConstants.USE);
		custDevice.setLoss_reg(SystemConstants.BOOLEAN_FALSE);
		custDevice.setDone_code(doneCode);
		cCustDeviceDao.save(custDevice);
	}
	
	/**
	 * 更换设备
	 * @param doneCode
	 * @param custId
	 * @param deviceId
	 * @param deviceType
	 * @param deviceCode
	 * @param pairCardId
	 * @param pairCardCode
	 * @param pairModemId
	 * @param pairModemCode
	 * @param buyMode
	 * @throws Exception
	 */
	public void changeDevice(Integer doneCode,String custId,String oldDeviceId, String deviceId,String deviceType,String deviceCode,
			String pairCardId,String pairCardCode,String pairModemId,String pairModemCode,String changeReason) throws Exception {
			CCustDevice custDevice = new CCustDevice ();
			CCustDevice oldCustDevice = cCustDeviceDao.findByKey(oldDeviceId);
			
			setBaseInfo(custDevice);
			custDevice.setCust_id(custId);
			custDevice.setDevice_id(deviceId);
			custDevice.setDevice_type(deviceType);
			custDevice.setDevice_code(deviceCode);
			custDevice.setPair_card_id(pairCardId);
			custDevice.setPair_card_code(pairCardCode);
			custDevice.setPair_modem_id(pairModemId);
			custDevice.setPair_modem_code(pairModemCode);
			custDevice.setStatus_date(DateHelper.now());
			custDevice.setReplacover_date(oldCustDevice.getReplacover_date());
			custDevice.setBuy_mode(oldCustDevice.getBuy_mode());
			custDevice.setBuy_time(oldCustDevice.getBuy_time());
			custDevice.setStatus(oldCustDevice.getStatus());
			custDevice.setLoss_reg(SystemConstants.BOOLEAN_FALSE);
			custDevice.setDone_code(doneCode);
			custDevice.setChange_reason(changeReason);
			cCustDeviceDao.save(custDevice);
	}
	
	public void saveChangeDeviceById(String deviceId,String changeReason) throws Exception {
			CCustDevice oldCustDevice = cCustDeviceDao.findByKey(deviceId);
			oldCustDevice.setChange_reason(changeReason);
			cCustDeviceDao.update(oldCustDevice);
	}
	
	public void saveChangeDevice(CCustDevice custDevice,String changeReason) throws Exception {
		custDevice.setChange_reason(changeReason);
		cCustDeviceDao.update(custDevice);
}
	
	
	public void saveDeviceBuyModeProp(Integer doneCode, String custId,
			String deviceType, String oldDeviceId, String oldDeviceCode,
			String oldBuyMode, String newDeviceId, String newDeviceCode,
			String newBuyMode) throws Exception {
		CCustDeviceBuymodeProp prop = new CCustDeviceBuymodeProp();
		
		setBaseInfo(prop);
		prop.setDone_code(doneCode);
		prop.setCust_id(custId);
		prop.setDevice_type(deviceType);
		prop.setOld_device_id(oldDeviceId);
		prop.setOld_device_code(oldDeviceCode);
		prop.setOld_buy_mode(oldBuyMode);
		prop.setNew_device_id(newDeviceId);
		prop.setNew_device_code(newDeviceCode);
		prop.setNew_buy_mode(newBuyMode);
		
		cCustDeviceBuymodePropDao.save(prop);
	}
	
	/**
	 * 删除客户所有设备
	 * c_cust_device_his 只记录销客户未回收的客户设备信息
	 * @param custId
	 * @throws Exception
	 */
	public void removeAllDevice(String custId, Integer doneCode) throws Exception{
		List<CCustDevice> deviceList = cCustDeviceDao.queryDevices(custId);
		if(deviceList != null){
			List<CCustDeviceHis> hisList = new ArrayList<CCustDeviceHis>();
			for(CCustDevice custDevice : deviceList){
				CCustDeviceHis custDeviceHis = new CCustDeviceHis();
				BeanUtils.copyProperties(custDevice, custDeviceHis);
				custDeviceHis.setDone_code(doneCode);
				custDeviceHis.setBuy_done_code(custDevice.getDone_code());
				custDeviceHis.setIs_reclaim(SystemConstants.BOOLEAN_FALSE);
				hisList.add(custDeviceHis);
			}
			cCustDeviceHisDao.save(hisList.toArray(new CCustDeviceHis[hisList.size()]));
		}
		cCustDeviceDao.removeAllDevice(custId);
	}

	public void removeDeviceChange(Integer doneCode) throws Exception{
		cCustDeviceChangeDao.removeByDoneCode(doneCode);
	}
	/**
	 * 删除客户单个设备并备份到历史记录表
	 * @param custId		客户编号
	 * @param deviceId		设备id
	 * @param isReclaim		是否回收
	 */
	public void removeDevice(String custId, String deviceId, Integer doneCode, String isReclaim) throws Exception {
		CCustDevice custDevice = cCustDeviceDao.queryCustDeviceByDeviceId(custId, deviceId);
		if(custDevice != null){
			cCustDeviceDao.removeDevice(custId, deviceId, doneCode, isReclaim);
		}else{
			//已销户，设备在c_cust_device_his中，可回收
			cCustDeviceHisDao.updateRecliam(custId, deviceId);
		}
	}
	
	public void saveCustDevice(CCustDevice custDevice) throws Exception {
		cCustDeviceDao.save(custDevice);
	}

	
	/**
	 * 将一个不在任何客户名下的或者其它客户名下的设备转到新客户名下
	 * @param deviceId
	 * @param newCustId
	 * @param newDevice
	 * @throws Exception
	 */
	public void updateDevice(CCustDevice ... device) throws Exception {
		cCustDeviceDao.update(device);
	}
	
	/**
	 * 将一个不在任何客户名下的或者其它客户名下的设备转到新客户名下
	 * @param deviceId
	 * @param newCustId
	 * @param newDevice
	 * @throws Exception
	 */
	public void transDevice(Integer doneCode,String newCustId, DeviceDto newDevice)
		throws Exception {
		if(null != newDevice){
			CCustDevice custDevice = queryCustDeviceByDeviceId(newDevice.getDevice_id());
			String pairCardId = newDevice.getPairCard() == null ? null : newDevice
					.getPairCard().getDevice_id();
			String pairCardCode = newDevice.getPairCard() == null ? null : newDevice
					.getPairCard().getCard_id();

			String pairModemId = newDevice.getPairModem() == null ? null : newDevice
					.getPairModem().getDevice_id();
			String pairModemCode = newDevice.getPairModem() == null ? null : newDevice
					.getPairModem().getModem_mac();

			if (custDevice != null){
				if (!custDevice.getCust_id().equals(newCustId) && newDevice.getIs_virtual().equals(SystemConstants.BOOLEAN_FALSE)){
//					CCustDeviceChange change = new CCustDeviceChange();
//					change.setDone_code(doneCode);
//					change.setDevice_id(custDevice.getDevice_id());
//					change.setColumn_name("cust_id");
//					change.setOld_value(custDevice.getCust_id());
//					change.setNew_value(newCustId);
//					setBaseInfo(change);
//					cCustDeviceChangeDao.save(change);
					
					removeDevice(custDevice.getCust_id(), custDevice.getDevice_id(), doneCode, SystemConstants.BOOLEAN_FALSE);
					addDevice(doneCode, newCustId, newDevice.getDevice_id(),
							newDevice.getDevice_type(), newDevice.getDevice_code(), 
							pairCardId,pairCardCode, pairModemId, pairModemCode,
							custDevice.getBuy_mode());
				}
			}else {
				if(newDevice.getIs_virtual().equals(SystemConstants.BOOLEAN_FALSE)){
					addDevice(doneCode, newCustId, newDevice.getDevice_id(),
							newDevice.getDevice_type(), newDevice.getDevice_code(),
							pairCardId, pairCardCode, pairModemId, pairModemCode,
							SystemConstants.BUSI_BUY_MODE_SALE);
				}
			}
		}
	}
	/**
	 * 修改设备购买方式
	 * @param custId
	 * @param deviceId
	 * @param buyMode
	 * @throws Exception
	 */
	public void updateDeviceBuyMode(Integer doneCode,String custId,String deviceId,String buyMode)throws Exception {
		CCustDevice custDevice = cCustDeviceDao.findByKey(deviceId);
		List<CCustDeviceChange> deviceChanges = new ArrayList<CCustDeviceChange>();
		CCustDeviceChange change = new CCustDeviceChange();
		change.setColumn_name("buy_mode");
		change.setOld_value(custDevice.getBuy_mode());
		change.setNew_value(buyMode);
		deviceChanges.add(change);
		editCustDevice(doneCode,deviceId,deviceChanges);
	}

	/**
	 * 挂失设备
	 * @param custId
	 * @param deviceId
	 * @throws Exception
	 */
	public void saveDeviceRegLoss(Integer doneCode,String custId,String deviceId)throws Exception {
		List<CCustDeviceChange> deviceChanges = new ArrayList<CCustDeviceChange>();
		CCustDeviceChange change = new CCustDeviceChange();
		change.setColumn_name("loss_reg");
		change.setOld_value(SystemConstants.BOOLEAN_FALSE);
		change.setNew_value(SystemConstants.BOOLEAN_TRUE);
		deviceChanges.add(change);
		editCustDevice(doneCode,deviceId,deviceChanges);
	}
	
	/**
	 * 购买包换期.
	 * @param deviceId
	 * @param deviceCode
	 * @param doneCode
	 */
	public void saveBuyReplacover(String deviceId, Integer doneCode) throws Exception{
		CCustDevice device = queryCustDeviceByDeviceId(deviceId);
		Date oldDate = device.getReplacover_date();
		String oldValue = null;
		String newValue = null;
		Calendar calendar = Calendar.getInstance(); 
		if(oldDate != null && !oldDate.before(calendar.getTime())){
			calendar.setTime(oldDate);
		}
		calendar.add(Calendar.YEAR, 1);
		oldValue = null == oldDate ? "" : DateHelper.format(oldDate, DateHelper.FORMAT_YMD);
		newValue = DateHelper.format(calendar.getTime(), DateHelper.FORMAT_YMD);
		
		
		List<CCustDeviceChange> deviceChanges = new ArrayList<CCustDeviceChange>();
		CCustDeviceChange change = new CCustDeviceChange();
		change.setColumn_name("replacover_date");
		change.setDevice_id(deviceId);
		change.setOld_value(oldValue);
		change.setNew_value(newValue);
		deviceChanges.add(change);
		editCustDevice(doneCode,deviceId,deviceChanges);
	}
	

	/**
	 * 取消挂失
	 * @param custId
	 * @param deviceId
	 * @throws Exception
	 */
	public void saveCancelDeviceRegLoss(Integer doneCode,String custId,String deviceId)throws Exception {
		List<CCustDeviceChange> deviceChanges = new ArrayList<CCustDeviceChange>();
		CCustDeviceChange change = new CCustDeviceChange();
		change.setColumn_name("loss_reg");
		change.setOld_value(SystemConstants.BOOLEAN_TRUE);
		change.setNew_value(SystemConstants.BOOLEAN_FALSE);
		deviceChanges.add(change);
		editCustDevice(doneCode,deviceId,deviceChanges);
	}
	
	/**
	 * 修改客户设备属性
	 * @param doneCode
	 * @param deviceId
	 * @param deviceChanges
	 */
	public void editCustDevice(Integer doneCode, String deviceId,
			List<CCustDeviceChange> deviceChanges) throws Exception {
		CCustDevice custDevice = new CCustDevice();
		custDevice.setDevice_id(deviceId);
		
		for (CCustDeviceChange change : deviceChanges) {
			BeanHelper.setPropertyString(custDevice, change.getColumn_name(), change
					.getNew_value());
			change.setArea_id(getOptr().getArea_id());
			change.setCounty_id(getOptr().getCounty_id());
			change.setChange_time(DateHelper.now());
			change.setDone_code(doneCode);
			change.setDevice_id(deviceId);
		}
		cCustDeviceDao.update(custDevice);
		cCustDeviceChangeDao.save(deviceChanges.toArray(new CCustDeviceChange[deviceChanges.size()]));
	}
	
	public void updateCustDevice(Integer doneCode, String oldCustId, String newCustId) throws Exception {
		List<CCustDevice> oldCustDeviceList = cCustDeviceDao.queryDevices(oldCustId);
		for(CCustDevice oldCustDevice : oldCustDeviceList){
			this.removeDevice(oldCustId, oldCustDevice.getDevice_id(), doneCode, SystemConstants.BOOLEAN_TRUE);
			
			CCustDevice newCustDevice = new CCustDevice();
			BeanUtils.copyProperties(oldCustDevice, newCustDevice);
			newCustDevice.setCust_id(newCustId);
			cCustDeviceDao.save(newCustDevice);
			
			CCustDeviceChange change = new CCustDeviceChange();
			change.setDone_code(doneCode);
			change.setDevice_id(newCustDevice.getDevice_id());
			change.setColumn_name("cust_id");
			change.setOld_value(oldCustId);
			change.setNew_value(newCustId);
			change.setArea_id(getOptr().getArea_id());
			change.setCounty_id(getOptr().getCounty_id());
			cCustDeviceChangeDao.save(change);
		}
	}
	
	/**
	 * 修改客户状态
	 * @param doneCode
	 * @param custId
	 * @param active
	 */
	public List<CCustPropChange> updateCustStatus(Integer doneCode, String custId, String oldStatus,String newStatus) throws Exception{
		CCustPropChange propChange = new CCustPropChange();
		propChange.setColumn_name("status");
		propChange.setOld_value(oldStatus);
		propChange.setNew_value(newStatus);

		List<CCustPropChange> changeList = new ArrayList<CCustPropChange>();
		changeList.add(propChange);
		editCust(doneCode, custId, changeList);
		return changeList;
	}
	
	/**
	 * 修改客户Class
	 * @param doneCode
	 * @param cust
	 * @param newClass
	 * @param newClassDate
	 * @throws Exception
	 */
	public List<CCustPropChange> updateCustClass(Integer doneCode, CCust cust,String newClass,String newClassDate) throws Exception{
		List<CCustPropChange> changeList = new ArrayList<CCustPropChange>();
		String oldCustClass = cust.getCust_class();
		if(!StringHelper.bothEmptyOrEquals(oldCustClass, newClass)){
			CCustPropChange propChange = new CCustPropChange();
			propChange.setColumn_name("cust_class");
			propChange.setParam_name("CUST_CLASS");
			propChange.setOld_value(oldCustClass);
			propChange.setNew_value(newClass);
			changeList.add(propChange);
		}

		String oldClassDate = cust.getCust_class_date();
		if(!StringHelper.bothEmptyOrEquals(oldClassDate, newClassDate)){
			CCustPropChange propChange1 = new CCustPropChange();
			propChange1.setColumn_name("cust_class_date");
			propChange1.setOld_value(oldClassDate);
			propChange1.setNew_value(newClassDate);
			changeList.add(propChange1);
		}
		if(!changeList.isEmpty()){
			editCust(doneCode, cust.getCust_id(), changeList);
		}
		return changeList;
	}
	
	/**
	 * 根据设备id修改客户设备状态
	 * @param custId
	 * @param deviceId
	 * @param status
	 */
	public void updateDeviceStatus(String custId,String deviceId,String status)throws Exception{
		cCustDeviceDao.updateDeviceStatus(custId, deviceId, status);
	}

	/**
	 * 根据设备号修改客户设备状态
	 * @param custId
	 * @param deviceId
	 * @param status
	 */
	public void updateDeviceStatusByCode(String custId,
			String deviceCode, String status) throws Exception {
		if (StringHelper.isNotEmpty(deviceCode)) {
			cCustDeviceDao.updateDeviceStatusByDeviceCode(custId, deviceCode,
					status);
		}
	}
	
	public List<CCustDevice> findBuyModeById(String custId,String[] deviceCode) throws Exception {
		return cCustDeviceDao.findBuyModeById(custId, deviceCode,getOptr().getCounty_id());
	}
	
	/**
	 * 查询单位下制定的客户
	 * @param unitId
	 * @param custIds
	 * @return
	 */
	public List<CCust> queryCustByUnit(String unitId, String[] custIds) throws Exception {
		return cCustDao.queryCustByUnit(unitId, custIds,getOptr().getCounty_id());
	}
	
	private String queryDataRightCon() {
		String dataRight = "";
		try {
			dataRight = this.queryDataRightCon(getOptr(),DataRight.QUERY_CUST.toString());
		} catch (Exception e) {
			dataRight=" 1=1 ";
		}
		return dataRight;
	}
	
	/**
	 * 根据客户ID查询客户
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public CustFullInfoDto searchCustInfoById(String custId) throws Exception{
		String dataType = this.queryDataRightCon();
		CustFullInfoDto custInfoDto = new CustFullInfoDto();
		CCust cust = cCustDao.searchById(custId,dataType);
		CCustLinkman linkman = null;
		CCustBonuspoint bonuspoint = null;
		CAcctBank acctBank = null;
		if(cust != null/* && !cust.getStatus().equals(StatusConstants.INVALID)*/){
			linkman = cCustLinkmanDao.findByKey(custId);
			bonuspoint = cCustBonuspointDao.findByKey(custId);
			acctBank = cAcctBankDao.findByCustId(custId);
		}/*else{
			cust = cCustHisDao.findByKey(custId);
			CCustAddr custAddr = cCustAddrHisDao.findByKey(custId);
			BeanUtils.copyProperties(custAddr, cust);
			linkman = cCustLinkmanHisDao.findByKey(custId);
		}*/
		//设置客户单位信息
		if (SystemConstants.CUST_TYPE_RESIDENT.equals(cust.getCust_type())){
			List<CCust> custList = this.queryUnitByResident(cust.getCust_id());
			if (custList != null && custList.size()>0){
				cust.setUnit_id(custList.get(0).getCust_id());
				cust.setUnit_name(custList.get(0).getCust_name());
			}
		}
		
		//模拟大客户中有多少客户数量
		if (SystemConstants.CUST_COLONY_MNDKH.equals(cust.getCust_colony())
				|| SystemConstants.CUST_COLONY_XYKH.equals(cust
						.getCust_colony())){
			int count = cCustUnitToResidentDao.getMnCustCountByCustId(cust.getCust_id());
			cust.setReal_cust_count(count);
		}
		if(null == acctBank)
			acctBank = new CAcctBank();
		
		SAgent agent = this.sAgentDao.queryAgentByDeptId(cust.getDept_id());
		if(agent != null){
			cust.setAgent_id(agent.getId());
			cust.setAgent_name(agent.getName());
		}
		
		custInfoDto.setCust(cust);
		custInfoDto.setLinkman(linkman);
		custInfoDto.setBonuspoint(bonuspoint);
		custInfoDto.setAcctBank(acctBank);
		return custInfoDto;
	}

	/**
	 * 根据传入的客户属性，获取符合条件的客户集合
	 * @param cust
	 * @return
	 * @throws Exception
	 */
	public Pager<CCust> queryCust(Pager<Map<String ,Object>> p)throws Exception{
		String dataType = this.queryDataRightCon();
		 Pager<CCust> page = cCustDao.searchCust(p, dataType ,getOptr().getCounty_id());
		 if(page.getTotalProperty()<=1){
			 return page;
		 }
		 //修改正常客户的状态为房间状态
		 for(CCust cust: page.getRecords()){
			 if(cust.getStatus().equals(StatusConstants.ACTIVE)){
				 String note_status_type=cCustAddrDao.queryNoteStatusType(cust.getCust_id());
				 if(StringHelper.isNotEmpty(note_status_type)){
					 String note_desc=MemoryDict.getDictName(DictKey.NOTE_STATUS_TYPE, note_status_type);
					if( StringHelper.isNotEmpty(note_desc)){
						 cust.setStatus_text(note_desc);
					}
				 }
			 }
		 }
		 return page;
	}
	
	/**
	 * 多条件查询客户
	 * @param cust
	 * @param start
	 * @param limit
	 * @return
	 */
	public Pager<CCust> complexSearchCust(CCust cust, Integer start,
			Integer limit) throws Exception {
		String dataType = this.queryDataRightCon();
		return cCustDao.complexSearchCust(cust,start,limit, dataType ,getOptr().getCounty_id());
	}

	/**
	 * 根据单位客户的名称和地址模糊查找当前县市的单位客户信息
	 * @param unitName
	 * @return
	 * @throws Exception
	 */
	public List<CCust> queryUnitByNameAndAddr(String unitName,String addr)throws Exception{
		String dataType = this.queryDataRightCon();
		return cCustDao.queryUnitByNameAndAddr(unitName, addr, getOptr().getCounty_id(),dataType);

	}
	
	/**
	 * 根据模拟大客户的名称和地址模糊查找当前县市的模拟大客户信息
	 * @param unitName
	 * @return
	 * @throws Exception
	 */
	public List<CCust> queryMnCustByNameAndAddr(String mnCustName,String addr)throws Exception{
		return cCustDao.queryMnCustByNameAndAddr(mnCustName, addr, getOptr().getCounty_id());

	}

	/**
	 * 查询客户
	 * @param custId
	 * @return
	 */
	public CCust queryCustById(String custId) throws JDBCException {
		return cCustDao.findByKey(custId);
	}
	
	public CCustLinkman queryCustLinkmanById(String custId) throws JDBCException {
		return cCustLinkmanDao.findByKey(custId);
	}

	/**
	 * 根据居民客户id获取所属单位信息
	 * @param residentCustId
	 * @return
	 * @throws Exception
	 */
	public List<CCust> queryUnitByResident(String residentCustId)throws Exception{
		return cCustDao.queryUnitByResident(residentCustId, getOptr().getCounty_id());

	}
	
	/**
	 * 根据居民客户id获取所属模拟大客户信息
	 * @param residentCustId
	 * @return
	 * @throws Exception
	 */
	public List<CCust> queryMnCustByResident(String residentCustId)throws Exception{
		return cCustDao.queryMnCustByResident(residentCustId, getOptr().getCounty_id());

	}
	
	/**
	 * 根据客户地址，模糊查找没加入单位的居民客户
	 * @param query
	 * @return
	 */
	public List<CCust> searchResidentCust(CCust cust) throws Exception {
		String dataType = this.queryDataRightCon();
		return cCustDao.searchResidentCust(cust, dataType ,getOptr().getCounty_id());
	}

	public List<CCustDeviceChange> queryDeviceChangeByDoneCode (Integer doneCode) throws Exception{
		return cCustDeviceChangeDao.queryByDoneCode(doneCode);
	}

	/**
	 * 根据客户编号获取客户属性的异动信息
	 * @param custId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CCustPropChange>  queryPropChangeByCustID(String custId,String custType)throws Exception{
		List<CCustPropChange> propChangeList = cCustPropChangeDao.queryPropChangeByCustID(custId,custType, getOptr().getCounty_id());
		for (CCustPropChange change :propChangeList){
			setPropText(change);
		}
		return propChangeList;
	}

	public List<CCustPropChange>  queryPropChangeByDoneCode(String custId,Integer doneCode)throws Exception{
		List<CCustPropChange> propChangeList = cCustPropChangeDao.queryPropChangeByDoneCode(custId,doneCode, getOptr().getCounty_id());
		for (CCustPropChange change :propChangeList){
			setPropText(change);
		}
		return propChangeList;
	}

	/**
	 * 根据设备id查询客户设备
	 * @param deviceId
	 * @return
	 */
	public CCustDevice queryCustDeviceByDeviceId(String deviceId) throws Exception{
		return cCustDeviceDao.findByDeviceId(deviceId,getOptr().getCounty_id());
	}
	
	public CCustDevice queryCustDeviceByDeviceCode(String deviceCode) throws Exception{
		return cCustDeviceDao.findByDeviceCode(deviceCode,getOptr().getCounty_id());
	}
	
	public CCustDevice queryCustDeviceByCodeAndCustId(String custId,String deviceCode) throws Exception {
		return cCustDeviceDao.queryCustDeviceByDeviceCode(custId, deviceCode);
	}
	
	/**
	 * 查询客户的所有设备信息
	 * @param custId 客户编号
	 * @return
	 */
	public List<CustDeviceDto> queryCustDevices(String custId) throws Exception{
		String custStatus = "";
		CCust cust = cCustDao.findByKey(custId);
		if(cust == null){
			custStatus = StatusConstants.INVALID;
		}else{
			custStatus = cust.getStatus();
		}
		List<CustDeviceDto> cdevices = cCustDeviceDao.queryCustDevices(custId,
				custStatus, getOptr().getCounty_id());
		return cdevices;
	}


	/**
	 * 更新客户设备表配对信息
	 * @param stbDeviceId
	 * @param newCardId
	 * @param newCardCode
	 * @throws JDBCException
	 */
	public void updatePairCard(String stbDeviceId, String newCardId,String newCardCode) throws JDBCException {
		cCustDeviceDao.updatePairCard(stbDeviceId, newCardId,newCardCode);
	}
	
	/**
	 * 更新客户设备表配对信息
	 * @param stbDeviceId
	 * @param newCardId
	 * @param newCardCode
	 * @throws JDBCException
	 */
	public void updatePairModem(String stbDeviceId, String newModemId,String newModemCode) throws JDBCException {
		cCustDeviceDao.updatePairModem(stbDeviceId, newModemId,newModemCode);
	}

	/**
	 * 获取客户id
	 */
	private String gCustId() throws Exception{
		return cCustDao.findSequence().toString();
	}

	/**
	 * 获取客户受理名
	 */
	private String gCustNo() throws Exception{
		String seq=cCustDao.findSequence(SequenceConstants.SEQ_CUST_NO+"_"+getOptr().getCounty_id()).toString();
		while(seq.length()<7){
			seq ="0"+seq;
		}
		seq =getOptr().getCounty_id()+seq;
		return  seq;
	}
	
	
	/**
	 * 根据地址码获得客户对应的宽带域名
	 * @param custCode 
	 * @param cust
	 * @return
	 */
	public String getDomainByAddr(String addrId) throws Exception {
		String Domain = "";
		
		TAddress adr = tAddressDao.findByKey(addrId);
		if(adr == null){
			throw new ComponentException(ErrorCode.CustAddressIsNull);
		}
		TDistrict district = tDistrictDao.findByKey(adr.getDistrict_id());
		if(district == null){
			throw new ComponentException(ErrorCode.CustDistrictIsNull,addrId);
		}
		TProvince province = tProvinceDao.findByKey(district.getProvince_id());
		if(province == null || StringHelper.isEmpty(province.getCust_code())){
			throw new ComponentException(ErrorCode.CustProvinceIsNull,adr.getDistrict_id());
		}
		Domain=province.getDomain_name();
		
		if(StringHelper.isEmpty(Domain)){
			throw new ComponentException(ErrorCode.CustProvinceDomainIsNull,province.getName());
		}
		
		return Domain;
	}
	/**
	 * 根据地址码产生客户编号
	 * @param custCode 
	 * @param cust
	 * @return
	 */
	public String gCustNoByAddr(String addrId, String custCode ) throws Exception {
		String custNo = "";
		if(StringHelper.isNotEmpty(custCode)){
			custNo = custCode;
		}else{
			TAddress adr = tAddressDao.findByKey(addrId);
			if(adr == null){
				throw new ComponentException(ErrorCode.CustAddressIsNull);
			}
			TDistrict district = tDistrictDao.findByKey(adr.getDistrict_id());
			if(district == null){
				throw new ComponentException(ErrorCode.CustDistrictIsNull,addrId);
			}
			TProvince province = tProvinceDao.findByKey(district.getProvince_id());
			if(province == null || StringHelper.isEmpty(province.getCust_code())){
				throw new ComponentException(ErrorCode.CustProvinceIsNull,adr.getDistrict_id());
			}
			
			custNo = province.getCust_code();
		}
		
		String seq=cCustDao.findSequence(SequenceConstants.SEQ_CUST_NO+"_"+custNo).toString();
		if(seq == null){
			throw new ComponentException(ErrorCode.CustSeqIsNull,custNo);
		}
		seq = custNo+seq;
		
//		while(seq.length()<6){
//			seq ="0"+seq;
//		}
		return seq;
	}

	private void setPropText(CCustPropChange change) throws Exception {
		change.setColumn_name_text( MemoryDict.getTransData(change.getColumn_name_text()) );
		//属性值有对应的系统参数
		if (StringHelper.isNotEmpty(change.getParam_name())){
			change.setOld_value_text(MemoryDict.getDictName(change.getParam_name(), change.getOld_value()));
			change.setNew_value_text(MemoryDict.getDictName(change.getParam_name(), change.getNew_value()));
		} else {
			change.setOld_value_text( MemoryDict.getTransData( change.getOld_value() ));
			change.setNew_value_text( MemoryDict.getTransData( change.getNew_value() ));
		}
	}
	
	/**
	 * 根据传入的客户属性，获取符合条件的客户集合
	 * @param cust
	 * @return
	 * @throws Exception
	 */
	public Pager<CCust> searchTransportableCust(String custNameLike, Integer start, Integer limit)throws Exception{
		String dataType = this.queryDataRightCon();
		return cCustDao.searchTransportableCust(custNameLike, dataType, getOptr().getCounty_id(), start, limit);
	}

	/**
	 * @param searchType
	 * @param searchValue
	 * @return
	 */
	public CCust queryCust(String searchType, String searchValue) throws Exception {
		String dataRight = this.queryDataRightCon();
		CCust cust = cCustDao.queryCustFullInfo(searchType,searchValue,dataRight);
		if (cust != null && cust.getCust_type().equals(SystemConstants.CUST_TYPE_RESIDENT)){
			List<CCust> custList = this.queryUnitByResident(cust.getCust_id());
			if (custList != null && custList.size()>0){
				cust.setUnit_id(custList.get(0).getCust_id());
				cust.setUnit_name(custList.get(0).getCust_name());
			}
		}
		return cust;
	}

	/**
	 *
	 * @param custId
	 * @return
	 */
	public List<CCust> queryUnitMember(String custId) throws JDBCException {
		return cCustDao.queryUnitMember(custId);
	}
	/**
	 *
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public List<CCust> queryCustToCallCenter(String cust_no, String cust_name, String address, String card_id, String telOrMobile, String modem_mac, String stb_id, String band_login_name)throws Exception{
		return cCustDao.queryCustFullInfoToCallCenter(cust_no, cust_name, address, card_id, telOrMobile, modem_mac, stb_id, band_login_name, getOptr().getCounty_id());
	}
	
	public CustGeneralInfo SearchCustGeneralInfo(String custId) throws Exception{
		CustGeneralInfo custGeneralInfo = cCustDao.SearchCustGeneralInfo(custId, getOptr().getCounty_id());
		custGeneralInfo.setStopUserAmount(custGeneralInfo.getTotalUserAmount() - custGeneralInfo.getActiveUserAmount());
		custGeneralInfo.setOweStopProdAmount(custGeneralInfo.getTotalProdAmount()
				- custGeneralInfo.getActiveProdAmount() - custGeneralInfo.getOweUnStopProdAmount());
		return custGeneralInfo;
	}
	
	/**
	 * 验证客户密码
	 * @param custId
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public CCust validCustByPassword(String custId,String password) throws Exception{
		return cCustDao.validCustByPassword(custId, password);
	}

	
	/**
	 * 根据客户地址信息查询客户
	 * @param cust
	 * @param optr
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<CCust> queryCustAddress(CCust cust,SOptr optr,Integer start,Integer limit) throws Exception{
		return cCustDao.queryCustAddress(cust, getOptr().getCounty_id(),start,limit);
	}
	
	/**
	 * 根据多个客户编号查询客户信息
	 * @param custIds
	 * @param optr
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<CCust> queryCustAddrByCustIds(String[] custIds,SOptr optr,Integer start,Integer limit) throws Exception{
		return cCustDao.queryCustAddrByCustIds(custIds, getOptr().getCounty_id(),start,limit);
	}
	
	public List<CCust> queryCustBycustIds(String[] custIds) throws Exception{
		return cCustDao.queryCustByCustIds(custIds);
	}
	
	/**
	 * 根据客户地址编号查询客户
	 * @param addrId
	 * @param optr
	 * @return
	 * @throws Exception
	 */
	public List<CCust> queryAddressAll(String addrId,SOptr optr) throws Exception{
		return cCustDao.queryAddressAll(addrId, getOptr().getCounty_id());
	}
	
	/**
	 * 根据地址编号查询该地址和其父地址的信息
	 * @param addrId
	 * @return
	 * @throws Exception
	 */
	public TAddressDto getAddr(String addrId) throws Exception{
		return  tAddressDao.getAddressByAddrId(addrId);
	}
	
	/**
	 * 根据小区信息newAddr，oldAddr来判断原客户地址可以进行修改的客户信息
	 * @param newAddr
	 * @param oldAddr
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CCust> getCustAddrList(TAddressDto newAddr,TAddressDto oldAddr,String countyId) throws Exception{
		//查询可以修改的c_cust的地址数据
		List<CCust>  custList =  new ArrayList<CCust>();
		List<CCust> oneList = tAddressDao.getoneCustByAddrPId(newAddr,oldAddr,countyId);
		List<CCust> twoList = tAddressDao.gettwoCustByAddrPId(newAddr,oldAddr,countyId);
		
		for(CCust dto : oneList){
			CCust cust = new CCust();
			BeanUtils.copyProperties(dto, cust);
			custList.add(cust);
		}
		for(CCust dto : twoList){
			CCust cust = new CCust();
			BeanUtils.copyProperties(dto, cust);
			custList.add(cust);
		}
		return  custList;
	}
	
	/**
	 * 根据小区信息newAddr，oldAddr来判断原邮件地址可以进行修改的客户信息
	 * @param newAddr
	 * @param oldAddr
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CCust> getCustLinkmanAddrList(TAddressDto newAddr,TAddressDto oldAddr,String countyId) throws Exception{
		//查询可以修改的c_cust_linkman的地址数据
		List<CCust>  custList =  new ArrayList<CCust>();
		List<CCust> oneList = tAddressDao.getoneCustLinkmanByAddrPId(newAddr,oldAddr,countyId);
		List<CCust> twoList = tAddressDao.gettwoCustLinkmanByAddrPId(newAddr,oldAddr,countyId);
		
		for(CCust dto : oneList){
			CCust cust = new CCust();
			BeanUtils.copyProperties(dto, cust);
			custList.add(cust);
		}
		for(CCust dto : twoList){
			CCust cust = new CCust();
			BeanUtils.copyProperties(dto, cust);
			custList.add(cust);
		}
		return  custList;
	}
	
	/**变更客户小区
	 * @param oldAddrId
	 * @param newAddrId
	 * @param countyId
	 * @throws Exception
	 */
	public void updateCustAddr(String oldAddrId, String newAddrId,String countyId)  throws Exception{
		cCustDao.updateCustAddr(oldAddrId,newAddrId,countyId); 
	}
	
	/**
	 * 更换地址
	 * @param custAddrList
	 * @throws Exception
	 */
	public void updateAddressList(Integer doneCode,List<CCust> custAddrList,List<CCust> custLinkmanList)throws Exception{
		List<CCustLinkman> LinkmanList = new ArrayList<CCustLinkman>();
		List<CCustAddr> CustAddrList = new ArrayList<CCustAddr>();
		List<CCustPropChange> propChangeList = new ArrayList<CCustPropChange>();
		if(custAddrList !=null){
			for(CCust dto :custAddrList){
				CCustAddr custaddr = new CCustAddr();
				BeanUtils.copyProperties(dto, custaddr);
				CustAddrList.add(custaddr);
				
				CCustPropChange propChange = new CCustPropChange();
				propChange.setDone_code(doneCode);
				propChange.setCust_id(dto.getCust_id());
				propChange.setColumn_name("address");
				propChange.setOld_value(dto.getOld_address());
				propChange.setNew_value(dto.getAddress());
				propChange.setCounty_id(dto.getCounty_id());
				propChange.setArea_id(dto.getArea_id());
				propChangeList.add(propChange);
			}
		}
		if(custLinkmanList !=null){
			for(CCust dto:custLinkmanList){
				CCustLinkman custlinkman = new CCustLinkman();
				custlinkman.setCust_id(dto.getCust_id());
				custlinkman.setMail_address(dto.getAddress());
				LinkmanList.add(custlinkman);
				
				CCustPropChange propChange = new CCustPropChange();
				propChange.setDone_code(doneCode);
				propChange.setCust_id(dto.getCust_id());
				propChange.setColumn_name("mail_address");
				propChange.setOld_value(dto.getOld_address());
				propChange.setNew_value(dto.getAddress());
				propChange.setCounty_id(dto.getCounty_id());
				propChange.setArea_id(dto.getArea_id());
				propChangeList.add(propChange);
				
			}
		}
		cCustAddrDao.update(CustAddrList.toArray(new CCustAddr[CustAddrList.size()]));
		cCustDao.update(custAddrList.toArray(new CCust[custAddrList.size()]));
		cCustLinkmanDao.update(LinkmanList.toArray(new CCustLinkman[LinkmanList.size()]));
		cCustPropChangeDao.save(propChangeList.toArray(new CCustPropChange[propChangeList.size()]));
	}
	
	public List<String> updateAddressList(Integer doneCode, CCust cust, String newAddrId, String newCustAddress, String oldAddrText) throws Exception {
		Pager<CCust> pages = this.queryCustAddress(cust, getOptr(),0,Integer.MAX_VALUE);
		List<CCust> custList = pages.getRecords();
		List<String> custIdList = new ArrayList<String>();
		if(custList.size() > 0){
			String replaceAddress = cust.getAddress();
//			TAddressDto newAddress = this.getAddr(newAddrId);
//			String newAddressName = newAddress.getAddr_p_name()+""+newAddress.getAddr_name();
			List<CCustPropChange> propList = new ArrayList<CCustPropChange>();
			for(CCust ccust : custList){
				String custId = ccust.getCust_id();
				custIdList.add(custId);
				
				CCustPropChange prop = null;
				if(StringHelper.isNotEmpty(newAddrId)){
					prop = new CCustPropChange();
					prop.setCust_id(custId);
					prop.setColumn_name("addr_id");
					prop.setOld_value(ccust.getAddr_id());
					prop.setNew_value(newAddrId);
					propList.add(prop);
				}
				
				if(StringHelper.isNotEmpty(newCustAddress) && ccust.getAddress().indexOf(replaceAddress) >= 0){
					String address = ccust.getAddress().replaceAll(replaceAddress, newCustAddress);
					prop = new CCustPropChange();
					prop.setCust_id(custId);
					prop.setColumn_name("address");
					prop.setOld_value(ccust.getAddress());
					prop.setNew_value(address);
					propList.add(prop);
				}
				
				/*prop = new CCustPropChange();
				prop.setCust_id(custId);
				prop.setColumn_name("address");
				prop.setOld_value(ccust.getAddress());
				
				TAddressDto oldAddress = this.getAddr(oldAddrId);
				
				String oldAddrName = StringHelper.isNotEmpty(oldAddrText) ? oldAddrText : oldAddress.getAddr_p_name()+""+oldAddress.getAddr_name();
				prop.setNew_value(ccust.getAddress().replaceAll(oldAddrName, newAddressName));
				propList.add(prop);
				
				CCustLinkman custLinkman = cCustLinkmanDao.findByKey(custId);
				prop = new CCustPropChange();
				prop.setCust_id(custId);
				prop.setColumn_name("mail_address");
				prop.setOld_value(custLinkman.getMail_address());
				if(StringHelper.isNotEmpty(custLinkman.getMail_address())){
					prop.setNew_value(custLinkman.getMail_address().replaceAll(custLinkman.getMail_address(), newAddressName));
				}else{
					prop.setNew_value(ccust.getAddress().replaceAll(oldAddrName, newAddressName));
				}
				propList.add(prop);*/
				
				editCust(doneCode, custId, propList);
				propList = new ArrayList<CCustPropChange>();
			}
		}
		return custIdList;
	}
	
	public Object queryImportanceCustNum() throws Exception{
		String dataRight = "";
		try {
			dataRight = this.queryDataRightCon(getOptr(),DataRight.PAY_REMIND.toString());
		} catch (Exception e) {
			dataRight=null;
		}
		if(dataRight == null){
			return true;
		}else{
			return  cCustDao.queryImportanceCustNum(getOptr().getCounty_id(),dataRight);
		}
	}
	
	
	public Pager<CCust> queryImportanceCust(Integer start,
			Integer limit) throws Exception {
		String dataRight = "";
		try {
			dataRight = this.queryDataRightCon(getOptr(),DataRight.PAY_REMIND.toString());
		} catch (Exception e) {
			dataRight=null;
		}
		if(dataRight == null){
			return null;
		}else{
			return cCustDao.queryImportanceCust(start,limit, dataRight ,getOptr().getCounty_id());
		}
	}
	
	public List<CCust> queryCustByCustNos(List<String> custNos) throws Exception {
		return cCustDao.queryCustByCustNos(custNos.toArray(new String[custNos.size()]));
	}
	
	/**
	 * 保存doneCode信息,提供给涉及多个客户的业务,为涉及到的客户都添加业务流水记录.
	 * @param dcList
	 * @param dcdList
	 * @throws Exception
	 */
	public void saveDoneCodeInfo(List<CDoneCode> dcList,List<CDoneCodeDetail> dcdList) throws Exception{
		cDoneCodeDao.save(dcList.toArray(new CDoneCode[dcList.size()]));
		cDoneCodeDetailDao.save(dcdList.toArray(new CDoneCodeDetail[dcdList.size()]));
	}
	
	public void updateCustStatus(List<CCust> custList,
			List<CCustPropChange> cpcList, List<CDoneCode> dcList,
			List<CDoneCodeDetail> dcdList) throws Exception {
		cCustDao.update(custList.toArray(new CCust[custList.size()]));
		cCustPropChangeDao.save(cpcList.toArray(new CCustPropChange[cpcList.size()]));
		saveDoneCodeInfo(dcList, dcdList);
	}
	
	public String renewCust(String custId,Integer doneCode) throws Exception {
		CCust cust = cCustDao.findByKey(custId);
		//最近一条status异动
		CCustPropChange cpchange = cCustPropChangeDao.queryPropByCustIdAndColumn(custId, getOptr().getCounty_id(),"status");
		String newValue = (cpchange != null) ? cpchange.getOld_value() : StatusConstants.ACTIVE;
		
		List<CCustPropChange> cpcList = new ArrayList<CCustPropChange>();
		CCustPropChange cpc = new CCustPropChange();
		cpc.setCust_id(custId);
		cpc.setColumn_name("status");
		cpc.setOld_value(cust.getStatus());
		cpc.setNew_value(newValue);
		cpcList.add(cpc);
		editCust(doneCode, custId, cpcList);
		return newValue;
	}
	
	/**
	 * @param custIds
	 */
	public void batchLogoffCust(Integer doneCode,String remark,List<String> custIds,String isReclaimDevice,String deviceStatus) throws Exception {
		cCustDao.batchLogoffCust(doneCode,remark,custIds,isReclaimDevice, deviceStatus,getOptr());
	}
	
	public Pager<TNonresCustApproval> queryNonresCustApp(String status,String query,Integer start,Integer limit) throws Exception {
		return tNonresCustApprovalDao.queryNonresCustApp(status, query, start, limit);
	}
	

	/**
	 * 恢复已注销的客户.
	 * @param custId
	 * @return
	 */
	public CustFullInfoDto restoeCust(String custId,Integer doneCode) throws Exception{
		CustFullInfoDto custFullInfo = searchCustInfoById(custId);
		CCust cust = custFullInfo.getCust();
		
		CCustPropChange cpc = new CCustPropChange();
		cpc.setArea_id(cust.getAddr_id());
		cpc.setCounty_id(cust.getCounty_id());
		cpc.setCust_id(cust.getCust_id());
		cpc.setColumn_name("status");
		cpc.setOld_value(cust.getStatus());
		//更改状态
		cust.setStatus(StatusConstants.ACTIVE);
		cpc.setNew_value(cust.getStatus());
		cpc.setDone_code(doneCode);
		cCustPropChangeDao.save(cpc);
		
		
		CCustLinkman linkman = custFullInfo.getLinkman();
		if(linkman ==null){
			throw new ComponentException("获取客户的联系人信息出错!");
		}
		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put("cust_id", custId);
		
		CCustAddrHis addrHis = cCustAddrHisDao.findByKey(custId);
		if(null == addrHis){
			throw new ComponentException("获取客户的地址信息出错!");
		}
		CCustAddr addr = new CCustAddr();
		BeanUtils.copyProperties(addrHis, addr);
		
		cCustDao.save(cust);
		cCustAddrDao.save(addr);
		cCustLinkmanDao.save(linkman);
		
		cCustHisDao.remove(custId);
		cCustLinkmanHisDao.remove(custId);
		cCustAddrHisDao.remove(custId);
		
		//同步客户开户数量限制
		addCustColony(cust.getCust_id());
		
		return custFullInfo;
	}
	
	public List<TProvince> queryProvince() throws Exception{
		return tProvinceDao.queryProvince();
	}
	
	public void setCCustDao(CCustDao custDao) {
		cCustDao = custDao;
	}
	
	public void setCCustLinkmanDao(CCustLinkmanDao custLinkmanDao) {
		cCustLinkmanDao = custLinkmanDao;
	}

	public void setCCustUnitToResidentDao(
			CCustUnitToResidentDao custUnitToResidentDao) {
		cCustUnitToResidentDao = custUnitToResidentDao;
	}

	public void setCCustPropChangeDao(CCustPropChangeDao custPropChangeDao) {
		cCustPropChangeDao = custPropChangeDao;
	}

	public void setCCustDeviceDao(CCustDeviceDao custDeviceDao) {
		cCustDeviceDao = custDeviceDao;
	}
	public void setCCustHisDao(CCustHisDao custHisDao) {
		cCustHisDao = custHisDao;
	}
	public void setCCustLinkmanHisDao(CCustLinkmanHisDao custLinkmanHisDao) {
		cCustLinkmanHisDao = custLinkmanHisDao;
	}
	public void setCCustDeviceChangeDao(CCustDeviceChangeDao custDeviceChangeDao) {
		cCustDeviceChangeDao = custDeviceChangeDao;
	}
	public void setCCustAddrDao(CCustAddrDao custAddrDao) {
		cCustAddrDao = custAddrDao;
	}
	public void setTDeviceBuyModeDao(TDeviceBuyModeDao deviceBuyModeDao) {
		tDeviceBuyModeDao = deviceBuyModeDao;
	}

	public void setCCustAddrHisDao(CCustAddrHisDao custAddrHisDao) {
		cCustAddrHisDao = custAddrHisDao;
	}

	public void setCCustBonuspointDao(CCustBonuspointDao custBonuspointDao) {
		cCustBonuspointDao = custBonuspointDao;
	}

	public void setCCustDeviceHisDao(CCustDeviceHisDao custDeviceHisDao) {
		cCustDeviceHisDao = custDeviceHisDao;
	}

	public void setTAddressDao(TAddressDao addressDao) {
		tAddressDao = addressDao;
	}

	public void setCAcctBankDao(CAcctBankDao acctBankDao) {
		cAcctBankDao = acctBankDao;
	}
	public void setTCustColonyCfgDao(TCustColonyCfgDao custColonyCfgDao) {
		tCustColonyCfgDao = custColonyCfgDao;
	}
	/**
	 * 
	 */
	public void test(String optrId) throws ServicesException {
		if (getOptr().getOptr_id().equals(optrId)){
			LoggerHelper.error(this.getClass(), "error "+Thread.currentThread());
		}
	}

	public void setCCustDeviceBuymodePropDao(
			CCustDeviceBuymodePropDao custDeviceBuymodePropDao) {
		cCustDeviceBuymodePropDao = custDeviceBuymodePropDao;
	}

	public void setTNonresCustApprovalDao(
			TNonresCustApprovalDao nonresCustApprovalDao) {
		tNonresCustApprovalDao = nonresCustApprovalDao;
	}

	public void setTDistrictDao(TDistrictDao districtDao) {
		this.tDistrictDao = districtDao;
	}

	public void setTProvinceDao(TProvinceDao provinceDao) {
		this.tProvinceDao = provinceDao;
	}




}
