/**
 *
 */
package com.ycsoft.business.component.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.job.JBandCommand;
import com.ycsoft.beans.core.job.JBusiCmd;
import com.ycsoft.beans.core.job.JCaCommand;
import com.ycsoft.beans.core.job.JCustAcctmodeCal;
import com.ycsoft.beans.core.job.JCustCreateBill;
import com.ycsoft.beans.core.job.JCustCreditCal;
import com.ycsoft.beans.core.job.JCustCreditExec;
import com.ycsoft.beans.core.job.JCustInvalidCal;
import com.ycsoft.beans.core.job.JCustWriteoff;
import com.ycsoft.beans.core.job.JCustWriteoffAcct;
import com.ycsoft.beans.core.job.JProdNextTariff;
import com.ycsoft.beans.core.job.JProdNextTariffHis;
import com.ycsoft.beans.core.job.JProdPreopen;
import com.ycsoft.beans.core.job.JUserStop;
import com.ycsoft.beans.core.job.JVodCommand;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.core.job.JBandCommandDao;
import com.ycsoft.business.dao.core.job.JBusiCmdDao;
import com.ycsoft.business.dao.core.job.JCaCommandDao;
import com.ycsoft.business.dao.core.job.JCustAcctmodeCalDao;
import com.ycsoft.business.dao.core.job.JCustCreateBillDao;
import com.ycsoft.business.dao.core.job.JCustCreditCalDao;
import com.ycsoft.business.dao.core.job.JCustCreditExecDao;
import com.ycsoft.business.dao.core.job.JCustInvalidCalDao;
import com.ycsoft.business.dao.core.job.JCustWriteoffAcctDao;
import com.ycsoft.business.dao.core.job.JCustWriteoffDao;
import com.ycsoft.business.dao.core.job.JProdNextTariffDao;
import com.ycsoft.business.dao.core.job.JProdNextTariffHisDao;
import com.ycsoft.business.dao.core.job.JProdPreopenDao;
import com.ycsoft.business.dao.core.job.JUserStopDao;
import com.ycsoft.business.dao.core.job.JVodCommandDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.JBandCommandDto;
import com.ycsoft.business.dto.core.prod.JCaCommandDto;
import com.ycsoft.business.dto.core.prod.JVodCommandDto;
import com.ycsoft.business.dto.core.user.UserRes;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

/**
 * @author YC-SOFT
 * 处理异步任务
 */
@Component
public class JobComponent  extends BaseBusiComponent {

	private JBusiCmdDao jBusiCmdDao;
	private JCustCreateBillDao jCustCreateBillDao;
	private JCustCreditCalDao jCustCreditCalDao;
	private JCustCreditExecDao jCustCreditExecDao;
	private JCustInvalidCalDao jCustInvalidCalDao;
	private JCustWriteoffDao jCustWriteoffDao;
	private JCustWriteoffAcctDao jCustWriteoffAcctDao;
	private JProdNextTariffDao jProdNextTariffDao;
	private JProdNextTariffHisDao jProdNextTariffHisDao;
	private JUserStopDao jUserStopDao;
	private JCaCommandDao jCaCommandDao;
	private JVodCommandDao jVodCommandDao;
	private JBandCommandDao jBandCommandDao;
	private JProdPreopenDao jProdPreopenDao;
	private JCustAcctmodeCalDao jCustAcctmodeCalDao;
	@Autowired
	private CProdOrderDao cProdOrderDao;
	
