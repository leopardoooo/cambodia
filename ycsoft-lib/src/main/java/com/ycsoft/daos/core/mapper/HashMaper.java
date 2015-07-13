package com.ycsoft.daos.core.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * 将结果及封装至改<code> java.util.HashMap </code>
 *
 * @author hh
 * @data Mar 26, 2010 9:34:21 AM
 */
public class HashMaper extends AbstractMapper {

	private Map<String ,Object> record ;

	public Object mapRow(ResultSet rs, int row) throws SQLException {

		if(columnsIsNull())setCurrentColumns(rs);

		record = new HashMap<String,Object>();

		for (String element : columns) {
			record.put( element.toLowerCase() ,  rs.getObject(element) );
		}

		return record;
	}

}
