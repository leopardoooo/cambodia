package com.yaochen.boss.job;

import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.TaskComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.beans.system.SBullentionWorkCount;
import com.ycsoft.beans.system.SBulletinWorktask;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.DateHelper;

/**
 * 工单公告提醒JOB
 * @author new
 *
 */
@Service
public class TaskBulletinJob implements Job2 {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 上次执行检查的时间戳
	 */
	private static Date queryTimeStamp=null;
	@Autowired
	private TaskComponent taskComponent;
	
	@Override
	public void execute(Job2ExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		Date today=DateHelper.today();
		if(queryTimeStamp==null||queryTimeStamp.before(today)){
			queryTimeStamp=today;
		}
		
		try {
			List<SBullentionWorkCount> list = taskComponent.queryBullentionWorkCount(queryTimeStamp);
			if(list!=null&&list.size()>0){
				for(SBulletinWorktask worktask: taskComponent.queryDeptBullentionConfig()){
					if(worktask.getBulletin_worktask_type().equals(SystemConstants.Bulletin_worktask_YYT)){
						String bulletinText=this.createYYTBulletin(list, worktask.getDept_id(), queryTimeStamp);
						if(bulletinText!=null){
							//更新公告内容,删除营业员已查看信息
							if(taskComponent.updateBullentin(worktask.getBulletin_id(), bulletinText)<=0){
								logger.error("找不到公告配置信息，公告ID="+worktask.getBulletin_id());
							}
						}
					}else if(worktask.getBulletin_worktask_type().equals(SystemConstants.Bulletin_worktask_CALL)){
						String bulletinText=this.createCallBulletin(list, queryTimeStamp);
						if(bulletinText!=null){
							//更新公告内容,删除营业员已查看信息
							if(taskComponent.updateBullentin(worktask.getBulletin_id(), bulletinText)<=0){
								logger.error("找不到公告配置信息，公告ID="+worktask.getBulletin_id());
							}
						}
					}else if(worktask.getBulletin_worktask_type().equals(SystemConstants.Bulletin_worktask_WORK)){
						String bulletinText=this.createWorkBullentin(list, queryTimeStamp);
						if(bulletinText!=null){
							//更新公告内容,删除营业员已查看信息
							if(taskComponent.updateBullentin(worktask.getBulletin_id(), bulletinText)<=0){
								logger.error("找不到公告配置信息，公告ID="+worktask.getBulletin_id());
							}
						}
					}else{
						logger.error("未定义的工单提醒类型"+worktask.getBulletin_worktask_type());
					}
				}
			}
			//更新查询时间戳
			if(list!=null&&list.size()>0){
				queryTimeStamp=list.get(0).getQuery_date();
			}
		} catch (Exception e) {
			logger.error("公告提醒生成失败",e);
		}
	}
	
