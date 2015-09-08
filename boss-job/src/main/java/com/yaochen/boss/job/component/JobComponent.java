package com.yaochen.boss.job.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yaochen.boss.dao.CfgDao;
import com.yaochen.boss.dao.JobDao;
import com.yaochen.boss.model.CfgData;
import com.ycsoft.beans.config.TBusiCmdSupplier;
import com.ycsoft.beans.config.TServer;
import com.ycsoft.beans.config.TServerRes;
import com.ycsoft.beans.core.acct.CAcctAcctitemChange;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactiveHis;
import com.ycsoft.beans.core.acct.CAcctPreFee;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.common.CDoneCodeDetail;
import com.ycsoft.beans.core.job.JCustAcctmodeCal;
import com.ycsoft.beans.core.job.JCustInvalidCal;
import com.ycsoft.beans.core.job.JCustWriteoffAcct;
import com.ycsoft.beans.core.job.JExecResult;
import com.ycsoft.beans.core.job.JProdNextTariff;
import com.ycsoft.beans.core.job.JProdNextTariffHis;
import com.ycsoft.beans.core.job.JProdPreopen;
import com.ycsoft.beans.core.job.JUserStop;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.device.RCardModel;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdCountyRes;
import com.ycsoft.beans.system.SArea;
import com.ycsoft.business.dao.config.TAcctitemToProdDao;
import com.ycsoft.business.dao.config.TRuleDefineDao;
import com.ycsoft.business.dao.config.TTemplateDao;
import com.ycsoft.business.dao.core.common.CDoneCodeDao;
import com.ycsoft.business.dao.core.job.JExecResultDao;
import com.ycsoft.business.dao.core.job.JProdNextTariffHisDao;
import com.ycsoft.business.dao.core.prod.CProdDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.task.WWorkDao;
import com.ycsoft.business.dto.config.TaskQueryWorkDto;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.PPromotionDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.store.AcctItemToProdConfig;
import com.ycsoft.commons.store.TemplateConfig;
import com.ycsoft.daos.core.DataHandler;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.prod.ProdDto;

@Component
public class JobComponent {
	private CfgDao cfgDao;
	private JExecResultDao jExecResultDao;
	private JProdNextTariffHisDao jProdNextTariffHisDao;
	private JobDao jobDao;
	private TRuleDefineDao tRuleDefineDao;
	private CProdDao cProdDao;
	private CDoneCodeDao cDoneCodeDao;
	private TAcctitemToProdDao tAcctitemToProdDao;
	private TTemplateDao tTemplateDao;
	private WWorkDao wWorkDao;
	@Autowired
	private CProdOrderDao cProdOrderDao;
	/**
	 * 查询需要修正带宽的宽带订单
	 * @return
	 * @throws Exception 
	 */
	public List<String> queryUserNeedChangeBandWidth() throws Exception{
		/**
		 select distinct  t.user_id
		from busi.c_prod_order t,busi.p_prod p
		 where  t.prod_id=p.prod_id and p.serv_id='BAND' 
		 and t.status in ('ACTIVE','INSTALL')
		AND t.EXP_DATE>=TRUNC(SYSDATE) AND t.check_time is null 
		and t.eff_date <=trunc(sysdate)+1 and t.is_pay='T'
		**/
		return jobDao.queryUserNeedChanageBandWidth();
	}
	
	/**
	 * @param workDao the wWorkDao to set
	 */
	public void setWWorkDao(WWorkDao workDao) {
		wWorkDao = workDao;
	}
	
	public List<TaskQueryWorkDto> queryUnSyncWork() throws JDBCException{
		return wWorkDao.queryUnSyncWork(100);
	}
	
	public int syncWork(String workid,String syncStatus) throws JDBCException{
		return wWorkDao.syncWork(workid,syncStatus);
	}

	public void setTTemplateDao(TTemplateDao templateDao) {
		tTemplateDao = templateDao;
	}

	public void loadBaseConfig()throws Exception{
		AcctItemToProdConfig.loadData(tAcctitemToProdDao);
		TemplateConfig.loadData(tTemplateDao);
	}
	
