package com.ycsoft.report.component.query;

/**
 * 添加报表信息
 * @param depot    
 */
import java.io.File;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.config.TOsdSql;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SResource;
import com.ycsoft.beans.system.SRole;
import com.ycsoft.beans.system.SRoleResource;
import com.ycsoft.business.dao.system.SResourceDao;
import com.ycsoft.business.dao.system.SRoleDao;
import com.ycsoft.business.dao.system.SRoleResourceDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.report.bean.RepCube;
import com.ycsoft.report.bean.RepDatabase;
import com.ycsoft.report.bean.RepDefine;
import com.ycsoft.report.bean.RepDefineLog;
import com.ycsoft.report.bean.RepDetailDim;
import com.ycsoft.report.bean.RepDetailRow;
import com.ycsoft.report.bean.RepDimension;
import com.ycsoft.report.bean.RepGroup;
import com.ycsoft.report.bean.RepMyCube;
import com.ycsoft.report.bean.RepMyreport;
import com.ycsoft.report.bean.RepOptrExport;
import com.ycsoft.report.bean.RepQueryLog;
import com.ycsoft.report.bean.RepSql;
import com.ycsoft.report.bean.RepTotal;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.dao.config.QueryRepDao;
import com.ycsoft.report.dao.config.RepCubeDao;
import com.ycsoft.report.dao.config.RepDefineDao;
import com.ycsoft.report.dao.config.RepDefineLogDao;
import com.ycsoft.report.dao.config.RepDetailDimDao;
import com.ycsoft.report.dao.config.RepDetailRowDao;
import com.ycsoft.report.dao.config.RepGroupDao;
import com.ycsoft.report.dao.config.RepMyCubeDao;
import com.ycsoft.report.dao.config.RepMyreportDao;
import com.ycsoft.report.dao.config.RepOptrExportDao;
import com.ycsoft.report.dao.config.RepQueryLogDao;
import com.ycsoft.report.dao.config.RepSqlDao;
import com.ycsoft.report.dao.config.RepTOsdSqlDao;
import com.ycsoft.report.dao.config.RepTotalDao;
import com.ycsoft.report.dto.RepCubeDto;
import com.ycsoft.report.dto.RepDefineDto;
import com.ycsoft.report.dto.RepKeyDto;
import com.ycsoft.report.dto.RepOptrExportDto;
import com.ycsoft.report.query.QueryManage;
import com.ycsoft.report.query.QueryResultOlap;
import com.ycsoft.report.query.ResultSetExtractor;
import com.ycsoft.report.query.cube.CubeExec;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.DimensionRolap;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.cube.MeasureDataType;
import com.ycsoft.report.query.cube.MeasureGather;
import com.ycsoft.report.query.cube.detail.CubeDetail;
import com.ycsoft.report.query.cube.detail.custom.RowType;
import com.ycsoft.report.query.cube.impl.CubeManage;
import com.ycsoft.report.query.cube.impl.DimensionManage;
import com.ycsoft.report.query.cube.impl.MyCube;
import com.ycsoft.report.query.cube.showclass.cellwarn.MeaWarn;
import com.ycsoft.report.query.cube.showclass.cellwarn.Operator;
import com.ycsoft.report.query.cube.showclass.cellwarn.WarnRowType;
import com.ycsoft.report.query.cube.showclass.cellwarn.WarnValueType;
import com.ycsoft.report.query.key.QueryKey;
import com.ycsoft.report.query.key.Impl.ConKeyValue;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;
import com.ycsoft.report.query.sql.AnalyseMemoryKey;
import com.ycsoft.report.query.sql.AnalyseSqlFactory;
import com.ycsoft.report.query.sql.QuerySql;

/**
 * 报表定义组件
 */

@Component
public class RepDesignComponent extends BaseComponent {

	private SResourceDao sResourceDao;
	private RepSqlDao repSqlDao;
	private RepDefineDao repDefineDao;
	private RepTotalDao repTotalDao;
	private RepGroupDao repGroupDao;
	private QuerySql querySql;
	private RepDefineLogDao repDefineLogDao;
	private QueryRepDao queryRepDao;
	private RepMyreportDao repMyreportDao;
	private QueryKey queryKey;
	private RepQueryLogDao repQueryLogDao;
	private RepOptrExportDao repOptrExportDao;
	private QueryManage queryManage;
	private RepTOsdSqlDao repTOsdSqlDao;
	private RepCubeDao repCubeDao;
	private CubeManage cubeManage;
	private RepMyCubeDao repMyCubeDao;
	private CubeDetail cubeDetail;
	private RepDetailRowDao repDetailRowDao;
	private RepDetailDimDao repDetailDimDao;
	private SRoleDao sRoleDao;
	private SRoleResourceDao sRoleResourceDao;

	/**
	 * 清楚报表缓存
	 * @param rep_id
	 * @throws ReportException
	 */
	public void saveRepClearCache(String rep_id) throws ReportException{
		repQueryLogDao.clearCacheRepAndDetail(rep_id);
	}
	public void saveReportRemark(String rep_id,String remark) throws Exception{
		repDefineDao.updateRemark(rep_id, remark);
	}
	/**
	 * 保存角色资源表
	 * @param sRoleResourceDao
	 */
	public boolean saveRoleResource(String[] roleIds,String res_id) throws Exception{
		return sRoleResourceDao.saveRoleResource(roleIds, res_id);
	}
	/**
	 * 删除角色资源表
	 * @param sRoleResourceDao
	 */
	public boolean removeRoleResource(String res_id) throws Exception{
		return sRoleResourceDao.removeRoleResource(res_id);
	}
	/**
	 * 报表资源分配角色
	 */
	public List<TreeNode> getRepRole(String resource_id) throws Exception {
		List<SRole> roles = sRoleDao.queryRoleBySystemId("7");
		List<SRoleResource> roles2= sRoleResourceDao.getRoleResource(resource_id);
		List<TreeNode> rt = TreeBuilder.convertToNodes( roles, "role_id", "role_name");
		//将县市对应的部门添加至县市的子节点中
	
		for(int j=0;j < roles.size();j++){
			rt.get(j).setLeaf( true );
			rt.get(j).setChecked(false);
			for(SRoleResource o:roles2){
				if (o.getRole_id().equals(rt.get(j).getId())){
					rt.get(j).setChecked(true);
					break;
				}
			}
		}
		return rt;
	}
	/**
	 * 保存模板的警戒配置
	 * 
	 * @throws ReportException
	 */
	public void saveCubeWarn(String template_id, List<MeaWarn> warnlist)
			throws ReportException {
		try {
			String warn_json = "";
			if (warnlist != null && warnlist.size() > 0) {
				warn_json = JsonHelper.fromObject(warnlist);
			}
			repMyCubeDao.updateWarnJson(template_id, warn_json);
		} catch (ReportException e) {
			throw e;
		} catch (Exception e) {
			throw new ReportException(e.getMessage(), e);
		}
	}

