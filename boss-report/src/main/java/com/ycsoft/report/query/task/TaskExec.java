package com.ycsoft.report.query.task;

import java.util.Calendar;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.report.bean.RepTask;
import com.ycsoft.report.component.query.QueryComponent;

public class TaskExec extends Thread {
	private QueryComponent queryComponent;
	
	public TaskExec(QueryComponent queryComponent){
		this.queryComponent=queryComponent;
	}
	
	private int task_max=1000;
	
	public void exec(){
		try {
			//清理3天前的一次性任务
			for(RepTask task:queryComponent.queryTaskTypeIsOne(3)){
				queryComponent.deleteRepTask(task.getTask_id());
			}
			
			RepTask task=null;
			int i=0;
			task_max=queryComponent.queryAllRepTask().size();
					
			while((task=queryComponent.queryCanExecTask())!=null){
				i++;
				if(i>task_max) {
					LoggerHelper.error(this.getClass(), "报表计划任务异常,达到任务数量上限退出！");
					return;
				}
				queryComponent.execTask(task);
				
			}
		} catch (ReportException e) {
			LoggerHelper.error(this.getClass(), "报表计划任务异常退出！",e);
		}
	}
	/**
	 * 上午六点半时间
	 * @return
	 */
	private long getCalendar(int day,int hour,int minute){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		System.out.println(DateHelper.format(c.getTime()));
		return c.getTimeInMillis();
	}
	
	public void run(){
		//每天早上6点半到下午3点之间执行任务
		try{
			while(true){
				long todaynow=System.currentTimeMillis();
				long today630=this.getCalendar(0,6,30);
				long today1500=this.getCalendar(0,15,0);
				long nextday630=this.getCalendar(1, 6, 30);
				if(todaynow<today630){
					this.sleep(today630-todaynow);
				}else if(todaynow>today1500){
					this.sleep(nextday630-todaynow);
				}else{
					this.exec();
					this.sleep(nextday630-System.currentTimeMillis());
				}
			}
		}catch(Exception e){
			LoggerHelper.error(this.getClass(), "报表任务异常退出",e);
		}
		
	}
	

}
