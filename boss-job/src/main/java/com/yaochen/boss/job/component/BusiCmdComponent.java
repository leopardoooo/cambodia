package com.yaochen.boss.job.component;

import static com.ycsoft.commons.constants.BusiCmdConstants.ACCTIVATE_PROD;
import static com.ycsoft.commons.constants.BusiCmdConstants.ACCTIVATE_RES;
import static com.ycsoft.commons.constants.BusiCmdConstants.ADD_REJECT_RES;
import static com.ycsoft.commons.constants.BusiCmdConstants.BAND_CLEAR_BIND;
import static com.ycsoft.commons.constants.BusiCmdConstants.BAND_EDIT_EXPIREDATE;
import static com.ycsoft.commons.constants.BusiCmdConstants.BAND_EDIT_PWD;
import static com.ycsoft.commons.constants.BusiCmdConstants.CHG_SERVICE;
import static com.ycsoft.commons.constants.BusiCmdConstants.CREAT_USER;
import static com.ycsoft.commons.constants.BusiCmdConstants.EMS;
import static com.ycsoft.commons.constants.BusiCmdConstants.MAIL;
import static com.ycsoft.commons.constants.BusiCmdConstants.OSD;
import static com.ycsoft.commons.constants.BusiCmdConstants.PASSVATE_PROD;
import static com.ycsoft.commons.constants.SystemConstants.BAND_SUPPLYIER_YX;
import static com.ycsoft.commons.constants.SystemConstants.BAND_SUPPLYIER_ZX;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.yaochen.boss.commons.LoggerUtil;
import com.yaochen.boss.dao.BusiDao;
import com.yaochen.boss.dao.CfgDao;
import com.yaochen.boss.job.BusiCfgDataJob;
import com.ycsoft.beans.config.TBusiCmdSupplier;
import com.ycsoft.beans.config.TOpenTemp;
import com.ycsoft.beans.config.TServer;
import com.ycsoft.beans.config.TServerRes;
import com.ycsoft.beans.config.TStbFilled;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.job.JBandCommand;
import com.ycsoft.beans.core.job.JBusiCmd;
import com.ycsoft.beans.core.job.JBusiCmdSendosd;
import com.ycsoft.beans.core.job.JCaCommand;
import com.ycsoft.beans.core.job.JVodCommand;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.user.BandSimpleInfo;
import com.ycsoft.beans.device.RCard;
import com.ycsoft.beans.prod.PProdStaticRes;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.business.dao.config.TOpenTempDao;
import com.ycsoft.business.dao.core.common.CDoneCodeDao;
import com.ycsoft.business.dao.core.job.JBandCommandDao;
import com.ycsoft.business.dao.core.job.JBusiCmdDao;
import com.ycsoft.business.dao.core.job.JBusiCmdSendosdDao;
import com.ycsoft.business.dao.core.job.JCaCommandDao;
import com.ycsoft.business.dao.core.job.JVodCommandDao;
import com.ycsoft.business.dao.core.prod.CProdDao;
import com.ycsoft.business.dao.core.prod.CProdPropChangeDao;
import com.ycsoft.business.dao.prod.PProdStaticResDao;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.business.dto.core.user.UserRes;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;


@Component
public class BusiCmdComponent {
	private JBusiCmdDao jBusiCmdDao;
	private JCaCommandDao jCaCommandDao;
	private JVodCommandDao jVodCommandDao;
	private JBandCommandDao jBandCommandDao;
	private JBusiCmdSendosdDao jBusiCmdSendosdDao;
	private CDoneCodeDao cDoneCodeDao;
	private CfgDao cfgDao;
	private BusiDao busiDao;
	private CProdDao cProdDao;
	private CProdPropChangeDao cProdPropChangeDao;
	private final String CA_LONG_AUTH_DATE ="20151231000000";//ca长授权时间
	private TOpenTempDao tOpenTempDao;
	private PProdStaticResDao pProdStaticResDao;
	
	/**
	 * 处理智能卡指令
	 * @param cmd
	 * @param cfg
	 * @param cardModel
	 * @throws Exception
	 */
	public void sendCardCmd(JBusiCmd cmd,String cardModel)  throws Exception{
		JCaCommand caCmd = gCaCmd(cmd, cardModel);
		List<TServer> servList = BusiCfgDataJob.CFG.getServer(caCmd.getCas_type(), caCmd.getCounty_id());
		List<String> servCmdList = BusiCfgDataJob.CFG.getCmd(cmd.getBusi_cmd_type(), caCmd.getCas_type());
		if(null != servList && null != servCmdList){
			for (TServer serv:servList){
				caCmd.setCas_id(serv.getServer_id());
				for (String cmdType:servCmdList){
					caCmd.setTransnum(gTransnum());
					caCmd.setCmd_type(cmdType);
					jCaCommandDao.save(caCmd);
				}
			}
		}
	}
	
