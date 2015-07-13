/**
 *
 */
package com.ycsoft.business.dto.system;

import com.ycsoft.beans.system.SResource;

/**
 * 按钮菜单dto
 * @author YC-SOFT
 */
public class MenuButtonDto{

	private String text;
	private String handler;
	private String iconCls;
	private SResource attrs;

	public MenuButtonDto(SResource sr){
		text = sr.getRes_name();
		handler = sr.getHandler();
		iconCls = sr.getIconcls();
		attrs = sr ;
	}


	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}


	public SResource getAttrs() {
		return attrs;
	}


	public void setAttrs(SResource attrs) {
		this.attrs = attrs;
	}


	public String getHandler() {
		return handler;
	}


	public void setHandler(String handler) {
		this.handler = handler;
	}

}
