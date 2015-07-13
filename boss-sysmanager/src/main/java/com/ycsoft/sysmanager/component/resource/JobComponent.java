package com.ycsoft.sysmanager.component.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TServer;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.job.JBusiCmd;
import com.ycsoft.beans.core.job.JBusiCmdSendosd;
import com.ycsoft.beans.core.job.JBusiCmdSendosdCfg;
import com.ycsoft.beans.core.job.JBusiCmdSendosdHis;
import com.ycsoft.beans.core.job.JCaCommand;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.device.RCard;
import com.ycsoft.beans.device.RStb;
import com.ycsoft.beans.system.SLog;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dao.core.job.JBusiCmdDao;
import com.ycsoft.business.dao.core.job.JBusiCmdSendosdCfgDao;
import com.ycsoft.business.dao.core.job.JBusiCmdSendosdDao;
import com.ycsoft.business.dao.core.job.JBusiCmdSendosdHisDao;
import com.ycsoft.business.dao.core.job.JCaCommandDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.resource.device.RCardDao;
import com.ycsoft.business.dao.resource.device.RDeviceChangeDao;
import com.ycsoft.business.dao.resource.device.RDeviceDao;
import com.ycsoft.business.dao.resource.device.RStbDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.FuncCode;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.resource.DeviceDto;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;

@Component
public class JobComponent extends BaseComponent{
	private JBusiCmdDao jBusiCmdDao;
	private JCaCommandDao jCaCommandDao;
	private JBusiCmdSendosdDao jBusiCmdSendosdDao;
	private JBusiCmdSendosdCfgDao jBusiCmdSendosdCfgDao;
	private JBusiCmdSendosdHisDao jBusiCmdSendosdHisDao;
	private RDeviceDao rDeviceDao;
	private RStbDao rStbDao;
	private RCardDao rCardDao;
	private CUserDao cUserDao;
	private RDeviceChangeDao rDeviceChangeDao;
	
	public List<SLog> queryCurrentDateLog(String cardId) throws Exception {
		return sLogDao.queryCurrDateLog(FuncCode.STB_FILLED.toString(),cardId);
	}
	
	public List<JCaCommand> queryCurrDateCommand(String jobId, String cardId) throws Exception {
		return jCaCommandDao.queryCurrDateCommand(jobId, cardId);
	}
	
	public List<TServer> queryServerByCountyId(String countyId) throws Exception {
		return jBusiCmdDao.queryForOsdServer(countyId);
	}
	
