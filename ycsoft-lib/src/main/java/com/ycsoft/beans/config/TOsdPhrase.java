package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

@POJO(tn = "T_OSD_PHRASE", pk = "pid",sn = "seq_osd_phrase_id")
public class TOsdPhrase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1547386034201832495L;
	private String pid;
	private String phrase;

	/**
	 * @return the pid
	 */
	public String getPid() {
		return pid;
	}

	/**
	 * @param pid
	 *            the pid to set
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}

	/**
	 * @return the phrase
	 */
	public String getPhrase() {
		return phrase;
	}

	/**
	 * @param phrase
	 *            the phrase to set
	 */
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

}
