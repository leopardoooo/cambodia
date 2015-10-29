package com.ycsoft.sysmanager.component.resource;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TConfigTemplate;
import com.ycsoft.beans.depot.RDepotDefine;
import com.ycsoft.beans.invoice.RInvoice;
import com.ycsoft.beans.invoice.RInvoiceDetail;
import com.ycsoft.beans.invoice.RInvoiceInput;
import com.ycsoft.beans.invoice.RInvoiceOptr;
import com.ycsoft.beans.invoice.RInvoiceTransfer;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dao.config.TConfigTemplateDao;
import com.ycsoft.business.dao.resource.device.RDepotDefineDao;
import com.ycsoft.business.dao.resource.invoice.RInvoiceDao;
import com.ycsoft.business.dao.resource.invoice.RInvoiceDetailDao;
import com.ycsoft.business.dao.resource.invoice.RInvoiceInputDao;
import com.ycsoft.business.dao.resource.invoice.RInvoiceOptrDao;
import com.ycsoft.business.dao.resource.invoice.RInvoiceTransferDao;
import com.ycsoft.business.dao.system.SDeptDao;
import com.ycsoft.business.dto.config.TemplateConfigDto;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.DataRightLevel;
import com.ycsoft.commons.constants.InvoiceOptrType;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.resource.invoice.InvoiceDepotDto;
import com.ycsoft.sysmanager.dto.resource.invoice.InvoiceDetailDto;
import com.ycsoft.sysmanager.dto.resource.invoice.InvoiceDto;
import com.ycsoft.sysmanager.dto.system.RDepotDto;

/**
 * @author danjp
 *
 */
@Component
public class InvoiceComponent extends BaseComponent {

	private RInvoiceDao rInvoiceDao;
	private RInvoiceInputDao rInvoiceInputDao;
	private RInvoiceDetailDao rInvoiceDetailDao;
	private RInvoiceTransferDao rInvoiceTransferDao;
	private RInvoiceOptrDao rInvoiceOptrDao;
	private RDepotDefineDao rDepotDefineDao;
	@Autowired
	private TConfigTemplateDao tConfigTemplateDao;
	@Autowired
	private SDeptDao sDeptDao;
	
	/**
	 * 查找操作员管理的仓库
	 * @param optrId
	 * @return
	 * @throws JDBCException
	 * @throws ComponentException
	 */
	private String findDepot(SOptr optr) throws Exception,
			ComponentException {
		/*String dataRight = queryDataRightCon(optr, DataRight.INVOICE_MNG.toString());
		if (DataRightLevel.DEPT.toString().equals(dataRight)) {
			return optr.getDept_id();
		} else {
			return optr.getCounty_id();
		}*/
//		因为有切换仓库功能，所以只取操作员当前操作的仓库
		return optr.getDept_id();
	}
	
	private String findQueryDepot(SOptr optr) throws Exception,
			ComponentException {
		String dataRight = queryDataRightCon(optr, DataRight.INVOICE_MNG.toString());
		if (DataRightLevel.DEPT.toString().equals(dataRight)) {
			return optr.getDept_id();
		} else if (DataRightLevel.COUNTY.toString().equals(dataRight)){
			return  " SELECT depot_id "+
					" FROM vew_invoice_depot s "+
					" start with depot_pid = '"+optr.getCounty_id()+"' "+
					" connect by prior s.depot_id = s.depot_pid "+
					" union all "+
					" SELECT depot_id FROM vew_invoice_depot where depot_id = '"+optr.getCounty_id()+"'";

		} else {
			return null;
		}
	}


	/**
	 * 查找可以调拨的仓库
	 * @param optrId
	 * @return
	 * @throws Exception
	 */
	public List<RDepotDefine> queryTransDepot(SOptr optr) throws Exception {
		String depotId = findDepot(optr);
		return rDepotDefineDao.queryInvoiceTransDepot(depotId);
	}
	
	/**
	 * 查找可以调拨的仓库(定额发票)
	 * @param optrId
	 * @return
	 * @throws Exception
	 */
	public List<SDept> queryQuotaInvoiceTransDepot(SOptr optr) throws Exception {
		String depotId = findDepot(optr);
		return rDepotDefineDao.queryDeptForTransById(depotId);
	}


