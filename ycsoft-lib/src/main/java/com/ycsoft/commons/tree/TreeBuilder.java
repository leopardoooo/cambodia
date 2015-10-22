package com.ycsoft.commons.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.ycsoft.beans.prod.PProd;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;

/**
 * 创建Tree的类
 *
 * @author hh
 * @data Mar 19, 2010 4:08:00 PM
 */
public class TreeBuilder {


	public static List<TreeNode> createTree(List<Tree> src){
		return createTree( src,null);
	}
	/**
	 * 创建一个没有根节点的树，
	 * @param src 必须按照节点的顺序排序
	 * @param checked  null 不显示复选框，true 勾选复选框 ,false 不勾选复选框
	 * @return
	 */
	public static List<TreeNode> createTree(List<Tree> src,Boolean checked){
		List<TreeNode> target = new ArrayList<TreeNode>();
		Map<String,TreeNode> tempMap = new HashMap<String,TreeNode>();
		for (Tree tree : src){
			TreeNode node = new TreeNode();
			tree.transform( node );
			node.setChecked(checked);
			//如果不是叶子节点，直接修改
			if(StringHelper.isNotEmpty(node.getIs_leaf()) && node.getIs_leaf().equals("F")){
				node.setLeaf(false);
				node.setCls("fold");
			}
			TreeNode parentNode = tempMap.get( node.getPid() );
			if (parentNode == null){
				target.add(node);

			} else {
				parentNode.getChildren().add( node );
				parentNode.setLeaf(false);
				parentNode.setCls("fold");
			}
			tempMap.put( node.getId() , node);
		}
		removeNullNode(target);
		return target;
	}
	
	
	public static List<TreeNode> createAdreeTree(List<Tree> src,boolean falseChildren){
		List<TreeNode> target = new ArrayList<TreeNode>();
		Map<String,TreeNode> tempMap = new HashMap<String,TreeNode>();
		for (Tree tree : src){
			TreeNode node = new TreeNode();
			node.setChildren(null);
			tree.transform( node );
			
			//如果不是叶子节点，直接修改
			if(StringHelper.isNotEmpty(node.getIs_leaf()) && node.getIs_leaf().equals("F")){
				node.setLeaf(false);
				node.setCls("fold");
			}
			TreeNode parentNode = tempMap.get( node.getPid() );
			if (parentNode == null){
				target.add(node);

			} else {
				if(parentNode.getChildren() == null){
					parentNode.setChildren(new ArrayList<TreeNode>());
				}
				parentNode.setExpanded(true);
				parentNode.getChildren().add( node );
				parentNode.setLeaf(false);
				parentNode.setCls("fold");
			}
			tempMap.put( node.getId() , node);
		}
		if(falseChildren){
			removeNullNode(target);
		}
		return target;
	}
	
