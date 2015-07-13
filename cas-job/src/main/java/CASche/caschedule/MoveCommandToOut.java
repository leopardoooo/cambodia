package CASche.caschedule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import CASche.CADB;
import CASche.CASPrivatePara;
import CASche.CASchePara;
import CASche.CATools;
import CASche.check.OsdCheckRefresh;
import CASche.check.OsdLawfulCheck;
import CASche.common.CaConstants;
import CASche.common.SchedultException;
import CASche.help.LoggerHelper;

/**
 * 移动j_ca_command 合并后记录写入j_ca_command_out,并原始记录写入j_ca_command_day
 */
public class MoveCommandToOut implements Runnable {

	protected CADB db=null;//数据源
	protected CASPrivatePara cas_para = null;//ca服务器配置信息
	protected CASchePara cas_sche = null;//调度配置信息
	
	private String query_min_sql[]=null;
	private String commandtoday=null;
	private String commandtoout=null;
	private String deletecommandsql=null;
	private String query_out_sql=null;
	private String error_sql=null;
	
	private PreparedStatement[] ps_min=null;
	private PreparedStatement ps_day=null;
	private PreparedStatement ps_out=null;
	private PreparedStatement ps_del=null;
	private PreparedStatement ps_queryout=null;
	
	private PreparedStatement ps_error=null;
	
	private PreparedStatement ps_osdinvalid=null;
	private PreparedStatement ps_osddelete=null;
	
	private List<JCaCommand> primitiveCaCmds=null;
	
	public MoveCommandToOut(CADB db,CASPrivatePara cas_para,CASchePara cas_sche){
		
		this.db=db;
		this.db.connDB();
		

		this.cas_para=cas_para;
		this.cas_sche=cas_sche;
		this.ps_min=new PreparedStatement[this.cas_sche.getOrderCount()];
		//查询最小的指令 优先级
		String query_min_basesql="select TRANSNUM     ,JOB_ID ,CAS_ID ,CAS_TYPE    ,USER_ID      ,CUST_ID   ,DONE_CODE      ," +
	    "  CMD_TYPE     ,STB_ID ,CARD_ID,PRG_NAME    ,BOSS_RES_ID  ,CONTROL_ID,AUTH_BEGIN_DATE," +
	    "  AUTH_END_DATE,AREA_ID,IS_SENT,RECORD_DATE ,DETAIL_PARAMS,PRIORITY,result_flag    ,error_info,send_date" +
	    " from (select * from "+cas_sche.getSrcTable()+" t where #order# and t.cas_id=? order by t.transnum) where rownum<#canum#";
		
		this.query_min_sql=this.createquery_min_sqls(query_min_basesql);
		
	    this.commandtoday="INSERT INTO " + cas_sche.getSrcTableBak() + 
	    " (TRANSNUM     ,JOB_ID ,CAS_ID ,CAS_TYPE    ,USER_ID      ,CUST_ID   ,DONE_CODE      ," +
	    "  CMD_TYPE     ,STB_ID ,CARD_ID,PRG_NAME    ,BOSS_RES_ID  ,CONTROL_ID,AUTH_BEGIN_DATE," +
	    "  AUTH_END_DATE,AREA_ID,IS_SENT,RECORD_DATE ,DETAIL_PARAMS,PRIORITY,result_flag    ,error_info,send_date,merge_trunsnum) " +
	    "values(?       ,?      ,?      ,?           ,?            ,?         ,?              ,"+
	    "       ?       ,?      ,?      ,?           ,?            ,?         ,?              ,"+
	    "       ?       ,?      ,?      ,?           ,?            ,?         ,?              ,?,?,?) ";
	    
	    this.commandtoout="INSERT INTO " + cas_sche.getDstTable() + 
	    " (TRANSNUM     ,JOB_ID ,CAS_ID ,CAS_TYPE    ,USER_ID      ,CUST_ID   ,DONE_CODE      ," +
	    "  CMD_TYPE     ,STB_ID ,CARD_ID,PRG_NAME    ,BOSS_RES_ID  ,CONTROL_ID,AUTH_BEGIN_DATE," +
	    "  AUTH_END_DATE,AREA_ID,IS_SENT,RECORD_DATE ,DETAIL_PARAMS,PRIORITY  ,result_flag    ,error_info,send_date) " +
	    "values(?       ,?      ,?      ,?           ,?            ,?         ,?              ,"+
	    "       ?       ,?      ,?      ,?           ,?            ,?         ,?              ,"+
	    "       ?       ,?      ,?      ,?           ,?            ,?         ,?              ,?,?) ";
	    this.deletecommandsql="delete from "+cas_sche.getSrcTable()+" where transnum=?";
	    
	    this.query_out_sql="select count(1) from "+cas_sche.getDstTable()+" where is_sent in ('1','N') and cas_id=?";
	    
	    this.error_sql="update "+cas_sche.getSrcTable()+" set is_sent=?,send_date=?,result_flag=?,error_info=? where transnum=?";
	}
	
