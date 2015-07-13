package com.ycsoft.commons.pojo;

public class ExceptionData {
	private String title;
	private String content;
	private int type;
	private String detail;

	public static int EXCEPTION=1;	//业务异常类型
	public static int ERROR=2;	//系统异常类型
	public static int LOGIN_EXCEPTION=3;	//登录超时类型

	public static int REPORT_EXCEPTION=7;//报表异常类型

	public static String TITLE_EXCEPTION="异常信息";
	public static String TITLE_ERROR="错误信息";

	public ExceptionData(){}

	public ExceptionData(String title, String content, int type,String detail) {
		super();
		this.title = title;
		this.content = content;
		this.type = type;
		this.detail = detail;
	}

	public ExceptionData(String content, int type) {
		this(TITLE_EXCEPTION,content,type,null);
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