	/**
	 * 警戒列类型清单
	 * 
	 * @return
	 */
	public List<ConKeyValue> queryWarnRowTypes() {
		List<ConKeyValue> list = new ArrayList<ConKeyValue>();
		for (WarnRowType o : WarnRowType.values()) {
			ConKeyValue k = new ConKeyValue();
			k.setKey(o.name());
			k.setValue(o.getDesc());
			list.add(k);
		}
		return list;
	}

	/**
	 * 警戒 逻辑操作符清单
	 * 
	 * @return
	 */
	public List<ConKeyValue> queryWarnOperators() {
		List<ConKeyValue> list = new ArrayList<ConKeyValue>();
		for (Operator o : Operator.values()) {
			ConKeyValue k = new ConKeyValue();
			k.setKey(o.name());
			k.setValue(o.getDesc());
			list.add(k);
		}
		return list;
	}

	/**
	 * 多项目 运算符号清单
	 * 
	 * @return
	 */
	public List<ConKeyValue> queryWarnValueTypes() {
		List<ConKeyValue> list = new ArrayList<ConKeyValue>();
		for (WarnValueType o : WarnValueType.values()) {
			ConKeyValue k = new ConKeyValue();
			k.setKey(o.name());
			k.setValue(o.getDesc());
			list.add(k);
		}
		return list;
	}

	/**
	 * 从数据库中提取指标配置的警戒信息
	 * 
	 * @param template_id
	 * @return
	 * @throws ReportException
	 */
	public List<MeaWarn> queryCubeWarn(String template_id) throws ReportException{
		try {
			List<MeaWarn> list=new ArrayList<MeaWarn>();
			RepMyCube mycube= repMyCubeDao.queryMyCubeByTemplateId(template_id);
			if(mycube!=null){
				if(StringHelper.isNotEmpty(mycube.getWarn_json())){
					Type type = new TypeToken<List<MeaWarn>>(){}.getType();
					list=JsonHelper.toList(mycube.getWarn_json(), type);
				}else{
					MyCube cubeconfig = JsonHelper.toObject(mycube.getCube_json(), MyCube.class);
					for(String mea:cubeconfig.getMealist()){
						MeaWarn mw=new MeaWarn();
						mw.setMea(mea);
						list.add(mw);
					}
				}
			}
			return list;
		}catch (ReportException e) {
			throw e;
		} catch (Exception e) {
			throw new ReportException(e);
		}
	}

	/**
	 * 报表可编辑表格的 配置信息
	 * 
	 * @param rep_id
	 * @param rowlist
	 * @throws ReportException
	 */
	public void saveCustomRows(String rep_id, List<RepDetailRow> rowlist)
			throws ReportException {
		try {

			repDetailRowDao.delete(rep_id);
			repDetailDimDao.delete(rep_id);
			RepDetailRow[] rows = new RepDetailRow[rowlist.size()];
			repDetailRowDao.save(rowlist.toArray(rows));

			List<RepDetailDim> list = new ArrayList<RepDetailDim>();
			int i = 1;
			for (DimensionRolap rolap : cubeManage.getCube(rep_id)
					.getDimensionRolaps()) {
				RepDetailDim vo = new RepDetailDim();
				vo.setRep_id(rep_id);
				vo.setIdx(i);
				vo.setDim(rolap.getId());
				list.add(vo);
				i++;
			}
			repDetailDimDao.save(list.toArray(new RepDetailDim[list.size()]));
		} catch (JDBCException e) {
			throw new ReportException(e, e.getSQL());
		}
	}

	/**
	 * 可编辑表格的配置 行类型
	 * 
	 * @return
	 */
	public List<ConKeyValue> queryCustomRowTypes() {
		List<ConKeyValue> list = new ArrayList<ConKeyValue>();
		for (RowType t : RowType.values()) {
			ConKeyValue vo = new ConKeyValue();
			vo.setKey(t.name());
			vo.setValue(t.getDesc());
			list.add(vo);
		}
		return list;
	}

	/**
	 * 查询可编辑表格的配置信息
	 * 
	 * @param rep_id
	 * @return
	 * @throws ReportException
	 */
	public List<RepDetailRow> queryCustomRows(String rep_id)
			throws ReportException {

		return repDetailRowDao.queryRowsByRepid(rep_id);
	}

	/**
	 * 返回可以合计的统计列字符串(格式: 字段a,字段b,字段c)
	 * 
	 * @param cubedetail
	 * @param cube_rep_id
	 * @return
	 * @throws ReportException
	 */
	public String testCubeDetailSql(RepDefineDto cubedetail, String cube_rep_id)
			throws ReportException {

		if (StringHelper.isEmpty(cube_rep_id))
			throw new ReportException("cube_rep_id is null");
		// 验证SQL
		String sql = cubedetail.getSql();
		// 加载列模板
		for (String key : SystemConfig.getTemplateKeyList())
			sql = sql
					.replaceAll(key, SystemConfig.getTemplateKeyMap().get(key));
		// 使用测试值填充SQL 验证SQL正确性
		sql = AnalyseMemoryKey.translateColumnToDesc(sql);
		sql = new AnalyseSqlFactory(sql).getTestSql();
		// dim验证
		CubeExec cube = cubeManage.getCube(cube_rep_id);
		sql = new AnalyseSqlFactory(sql).getCubeDetail(cubeDetail
				.createTestKeys(cube));
		return querySql.testSQL(sql, cubedetail.getDatabase()).get("total");
	}

	/**
	 * cube_rep_id cube的报表ID 用于验证维度键值是否已经配置 返回
	 * 
	 * @param cubedetail
	 * @param cube_rep_id
	 */
	public String saveCubeDetail(RepDefineDto cubedetail, String cube_rep_id)
			throws ReportException {
		try {
			if (StringHelper.isEmpty(cube_rep_id))
				throw new ReportException("cube_rep_id is null");
			if (StringHelper.isEmpty(cubedetail.getSql()))
				throw new ReportException("sql is null");
			boolean insert_sign = false;
			if (StringHelper.isEmpty(cubedetail.getRep_id())) {
				insert_sign = true;
				cubedetail.setRep_id(sResourceDao
						.findSequence("SEQ_S_RESOURCE").toString());
			}
			// 验证sql
			this.testCubeDetailSql(cubedetail, cube_rep_id);
			// 验证查询条件key是否一致
			// 验证统计和明细的查询条件是是否一致
			String detailkeylist = queryKey.toString(queryKey
					.getQueryKeyList(cubedetail.getSql()));
			String totalkeylist = queryKey.toString(queryKey
					.getQueryKeyList(cubedetail.getMain_sql()));
			if (!detailkeylist.equals(totalkeylist))
				throw new ReportException("主报表和明细报表查询条件不一致");
			// 验证cube配置的维度是否都在明细报表中
			CubeExec cube = cubeManage.getCube(cube_rep_id);
			for (DimensionRolap rolap : cube.getDimensionRolaps()) {
				if (cubedetail.getSql().indexOf(rolap.getDim().getPrefixid()) <= 0)
					throw new ReportException(rolap.getDim().getPrefixid()
							+ ": is not config in cube_detail_sql.");
			}
			// SQL定义
			RepSql repSql = new RepSql();
			repSql.setRep_id(cubedetail.getRep_id());
			repSql.setQuery_sql(cubedetail.getSql());
			// 统计项定义
			RepTotal repTotal = new RepTotal();
			repTotal.setRep_id(cubedetail.getRep_id());
			repTotal.setRep_column(cubedetail.getRep_total_list());

			if (insert_sign) {
				repDefineDao.save(cubedetail);
				repSqlDao.saveSql(repSql);
				repTotalDao.saveRepTotal(repTotal);
			} else {
				// 更新自己
				repDefineDao.update(cubedetail);
				repSqlDao.delete(repSql.getRep_id());
				repSqlDao.saveSql(repSql);
				repTotalDao.delete(repTotal.getRep_id());
				repTotalDao.saveRepTotal(repTotal);
			}
			return cubedetail.getRep_id();
		} catch (JDBCException e) {
			throw new ReportException(e, e.getSQL());
		} catch (ReportException e) {
			throw e;
		}
	}