	/**
	 * 处理用户vod指令
	 * @param cmd
	 * @throws Exception
	 */
	public void sendUserVodCmd(JBusiCmd cmd) throws Exception{
		JVodCommand vodCmd = gVodCmd(cmd);
		vodCmd.setDetail_param(cmd.getDetail_params());
		jVodCommandDao.save(vodCmd);
	}
	
	/**
	 * 处理产品VOD指令 
	 * @param cmd
	 * @param validDoubleProdMap 
	 * @param userRejectResMap 
	 * @param userResMap 
	 * @param cardModel 
	 * @throws Exception
	 */
	public void sendProdVodCmd(JBusiCmd cmd, Map<String, List<CProd>> validDoubleProdMap) throws Exception{
		PProdTariff tariff = busiDao.queryProdTariff(cmd.getProd_sn(),cmd.getCounty_id());
		//资费为空，或者不是按次产品
		if(null == tariff || !SystemConstants.BILLING_TYPE_JC.equals(tariff.getBilling_type())){
			//如果是钝化产品
			if(PASSVATE_PROD.equals(cmd.getBusi_cmd_type())){
				List<CProd> prodList = validDoubleProdMap.get(cmd.getUser_id());
				if(null != prodList && prodList.size() > 0){
					boolean include = false;
					for(CProd prod : prodList){
						if(prod.getProd_id().equals(cmd.getProd_id())){
							include = true;
							break;
						}
					}
					//如果有效产中包含这个产品ID，则不需要发送减授权
					if(include){
						return;
					}
				}
			}
			JVodCommand vodCmd = gVodCmd(cmd);
			jVodCommandDao.save(vodCmd);
		}
	}
	
	private void sendChangeBandCmd(JBusiCmd cmd, String cmdType) throws Exception {
		JBandCommand changeBandCmd = this.gBandCmd(cmd);
		changeBandCmd.setCmd_type(cmdType);
		jBandCommandDao.save(changeBandCmd);
	}
	
	/**
	 * 处理宽带产品指令
	 * @param cmd
	 * @throws Exception
	 */
	public void sendBandCmd(JBusiCmd cmd) throws Exception{
		//判断是否有正常状态产品
		boolean isStopflag=true;
		List<CProd> prodList = busiDao.queryUserOpenProd(cmd.getUser_id(), cmd.getCounty_id());
		//如果有正常状态产品，不进行发送停机
		if(null != prodList && prodList.size()>0)
			isStopflag=false;
		
		
		JBandCommand bandCmd = this.gBandCmd(cmd);
		String supplyierId = bandCmd.getSupplyier_id();
		String cmdType = bandCmd.getCmd_type();
		
		//宜昌中兴
		if(supplyierId.equals(BAND_SUPPLYIER_ZX)){
			if(cmdType.equals(ACCTIVATE_PROD)){
				//查找用户宽带历史产品的数量
				int cmdCount = jBandCommandDao.queryCreateUserCmd(cmd.getUser_id());
				if (cmdCount == 0){
					JBandCommand changeBandCmd = this.gBandCmd(cmd);
					//对宜昌做特殊处理，老用户，不开户，直接修改密码
					if (busiDao.queryYcOldBand(cmd.getUser_id()))
						changeBandCmd.setCmd_type(BAND_EDIT_PWD);
					else
						changeBandCmd.setCmd_type(CREAT_USER);
	 				jBandCommandDao.save(changeBandCmd);
				}
				
				//中兴先进行修改服务，然后再进行开机
				this.sendChangeBandCmd(cmd, CHG_SERVICE);
				this.sendChangeBandCmd(cmd, ACCTIVATE_PROD);
				
			}else if(cmdType.equals(PASSVATE_PROD)){
				//中兴先进行停机，然后再变更服务
				if(isStopflag){
					this.sendChangeBandCmd(cmd, PASSVATE_PROD);
				}
				this.sendChangeBandCmd(cmd, CHG_SERVICE);
			}else{
				jBandCommandDao.save(bandCmd);
			}
		} else if(supplyierId.equals(BAND_SUPPLYIER_YX)) {
			//亚信
			if(cmdType.equals(ACCTIVATE_PROD)){
				//查找用户宽带历史产品的数量
				int cmdCount = jBandCommandDao.queryCreateUserCmd(cmd.getUser_id());
				if (cmdCount == 0){
	 				this.sendChangeBandCmd(cmd, CREAT_USER);
				}
				//亚信宽带先发开机指令，再发送修改失效日期指令
				this.sendChangeBandCmd(cmd, ACCTIVATE_PROD);
				
				//亚信宽带加授权，修改ca_end_time，用于宽带巡查
				String prodSn = cmd.getProd_sn();
				if(StringHelper.isNotEmpty(prodSn)){
					CProd prod = cProdDao.findByKey(prodSn);
					prod.setCa_end_time(DateHelper.addDate(prod.getInvalid_date(), 30));
					cProdDao.update(prod);
				}
				
				this.sendChangeBandCmd(cmd, BAND_EDIT_EXPIREDATE);
				
			}else if(cmdType.equals(PASSVATE_PROD)){
				//亚信宽带先修改失效日期指令,再发停机指令
				this.sendChangeBandCmd(cmd, BAND_EDIT_EXPIREDATE);
				if(isStopflag){
					this.sendChangeBandCmd(cmd, PASSVATE_PROD);
				}
			}else{
				jBandCommandDao.save(bandCmd);
			}
		}
		
	}
	