	/**
	 * 通授权
	 * @param start_date
	 * @param end_date
	 * @param send_time
	 * @param end_time
	 * @param send_num
	 * @param time_num
	 * @param send_for
	 * @param serverId
	 * @param supplierId
	 * @param detailParams
	 * @param optr
	 * @throws Exception
	 */
	public void saveSendAllCmd(Date start_date,Date end_date,Date send_time,Date end_time,Integer send_num,Integer time_num,
			Integer send_for,String serverId, String supplierId,String detailParams,String caType,SOptr optr) throws Exception {
		
		
		List<JBusiCmdSendosd> dList = new ArrayList<JBusiCmdSendosd>();
		Integer doneCode = gDoneCode();
		Date sendTime = new Date();  //最终执行时间
		Date runDate = new Date();   //多天任务的开始执行时间
		Date sendTimeDate = new Date(); //第一天任务的开始时间包含时间段
		Date endTimeDate = new Date();  //第一天任务的截止时间包含时间段
		
		sendTimeDate.setYear(start_date.getYear());
		sendTimeDate.setMonth(start_date.getMonth());
		sendTimeDate.setDate(start_date.getDate());
		sendTimeDate.setHours(send_time.getHours());
		sendTimeDate.setMinutes(send_time.getMinutes());
		sendTimeDate.setSeconds(send_time.getSeconds());
		
		endTimeDate.setYear(start_date.getYear());
		endTimeDate.setMonth(start_date.getMonth());
		endTimeDate.setDate(start_date.getDate());
		endTimeDate.setHours(end_time.getHours());
		endTimeDate.setMinutes(end_time.getMinutes());
		endTimeDate.setSeconds(end_time.getSeconds());
		
		double seconds = ((double)time_num/(double)send_num)*60*60;  //每次需要多少秒
		long onceTime = (endTimeDate.getTime()-sendTimeDate.getTime())/1000;  //时间段一共多少秒
		int a = (int)((double)onceTime/seconds); //在时间段里，执行多少次		
		int c = DateHelper.getDiffDays(start_date, end_date); //执行多少天
		for(int i=0;i<=c;i++){
			runDate = DateHelper.addNumDate(sendTimeDate, i, "day");
			if(a==0){
				sendTime = runDate;
				for(int k=0;k<send_for;k++){
					JBusiCmdSendosd osd = new JBusiCmdSendosd();
					osd.setJob_id(getJobId());
					osd.setDone_code(doneCode);
					osd.setCas_id(serverId);
					osd.setSupplier_id(supplierId);
					osd.setOptr_id(optr.getOptr_id());
					osd.setCounty_id(optr.getCounty_id());
					osd.setArea_id(optr.getArea_id());
					osd.setCa_type(caType);
					osd.setMessage(detailParams);
					osd.setSend_time(sendTime);
					dList.add(osd);
				}
			}else{
				for(int j=0;j<a;j++){
					for(int k=0;k<send_for;k++){
						sendTime = DateHelper.addNumDate(runDate, (int)seconds*j, "second");
						JBusiCmdSendosd osd = new JBusiCmdSendosd();
						osd.setJob_id(getJobId());
						osd.setDone_code(doneCode);
						osd.setCas_id(serverId);
						osd.setSupplier_id(supplierId);
						osd.setOptr_id(optr.getOptr_id());
						osd.setCounty_id(optr.getCounty_id());
						osd.setArea_id(optr.getArea_id());
						osd.setCa_type(caType);
						osd.setMessage(detailParams);
						osd.setSend_time(sendTime);
						dList.add(osd);
					}
				}
			}
		}
		jBusiCmdSendosdDao.save(dList.toArray(new JBusiCmdSendosd[dList.size()]));
		
		Date now = DateHelper.now();
		String optrId = optr.getOptr_id();
		String countyId = optr.getCounty_id();
		String areaId = optr.getArea_id();
		List<JBusiCmdSendosdCfg> cfgList = new ArrayList<JBusiCmdSendosdCfg>();
		
		JBusiCmdSendosdCfg cfg = new JBusiCmdSendosdCfg();
		cfg.setDone_code(doneCode);
		cfg.setCas_id(serverId);
		cfg.setSupplier_id(supplierId);
		cfg.setCmd_type(caType);
		cfg.setSend_start_date(start_date);
		cfg.setSend_end_date(end_date);
		cfg.setSend_start_time(String.valueOf(send_time.getHours()));
		cfg.setSend_end_time(String.valueOf(end_time.getHours()));
		cfg.setSend_cycle(time_num);
		cfg.setSend_times(send_num);
		cfg.setSend_repeat_times(send_for);
		cfg.setMessage(detailParams);
		cfg.setOptr_id(optr.getOptr_id());
		cfg.setCounty_id(optr.getCounty_id());
		cfg.setArea_id(optr.getArea_id());
		
		jBusiCmdSendosdCfgDao.save(cfg);
		
	}

	
	/**
	 * 查询通授权
	 * @param start
	 * @param limit
	 * @param keyword
	 * @param optr
	 * @return
	 * @throws Exception
	 */
	public Pager<JBusiCmdSendosd> querySendAllCmd(Integer start , Integer limit ,String keyword,SOptr optr)throws Exception{
		
		return jBusiCmdSendosdDao.query(start, limit,keyword,optr.getCounty_id());
	}
	
