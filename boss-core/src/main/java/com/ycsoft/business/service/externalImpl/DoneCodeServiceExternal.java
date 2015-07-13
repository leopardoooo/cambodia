/**
 * 
 */
package com.ycsoft.business.service.externalImpl;

import java.util.List;

import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dto.core.fee.BusiFeeDto;
import com.ycsoft.business.service.impl.DoneCodeService;

/**
 * @author liujiaqi
 * 
 */
public class DoneCodeServiceExternal extends ParentService implements
		IDoneCodeServiceExternal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IDoneCodeServiceExternal#queryEditFee(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.Integer, java.lang.String)
	 */
	public List<BusiFeeDto> queryEditFee(BusiParameter p, String custId,
			Integer doneCode, String busiCode) throws Exception {
		DoneCodeService doneCodeService = (DoneCodeService) getBean(
				DoneCodeService.class, p);
		return doneCodeService.queryEditFee(custId, doneCode, busiCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IDoneCodeServiceExternal#cancelDoneCode(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.Integer)
	 */
	public void cancelDoneCode(BusiParameter p, Integer doneCode)
			throws Exception {
		DoneCodeService doneCodeService = (DoneCodeService) getBean(
				DoneCodeService.class, p);
		doneCodeService.cancelDoneCode(doneCode);
	}

}
