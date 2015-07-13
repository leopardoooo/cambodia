/**
 * RStbModel.java	2010/06/23
 */

package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * RStbModel -> R_STB_MODEL mapping
 */
@POJO(tn = "R_STB_MODEL", sn = "", pk = "DEVICE_MODEL")
public class RStbModel extends RDeviceModel implements Serializable {

	// RStbModel all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -5395472234610382638L;
	private String interactive_type;
	private String definition_type;
	private String virtual_card_model;
	private String virtual_modem_model;

	private String interactive_type_text;
	private String definition_type_text;
	private String model_type_name;


	/**
	 * default empty constructor
	 */
	public RStbModel() {
	}

	// interactive_type getter and setter
	public String getModel_type_name() {
		return model_type_name;
	}
	
	public void setModel_type_name(String modelTypeName) {
		model_type_name = modelTypeName;
	}
	public String getInteractive_type() {
		return interactive_type;
	}

	public void setInteractive_type(String interactive_type) {
		interactive_type_text = MemoryDict.getDictName(DictKey.DTV_SERV_TYPE,
				interactive_type);
		this.interactive_type = interactive_type;
	}

	// definition_type getter and setter
	public String getDefinition_type() {
		return definition_type;
	}

	public void setDefinition_type(String definition_type) {
		definition_type_text = MemoryDict.getDictName(DictKey.STB_DEFINITION,
				definition_type);
		this.definition_type = definition_type;
	}

	// virtual_card_model getter and setter
	public String getVirtual_card_model() {
		return virtual_card_model;
	}

	public void setVirtual_card_model(String virtual_card_model) {
		this.virtual_card_model = virtual_card_model;
	}

	/**
	 * @return the interactive_type_text
	 */
	public String getInteractive_type_text() {
		return interactive_type_text;
	}

	/**
	 * @return the definition_type_text
	 */
	public String getDefinition_type_text() {
		return definition_type_text;
	}

	public String getVirtual_modem_model() {
		return virtual_modem_model;
	}

	public void setVirtual_modem_model(String virtual_modem_model) {
		this.virtual_modem_model = virtual_modem_model;
	}


}