	/**
	 * 创建计算信用度任务
	 * @param doneCode  业务流水号
	 * @param custId    客户编号
	 * @param acctId    账户编号
	 * @param acctItemId  账目编号
	 *
	 * 账户编号和账目编号可以为空，如果为空，则对客户名下的所有账目计算信用度
	 * 在客户资料修改和用户资料修改业务中调用，不需要指定账目编号
	 * 如果在产品订购业务中调用，需要指定账目编号
	 */
	public void createCreditCalJob(Integer doneCode,String custId,List<CAcctAcctitem> acctItemList,String creditExec) throws Exception{
		List <JCustCreditCal> jobList = new ArrayList<JCustCreditCal>();
		if (acctItemList != null && acctItemList.size()>0){
			for (CAcctAcctitem acctItem :acctItemList){
				JCustCreditCal creditCal = new JCustCreditCal();
				creditCal.setJob_id(getJobId());
				creditCal.setDone_code(doneCode);
				creditCal.setCust_id(custId);
				creditCal.setAcct_id(acctItem.getAcct_id());
				creditCal.setAcctitem_id(acctItem.getAcctitem_id());
				creditCal.setArea_id(getOptr().getArea_id());
				creditCal.setCounty_id(getOptr().getCounty_id());
				creditCal.setCredit_exec(creditExec);
				jobList.add(creditCal);
			}
		} else {
			JCustCreditCal creditCal = new JCustCreditCal();
			creditCal.setJob_id(getJobId());
			creditCal.setDone_code(doneCode);
			creditCal.setCust_id(custId);
			creditCal.setArea_id(getOptr().getArea_id());
			creditCal.setCounty_id(getOptr().getCounty_id());
			creditCal.setCredit_exec(creditExec);
			jobList.add(creditCal);
		}

		jCustCreditCalDao.save(jobList.toArray(new JCustCreditCal[jobList.size()]));
	}
	
	public void createCreditExecJob(Integer doneCode,String custId) throws Exception{
		JCustCreditExec creditExec = new JCustCreditExec();
		creditExec.setJob_id(getJobId());
		creditExec.setCust_id(custId);
		creditExec.setDone_code(doneCode);
		creditExec.setBusi("T");
		creditExec.setArea_id(getOptr().getArea_id());
		creditExec.setCounty_id(getOptr().getCounty_id());
		this.jCustCreditExecDao.save(creditExec);
	}
	
	/**
	 * 客户到期日计算任务
	 * @param doneCode
	 * @param custId
	 * @throws Exception
	 */
	public void createInvalidCalJob(Integer doneCode,String custId) throws Exception{
		//溧阳没有客户到期日计算
	}
	
	public void createInvalidCalJobByCustIds(Integer doneCode,String[] custIds) throws Exception{
		 List<JCustInvalidCal> jList = new ArrayList<JCustInvalidCal>();
		 for(int i=0;i<custIds.length;i++){ 
			JCustInvalidCal invalidCal = new JCustInvalidCal();
			invalidCal.setJob_id(getJobId());
			invalidCal.setCust_id(custIds[i]);
			invalidCal.setDone_code(doneCode);
			invalidCal.setArea_id(getOptr().getArea_id());
			invalidCal.setCounty_id(getOptr().getCounty_id());
			jList.add(invalidCal);
		 }
		jCustInvalidCalDao.save(jList.toArray(new JCustInvalidCal[jList.size()]));
	}

	/**
	 * 创建修改资费任务
	 * @param doneCode       业务流水号
	 * @param prodSn		 用户产品编号
	 * @param tariffId       新资费id
	 * @param effDate        生效日期
	 * @param delFlag        是否删除前面的未生效资费
	 * @throws Exception
	 *
	 * 将资费修改任务保存到j_prod_next_tariff中
	 */
	public void createNewProdTariffJob(Integer doneCode,String prodSn,String tariffId,String oldTariffId,String effDate,boolean delFlag)
		throws Exception{
		//删除该产品已经存在的未生效资费
		if (delFlag)
			deleteNewProdTariffJob(prodSn);
		//增加新的未生效资费
		JProdNextTariff tariffJob = new JProdNextTariff();
		tariffJob.setJob_id(getJobId());
		tariffJob.setDone_code(doneCode);
		tariffJob.setProd_sn(prodSn);
		tariffJob.setTariff_id(tariffId);
		tariffJob.setOld_tariff_id(oldTariffId);
		tariffJob.setEff_date(DateHelper.strToDate(effDate));
		tariffJob.setArea_id(getOptr().getArea_id());
		tariffJob.setCounty_id(getOptr().getCounty_id());
		jProdNextTariffDao.save(tariffJob);
	}
	//删除该产品已经存在的未生效资费
	public void deleteNewProdTariffJob(String prodSn) throws Exception{
		jProdNextTariffDao.removeByProdSn(prodSn,getOptr().getCounty_id());
	}

