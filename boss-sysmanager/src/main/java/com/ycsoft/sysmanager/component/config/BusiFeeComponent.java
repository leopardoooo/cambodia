package com.ycsoft.sysmanager.component.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TAcctFeeType;
import com.ycsoft.beans.config.TBusiCode;
import com.ycsoft.beans.config.TBusiCodeFee;
import com.ycsoft.beans.config.TBusiDoc;
import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.config.TDeviceBuyMode;
import com.ycsoft.beans.config.TDeviceBuyModeFee;
import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.business.dao.config.TAcctFeeTypeDao;
import com.ycsoft.business.dao.config.TAcctitemToProdDao;
import com.ycsoft.business.dao.config.TBusiCodeDao;
import com.ycsoft.business.dao.config.TBusiCodeFeeDao;
import com.ycsoft.business.dao.config.TBusiDocDao;
import com.ycsoft.business.dao.config.TBusiFeeDao;
import com.ycsoft.business.dao.config.TBusiFeeStdDao;
import com.ycsoft.business.dao.config.TDeviceBuyModeDao;
import com.ycsoft.business.dao.config.TDeviceBuyModeFeeDao;
import com.ycsoft.business.dao.system.SSysChangeDao;
import com.ycsoft.business.dto.config.TBusiFeeDto;
import com.ycsoft.business.dto.core.fee.BusiFeeDto;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.sysmanager.dto.config.VewAcctitemDto;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;
@Component
public class BusiFeeComponent extends  BaseComponent {
	private TBusiFeeDao tBusiFeeDao;
	private TBusiFeeStdDao tBusiFeeStdDao;
	private TAcctitemToProdDao tAcctitemToProdDao;
	private TBusiDocDao tBusiDocDao;
	private TAcctFeeTypeDao tAcctFeeTypeDao;
	private TBusiCodeDao tBusiCodeDao;
	private TBusiCodeFeeDao tBusiCodeFeeDao;
	private TDeviceBuyModeFeeDao tDeviceBuyModeFeeDao;
	private TDeviceBuyModeDao tDeviceBuyModeDao;
	private SSysChangeDao sSysChangeDao;
	
	/**
	 * 查询当个费用项目
	 * @param fee_id
	 * @return
	 * @throws Exception
	 */
	public TBusiFeeDto queryTBusiFeeDto(String fee_id) throws Exception{
		if(StringHelper.isNotEmpty(fee_id)){
			Map<String,TBusiFeeDto> map= CollectionHelper.converToMapSingle(this.queryFee("",""), "fee_id") ;
			return map.get(fee_id);
		}
		return null;
	}
	/**
	 * 保存费用项异动
	 * @param oldBean
	 * @param newBean
	 * @throws Exception
	 */
	public void saveChangeTBusiFeeDto(TBusiFeeDto oldBean,TBusiFeeDto newBean) throws Exception{
	
		String change_text=BeanHelper.beanchange(oldBean, newBean);
		if(StringHelper.isNotEmpty(change_text)){
			SSysChange change=new SSysChange();
			change.setContent(change_text);
			change.setCreate_time(new Date());
			change.setOptr_id(WebOptr.getOptr().getOptr_id());
			change.setChange_type(SysChangeType.BUSIFEE.name());
			change.setKey(oldBean==null?newBean.getFee_id():oldBean.getFee_id());
			change.setKey_desc(oldBean==null?newBean.getFee_name():oldBean.getFee_name());
			change.setChange_desc("费用项定义");
			sSysChangeDao.save(change);
		}
	}
	/**
	 * 查询销售类型改为BUSI的销售信息
	 * @return
	 * @throws Exception
	 */
	public List<TDeviceBuyMode> queryBusiDeviceBuyMode() throws Exception {
		return tDeviceBuyModeDao.queryDeviceBuyModeByBuyType(SystemConstants.BUY_TYPE_BUSI);
	}
	
	/**
	 * 根据业务类型查询业务编号定义
	 * @return
	 * @throws Exception
	 */
	public List<TBusiCode> queryBusiCodeByBusiType() throws Exception {
		return tBusiCodeDao.queryBusiCodeByBusiFee(SystemConstants.BOOLEAN_TRUE);
	}

	//保存、修改TBusiDoc
	public void updateBusiDoc(TBusiDoc busiDoc) throws Exception{
		String doc_type = busiDoc.getDoc_type();
		if(StringHelper.isEmpty(doc_type)){
			busiDoc.setDoc_type(null);
			tBusiDocDao.save(busiDoc);
		}else{
			tBusiDocDao.update(busiDoc);
		}
	}


	//所有专项公用账目查询
	public Pager<VewAcctitemDto> queryAllVewAcctitem(Integer start,Integer limit,String key) throws Exception{
		return tAcctitemToProdDao.queryAllVewAcctitem(start,limit,key);
	}

