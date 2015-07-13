package CASche.check;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import CASche.CADB;
import CASche.CATools;
import CASche.common.CaConstants;
import CASche.help.LoggerHelper;

/**
 * osd合法验证组件
 * @author new
 *
 */
public class OsdCheckRefresh implements Runnable{

	private static Set<String> phrases=new HashSet<String>();
	
	private static boolean osd_stop=false;
	
	private static int refresh_time=600;
	
	private CADB db=null;
	private OsdCheckDao checkdao=new OsdCheckDao();
	
	private Statement	stat=null;
	
	public OsdCheckRefresh(CADB db){
		this.db=db;
		this.db.connDB();
	}
	
	public void refresh(){
		try{
			//数据库重连
			if (db.getConnectInterruptFlag() == true){
				while(db.getConnectInterruptFlag() == true){
			 		db.connDB();
			 		CATools.sleeping(CaConstants.free_sleep_time);
			 	}
			}
			if(stat==null){
				stat= this.db.statement();
			}
			this.phrases=checkdao.queryOsdPhrase(stat);
			this.osd_stop=checkdao.queryOsdStop(stat);
			LoggerHelper.info(this.getClass(), "同步OSD合法检查配置数据");
		}catch(SQLException e){
			if(stat!=null){
				try {stat.close();} catch (SQLException e1) {}
				stat=null;
			}
			if (db.checkSpecialErrorCode(e.getErrorCode()) == -1) {
				db.setConnectInterruptFlag(true);
				db.closeConn();
			}
		}catch(Exception e){
			if(LoggerHelper.isDebugEnabled(this.getClass())){
				LoggerHelper.debug(this.getClass(),e.getMessage());	
			}
			if(stat!=null){
				try {stat.close();} catch (SQLException e1) {}
				stat=null;
			}
			db.setConnectInterruptFlag(true);
			db.closeConn();
		}
	}
	
	public void run() {
		while(true){
			try{
				Thread.sleep(this.refresh_time*1000);
				this.refresh();
			}catch(Exception e){
				if(LoggerHelper.isDebugEnabled(this.getClass())){
					LoggerHelper.debug(this.getClass(),e.getMessage());	
				}
			}
		}
		
	}

	public static Set<String> getPhrases() {
		return phrases;
	}

	public static boolean isOsd_stop() {
		return osd_stop;
	}

	public static void setRefresh_time(int refresh_time) {
		OsdCheckRefresh.refresh_time = refresh_time;
	}

	public static void setPhrases(Set<String> phrases) {
		OsdCheckRefresh.phrases = phrases;
	}
	
}