	public Pager<JBusiCmdSendosdCfg> querySendAllCmdProp(Integer start , Integer limit ,String query,SOptr optr)throws Exception{
		return jBusiCmdSendosdCfgDao.query(start, limit, query, optr.getCounty_id());
	}
	
	public Pager<JBusiCmdSendosdHis> querySendAllCmdHis(Integer start , Integer limit ,String query,SOptr optr)throws Exception{
		return jBusiCmdSendosdHisDao.query(start, limit, query, optr.getCounty_id());
	}
	
	
	/**删除未发送的通授权
	 * @param doneCode
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSendAllCmd(Integer doneCode) throws Exception {
		jBusiCmdSendosdDao.delete(doneCode);
		return true;
	}
	
	/**
	 * 创建单机灌装命令
	 * @param cardId
	 * @param optr
	 * @throws Exception
	 */
	public void createCmdStbFilled(String cardId,String stbId,SOptr optr) throws Exception {
		if(StringHelper.isEmpty(cardId)){
			throw new ComponentException("智能卡不能为空!");
		}
		//查询配对的机顶盒号
		RCard card = rCardDao.queryCardById(cardId);
		if(card == null){
			throw new ComponentException("智能卡不存在!");
		}
		//查询卡号原配对的机顶盒号
		RStb stbByCard = rStbDao.findPairStbByCardDeviceId(card.getDevice_id());
		RStb stbByStb = null;
		if( StringHelper.isNotEmpty(stbId) ){
			stbByStb = rStbDao.queryStbById(stbId);
			if(stbByStb == null){
				throw new ComponentException("该机顶盒【"+stbId+"】不存在!");
			}
		}
		
		if(stbByCard != null && stbByStb != null && StringHelper.isNotEmpty(stbByStb.getPair_card_id()) 
				&& !stbByStb.getPair_card_id().equals(card.getDevice_id())){
			RCard cardByCard = rCardDao.findByKey(stbByStb.getPair_card_id());
			throw new ComponentException("灌装的智能卡已经和:"+stbByCard.getStb_id()+"配对，灌装的机顶盒已经和："+cardByCard.getCard_id()+"配对!");
		}
		//记录异动
		Integer doneCode = gDoneCode();
		if( StringHelper.isNotEmpty(stbByStb.getStb_id()) ){
			if(stbByStb.getPair_card_id() != null && !stbByStb.getPair_card_id().equals(card.getDevice_id())){
				saveDeviceChange(doneCode, BusiCodeConstants.DEVICE_STB_FILLED, stbByStb.getDevice_id(), "pair_card_id",stbByStb.getPair_card_id(), card.getDevice_id());
				if( StringHelper.isNotEmpty(stbByStb.getPair_card_id()) ){
					saveDeviceChange(doneCode, BusiCodeConstants.DEVICE_STB_FILLED, stbByStb.getPair_card_id(), "pair_stb_id",stbByStb.getDevice_id(), "");
				}
			}
		}
		
		if(StringHelper.isEmpty(stbId)){
			if(stbByCard != null)
				stbId = stbByCard.getStb_id();
		}else{
			//取消该卡原来配对的机顶盒号
			if(stbByCard != null){
				stbByCard.setPair_card_id("");
				rStbDao.update(stbByCard);
			}
			//把卡号配对到灌装的机顶盒号
			stbByStb.setPair_card_id(card.getDevice_id());
			rStbDao.update(stbByStb);
		}
		JBusiCmd cmd = new JBusiCmd();

		cmd.setJob_id(getJobId());
		cmd.setDone_code(gDoneCode());
		cmd.setStb_id(stbId);
		cmd.setCard_id(cardId);
		cmd.setOptr_id(optr.getOptr_id());
		cmd.setDept_id(optr.getDept_id());
		cmd.setCounty_id(optr.getCounty_id());
		cmd.setArea_id(optr.getArea_id());
		cmd.setBusi_cmd_type(BusiCmdConstants.ACCTIVATE_TERMINAL);
		cmd.setCreate_time(DateHelper.now());

		jBusiCmdDao.save(cmd);

		cmd.setJob_id(getJobId());
		cmd.setBusi_cmd_type(BusiCmdConstants.STB_FILLED);
		cmd.setCreate_time(DateHelper.now());

		jBusiCmdDao.save(cmd);
		
		saveOperateLog(FuncCode.STB_FILLED.toString(), String.valueOf(cmd.getJob_id()), cmd.getCard_id(), optr);
	}
	
