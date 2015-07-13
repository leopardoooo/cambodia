
package com.ycsoft.business.dto.core.prod;

import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdDict;
import com.ycsoft.commons.helper.CnToSpell;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.Tree;
import com.ycsoft.commons.tree.TreeNode;

/**
 */
public class ProdDictDto extends PProdDict implements Tree  {
	/**
	 *
	 */
	private static final long serialVersionUID = -7348786270886453726L;

	private PProd pProd;

	private String is_leaf;


	/* (non-Javadoc)
	 * @see com.ycsoft.commons.tree.Tree#transform(com.ycsoft.commons.tree.TreeNode)
	 */
	public void transform(TreeNode node) {
		node.setId(getNode_id());
		node.setPid(getNode_pid());
		node.setText(getNode_name());
		node.setLeaf(true);
		node.setCls("file");
		node.setIs_leaf(is_leaf);
		if (pProd!=null){
			node.getOthers().put("just_for_once",pProd.getJust_for_once());
			node.getOthers().put("is_bank_pay", pProd.getIs_bank_pay());
			node.getOthers().put("is_base",pProd.getIs_base());
			node.getOthers().put("invalid_date",DateHelper.dateToStr(pProd.getInvalid_date()) );
			String prodName = pProd.getProd_name();
			node.getOthers().put("attr", StringHelper.append(prodName,"_",CnToSpell.getPinYin(prodName),"_",CnToSpell.getPinYinHeadChar(prodName)));
		}
	}

	/**
	 * @return
	 */
	public String getIs_leaf() {
		return is_leaf;
	}

	/**
	 * @param is_leaf
	 */
	public void setIs_leaf(String is_leaf) {
		this.is_leaf = is_leaf;
	}

	/**
	 * @return the pProd
	 */
	public PProd getPProd() {
		return pProd;
	}

	/**
	 * @param prod the pProd to set
	 */
	public void setPProd(PProd prod) {
		pProd = prod;
	}
}
