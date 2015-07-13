package com.ycsoft.report.query.treequery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.bean.RepColumn;
import com.ycsoft.report.bean.RepHead;
import com.ycsoft.report.commons.FileObjectInputStream;
import com.ycsoft.report.commons.FileObjectOutputStream;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.dto.InitQueryDto;
import com.ycsoft.report.query.QueryManage;
import com.ycsoft.report.query.QueryManageImpl;
import com.ycsoft.report.query.QueryResult;
import com.ycsoft.report.query.QueryResultCommon;
import com.ycsoft.report.query.tree.QManyNode;
import com.ycsoft.report.query.tree.QManyTree;

/**
 * 列可扩展的查询结果集
 * @author new
 */
public class QueryResultQuiee extends QueryResultCommon {

	public QueryResultQuiee(InitQueryDto qdto) throws ReportException {
		super(qdto);
		// TODO Auto-generated constructor stub
	}

	// 查询列属性
	class QueryRsmd  {
		public String OlapType = null;

		public String super_id = null;// 当前设置的最高级的id
		public DimKey super_dimkey;// 当前设置的最高级dim_key
		public String super_desc;

		public DimKey dimkey;

		public String ID = null;
		public String name = null;
		
		public double total=0.0;
	}

	public void QueryResultOlap111(InitQueryDto initQueryDto) throws ReportException {
		FileObjectInputStream foi = null;// 读文件
		FileObjectOutputStream foo = null;// 写文件
		try {
			// 声明了一颗树
			QManyTree<List<Double>> qManyTree = new QManyTree<List<Double>>();
			this.query_id = initQueryDto.getQuery_id();
			this.database = ReportConstants.DATABASETYPE_HISTROY;
			

			QueryManage qm = new QueryManageImpl();
			QueryResult queryresult = qm.get(initQueryDto.getHistory_query_id());
			this.rep_id=queryresult.getRepId();
			foo = new FileObjectOutputStream(ReportConstants.REP_TEMP_TXT+ initQueryDto.getQuery_id());
			foi = new FileObjectInputStream(ReportConstants.REP_TEMP_TXT+ queryresult.getQueryId());

			List<QueryRsmd> qrlist = this.getQueryRsmdList(initQueryDto);// 设置属性列
			// 查询头保存
			saveHead(foi, foo, qrlist);

			List<String> list = null;
			while ((list = (List<String>) foi.readObject()) != null
					&& foi.getRowIndex() <= queryresult.getRowSize()) {
				// 根节点
				QManyNode<List<Double>> node = qManyTree.getRoot();
				// 获取需要sum的数据列
				List<Double> datalist = new ArrayList<Double>();
				for (int i = 0; i < qrlist.size(); i++) {
					QueryRsmd qr = qrlist.get(i);
					if (qr.OlapType.equals(ReportConstants.OLAP_TYPE_TOTAL)) {
						Double d = Double.valueOf(list.get(i));
						datalist.add(d);
					}
				}

				// 生成树
				for (int i = 0; i < qrlist.size(); i++) {
					QueryRsmd qr = qrlist.get(i);
					DimKey dimkey = qr.dimkey;
					qr.ID = list.get(i);
					qr.name = qr.dimkey.getName(qr.ID);
					// 取对应最高级的dimkey的ID,Name
					while (dimkey != qr.super_dimkey) {
						dimkey = DimKeyContainer.getDimKey(dimkey.getPkey());
						if (dimkey == null)
							break;
						qr.ID = DimKeyContainer.getDimKey(dimkey.getSkey())
								.getPid(qr.ID);
						qr.name = dimkey.getName(qr.ID);
					}
					// 不计算和页面选择不一致的ID代表的行
					if (qr.super_id != null && !"".equals(qr.super_id)
							&& !qr.super_id.equals(qr.ID))
						break;
					if (i + 1 < qrlist.size()
							&& qrlist.get(i).OlapType
									.equals(ReportConstants.OLAP_TYPE_EXTEND)
							&& qrlist.get(i + 1).OlapType
									.equals(ReportConstants.OLAP_TYPE_EXTEND))
						// 下一个属性还是扩展属性时
						node = node
								.addChild(new QueryData(qr.ID, qr.name, null));
					else {// 下一个属性不是扩展属性时,记录数据列，并退出循环
						node.addChild(new QueryData(qr.ID, qr.name, datalist));
						break;
					}
				}
			}
			//把统计内容保存到文件
			iteratorTreeToFile(foo, qManyTree.getRoot(),qrlist);
			// 把最后合计项写入文件
			foo.writeObject(getTail(qrlist));
			// 去掉查询头那一行
			this.rows = foo.size() - 1;
		} catch (IOException e) {
			throw new ReportException(e);
		} catch (ClassNotFoundException e) {
			throw new ReportException(e);
		} catch (Exception e) {
			throw new ReportException("system_error:", e);
		} finally {
			try {
				if (foi != null)
					foi.close();
			} catch (Exception e) {
			}
			try {
				if (foo != null)
					foo.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 把树写入文件 递归遍历到树叶，然后通过树叶查找上级，生成一个完整List保存到文件
	 * 
	 * @param foo
	 * @param treeNode
	 * @throws IOException
	 * @throws ReportException
	 */
	public void iteratorTreeToFile(FileObjectOutputStream foo,
			QManyNode<List<Double>> treeNode,List<QueryRsmd> qrlist) throws IOException,
			ReportException {
		if (treeNode == null)
			throw new ReportException("root_treeNode is null");
		for (QManyNode<List<Double>> index : treeNode.getChildList()) {
			if (!index.isLeaf())
				iteratorTreeToFile(foo, index,qrlist);
			else {
				List<String> list = new ArrayList<String>();
				for (int i = index.getNodeData().getData().size() - 1, j=qrlist.size()-1; i >= 0&&j>=0; i--,j--){
					Double d=index.getNodeData().getData().get(i);
					qrlist.get(j).total=qrlist.get(j).total+d;
					list.add(d.toString());
				}
				list.add(index.getNodeData().getName());
				list.add(index.getNodeData().getKey());
				QManyNode<List<Double>> parentNode = index.getParentNode();
				while (parentNode != null && parentNode.getNodeData() != null) {
					list.add(parentNode.getNodeData().getName());
					list.add(parentNode.getNodeData().getKey());
					parentNode = parentNode.getParentNode();
				}
				List<String> write = new ArrayList<String>(list.size());
				for (int i = list.size() - 1; i >= 0; i--) {
					write.add(list.get(i));
				}
				foo.writeObject(write);
			}
		}
	}

	/**
	 * 设置列属性
	 * 
	 * @param initQueryDto
	 * @return
	 */
	private List<QueryRsmd> getQueryRsmdList(InitQueryDto initQueryDto) {

		List<QueryRsmd> qrlist = new ArrayList<QueryRsmd>();
		for (int i = 0; i < initQueryDto.getColumnlist().size(); i++) {
			RepColumn column = initQueryDto.getColumnlist().get(i);
			QueryRsmd qr = new QueryRsmd();
			qr.OlapType = column.getOlap_type();
			if (column.getOlap_type().equals(ReportConstants.OLAP_TYPE_EXTEND)) {
				qr.super_id = column.getDim_id_select();
				qr.super_dimkey = DimKeyContainer.getDimKey(column
						.getDim_key_select());
				qr.dimkey = DimKeyContainer.getDimKey(column.getDim_key());
			}
			qrlist.add(qr);
		}
		return qrlist;
	}

	/**
	 * 重置查询头
	 * 
	 * @param foi
	 * @param foo
	 * @param qrlist
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void saveHead(FileObjectInputStream foi,
			FileObjectOutputStream foo, List<QueryRsmd> qrlist)
			throws IOException, ClassNotFoundException {
		List<List<RepHead>> headlist = new ArrayList<List<RepHead>>();
		//原查询头，报表定义配置或默认的
		List<List<RepHead>> historylist = (List<List<RepHead>>) foi.readObject();
		
		for (int i = 0; i < historylist.size(); i++) {
			List<RepHead> list = historylist.get(i);
			List<RepHead> headrow = new ArrayList<RepHead>();
			for (RepHead rephead : list) {
				QueryRsmd qr = qrlist.get(rephead.getCol_seq());
				if (qr.OlapType.equals(ReportConstants.OLAP_TYPE_EXTEND)) {
					rephead.setOlap_type(ReportConstants.OLAP_TYPE_EXTEND);
					headrow.add(rephead);
					// ID对应显示列
					RepHead showrephead = new RepHead();
					if (i == 0){
						DimKey dimkey=qr.dimkey;
						qr.super_desc=dimkey.getDesc();
						while (dimkey != qr.super_dimkey) {
							dimkey = DimKeyContainer.getDimKey(dimkey.getPkey());
							if (dimkey == null)
								break;
							qr.super_desc=dimkey.getDesc();
						}
						showrephead.setCol_desc(qr.super_desc);
					}
					showrephead.setCol_length(rephead.getCol_length());
					headrow.add(showrephead);
				} else {
					headrow.add(rephead);
				}
			}
			headlist.add(headrow);
		}
		foo.writeObject(headlist);
	}
	
	private List<String> getTail(List<QueryRsmd> qrlist){
		List<String> taillist = new ArrayList<String>();
		for (QueryRsmd qr : qrlist) {
			if (qr.OlapType.equals(ReportConstants.OLAP_TYPE_TOTAL))
				taillist.add(String.valueOf(qr.total));
			else{
				taillist.add("");
				taillist.add("");
			}
		}
		return taillist;
	}
}
