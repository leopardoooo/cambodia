package com.ycsoft.report.query.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 多叉树节点
 * @author new
 *
 * @param <T>
 */
public class QManyNode<T> {

	// 节点的内容
	private QData<T> data;
	// 子节点容器
	private Map<String, QManyNode<T>> childMap;
	//父节点
	private QManyNode<T> parentNode;



	// 构造函数
	public QManyNode() {
		this.data = null;
		this.parentNode=null;
	}

	public QManyNode(QData<T> data) {
		this.data = data;
		this.parentNode=null;
	}

	/**
	 * 增加一个子节点
	 * @param childdata
	 * @return QManyNode<T>
	 */
	public QManyNode<T> addChild(QData<T> childdata) {
		initChildMap();
		// 如果已存在该节点则 执行节点内容的运算功能，否则装入该节点
		if (childMap.containsKey(childdata.getKey())) {
			QManyNode<T> node=childMap.get(childdata.getKey());
			node.getNodeData().operationData(childdata.getData());
			return node;
		} else {
			QManyNode<T> node=new QManyNode<T>(childdata);
			node.parentNode=this;
			childMap.put(childdata.getKey(),node);
			return node;
		}
	}

	/**
	 * 获取一个子节点ByData
	 * @param childdata
	 * @return
	 */
	public QManyNode<T> getChild(QData<T> childdata) {
		if (childMap == null||childdata==null)
			return null;
		return this.childMap.get(childdata.getKey());
	}

	public void initChildMap() {
		if (childMap == null) {
			childMap = new HashMap<String, QManyNode<T>>();
		}
	}

	/**
	 * 按QData.key排序的子节点列表
	 * 
	 * @return
	 */
	public List<QManyNode<T>> getChildList() {
		if(childMap==null) return null;
		Object[] keys= getChildKeyS();
		List<QManyNode<T>> list=new ArrayList<QManyNode<T>>(keys.length);
		for(Object key:keys)
			list.add(childMap.get(key.toString()));
		return list;
	}

	/**
	 * 是否树叶
	 * 
	 * @return
	 */
	public boolean isLeaf() {
		if (childMap == null)
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @return
	 */
	public Object[] getChildKeyS() {
		if (childMap == null)
			return null;

		Object[] keys = childMap.keySet().toArray();
		java.util.Arrays.sort(keys);
		return keys;
	}

	public QData<T> getNodeData() {
		return data;
	}
	public QManyNode<T> getParentNode() {
		return parentNode;
	}
}
