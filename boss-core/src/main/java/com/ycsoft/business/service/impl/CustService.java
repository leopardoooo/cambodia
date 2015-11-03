package com.ycsoft.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TDeviceBuyMode;
import com.ycsoft.beans.config.TNonresCustApproval;
import com.ycsoft.beans.core.acct.CAcct;
import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.beans.core.acct.CAcctAcctitemOrder;
import com.ycsoft.beans.core.bill.BBill;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.common.CDoneCodeDetail;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustAddr;
import com.ycsoft.beans.core.cust.CCustDevice;
import com.ycsoft.beans.core.cust.CCustDeviceChange;
import com.ycsoft.beans.core.cust.CCustLinkman;
import com.ycsoft.beans.core.cust.CCustPropChange;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.device.RDevice;
import com.ycsoft.beans.device.RDeviceFee;
import com.ycsoft.beans.device.RDeviceModel;
import com.ycsoft.beans.device.RDeviceReclaim;
import com.ycsoft.beans.device.RStb;
import com.ycsoft.beans.device.RStbModel;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.component.core.OrderComponent;
import com.ycsoft.business.component.task.SnTaskComponent;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dto.config.TAddressDto;
import com.ycsoft.business.dto.core.acct.AcctAcctitemActiveDto;
import com.ycsoft.business.dto.core.acct.AcctitemDto;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.dto.core.cust.CustOTTMobile;
import com.ycsoft.business.dto.core.fee.CFeePayDto;
import com.ycsoft.business.dto.core.fee.FeeBusiFormDto;
import com.ycsoft.business.dto.core.fee.FeeInfoDto;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.CustProdDto;
import com.ycsoft.business.dto.core.prod.OrderProd;
import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.business.dto.core.user.UserRes;
import com.ycsoft.business.dto.device.DeviceDto;
import com.ycsoft.business.service.ICustService;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.NumericHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.resource.RDeviceModelTotalDto;

@Service
public class CustService extends BaseBusiService implements ICustService {
	@Autowired
	private OrderComponent orderComponent;
	@Autowired
	private SnTaskComponent snTaskComponent;
	@Autowired
	private CUserDao cUserDao;
	/**
	 * 创建新客户
	 * @param busiCode
	 * @param cust
	 * @param linkman
	 * @param resident
	 * @throws Exception
	 */
	public void createCust(CCust cust, CCustLinkman linkman,String custCode) throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//保存客户信息
		String custId = custComponent.createCust(cust,linkman,custCode);
		//为客户创建公用账户
		acctComponent.createAcct(custId,null, SystemConstants.ACCT_TYPE_PUBLIC, null);
		//设置拦截器所需要的参数
		getBusiParam().getCustFullInfo().setCust(cust);
		getBusiParam().getCustFullInfo().setLinkman(linkman);

		//设置扩展表信息
		setExtAttrInfo("EXT_C_CUST", "cust_id", custId);

//		saveAllPublic(doneCode,getBusiParam(),"开户日期:"+DateHelper.dateToStr(cust.getOpen_time()));
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/**
	 * 批量预开户
	 */
	public void createCustBatch(String addrId, String address,String custName,int custCount,List<CCustAddr> addrList)
			throws Exception {
		CCust cust =new CCust();
		CCustLinkman linkman = new CCustLinkman();
		cust.setCust_type(SystemConstants.CUST_TYPE_RESIDENT);
		cust.setAddr_id(addrId);
		Integer doneCode = doneCodeComponent.gDoneCode();
		List<String> custIds = new ArrayList<String>();
		for (int i=1;i<=custCount;i++){
			cust.setCust_name(custName+i);
			cust.setCust_class(SystemConstants.CUST_CLASS_YBKH);
			cust.setCust_colony("YBKH");
			if(addrList.size()>0){
				CCustAddr cAddr = addrList.get(i-1);
				String addr = findAddr(address,cAddr);
				cust.setT1(cAddr.getT1());
				cust.setT2(cAddr.getT2());
				cust.setT3(cAddr.getT3());
				cust.setT4(cAddr.getT4());
				cust.setT5(cAddr.getT5());
				cust.setNote(cAddr.getNote());
				cust.setAddress(addr);
				linkman.setMail_address(addr);
			}else{
				cust.setAddress(address);
				linkman.setMail_address(address);
			}
			String custId = custComponent.createCust(cust,linkman,null);
			//为客户创建公用账户
			acctComponent.createAcct(custId,null, SystemConstants.ACCT_TYPE_PUBLIC, null);
			
			//检查支付信息是否为NULL，如果不为NULL则保存支付信息，并根据一定的规则保存合并记录。
			CFeePayDto pay = getBusiParam().getPay();
			String payType = SystemConstants.PAY_TYPE_CASH;
			if(null != pay){
				payType = pay.getPay_type();
			}
			if (getBusiParam().getFees() !=null){
				for (FeeBusiFormDto feeDto : getBusiParam().getFees()) {
					if(feeDto.getCount()!=custCount){
						throw new ServicesException("杂费中的户数与预开户数不一致!");
					}
					if(feeDto.getReal_pay() > 0){
						feeComponent.saveBusiFee(custId,addrId, feeDto.getFee_id(), 1,payType,feeDto
								.getReal_pay()/custCount, doneCode,doneCode, BusiCodeConstants.CUST_BATCH_OPEN,
								null,null);
					}
				}
			}
			custIds.add(custId);
			
		}
		doneCodeComponent.saveBatchDoneCode(doneCode, BusiCodeConstants.CUST_BATCH_OPEN,null, custIds, null);
	}
	
	private String findAddr(String address,CCustAddr addr) throws Exception{
		if(StringHelper.isEmpty(address)){
			throw new ServicesException("小区不能为空!");
		}
		if(StringHelper.isNotEmpty(addr.getT1()))
			address =address + addr.getT1()+"号";
		if(StringHelper.isNotEmpty(addr.getT2()))
			address =address + addr.getT2()+"栋";
		if(StringHelper.isNotEmpty(addr.getT3()))
			address =address + addr.getT3()+"单元";
		if(StringHelper.isNotEmpty(addr.getT4()))
			address =address + addr.getT4()+"楼";
		if(StringHelper.isNotEmpty(addr.getT5()))
			address =address + addr.getT5()+"室";
		if(StringHelper.isNotEmpty(addr.getNote()))
			address =address + addr.getNote();
		return address;
	}
	

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.ICustService#editCustClass(java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.lang.String, java.util.Date)
	 */
	public void editCustClass(String userId, String prodSn, String newTariffId,
			String tariffStartDate,String expDate, String custClass, String custClassDate)
			throws Exception {
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//保存客户信息
		CCust cust = custComponent.queryCustById(this.getBusiParam().getCust().getCust_id());
		List<CCustPropChange> changeList = custComponent.updateCustClass(doneCode, cust, custClass, custClassDate);
		doneCodeComponent.saveDoneCodeInfo(doneCode, cust.getCust_id(),null, changeList);
		if(StringHelper.isNotEmpty(prodSn)){
			changeTariff(prodSn, newTariffId, tariffStartDate, null, true, true, doneCode);
		}
		
		//生成计算信用度任务
		jobComponent.createCreditCalJob(doneCode, cust.getCust_id(), null,SystemConstants.BOOLEAN_TRUE);
		
		saveAllPublic(doneCode, getBusiParam());
	}
	
	public void editCustLevel(String cust_level) throws Exception{
		List<CCustPropChange> propChangeList = new ArrayList<CCustPropChange>();
		CCustPropChange propChange = new CCustPropChange();
		propChange.setColumn_name("cust_level");
		propChange.setOld_value(this.getBusiParam().getCust().getCust_level());
		propChange.setNew_value(cust_level);
		propChangeList.add(propChange);
		editCust(propChangeList);
	}
	
