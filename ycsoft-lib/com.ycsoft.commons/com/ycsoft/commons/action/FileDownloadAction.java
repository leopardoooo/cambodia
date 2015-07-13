package com.ycsoft.commons.action;

import java.io.InputStream;

import org.apache.struts2.ServletActionContext;

import com.ycsoft.commons.abstracts.AbstractAction;



/**
 * 实现文件下载的功能。
 * 设置fileName， 下载本地资源文件 ,文件路径相对application root
 * 如(/resources/resfile/aaa.gif)
 *
 * @see struts.xml
 *
 * @author hh
 */
public class FileDownloadAction extends AbstractAction {

	/**
	 *
	 */
	private static final long serialVersionUID = -2584432382286866548L;
	/**
	 * 需要下载的文件名称
	 */
	private String fileName ;

	/**
	 * 默认情况下，Struts Stream Result会自动调用该函数
	 * @return
	 * @throws Exception
	 */
	public InputStream getInputStream() throws Exception {
        return ServletActionContext.getServletContext().
        				getResourceAsStream(fileName);
    }

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


}
