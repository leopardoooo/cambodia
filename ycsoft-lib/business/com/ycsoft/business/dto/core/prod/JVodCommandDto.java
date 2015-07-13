/**
 * JVodCommand.java	2010/11/18
 */

package com.ycsoft.business.dto.core.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.core.job.JVodCommand;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;


public class JVodCommandDto extends JVodCommand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	// JVodCommandDto all properties

	private String card_Id; 

	/**
	 * default empty constructor
	 */
	public JVodCommandDto() {
	}

	public String getCard_Id() {
		return card_Id;
	}

	public void setCard_Id(String card_Id) {
		this.card_Id = card_Id;
	}
 


}