	public void editCust(List<CCustPropChange> propChangeList) throws Exception {
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//保存客户信息
		CCust cust = getBusiParam().getCustFullInfo().getCust();
		for (int i=propChangeList.size()-1;i>=0;i--){
			CCustPropChange change = propChangeList.get(i);
			if (change.getNew_value().equals(change.getOld_value()))
				propChangeList.remove(i);
		}
		custComponent.editCust(doneCode,cust.getCust_id(), propChangeList);
		String busiCode = getBusiParam().getBusiCode();
		Map<String, Object> doneInfo = new HashMap<String, Object>();
		/*
		//如果是移机或者是过户业务，且客户状态为拆迁
		if((BusiCodeConstants.CUST_CHANGE_ADDR.equals(busiCode)|| BusiCodeConstants.CUST_TRANS.equals(busiCode) ) ){
			//修改拆迁客户状态为正常
			if(StatusConstants.RELOCATE.equals(cust.getStatus())){
				List<CCustPropChange> updateCustStatus = custComponent.updateCustStatus(doneCode,cust.getCust_id(),cust.getStatus(),StatusConstants.ACTIVE);
				CCustPropChange change = updateCustStatus.get(0);
				if(change!=null){
					change.setColumn_name_text("状态");
					propChangeList.add(change);
				}
			}
			
			if(BusiCodeConstants.CUST_CHANGE_ADDR.equals(busiCode)){//移机
				CCust xhCust = new CCust();
				BeanUtils.copyProperties(cust, xhCust);
				xhCust.setCust_id(null);
				xhCust.setCust_no(null);
				xhCust.setStatus(StatusConstants.TRANSFER_LOGOFF);
				xhCust.setRemark("移出客户的客户编号为:"+cust.getCust_no());
				xhCust.setOld_cust_no("");
				String newCustId = custComponent.createCust(xhCust, new CCustLinkman());
				xhCust = custComponent.queryCustById(newCustId);
				if (StringHelper.isEmpty(this.getBusiParam().getRemark()))
					this.getBusiParam().setRemark("移出地址的受理编号为:"+xhCust.getCust_no());
				else 
					this.getBusiParam().setRemark(this.getBusiParam().getRemark()+",移出地址的受理编号为:"+xhCust.getCust_no());
			}else if(BusiCodeConstants.CUST_TRANS.equals(busiCode)){//过户
				
			}
			
			
		}
		*/
		boolean isMoveTask = false;
		String newAddress = "";
		//判断客户地址是否变化，如果有变化，设置旧客户地址
		for (CCustPropChange change:propChangeList){
			change.fillPropertyChineseText();
			if (change.getColumn_name().equals("address")){
				getBusiParam().getTempVar().put(SystemConstants.EXTEND_ATTR_KEY_NEWADDR,
						cust.getAddress());
				isMoveTask = true;
				newAddress = change.getNew_value();
				break;
			}
			
		}
		//生成移机工单
		if(BusiCodeConstants.CUST_CHANGE_ADDR.equals(busiCode) && isMoveTask){
			snTaskComponent.createMoveTask(doneCode, cust, null, newAddress, getBusiParam().getWorkBillAsignType());
			snTaskComponent.saveTaskCreateBusiExt(cust.getCust_id(), doneCode, getBusiParam());
		}
		
		//设置扩展表信息
		setExtAttrInfo("EXT_C_CUST", "cust_id", cust.getCust_id());
				
		//生成计算信用度任务
//		jobComponent.createCreditCalJob(doneCode, cust.getCust_id(), null,SystemConstants.BOOLEAN_TRUE);
		
		doneInfo.put("changes", propChangeList);
		doneCodeComponent.saveDoneCodeInfo(doneCode, cust.getCust_id() ,null, doneInfo );
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public void saveRemoveCust(String banlanceDealType) throws Exception {
		//保存客户信息
		CCust cust = getBusiParam().getCustFullInfo().getCust();
		
		// 销户之前先检查该客户名下是否有未退的押金
		List<CFee> depositFees = feeComponent.hasDepositInCust(cust.getCust_id());
		if(depositFees.size() > 0){
			StringBuffer sb = new StringBuffer("客户名下发现未退的押金[");
			for (CFee fee : depositFees) {
				sb.append(fee.getFee_name() + ": " + NumericHelper.changeF2Y(new Long(fee.getReal_pay())) );
			}
			sb.append(" ]，请前往受理记录 - 修改费用功能退回押金!");
			throw new ServicesException(sb.toString());
		}
		
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		//终止账目
		CAcct acct = acctComponent.queryCustAcctByCustId(cust.getCust_id());
		if (banlanceDealType.equals(SystemConstants.ACCT_BALANCE_REFUND)){
			List<CAcctAcctitem> acctItemList = acctComponent.queryAcctItemByAcctId(acct.getAcct_id());
			for (CAcctAcctitem acctItem :acctItemList){
				terminateAcctItem(cust.getCust_id(), doneCode, busiCode,
						null, acct.getAcct_id(), acctItem.getAcctitem_id(),
						banlanceDealType, null, null);
			}
		}
		//终止账户
//		acctComponent.removeAcctWithHis(acct,doneCode,busiCode);
		//删除客户设备
		custComponent.removeAllDevice(cust.getCust_id(),doneCode);
		//删除客户
		/*custComponent.removeCustWithHis(doneCode, cust,
				 getBusiParam().getCustFullInfo().getLinkman(),false);*/
		
		//客户销户，不删除账目，只修改客户状态
		cust = custComponent.queryCustById(cust.getCust_id());
		List<CCustPropChange> propChangeList = new ArrayList<CCustPropChange>();
		propChangeList.add(new CCustPropChange("status", cust.getStatus(), StatusConstants.INVALID));
		custComponent.editCust(doneCode, cust.getCust_id(), propChangeList);

		doneCodeComponent.saveDoneCodeInfo(doneCode, cust.getCust_id() ,null, "" );
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public CustFullInfoDto restoeCust(String custId) throws ServicesException{
		CustFullInfoDto custFullInfo = null;
		try { 
			Integer doneCode = doneCodeComponent.gDoneCode();
			custFullInfo = custComponent.restoeCust(custId,doneCode);
			BusiParameter busiParam = getBusiParam();
			acctComponent.restorePublicAcctInfo(custId,doneCode,busiParam.getBusiCode());
			saveAllPublic(doneCode, busiParam);
			return custFullInfo ;
		} catch (Exception e) {
			throw new ServicesException(e);
		}
	}

	public void saveBuyDevice(String deviceId, String cardId, String modemMac,
			String buyMode, List<FeeInfoDto> feeInfoList,
			String virtualCard, String virtualModem) throws Exception {
		BusiParameter param = (BusiParameter)getParam();
		CCust cust = param.getCustFullInfo().getCust();
		String busiCode = param.getBusiCode();
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		DeviceDto device = deviceComponent.queryDeviceByDeviceId(deviceId);
		device.setBuy_mode(buyMode);
		getBusiParam().setBusiConfirmParam("device", device);
		
		buyDevice(device, buyMode, feeInfoList,busiCode , cust, doneCode,null);
		//不是虚拟卡
		if (StringHelper.isNotEmpty(cardId) && virtualCard.equals(SystemConstants.BOOLEAN_FALSE)) {
			DeviceDto card = deviceComponent.queryDeviceByDeviceId(cardId);
//			getBusiParam().setBusiConfirmParam("card", card);
			buyDevice(card, buyMode, null, busiCode, cust, doneCode,null);
		}
		//不是虚拟MODEM
		if (StringHelper.isNotEmpty(modemMac) && virtualModem.equals(SystemConstants.BOOLEAN_FALSE)) {
			DeviceDto modem = deviceComponent.queryDeviceByDeviceId(modemMac);
//			getBusiParam().setBusiConfirmParam("modem", modem);
			buyDevice(modem, buyMode, null, busiCode, cust, doneCode,null);
		}
		//记录打印信息
		Map<String, Object> deviceInfo = getBusiParam().getBusiConfirmParamInfo();
		doneCodeComponent.saveDoneCodeInfo(doneCode, cust.getCust_id() ,null, deviceInfo);
		
		//获得设备型号
		getBusiParam().setRemark(getBusiParam().getRemark()+" 设备型号: "+device.getDevice_model_text()+"; 设备编号: "+device.getDevice_code());
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public void saveBuyReplacover(String deviceId,String deviceCode) throws Exception{
		Integer doneCode = doneCodeComponent.gDoneCode();
		custComponent.saveBuyReplacover(deviceId,doneCode);
		saveAllPublic(doneCode, getBusiParam());
	}
	
	public void saveChangeOwnership(String deviceId) throws Exception{
		BusiParameter param = (BusiParameter)getParam();
		String busiCode = param.getBusiCode();
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		DeviceDto device = deviceComponent.queryDeviceByDeviceId(deviceId);
		String ownership = SystemConstants.OWNERSHIP_CUST;
		if(null != device && SystemConstants.OWNERSHIP_CUST.equals(device.getOwnership())){
			ownership = SystemConstants.OWNERSHIP_GD;
		}
		
		deviceComponent.updateDeviceOwnership(doneCode, busiCode, deviceId,device.getOwnership(),ownership,null,true);
		
		if(device.getPairCard() != null){
			DeviceDto pairCard = deviceComponent.queryDeviceByDeviceId(device.getPairCard().getDevice_id());
			if(pairCard.getDevice_id() != null){
				deviceComponent.updateDeviceOwnership(doneCode, busiCode, pairCard.getDevice_id(),pairCard.getOwnership(),ownership,null,false);
			}
		}
		if(device.getPairModem() != null){
			DeviceDto pairModem = deviceComponent.queryDeviceByDeviceId(device.getPairModem().getDevice_id());
			if(pairModem.getDevice_id() != null){
				deviceComponent.updateDeviceOwnership(doneCode, busiCode, pairModem.getDevice_id(),pairModem.getOwnership(),ownership,null,false);
			}
		}
		
		
//		saveAllPublic(doneCode,getBusiParam(),"");
		saveAllPublic(doneCode,getBusiParam());
	}

	private void buyDevice(DeviceDto device, String buyMode,
			List<FeeInfoDto> feeInfoList, String busiCode, CCust cust,
			Integer doneCode, String oldDeviceOwnShip) throws JDBCException,Exception {
		if(device == null){
			throw new ServicesException("未能正确的获取设备信息,请重新确认是否输入正确.");
		}
		//虚拟卡
		if(device.getIs_virtual().equals(SystemConstants.BOOLEAN_FALSE)){
			//查询设备的基本信息
			
			DeviceDto pairDevice = null, pairModemDevice = null;
			String is_card_virtual = SystemConstants.BOOLEAN_FALSE,is_modem_virtual = SystemConstants.BOOLEAN_FALSE;
			if (device.getPairCard() != null){
				pairDevice = deviceComponent.queryDeviceByDeviceId(device.getPairCard().getDevice_id());
				is_card_virtual = pairDevice.getIs_virtual();
			}
			if (device.getPairModem() != null){
				pairModemDevice = deviceComponent.queryDeviceByDeviceId(device.getPairModem().getDevice_id());
				is_modem_virtual = pairModemDevice.getIs_virtual();
			}
			//增加客户设备
			custComponent.addDevice(doneCode,cust.getCust_id(), device.getDevice_id(),
					device.getDevice_type(),
					device.getDevice_code(),
					pairDevice==null?null:pairDevice.getDevice_id(),
					pairDevice==null?null:pairDevice.getDevice_code(),
					pairModemDevice==null?null:pairModemDevice.getDevice_id(),
					pairModemDevice==null?null:pairModemDevice.getDevice_code(),
					buyMode);
			if (pairDevice != null && is_card_virtual.equals(SystemConstants.BOOLEAN_FALSE)){
				custComponent.addDevice(doneCode,cust.getCust_id(), pairDevice.getDevice_id(),
					pairDevice.getDevice_type(), pairDevice.getDevice_code(),"", "","","", buyMode);
			}
			if (pairModemDevice != null && is_modem_virtual.equals(SystemConstants.BOOLEAN_FALSE)){
				custComponent.addDevice(doneCode,cust.getCust_id(), pairDevice.getDevice_id(),
					pairDevice.getDevice_type(), pairDevice.getDevice_code(),"", "","","", buyMode);
			}
			/*//增加设备销售费用记录
			if (StringHelper.isNotEmpty(feeId)){
				String payType = SystemConstants.PAY_TYPE_CASH;
				if (this.getBusiParam().getPay()!= null && this.getBusiParam().getPay().getPay_type() !=null)
					payType = this.getBusiParam().getPay().getPay_type();
				feeComponent.saveDeviceFee( cust.getCust_id(), feeId,feeStdId, payType,device.getDevice_type(), device.getDevice_id(), device.getDevice_code(),
						pairDevice==null?null:pairDevice.getDevice_id(),
						pairDevice==null?null:pairDevice.getDevice_code(),
						pairModemDevice==null?null:pairModemDevice.getDevice_id(),
						pairModemDevice==null?null:pairModemDevice.getDevice_code(),
						fee, doneCode,doneCode, busiCode);
			}*/
			if(feeInfoList != null){
				for(int i=0,len=feeInfoList.size();i<len;i++){
					FeeInfoDto dto = feeInfoList.get(i);
					String feeId = dto.getFee_id();
					String feeStdId = dto.getFee_std_id();
					Integer fee = dto.getFee();
					
					String payType = SystemConstants.PAY_TYPE_CASH;
					if (this.getBusiParam().getPay()!= null && this.getBusiParam().getPay().getPay_type() !=null)
						payType = this.getBusiParam().getPay().getPay_type();
					feeComponent.saveDeviceFee( cust.getCust_id(), cust.getAddr_id(),feeId,feeStdId, payType,device.getDevice_type(), device.getDevice_id(), device.getDevice_code(),
							pairDevice==null?null:pairDevice.getDevice_id(),
							pairDevice==null?null:pairDevice.getDevice_code(),
							pairModemDevice==null?null:pairModemDevice.getDevice_id(),
							pairModemDevice==null?null:pairModemDevice.getDevice_code(),device.getDevice_model(),
							fee, doneCode,doneCode, busiCode, 1);
				}
			}
			//购买方式异动
			deviceComponent.updateBuyMode(doneCode, busiCode, device.getDevice_id(),null,buyMode);
			
			//修改设备的仓库状态为占用，如果设备有配对的智能卡，修改智能卡的状态为占用
			deviceComponent.updateDeviceDepotStatus(doneCode, busiCode, device.getDevice_id(),device.getDepot_status(), StatusConstants.USE,null,true);
			if (pairDevice != null)
				deviceComponent.updateDeviceDepotStatus(doneCode, busiCode, pairDevice.getDevice_id(),pairDevice.getDepot_status(), StatusConstants.USE,null,false);
			if (pairModemDevice != null)
				deviceComponent.updateDeviceDepotStatus(doneCode, busiCode, pairModemDevice.getDevice_id(),pairModemDevice.getDepot_status(), StatusConstants.USE,null,false);
			//根据设备的购买方式判断是否需要修改设备的产权
			if (StringHelper.isNotEmpty(oldDeviceOwnShip)){
				if ( oldDeviceOwnShip.equals(SystemConstants.OWNERSHIP_CUST)){
					deviceComponent.updateDeviceOwnership(doneCode, busiCode, device.getDevice_id(),device.getOwnership(),SystemConstants.OWNERSHIP_CUST,null,true);
					if (pairDevice != null)
						deviceComponent.updateDeviceOwnership(doneCode, busiCode, pairDevice.getDevice_id(),pairDevice.getOwnership(), SystemConstants.OWNERSHIP_CUST,null,false);
					if (pairModemDevice != null)
						deviceComponent.updateDeviceOwnership(doneCode, busiCode, pairModemDevice.getDevice_id(),pairModemDevice.getOwnership(), SystemConstants.OWNERSHIP_CUST,null,false);
				}
			} else {
				TDeviceBuyMode deviceBuyMode = busiConfigComponent.queryBuyMode(buyMode);
				if (SystemConstants.BOOLEAN_TRUE.equals(deviceBuyMode.getChange_ownship())){
					deviceComponent.updateDeviceOwnership(doneCode, busiCode, device.getDevice_id(),device.getOwnership(),SystemConstants.OWNERSHIP_CUST,buyMode,true);
					if (pairDevice != null)
						deviceComponent.updateDeviceOwnership(doneCode, busiCode, pairDevice.getDevice_id(),pairDevice.getOwnership(), SystemConstants.OWNERSHIP_CUST,buyMode,false);
					if (pairModemDevice != null)
						deviceComponent.updateDeviceOwnership(doneCode, busiCode, pairModemDevice.getDevice_id(),pairModemDevice.getOwnership(), SystemConstants.OWNERSHIP_CUST,buyMode,false);
				}			
			}
			
			//更新设备为旧设备
			if (device.getUsed().equals(SystemConstants.BOOLEAN_TRUE))
				deviceComponent.updateDeviceUsed(doneCode, busiCode, device.getDevice_id(), SystemConstants.BOOLEAN_TRUE, SystemConstants.BOOLEAN_FALSE,true);
			if (pairDevice != null && pairDevice.getUsed().equals(SystemConstants.BOOLEAN_TRUE))
				deviceComponent.updateDeviceUsed(doneCode, busiCode, pairDevice.getDevice_id(), SystemConstants.BOOLEAN_TRUE, SystemConstants.BOOLEAN_FALSE,false);
			if (pairModemDevice != null && pairModemDevice.getUsed().equals(SystemConstants.BOOLEAN_TRUE))
				deviceComponent.updateDeviceUsed(doneCode, busiCode, pairModemDevice.getDevice_id(), SystemConstants.BOOLEAN_TRUE, SystemConstants.BOOLEAN_FALSE,false);
			
			deviceComponent.saveDeviceUseRecords(doneCode, busiCode, device.getDevice_id(),
					device.getDevice_type(), device.getDevice_code(), cust.getCust_id(), cust.getCust_no());
			if (pairDevice != null){
				deviceComponent.saveDeviceUseRecords(doneCode, busiCode, pairDevice.getDevice_id(),
						pairDevice.getDevice_type(), pairDevice.getDevice_code(), cust.getCust_id(), cust.getCust_no());
			}
			if (pairModemDevice != null){
				deviceComponent.saveDeviceUseRecords(doneCode, busiCode, pairModemDevice.getDevice_id(),
						pairModemDevice.getDevice_type(), pairModemDevice.getDevice_code(), cust.getCust_id(), cust.getCust_no());
			}
		}
	}
	
	

	
	/** 
	 * 保存购买器材
	 */
	public void saveBuyMaterial(String deviceType, String deviceModel,
			String buyMode, List<FeeInfoDto> feeInfoList, int buyNum)
			throws Exception {
		CCust cust = getBusiParam().getCustFullInfo().getCust();
		doneCodeComponent.lockCust(cust.getCust_id());
		String busiCode = getBusiParam().getBusiCode();
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//获取本地该器材的数量
		RDevice device = deviceComponent.queryTotalNumDevice(deviceModel, getOptr().getDept_id());
		//本地器材数量减去已购数量
		deviceComponent.removeTotalNumDevice(doneCode,BusiCodeConstants.DEVICE_BUY_PJ,device.getDevice_id(), buyNum,buyMode,getOptr());
		
		//保存设备销售费用
		if(feeInfoList != null){
			//记录未支付业务
			doneCodeComponent.saveDoneCodeUnPay(cust.getCust_id(), doneCode, getOptr().getOptr_id());
			
			for(int i=0,len=feeInfoList.size();i<len;i++){
				FeeInfoDto dto = feeInfoList.get(i);
				String feeId = dto.getFee_id();
				String feeStdId = dto.getFee_std_id();
				Integer fee = dto.getFee().intValue() * buyNum;
				
				feeComponent.saveDeviceFee( cust.getCust_id(),cust.getAddr_id(), feeId,feeStdId, StatusConstants.UNPAY,deviceType, 
						device.getDevice_id(), null, null, null, null, null,deviceModel,
						fee, doneCode,doneCode, busiCode, buyNum);
			}
		}
		
		saveAllPublic(doneCode,getBusiParam());
		
	}
	
	
	public void transferCust(CCust srcCust) throws ServicesException {
		try{
			if(srcCust == null ){
				throw new IllegalArgumentException("参数有误,没有获取需要的迁移的客户数据!");
			}
			String srcCustId = srcCust.getCust_id();
			//需要迁移的客户不存在,抛错.
			if(StringHelper.isEmpty(srcCustId)){
				throw new IllegalArgumentException("参数有误,没有获取需要的迁移的客户ID!");
			}
			srcCust = custComponent.queryCustById(srcCustId);
			CCustLinkman srcLinkMan = custComponent.queryCustLinkmanById(srcCustId);
			CCust targetCust = getBusiParam().getCust();
			String targetCustId = targetCust.getCust_id();
			Integer doneCode = doneCodeComponent.gDoneCode();
			
			List<CCustPropChange> propChangeList = new ArrayList<CCustPropChange>();
			CCustPropChange custNoChange = new CCustPropChange();
			custNoChange.setColumn_name("cust_no");
			custNoChange.setOld_value(srcCust.getCust_no());
			custNoChange.setNew_value(targetCust.getCust_no());
			propChangeList.add(custNoChange);
			
			if(!srcCust.getAddress().equals(targetCust.getAddress())){
				CCustPropChange addressChange = new CCustPropChange();
				addressChange.setColumn_name("address");
				addressChange.setOld_value(srcCust.getAddress());
				addressChange.setNew_value(targetCust.getAddress());
				propChangeList.add(addressChange);
			}
			
			
			String busiCode = getBusiParam().getBusiCode();
			
			if(targetCust.getStatus().equals(StatusConstants.PREOPEN)){//修改客户状态为正常状态
				targetCust.setStatus(StatusConstants.ACTIVE);
				CCustPropChange propChange = new CCustPropChange();
				propChange.setColumn_name("status");
				propChange.setOld_value(StatusConstants.PREOPEN);
				propChange.setNew_value(StatusConstants.ACTIVE);
				propChangeList.add(propChange);
			}
			//修改属性.
//			editCust(propChangeList);
			custComponent.editCust(doneCode,targetCust.getCust_id(), propChangeList);
			//判断客户地址是否变化，如果有变化，设置旧客户地址
			for (CCustPropChange change:propChangeList){
				if (change.getColumn_name().equals("addr_id") ||
					change.getColumn_name().equals("address")){
					getBusiParam().getTempVar().put(SystemConstants.EXTEND_ATTR_KEY_NEWADDR,targetCust.getAddress());
					break;
				}
			}
			//设置扩展表信息
			setExtAttrInfo("EXT_C_CUST", "cust_id", targetCust.getCust_id());
			
			//复制来源客户设备到目标客户下,记录设备移动记录
			custComponent.updateCustDevice(doneCode, srcCustId, targetCustId);
			
			CAcct acct = acctComponent.queryCustAcctByCustId(srcCustId);
			//复制公用账户、账目
			acctComponent.updateAcct(doneCode, busiCode, acct.getAcct_id(), targetCustId, null);
			
			List<CUser> resUserList = userComponent.queryUserByCustId(srcCustId);
			for(CUser oldUser : resUserList){
				String newUserId = userComponent.gUserId();		//新建用户ID
				//原双向用户 销户指令，双向用户按用户id来授权
				CUser oldUserDto = userComponent.queryUserById(oldUser.getUser_id());
//				if(oldUserDto.getServ_type().equals(SystemConstants.DTV_SERV_TYPE_DOUBLE)){
//					delUserJob(oldUser, oldUser.getCust_id(), doneCode);
//				}
				
				//复制用户
				userComponent.updateUser(doneCode, busiCode, oldUser, targetCustId, newUserId,false);
				
				CUser newUser = userComponent.queryUserById(newUserId);
				//新双向用户发开户指令
//				if(newUser.getServ_type().equals(SystemConstants.DTV_SERV_TYPE_DOUBLE)){
//					createUserJob(newUser, newUser.getCust_id(), doneCode);
//				}
				
				//复制专用账户、账目
				CAcct oldAcct = acctComponent.queryUserAcct(oldUser.getCust_id(), oldUser.getUser_id());
				String newAcctId = acctComponent.updateAcct(doneCode, busiCode, oldAcct.getAcct_id(), targetCustId, newUserId);
				
				//复制产品
				userProdComponent.updateProd(doneCode, busiCode, oldUser.getUser_id(), targetCustId, newUserId, newAcctId);
			}
			
			//删除客户
			custComponent.removeCustWithHis(doneCode, srcCust, srcLinkMan,true);
			
			jobComponent.createCustWriteOffJob(doneCode, srcCustId, SystemConstants.BOOLEAN_TRUE);
			jobComponent.createAcctModeCalJob(doneCode, srcCustId);
			jobComponent.createInvalidCalJob(doneCode, srcCustId);
			//给源客户增加一条流水记录
			String remark = srcCust.getCust_name() + "(" + srcCust.getCust_no() +") " +srcCust.getAddress()
					+ " 迁移到 " + targetCust.getCust_name() + "(" + targetCust.getCust_no() +") " +targetCust.getAddress();//客户编号的变化，姓名，地址
			List<CDoneCode> cdList = new ArrayList<CDoneCode>();
			CDoneCode sourceCustDoneCode = new CDoneCode();
			Integer srCustDoneCode = doneCodeComponent.gDoneCode();
			
			sourceCustDoneCode.setDone_code(srCustDoneCode);
			sourceCustDoneCode.setBusi_code(getBusiParam().getBusiCode());
			sourceCustDoneCode.setStatus(StatusConstants.ACTIVE);
			sourceCustDoneCode.setCounty_id(srcCust.getCounty_id());
			sourceCustDoneCode.setArea_id(srcCust.getArea_id());
			sourceCustDoneCode.setDept_id(getOptr().getDept_id());
			sourceCustDoneCode.setOptr_id(getOptr().getOptr_id());
			sourceCustDoneCode.setRemark(remark);
			cdList.add(sourceCustDoneCode);
			
			List<CDoneCodeDetail> cddList = new ArrayList<CDoneCodeDetail>();
			CDoneCodeDetail sourceCustDoneCodeDetail = new CDoneCodeDetail();
			sourceCustDoneCodeDetail.setDone_code(srCustDoneCode);
			sourceCustDoneCodeDetail.setCust_id(srcCustId);
			sourceCustDoneCodeDetail.setArea_id(srcCust.getArea_id());
			sourceCustDoneCodeDetail.setCounty_id(srcCust.getCounty_id());
			cddList.add(sourceCustDoneCodeDetail);
			custComponent.saveDoneCodeInfo(cdList, cddList);
			
			Map<String, CCustPropChange> converToMapSingle = CollectionHelper.converToMapSingle(propChangeList, "column_name");
			
			doneCodeComponent.saveDoneCodeInfo(doneCode, targetCustId,null, converToMapSingle );
			
			getBusiParam().setRemark( remark );
			saveAllPublic(doneCode, getBusiParam());
		}catch (Exception e) {
			throw new ServicesException(e);
		}
	}
	
	public void changeNonresCust(CCust nonresCust, CCustLinkman linkman) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		String residentCustId = getBusiParam().getCust().getCust_id();
		String nonresCustId = nonresCust.getCust_id();
		//非居民不存在，需开户
		if(StringHelper.isEmpty(nonresCustId)){
			custComponent.createCust(nonresCust, linkman,null);
			nonresCustId = nonresCust.getCust_id();
			acctComponent.createAcct(nonresCustId,null, SystemConstants.ACCT_TYPE_PUBLIC, null);
		}else{
			nonresCust = custComponent.queryCustById(nonresCustId);
		}
		if(nonresCust.getStatus().equals(StatusConstants.PREOPEN)){
			//修改客户状态为正常状态
			custComponent.updateCustStatus(doneCode,nonresCustId,StatusConstants.PREOPEN,StatusConstants.ACTIVE);
		}
		
		//复制居民客户设备到非居民客户下,记录设备移动记录
		custComponent.updateCustDevice(doneCode, residentCustId, nonresCustId);
		
		CAcct acct = acctComponent.queryCustAcctByCustId(residentCustId);
		//复制公用账户、账目
		acctComponent.updateAcct(doneCode, busiCode, acct.getAcct_id(), nonresCustId, null);
		
		List<CUser> resUserList = userComponent.queryUserByCustId(residentCustId);
		for(CUser oldUser : resUserList){
			String newUserId = userComponent.gUserId();		//新建用户ID
			//原双向用户 销户指令，双向用户按用户id来授权
			CUser oldUserDto = userComponent.queryUserById(oldUser.getUser_id());
//			if(oldUserDto.getServ_type().equals(SystemConstants.DTV_SERV_TYPE_DOUBLE)){
//				delUserJob(oldUser, oldUser.getCust_id(), doneCode);
//			}
			
			//复制用户
			userComponent.updateUser(doneCode, busiCode, oldUser, nonresCustId, newUserId);
			
			CUser newUser = userComponent.queryUserById(newUserId);
			//新双向用户发开户指令
//			if(newUser.getServ_type().equals(SystemConstants.DTV_SERV_TYPE_DOUBLE)){
//				createUserJob(newUser, newUser.getCust_id(), doneCode);
//			}
			
			//复制专用账户、账目
			CAcct oldAcct = acctComponent.queryUserAcct(oldUser.getCust_id(), oldUser.getUser_id());
			String newAcctId = acctComponent.updateAcct(doneCode, busiCode, oldAcct.getAcct_id(), nonresCustId, newUserId);
			
			//复制产品
			userProdComponent.updateProd(doneCode, busiCode, oldUser.getUser_id(), nonresCustId, newUserId, newAcctId);
		}
		
		//删除客户
		CCust residentCust = custComponent.queryCustById(residentCustId);
		CCustLinkman residentLinkman = custComponent.queryCustLinkmanById(residentCustId);
		custComponent.removeCustWithHis(doneCode, residentCust, residentLinkman,false);
		
		jobComponent.createCustWriteOffJob(doneCode, nonresCustId, SystemConstants.BOOLEAN_TRUE);
		jobComponent.createAcctModeCalJob(doneCode, nonresCustId);
		jobComponent.createInvalidCalJob(doneCode, nonresCustId);
		
		saveAllPublic(doneCode, getBusiParam());
	}
	
	/**
	 * 记录用户下所有账目解冻异动
	 * @param residentUserList
	 * @throws Exception
	 */
	private void inactiveAcctItem(Integer doneCode, String busiCode,
			String residentCustId, List<CUser> residentUserList) throws Exception {
		for(CUser residentUser : residentUserList){
			List<AcctitemDto> acctItemList = acctComponent.queryAcctItemByUserId(residentUser.getUser_id());
			for(AcctitemDto acctitem : acctItemList){
				String acctId = acctitem.getAcct_id();
				String acctItemId = acctitem.getAcctitem_id();
				List<CAcctAcctitemInactive> inactiveList = acctComponent.queryInactive(acctId, acctItemId);
				int inactiveBalance = 0;	//赠送金额
				
				//记录解冻异动
				for(CAcctAcctitemInactive inactive : inactiveList){
					if(inactive.getBalance() > 0){
						inactiveBalance += inactive.getBalance();
						acctComponent.saveAcctitemChange(doneCode,
								busiCode, residentCustId, inactive.getAcct_id(), 
								inactive.getAcctitem_id(), SystemConstants.ACCT_CHANGE_UNFREEZE,
								SystemConstants.ACCT_FEETYPE_PRESENT, inactive.getBalance(), 0, inactive.getDone_code());
					}
				}
				
				//活动余额
				List<AcctAcctitemActiveDto> activeList = acctComponent.queryActiveById(acctId, acctItemId);
				//预约金额
				List<CAcctAcctitemOrder> orderList = acctComponent.queryOrder(acctId, acctItemId);
				
				CProd prod = userProdComponent.queryByAcctItem(acctId, acctItemId);
				
				BBill bill = billComponent.confirmBill(prod.getProd_sn(), doneCode);
			}
		}
	}

	/**
	 * 查询产权变更的设备销售方式
	 * @return
	 * @throws Exception
	 */
	public List<TDeviceBuyMode> queryDeviceBuyModeByOwnership() throws Exception {
		return custComponent.queryDeviceBuyModeByOwnership(SystemConstants.BOOLEAN_TRUE);
	}
	
	public List<TDeviceBuyMode> queryDeviceCanFee() throws Exception {
		return custComponent.queryDeviceCanFee();
	}
	

	/**
	 * 设备销售
	 */
	public void saveSaleDevice(String deviceId, List<FeeInfoDto> feeInfoList,String buyMode,String changeOwnship) throws Exception {
		BusiParameter param = (BusiParameter)getParam();
		CCust cust = param.getCustFullInfo().getCust();
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();

		//查询设备的基本信息
		DeviceDto device = deviceComponent.queryDeviceByDeviceId(deviceId);
		DeviceDto pairDevice = new DeviceDto();
		DeviceDto pairModemDevice = new DeviceDto();
		getBusiParam().setBusiConfirmParam("device", device);
		if (device.getPairCard() != null){
			pairDevice = deviceComponent.queryDeviceByDeviceId(device.getPairCard().getDevice_id());
			getBusiParam().setBusiConfirmParam("paired_card", pairDevice);
		}
		if (device.getPairModem() != null){
			pairModemDevice = deviceComponent.queryDeviceByDeviceId(device.getPairModem().getDevice_id());
			getBusiParam().setBusiConfirmParam("paired_modem", pairModemDevice);
		}
		//增加设备销售费用记录
		if(feeInfoList != null){
			String payType = SystemConstants.PAY_TYPE_CASH;
			if (this.getBusiParam().getPay()!= null && this.getBusiParam().getPay().getPay_type() !=null)
				payType = this.getBusiParam().getPay().getPay_type();
			for(int i=0,len=feeInfoList.size();i<len;i++){
				FeeInfoDto dto = feeInfoList.get(i);
				String feeId = dto.getFee_id();
				String feeStdId = dto.getFee_std_id();
				Integer fee = dto.getFee();
				
				feeComponent.saveDeviceFee(cust.getCust_id(),cust.getAddr_id(), feeId, feeStdId,
						payType, device.getDevice_type(), deviceId, device.getDevice_code(), 
						pairDevice.getDevice_id(),pairDevice.getDevice_code(),
						pairModemDevice.getDevice_id(), pairModemDevice.getDevice_code(), device.getDevice_model(),
						fee, doneCode, doneCode, param.getBusiCode(), 1);
			}
		}
		
		//修改设备的产权
		if(device.getOwnership().equals(SystemConstants.OWNERSHIP_GD) && changeOwnship.equals("T")){
			String newValue = SystemConstants.OWNERSHIP_CUST;
			deviceComponent.updateDeviceOwnership(doneCode, param.getBusiCode(), deviceId,device.getOwnership(), newValue,buyMode,true);
			if (StringHelper.isNotEmpty(pairDevice.getDevice_id()) && pairDevice.getIs_virtual().equals(SystemConstants.BOOLEAN_FALSE))
				deviceComponent.updateDeviceOwnership(doneCode, param.getBusiCode(), pairDevice.getDevice_id(), pairDevice.getOwnership(),newValue,buyMode,false);
			if (StringHelper.isNotEmpty(pairModemDevice.getDevice_id()) && pairModemDevice.getIs_virtual().equals(SystemConstants.BOOLEAN_FALSE))
				deviceComponent.updateDeviceOwnership(doneCode, param.getBusiCode(), pairModemDevice.getDevice_id(), pairModemDevice.getOwnership(),newValue,buyMode,false);
		}
		
		//记录购买方式异动
		CCustDevice custDevice = custComponent.queryCustDeviceByDeviceId(device.getDevice_id());
		deviceComponent.updateBuyMode(doneCode, param.getBusiCode(), device.getDevice_id(),custDevice.getBuy_mode(),buyMode);
		
		//修改客户设备的购买方式
		custComponent.updateDeviceBuyMode(doneCode, cust.getCust_id(), deviceId, buyMode);
		if (StringHelper.isNotEmpty(pairDevice.getDevice_id()) && pairDevice.getIs_virtual().equals(SystemConstants.BOOLEAN_FALSE))
			custComponent.updateDeviceBuyMode(doneCode, cust.getCust_id(), pairDevice.getDevice_id(), buyMode);
		if (StringHelper.isNotEmpty(pairModemDevice.getDevice_id()) && pairModemDevice.getIs_virtual().equals(SystemConstants.BOOLEAN_FALSE))
			custComponent.updateDeviceBuyMode(doneCode, cust.getCust_id(), pairModemDevice.getDevice_id(), buyMode);
		
		deviceComponent.saveDeviceUseRecords(doneCode, BusiCodeConstants.DEVICE_SALE, device.getDevice_id(),
				device.getDevice_type(), device.getDevice_code(), cust.getCust_id(), cust.getCust_no());
		if (StringHelper.isNotEmpty(pairDevice.getDevice_id())){
			deviceComponent.saveDeviceUseRecords(doneCode, BusiCodeConstants.DEVICE_SALE, pairDevice.getDevice_id(),
					pairDevice.getDevice_type(), pairDevice.getDevice_code(), cust.getCust_id(), cust.getCust_no());
		}
		if (StringHelper.isNotEmpty(pairModemDevice.getDevice_id())){
			deviceComponent.saveDeviceUseRecords(doneCode, BusiCodeConstants.DEVICE_SALE, pairModemDevice.getDevice_id(),
					pairModemDevice.getDevice_type(), pairModemDevice.getDevice_code(), cust.getCust_id(), cust.getCust_no());
		}
		
//		saveAllPublic(doneCode,getBusiParam(),busiInfo);
		saveAllPublic(doneCode,getBusiParam());
	}

	public void saveChangeDeviceType(String deviceId, String buyMode) throws Exception {
		BusiParameter param = (BusiParameter)getParam();
		CCust cust = param.getCustFullInfo().getCust();
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();

		//查询设备的基本信息
		DeviceDto device = deviceComponent.queryDeviceByDeviceId(deviceId);
		DeviceDto pairDevice = new DeviceDto();
		DeviceDto pairModemDevice = new DeviceDto();
//		String busiInfo  = "设备编号："+device.getDevice_code();
		getBusiParam().setBusiConfirmParam("device", device);
		if (device.getPairCard() != null){
			pairDevice = deviceComponent.queryDeviceByDeviceId(device.getPairCard().getDevice_id());
//			busiInfo+=" 配对卡编号："+pairDevice.getDevice_code();
			getBusiParam().setBusiConfirmParam("paired_card", pairDevice);
		}
		if (device.getPairModem() != null){
			pairModemDevice = deviceComponent.queryDeviceByDeviceId(device.getPairModem().getDevice_id());
//			busiInfo+=" 配对MODEM编号："+pairModemDevice.getDevice_code();
			getBusiParam().setBusiConfirmParam("paired_modem", pairModemDevice);
		}
		
		TDeviceBuyMode deviceBuyMode = busiConfigComponent.queryBuyMode(buyMode);
		String newOwnership = SystemConstants.OWNERSHIP_CUST;
		if (SystemConstants.BOOLEAN_FALSE.equals(deviceBuyMode.getChange_ownship())){
			   newOwnership = SystemConstants.OWNERSHIP_GD;
		}	
		deviceComponent.updateDeviceOwnership(doneCode, param.getBusiCode(), device.getDevice_id(),device.getOwnership(),newOwnership,buyMode,true);
		if (pairDevice.getDevice_id() != null)
			deviceComponent.updateDeviceOwnership(doneCode, param.getBusiCode(), pairDevice.getDevice_id(),pairDevice.getOwnership(), newOwnership,buyMode,false);
		if (pairModemDevice.getDevice_id() != null)
			deviceComponent.updateDeviceOwnership(doneCode, param.getBusiCode(), pairModemDevice.getDevice_id(),pairModemDevice.getOwnership(), newOwnership,buyMode,false);
		
		//记录购买方式异动
		CCustDevice custDevice = custComponent.queryCustDeviceByDeviceId(device.getDevice_id());
		deviceComponent.updateBuyMode(doneCode, param.getBusiCode(), device.getDevice_id(),custDevice.getBuy_mode(),buyMode);
		
		//修改客户设备的购买方式
		custComponent.updateDeviceBuyMode(doneCode, cust.getCust_id(), deviceId, buyMode);
		if (pairDevice.getDevice_id() != null && pairDevice.getIs_virtual().equals(SystemConstants.BOOLEAN_FALSE))
			custComponent.updateDeviceBuyMode(doneCode, cust.getCust_id(), pairDevice.getDevice_id(), buyMode);
		if (pairModemDevice.getDevice_id() != null && pairModemDevice.getIs_virtual().equals(SystemConstants.BOOLEAN_FALSE))
			custComponent.updateDeviceBuyMode(doneCode, cust.getCust_id(), pairModemDevice.getDevice_id(), buyMode);
		
//		saveAllPublic(doneCode,getBusiParam(),busiInfo);
		saveAllPublic(doneCode,getBusiParam());

	}
	
	public void saveReclaimDevice(String deviceId,String deviceStatus,String reclaimReason,int fee) throws Exception {
		BusiParameter param = (BusiParameter)getParam();
		CCust cust = param.getCustFullInfo().getCust();
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = param.getBusiCode();

		String busiInfo = reclaimDevice(deviceId, deviceStatus, reclaimReason, fee, 
				cust, doneCode, busiCode);

//		saveAllPublic(doneCode,getBusiParam(),busiInfo);
		saveAllPublic(doneCode,getBusiParam());
	}

	
	
	/**
	 * 仓库管理调用回收设备
	 * @param deviceId
	 * @param deviceStatus
	 * @throws JDBCException
	 * @throws Exception
	 * @throws ComponentException
	 */
	public void saveCancelDevice(String deviceId, String deviceStatus)
			throws JDBCException, Exception, ComponentException {
		
		BusiParameter param = (BusiParameter)getParam();
		CCust cust = param.getCustFullInfo().getCust();
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = param.getBusiCode();
		//查询设备的基本信息
		DeviceDto device = deviceComponent.queryDeviceByDeviceId(deviceId);
		//如果是配对设备,查出所配对的机顶盒,一起把机顶盒，配对卡、猫都回收.
		boolean isPairDevice = SystemConstants.BOOLEAN_TRUE.equals(device.getIsPairModem()) || 
				SystemConstants.BOOLEAN_TRUE.equals(device.getIsPairCard()) ;
		if(!device.getDevice_type().equals(SystemConstants.DEVICE_TYPE_STB) && isPairDevice){
			device = deviceComponent.queryDevice(device.getDevice_code());
		}
		
		DeviceDto pairDevice = null, pairModemDevice = null;
		if (device.getPairCard() != null){
			pairDevice = deviceComponent.queryDeviceByDeviceId(device.getPairCard().getDevice_id());
		}
		if (device.getPairModem() != null){
			pairModemDevice = deviceComponent.queryDeviceByDeviceId(device.getPairModem().getDevice_id());
		}
		
		this.saveRemoveDevice(doneCode, busiCode, cust, device.getDevice_id(), deviceStatus);
		
		CCust ccust = custComponent.queryCustById(cust.getCust_id());
		if(ccust != null)	cust = ccust;	//有可能客户已销户
		deviceComponent.saveDeviceUseRecords(doneCode, busiCode, device.getDevice_id(),
				device.getDevice_type(), device.getDevice_code(), cust.getCust_id(), cust.getCust_no());
		if (pairDevice != null){
			deviceComponent.saveDeviceUseRecords(doneCode, busiCode, pairDevice.getDevice_id(),
					pairDevice.getDevice_type(), pairDevice.getDevice_code(), cust.getCust_id(), cust.getCust_no());
		}
		if (pairModemDevice != null){
			deviceComponent.saveDeviceUseRecords(doneCode, busiCode, pairModemDevice.getDevice_id(),
					pairModemDevice.getDevice_type(), pairModemDevice.getDevice_code(), cust.getCust_id(), cust.getCust_no());
		}
	}

	public void saveChangeModem(String oldModemId, String newModemId,
			String buyMode, List<FeeInfoDto> feeInfoList, int modemZjFee,
			boolean reclaim, String deviceStatus) throws Exception {
		BusiParameter param = (BusiParameter)getParam();
		String busiCode = param.getBusiCode();
		CCust cust = param.getCustFullInfo().getCust();
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		DeviceDto newDevice = deviceComponent.queryDeviceByDeviceCode(newModemId);
		DeviceDto oldDevice = deviceComponent.queryDeviceByDeviceCode(oldModemId);
		if(StringHelper.isNotEmpty(buyMode)){
			buyDevice(newDevice, buyMode, feeInfoList,busiCode, cust, doneCode,oldDevice.getOwnership());
			
			CCustDevice oldCustDevice = custComponent.queryCustDeviceByDeviceCode(oldModemId);
			custComponent.saveDeviceBuyModeProp(doneCode,cust.getCust_id(), oldCustDevice.getDevice_type(),
					oldCustDevice.getDevice_id(), oldCustDevice.getDevice_code(), oldCustDevice.getBuy_mode(),
					newDevice.getDevice_id(), newDevice.getDevice_code(),buyMode);
		}
		
		changeModem(doneCode,oldModemId, newModemId,  cust.getCust_id(),
				doneCode);
		
		if(modemZjFee > 0){
			String payType = SystemConstants.PAY_TYPE_CASH;
			if (this.getBusiParam().getPay()!= null && this.getBusiParam().getPay().getPay_type() !=null)
				payType = this.getBusiParam().getPay().getPay_type();
			DeviceDto modemDevice = deviceComponent.queryDeviceByDeviceCode(oldModemId);
			List<RDeviceFee> modemFeeList = deviceComponent.queryDeviceFee(modemDevice.getDevice_type(), 
					modemDevice.getDevice_model(),SystemConstants.BUSI_BUY_MODE_CHANGE);
			for(int i=0,len=modemFeeList.size();i<len;i++){
				RDeviceFee modemFee = modemFeeList.get(i);
				feeComponent.saveDeviceFee(cust.getCust_id(),cust.getAddr_id(), modemFee.getFee_id(),
						modemFee.getFee_std_id(), payType, modemDevice.getDevice_type(), 
						modemDevice.getDevice_id(),modemDevice.getDevice_code(),
						null, null, null, null,modemDevice.getDevice_model(), modemZjFee, doneCode,doneCode, busiCode, 1);
			}
		}
		
		if(reclaim){
			reclaimDevice(oldDevice.getDevice_id(), deviceStatus, "",0, cust, doneCode, busiCode);
		}

		saveAllPublic(doneCode,getBusiParam());
	}
	
	/**
	 *
	 * 修改用户产品信息
	 * @param oldModemId
	 * @param newModemId
	 * @param changeOwnership
	 * @param param
	 * @param cust
	 * @param doneCode
	 * @throws JDBCException
	 * @throws Exception
	 */
	private void changeModem(Integer doneCdoe,String oldModemId, String newModemId,
			String custId,
			Integer doneCode) throws JDBCException, Exception {
		//查询MODEM的基本信息
		DeviceDto oldModem = deviceComponent.queryDeviceByDeviceCode(oldModemId);
		DeviceDto newModem = deviceComponent.queryDeviceByDeviceCode(newModemId);
		//将新设备转到客户名下
		custComponent.transDevice(doneCdoe, custId, newModem);
		//修改原modem的客户设备状态为空闲,修改新的客户设备的状态为使用
		custComponent.updateDeviceStatus(custId, oldModem.getDevice_id(), StatusConstants.IDLE);
		custComponent.updateDeviceStatus(custId, newModem.getDevice_id(), StatusConstants.USE);
		
		//调用用户组件，更新用户设备编号
		List<CUser> userList = userComponent.queryUserByDevice(SystemConstants.DEVICE_TYPE_MODEM, oldModemId);
		for (CUser user : userList){
			userComponent.updateDevice(doneCode, user);
		}
	}
	
	private void saveDeviceBuyModeProp(String newDeviceId,
			String newDeviceType, String newDeviceCode, String oldDeviceId,
			Integer doneCode, String custId, String buyMode) throws Exception {
		CCustDevice oldCustDevice = custComponent.queryCustDeviceByDeviceCode(oldDeviceId);
		if(oldCustDevice != null){
			custComponent.saveDeviceBuyModeProp(doneCode, custId,
					newDeviceType, oldCustDevice.getDevice_id(), oldCustDevice
							.getDevice_code(), oldCustDevice.getBuy_mode(),
					newDeviceId, newDeviceCode, buyMode);
		}
	}
	
	private void saveDeviceOwnership(RDevice newDevice,RDevice oldDevice,String busiCode,Integer doneCode,boolean key) throws Exception {
		//根据设备的购买方式判断是否需要修改设备的产权
		if(newDevice != null && oldDevice != null){
			deviceComponent.updateDeviceOwnership(doneCode, busiCode, newDevice.getDevice_id(), newDevice.getOwnership(), oldDevice.getOwnership(),null,key);
			deviceComponent.updateDeviceOwnership(doneCode, busiCode, oldDevice.getDevice_id(), oldDevice.getOwnership(), newDevice.getOwnership(),null,key);
		}
	}
	
	
	private void saveChangeDevice(String oldDevice,DeviceDto newDevice,List<FeeInfoDto> feeInfoList, String busiCode, 
			CCust cust,Integer doneCode,String changeReason) throws Exception {
		if(newDevice == null){
			throw new ServicesException("未能正确的获取设备信息,请重新确认是否输入正确.");
		}
		//虚拟卡
		if(newDevice.getIs_virtual().equals(SystemConstants.BOOLEAN_FALSE)){
			//查询设备的基本信息
			
			DeviceDto pairDevice = null, pairModemDevice = null;
			String is_card_virtual = SystemConstants.BOOLEAN_FALSE,is_modem_virtual = SystemConstants.BOOLEAN_FALSE;
			if (newDevice.getPairCard() != null){
				pairDevice = deviceComponent.queryDeviceByDeviceId(newDevice.getPairCard().getDevice_id());
				is_card_virtual = pairDevice.getIs_virtual();
			}
			if (newDevice.getPairModem() != null){
				pairModemDevice = deviceComponent.queryDeviceByDeviceId(newDevice.getPairModem().getDevice_id());
				is_modem_virtual = pairModemDevice.getIs_virtual();
			}
			
			CCustDevice oldCustDevice = custComponent.queryCustDeviceByDeviceCode(oldDevice);
			if(oldCustDevice != null){
				//增加客户设备
				custComponent.changeDevice(doneCode,cust.getCust_id(),oldCustDevice.getDevice_id(), newDevice.getDevice_id(),
						newDevice.getDevice_type(),
						newDevice.getDevice_code(),
						pairDevice==null?null:pairDevice.getDevice_id(),
						pairDevice==null?null:pairDevice.getDevice_code(),
						pairModemDevice==null?null:pairModemDevice.getDevice_id(),
						pairModemDevice==null?null:pairModemDevice.getDevice_code(),changeReason);
				if (pairDevice != null && is_card_virtual.equals(SystemConstants.BOOLEAN_FALSE)){
					custComponent.changeDevice(doneCode,cust.getCust_id(),oldCustDevice.getPair_card_id(), pairDevice.getDevice_id(),
						pairDevice.getDevice_type(), pairDevice.getDevice_code(),"", "","","",changeReason);
				}
				if (pairModemDevice != null && is_modem_virtual.equals(SystemConstants.BOOLEAN_FALSE)){
					custComponent.changeDevice(doneCode,cust.getCust_id(),oldCustDevice.getPair_modem_id(), pairDevice.getDevice_id(),
						pairDevice.getDevice_type(), pairDevice.getDevice_code(),"", "","","",changeReason);
				}
			}else{
				throw new ServicesException("更换错误,原该类型设备为空,请先购买设备!");
			}

			if(feeInfoList != null){
				for(int i=0,len=feeInfoList.size();i<len;i++){
					FeeInfoDto dto = feeInfoList.get(i);
					String feeId = dto.getFee_id();
					String feeStdId = dto.getFee_std_id();
					Integer fee = dto.getFee();
					
					String payType = SystemConstants.PAY_TYPE_CASH;
					if (this.getBusiParam().getPay()!= null && this.getBusiParam().getPay().getPay_type() !=null)
						payType = this.getBusiParam().getPay().getPay_type();
					feeComponent.saveDeviceFee( cust.getCust_id(),cust.getAddr_id(), feeId,feeStdId, payType,newDevice.getDevice_type(), newDevice.getDevice_id(), newDevice.getDevice_code(),
							pairDevice==null?null:pairDevice.getDevice_id(),
							pairDevice==null?null:pairDevice.getDevice_code(),
							pairModemDevice==null?null:pairModemDevice.getDevice_id(),
							pairModemDevice==null?null:pairModemDevice.getDevice_code(),newDevice.getDevice_model(),
							fee, doneCode,doneCode, busiCode, 1);
				}
			}
			//修改设备的仓库状态为占用，如果设备有配对的智能卡，修改智能卡的状态为占用
			deviceComponent.updateDeviceDepotStatus(doneCode, busiCode, newDevice.getDevice_id(),newDevice.getDepot_status(), StatusConstants.USE,null,true);
			if (pairDevice != null)
				deviceComponent.updateDeviceDepotStatus(doneCode, busiCode, pairDevice.getDevice_id(),pairDevice.getDepot_status(), StatusConstants.USE,null,false);
			if (pairModemDevice != null)
				deviceComponent.updateDeviceDepotStatus(doneCode, busiCode, pairModemDevice.getDevice_id(),pairModemDevice.getDepot_status(), StatusConstants.USE,null,false);
			
			//更新设备为旧设备
			if (newDevice.getUsed().equals(SystemConstants.BOOLEAN_TRUE))
				deviceComponent.updateDeviceUsed(doneCode, busiCode, newDevice.getDevice_id(), SystemConstants.BOOLEAN_TRUE, SystemConstants.BOOLEAN_FALSE,true);
			if (pairDevice != null && pairDevice.getUsed().equals(SystemConstants.BOOLEAN_TRUE))
				deviceComponent.updateDeviceUsed(doneCode, busiCode, pairDevice.getDevice_id(), SystemConstants.BOOLEAN_TRUE, SystemConstants.BOOLEAN_FALSE,false);
			if (pairModemDevice != null && pairModemDevice.getUsed().equals(SystemConstants.BOOLEAN_TRUE))
				deviceComponent.updateDeviceUsed(doneCode, busiCode, pairModemDevice.getDevice_id(), SystemConstants.BOOLEAN_TRUE, SystemConstants.BOOLEAN_FALSE,false);
			
			deviceComponent.saveDeviceUseRecords(doneCode, busiCode, newDevice.getDevice_id(),
					newDevice.getDevice_type(), newDevice.getDevice_code(), cust.getCust_id(), cust.getCust_no());
			if (pairDevice != null){
				deviceComponent.saveDeviceUseRecords(doneCode, busiCode, pairDevice.getDevice_id(),
						pairDevice.getDevice_type(), pairDevice.getDevice_code(), cust.getCust_id(), cust.getCust_no());
			}
			if (pairModemDevice != null){
				deviceComponent.saveDeviceUseRecords(doneCode, busiCode, pairModemDevice.getDevice_id(),
						pairModemDevice.getDevice_type(), pairModemDevice.getDevice_code(), cust.getCust_id(), cust.getCust_no());
			}
		}
	}
	
	/**
	 * 实际上有一种情况,鸡毛一体,这个设备即开双向又开宽带,因此不允许还猫.
	 */
	public void saveSwitchDevice(String oldStbId, String oldCardId, String oldModemId,
			String newStbId, String newCardId, String newModemId)throws Exception {
		
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		if(StringHelper.isEmpty(oldCardId) || StringHelper.isEmpty(newCardId)){
			throw new ServicesException("参数错误,未能正确获取两边用户的智能卡号!");
		}
		
		boolean deviceMatchFlag = false;//两边必须都有机顶盒或者都没有机顶盒.智能卡是前提,都必须有,前面已做判断
		if(StringHelper.isNotEmpty(oldStbId) && StringHelper.isNotEmpty(newStbId) ){//机卡都有
			deviceMatchFlag = true;
		}else if(StringHelper.isEmpty(oldStbId) && StringHelper.isEmpty(newStbId)){//都只有卡
			deviceMatchFlag = true;
		}
		if(!deviceMatchFlag){
			throw new ServicesException("两边用户的设备数量、类型不一致,不能进行该项业务");
		}
		
		//查询设备信息
		CCustDevice oldCustCardObj = custComponent.queryCustDeviceByDeviceCode(oldCardId);
		CCustDevice newCustCardObj = custComponent.queryCustDeviceByDeviceCode(newCardId);
		CCustDevice oldCustStbObj = custComponent.queryCustDeviceByDeviceCode(oldStbId);
		CCustDevice newCustStbObj = custComponent.queryCustDeviceByDeviceCode(newStbId);
		
		List<UserDto> userList = new ArrayList<UserDto>();
		UserDto oldUser = userComponent.queryUserByDeviceId(oldCustCardObj.getDevice_code());
		UserDto newUser = userComponent.queryUserByDeviceId(newCustCardObj.getDevice_code());
		if(oldUser == null && oldCustStbObj !=null){
			oldUser = userComponent.queryUserByDeviceId(oldCustStbObj.getDevice_code());
		}
		if(newUser == null && newCustStbObj !=null){
			newUser = userComponent.queryUserByDeviceId(newCustStbObj.getDevice_code());
		}
		if(oldUser == null || newUser == null){
			throw new ServicesException("未能正确的获取设备对应的用户信息,请重新确认是否输入正确.");
		}
		userList.add(oldUser);
		userList.add(newUser);
		if(!oldUser.getServ_type().equals(newUser.getServ_type())){
			throw new ServicesException("互换设备的两个用户服务类型不一致,一边为单向,一边为双向.");
		}
		String oldModemMac = oldUser.getModem_mac();
		String newModemMac = newUser.getModem_mac();
		if(StringHelper.isEmpty(oldModemMac) && StringHelper.isNotEmpty(newModemMac)
				|| StringHelper.isNotEmpty(oldModemMac) && StringHelper.isEmpty(newModemMac)){
			throw new ServicesException("互换设备的两个用户设备不一致,一边有猫一边没猫.");
		}
		
		DeviceDto oldModem = deviceComponent.queryDeviceByDeviceCode(oldModemMac);
		DeviceDto newModem = deviceComponent.queryDeviceByDeviceCode(newModemMac);
		
		if(oldModem !=null || newModem !=null){//有猫的设备暂时不给换
			throw new ServicesException("设备不符合更换条件,不能互换!");
		}
		
		/*
		if(oldModem != null && newModem !=null){
			//比较两边猫，不能一个是虚拟，一个是实体
			if(!oldModem.getIs_virtual().equals(newModem.getIs_virtual())){
				throw new ServicesException("互换设备的两个用户猫有一个是实体猫一个是虚拟猫.");
			}
		}
		*/
		/**
		 * 对于原有机卡的所有有效资源发减授权
		 */
		for (CUser user:userList){
			List<UserRes> resList = this.queryValidRes(user.getUser_id().split(","));
			for (UserRes res: resList){
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ADD_REJECT_RES, 
						user.getCust_id(), user.getUser_id(), user.getStb_id(), user.getCard_id(),
						user.getModem_mac(),null, null,"RES_ID:''"+res.getRes_id()+"''");
			}
		}
		//实体猫不给换，虚拟猫不用换
		//修改用户的设备属性.
		//userComponent.updateDevice(doneCode, newUser, oldUser.getStb_id(), oldUser.getCard_id(), newModemMac);
		//userComponent.updateDevice(doneCode, oldUser, newUser.getStb_id(), newUser.getCard_id(), oldModemMac);
		String fromCustId = newUser.getCust_id();
		String currentCustId = oldUser.getCust_id();

		List<CCustDeviceChange> oldDevChanges = new ArrayList<CCustDeviceChange>();
		List<CCustDeviceChange> newDevChanges = new ArrayList<CCustDeviceChange>();
		List<CCustDeviceChange> oldCardChanges = new ArrayList<CCustDeviceChange>();
		List<CCustDeviceChange> newCardChanges = new ArrayList<CCustDeviceChange>();
		
		if(oldCustCardObj!=null){//不是虚拟设备
			CCustDeviceChange change = new CCustDeviceChange();
			change.setColumn_name("cust_id");
			change.setDevice_id(oldCustCardObj.getDevice_id());
			change.setOld_value(currentCustId);
			change.setNew_value(fromCustId);
			oldCardChanges.add(change);
			
			if(!oldCustCardObj.getStatus().equals(newCustCardObj.getStatus())){
				CCustDeviceChange change1 = new CCustDeviceChange();
				change1.setColumn_name("status");
				change1.setDevice_id(oldCustCardObj.getDevice_id());
				change1.setOld_value(oldCustCardObj.getStatus());
				change1.setNew_value(newCustCardObj.getStatus());
				oldCardChanges.add(change1);
			}
			custComponent.editCustDevice(doneCode,oldCustCardObj.getDevice_id(),oldCardChanges);
		}
		if(newCustCardObj!=null){
			CCustDeviceChange change = new CCustDeviceChange();
			change.setColumn_name("cust_id");
			change.setDevice_id(newCustCardObj.getDevice_id());
			change.setOld_value(fromCustId);
			change.setNew_value(currentCustId);
			newCardChanges.add(change);
			
			if(!newCustCardObj.getStatus().equals(oldCustCardObj.getStatus())){
				CCustDeviceChange change1 = new CCustDeviceChange();
				change1.setColumn_name("status");
				change1.setDevice_id(newCustCardObj.getDevice_id());
				change1.setOld_value(newCustCardObj.getStatus());
				change1.setNew_value(oldCustCardObj.getStatus());
				newCardChanges.add(change1);
			}
			custComponent.editCustDevice(doneCode,newCustCardObj.getDevice_id(),newCardChanges);
		}
		
		if(oldCustStbObj != null && newCustStbObj!=null){//机顶盒不为空
			CCustDeviceChange oldDevCustIdChange = new CCustDeviceChange();
			oldDevCustIdChange.setColumn_name("cust_id");
			oldDevCustIdChange.setDevice_id(oldCustStbObj.getDevice_id());
			oldDevCustIdChange.setOld_value(currentCustId);
			oldDevCustIdChange.setNew_value(fromCustId);
			oldDevChanges.add(oldDevCustIdChange);
			
			CCustDeviceChange newDevCustIdChange = new CCustDeviceChange();
			newDevCustIdChange.setColumn_name("cust_id");
			newDevCustIdChange.setDevice_id(newCustStbObj.getDevice_id());
			newDevCustIdChange.setOld_value(fromCustId);
			newDevCustIdChange.setNew_value(currentCustId);
			newDevChanges.add(newDevCustIdChange);
			
			//包换期同客户的跟着盒子走		不同客户不跟盒子走
			if(!fromCustId.equals(currentCustId)){
				Date oldDate = oldCustStbObj.getReplacover_date();
				String oldDevRstr = oldDate!=null?DateHelper.format(oldDate, DateHelper.FORMAT_YMD):"";
				Date newDate = newCustStbObj.getReplacover_date();
				String newDevRstr = newDate!=null?DateHelper.format(newDate, DateHelper.FORMAT_YMD):"";
				
				CCustDeviceChange oldChange = new CCustDeviceChange();
				oldChange.setColumn_name("replacover_date");
				oldChange.setDevice_id(oldCustStbObj.getDevice_id());
				oldChange.setOld_value(oldDevRstr);
				oldChange.setNew_value(newDevRstr);
				oldDevChanges.add(oldChange);
				
				CCustDeviceChange newChange = new CCustDeviceChange();
				newChange.setColumn_name("replacover_date");
				newChange.setDevice_id(newCustStbObj.getDevice_id());
				newChange.setOld_value(newDevRstr);
				newChange.setNew_value(oldDevRstr);
				newDevChanges.add(newChange);
			}
			

			if(!oldCustStbObj.getStatus().equals(newCustStbObj.getStatus())){
				CCustDeviceChange change1 = new CCustDeviceChange();
				change1.setColumn_name("status");
				change1.setDevice_id(oldCustStbObj.getDevice_id());
				change1.setOld_value(oldCustStbObj.getStatus());
				change1.setNew_value(newCustStbObj.getStatus());
				oldDevChanges.add(change1);
				
				CCustDeviceChange change2 = new CCustDeviceChange();
				change2.setColumn_name("status");
				change2.setDevice_id(newCustStbObj.getDevice_id());
				change2.setOld_value(newCustStbObj.getStatus());
				change2.setNew_value(oldCustStbObj.getStatus());
				newDevChanges.add(change2);
			}
			
			custComponent.editCustDevice(doneCode,oldCustStbObj.getDevice_id(),oldDevChanges);
			custComponent.editCustDevice(doneCode,newCustStbObj.getDevice_id(),newDevChanges);
		}
//		custComponent.updateDevice(devicesList.toArray(new CCustDevice[devicesList.size()]));
		
		//最后指令重发,从 userService抄过来,简单几行代码不重新构造一个方法了
		for (CUser user:userList){
			//重发加授权指令
			List<CProdDto> prodList = userProdComponent.queryAllProdsByUserId(user.getUser_id());
			for (CProd prod:prodList){
				//正常状态的产品,重复指令
				if (isProdOpen(prod.getStatus())){
					jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD,
							user.getCust_id(), user.getUser_id(), user.getStb_id(),
							user.getCard_id(), user.getModem_mac(), prod.getProd_sn(),prod.getProd_id());
				}
			}
		}
		
		saveAllPublic(doneCode, getBusiParam());
	}
	
	
	public void saveChangeStbCard(String oldStbId, String oldCardId,
			String oldModemId, String newStbId, String newCardId,
			String newModemId, List<FeeInfoDto> feeInfoList,
			int stbZjFee, int cardZjFee, int modemZjFee, 
			String deviceStatus, int buyFlag, boolean singleCard,String changeReason)
			throws Exception {
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		BusiParameter param = getBusiParam();
		CCust cust = param.getCustFullInfo().getCust();
		String custId = cust.getCust_id();
		String busiCode = param.getBusiCode();
		
		
		//判断是否需要购买设备
		if (buyFlag > 0){
			if(buyFlag == 1 || buyFlag == 3 || buyFlag == 4 || buyFlag == 5){//需要买机或者需要买机卡
				DeviceDto newDevice = deviceComponent.queryDeviceByDeviceCode(newStbId);
				this.saveChangeDevice(oldStbId,newDevice, feeInfoList,busiCode , cust, doneCode,changeReason);
				
				this.saveDeviceBuyModeProp(newDevice.getDevice_id(), 
						newDevice.getDevice_type(), newDevice.getDevice_code(),
							oldStbId, doneCode, custId, SystemConstants.BUSI_BUY_MODE_CHANGE);
			}
			
			if(buyFlag == 2 || buyFlag == 4 || buyFlag == 5 || buyFlag == 6){//需要买卡或者需要买机卡
				DeviceDto newDevice = deviceComponent.queryDeviceByDeviceCode(newCardId);
				if(buyFlag == 2){//单独买卡
					this.saveChangeDevice(oldCardId,newDevice, feeInfoList, busiCode, cust, doneCode,changeReason);
					//TODO 需要加 机顶盒A R_device_change记录pair_card_id 从B1到B2，pair_card_id记录卡的B2。
					//智能卡B1  R_device_change记录 pair_stb_id  从A的ID到无
				}else{
					this.saveChangeDevice(oldCardId,newDevice, null, busiCode, cust, doneCode,changeReason);
				}
				
				this.saveDeviceBuyModeProp(newDevice.getDevice_id(), newDevice
						.getDevice_type(), newDevice.getDevice_code(), oldCardId, doneCode, custId, SystemConstants.BUSI_BUY_MODE_CHANGE);
			}
			
			if(buyFlag == 3 || buyFlag == 5 || buyFlag == 6 || buyFlag == 7){
				DeviceDto newDevice = deviceComponent.queryDeviceByDeviceCode(newModemId);
				if(buyFlag == 7){//单独买MODEM
					this.saveChangeDevice(oldModemId,newDevice, feeInfoList, busiCode, cust, doneCode,changeReason);
				}else{
					this.saveChangeDevice(oldModemId,newDevice, null, busiCode, cust, doneCode,changeReason);
				}
				
				this.saveDeviceBuyModeProp(newDevice.getDevice_id(), newDevice
						.getDevice_type(), newDevice.getDevice_code(), oldModemId, doneCode, custId, SystemConstants.BUSI_BUY_MODE_CHANGE);
			}
			
			
			
		} else if(buyFlag == 0){	//已经在客户名下，无需再购买
			if(StringHelper.isNotEmpty(newStbId)){
				CCustDevice newCustDevice = custComponent.queryCustDeviceByDeviceCode(newStbId);
				//保存更换原因
				custComponent.saveChangeDevice(newCustDevice, changeReason);
				if(newCustDevice != null){
					this.saveDeviceBuyModeProp(newCustDevice.getDevice_id(), newCustDevice.getDevice_type(), newCustDevice.getDevice_code(), 
							oldStbId, doneCode, custId, newCustDevice.getBuy_mode());
					deviceComponent.saveDeviceUseRecords(doneCode, busiCode, newCustDevice.getDevice_id(),
							newCustDevice.getDevice_type(), newCustDevice.getDevice_code(), cust.getCust_id(), cust.getCust_no());
				}
			}
			
			if(StringHelper.isNotEmpty(newCardId)){
				CCustDevice newCustDevice = custComponent.queryCustDeviceByDeviceCode(newCardId);
				//保存更换原因
				custComponent.saveChangeDevice(newCustDevice, changeReason);
				if(newCustDevice != null){
					this.saveDeviceBuyModeProp(newCustDevice.getDevice_id(), newCustDevice.getDevice_type(), newCustDevice.getDevice_code(),
							oldCardId, doneCode, custId, newCustDevice.getBuy_mode());
					deviceComponent.saveDeviceUseRecords(doneCode, busiCode, newCustDevice.getDevice_id(),
							newCustDevice.getDevice_type(), newCustDevice.getDevice_code(), cust.getCust_id(), cust.getCust_no());
				}
			}
			
			if(StringHelper.isNotEmpty(newModemId)){
				CCustDevice newCustDevice = custComponent.queryCustDeviceByDeviceCode(newModemId);
				//保存更换原因
				custComponent.saveChangeDevice(newCustDevice, changeReason);
				if(newCustDevice != null){
					this.saveDeviceBuyModeProp(newCustDevice.getDevice_id(), newCustDevice.getDevice_type(), newCustDevice.getDevice_code(),
							oldModemId, doneCode, custId, newCustDevice.getBuy_mode());
					deviceComponent.saveDeviceUseRecords(doneCode, busiCode, newCustDevice.getDevice_id(),
							newCustDevice.getDevice_type(), newCustDevice.getDevice_code(), cust.getCust_id(), cust.getCust_no());
				}
			}
		}
		
		
		RDevice  oldStbDevice = deviceComponent.findByDeviceCode(oldStbId);
		RDevice  oldCardDevice = deviceComponent.findByDeviceCode(oldCardId);
		RDevice  oldModemDevice = deviceComponent.findByDeviceCode(oldModemId);
		
		RDevice  newStbDevice = deviceComponent.findByDeviceCode(newStbId);
		RDevice  newCardDevice = deviceComponent.findByDeviceCode(newCardId);
		RDevice  newModemDevice = deviceComponent.findByDeviceCode(newModemId);
		
		this.saveDeviceOwnership(oldStbDevice,newStbDevice,busiCode,doneCode,true);
		//记录更换原因'
		if (newStbDevice != null)
			deviceComponent.updateChangeReason(doneCode, busiCode, newStbDevice.getDevice_id(),changeReason);
		if(buyFlag == 2){//单独买卡
			this.saveDeviceOwnership(oldCardDevice,newCardDevice,busiCode,doneCode,true);
			deviceComponent.updateChangeReason(doneCode, busiCode, newCardDevice.getDevice_id(),changeReason);
		}else{
			this.saveDeviceOwnership(oldCardDevice,newCardDevice,busiCode,doneCode,false);	
		}
		if(buyFlag == 7){//单独买MODEM	
			this.saveDeviceOwnership(oldModemDevice,newModemDevice,busiCode,doneCode,true);
			deviceComponent.updateChangeReason(doneCode, busiCode, newModemDevice.getDevice_id(),changeReason);
		}else{
			this.saveDeviceOwnership(oldModemDevice,newModemDevice,busiCode,doneCode,false);
		}
		
		
		
		String payType = SystemConstants.PAY_TYPE_CASH;
		if (this.getBusiParam().getPay()!= null && this.getBusiParam().getPay().getPay_type() !=null)
			payType = this.getBusiParam().getPay().getPay_type();
		
		String feeId = busiConfigComponent.queryZjFeeId();
		if(stbZjFee > 0){
			DeviceDto stbDevice = deviceComponent.queryDeviceByDeviceCode(oldStbId);
			
			DeviceDto cardPairDevice = new DeviceDto();
			DeviceDto modemPairDevice = new DeviceDto();
			if (stbDevice.getPairCard() != null){
				cardPairDevice = deviceComponent.queryDeviceByDeviceId(stbDevice.getPairCard().getDevice_id());
			}
			if (stbDevice.getPairModem() != null){
				modemPairDevice = deviceComponent.queryDeviceByDeviceId(stbDevice.getPairModem().getDevice_id());
			}
			feeComponent.saveDeviceFee(cust.getCust_id(),cust.getAddr_id(),
					feeId, null, payType,
					stbDevice.getDevice_type(), stbDevice.getDevice_id(),stbDevice.getDevice_code(), 
					cardPairDevice.getDevice_id(),cardPairDevice.getDevice_code(),
					modemPairDevice.getDevice_id(),modemPairDevice.getDevice_code(),stbDevice.getDevice_model(),
					stbZjFee, doneCode, doneCode,busiCode, 1);
		}
		if(cardZjFee > 0){
			DeviceDto cardDevice = deviceComponent.queryDeviceByDeviceCode(oldCardId);
			feeComponent.saveDeviceFee(cust.getCust_id(),cust.getAddr_id(), feeId, null, payType, cardDevice.getDevice_type(), 
					cardDevice.getDevice_id(),cardDevice.getDevice_code(),
					null, null, null, null,cardDevice.getDevice_model(), cardZjFee, doneCode,doneCode, busiCode, 1);
		}
		
		if(modemZjFee > 0){
			DeviceDto modemDevice = deviceComponent.queryDeviceByDeviceCode(oldModemId);
				feeComponent.saveDeviceFee(cust.getCust_id(),cust.getAddr_id(), feeId, null, payType, modemDevice.getDevice_type(), 
						modemDevice.getDevice_id(),modemDevice.getDevice_code(),
						null, null, null, null,modemDevice.getDevice_model(), cardZjFee, doneCode,doneCode, busiCode, 1);
		}
		
		String busiInfo = changeStbCard(singleCard, oldStbId, oldCardId,
				oldModemId, newStbId, newCardId, newModemId, cust.getCust_id(),
				doneCode, param.getBusiCode());
		
		if (buyFlag > 0){
			//新设备是属于广电的直接回收设备
			String pairCard ="";
			String pairModem = "";
			DeviceDto device = null;
			if(StringHelper.isNotEmpty(newStbId) && !newStbId.equals(oldStbId)){
				device = deviceComponent.queryDeviceByDeviceCode(oldStbId);
				if(device != null){
					if(device.getPairCard() != null)
						pairCard = device.getPairCard().getCard_id();
					if(device.getPairModem() !=null)
						pairModem = device.getPairModem().getModem_mac();
					
					reclaimDevice(device.getDevice_id(), deviceStatus,changeReason, 0, cust, doneCode, busiCode);
				}
				
			}
			if(StringHelper.isNotEmpty(newCardId) && !newCardId.equals(oldCardId) && !pairCard.equals(oldCardId) ){
				device = deviceComponent.queryDeviceByDeviceCode(oldCardId);
				if(device != null && device.getIs_virtual().equals(SystemConstants.BOOLEAN_FALSE))
					reclaimDevice(device.getDevice_id(), deviceStatus,changeReason, 0, cust, doneCode, busiCode);
			}
			if(StringHelper.isNotEmpty(newModemId) && !newModemId.equals(oldModemId) && !pairModem.equals(oldModemId) ){
				device = deviceComponent.queryDeviceByDeviceCode(oldModemId);
				if(device != null && device.getIs_virtual().equals(SystemConstants.BOOLEAN_FALSE))
					reclaimDevice(device.getDevice_id(), deviceStatus,changeReason, 0, cust, doneCode, busiCode);
			}
			
		}
		
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, custId);
		Map<String, Object> busiConfirmParamInfo = getBusiParam().getBusiConfirmParamInfo();
		busiConfirmParamInfo.put("changeReason", MemoryDict.getDictName(DictKey.CHANGE_REASON, changeReason));
		doneCodeComponent.saveDoneCodeInfo(doneCode, cust.getCust_id() ,null, busiConfirmParamInfo);
//		saveAllPublic(doneCode,getBusiParam(),busiInfo);
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/**
	 * 设备挂失
	 */
	public void saveRegLossDevcie(String deviceId) throws Exception {
		BusiParameter param = (BusiParameter)getParam();
		CCust cust = param.getCustFullInfo().getCust();
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		DeviceDto device = deviceComponent.queryDeviceByDeviceId(deviceId);
		custComponent.saveDeviceRegLoss(doneCode,cust.getCust_id(), deviceId);
		saveAllPublic(doneCode,getBusiParam());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_type", device.getDevice_type_text());
		map.put("device_code", device.getDevice_code());
		doneCodeComponent.saveDoneCodeInfo(doneCode, cust.getCust_id(),null, map);
	}

