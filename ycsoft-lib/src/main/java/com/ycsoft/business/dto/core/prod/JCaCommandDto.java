package com.ycsoft.business.dto.core.prod;

import com.ycsoft.beans.core.job.JCaCommand;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class JCaCommandDto extends JCaCommand {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8001300301500799639L;
	private String terminal_type;
	private String terminal_type_text;
	/**
	 * @return the terminal_type
	 */
	public String getTerminal_type() {
		return terminal_type;
	}
	/**
	 * @param terminal_type the terminal_type to set
	 */
	public void setTerminal_type(String terminal_type) {
		this.terminal_type = terminal_type;
		this.terminal_type_text = MemoryDict.getDictName(DictKey.TERMINAL_TYPE, this.terminal_type);
	}
	/**
	 * @return the terminal_type_text
	 */
	public String getTerminal_type_text() {
		return terminal_type_text;
	}
	public void setTerminal_type_text(String terminal_type_text) {
		this.terminal_type_text = terminal_type_text;
	}

}
