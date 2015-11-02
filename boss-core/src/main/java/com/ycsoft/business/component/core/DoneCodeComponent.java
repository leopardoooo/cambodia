package com.ycsoft.business.component.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.common.CDoneCodeDetail;
import com.ycsoft.beans.core.common.CDoneCodeInfo;
import com.ycsoft.beans.core.common.ExtCDoneCode;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.config.TBusiConfirmDao;
import com.ycsoft.business.dao.core.common.CDoneCodeInfoDao;
import com.ycsoft.business.dao.core.common.CDoneCodeUnpayDao;
import com.ycsoft.business.dao.core.common.ExtCDoneCodeDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.fee.CFeeDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.core.user.CUserHisDao;
import com.ycsoft.business.dto.core.cust.DoneCodeDto;
import com.ycsoft.business.dto.core.cust.DoneCodeExtAttrDto;
import com.ycsoft.business.dto.core.cust.DoneInfoDto;
import com.ycsoft.business.dto.core.cust.ExtAttributeDto;
import com.ycsoft.business.dto.core.fee.FeeDto;
import com.ycsoft.business.dto.core.fee.QueryFeeInfo;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

/**
 * 通用业务组件，包括以下功能
 * 1、保存业务流水及流水明细
 * 2、保存需要打印的业务单据
 *
 * @author pyb
 *
 * Mar 16, 2010
 *
 */
@Component
public class DoneCodeComponent extends BaseBusiComponent {
	private CDoneCodeInfoDao cDoneCodeInfoDao;
	private TBusiConfirmDao tBusiConfirmDao;
	private CDoneCodeUnpayDao cDoneCodeUnpayDao;
	@Autowired
	private CCustDao cCustDao;
	@Autowired
	private CFeeDao cFeeDao;
	@Autowired
	private CUserDao cUserDao;
	@Autowired
	private CUserHisDao cUserHisDao;
	@Autowired
	private ExtCDoneCodeDao extCDoneCodeDao;
	/**
	 * 给业务增加用户锁，防止并发临界时数据不一致。
	 * @param cust_id
	 * @throws JDBCException 
	 */
	public boolean lockCust(String cust_id) throws JDBCException{
		if( cCustDao.lockCust(cust_id)==null){
			return false;
		}else{
			return true;
		}
	}
	
	public void saveDoneCodeDetail(Integer doneCode, String custId, List<String> userIdList) throws Exception {
		CDoneCodeDetail[] detail = new CDoneCodeDetail[userIdList.size()];
		for( int i=0;i<userIdList.size();i++){
			detail[i] = new CDoneCodeDetail();
			detail[i].setDone_code(doneCode);
			detail[i].setCust_id(custId);
			detail[i].setUser_id(userIdList.get(i));
			detail[i].setArea_id(getOptr().getArea_id());
			detail[i].setCounty_id(getOptr().getCounty_id());
		}
		cDoneCodeDetailDao.save(detail);
	}
	
	public void saveDoneCode(Integer doneCode, String busiCode, String addrId) throws Exception {
		CDoneCode cDoneCode = new CDoneCode();
		cDoneCode.setDone_code(doneCode);
		cDoneCode.setBusi_code(busiCode);
		cDoneCode.setStatus(StatusConstants.ACTIVE);
		cDoneCode.setAddr_id(addrId);
		cDoneCode.setService_channel(SystemConstants.SERVICE_CHANNEL_YYT);
		setBaseInfo(cDoneCode);
		cDoneCodeDao.save(cDoneCode);
	}
	
	/**
	 * 保存未支付业务
	 * @param cust_id
	 * @param done_code
	 * @throws JDBCException
	 */
	public void saveDoneCodeUnPay(String cust_id,Integer done_code,String optr_id) throws Exception{
		if(cDoneCodeUnpayDao.findByKey(done_code)!=null){
				return;//已经保存过，不需要重复保存
		}
		cDoneCodeUnpayDao.saveUnpay(cust_id, done_code,optr_id);
		
	}
	/**
	 * 业务被其他营业员锁定检查
	 * @param cust_id
	 * @param done_code
	 * @param optr_id
	 * @throws Exception
	 
	public void checkUnPayOtherLock(String cust_id,String optr_id)throws Exception{
		List<CDoneCodeUnpay>  otherLocks=cDoneCodeUnpayDao.queryUnPayOtherLock(cust_id,optr_id);
		if(otherLocks!=null&&otherLocks.size()>0){	
			String login_name = MemoryDict.getDictName(DictKey.OPTR_LOGIN, otherLocks.get(0).getOptr_id());
			String optr_name=MemoryDict.getDictName(DictKey.OPTR, otherLocks.get(0).getOptr_id());
			throw new ComponentException(ErrorCode.UnPayLock,optr_name,login_name);
		}
	}
	*/