	/**
	 * 根据发票号查询发票
	 * @param invoiceId
	 * @return
	 * @throws JDBCException 
	 */
	public List<RInvoice> queryInvoiceByCountyId(String invoiceId,String countyId) throws JDBCException {
			if(countyId.equals(SystemConstants.COUNTY_ALL)){
				return rInvoiceDao.getInvoiceByInvoiceId(invoiceId);
			}else{
				return rInvoiceDao.queryInvoiceByCountyId(invoiceId,countyId);
			}
	}
	
	/**
	 * 检查当前发票号码段是否存在
	 * @param startInvoiceId
	 * @param endInvoiceId
	 * @return
	 * @throws Exception
	 */
	public boolean checkInvoic(String startInvoiceId,String endInvoiceId,SOptr optr) throws Exception {
		String depotId = findDepot(optr);
		return rInvoiceDao.checkInvoic(startInvoiceId, endInvoiceId,depotId);
	}

	/**
	 * 返回补零字符串
	 * @param zero 补零位数
	 * @return
	 */
	private String transferZero(long num,String zero){
		DecimalFormat df = new DecimalFormat(zero);
		return df.format(num);
	}

	private static final String INVOICE_ID_ZERO = "00000000";//发票号码8位
	private static final String INVOICE_BOOK_ID_ZERO = "0000000000";//发票本号10位
	private static final int INVOICE_ID_LEN = 8;//发票号码8位
	private static final int INVOICE_BOOK_ID_LEN = 10;//发票本号10位
	/**
	 * 发票录入
	 * @param invoiceDto
	 * @param optrId
	 * @param countyId
	 * @throws JDBCException
	 */
	public void saveInvoiceInput(InvoiceDto invoiceDto,SOptr optr) throws Exception{
		Integer doneCode = gDoneCode();

		String invoiceCode = invoiceDto.getInvoice_code();
		String invoiceType = invoiceDto.getInvoice_type();//发票类型
		String startInvoiceId = invoiceDto.getStart_invoice_id();//开始发票号
		String endInvoiceId = invoiceDto.getEnd_invoice_id();//结束发票号
		int invoice_amount = invoiceDto.getInvoice_amount();//定额发票面额

		//发票号
		int lenthnum = startInvoiceId.length();
		int intStartInvoiceId = Integer.parseInt(startInvoiceId);
		int intEndInvoiceId = Integer.parseInt(endInvoiceId);
		//发票本号

		String depotId = findDepot(optr);
		RInvoiceInput invoiceInput = new RInvoiceInput();
		invoiceInput.setCreate_time(DateHelper.now());
		invoiceInput.setDone_code(doneCode);
		invoiceInput.setInvoice_count(intEndInvoiceId -
				intStartInvoiceId+1);
		invoiceInput.setInvoice_type(invoiceType);
		invoiceInput.setOptr_id(optr.getOptr_id());
		invoiceInput.setDepot_id(depotId);
		
		if (invoiceInput.getInvoice_count()>10000){
			throw new ComponentException("发票入库每次只能入库10000张！");
		}

		List<RInvoice> invoiceList = new ArrayList<RInvoice>();
		String invoiceId = "";
		RInvoice invoice = null;
		for(int i=intStartInvoiceId;i<=intEndInvoiceId;i++){

			invoiceId = StringHelper.leftWithZero(i + "", lenthnum);
			boolean flag = rInvoiceDao.isExistsInvoice(invoiceId, invoiceCode);
			if(flag){
				throw new ComponentException("发票已存在,发票号: " + invoiceId
						+ ", 代码: " + invoiceCode);
			}
			invoice = new RInvoice();
			invoice.setInvoice_id(invoiceId);
			invoice.setDepot_id(depotId);
			invoice.setInvoice_code(invoiceCode);
			invoice.setInvoice_book_id(invoiceCode);
			invoice.setInvoice_type(invoiceType);
			invoice.setStatus(SystemConstants.INVOICE_STATUS_IDLE);
			invoice.setFinance_status(SystemConstants.INVOICE_STATUS_IDLE);
			invoice.setInvoice_amount(invoice_amount);
			invoiceList.add(invoice);
		}
		rInvoiceDao.save(invoiceList.toArray(new RInvoice[invoiceList.size()]));
		rInvoiceInputDao.save(invoiceInput);
		saveDetails(doneCode, invoiceList);
	}

