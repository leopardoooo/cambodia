/**
 *
 */
package com.ycsoft.business.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.config.TAddress;
import com.ycsoft.beans.config.TPayType;
import com.ycsoft.beans.config.TUpdateCfg;
import com.ycsoft.beans.core.job.JOdscntRecord;
import com.ycsoft.beans.core.voucher.CVoucher;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SDeptAddr;
import com.ycsoft.beans.system.SDeptBusicode;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SSubSystem;
import com.ycsoft.business.commons.abstracts.IBaseService;
import com.ycsoft.business.dto.config.ExtendTableAttributeDto;
import com.ycsoft.business.dto.config.TAddressDto;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.system.SDeptDto;

/**
 *
 * 查询基础配置服务
 * @author YC-SOFT
 *
 */
public interface IQueryCfgService extends IBaseService {

	/**
	 * 查询所有子系统定义
	 * @return
	 * @throws Exception
	 */
	public List<SSubSystem> queryAllSubSystem(SOptr optr) throws Exception;

	/**
	 * 查询业务配置信息
	 * @return
	 * @throws Exception
	 */
	public Map<String,Map<String,List>> queryBusiCfgData()throws Exception;

	/**
	 * 查询系统配置信息
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> queryCfgData()throws Exception;


	/**
	 * 获取字典表name项
	 *
	 * @param keyname
	 * @param value
	 * @return
	 */
	public String getDictName(String keyname, String value);

	/**
	 * 获取指定key字典集合
	 *
	 * @param keyname
	 * @return
	 */
	public List<SItemvalue> getDicts(String keyname) ;


	/**
	 * 获取可以修改的客户属性
	 * @return
	 */
	public List<TUpdateCfg> queryCanUpdateField(String busiCode) throws Exception;


	/**
	 *  查询指定表名的扩展输入框
	 * @param tabName 通过表明获取扩展信息
	 * @throws Exception
	 */
	public List<ExtendTableAttributeDto> extAttrForm(String groupId,String tabName) throws Exception;


	/**
	 * 通过表名和主键值查询扩展信息。
	 * @param tabName
	 * @return
	 * @throws Exception
	 */
	public List<ExtendTableAttributeDto> extAttrView(String groupId,String tabName,String pkValue) throws Exception;

	/**
	 * 根据名称查询地址目录
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public List<TAddressDto> queryAddrByName(String name,String addrId) throws Exception;
	
	public String queryCustAddrName(String addrId) throws Exception;

	/**
	 * 返回当前地区的部门
	 *
	 * @return
	 */
	public List<SDept> queryDepts() throws Exception;

	/**
	 * 查询子系统
	 * @param sysId
	 * @return
	 */
	public SSubSystem querySubSystem(String sysId) throws JDBCException;


	/**
	 * 查询当前账务日期
	 * @return
	 */
	public Date queryAcctDate() throws Exception;

	/**
	 * 修改账务日期
	 * @param acctDate
	 */
	public void modifyAcctDate(Date acctDate) throws Exception;
	
	

	/**
	 * @return
	 */
	public SItemvalue queryGripAccountMode() throws Exception;

	/**
	 * 重载内存数据
	 * @throws Exception
	 */
	public void reloadMemoryData() throws Exception;
	
	/**
	 * 查询营业厅树(分权限)
	 * @return
	 * @throws Exception
	 */
	public List<SDeptDto> queryDeptTree() throws Exception;
	
	/**
	 * 查询营业厅树(不分权限)
	 * @return
	 * @throws Exception
	 */
	public List<SDeptDto> queryOtherDeptTree() throws Exception;

	/**
	 * 查询缴费类型
	 * @return
	 * @throws Exception
	 */
	public List<TPayType> queryPayType() throws Exception;
	
	/**
	 * 数据权限查询下拉数据
	 * @param dataType
	 * @param itemKey
	 * @return
	 * @throws Exception
	 */
	public List<SItemvalue> queryItemValues(String dataType,String itemKey) throws Exception;
	
	/**
	 * 查询代金券详细信息
	 * @param voucherId
	 * @return
	 * @throws Exception
	 */
	public CVoucher queryVoucherById(String voucherId) throws Exception;

	/**
	 * 查询地址区域
	 * @return
	 * @throws Exception
	 */
	public List<TAddressDto> queryAddrDistrict() throws Exception;

	/**
	 * 查询区域下的小区
	 * @param addrPid
	 * @return
	 * @throws Exception
	 */
	public List<TAddressDto> queryAddrCommunity(String addrPid) throws Exception;

	/**
	 * 
	 */
	public void reloadPrintData()throws Exception;

	/**
	 * @param acctDate
	 * @return
	 */
	public void checkUserCount(String acctDate,String addrIds)throws Exception;

	/**
	 * @param acctDate
	 * @return
	 */
	public void checkDeviceCount(String acctDate,String optrId,String deptId)throws Exception;
	
	public List<JOdscntRecord> queryRecordByDeptId() throws Exception;

	/**
	 * 查询当前营业厅禁用的业务.
	 * @param county_id
	 * @return
	 */
	public List<SDeptBusicode> queryDeptBusiCode(String county_id) throws Exception;
	
	/**
	 * 查询当前营业厅关联的区域.
	 * @param dept_id
	 * @return
	 * @throws Exception
	 */
	public List<SDeptAddr> queryDeptAddress(String dept_id) throws Exception;

	/**
	 * 查询单个地址信息.
	 * @param addrId
	 * @return
	 */
	public TAddress querySingleAddress(String addrId) throws Exception;

	/**
	 * 查询产品的免费使用时间,用于前台配置开始计费日期.
	 * @return
	 */
	public Map<String, String> queryProdFreeDay() throws Exception;
}
