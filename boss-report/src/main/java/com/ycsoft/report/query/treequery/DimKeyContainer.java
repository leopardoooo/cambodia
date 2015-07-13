package com.ycsoft.report.query.treequery;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 维度key装载容器
 * 
 * @author new
 * 
 */
public class DimKeyContainer implements Serializable {

	private static Map<String, DimKey> dimkeymap = new HashMap<String, DimKey>();

	public static DimKey getDimKey(String key) {
		return dimkeymap.get(key);
	}

	public static Map<String, DimKey> getDimkeymap() {
		return dimkeymap;
	}

	public static void setDimkeymap(Map<String, DimKey> dimkeymap) {
		DimKeyContainer.dimkeymap = dimkeymap;
	}
}
