package CASche;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import CASche.CATools;

public class DealThread implements Runnable {
	CASPrivatePara caspp = null;
	
	CASchePara casp = null;
	String ip = null;
	String port = null;
	String service = null;
	String jdbcurl = null;
	String user = null;
	String password = null;
	String logPath = null;
	String prefix = null;
	CADB db = null;
	
	List<CACommand> listAddCmd = null;
	List<CACommand> listCancelCmd = null;
	List<Long> listTransnum = null;
	List<CACommand> listNewCmd = null;
	
	public DealThread()
	{
		listAddCmd = new ArrayList<CACommand>();
		listCancelCmd = new ArrayList<CACommand>();
		listTransnum = new ArrayList<Long>();
		listNewCmd = new ArrayList<CACommand>();
		
	}
	
	public void setLogPath(String logPath){
		this.logPath = logPath;
	}
	
	public String getLogPath(){
		return logPath;
	}
	
	public void setPrefix(String prefix){
		this.prefix = prefix;
	}
	
	public String getPrefix(){
		return this.prefix;
	}
	
	public CASPrivatePara getCaspp() {
		return caspp;
	}


	public void setCaspp(CASPrivatePara caspp) {
		this.caspp = caspp;
	}



	public CASchePara getCasp() {
		return casp;
	}



	public void setCasp(CASchePara casp) {
		this.casp = casp;
	}



	public String getIp() {
		return ip;
	}



	public void setIp(String ip) {
		this.ip = ip;
	}



	public String getPort() {
		return port;
	}



	public void setPort(String port) {
		this.port = port;
	}



	public String getService() {
		return service;
	}



	public void setService(String service) {
		this.service = service;
	}

	public String getJdbcurl() {
		return jdbcurl;
	}



	public void setJdbcurl(String jdbcurl) {
		this.jdbcurl = jdbcurl;
	}


	public String getUser() {
		return user;
	}



	public void setUser(String user) {
		this.user = user;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}


