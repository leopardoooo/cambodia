/**
/**
 * RInvoiceDao.java	2010/09/17
 */

package com.ycsoft.business.dao.resource.invoice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.invoice.RInvoice;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.resource.invoice.InvoiceDepotDto;
import com.ycsoft.sysmanager.dto.resource.invoice.InvoiceDetailDto;
import com.ycsoft.sysmanager.dto.resource.invoice.InvoiceDto;

/**
 * RInvoiceDao -> R_INVOICE table's operator
 */
@Component
public class RInvoiceDao extends BaseEntityDao<RInvoice> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5882470671503507840L;

	/**
	 * default empty constructor
	 */
	public RInvoiceDao() {
	}

	/**
	 * 查询核销的发票
	 * 
	 * @param depotId
	 * @return
	 * @throws Exception
	 */
	public List<RInvoice> queryFinanceInvoice(String depotId) throws Exception {
		String sql = "select * from r_invoice where FINANCE_STATUS=? and t.depot_id=?";
		return this.createQuery(sql, StatusConstants.CLOSE, depotId).list();
	}

	/**
	 * 检查当前发票号码段是否存在
	 * 
	 * @param startInvoiceId
	 * @param endInvoiceId
	 * @return
	 * @throws JDBCException
	 */
	public boolean checkInvoic(String startInvoiceId, String endInvoiceId,
			String depotId) throws JDBCException {
		String sql = "select count(invoice_id) from r_invoice where invoice_id between ? and ? and depot_id=?";
		int count = count(sql, startInvoiceId, endInvoiceId, depotId);
		return count == 0;
	}

	public List<RInvoice> queryInvoice(String invoiceCode,String invoiceType, String startBookId, String endBookId,
			String startInvoiceId, String endInvoiceId, String depotId,
			String[] statusArr) throws Exception {
		String sql = "select * from r_invoice where depot_id in (select depot_id from vew_invoice_depot start with depot_id=? connect by prior  depot_id = depot_pid )"
				+ "  and is_loss=? and status in ("
				+ sqlGenerator.in(statusArr) + ")";
		if(StringHelper.isNotEmpty(invoiceType))
			sql += " and invoice_type='"+invoiceType+"'";
		if(StringHelper.isNotEmpty( invoiceCode )){
			sql += " and invoice_code = '" + invoiceCode + "' ";
		}
		if (StringHelper.isNotEmpty(startBookId))
			sql += " and invoice_book_id between '" + startBookId + "' and '"
					+ endBookId + "'";
		if (StringHelper.isNotEmpty(startInvoiceId))
			sql += " and invoice_id between '" + startInvoiceId + "' and '"
					+ endInvoiceId + "'";
		sql += " order by invoice_id";
		return createQuery(sql, depotId, SystemConstants.BOOLEAN_FALSE).list();
	}

	/**
	 * 根据发票本号、发票号区间查找发票
	 * 
	 * @param startBookId
	 * @param endBookId
	 * @param startInvoiceId
	 * @param endInvoiceId
	 * @param depotId
	 * @return
	 * @throws Exception
	 */
	public List<RInvoice> queryInvoice(String startBookId, String endBookId,
			String startInvoiceId, String endInvoiceId, String depotId,
			String isLoss,String invoiceCode) throws Exception { 
		String sql = "select r.INVOICE_ID,r.INVOICE_BOOK_ID,r.INVOICE_CODE,r.INVOICE_TYPE,r.DEPOT_ID,r.STATUS,r.INVOICE_MODE,r.USE_TIME,r.FINANCE_STATUS,r.CHECK_TIME,r.CHECK_DEPOT_ID,r.CLOSE_TIME,r.CREATE_TIME,r.INVOICE_AMOUNT,r.OPTR_ID,r.IS_LOSS,nvl(SUM(decode(c.status,'PAY',c.real_pay,0)), 0) amount from r_invoice  r,c_fee c " +
				" where r.depot_id=? and r.is_loss=? " +
				" and r.invoice_id=c.invoice_id(+) and r.invoice_book_id=c.invoice_book_id(+) ";
		if (StringHelper.isNotEmpty(startBookId))
			sql += " and r.invoice_book_id between '" + startBookId + "' and '"
					+ endBookId + "'";
		if(StringHelper.isNotEmpty(invoiceCode)){
			sql += " and r.invoice_code = '" + invoiceCode + "' ";
		}
		if (StringHelper.isNotEmpty(startInvoiceId))
			sql += " and r.invoice_id between '" + startInvoiceId + "' and '"
					+ endInvoiceId + "'";
		sql += " group by r.INVOICE_ID,r.INVOICE_BOOK_ID,r.INVOICE_CODE,r.INVOICE_TYPE,r.DEPOT_ID,r.STATUS,r.INVOICE_MODE,r.USE_TIME,r.FINANCE_STATUS,r.CHECK_TIME,r.CHECK_DEPOT_ID,r.CLOSE_TIME,r.CREATE_TIME,r.INVOICE_AMOUNT,r.OPTR_ID,r.IS_LOSS" +
				" order by r.invoice_id";
		return createQuery(sql, depotId, isLoss).list();
	}

	public List<RInvoice> queryInvoiceExceptQuota(String startBookId,
			String endBookId, String startInvoiceId, String endInvoiceId,
			String depotId,String invoiceCode) throws Exception {
		String sql = "select * from r_invoice where depot_id=?"
				+ " and is_loss=? and invoice_type <> ? ";
		if (StringHelper.isNotEmpty(startBookId))
			sql += " and invoice_book_id between '" + startBookId + "' and '"
					+ endBookId + "'";
		if(StringHelper.isNotEmpty(invoiceCode)){
			sql+= " and invoice_code = '" + invoiceCode + "' ";
		}
		if (StringHelper.isNotEmpty(startInvoiceId))
			sql += " and invoice_id between '" + startInvoiceId + "' and '"
					+ endInvoiceId + "'";
		sql += " order by invoice_id";
		
		return createQuery(sql, depotId, SystemConstants.BOOLEAN_FALSE,
				SystemConstants.DOC_TYPE_QUOTA).list();
	}

	public List<RInvoice> queryQuotaInvoice(String startBookId,
			String endBookId, String startInvoiceId, String endInvoiceId,
			String depotId, String isLoss,String invoiceCode) throws Exception {
		String sql = "select * from r_invoice where depot_id=?"
				+ " and is_loss=? and invoice_type =? ";
		if (StringHelper.isNotEmpty(startBookId))
			sql += " and invoice_book_id between '" + startBookId + "' and '"
					+ endBookId + "'";
		if(StringHelper.isNotEmpty(invoiceCode)){
			sql += " and invoice_code = '" + invoiceCode + "' ";
		}
		if (StringHelper.isNotEmpty(startInvoiceId))
			sql += " and invoice_id between '" + startInvoiceId + "' and '"
					+ endInvoiceId + "'";
		sql += " order by invoice_id";
		return createQuery(sql, depotId, isLoss, SystemConstants.DOC_TYPE_QUOTA)
				.list();
	}

	/**
	 * 根据发票id查询发票详细信息
	 * 
	 * @param invoiceId
	 * @return
	 * @throws Exception
	 */
	public InvoiceDto queryInvoiceByInvoiceId(String invoiceId,
			String invoiceCode) throws Exception {
		String sql = StringHelper
				.append(
						"select c.cust_id,c.cust_name,f.county_id,r.*,r.optr_id,",
						" (select b1.dept_name||'_'||a1.optr_name||decode(a1.status,'ACTIVE','','(失效)') ",
						" from s_optr a1,s_dept b1 where a1.dept_id=b1.dept_id and a1.optr_id=r.optr_id) optr_name, ",
						" f.optr_id user_optr_id from r_invoice r, c_fee f, c_cust c where r.invoice_id = f.invoice_id(+) ",
						" and r.invoice_code = f.invoice_code(+) and f.cust_id = c.cust_id(+) ",
						" and r.invoice_id=? and r.invoice_code=?");
		InvoiceDto invoice = createQuery(InvoiceDto.class, sql, invoiceId,invoiceCode).first();
		sql = "SELECT nvl(SUM(t.real_pay),0) FROM c_fee t WHERE t.status='PAY' AND t.invoice_id =?  AND t.invoice_code =?";
		String amount = findUnique(sql, invoiceId, invoiceCode);
		if (StringHelper.isEmpty(amount)) {
			amount = "0";
		}
		invoice.setAmount(Integer.parseInt(amount));
		return invoice;
	}

	/**
	 * 根据发票id查询发票详细信息
	 * 
	 * @param invoiceId
	 * @return
	 * @throws Exception
	 */
	public InvoiceDto queryInvoiceById(String invoiceId, String invoiceCode)
			throws Exception {
		String sql = StringHelper
				.append("select * from r_invoice  where invoice_id=? and invoice_code=?");
		return this
				.createQuery(InvoiceDto.class, sql, invoiceId, invoiceCode)
				.first();
	}

	public List<InvoiceDetailDto> queryDetail(String invoiceId,
			String invoiceBookId, String invoiceCode) throws Exception {
		List<String> params = new ArrayList<String>();
		params.add(invoiceId);
		String sql = StringHelper
				.append(
						"select nvl(cc.cust_no,ccs.cust_no) cust_no,nvl(cc.cust_name,ccs.cust_name) cust_name,tbc.busi_name, ",
						"nvl(pp.prod_name,nvl(bf.fee_name,tpa.acctitem_name)) fee_name, t.real_pay,t.create_time,t.status,t.optr_id ",
						"from c_fee t,r_invoice r,s_dept d,c_cust cc,c_cust_his ccs ,t_public_acctitem tpa,t_busi_fee bf,p_prod pp ,t_busi_code tbc ",
						"where r.invoice_id=t.invoice_id and r.invoice_book_id=t.invoice_book_id and r.invoice_code=t.invoice_code ",
						"and d.dept_id=r.depot_id and d.county_id=t.county_id and cc.cust_id(+)=t.cust_id and cc.county_id(+)=t.county_id ",
						"and ccs.cust_id(+)=t.cust_id and ccs.county_id(+)=t.county_id and pp.prod_id(+)=t.acctitem_id ",
						"and tpa.acctitem_id(+)=t.acctitem_id and bf.fee_id(+)=t.fee_id and tbc.busi_code=t.busi_code ",
						"and r.invoice_id=? ");
		if(StringHelper.isNotEmpty(invoiceBookId)){
			sql += " and r.invoice_book_id=? ";
			params.add(invoiceBookId);
		}
		sql+=" and r.invoice_code=? ";
		params.add(invoiceCode);
		return this.createQuery(InvoiceDetailDto.class, sql, params.toArray()).list();
	}

	public List<InvoiceDepotDto> queryDepot(String invoiceId, String invoiceCode)
			throws Exception {
		String sql = StringHelper
				.append(
						"select si.item_name optr_type,a.create_time,optr.optr_name,a.depot_name from( ",
						"select 'INPUT' optr_type,input.create_time,input.optr_id,d.depot_name ",
						"from r_invoice_detail t ,r_invoice_input input,vew_invoice_depot d ",
						"where t.invoice_id=? and t.invoice_code=? and input.done_code=t.done_code and d.depot_id=input.depot_id ",
						"union all ",
						"select o.optr_type,o.create_time,o.optr_id, decode( o.optr_type,'CLOSE',d2.depot_name,d1.depot_name)  depot_name ",
						"from r_invoice_detail t,r_invoice_optr o,r_invoice r,vew_invoice_depot d1,vew_invoice_depot d2 ",
						"where t.done_code=o.done_code and r.invoice_id=t.invoice_id and r.invoice_code=t.invoice_code ",
						"and d1.depot_id=r.depot_id and d2.depot_id(+)=r.check_depot_id and t.invoice_id=? and t.invoice_code=? ",
						"union all ",
						"select o.optr_type,o.create_time,o.optr_id,d1.depot_name||'=>'||d2.depot_name depot_name ",
						"from r_invoice_detail t,r_invoice_transfer o,(select dept_id depot_id,dept_name depot_name   from s_dept union all select optr_id,optr_name  from s_optr ) d1," +
						"(select dept_id depot_id,dept_name depot_name   from s_dept union all select optr_id,optr_name  from s_optr )  d2 ",
						"where t.done_code=o.done_code and d1.depot_id=o.source_depot_id and d2.depot_id=o.order_depot_id ",
						"and t.invoice_id=? and t.invoice_code=?) a,s_optr optr,s_itemvalue si ",
						"where a.optr_id=optr.optr_id and si.item_value=a.optr_type and si.item_key=? order by a.create_time desc ");
		return this.createQuery(InvoiceDepotDto.class, sql, invoiceId,
				invoiceCode, invoiceId, invoiceCode, invoiceId, invoiceCode,
				DictKey.INVOICE_OPTR_TYPE.toString()).list();
	}

	/**
	 * 多条件查询发票及客户信息
	 * 
	 * @param invoiceDto
	 * @return
	 * @throws Exception
	 */
	public Pager<RInvoice> queryMulitInvoice(InvoiceDto invoiceDto,
			Integer start, Integer limit) throws Exception {
		String startInvoiceId = invoiceDto.getStart_invoice_id();// 发票号码
		String endInvoiceId = invoiceDto.getEnd_invoice_id();
		String startInvoiceBook = invoiceDto.getStart_invoice_book();// 发票号码
		String endInvoiceBook = invoiceDto.getEnd_invoice_book();
		String startInputTime = invoiceDto.getStart_input_time();// 入库时间
		String endInputTime = invoiceDto.getEnd_input_time();
		String startCheckTime = invoiceDto.getStart_check_time();// 结账时间
		String endCheckTime = invoiceDto.getEnd_check_time();
		String startCloseTime = invoiceDto.getStart_close_time();// 核销时间
		String endCloseTime = invoiceDto.getEnd_close_time();
		String startUseTime = invoiceDto.getStart_use_time();// 开票时间
		String endUseTime = invoiceDto.getEnd_use_time();
		String invoice_code = invoiceDto.getInvoice_code();

		String invoiceType = invoiceDto.getInvoice_type();// 发票类型
		String status = invoiceDto.getStatus();// 使用状态
		String financeStatus = invoiceDto.getFinance_status();// 结存状态
		String depotId = invoiceDto.getDepot_id();// 所在仓库
		String optrids = invoiceDto.getOptrids();//分配到的操作员

		if (StringHelper.isEmpty(endInvoiceBook)) {
			endInvoiceBook = startInvoiceBook;
		}

		if (StringHelper.isEmpty(endInvoiceId)) {
			endInvoiceId = startInvoiceId;
		}

		String sql = StringHelper
				.append(
						"select nvl(SUM(decode(c.status,'PAY',c.real_pay,0)), 0) amount,r.invoice_id,r.invoice_book_id,r.invoice_code,",
						" r.invoice_type,r.depot_id,r.status,r.invoice_mode,r.use_time,r.finance_status,r.check_time, ",
						" r.check_depot_id,r.close_time,r.create_time,r.invoice_amount,r.optr_id,is_loss,r.open_optr_id",
						" from r_invoice r,c_fee c ",
						" where r.invoice_id=c.invoice_id(+) and r.invoice_book_id=c.invoice_book_id(+) ");

		if (StringHelper.isNotEmpty(depotId)) {
			if (depotId.toLowerCase().indexOf("select") > -1)
				sql = StringHelper.append(sql, " and r.depot_id in (", depotId,
						")");
			else
				sql = StringHelper.append(sql, " and r.depot_id in ('",
						depotId, "')");
		}

		if(StringHelper.isNotEmpty(invoice_code)){//此时invoice_code相当于之前的 invoice_book_id
			sql = StringHelper.append(sql," and r.invoice_code = '" + invoice_code + "' ");
		}
		if (StringHelper.isNotEmpty(startInvoiceId)) {
			sql = StringHelper.append(sql, " and to_number(r.invoice_id)>='",
					startInvoiceId, "'");
		}
		if (StringHelper.isNotEmpty(endInvoiceId)) {
			sql = StringHelper.append(sql, " and to_number(r.invoice_id)<='",
					endInvoiceId, "'");
		}
		if (StringHelper.isNotEmpty(startInvoiceBook)) {
			sql = StringHelper.append(sql, " and r.invoice_book_id>='",
					startInvoiceBook, "'");
		}
		if (StringHelper.isNotEmpty(endInvoiceBook)) {
			sql = StringHelper.append(sql, " and r.invoice_book_id<='",
					endInvoiceBook, "'");
		}

		if (StringHelper.isNotEmpty(startInputTime)) {
			sql = StringHelper.append(sql, " and r.create_time>=to_date('",
					startInputTime, " 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		}
		if (StringHelper.isNotEmpty(endInputTime)) {
			sql = StringHelper.append(sql, " and r.create_time<=to_date('",
					endInputTime, " 23:59:59','yyyy-mm-dd hh24:mi:ss')");
		}
		if (StringHelper.isNotEmpty(startCheckTime)) {
			sql = StringHelper.append(sql, " and r.check_time>=to_date('",
					startCheckTime, " 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		}
		if (StringHelper.isNotEmpty(endCheckTime)) {
			sql = StringHelper.append(sql, " and r.check_time<=to_date('",
					endCheckTime, " 23:59:59','yyyy-mm-dd hh24:mi:ss')");
		}
		if (StringHelper.isNotEmpty(startCloseTime)) {
			sql = StringHelper.append(sql, " and r.close_time>=to_date('",
					startCloseTime, " 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		}
		if (StringHelper.isNotEmpty(endCloseTime)) {
			sql = StringHelper.append(sql, " and r.close_time<=to_date('",
					endCloseTime, " 23:59:59','yyyy-mm-dd hh24:mi:ss')");
		}
		if (StringHelper.isNotEmpty(startUseTime)) {
			sql = StringHelper.append(sql, " and r.use_time>=to_date('",
					startUseTime, " 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		}
		if (StringHelper.isNotEmpty(endUseTime)) {
			sql = StringHelper.append(sql, " and r.use_time<=to_date('",
					endUseTime, " 23:59:59','yyyy-mm-dd hh24:mi:ss')");
		}
		if (StringHelper.isNotEmpty(invoiceType)) {
			sql = StringHelper.append(sql, " and r.invoice_type in (",
					sqlGenerator.in(invoiceType.split(",")), ")");
		}
		if (StringHelper.isNotEmpty(status)) {
			sql = StringHelper.append(sql, " and r.status in (", sqlGenerator
					.in(status.split(",")), ")");
		}
		if (StringHelper.isNotEmpty(financeStatus)) {
			sql = StringHelper.append(sql, " and r.finance_status in (",
					sqlGenerator.in(financeStatus.split(",")), ")");
		}
		if(StringHelper.isNotEmpty(optrids)){
			sql = StringHelper.append(sql, " and r.optr_id in (",
					sqlGenerator.in(optrids.split(",")), ")");
		}
		// if(StringHelper.isNotEmpty(depotId)){
		// sql = StringHelper.append(sql," and r.depot_id='",depotId,"'");
		// }
		sql = StringHelper
				.append(
						sql,
						" group by r.invoice_id,r.invoice_book_id,r.invoice_code,r.invoice_type,",
						" r.depot_id,r.status,r.invoice_mode,r.use_time,r.finance_status,r.check_time,",
						" r.check_depot_id,r.close_time,r.create_time,r.invoice_amount,r.optr_id,is_loss,r.open_optr_id",
						" order by r.invoice_id");
		return this.createQuery(RInvoice.class, sql).setStart(start).setLimit(
				limit).page();
	}

	/**
	 * 根据操作流水号更新发票对应的仓库
	 * 
	 * @param doneCode
	 * @param depotId
	 */
	public void saveTrans(Integer doneCode, String depotId, String optrId)
			throws Exception {
		String sql = "update r_invoice set depot_id=?, optr_id=? "
				+ " where (invoice_id,invoice_code) in (select invoice_id,invoice_code from r_invoice_detail where done_code=?)";
		this.executeUpdate(sql, depotId, optrId, doneCode);

	}
	//修改开票人
	public void updateInvoiceOpenOptrId(Integer doneCode, String optrId)
			throws Exception {
		String sql = "update r_invoice set open_optr_id=? "
				+ " where (invoice_id,invoice_code) in (select invoice_id,invoice_code from r_invoice_detail where done_code=?)";
		this.executeUpdate(sql, optrId, doneCode);

	}

	/**
	 * 保存修改状态
	 * 
	 * @param doneCode
	 * @param status
	 * @throws Exception
	 */

	public void updateStatus(Integer doneCode, String status) throws Exception {
		String sql = "update r_invoice set status=? ,use_time=sysdate "
				+ " where (invoice_id,invoice_code) in (select invoice_id,invoice_code from r_invoice_detail where done_code=?)";
		this.executeUpdate(sql, status, doneCode);
	}
	
	/**
	 * 根据发票ID 和 CODE 修改发票状态.
	 * @param invoiceId
	 * @param invoiceCode
	 * @param newStatus
	 * @throws Exception
	 */
	public void updateStatusByIdAndCode(String invoiceId, String invoiceCode,String newStatus) throws Exception{
		String sql = "update r_invoice set status=?,use_time=sysdate,amount=0  where invoice_id = ? and invoice_code = ? ";
		this.executeUpdate(sql, newStatus, invoiceId,invoiceCode);
	}

	/**
	 * 保存结账
	 * 
	 * @param doneCode
	 * @param depotId
	 *            结账后发票归属仓库
	 */

	public void saveCheck(Integer doneCode, String depotId) throws Exception {
		String sql = "update r_invoice set check_depot_id=depot_id,depot_id=? ,finance_status=? ,check_time=sysdate "
				+ " where (invoice_id,invoice_code) in (select invoice_id,invoice_code from r_invoice_detail where done_code=?)";
		this.executeUpdate(sql, depotId, StatusConstants.CHECKED, doneCode);
	}

	public void updateInvoiceOptr(Integer doneCode, String optrId)
			throws Exception {
		String sql = "update r_invoice set optr_id=? "
				+ " where (invoice_id,invoice_code) in (select invoice_id,invoice_code from r_invoice_detail where done_code=?)";
		this.executeUpdate(sql, optrId, doneCode);
	}

	/**
	 * 保存取消结账
	 * 
	 * @param doneCode
	 * @param depotId
	 *            结账后发票归属仓库
	 */

	public void saveCancelCheck(Integer doneCode) throws Exception {
		String sql = "update r_invoice set depot_id=check_depot_id,check_depot_id=null ,finance_status=? ,check_time=null "
				+ " where (invoice_id,invoice_code) in (select invoice_id,invoice_code from r_invoice_detail where done_code=?)";
		this.executeUpdate(sql, StatusConstants.IDLE, doneCode);
	}

	/**
	 * 保存核销
	 * 
	 * @param doneCode
	 */
	public void saveClose(Integer doneCode) throws Exception {
		String sql = "update r_invoice set finance_status=? ,close_time=sysdate "
				+ " where (invoice_id,invoice_code) in (select invoice_id,invoice_code from r_invoice_detail where done_code=?)";
		this.executeUpdate(sql, StatusConstants.CLOSE, doneCode);
	}

	/**
	 * 保存取消核销
	 * 
	 * @param doneCode
	 */
	public void saveCancelClose(Integer doneCode) throws Exception {
		String sql = "update r_invoice set finance_status=? ,close_time=null "
				+ " where (invoice_id,invoice_code) in (select invoice_id,invoice_code from r_invoice_detail where done_code=?)";
		this.executeUpdate(sql, StatusConstants.CHECKED, doneCode);
	}

	/**
	 * 作废发票
	 * 
	 * @param doneCode
	 * @param status
	 * @throws Exception
	 */
	public void saveEditStatus(Integer doneCode, String status,
			String invoiceType) throws Exception {
		String cond = "";
		if (status.equals(StatusConstants.IDLE)) {
			cond = ", amount=0 ";
		}
		String sql = "update r_invoice set status=? ,use_time=sysdate "
				+ cond
				+ " where (invoice_id,invoice_code) in (select invoice_id,invoice_code from r_invoice_detail where done_code=?)";
		if (invoiceType.equals(SystemConstants.DOC_TYPE_QUOTA)) { // 定额发票
			// 定额发票使用时，实际使用金额为系统定额发票金额
			if (status.equals(StatusConstants.USE)) {
				sql = "update r_invoice set status=? ,use_time=sysdate, amount=invoice_amount "
						+ " where (invoice_id,invoice_code) in (select invoice_id,invoice_code from r_invoice_detail where done_code=?)";
			} else {
				sql = "update r_invoice set status=? ,use_time=sysdate, amount=0 "
						+ " where (invoice_id,invoice_code) in (select invoice_id,invoice_code from r_invoice_detail where done_code=?)";
			}
		}
		this.executeUpdate(sql, status, doneCode);

		if (status.equals(StatusConstants.IDLE)) {
			// 清除发票使用信息
			sql = "update c_fee set invoice_id=null,invoice_book_id=null,invoice_code=null "
					+ " where (invoice_id,invoice_code) in (select invoice_id,invoice_code from r_invoice_detail where done_code=?)";
			this.executeUpdate(sql, doneCode);
		}
	}

	/**
	 * 定额发票挂失
	 * 
	 * @param doneCode
	 * @param isLoss
	 * @throws Exception
	 */
	public void saveQutoInvoiceLoss(Integer doneCode, String isLoss)
			throws Exception {
		String sql = "update r_invoice set is_loss=? "
				+ " where (invoice_id,invoice_code) in (select invoice_id,invoice_code from r_invoice_detail where done_code=?)";
		this.executeUpdate(sql, isLoss, doneCode);
	}

	public void saveQutoInvoiceAdjust(Integer doneCode, Integer amount)
			throws Exception {
		String sql = "update r_invoice set amount=amount+? "
				+ " where (invoice_id,invoice_code) in (select invoice_id,invoice_code from r_invoice_detail where done_code=?)";
		this.executeUpdate(sql, amount, doneCode);
	}

	/**
	 * 使用发票
	 * 
	 * @param invoiceBook
	 * @param invoiceId
	 * @param invoiceMode
	 * @param amount
	 * @return
	 * @throws JDBCException
	 */
	public int useInvoice(String invoiceCode, String invoiceId,
			String invoiceMode, int amount) throws JDBCException {
		String sql = "UPDATE r_invoice i SET i.status=?,i.invoice_mode=? ,amount=amount+?,use_time=sysdate"
				+ "  WHERE  i.invoice_code=? AND i.invoice_id=?  and i.FINANCE_STATUS=?";
		return executeUpdate(sql, StatusConstants.USE, invoiceMode, amount,
				invoiceCode, invoiceId, StatusConstants.IDLE);
	}
	
	public void updateInvoiceInfo(String invoiceCode, String invoiceId, String invoiceMode, int amount, String optrId) throws Exception {
		String sql = "UPDATE r_invoice i SET i.status=?,i.invoice_mode=? ,amount=amount+?, optr_id=?,use_time=sysdate"
				+ "  WHERE  i.invoice_code=? AND i.invoice_id=?  and i.FINANCE_STATUS=?";
		executeUpdate(sql, StatusConstants.USE, invoiceMode, amount, optrId, invoiceCode, invoiceId, StatusConstants.IDLE);
	}

	/**
	 * 用于打印票 取消使用发票
	 * 
	 * @param invoiceBook
	 * @param invoiceId
	 * @throws JDBCException
	 */
	public void cancelUseInvoice(String status, String invoiceCode,
			String invoiceId) throws JDBCException {
		String sql = "UPDATE r_invoice i SET i.status=?,i.invoice_mode=null ,amount=0,use_time=sysdate "
				+ "  WHERE i.invoice_code=? AND i.invoice_id=? ";
		executeUpdate(sql, status, invoiceCode, invoiceId);

	}

	/**
	 * 用于手工票的 取消使用
	 * 
	 * @param amount
	 * @param invoiceCode
	 * @param invoiceId
	 * @throws JDBCException
	 */
	public void cancelUseInvoice(Integer amount, String invoiceCode,
			String invoiceId) throws JDBCException {
		executeUpdate(
				"UPDATE r_invoice i SET amount=amount-? WHERE i.invoice_code=? AND i.invoice_id=?",
				amount, invoiceCode, invoiceId);

		executeUpdate(
				"update r_invoice set status=?,use_time=null,invoice_mode=null where amount=0 and invoice_id=? and invoice_code=?",
				StatusConstants.IDLE, invoiceId, invoiceCode);

	}

	public void removeInvoice(Integer doneCode, String invoiceId,
			String invoiceCode) throws JDBCException {
		String sql = "insert into r_invoice_his(DONE_CODE,INVOICE_ID,"
				+ "INVOICE_BOOK_ID,INVOICE_CODE,INVOICE_TYPE,DEPOT_ID,status,AMOUNT,INVOICE_MODE,"
				+ "USE_TIME,FINANCE_STATUS,CHECK_TIME,CHECK_DEPOT_ID,CLOSE_TIME,CREATE_TIME,REMOVE_TIME) "
				+ "select ?,INVOICE_ID,"
				+ "INVOICE_BOOK_ID,INVOICE_CODE,INVOICE_TYPE,DEPOT_ID,status,AMOUNT,INVOICE_MODE,"
				+ "USE_TIME,FINANCE_STATUS,CHECK_TIME,CHECK_DEPOT_ID,CLOSE_TIME,CREATE_TIME,sysdate from r_invoice"
				+ " where invoice_id=? and invoice_code=?";
		this.executeUpdate(sql, doneCode, invoiceId, invoiceCode);

		sql = "delete from r_invoice t where t.invoice_id=? and t.invoice_code=?";
		this.executeUpdate(sql, invoiceId, invoiceCode);
	}

	/**
	 * 查询发票
	 * 
	 * @param invoiceBook
	 * @param invoiceCode
	 * @param invoiceId
	 * @return
	 * @throws JDBCException
	 */
	public RInvoice queryInvoice(String invoiceBook, String invoiceId)
			throws JDBCException {
		String sql = "select * from r_invoice where invoice_code=? AND invoice_id=? ";
		return createQuery(sql, invoiceBook, invoiceId).first();
	}

	/**
	 * 根据发票号查询发票
	 * 
	 * @param invoiceId
	 * @return
	 * @throws JDBCException
	 */
	public List<RInvoice> queryInvoiceByCountyId(String invoiceId,
			String countyId) throws JDBCException {
		String sql = "select * from r_invoice where invoice_id=? ";
		return createQuery(RInvoice.class, sql, invoiceId).list();
	}

	public List<RInvoice> queryInvoiceByDepot(String invoiceId, String docType,
			String[] depotId) throws JDBCException {
		String sql = "select * from r_invoice where invoice_id=? and invoice_type=? and depot_id in (" +sqlGenerator.in(depotId)+") ";
		return createQuery(RInvoice.class, sql, invoiceId, docType)
				.list();
	}
	
	public List<RInvoice> queryInvoiceByDepot(String invoiceId, String[] depotId)
			throws JDBCException {
		String sql = "select * from r_invoice where invoice_id=? and depot_id in (" +sqlGenerator.in(depotId)+")";
		return createQuery(RInvoice.class, sql, invoiceId).list();
	}

	public List<RInvoice> getInvoiceByInvoiceId(String invoiceId)
			throws JDBCException {
		String sql = "select * from r_invoice where invoice_id=? ";
		return createQuery(RInvoice.class, sql, invoiceId).list();
	}

	/**
	 * 查询费用数据中的发票，用于验证时候可用
	 * 
	 * @param feeList
	 * @param depotId
	 * @return
	 * @throws Exception
	 */
	public List<RInvoice> queryInvoiceByIdAndCode(List<PayDto> feeList,
			String depotId) throws Exception {
		String sql = "select invoice_id,invoice_code,invoice_book_id from r_invoice where depot_id = ? and status = ? and (invoice_id,invoice_code) in (";

		for (int i = 0; i < feeList.size(); i++) {
			PayDto pay = feeList.get(i);
			if (i != feeList.size() - 1) {
				sql = StringHelper.append(sql, "('", pay.getInvoice_id(),
						"','", pay.getInvoice_code(), "'),");
			} else {
				sql = StringHelper.append(sql, "('", pay.getInvoice_id(),
						"','", pay.getInvoice_code(), "'))");
			}
		}

		return createQuery(RInvoice.class, sql, depotId, StatusConstants.IDLE)
				.list();
	}

	public boolean isExistsInvoiceByDepotId(String depotId)
			throws JDBCException {
		String sql = "select count(1) from r_invoice where depot_id=?";
		return this.count(sql, depotId) > 0;
	}

	public boolean isExistsInvoice(String invoiceId, String invoiceCode)
			throws JDBCException {
		String sql = "select count(1) from r_invoice where invoice_id = ? and invoice_code = ?";
		return this.count(sql, invoiceId, invoiceCode) > 0;
	}

}
