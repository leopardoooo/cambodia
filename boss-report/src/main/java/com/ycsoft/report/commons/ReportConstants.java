package com.ycsoft.report.commons;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 报表常量配置
 * @author new
 *
 */
public class ReportConstants {
	
	/**
	 * 报表任务状态
	 */
	public static final String TASK_STATUS_WAITEXEC="waitexec";//待执行
	public static final String TASK_STATUS_INVALID="invalid";//失效
	public static final String TASK_STATUS_EXEC="exec";//正在执行
	public static final String TASK_STATUS_EXECSUCESS="execsucess";//执行成功
	public static final String TASK_STATUS_EXECFFAILED="execfailed";//执行失败
	/**
	 * 报表任务执行类型
	 */
	public static final String TASK_TYPE_DAY="day";//每日重复
	public static final String TASK_TYPE_WEEK="week";//每周重复
	public static final String TASK_TYPE_MONTH="month";//每月重复
	public static final String TASK_TYPE_ONE="one";//一次性执行
	
	/**
	 * 快逸 数据集定义的参数
	 */
	public static final String DIM_KEY="dim_key";
	
	/**
	 * 快逸 模板定义的参数
	 */
	public static final String QUERY_ID="query_id";
	public static final String REP_ID="rep_id";	
	public static final String TEST_QUERY_ID="test_query_id";
	
	/**
	 * 分区标志
	 */
	public final static String PARTITION_KEY_CON="#countyid#";
	/**
	 *  15分钟清理一次非历史查询缓存
	 */
	public  static int CLEAR_REP_CACHE=15;

	/**
	 * 系统数据库数据库,报表配置数据库
	 */
	public final static String DATABASE_SYSTEM="system";//系统默认数据库
	/**
	 * 数据库类型
	 */
	public final static String DATABASETYPE_REALTIME="realtime";//数据库类型=实时库
	public final static String DATABASETYPE_HISTROY="histroy";//数据库类型=历史库
	/**
	 * 报表类型
	 */
	public final static String REP_TYPE_COMMON="common";//普通报表
	public final static String REP_TYPE_QUIEE="total";//快逸模板报表
	public final static String REP_TYPE_OLAP="olap";//olap报表
	public final static String REP_TYPE_OLAP_DETAIL="olap_detail";//olap明细报表
	/**
	 * 报表属性定义
	 */
	public final static String REP_INFO_COMMON="common";//普通
	public final static String REP_INFO_BUSINESS ="business";//停业报表
	public final static String REP_INFO_FINANCE="finance";//财务报表
	/**
	 * 页面组件类型
	 */
	public final static String htmlcode_checkboxgroup="checkboxgroup";
	public final static String htmlcode_combo="combo";
	public final static String htmlcode_datefield="datefield";
	public final static String htmlcode_textarea="textarea";
	public final static String htmlcode_textfield="textfield";
	public final static String htmlcode_lovcombo="lovcombo";
	public final static String htmlcode_datetimefield="datetimefield";
	public final static String htmlcode_fileupload="fileupload";//文件上传框
	public final static String htmlcode_itemselector="itemselector";//双栏选择框
	
	
	/**
	 * key_con条件定义类型
	 */
	public final static String keytype_date="date";//日期型
	public final static String keytype_like="like";//like
	public final static String keytype_common="common";//通用型
	public final static String keytype_nunulldate="nunulldate";//非空日期
	public final static String keytype_reform="reform";//组装
	public final static String keytype_cascade="cascade";//级联类型条件，存在级联状况，如果启用则修改QueryKeyImpl.java 中关于子key的判断代码
		
	/**
	 * 有效无效
	 */
	public final static String VALID_T = "T";
	public final static String VALID_F = "F";
	
	/**
	 * 维度key类型
	 */
	public final static String DIM_KEY_SUBSTR="SUBSTR";
	public final static String DIM_KEY_DATATREE="DATATREE";
	
	/**
	 * OLAP类型
	 */
	public final static String OLAP_TYPE_TOTAL="TOTAL";//对应列数据计算，合计
	public final static String OLAP_TYPE_GROUP="GROUP";//分组项目
	public final static String OLAP_TYPE_EXTEND="EXTEND";//列扩展
	