	/**
	 * 不同优先级的查询待发送指令command表的sql
	 * @return
	 */
	private String[] createquery_min_sqls(String base_sql){
		String[] sqls=new String[ this.cas_sche.getOrderCount()];
		for(int i=1;i<=this.cas_sche.getOrderCount();i++){
			if(i==this.cas_sche.getOrder_notsupport()){
				sqls[i-1]=base_sql.replaceFirst(CaConstants.order_key, this.cas_sche.getMapOrder().get("ORDER"+i)).replaceFirst(CaConstants.canum_key, "100");
			}else{
				sqls[i-1]=base_sql.replaceFirst(CaConstants.order_key, this.cas_sche.getMapOrder().get("ORDER"+i)).replaceFirst(CaConstants.canum_key, "2");
			}
		}
		return sqls;
	}
	
	public void run() {
		LoggerHelper.debug(this.getClass(), this.cas_para.getName()+"======》开始 从command 移到 out 表 并回写day表");
		int ps_min_index=0;
		while(true){
			try {
				//数据库重连
				if (db.getConnectInterruptFlag() == true){
					while(db.getConnectInterruptFlag() == true){
				 		db.connDB();
				 		CATools.sleeping(CaConstants.free_sleep_time);
				 	}
				}
				//创建预编译器
				this.createStatement();	
				//检查out表未发送指令量
				if(this.query_out_cnt(this.cas_para.getName())>this.cas_para.getPool()){
					
					LoggerHelper.debug(this.getClass(), this.cas_para.getName()+"调度out发送忙碌等待");
					CATools.sleeping(CaConstants.free_sleep_time);
					continue;
				}
				List<JCmdDay> daylist=new ArrayList<JCmdDay>();
				List<JCaCommand> osdlist=new ArrayList<JCaCommand>();
				//按优先级轮换提取要发送数据，高优先级单条提取数据当轮换到最低优先级时，
				List<JCaCommand> list=this.queryMoveCaCmd(daylist,ps_min[ps_min_index], this.cas_para.getName(),ps_min_index,osdlist);
				if(daylist.size()>0||osdlist.size()>0){
					ps_min_index=0;//有数据则切换回优先级0，从0优先级循环执行
					if(daylist.size()>0){
						this.movetoout(list, this.ps_out);
						this.movetoday(daylist, this.ps_day);
						this.deletecommand(daylist, this.ps_del);
					}
					if(osdlist.size()>0){
						this.checkOsdInvalid(osdlist);
						LoggerHelper.info(this.getClass(),  this.cas_para.getName()+"发现"+osdlist.size()
								+"条非法OSD:"+osdlist.get(0).getTransnum()+",非法内容:"+osdlist.get(0).getError_info());
					}
					//提交数据库
					db.commit();
				}else{
					//优先级循环处理
					ps_min_index++;
					if(ps_min_index>=this.cas_sche.getOrderCount()){
						ps_min_index=0;
						//最低优先级无数据提取时，进程休眠
						LoggerHelper.debug(this.getClass(), this.cas_para.getName()+"调度空闲等待");
						CATools.sleeping(CaConstants.free_sleep_time);
					}
					//if(LoggerHelper.isDebugEnabled(this.getClass()))
					//	LoggerHelper.debug(this.getClass(), this.cas_para.getName()+"切换到ORDER"+(ps_min_index+1)+"执行");
				}
		
			} catch (SQLException e) {
				LoggerHelper.error(this.getClass(),"数据库错误", e);
				db.rollback();
				if (db.checkSpecialErrorCode(e.getErrorCode()) == -1) {
					db.setConnectInterruptFlag(true);
					db.closeConn();
				}else{
					//非数据库连不上错误处理，跳过出错的原始记录
					this.error_update(e.getMessage());
				}
				this.closeStatement();
				
			}catch (Exception e) {
				LoggerHelper.error(this.getClass(),"其他错误", e);
				db.rollback();
				//非数据库连不上错误处理，跳过出错的原始记录
				this.error_update(e.getMessage());
				this.closeStatement();
			}
		}
	}
	/**
	 * this.error_sql="update "+cas_sche.getSrcTable()+
	 * " set is_sent=?,send_date=?,result_flag=?,error_info=? where transnum=?";
	 * 出错处理，跳过错误的指令
	 */
	private void error_update(String error_info){
		try {
			if(this.primitiveCaCmds!=null){
				for(JCaCommand cacmd: this.primitiveCaCmds){
					this.ps_error.setString(1,CaConstants.status_schedult);
					this.ps_error.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
					this.ps_error.setString(3, "调度错误");
					this.ps_error.setString(4, error_info!=null&&error_info.length()>60?error_info.substring(0,60):error_info);
					this.ps_error.setLong(5, cacmd.getTransnum());
					this.ps_error.addBatch();
				}
				this.ps_error.executeBatch();
				this.db.commit();
			}
		} catch (SQLException e) {
			LoggerHelper.error(this.getClass(),"database_error"+this.error_sql,e);
			this.db.rollback();
			if (db.checkSpecialErrorCode(e.getErrorCode()) == -1) {
				db.setConnectInterruptFlag(true);
				db.closeConn();
			}
		} catch(Exception e){
			LoggerHelper.error(this.getClass(), "system_error",e);
			this.db.rollback();
		}
		
	}
	/**
	 * 查询out表未发送指令堆积量
	 * @param cas_id
	 * @return
	 * @throws SQLException
	 */
	private int query_out_cnt(String cas_id) throws SQLException{
		ResultSet rs=null;
		try {
			this.ps_queryout.setString(1, cas_id);
			rs=this.ps_queryout.executeQuery();
			if(rs.next()){
				return rs.getInt(1);
			}else
				return 0;
		} catch (SQLException e) {
			LoggerHelper.error(this.getClass(), this.query_out_sql);
			throw e;
		}finally{
			if(rs!=null){
				try{rs.close();}catch(Exception e){}
				rs=null;
			}
		}
	}
	/**
	 * 从command移动到day
	 * @param list
	 * @param ps
	 * @throws SQLException 
	 */
	private void movetoday(List<JCmdDay> list,PreparedStatement ps) throws SQLException{
		try{
		for(JCmdDay day:list){
			JCaCommand ca=day.getCacmd();
			ps.setLong(1, ca.getTransnum());
			ps.setLong(2,ca.getJob_id());
			ps.setString(3, ca.getCas_id());
			ps.setString(4, ca.getCas_type());
			ps.setString(5, ca.getUser_id());
			ps.setString(6, ca.getCust_id());
			ps.setLong(7, ca.getDone_code());
			ps.setString(8, ca.getCmd_type());
			ps.setString(9, ca.getStb_id());
			ps.setString(10, ca.getCard_id());
			ps.setString(11, ca.getPrg_name());
			ps.setString(12, ca.getBoss_res_id());
			ps.setString(13,ca.getControl_id());
			ps.setString(14, ca.getAuth_begin_date());
			ps.setString(15, ca.getAuth_end_date());
			ps.setString(16, ca.getArea_id());
			ps.setString(17, CaConstants.status_schedult);
			ps.setTimestamp(18,ca.getRecord_date());
			ps.setString(19, ca.getDetail_params());
			ps.setInt(20, ca.getPriority());
			ps.setString(21, ca.getResult_flag());
			ps.setString(22, ca.getError_info());
			ps.setTimestamp(23, ca.getSend_date());
			ps.setLong(24, day.getMerge_trunsnum());
			ps.addBatch();
		}
		ps.executeBatch();
		}catch(SQLException e){
			LoggerHelper.error(this.getClass(), this.commandtoday);
			throw e;
		}
	}
	
