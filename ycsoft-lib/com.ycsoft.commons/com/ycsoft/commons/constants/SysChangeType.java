package com.ycsoft.commons.constants;

import java.lang.String;

/**
 * 异动类型.
 */
public enum SysChangeType {
	PROD("产品异动"), OPTRCONFIG("操作员信息异动"), ROLE("角色异动"),
	RULE("规则异动"),PROMFEE("套餐缴费异动"),PROMOTION("促销异动"),
	DEPT("机构配置"),SYS_PARAM("系统参数配置"),
	EXT_CFG("扩展配置"),TASK_CFG("任务配置"),ADDRESS("地区配置"),
	BUSIFEE("费用项异动"),TEMPLATE("模板异动"),PROMTHEME("促销异动");

	private SysChangeType(String name) {
		this.showName = name;
	}

	public String getShowName(){
		return this.showName;
	}

	private String showName; // 自定义数据域，private为了封装。
}
