package com.ycsoft.report.query.tree;
/**
 * 
 * @author new
 *
 * @param <T>
 */
public class QManyTree<T> {
	// 树根
	private QManyNode<T> root;

	// 构造函数
	public QManyTree(){
		root=new QManyNode<T>();
	}
	
	public QManyTree(QData<T> data){
		root=new QManyNode<T>(data);
	}

	// 遍历多叉树
	public String iteratorTree(QManyNode<T> treeNode) {
		StringBuilder sb = new StringBuilder();
		if (treeNode != null) {
			if(treeNode.getNodeData()!=null)
				sb.append(treeNode.getNodeData().getKey()+",");
			if(treeNode.isLeaf()) return sb.toString();
			for (QManyNode<T> index : treeNode.getChildList()) {
				sb.append(index.getNodeData().getKey() + ",");
				if (!index.isLeaf() ) 
					sb.append(iteratorTree(index));
			}
		}
		return sb.toString();
	}

	/**
	 * 获取根节点
	 * @return
	 */
	public QManyNode<T> getRoot() {
		return root;
	}
}
