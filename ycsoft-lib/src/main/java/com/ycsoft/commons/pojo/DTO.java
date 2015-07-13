package com.ycsoft.commons.pojo;

/**
 * DTO接口定义，
 * Modal 与 Service/Action之间的转换对象。
 * @param T 要转换的对象类型
 * @author hh
 * @date Dec 30, 2009 10:15:26 AM
 */
public interface DTO<T> {

	/**
	 * 该抽象函数定义将当前的DTO对象装换为目标T对象
	 */
	T transform();

}
