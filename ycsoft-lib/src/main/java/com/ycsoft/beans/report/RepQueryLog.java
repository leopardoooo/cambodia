/**
 * RepQueryLog.java	2010/06/23
 */

package com.ycsoft.beans.report;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;

/**
 * RepQueryLog -> REP_QUERY_LOG mapping
 */
@POJO(tn = "REP_QUERY_LOG", sn = "SEQ_REPQUERY_ID", pk = "")
public class RepQueryLog implements Serializable {

	// RepQueryLog all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 2055981267951497951L;
	private String query_id;
	private String rep_id;
	private String isvalid;
	private String keylist;
	private Integer querytime;
	private Integer querynum;
	private String optr_id;
	private Date create_date;
	private String client_ip;

	public String getClient_ip() {
		return client_ip;
	}

	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}

	/**
	 * default empty constructor
	 */
	public RepQueryLog() {
	}

	// query_id getter and setter
	public String getQuery_id() {
		return query_id;
	}

	public void setQuery_id(String query_id) {
		this.query_id = query_id;
	}

	// rep_id getter and setter
	public String getRep_id() {
		return rep_id;
	}

	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}

	// isvalid getter and setter
	public String getIsvalid() {
		return isvalid;
	}

	public void setIsvalid(String isvalid) {
		this.isvalid = isvalid;
	}

	// keylist getter and setter
	public String getKeylist() {
		return keylist;
	}

	public void setKeylist(String keylist) {
		this.keylist = keylist;
	}

	// querytime getter and setter
	public Integer getQuerytime() {
		return querytime;
	}

	public void setQuerytime(Integer querytime) {
		this.querytime = querytime;
	}

	// querynum getter and setter
	public Integer getQuerynum() {
		return querynum;
	}

	public void setQuerynum(Integer querynum) {
		this.querynum = querynum;
	}

	// optr_id getter and setter
	public String getOptr_id() {
		return optr_id;
	}

	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}

	// create_date getter and setter
	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

}