	/**
	 * 
	 * @param doneCode
	 * @return
	 * @throws JDBCException
	
	public CDoneCodeUnpay queryDoneCodeUnPayByKey(Integer doneCode) throws JDBCException{
		return cDoneCodeUnpayDao.findByKey(doneCode);
	}
	 */
	/**
	 * 加锁查询未支付业务
	 * @param cust_id
	 * @return
	 * @throws JDBCException 
	 
	public List<CDoneCodeUnpay> queryUnPayList(String cust_id) throws JDBCException{
		return cDoneCodeUnpayDao.queryUnPay(cust_id);
	}
	*/
	/**
	 * 查询一个营业员的未支付业务
	 * @param optr_id
	 * @return
	 * @throws JDBCException
	 
	public List<CDoneCodeUnpay> queryUnPayByOptr(String optr_id) throws JDBCException{
		return cDoneCodeUnpayDao.queryUnPayByOptr(optr_id);
	}
	*/
	/**
	 * 删除未支付业务信息
	 * @param unPayList
	 * @throws JDBCException
	 */
	public void deleteDoneCodeUnPay(List<FeeDto> feeList) throws JDBCException{
	
		Set<Integer> doneCodeSet=new HashSet<>();
		
		for(FeeDto fee:feeList){
			doneCodeSet.add(fee.getCreate_done_code());
		}
		for(Integer doneCode:doneCodeSet){
			List<CFee> list=cFeeDao.queryUnPayByDoneCode(doneCode);
			if(list==null||list.size()==0){
				//不存在未支付则删除未支付业务流水号
				cDoneCodeUnpayDao.remove(doneCode);
			}
		}
		
	} 
		
	public void updateStatus(Integer doneCode,String busiCode) throws Exception{
		CDoneCode cDoneCode = new CDoneCode();
		cDoneCode.setDone_code(doneCode);
		cDoneCode.setStatus(StatusConstants.INVALID);
		cDoneCodeDao.update(cDoneCode);
		
		cDoneCodeDao.saveCancel(doneCode,busiCode);
	}

	/**
	 * 获取业务流水
	 *
	 */
	public Integer gDoneCode() throws Exception{
		return Integer.parseInt(cDoneCodeDao.findSequence().toString());
	}

	public CDoneCode queryByKey(Integer doneCode) throws Exception{
		return cDoneCodeDao.findByKey(doneCode);
	}

	public Pager<DoneInfoDto> getUserOpenDate(Integer cDoneCode, Integer start, Integer limit) throws Exception {
		return cDoneCodeDao.getUserOpenDate(cDoneCode, start, limit);
	}
	
	public Pager<DoneInfoDto> getOrderProdDate(Integer cDoneCode,String custId, Integer start, Integer limit) throws Exception {
		return cDoneCodeDao.getOrderProdDate(cDoneCode, custId, start, limit);
	}
	
	public Pager<DoneInfoDto> getBandUpgradeDate(Integer cDoneCode,String countyId, Integer start, Integer limit) throws Exception {
		return cDoneCodeDao.getBandUpgradeDate(cDoneCode, countyId, start, limit);
	}
	
	public Pager<DoneInfoDto> getDeviceChangeDate(Integer cDoneCode,String countyId, Integer start, Integer limit) throws Exception {
		return cDoneCodeDao.getDeviceChangeDate(cDoneCode, countyId, start, limit);
	}
	
	public Pager<DoneInfoDto> getDeviceBuyDate(Integer cDoneCode,String countyId, Integer start, Integer limit) throws Exception {
		return cDoneCodeDao.getDeviceBuyDate(cDoneCode, countyId, start, limit);
	}
	
