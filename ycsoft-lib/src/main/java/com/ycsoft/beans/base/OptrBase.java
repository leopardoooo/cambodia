package com.ycsoft.beans.base;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

/**
 * 业务操作记录类
 *
 * 业务操作记录Bean继承本类，删除子类中的相关属性
 *
 * @author liujiaqi
 *
 */
public class OptrBase extends CountyBase  implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -5118191800720091364L;
	private String optr_id;
	private String dept_id;
	private String agent_id;

	private String agent_name;
	private String dept_name;
	private String optr_name;
	private String login_name;
	
	public String getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(String agent_id) {
		agent_name = MemoryDict.getDictName(DictKey.AGENT, agent_id);
		this.agent_id = agent_id;
	}

	public String getAgent_name() {
		return agent_name;
	}

	public void setAgent_name(String agent_name) {
		this.agent_name = agent_name;
	}


	public void setDept_id(String dept_id) {
		dept_name = MemoryDict.getDictName(DictKey.DEPT, dept_id);
		login_name = MemoryDict.getDictName(DictKey.OPTR_LOGIN, optr_id);
		this.dept_id = dept_id;
	}

	/**
	 * @return the optr_id
	 */
	public String getOptr_id() {
		return optr_id;
	}

	/**
	 * @param optr_id
	 *            the optr_id to set
	 */
	public void setOptr_id(String optr_id) {
		optr_name = MemoryDict.getDictName(DictKey.OPTR, optr_id);
		this.optr_id = optr_id;
	}

	/**
	 * @return the dept_name
	 */
	public String getDept_name() {
		return dept_name;
	}

	/**
	 * @param dept_name
	 *            the dept_name to set
	 */
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	/**
	 * @return the optr_name
	 */
	public String getOptr_name() {
		return optr_name;
	}

	/**
	 * @param optr_name
	 *            the optr_name to set
	 */
	public void setOptr_name(String optr_name) {
		this.optr_name = optr_name;
	}

	/**
	 * @return the dept_id
	 */
	public String getDept_id() {
		return dept_id;
	}

	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}
}
