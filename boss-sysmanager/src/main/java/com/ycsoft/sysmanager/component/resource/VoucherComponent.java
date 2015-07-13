package com.ycsoft.sysmanager.component.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.voucher.CVoucher;
import com.ycsoft.beans.core.voucher.CVoucherDoneVoucherid;
import com.ycsoft.beans.core.voucher.CVoucherProcure;
import com.ycsoft.beans.core.voucher.CVoucherType;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dao.core.voucher.CVoucherDao;
import com.ycsoft.business.dao.core.voucher.CVoucherDoneVoucheridDao;
import com.ycsoft.business.dao.core.voucher.CVoucherProcureDao;
import com.ycsoft.business.dao.core.voucher.CVoucherTypeDao;
import com.ycsoft.business.dao.system.SDeptDao;
import com.ycsoft.business.dto.core.voucher.VoucherDto;
import com.ycsoft.business.dto.core.voucher.VoucherProcureDto;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.Pager;

@Component
public class VoucherComponent extends BaseComponent {

	private CVoucherDao cVoucherDao;
	private CVoucherTypeDao cVoucherTypeDao;
	private CVoucherProcureDao cVoucherProcureDao;
	private CVoucherDoneVoucheridDao cVoucherDoneVoucheridDao;
	private SDeptDao sDeptDao;
	
	public Pager<VoucherDto> queryMulitVoucher(VoucherDto voucherDto,
			Integer start, Integer limit) throws Exception {
		return cVoucherDao.queryMulitVoucher(voucherDto, start, limit);
	}
	
	/**
	 * 代金券失效
	 * @param voucherId
	 * @param countyId
	 * @throws Exception
	 */
	public void updateVoucherStatus(String voucherId) throws Exception {
		cVoucherDao.updateVoucherStatus(voucherId);
	}
	
	/**
	 * 取消领用
	 * @param voucherIds
	 * @param countyId
	 * @throws Exception
	 */
	public void updateVoucherProcureStatus(String [] voucherIds,String countyId) throws Exception {
		cVoucherDao.updateVoucherProcureStatus(voucherIds, countyId);
	}
	
	public Pager<CVoucher> queryProcureByDoneCode(Integer doneCode,
			Integer start, Integer limit) throws Exception {
		return cVoucherDao.queryProcureByDoneCode(doneCode, start, limit);
	}
	
	public List<SDept> queryFgsByDeptId(String countyId) throws Exception {
		return sDeptDao.queryFgsByDeptId(countyId);
	}
	
