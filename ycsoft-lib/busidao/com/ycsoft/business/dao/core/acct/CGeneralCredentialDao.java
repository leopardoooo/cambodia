package com.ycsoft.business.dao.core.acct;

/**
 * CGeneralCredentialDao.java	2011/01/24
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CGeneralCredential;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

/**
 * CGeneralCredentialDao -> C_GENERAL_CREDENTIAL table's operator
 */
@Component
public class CGeneralCredentialDao extends BaseEntityDao<CGeneralCredential> {
	

	/**
	 * default empty constructor
	 */
	public CGeneralCredentialDao() {}

	/**
	 * 根据凭据号查找凭据
	 * @param contractId
	 * @param credentialNo
	 * @return
	 */
	public CGeneralCredential queryCredentialById(String credentialNo,String countyId) throws JDBCException {
		String sql = StringHelper.append("select * from c_general_credential c,c_general_contract cg where c.credential_no=?",
				" and c.contract_id=cg.contract_id and cg.county_id=?");
		return createQuery(sql, credentialNo,countyId).first();
	}
	
	/**
	 * 根据合同编号查询最大合同
	 * @param contractId
	 * @return
	 * @throws JDBCException 
	 */
	public CGeneralCredential queryLastCredential(String contractId) throws JDBCException {
		String sql = StringHelper.append("select * from c_general_credential c where c.contract_id=?",
				" order by c.credential_no desc");
		return createQuery(sql, contractId).first();
	}
	
	/**
	 * 更新凭据对应余额
	 * @param fee
	 * @param credentialNo
	 * @throws JDBCException
	 */
	public void updateCredentialByNo(Integer fee , String credentialNo) throws JDBCException {
		String sql = "update c_general_credential c set c.balance=c.balance - ? where c.credential_no=?";
		executeUpdate(sql, fee , credentialNo);
	}

	/**
	 * 查询合同凭据信息
	 * @param contractId
	 * @param start
	 * @param limit
	 * @return
	 * @throws JDBCException 
	 */
	public Pager<CGeneralCredential> queryCredential(String contractId,
			Integer start, Integer limit) throws JDBCException {
		String sql = "select * from c_general_credential t where t.contract_id=?";
		return createQuery(CGeneralCredential.class, sql, contractId).setStart(start).setLimit(limit).page();
	}

	/**
	 * 验证凭据是否重复
	 * @param credentialNos
	 * @return
	 * @throws JDBCException
	 */
	public List<CGeneralCredential> checkCredentialNo(String[] credentialNos) throws JDBCException {
		String sql = "select * from c_general_credential where "+getSqlGenerator().setWhereInArray("credential_no",credentialNos);
		return createQuery(sql).list();
	}

	/**
	 * 根据合同编号删除
	 * @param contractId
	 * @throws Exception
	 */
	public void deleteByContractId(Integer contractId) throws JDBCException{
		String sql = "delete from c_general_credential c where c.contract_id=?";
		executeUpdate(sql, contractId);
	}

}