	/**
	 * 根据以下条件几个或者多个查询r_invoice.
	 * @param startBookId		开始本号
	 * @param endBookId			结束本号
	 * @param startInvoiceId	开始发票号
	 * @param endInvoiceId		结束发票号
	 * @param isLoss			是否丢失
	 * @param optr				当前操作员
	 * @param invoiceCode		发票代码
	 * @param optrId			领票人
	 * @return
	 * @throws Exception
	 */
	public List<RInvoice> queryInvoice(String startBookId,String endBookId,
			String startInvoiceId,String endInvoiceId,String isLoss,SOptr optr,String invoiceCode) throws Exception {
		String depotId = findDepot(optr); 
		List<RInvoice> list = rInvoiceDao.queryInvoice(startBookId, endBookId,
				startInvoiceId, endInvoiceId, depotId, isLoss,invoiceCode);
		return list==null?new ArrayList<RInvoice>():list;
	}
	
	public List<RInvoice> queryInvoiceExceptQuota(String startBookId,String endBookId,
			String startInvoiceId,String endInvoiceId,SOptr optr,String invoiceCode) throws Exception {
		String depotId = findDepot(optr);
		List<RInvoice> list = rInvoiceDao.queryInvoiceExceptQuota(startBookId,
				endBookId, startInvoiceId, endInvoiceId, depotId,invoiceCode);
		return list==null?new ArrayList<RInvoice>():list;
	}
	
	public List<RInvoice> queryQuotaInvoice(String startBookId,String endBookId,
			String startInvoiceId,String endInvoiceId,String isLoss,SOptr optr,String invoiceCode) throws Exception {
		String depotId = findDepot(optr);
		List<RInvoice> list = rInvoiceDao.queryQuotaInvoice(startBookId,
				endBookId, startInvoiceId, endInvoiceId, depotId,isLoss,invoiceCode);
		return list==null?new ArrayList<RInvoice>():list;
	}

	/**
	 * 查找可以操作的发票
	 * @param optrType  操作类型  1调拨  2结账  3取消结账 4核销  5取消核销
	 * @param optrId
	 * @param startBookId
	 * @param endBookId
	 * @param startInvoiceId
	 * @param endInvoiceId
	 * @return
	 * @throws Exception
	 */

