package com.ycsoft.business.dao.core.bill;

/**
 * TvOrderDao.java	2011/09/02
 */
 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.bill.TvOrder;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TvOrderDao -> TV_ORDER table's operator
 */
@Component
public class TvOrderDao extends BaseEntityDao<TvOrder> {
	
	/**
	 * default empty constructor
	 */
	public TvOrderDao() {}

}
