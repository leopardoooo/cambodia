package com.ycsoft.report.test.other;


//多叉树 
public class ManyNodeTree {

	// 树根
	private ManyTreeNode root;

	// 构造函数
	public ManyNodeTree() {
		root = new ManyTreeNode();
		root.getData().setNodeName("root");
	}

	// 构造函数
	public ManyNodeTree(int key) {
		root = new ManyTreeNode();
		root.getData().setKey(key);
		root.getData().setNodeName("root");
	}

	// 遍历多叉树
	public String iteratorTree(ManyTreeNode treeNode) {

		StringBuilder sb = new StringBuilder();

		if (treeNode != null) {

			if ("root".equals(treeNode.getData().getNodeName())) {
				sb.append(treeNode.getData().getKey() + ",");
			}

			for (ManyTreeNode index : treeNode.getChildList()) {

				sb.append(index.getData().getKey() + ",");

				if (index.getChildList() != null
						&& index.getChildList().size() > 0) {

					sb.append(iteratorTree(index));

				}
			}
		}

		return sb.toString();
	}

	public ManyTreeNode getRoot() {
		return root;
	}

	public void setRoot(ManyTreeNode root) {
		this.root = root;
	}

	// 构造多叉树
	public static ManyNodeTree createTree() {

		// 用构造函数的值
		ManyNodeTree tree = new ManyNodeTree(60);

		// 第一层的节点
		ManyTreeNode node1 = new ManyTreeNode(40);
		ManyTreeNode node2 = new ManyTreeNode(50);
		ManyTreeNode node3 = new ManyTreeNode(30);

		tree.getRoot().getChildList().add(0, node1);
		tree.getRoot().getChildList().add(1, node2);
		tree.getRoot().getChildList().add(2, node3);

		// 第二层的节点
		ManyTreeNode node21 = new ManyTreeNode(85);
		ManyTreeNode node22 = new ManyTreeNode(70);
		ManyTreeNode node23 = new ManyTreeNode(15);
		ManyTreeNode node24 = new ManyTreeNode(102);
		ManyTreeNode node25 = new ManyTreeNode(83);
		ManyTreeNode node26 = new ManyTreeNode(9);

		tree.getRoot().getChildList().get(0).getChildList().add(0, node21);
		tree.getRoot().getChildList().get(0).getChildList().add(1, node22);
		tree.getRoot().getChildList().get(0).getChildList().add(2, node23);

		tree.getRoot().getChildList().get(1).getChildList().add(0, node24);
		tree.getRoot().getChildList().get(1).getChildList().add(1, node25);

		tree.getRoot().getChildList().get(2).getChildList().add(0, node26);

		// 第三层的节点
		ManyTreeNode node31 = new ManyTreeNode(15);
		ManyTreeNode node32 = new ManyTreeNode(20);
		ManyTreeNode node33 = new ManyTreeNode(100);
		ManyTreeNode node44 = new ManyTreeNode(60);

		tree.getRoot().getChildList().get(0).getChildList().get(0)
				.getChildList().add(0, node31);
		tree.getRoot().getChildList().get(0).getChildList().get(0)
				.getChildList().add(1, node32);
		tree.getRoot().getChildList().get(0).getChildList().get(0)
				.getChildList().add(2, node33);

		tree.getRoot().getChildList().get(0).getChildList().get(2)
				.getChildList().add(0, node44);

		return tree;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ManyNodeTree testTree = ManyNodeTree.createTree();
		String result = testTree.iteratorTree(testTree.getRoot());
		System.out.println(result);
	}

}