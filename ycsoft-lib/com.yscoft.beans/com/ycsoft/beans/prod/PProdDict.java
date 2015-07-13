/**
 * PProdDict.java	2010/07/06
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;

import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.daos.config.POJO;

/**
 * PProdDict -> P_PROD_DICT mapping
 */
@POJO(tn = "P_PROD_DICT", sn = SequenceConstants.SEQ_P_PROD_DICT, pk = "node_id")
public class PProdDict extends OptrBase implements Serializable {

	// PProdDict all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -140701206439416712L;
	private String node_id;
	private String node_pid;
	private String node_name;
	private Boolean checked;
	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	/**
	 * default empty constructor
	 */
	public PProdDict() {
	}

	// node_id getter and setter
	public String getNode_id() {
		return node_id;
	}

	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}

	// node_pid getter and setter
	public String getNode_pid() {
		return node_pid;
	}

	public void setNode_pid(String node_pid) {
		this.node_pid = node_pid;
	}

	// node_name getter and setter
	public String getNode_name() {
		return node_name;
	}

	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}

}