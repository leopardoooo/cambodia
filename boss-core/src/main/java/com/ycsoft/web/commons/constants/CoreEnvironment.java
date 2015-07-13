package com.ycsoft.web.commons.constants;

import com.ycsoft.commons.constants.Environment;

/**
 * 系统常量定义
 */
public final class CoreEnvironment extends Environment {


	/**
	 * 系统资源菜单根节点id
	 */
	public static final int SYSTEM_RESOURCE_ROOT_ID = 0 ;

	/**
	 * 登陆处理函数
	 */
	public static final String LOGIN_PROCESS_METHOD = "Index!login.action";

	/**
	 * 系统中请求参数的JSON串的名称
	 */
	public static final String REQUEST_JSON_PARATEMER_STRING = "jsonParames";

	/**
	 * 封装好的参数，设置到Request的名称，
	 * 方便在Spring拦截器获取这个所有的参数对象
	 */
	public static final String BUSIPARAM_INTERCEPT_KEY = "busiParameterInterceptorKey";
}
