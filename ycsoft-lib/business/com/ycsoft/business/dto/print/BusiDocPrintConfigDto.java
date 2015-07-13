/*
 * @(#) BusiDocPrintDto.java 1.0.0 Aug 5, 2011 2:52:21 PM
 *
 * Copyright 2011 YaoChen, Ltd. All rights reserved.
 * YaoChen PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.ycsoft.business.dto.print;

import com.ycsoft.beans.config.TBusiDocTemplatefile;



/**
 * 
 *
 * @author allex
 * @since 1.0
 */
public class BusiDocPrintConfigDto extends TBusiDocTemplatefile {

	private String tpl ;

	public String getTpl() {
		return tpl;
	}

	public void setTpl(String tpl) {
		this.tpl = tpl;
	}
}
