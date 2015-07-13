package com.ycsoft.business.dto.core.fee;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.ycsoft.beans.config.TBusiFeeStd;

public class BusiFeeItemDto{
	private String fee_id;
	private String fee_name;
	private List<TBusiFeeStd> feeStandards;
	private String busi_code ;

	public static List<BusiFeeItemDto> converBeanFee(List<BusiFeeDto> fees)
			throws IllegalAccessException, InvocationTargetException {
		String tempFee_id = "";
		String tempBusi_code = "";
		List<BusiFeeItemDto> result = new ArrayList<BusiFeeItemDto>();
		List<TBusiFeeStd> feeStandards = null;
		for (BusiFeeDto fee : fees) {
			if (!tempFee_id.equals(fee.getFee_id())
					|| !tempBusi_code.equals(fee.getBusi_code())) {
				BusiFeeItemDto busiFeeItemDto = new BusiFeeItemDto();
				result.add(busiFeeItemDto);
				BeanUtils.copyProperties(busiFeeItemDto, fee);
				feeStandards = new ArrayList<TBusiFeeStd>();
				busiFeeItemDto.setFeeStandards(feeStandards);
				tempFee_id = fee.getFee_id();
				tempBusi_code = fee.getBusi_code();
			}
			TBusiFeeStd tBusiFeeStandard = new TBusiFeeStd();
			BeanUtils.copyProperties(tBusiFeeStandard, fee);
			feeStandards.add(tBusiFeeStandard);
		}
		return result;
	}

	public String getBusi_code() {
		return busi_code;
	}

	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}

	public String getFee_id() {
		return fee_id;
	}

	public void setFee_id(String feeId) {
		fee_id = feeId;
	}

	public String getFee_name() {
		return fee_name;
	}

	public void setFee_name(String feeName) {
		fee_name = feeName;
	}

	public List<TBusiFeeStd> getFeeStandards() {
		return feeStandards;
	}

	public void setFeeStandards(List<TBusiFeeStd> feeStandards) {
		this.feeStandards = feeStandards;
	}
}