	/**
	 * 处理osd指令
	 * @param cmd
	 * @param cfg
	 * @param cardModel
	 * @throws Exception
	 */
	public void sendMsg(JBusiCmd cmd,String cardModel)  throws Exception{
		JCaCommand caCmd = gCaCmd(cmd, cardModel);
		List<TServer> servList = BusiCfgDataJob.CFG.getServer(caCmd.getCas_type(), caCmd.getCounty_id());
		for (TServer serv:servList){
			if (SystemConstants.BOOLEAN_TRUE.equals(serv.getFor_osd())){
				caCmd.setCas_id(serv.getServer_id());
				String cmdFlag = cmd.getBusi_cmd_type().substring(3);
				if (cmdFlag.charAt(0) == '1'){
					List<String> servCmdList = BusiCfgDataJob.CFG.getCmd(OSD,caCmd.getCas_type() );
					for (String cmdType:servCmdList){
						caCmd.setTransnum(gTransnum());
						caCmd.setCmd_type(cmdType);
						jCaCommandDao.save(caCmd);
					}
				}
				
				if (cmdFlag.charAt(1) == '1'){
					List<String> servCmdList = BusiCfgDataJob.CFG.getCmd( MAIL,caCmd.getCas_type());
					for (String cmdType:servCmdList){
						caCmd.setTransnum(gTransnum());
						caCmd.setCmd_type(cmdType);
						jCaCommandDao.save(caCmd);
					}
				}
				
				if (cmdFlag.charAt(2) == '1'){
					List<String> servCmdList = BusiCfgDataJob.CFG.getCmd(EMS,caCmd.getCas_type());
					for (String cmdType:servCmdList){
						caCmd.setTransnum(gTransnum());
						caCmd.setCmd_type(cmdType);
						jCaCommandDao.save(caCmd);
					}
				}
			}
		}
	}