	public static List<AddrTreeNode> createSysAdreeTree(List<AddrTree> src){
		List<AddrTreeNode> target = new ArrayList<AddrTreeNode>();
		Map<String,AddrTreeNode> tempMap = new HashMap<String,AddrTreeNode>();
		for (AddrTree tree : src){
			AddrTreeNode node = new AddrTreeNode();
			node.setChildren(null);
			tree.transform( node );
			
			//如果不是叶子节点，直接修改
			if(StringHelper.isNotEmpty(node.getIs_leaf()) && node.getIs_leaf().equals("F")){
				node.setLeaf(false);
				node.setCls("fold");
			}
			AddrTreeNode parentNode = tempMap.get( node.getPid() );
			if (parentNode == null){
				target.add(node);

			} else {
				if(parentNode.getChildren() == null){
					parentNode.setChildren(new ArrayList<AddrTreeNode>());
				}
				parentNode.setExpanded(true);
				parentNode.getChildren().add( node );
				parentNode.setLeaf(false);
				parentNode.setCls("fold");
			}
			tempMap.put( node.getId() , node);
		}
		return target;
	}
	
	
	public static List<AddrTreeNode> createSysTree(List<AddrTree> src,boolean isExpanded){
		List<AddrTreeNode> target = new ArrayList<AddrTreeNode>();
		Map<String,AddrTreeNode> tempMap = new HashMap<String,AddrTreeNode>();
		for (AddrTree tree : src){
			AddrTreeNode node = new AddrTreeNode();
			node.setChildren(null);
			tree.transform( node );
			
			//如果不是叶子节点，直接修改
			if(StringHelper.isNotEmpty(node.getIs_leaf()) && node.getIs_leaf().equals("F")){
				node.setLeaf(false);
				node.setCls("fold");
			}
			AddrTreeNode parentNode = tempMap.get( node.getPid() );
			if (parentNode == null){
				target.add(node);

			} else {
				if(parentNode.getChildren() == null){
					parentNode.setChildren(new ArrayList<AddrTreeNode>());
				}
				if(isExpanded){
					parentNode.setExpanded(true);
				}
				parentNode.getChildren().add( node );
				parentNode.setLeaf(false);
				parentNode.setCls("fold");
			}
			tempMap.put( node.getId() , node);
		}
		return target;
	}
	
	
	public static List<TreeNode> createAdreeSynchronousTree(List<Tree> src){
		List<TreeNode> target = new ArrayList<TreeNode>();
		Map<String,TreeNode> tempMap = new HashMap<String,TreeNode>();
		for (Tree tree : src){
			TreeNode node = new TreeNode();
			tree.transform( node );
			
			//如果不是叶子节点，直接修改
			if(StringHelper.isNotEmpty(node.getIs_leaf()) && node.getIs_leaf().equals("F")){
				node.setLeaf(false);
				node.setCls("fold");
			}
			TreeNode parentNode = tempMap.get( node.getPid() );
			if (parentNode == null){
				target.add(node);

			} else {
				parentNode.setExpanded(true);
				parentNode.getChildren().add( node );
				parentNode.setLeaf(false);
				parentNode.setCls("fold");
			}
			tempMap.put( node.getId() , node);
		}
		return target;
	}
	
	
/**
 * 复选框树，src已经设定好Checked的值
 */
	public static List<TreeNode> createTreeCheck(List<Tree> src){
		List<TreeNode> target = new ArrayList<TreeNode>();
		Map<String,TreeNode> tempMap = new HashMap<String,TreeNode>();
		for (Tree tree : src){
			TreeNode node = new TreeNode();
			tree.transform( node );
			//如果不是叶子节点，直接修改
			if(StringHelper.isNotEmpty(node.getIs_leaf()) && node.getIs_leaf().equals("F")){
				node.setLeaf(false);
				node.setCls("fold");
			}
			TreeNode parentNode = tempMap.get( node.getPid() );
			if (parentNode == null){
				target.add(node);

			} else {
				parentNode.getChildren().add( node );
				parentNode.setLeaf(false);
				parentNode.setCls("fold");
			}
			tempMap.put( node.getId() , node);
		}
		removeNullNode(target);
		return target;
	}


	public static boolean removeNullNode(List<TreeNode> target) {
		boolean flag = true;//true 表示所有儿子都删除了
		List<TreeNode> dels = new ArrayList<TreeNode>();
		for (TreeNode t : target) {
			if (t.isLeaf()) {
				//叶子不删除
				flag = false;
			} else{
				//不是叶子
				if (t.getChildren()!=null && t.getChildren().size() > 0) {
					//有儿子
					if (removeNullNode(t.getChildren()))
						dels.add(t);
					else{
						flag = false;
					}
				}else{
					//没有儿子
					dels.add(t);
				}
			}
		}
		for (TreeNode t : dels) {
			target.remove(t);
		}
		return flag;
	}

	/**
	 * 将给定的List 对象转化为List TreeNode对象
	 * @param lst
	 * @return
	 */
	public static List<TreeNode> convertToNodes (List<?> lst, String id, String text)throws Exception{
		List<TreeNode> target = new LinkedList<TreeNode>();
		TreeNode _t = null;
		for (Object v : lst) {
			_t = new TreeNode();
			_t.setId( BeanUtils.getProperty( v, id ) );
			_t.setText( BeanUtils.getProperty( v, text ) );
			target.add(_t);
		}
		return target;
	}
	
	/**
	 * 将给定的List 对象转化为List TreeNode对象
	 * @param lst
	 * @return
	 */
	public static List<TreeNode> convertToProdNodes (List<PProd> lst, String id, String text)throws Exception{
		List<TreeNode> target = new LinkedList<TreeNode>();
		TreeNode _t = null;
		for (PProd v : lst) {
			_t = new TreeNode();
			_t.setId( BeanUtils.getProperty( v, id ) );
			if(v.getIs_invalid().equals(SystemConstants.BOOLEAN_TRUE)){
				_t.setText( "<font color=red>"+BeanUtils.getProperty( v, text )+"</font>" );
			}else{
				_t.setText( BeanUtils.getProperty( v, text ) );
			}
			_t.getOthers().put("optrId", v.getOptr_id());
			_t.getOthers().put("countyId", v.getCounty_id());
			target.add(_t);
		}
		return target;
	}
}