	/**
	 * 机卡解绑
	 * @param cardId
	 * @param stbId
	 * @param optr
	 * @throws Exception
	 */
	public void cancelStbCardFilled(String cardId,String stbId,SOptr optr) throws Exception {
		RStb stb =  rStbDao.findByKey(stbId);
		
		Integer doneCode = gDoneCode();
		saveDeviceChange(doneCode, BusiCodeConstants.DEVICE_CARDSTBFILLED, stb.getDevice_id(), "pair_card_id",stb.getPair_card_id(), "");
		saveDeviceChange(doneCode, BusiCodeConstants.DEVICE_CARDSTBFILLED, stb.getPair_card_id(), "pair_stb_id",stb.getDevice_id(), "");
		
		stb.setPair_card_id("");
		rStbDao.update(stb);
	}
	
	private void saveDeviceChange(Integer doneCode, String busiCode,
			String deviceId, String columnName, String oldValue, String newValue) throws Exception {
		rDeviceChangeDao.saveDeviceChange(doneCode,busiCode,deviceId,columnName,oldValue,newValue,WebOptr.getOptr().getOptr_id()
				,WebOptr.getOptr().getDept_id(),WebOptr.getOptr().getCounty_id(),WebOptr.getOptr().getArea_id());
	}
	
	/**
	 * 创建取消单机灌装命令
	 * @param cardId
	 * @param optr
	 * @throws Exception
	 */
	public void createCmdCancelStbFilled(String cardId,SOptr optr) throws Exception {
		JBusiCmd cmd = new JBusiCmd();

		cmd.setJob_id(getJobId());
		cmd.setDone_code(gDoneCode());
		cmd.setCard_id(cardId);
		cmd.setOptr_id(optr.getOptr_id());
		cmd.setDept_id(optr.getDept_id());
		cmd.setCounty_id(optr.getCounty_id());
		cmd.setArea_id(optr.getArea_id());
		cmd.setBusi_cmd_type(BusiCmdConstants.CANCEL_STB_FILLED);
		cmd.setCreate_time(DateHelper.now());

		jBusiCmdDao.save(cmd);

		cmd.setJob_id(getJobId());
		cmd.setBusi_cmd_type(BusiCmdConstants.PASSVATE_TERMINAL);
		cmd.setCreate_time(DateHelper.now());

		jBusiCmdDao.save(cmd);
	}

