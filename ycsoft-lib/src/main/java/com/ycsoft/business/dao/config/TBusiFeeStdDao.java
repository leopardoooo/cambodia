/**
 * TBusiFeeStdDao.java	2010/10/30
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.config.TBusiFeeStd;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.business.dto.core.fee.BusiFeeDto;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * TBusiFeeStdDao -> T_BUSI_FEE_STD table's operator
 */
@Component
public class TBusiFeeStdDao extends BaseEntityDao<TBusiFeeStd> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1623714296682053420L;

	/**
	 * default empty constructor
	 */
	public TBusiFeeStdDao() {}

	/**
	 * 返回所有费用信息
	 * @return
	 * @throws JDBCException
	 */
	public List<BusiFeeDto> getAllFee() throws JDBCException {
		String template_type = SystemConstants.TEMPLATE_TYPE_FEE;
		String sql = "select tc.county_id||busi_code||tc.template_id keyname,tc.*,bf.*,tf.* from t_template_county tc ,"
				+ " t_busi_code_fee bf,"
				+ " t_busi_fee tf where tc.template_type=? and tf.status=? "
				+ " and tc.template_id=bf.template_id "
				+ " and tf.fee_id= bf.fee_id  order by ts.fee_id";

		List<BusiFeeDto> fees = createQuery(BusiFeeDto.class, sql,
				template_type,StatusConstants.ACTIVE).list();
		return fees;
	}

	public List<TBusiFeeStd> queryByTemplateId(String templateId) throws Exception{
		String sql ="select distinct a.*,b.fee_name,b.fee_type,c.device_buy_mode,c.device_type " +
				" from t_busi_fee_std a,t_busi_fee b ,t_busi_fee_device c" +
				" where a.fee_id=b.fee_id " +
				" and a.fee_std_id = c.fee_std_id(+) " +
				" and a.template_id=? and b.status=?";
		return this.createQuery(sql, templateId,StatusConstants.ACTIVE).list();
	}

	/**
	 * 查找设备费用标准对应的型号
	 * @param feeStdId
	 * @return
	 * @throws Exception
	 */
	public List<String> queryDeviceModel(String feeStdId) throws Exception{
		String sql ="select device_model from t_busi_fee_device " +
				" where fee_std_id=?";
		return findUniques(sql, feeStdId);
	}

	/**
	 * 查找可以配置费用标准的费用，过滤掉已经配置了费用标准的业务费用
	 * @param templateId
	 * @return
	 * @throws Exception
	 */
	public List<TBusiFee> queryBusiFeeForStdCfg(String templateId, String countyIds)throws Exception{
		String sql = "select distinct t.* from t_busi_fee t,s_county s, s_county sc " +
				" where ((t.fee_type=?)" +
				" or (t.fee_type=?  and t.fee_id not in (select fee_id from t_busi_fee_std where template_id=?)))" +
				" and t.status =?" +
				" and s.area_id = sc.county_id(+)"+
				" and (t.county_id = s.county_id or t.county_id = sc.county_id or t.county_id = ?)"+
				" and s.county_id in ("+countyIds+")";
		return  this.createQuery(TBusiFee.class,sql,
				SystemConstants.FEE_TYPE_DEVICE, SystemConstants.FEE_TYPE_BUSI,
				templateId,StatusConstants.ACTIVE,
				SystemConstants.COUNTY_ALL).list();
	}

	/**
	 * 查找费用标准可以选择的设备型号
	 * @param templateId
	 * @param deviceBuyMode
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	public List<SItemvalue> qeuryDeviceModelForStdCfg(String templateId,
			String feeStdId, String deviceBuyMode, String deviceType,
			String feeId,String[] countyId) throws Exception {
		String itemKey="";String modelType = "";
		if (deviceType.equals(SystemConstants.DEVICE_TYPE_STB)){
			itemKey=DictKey.STB_MODEL.toString();
			modelType = SystemConstants.DEVICE_TYPE_STB;
		}else if (deviceType.equals(SystemConstants.DEVICE_TYPE_CARD)){
			itemKey=DictKey.CARD_MODEL.toString();
			modelType = SystemConstants.DEVICE_TYPE_CARD;
		}else if (deviceType.equals(SystemConstants.DEVICE_TYPE_MODEM)){
			itemKey=DictKey.MODEM_MODEL.toString();
			modelType = SystemConstants.DEVICE_TYPE_MODEM;
		}else {
			modelType = SystemConstants.DEVICE_TYPE_FITTING;
		}
		
		String condition="";
		if (StringHelper.isNotEmpty(feeStdId))
			condition = "and a.fee_std_id <> '"+feeStdId+"'";
		String sql = "";
		
		if(modelType.equals(SystemConstants.DEVICE_TYPE_FITTING)){		
			sql =" select rm.device_model item_value,rm.model_name item_name from r_device_type rt,r_device_model rm " +
					" where rt.device_type = rm.device_type and rt.device_type =?  " +
					" and rm.device_model not in (select a.device_model from t_busi_fee_device a,t_busi_fee_std b where " +
					" a.fee_std_id=b.fee_std_id and b.template_id=? and a.device_buy_mode=? and b.fee_id=? "+condition+") ";
			return this.createQuery(SItemvalue.class,sql, deviceType,templateId,deviceBuyMode, feeId).list();
		}else{
			sql = "select * from vew_dict" +
				" where item_key=?" +
				" and item_value not in (select a.device_model from t_busi_fee_device a,t_busi_fee_std b where " +
				" a.fee_std_id=b.fee_std_id and b.template_id=? and a.device_buy_mode=? and b.fee_id=? "+condition+") ";
			return this.createQuery(SItemvalue.class,sql, itemKey,templateId,deviceBuyMode, feeId).list();
		}
		
	}

	/**
	 * 获取费用SEQ编号
	 */
	public String getBusiFeeStdID() throws Exception{
		return findSequence(SequenceConstants.SEQ_BUSI_FEE_STD).toString();
	}

	/**
	 * 返回费用信息
	 * @param templteId
	 * @return
	 * @throws JDBCException
	 */
	public List<BusiFeeDto> queryBusiFeeStd(String templteId) throws JDBCException {
		String sql = "SELECT * FROM T_BUSI_FEE TF,T_BUSI_FEE_STD BFS,t_busi_code_fee   bf "
				+ " WHERE BFS.TEMPLATE_ID=? AND bf.fee_id = tf.fee_id "
				+ " AND bfs.fee_id=tf.fee_id AND tf.fee_type=? AND tf.status=?";

		return createQuery(BusiFeeDto.class, sql, templteId,
				SystemConstants.FEE_TYPE_BUSI,StatusConstants.ACTIVE).list();
	}
	
	public List<BusiFeeDto> queryBusiFeeStdByBusiCode(String templteId, String busiCode) throws JDBCException {
		String sql = "SELECT * FROM T_BUSI_FEE TF,T_BUSI_FEE_STD BFS,t_busi_code_fee   bf "
				+ " WHERE BFS.TEMPLATE_ID=? AND bf.fee_id = tf.fee_id "
				+ " AND bfs.fee_id=tf.fee_id AND tf.fee_type=? AND tf.status=?"
				+ " and bf.busi_code=?";

		return createQuery(BusiFeeDto.class, sql, templteId,
				SystemConstants.FEE_TYPE_BUSI,StatusConstants.ACTIVE, busiCode).list();
	}
	
	/**
	 * 查询一个业务费信息
	 * @param templteId
	 * @param busiFeeId
	 * @return
	 * @throws JDBCException
	 */
	public BusiFeeDto queryBusiFeeStdByFeeId(String templteId,String busiFeeId) throws JDBCException {
		String sql = "SELECT * FROM T_BUSI_FEE TF,T_BUSI_FEE_STD BFS,t_busi_code_fee   bf "
				+ " WHERE BFS.TEMPLATE_ID=? AND bf.fee_id = tf.fee_id "
				+ " AND bfs.fee_id=tf.fee_id AND tf.fee_type=? AND tf.status=? and bf.fee_id=? ";

		return createQuery(BusiFeeDto.class, sql, templteId,
				SystemConstants.FEE_TYPE_CONTRACT,StatusConstants.ACTIVE,busiFeeId).first();
	}
	
	public BusiFeeDto queryIpBusiFeeStdByFeeId(String templteId,String busiFeeId) throws JDBCException {
		String sql = "SELECT * FROM T_BUSI_FEE TF,T_BUSI_FEE_STD BFS "
				+ " WHERE BFS.TEMPLATE_ID=?  "
				+ " AND bfs.fee_id=tf.fee_id AND tf.fee_type=? AND tf.status=? and tf.fee_id=? ";

		return createQuery(BusiFeeDto.class, sql, templteId,
				SystemConstants.FEE_TYPE_BUSI,StatusConstants.ACTIVE,busiFeeId).first();
	}
	
	/**
	 * 返回设备费用信息
	 * @param templteId
	 * @return
	 * @throws JDBCException
	 * 	去掉t_busi_fee_device，有些设备费用，是不需要配置这个表
	 */
	public List<BusiFeeDto> queryDeviceFeeStd(String templteId) throws JDBCException {
		String sql = "SELECT * FROM T_BUSI_FEE TF,T_BUSI_FEE_STD BFS "
				+ " WHERE BFS.TEMPLATE_ID=?  "
				+ " AND bfs.fee_id=tf.fee_id AND tf.status=?";
		return createQuery(BusiFeeDto.class, sql, templteId,
				StatusConstants.ACTIVE).list();
	}

	public  Pager<BusiFeeDto> queryFeeValue(Integer start , Integer limit , String feeType)throws Exception{
		String sql = "  select t2.fee_name,t2.fee_type,t1.* from t_busi_fee_standard t1,t_busi_fee t2 where t1.fee_id=t2.fee_id   ";
		if(StringHelper.isNotEmpty(feeType)){
			sql = sql + " and t2.fee_type = ?   ";
			 return this.createQuery(BusiFeeDto.class, sql,feeType)
				.setStart(start)
				.setLimit(limit)
				.page();
		}else{
			 return this.createQuery(BusiFeeDto.class, sql)
				.setStart(start)
				.setLimit(limit)
				.page();
		}

	}

}
