/**
 *
 */
package com.ycsoft.business.dto.config;

import java.util.List;
import java.util.Map;

import com.ycsoft.beans.config.TTemplate;
import com.ycsoft.beans.config.TUpdateCfg;

/**
 * @author liujiaqi
 *
 */
public class TemplateUpdatePorpDto extends TTemplate {

	/**
	 *
	 */
	private static final long serialVersionUID = 184160559259399749L;
	private List<TUpdateCfg> updateList = null;
	private Map<String, List<TUpdateCfg>> updateMap = null;
	/**
	 * @return the updateList
	 */
	public List<TUpdateCfg> getUpdateList() {
		return updateList;
	}
	/**
	 * @param updateList the updateList to set
	 */
	public void setUpdateList(List<TUpdateCfg> updateList) {
		this.updateList = updateList;
	}
	/**
	 * @return the updateMap
	 */
	public Map<String, List<TUpdateCfg>> getUpdateMap() {
		return updateMap;
	}
	/**
	 * @param updateMap the updateMap to set
	 */
	public void setUpdateMap(Map<String, List<TUpdateCfg>> updateMap) {
		this.updateMap = updateMap;
	}
	public List<TUpdateCfg> get(String busiCode) {
		return updateMap.get(busiCode);
	}

}