	/**
	 * 查询cube明细报表配置
	 * 
	 * @param rep_id
	 * @param mea_detail_id
	 * @throws ReportException
	 */
	public RepDefineDto queryCubeDetail(String rep_id, String mea_detail_id)
			throws ReportException {

		try {
			RepDefineDto cubedetail = new RepDefineDto();
			if (StringHelper.isNotEmpty(mea_detail_id)) {
				BeanUtils.copyProperties(repDefineDao
						.getRepDefine(mea_detail_id), cubedetail);
				cubedetail.setSql(repSqlDao.getSql(mea_detail_id));
				cubedetail.setRep_total_list(repTotalDao
						.findTotalList(mea_detail_id));
			} else {
				BeanUtils.copyProperties(repDefineDao.getRepDefine(rep_id),
						cubedetail);
				cubedetail.setRep_type(ReportConstants.REP_TYPE_OLAP_DETAIL);
				cubedetail.setRep_id("");
			}
			cubedetail.setMain_sql(repSqlDao.getSql(rep_id));
			return cubedetail;
		} catch (ReportException e) {
			throw e;
		} catch (JDBCException e) {
			throw new ReportException(e, e.getSQL());
		}
	}

	/**
	 * 指标的显示格式
	 * 
	 * @return
	 */
	public List<QueryKeyValue> queryMeaDataType() {
		List<QueryKeyValue> list = new ArrayList<QueryKeyValue>();
		for (MeasureDataType type : MeasureDataType.values()) {
			QueryKeyValue vo = new QueryKeyValue();
			vo.setId(type.name());
			vo.setName(type.getDesc());
			list.add(vo);
		}
		return list;
	}

	/**
	 * 根据报表ID获取cube的维度清单
	 * 
	 * @param rep_id
	 * @return
	 * @throws ReportException
	 */
	public List<Dimension> queryDimensionsByRepId(String rep_id)
			throws ReportException {
		if (StringHelper.isEmpty(rep_id))
			throw new ReportException("rep_id is null");
		List<Dimension> list = new ArrayList<Dimension>();
		for (RepCube repcube : repCubeDao.queryRepCube(rep_id)) {
			if (DimensionType.crosswise.name().equals(repcube.getColumn_type())
					|| DimensionType.vertical.name().equals(
							repcube.getColumn_type())) {
				Dimension dim = DimensionManage.getDimension(repcube
						.getColumn_define());
				if (dim == null)
					throw new ReportException(
							rep_id
									+ ":rep_cube.column_define is null or is not dimension.");
				list.add(dim);
			}
		}
		return list;
	}

	/**
	 * 保存cube定义 并验证cube模板的有效性
	 * 
	 * @param rep_id
	 * @param optr_id
	 * @param repcubes
	 * @throws ReportException
	 */
	public void saveCuleDefine(String rep_id, String optr_id,
			List<RepCube> repcubes) throws ReportException {
		try {
			String validate = validateCube(rep_id, repcubes);
			if (validate != null)
				throw new ReportException("validate_cube_error:" + validate);
			// 判断cube配置有无变化，有变化则清理个人cube配置
			List<RepCube> oldrepcubes = repCubeDao.queryRepCube(rep_id);
			if (oldrepcubes == null || oldrepcubes.size() == 0) {
				repCubeDao.save(repcubes.toArray(new RepCube[repcubes.size()]));
			} else {
				String oldcube = JsonHelper.fromObject(oldrepcubes);
				String newcube = JsonHelper.fromObject(repcubes);
				if (!oldcube.equals(newcube)) {
					repCubeDao.deleteRepCube(rep_id);
					repCubeDao.save(repcubes.toArray(new RepCube[repcubes
							.size()]));
				}
			}
			cubeManage.initAll();
			CubeExec cube = cubeManage.getCube(rep_id);
			// 验证cube模板的有效性，如果失效了是改状态
			for (RepMyCube repmycube : repMyCubeDao
					.queryMyCube(rep_id, optr_id)) {
				boolean is_right = true;
				try {
					MyCube mycube = JsonHelper.toObject(repmycube
							.getCube_json(), MyCube.class);
					cubeManage.setCube(cube, mycube);
				} catch (Exception e) {
					is_right = false;
				}
				if (is_right
						&& ReportConstants.VALID_F
								.equals(repmycube.getStatus())) {
					repMyCubeDao.updateStatus(repmycube.getRep_id(), repmycube
							.getOptr_id(), repmycube.getName(), is_right);
				}
				if (!is_right
						&& ReportConstants.VALID_T
								.equals(repmycube.getStatus())) {
					repMyCubeDao.updateStatus(repmycube.getRep_id(), repmycube
							.getOptr_id(), repmycube.getName(), is_right);
				}
			}
		} catch (ReportException e) {
			throw e;
		} catch (JDBCException e) {
			throw new ReportException(e, e.getSQL());
		} catch (Exception e) {
			throw new ReportException("error:" + e.getMessage(), e);
		}
	}

	/**
	 * 查询cube的设置清单
	 * 
	 * @param query_id
	 * @return
	 * @throws ReportException
	 */
	public String queryMyCubeConfig(String query_id) throws ReportException {
		if (StringHelper.isEmpty(query_id))
			throw new ReportException("query_id is null");
		QueryResultOlap olap = (QueryResultOlap) queryManage.get(query_id);
		return cubeManage.getMyCubeConfig(olap.getRepId(), cubeManage
				.getMyCube(olap.getCube()));
	}

	/**
	 * 更新模板默认首选
	 * 
	 * @param optr_id
	 * @param rep_id
	 * @param name
	 * @throws ReportException
	 */
	public void updateMyCubeDefault(String optr_id, String rep_id, String name)
			throws ReportException {
		if (StringHelper.isEmpty(rep_id))
			throw new ReportException("repmycube.rep_id is null");
		if (StringHelper.isEmpty(name))
			throw new ReportException("mycubename is null");
		repMyCubeDao.updateMyCubeDufaultShow(rep_id, optr_id, name);
	}