	/**
	 * @Description:查询配置信息并分页
	 * @param status 状态
	* @param feeType
	* @return
	* @throws Exception
	* @return List<TBusiFee>
	 */
	public List<TBusiFeeDto> queryFee(String query, String status)throws Exception{
		 List<TBusiFeeDto> busiFeeList = tBusiFeeDao.queryFee(query, WebOptr.getOptr().getCounty_id(),status) ;
		 
		 Map<String,List<TBusiFeeDto>> map = CollectionHelper.converToMap(busiFeeList, "fee_id");
		 for(String feeId : map.keySet()){
			 List<TBusiFeeDto> list = map.get(feeId);
			 if(list.size()>1){
				 String busiCode="",busiName="",buyMode="",buyModeName="";
				 
				 for(TBusiFeeDto dto : list){
					 if(StringHelper.isNotEmpty(dto.getBusi_code())){
						 busiCode += dto.getBusi_code()+",";
						 busiName += dto.getBusi_name()+",";
					 }else if(StringHelper.isNotEmpty(dto.getBuy_mode())){
						 buyMode += dto.getBuy_mode()+",";
						 buyModeName += dto.getBuy_mode_name()+",";
					 }
				 }
				 TBusiFeeDto feeDto = list.get(0);
				 if(StringHelper.isNotEmpty(busiCode)){
					 feeDto.setBusi_code(busiCode.substring(0, busiCode.length()-1));
					 feeDto.setBusi_name(busiName.substring(0, busiName.length()-1));
				 }
				 if(StringHelper.isNotEmpty(buyMode)){
					 feeDto.setBuy_mode(buyMode.substring(0, buyMode.length()-1));
					 feeDto.setBuy_mode_name(buyModeName.substring(0, buyModeName.length()-1));
				 }
				 List<TBusiFeeDto> newList = new ArrayList<TBusiFeeDto>();
				 newList.add(feeDto);
				 map.put(feeId, newList);//覆盖当前key对应的值,将list中的多个对象覆盖成一个对象
			 }
		 }
		 List<TBusiFeeDto> allList = new ArrayList<TBusiFeeDto>();
		 for(String key : map.keySet()){
			 allList.addAll(map.get(key));//合并
		 }
		 return allList;
	}

	public void setTAcctitemToProdDao(TAcctitemToProdDao acctitemToProdDao) {
		tAcctitemToProdDao = acctitemToProdDao;
	}

	/**
	* @Description:保存费用配置
	* @param busiFee
	* @return
	* @throws Exception
	* @return boolean
	 */
	public boolean saveFee(TBusiFeeDto busiFeeDto) throws Exception{
		String feeIdSEQ = tBusiFeeDao.findSequence().toString();
		String busiCode = busiFeeDto.getBusi_code();
		String buyMode = busiFeeDto.getBuy_mode();
		
		TBusiFee busiFee = new TBusiFee();
		BeanUtils.copyProperties(busiFeeDto, busiFee);
		String feeId = busiFee.getFee_id();//前台传递的feeId
		if(StringHelper.isEmpty(feeId)){//保存
			busiFee.setFee_id(feeIdSEQ);
			busiFee.setStatus(StatusConstants.ACTIVE);
			busiFee.setCounty_id(WebOptr.getOptr().getCounty_id());
			busiFee.setArea_id(WebOptr.getOptr().getArea_id());
			busiFee.setOptr_id(WebOptr.getOptr().getOptr_id());
			tBusiFeeDao.save(busiFee);
			//异动记录需要
			busiFeeDto.setFee_id(feeIdSEQ);
		}
		else{//修改
			tBusiFeeDao.update(busiFee);
		}
		
		//当费用配置类型为“服务费”时
		if(StringHelper.isNotEmpty(busiCode)){
			String busiFeeId = feeIdSEQ;//添加到t_busi_code_fee中的fee_id,默认为添加
			if(StringHelper.isNotEmpty(feeId)){//修改时，先删除
				tBusiCodeFeeDao.deleteByFeeId(feeId);
				busiFeeId = feeId;
			}
			String[] busiCodeArr = busiCode.split(",");//业务为多选，值用","隔开
			List<TBusiCodeFee> codeFeeList = new ArrayList<TBusiCodeFee>();
			TBusiCodeFee codeFee = null;
			for(String code : busiCodeArr){
				codeFee = new TBusiCodeFee();
				codeFee.setBusi_code(code);
				codeFee.setFee_id(busiFeeId);
				codeFeeList.add(codeFee);
			}
			tBusiCodeFeeDao.save(codeFeeList.toArray(new TBusiCodeFee[codeFeeList.size()]));
		}else if(StringHelper.isNotEmpty(buyMode)){//当费用配置为"设备费"时
			String buyFeeId = feeIdSEQ;//添加到t_device_buy_mode_fee中的fee_id,默认为添加
			if(StringHelper.isNotEmpty(feeId)){
				tDeviceBuyModeFeeDao.deleteByFeeId(feeId);
				buyFeeId = feeId;
			}
			String[] buyModeArr = buyMode.split(",");//销售方式为多选，值用","隔开
			tDeviceBuyModeFeeDao.deleteBach(buyModeArr);//t_device_buy_mode_fee中buy_mode为PK
			
			List<TDeviceBuyModeFee> modeFeeList = new ArrayList<TDeviceBuyModeFee>();
			TDeviceBuyModeFee modeFee = null;
			for(String mode : buyModeArr){
				modeFee = new TDeviceBuyModeFee();
				modeFee.setBuy_mode(mode);
				modeFee.setFee_id(buyFeeId);
				modeFeeList.add(modeFee);
			}
			tDeviceBuyModeFeeDao.save(modeFeeList.toArray(new TDeviceBuyModeFee[modeFeeList.size()]));
		}
//		int sues = tBusiFeeDao.save(busiFee)[0];
//		
//		if(sues >= 0 || sues == -2){
//			return true;
//		}
		return true;
	}

