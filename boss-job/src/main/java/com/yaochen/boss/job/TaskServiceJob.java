package com.yaochen.boss.job;

import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.TaskComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.beans.task.WTaskLog;
import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.ResultHead;
import com.ycsoft.boss.remoting.cfocn.WordOrderException;
import com.ycsoft.boss.remoting.cfocn.WorkOrderClient;
import com.ycsoft.boss.remoting.ott.Result;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.SystemConstants;

/**
 * 发送工单创建信息
 * @author panyb
 */
@Service
public class TaskServiceJob implements Job2 {

	private static final long serialVersionUID = -5000336520239971955L;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private TaskComponent taskComponent;

	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		WorkOrderClient client = new WorkOrderClient();
		List<WTaskLog> taskLogList = null;
		//施工队
		String cfonTeamId =null; 
		try{
			taskLogList = taskComponent.querySynTaskLog();
			cfonTeamId=taskComponent.getTeamId(SystemConstants.TEAM_TYPE_CFOCN);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("读取工单同步信息错误"+e.getMessage());
			return;
		}
		
		for (WTaskLog taskLog:taskLogList){
			Result result = new Result();
			/**JsonObject params =null;
			if (StringHelper.isNotEmpty(taskLog.getLog_detail()))
				params = new JsonParser().parse(taskLog.getLog_detail()).getAsJsonObject();
			**/
			try{
				if (taskLog.getBusi_code().equals(BusiCodeConstants.TASK_Withdraw)){
					//撤回工单
					taskComponent.cancelTaskService(client,taskLog.getTask_id(),taskLog.getDone_code());
				} else if (taskLog.getBusi_code().equals(BusiCodeConstants.TASK_ASSIGN)){
					taskComponent.sendNewWorkOrder(client,taskLog.getTask_id(),cfonTeamId);		
				}else {
					result.setStatus(Result.BOSS_ERROR_STATUS);
					result.setReason("未定义的同步类型");
				}
				
				if(result.getStatus()==null){
					result.setErr("0");
					result.setReason("成功");
				}
			} catch (WordOrderException e){
				ResultHead rh =e.getResult();
				if (rh == null){
					//系统异常
					result.setErr(Result.UNDEFINED_ERROR_STATUS);
					result.setReason(e.getMessage());
					logger.error("未知严重错误，暂停发送!",e);
				} else {
					result.setErr(rh.getHeadCode());
					result.setReason(rh.getHeadMsg());
				}
			}catch(Exception e){
				result.setErr(Result.UNDEFINED_ERROR_STATUS);
				result.setReason(e.getMessage());
				logger.error("未知严重错误，暂停发送!",e);
			}
			//保存发送结果
			try{
				taskComponent.saveTaskSynResult(taskLog, result);
			} catch(Exception e){
				e.printStackTrace();
				logger.error("保存工单同步结果失败",e);
				return;
			}
			//网络错误或系统未知错误额外休眠5秒
			if (!result.isSuccess()&&(result.isConnectionError()||result.isUndefinedError())){
				try {
					Thread.sleep(1000*5);
				} catch (Exception e) {
					logger.error("网络错误和未知错误休眠失败",e);
				}
			}
		}

	}

	/**
	private String getJsonValue(JsonObject jo,String key){
		return jo.get(key).isJsonNull()?null:jo.get(key).getAsString();
	}**/

}
