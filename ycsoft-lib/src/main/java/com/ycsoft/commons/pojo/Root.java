package com.ycsoft.commons.pojo;

import java.util.List;
import java.util.Map;

import com.ycsoft.daos.core.Pager;


/**
 * 扩展业务的Root ,封装了前台所需要的相关属性，
 *
 * @author hh
 * @date Dec 30, 2009 4:36:10 PM
 */
public class Root {

	//是否成功执行
	private boolean success = true;

	//简单对象
	protected Object simpleObj ;

	//同一类型的结果集
	protected List<?> records;

	//返回一个分页对象
	protected Pager<?> page;

	//多个不同类型的对象
	protected Map<String , ?> others;

	//异常对象
	protected Object exception;

	public Root(){
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getSimpleObj() {
		return simpleObj;
	}

	public void setSimpleObj(Object simpleObj) {
		this.simpleObj = simpleObj;
	}

	public List<?> getRecords() {
		return records;
	}
	public void setRecords(List<?> records) {
		this.records = records;
	}

	public Pager<?> getPage() {
		return page;
	}
	public void setPage(Pager<?> page) {
		this.page = page;
	}
	public Map<String, ?> getOthers() {
		return others;
	}
	public void setOthers(Map<String, ?> others) {
		this.others = others;
	}

	public Object getException() {
		return exception;
	}

	public void setException(Object exception) {
		this.exception = exception;
	}
}
