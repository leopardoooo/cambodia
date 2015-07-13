package CASche;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import CASche.CATools;

public class DealThread_bak implements Runnable {
	CASPrivatePara caspp = null;
	
	CASchePara casp = null;
	String ip = null;
	String port = null;
	String service = null;
	String user = null;
	String password = null;
	String logPath = null;
	String prefix = null;
	CADB db = null;
	
	List<CACommand> listAddCmd = null;
	List<CACommand> listCancelCmd = null;
	List<Long> listTransnum = null;
	
	public DealThread_bak()
	{
		listAddCmd = new ArrayList<CACommand>();
		listCancelCmd = new ArrayList<CACommand>();
		listTransnum = new ArrayList<Long>();
		
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
		Statement querySql = null;
		ResultSet resultSet = null;	
		int count = 0;

		String sql = " SELECT COUNT(*) cnt FROM " + casp.getDstTable() + " WHERE is_sent='Y' ";
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
		
		sql = "INSERT INTO " + casp.getDstTableBak() + " SELECT * FROM " + casp.getDstTable();
//		sql += " WHERE MOVE_FLAG='A' AND is_sent='Y' ";
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

		return 1;
	}

	public int GetRecordsOfDstTable()
	{
		int count = 0;
		Statement querySql = null;
		ResultSet resultSet = null;

		String sql = " SELECT COUNT(*) cnt FROM " + casp.getDstTable() + " WHERE is_sent<>'Y' ";
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
		return 1;
	}
	
	public int 	insertIntoSrcTableBakAndDeleteFromSrcTable(long transnum)
	{
		String sql = null;
		sql = "INSERT INTO " + casp.getSrcTableBak() + " SELECT * FROM " + casp.getSrcTable() + " WHERE TRANSNUM=?";
			
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
		
	    return 1;
	}
    
	public int DoProductCmd(String stbID, String cardID, String cmdType)
	{
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
/*			sql += " stb_id='" ;
			sql += stbID;
			sql += "' ";
*/
		}

		if (cardIDNull==true){
			sql += " and card_id is null " ;
		}
		else{
			sql += " and card_id=? " ;
/*			sql += "  and card_id='";
			sql += cardID;
			sql += "' ";
			*/			
		}

