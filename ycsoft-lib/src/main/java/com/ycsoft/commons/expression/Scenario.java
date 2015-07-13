package com.ycsoft.commons.expression;

import java.util.HashMap;
import java.util.Map;

import com.ycsoft.beans.config.TBusiCode;

public class Scenario {
	public Map<String, String> primes = new HashMap<String, String>();

	public TBusiCode busiCode = new TBusiCode();

	/**
	 *
	 * @param distStr
	 *            被包含
	 * @param includeStr
	 *            查找串
	 * @return
	 */
	public boolean includeOf(String distStr, String includeStr) {
		int s = (distStr + ",").indexOf("'" + includeStr + "',");
		if (s == -1)
			return false;
		return true;
	}

	public TBusiCode getBusiCode() {
		return busiCode;
	}

	public void setBusiCode(TBusiCode busiCode) {
		this.busiCode = busiCode;
	}
}
