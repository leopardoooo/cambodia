package com.ycsoft.commons.abstracts;

import com.ycsoft.daos.core.impl.AbstractSessionImpl;

/**
 *
 * <b>Dao 基类定义 ,只针对底层Spring JDBCTemplate 进行的封装， 如果要使用更多的函数，请查看
 * <code>BaseEntityDao</code></b>
 * <ul>
 * <li>提供系统通用的功能</li>
 * <li>用于以后更容易扩展。</li>
 * <ul>
 *
 * @see AbstractSessionImpl
 *
 * @author hh
 * @date Dec 30, 2009 10:10:56 AM
 */
public class BaseDao extends AbstractSessionImpl {

	/**
	 *
	 */
	private static final long serialVersionUID = -7483155297977388374L;

}