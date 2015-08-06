package com.yaochen.boss.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yaochen.boss.commons.LoggerUtil;
import com.yaochen.boss.job.component.BusiCmdComponent;
import com.yaochen.boss.job.component.JobComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.beans.core.job.JBusiCmd;
import com.ycsoft.beans.core.job.JBusiCmdSendosd;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.device.RCard;
import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.business.dto.core.user.UserRes;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;

/**
 * 业务指令任务处理
 * @author Tom
 */
@Service
@Scope("prototype")
public class BusiCmdJob implements Job2 {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private BusiCmdComponent busiCmdComponent;
	private JobComponent jobComponent;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		List<JBusiCmd> cmdList = null;
		List<JBusiCmdSendosd> osdList = null;
		Map<String,RCard> cardMap = null; //卡号和卡型号的对应关系
		Map<String,List<UserRes>> userResMap = null;//用户名下有效的资源
		Map<String,List<UserRes>> userRejectResMap = null;//用户排斥的资源
		Map<String,List<CProd>> validDoubleProdMap = null;//用户有效的双向产品
		//查找业务指令
		try {
			logger.info("业务指令任务处理开始...");
			cmdList = busiCmdComponent.queryBusiCmd(null);
			//osdList = busiCmdComponent.queryAllOsdCmd();
//			if(cmdList.size() == 0 && osdList.size() == 0){
//				Thread.currentThread().sleep(5000);
//			}
			if(cmdList.size() == 0){
				Thread.currentThread().sleep(5000);
			}
//			if(osdList.size()>0){
//				//发送通授权
//				dealAllOsd(osdList);
//			}
			if(cmdList.size()>0){
				//获取卡信息
				String[] userIds = new String[cmdList.size()];
				List<String> cardList = new ArrayList<String>();
				int i=0;
				for (JBusiCmd cmd:cmdList){
					if (StringHelper.isNotEmpty(cmd.getCard_id())){
						cardList.add(cmd.getCard_id());
					}
					userIds[i]=cmd.getUser_id();
					i++;
				}
				String[] cardIds = cardList.toArray(new String[cardList.size()]);
				if (cardIds != null && cardIds.length>0)
					cardMap = busiCmdComponent.queryCardModel(cardIds);
				userResMap = busiCmdComponent.queryUserValidRes(userIds);
				userRejectResMap = busiCmdComponent.queryUserRejectRes(userIds);
				validDoubleProdMap = busiCmdComponent.queryValidDoubleProd(userIds);
				int errorCount = sendCmd(cmdList, cardMap, userResMap,userRejectResMap,validDoubleProdMap);
				logger.debug("共处理了"+cmdList.size()+"条指令;错误数:"+errorCount);
			}
		} catch(Exception e){
			logger.error("业务指令出错" , e.getMessage());
		}
	}
	public void dealAllOsd(List<JBusiCmdSendosd> osdList) throws Exception{
		for (JBusiCmdSendosd osd:osdList){
			try{
				busiCmdComponent.dealAllOsd(osd); 
				//将指令信息插入到历史表
				busiCmdComponent.saveAllOsdHis(osd.getJob_id());
			} catch (Exception e){
				e.printStackTrace();
				LoggerUtil.PrintInfo(this.getClass(),"通授权指令",e.getMessage());
			}
		}
	}

	private int sendCmd(List<JBusiCmd> cmdList, Map<String, RCard> cardMap,
			Map<String, List<UserRes>> userResMap,Map<String, List<UserRes>> userRejectResMap, Map<String, List<CProd>> validDoubleProdMap) throws Exception {
		int errorCount=0;
		int minJobId=cmdList.get(0).getJob_id();
		int maxJobId=cmdList.get(0).getJob_id();
		for (JBusiCmd cmd:cmdList){
			if (cmd.getJob_id()<minJobId)
				minJobId = cmd.getJob_id();
			if (cmd.getJob_id()>maxJobId)
				maxJobId = cmd.getJob_id();
			try{
				String cardModel ="";
				if (StringHelper.isNotEmpty(cmd.getCard_id())){
					RCard card = cardMap.get(cmd.getCard_id());
					if (card == null)
						throw new ComponentException("JOBID "+cmd.getJob_id()+" 对应的卡号不存在");
					cardModel = card.getDevice_model();
				}
				
				if(StringHelper.isNotEmpty(cardModel) && cardModel.equalsIgnoreCase("SC1201")){
					//将指令信息插入到历史表
					busiCmdComponent.saveBusiCmdHis(cmd.getJob_id());
					continue;
				}
				if (cmd.getBusi_cmd_type().equals(BusiCmdConstants.DEL_USER)
						|| cmd.getBusi_cmd_type().equals(BusiCmdConstants.CREAT_USER)
						|| cmd.getBusi_cmd_type().equals(BusiCmdConstants.CHANGE_USER)){
					// 处理用户vod指令
					if(StringHelper.isNotEmpty(cmd.getDetail_params())){
						UserDto userDto = JsonHelper.toObject(cmd.getDetail_params(), UserDto.class);
						if("DOUBLE".equals(userDto.getServ_type())){
							busiCmdComponent.sendUserVodCmd(cmd);
						}
						if(cmd.getBusi_cmd_type().equals(BusiCmdConstants.DEL_USER)&&"BAND".equals(userDto.getUser_type())){
							busiCmdComponent.sendBandCmd(cmd);
						}
					}
				} else if (cmd.getBusi_cmd_type().equals(BusiCmdConstants.ACCTIVATE_TERMINAL) 
						|| cmd.getBusi_cmd_type().equals(BusiCmdConstants.PASSVATE_TERMINAL)
						|| cmd.getBusi_cmd_type().equals(BusiCmdConstants.REFRESH_TERMINAL)
						){
					//设备指令
					if (StringHelper.isNotEmpty(cmd.getCard_id())){
						//机卡设备
						busiCmdComponent.sendCardCmd(cmd, cardModel);
					} else {
						//其他未知设备
					}
				} else if (cmd.getBusi_cmd_type().equals(BusiCmdConstants.ACCTIVATE_PROD)
						|| cmd.getBusi_cmd_type().equals(BusiCmdConstants.PASSVATE_PROD)){
					//产品指令
					String servId = "";
					try{
						servId = BusiCfgDataJob.CFG.getProd(cmd.getProd_id()).getServ_id();
					} catch(Exception e){
						throw new ComponentException("产品不存在");
					}

					if (servId.equals(SystemConstants.PROD_SERV_ID_ATV)){
						//模拟电视产品不做授权处理
					} else if (servId.equals(SystemConstants.PROD_SERV_ID_DTV)){
						//数字电视产品
						busiCmdComponent.sendDtvProdCmd(cmd, cardModel, userResMap,userRejectResMap);
					} else if (servId.equals(SystemConstants.PROD_SERV_ID_ITV)){
						//双向产品
						busiCmdComponent.sendProdVodCmd(cmd, validDoubleProdMap);
					} else if (servId.equals(SystemConstants.PROD_SERV_ID_BAND)){
						//宽带产品
						busiCmdComponent.sendBandCmd(cmd);
					}
				} else if (cmd.getBusi_cmd_type().equals(BusiCmdConstants.STB_FILLED)){
					//机顶盒灌装指令
					busiCmdComponent.sendStbFilledCmd(cmd,cardModel);
				}  else if (cmd.getBusi_cmd_type().equals(BusiCmdConstants.CARD_FILLED)){
					//一体机预授权指令
					busiCmdComponent.sendCaCardCmd(cmd,cardModel);
				} else if (cmd.getBusi_cmd_type().equals(BusiCmdConstants.CANCEL_STB_FILLED)){
					//机顶盒灌装指令
					busiCmdComponent.sendCancelStbFilledCmd(cmd,cardModel);
				} else if (cmd.getBusi_cmd_type().equals(BusiCmdConstants.ACCTIVATE_RES)
						|| cmd.getBusi_cmd_type().equals(BusiCmdConstants.PASSVATE_RES)){
					//单卡资源指令发送
					busiCmdComponent.sendCardResCmd(cmd,cardModel);
				} else if (cmd.getBusi_cmd_type().equals(BusiCmdConstants.ADD_REJECT_RES) 
						|| cmd.getBusi_cmd_type().equals(BusiCmdConstants.DEL_REJECT_RES)){
					//机顶盒灌装指令
					busiCmdComponent.sendRejectResCmd(cmd,cardModel,userResMap);
				} else if (cmd.getBusi_cmd_type().indexOf(BusiCmdConstants.MSG)>-1){
					//催缴
					busiCmdComponent.sendMsg(cmd, cardModel);
				} else if (cmd.getBusi_cmd_type().indexOf("BAND")>-1){
					busiCmdComponent.sendBandCmd(cmd);
				}
				jobComponent.saveJobExecute(cmd.getJob_id(), cmd.getArea_id(),cmd.getCounty_id(), SystemConstants.BOOLEAN_TRUE, "");
			}catch(Exception e){
				logger.error("业务指令错误，指令编号【"+cmd.getJob_id()+"】"+e.getMessage());
				errorCount++;
				jobComponent.saveJobExecute(cmd.getJob_id(), cmd.getArea_id(),cmd.getCounty_id(), SystemConstants.BOOLEAN_FALSE, e.getMessage());
			}
			//将指令信息插入到历史表
			busiCmdComponent.saveBusiCmdHis(cmd.getJob_id());
		}
		
		return errorCount;
	}
	
	public void setBusiCmdComponent(BusiCmdComponent busiCmdComponent) {
		this.busiCmdComponent = busiCmdComponent;
	}

	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}

}
