/**
 * 
 */
package com.ycsoft.business.service.externalImpl;

import java.util.Date;
import java.util.List;

import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.beans.core.acct.CAcctBank;
import com.ycsoft.beans.core.acct.CAcctPreFee;
import com.ycsoft.beans.core.bank.CBankAgree;
import com.ycsoft.beans.core.bank.CBankPay;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dto.core.acct.AcctDto;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.daos.core.JDBCException;

/**
 * @author liujiaqi
 * 
 */
public interface IPayServiceExternal {

	List<CFee> queryFeeByBankTransCode(BusiParameter p, String startTransCode,
			String endTransCode, String countyId);

	void batchEditAcctDate(BusiParameter p, List<CFee> feeList, String dateToStr);

}