	public Pager<DoneInfoDto> getPromotionDate(Integer cDoneCode,String countyId, Integer start, Integer limit) throws Exception {
		return cDoneCodeDao.getPromotionDate(cDoneCode, countyId, start, limit);
	}
	
	public Pager<DoneInfoDto> getPromFeeDate(Integer cDoneCode,String countyId, Integer start, Integer limit) throws Exception {
		return cDoneCodeDao.getPromFeeDate(cDoneCode, countyId, start, limit);
	}
	
	/**
	 * 查找donecode之前可以回退且不可忽略的记录条数
	 * @param doneCode
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public int getNeedCancleCount(Integer doneCode,String custId) throws Exception{
		List<CDoneCode> list  = cDoneCodeDao.queryAfterDoneCode(doneCode, custId, getOptr().getCounty_id());
		return list== null?0:list.size();
	}
	/**
	 * 根据用户ID， 查询用户受理记录
	 * @param userId
	 */
	public List<CDoneCode> queryByUserId(String userId) throws Exception{
		List<CDoneCode> doneCodeList =  cDoneCodeDao.queryUserDoneCode(userId, getOptr().getCounty_id());
		return doneCodeList;
	}

	/**
	 * 查询用户几个月内临时授权的次数
	 * @param userId
	 * @param months
	 * @return
	 * @throws Exception
	 */
	public int queryOpenTempTimes(String userId,int months) throws Exception{
		return cDoneCodeDao.queryOpenTempTimes(userId, months,getOptr().getCounty_id());
	}
	/**
	 *  通过客户ID，查询客户受理记录,并关联业务的扩展信息
	 * @param custId
	 */
	public Pager<DoneCodeExtAttrDto> queryByCustId(String custId, QueryFeeInfo queryFeeInfo,
			Integer start,Integer limit)throws Exception{
		Pager<DoneCodeDto> pageLstDone = cDoneCodeDao.queryCustDoneCode(custId, queryFeeInfo, getOptr().getCounty_id(),start,limit);
		
		Pager<DoneCodeExtAttrDto> pageTarget = new Pager<DoneCodeExtAttrDto>();
		List<DoneCodeExtAttrDto> target = new ArrayList<DoneCodeExtAttrDto>();
		
		pageTarget.setRecords(target);
		pageTarget.setStart(pageLstDone.getStart());
		pageTarget.setLimit(pageLstDone.getLimit());
		pageTarget.setTotalProperty(pageLstDone.getTotalProperty());
		
		ExtAttributeDto temp = null ;
		DoneCodeExtAttrDto tempQ = null;
		List<DoneCodeDto> lstDone = pageLstDone.getRecords();
		String[] doneCodeArr =  CollectionHelper.converValueToArray(lstDone, "done_code");
		List<DoneCodeDto> queryCfeeByDoneCode = cDoneCodeDao.queryCfeeByDoneCode(doneCodeArr,custId);
		Map<String, List<DoneCodeDto>> map2 = CollectionHelper.converToMap(queryCfeeByDoneCode, "reverse_done_code");
		for (DoneCodeDto doneCodeDto : lstDone) {			
			temp = new ExtAttributeDto( doneCodeDto );
			tempQ = null;			
			for (DoneCodeExtAttrDto q : target) {				
				if(q.getDone_code().equals(doneCodeDto.getDone_code())){
					tempQ = q;
					break;
				}
			}
			if(null == tempQ){
				tempQ = new DoneCodeExtAttrDto();
				//增加作废的负金额
				
				List<DoneCodeDto> list = map2.get(doneCodeDto.getDone_code().toString());
				if(CollectionHelper.isNotEmpty(list)){
					Integer realPay = 0;
					for(DoneCodeDto d:list){
						realPay += d.getReal_pay();
					}
					doneCodeDto.setReal_pay(0-realPay);
				}
				
				BeanUtils.copyProperties(doneCodeDto, tempQ);
				target.add(tempQ);
			}
			if(StringHelper.isNotEmpty(doneCodeDto.getAttribute_id())){
				StringBuffer str = new StringBuffer();
				if(StringHelper.isNotEmpty(temp.getAttribute_value())){
					str.append("["+temp.getAttribute_name()+"]:"+temp.getAttribute_value()+";");
					if(StringHelper.isNotEmpty(tempQ.getAttr_remark())){
						str.append(tempQ.getAttr_remark());
					}
					tempQ.setAttr_remark(str.toString());
				}
				tempQ.getExtAttrs().add(temp);
			}
			if(doneCodeDto.getBusi_code().equals(BusiCodeConstants.USER_OPEN)){
				CUser user = cUserDao.findByKey(doneCodeDto.getUser_id());
				if(user == null ){
					user = (CUser)cUserHisDao.findByKey(doneCodeDto.getUser_id());
				}
				String str = "";
				if(user!=null){
					if(str.equals(SystemConstants.USER_TYPE_BAND) && StringHelper.isNotEmpty(user.getModem_mac())){
						str += user.getUser_type()+" Modem: "+user.getModem_mac();
					}else  if(StringHelper.isNotEmpty(user.getStb_id())){
						str += user.getUser_type()+" Stb: "+user.getStb_id();
					}
				}
				tempQ.setRemark(str);
			}
		}
		if(pageTarget.getRecords()!=null&&pageTarget.getRecords().size()>0){
			for(DoneCodeExtAttrDto dto:  pageTarget.getRecords()){
				StringBuilder buffer=new StringBuilder();
				for(ExtCDoneCode ext:extCDoneCodeDao.queryExtOperateObjByDoneCode(dto.getDone_code())){
					buffer.append(ext.getAttribute_value()).append(" ");
				}
				dto.setAttr_remark(buffer.toString());
			}
		}
		return pageTarget;
	}