	/**
	 * 处理数字电视产品指令
	 * <p>lxr: 加授权时，当授权来源于指令重发时，不修改授权截止日期和授权发送日期
	 * @param cmd
	 * @param cfg
	 * @param cardModel
	 * @param userResMap
	 * @throws Exception
	 */
	public void sendDtvProdCmd(JBusiCmd cmd,String cardModel,Map<String,List<UserRes>> userResMap,Map<String, List<UserRes>> userRejectResMap)  throws Exception{
		LoggerUtil.PrintInfo(this.getClass(),"业务指令", "开始"+cmd.getJob_id()+"产品:"+cmd.getProd_id());
		JCaCommand caCmd = gCaCmd(cmd, cardModel);
//		CProd cp = cProdDao.findByKey(cmd.getProd_sn());
		CProdDto cp =  cProdDao.queryByProdSnCommon(cmd.getProd_sn());
		//激活产品且产品存在的情况
		if(null != cp&&cmd.getBusi_cmd_type().equals(ACCTIVATE_PROD)){
			//更新c_prod的last_send_date
			String endDate = getBaseAuthEndDate(cp);
			CDoneCode cDoneCode = cDoneCodeDao.findByKey(cmd.getDone_code());
			
			//如果业务是指令重发且产品的授权截止日期是大于今天,则不修改授权截止日期，使用原产品上的授权截止日期
			if(cDoneCode !=null && BusiCodeConstants.USER_RESEND_CA.equals(cDoneCode.getBusi_code())&&
					cp.getCa_end_time()!=null&&DateHelper.isDateBefore(cp.getCa_end_time())){
				
				endDate=DateHelper.dateToStrYmvod(cp.getCa_end_time());
			}else{
				updateCpod(cmd.getProd_sn(),cmd.getDone_code(),cmd.getArea_id(),cmd.getCounty_id(),cp.getLast_send_time(),cp.getCa_end_time(),endDate);
			}
			
			caCmd.setAuth_end_date(endDate);
		}else{
			caCmd.setAuth_end_date(CA_LONG_AUTH_DATE);
		}
		
		caCmd.setCmd_type(BusiCfgDataJob.CFG.getCmd(cmd.getBusi_cmd_type(), caCmd.getCas_type()).get(0));
		if (caCmd.getDetail_params()!= null && caCmd.getDetail_params().indexOf("RES_ID")>-1){
			String[] resIdArr = cmd.getDetail_params().split(":")[1].replaceAll("'", "").split(",");
			for (String resId:resIdArr){
				sendControlCmd( caCmd, resId,cmd.getUser_id(),cmd.getBusi_cmd_type(),userResMap,userRejectResMap);
			}
			
		} else {
			//获取产品对应的资源
			List<String> resList = cfgDao.queryProdRes(cmd.getProd_id());
			for (String resId:resList){
				sendControlCmd( caCmd, resId,cmd.getUser_id(),cmd.getBusi_cmd_type(),userResMap,userRejectResMap);
				LoggerUtil.PrintInfo(this.getClass(),"业务指令", "静态资源:"+resId);
			}
			List<String> countyResList = cfgDao.queryProdResCounty(cmd.getProd_id(),cmd.getCounty_id());//prod.getCountyResMap().get(cmd.getCounty_id());
			if (countyResList!= null){
				for (String resId:countyResList){
					sendControlCmd( caCmd, resId,cmd.getUser_id(),cmd.getBusi_cmd_type(),userResMap,userRejectResMap);
					LoggerUtil.PrintInfo(this.getClass(),"业务指令", "本地资源:"+resId);
				}
			}
			
			//产品有动态资源
			List<String> dynRes = jBusiCmdDao.queryDnyRes(cmd.getProd_sn());
			for (String resId:dynRes){
				sendControlCmd( caCmd, resId,cmd.getUser_id(),cmd.getBusi_cmd_type(),userResMap,userRejectResMap);
				LoggerUtil.PrintInfo(this.getClass(),"业务指令", "动态资源:"+resId);
			}
		}
		LoggerUtil.PrintInfo(this.getClass(),"业务指令", "结束"+cmd.getJob_id());
	}
	public void updateCpod(String prodSn,Integer doneCode,String areaId, String countyId,Date oldLastDate,Date oldEndDate,String endTime) throws Exception {
			Date now = new Date();
		
			CProd cp = new CProd();
			cp.setProd_sn(prodSn);
			cp.setLast_send_time(now);
			cp.setCa_end_time(DateHelper.vodStrToDate(endTime));
			cProdDao.update(cp);

			List<CProdPropChange> list = new ArrayList<CProdPropChange>();
			CProdPropChange change = new CProdPropChange();
			change.setProd_sn(prodSn);
			change.setDone_code(doneCode);
			change.setArea_id(areaId);
			change.setCounty_id(countyId);
			
			change.setColumn_name("last_send_time");
			change.setOld_value(oldLastDate==null?"":DateHelper.format(oldLastDate));
			change.setNew_value(DateHelper.format(now));
			list.add(change);
			
			CProdPropChange changeCa = new CProdPropChange();
			changeCa.setProd_sn(prodSn);
			changeCa.setDone_code(doneCode);
			changeCa.setArea_id(areaId);
			changeCa.setCounty_id(countyId);
			changeCa.setColumn_name("ca_end_time");
			changeCa.setOld_value(oldEndDate==null?"":DateHelper.format(oldEndDate));
			changeCa.setNew_value(DateHelper.format(DateHelper.vodStrToDate(endTime)));
			list.add(changeCa);
			cProdPropChangeDao.save(list.toArray(new CProdPropChange[list.size()]));
	}
	
	/**
	 * 授权截止日期
	 * @param cp
	 * @return
	 * @throws Exception
	 */
	public String getBaseAuthEndDate(CProdDto cp) throws Exception{
		Date now = new Date();
		if(cp.getIs_base().equals(SystemConstants.BOOLEAN_TRUE)){
			//基本包截止日期
			return DateHelper.dateToStrYMD(DateHelper.getNextMonthByNum(now,24)) +"000000";
		}else if(cp.getBilling_cycle()!=null && cp.getBilling_cycle()>=12){
			if(cp.getLast_send_time() == null){
				return DateHelper.dateToStrYMD(DateHelper.getNextMonthByNum(now,12)) +"000000";
			}else{
				return DateHelper.dateToStrYMD(cp.getInvalid_date()) +"000000";
			}
		}else{
			return DateHelper.dateToStrYMD(DateHelper.getNextMonthByNum(now,7)) +"000000";
		}
	}
	
	
	/**
	 * 处理机顶盒灌装指令
	 * @param cmd
	 * @param cfg
	 * @param cardModel
	 * @throws Exception
	 */
	public void sendStbFilledCmd(JBusiCmd cmd,String cardModel)  throws Exception{
		JCaCommand caCmd = gCaCmd(cmd, cardModel);
		//caCmd.setAuth_end_date(CA_LONG_AUTH_DATE);
		caCmd.setCmd_type(BusiCfgDataJob.CFG.getCmd(ACCTIVATE_PROD, caCmd.getCas_type()).get(0));
		//获取该县市对应的预授权信息
		List<TStbFilled> stbFillResList = cfgDao.queryStbFilledCfg(cmd.getCounty_id());
		for (TStbFilled stbFill :stbFillResList){
			//stbFill.getMonths() 实际意思是days
			caCmd.setAuth_end_date(DateHelper.format(DateHelper.addDate(new Date(),stbFill.getMonths()), "yyyyMMddHHmmss"));
			
			if(StringHelper.isEmpty(caCmd.getStb_id())){
				caCmd.setStb_id("11111111111111111111");
			}
			sendControlCmd(caCmd, stbFill.getRes_id(),null,cmd.getBusi_cmd_type(),null,null);
		}
	}
	