	/**
	 * 生成代金券
	 * @param startVoucherId
	 * @param endVoucherId
	 * @param optr
	 * @throws Exception
	 */
	public boolean saveVoucher(VoucherDto voucherDto, SOptr optr) throws Exception {
		long startVoucherId = Long.parseLong(voucherDto.getStart_voucher_id());
		long endVoucherId = Long.parseLong(voucherDto.getEnd_voucher_id());
		
		List<CVoucher> voucherList = cVoucherDao.queryVoucher(startVoucherId, endVoucherId, optr.getCounty_id());
		if(voucherList == null || voucherList.size() == 0){
			String optrId = optr.getOptr_id();
			CVoucher voucher = null;
			ArrayList<CVoucher> list = new ArrayList<CVoucher>();
			for(long i=startVoucherId;i<=endVoucherId;i++){
				voucher = new CVoucher();
				BeanUtils.copyProperties(voucherDto, voucher);
				voucher.setVoucher_id(String.valueOf(i));
				voucher.setStatus(StatusConstants.IDLE);
				voucher.setStatus_time(new Date());
				voucher.setCreate_time(new Date());
				voucher.setOptr_id(optrId);
				voucher.setUnused_money(voucher.getVoucher_value());//生成时，未使用金额就是面额
				list.add(voucher);
			}
			int num = cVoucherDao.save(list.toArray(new CVoucher[list.size()]))[0];
			if (num>=0 || num == -2){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 查询代金券信息
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public Pager<CVoucher> queryVoucher(String query, Integer start,
			Integer limit, String countyId) throws Exception {
		return cVoucherDao.queryVoucher(query, start, limit, countyId);
	}
	
	/**
	 * 领用代金券
	 * 只记录领用信息，并不变更代金券的适用地区
	 * @param voucherProduce
	 * @param voucherIds
	 * @param optrId
	 * @throws Exception
	 */
	public int saveVoucherProcure(VoucherProcureDto voucherProcureDto, SOptr optr) throws Exception {
		CVoucherProcure voucherProduce = new CVoucherProcure();
		long startVoucherId = Long.parseLong(voucherProcureDto.getStart_voucher_id());
		long endVoucherId = Long.parseLong(voucherProcureDto.getEnd_voucher_id());
		//空闲、未失效且未领用的代金券数量 可用的
		List<CVoucher> idleList = cVoucherDao.queryIdleVoucher(startVoucherId,
				endVoucherId, optr.getCounty_id());
		int num = idleList.size();
		if(num>0){
			//在库的总数量
			List<CVoucher> voucherList = cVoucherDao.queryVoucher(startVoucherId, endVoucherId, optr.getCounty_id());
			int inSum = voucherList.size();
			int noSum = inSum - num;//已经使用、失效和领用的代金券
			
			voucherList.removeAll(idleList);
			int usedSum = 0;//已经使用的数量
			int procureSum = 0;//已领用的数量
			for(CVoucher voucher : voucherList){
				if(voucher.getStatus().equals(StatusConstants.USE)){
					++usedSum;
				}else if(voucher.getIs_procured().equals(SystemConstants.BOOLEAN_TRUE)){
					++procureSum;
				}
				
				
			}
			int invalidSum = noSum - usedSum - procureSum;//已经失效的数量
			
			Integer doneCode = gDoneCode();
			BeanUtils.copyProperties(voucherProcureDto, voucherProduce);
			voucherProduce.setVoucher_done_code(doneCode);
			voucherProduce.setOptr_id(optr.getOptr_id());
			voucherProduce.setCount(idleList.size());
			voucherProduce.setCreate_time(new Date());
			voucherProduce.setCounty_id(optr.getCounty_id());
			String remark = voucherProduce.getRemark();
			remark = StringHelper.isNotEmpty(remark)?remark+"; ":remark;
			
			long sum =  endVoucherId - startVoucherId+1;//领用代金券总数
			String str = startVoucherId + " 至 " + endVoucherId + " 共" + sum
					+ "张代金券; " + optr.getOptr_name() + "; 成功领用" + num + "张; "
					+ "失败领用" + (sum - num) + "张,包括( " + usedSum + "张已使用,"
					+ procureSum + "张已领用," + invalidSum + "张已失效)";
			
			voucherProduce.setRemark(remark+""+str);
			
			CVoucherDoneVoucherid voucherid = null;
			ArrayList<CVoucherDoneVoucherid> list = new ArrayList<CVoucherDoneVoucherid>();
			for(CVoucher voucher : idleList){
				voucherid = new CVoucherDoneVoucherid();
				voucherid.setVoucher_done_code(doneCode);
				voucherid.setVoucher_id(voucher.getVoucher_id());
				list.add(voucherid);
				
				voucher.setIs_procured(SystemConstants.BOOLEAN_TRUE);
			}
			cVoucherProcureDao.save(voucherProduce);
			cVoucherDoneVoucheridDao.save(list.toArray(new CVoucherDoneVoucherid[list.size()]));
			//领用后记录领用
			cVoucherDao.update(idleList.toArray(new CVoucher[idleList.size()]));
		}
		return num;
	}
	
	public List<CVoucherType> queryVoucherTypes() throws Exception{
		return cVoucherTypeDao.findAllTypes();
	}
	
	/**
	 * 维护代金券类型.
	 * @param list
	 * @throws Exception
	 */
	public void editVoucherType(CVoucherType vtype) throws Exception{
		String voucher_type = vtype.getVoucher_type();
		CVoucherType bean = cVoucherTypeDao.findByKey(voucher_type);
		if(bean!=null){
			cVoucherTypeDao.update(vtype);
		}else{
			cVoucherTypeDao.save(vtype);
		}
	}
	
	/**
	 * 查询代金券领用记录
	 * @param countyId
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<CVoucherProcure> queryVoucherProcure(String query, String countyId,
			Integer start, Integer limit) throws Exception {
		return cVoucherProcureDao.queryVoucherProcure(query, countyId, start, limit);
	}
	
	private Integer gDoneCode() throws Exception {
		return Integer.parseInt(cDoneCodeDao.findSequence().toString());
	}
	
	public void setCVoucherDao(CVoucherDao voucherDao) {
		cVoucherDao = voucherDao;
	}
	public void setCVoucherProcureDao(CVoucherProcureDao voucherProcureDao) {
		cVoucherProcureDao = voucherProcureDao;
	}

	public void setCVoucherDoneVoucheridDao(
			CVoucherDoneVoucheridDao voucherDoneVoucheridDao) {
		cVoucherDoneVoucheridDao = voucherDoneVoucheridDao;
	}

	public void setSDeptDao(SDeptDao deptDao) {
		sDeptDao = deptDao;
	}

	public void setCVoucherTypeDao(CVoucherTypeDao cVoucherTypeDao) {
		this.cVoucherTypeDao = cVoucherTypeDao;
	}
}