	public List<CDoneCodeDetail> queryDetail(Integer doneCode) throws Exception{
		return cDoneCodeDetailDao.queryDetail(doneCode);
	}
	
	public void saveDoneCodeInfo(Integer doneCode, String custId, String userId,Object info)
			throws Exception {
		CDoneCodeInfo doneCodeInfo = new CDoneCodeInfo();
		doneCodeInfo.setDone_code(doneCode);
		doneCodeInfo.setArea_id(getOptr().getArea_id());
		doneCodeInfo.setCounty_id(getOptr().getCounty_id());
		doneCodeInfo.setUser_id(userId);
		if(info == null){
			doneCodeInfo.setInfo("");
		}else{
			String strInfo = new Gson().toJson(info);
			if(strInfo.length()<3000){
				doneCodeInfo.setInfo(strInfo);
			}else{
				int mNum = (strInfo.length()%3000);
				int strNum = (strInfo.length()/3000);
				if(mNum>0){
					strNum++;
				}
				if(strNum>11){
					throw new Exception("所选用户数太多，无法生成业务单，请重新选择用户进行业务操作！");
				}
				for(int i=1;i<=strNum;i++){
					if(i==strNum){
						if(i==1)
							doneCodeInfo.setInfo(strInfo);
						if(i==2)
							doneCodeInfo.setInfo1(strInfo.substring((i-1)*3000));
						if(i==3)
							doneCodeInfo.setInfo2(strInfo.substring((i-1)*3000));
						if(i==4)
							doneCodeInfo.setInfo3(strInfo.substring((i-1)*3000));
						if(i==5)
							doneCodeInfo.setInfo4(strInfo.substring((i-1)*3000));
						if(i==6)
							doneCodeInfo.setInfo5(strInfo.substring((i-1)*3000));
						if(i==6)
							doneCodeInfo.setInfo6(strInfo.substring((i-1)*3000));
						if(i==8)
							doneCodeInfo.setInfo7(strInfo.substring((i-1)*3000));
						if(i==9)
							doneCodeInfo.setInfo8(strInfo.substring((i-1)*3000));
						if(i==10)
							doneCodeInfo.setInfo9(strInfo.substring((i-1)*3000));
						if(i==11)
							doneCodeInfo.setInfo10(strInfo.substring((i-1)*3000));
					}else{
						if(i==1)
							doneCodeInfo.setInfo(strInfo.substring(0,i*3000));
						if(i==2)
							doneCodeInfo.setInfo1(strInfo.substring((i-1)*3000,i*3000));
						if(i==3)
							doneCodeInfo.setInfo2(strInfo.substring((i-1)*3000,i*3000));
						if(i==4)
							doneCodeInfo.setInfo3(strInfo.substring((i-1)*3000,i*3000));
						if(i==5)
							doneCodeInfo.setInfo4(strInfo.substring((i-1)*3000,i*3000));
						if(i==6)
							doneCodeInfo.setInfo5(strInfo.substring((i-1)*4000,i*4000));
						if(i==7)
							doneCodeInfo.setInfo6(strInfo.substring((i-1)*3000,i*3000));
						if(i==8)
							doneCodeInfo.setInfo7(strInfo.substring((i-1)*3000,i*3000));
						if(i==9)
							doneCodeInfo.setInfo8(strInfo.substring((i-1)*3000,i*3000));
						if(i==10)
							doneCodeInfo.setInfo9(strInfo.substring((i-1)*3000,i*3000));
						if(i==11)
							doneCodeInfo.setInfo10(strInfo.substring((i-1)*3000,i*3000));
					}
				}
			}
		}
		doneCodeInfo.setCust_id(custId);
		cDoneCodeInfoDao.save(doneCodeInfo);
	}
	