	/**
	 * 更新备注
	 * 
	 * @param optr_id
	 * @param rep_id
	 * @param name
	 * @param remark
	 * @throws ReportException
	 */
	public void updateMyCubeRemark(String optr_id, String rep_id, String name,
			String remark) throws ReportException {
		if (StringHelper.isEmpty(rep_id))
			throw new ReportException("repmycube.rep_id is null");
		if (StringHelper.isEmpty(name))
			throw new ReportException("mycubename is null");

		repMyCubeDao.updateMyCubeRemark(rep_id, optr_id, name, remark);
	}

	/**
	 * 删除模板
	 * 
	 * @param optr_id
	 * @param rep_id
	 * @param mycube_name
	 * @throws ReportException
	 */
	public void deleteMyCube(String optr_id, String rep_id, String mycube_name)
			throws ReportException {

		if (StringHelper.isEmpty(rep_id))
			throw new ReportException("repmycube.rep_id is null");
		if (StringHelper.isEmpty(mycube_name))
			throw new ReportException("mycubename is null");

		repMyCubeDao.deleteMyCubeByName(rep_id, optr_id, mycube_name);
	}

	/**
	 * 保存我的模板
	 * 
	 * @param optr_id
	 * @param repmycube
	 * @throws ReportException
	 */
	public void saveMycube(String query_id, String optr_id, String rep_id,
			String name, String remark, Boolean default_show)
			throws ReportException {
		try {

			if (StringHelper.isEmpty(query_id))
				throw new ReportException("query_id is null");
			if (StringHelper.isEmpty(name))
				throw new ReportException("mycube.name is null");
			if (StringHelper.isEmpty(rep_id))
				throw new ReportException("mycube.rep_id is null");
			if (default_show == null)
				throw new ReportException("mycube.show is null");
			if (repMyCubeDao.checkMyCubeName(rep_id, optr_id, name))
				throw new ReportException("模板名称已使用，请换其他名称！");
			RepMyCube repmycube = new RepMyCube();
			repmycube.setName(name);
			repmycube.setStatus(ReportConstants.VALID_T);
			repmycube.setOptr_id(optr_id);
			repmycube.setRep_id(rep_id);
			QueryResultOlap olap = (QueryResultOlap) queryManage.get(query_id);
			repmycube.setCube_json(JsonHelper.fromObject(cubeManage
					.getMyCube(olap.getCube())));
			repmycube.setRemark(remark);
			repmycube.setDefault_show(default_show.toString());
			repmycube.setTemplate_id(repMyCubeDao.findSequence().toString());
			if (default_show)
				this.updateMyCubeDefault(optr_id, rep_id, name);
			repMyCubeDao.saveRepMyCube(repmycube);
		} catch (JDBCException e) {
			throw new ReportException(e, e.getSQL());
		} catch (Exception e) {
			throw new ReportException(e);
		}
	}

	/**
	 * 查询我的模板
	 * 
	 * @param optr_id
	 * @return
	 * @throws ReportException
	 */
	public List<RepMyCube> queryMyCubes(String optr_id, String rep_id)
			throws ReportException {

		if (StringHelper.isEmpty(rep_id))
			throw new ReportException("rep_id is null");
		List<RepMyCube> list = repMyCubeDao.queryMyCube(rep_id, optr_id);
		for (RepMyCube repmycube : list) {
			if (StringHelper.isNotEmpty(repmycube.getCube_json())) {
				try {
					repmycube.setCube_config_text(cubeManage.getMyCubeConfig(
							repmycube.getRep_id(), JsonHelper.toObject(
									repmycube.getCube_json(), MyCube.class)));
				} catch (Exception e) {
					throw new ReportException(e);
				}
			}
		}
		return list;
	}

	/**
	 * 维定义类型
	 */
	public List<QueryKeyValue> queryCubeDimensionTypes() {

		List<QueryKeyValue> list = new ArrayList<QueryKeyValue>();

		for (DimensionType type : DimensionType.values()) {
			QueryKeyValue vo = new QueryKeyValue();
			vo.setId(type.name());
			vo.setName(type.getDesc());
			list.add(vo);
		}
		return list;
	}

	/**
	 * 度量计算方法
	 * 
	 * @return
	 */
	public List<QueryKeyValue> queryMeasureTypes() {
		List<QueryKeyValue> list = new ArrayList<QueryKeyValue>();

		for (MeasureGather type : MeasureGather.values()) {
			QueryKeyValue vo = new QueryKeyValue();
			vo.setId(type.name());
			vo.setName(type.name());
			list.add(vo);
		}
		return list;
	}

