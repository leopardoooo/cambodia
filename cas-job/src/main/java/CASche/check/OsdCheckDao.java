package CASche.check;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 * Osd合法性验证查询
 * @author new
 *
 */
public class OsdCheckDao {
	
	/*
	 * 查询osd合法词组
	 */
	public Set<String> queryOsdPhrase(Statement stat) throws SQLException{
		ResultSet rs=null;
		Set<String> phrase_set=new HashSet<String>();
		try {
			rs=stat.executeQuery("select distinct t.phrase  from   t_osd_phrase t ");
			while(rs.next()){
				phrase_set.add(rs.getString(1));
			}
			return phrase_set;
		} catch (SQLException e) {
			throw e;
		}finally{
			if(rs!=null){
				try {rs.close();} catch (SQLException e) {}
			}
		}
	}
	/**
	 * 查询OSD强制终止配置是否有效
	 * @param conn
	 * @throws SQLException 
	 */
	public boolean queryOsdStop(Statement stat) throws SQLException{
		
		ResultSet rs=null;
		try {
			boolean _stop=false;
			rs=stat.executeQuery("select count(1) from t_osd_stop t where t.status='ACTIVE' and t.eff_end_time>=trunc(sysdate)");
			while(rs.next()){
				if(rs.getInt(1)>0){
					_stop=true;
				}
			}
			return _stop;
		} catch (SQLException e) {
			throw e;
		}finally{
			if(rs!=null){
				try {rs.close();} catch (SQLException e) {}
			}
		}
		
	}

}
