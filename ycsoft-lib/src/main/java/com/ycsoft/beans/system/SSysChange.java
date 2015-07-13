package com.ycsoft.beans.system;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;
@POJO(tn="S_SYS_CHANGE",sn="SEQ_CHANGE_ID",pk="")
public class SSysChange implements Serializable{

	private String change_type;//异动对象
	private String change_type_text;//异动对象
	private Integer done_code;//
	private String key;//内容键值
	private String key_desc;//键值说明
	private String change_desc;//哪个表变化，什么东西变化
	private String content;//变化内容
	private String optr_id;//操作员
	private String optr_name;//操作员
	private Date create_time;//操作时间
	private String change_id;
	
	public String getChange_id() {
		return change_id;
	}

	public void setChange_id(String change_id) {
		this.change_id = change_id;
	}

	public SSysChange() {
	}
	
	public SSysChange(String changeType, Integer doneCode, String key,
			String keyDesc, String changeDesc, String content, String optrId,
			Date createTime) {
		super();
		change_type = changeType;
		done_code = doneCode;
		this.key = key;
		key_desc = keyDesc;
		change_desc = changeDesc;
		this.content = content;
		optr_id = optrId;
		create_time = createTime;
	}



	/**
	 * @return the change_type
	 */
	public String getChange_type() {
		return change_type;
	}
	/**
	 * @param changeType the change_type to set
	 */
	public void setChange_type(String changeType) {
		change_type = changeType;
		change_type_text = SysChangeType.valueOf(changeType).getShowName();
	}
	/**
	 * @return the change_type_text
	 */
	public String getChange_type_text() {
		return change_type_text;
	}
	/**
	 * @return the done_code
	 */
	public Integer getDone_code() {
		return done_code;
	}
	/**
	 * @param doneCode the done_code to set
	 */
	public void setDone_code(Integer doneCode) {
		done_code = doneCode;
	}
	/**
	 * @return the change_desc
	 */
	public String getChange_desc() {
		return change_desc;
	}
	/**
	 * @param changeDesc the change_desc to set
	 */
	public void setChange_desc(String changeDesc) {
		change_desc = changeDesc;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the optr_id
	 */
	public String getOptr_id() {
		return optr_id;
	}
	/**
	 * @param optrId the optr_id to set
	 */
	public void setOptr_id(String optrId) {
		optr_id = optrId;
		setOptr_name(MemoryDict.getDictName(DictKey.OPTR, optrId));
	}
	/**
	 * @return the optr_name
	 */
	public String getOptr_name() {
		return optr_name;
	}
	/**
	 * @param optrName the optr_name to set
	 */
	public void setOptr_name(String optrName) {
		optr_name = optrName;
	}
	/**
	 * @return the create_time
	 */
	public Date getCreate_time() {
		return create_time;
	}
	/**
	 * @param createTime the create_time to set
	 */
	public void setCreate_time(Date createTime) {
		create_time = createTime;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey_desc() {
		return key_desc;
	}

	public void setKey_desc(String keyDesc) {
		key_desc = keyDesc;
	}
}