	public List<RInvoice> queryInvoiceForOptr(String optrType,SOptr optr,String startBookId,String endBookId,
			String startInvoiceId,String endInvoiceId,String invoiceCode,String optrId) throws Exception{
		List<RInvoice> list = null;
		
		//发票调拨(不含定额票)
		if(optrType.equals(InvoiceOptrType.TRANS.toString())){
			//查询 除定额发票外的其他发票
			list = queryInvoiceExceptQuota(startBookId, endBookId, startInvoiceId,
					endInvoiceId, optr,invoiceCode);
		} else if(optrType.equals(InvoiceOptrType.QUOTA_TRANS.toString())
				|| optrType.equals(InvoiceOptrType.QUOTA_LOSS.toString())
				|| optrType.equals(InvoiceOptrType.QUOTA_ADJUST.toString()) ){		//定额票下发、挂失、调账
			//查询 定额发票
			list = queryQuotaInvoice(startBookId, endBookId, startInvoiceId,
					endInvoiceId,SystemConstants.BOOLEAN_FALSE, optr,invoiceCode);
		} else if(optrType.equals(InvoiceOptrType.QUOTA_CANCEL_LOSS.toString())){	//定额票取消挂失
			//查询 定额发票
			list = queryQuotaInvoice(startBookId, endBookId, startInvoiceId,
					endInvoiceId,SystemConstants.BOOLEAN_TRUE, optr,invoiceCode);
		} else{
			list = queryInvoice(startBookId, endBookId, startInvoiceId,
					endInvoiceId, SystemConstants.BOOLEAN_FALSE, optr,invoiceCode);
		}
		for (int i=list.size()-1 ;i>=0;i--){
			RInvoice invoice = list.get(i);
			if (optrType.equals(InvoiceOptrType.TRANS.toString())
					|| optrType.equals(InvoiceOptrType.RECEIVE.toString())
					|| optrType.equals(InvoiceOptrType.CANCEL_RECEIVE.toString())
					|| optrType.equals(InvoiceOptrType.EDITSTATUS.toString())
					|| optrType.equals(InvoiceOptrType.REFUND.toString())
					|| optrType.equals(InvoiceOptrType.QUOTA_TRANS.toString())
					|| optrType.equals(InvoiceOptrType.QUOTA_LOSS.toString())
					|| optrType.equals(InvoiceOptrType.QUOTA_CANCEL_LOSS.toString())
					) {
				// 发票状态只能为空闲
				//定额票 只有空闲状态才能挂失
				if (!invoice.getStatus().equals(StatusConstants.IDLE)){
					list.remove(i);
					continue;
				}
				// 未领用的才能领用 
				if(optrType.equals(InvoiceOptrType.RECEIVE.toString()) && StringHelper.isNotEmpty(invoice.getOptr_id())){
					list.remove(i);
				}
				if(optrType.equals(InvoiceOptrType.CANCEL_RECEIVE.toString()) && 
						(StringHelper.isEmpty(optrId) || !optrId.equals(invoice.getOptr_id() ) ) ) {
					//传入的操作员(领用人)为空或者发票的领用人不是传入的领用人
					list.remove(i);
				}
			} else if(optrType.equals(InvoiceOptrType.CHECK.toString())) {//发票状态不为空闲并且财务状态为空闲 结账
				if (StatusConstants.IDLE.equals(invoice.getStatus())
						|| !StatusConstants.IDLE.equals(invoice.getFinance_status())){
					list.remove(i);
				}
			} else if(optrType.equals(InvoiceOptrType.CLOSE.toString())) {//财务状态为核销
				if (!StatusConstants.CHECKED.equals(invoice.getFinance_status())){
					list.remove(i);
				}

			} else if(optrType.equals(InvoiceOptrType.CANCELCHECK.toString())) {//财务状态为取消结账
				if (!StatusConstants.CHECKED.equals(invoice.getFinance_status())){
					list.remove(i);
				}

			} else if(optrType.equals(InvoiceOptrType.CANCELCLOSE.toString())) {//财务状态为核销 取消核销
				if (!StatusConstants.CLOSE.equals(invoice.getFinance_status())){
					list.remove(i);
				}

			} else if(optrType.equals(InvoiceOptrType.QUOTA_ADJUST.toString())) {
				//定额发票调账 调实际金额, 状态为使用，未结账
				if (!StatusConstants.USE.equals(invoice.getStatus())
						|| !StatusConstants.IDLE.equals(invoice.getFinance_status())){
					list.remove(i);
				}

			}
		}
		return list;
	}
	
	/**
	 *  查找可以修改状态的发票
	 * @param optrType
	 * @param optrId 
	 * @param startInvoiceBook
	 * @param endInvoiceBook
	 * @param startInvoiceId
	 * @param endInvoiceId
	 * @param status
	 * @return
	 * @throws Exception 
	 */
	public List<RInvoice> queryInvoiceForEdit(String optrType, SOptr optr,
			String startInvoiceBook, String endInvoiceBook,
			String startInvoiceId, String endInvoiceId, String status, String invoiceType,String invoiceCode) throws Exception{
		String depotId = findDepot(optr);
		
		List<String> statusList = new ArrayList<String>();
		
		if(invoiceType.equals(SystemConstants.DOC_TYPE_QUOTA)){		//定额发票
			if (status.equals(StatusConstants.IDLE)){
				//定额发票,只能从使用状态改为空闲，不能从失效状态改为空闲
				statusList.add(StatusConstants.USE);
			} else if(status.equals(StatusConstants.INVALID)){
				statusList.add(StatusConstants.IDLE);
				statusList.add(StatusConstants.USE);
			} else if(status.equals(StatusConstants.USE)){
				statusList.add(StatusConstants.IDLE);
				statusList.add(StatusConstants.INVALID);
			}
		} else {
			if (status.equals(StatusConstants.IDLE)){
				statusList.add(StatusConstants.INVALID);
			} else if(status.equals(StatusConstants.INVALID)){
				statusList.add(StatusConstants.IDLE);
			}
		}
		return this.rInvoiceDao.queryInvoice(invoiceCode,invoiceType, startInvoiceBook, endInvoiceBook,
				startInvoiceId, endInvoiceId, depotId, statusList
						.toArray(new String[statusList.size()]));
	}
	/**
	 * 根据发票id查询发票详细信息
	 * @param invoiceId
	 * @return
	 * @throws Exception
	 */
	public InvoiceDto queryInvoiceByInvoiceId(String invoiceId,String invoiceBookId,String invoiceCode) throws Exception {
		boolean flag = rInvoiceDao.isExistsInvoice(invoiceId, invoiceCode);
		if(!flag){
			throw new ComponentException(ErrorCode.InvoiceNotExists);
		}
		InvoiceDto dto = rInvoiceDao.queryInvoiceByInvoiceId(invoiceId,invoiceCode);
		
		List<InvoiceDepotDto> depot = rInvoiceDao.queryDepot(invoiceId,invoiceCode);
		if(depot.size()>0)
			dto.setInvoiceDepotList(depot);
		
		List<InvoiceDetailDto> detail = rInvoiceDao.queryDetail(invoiceId,invoiceBookId,invoiceCode);
		if(detail.size()>0)
			dto.setInvoiceDetailList(detail);	
		return dto;
	}
	
