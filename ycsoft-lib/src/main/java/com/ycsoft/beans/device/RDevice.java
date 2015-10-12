/**
 * RDevice.java	2010/06/23
 */

package com.ycsoft.beans.device;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * RDevice -> R_DEVICE mapping
 */
@POJO(tn = "R_DEVICE", sn = "SEQ_DEVICE_ID", pk = "DEVICE_ID")
public class RDevice extends RDeviceModel implements Serializable {

	// RDevice all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 7810482625793405131L;
	private String device_id;
	private String device_status;
	private String depot_status;
	private String tran_status;
	private String used;
	private String backup;
	private String freezed;
	private String diffence_type;
	private String depot_id;
	private String ownership;
	private String ownership_depot;
	private Date warranty_date;
	private String is_virtual;
	private String is_local;
	private String is_loss;
	private String is_new_stb;
	private String batch_num;
	private Integer total_num;
	private String box_no;
	
	private String ownership_text;
	private String device_status_text;
	private String depot_status_text;
	private String diffence_type_text;
	private String depot_id_text;
	private String tran_status_text;
	private String is_virtual_text;
	private String is_new_stb_text;

	/**
	 * default empty constructor
	 */
	public RDevice() {
	}


	public RDevice(String deviceType, String deviceStatus, String depotStatus) {
		setDevice_type(deviceType);
		setDevice_status(deviceStatus);
		setDepot_status(depotStatus);
		setUsed(SystemConstants.BOOLEAN_FALSE);
		setBackup(SystemConstants.BOOLEAN_FALSE);
		setFreezed(SystemConstants.BOOLEAN_FALSE);
		setDiffence_type(SystemConstants.DEVICE_DIFFENCN_TYPE_NODIFF);
		setIs_virtual(SystemConstants.BOOLEAN_FALSE);
		setIs_local(SystemConstants.BOOLEAN_FALSE);
		setTran_status(StatusConstants.IDLE);
	}


	public String getBox_no() {
		return box_no;
	}

	public void setBox_no(String box_no) {
		this.box_no = box_no;
	}

	public String getBatch_num() {
		return batch_num;
	}

	public void setBatch_num(String batch_num) {
		this.batch_num = batch_num;
	}

	public String getIs_new_stb() {
		return is_new_stb;
	}


	public void setIs_new_stb(String isNewStb) {
		is_new_stb = isNewStb;
		this.is_new_stb_text = MemoryDict.getDictName(DictKey.BOOLEAN, isNewStb);
	}


	// device_id getter and setter
	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	// device_status getter and setter
	public String getDevice_status() {
		return device_status;
	}

	public void setDevice_status(String device_status) {
		this.device_status = device_status;
		device_status_text = MemoryDict.getDictName(DictKey.STATUS, device_status);
	}

	// depot_status getter and setter
	public String getDepot_status() {
		return depot_status;
	}

	public void setDepot_status(String depot_status) {
		this.depot_status = depot_status;
		depot_status_text = MemoryDict.getDictName(DictKey.STATUS, depot_status);
	}

	// depot_id getter and setter
	public String getDepot_id() {
		return depot_id;
	}

	public void setDepot_id(String depot_id) {
		depot_id_text = MemoryDict.getDictName(DictKey.DEPOT, depot_id);
		this.depot_id = depot_id;
	}

	public String getOwnership() {
		// ownership getter and setter
		return ownership;
	}

	public void setOwnership(String ownership) {
		this.ownership = ownership;
		ownership_text = MemoryDict
				.getDictName(DictKey.DEVICE_OWNERSHIP, ownership);
	}

	// is_virtual getter and setter
	public String getIs_virtual() {
		return is_virtual;
	}

	public void setIs_virtual(String is_virtual) {
		this.is_virtual = is_virtual;
		this.is_virtual_text = MemoryDict.getDictName(DictKey.BOOLEAN, is_virtual);
	}

	// is_local getter and setter
	public String getIs_local() {
		return is_local;
	}

	public void setIs_local(String is_local) {
		this.is_local = is_local;
	}

	// warranty_date getter and setter
	public Date getWarranty_date() {
		return warranty_date;
	}

	public void setWarranty_date(Date warranty_date) {
		this.warranty_date = warranty_date;
	}

	public String getDevice_status_text() {
		return device_status_text;
	}

	public void setDevice_status_text(String device_status_text) {
		this.device_status_text = device_status_text;
	}

	public String getDepot_status_text() {
		return depot_status_text;
	}

	public void setDepot_status_text(String depot_status_text) {
		this.depot_status_text = depot_status_text;
	}

	public String getOwnership_text() {
		return ownership_text;
	}

	public void setOwnership_text(String ownership_text) {
		this.ownership_text = ownership_text;
	}

	/**
	 * @return the used
	 */
	public String getUsed() {
		return used;
	}

	/**
	 * @param used the used to set
	 */
	public void setUsed(String used) {
		this.used = used;
	}

	/**
	 * @return the freezed
	 */
	public String getFreezed() {
		return freezed;
	}

	/**
	 * @param freezed the freezed to set
	 */
	public void setFreezed(String freezed) {
		this.freezed = freezed;
	}

	/**
	 * @return the diffence_type
	 */
	public String getDiffence_type() {
		return diffence_type;
	}

	/**
	 * @param diffence_type the diffence_type to set
	 */
	public void setDiffence_type(String diffence_type) {
		diffence_type_text = MemoryDict.getDictName(DictKey.DEVICE_DIFF_TYPE, diffence_type);
		this.diffence_type = diffence_type;
	}

	/**
	 * @return the ownership_depot
	 */
	public String getOwnership_depot() {
		return ownership_depot;
	}

	/**
	 * @param ownership_depot the ownership_depot to set
	 */
	public void setOwnership_depot(String ownership_depot) {
		this.ownership_depot = ownership_depot;
	}

	/**
	 * @return the backup
	 */
	public String getBackup() {
		return backup;
	}

	/**
	 * @param backup the backup to set
	 */
	public void setBackup(String backup) {
		this.backup = backup;
	}




	/**
	 * @return the tran_status
	 */
	public String getTran_status() {
		return tran_status;
	}


	/**
	 * @param tran_status the tran_status to set
	 */
	public void setTran_status(String tran_status) {
		this.tran_status = tran_status;
		tran_status_text = MemoryDict.getDictName(DictKey.STATUS, tran_status);
	}


	/**
	 * @return the diffence_type_text
	 */
	public String getDiffence_type_text() {
		return diffence_type_text;
	}


	public String getDepot_id_text() {
		return depot_id_text;
	}


	public String getTran_status_text() {
		return tran_status_text;
	}


	public String getIs_loss() {
		return is_loss;
	}


	public void setIs_loss(String is_loss) {
		this.is_loss = is_loss;
	}


	public String getIs_virtual_text() {
		return is_virtual_text;
	}


	/**
	 * @return the is_new_stb_text
	 */
	public String getIs_new_stb_text() {
		return is_new_stb_text;
	}


	public Integer getTotal_num() {
		return total_num;
	}


	public void setTotal_num(Integer total_num) {
		this.total_num = total_num;
	}

}