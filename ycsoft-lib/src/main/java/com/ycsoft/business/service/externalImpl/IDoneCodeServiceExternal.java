/**
 * 
 */
package com.ycsoft.business.service.externalImpl;

import java.util.List;

import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dto.core.fee.BusiFeeDto;

/**
 * @author liujiaqi
 * 
 */
public interface IDoneCodeServiceExternal {

	List<BusiFeeDto> queryEditFee(BusiParameter p,String custId, Integer doneCode,
			String busiCode) throws Exception;

	void cancelDoneCode(BusiParameter p, Integer doneCode) throws Exception;
	
}