	public int MoveRecordsFromDstTableToDstTableBak()
	{
		System.out.println("======》开始 从out 移到 out_bak 表");
		Statement querySql = null;
		ResultSet resultSet = null;	
		int count = 0;

		String sql = " SEL/Users/killer/Downloads/workspace_ly/CASche/src/CASche/CATools.javaECT COUNT(*) cnt FROM " + casp.getDstTable() + " WHERE is_sent='Y' ";
		sql += caspp.getCasSQL();

		querySql = db.statement();
		resultSet = null;
		try {
			int i = 1;
			resultSet = querySql.executeQuery(sql);
			while (resultSet.next()) {
				count = resultSet.getInt(1);
				break;
			}
			resultSet.close();
			querySql.close();
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("sql=[" + sql + "]");
			try {
				querySql.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			CATools.sleeping(10);
			if (db.checkSpecialErrorCode(e.getErrorCode()) == -1) {
				db.setConnectInterruptFlag(true);
				db.closeConn();
				return -1;
			}
		}

		if (count <= 5){
//			System.out.println("======》判断数量少于5，返回");
			return 1;
			
		}
			
		//sql = " UPDATE " + casp.getDstTable() + " SET move_flag='A'  WHERE is_sent='Y' ";
		sql = " UPDATE " + casp.getDstTable() + " SET is_sent='A'  WHERE is_sent='Y' ";
		sql += caspp.getCasSQL();
		PreparedStatement   update   =   db.prepareStatement(sql);
		try {
			update.executeUpdate();
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(db.checkSpecialErrorCode(e.getErrorCode()) == -1){
				db.setConnectInterruptFlag(true);
				db.closeConn();
				try {
					update.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				CATools.sleeping(5);
				return -1;
			}
		}
		
		try {
			update.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		sql = "INSERT INTO " + casp.getDstTableBak() +  
		" SELECT TRANSNUM,JOB_ID,CAS_ID,CAS_TYPE,USER_ID,CUST_ID,DONE_CODE,CMD_TYPE "+
		" ,STB_ID,CARD_ID,PRG_NAME,BOSS_RES_ID,CONTROL_ID,AUTH_BEGIN_DATE "+
		",AUTH_END_DATE,result_flag,ERROR_INFO,AREA_ID,'Y',RECORD_DATE "+
		" ,send_date,DETAIL_PARAMS,PRIORITY FROM "+casp.getDstTable();
 		sql += " WHERE is_sent='A' ";
		sql += caspp.getCasSQL();
		update   =   db.prepareStatement(sql);
		try {
			update.executeUpdate();
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(db.checkSpecialErrorCode(e.getErrorCode()) == -1){
				db.setConnectInterruptFlag(true);
				db.closeConn();
				
				try {
					update.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				CATools.sleeping(5);
				return -1;
			}
		}
			
		try {
			update.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

//		sql = "DELETE FROM " + casp.getDstTable() + " WHERE MOVE_FLAG='A' AND is_sent='Y' ";
		sql = "DELETE FROM " + casp.getDstTable() + " WHERE is_sent='A' ";
		sql += caspp.getCasSQL();
		update   =   db.prepareStatement(sql);
		try {
			update.executeUpdate();
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(db.checkSpecialErrorCode(e.getErrorCode()) == -1){
				db.setConnectInterruptFlag(true);
				db.closeConn();
				try {
					update.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				CATools.sleeping(5);
				return -1;
			}
		}

		int ret = db.commit();
		if (ret < 1){
			return -1;
		}
		
		try {
			update.close();
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(db.checkSpecialErrorCode(e.getErrorCode()) == -1){
					db.setConnectInterruptFlag(true);
					db.closeConn();
					return -1;
				}
		}
		System.out.println("======》结束 从out 移到 out_bak 表");
		return 1;
	}

	public int GetRecordsOfDstTable()
	{
//		System.out.println("======》开始 查询out表未发送记录数");
		int count = 0;
		Statement querySql = null;
		ResultSet resultSet = null;

		String sql = " SELECT COUNT(*) cnt FROM " + casp.getDstTable() + " WHERE is_sent in ('1','N') ";
		sql += caspp.getCasSQL();

		querySql = db.statement();
		resultSet = null;
		try {
			int i = 1;
			resultSet = querySql.executeQuery(sql);
			while (resultSet.next()) {
				count = resultSet.getInt(1);
				break;
			}
			resultSet.close();
			querySql.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("sql=[" + sql + "]");
			try {
				querySql.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			if (db.checkSpecialErrorCode(e.getErrorCode()) == -1) {
				db.setConnectInterruptFlag(true);
				db.closeConn();
				return -1;
			}
	    }
//		System.out.println("======》结束 out表未发送记录数：" + count);
		return count;
	}
	
	public int getIndex(int index)
	{
		if (index > casp.getOrderCount()){
			CATools.sleeping(2);
			return 1;
		}
		return index;
	}
	
	public String getSrcSQL( int index)
	{
		String srcSQL = null;
		srcSQL = " SELECT * FROM " + casp.getSrcTable() ;
		srcSQL += " WHERE transnum=(select min(transnum) from " + casp.getSrcTable() + " where " + casp.getMapOrder().get("ORDER"+index);
		srcSQL += caspp.getCasSQL();
		srcSQL += " ) ";
		//System.out.println("======》开始获取最小序列号：");
		return srcSQL;
	}
	
	public long getSeqDstTransnum()
	{
		long seq = 0;
		Statement querySql = null;
		ResultSet resultSet = null;

		String sql = " SELECT " + casp.getSeqDstTransnum() + ".nextval from dual ";

		querySql = db.statement();
		resultSet = null;
		try {
			int i = 1;
			resultSet = querySql.executeQuery(sql);
			while (resultSet.next()) {
				seq = resultSet.getLong(1);
				break;
			}
			resultSet.close();
			querySql.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("sql=[" + sql + "]");
			try {
				querySql.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			CATools.sleeping(10);
			if (db.checkSpecialErrorCode(e.getErrorCode()) == -1) {
				db.setConnectInterruptFlag(true);
				db.closeConn();
				return -1;
			}
		}
		return seq;
	}
	
	
	public int insertIntoDstTable(long transnum, String userID, String custID, String casID, String casType,
			String cmdType, String stbID, String cardID, String controlID, String authBeginDate, String authEndDate,
			String isSent, String recordDate, String detailParams, String areaID)
	{
//		System.out.println("======》开始插入out 表");
		String sql = null;
		sql = "INSERT INTO " + casp.getDstTable() + " (transnum, user_id, cust_id, cas_id, cas_type, " ;
		sql += " cmd_type, stb_id, card_id, control_id, auth_begin_date, auth_end_date, ";
		sql += " is_sent, record_date, detail_params, area_id) ";
		sql += " values( ?, ?, ?, ?, ?, ";
		sql += " ?, ?,  ?, ?,  ?, ?, ";
		sql += "  ?, SYSDATE,  ?, ? ) ";
			
		PreparedStatement update = db.prepareStatement(sql);
		try {
			update.setLong(1, transnum);
			update.setString(2, userID);
			update.setString(3, custID);
			update.setString(4, casID);
			update.setString(5, casType);
			update.setString(6, cmdType);
			update.setString(7, stbID);
			update.setString(8, cardID);
			update.setString(9, controlID);
			update.setString(10, authBeginDate);
			update.setString(11, authEndDate);
			update.setString(12, isSent);
			update.setString(13, detailParams);
			update.setString(14, areaID);
			update.executeUpdate();
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("card_id=" + cardID + " stb_id=" + stbID + "detailparams=" + detailParams);
			if(db.checkSpecialErrorCode(e.getErrorCode()) == -1){
				db.setConnectInterruptFlag(true);
				db.closeConn();
					
				try {
					update.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				CATools.sleeping(5);
				return -1;
			}
		}
			
		try {
			update.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}
//		System.out.println("======》结束插入out 表");
		return 1;
	}
	
	public int 	insertIntoSrcTableBakAndDeleteFromSrcTable(long transnum)
	{
//		System.out.println("======》开始从cmd 表 移到 day  表");
		String sql = null;
		sql = "INSERT INTO " + casp.getSrcTableBak() + 
		" SELECT TRANSNUM,JOB_ID,CAS_ID,CAS_TYPE,USER_ID,CUST_ID,DONE_CODE,CMD_TYPE "+
		" ,STB_ID,CARD_ID,PRG_NAME,BOSS_RES_ID,CONTROL_ID,AUTH_BEGIN_DATE"+
		" ,AUTH_END_DATE,'正确',ERROR_INFO,AREA_ID,'Y',RECORD_DATE "+
		" ,sysdate,DETAIL_PARAMS,PRIORITY FROM "+ casp.getSrcTable() + " WHERE TRANSNUM=?";
		
		//		System.out.println("======》开始从cmd 表 移到 day  表："+sql);
		PreparedStatement update = db.prepareStatement(sql);
		try {
			update.setLong(1, transnum);
			update.executeUpdate();
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(db.checkSpecialErrorCode(e.getErrorCode()) == -1){
				db.setConnectInterruptFlag(true);
				db.closeConn();
					
				try {
					update.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				CATools.sleeping(5);
				return -1;
			}
		}
			
		try {
			update.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}
			
		sql = " DELETE FROM " + casp.getSrcTable() + " WHERE TRANSNUM=? ";
//		System.out.println("======》从cmd 表删除");
		update = db.prepareStatement(sql);
		try {
			update.setLong(1, transnum);
			update.executeUpdate();
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(db.checkSpecialErrorCode(e.getErrorCode()) == -1){
				db.setConnectInterruptFlag(true);
				db.closeConn();
								try {
					update.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				CATools.sleeping(5);
				return -1;
			}
		}
			
		try {
			update.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}
//		System.out.println("======》结束从cmd 表移到 day 表");
	    return 1;
	}
    
	public int DoProductCmd(String stbID, String cardID, String cmdType, long jobID)
	{
		System.out.println("开始合并指令：机顶盒："+stbID + "智能卡："+cardID +"指令类型："+cmdType+ "jobID"+jobID);
		boolean stbIDNull = false;
		if (stbID==null || "".equalsIgnoreCase(stbID) == true){
			stbIDNull = true;
		}
		boolean cardIDNull = false;
		if (cardID==null || "".equalsIgnoreCase(stbID) == true){
			cardIDNull = true;
		}
		PreparedStatement querySql2 = null;
		ResultSet resultSet2 = null;
		String sql = " SELECT * FROM " + casp.getSrcTable() + " WHERE ";
//		String sql = " SELECT cas_id, cas_type, stb_id, card_id, cmd_type,control_id, auth_begin_date, auth_end_date, detail_params FROM " + casp.getSrcTable() + " WHERE ";
//		String sql = " SELECT cas_type FROM " + casp.getSrcTable() + " WHERE ";
		if (stbIDNull==true){
			sql += " stb_id is null " ;
		}
		else{
			sql += " stb_id=? ";
		}

		if (cardIDNull==true){
			sql += " and card_id is null " ;
		}
		else{
			sql += " and card_id=? " ;
		}

		sql += " and cmd_type in ('CancelProduct', 'AddProduct') ";
		sql += " and is_sent='N'  ";
		sql += caspp.getCasSQL();
		//sql += " order by control_id asc, record_date desc, transnum desc ";
		sql += " order by transnum asc ";
		System.out.println("开始合并指令：");
    	String controlID2 = "";
    	String cmdType2 = "";
    	String authBeginDate2 = "";
    	String authEndDate2 = "";
    	String casID2 = "";
    	String casType2 = "";
    	String detailParams = "";
	    	
		querySql2 = db.prepareStatement(sql);
		resultSet2 = null;
		try {
			if (stbIDNull == false){
				querySql2.setString(1, stbID);
			}
			if (cardIDNull == false){
				if (stbIDNull == false){
					querySql2.setString(2, cardID);
				}
				else{
					querySql2.setString(1, cardID);
				}
			}
			
		//	querySql2.setString(1, cardID);
//			System.out.println("开始合并指令：查询："+sql);
			resultSet2 = querySql2.executeQuery();
			CACommand cmd = null;
			while (resultSet2.next()) {
				String controlID3 = resultSet2.getString("CONTROL_ID");
				String cmdType3 = resultSet2.getString("cmd_type");
				String authBeginDate3 = resultSet2.getString("AUTH_BEGIN_DATE");
				String authEndDate3 = resultSet2.getString("AUTH_END_DATE");
				
				if ("ADDPRODUCT".equalsIgnoreCase(cmdType3) == false 
					&& 	"CANCELPRODUCT".equalsIgnoreCase(cmdType3) == false	){
					break;
				}

				if (listNewCmd.size()>=31){
					break;
				}
				
				int i=0;
				for (i=0; i<listNewCmd.size(); i++){
					if (listNewCmd.get(i).controlID.equals(controlID3) == true ){
						listNewCmd.get(i).setCmdType(cmdType3);
						listNewCmd.get(i).setAuthBeginDate(authBeginDate3);
						listNewCmd.get(i).setAuthEndDate(authEndDate3);
						break;
					}
				}
				if (i == listNewCmd.size()){
					CACommand newCmd  = new CACommand();
					newCmd.setUserID(resultSet2.getString("USER_ID"));
					newCmd.setCustID(resultSet2.getString("CUST_ID"));
					newCmd.setCasID(resultSet2.getString("CAS_ID"));
					newCmd.setCasType(resultSet2.getString("CAS_TYPE"));
					newCmd.setCmdType(cmdType3);
					newCmd.setControlID(controlID3);
					newCmd.setStbID(resultSet2.getString("STB_ID"));
					newCmd.setCardID(resultSet2.getString("CARD_ID"));
					newCmd.setDetailParams("");
					newCmd.setAreaID(resultSet2.getString("AREA_ID"));
					newCmd.setAuthBeginDate(authBeginDate3);
					newCmd.setAuthEndDate(authEndDate3);
					newCmd.setCount(1);
					listNewCmd.add(newCmd);
				}

				listTransnum.add(resultSet2.getLong("TRANSNUM"));
			}
			System.out.println("开始合并指令：查询指令数量："+listNewCmd.size());
			
			resultSet2.close();
			querySql2.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				querySql2.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(sql);
			e.printStackTrace();
			CATools.sleeping(10);
			if (db.checkSpecialErrorCode(e.getErrorCode()) == -1) {
				db.setConnectInterruptFlag(true);
				db.closeConn();
				return -1;
			}
		}

		int i=0;
		for (i=0; i<listNewCmd.size(); i++){
			String cmdType4 = listNewCmd.get(i).getCmdType();
			if ("ADDPRODUCT".equalsIgnoreCase(cmdType4) == true){
				if (caspp.getAddMode().equalsIgnoreCase(CASPrivatePara.MODE_NOTSUPPORT) == true){
					listAddCmd.add(listNewCmd.get(i));
				}
				else if (caspp.getAddMode().equalsIgnoreCase(CASPrivatePara.MODE_SUPPORT) == true){
					if (listAddCmd.size()==0){
						listNewCmd.get(i).setDetailParams("CTRL1:'" + listNewCmd.get(i).getControlID() +"';");
						listAddCmd.add(listNewCmd.get(i));
					}
					else{
						CACommand cmdYes = listAddCmd.get(0);
						cmdYes.setCount(cmdYes.getCount()+1);
						if (cmdYes.getAuthBeginDate().equals(listNewCmd.get(i).getAuthBeginDate()) == true
							&& cmdYes.getAuthEndDate().equals(listNewCmd.get(i).getAuthEndDate()) == true){
							String dp = cmdYes.getDetailParams();
							dp += "CTRL" + cmdYes.getCount() + ":'";
							dp += listNewCmd.get(i).getControlID();
							dp += "';";
							cmdYes.setDetailParams(dp);
						}
						else{
							String dp = cmdYes.getDetailParams();
							dp += "CTRL" + cmdYes.getCount() + ":'";
							dp += listNewCmd.get(i).getControlID();
							dp += "';";
							dp += "BD" + cmdYes.getCount() + ":'";
							dp += listNewCmd.get(i).getAuthBeginDate();
							dp += "';";
							dp += "ED" + cmdYes.getCount() + ":'";
							dp += listNewCmd.get(i).getAuthEndDate();
							dp += "';";
							cmdYes.setDetailParams(dp);
						}
					}
				}//else if (caspp.getAddMode()==CASPrivatePara.MODE_SUPPORT){
				else if (caspp.getAddMode().equalsIgnoreCase(CASPrivatePara.MODE_SAMETIME) == true){
					if (listAddCmd.size()==0){
						listNewCmd.get(i).setDetailParams("CTRL1:'" + listNewCmd.get(i).getControlID() +"';");
						listAddCmd.add(listNewCmd.get(i));
					}
					else{
						int len = listAddCmd.size();
						int j=0;
						for (j=0; j<len; j++){
						   if (listAddCmd.get(j).getAuthBeginDate().equals(listNewCmd.get(i).getAuthBeginDate()) == true
								   && listAddCmd.get(j).getAuthEndDate().equals(listNewCmd.get(i).getAuthBeginDate()) == true){
							   listAddCmd.get(j).setCount(listAddCmd.get(j).getCount()+1);
							   String dp = listAddCmd.get(j).getDetailParams();
							   dp += "CTRL"+(listAddCmd.get(j).getCount())+":'";
							   dp += listNewCmd.get(i).getControlID();
							   dp += "';";
							   listAddCmd.get(j).setDetailParams(dp);
							   break;
						   }
						} //for (int j=0; j<len; j++){
						
						if( j==len){
							listNewCmd.get(i).setDetailParams("CTRL1:'" + listNewCmd.get(i).getControlID() +"';");
							listAddCmd.add(listNewCmd.get(i));							
						}
					}
				} //else if (caspp.getAddMode()==CASPrivatePara.MODE_SAMETIME){

				
			}
			
			else if ("CANCELPRODUCT".equalsIgnoreCase(cmdType4) == true){
				if (caspp.getAddMode().equalsIgnoreCase(CASPrivatePara.MODE_NOTSUPPORT) == true){
					listCancelCmd.add(listNewCmd.get(i));
				}
				else if (caspp.getAddMode().equalsIgnoreCase(CASPrivatePara.MODE_SUPPORT) == true){
					if (listCancelCmd.size()==0){
						listNewCmd.get(i).setDetailParams("CTRL1:'" + listNewCmd.get(i).getControlID() +"';");
						listCancelCmd.add(listNewCmd.get(i));
					}
					else{
						CACommand cmdYes = listCancelCmd.get(0);
						cmdYes.setCount(cmdYes.getCount()+1);
						if (cmdYes.getAuthBeginDate().equals(listNewCmd.get(i).getAuthBeginDate()) == true
							&& cmdYes.getAuthEndDate().equals(listNewCmd.get(i).getAuthEndDate()) == true){
							String dp = cmdYes.getDetailParams();
							dp += "CTRL" + cmdYes.getCount() + ":'";
							dp += listNewCmd.get(i).getControlID();
							dp += "';";
							cmdYes.setDetailParams(dp);
						}
						else{
							String dp = cmdYes.getDetailParams();
							dp += "CTRL" + cmdYes.getCount() + ":'";
							dp += listNewCmd.get(i).getControlID();
							dp += "';";
							dp += "BD" + cmdYes.getCount() + ":'";
							dp += listNewCmd.get(i).getAuthBeginDate();
							dp += "';";
							dp += "ED" + cmdYes.getCount() + ":'";
							dp += listNewCmd.get(i).getAuthEndDate();
							dp += "';";
							cmdYes.setDetailParams(dp);
						}
					}
				}//else if (caspp.getAddMode()==CASPrivatePara.MODE_SUPPORT){
				else if (caspp.getAddMode().equalsIgnoreCase(CASPrivatePara.MODE_SAMETIME) == true){
					if (listCancelCmd.size()==0){
						listNewCmd.get(i).setDetailParams("CTRL1:'" + listNewCmd.get(i).getControlID() +"';");
						listCancelCmd.add(listNewCmd.get(i));
					}
					else{
						int len = listCancelCmd.size();
						int j=0;
						for (j=0; j<len; j++){
						   if (listCancelCmd.get(j).getAuthBeginDate().equals(listNewCmd.get(i).getAuthBeginDate()) == true
								   && listCancelCmd.get(j).getAuthEndDate().equals(listNewCmd.get(i).getAuthBeginDate()) == true){
							   listCancelCmd.get(j).setCount(listCancelCmd.get(j).getCount()+1);
							   String dp = listCancelCmd.get(j).getDetailParams();
							   dp += "CTRL"+(listCancelCmd.get(j).getCount())+":'";
							   dp += listNewCmd.get(i).getControlID();
							   dp += "';";
							   listCancelCmd.get(j).setDetailParams(dp);
							   break;
						   }
						} //for (int j=0; j<len; j++){
						
						if( j==len){
							listNewCmd.get(i).setDetailParams("CTRL1:'" + listNewCmd.get(i).getControlID() +"';");
							listCancelCmd.add(listNewCmd.get(i));							
						}
					}
				} //else if (caspp.getAddMode()==CASPrivatePara.MODE_SAMETIME){

				
			}

			
		}

		
		int len = listAddCmd.size();
		for (i=0; i<len; i++){
			if (listAddCmd.get(i).getCount()>1){
				String dp = listAddCmd.get(i).getDetailParams();
				String dp2 = "CNT:'" + listAddCmd.get(i).getCount() + "';";
				dp2 += dp;
				listAddCmd.get(i).setDetailParams(dp2);
			}
		}
		len = listCancelCmd.size();
		for (i=0; i<len; i++){
			if (listCancelCmd.get(i).getCount()>1){
				String dp = listCancelCmd.get(i).getDetailParams();
				String dp2 = "CNT:'" + listCancelCmd.get(i).getCount() + "';";
				dp2 += dp;
				listCancelCmd.get(i).setDetailParams(dp2);
			}
		}
		
		if (dealList() < 0)
			return -1;
		
		return 1;
	}
	
	public int dealList()
	{
		boolean haveRecord = false;
		
		int len = listAddCmd.size();
		for (int i=0; i<len; i++){
			haveRecord = true;
			CACommand cmd = listAddCmd.get(i);
			long transnum = getSeqDstTransnum();
			if (cmd.getCount()>1){
				
				if (insertIntoDstTable(transnum, cmd.getUserID(), cmd.getCustID(), cmd.getCasID(), cmd.getCasType(),
						cmd.getCmdType(), cmd.getStbID(), cmd.getCardID(), 
						"", cmd.getAuthBeginDate(), cmd.getAuthEndDate(),
						"N", "", cmd.getDetailParams(), cmd.getAreaID()) < 0){
					return -1;
				}
			}
			else{
				if (insertIntoDstTable(transnum, cmd.getUserID(), cmd.getCustID(), cmd.getCasID(), cmd.getCasType(),
						cmd.getCmdType(), cmd.getStbID(), cmd.getCardID(), 
						cmd.getControlID(), cmd.getAuthBeginDate(), cmd.getAuthEndDate(),
						"N", "", cmd.getDetailParams(), cmd.getAreaID()) < 0){
					return -1;
				}
			}
		}
		
		len = listCancelCmd.size();
		for (int i=0; i<len; i++){
			haveRecord = true;
			CACommand cmd = listCancelCmd.get(i);
			long transnum = getSeqDstTransnum();
			if (cmd.getCount()>1){
				
				if (insertIntoDstTable(transnum, cmd.getUserID(), cmd.getCustID(), cmd.getCasID(), cmd.getCasType(),
						cmd.getCmdType(), cmd.getStbID(), cmd.getCardID(), 
						"", cmd.getAuthBeginDate(), cmd.getAuthEndDate(),
						"N", "", cmd.getDetailParams(), cmd.getAreaID()) < 0){
					return -1;
				}
			}
			else{
				if (insertIntoDstTable(transnum, cmd.getUserID(), cmd.getCustID(), cmd.getCasID(), cmd.getCasType(),
						cmd.getCmdType(), cmd.getStbID(), cmd.getCardID(), 
						cmd.getControlID(), cmd.getAuthBeginDate(), cmd.getAuthEndDate(),
						"N", "", cmd.getDetailParams(), cmd.getAreaID()) < 0){
					return -1;
				}
			}
		}

		len = listTransnum.size();
		for (int i=0; i<len; i++){
			haveRecord = true;
			Long Ltransnum = listTransnum.get(i);
			if (insertIntoSrcTableBakAndDeleteFromSrcTable(Ltransnum.longValue()) < 0){
				return -1;
			}
		}
		
		if (haveRecord == true){
			if (db.commit() < 1){
				return -1;
			}
		}
		
		listAddCmd.clear();
		listCancelCmd.clear();
		listTransnum.clear();
		listNewCmd.clear();
		
		return 1;
	}
	
	
	public void run() {
		System.out.println("======》开始调度指令信息========>");
		//db = new CADB(ip, port, service, user, password);
		db = new CADB(jdbcurl, user, password);
		while (db.connDB()<1){
			System.out.println("======》数据库连接错误");
			CATools.sleeping(10);
		}
		
		int index = 1;
		boolean startFlag = false;
		while(true){
			startFlag = false;
			
			if (db.getConnectInterruptFlag() == true){
				while(db.getConnectInterruptFlag() == true){
			 		db.connDB();
			 		CATools.sleeping(5);
			 	}
			}
			
			if (dealList() < 0){
				startFlag = true;
				continue;
			}
			
			index = 1;
			//move cmd record from dst_table to dst_table_bak;
			if (MoveRecordsFromDstTableToDstTableBak() < 0){
				startFlag = true;
				continue;
			}

			//check the undo records of the dst_table
			int cnt = GetRecordsOfDstTable();
			if (cnt >= caspp.getPool()){
				CATools.sleeping(5);
				continue;
			}
			int iCount = 0;

			while (cnt < caspp.getPool()){
				Statement querySql = null;
				ResultSet resultSet = null;

				iCount = 0;
				querySql = db.statement();
				resultSet = null;
				try {
					index = getIndex(index);
//					System.out.println("======》查询需要发送指令");
					resultSet = querySql.executeQuery(getSrcSQL(index));
					while (resultSet.next()) {
						iCount++;
						String cmdType = resultSet.getString("CMD_TYPE");

						if ("ADDPRODUCT".equalsIgnoreCase(cmdType)==true
							|| "CANCELPRODUCT".equalsIgnoreCase(cmdType) == true){
							System.out.println("======》处理合并指令："+resultSet.getString("card_id")+cmdType);
							if (DoProductCmd(resultSet.getString("stb_id"), resultSet.getString("card_id"), resultSet.getString("cmd_type"), resultSet.getLong("job_id")) < 0){
								startFlag = true;
								System.out.println("======》获取需要处理指令数小于0 退出： "+resultSet.getString("card_id")+cmdType);
								break;
							}
						}
						else{
							System.out.println("======》处理其他类型指令："+resultSet.getString("card_id")+cmdType);
							long transnum = getSeqDstTransnum();
								
							//Insert into dst_table
							if (insertIntoDstTable(transnum, resultSet.getString("USER_ID"), resultSet.getString("CUST_ID"), resultSet.getString("CAS_ID"), resultSet.getString("CAS_TYPE"),
										resultSet.getString("CMD_TYPE"), resultSet.getString("STB_ID"), resultSet.getString("CARD_ID"), 
										resultSet.getString("CONTROL_ID"), resultSet.getString("AUTH_BEGIN_DATE"), resultSet.getString("AUTH_END_DATE"),
										"N", "", resultSet.getString("DETAIL_PARAMS"), resultSet.getString("AREA_ID")) < 0){
								startFlag = true;
								break;
							}
								
							//Insert into src_table_bak; and delete from src_table
							if (insertIntoSrcTableBakAndDeleteFromSrcTable(resultSet.getLong("TRANSNUM")) < 0){
								startFlag = true;
								break;
							}
							
							if (db.commit() < 1){
								startFlag = true;
								break;
							}
						} // else{
						
					} //while (resultSet.next()) {
					resultSet.close();
					querySql.close();
					if (startFlag == true){
						break;
					}
				}catch (SQLException e) {
					// TODO Auto-generated catch block
					
					try {
						querySql.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
					CATools.sleeping(10);
					if (db.checkSpecialErrorCode(e.getErrorCode()) == -1) {
						db.setConnectInterruptFlag(true);
						db.closeConn();

						startFlag = true;
						break;
					}
				} //}catch (SQLException e) {
		
				if (startFlag == true){
					break;
					
				}
				if (iCount==0){
					index++;
				}
				else{
					cnt++;
				}
			} //while (cnt < caspp.getPool()){

		} //while(true){
	} // public void run() 

}