	/**
	 * 删除j_ca_command记录
	 * @param list
	 * @param ps_deletecmd
	 * @throws SQLException
	 */
	private void deletecommand(List<JCmdDay> list,PreparedStatement ps_deletecmd) throws SQLException{
		try{
		for(JCmdDay day:list){
			JCaCommand ca=day.getCacmd();
			ps_deletecmd.setLong(1, ca.getTransnum());
			ps_deletecmd.addBatch();
		}
		ps_deletecmd.executeBatch();
		}catch(SQLException e){
			LoggerHelper.error(this.getClass(), this.deletecommandsql);
			throw e;
		}
	}
	/**
	 * command到out表
	 * @param list
	 * @param ps
	 * @throws SQLException
	 */
	private void movetoout(List<JCaCommand> list,PreparedStatement ps) throws SQLException{
		try{
			/**
this.commandtoout="INSERT INTO " + cas_sche.getDstTable() + 
	    " (TRANSNUM     ,JOB_ID ,CAS_ID ,CAS_TYPE    ,USER_ID      ,CUST_ID   ,DONE_CODE      ," +
	    "  CMD_TYPE     ,STB_ID ,CARD_ID,PRG_NAME    ,BOSS_RES_ID  ,CONTROL_ID,AUTH_BEGIN_DATE," +
	    "  AUTH_END_DATE,AREA_ID,IS_SENT,RECORD_DATE ,DETAIL_PARAMS,PRIORITY  ,result_flag    ,error_info,send_date) " +
	    "values(?       ,?      ,?      ,?           ,?            ,?         ,?              ,)"+
	    "       ?       ,?      ,?      ,?           ,?            ,?         ,?              ,)"+
	    "       ?       ,?      ,?      ,?           ,?            ,?         ,?              ,?,?) ";
	
			 */
		for(JCaCommand ca:list){
			ps.setLong(1, ca.getTransnum());
			ps.setLong(2,ca.getJob_id());
			ps.setString(3, ca.getCas_id());
			ps.setString(4, ca.getCas_type());
			ps.setString(5, ca.getUser_id());
			ps.setString(6, ca.getCust_id());
			ps.setLong(7, ca.getDone_code());
			ps.setString(8, ca.getCmd_type());
			ps.setString(9, ca.getStb_id());
			ps.setString(10, ca.getCard_id());
			ps.setString(11, ca.getPrg_name());
			ps.setString(12, ca.getBoss_res_id());
			ps.setString(13,ca.getControl_id());
			ps.setString(14, ca.getAuth_begin_date());
			ps.setString(15, ca.getAuth_end_date());
			ps.setString(16, ca.getArea_id());
			ps.setString(17, ca.getIs_sent());
			ps.setTimestamp(18,ca.getRecord_date());
			ps.setString(19, ca.getDetail_params());
			ps.setInt(20, ca.getPriority());
			ps.setString(21, ca.getResult_flag());
			ps.setString(22, ca.getError_info());
			ps.setTimestamp(23, ca.getSend_date());
			ps.addBatch();
		}
		ps.executeBatch();
		}catch(SQLException e){
			LoggerHelper.error(this.getClass(), this.commandtoout);
			throw e;
		}
	}
	/**
	 * 查询commad的未发记录
	 * @param daylist
	 * @param ps_min
	 * @param cas_id
	 * @param min_index
	 * @return
	 * @throws SQLException
	 * @throws Exception 
	 */
	public List<JCaCommand> queryMoveCaCmd(List<JCmdDay> daylist, PreparedStatement ps_min,String cas_id,int min_index,List<JCaCommand> osdlist) throws SQLException, SchedultException{
		ResultSet rs=null;
		try {
			ps_min.setString(1, cas_id);
			rs=ps_min.executeQuery();

			List<JCaCommand> list=new ArrayList<JCaCommand>();
			List<JCaCommand> primitivelist=new ArrayList<JCaCommand>();
			while(rs.next()){
				JCaCommand ca=new JCaCommand();
				ca.setTransnum(rs.getLong(1));
				ca.setJob_id(rs.getLong(2));
				ca.setCas_id(rs.getString(3));
				ca.setCas_type(rs.getString(4));
				ca.setUser_id(rs.getString(5));
				ca.setCust_id(rs.getString(6));
				ca.setDone_code(rs.getLong(7));
				ca.setCmd_type(rs.getString(8));
				ca.setStb_id(rs.getString(9));
				ca.setCard_id(rs.getString(10));
				ca.setPrg_name(rs.getString(11));
				ca.setBoss_res_id(rs.getString(12));
				ca.setControl_id(rs.getString(13));
				ca.setAuth_begin_date(rs.getString("auth_begin_date"));
				ca.setAuth_end_date(rs.getString("auth_end_date"));
				ca.setArea_id(rs.getString("area_id"));
				ca.setIs_sent(rs.getString("is_sent"));
				ca.setRecord_date(rs.getTimestamp("Record_date"));
				ca.setDetail_params(rs.getString("Detail_params"));
				ca.setPriority(rs.getInt("Priority"));
				ca.setResult_flag(rs.getString("Result_flag"));
				ca.setError_info(rs.getString("Error_info"));
				ca.setSend_date(rs.getTimestamp("Send_date"));
				primitivelist.add(ca);
				//OSD合法性检查切入
				if(this.cas_sche.isOsd_content_check()
						&&CaConstants.SendOsd.equals(ca.getCmd_type())){
					if(OsdCheckRefresh.isOsd_stop()){
						ca.setIs_sent(CaConstants.is_sent_E);
						ca.setError_info("强制终止");
						osdlist.add(ca);
					}else{
						//提取和检查OSD内容
						try{
							String check=OsdLawfulCheck.LawfulCheck(OsdLawfulCheck.extractOsdContent(ca));
							if(check==null){
								list.add(ca);
								JCmdDay day=new JCmdDay();
								day.setCacmd(ca);
								day.setMerge_trunsnum(ca.getTransnum());
								daylist.add(day);
							}else{
								ca.setError_info(check.substring(0, check.length()>100?100:check.length()));
								ca.setIs_sent(CaConstants.is_sent_E);
								osdlist.add(ca);
							}
						}catch(Exception e1){
							ca.setError_info(e1.getMessage().substring(0, e1.getMessage().length()>100?100:e1.getMessage().length()));
							ca.setIs_sent(CaConstants.is_sent_E);
							osdlist.add(ca);
						}
					}
					
				}else{
					list.add(ca);
					JCmdDay day=new JCmdDay();
					day.setCacmd(ca);
					day.setMerge_trunsnum(ca.getTransnum());
					daylist.add(day);
				}
			}
			//记录原始提取的ca指令
			this.primitiveCaCmds=list;
			return list;
		} catch (SQLException e) {
			LoggerHelper.error(this.getClass(), this.query_min_sql[min_index]);
			throw e;
		}finally{
			if(rs!=null){
				try{rs.close();}catch(Exception e1){}
				rs=null;
			}
		}
	}
	
