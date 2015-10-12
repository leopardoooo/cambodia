package com.yaochen.boss.job;

import java.util.Date;

import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.TaskComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;

/**
 * 工单公告提醒JOB
 * @author new
 *
 */
@Service
public class TaskBulletinJob implements Job2 {

	/**
	 * 上次执行检查的时间戳
	 */
	private static Date queryTimeStamp=null;
	@Autowired
	private TaskComponent taskComponent;
	
	@Override
	public void execute(Job2ExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 创建营业厅的工单提醒公告
	 *　由本营业厅创建的工单需要提醒，关心派单和完工。
　　　 *　截止当前工单，总共0个待派单，2个待完工。今天已派1个。今天已完工1个。2个派单cfocn同步失败。
	 * @param deptId 部门ID
	 * @param currentTimeStamp 当前时间戳
	 * @return
	 * @throws Exception 
	 */
	public String createYYTBulletin(String deptId,Date currentTimeStamp) throws Exception{
		//有无新增派单
		int initAdd=taskComponent.queryInitAddCntByDeptId(deptId, currentTimeStamp);
		//有无新增完工
		int endAdd=taskComponent.queryEndAddCntByDeptId(deptId, currentTimeStamp);
		if(initAdd+endAdd<=0){
			//无新增不更新公告
			return null;
		}
		
		
		return null;
	}

}
