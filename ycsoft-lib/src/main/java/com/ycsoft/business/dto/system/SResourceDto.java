package com.ycsoft.business.dto.system;

import com.ycsoft.beans.system.SResource;
import com.ycsoft.commons.tree.Tree;
import com.ycsoft.commons.tree.TreeNode;

/**
 * 资源菜单Dto
 *
 * @author hh
 * @data Mar 19, 2010 5:29:34 PM
 */
public class SResourceDto extends SResource implements Tree {

	/**
	 *
	 */
	private static final long serialVersionUID = -3851586942883712534L;

	public void transform(TreeNode node) {
		node.setId( getRes_id() );
		node.setPid( getRes_pid() );
		node.setText( getRes_name() );
		node.setLeaf(true);
		node.getOthers().put("url", getUrl());
	}

}