	/**
	 * 加载授权的基础数据
	 * @return
	 * @throws Exception
	 */
	public com.yaochen.boss.model.CfgData loadCfgData() throws Exception{
		
		CfgData cfg = new CfgData();
		/**
		 * 基础数据检查
		 * 1、促销配置中的产品和资费编号是否都存在
		 */
//		if (this.cfgDao.queryIllegalProd()>0)
//			throw new ComponentException("促销配置中有非法资费，请检查数据！");
		//加载产品数据
		Map<String,ProdDto> prodMap = new HashMap<String,ProdDto>();
		List<ProdDto> prodList = cfgDao.queryProd();
		for (ProdDto prod:prodList){
			prod.setResList(cfgDao.queryProdRes(prod.getProd_id()));
			
			List<PProdCountyRes> countyResList = cfgDao.queryProdResCounty(prod.getProd_id());
			Map<String,List<String>> countyResMap = new HashMap<String,List<String>>();
			for (PProdCountyRes countyRes:countyResList){
				if (countyResMap.get(countyRes.getCounty_id()) == null){
					List<String> resList = new ArrayList<String>();
					resList.add(countyRes.getRes_id());
					countyResMap.put(countyRes.getCounty_id(), resList);
				} else {
					countyResMap.get(countyRes.getCounty_id()).add(countyRes.getRes_id());
				}
			}
			prod.setCountyResMap(countyResMap);
			int dynamicResCount = cfgDao.queryProdDynamicResCount(prod.getProd_id());
			if (dynamicResCount>0){
				prod.setHasDynRes(true);
			} else {
				prod.setHasDynRes(false);
			}
			prodMap.put(prod.getProd_id(), prod);
		}
		cfg.setProdMap(prodMap);
		//加载促销数据
		cfg.setPromotionList(cfgDao.queryPromotion());
		for (PPromotionDto promotion:cfg.getPromotionList()){
			promotion.setAcctList(cfgDao.queryPromotionAcct(promotion.getPromotion_id()));
		}
		//加载卡型号和服务器供应商关系
		List<RCardModel> cardModelList = cfgDao.queryCardModel();
		cfg.setCardCaMap(new HashMap<String,RCardModel>());
		for (RCardModel cardModel:cardModelList){
			cfg.getCardCaMap().put(cardModel.getDevice_model(), cardModel);
		}
		//加载服务器配置信息
		List<TServer> serverList = cfgDao.queryServer();
		for(TServer server:serverList){
			server.setCountyList(cfgDao.queryServerCounty(server.getServer_id()));
		}
		cfg.setServerList(serverList);
		//加载BOSS资源和外部资源的对应关系
		List<TServerRes> resList = cfgDao.queryServerRes();
		Map<String,Map<String,TServerRes>> resMap = new HashMap<String,Map<String,TServerRes>>();
		for (TServerRes res :resList){
			if (resMap.get(res.getBoss_res_id()) == null){
				Map<String,TServerRes> servResMap = new HashMap<String,TServerRes>();
				servResMap.put(res.getServer_id(), res);
				resMap.put(res.getBoss_res_id(), servResMap);
			} else {
				resMap.get(res.getBoss_res_id()).put(res.getServer_id(), res);
			}
		}
		cfg.setResMap(resMap);
		//加载业务指令和服务器指令的对应关系
		List<TBusiCmdSupplier> cmdList = cfgDao.queryCmdSupplier();
		Map<String,Map<String,List<String>>> busiCmdMap = new HashMap<String,Map<String,List<String>>>();
		for (TBusiCmdSupplier cmd:cmdList){
			if (busiCmdMap.get(cmd.getCmd_id()) == null){
				Map<String,List<String>> servCmdMap = new HashMap<String,List<String>>();
				List<String> servCmdList = new ArrayList<String>();
				servCmdList.add(cmd.getSupplier_cmd_id());
				servCmdMap.put(cmd.getSupplier_id(), servCmdList);
				busiCmdMap.put(cmd.getCmd_id(), servCmdMap);
			} else {
				if (busiCmdMap.get(cmd.getCmd_id()).get(cmd.getSupplier_id()) == null){
					List<String> servCmdList = new ArrayList<String>();
					servCmdList.add(cmd.getSupplier_cmd_id());
					busiCmdMap.get(cmd.getCmd_id()).put(cmd.getSupplier_id(), servCmdList);
				} else {
					busiCmdMap.get(cmd.getCmd_id()).get(cmd.getSupplier_id()).add(cmd.getSupplier_cmd_id());
				}
			}
		}
		cfg.setBusiCmdMap(busiCmdMap);
		
		cfg.setRuleList(tRuleDefineDao.findAll());
		
		return cfg;
	}
	/**
	 * 保存任务执行结果
	 * @param jobId
	 * @param areaId
	 * @param countyId
	 * @param success
	 * @param errorInfo
	 */
	public void saveJobExecute(int jobId,String areaId,String countyId,String success,String errorInfo) throws Exception{
		JExecResult result = new JExecResult();
		result.setJob_id(jobId);
		result.setArea_id(areaId);
		result.setCounty_id(countyId);
		result.setSuccess(success);
		result.setError_info(errorInfo);
		jExecResultDao.save(result);
	}
	
