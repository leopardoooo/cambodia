/**
 *
 */
package com.ycsoft.business.dto.core.cust;

import com.ycsoft.beans.core.cust.CCustDevice;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

/**
 * @author liujiaqi
 *
 */
public class CustDeviceDto extends CCustDevice {
	/**
	 *
	 */
	private static final long serialVersionUID = -3301974200890518612L;

	private String ownership;
	private String device_model;
	private String pair_stb_device_id;
	private String is_virtual_card;
	private String is_virtual_modem;
	private String definition_type;
	private String depot_id;

	private String depot_name;
	private String ownership_text;
	private String interactive_type;
	private String device_model_text;
	private String definition_type_text;

	public String getDefinition_type() {
		return definition_type;
	}

	public void setDefinition_type(String definition_type) {
		this.definition_type = definition_type;
		this.definition_type_text = MemoryDict.getDictName(DictKey.STB_DEFINITION, definition_type);
	}

	public String getDefinition_type_text() {
		return definition_type_text;
	}

	/**
	 * @return the ownership
	 */
	public String getOwnership() {
		return ownership;
	}

	/**
	 * @param ownership
	 *            the ownership to set
	 */
	public void setOwnership(String ownership) {
		this.ownership = ownership;
		ownership_text = MemoryDict
		.getDictName(DictKey.DEVICE_OWNERSHIP, ownership);
	}

	/**
	 * @return the ownership_text
	 */
	public String getOwnership_text() {
		return ownership_text;
	}

	/**
	 * @param ownership_text
	 *            the ownership_text to set
	 */
	public void setOwnership_text(String ownership_text) {
		this.ownership_text = ownership_text;
	}

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public String getDevice_model_text() {
		String deviceType = getDevice_type();
		if(!"STB".equals(deviceType) && !"CARD".equals(deviceType) && !"MODEM".equals(deviceType)){
			deviceType = "CTL";
		}
		device_model_text = MemoryDict.getDictName(deviceType+"_MODEL", device_model);
		return device_model_text;
	}

	/**
	 * @return the pair_stb_device_id
	 */
	public String getPair_stb_device_id() {
		return pair_stb_device_id;
	}

	/**
	 * @param pair_stb_device_id the pair_stb_device_id to set
	 */
	public void setPair_stb_device_id(String pair_stb_device_id) {
		this.pair_stb_device_id = pair_stb_device_id;
	}

	public String getIs_virtual_card() {
		return is_virtual_card;
	}

	public void setIs_virtual_card(String is_virtual_card) {
		this.is_virtual_card = is_virtual_card;
	}

	public String getIs_virtual_modem() {
		return is_virtual_modem;
	}

	public void setIs_virtual_modem(String is_virtual_modem) {
		this.is_virtual_modem = is_virtual_modem;
	}

	public String getDepot_id() {
		return depot_id;
	}

	public void setDepot_id(String depot_id) {
		this.depot_id = depot_id;
		depot_name = MemoryDict.getDictName(DictKey.DEPT, depot_id);
	}

	public String getDepot_name() {
		return depot_name;
	}

	public void setDepot_name(String depot_name) {
		this.depot_name = depot_name;
	}

	/**
	 * @return the interactive_type
	 */
	public String getInteractive_type() {
		return interactive_type;
	}

	/**
	 * @param interactive_type the interactive_type to set
	 */
	public void setInteractive_type(String interactive_type) {
		this.interactive_type = interactive_type;
	}
	
}
