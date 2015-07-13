package com.ycsoft.commons.tree;


/**
 * 该类对树形结构数据进行的接口封装
 *
 * @author hh
 * @data Mar 19, 2010 3:53:33 PM
 */
public interface Tree {

	/**
	 * 所有的目标对象都必须实现改函数
	 * @param node
	 */
	public abstract void transform(TreeNode node);

}