	/**
	 * 单卡指令发送
	 * @param cmd
	 * @param optr
	 * @throws Exception
	 */
	public void createBusiCmd(JBusiCmd cmd,SOptr optr) throws Exception {
		Integer doneCode = gDoneCode();
		cmd.setJob_id(getJobId());
		cmd.setDone_code(doneCode);
		cmd.setCreate_time(DateHelper.now());
		if(StringHelper.isEmpty(cmd.getStb_id())){
			cmd.setStb_id("11111111111111111111");
		}
		if(cmd.getBusi_cmd_type().equals(BusiCmdConstants.OSD)){
			cmd.setBusi_cmd_type("MSG10000000");
			cmd.setPriority(SystemConstants.PRIORITY_XXTZ);
		}else if(cmd.getBusi_cmd_type().equals(BusiCmdConstants.MAIL)){
			cmd.setBusi_cmd_type("MSG01000000");
			cmd.setPriority(SystemConstants.PRIORITY_XXTZ);
		}else if(cmd.getBusi_cmd_type().equals(BusiCmdConstants.EMS)){
			cmd.setBusi_cmd_type("MSG00100000");
			cmd.setPriority(SystemConstants.PRIORITY_XXTZ);
		}else {
			cmd.setPriority(SystemConstants.PRIORITY_SSSQ);
		}
		jBusiCmdDao.save(cmd);
		
		//保存流水
		CDoneCode cDoneCode = new CDoneCode();
		cDoneCode.setDone_code(doneCode);
		cDoneCode.setBusi_code(BusiCodeConstants.CARD_CA_SEND);
		cDoneCode.setStatus(StatusConstants.ACTIVE);
		cDoneCode.setCounty_id(optr.getCounty_id());
		cDoneCode.setArea_id(optr.getArea_id());
		cDoneCode.setDept_id(optr.getDept_id());
		cDoneCode.setOptr_id(optr.getOptr_id());
		cDoneCodeDao.save(cDoneCode);
	}
	
	public List<JBusiCmd> createBusiCmdFile(List<String> cardList, JBusiCmd cmd,SOptr optr) throws Exception {
		Integer doneCode = gDoneCode();
		cmd.setDone_code(doneCode);
		cmd.setCreate_time(DateHelper.now());
		if(cmd.getBusi_cmd_type().equals(BusiCmdConstants.OSD)){
			cmd.setBusi_cmd_type("MSG10000000");
			cmd.setPriority(SystemConstants.PRIORITY_XXTZ);
		}else if(cmd.getBusi_cmd_type().equals(BusiCmdConstants.MAIL)){
			cmd.setBusi_cmd_type("MSG01000000");
			cmd.setPriority(SystemConstants.PRIORITY_XXTZ);
		}else if(cmd.getBusi_cmd_type().equals(BusiCmdConstants.EMS)){
			cmd.setBusi_cmd_type("MSG00100000");
			cmd.setPriority(SystemConstants.PRIORITY_XXTZ);
		}else {
			cmd.setPriority(SystemConstants.PRIORITY_SSSQ);
		}
		
		List<JBusiCmd> busiCmdList = new ArrayList<JBusiCmd>();
//		Map<String, List<String>> map = new HashMap<String,List<String>>();
		List<JBusiCmd> list = new ArrayList<JBusiCmd>();
		for(String cardId : cardList){
			DeviceDto device = rDeviceDao.queryDeviceByCard(cardId);
			if (device == null) {
				throw new ComponentException("设备【"+cardId+"】不存在");
			}
			String deviceCaType = device.getCa_type();
			String cmdCaType = cmd.getSupplier_id();
			//加授权、减授权
			boolean flag = cmd.getBusi_cmd_type().equals(BusiCmdConstants.ACCTIVATE_RES) || 
				cmd.getBusi_cmd_type().equals(BusiCmdConstants.PASSVATE_RES);
			if( (flag && deviceCaType.equals(cmdCaType) && device.getCounty_id().equals(cmd.getCounty_id()))
					||  (!flag && deviceCaType.equals(cmdCaType)) ){
				RStb stb = rStbDao.findPairStbByCardDeviceId(cardId);
				String stbId = "";
				if(stb != null){
					stbId = stb.getStb_id();
				}else{
					List<CUser> user = cUserDao.queryUserByCardId(cardId);
					if(user.size()>0){
						stbId = user.get(0).getStb_id();
					}
				}
				
				cmd.setJob_id(getJobId());
				JBusiCmd busiCmd = new JBusiCmd();
				BeanUtils.copyProperties(cmd, busiCmd);
				busiCmd.setCard_id(cardId);
				
				if(StringHelper.isEmpty(stbId)){
					busiCmd.setStb_id("111111111111111111");
				}else{
					busiCmd.setStb_id(stbId);
				}
				
				busiCmdList.add(busiCmd);
				
				JBusiCmd jBusiCmd = new JBusiCmd();
				jBusiCmd.setCard_id(cardId);
				list.add(jBusiCmd);
			}else{
				if(!deviceCaType.equals(cmdCaType)){
					JBusiCmd jBusiCmd = new JBusiCmd();
					jBusiCmd.setSupplier_id(cardId);
					list.add(jBusiCmd);
				}
				
				if(flag && !device.getCounty_id().equals(cmd.getCounty_id())){
					JBusiCmd jBusiCmd = new JBusiCmd();
					jBusiCmd.setCounty_id(cardId);
					list.add(jBusiCmd);
				}
			}
		}
		
		if(busiCmdList.size() > 0){
			jBusiCmdDao.save(busiCmdList.toArray(new JBusiCmd[busiCmdList.size()]));
			//保存流水
			CDoneCode cDoneCode = new CDoneCode();
			cDoneCode.setDone_code(doneCode);
			cDoneCode.setBusi_code(BusiCodeConstants.CARD_CA_SEND);
			cDoneCode.setStatus(StatusConstants.ACTIVE);
			cDoneCode.setCounty_id(optr.getCounty_id());
			cDoneCode.setArea_id(optr.getArea_id());
			cDoneCode.setDept_id(optr.getDept_id());
			cDoneCode.setOptr_id(optr.getOptr_id());
			cDoneCodeDao.save(cDoneCode);
		}
		return list;
	}
	
