package com.ycsoft.daos.abstracts;

import com.ycsoft.daos.core.impl.EntitySessionImpl;

/**
 *
 * 继承该类，可以拥有一些常用的数据库操作函数。必须制定 T 的类型。
 *
 * 实体类的类型，通过<code>@POJO注释指定相关的表信息</code>
 *
 * @author hh
 * @date Jan 14, 2010 9:28:19 AM
 */
public class BaseEntityDao<T> extends EntitySessionImpl<T> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5492385469068154032L;

}