	/**
	 * 取消挂失
	 */
	public void saveCancelLossDevcie(String deviceId) throws Exception {
		BusiParameter param = getBusiParam();
		CCust cust = param.getCustFullInfo().getCust();
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		DeviceDto device = deviceComponent.queryDeviceByDeviceId(deviceId);
		custComponent.saveCancelDeviceRegLoss(doneCode,cust.getCust_id(), deviceId);

//		saveAllPublic(doneCode,getBusiParam(),"设备编号："+device.getDevice_code());
		getBusiParam().setBusiConfirmParam("device", device);
		saveAllPublic(doneCode,getBusiParam());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_type", device.getDevice_type_text());
		map.put("device_code", device.getDevice_code());
		doneCodeComponent.saveDoneCodeInfo(doneCode, cust.getCust_id(),null, map);
	}


	/**
	 * 加入单位
	 */
	public void saveCustJoinUnit( String unitId) throws Exception {
		String custId = getBusiParam().getCust().getCust_id();
		//判断该客户是否已经加入该单位
		List <CCust> custIds = custComponent.queryUnitByResident(custId);
		for(CCust cust : custIds){
			if(unitId.equals(cust.getCust_id()) ){
				throw new ServicesException("已经加入该单位，不能重复加入");
			}
		}
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//保存单位客户和居民客户之间的关系
		custComponent.custJoinUnit(unitId,custId);

		saveAllPublic(doneCode,getBusiParam());
	}
	
