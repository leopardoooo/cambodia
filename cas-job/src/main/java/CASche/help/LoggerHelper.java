package CASche.help;

import org.apache.commons.logging.LogFactory;

/**
 * 日志输出帮助类
 *
 * @author hh
 * @date Jan 6, 2010 11:14:27 AM
 */
public class LoggerHelper {

	/**
	 * debug output
	 * @param cls 样式
	 * @param msg 信息
	 */
	public static void debug( Class<?> cls , String msg ){
		LogFactory.getLog(cls).debug( msg );
	}

	/**
	 * info level output
	 * @param cls
	 * @param msg
	 */
	@SuppressWarnings("unchecked")
	public static void info( Class cls , String msg ){
		LogFactory.getLog(cls).info( msg );
	}

	/**
	 * error level output
	 * @param cls
	 * @param msg
	 */
	@SuppressWarnings("unchecked")
	public static void error( Class cls , String msg ){
		LogFactory.getLog(cls).error( msg );
	}

	/**
	 * warn level output
	 */
	@SuppressWarnings("unchecked")
	public static void warn( Class cls , String msg ){
		LogFactory.getLog(cls).warn( msg );
	}

	/**
	 * 采用默认的开始样式输出信息。默认级别:debug
	 * @param cls
	 * @param msg
	 */
	@SuppressWarnings("unchecked")
	public static void style(Class cls, String msg) {
		String style = msg;
		LogFactory.getLog(cls).debug(style);
	}

	/**
	 * 日志输出堆栈信息
	 * 
	 * @param cls
	 * @param msg
	 * @param e
	 */
	@SuppressWarnings("unchecked")
	public static void error(Class cls,String msg,Throwable e){
		LogFactory.getLog(cls).error(msg,e);
	}
	
	@SuppressWarnings("unchecked")
	public static boolean isDebugEnabled(Class cls){
		return LogFactory.getLog(cls).isDebugEnabled();
	}
}