	/**
	 * 保存业务流水
	 * @param doneCode 流水号
	 * @param busiCode 业务编号
	 * @param custId   客户编号
	 * @param userIds  用户编号数组
	 * @throws Exception
	 */
	public void saveDoneCode(Integer doneCode,String busiCode,String remark,
			String deptId, String countyId, String areaId,String custId, List<String> userIds) throws Exception{
		if (StringHelper.isEmpty(busiCode))
			throw new ComponentException("业务代码为空");
		if (0 == doneCode)
			throw new ComponentException("业务流水为空");

		//保存流水
		CDoneCode cDoneCode = new CDoneCode();
		cDoneCode.setDone_code(doneCode);
		cDoneCode.setBusi_code(busiCode);
		cDoneCode.setStatus(StatusConstants.ACTIVE);
		cDoneCode.setRemark(remark);
		cDoneCode.setCounty_id(countyId);
		cDoneCode.setArea_id(areaId);
		cDoneCode.setDept_id(deptId);
		cDoneCode.setOptr_id(getOptr().getOptr_id());
		cDoneCodeDao.save(cDoneCode);
		
		//保存流水明细
		if (StringHelper.isNotEmpty(custId)){
			if (userIds != null && userIds.size() > 0) {
				CDoneCodeDetail[] detail = new CDoneCodeDetail[userIds.size()];
				for( int i=0;i<userIds.size();i++){
					detail[i] = new CDoneCodeDetail();
					detail[i].setDone_code(doneCode);
					detail[i].setCust_id(custId);
					detail[i].setUser_id(userIds.get(i).toString());
					detail[i].setArea_id(areaId);
					detail[i].setCounty_id(countyId);
				}
				cDoneCodeDetailDao.save(detail);
			} else {
				CDoneCodeDetail detail = new CDoneCodeDetail();
				detail.setDone_code(doneCode);
				detail.setCust_id(custId);
				detail.setArea_id(areaId);
				detail.setCounty_id(countyId);
				cDoneCodeDetailDao.save(detail);
			}
		}
	}
	
	
	/**
	 * 保存业务流水
	 * @param doneCode 流水号
	 * @param busiCode 业务编号
	 * @param custId   客户编号
	 * @param userIds  用户编号数组
	 * @throws Exception
	 */
	public void saveDoneCode(Integer doneCode,String busiCode,String remark
			,String custId, List<String> userIds,String addr_id,String service_channel) throws Exception{
		if (StringHelper.isEmpty(busiCode))
			throw new ComponentException("业务代码为空");
		if (0 == doneCode)
			throw new ComponentException("业务流水为空");
//		if (StringHelper.isEmpty(custId))
//			throw new ComponentException("客户id为空");

		//保存流水
		CDoneCode cDoneCode = new CDoneCode();
		cDoneCode.setDone_code(doneCode);
		cDoneCode.setBusi_code(busiCode);
		cDoneCode.setStatus(StatusConstants.ACTIVE);
		cDoneCode.setRemark(remark);
		cDoneCode.setAddr_id(addr_id);
		cDoneCode.setService_channel(service_channel);
		setBaseInfo(cDoneCode);
		cDoneCodeDao.save(cDoneCode);

		//保存流水明细
		if (StringHelper.isNotEmpty(custId)){
			if (userIds != null && userIds.size() > 0) {
				CDoneCodeDetail[] detail = new CDoneCodeDetail[userIds.size()];
				for( int i=0;i<userIds.size();i++){
					detail[i] = new CDoneCodeDetail();
					detail[i].setDone_code(doneCode);
					detail[i].setCust_id(custId);
					if(StringHelper.isNotEmpty(userIds.get(i))){//lxr临时添加
						detail[i].setUser_id(userIds.get(i).toString());
					}
					detail[i].setArea_id(getOptr().getArea_id());
					detail[i].setCounty_id(getOptr().getCounty_id());
				}
				cDoneCodeDetailDao.save(detail);
			} else {
				CDoneCodeDetail detail = new CDoneCodeDetail();
				detail.setDone_code(doneCode);
				detail.setCust_id(custId);
				detail.setArea_id(getOptr().getArea_id());
				detail.setCounty_id(getOptr().getCounty_id());
				cDoneCodeDetailDao.save(detail);
			}
		}
	}

