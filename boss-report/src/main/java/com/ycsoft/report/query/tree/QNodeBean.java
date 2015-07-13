package com.ycsoft.report.query.tree;

public class QNodeBean implements QData<Double> {
	
	private String key;//
	
	private Double data;//节点保存的数据

	public QNodeBean(String key,Double data){
		this.key=key;
		if(data==null) this.data=0.0;
		this.data=data;
	}
	public String getKey() {
		return key;
	}

	public Double getData() {
		return data;
	}

	public void operationData(Double data) {
		this.data=this.data+data;	
	}
	

	public static void main(String[] args){
		// 创建一颗树
		QManyTree<Double> tree = new QManyTree<Double>();

		// 第一层的节点
		QNodeBean node1 =  (new QNodeBean("1",1.0));
		QNodeBean node2 =  (new QNodeBean("2",2.0));
		QNodeBean node3 =  (new QNodeBean("3",3.0));
		QNodeBean node4 =  (new QNodeBean("3",4.0));
		tree.getRoot().addChild(node1);
		tree.getRoot().addChild(node2);
		tree.getRoot().addChild(node3);
		tree.getRoot().addChild(node4);
		// 第二层的节点
		QNodeBean node21 =  (new QNodeBean("21",1.0));
		QNodeBean node22 =  (new QNodeBean("22",2.0));
		QNodeBean node23 =  (new QNodeBean("21",1.0));
		QNodeBean node24 =  (new QNodeBean("24",4.0));
		QNodeBean node25 =  (new QNodeBean("25",5.0));
		QNodeBean node26 =  (new QNodeBean("25",5.0));

		tree.getRoot().getChild(node1).addChild(node21);
		tree.getRoot().getChild(node2).addChild(node22);
		tree.getRoot().getChild(node1).addChild(node23);
		tree.getRoot().getChild(node3).addChild(node24);
		tree.getRoot().getChild(node1).addChild(node25);
		tree.getRoot().getChild(node2).addChild(node26);


		// 第三层的节点
		QNodeBean node31 =  (new QNodeBean("31",1.0));
		QNodeBean node32 = (new QNodeBean("32",2.0));
		QNodeBean node33 =  (new QNodeBean("31",3.0));
		QNodeBean node34 =  (new QNodeBean("32",4.0));

		tree.getRoot().getChild(node2).getChild(node22).addChild(node31);
		tree.getRoot().getChild(node2).getChild(node25).addChild(node32);
		tree.getRoot().getChild(node3).getChild(node24).addChild(node33);
		tree.getRoot().getChild(node1).getChild(node25).addChild(node34);
		
		System.out.println(tree.iteratorTree(tree.getRoot()));
	}
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