	/**
	 * 删除账目任务
	 * @param jobId
	 * @throws Exception
	 */
	public void removeAcctJob(int jobId) throws Exception{
		jobDao.removeAcctJob(jobId);
	}
	
	public void removeInvalidCalJob(Integer jobId) throws JDBCException {
		jobDao.removeInvalidCalJob(jobId);
	}
	
	public void removeAcctmodeCalJob(Integer jobId) throws JDBCException {
		jobDao.removeAcctmodeCalJob(jobId);
	}
	
	public void deleteTariffJobWithHis(JProdNextTariff tariffJob)throws Exception{
		JProdNextTariffHis tariffJobHis = new JProdNextTariffHis();
		BeanUtils.copyProperties(tariffJob, tariffJobHis);
		jProdNextTariffHisDao.save(tariffJobHis);
		jobDao.deleteTariffJob(tariffJob.getJob_id());
	}
	
	public void deleteUserStopJob(int jobId) throws Exception{
		jobDao.deleteUserStopJob(jobId);
		
	}
	
	/**
	 * 保存处理过来的流水号
	 * @param doneCode
	 * @throws Exception
	 */
	public void updateMaxDoneCode(long doneCode) throws Exception {
			jobDao.updateMaxDoneCode(doneCode);
	}
	
	public void updateCancelDoneCode(long doneCode) throws Exception {
		jobDao.updateCancelDoneCode(doneCode);
	}
	/**
	 * 保存处理过的最大FeeSn
	 * @param feeSn
	 * @throws Exception
	 */
	public void updateFeeAutoPromotion(String userId,String custId,String countyId) throws Exception{
		this.jobDao.updateFeeAutoPromotion(userId,custId,countyId);
	}

	/**
	 * 查找需要执行的资费变更任务
	 * @return
	 * @throws Exception
	 */
	public List<JProdNextTariff> queryTariffJob() throws Exception{
		return jobDao.queryTariffJob();
	}
	
	/**
	 * 查找资费失效且公用账目使用类型不为NONE的产品
	 * @return
	 * @throws Exception
	 */
	public List<CProd> queryProdWithInvalidTariff() throws Exception{
		return jobDao.queryProdWithInvalidTariff();
	}
	
	/**
	 * 查找武汉直属，副终端基本包资费为24元每月（资费ID为4357)，且满3年的产品
	 * @return
	 * @throws JDBCException
	 */
	public List<CProd> queryFzdProdTariff() throws Exception{
		return jobDao.queryFzdProdTariff();
	}
	
	/**
	 * 查找用户下产权为广电的设备编号
	 * @return
	 * @throws Exception
	 */
	public List<String> queryUserGDDevices(String userId) throws Exception{
		return jobDao.queryUserGDDevices(userId);
	}
	
	public List<CDoneCode> queryDoneCode()throws Exception{
		return jobDao.queryDoneCode();
	}
	
	public List<JProdNextTariff> queryNextTariffByJobId() throws Exception {
		return jobDao.queryNextTariffByJobId();
	}

	/**
	 * 根据doneCode 查找业务流水明细
	 * @param doneCode
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CDoneCodeDetail> queryDoneCodeDetail (long doneCode) throws Exception {
		return jobDao.queryDoneCodeDetail(doneCode);
	}
	
	/**
	 * 当天订购的所有流水明细记录
	 * @return
	 * @throws Exception
	 */
	public List<CDoneCodeDetail> queryDoneCodeDetailByOrder () throws Exception {
		return jobDao.queryDoneCodeDetailByOrder();
	}
	
