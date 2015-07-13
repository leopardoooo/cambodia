package com.ycsoft.report.query.key;

import java.io.Serializable;

/**
 * 条件
 * 基本接口，所有维，度量，查询条件，内存键，系统键，权限控制的
 */
public interface ConKey extends Serializable {

	public String getId() ;

	public String getName() ;
}