	/**
	 * 一体机临时授权
	 * @param cmd
	 * @param cardModel
	 * @throws Exception
	 */
	public void sendCaCardCmd(JBusiCmd cmd,String cardModel)  throws Exception{
		JCaCommand caCmd = gCaCmd(cmd, cardModel);
		List<CDoneCode> list = cDoneCodeDao.queryCardCaDoneCode(cmd.getCard_id(), cmd.getCounty_id());
		Date invalidDate = new Date();
		if(list.size()>0){
			invalidDate = list.get(0).getDone_date();
		}
		TOpenTemp openTempCfg = tOpenTempDao.queryByCountyId("DTV", cmd.getCounty_id());
		invalidDate = DateHelper.addDate(invalidDate,openTempCfg.getDays());
		if(invalidDate.after(new Date())){
			caCmd.setAuth_end_date(DateHelper.dateToStrYMD(invalidDate) +"000000");
			caCmd.setCmd_type(BusiCfgDataJob.CFG.getCmd(ACCTIVATE_PROD, caCmd.getCas_type()).get(0));
			//获取基本包资源
			List<PProdStaticRes> resList = pProdStaticResDao.queryBaseProdRes();
			for(PProdStaticRes res : resList){
				if(StringHelper.isEmpty(caCmd.getStb_id())){
					caCmd.setStb_id("11111111111111111111");
				}
				sendControlCmd(caCmd, res.getRes_id(),null,cmd.getBusi_cmd_type(),null,null);
			}
		}
	}
	
	
	/**
	 * 处理机顶盒灌装指令
	 * @param cmd
	 * @param cfg
	 * @param cardModel
	 * @throws Exception
	 */
	public void sendCancelStbFilledCmd(JBusiCmd cmd,String cardModel)  throws Exception{
		JCaCommand caCmd = gCaCmd(cmd, cardModel);
		//caCmd.setAuth_end_date(CA_LONG_AUTH_DATE);
		caCmd.setCmd_type(BusiCfgDataJob.CFG.getCmd(PASSVATE_PROD, caCmd.getCas_type()).get(0));
		//获取该县市对应的预授权信息
		List<TStbFilled> stbFillResList = cfgDao.queryStbFilledCfg(cmd.getCounty_id());
		for (TStbFilled stbFill :stbFillResList){
			caCmd.setAuth_end_date(DateHelper.format(new Date(), "yyyyMMddHHmmss"));
			
			if(StringHelper.isEmpty(caCmd.getStb_id())){
				caCmd.setStb_id("11111111111111111111");
			}
			sendControlCmd(caCmd, stbFill.getRes_id(),null,cmd.getBusi_cmd_type(),null,null);
		}
	}
	
	
	
	/**
	 * 处理单卡资源指令发送
	 * @param cmd
	 * @param cfg
	 * @param cardModel
	 * @throws Exception
	 */
	public void sendCardResCmd(JBusiCmd cmd,String cardModel)  throws Exception{
		JCaCommand caCmd = gCaCmd(cmd, cardModel);
		if(cmd.getBusi_cmd_type().equals(ACCTIVATE_RES)){
			caCmd.setCmd_type(BusiCfgDataJob.CFG.getCmd(ACCTIVATE_PROD, caCmd.getCas_type()).get(0));
		}else{
			caCmd.setCmd_type(BusiCfgDataJob.CFG.getCmd(PASSVATE_PROD, caCmd.getCas_type()).get(0));
		}
		
		String[] resIdArr = cmd.getDetail_params().split(";")[0].split(":")[1].replaceAll("'", "").split(",");
		String endDate = cmd.getDetail_params().split(";")[1].split(":")[1].replaceAll("'", "").toString();
		caCmd.setAuth_end_date(DateHelper.format(DateHelper.strToDate(endDate),"yyyyMMddHHmmss"));
		for (String resId:resIdArr){
				this.sendControlCmd(caCmd, resId, cmd.getUser_id(), cmd.getBusi_cmd_type(), null, null);
		}		
	}
	