	/**
	 * 根据发票id查询发票详细信息
	 * @param invoiceId
	 * @return
	 * @throws Exception
	 */
	public InvoiceDto queryInvoiceDetailByInvoiceId(String invoiceId,String invoiceCode) throws Exception {
		InvoiceDto invoice = rInvoiceDao.queryInvoiceById(invoiceId,invoiceCode);
		List<InvoiceDetailDto> detail = rInvoiceDao.queryDetail(invoiceId,invoice.getInvoice_book_id(),invoice.getInvoice_code());
		invoice.setInvoiceDetailList(detail);
		return invoice;
	}

	/**
	 * 多条件查询发票及客户信息
	 * @param invoiceDto
	 * @return
	 * @throws Exception
	 */
	public Pager<RInvoice> queryMulitInvoice(InvoiceDto invoiceDto,SOptr optr,Integer start,Integer limit)
		throws Exception {
		/**if (StringHelper.isEmpty(invoiceDto.getDepot_id())){
			String depotId= this.findQueryDepot(optr);
			if (StringHelper.isNotEmpty(depotId))
				invoiceDto.setDepot_id(depotId);
		}**/
		return rInvoiceDao.queryMulitInvoice(invoiceDto, start, limit);
	}

	/**
	 * 查询当前仓库及以下子仓库
	 * @param depotId
	 * @return
	 * @throws JDBCException
	 */
	public List<SDept> queryChildInvoiceDepot(SOptr optr) throws Exception{
		String dataRight = this.queryDataRightCon(optr, DataRight.INVOICE_MNG.toString());
		if (dataRight.equals(DataRightLevel.AREA.toString()) || optr.getCounty_id().equals(SystemConstants.COUNTY_ALL)) {
			return sDeptDao.queryAllDept();
		}
		String depotId = findDepot(optr);
		return sDeptDao.queryChildDept(depotId);
	}

	/**
	 * 保存发票调拨
	 * @param transDepotId
	 * @param optrId
	 * @param invoiceList
	 */
	public void saveTrans(String transDepotId,String optrId,String transType,SOptr optr,List<RInvoice> invoiceList) throws Exception{
		//保存调拨信息
		Integer doneCode = gDoneCode();//流水号
		String depotId = findDepot(optr);//原仓库
		RInvoiceTransfer transfer = new RInvoiceTransfer();
		transfer.setDone_code(doneCode);
		transfer.setOptr_type(transType);
		if(transType.equals(InvoiceOptrType.RECEIVE.toString())){
			transfer.setOrder_depot_id(optrId);//如果是领用,目标库用optr代替
		}else{
			transfer.setOrder_depot_id(transDepotId);
		}
		
		if(transType.equals(InvoiceOptrType.CANCEL_RECEIVE.toString())){//如果是取消领用,来源设置为操作员
			transfer.setSource_depot_id(optrId);
		}else{
			transfer.setSource_depot_id(depotId);
		}
		
		transfer.setCreate_time(DateHelper.now());
		transfer.setOptr_id(optr.getOptr_id());
		transfer.setInvoice_count(invoiceList.size());
		rInvoiceTransferDao.save(transfer);
		
		//保存明细信息
		saveDetails(doneCode, invoiceList);
		//修改发票对应的仓库
		//定额发票下发,选择营业员,修改发票营业员
		//不是领用的话,置空optr_id
		if(!InvoiceOptrType.RECEIVE.toString().equals(transType)){
			optrId = null;
		}
		rInvoiceDao.saveTrans(doneCode, transDepotId, optrId);
		if(InvoiceOptrType.RECEIVE.toString().equals(transType)){
			rInvoiceDao.updateInvoiceOpenOptrId(doneCode, optrId);
		}else if(InvoiceOptrType.CANCEL_RECEIVE.toString().equals(transType)){
			rInvoiceDao.updateInvoiceOpenOptrId(doneCode, null);
		}
	}