	/**
	 * 创建预报停任务
	 * @param doneCode      业务流水号
	 * @param userId		用户编号
	 * @param stopDate		停机日期
	 * @throws Exception
	 *
	 * 将报停任务保存到j_user_stop表中
	 */
	public void createUserStopJob(Integer doneCode,String userId,String stopDate) throws Exception{
		JUserStop stopJob = new JUserStop();
		stopJob.setJob_id(getJobId());
		stopJob.setDone_code(doneCode);
		stopJob.setUser_id(userId);
		stopJob.setArea_id(getOptr().getArea_id());
		stopJob.setCounty_id(getOptr().getCounty_id());
		stopJob.setStop_date(DateHelper.strToDate(stopDate));

		jUserStopDao.save(stopJob);
	}

	/**
	 * 删除用户所有的预报停
	 * @param userId
	 * @throws Exception
	 */
	public void removeByUserId(String userId) throws Exception{
		jUserStopDao.removeByUserId(userId);
	}
	
	/**
	 * 查询用户所有的预报停
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<JUserStop> queryStopByUserId(String userId)throws Exception{
		return jUserStopDao.queryStopByUserId(userId);
	}
	
	public void cancelStopUser(String[] userall)throws Exception{
		 jUserStopDao.cancelStopUser(userall);
	}
	
	/**
	 * 创建出帐job
	 * @param doneCode      业务流水号
	 * @param custId		客户id
	 * @param acctId		账户id
	 * @param acctItemId	账目id（同产品编号相同)
	 * @throws Exception
	 *
	 * 将出帐任务保存到 J_CUST_CREATE_BILL
	 */
	public void createCustBillJob(Integer doneCode,String custId,String acctId,String acctItemId)throws Exception{
		JCustCreateBill billJob = new JCustCreateBill();
		billJob.setJob_id(getJobId());
		billJob.setDone_code(doneCode);
		billJob.setCust_id(custId);
		billJob.setAcct_id(acctId);
		billJob.setAcctitem_id(acctItemId);
		billJob.setArea_id(getOptr().getArea_id());
		billJob.setCounty_id(getOptr().getCounty_id());

		jCustCreateBillDao.save(billJob);
	}

	/**
	 * 创建销帐job
	 * @param doneCode      业务流水号
	 * @param custId		客户id
	 * @throws Exception
	 *
	 * 将销帐任务保存到 J_CUST_WRITE_OFF
	 */
	public int createCustWriteOffJob(Integer doneCode,String custId,String writeOff)throws Exception{
		JCustWriteoff writeOffJob = new JCustWriteoff();
		writeOffJob.setJob_id(getJobId());
		writeOffJob.setDone_code(doneCode);
		writeOffJob.setCust_id(custId);
		writeOffJob.setArea_id(getOptr().getArea_id());
		writeOffJob.setCounty_id(getOptr().getCounty_id());
		writeOffJob.setWriteoff(writeOff);

		jCustWriteoffDao.save(writeOffJob);
		return writeOffJob.getJob_id();
	}
	/**
	 * 创建账务模式判断Job
	 * @param doneCode
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public int createAcctModeCalJob(Integer doneCode,String custId)throws Exception{
//		溧阳不要账目计算
		return 0;
	}
	
	public void createAcctModeCalJobByCustIds(Integer doneCode,String[] custIds) throws Exception{
		 List<JCustAcctmodeCal> jList = new ArrayList<JCustAcctmodeCal>();
		 for(int i=0;i<custIds.length;i++){ 
			 JCustAcctmodeCal invalidCal = new JCustAcctmodeCal();
			invalidCal.setJob_id(getJobId());
			invalidCal.setCust_id(custIds[i]);
			invalidCal.setDone_code(doneCode);
			invalidCal.setArea_id(getOptr().getArea_id());
			invalidCal.setCounty_id(getOptr().getCounty_id());
			jList.add(invalidCal);
		 }
		 jCustAcctmodeCalDao.save(jList.toArray(new JCustAcctmodeCal[jList.size()]));
	}
	
	/**
	 * 增加需要终止的账户或者账目
	 * @param jobId	销帐任务id
	 * @param acctId
	 * @param acctItemId
	 */
	public void terminateAcct(int jobId,String acctId,String acctItemId,Integer doneCode) throws Exception{
		//增加需要终止的账户或者账目任务
		JCustWriteoffAcct jcw = new JCustWriteoffAcct();
		jcw.setJob_id(jobId);
		jcw.setAcct_id(acctId);
		jcw.setAcctitem_id(acctItemId);
		jcw.setDone_code(doneCode);
		jCustWriteoffAcctDao.save(jcw);
	}
	
