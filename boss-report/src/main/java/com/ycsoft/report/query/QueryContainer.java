package com.ycsoft.report.query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.ycsoft.report.commons.ReportConstants;

/**
 * 查询容器:装载查询对象
 * 
 * @author new
 * 
 */
public class QueryContainer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2792097485438634563L;

	private static Map<String, QueryResult> querycontainer = new HashMap<String, QueryResult>();

	private static Map<String, QueryResult> queryrealtimecon = new HashMap<String, QueryResult>();

	public static void addRepQuery(QueryResult query,String isrealtimequery) {
		querycontainer.put(query.getQueryId(), query);

		if (ReportConstants.VALID_F.equals(isrealtimequery))
			queryrealtimecon.put(query.getQueryId(), query);
	}

	public static void deleteRepQuery(QueryResult query) {
		if (querycontainer.containsKey(query.getQueryId())) {
			querycontainer.remove(query.getQueryId());
			queryrealtimecon.remove(query.getQueryId());
			query.clear();
		}
	}

	public static QueryResult getRepQuery(String query_id) {
		if (querycontainer.containsKey(query_id)) {
			return querycontainer.get(query_id);
		} else {
			return null;
		}
	}


	public static Map<String, QueryResult> getQuerycontainer() {
		return querycontainer;
	}

	public static Map<String, QueryResult> getQueryrealtimecon() {
		return queryrealtimecon;
	}

}
