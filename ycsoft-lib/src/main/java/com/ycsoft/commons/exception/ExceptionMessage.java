package com.ycsoft.commons.exception;

import java.sql.SQLException;

import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.pojo.ExceptionData;

public class ExceptionMessage {
	public static ExceptionData getMsgFromException(Exception ex) {
		// 业务异常的处理
		if (ServicesException.class.isInstance(ex)||ComponentException.class.isInstance(ex)) {
			return msgFromService(ex);
		}
		else if (LoginException.class.isInstance(ex)) {
			return msgFromLogin(ex);
		}else if(ReportException.class.isInstance(ex)){
			//报表系统错误
			return msgFromReport((ReportException)ex);
		}
		//其他系统异常
		else
			return msgFromException(ex);
	}

	// 对于登录超时的处理
	private static ExceptionData msgFromLogin(Exception ex) {
		ExceptionData msg = new ExceptionData();
		msg.setType(ExceptionData.LOGIN_EXCEPTION);
		msg.setTitle(ExceptionData.TITLE_EXCEPTION);
		msg.setContent(ex.getMessage());
		return msg;
	}

	// 对于业务异常消息的处理
	private static ExceptionData msgFromService(Exception ex) {
		ExceptionData msg = new ExceptionData();
		msg.setType(ExceptionData.EXCEPTION);
		msg.setTitle(ExceptionData.TITLE_EXCEPTION);
		msg.setContent(ex.getMessage());
		return msg;
	}

	//报表系统异常消息的处理
	private static ExceptionData msgFromReport(ReportException ex){
		ExceptionData msg = new ExceptionData();
		msg.setType(ExceptionData.REPORT_EXCEPTION);
		msg.setTitle(ExceptionData.TITLE_EXCEPTION);
		msg.setContent(ex.getMessage());
		if(StringHelper.isEmpty(ex.getSql())&&!ex.isPrompt_sign()){
			
			StackTraceElement[] trace = ex.getStackTrace();
			StringBuilder buffer = new StringBuilder();
			Throwable e_message=ex.getCause();
			if(e_message!=null)
				buffer.append("\n\tat").append(e_message.getClass().getName()).append(":").append(e_message.getMessage());
			while(e_message!=null&&e_message.getCause()!=null){
				e_message=e_message.getCause();
				buffer.append("\n\tat").append(e_message.getClass().getName()).append(":").append(e_message.getMessage());
			}
			for (StackTraceElement element : trace)
				buffer.append("\n\tat ").append(element);
			// 异常堆栈 信息结束 同时设置异常堆栈
			msg.setDetail(buffer.toString());
		}else 
			msg.setDetail(ex.getSql());
		return msg;
	}

	// 对于其他异常消息 作为错误信息处理 包括向客户端输送 堆栈信息
	private static ExceptionData msgFromException(Exception ex) {
		ExceptionData msg = new ExceptionData();
		msg.setType(ExceptionData.ERROR);
		msg.setTitle(ExceptionData.TITLE_ERROR);
		// 异常堆栈 信息开始
		StackTraceElement[] trace = ex.getStackTrace();
		StringBuffer buffer = new StringBuffer();
		for (StackTraceElement element : trace)
			buffer.append("\n\tat " + element);
		// 异常堆栈 信息结束 同时设置异常堆栈
		msg.setDetail(buffer.toString());
		if (null != ex.getMessage())
			msg.setContent(ex.getMessage());
		else
			msg.setContent(ex.toString());
		return msg;
	}

/*	private static ExceptionData msgFromDbException(Exception ex) {
		SQLException sqlEx = getSqlExFromEx(ex);
		if (sqlEx == null)
			return msgFromService(new ServicesException("空异常"));
		//需要继续扩展
		//.....
		else if (ConnectException.class.isInstance(sqlEx.getCause()))
			return msgFromService(new ServicesException(
					"数据库连接异常: \n 请检查数据库连接或配置!"));
		else
			return msgFromException((Exception) sqlEx.getCause());
	}*/

	private static SQLException getSqlExFromEx(Exception ex) {
		SQLException sqlEx = null;
		Throwable cause = ex.getCause();
		while (null != cause) {
			if (SQLException.class.isInstance(cause))
				return (SQLException) cause;

			cause = cause.getCause();
		}
		return sqlEx;
	}
}
