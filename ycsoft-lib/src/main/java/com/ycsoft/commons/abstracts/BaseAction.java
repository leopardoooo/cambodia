package com.ycsoft.commons.abstracts;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.pojo.Root;

/**
 * <p>业务<code>Action</code>的基类，
 * 包含操作员等大部分业务模块<code>Action</code>共有的信息。</p>
 *
 * @author hh
 */
public class BaseAction extends AbstractAction{

	/**
	 *
	 */
	private static final long serialVersionUID = -4798784078940038896L;
	protected SOptr optr ;
	protected Root root ;

	//定义JSON返回结果类型
	protected static final String JSON_SIMPLEOBJ = "json-simpleObj";
	protected static final String JSON_RECORDS = "json-records";
	protected static final String JSON_OTHER = "json-others";
	protected static final String JSON_PAGE = "json-page";
	protected static final String JSON_SUCCESS = "json-success";
	protected static final String JSON = "json";
	protected static final String EXCEL = "excel";
	
	/**
	 * <p>当使用Ajax上传文件时，返回该结果类型，
	 * 将需要返回的JSON内容设置到当前的request作用域中</p>
	 * <p> examples: 
	 * 	<pre>
	 * 	getProxyRoot().setSimpleObj("文件上传成功!");
	 *	return AJAX_UPLOAD;
	 *	</pre>
	 * </p>
	 */
	protected static final String AJAX_UPLOAD = "ajax-upload";


	protected Integer start ;
	protected Integer limit ;

	/**
	 * @see {@link #AJAX_UPLOAD} 
	 */
	@Deprecated
	protected String retrunNone(String msg) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		msg = msg.replaceAll("'", " ");
		out.print("{success:true,msg:'"+msg+"'}");
		out.flush();
		out.close();
		return NONE;
	}
	
	/**
	 * @see {@link #AJAX_UPLOAD} 
	 */
	@Deprecated
	protected String returnList(List<?> list) throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String records = JsonHelper.fromObject(list);
		out.print("{success:true,records : "+records+"}");
		out.flush();
		out.close();
		return NONE;
	}

	public Root getRoot() {
		return root = root == null
				? new Root()
				: root;
	}
	
	public Root getProxyRoot(){
		Root root= this.getRoot();
		request.setAttribute("root", root);
		return root;
	}

	public void setRoot(Root rootOfPager) {
		root = rootOfPager;
	}

	public SOptr getOptr() {
		return optr;
	}

	public void setOptr(SOptr optr) {
		this.optr = optr;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}
