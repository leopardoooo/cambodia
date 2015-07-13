package com.ycsoft.sysmanager.dto.prod;

import com.ycsoft.beans.prod.PProdDict;
import com.ycsoft.commons.tree.Tree;
import com.ycsoft.commons.tree.TreeNode;

public class PProdDictDto extends PProdDict implements Tree {
	/**
	 *
	 */
	private static final long serialVersionUID = 5818033123462387263L;

	public void transform(TreeNode node) {
		node.setId(getNode_id());
		node.setPid(getNode_pid());
		node.setText(getNode_name());
		node.setChecked(getChecked());
		node.setLeaf(true);
		node.getOthers().put("countyId", getCounty_id());
		node.getOthers().put("areaId", getArea_id());
	}

}
