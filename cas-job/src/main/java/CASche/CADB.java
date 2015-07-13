package CASche;

import java.sql.*;

import CASche.help.LoggerHelper;
//import oracle.sql.*;

public class CADB {
		String dbHostIP;
		String dbPort;
		String dbService;
		String jdbcurl;
		String dbUserName;
		String dbPassword;
		private java.sql.Connection conn;
		private boolean connectInterruptFlag = false;
		
		public CADB(String dbHostIP, String dbPort,
				String dbService, String dbUserName,
				String dbPassword){
			this.dbHostIP = dbHostIP;
			this.dbPort = dbPort;
			this.dbService = dbService;
			this.dbUserName = dbUserName;
			this.dbPassword = dbPassword;
		}
		
		public CADB(String jdbcurl, String dbUserName, String dbPassword) {
			this.jdbcurl = jdbcurl;
			this.dbUserName = dbUserName;
			this.dbPassword = dbPassword;
		}

		public void setConnectInterruptFlag(boolean connectInterruptFlag)
		{
			this.connectInterruptFlag = connectInterruptFlag;
		}
		public boolean getConnectInterruptFlag()
		{
			return this.connectInterruptFlag;
		}
				
		public int connDB()
		{
			try{
				connectInterruptFlag = false;
				connectDatabase();
				conn.setAutoCommit(false);
				return 1;
			}catch(SQLException sqlEx){
				LoggerHelper.error(this.getClass(), "database_conn_error",sqlEx);
				if (checkSpecialErrorCode(sqlEx.getErrorCode())==-1){
					connectInterruptFlag = true;
				}
				return -1;
			}catch(Exception e){
				LoggerHelper.error(this.getClass(), "system_error",e);
				connectInterruptFlag = true;
				return -1;
			}
		}

		public int connectDatabase() throws SQLException,Exception
		{
			LoggerHelper.info(this.getClass(), "开始连接数据库!");
			connectInterruptFlag = false;
			try{
				if (conn != null && !conn.isClosed()){
					conn.close();
					conn = null;
				}
				Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
				conn = DriverManager.getConnection(jdbcurl, dbUserName, dbPassword);
				if (conn == null){
					connectInterruptFlag = true;
					return -1;
				}
				return 0;
			}catch(java.sql.SQLException e){
				throw e;
			} catch (Exception e) {
				throw e;
			}
		}

		public PreparedStatement prepareStatement(String sql){
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = conn.prepareStatement(sql);
			}
			catch(SQLException e){
				LoggerHelper.error(this.getClass(), "prepareStatement_create_error",e);
				
				if (checkSpecialErrorCode(e.getErrorCode()) == -1){
					connectInterruptFlag = true;
					closeConn();
				}
			}
			return preparedStatement;
		}

		public   Statement   statement()   {   
	          Statement   statement   =   null;   
	          try   {   
	              statement   =   conn.createStatement();   
	          }   
	          catch   (SQLException   e)   {   
	              e.printStackTrace();
	              if (checkSpecialErrorCode(e.getErrorCode()) == -1){
	            	  connectInterruptFlag = true;
	            	  closeConn();
	              }
	          }   
	          return   statement;   
	      }   

		public   int   commit()   {   
	          try   {   
	              conn.commit();
	              return 1;
	          }   
	          catch   (java.sql.SQLException   e)   {   
	        	  LoggerHelper.error(this.getClass(), "database_commit_error",e);
	              if (checkSpecialErrorCode(e.getErrorCode()) == -1){
	            	  connectInterruptFlag = true;
	            	  closeConn();
	              }
	              return -1;
	          }   
	      }   

		public   int   rollback()
		{   
	          try   {   
	              conn.rollback();
	              return 1;
	          }   
	          catch (java.sql.SQLException sqle) {
	              LoggerHelper.error(this.getClass(), "database_rollback_error",sqle);
	              if(checkSpecialErrorCode(sqle.getErrorCode()) == -1){
	            	  connectInterruptFlag = true;
	            	  closeConn();
	              }
	              return -1;
	            }
	      }   

		public void closeConn(){
			try {
				if (conn != null && !conn.isClosed()){
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	

		public int checkSpecialErrorCode(int errorCode)
		{
			if (errorCode==17002){
				//IO异常
				return -1;
			}
			else if (errorCode==17008){
				//关闭的连接
				return -1;
			}
			else if (errorCode==17009||errorCode==17010||errorCode==17015||errorCode==17016){
				//17009=关闭的语句;17010=关闭的 Resultset;17015=语句被取消;17016=语句超时
				return -1;
			}
			else if (errorCode>=18000||errorCode<17000){
				//其他错误，非oracle_jdbc定义错误
				return 0;
			}
			
			return 1;
		}
		
}