	private void checkOsdInvalid(List<JCaCommand> osdinvalidCaCmds) throws SQLException{
		//移动记录到j_ca_command_osd_invalid表
		if(ps_osdinvalid==null){
			this.ps_osdinvalid=db.prepareStatement("INSERT INTO j_ca_command_osd_invalid" + 
				    " (TRANSNUM     ,JOB_ID ,CAS_ID ,CAS_TYPE    ,USER_ID      ,CUST_ID   ,DONE_CODE      ," +
				    "  CMD_TYPE     ,STB_ID ,CARD_ID,PRG_NAME    ,BOSS_RES_ID  ,CONTROL_ID,AUTH_BEGIN_DATE," +
				    "  AUTH_END_DATE,AREA_ID,IS_SENT,RECORD_DATE ,DETAIL_PARAMS,PRIORITY  ,result_flag    ,error_info,send_date) " +
				    "values(?       ,?      ,?      ,?           ,?            ,?         ,?              ,"+
				    "       ?       ,?      ,?      ,?           ,?            ,?         ,?              ,"+
				    "       ?       ,?      ,?      ,?           ,?            ,?         ,?              ,?,?) ");
		}
		try{
			for(JCaCommand ca:osdinvalidCaCmds){
				ps_osdinvalid.setLong(1, ca.getTransnum());
				ps_osdinvalid.setLong(2,ca.getJob_id());
				ps_osdinvalid.setString(3, ca.getCas_id());
				ps_osdinvalid.setString(4, ca.getCas_type());
				ps_osdinvalid.setString(5, ca.getUser_id());
				ps_osdinvalid.setString(6, ca.getCust_id());
				ps_osdinvalid.setLong(7, ca.getDone_code());
				ps_osdinvalid.setString(8, ca.getCmd_type());
				ps_osdinvalid.setString(9, ca.getStb_id());
				ps_osdinvalid.setString(10, ca.getCard_id());
				ps_osdinvalid.setString(11, ca.getPrg_name());
				ps_osdinvalid.setString(12, ca.getBoss_res_id());
				ps_osdinvalid.setString(13,ca.getControl_id());
				ps_osdinvalid.setString(14, ca.getAuth_begin_date());
				ps_osdinvalid.setString(15, ca.getAuth_end_date());
				ps_osdinvalid.setString(16, ca.getArea_id());
				ps_osdinvalid.setString(17, ca.getIs_sent());
				ps_osdinvalid.setTimestamp(18,ca.getRecord_date());
				ps_osdinvalid.setString(19, ca.getDetail_params());
				ps_osdinvalid.setInt(20, ca.getPriority());
				ps_osdinvalid.setString(21, ca.getResult_flag());
				ps_osdinvalid.setString(22, ca.getError_info());
				ps_osdinvalid.setTimestamp(23, ca.getSend_date());
				ps_osdinvalid.addBatch();
			}
			ps_osdinvalid.executeBatch();
		}catch(SQLException e){
			LoggerHelper.error(this.getClass(), "insert_into_error:j_ca_command_osd_invalid");
			throw e;
		}
		
		//删除合法性验证错误的osd记录
		if(ps_osddelete==null){
			this.ps_osddelete=db.prepareStatement("delete from "+cas_sche.getSrcTable()+" where transnum=?");
		}	
		try {
			for(JCaCommand ca:osdinvalidCaCmds){	
				ps_osddelete.setLong(1, ca.getTransnum());
				ps_osddelete.addBatch();
			}
			ps_osddelete.executeBatch();
		} catch (SQLException e) {
			LoggerHelper.error(this.getClass(), "delete from "+cas_sche.getSrcTable()+" where transnum=?");
			throw e;
		}
	}
	
	
	protected void createStatement(){
		for(int i=0;i<this.ps_min.length;i++){	
			if(this.ps_min[i]==null)
				this.ps_min[i]= db.prepareStatement(this.query_min_sql[i]);
		}
		if(ps_day==null)
			ps_day=db.prepareStatement(this.commandtoday);
		if(ps_out==null)
			ps_out=db.prepareStatement(this.commandtoout);
		if(ps_del==null)
			ps_del=db.prepareStatement(this.deletecommandsql);
		if(ps_queryout==null)
			ps_queryout=db.prepareStatement(this.query_out_sql);
		if(ps_error==null)
			ps_error=db.prepareStatement(this.error_sql);
	}
	
	protected void closeStatement(){
		for(int i=0;i<this.ps_min.length;i++){	
			if(this.ps_min[i]!=null){
				try{ps_min[i].close();}catch(Exception e){}
				this.ps_min[i]=null;
			}
		}
		if(ps_day!=null){
			try{ps_day.close();}catch(Exception e){}
			ps_day=null;
		}
		if(ps_out!=null){
			try{ps_out.close();}catch(Exception e){}
			ps_out=null;
		}
		if(ps_del!=null){
			try{ps_del.close();}catch(Exception e){}
			ps_del=null;
		}
		if(ps_queryout!=null){
			try{ps_queryout.close();}catch(Exception e){}
			ps_queryout=null;
		}
		if(ps_error!=null){
			try{ps_error.close();}catch(Exception e){}
			ps_error=null;
		}
		if(this.ps_osddelete!=null){
			try{ps_osddelete.close();}catch(Exception e){}
			ps_osddelete=null;
		}
		if(this.ps_osdinvalid!=null){
			try{ps_osdinvalid.close();}catch(Exception e){}
			ps_osdinvalid=null;
		}
	}

}
