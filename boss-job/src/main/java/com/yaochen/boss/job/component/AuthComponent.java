package com.yaochen.boss.job.component;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.job.JBandCommand;
import com.ycsoft.beans.core.job.JVodCommand;
import com.ycsoft.boss.remoting.ott.Result;
import com.ycsoft.business.dao.core.job.JBandCommandDao;
import com.ycsoft.business.dao.core.job.JVodCommandDao;
import com.ycsoft.commons.constants.SystemConstants;

@Component
public class AuthComponent {
	@Autowired
	private JVodCommandDao jVodCommandDao;
	@Autowired
	private JBandCommandDao jBandCommandDao;
	public List<JVodCommand> queryOttCmd() throws Exception{
		return jVodCommandDao.queryCmd();
	}
	
	public List<JBandCommand> queryBandCmd() throws Exception{
		return jBandCommandDao.queryCmd();
	} 
	
	
	
	public void saveOttSendResult(JVodCommand cmd,Result result) throws Exception{
		if(result.isSuccess()||result.isBossError()||
				(!result.isUndefinedError()&&!result.isConnectionError())){
			cmd.setIs_send(SystemConstants.BOOLEAN_TRUE);
		}else{
			//网络错误或者未知严重错误需要重发,所有不设置已发状态
			cmd.setIs_send(SystemConstants.BOOLEAN_FALSE);
		}
		cmd.setSend_time(new Date());
		cmd.setIs_success(result.isSuccess()?SystemConstants.BOOLEAN_TRUE:SystemConstants.BOOLEAN_FALSE);
		cmd.setError_info(result.getReason());
		cmd.setReturn_code(Integer.parseInt(result.getStatus()));
		jVodCommandDao.updateByCmd(cmd.getTransnum(), cmd.getIs_send(), cmd.getIs_success(), cmd.getError_info(), cmd.getReturn_code());
	}
	
	public void saveBandSendResult(JBandCommand cmd,Result result) throws Exception{
		if(result.isSuccess()||result.isBossError()||
				(!result.isUndefinedError()&&!result.isConnectionError())){
			cmd.setIs_send(SystemConstants.BOOLEAN_TRUE);
		}else{
			//网络错误或者未知严重错误需要重发,所有不设置已发状态
			cmd.setIs_send(SystemConstants.BOOLEAN_FALSE);
		}
		cmd.setSend_time(new Date());
		cmd.setIs_success(result.isSuccess()?SystemConstants.BOOLEAN_TRUE:SystemConstants.BOOLEAN_FALSE);
		cmd.setError_info(result.getReason());
		cmd.setReturn_code(Integer.parseInt(result.getStatus()));
		//jBandCommandDao.update(cmd);
		jBandCommandDao.updateByCmd(cmd.getTransnum(), cmd.getIs_send(), cmd.getIs_success(), cmd.getError_info(), cmd.getReturn_code());
	}
	
	
}
