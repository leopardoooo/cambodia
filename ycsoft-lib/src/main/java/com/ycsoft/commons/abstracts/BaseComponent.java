package com.ycsoft.commons.abstracts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import com.ycsoft.beans.core.job.JDataSync;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.user.UserResExpDate;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.beans.system.SDataRightType;
import com.ycsoft.beans.system.SLog;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SRole;
import com.ycsoft.beans.task.WTaskUser;
import com.ycsoft.business.dao.core.common.CDoneCodeDao;
import com.ycsoft.business.dao.core.job.JDataSyncDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.system.SDataRightTypeDao;
import com.ycsoft.business.dao.system.SDataTranslationDao;
import com.ycsoft.business.dao.system.SItemvalueDao;
import com.ycsoft.business.dao.system.SLogDao;
import com.ycsoft.business.dao.system.SOptrDao;
import com.ycsoft.business.dao.system.SRoleDao;
import com.ycsoft.business.dao.system.SSysChangeDao;
import com.ycsoft.business.dao.task.WLogDao;
import com.ycsoft.business.dao.task.WTaskUserDao;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;


/**
 *
 * <b>Component 基类定义</b>
 * <ul>
 * <li>提供系统通用的功能</li>
 * <li>用于以后更容易扩展。</li>
 * <ul>
 *
 * @author hh
 * @date Dec 30, 2009 10:10:56 AM
 */
public abstract class BaseComponent{

	@Autowired
	protected SRoleDao sRoleDao;
	@Autowired
	protected SDataRightTypeDao sDataRightTypeDao;
	@Autowired
	protected SItemvalueDao sItemvalueDao;
	@Autowired
	protected JDataSyncDao jDataSyncDao;
	@Autowired
	protected SLogDao sLogDao;
	@Autowired
	protected SOptrDao sOptrDao;
	@Autowired
	protected CDoneCodeDao cDoneCodeDao;
	@Autowired
	protected SSysChangeDao sSysChangeDao;
	@Autowired
	protected WLogDao wLogDao;
	@Autowired
	private CUserDao cUserDao;
	@Autowired
	protected SDataTranslationDao sDataTranslationDao;
	@Autowired
	protected WTaskUserDao wTaskUserDao;
	@Autowired
	protected CProdOrderDao cProdOrderDao;
	
	public List<WTaskUser> queryTaskUser(String taskId) throws Exception{
		List<WTaskUser> taskUserList=wTaskUserDao.queryByTaskId(taskId);
		for(WTaskUser taskuser:taskUserList){
			if(SystemConstants.USER_TYPE_BAND.equals(taskuser.getUser_type())){
				//提取带宽
				List<CProdOrder> orders=cProdOrderDao.queryNotExpAllOrderByUser(taskuser.getUser_id());
				if(orders.size()>0){
					//提取控制字
					List<PRes> pResList=cProdOrderDao.queryPRes(orders.get(0).getProd_id());
					for(PRes res:pResList){
						if(res.getBand_width()!=null&&res.getBand_width()>0){
							taskuser.setBandwidth(res.getBand_width()+"M");
						}
					}
				}
			}
		}
		return taskUserList;
	}
	