	private Integer gDoneCode() throws JDBCException {
		return jBusiCmdDao.findSequence(SequenceConstants.SEQ_DONE_CODE).intValue();
	}

	private int getJobId() throws Exception{
		return Integer.parseInt(jBusiCmdDao.findSequence(SequenceConstants.SEQ_JOB_ID).toString());
	}
	
	private Integer gTransnum() throws Exception{
		return Integer.parseInt(jCaCommandDao.findSequence().toString());
	}

	public Pager<JCaCommand> queryCaCommandByCardId(String[] cardIds,
			Integer start, Integer limit) throws JDBCException {
		return jCaCommandDao.queryCaByCardId(cardIds,start,limit);
	}
	
	public void setJBusiCmdDao(JBusiCmdDao busiCmdDao) {
		jBusiCmdDao = busiCmdDao;
	}

	public void setJCaCommandDao(JCaCommandDao caCommandDao) {
		jCaCommandDao = caCommandDao;
	}
	public void setJBusiCmdSendosdDao(JBusiCmdSendosdDao busiCmdSendosdDao) {
		jBusiCmdSendosdDao = busiCmdSendosdDao;
	}

	public void setJBusiCmdSendosdHisDao(JBusiCmdSendosdHisDao busiCmdSendosdHisDao) {
		jBusiCmdSendosdHisDao = busiCmdSendosdHisDao;
	}

	public void setJBusiCmdSendosdCfgDao(JBusiCmdSendosdCfgDao busiCmdSendosdCfgDao) {
		jBusiCmdSendosdCfgDao = busiCmdSendosdCfgDao;
	}

	public void setRDeviceDao(RDeviceDao deviceDao) {
		rDeviceDao = deviceDao;
	}

	public void setRStbDao(RStbDao stbDao) {
		rStbDao = stbDao;
	}

	public void setCUserDao(CUserDao userDao) {
		cUserDao = userDao;
	}

	public void setRCardDao(RCardDao cardDao) {
		rCardDao = cardDao;
	}

	public RDeviceChangeDao getRDeviceChangeDao() {
		return rDeviceChangeDao;
	}

	public void setRDeviceChangeDao(RDeviceChangeDao deviceChangeDao) {
		rDeviceChangeDao = deviceChangeDao;
	}
	
}
