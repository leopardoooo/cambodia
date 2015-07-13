package com.ycsoft.report.dao.keycon;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepMemoryKey;

@Component
public class RepMemoryKeyDao extends BaseEntityDao<RepMemoryKey>{

	public RepMemoryKeyDao(){
		
	}
	public List<RepMemoryKey> queryAllOrderbtRemark() throws JDBCException{
		return this.findList("select * from rep_memory_key order by remark,memory_desc");
	}
}