	/**
	 * 查询指定业务订购的基本包
	 * @param doneCode
	 * @return
	 * @throws Exception
	 */
	public PProd queryProdByDoneCode (long doneCode) throws Exception {
		return cProdDao.queryBaseProdByDoneCode(doneCode);
	}


	public List<CProd> queryUpkgProd() throws Exception{
		return cProdDao.queryUpkgProd();
	}

	/**
	 * 查找需要执行的报停任务
	 * @return
	 * @throws Exception
	 */
	public List<JUserStop> queryUserStopJob() throws Exception{
		return jobDao.queryUserStopJob();
	}
	
	/**
	 * 查找需要自动退订的产品
	 * @return
	 * @throws Exception
	 */
	public List<CProdDto> queryProdStopJob(String prodCancelDays) throws Exception{
		List<CProdDto> cpList = jobDao.queryProdStopJob(prodCancelDays);
		return cpList;
	}
	
	/**
	 * 查找自动加授权
	 * @return
	 * @throws Exception
	 */
	public List<CProdDto> queryAutoBusiCmd() throws Exception{
		List<CProdDto> cpList = jobDao.queryAutoBusiCmd();
		return cpList;
	}
	
	/**
	 * 查找宽带加授权
	 * @return
	 * @throws Exception
	 */
	public List<CProdDto> queryBandAutoBusiCmd() throws Exception {
		return jobDao.queryBandAutoBusiCmd();
	}
	
	/**
	 * 查找到期的客户套餐下，还有钱的子产品
	 * @return
	 * @throws Exception
	 */
	public List<CProdDto> queryPkgProds() throws Exception{
		return jobDao.queryPkgProds();
	}
	
	public List<CDoneCode> queryReversalJob() throws Exception {
		return jobDao.queryReversalJob();
	}
	
	public List<CAcctAcctitemInactiveHis> queryInactiveHisByDoneCode(Integer doneCode) throws Exception {
		return jobDao.queryInactiveHisByDoneCode(doneCode);
	}
	
	/**
	 * 解冻记录
	 * @param doneCode
	 * @return
	 * @throws Exception
	 */
	public CAcctAcctitemChange queryFreezeChangeByDoneCode(
			Integer doneCode, String acctId, String acctitemId)
			throws Exception {
		return jobDao.queryAcctitemChangeByDoneCode(doneCode, acctId,
				acctitemId, SystemConstants.ACCT_FEETYPE_PRESENT,
				SystemConstants.ACCT_CHANGE_UNFREEZE);
	}
	
	/**
	 * 解冻对应冲正记录
	 * @param doneCode
	 * @return
	 * @throws Exception
	 */
	public CAcctAcctitemChange queryReversalChangeByDoneCode(
			Integer doneCode, String acctId, String acctitemId)
			throws Exception {
		return jobDao.queryAcctitemChangeByDoneCode(doneCode, acctId,
				acctitemId, SystemConstants.ACCT_FEETYPE_PRESENT,
				SystemConstants.ACCT_CHANGE_UNCFEE);
	}
	
	/**
	 * 标示流水已处理过
	 * @param list
	 * @throws Exception
	 */
	public void updateDoneCodeFalg(List<CDoneCode> list) throws Exception {
		cDoneCodeDao.update(list.toArray(new CDoneCode[list.size()]));
	}
	
	/**
	 * 查找需要执行的资金解冻任务
	 * @return
	 * @throws Exception
	 */
	public void queryAcctUnfreezeJob(DataHandler<CAcctAcctitemInactive> dataHandler) throws Exception{
		jobDao.queryAcctUnfreezeJob(dataHandler);
	}

	public List<CAcctAcctitemInactive> queryAcctFirstUnfreezeJob() throws Exception{
		return jobDao.queryAcctFirstUnfreezeJob();
	}
	
	public int queryAcctUnfreezeJobCount() throws Exception {
		return jobDao.queryAcctUnfreezeJobCount();
	}
	
	public void updateInvoiceByDoneCode(Integer doneCode) throws JDBCException {
		jobDao.updateInvoiceByDoneCode(doneCode);
	}
	
	
	/**
	 * 查找需要取消的VOD预扣费记录
	 * @return
	 * @throws JDBCException 
	 */
	public List<CAcctPreFee> queryNeedCancelPreFee() throws JDBCException {
		return jobDao.queryNeedCancelPreFee();
	}
	
