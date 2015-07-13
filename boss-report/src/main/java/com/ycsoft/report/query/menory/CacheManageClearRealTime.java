package com.ycsoft.report.query.menory;

import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.report.commons.CopiedIterator;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.query.QueryContainer;
import com.ycsoft.report.query.QueryResult;

public class CacheManageClearRealTime extends Thread {
	
	public long clearCache() throws ReportException{
		Map<String, QueryResult> qrmap = QueryContainer.getQueryrealtimecon();
		long datenow=System.currentTimeMillis();
		if(qrmap.size()==0) return datenow;

		//Iterator<String> iterator=qrmap.keySet().iterator();
		CopiedIterator<String> iterator=new CopiedIterator<String>(qrmap);
		while(iterator.hasNext()){
			QueryResult qr=qrmap.get(iterator.next());
			int mi= (int)((datenow-qr.getVisitDate().getTime())/(1000*60));
			if(mi>=ReportConstants.CLEAR_REP_CACHE){
				qr.clear();
				iterator.remove();
				//LoggerHelper.debug(this.getClass(),"clear cache id:"+ qr.getQueryId());
			}
		}
		
		return datenow;
	}
	/**
	 * 定时清理报表容器中的实时查询记录
	 */
	public void run() {
		while (true) {
			try {
				Thread.sleep(ReportConstants.CLEAR_REP_CACHE * 60 * 1000);
				clearCache();
			} catch (Exception e) {
				LoggerHelper.error(CacheManageClearRealTime.class, "CLEAR_REP_CACHE_ERROR:", e);
			}
		}
	}

}