	/**
	 * 处理排斥资源指令
	 * @param cmd
	 * @param cardModel
	 * @param userResMap
	 */
	public void sendRejectResCmd(JBusiCmd cmd, String cardModel,Map<String,List<UserRes>> userResMap) throws Exception{
		JCaCommand caCmd = gCaCmd(cmd, cardModel);
		caCmd.setAuth_end_date(CA_LONG_AUTH_DATE);
		String[] resIdArr = cmd.getDetail_params().split(":")[1].replaceAll("'", "").split(",");
		if (cmd.getBusi_cmd_type().equals(ADD_REJECT_RES)){
			caCmd.setCmd_type(BusiCfgDataJob.CFG.getCmd(PASSVATE_PROD, caCmd.getCas_type()).get(0));
		} else {
			caCmd.setCmd_type(BusiCfgDataJob.CFG.getCmd(ACCTIVATE_PROD, caCmd.getCas_type()).get(0));
		}
		
		for (String resId:resIdArr){
			if (this.hasRes(resId, cmd.getUser_id(), userResMap)){
				this.sendControlCmd(caCmd, resId, cmd.getUser_id(), cmd.getBusi_cmd_type(), null, null);
			}
		}
	}
	/**
	 * 发送控制字指令
	 * @param cfg
	 * @param caCmd
	 * @param resId
	 * @param userId
	 * @param busiCmdType
	 * @param userResMap
	 * @throws Exception
	 */
	private void sendControlCmd( JCaCommand caCmd, String resId,String userId,String busiCmdType,
			Map<String,List<UserRes>> userResMap,Map<String, List<UserRes>> userRejectResMap)
			throws Exception {
		/**
		 * 过滤指令
		 * 发减授权时，如果资源仍然是有效的，则不发送；
		 * 发加授权是，如果资源是排斥的，则不发送
		 */
		if (busiCmdType.equals(PASSVATE_PROD)){
			if (hasRes(resId, userId, userResMap))
				return;
		} else {
			if (hasRes(resId, userId, userRejectResMap))
				return ;
		}
		List<TServer> servList = BusiCfgDataJob.CFG.getServer(caCmd.getCas_type(), caCmd.getCounty_id());
		boolean success=false;
		for (TServer serv:servList){
			TServerRes serverRes = BusiCfgDataJob.CFG.getExtenalRes(serv.getServer_id(), resId);
			if (serverRes != null){
				caCmd.setCas_id(serverRes.getServer_id());
				caCmd.setControl_id(serverRes.getExternal_res_id());
				LoggerUtil.PrintInfo(this.getClass(),"业务指令", "实际控制字:"+resId);
				caCmd.setBoss_res_id(serverRes.getBoss_res_id());
				caCmd.setPrg_name(serverRes.getRes_name());
				jCaCommandDao.save(caCmd);
				success=true;
				break;
			}
		}
		if (!success){
			throw new ComponentException("找不到资源"+resId+"在"+caCmd.getCounty_id()+"的服务器");
		}
	}

