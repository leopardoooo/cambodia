package com.ycsoft.daos.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;

import com.ycsoft.commons.helper.LoggerHelper;

/**
 * 连接类，根据ClassPath下的jdbc.properties资源文件
 * 加载JDBC Connection对象
 *
 * @author hh
 * @date Feb 3, 2010 3:15:53 PM
 */
public final class ConnectionProvider {

	static final String JDBC_USER = "jdbc.username" ;
	static final String JDBC_PWD = "jdbc.password" ;
	static final String JDBC_URL = "jdbc.url" ;
	static final String JDBC_DRIVER = "jdbc.driver" ;

	static final String JDBC_FILE = "/jdbc.properties" ;

	/**
	 * 属性配置文件内存对象
	 */
	static Properties props = new Properties();

	static {
		InputStream is = ConnectionProvider.class.getResourceAsStream(JDBC_FILE);
		try {
			props.load(is);
		} catch (IOException e) {
			LoggerHelper.error(ConnectionProvider.class, "加载jdbc.properties失败!"
					+ e.getLocalizedMessage());
		}
	}

	/**
	 * 生成一个新的连接对象。
	 * @return
	 */
	public static Connection getConnection()throws Exception{
		Connection conn = null ;
		String driv = props.getProperty(JDBC_DRIVER);
		String url = props.getProperty(JDBC_URL);
		String user = props.getProperty(JDBC_USER);
		String pwd = props.getProperty(JDBC_PWD);
		Class.forName( driv );
		conn = DriverManager.getConnection(url, user, pwd);
		return conn ;
	}

	/**
	 * 关闭连接资源、会话对象、预处理对象
	 */
	public static void close(Connection conn ,
							 Statement state ,
							 PreparedStatement ps)throws Exception{
		if(state != null){
			state.close();
		}
		if(ps != null){
			ps.close();
		}
		if(conn != null){
			conn.close();
		}
	}

	/**
	 * 关闭连接资源、会话对象
	 */
	public static void close(Connection conn,
							 Statement state)throws Exception{
		close(conn , state, null);
	}

	/**
	 * 关闭连接资源、预编译对象
	 */
	public static void close(Connection conn,
							 PreparedStatement ps)throws Exception{
		close(conn , null , ps);
	}
}
