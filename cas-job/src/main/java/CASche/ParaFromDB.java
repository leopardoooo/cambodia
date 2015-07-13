package CASche;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class ParaFromDB implements ParaBase {

	CASchePara schePara = null;
	String user = null;
	String password = null;
	String service = null;
	String ip = null;
	String port = null;
	String jdbcurl = null;
	CADB db = null;
	
	public ParaFromDB(String ip, String port, String service, String user, String password)
	{
		this.ip = ip;
		this.port = port;
		this.user = user;
		this.password = password;
		this.service = service;
		this.db = new CADB(ip, port, service, user, password);
	}
	
	public ParaFromDB(String jdbcurl, String user, String password) {
		this.jdbcurl = jdbcurl;
		this.password = password;
		this.db = new CADB(jdbcurl, user, password);
	}

	public void setSchePara(CASchePara schePara)
	{
		this.schePara = schePara;
	}
	
	public CASchePara getSchePara()
	{
		return this.schePara;
	}

	public int GetPara(String processName) {
		schePara.getMapOrder().clear();
		schePara.getMapCAS().clear();
		
		if (db.connDB()<1){
			return -1;
		}
		
		String sql =  " SELECT para_name, para_value, pos from ("
			+ " SELECT para_name, para_value, 1 pos "
			+ " FROM busi.t_ca_interface "
			+ " WHERE program_type='CA_SCHE' and (para_name not like 'ORDER%' or para_name ='ORDER_COUNT') ";
		if (processName!= null && processName.equals("") == false){
			sql += " AND process_name='";
			sql += processName.toUpperCase();
			sql += "' ";
		}
		sql += "union ";
		sql += " SELECT para_name, para_value, 2 pos ";
		sql += " FROM busi.t_ca_interface ";
		sql	+= " WHERE program_type='CA_SCHE' and (para_name like 'ORDER%' and para_name <>'ORDER_COUNT') ";
		if (processName!= null && processName.equals("") == false){
			sql += " AND process_name='";
			sql += processName.toUpperCase();
			sql += "' ";
		} 
		sql += " ) order by 3";
		int count=0;
	    PreparedStatement statementSql = db.prepareStatement(sql);
	    try{
	    	ResultSet resultSet = statementSql.executeQuery();
	    	while(resultSet.next()){
	    		String paraName = resultSet.getString("para_name");
		
	    		if ("SRC_TABLE".equalsIgnoreCase(paraName) == true){
	    			schePara.setSrcTable(resultSet.getString("para_value"));
	    		}
	    		if ("SRC_TABLE_BAK".equalsIgnoreCase(paraName) == true){
	    			schePara.setSrcTableBak(resultSet.getString("para_value"));
	    		}
	    		if ("DST_TABLE".equalsIgnoreCase(paraName) == true){
	    			schePara.setDstTable(resultSet.getString("para_value"));
	    		}
	    		if ("DST_TABLE_BAK".equalsIgnoreCase(paraName) == true){
	    			schePara.setDstTableBak(resultSet.getString("para_value"));
	    		}
	    		if ("SEQ_DST_TRANSNUM".equalsIgnoreCase(paraName) == true){
	    			schePara.setSeqDstTransnum(resultSet.getString("para_value"));
	    		}
	    		
	    		if ("ORDER_COUNT".equalsIgnoreCase(paraName) == true){
	    			count = Integer.parseInt(resultSet.getString("para_value"));
	    			schePara.setOrderCount(count) ;
	    		}
	    		else{
	    			String paraName2 = paraName.toUpperCase().trim();
	    			int c = paraName2.charAt(paraName2.length()-1);
	    			if (paraName2.startsWith("ORDER") == true
	    					&& c >='0' && c <='9'){
	    				
	    				String no = paraName2.substring(5, paraName2.length());
	    				int ino = Integer.parseInt(no);
	    				if (ino <=count){
	    					schePara.getMapOrder().put(paraName2, resultSet.getString("para_value"));
	    				}
	    			}
	    		}
	    	}
	    	statementSql.close();   
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (db.checkSpecialErrorCode(e.getErrorCode()) == -1){
				db.setConnectInterruptFlag(true);
				db.closeConn();
				return -1;
			}
		}   

	    
	    sql = "SELECT tsp.server_id, tsp.para_name, tsp.para_value "
    		+ " FROM busi.t_server_parameter tsp "
    		+ " WHERE tsp.status='ZC' "
    		+ "  and program_type='CA_SCHE' ";
	    if (processName!= null && processName.equals("") == false){
    		sql += " and server_id in (select server_id from busi.t_ca_interface where process_name='";
    		sql += processName.toUpperCase();
    		sql += "' and program_type='CA_SCHE' ) ";
	    }
	    sql += " order by server_id ";
	    PreparedStatement statement = db.prepareStatement(sql);
	    String serverID = null;
	    String casType = null;
	    String casSQL = null;
	    int pool = 0;
	    String addMode = null;
	    String cancelMode= null;
	    CASPrivatePara caspp = null;
	    try{
	    	ResultSet resultSet = statement.executeQuery();
	    	while(resultSet.next()){
	    		if (serverID == null){
	    			serverID = resultSet.getString("server_id");
	    			casType = null;
    				casSQL = null;
    	        	pool = 0;
    	        	addMode = null;
    	        	cancelMode = null;
    	        	//caspp = new CASPrivatePara();
	    		}
	    		else if (serverID.equals(resultSet.getString("server_id")) == false){	    			
					caspp = new CASPrivatePara();
	    			caspp.setCasSQL(casSQL);
	    			caspp.setCasType(casType);
	    			caspp.setName(serverID);
	    			caspp.setPool(pool);
	    			caspp.setAddMode(addMode);
	    			caspp.setCancelMode(cancelMode);
				
	    			schePara.getMapCAS().put(serverID, caspp);
				
	    			serverID = resultSet.getString("server_id");
	    		}
    		
	    		String paraName = resultSet.getString("para_name");
    			
	    		if ("CAS_TYPE".equalsIgnoreCase(paraName) == true){
	    			casType = resultSet.getString("para_value");
	    		}
	    		if ("CAS_SQL".equalsIgnoreCase(paraName) == true){
	    			casSQL = resultSet.getString("para_value");
	    		}
	    		if ("POOL".equalsIgnoreCase(paraName) == true){
	    			String pools = resultSet.getString("para_value");
	    			pool = Integer.parseInt(pools);
	    		}
	    		if ("ADD_MODE".equalsIgnoreCase(paraName) == true){
	    			addMode = resultSet.getString("para_value");
	    		}
	    		if ("CANCEL_MODE".equalsIgnoreCase(paraName) == true){
	    			cancelMode = resultSet.getString("para_value");
	    		}
	    		
	    	}
	    	if (serverID != null){
	    		caspp = new CASPrivatePara();
    			caspp.setCasSQL(casSQL);
    			caspp.setCasType(casType);
    			caspp.setName(serverID);
    			caspp.setPool(pool);
    			caspp.setAddMode(addMode);
    			caspp.setCancelMode(cancelMode);
			
    			schePara.getMapCAS().put(serverID, caspp);	    		
	    	}
	    	statement.close();   
	    	return 1;
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(sql);
			if (db.checkSpecialErrorCode(e.getErrorCode()) == -1){
				db.setConnectInterruptFlag(true);
				db.closeConn();
				return -1;
			}
		}   

	    db.closeConn();
		return 1;
	}

}
