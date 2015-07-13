package com.ycsoft.commons.abstracts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.ycsoft.commons.helper.LoggerHelper;

/**
 * <p>
 * 抽象的Action，包含常用的Action属性、函数等。将错误消息进行了封装。
 * 所有的消息内容均来自属性配置文件中。并且将<code>ActionError、ActionMessage</code>
 * 统一定义为：<code>ActionError</code> </p>
 *
 * @author hh
 */
public class AbstractAction extends ActionSupport
			 implements ServletRequestAware {
	private static final long serialVersionUID = 100321896328163821l;

	/**
	 * 日志管理对象
	 */
	protected HttpServletRequest request ;

	/**
	 * @deprecated  "error" result has not been used
	 */
	@Deprecated
	public static final String ERROR = "error";

	/**
	 * 添加 Action 错误
	 * @param msgKey 消息key
	 * @param args 消息中包含占位符对应的value
	 */
	public void addActionErrorMsg(String msgKey , String ...args) {
		if( hasMsgKey( msgKey ) ){
			super.addActionError( getText( msgKey , args) ) ;
		}
    }

	/**
	 * 添加属性错误
	 * @param fieldName 属性名称
	 * @param msgKey 消息key
	 */
	public void addFieldErrorMsg( String fieldName ,String msgKey){
		if( hasMsgKey( msgKey ) ){
			super.addFieldError( fieldName , getText( msgKey) ) ;
		}
	}

	// check msg key and log console
	private boolean hasMsgKey( String msgKey ){
		if ( false == hasKey( msgKey )) {
			LoggerHelper.warn(AbstractAction.class,getText("abstract.msg_not_found", new String[]{msgKey}));
			return false ;
		}
		return true ;
	}


	/**
	 * @deprecated Use {@link #addActionErrorMsg()}.
	 */
	@Override
	@Deprecated
	public void addActionError(String anErrorMessage) {
       super.addActionError( anErrorMessage );
    }

	/**
	 * @deprecated Use {@link #addFieldErrorMsg()}.
	 */
	@Override
	@Deprecated
	public void addFieldError(String fieldName, String errorMessage) {
        super.addFieldError(fieldName, errorMessage);
    }
	/**
	 * @deprecated Use {@link #addActionErrorMsg()}
	 */
	@Override
	@Deprecated
    public void addActionMessage(String aMessage) {
		 super.addActionMessage( aMessage );
    }

	public HttpSession getSession() {
		return request.getSession() ;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}