	/**
	* @Description:更新费用配置
	* @param busiFee
	* @return
	* @throws Exception
	* @return boolean
	 */
	public boolean updateFee(TBusiFee busiFeeDto) throws Exception{
		TBusiFee busiFee = new TBusiFee();
		BeanUtils.copyProperties(busiFeeDto, busiFee);
		int sues = tBusiFeeDao.update(busiFee)[0];
		
		if(sues >= 0 || sues == -2){
			return true;
		}
		return false;

	}

	/**
	* @Description: 删除费用配置
	* @param busiFee
	* @return
	* @throws Exception
	* @return boolean
	 */
	public boolean updateFeeStatus(String feeId, String status) throws Exception{
		return tBusiFeeDao.updateFeeStatus(feeId, status)>0;
	}


/**
 * ------------------------------------------------------------------------
* 费用金额组件
 */


	/**
	* @Description: 查询金额信息
	* @param start
	* @param limit
	* @param feeType
	* @return
	* @throws Exception
	* @return Pager<BusiFeeDto>
	*/
	public Pager<BusiFeeDto> queryFeeValue(Integer start , Integer limit , String feeType)throws Exception{

		 return tBusiFeeStdDao.queryFeeValue(start, limit, feeType) ;
	}

	/**
	 * 查询所有的设备费用
	 *
	 * @return
	 * @throws Exception
	 */
	public List<TBusiFee> queryDeviceFee() throws Exception {
		return tBusiFeeDao.queryFeeByFeeType(SystemConstants.FEE_TYPE_DEVICE);
	}
	
	/**
	 * 查询所有费用类型数据
	 * @return
	 * @throws Exception
	 */
	public List<TAcctFeeType> queryAllFeeType() throws Exception{
		return tAcctFeeTypeDao.findAll();
	}

	/**
	 * 更新费用类型数据
	 * @param feeTypeList
	 * @throws Exception
	 */
	public void updateFeeType(List<TAcctFeeType> feeTypeList)throws Exception{
		if(feeTypeList != null && feeTypeList.size() > 0){
			tAcctFeeTypeDao.update(feeTypeList.toArray(new TAcctFeeType[feeTypeList.size()]));
		}
	}

	public void setTBusiFeeDao(TBusiFeeDao busiFeeDao) {
		tBusiFeeDao = busiFeeDao;
	}

	public void setTBusiDocDao(TBusiDocDao busiDocDao) {
		tBusiDocDao = busiDocDao;
	}

	public TAcctFeeTypeDao getTAcctFeeTypeDao() {
		return tAcctFeeTypeDao;
	}

	public void setTAcctFeeTypeDao(TAcctFeeTypeDao acctFeeTypeDao) {
		tAcctFeeTypeDao = acctFeeTypeDao;
	}

	/**
	 * @param busiFeeStdDao the tBusiFeeStdDao to set
	 */
	public void setTBusiFeeStdDao(TBusiFeeStdDao busiFeeStdDao) {
		tBusiFeeStdDao = busiFeeStdDao;
	}

	public void setTBusiCodeDao(TBusiCodeDao busiCodeDao) {
		tBusiCodeDao = busiCodeDao;
	}

	public void setTBusiCodeFeeDao(TBusiCodeFeeDao busiCodeFeeDao) {
		tBusiCodeFeeDao = busiCodeFeeDao;
	}

	public void setTDeviceBuyModeFeeDao(TDeviceBuyModeFeeDao deviceBuyModeFeeDao) {
		tDeviceBuyModeFeeDao = deviceBuyModeFeeDao;
	}

	public void setTDeviceBuyModeDao(TDeviceBuyModeDao deviceBuyModeDao) {
		tDeviceBuyModeDao = deviceBuyModeDao;
	}
	public SSysChangeDao getSSysChangeDao() {
		return sSysChangeDao;
	}

	public void setSSysChangeDao(SSysChangeDao sysChangeDao) {
		sSysChangeDao = sysChangeDao;
	}
}
