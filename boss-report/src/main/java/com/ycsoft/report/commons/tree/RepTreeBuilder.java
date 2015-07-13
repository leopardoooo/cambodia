package com.ycsoft.report.commons.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.report.bean.RepKeyCon;

/**
 * 创建Tree的类
 *
 * @author hh
 * @data Mar 19, 2010 4:08:00 PM
 */
public class RepTreeBuilder {

	
	/**
	 * 将传入的节点组装成ExtJS需要的树形结构，
	 * 
	 * @param src 结构类似oracle start with connect by prior模式的结果集
	 * @param checked NULL不需要复选框，true&false是否选中或者不选择节点
	 * @return
	 */
	public static RepTreeNode createTreeByPrior(List<RepTreeNode> src, Boolean checked){
		if(src == null || src.size() == 0){ return null; }
		RepTreeNode root = src.get(0);
		Map<String,RepTreeNode> tempMap = new HashMap<String,RepTreeNode>();
		for (RepTreeNode node : src){
			node.setChecked(checked);
			node.setLeaf(true);
			RepTreeNode pnode = tempMap.get( node.getPid());
			//添加子节点
			if (pnode != null){
				pnode.getChildren().add( node );
				pnode.setLeaf(false);
				pnode.setCls("fold");
			}
			//将当前的节点添加至map，供添加属于它的子节点
			tempMap.put( node.getId() , node);
			//设置节点的样式属性
			if("T".equals(node.getIs_leaf())){
				node.setLeaf(true);
			}
		}
		return root;
	}
	
	
	/**
	 * 将传入的节点组装成ExtJS需要的树形结构，
	 * 
	 * @param src 结构类似oracle start with connect by prior模式的结果集
	 * @param checked NULL不需要复选框，true&false是否选中或者不选择节点
	 * @return
	 */
	public static RepTreeNode createTreeByPriorWithChecked(List<RepTreeNode> src, List<String> defaultSelected){
		if(src == null || src.size() == 0){ return null; }
		RepTreeNode root = src.get(0);
		Map<String,RepTreeNode> tempMap = new HashMap<String,RepTreeNode>();
		for (RepTreeNode node : src){
			node.setChecked(defaultSelected.contains(node.getId()));
			node.setLeaf(true);
			RepTreeNode pnode = tempMap.get( node.getPid());
			//添加子节点
			if (pnode != null){
				pnode.getChildren().add( node );
				pnode.setLeaf(false);
				pnode.setCls("fold");
			}
			//将当前的节点添加至map，供添加属于它的子节点
			tempMap.put( node.getId() , node);
			//设置节点的样式属性
			if("T".equals(node.getIs_leaf())){
				node.setLeaf(true);
			}
		}
		return root;
	}
	
	
	public static List<RepTreeNode> createTree(List<RepTree> src){
		return createTree( src,null);
	}
	/**
	 * 创建一个没有根节点的树，
	 * @param src 必须按照节点的顺序排序
	 * @param checked  null 不显示复选框，true 勾选复选框 ,false 不勾选复选框
	 * @return
	 */
	public static List<RepTreeNode> createTree(List<RepTree> src,Boolean checked){
		List<RepTreeNode> target = new ArrayList<RepTreeNode>();
		Map<String,RepTreeNode> tempMap = new HashMap<String,RepTreeNode>();
		for (RepTree tree : src){
			RepTreeNode node = new RepTreeNode();
			tree.transform( node );
			node.setChecked(checked);
			//如果不是叶子节点，直接修改
			if(StringHelper.isNotEmpty(node.getIs_leaf()) && node.getIs_leaf().equals("F")){
				node.setLeaf(false);
				node.setCls("fold");
			}
			RepTreeNode parentNode = tempMap.get( node.getPid() );
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
/**
 * 复选框树，src已经设定好Checked的值
 */
	public static List<RepTreeNode> createTreeCheck(List<RepTree> src){
		List<RepTreeNode> target = new ArrayList<RepTreeNode>();
		Map<String,RepTreeNode> tempMap = new HashMap<String,RepTreeNode>();
		for (RepTree tree : src){
			RepTreeNode node = new RepTreeNode();
			tree.transform( node );
			//如果不是叶子节点，直接修改
			if(StringHelper.isNotEmpty(node.getIs_leaf()) && node.getIs_leaf().equals("F")){
				node.setLeaf(false);
				node.setCls("fold");
			}
			RepTreeNode parentNode = tempMap.get( node.getPid() );
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


	public static boolean removeNullNode(List<RepTreeNode> target) {
		boolean flag = true;//true 表示所有儿子都删除了
		List<RepTreeNode> dels = new ArrayList<RepTreeNode>();
		for (RepTreeNode t : target) {
			if (t.isLeaf()) {
				//叶子不删除
				flag = false;
			} else{
				//不是叶子
				if (t.getChildren().size() > 0) {
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
		for (RepTreeNode t : dels) {
			target.remove(t);
		}
		return flag;
	}

	/**
	 * 将给定的List 对象转化为List TreeNode对象
	 * @param lst
	 * @return
	 */
	public static List<RepTreeNode> convertToNodes (List<?> lst, String id, String text)throws Exception{
		List<RepTreeNode> target = new LinkedList<RepTreeNode>();
		RepTreeNode _t = null;
		for (Object v : lst) {
			_t = new RepTreeNode();
			_t.setId( BeanUtils.getProperty( v, id ) );
			_t.setText( BeanUtils.getProperty( v, text ) );
			target.add(_t);
		}
		return target;
	}
	
	
	/**
	 * 把一个TreeLink的List按start元素开始按树形结构排序
	 * 把一个无序的List<TreeLink>,按结构类似oracle start with connect by prior模式的结果集排序
	 * 适合大批量数据排序
	 * @param list
	 * @param start
	 * @return
	 */
	public static <T extends TreeLink> List<T> orderByTree(List<T> list,String start){
		
		Map<String,List<T>> pidMap=new HashMap<String,List<T>>();
		for(T vo: list){
			if(pidMap.containsKey(vo.getPid())){
				pidMap.get(vo.getPid()).add(vo);
			}else{
				List<T> pidlist=new ArrayList<T>();
				pidlist.add(vo);
				pidMap.put(vo.getPid(), pidlist);
			}
		}
		List<T> tar=new ArrayList<T>();
		orderByTree(tar,pidMap,start);
		return tar;
	}
	
	private static <T extends TreeLink> void orderByTree(List<T> tar,Map<String,List<T>> pidMap,String start){
		
		List<T> list=pidMap.get(start);
		if(list==null||list.size()==0) return;
		for(T vo:list){
			tar.add(vo);
			orderByTree(tar,pidMap,vo.getId());
		}
	}
	
	/**
	 * 把一个TreeLink的List按start元素开始按树形结构排序
	 * 把一个无序的List<TreeLink>,按结构类似oracle start with connect by prior模式的结果集排序
	 * 适合小批量数据排序，大数量严重消耗资源
	 * @param list
	 * @param start
	 * @return
	 */
	public static List<TreeLink> orderByTree2(List<TreeLink> list,String start){
		//null转换对象
		List<TreeLink> tar=new ArrayList<TreeLink>();
		orderByTree2(list,tar,start);
		return tar;
	}
	
	/**
	 * Tree结构排序

	 * @param sourelist
	 * @param tarlist
	 * @param start
	 */
	private static void orderByTree2(List<TreeLink> sourelist,List<TreeLink> tarlist,String start){
		for(TreeLink vo:sourelist){
			if((start==null&&vo.getPid()==null)
					||(start!=null&&start.equals(vo.getPid()))){
				tarlist.add(vo);
				orderByTree2(sourelist,tarlist,vo.getId());
			}
		}
	}
	
	public static void main(String[] args){
		
		RepKeyCon o1=new RepKeyCon();
		o1.setKey("a");
		o1.setFkey("1");
		RepKeyCon o2=new RepKeyCon();
		o2.setKey("b");
		o2.setFkey("1");
		RepKeyCon o3=new RepKeyCon();
		o3.setKey("a1");
		o3.setFkey("a");
		RepKeyCon o4=new RepKeyCon();
		o4.setKey("a2");
		o4.setFkey("a");
		RepKeyCon o5=new RepKeyCon();
		o5.setKey("a3");
		o5.setFkey("a");
		RepKeyCon o6=new RepKeyCon();
		o6.setKey("a23");
		o6.setFkey("a2");
		
		RepKeyCon o8=new RepKeyCon();
		o8.setKey("b2");
		o8.setFkey("b");
		RepKeyCon o7=new RepKeyCon();
		o7.setKey("b1");
		o7.setFkey("b");
		RepKeyCon o9=new RepKeyCon();
		o9.setKey("a211");
		o9.setFkey("a2");
		
		List<TreeLink> list=new ArrayList<TreeLink>();
		list.add(o9);
		list.add(o8);
		list.add(o1);
		list.add(o7);
		list.add(o2);
		list.add(o6);
		list.add(o3);
		list.add(o5);
		list.add(o4);
		
		List<TreeLink> tar=RepTreeBuilder.orderByTree(list,"1");
		for(TreeLink vo:tar)
			System.out.println(vo.getId()+"   "+vo.getPid());
		
		Map<String,String> aa=new HashMap<String,String>();
		System.out.println(aa.containsKey(null));
		aa.put(null, "ss");
		System.out.println(aa.containsKey(null));
	}
}