	//保存发票结账
	public void saveCheck(SOptr optr,List<RInvoice> invoiceList) throws Exception{
		Integer doneCode = gDoneCode();//流水号
//		String depotId = findDepot(optr);//原仓库
//		String depotPid= rDepotDefineDao.queryParentDepot(depotId);//结账后归属仓库
		//结账后归属仓库 根据模板配置
		TConfigTemplate ct = tConfigTemplateDao.queryConfigByConfigName(
				TemplateConfigDto.Config.INVOICE_DEPT_CHECKOUT.toString(), optr.getCounty_id());	//当前值
		if(ct == null){
			throw new ComponentException(ErrorCode.InvoiceTemplateDeptIsNull);
		}
		String depotPid = ct.getConfig_value();
		//保存结账记录
		saveInvoiceOptr(doneCode, optr, invoiceList.size(),InvoiceOptrType.CHECK.toString());
		saveDetails(doneCode, invoiceList);
		
//		if(depotPid.equals(SystemConstants.COUNTY_ALL)){
//			depotPid = depotId;
//		}
		//修改发票信息
		rInvoiceDao.saveCheck(doneCode, depotPid);

	}

	//保存取消发票结账
	public void saveCancelCheck(SOptr optr,List<RInvoice> invoiceList) throws Exception{
		Integer doneCode = gDoneCode();//流水号
		//保存结账记录
		saveInvoiceOptr(doneCode, optr, invoiceList.size(),InvoiceOptrType.CANCELCHECK.toString());
		saveDetails(doneCode, invoiceList);
		//修改发票信息
		rInvoiceDao.saveCancelCheck(doneCode);
	}

	//保存发票核销
	public void saveClose(SOptr optr,List<RInvoice> invoiceList) throws Exception{
		Integer doneCode = gDoneCode();//流水号
		//保存结账记录
		saveInvoiceOptr(doneCode, optr, invoiceList.size(),InvoiceOptrType.CLOSE.toString());
		saveDetails(doneCode, invoiceList);
		//修改发票信息
		rInvoiceDao.saveClose(doneCode);
	}

	//保存发票核销
	public void saveCancelClose(SOptr optr,List<RInvoice> invoiceList) throws Exception{
		Integer doneCode = gDoneCode();//流水号
		//保存记录
		saveInvoiceOptr(doneCode, optr, invoiceList.size(),InvoiceOptrType.CANCELCLOSE.toString());
		saveDetails(doneCode, invoiceList);
		//修改发票信息
		rInvoiceDao.saveCancelClose(doneCode);
	}

	//修改发票状态
	public void saveEditStatus(SOptr optr, List<RInvoice> invoiceList,
			String status, String invoiceType) throws Exception {
		Integer doneCode = gDoneCode();//流水号
		//保存记录
		saveInvoiceOptr(doneCode, optr, invoiceList.size(),InvoiceOptrType.EDITSTATUS.toString());
		saveDetails(doneCode, invoiceList);
		//修改发票信息
		rInvoiceDao.saveEditStatus(doneCode,status,invoiceType);
	}
	
	//退库
	public void saveRefund(SOptr optr,List<RInvoice> invoiceList,String status) throws Exception{
		Integer doneCode = gDoneCode();//流水号
		//保存记录
		saveInvoiceOptr(doneCode, optr, invoiceList.size(),InvoiceOptrType.REFUND.toString());
		saveDetails(doneCode, invoiceList);
		
		for(RInvoice invoice : invoiceList){
			rInvoiceDao.removeInvoice(doneCode, invoice.getInvoice_id(), invoice.getInvoice_code());
		}
	}
	
