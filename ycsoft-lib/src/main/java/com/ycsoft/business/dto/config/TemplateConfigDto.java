/**
 *
 */
package com.ycsoft.business.dto.config;

import java.util.List;
import java.util.Map;

import com.ycsoft.beans.config.TConfigTemplate;
import com.ycsoft.beans.config.TTemplate;

/**
 * @author liujiaqi
 *
 */
public class TemplateConfigDto extends TTemplate {

	/**
	 *
	 */
	private static final long serialVersionUID = 2222229563875666863L;

	public enum Config {
		PROD_PUBLIC_TYPE_DETAIL,
		/** 专项公用账目是否允许缴费 */
		SPEC_PULIC_ACCTITEM_FLAG,
		/** 公用账目是否允许缴费 */
		PUBLIC_ACCTITEM_FLAG,
		/** 公用账目是否允许转账 */
		PUBLIC_ACCTITEM_TRAN_FLAG,
		/** 专用账目是否允许缴费 */
		SPEC_ACCTITEM_FLAG,
		/** 使用发票 */
		USE_INVOICE,
		/** 发票使用级别 */
		SCOPE_INVOICE,
		/** 设备使用级别 */
		SCOPE_DEVICE,		
		/** 轧帐级别 */
		GRIP_ACCOUNT,
		/** 报亭是否计费**/
		IS_RSTOP_FEE,
		/** 最低折扣**/
		LOWEST_DISCT,
		/** 专用账目允许缴费需要满足的规则**/
		SPEC_ACCTITEM_RULE,
		/** 专项公用账目允许缴费需要满足的规则**/
		SPEC_PULIC_ACCTITEM_RULE,
		/**是否手工设置发票打印内容*/
		GEN_INVOICE_MANUAL,
		/**是否自动订购*/
		AUTO_ORDER_VOD,
		/**基本包欠费抹零*/
		OWE_FEE_NUMBER,
		/**宽带欠费抹零*/
		BAND_OWE_FEE_NUMBER,
		/**小额减免天数*/
		BASE_EASY_ADJUST_DAYS,
		/**默认不使用公用产品类型配置**/
		PROD_PUBLIC_TYPE,
		/**默认不使用公用例外明细配置**/
		PROD_NONE_PUBLIC_TYPE,
		/**控制打印标记天数**/
		PRINT_DATE_CFG,
		/**欠费后自动改为长期欠费天数**/
		OWN_LONG_DAYS,
		/**自动退订天数**/
		PROD_CANCEL_DAYS
	};

	private List<TConfigTemplate> configList = null;
	private Map<String, TConfigTemplate> configMap = null;

	/**
	 * @return the configList
	 */
	public List<TConfigTemplate> getConfigList() {
		return configList;
	}

	/**
	 * @param configList
	 *            the configList to set
	 */
	public void setConfigList(List<TConfigTemplate> configList) {
		this.configList = configList;
	}

	/**
	 * @return the configMap
	 */
	public Map<String, TConfigTemplate> getConfigMap() {
		return configMap;
	}

	/**
	 * @param configMap
	 *            the configMap to set
	 */
	public void setConfigMap(Map<String, TConfigTemplate> configMap) {
		this.configMap = configMap;
	}

	public TConfigTemplate get(String configValue) {
		return configMap.get(configValue);
	}

}