	/**
	 * 查找需要处理促销用户
	 * @return
	 * @throws Exception
	 */
	public List<CUser> queryPromotionUsers() throws Exception{
		return jobDao.queryPromotionUsers();
	}
	
	/**
	 * 查找需要删除账目或者账户的任务
	 * @return
	 * @throws Exception
	 */
	public List<JCustWriteoffAcct> queryWriteOffAcct() throws Exception{
		return jobDao.queryWriteOffAcct();
	}
	
	/**
	 * 查找资费计算任务
	 * @return
	 */
	public List<JCustInvalidCal> queryInvalidCal(int count,int jobId) throws Exception{
		return jobDao.queryInvalidCal(count,jobId);
	}
	/**
	 * 查找到期日计算任务按起止地区
	 *  (area_id > start and  area_id <= end)
	 * @param count
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public List<JCustInvalidCal> queryInvalidCalByAreaid(int count,String start,String end) throws Exception{
		return jobDao.queryInvalidCalByAreaid(count, start, end);
	}
	/**
	 * 查找账务模式判断任务按起止地区
	 *  (area_id > start and  area_id <= end)
	 * @param count
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public List<JCustAcctmodeCal> queryAcctmodeCalByAreaid(int count,String start,String end) throws Exception{
		return jobDao.queryAcctmodeCalByAreaid(count, start, end);
	}
	/**
	 * 查找预开通计算任务
	 * @param jobstep
	 * @return
	 * @throws Exception
	 */
	public List<JProdPreopen> queryProdPreopen(Integer jobstep)throws Exception{
		return jobDao.queryProdPreopen(jobstep);
	}
	/**
	 * 更新预开通的执行步骤
	 * @param jobId
	 * @param jobstep
	 * @throws Exception
	 */
	public void updateProdPreopenStep(String jobId,Integer jobstep)throws Exception{
		jobDao.updateProdPreopenStep(jobId, jobstep);
	}
	/**
	 * 移动预开通到历史表
	 * @param preopen
	 * @throws Exception
	 */
	public void removeProdPreopen(JProdPreopen preopen)throws Exception{
		jobDao.removeProdPreopenToHis(preopen);
		jobDao.deleteProdPreopen(preopen);
	}
	
	public CProdDto queryProdBySn(String prodSn,String countyId)throws Exception{
		return jobDao.queryProdBySn(prodSn,countyId);
		
	}
	
	/**
	 * 查找资费计算任务
	 * @return
	 */
	public List<JCustInvalidCal> queryInvalidCalForPatch(int count,int jobId) throws Exception{
		return jobDao.queryInvalidCalForPatch(count, jobId);
	}
	
	public void dealProdStatusError() throws JDBCException {
		jobDao.dealProdStatusError();
	}
	


	public String queryMaxFeeSn() throws Exception {
		return jobDao.queryMaxFeeSn();
	}

	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}

	/**
	 * 获取地区信息
	 * @param cfgDao
	 */
	public List<SArea> queryArea() throws Exception{
		return cfgDao.queryArea();
	}
	public void setCfgDao(CfgDao cfgDao) {
		this.cfgDao = cfgDao;
	}
	public void setJExecResultDao(JExecResultDao execResultDao) {
		jExecResultDao = execResultDao;
	}
	/**
	 * @param ruleDefineDao the tRuleDefineDao to set
	 */
	public void setTRuleDefineDao(TRuleDefineDao ruleDefineDao) {
		tRuleDefineDao = ruleDefineDao;
	}
	public void setCProdDao(CProdDao prodDao) {
		cProdDao = prodDao;
	}
	public void setJProdNextTariffHisDao(JProdNextTariffHisDao prodNextTariffHisDao) {
		jProdNextTariffHisDao = prodNextTariffHisDao;
	}
	public void setCDoneCodeDao(CDoneCodeDao doneCodeDao) {
		cDoneCodeDao = doneCodeDao;
	}
	public void setTAcctitemToProdDao(TAcctitemToProdDao acctitemToProdDao) {
		tAcctitemToProdDao = acctitemToProdDao;
	}
}
