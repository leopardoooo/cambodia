package com.yaochen.boss;

import com.yaochen.myquartz.Constants;
import com.yaochen.myquartz.config.QrtzJobDetail;
import com.yaochen.myquartz.util.MyQuartzUtils;


/**
 * 编程式的启动一个JOB用例
 * 
 * @author allex
 */
public class Bootstrap2 {

	//
	private static QrtzJobDetail getExecutingJob()throws Exception{
		QrtzJobDetail job = new QrtzJobDetail();
		job.setJob_name("Test_job");
		job.setJob_pool(Constants.JOB_POOL_NONE);
		job.setJob_bean("orderExpChangeStatusJob");
		job.setTrigger_type(Constants.TRIGGER_TYPE_SIMPLE);
		job.setRepeat_count(0);
		job.setRepeat_interval(1000L);
		
		return job;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		QrtzJobDetail job = getExecutingJob();
		
		//start quartz container
		MyQuartzUtils.initialize(new String[]{"spring-beans.xml", "spring-client.xml"}, job).start();
	}

}
