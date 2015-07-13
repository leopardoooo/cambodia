package com.ycsoft.login;

import java.util.HashMap;
import java.util.Map;

public class SubSystem {
	private static Map<String, String> serversMap = new HashMap<String, String>();

	static {
		try {
			serversMap = new HashMap<String, String>();
			String s = LoadPropertie.getInstance().getProperty("servers");
			if (s != null && !s.equals("")) {
				String[] serverIds = s.split(",");
				for (String id : serverIds) {
					String url = LoadPropertie.getInstance().getProperty(
							"servers." + id + ".url");
					serversMap.put(id, url);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据子系统编号获取相应的url
	 * @param sub_system_id
	 * @return
	 */
	public static String gUrl(String sub_system_id) {

		return serversMap.get(sub_system_id);
	}
}
