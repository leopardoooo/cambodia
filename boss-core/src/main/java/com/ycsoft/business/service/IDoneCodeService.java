/**
 *
 */
package com.ycsoft.business.service;

import java.util.List;

import com.ycsoft.business.commons.abstracts.IBaseService;
import com.ycsoft.business.dto.core.cust.DoneCodeExtAttrDto;
import com.ycsoft.business.dto.core.cust.DoneInfoDto;
import com.ycsoft.business.dto.core.fee.BusiFeeDto;
import com.ycsoft.business.dto.core.fee.QueryFeeInfo;
import com.ycsoft.daos.core.Pager;

/**
 * @author YC-SOFT
 *
 */
public interface IDoneCodeService extends IBaseService{
	
	/**
	 * 根据流水号查找可以收取的费用以及已经收取过的费用的合计
	 * @param doneCode
	 * @param busiCode
	 * @return
	 * @throws Exception
	 */
	public List<BusiFeeDto> queryEditFee(String custId,Integer doneCode,String busiCode) throws Exception;
	
	
	/**
	 * 根据流水获取业务明细
	 * @param doneCode
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<DoneInfoDto> getGridDate(Integer doneCode, String custId, Integer start,Integer limit) throws Exception;
	
	/**
	 * 业务回退
	 * 业务回退指取消客户最后一比办理的业务。
	 * 回退的业务有几个条件
	 * 1、本人办理的业务
	 * 2、当天办理
	 * @param doneCode
	 * @throws Exception
	 */
	public void cancelDoneCode(Integer doneCode) throws Exception;

	/**
	 * 根据客户编号，获取办理过的业务流水
	 * @param custId
	 * @param queryFeeInfo 条件过滤对象
	 * @return
	 * @throws Exception
	 */
	Pager<DoneCodeExtAttrDto> queryByCustId(String custId, QueryFeeInfo queryFeeInfo,
			Integer start,Integer limit) throws Exception;
}
