package com.ycsoft.commons.constants;

/**
 * 系统常量定义
 * @author <a href='mailTo:huanghui2004@hotmail.com'>hh</a>
 */
public class Environment {

	/**
	 * 操作员登录成功后，在Session缓存，保存用户报表权限的KEY
	 */
	public static final String REPORT_USER_ROLE="report_user_role";

	public static final String REPORT_SYSTEM_KEY_MAP="report_system_keyvalue_map";

	/**
	 * 当操作员登录成功后，在Session缓存，保存用户信息的KEY
	 */
	public static final String USER_IN_SESSION_NAME = "boss_user_login_name" ;

	/**
	 * SSO登录的参数KEY
	 */
	public static final String SSO_TOKEN_PARAM_KEY = "tokenId";
	
	
	/**
	 * web工程名
	 */
	public static final String CONTEXT_PATH = "contextpath";

	/**
	 * 系统资源菜单根节点id
	 */
	public static final int SYSTEM_RESOURCE_ROOT_ID = 0 ;

	/**
	 * 指定登陆处理函数，用于拦截器不验证登录LoginValid
	 */
	public static final String LOGIN_PROCESS_METHOD = "Index!login.action";

	/**
	 * 指定切换系统的处理函数。用于拦截器
	 */
	public static final String TOGGLE_SYSTEM_PROCESS_METHOD = "rego";

	/**
	 * 系统中请求参数的JSON串的名称
	 */
	public static final String REQUEST_JSON_PARATEMER_STRING = "jsonParams";

	/**
	 * 封装好的参数，设置到Request的名称，
	 * 方便在Spring拦截器获取这个所有的参数对象
	 */
	public static final String BUSIPARAM_INTERCEPT_KEY = "busiParameterInterceptorKey";

	public static final String ROOT_PATH_BOSS_CORE = "/boss-core" ;
	public static final String ROOT_PATH_BOSS_LOGIN = "/boss-login";

	/**
	 * 登录的URL
	 */
	public static final String LOGIN_URL = ROOT_PATH_BOSS_LOGIN + "/login.jsp";
	
	public static final String CURRENT_BUSI_OPTR_ID = "busiOptrId";

}
