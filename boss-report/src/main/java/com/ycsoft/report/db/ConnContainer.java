package com.ycsoft.report.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.report.bean.RepDatabase;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.dao.config.RepDatabaseDao;
/**
 * 连接池容器
 */
public class ConnContainer {

	private static Map<String, DataSource> dsmap = new HashMap<String, DataSource>();

	public static void init(RepDatabaseDao dbdao) throws ReportException {
		RepDatabase db = new RepDatabase();
		try {
			List<RepDatabase> dblist = dbdao.findAll();

			for (RepDatabase dbcfg : dblist) {
				db = dbcfg;
				if(!dbcfg.getType().equals(ReportConstants.DATABASETYPE_REALTIME)
						&&!dbcfg.getType().equals(ReportConstants.DATABASETYPE_HISTROY)){
					throw new ReportException("rep_datebase:"+dbcfg.getDatabase()
							+"的type无法识别!(正确类型为"+ReportConstants.DATABASETYPE_REALTIME+","+ReportConstants.DATABASETYPE_HISTROY+"请仔细检查)");
				}
				if (ReportConstants.DATABASE_SYSTEM.equals(dbcfg.getDatabase()))
					dsmap.put(ReportConstants.DATABASE_SYSTEM, dbdao.getDataSource());
				else {
					dsmap.put(dbcfg.getDatabase(), DataSourceUtil
							.configDataSource(dbcfg));
					LoggerHelper.info(ConnContainer.class,"database init:"+dbcfg.getDatabase());
				}
			}
			try{
				db=dbdao.getRepSortDB();
				if(db==null) throw new ReportException("装载多维计算用排序数据源错误：rep_sort_db无记录");
				dsmap.put(ReportConstants.DATABASETYPE_CUBE_SORT, DataSourceUtil.configDataSource(db));
				LoggerHelper.info(ConnContainer.class,"rep_sort_db init:"+ReportConstants.DATABASETYPE_CUBE_SORT);
			}catch(ReportException e1){
				throw e1;
			}catch(Exception e1){
				throw new ReportException("装载多维计算用排序数据源错误.请检查rep_sort_db表配置是否正确",e1);
			}
			
		} catch (Exception e) {
			throw new ReportException("装载报表数据源" + db.getDatabase() + "错误", e);
		}

	}

	public static Connection getConn(String database) throws SQLException {

		if(database==null||"".equals(database))
			return dsmap.get(ReportConstants.DATABASE_SYSTEM).getConnection();
		if (!dsmap.containsKey(database))
			throw new SQLException("database :"+database + " is no config.");
		return dsmap.get(database).getConnection();
	}

	public static void clossContainer() {
		Iterator<String> it = dsmap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			try {
				if (!ReportConstants.DATABASE_SYSTEM.equals(key))
					((ComboPooledDataSource) dsmap.get(key)).close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

class DataSourceUtil {

	public static DataSource configDataSource(RepDatabase cfg)
			throws PropertyVetoException {

		ComboPooledDataSource ds = new ComboPooledDataSource();

		ds.setDriverClass(cfg.getDriverclass().trim());
		// loads the jdbc driver
		ds.setJdbcUrl(cfg.getUrl().trim());
		ds.setUser(cfg.getUsername().trim());
		ds.setPassword(cfg.getPassword().trim());
		ds.setMaxStatements(20);//
		ds.setMaxIdleTime(cfg.getMaxidletime());// 最大空闲时间,60秒内未使用则连接被丢弃。若为0则永不丢弃
		ds.setMaxPoolSize(cfg.getMaxpoolsize());// 池最大连接数
		ds.setMinPoolSize(cfg.getMinpoolsize());// 池最小连接数

		// 检查有效性
		if(StringHelper.isNotEmpty(cfg.getTestquerysql())&&cfg.getTestoeriod()!=null){
			ds.setPreferredTestQuery(cfg.getTestquerysql());
			ds.setIdleConnectionTestPeriod(cfg.getTestoeriod());// 每隔10分钟检查一次空闲连接的有效性
		}
		
		//获取连接超时
		ds.setCheckoutTimeout(10000);

		return ds;
	}

	public static void closeDataSource(DataSource ds) throws SQLException {
		ComboPooledDataSource bds = (ComboPooledDataSource) ds;
		if (bds != null)
			bds.close();
		bds = null;
	}
}