	/**
	 * 定额发票挂失
	 * @param optr
	 * @param invoiceList
	 * @param invoiceOptrType
	 * @param isLoss
	 * @throws Exception
	 */
	public void saveQutaLoss(SOptr optr, List<RInvoice> invoiceList,
			String invoiceOptrType, String isLoss) throws Exception {
		Integer doneCode = gDoneCode();
		//保存记录
		saveInvoiceOptr(doneCode, optr, invoiceList.size(),invoiceOptrType);
		saveDetails(doneCode, invoiceList);
		
		rInvoiceDao.saveQutoInvoiceLoss(doneCode, isLoss);
	}
	
	/**
	 * 定额发票调账
	 * @param optr
	 * @param invoiceList
	 * @param invoiceOptrType
	 * @param amount
	 * @throws Exception
	 */
	public void saveQutaAdjust(SOptr optr, List<RInvoice> invoiceList,Integer amount) throws Exception {
		Integer doneCode = gDoneCode();
		//保存记录
		saveInvoiceOptr(doneCode, optr, invoiceList.size(),InvoiceOptrType.QUOTA_ADJUST.toString());
		saveDetails(doneCode, invoiceList);
		
		rInvoiceDao.saveQutoInvoiceAdjust(doneCode, amount);
	}

	/**
	 * 保存发票财务操作信息
	 * @param optrId
	 * @param invoiceCount
	 * @throws Exception
	 */
	private void saveInvoiceOptr(int doneCode,SOptr optr,int invoiceCount,String optrType) throws Exception{
		RInvoiceOptr invoiceOptr = new RInvoiceOptr();
		invoiceOptr.setDone_code(doneCode);
		invoiceOptr.setOptr_type(optrType);
		invoiceOptr.setInvoice_count(invoiceCount);
		invoiceOptr.setOptr_id(optr.getOptr_id());
		invoiceOptr.setDept_id(optr.getDept_id());
		invoiceOptr.setCounty_id(optr.getCounty_id());
		invoiceOptr.setCreate_time(DateHelper.now());
		rInvoiceOptrDao.save(invoiceOptr);
	}

	private void saveDetails(int doneCode, List<RInvoice> invoiceList) throws Exception{
		List<RInvoiceDetail> invoiceDetailList = new ArrayList<RInvoiceDetail>();
		for (RInvoice invoice :invoiceList){
			RInvoiceDetail invoiceDetail = new RInvoiceDetail();
			invoiceDetail.setDone_code(doneCode);
			invoiceDetail.setInvoice_code(invoice.getInvoice_code());
			invoiceDetail.setInvoice_id(invoice.getInvoice_id());
			invoiceDetailList.add(invoiceDetail);
		}
		rInvoiceDetailDao.save(invoiceDetailList.toArray(new RInvoiceDetail[invoiceDetailList.size()]));
	}
	
	public List<SOptr> getByDeptId(String deptId) throws Exception {
		return sOptrDao.findByDeptId(deptId);
	}



	private Integer gDoneCode() throws JDBCException {
		return rInvoiceInputDao.findSequence(SequenceConstants.SEQ_DONE_CODE).intValue();
	}

	public void setRInvoiceDao(RInvoiceDao invoiceDao) {
		rInvoiceDao = invoiceDao;
	}

	public void setRInvoiceInputDao(RInvoiceInputDao invoiceInputDao) {
		rInvoiceInputDao = invoiceInputDao;
	}

	public void setRInvoiceDetailDao(RInvoiceDetailDao invoiceDetailDao) {
		rInvoiceDetailDao = invoiceDetailDao;
	}

	public void setRInvoiceTransferDao(RInvoiceTransferDao invoiceTransferDao) {
		rInvoiceTransferDao = invoiceTransferDao;
	}

	public void setRInvoiceOptrDao(RInvoiceOptrDao invoiceOptrDao) {
		rInvoiceOptrDao = invoiceOptrDao;
	}
	

	public void setRDepotDefineDao(RDepotDefineDao depotDefineDao) {
		rDepotDefineDao = depotDefineDao;
	}
	

}