	/**
	 * @param userId
	 * @param creatUser
	 */
	public void deleteUserBand(String userId, String cmdType) throws JDBCException {
		jBandCommandDao.deleteUserBand(userId,cmdType);
	}
	


	/**
	 * 创建业务指令任务
	 * @param doneCode     业务流水号
	 * @param busiCmdType  指令类型
	 * @param custId	   客户id
	 * @param userId	   用户id
	 * @param stbId        机顶盒编号
	 * @param cardId       卡号
	 * @param modemMac     modem mac
	 * @param prodSn       用户产品（套餐）
	 * @throws Exception
	 *
	 * 如果是设备类指令，则需要指定设备的编号
	 * 如果是产品类指令，则需要指定产品sn
	 */
	public int createBusiCmdJob(Integer doneCode,String busiCmdType,String custId,String userId,
			String stbId,String cardId,String modemMac,String prodSn,String prodId)throws Exception{
//		if(BusiCmdConstants.ACCTIVATE_PROD.equals(busiCmdType) && StringHelper.isEmpty(cardId)){
//			return 0;
//		}
		return this.createBusiCmdJob(doneCode, busiCmdType, custId, userId, stbId, cardId, modemMac, prodSn, prodId, null);
	}
	
	public int createBusiCmdJob(Integer doneCode,String busiCmdType,String custId,String userId,
			String stbId,String cardId,String modemMac,String prodSn,String prodId,String detailParams)throws Exception{
		return this.createBusiCmdJob(doneCode, busiCmdType, custId, userId,
				stbId, cardId, modemMac, prodSn, prodId, detailParams,
				SystemConstants.PRIORITY_SSSQ);
	}
	
	/**
	 * 创建产品的授权任务
	 * @param cProdOrder
	 * @param prod_type
	 * @param userMap
	 * @param done_code
	 * @param busi_cmd_type
	 * @throws Exception
	 */
	public void createProdBusiCmdJob(CProdOrder cProdOrder,String prod_type,Map<String,CUser> userMap,Integer done_code,String busi_cmd_type,int priority) throws Exception{
		
		if(prod_type.equals(SystemConstants.PROD_TYPE_BASE)){
			//单产品授权
			CUser user=userMap.get(cProdOrder.getUser_id());
			this.createBusiCmdJob(done_code,busi_cmd_type, cProdOrder.getCust_id(), cProdOrder.getUser_id()
					, user.getStb_id(), user.getCard_id(), user.getModem_mac(),
					cProdOrder.getOrder_sn(), cProdOrder.getProd_id(), null, priority);
		}else{
			//套餐的授权
			//Map<String,CUser> userMap=CollectionHelper.converToMapSingle(cUserDao.queryUserByCustId(cProdOrder.getCust_id()), "user_id");
			for(CProdOrder order:cProdOrderDao.queryPakDetailOrder(cProdOrder.getOrder_sn())){
				CUser user=userMap.get(order.getUser_id());
				this.createBusiCmdJob(done_code,busi_cmd_type, order.getCust_id(), order.getUser_id()
						, user.getStb_id(), user.getCard_id(), user.getModem_mac(),
						order.getOrder_sn(), order.getProd_id(), null, priority);
			}
		}
	}
	