	/**
	 * 维定义列表
	 * 
	 * @return
	 * @throws ReportException 
	 */
	public List<Dimension> queryDimensions(String rep_id) throws ReportException {
		try {
			String database= repDefineDao.getRepDefine(rep_id).getDatabase();
			List<Dimension> list=new ArrayList<Dimension>();
			for(Dimension dim: DimensionManage.getDimList()){
				if(dim.getDatabase().equals(database))
					list.add(dim);
			}
			return list;
		} catch (ReportException e) {
			throw e;
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
		
	}

	/**
	 * 验证cube配置
	 * 
	 * @return
	 * @throws ReportException
	 */
	public String validateCube(String rep_id, List<RepCube> repcubes)
			throws ReportException {
		try {
			if (repcubes == null || repcubes.size() == 0)
				return "cubedefine is null";
			String sql = repSqlDao.getSql(rep_id);
			sql = new AnalyseSqlFactory(sql).getTestSql();
			QueryResultOlap olap = new QueryResultOlap(null, repDefineDao
					.getRepDefine(rep_id), sql, null);
			CubeExec cube = cubeManage.createCube(repcubes);
			return cube.validate(olap);
		} catch (JDBCException e) {
			throw new ReportException(e, e.getSQL());
		} catch (ReportException e) {
			throw e;
		}
	}

	/**
	 * 查询cube定义配置
	 * 
	 * @param rep_id
	 * @return
	 * @throws ReportException
	 */
	public List<RepCubeDto> queryCuleDefine(String rep_id)
			throws ReportException {
		try {
			List<RepCube> repcubelist = repCubeDao.queryRepCube(rep_id);
			final Map<String, RepCube> repcubemap = CollectionHelper
					.converToMapSingle(repcubelist, "column_code");
			String sql = repSqlDao.getSql(rep_id);
			sql = new AnalyseSqlFactory(sql).getTestSql();
			List<RepCubeDto> list = querySql.getColumnRSMD(sql,
					repDefineDao.getRepDefine(rep_id).getDatabase(),
					new ResultSetExtractor<List<RepCubeDto>>() {
						public List<RepCubeDto> extractData(ResultSet result)
								throws Exception {
							List<RepCubeDto> tlist = new ArrayList<RepCubeDto>();
							ResultSetMetaData rsmd = result.getMetaData();
							for (int i = 0; i < rsmd.getColumnCount(); i++) {
								RepCubeDto dto = new RepCubeDto();
								if (repcubemap.containsKey(rsmd
										.getColumnName(i + 1))) {
									BeanHelper.copyProperties(dto, repcubemap
											.get(rsmd.getColumnName(i + 1)));
									dto.setAttribute_type(rsmd
											.getColumnTypeName(i + 1));
								} else {
									dto.setAttribute_type(rsmd
											.getColumnTypeName(i + 1));
									dto.setColumn_code(rsmd
											.getColumnName(i + 1));
								}
								tlist.add(dto);
							}
							return tlist;
						}
					});
			return list;
		} catch (JDBCException e) {
			throw new ReportException(e, e.getSQL());
		} catch (ReportException e) {
			throw e;
		} catch (Exception e) {
			throw new ReportException("error" + e.getMessage(), e);
		}
	}

	/**
	 * 获得组装后查询语句
	 * 
	 * @param query_id
	 * @param system_optr_map
	 * @return
	 * @throws ReportException
	 */
	public String showSql(String query_id, SOptr optr) throws ReportException {
		try {
			// 判断权限,只有数据权限是广电级和具有角色资源的 操作员能查询
			/**
			 * danjp
			 * 操作员有查看SQL或修改备注的数据权限
			 */
//			if (!optr.getLogin_name().equals("admin")
//					&& !SystemConfig.getOptrConfigRepMap().containsKey(
//							optr.getOptr_id()))
//				throw new ReportException("权限不足");

			RepQueryLog log = repQueryLogDao.getRepQuerLog(query_id);
			String sql = repSqlDao.getSql(log.getRep_id());
			sql = querySql.translateTemplateKey(sql);
			// 使用测试值填充SQL 验证SQL正确性
			sql = AnalyseMemoryKey.translateColumnToDesc(sql);
			List<ConKeyValue> keylist = new ArrayList<ConKeyValue>();
			if (StringHelper.isNotEmpty(log.getKeylist())) {
				Map<String, String> keyvaluemap = queryKey.toValueMap(log
						.getKeylist());
				for (Object key : keyvaluemap.keySet().toArray()) {
					ConKeyValue o = new ConKeyValue();
					o.setKey(key.toString());
					o.setValue(keyvaluemap.get(key));
					keylist.add(o);
				}
			}
			sql = new AnalyseSqlFactory(sql).getAnaSql(keylist);
			return sql;
		} catch (JDBCException e) {
			throw new ReportException(e, e.getSQL());
		} catch (ReportException e) {
			throw e;
		} catch (Exception e) {
			throw new ReportException("system_error.", e);
		}
	}

	public String queryOsdSqlTitle(String query_id) throws ReportException {

		try {
			TOsdSql o = repTOsdSqlDao.findByKey(query_id);
			if (o != null)
				return o.getTitle();
			return "";
		} catch (JDBCException e) {
			throw new ReportException(e, e.getSQL());
		}
	}

	public void createOsdSql(String query_id, String title, String optr_id)
			throws ReportException {
		try {
			TOsdSql o_old = repTOsdSqlDao.findByKey(query_id);
			if (o_old != null) {
				repTOsdSqlDao.remove(query_id);
			}

			RepQueryLog log = repQueryLogDao.getRepQuerLog(query_id);
			String sql = repSqlDao.getSql(log.getRep_id());
			sql = querySql.translateTemplateKey(sql);
			sql = AnalyseMemoryKey.translateColumnToDesc(sql);
			List<ConKeyValue> keylist = new ArrayList<ConKeyValue>();
			if (StringHelper.isNotEmpty(log.getKeylist())) {
				Map<String, String> keyvaluemap = queryKey.toValueMap(log
						.getKeylist());
				for (Object key : keyvaluemap.keySet().toArray()) {
					ConKeyValue o = new ConKeyValue();
					o.setKey(key.toString());
					o.setValue(keyvaluemap.get(key));
					keylist.add(o);
				}
			}
			sql = new AnalyseSqlFactory(sql).getAnaSql(keylist);
			TOsdSql osdsql = new TOsdSql();
			osdsql.setQuery_id(log.getQuery_id());
			osdsql.setSql_content(sql);
			osdsql.setStatus("ACTIVE");
			osdsql.setTitle(title);
			osdsql.setOptr_id(optr_id);
			osdsql.setCreate_time(new Date());

			repTOsdSqlDao.saveRepTOsdSql(osdsql);

		} catch (JDBCException e) {
			throw new ReportException(e, e.getSQL());
		} catch (Exception e) {
			throw new ReportException("system_error.", e);
		}
	}

	public void setQueryManage(QueryManage queryManage) {
		this.queryManage = queryManage;
	}

	public void setRepOptrExportDao(RepOptrExportDao repOptrExportDao) {
		this.repOptrExportDao = repOptrExportDao;
	}

	public void saveColExport(String query_id, String optr_id, String... cols)
			throws ReportException {

		try {
			String rep_id = queryManage.get(query_id).getRepId();
			repOptrExportDao.deletebyoptridrepid(rep_id, optr_id);

			if (cols != null && cols.length > 0) {

				for (String col : cols) {
					if (col != null && !col.equals("")) {
						RepOptrExport bean = new RepOptrExport();
						bean.setRep_id(rep_id);
						bean.setOptr_id(optr_id);
						bean.setCol_idx(Integer.valueOf(col));
						repOptrExportDao.save(bean);
					}
				}
			}
		} catch (JDBCException e) {
			throw new ReportException(e, e.getSQL());
		} catch (Exception e) {
			throw new ReportException("system_error", e);
		}
	}

	/**
	 * 获得报表列配置
	 * 
	 * @param rep_id
	 * @param query_id
	 * @return
	 * @throws ReportException
	 */
	public List<RepOptrExportDto> findColExcport(String query_id, String optr_id)
			throws ReportException {
		try {
			String rep_id = queryManage.get(query_id).getRepId();
			RepDefine repdefine = repDefineDao.getRepDefine(rep_id);
			String sql = repSqlDao.getSql(rep_id);
			sql = querySql.translateTemplateKey(sql);
			// 使用测试值填充SQL 验证SQL正确性
			sql = AnalyseMemoryKey.translateColumnToDesc(sql);
			sql = new AnalyseSqlFactory(sql).getTestSql();
			// 列名
			List<String> columnnamelist = querySql.getColumnNameList(sql,
					repdefine.getDatabase());

			// 获取后台列配置
			Integer cols[] = repOptrExportDao
					.queryExportConfig(rep_id, optr_id);
			Map<String, Integer> cols_check_map = new HashMap<String, Integer>();
			if (cols != null) {
				for (Integer col : cols)
					cols_check_map.put(col.toString(), col);
			}

			List<RepOptrExportDto> explist = new ArrayList<RepOptrExportDto>();
			for (int i = 0; i < columnnamelist.size(); i++) {
				RepOptrExportDto o = new RepOptrExportDto();
				o.setCol_idx(i);
				o.setCol_name(columnnamelist.get(i));
				if (cols_check_map.containsKey(String.valueOf(i)))
					o.setIscheck(true);
				else
					o.setIscheck(false);
				explist.add(o);
			}
			return explist;
		} catch (JDBCException e) {
			throw new ReportException(e, e.getSQL());
		} catch (ReportException e) {
			throw e;
		}

	}

	/**
	 * 查找主报表对应的明细查询的配置信息
	 * 
	 * @param rep_id
	 * @param optr
	 * @return
	 * @throws ReportException
	 */
	public RepDefineDto findRepDefineDetail(String rep_id)
			throws ReportException {
		if (StringHelper.isEmpty(rep_id))
			throw new ReportException("主报表未定义或未保存.");

		try {
			RepDefineDto rd = new RepDefineDto();
			BeanUtils.copyProperties(repDefineDao.getRepDefine(rep_id), rd);
			rd.setQuiee_raq("");
			if (StringHelper.isNotEmpty(rd.getDetail_id())) {
				rd.setRep_total_list(repTotalDao.findTotalList(rd
						.getDetail_id()));
				rd.setSql(repSqlDao.getSql(rd.getDetail_id()));
				rd.setRep_type(ReportConstants.REP_TYPE_COMMON);
			}
			return rd;
		} catch (BeansException e) {
			throw new ReportException(e);
		} catch (JDBCException e) {
			throw new ReportException(e, e.getSQL());
		}

	}

	/**
	 * 按rep_optr_ConfigRep配置查询报表扩展信息
	 * 
	 * @param rep_id
	 * @param optr_id
	 * @return
	 * @throws Exception
	 */
	public RepDefineDto findRepDefine(String rep_id, SOptr optr)
			throws Exception {
		if (StringHelper.isNotEmpty(rep_id)) {
			RepDefineDto rd = new RepDefineDto();
			BeanUtils.copyProperties(repDefineDao.getRepDefine(rep_id), rd);
			SResource res = sResourceDao.findByKey(rep_id);
			rd.setRep_name(res.getRes_name());
			rd.setRes_pid(res.getRes_pid());
			rd.setRes_pid_text(sResourceDao.findByKey(res.getRes_pid())
					.getRes_name());
			rd.setSort_num(res.getSort_num());
			rd.setRep_total_list(repTotalDao.findTotalList(rep_id));
			rd.setSql(repSqlDao.getSql(rep_id));
			rd.setRep_group(repGroupDao.findColumnName(rep_id));
			if (StringHelper.isNotEmpty(rd.getQuiee_raq())) {
				String quiee_raq_text = "";
				String quieeFilePath = ReportConstants.CONTEXT_REAL_PATH
						+ File.separator + "reportFiles" + File.separator;
				if (new File(quieeFilePath + rd.getQuiee_raq() + ".raq")
						.exists())
					quiee_raq_text = quiee_raq_text + rd.getQuiee_raq()
							+ ".raq";
				if (new File(quieeFilePath + rd.getQuiee_raq() + "_arg.raq")
						.exists())
					quiee_raq_text = quiee_raq_text + "," + rd.getQuiee_raq()
							+ "_arg.raq";
				rd.setQuiee_raq_text(quiee_raq_text);
			}
			return rd;
		}
		return null;
	}

	/**
	 * 测试SQL 返回值null表示正常，total_list 是返回的可合计字段
	 * 
	 * @param total_list
	 * @param repDefineDto
	 * @param system_optr_map
	 * @return
	 * @throws ReportException
	 */
	public Map<String, String> testSql(RepDefineDto repDefineDto)
			throws ReportException {
		// 验证SQL
		String sql = repDefineDto.getSql();
		// 如果验证出错 则返回错误值
		try {
			//验证文件组件的数据源是否和报表数据一致
			for(RepKeyDto  key:queryKey.getQueryKeyList(repDefineDto.getSql())){
				if(key.getHtmlcode().equals(ReportConstants.htmlcode_fileupload)){
					if(!repDefineDto.getDatabase().equals(key.getDatabase()))
						throw new ReportException("Error:文件组件"+key.getKey()+"的数据源和报表不一致!");
				}
			}
			sql = querySql.translateTemplateKey(sql);
			// 使用测试值填充SQL 验证SQL正确性
			sql = AnalyseMemoryKey.translateColumnToDesc(sql);
			sql = new AnalyseSqlFactory(sql).getTestSql();
			return querySql.testSQL(sql, repDefineDto.getDatabase());
		} catch (ReportException e) {
			throw e;
		} catch (Exception e) {
			throw new ReportException("System_error:" + e.getMessage(), e, sql);
		}
	}

	/**
	 * 保存报表定义 返回报表ID
	 * 
	 * @param repDefineDto
	 * @param system_optr_map
	 * @param optr
	 * @return
	 * @throws Exception
	 */
	public String saveRepDesign(RepDefineDto repDefineDto, SOptr optr)
			throws ReportException {
		String sql = "";
		try {
			boolean insert_sign = false;
			// rep_id为空 表示增加一张报表
			if (repDefineDto.getRep_id() == null
					|| repDefineDto.getRep_id().equals("")) {
				insert_sign = true;
				repDefineDto.setRep_id(sResourceDao.findSequence(
						"SEQ_S_RESOURCE").toString());
			} else {
				repDefineDto.setDetail_id(repDefineDao.getRepDefine(
						repDefineDto.getRep_id()).getDetail_id());
				
				//判断数据源和维度的数据是否一致，不一致报错
				CubeExec cube= cubeManage.getCube(repDefineDto.getRep_id());
				if(cube!=null){
					for(DimensionRolap roap:cube.getDimensionRolaps()){
						if(!roap.getDim().getDatabase().equals(repDefineDto.getDatabase()))
							throw new ReportException("ERROR：主报表数据源("+
									SystemConfig.getDatabaseMap().get(repDefineDto.getDatabase()).getName()
									+")和已配置的维度"+roap.getDim().getName()+"数据源("+SystemConfig.getDatabaseMap().get(roap.getDim().getDatabase()).getName()+")不一致！");
					
					}
				}
			}
			//验证文件组件的数据源是否和报表数据一致
			for(RepKeyDto  key:queryKey.getQueryKeyList(repDefineDto.getSql())){
				if(key.getHtmlcode().equals(ReportConstants.htmlcode_fileupload)){
					if(!repDefineDto.getDatabase().equals(key.getDatabase()))
						throw new ReportException("Error:文件组件"+key.getKey()+"的数据源和报表不一致!");
				}
			}
			// 验证SQL
			sql = repDefineDto.getSql();
			sql = querySql.translateTemplateKey(sql);
			// 使用测试值填充SQL 验证SQL正确性
			sql = AnalyseMemoryKey.translateColumnToDesc(sql);
			sql = new AnalyseSqlFactory(sql).getTestSql();
			querySql.testSQL(sql, repDefineDto.getDatabase());

			// 如果存在明细报表配置，则验证两边sql查询条件是否一致
			if (repDefineDto.getDetail_id() != null
					&& !repDefineDto.getDetail_id().equals("")) {
				String detailkeylist = queryKey.toString(queryKey
						.getQueryKeyList(repSqlDao.getSql(repDefineDto
								.getDetail_id())));
				String totalkeylist = queryKey.toString(queryKey
						.getQueryKeyList(repDefineDto.getSql()));
				if (!detailkeylist.equals(totalkeylist))
					throw new ReportException("明细报表和主报表查询条件不一致");

			}
			// SQL定义
			RepSql repSql = new RepSql();
			repSql.setRep_id(repDefineDto.getRep_id());
			repSql.setQuery_sql(repDefineDto.getSql());

			// 资源
			SResource sResource = new SResource();
			sResource.setRes_id(repDefineDto.getRep_id());
			sResource.setRes_name(repDefineDto.getRep_name());
			sResource.setRes_pid(repDefineDto.getRes_pid());
			sResource.setRes_type("MENU");
			sResource.setRes_status("ACTIVE");
			sResource.setSort_num(repDefineDto.getSort_num());
			sResource.setSub_system_id("7");

			// 记录日志
			RepDefineLog log = new RepDefineLog();
			log.setRep_id(repDefineDto.getRep_id());
			log.setRep_name(repDefineDto.getRep_name());
			log.setCreate_date(new Date());
			log.setOptr_id(optr.getOptr_id());
			log.setOptr_login_name(optr.getLogin_name());

			// 统计项定义
			RepTotal repTotal = new RepTotal();
			if (repDefineDto.getRep_type().equals(
					ReportConstants.REP_TYPE_COMMON)) {
				repTotal.setRep_id(repDefineDto.getRep_id());
				repTotal.setRep_column(repDefineDto.getRep_total_list());
			}

			// 分组项定义
			RepGroup repGroup = new RepGroup();
			if (repDefineDto.getRep_type().equals(
					ReportConstants.REP_TYPE_COMMON)) {
				repGroup.setRep_id(repDefineDto.getRep_id());
				repGroup.setRep_column(repDefineDto.getRep_group());
			}
			if (insert_sign) {
				sResourceDao.save(sResource);
				repDefineDao.save(repDefineDto);
				repSqlDao.saveSql(repSql);
				repTotalDao.saveRepTotal(repTotal);
				log.setUpdate_type("INSERT");
				repDefineLogDao.save(log);
				repGroupDao.saveGroup(repGroup);
			} else {
				sResourceDao.update(sResource);
				repDefineDao.update(repDefineDto);
				if (StringHelper.isNotEmpty(repDefineDto.getDetail_id())) {
					RepDefine detail = repDefineDao.findByKey(repDefineDto
							.getDetail_id());
					if (detail != null) {
						detail.setDatabase(repDefineDto.getDatabase());
						detail.setRep_info(repDefineDto.getRep_info());
						detail.setRep_type(repDefineDto.getRep_type());
						repDefineDao.update(detail);
					}
				}
				repSqlDao.delete(repDefineDto.getRep_id());
				repSqlDao.saveSql(repSql);
				repTotalDao.delete(repDefineDto.getRep_id());
				repTotalDao.saveRepTotal(repTotal);
				repGroupDao.remove(repDefineDto.getRep_id());
				repGroupDao.saveGroup(repGroup);
				log.setUpdate_type("UPDATE");

				repDefineLogDao.save(log);
				// 设置查询缓存无效化
				repQueryLogDao.clearCacheByRepUpdate(repDefineDto.getRep_id());
			}
			return repDefineDto.getRep_id();
		} catch (ReportException e) {
			throw e;
		} catch (Exception e) {
			throw new ReportException("System_Error:" + e.getMessage(), e, sql);
		}
	}

	/**
	 * 保存明细报表
	 * 
	 * @param repDefineDto
	 * @param system_optr_map
	 * @param optr
	 * @return
	 * @throws JDBCException
	 * @throws ReportException
	 */
	public String saveDetailsReport(RepDefineDto repDefineDto, SOptr optr)
			throws ReportException {
		boolean insert_sign = false;
		String sql = "";
		try {
			// detail_id为空 表示增加一张明细报表
			String detail_id = repDefineDto.getDetail_id();
			sql = repDefineDto.getSql();
			// 查询语句为空，则认为不设置明细查询
			if (sql == null || sql.trim().equals("")) {
				if (detail_id == null || detail_id.equals(""))
					return null;

				repDefineDao.remove(detail_id);
				repSqlDao.delete(detail_id);
				repTotalDao.delete(detail_id);
				repDefineDao.updateDetail(repDefineDto.getRep_id(), "");
				return null;
			}
			String rep_pid = repDefineDto.getRep_id();
			if (detail_id == null || detail_id.equals("")) {
				insert_sign = true;
				detail_id = sResourceDao.findSequence("SEQ_S_RESOURCE")
						.toString();
			}
			repDefineDto.setRep_id(detail_id);
			repDefineDto.setDetail_id(null);

			// 验证SQL
			// 使用测试值填充SQL 验证SQL正确性
			sql = querySql.translateTemplateKey(sql);
			sql = AnalyseMemoryKey.translateColumnToDesc(sql);
			sql = new AnalyseSqlFactory(sql).getTestSql();
			querySql.testSQL(sql, repDefineDto.getDatabase());

			// 验证统计和明细的查询条件是是否一致
			String detailkeylist = queryKey.toString(queryKey
					.getQueryKeyList(repDefineDto.getSql()));
			String totalkeylist = queryKey.toString(queryKey
					.getQueryKeyList(repDefineDto.getMain_sql()));

			if (!detailkeylist.equals(totalkeylist))
				throw new ReportException("主报表和明细报表查询条件不一致");

			// SQL定义
			RepSql repSql = new RepSql();
			repSql.setRep_id(detail_id);
			repSql.setQuery_sql(repDefineDto.getSql());

			// 统计项定义
			RepTotal repTotal = new RepTotal();
			if (repDefineDto.getRep_type().equals(
					ReportConstants.REP_TYPE_COMMON)) {
				repTotal.setRep_id(repDefineDto.getRep_id());
				repTotal.setRep_column(repDefineDto.getRep_total_list());
			}

			if (insert_sign) {
				repDefineDao.save(repDefineDto);
				repSqlDao.saveSql(repSql);
				repTotalDao.saveRepTotal(repTotal);

				// 更新主报表统计报表的明细ID
				repDefineDto.setRep_id(rep_pid);
				repDefineDto.setDetail_id(detail_id);
				repDefineDao.updateDetail(rep_pid, detail_id);
			} else {
				// 更新自己
				repDefineDao.update(repDefineDto);

				repSqlDao.delete(repDefineDto.getRep_id());
				repSqlDao.saveSql(repSql);
				repTotalDao.delete(repDefineDto.getRep_id());
				repTotalDao.saveRepTotal(repTotal);
				// 设置查询缓存无效化
				repQueryLogDao.clearCacheByRepUpdate(repDefineDto.getRep_id());
			}
			return detail_id;
		} catch (JDBCException e) {
			throw new ReportException(e);
		} catch (ReportException e) {
			throw e;
		} catch (Exception e) {
			throw new ReportException("System_Error:" + e.getMessage(), e, sql);
		}
	}

	/**
	 * 查询报表类型
	 */
	public List<SItemvalue> queryRepType() throws Exception {
		return SystemConfig.getRepType();
	}

	/**
	 * 查询报表属性定义
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SItemvalue> queryRepInfo() throws Exception {
		return SystemConfig.getRepInfo();
	}

	/**
	 * 查询数据源
	 */
	public List<RepDatabase> queryDatabase() throws Exception {
		List<RepDatabase> list = new ArrayList<RepDatabase>();
		for (RepDatabase rd : SystemConfig.getDatabaseList()) {
			RepDatabase temp = new RepDatabase();
			temp.setDatabase(rd.getDatabase());
			temp.setName(rd.getName());
			list.add(temp);
		}
		return list;
	}

	/**
	 * 我的报表
	 * 
	 * @param optr
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<SResource> queryMyRep(SOptr optr, Integer start, Integer limit)
			throws Exception {
		return queryRepDao.queryMyRep(optr.getOptr_id(), start, limit);
	}
	
	public Pager<RepQueryLog> queryDayOpen(SOptr optr, Integer start, Integer limit)throws Exception {
		return queryRepDao.queryDayOpen(optr.getOptr_id(), start, limit);
		
	}

	/**
	 * 营业报表
	 * 
	 * @param optr
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<SResource> queryBusinessRep(SOptr optr, Integer start,
			Integer limit) throws Exception {
		return queryRepDao.queryBusiness(optr.getOptr_id(), start, limit);
	}

	/**
	 * 财务报表
	 * 
	 * @param optr
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<SResource> queryFinanceRep(SOptr optr, Integer start,
			Integer limit) throws Exception {
		return queryRepDao.queryFinance(optr.getOptr_id(), start, limit);
	}

	/**
	 * 更新日志，只显示该用户具有权限的报表
	 * 
	 * @param optr
	 * @param start
	 * @param limit
	 * @return
	 */
	public Pager<RepDefineLog> queryLog(SOptr optr, String rep_id,
			Integer start, Integer limit) throws Exception {
		return repDefineLogDao
				.queryLog(optr.getOptr_id(), rep_id, start, limit);
	}

	/**
	 * 查询所有报表
	 * 
	 * @param optr
	 * @return
	 * @throws Exception
	 */
	public List<SResource> queryAllRep(SOptr optr) throws Exception {
		return queryRepDao.queryAllRep(optr.getOptr_id());
	}

	/**
	 * 保存一个我的报表
	 * 
	 * @param optr
	 * @param rep_id
	 * @throws Exception
	 */
	public void saveMyRep(SOptr optr, String rep_id) throws Exception {
		RepMyreport myrep = new RepMyreport();
		myrep.setOptr_id(optr.getOptr_id());
		myrep.setRep_id(rep_id);
		repMyreportDao.delete(optr.getOptr_id(), rep_id);
		repMyreportDao.save(myrep);
	}

	public void deleteMyRep(SOptr optr, String rep_id) throws Exception {
		repMyreportDao.delete(optr.getOptr_id(), rep_id);
	}

	public SResourceDao getSResourceDao() {
		return sResourceDao;
	}

	public void setSResourceDao(SResourceDao resourceDao) {
		sResourceDao = resourceDao;
	}

	public RepSqlDao getRepSqlDao() {
		return repSqlDao;
	}

	public void setRepSqlDao(RepSqlDao repSqlDao) {
		this.repSqlDao = repSqlDao;
	}

	public RepDefineDao getRepDefineDao() {
		return repDefineDao;
	}

	public void setRepDefineDao(RepDefineDao repDefineDao) {
		this.repDefineDao = repDefineDao;
	}

	public RepTotalDao getRepTotalDao() {
		return repTotalDao;
	}

	public void setRepTotalDao(RepTotalDao repTotalDao) {
		this.repTotalDao = repTotalDao;
	}

	public QuerySql getQuerySql() {
		return querySql;
	}

	public void setQuerySql(QuerySql querySql) {
		this.querySql = querySql;
	}

	public RepDefineLogDao getRepDefineLogDao() {
		return repDefineLogDao;
	}

	public void setRepDefineLogDao(RepDefineLogDao repDefineLogDao) {
		this.repDefineLogDao = repDefineLogDao;
	}

	public QueryRepDao getQueryRepDao() {
		return queryRepDao;
	}

	public void setQueryRepDao(QueryRepDao queryRepDao) {
		this.queryRepDao = queryRepDao;
	}

	public RepMyreportDao getRepMyreportDao() {
		return repMyreportDao;
	}

	public void setRepMyreportDao(RepMyreportDao repMyreportDao) {
		this.repMyreportDao = repMyreportDao;
	}

	public RepQueryLogDao getRepQueryLogDao() {
		return repQueryLogDao;
	}

	public void setRepQueryLogDao(RepQueryLogDao repQueryLogDao) {
		this.repQueryLogDao = repQueryLogDao;
	}

	public QueryKey getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(QueryKey queryKey) {
		this.queryKey = queryKey;
	}

	public RepGroupDao getRepGroupDao() {
		return repGroupDao;
	}

	public void setRepGroupDao(RepGroupDao repGroupDao) {
		this.repGroupDao = repGroupDao;
	}

	public RepOptrExportDao getRepOptrExportDao() {
		return repOptrExportDao;
	}

	public void setRepTOsdSqlDao(RepTOsdSqlDao repTOsdSqlDao) {
		this.repTOsdSqlDao = repTOsdSqlDao;
	}

	public RepCubeDao getRepCubeDao() {
		return repCubeDao;
	}

	public void setRepCubeDao(RepCubeDao repCubeDao) {
		this.repCubeDao = repCubeDao;
	}

	public void setRepMyCubeDao(RepMyCubeDao repMyCubeDao) {
		this.repMyCubeDao = repMyCubeDao;
	}

	public void setCubeManage(CubeManage cubeManage) {
		this.cubeManage = cubeManage;
	}

	public CubeDetail getCubeDetail() {
		return cubeDetail;
	}

	public void setCubeDetail(CubeDetail cubeDetail) {
		this.cubeDetail = cubeDetail;
	}

	public QueryManage getQueryManage() {
		return queryManage;
	}

	public RepTOsdSqlDao getRepTOsdSqlDao() {
		return repTOsdSqlDao;
	}

	public CubeManage getCubeManage() {
		return cubeManage;
	}

	public RepMyCubeDao getRepMyCubeDao() {
		return repMyCubeDao;
	}

	public void setRepDetailRowDao(RepDetailRowDao repDetailRowDao) {
		this.repDetailRowDao = repDetailRowDao;
	}

	public RepDetailDimDao getRepDetailDimDao() {
		return repDetailDimDao;
	}

	public void setRepDetailDimDao(RepDetailDimDao repDetailDimDao) {
		this.repDetailDimDao = repDetailDimDao;
	}

	public RepDetailRowDao getRepDetailRowDao() {
		return repDetailRowDao;
	}
	public SRoleDao getSRoleDao() {
		return sRoleDao;
	}
	public void setSRoleDao(SRoleDao roleDao) {
		sRoleDao = roleDao;
	}
	public SRoleResourceDao getSRoleResourceDao() {
		return sRoleResourceDao;
	}
	public void setSRoleResourceDao(SRoleResourceDao roleResourceDao) {
		sRoleResourceDao = roleResourceDao;
	}
}