	/**
	 * 暂停卡扣
	 * @throws Exception
	 */
	public void saveBankStop()throws Exception{
		String custId = getBusiParam().getCust().getCust_id();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		custComponent.saveBankStop(custId, doneCode);
		
		//生成计算信用度任务
		jobComponent.createCreditCalJob(doneCode, custId, null, SystemConstants.BOOLEAN_TRUE);
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/**
	 * 恢复卡扣
	 * @throws Exception
	 */
	public void saveBankResume()throws Exception{
		String custId = getBusiParam().getCust().getCust_id();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		custComponent.saveBankResume(custId, doneCode);
		
		//生成计算信用度任务
		jobComponent.createCreditCalJob(doneCode, custId, null, SystemConstants.BOOLEAN_TRUE);
		saveAllPublic(doneCode,getBusiParam());
	}
	
	
	public void saveCustJoinUnit(String[] custIds) throws Exception {
		String unitId = getBusiParam().getCust().getCust_id();
		Integer doneCode = doneCodeComponent.gDoneCode();
		List <CCust> custs = custComponent.queryCustByUnit(unitId,custIds);
		if (custs.size()>0){
			throw new ServicesException(custs.get(0).getCust_no()+"已经加入该单位，不能重复加入");
		}
		for (String custId:custIds){
			custComponent.custJoinUnit(unitId,custId);
		}
		saveAllPublic(doneCode,getBusiParam());
	}

	/**
	 * 退出单位
	 */
	public void saveCustQuitUnit(String[] unitIds) throws Exception {
		String custId = getBusiParam().getCust().getCust_id();
		List<CProd> prods=  userProdComponent.queryUnitProdByUnitIds(custId,unitIds);
		if (prods.size()>0)
			throw new ServicesException("选择的单位与当前客户下产品有关联，请退出客户套餐后再退出单位"); 
		
		Integer doneCode = doneCodeComponent.gDoneCode();
		custComponent.custQuitUnit(unitIds, custId, doneCode);

		saveAllPublic(doneCode,getBusiParam());
	}
	
	public List<RDeviceFee> queryDeviceFee(String deviceType,String deviceModel,String buyMode) throws Exception {
		return deviceComponent.queryDeviceFee(deviceType,deviceModel,buyMode);
	}
	
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.ICustService#queryFeeByModel(java.lang.String, java.lang.String, java.lang.String)
	 */
	public RDeviceFee queryFeeByModel(String deviceType,String oldModel,String newModel) throws Exception {
		List<RDeviceFee> oldFeeList = deviceComponent.queryDeviceFee(deviceType,oldModel, SystemConstants.BUSI_BUY_MODE_BUY);
		List<RDeviceFee> newFeeList = deviceComponent.queryDeviceFee(deviceType,newModel, SystemConstants.BUSI_BUY_MODE_BUY);
		List<RDeviceFee> feeList = deviceComponent.queryUpgradeuFee(deviceType);
		RDeviceFee oldFee = new RDeviceFee();
		RDeviceFee newFee = new RDeviceFee();
		RDeviceFee fee = null;
		if(feeList.size() > 0 && oldFeeList.size()>0 && newFeeList.size()>0 ){
			oldFee = oldFeeList.get(0);
			newFee = newFeeList.get(0);
			Integer feeValue = newFee.getFee_value()-oldFee.getFee_value();
			if(feeValue <= 0){
				return null;
			}
			fee =feeList.get(0);
			fee.setFee_value(feeValue);
			fee.setMax_fee_value(feeValue);
		}
		return fee;
	}
	
	
	public com.ycsoft.sysmanager.dto.resource.DeviceDto queryDeviceInfoByCode(String deviceCode) 
		throws Exception {
		com.ycsoft.sysmanager.dto.resource.DeviceDto dev = deviceComponent.queryDeviceInfoByCode(deviceCode);
		if(dev !=null){
			if(dev.getDevice_type().equals(SystemConstants.DEVICE_TYPE_CARD)){
				RStb stb = deviceComponent.findPairStbByCardDeviceId(dev.getDevice_id());
				if(null != stb){
					dev.setPair_device_stb_code(stb.getStb_id());
					dev.setPair_device_stb_model(stb.getDevice_model());
				}
			}else if(dev.getDevice_type().equals(SystemConstants.DEVICE_TYPE_MODEM)){
				RStb stb = deviceComponent.findPairStbByModemDeviceId(dev.getDevice_id());
				if(null != stb){
					dev.setPair_device_stb_code(stb.getStb_id());
					dev.setPair_device_stb_model(stb.getDevice_model());
				}
			}
		}
		return dev;
	}

	public RDeviceModel queryDeviceModelByDeviceType(String deviceId,String deviceType) throws Exception{
		return deviceComponent.queryDeviceModelByDeviceType(deviceId, deviceType);
	}

	public DeviceDto querySaleableDevice(String deviceCode) throws Exception {
		return deviceComponent.querySaleableDevice(deviceCode);
	}
	
	public DeviceDto querySaleableDeviceArea(String deviceCodes,String userType)throws Exception {
		List<DeviceDto> list= deviceComponent.querySaleableDeviceArea(deviceCodes,userType);
		if(list.size()>0){
			return  list.get(list.size()-1);
		}else{
			return null;
		}
	}
	
	public DeviceDto queryChangeDevice(String userType, String deviceCode) throws Exception {
		return deviceComponent.queryChangeDevice(userType, deviceCode);
	}
	
	public Map<String, Object> queryDeviceForSwitch(String deviceCode,String deviceType, String custId) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<CUser> users = userComponent.queryUserByDevice(deviceType, deviceCode);
		if (users == null || users.size() == 0){
			throw new ServicesException(MemoryDict.getDictName(DictKey.DEVICE_TYPE, deviceType) + " " + deviceCode + " 不存在或者未被使用");
		}
		CUser user = users.get(0);
		CUser userDto = userComponent.queryUserById(user.getUser_id());
		result.put("user", userDto);
		
		DeviceDto dev = deviceComponent.queryDevice(deviceCode);
		result.put(deviceCode, dev);
		
		DeviceDto anotherDevice = queryUserAnotherDevice(deviceType, deviceCode,user);
		result.put("anotherDevice", anotherDevice);
		if(StringHelper.isNotEmpty(custId) && !custId.equals(user.getCust_id())){
			CustFullInfoDto cust = custComponent.searchCustInfoById(user.getCust_id());
			result.put("fromCust", cust.getCust());
		}
		
		return result;
	}