	private boolean hasRes(String resId, String userId,
			Map<String, List<UserRes>> userResMap) {
		if (userResMap != null){
			//判断资源是否在有效的资源列表中
			List<UserRes> userResList = userResMap.get(userId);
			if (userResList != null ){
				for (UserRes userRes:userResList){
					if (userRes.getRes_id().equals(resId)){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	/**
	 * 设置ca指令的基础信息
	 * @param cmd
	 * @param cfg
	 * @param cardModel
	 * @return
	 * @throws Exception
	 */
	private JCaCommand gCaCmd(JBusiCmd cmd, 
			String cardModel) throws Exception {
		JCaCommand caCmd = new JCaCommand();
		BeanUtils.copyProperties(cmd, caCmd);//cust_id,user_id,stb_id,card_id,done_code
		String supplierId = BusiCfgDataJob.CFG.getServerSupplier(cardModel);
		//String serverId = cfg.getServer(supplierId, cmd.getCounty_id());
		caCmd.setCas_type(supplierId);
		caCmd.setIs_sent("N");
		if (StringHelper.isNotEmpty(caCmd.getDetail_params()))
			caCmd.setDetail_params(caCmd.getDetail_params().replaceAll("'", "''"));
		//caCmd.setCas_id(serverId);
		return caCmd;
	}
	
	/**
	 * 设置vod指令的基础信息
	 * @param cmd
	 * @return
	 * @throws Exception
	 */
	private JVodCommand gVodCmd(JBusiCmd cmd) throws Exception {
		JVodCommand vodCmd = new JVodCommand();
		vodCmd.setTransnum(this.gTransnum());
		BeanUtils.copyProperties(cmd, vodCmd);
		vodCmd.setCmd_type(cmd.getBusi_cmd_type());
		vodCmd.setIs_send("N");
		//TODO 设置supplier_id,server_id
		return vodCmd;
	}
	
	private JBandCommand gBandCmd(JBusiCmd cmd) throws Exception {
		JBandCommand bandCmd = new JBandCommand();
		bandCmd.setTransnum(this.gTransnum());
		BeanUtils.copyProperties(cmd, bandCmd);
		bandCmd.setCmd_type(cmd.getBusi_cmd_type());
		bandCmd.setIs_send("N");
		//设置supplier_id,server_id
		TServer server = cfgDao.queryBandServer(cmd.getCounty_id());
		bandCmd.setSupplyier_id(server.getSupplier_id());
		bandCmd.setServer_id(server.getServer_id());
		//查找宽带用户信息和该用户对应的服务信息
		Gson gson = new Gson();
		BandSimpleInfo userBand = busiDao.queryUserBand(cmd.getUser_id());
		String serviceNames = "";
 		if(null != userBand){
//			String prodId = cmd.getProd_id();
			//如果是清除绑定 获取所有的产品
			List<CProd> prodList = null;
			if(BAND_CLEAR_BIND.equals(cmd.getBusi_cmd_type())){
				 prodList = busiDao.queryUserProd(cmd.getUser_id(), cmd.getCounty_id());
			}else{
				if(bandCmd.getSupplyier_id().equals(SystemConstants.BAND_SUPPLYIER_ZX)){
					prodList = busiDao.queryUserOpenProd(cmd.getUser_id(), cmd.getCounty_id());
				}else{
					prodList = busiDao.queryUserProd(cmd.getUser_id(), cmd.getCounty_id());
				}
			}
			if(null != prodList && prodList.size() > 0){
				//如果亚信宽带的清除绑定则获取一个产品
				if(BAND_CLEAR_BIND.equals(cmd.getBusi_cmd_type()) && bandCmd.getServer_id().equals(SystemConstants.BAND_SUPPLYIER_YX)){
					serviceNames = cfgDao.queryBandService(prodList.get(0).getProd_id(),bandCmd.getServer_id());
				}else{
					//中兴宽带获取多个服务
					for(CProd prod : prodList){
						serviceNames+=cfgDao.queryBandService(prod.getProd_id(),bandCmd.getServer_id())+",";
	 				}
				}
 			} 
//			userBand.setService_name(cfgDao.queryBandService(prodId,bandCmd.getServer_id()));
			if(serviceNames.length()>0 && serviceNames.lastIndexOf(",")>0){
				serviceNames = serviceNames.substring(0,serviceNames.length()-1);
			}
			userBand.setService_name(serviceNames);
 			if (StringHelper.isNotEmpty(cmd.getDetail_params())){
				UserDto user = JsonHelper.toObject(cmd.getDetail_params(), UserDto.class);
				userBand.setOld_password(user.getLogin_password());
			}
			bandCmd.setDetail_param(gson.toJson(userBand));
		}
		
		return bandCmd;
	}
	
	public void dealAllOsd(JBusiCmdSendosd osd) throws Exception {

		List<TBusiCmdSupplier> bcs = jBusiCmdDao.queryOsdCmdSupplier(osd.getCa_type().split(","),osd.getSupplier_id());
		List<JCaCommand> caList = new ArrayList<JCaCommand>();
		JCaCommand caCmd = new JCaCommand();
		caCmd.setJob_id(0);
		
		caCmd.setCas_id(osd.getCas_id());
		caCmd.setCas_type(osd.getSupplier_id());
		
		caCmd.setDone_code(0);
		caCmd.setCust_id("0");
		caCmd.setUser_id("0");
		caCmd.setStb_id("11111111111111111111");
		caCmd.setCard_id("ALL");
		caCmd.setCounty_id(osd.getCounty_id());
		caCmd.setArea_id(osd.getArea_id());
		caCmd.setIs_sent("N");
		caCmd.setPriority(SystemConstants.PRIORITY_XXTZ);
		caCmd.setDetail_params("TITLE:''提示'';MSG:''"+osd.getMessage()+"'';");
		
		for(TBusiCmdSupplier dto:bcs){
			JCaCommand ca = new JCaCommand();
			BeanUtils.copyProperties(caCmd, ca);
			ca.setTransnum(gTransnum());
			if("DVN".equalsIgnoreCase(caCmd.getCas_type())){
				if("SendOsd".equalsIgnoreCase(dto.getSupplier_cmd_id())){
					ca.setCmd_type("SendOSDAll");
				}else{
					ca.setCmd_type("SendMailAll");
				}
			}else{
				ca.setCmd_type(dto.getSupplier_cmd_id());
			}
			
			caList.add(ca);
		}
		jCaCommandDao.save(caList.toArray(new JCaCommand[caList.size()]));
		LoggerUtil.PrintInfo(this.getClass(),"通授权发送","JOB编号【"+osd.getJob_id()+"】");
		
	}
	
	public void saveAllOsdHis(int jobId)throws Exception{
		jBusiCmdSendosdDao.saveAllOsdHis(jobId);
	}
	/**
	 * 保存指令任务历史记录
	 * @param minJobId
	 * @param maxJobId
	 * @throws Exception
	 */
	public void saveBusiCmdHis(int jobId)throws Exception{
		jBusiCmdDao.saveBusiCmdHis(jobId);
	}
	public void saveBusiCmdHis(int minJobId,int maxJobId)throws Exception{
		jBusiCmdDao.saveBusiCmdHis(minJobId, maxJobId);
	}
	/**
	 * 查找需要处理的指令
	 * @param areaId
	 * @return
	 * @throws Exception
	 */
	public List<JBusiCmd> queryBusiCmd(String areaId) throws Exception{
		return jBusiCmdDao.queryBusiCmd(areaId);
	}

	public List<JBusiCmdSendosd> queryAllOsdCmd() throws Exception{
		return jBusiCmdSendosdDao.queryAllOsdCmd();
	}
	
	/**
	 * 查找卡信息
	 * @param cardIds 
	 * @return
	 * @throws Exception
	 */
	public Map<String,RCard> queryCardModel (String[] cardIds) throws Exception{
		return jBusiCmdDao.queryCardModel(cardIds);
	}

	
	/**
	 * 查找流水记录
	 * @param donecode
	 * @return
	 * @throws Exception
	 */
	public CDoneCode queryByDonecode(Integer donecode) throws Exception{
		return jBusiCmdDao.queryByDonecode(donecode);
	}
	
	/**
	 * 查找用户下有效的资源
	 * @param userIds
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public Map<String,List<UserRes>> queryUserValidRes(String[] userIds) throws Exception{
		List<UserRes> userResList = jBusiCmdDao.queryValidRes(userIds);
		return CollectionHelper.converToMap(userResList, "user_id");
	}
	
	public Map<String,List<UserRes>> queryUserRejectRes(String[] userIds) throws Exception{
		List<UserRes> userResList = jBusiCmdDao.queryRejectRes(userIds);
		return CollectionHelper.converToMap(userResList, "user_id");
	}
	
	public Map<String,List<CProd>> queryValidDoubleProd(String[] userIds) throws Exception{
		List<CProd> prodList = jBusiCmdDao.queryValidDoubleProd(userIds);
		return CollectionHelper.converToMap(prodList, "user_id");
	}

	private Integer gTransnum() throws Exception{
		return Integer.parseInt(jCaCommandDao.findSequence().toString());
	}


	public void setJCaCommandDao(JCaCommandDao caCommandDao) {
		jCaCommandDao = caCommandDao;
	}

	public void setJBusiCmdDao(JBusiCmdDao busiCmdDao) {
		jBusiCmdDao = busiCmdDao;
	}

	public void setCfgDao(CfgDao cfgDao) {
		this.cfgDao = cfgDao;
	}

	public void setJVodCommandDao(JVodCommandDao vodCommandDao) {
		jVodCommandDao = vodCommandDao;
	}

	public void setJBandCommandDao(JBandCommandDao bandCommandDao) {
		jBandCommandDao = bandCommandDao;
	}

	public void setBusiDao(BusiDao busiDao) {
		this.busiDao = busiDao;
	}
	
	public void setCProdDao(CProdDao prodDao) {
		this.cProdDao = prodDao;
	}


	public void setJBusiCmdSendosdDao(JBusiCmdSendosdDao busiCmdSendosdDao) {
		jBusiCmdSendosdDao = busiCmdSendosdDao;
	}
	public void setCProdPropChangeDao(CProdPropChangeDao prodPropChangeDao) {
		cProdPropChangeDao = prodPropChangeDao;
	}

	public void setCDoneCodeDao(CDoneCodeDao cDoneCodeDao) {
		this.cDoneCodeDao = cDoneCodeDao;
	}

	public void setTOpenTempDao(TOpenTempDao openTempDao) {
		tOpenTempDao = openTempDao;
	}

	public void setPProdStaticResDao(PProdStaticResDao prodStaticResDao) {
		pProdStaticResDao = prodStaticResDao;
	}
	

}
