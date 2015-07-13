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
import CASche.common.CaConstants;
import CASche.common.SchedultException;
import CASche.help.LoggerHelper;

/**
 * 合并指令后移动到out表
 * @author new
 *
 */
public class MergeMoveToOut extends MoveCommandToOut {
	
	private String query_card_sql=null;
	private PreparedStatement ps_card=null;
	private Merge merge;
	public MergeMoveToOut(CADB db, CASPrivatePara cas_para, CASchePara cas_sche,Merge merge) {
		super(db, cas_para, cas_sche);
		this.merge=merge;
		this.query_card_sql="select * from "+cas_sche.getSrcTable()
		+" where card_id=? and cas_id=? and cmd_type in " +
		"('"+CaConstants.AddProduct+"','"+CaConstants.CancelProduct+"') order by transnum";
	}
	/**
	 * 查询一个card对应的所有未发加减授权
	 * @param ps_card
	 * @param card_id
	 * @param cas_id
	 * @return
	 * @throws SQLException
	 */
	private List<JCmdDay> queryCard(PreparedStatement ps_card,String card_id,String cas_id) throws SQLException{
		ResultSet rs=null;
		try{
			List<JCmdDay> list=new ArrayList<JCmdDay>();
			ps_card.setString(1, card_id);
			ps_card.setString(2, cas_id);
			rs=ps_card.executeQuery();
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
				JCmdDay day=new JCmdDay();
				day.setCacmd(ca);
				list.add(day);
			}
			return list;
		}catch(SQLException e){
			throw e;
		}finally{
			if(rs!=null){
				try{rs.close();}catch(Exception e){}
				rs=null;
			}
		}
	}

	/**
	 * 指令合并处理
	 */
	@Override
	public List<JCaCommand> queryMoveCaCmd(List<JCmdDay> daylist,PreparedStatement ps_min,
			String cas_id, int min_index,List<JCaCommand> osdlist) throws SQLException,SchedultException {
		List<JCaCommand> minlist= super.queryMoveCaCmd(daylist,ps_min, cas_id, min_index,osdlist);
		if(min_index==this.cas_sche.getOrder_notsupport()-1){
			//不启用指令合并处理
			if(LoggerHelper.isDebugEnabled(this.getClass())&&minlist.size()>0)
				LoggerHelper.debug(this.getClass(),this.cas_para.getName()+ ":ORDER"+this.cas_sche.getOrder_notsupport()
					+" 主动关闭优化，直接调度"+minlist.size()+"条指令");
			return minlist;
		}
		
		List<JCaCommand> list=new ArrayList<JCaCommand>();
		for(JCaCommand ca:minlist){
			if(CaConstants.AddProduct.equals(ca.getCmd_type())
			    		||CaConstants.CancelProduct.equals(ca.getCmd_type())){
				//加减授权的情况
				//取同一个智能卡的所有未处理的加减授权指令
				List<JCmdDay> cardcmdlist=this.queryCard(ps_card, ca.getCard_id(), ca.getCas_id());
				if(cardcmdlist.size()<=0){
					LoggerHelper.error(this.getClass(),this.cas_para.getName()+"指令优化合并:"+ca.getTransnum()+"的智能卡:"+ca.getCard_id()+"找不到记录");
					ca.setResult_flag("错误");
					ca.setError_info("智能卡:"+ca.getCard_id());
					ca.setSend_date(new Timestamp(System.currentTimeMillis()));
					return list;
				}
				if(cardcmdlist.size()==1){
					//使用card_id取出是单条指令，则不使用优化
					list.add(ca);
				}else{
					if(!CASPrivatePara.MODE_NOTSUPPORT.equals(this.cas_para.getAddMode())){
						//ca支持多指令的
						List<JCaCommand> mergelist=this.merge.mergeMultiCotrolId(cardcmdlist);
						list.addAll(mergelist);
						
						if(LoggerHelper.isDebugEnabled(this.getClass())){
							LoggerHelper.debug(this.getClass(),this.cas_para.getName()+ "智能卡:"+ca.getCard_id()+"多控制字优化合并"+cardcmdlist.size()+"条指令到"+mergelist.size()+"条" +
									"["+(mergelist.size()>0?mergelist.get(0).getTransnum()+",":"")+
									(mergelist.size()>1?mergelist.get(1).getTransnum()+",":"")+
									(mergelist.size()>2?mergelist.get(2).getTransnum()+",":"")+
									(mergelist.size()>3?mergelist.get(3).getTransnum()+",..":"")+"]");
						}
					}else{
						//ca不支持多指令合并则相同智能卡的相同控制字的指令合并
						List<JCaCommand> mergelist=this.merge.mergeSameCotrolId(cardcmdlist);
						list.addAll(mergelist);
						if(LoggerHelper.isDebugEnabled(this.getClass()))
							LoggerHelper.debug(this.getClass(), this.cas_para.getName()+"智能卡:"+ca.getCard_id()+"相同控制字优化合并"+cardcmdlist.size()+"条指令到"+mergelist.size()+"条"
							+"["+(mergelist.size()>0?mergelist.get(0).getTransnum()+",":"")+
							(mergelist.size()>1?mergelist.get(1).getTransnum()+",":"")+
							(mergelist.size()>2?mergelist.get(2).getTransnum()+",":"")+
							(mergelist.size()>3?mergelist.get(3).getTransnum()+",..":"")+"]");;
					}
					daylist.clear();
					daylist.addAll(cardcmdlist);
				}
			}else{
				//其他授权处理
				list.add(ca);
			}
		}
		return list;
	}
	
	@Override
	protected void closeStatement() {
		super.closeStatement();
		if(ps_card!=null){
			try{ps_card.close();}catch(Exception e){}
			ps_card=null;
		}
	}

	@Override
	protected void createStatement() {
		super.createStatement();
		if(ps_card==null){
			ps_card=this.db.prepareStatement(this.query_card_sql);
		}
	}

}