	public DeviceDto querySaleableCard(String deviceCode) throws Exception{
		return deviceComponent.querySaleableCard(deviceCode);
	}
	
	public DeviceDto querySaleableModem(String deviceCode) throws Exception{
		return deviceComponent.querySaleableModem(deviceCode);
	}

	public DeviceDto queryDevice(String deviceCode) throws Exception{
		return deviceComponent.queryDevice( deviceCode);
	}
	
	private DeviceDto queryUserAnotherDevice(String deviceType, String deviceCode,CUser user)throws Exception {
		DeviceDto device = new DeviceDto();
		String anotherDeviceCode = null;
		String stb_id = user.getStb_id();
		if (deviceType.equals(SystemConstants.DEVICE_TYPE_CARD)&&StringHelper.isNotEmpty(stb_id)) {
			anotherDeviceCode = stb_id;
		} else if (deviceType.equals(SystemConstants.DEVICE_TYPE_STB)) {
			anotherDeviceCode = user.getCard_id();
		}
		if (anotherDeviceCode==null) return null;
		DeviceDto deviceDto = deviceComponent.queryDeviceByDeviceCode(anotherDeviceCode);
		device.setDevice_id(deviceDto.getDevice_id());
		device.setDevice_type(deviceDto.getDevice_type());
		device.setIsPairCard(deviceDto.getIsPairCard());
		device.setIsPairModem(deviceDto.getIsPairModem());
		device.setPairCard(deviceDto.getPairCard());
		device.setPairModem(deviceDto.getPairModem());
		device.setDevice_model(deviceDto.getDevice_model_text());
		device.setDevice_code(anotherDeviceCode);
		return device;
	}
	