	public int createBusiCmdJob(Integer doneCode, String busiCmdType,
			String custId, String userId, String stbId, String cardId,
			String modemMac, String prodSn, String prodId, String detailParams,
			int priority) throws Exception {
		JBusiCmd busiCmdJob = new JBusiCmd();
		busiCmdJob.setJob_id(getJobId());
		busiCmdJob.setDone_code(doneCode);
		busiCmdJob.setBusi_cmd_type(busiCmdType);
		busiCmdJob.setCust_id(custId);
		busiCmdJob.setUser_id(userId);
		busiCmdJob.setStb_id(stbId);
		busiCmdJob.setCard_id(cardId);
		busiCmdJob.setModem_mac(modemMac);
		busiCmdJob.setProd_sn(prodSn);
		busiCmdJob.setArea_id(getOptr().getArea_id());
		busiCmdJob.setCounty_id(getOptr().getCounty_id());
		busiCmdJob.setPriority(priority);
		if (StringHelper.isNotEmpty(detailParams))
			busiCmdJob.setDetail_params(detailParams);
		if (StringHelper.isNotEmpty(prodId)){
			//是产品指令
			PProd prod = pProdDao.findByKey(prodId);
			if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
				busiCmdJob.setProd_id(prodId);
				jBusiCmdDao.save(busiCmdJob);
			} else {
				//是套餐
				PPackageProd pkgProd = new PPackageProd();
				pkgProd.setPackage_id(prod.getProd_id());
//				List<PPackageProd> pkgProdList= pPackageProdDao.findByEntity(pkgProd);
//				for (PPackageProd pp:pkgProdList){
//					busiCmdJob.setProd_id(pp.getProd_id());
//					jBusiCmdDao.save(busiCmdJob);
//				}
//				busiCmdJob.setProd_id(prodId);
//				jBusiCmdDao.save(busiCmdJob);
				//在c_prod 表中去套餐子产品,因为要对子产品的动态资源发送授权，动态资源是根据子产品的prod_sn 取的。
				List<CProdDto> cprodDtoList = cProdDao.queryChildProdByPkgsn(prodSn, getOptr().getCounty_id());
 				if(null!=cprodDtoList){
					for (CProdDto pp: cprodDtoList){
						busiCmdJob.setProd_id(pp.getProd_id());
						busiCmdJob.setProd_sn(pp.getProd_sn());
						jBusiCmdDao.save(busiCmdJob);
					}
				}
			}
		} else {
			jBusiCmdDao.save(busiCmdJob);
		}
		