	/**
	 * 报表权限相关
	 */
	public final static String SESSION_REP_ROLE="rep_role";
	public final static String SESSION_SYSTEM_MAP="system_key_map";
	public final static String SESSION_REP_ROLE_DATA="rep_role_data";
	
	public final static String SESSION_DATA_ROLE="REP_DATA_ROLE";

	public final static String SESSION_REP_ROLE_DATA_KEY="#datarighttype#"; 
	
	/**
	 * 内存值列转换类型
	 */
	public final static String memory_vew_dict="vew_dict";//系统视图
	public final static String memory_my_define="my_define";//自定义
	
	/**
	 * 列模板转换
	 */
	public final static String template_type_fixed_sql="fixed_sql";//固定模板
	public final static String template_type_translate_sql="translate_sql";//sql转换模板
	
	/**
	 * 工作目录
	 */
	protected static final String FILE_SEPARATOR = System.getProperty("file.separator");
	public static String CONTEXT_REAL_PATH = getLastPackageParentPath(ReportConstants.class);
	public static String  REP_TEMP_TXT=CONTEXT_REAL_PATH+FILE_SEPARATOR+"TXT"+FILE_SEPARATOR;
	
	public static final String INDEX="_index";
	public final static String CACHE_BASE="_base";
	/**
	 * 获取一个类的最外层包的上级目录
	 * @param cls
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static String getLastPackageParentPath(final Class cls){
		//类加载器
		final ClassLoader clsLoader = cls.getClassLoader();
		//类路径
		final String clsAsResource = cls.getName().replace('.', '/').concat(".class");
		
		URL result=clsLoader.getResource(clsAsResource);
		String path=result.getPath();
		if ("jar".equalsIgnoreCase(result.getProtocol())) {
			//jar中的类处理
			try {
			     path = new URL(path).getPath();
			} catch (MalformedURLException e) {}
			int location = path.indexOf("!/");
		    if (location != -1) {
		     path = path.substring(0, location);
		    }
		    path=URLDecoder.decode(path);
		    path=new File(path).getParentFile().getParent();
		}else{
			//非jar中类处理
			int location = path.lastIndexOf(clsAsResource);
			if (location != -1) {
			     path = path.substring(0, location);
			 }
			path=URLDecoder.decode(path);
			path=new File(path).getParent();
		}			
		return path;
	}
	/**
	 * Excel 导出参数
	 */
	public static int WORKBOOKMAXROWS=100000;//excel最大行数
	public static int WORKBOOKMINROWS=10000;//excel最小行数
	public static int WHEETMAXROWS=10000;//分页最大行数
	public static int WHEETMINROWS=1000;//分页最大行数
	/**
	 * 报表基础配置项
	 */
	public final static String OPTRCONFIG_WORKBOOKMAXROWS="WORKBOOKMAXROWS";
	public final static String OPTRCONFIG_WHEETMAXROWS="WHEETMAXROWS";
	public final static String OPTRCONFIG_CLEAR_REP_CACHE="CLEAR_REP_CACHE";
	public final static String OPTRCONFIG_OPTR="OPTR";
	
	public static final String EXCELPOSTFIX=".xls";
	
	public static final String ZIPPOSTFIX=".zip";
	
	/**
	 * SQL拆分基准长度
	 */
	public static final Integer SQLBASELENGTH=2000;
	/**
	 * 报表编辑备注和查看SQL数据权限
	 */
	public static final String SDATARIGHTTYPE_ES="REPORTEDIT";
	/**
	 * redis缓存处理结果集时分页大小
	 */
	public static final int cache_page_row_max=1000;
	/**
	 * redis批量操作一次操作大小
	 */
	public static final int cache_batch_size=200;
	
	/**
	 * 多维计算CacheKey超过限额后，使用数据库排序的数据库提取键值
	 */
	public static final String DATABASETYPE_CUBE_SORT="cube_query_cachekey_sort_db";
	/**
	 * 多维报表内存计算限额(超过该值，将使用redis辅助运算，并使用oracle排序)
	 */
	public static final int memory_row_max=1000;
		
}