		sql += " and cmd_type in ('CancelProduct', 'AddProduct') ";
		sql += caspp.getCasSQL();
		sql += " order by control_id asc, record_date desc, transnum desc ";
			
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
			resultSet2 = querySql2.executeQuery();
			CACommand cmd = null;
			while (resultSet2.next()) {
				String controlID3 = resultSet2.getString("CONTROL_ID");
				String cmdType3 = resultSet2.getString("cmd_type");
				String authBeginDate3 = resultSet2.getString("AUTH_BEGIN_DATE");
				String authEndDate3 = resultSet2.getString("AUTH_END_DATE");
				
				
				if (controlID2.equals(controlID3) == false){
					boolean addFlag = false;
					if ("ADDPRODUCT".equalsIgnoreCase(cmdType3) == true){
						if (caspp.getAddMode().equalsIgnoreCase(CASPrivatePara.MODE_NOTSUPPORT) == true){
							addFlag = true;
						}
						else if (caspp.getAddMode().equalsIgnoreCase(CASPrivatePara.MODE_SUPPORT) == true){
							if (listAddCmd.size()==0){
								addFlag = true;
							}
							else{
								CACommand cmdYes = listAddCmd.get(0);
								cmdYes.setCount(cmdYes.getCount()+1);
								if (cmdYes.getAuthBeginDate().equals(authBeginDate3) == true
									&& cmdYes.getAuthEndDate().equals(authEndDate3) == true){
									String dp = cmdYes.getDetailParams();
									dp += "CTRL" + cmdYes.getCount() + ":'";
									dp += controlID3;
									dp += "';";
									cmdYes.setDetailParams(dp);
								}
								else{
									String dp = cmdYes.getDetailParams();
									dp += "CTRL" + cmdYes.getCount() + ":'";
									dp += controlID3;
									dp += "';";
									dp += "BD" + cmdYes.getCount() + ":'";
									dp += authBeginDate3;
									dp += "';";
									dp += "ED" + cmdYes.getCount() + ":'";
									dp += authEndDate3;
									dp += "';";
									cmdYes.setDetailParams(dp);
								}
								addFlag = false;
							}
						} //else if (caspp.getAddMode()==CASPrivatePara.MODE_SUPPORT){
						else if (caspp.getAddMode().equalsIgnoreCase(CASPrivatePara.MODE_SAMETIME) == true){
							int len = listAddCmd.size();
							if (len == 0){
								addFlag = true;
							}
							for (int i=0; i<len; i++){
								if (listAddCmd.get(i).getAuthBeginDate().equals(authBeginDate3) == true
									&& listAddCmd.get(i).getAuthEndDate().equals(authEndDate3) == true){
									listAddCmd.get(i).setCount(listAddCmd.get(i).getCount()+1);
									String dp = listAddCmd.get(i).getDetailParams();
									/*if (dp!=null && "".equals(dp)==false){
										dp += ";";
									}*/
									dp += "CTRL"+(listAddCmd.get(i).getCount())+":'";
									dp += controlID3;
									dp += "';";
									listAddCmd.get(i).setDetailParams(dp);
									addFlag = false;
									break;
								}
								else{
									addFlag = true;
								}
							} //for (int i=0; i<len; i++){							
						} //else if (caspp.getAddMode()==CASPrivatePara.MODE_SAMETIME){

					}else if ("CANCELPRODUCT".equalsIgnoreCase(cmdType3) == true){
						if (caspp.getCancelMode().equalsIgnoreCase(CASPrivatePara.MODE_NOTSUPPORT) == true){
							addFlag = true;
						}
						else if (caspp.getCancelMode().equalsIgnoreCase(CASPrivatePara.MODE_SUPPORT) == true){
							if (listCancelCmd.size()==0){
								addFlag = true;
							}
							else{
								CACommand cmdYes = listCancelCmd.get(0);
								cmdYes.setCount(cmdYes.getCount()+1);
								if (cmdYes.getAuthBeginDate().equals(authBeginDate3) == true
									&& cmdYes.getAuthEndDate().equals(authEndDate3) == true){
									String dp = cmdYes.getDetailParams();
									dp += "CTRL" + cmdYes.getCount() + ":'";
									dp += controlID3;
									dp += "';";
									cmdYes.setDetailParams(dp);
								}
								else{
									String dp = cmdYes.getDetailParams();
									dp += "CTRL" + cmdYes.getCount() + ":'";
									dp += controlID3;
									dp += "';";
									dp += "BD" + cmdYes.getCount() + ":'";
									dp += authBeginDate3;
									dp += "';";
									dp += "ED" + cmdYes.getCount() + ":'";
									dp += authEndDate3;
									dp += "';";
									
									cmdYes.setDetailParams(dp);
								}
								addFlag = false;
							}
						} //else if (caspp.getAddMode()==CASPrivatePara.MODE_SUPPORT){
						else if (caspp.getCancelMode().equalsIgnoreCase(CASPrivatePara.MODE_SAMETIME) == true){
							int len = listCancelCmd.size();
							if (len == 0){
								addFlag = true;
							}
							for (int i=0; i<len; i++){
								if (listCancelCmd.get(i).getAuthBeginDate().equals(authBeginDate3) == true
									&& listCancelCmd.get(i).getAuthEndDate().equals(authEndDate3) == true){
									listCancelCmd.get(i).setCount(listCancelCmd.get(i).getCount()+1);
									String dp = listCancelCmd.get(i).getDetailParams();
									/*if (dp!=null && "".equals(dp)==false){
										dp += ";";
									} */
									dp += "CTRL"+(listCancelCmd.get(i).getCount())+":'";
									dp += controlID3;
									dp += "';";
									listCancelCmd.get(i).setDetailParams(dp);
									addFlag = false;
									break;
								}
								else{
									addFlag = true;
								}
							}							
						} //else if (caspp.getAddMode()==CASPrivatePara.MODE_SAMETIME){

					} //else if ("CANCELPRODUCT".equalsIgnoreCase(cmdType3) == true){
					
					if (addFlag==true){
						cmd = new CACommand();

						cmd.setUserID(resultSet2.getString("USER_ID"));
						cmd.setCustID(resultSet2.getString("CUST_ID"));
						cmd.setCasID(resultSet2.getString("CAS_ID"));
						cmd.setCasType(resultSet2.getString("CAS_TYPE"));
						cmd.setCmdType(cmdType3);
						cmd.setControlID(controlID3);
						cmd.setStbID(resultSet2.getString("STB_ID"));
						cmd.setCardID(resultSet2.getString("CARD_ID"));
						cmd.setDetailParams("");
						cmd.setAreaID(resultSet2.getString("AREA_ID"));
						cmd.setAuthBeginDate(authBeginDate3);
						cmd.setAuthEndDate(authEndDate3);
						cmd.setCount(1);
						
						
						if ("ADDPRODUCT".equalsIgnoreCase(cmdType3) == true){
							if (caspp.getAddMode().equalsIgnoreCase(CASPrivatePara.MODE_SUPPORT) == true){
								String dp = "CTRL1:'" + controlID3 + "';";
								cmd.setDetailParams(dp);
							}
							else if (caspp.getAddMode().equalsIgnoreCase(CASPrivatePara.MODE_SAMETIME) == true){
								String dp = "CTRL1:'" + controlID3 + "';";
								cmd.setDetailParams(dp);
							}
							listAddCmd.add(cmd);
						}
						else if ("CANCELPRODUCT".equalsIgnoreCase(cmdType3) == true){
							if (caspp.getCancelMode().equalsIgnoreCase(CASPrivatePara.MODE_SUPPORT) == true){
								String dp = "CTRL1:'" + controlID3 + "';";
								cmd.setDetailParams(dp);
							}
							else if (caspp.getCancelMode().equalsIgnoreCase(CASPrivatePara.MODE_SAMETIME) == true){
								String dp = "CTRL1:'" + controlID3 + "';";
								cmd.setDetailParams(dp);
							}
							listCancelCmd.add(cmd);
						}
					}
					
					listTransnum.add(resultSet2.getLong("TRANSNUM"));
					
					controlID2 = controlID3;
					cmdType2 = cmdType3;
					authBeginDate2 = authBeginDate3;
					authEndDate2 = authEndDate3;
				} //if (controlID2.equals(controlID3) == false){
				else{
					listTransnum.add(resultSet2.getLong("TRANSNUM"));
				}
				

			}
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
		
		int len = listAddCmd.size();
		for (int i=0; i<len; i++){
			if (listAddCmd.get(i).getCount()>1){
				String dp = listAddCmd.get(i).getDetailParams();
				String dp2 = "CNT:'" + listAddCmd.get(i).getCount() + "';";
				dp2 += dp;
				listAddCmd.get(0).setDetailParams(dp2);
			}
		}
		len = listCancelCmd.size();
		for (int i=0; i<len; i++){
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
		
		return 1;
	}
	
	
	public void run() {
		
		db = new CADB(ip, port, service, user, password);	
		while (db.connDB()<1){
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
					resultSet = querySql.executeQuery(getSrcSQL(index));
					while (resultSet.next()) {
						iCount++;
						String cmdType = resultSet.getString("CMD_TYPE");

						if ("ADDPRODUCT".equalsIgnoreCase(cmdType)==true
							|| "CANCELPRODUCT".equalsIgnoreCase(cmdType) == true){
							if (DoProductCmd(resultSet.getString("stb_id"), resultSet.getString("card_id"), resultSet.getString("cmd_type")) < 0){
								startFlag = true;
								break;
							}
						}
						else{
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
