/**
 * PPackageProdDao.java	2010/07/05
 */

package com.ycsoft.business.dao.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * PPackageProdDao -> P_PACKAGE_PROD table's operator
 */
@Component
public class PPackageProdDao extends BaseEntityDao<PPackageProd> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4068176199860724936L;
	/**
	 * default empty constructor
	 */
	public PPackageProdDao() {}
	
	public List<PPackageProd> queryPackProdById(String prodId)  throws Exception{
		String sql = "select pp.*, p.prod_type from p_package_prod pp,p_prod p where pp.package_id=p.prod_id and pp.package_id=? ";
		List<PPackageProd> tariffList = this.createQuery(PPackageProd.class,sql, prodId).list();
		return tariffList;
	}
	public void updatePack (List<Object[]> list) throws Exception {
		String	sql = "update p_package_prod set tariff_id = ? ,max_prod_count = ? where prod_id = ? and package_id = ? ";
		executeBatch(sql, list);
	}
	public void deletePack (List<Object[]> list) throws Exception {
		String	sql = "delete p_package_prod  where prod_id = ? and package_id = ?";
		executeBatch(sql, list);
	}
	public void deletePackById (String pkgId,String pkgTariffId) throws Exception {
		String	sql = "delete p_package_prod  where  package_id = ? and (package_tariff_id is null or package_tariff_id = ?)";
		executeUpdate(sql, pkgId,pkgTariffId);
	}
	public List<PPackageProd> queryPackProdByProdId(String prodId)  throws Exception{
		String sql = "select * from p_package_prod where prod_id=? ";
		List<PPackageProd> tariffList = this.createQuery(PPackageProd.class,sql, prodId).list();
		return tariffList;
	}
	
	public List<PPackageProd> queryPkgById(String pkId)  throws Exception{
		String sql = "select  distinct package_id  from p_package_prod where package_id=? ";
		List<PPackageProd> pkList = this.createQuery(PPackageProd.class,sql, pkId).list();
		return pkList;
	}
	
	public List<PPackageProd> queryPkgProdById(String prodId)  throws Exception{
		String sql = "select  distinct prod_id  from p_package_prod where package_id=? ";
		List<PPackageProd> tariffList = this.createQuery(PPackageProd.class,sql, prodId).list();
		return tariffList;
	}
	
	public List<PPackageProd> getPackProdById(String pkgId,String pkgTariffId)  throws Exception{
		List<PPackageProd> tariffList = null;
		if(StringHelper.isNotEmpty(pkgTariffId)){
			String sql = "select p.* from  p_package_prod p where p.package_id= ? and p.package_tariff_id =?";
			tariffList = this.createQuery(PPackageProd.class,sql, pkgId,pkgTariffId).list();
		}else{
			String sql = "select p.* from  p_package_prod p where p.package_id= ?";
			tariffList = this.createQuery(PPackageProd.class,sql, pkgId).list();
		}
		return tariffList;
	}
	
	public List<PPackageProd> getMarketPackProdById(String pkgId,String pkgTariffId)  throws Exception{
		String sql = "select p.* from  p_package_prod p where p.package_id= ? and p.package_tariff_id =? ";
		List<PPackageProd> tariffList = this.createQuery(PPackageProd.class,sql, pkgId,pkgTariffId).list();
		return tariffList;
	}
	
	public List<PPackageProd> queryPackProdById(String pkgId, String pkgTariffId) throws Exception {
		String condition = "";
		if(StringHelper.isNotEmpty(pkgTariffId)){
			condition = " and pa.package_tariff_id = '"+pkgTariffId+"'";
		}
		String sql = "select distinct b.*, b2.max_prod_count,b2.percent, b2.percent_value from "
				+ "(select a2.*, a1.type from ( select item_value type from s_itemvalue si where si.item_key = ?) a1,"
				+ "(select distinct pa.package_id,pa.prod_id,pa.tariff_id,pa.package_tariff_id from p_package_prod pa"
				+ " where pa.package_id = ?"+condition+") a2) b,p_package_prod b2"
				+ " where b2.package_id(+) = b.package_id and b2.prod_id(+) = b.prod_id"
				+ " and b2.tariff_id(+) = b.tariff_id and b2.package_tariff_id(+) = b.package_tariff_id"
				+ " and b2.type(+) = b.type order by b.type desc";
		return this.createQuery(sql, DictKey.SEPARATE_TYPE.toString(), pkgId).list();
	}
}
