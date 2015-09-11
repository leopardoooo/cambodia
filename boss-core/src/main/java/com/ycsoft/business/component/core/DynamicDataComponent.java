package com.ycsoft.business.component.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.print.CDoc;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.core.common.CDoneCodeInfoDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.print.CDocFeeDao;
import com.ycsoft.business.dao.core.print.CDocItemDao;
import com.ycsoft.business.dto.core.print.PrintDate;
import com.ycsoft.business.dto.core.print.PrintItemDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.CollectionHelper;

/**
 * 提供获取动态数据的方法
 *
 * @author YC-SOFT
 */
@Component
public class DynamicDataComponent extends BaseBusiComponent {

	private CCustDao cCustDao;
	private CDocItemDao cDocItemDao;
	private CDoneCodeInfoDao cDoneCodeInfoDao;
	private CDocFeeDao cDocFeeDao;

	/**
	 * printItems方法的参数必须和下面的configDocItems的参数一致！！！
	 * @param params
	 * @param cust
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public void printItems(Map<String, Object> params, CCust cust) throws Exception {
		CDoc doc = (CDoc) params.get("doc");
		if (doc != null){
			if(cust !=null && cust.getCust_type().equals(SystemConstants.CUST_TYPE_NONRESIDENT)){		
				List<PrintItemDto> list = cDocItemDao.queryNonresCustBySn(doc.getDoc_sn());
				
				Map<String,List<PrintItemDto>> map = CollectionHelper.converToMap(list, "printitem_name");
				List<PrintItemDto> pList = new ArrayList<PrintItemDto>();
				for(String key : map.keySet()){
					List<PrintItemDto> printList = map.get(key);
					PrintItemDto pp = printList.get(0);
					int money = 0;
					String docitem_sn = "";
					for(PrintItemDto p : printList){
						money += p.getAmount();
						docitem_sn += p.getDocitem_sn()+",";
					}
					pp.setAmount(money);
					pp.setDocitem_sn(docitem_sn.substring(0,docitem_sn.length()-1));
					pList.add(pp);
				}
				
				params.put("printItems", pList);
				params.put("count", cDocFeeDao.queryByDocSn(doc.getDoc_sn()));
			}else{
				//单位的需要传客户类型
				if(cust != null){
					params.put("printItems", cDocItemDao.queryBySn(doc.getDoc_sn(),cust.getCust_type(), String.valueOf(params.get("invoiceId")), String.valueOf(params.get("invoiceCode"))));
				}else{
					params.put("printItems", cDocItemDao.queryBySn(doc.getDoc_sn(),null, String.valueOf(params.get("invoiceId")), String.valueOf(params.get("invoiceCode"))));
				}
				
			}
		}
	}
	
	public void configDocItems(Map<String, Object> params, CCust cust) throws Exception {
		String[] doneCodes = (String[]) params.get("doneCodes");
			params.put("docItems", cDoneCodeInfoDao.queryPrintConfig(doneCodes));
//		CDoneCode[] doneCodes = (CDoneCode[]) params.get("doneCodes");
//		if (doneCodes != null && doneCodes.length > 0) {
//			params.put("docItems", cDocDao.queryByDoneCodes(doneCodes));
//		}
	}

	public void setCCustDao(CCustDao custDao) {
		cCustDao = custDao;
	}

	/**
	 * 获取客户信息，默认调用，无需再单据进行配置
	 * @param custId
	 * @return
	 * @throws Exception
	 */
//	public CustFullInfoDto getCustInfo(String custId) throws Exception {
//		if (StringHelper.isNotEmpty(custId)){
//			Pager<Map<String,Object>> p = new Pager<Map<String,Object>>();
////			Map<String,Object> cond = new HashMap<String,Object>();
////			cond.put("t1.cust_id", custId);
////			p.setParams( cond );
//			List<CustFullInfoDto> lst = cCustDao.queryCustFullInfo( p , getOptr().getCounty_id()).getRecords();
//			return lst.size() > 0 ? lst.get(0): null;
//		}
//		return null;
//	}

	/**
	 * 获取打印时间
	 * 默认调用，无需再单据进行配置
	 *
	 * @return
	 */
	public PrintDate getPrintDate() {
		return new PrintDate();
	}

	public void setCDocItemDao(CDocItemDao docItemDao) {
		cDocItemDao = docItemDao;
	}

	/**
	 * @param doneCodeInfoDao the cDoneCodeInfoDao to set
	 */
	public void setCDoneCodeInfoDao(CDoneCodeInfoDao doneCodeInfoDao) {
		cDoneCodeInfoDao = doneCodeInfoDao;
	}

	public void setCDocFeeDao(CDocFeeDao docFeeDao) {
		cDocFeeDao = docFeeDao;
	}
}