	public DeviceDto queryUserAnotherDevice(String deviceType, String deviceCode)
			throws Exception {
		if (deviceType.equals(SystemConstants.DEVICE_TYPE_MODEM)) {
			throw new ServicesException("无效设备类型");
		}
		List<CUser> userList = userComponent.queryUserByDevice(deviceType, deviceCode);
		if (userList == null || userList.size() == 0)
			throw new ComponentException("设备" + deviceCode + "没有找到对应的用户");
		
		CUser user = userList.get(0);
		return queryUserAnotherDevice(deviceType, deviceCode, user);
	}

	public DeviceDto queryUseableDevice(String custId,String deviceType, String deviceCode,String userType)
			throws Exception {
		DeviceDto device = deviceComponent.queryDeviceByDeviceCode(deviceCode);
		
		if (device == null)
			throw new ServicesException("设备不存在");
		if (device.getIs_loss().equals(SystemConstants.BOOLEAN_TRUE))
			throw new ServicesException("该设备已经挂失，不能使用");
		RDeviceReclaim deviceReclaim = deviceComponent.queryDeviceReclaim(device.getDevice_id());
		if(null != deviceReclaim || device.getTran_status().equals(StatusConstants.UNCONFIRM)){
			throw new ServicesException("设备属于待回收状态，请先操作完成");
		}
		if (!deviceType.equals(device.getDevice_type()))
			throw new ServicesException("设备类型不匹配，输入设备类型是"
					+ MemoryDict.getDictName(DictKey.DEVICE_TYPE, device
							.getDevice_type()));
		if(SystemConstants.BOOLEAN_TRUE.equals(device.getIsPairCard())){
			throw new ComponentException("智能卡有配对的机顶盒，请输入机顶盒号进行配对！");
		}
		if (!device.getDevice_status().equals(StatusConstants.ACTIVE))
			throw new ServicesException("该设备已经损坏，不能使用");
		CCustDevice custDevice = custComponent.queryCustDeviceByDeviceId(device
				.getDevice_id());
		
		if(custDevice == null){
			//如果是虚拟设备
			if(device.getIs_virtual().equals(SystemConstants.BOOLEAN_TRUE)){
				RStb stb = null;
				if(device.getDevice_type().equals(SystemConstants.DEVICE_TYPE_MODEM)){
					stb = deviceComponent.findPairStbByModemDeviceId(device.getDevice_id());
				}else if(device.getDevice_type().equals(SystemConstants.DEVICE_TYPE_CARD)){
					stb = deviceComponent.findPairStbByCardDeviceId(device.getDevice_id());
				}
				if(stb != null){
					custDevice = custComponent.queryCustDeviceByDeviceId(stb.getDevice_id());
				}
			}
		}
		
		if (null != custDevice && SystemConstants.BOOLEAN_TRUE.equals(custDevice.getLoss_reg())){
			throw new ServicesException("该设备已挂失!");
		}
		if (device.getOwnership().equals(SystemConstants.OWNERSHIP_CUST)) {
			// 该设备的当前产权为个人
			if (custDevice != null
					&& custDevice.getStatus().equals(StatusConstants.USE)) {
				if(SystemConstants.DEVICE_TYPE_MODEM.equals(deviceType)){
					if(!custDevice.getCust_id().equals(custId)){
						throw new ServicesException("该设备在其他客户名下，不能使用");
					}
					
					List<CUser> userList = userComponent.queryUserByDevice(deviceType, deviceCode);
					for(CUser user : userList){
						if(userType.equals(user.getUser_type())){
							throw new ServicesException("该设备已经被用于其它用户");
						}
					}
				}else{
					throw new ServicesException("该设备已经被用于其它用户");
				}
			}
		} else {
			// 该设备的当前产权为广电则设备必须在当前客户名下且状态为空闲
			if (custDevice == null) {
				throw new ServicesException("设备属于广电，请先购买此设备");
			} else if (custDevice.getStatus().equals(StatusConstants.USE)) {
				if(SystemConstants.DEVICE_TYPE_MODEM.equals(deviceType)){
					List<CUser> userList = userComponent.queryUserByDevice(deviceType, deviceCode);
					for(CUser user : userList){
						if(userType.equals(user.getUser_type())){
							throw new ServicesException("该设备已经被用于其它用户");
						}
					}
				}else{
					throw new ServicesException("该设备已经被用于其它用户");
				}
			} else if (!custDevice.getCust_id().equals(custId)) {
				throw new ServicesException("该设备在其他客户名下，不能使用");
			}
		}
		return device;
	}