	/**
	 * 工程部的工单提醒，关系 待派单，zte授权，完工等待
	 * 截止时间，新增2个待派单(总共 10个)，新增1个zte授权(总共 3个)，新增cfocn完工等待1个(总共1个),cfocn同步失败2个，总共 20个工单待完工。
	 */
	public String createWorkBullentin(List<SBullentionWorkCount> list,Date currentDate)throws Exception{
	
		int   create_supernet_total=0 ;// '工程部待派单',
		int  create_supernet_new=0 ;//'工程部有无新增待派单',
		int cfocn_failure_total=0;//'cfocn派单同步失败',
		int cfocn_failure_new=0;// '新增cfocn派单同步失败',
		int init_supernet_total=0;// '工程部的派单待完工',
		int zte_total=0; //'ZTE授权数',
		int  zte_new=0;// '新增ZTE授权数',
		int  endwait_total=0;// '完工等待',
		int endwait_new=0;// '新增完工等待',
		for(SBullentionWorkCount wc:list){
			
			create_supernet_total+=wc.getCreate_supernet_total();
			create_supernet_new+=wc.getCreate_supernet_new();
			cfocn_failure_total+=wc.getCfocn_failure_total();
			cfocn_failure_new+=wc.getCfocn_failure_new();
			init_supernet_total+=wc.getInit_supernet_total();
			zte_total+=wc.getZte_total();
			zte_new+=wc.getZte_new();
			endwait_total+=wc.getEndwait_total();
			endwait_new+=wc.getEndwait_new();
		}
		//工单提醒有无变化判断
		if(cfocn_failure_new+create_supernet_new+endwait_new+zte_new<=0){
			return null;
		}
		
		//组织公告数据
		StringBuilder chinaBulletin=new StringBuilder();
		StringBuilder englishBulletin=new StringBuilder();
		//截止时间，新增2个待派单(总共 10个)，新增1个zte授权(总共 3个)，新增cfocn同步失败2个(总共2个)，新增cfocn完工等待1个(总共1个)，总共 20个工单待完工。
		chinaBulletin.append("  截止").append(DateHelper.format(currentDate)).append(",");
		englishBulletin.append("  as of ").append(DateHelper.format(currentDate)).append(",");
		if(create_supernet_new>0){
			chinaBulletin.append("新增").append(create_supernet_new).append("个待派单,");
			//Added five working Dispatch
			englishBulletin.append("add ").append(create_supernet_new).append(" wait dispatch,");
		}
		if(create_supernet_total>0){
			chinaBulletin.append("总共").append(create_supernet_total).append("个待派单,");
			//Added five working Dispatch
			englishBulletin.append("total ").append(create_supernet_total).append(" wait dispatch,");
		}
		if(zte_new>0){
			chinaBulletin.append("新增").append(zte_new).append("个需要zte授权,");
			englishBulletin.append("add ").append(zte_new).append(" need zte authorization,");
		}
		if(zte_total>0){
			chinaBulletin.append("总共").append(zte_total).append("个需要zte授权,");
			//Today has been completed 5
			englishBulletin.append("total ").append(zte_total).append(" need zte authorization,");
		}
		if(endwait_new>0){
			chinaBulletin.append("新增").append(endwait_new).append("个cfocn完工等待,");
			englishBulletin.append("add ").append(endwait_new).append(" cfocn completion wait ,");
		}
		if(endwait_total>0){
			chinaBulletin.append("总共").append(endwait_total).append("个cfocn完工等待,");
			englishBulletin.append("total ").append(endwait_total).append(" cfocn completion wait ,");
		}
		
		if(cfocn_failure_total>0){
			chinaBulletin.append("cfocn派单同步失败").append(cfocn_failure_total).append("个,");
			englishBulletin.append("cfocn dispatch sync failure ").append(cfocn_failure_total).append(",");
		}
		if(init_supernet_total>0){
			chinaBulletin.append("总共").append(init_supernet_total).append("个正在施工.");
			englishBulletin.append("total ").append(init_supernet_total).append(" under construction.");
		}
		
		return chinaBulletin.append("\n").append(englishBulletin.toString()).toString();
	}
	/**
	 * 呼叫中心的工单提醒， 关心派单和完成
	 * 截止时间，新增2个派单，新增1个完工，今天已完工5个，cfocn同步失败2个，总共0个待派单
	 */
	public String createCallBulletin(List<SBullentionWorkCount> list,Date currentDate) throws Exception{
		return createYYTBulletin(list,null,currentDate);
	}
	/**
	 * 创建营业厅的工单提醒公告,由本营业厅创建的工单需要提醒，关心派单和完工。
　　　 *　截止时间，新增2个派单，新增1个完工，今天已完工5个，cfocn同步失败2个，总共0个待派单
	 */
	public String createYYTBulletin(List<SBullentionWorkCount> list,String dept_id,Date currentDate) throws Exception{
		
		int  create_total=0; //'总的待派单',
		int cfocn_failure_total=0;//'cfocn派单同步失败',
		int  cfocn_failure_new=0;// '新增cfocn派单同步失败',
		int  init_new=0;// '新增派单待完工',
		int  end_today_total=0;//'今天完工待回访',
		int  end_today_new=0;// '新增今日完工待回访'
		for(SBullentionWorkCount wc:list){
			if(dept_id==null||wc.getDept_id().equals(dept_id)){
				create_total+=wc.getCreate_total();
				cfocn_failure_total+=wc.getCfocn_failure_total();
				cfocn_failure_new+=wc.getCfocn_failure_new();
				init_new+=wc.getInit_new();
				end_today_total+=wc.getEnd_today_total();
				end_today_new+=wc.getEnd_today_new();
			}
		}
		//工单提醒有无变化判断
		if(cfocn_failure_new+init_new+end_today_new<=0){
			return null;
		}
		//组织公告数据
		StringBuilder chinaBulletin=new StringBuilder();
		StringBuilder englishBulletin=new StringBuilder();
		//截止时间，新增2个派单，新增1个完工，今天已完工5个，cfocn同步失败2个，总共0个待派单
		chinaBulletin.append("  截止").append(DateHelper.format(currentDate)).append(",");
		englishBulletin.append("  as of ").append(DateHelper.format(currentDate)).append(",");
		if(init_new>0){
			chinaBulletin.append("新增").append(init_new).append("个派单,");
			//Added five working Dispatch
			englishBulletin.append("add ").append(init_new).append("  dispatch,");
		}
		if(end_today_new>0){
			chinaBulletin.append("新增").append(end_today_new).append("个完工,");
			englishBulletin.append("add ").append(end_today_new).append(" completion,");
		}
		if(end_today_total>0){
			chinaBulletin.append("今天已完工").append(end_today_new).append("个,");
			//Today has been completed 5
			englishBulletin.append("today has been completed ").append(end_today_new).append(",");
		}
		if(cfocn_failure_total>0){
			chinaBulletin.append("cfocn派单同步失败").append(cfocn_failure_total).append("个,");
			englishBulletin.append("cfocn dispatch sync failure ").append(cfocn_failure_total).append(",");
		}
		if(create_total>0){
			chinaBulletin.append("总共").append(create_total).append("个待派单.");
			englishBulletin.append("total ").append(create_total).append(" wait dispatch.");
		}
		
		return chinaBulletin.append("\n").append(englishBulletin.toString()).toString();
	}

}