	public void saveBatchDoneCode(Integer doneCode,String busiCode,String remark
			,List<String> custIds, List<String> userIds) throws Exception{
		if (StringHelper.isEmpty(busiCode))
			throw new ComponentException("业务代码为空");
		if (0 == doneCode)
			throw new ComponentException("业务流水为空");
//		if (StringHelper.isEmpty(custId))
//			throw new ComponentException("客户id为空");

		//保存流水
		CDoneCode cDoneCode = new CDoneCode();
		cDoneCode.setDone_code(doneCode);
		cDoneCode.setBusi_code(busiCode);
		cDoneCode.setStatus(StatusConstants.ACTIVE);
		cDoneCode.setRemark(remark);
		setBaseInfo(cDoneCode);
		cDoneCodeDao.save(cDoneCode);
		
		//保存流水明细
		if (custIds != null && custIds.size() > 0){
			CDoneCodeDetail[] detail = new CDoneCodeDetail[custIds.size()];
			if(userIds != null && userIds.size() > 0){
				for( int i=0;i<custIds.size();i++){
					detail[i] = new CDoneCodeDetail();
					detail[i].setDone_code(doneCode);
					detail[i].setCust_id(custIds.get(i));
					detail[i].setUser_id(userIds.get(i));
					detail[i].setArea_id(getOptr().getArea_id());
					detail[i].setCounty_id(getOptr().getCounty_id());
				}
			}else{
				for( int i=0;i<custIds.size();i++){
					detail[i] = new CDoneCodeDetail();
					detail[i].setDone_code(doneCode);
					detail[i].setCust_id(custIds.get(i));
					detail[i].setArea_id(getOptr().getArea_id());
					detail[i].setCounty_id(getOptr().getCounty_id());
				}
			}
			cDoneCodeDetailDao.save(detail);
		}
	}
	
	public void editRemark(int doneCode,String remark) throws Exception {
		cDoneCodeDao.updateRemark(doneCode, remark);
	}
	
	public void cancelDoneCode(Integer doneCode) throws Exception{
		cDoneCodeDao.delete(doneCode);
	}
	
	/**
	 * @param doneCodeInfoDao the cDoneCodeInfoDao to set
	 */
	public void setCDoneCodeInfoDao(CDoneCodeInfoDao doneCodeInfoDao) {
		cDoneCodeInfoDao = doneCodeInfoDao;
	}

	public void setTBusiConfirmDao(TBusiConfirmDao tBusiConfirmDao) {
		this.tBusiConfirmDao = tBusiConfirmDao;
	}

	public void setCDoneCodeUnpayDao(CDoneCodeUnpayDao cDoneCodeUnpayDao) {
		this.cDoneCodeUnpayDao = cDoneCodeUnpayDao;
	}
	
}
