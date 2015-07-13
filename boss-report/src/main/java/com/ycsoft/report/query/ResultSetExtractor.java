package com.ycsoft.report.query;

import java.sql.ResultSet;

public interface ResultSetExtractor<T> {

	T extractData(ResultSet result)throws Exception;
	
}