	/**
	 * 获得一个用户的所有授权资源时间
	 * Map<res_id,exp_date>
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map<String,Date> getUserResExpDate(String userId) throws Exception{
		List<UserResExpDate> resList = cUserDao.queryUserProdResExpDate(userId);
		Map<String,Date> resMap = new HashMap<>();
		for (UserResExpDate res :resList){
			Date expDate = resMap.get(res.getRes_id());
			if (expDate== null){
				resMap.put(res.getRes_id(), res.getExpDate());
			} else {
				if (expDate.before(res.getExpDate())){
					resMap.put(res.getRes_id(), res.getExpDate());
				}
			}
		}
		
		return resMap;
	}
	/**
	 * 获得一个用户的授权资源按资源失效时间排序
	 * List<Entry<res_id,exp_date>>
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Entry<String, Date>> getUserResMappingListOrderByExpDate(String userId) throws Exception{
		Map<String,Date> userResMap=getUserResExpDate(userId);
		if(userResMap==null||userResMap.size()==0)
			return new ArrayList<>();
		
		List<Entry<String, Date>> mappingList = new ArrayList<Entry<String, Date>>(userResMap.entrySet());
		// 通过比较器实现比较排序
		Collections.sort(mappingList, new Comparator<Entry<String, Date>>() {
			public int compare(Map.Entry<String, Date> mapping1, Map.Entry<String, Date> mapping2) {
				return mapping1.getValue().compareTo(mapping2.getValue());
			}
		});
		return mappingList;
	}
	/**
	 * 账务模式的到期日计算(完全自然月计算方法)
	 * 包含余额增加和余额减少情况的到期日计算
	 */
	public static Date getInvalidDateByAcctmode(Date startDate,int changefee,int rent,int billing_cycle){
		if(changefee==0||rent==0||billing_cycle==0)
			return startDate;
		
		if(changefee>0){
			//余额增加情况
			//取整月
			int months=changefee*billing_cycle/rent;
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.MONTH,months);
			
			//剩余金额
			changefee=changefee-months*rent/billing_cycle;
			if(changefee<=0){
				return calendar.getTime();
			}
			//判断增加月后得到日期是否月底
			Calendar tempc=Calendar.getInstance();
			tempc.setTimeInMillis(calendar.getTimeInMillis());
			tempc.set(Calendar.DATE,1);//设为当前月的1号
			tempc.add(Calendar.MONTH,1);//加一个月，变为下月的1号
			tempc.add(Calendar.DATE,-1);//减去一天，变为当月最后一天
			//最后一个月的基本天数
			int lastbaseday=tempc.get(Calendar.DATE);
			if(tempc.get(Calendar.DATE)==calendar.get(Calendar.DATE)){
				tempc.set(Calendar.DATE,1);//设为当前月的1号
				tempc.add(Calendar.MONTH,2);//加下下个月，变为下月的1号
				tempc.add(Calendar.DATE,-1);//减去一天，变为下个月月最后一天
				lastbaseday=tempc.get(Calendar.DATE);
			}
			//四舍五入
			int changedays=Math.round(changefee*lastbaseday*billing_cycle*1f/rent);
			calendar.add(Calendar.DATE, changedays);
			return  calendar.getTime();
		}else{
			//余额减少情况
			//取整月
			int months=changefee*billing_cycle/rent;
			
			//剩余金额
			changefee=changefee-months*rent/billing_cycle;
			if(changefee>=0){
				//刚好整月退费
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(startDate);
				calendar.add(Calendar.MONTH,months);
				return calendar.getTime();
			}
			//非整月退费情况
			Calendar tempc=Calendar.getInstance();
			tempc.setTime(startDate);
			tempc.set(Calendar.DATE,1);//设为当前月的1号
			tempc.add(Calendar.MONTH,1);//加一个月，变为下月的1号
			tempc.add(Calendar.DATE,-1);//减去一天，变为当月最后一天
			//到期日所在月天数
			int lastbaseday=tempc.get(Calendar.DATE);
			//四舍五入
			int changedays=Math.round(changefee*lastbaseday*billing_cycle*1f/rent);
			
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.DATE, changedays);
			calendar.add(Calendar.MONTH,months);
			return calendar.getTime();	
		}
	}
	/**
	 * 获取DoneCode.
	 * @return
	 * @throws JDBCException
	 */
	public int getDoneCOde() throws JDBCException {
		return Integer.parseInt(cDoneCodeDao.findSequence().toString());
	}
	
	/**
	 * 更具操作员和数据权限类型获取数据权限条件
	 * @param optrId
	 * @param dataRightType
	 * @return
	 * @throws JDBCException
	 * @throws ComponentException
	 */
	protected String queryDataRightCon(SOptr optr,String dataRightType) throws Exception,
			ComponentException {
		SDataRightType dataRight = sDataRightTypeDao.findByKey(dataRightType);
		if (dataRight == null || SystemConstants.BOOLEAN_TRUE.equals(dataRight.getNull_is_all()))
			return SystemConstants.DEFAULT_DATA_RIGHT;
		
		//取操作员原来信息，参数optr有可能是切换后的
		SOptr sOptr = sOptrDao.findByKey(optr.getOptr_id());
		List<SRole> roleList = sRoleDao.queryByOptrId(sOptr.getOptr_id(),dataRightType,sOptr.getCounty_id());
		for (SRole role : roleList) {
			if (StringHelper.isNotEmpty(role.getData_right_level()))
				return role.getData_right_level();
			else if (StringHelper.isNotEmpty(role.getRule_str())) {
				return role.getRule_str().replaceAll("\"", "'");
			}
		}
		
		throw new ComponentException("您没有配置【"+dataRight.getType_name()+"】的数据权限");
	}
	
	/**
	 * 保存数据同步job
	 * @param dataSync
	 * @throws Exception 
	 */
	public void saveDataSyncJob(String cmdType,String detailParams,String tableName) throws Exception{
		JDataSync dataSync = new JDataSync();
		dataSync.setCmd_type(cmdType);
		dataSync.setDetail_params(detailParams);
		dataSync.setTable_name(tableName);
//		dataSync.setCreate_time(DateHelper.now());
		dataSync.setIs_send("N");
		dataSync.setJob_id(getJobId());
		jDataSyncDao.save(dataSync);
	}
	
	public boolean isSuccess(String cmdType,String detailParams,String tableName) throws Exception{
		List<JDataSync> list = jDataSyncDao.getDataSyncJob(cmdType,detailParams,tableName);
		if(list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 保存管理系统操作记录
	 * @param funcCode
	 * @param recId
	 * @param optr
	 * @throws Exception 
	 */
	protected void saveOperateLog(String funcCode,String recId,String recName,SOptr optr) throws Exception{
		SLog log = new SLog();
		log.setDone_code(getLogDonecode());
		log.setFunc_code(funcCode);
		log.setRec_id(recId);
		log.setRec_name(recName);
		log.setOptr_id(optr.getOptr_id());
		log.setCounty_id(optr.getCounty_id());
		log.setArea_id(optr.getArea_id());
		log.setDept_id(optr.getDept_id());
		sLogDao.save(log);
	}
	
	private int getJobId() throws Exception{
		return Integer.parseInt(jDataSyncDao.findSequence(SequenceConstants.SEQ_JOB_ID).toString());
	}
	
	protected int getLogDonecode() throws Exception{
		return Integer.parseInt(sLogDao.findSequence(SequenceConstants.SEQ_LOG_DONE_CODE).toString());
	}	
	
	public SSysChangeDao getSSysChangeDao(){
		return sSysChangeDao;
	}
	
	//public 
}
