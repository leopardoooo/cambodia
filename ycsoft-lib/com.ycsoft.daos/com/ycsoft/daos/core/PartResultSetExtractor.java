/*
 * @(#)PartResultSetExtractor.java 1.0.0 Jul 8, 2011 8:32:53 AM 
 *
 * Copyright 2011 YaoChen, Ltd. All rights reserved.
 * YaoChen PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.ycsoft.daos.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;


/**
 * <p>从游标读取数据时，按照给定的条数读取，读取到给定的条数时，
 * 则调用处理器，直到所有的结果集读取完毕。</p>
 * 
 * @see org.springframework.jdbc.core.ResultSetExtractor
 * @author allex
 */
public class PartResultSetExtractor<T> implements ResultSetExtractor<Object>{

	//默认从结果集中获取的记录条数
	public static final int DEFAULT_LIMIT = 1000;
	
	//default ResultSet's fetchSize
	public static final int DEFAULT_FETCH_SIZE = DEFAULT_LIMIT;
	
	private DataHandler<T> dataHandler;

	private int limit = PartResultSetExtractor.DEFAULT_LIMIT;

	private BeanPropertyRowMapper<T> rowMapper;

	//当前的fetch次数
	private int fetchCount = 0;

	public PartResultSetExtractor(Class<T> cls, DataHandler<T> dataHandler) {
		rowMapper = ParameterizedBeanPropertyRowMapper.newInstance(cls);
		this.dataHandler = dataHandler;
	}
	
	public PartResultSetExtractor(Class<T> cls, DataHandler<T> dataHandler, int limit) {
		this(cls, dataHandler);
		this.limit = limit;
	}

	public Object extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		boolean cursorEnd = false;

		try {
			while (rs.next()) {
				final int rowNum = fetchCount * limit;
				List<T> rows = new ArrayList<T>( limit );
				rows.add(rowMapper.mapRow(rs, rowNum));
				for (int i = 1; i < limit; i++) {
					if (rs.next()) {
						rows.add(rowMapper.mapRow(rs, rowNum + i));
					} else {
						cursorEnd = true;
						break;
					}
				}
				fetchCount++;
				// data handler
				dataHandler.fetchRows(rows, fetchCount);
				// clear data
				//rows.clear();

				// check cursor
				if (cursorEnd) {
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
