/**
 * RDeviceSupplierDao.java	2010/09/06
 */

package com.ycsoft.business.dao.resource.device;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceSupplier;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * RDeviceSupplierDao -> R_DEVICE_SUPPLIER table's operator
 */
@Component
public class RDeviceSupplierDao extends BaseEntityDao<RDeviceSupplier> {

	/**
	 *
	 */
	private static final long serialVersionUID = -8728568757398318924L;

	/**
	 * default empty constructor
	 */
	public RDeviceSupplierDao() {}

}