		if (busiCmdJob.getBusi_cmd_type().equals(BusiCmdConstants.CREAT_USER)){
			createRecordChange( "C_USER", "INS", busiCmdJob.getUser_id());
		} else if (busiCmdJob.getBusi_cmd_type().equals(BusiCmdConstants.DEL_USER)){
			createRecordChange( "C_USER", "DEL", busiCmdJob.getUser_id());
		} else if (busiCmdJob.getBusi_cmd_type().equals(BusiCmdConstants.CHANGE_USER)){
			createRecordChange( "C_USER", "MOD", busiCmdJob.getUser_id());
		} 
		
			
		return busiCmdJob.getJob_id();
	}
	
	/**
	 * 一体机预授权
	 * @param doneCode
	 * @param cardId
	 * @param optr
	 * @throws Exception
	 */
	public void createBusiCmdCard(Integer doneCode,String cardId,SOptr optr) throws Exception {
		JBusiCmd cmd = new JBusiCmd();
		cmd.setJob_id(getJobId());
		cmd.setDone_code(doneCode);
		cmd.setStb_id("111111111111111111");
		cmd.setCard_id(cardId);
		cmd.setOptr_id(optr.getOptr_id());
		cmd.setDept_id(optr.getDept_id());
		cmd.setCounty_id(optr.getCounty_id());
		cmd.setArea_id(optr.getArea_id());
		cmd.setBusi_cmd_type(BusiCmdConstants.ACCTIVATE_TERMINAL);
		cmd.setCreate_time(DateHelper.now());

		jBusiCmdDao.save(cmd);

		cmd.setJob_id(getJobId());
		cmd.setBusi_cmd_type(BusiCmdConstants.CARD_FILLED);
		cmd.setCreate_time(DateHelper.now());

		jBusiCmdDao.save(cmd);
	}
	
	public Pager<JCaCommand> queryCaCommandByCardId(String[] cardIds,
			Integer start, Integer limit) throws JDBCException {
		return jCaCommandDao.queryCaByCardId(cardIds,start,limit);
	}
	
	
	public List<JBusiCmd> queryNewProdCmdByDoneCode(String doneCode) throws Exception{
		List<JBusiCmd> jobList = jBusiCmdDao.queryNewProdByDoneCode(doneCode,getOptr().getCounty_id());
		return jobList;
	}

	public JProdNextTariff queryTariffJob(Integer doneCode) throws Exception{
		return jProdNextTariffDao.queryByDoneCode(doneCode,getOptr().getCounty_id());
	}
	
	public JProdNextTariff queryNextTariffJob(String prodSn,String tariffId,String countyId) throws Exception{
		return jProdNextTariffDao.queryByProdSn(prodSn, tariffId, countyId);
	}
	/**
	 * 取相同donecode,prodsn的资费变更任务的最后一个变更任务
	 * @param doneCode
	 * @param prodSn
	 * @return
	 * @throws Exception
	 */
	public JProdNextTariff queryLastTariffJobDoneCode(Integer doneCode,String prodSn) throws Exception{
		return jProdNextTariffDao.queryByDoneCodeProdSn(doneCode,prodSn,getOptr().getCounty_id());
	}

	/**
	 * 判断一个业务流水是否是预报停
	 * @param doneCode
	 */
	public boolean isPreStop(Integer doneCode) throws Exception{
		// TODO 根据业务流水查找预报停记录
		List<JUserStop> stopList = jUserStopDao.queryByDoneCode(doneCode);
		if (stopList != null && stopList.size()>0)
			return true;
		else
			return false;
	}
	
	/**
	 * 查询用户有效资源
	 * @param userIds
	 * @return
	 * @throws Exception
	 */
	public List<UserRes> queryValidRes(String[] userIds)throws Exception{
		return jBusiCmdDao.queryValidRes(userIds);
	}

	public Pager<JCaCommand> queryCaCommand(String type,String custId,Integer start,Integer limit) throws Exception {
		return jCaCommandDao.queryByCustId(type,custId,start,limit);
	}
	
	/**
	 * @param cardId
	 * @param start
	 * @param limit
	 * @return
	 */
	public Pager<JCaCommandDto> queryCaCommandByCardId(String cardId,
			Integer start, Integer limit) throws JDBCException {
		return jCaCommandDao.queryByCardId(cardId,start,limit);
	}
	
	/**
	 * @param create_done_code
	 */
	public JCustWriteoff queryWriteOff(Integer doneCode) throws JDBCException {
		// TODO Auto-generated method stub
		return jCustWriteoffDao.queryByDoneCode(doneCode);
	}
	
	

	/**
	 * @param custId
	 * @return
	 */
	public Pager<JVodCommand> queryVodCommand(String type,String custId,Integer start,Integer limit) throws JDBCException {
		return jVodCommandDao.queryByCustId(type,custId,start,limit);
	}
	/**
	 * @param custId
	 * @return
	 */
	public Pager<JVodCommandDto> queryVodCommandByCardId(String cardId,Integer start,Integer limit) throws JDBCException {
		return jVodCommandDao.queryByCardId(cardId,start,limit);
	}

	public Pager<JBandCommand> queryBandCommand(String custId,Integer start,Integer limit) throws JDBCException {
		return jBandCommandDao.queryByCustId(custId,start,limit);
	}
	
	public Pager<JBandCommand> queryBandCommandByParam(String custId,Integer start,Integer limit) throws JDBCException {
		return jBandCommandDao.queryByCustId(custId,start,limit);
	}
	public Pager<JBandCommandDto> queryBandCommandByParam(Map<String,String> param,Integer start,Integer limit) throws JDBCException {
		return jBandCommandDao.queryBandCommandByParam(param,start,limit);
	}
	
	public void removePreStopByDoneCode(Integer doneCode) throws Exception {
		jUserStopDao.removeByDoneCode(doneCode);
	}

	/**
	 * @param doneCode
	 */
	public void removeTariffJobByDoneCode(Integer doneCode) throws Exception {
		jProdNextTariffDao.removeByDoneCode(doneCode);

	}
	
	public void saveTariffJobHis(JProdNextTariffHis tariffJobHis) throws Exception {
		jProdNextTariffHisDao.save(tariffJobHis);
	}


	private int getJobId() throws Exception{
		return Integer.parseInt(jBusiCmdDao.findSequence(SequenceConstants.SEQ_JOB_ID).toString());
	}
	
	public void saveCancelCaAuth(CUser cuser,Integer doneCode) throws Exception{
		jCaCommandDao.saveCancelCaAuth(cuser,doneCode);
	}
	/**
	 * 插入到期日期计算任务
	 * @param doneCode
	 * @param custId
	 * @throws Exception
	 */
	public void saveCustInvalidCal(Integer doneCode,String custId)throws Exception{
		JCustInvalidCal custInvalidCal = new JCustInvalidCal();
		custInvalidCal.setJob_id(getJobId());
		custInvalidCal.setDone_code(doneCode);
		custInvalidCal.setCust_id(custId);
		custInvalidCal.setArea_id(getOptr().getArea_id());
		custInvalidCal.setCounty_id(getOptr().getCounty_id());
		custInvalidCal.setCreate_time(DateHelper.now());
		jCustInvalidCalDao.save(custInvalidCal);
	}
	
	public String syncServerTime() throws JDBCException, Exception{
		return cDoneCodeDao.queryDataBaseTime();
	}
	public void setJBusiCmdDao(JBusiCmdDao busiCmdDao) {
		jBusiCmdDao = busiCmdDao;
	}


	public void setJCustCreateBillDao(JCustCreateBillDao custCreateBillDao) {
		jCustCreateBillDao = custCreateBillDao;
	}

	public void setJCustCreditCalDao(JCustCreditCalDao custCreditCalDao) {
		jCustCreditCalDao = custCreditCalDao;
	}

	/**
	 * @param custWriteoffDao the jCustWriteoffDao to set
	 */
	public void setJCustWriteoffDao(JCustWriteoffDao custWriteoffDao) {
		jCustWriteoffDao = custWriteoffDao;
	}
	
	/**
	 * @param jCustInvalidCalDao the jCustInvalidCalDao to set
	 */
	public void setJCustInvalidCalDao(JCustInvalidCalDao custInvalidCalDao) {
		this.jCustInvalidCalDao = custInvalidCalDao;
	}

	

	public void setJProdNextTariffDao(JProdNextTariffDao prodNextTariffDao) {
		jProdNextTariffDao = prodNextTariffDao;
	}

	public void setJUserStopDao(JUserStopDao userStopDao) {
		jUserStopDao = userStopDao;
	}

	/**
	 * @param caCommandDao the jCaCommandDao to set
	 */
	public void setJCaCommandDao(JCaCommandDao caCommandDao) {
		jCaCommandDao = caCommandDao;
	}

	public void setJCustWriteoffAcctDao(JCustWriteoffAcctDao custWriteoffAcctDao) {
		jCustWriteoffAcctDao = custWriteoffAcctDao;
	}

	/**
	 * @param vodCommandDao the jVodCommandDao to set
	 */
	public void setJVodCommandDao(JVodCommandDao vodCommandDao) {
		jVodCommandDao = vodCommandDao;
	}

	public void setJBandCommandDao(JBandCommandDao bandCommandDao) {
		jBandCommandDao = bandCommandDao;
	}
	
	public JCustCreditExecDao getJCustCreditExecDao() {
		return jCustCreditExecDao;
	}

	public void setJCustCreditExecDao(JCustCreditExecDao custCreditExecDao) {
		jCustCreditExecDao = custCreditExecDao;
	}

	public void setJProdNextTariffHisDao(JProdNextTariffHisDao prodNextTariffHisDao) {
		jProdNextTariffHisDao = prodNextTariffHisDao;
	}
	
	/**
	 * 创建产品预开通job.
	 * @param doneCode 流水.
	 * @param sn	产品SN.
	 * @param preOpenTime	预开通时间.
	 */
	public void createPreAuthCmdJob(Integer doneCode, String sn,Date preOpenTime, String areaId, String countyId) throws ComponentException {
		try{
			String jobId = ""+getJobId();
			JProdPreopen preOpen = new JProdPreopen(jobId, ""+doneCode, sn, preOpenTime,areaId,countyId);
			jProdPreopenDao.save(preOpen);
		}catch (Exception e) {
			throw new ComponentException(e);
		}
	}

	public void setJProdPreopenDao(JProdPreopenDao jProdPreopenDao) {
		this.jProdPreopenDao = jProdPreopenDao;
	}

	public void setJCustAcctmodeCalDao(JCustAcctmodeCalDao custAcctmodeCalDao) {
		jCustAcctmodeCalDao = custAcctmodeCalDao;
	}

}