	public List<TDeviceBuyMode> queryDeviceBuyMode() throws Exception {
		return deviceComponent.queryDeviceBuyModel();
	}
	
	public List<RDeviceModelTotalDto> queryDeviceModel()throws Exception{
//		return deviceComponent.queryDeviceModel();
		return deviceComponent.queryMateralTransferDeviceByDepotId(getOptr());
	}

	public List<CustProdDto> queryCustProdForPkg(String custId,
			String custType, String pkgId,String pkgTarrifId) throws Exception {
		return userProdComponent.queryCustProdForPkg(custId,
				custType, pkgId,pkgTarrifId);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.ICustService#queryProdsOfPkg(java.lang.String, java.lang.String)
	 */
	public List<CustProdDto> queryProdsOfPkg(String custId, String pkgId)
			throws Exception {
		return userProdComponent.queryProdsOfPkg(custId, pkgId);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.ICustService#relocateCust(java.lang.String)
	 */
	public void relocateCust(String custId) throws Exception {
		CCust cust = custComponent.queryCustById(custId);
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String status = StatusConstants.LYRELOCATE;
		if(cust.getStatus().equals(StatusConstants.LYRELOCATE)){
			status = StatusConstants.ACTIVE;
		}
		//修改客户状态为拆迁状态
		custComponent.updateCustStatus(doneCode,custId,cust.getStatus(),status);
		
		//生成计算信用度任务
		jobComponent.createCreditCalJob(doneCode, cust.getCust_id(), null,SystemConstants.BOOLEAN_TRUE);
		
		saveAllPublic(doneCode,getBusiParam());
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.ICustService#queryStbModel(java.lang.String)
	 */
	public RStbModel queryStbModel(String stbId) throws Exception {
		return deviceComponent.queryByStbId(stbId);
	}

	public void updateAddressList(List<CCust> custAddrList,List<CCust> custLinkmanList,String busiCode)throws Exception{
		
		Integer doneCode = doneCodeComponent.gDoneCode();
		List<String> custIds = CollectionHelper.converValueToList(custAddrList, "cust_id");
		custComponent.updateAddressList(doneCode,custAddrList,custLinkmanList);
		doneCodeComponent.saveBatchDoneCode(doneCode, busiCode,null, custIds, null);
	}
	
	public void updateAddressList(CCust cust,String newAddrId,String newAddress,String oldAddrText,String busiCode) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		List<String> custIds = custComponent.updateAddressList(doneCode,cust,newAddrId,newAddress, oldAddrText);
		doneCodeComponent.saveBatchDoneCode(doneCode, busiCode,null, custIds, null);
	}


	public String updateCustAddress(String oldAddrId, String newAddrId,SOptr optr)throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		TAddressDto newaddr = custComponent.getAddr(newAddrId);
		TAddressDto oldaddr = custComponent.getAddr(oldAddrId);
		List<CCust> custList = custComponent.queryAddressAll(oldAddrId, optr);
		//地址变更
		List<CCust> custAddrList = custComponent.getCustAddrList(newaddr,oldaddr,optr.getCounty_id());
		//邮件变更
		List<CCust> custLinkmanList = custComponent.getCustLinkmanAddrList(newaddr,oldaddr,optr.getCounty_id());
		
		List<String> custIds = CollectionHelper.converValueToList(custList, "cust_id");
		custComponent.updateAddressList(doneCode,custAddrList,custLinkmanList);
		custComponent.updateCustAddr(oldAddrId, newAddrId, optr.getCounty_id());
		doneCodeComponent.saveBatchDoneCode(doneCode, BusiCodeConstants.ADDRESS_UPDATE_ALL,null, custIds, null);
		for(int i=custList.size()-1;i>=0;i--){
			CCust cust = custList.get(i);
			boolean flag=false;
			for (CCust dto:custAddrList){
				if (cust.getCust_id().equals(dto.getCust_id())){
					flag = true;
					break;
				}
			}
			if (flag)
				custList.remove(i);
		}
		if(custList.size()>0){
			String src ="";
			for(CCust dto :custList){
				src +=dto.getCust_id()+",";
			}
			src = StringHelper.delEndChar(src, 1);
			return src;
		}else{
			return null;
		}
	}
	
	public void updateCustStatus(List<String> custNos,String custStatus) throws Exception {
		
		List<CCust> custList = custComponent.queryCustByCustNos(custNos);
		if(custList == null || custList.size()==0)
			throw new Exception("未查询到客户，请确定客户编号是否正确");
		List<CCustPropChange> cpcList = new ArrayList<CCustPropChange>();
		//批量操作，流水不能取操作员countyId
		List<CDoneCode> dcList = new ArrayList<CDoneCode>();
		List<CDoneCodeDetail> dcdList = new ArrayList<CDoneCodeDetail>();
		BusiParameter bp = getBusiParam();
		for(CCust cust : custList){
			String status = cust.getStatus();
			if(!status.equals(StatusConstants.DATACLOSE)){
				Integer doneCode = doneCodeComponent.gDoneCode();
				String custId = cust.getCust_id();
				String countyId = cust.getCounty_id();
				String areaId = cust.getArea_id();
				
				CCustPropChange cpc = new CCustPropChange();
				cpc.setCust_id(custId);
				cpc.setColumn_name("status");
				cpc.setOld_value(status);
				cpc.setNew_value(custStatus);
				cpc.setDone_code(doneCode);
				cpc.setArea_id(areaId);
				cpc.setCounty_id(countyId);
				cpcList.add(cpc);
				cust.setStatus(custStatus);
				
				CDoneCode cDoneCode = new CDoneCode();
				cDoneCode.setDone_code(doneCode);
				cDoneCode.setBusi_code(bp.getBusiCode());
				cDoneCode.setStatus(StatusConstants.ACTIVE);
				cDoneCode.setCounty_id(countyId);
				cDoneCode.setArea_id(areaId);
				cDoneCode.setDept_id(getOptr().getDept_id());
				cDoneCode.setOptr_id(getOptr().getOptr_id());
				dcList.add(cDoneCode);
				
				CDoneCodeDetail detail = new CDoneCodeDetail();
				detail.setDone_code(doneCode);
				detail.setCust_id(custId);
				detail.setArea_id(areaId);
				detail.setCounty_id(countyId);
				dcdList.add(detail);
			}
		}
		custComponent.updateCustStatus(custList, cpcList,dcList,dcdList);
	}

	public String renewCust() throws Exception {
		BusiParameter bp = getBusiParam();
		Integer doneCode = doneCodeComponent.gDoneCode();
		CCust cust = getBusiParam().getCustFullInfo().getCust();
		String status = custComponent.renewCust(cust.getCust_id(), doneCode);
		
		List<CUser> userList = userComponent.queryUserByCustId(cust.getCust_id());
		for(CUser user : userList){
			//修改隔离用户产品状态为正常
			List<CProdDto> prodList = userProdComponent.queryByUserId(user.getUser_id());
			for (CProdDto prod:prodList){
				if(StatusConstants.ISOLATED.equals(prod.getStatus())){
					List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
					changeList.add(new CProdPropChange("status",
							prod.getStatus(),StatusConstants.ACTIVE));
					changeList.add(new CProdPropChange("status_date",
							DateHelper.dateToStr(prod.getStatus_date()),DateHelper.dateToStr(new Date())));
					
					userProdComponent.editProd(doneCode,prod.getProd_sn(),changeList);
					
					//生成钝化产品任务
					jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, user.getCust_id(),
						user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), prod.getProd_sn(),prod.getProd_id());
				}
			}
		}
		
		saveAllPublic(doneCode, bp);
		return status;
	}
	

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.ICustService#batchLogoffCust(java.util.List, java.lang.String)
	 */
	public void batchLogoffCust(List<String> custIdList, String isReclaimDevice, String deviceStatus, String remark)
			throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		
		if(null != custIdList && custIdList.size() == 0){
			throw new ServicesException("表格数据为空");
		}
		List<String> custIds = new ArrayList<String>();
		for(String userId : custIdList){
			if(!custIds.contains(userId)){
				custIds.add(userId);
			}
		}
		
		int size = custIds.size();
		List<CUser> userList = null;
		for(int i=0;i<size;i+=500){
			if(size > i){
				if(size - i > 500){
					userList =  userComponent.queryUserByCustIds(custIds.subList(i, i+500).toArray(new String[500]));
				}else{
					userList = userComponent.queryUserByCustIds(custIds.subList(i, size).toArray(new String[size-i]));
				}
				
				if(null !=userList && userList.size() > 0){
					throw new ServicesException("某客户下仍存在用户，请先销用户");
				}
			}
			
		}
		custComponent.batchLogoffCust(doneCode,remark,custIds,isReclaimDevice,deviceStatus);
		
		saveDoneCode(doneCode, busiCode, null,remark);
	}

	public Pager<TNonresCustApproval> queryNonresCustApp(Integer start,Integer limit) throws Exception{
		return custComponent.queryNonresCustApp(StatusConstants.IDLE, null, start, limit);
	}

	/**
	 * 
	 */
	public void savetaskFinish(String taskId, int success,String failureCause, Date finishTime)throws Exception {
		this.taskComponent.saveTaskFinish(taskId, success, failureCause, finishTime);
	}

	public List<RDeviceModelTotalDto> queryDeviceCanBuy(SOptr optr) throws Exception {
		
		return deviceComponent.queryDeviceCanBuy(optr.getDept_id());
	}

	public void saveBacthBuyMaterial(List<RDeviceModelTotalDto> feeInfoList)
			throws Exception {
		CCust cust = getBusiParam().getCustFullInfo().getCust();
		doneCodeComponent.lockCust(cust.getCust_id());
		String busiCode = getBusiParam().getBusiCode();
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		
		Integer isFee = 0;
		for(RDeviceModelTotalDto dto : feeInfoList){
			//获取本地该器材的数量
			RDevice device = deviceComponent.queryTotalNumDevice(dto.getDevice_model(), getOptr().getDept_id());
			//本地器材数量减去已购数量
			deviceComponent.removeTotalNumDevice(doneCode,BusiCodeConstants.DEVICE_BUY_PJ_BACTH,device.getDevice_id(), dto.getBuy_num(),SystemConstants.BUSI_BUY_MODE_BUY,getOptr());
			
			//保存费用
			String feeId = dto.getFee_id();
			String feeStdId = dto.getFee_std_id();
			Integer fee = dto.getFee();	
			if(fee>0){
				feeComponent.saveDeviceFee( cust.getCust_id(),cust.getAddr_id(), feeId,feeStdId,
						StatusConstants.UNPAY,SystemConstants.DEVICE_TYPE_FITTING, 
						device.getDevice_id(), null, null, null, null, null,dto.getDevice_model(),
						fee, doneCode,doneCode, busiCode, dto.getBuy_num());
				isFee = isFee + fee;
			}
		}
		
		if(isFee>0){
			//记录未支付业务
			doneCodeComponent.saveDoneCodeUnPay(cust.getCust_id(), doneCode, getOptr().getOptr_id());
		}
		
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/**
	 * 生成故障单
	 * @param bugDetail
	 * @throws Exception
	 */
	public void saveBugTask(String bugDetail, String bugPhone) throws Exception{
		CCust cust = getBusiParam().getCust();
		String  custId = cust.getCust_id();
		doneCodeComponent.lockCust(custId);
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		String taskId=snTaskComponent.createBugTask(doneCode, cust, bugDetail, bugPhone);
		this.getBusiParam().setOperateObj("WorkOrdersSn:"+taskId);
		saveAllPublic(doneCode, getBusiParam());
	}
	
}
