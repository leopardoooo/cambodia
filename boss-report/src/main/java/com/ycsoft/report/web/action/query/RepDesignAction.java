package com.ycsoft.report.web.action.query;


import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.google.gson.reflect.TypeToken;
import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.report.bean.RepDetailRow;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.component.query.FileComponent;
import com.ycsoft.report.component.query.KeyComponent;
import com.ycsoft.report.component.query.RepDesignComponent;
import com.ycsoft.report.dto.RepDefineDto;
import com.ycsoft.report.query.cube.showclass.cellwarn.MeaWarn;
import com.ycsoft.report.query.datarole.BaseDataControl;
import com.ycsoft.report.query.datarole.FuncType;

/**
 * 报表配置管理
 */
@Controller
public class RepDesignAction extends ReportAction {
	private RepDesignComponent repDesignComponent;
	private FileComponent fileComponent;
	private KeyComponent keyComponent;
	
	private RepDefineDto repDefineDto;
	
	private boolean clear ;

	private String[] roleIds;
	
	private File[] uploadQuieeRaqs;//上传文件
	
	private String uploadQuieeNames;
	
	private String quiee_raq;
	
	private String[] cols;
	
	private String title;
	
	private String mycube_name;
	private String mycube_remark;
	private Boolean mycube_show;
	
	private String mea_detail_id;
	
	private String gridrows;
	
	private String template_id;
	
	private String warnlist;
	
	/**
	 * 处理报表缓存
	 * @return
	 * @throws Exception
	 */
	public String saveRepClearCache()throws Exception{
		if (BaseDataControl.getRole().hasFunc(FuncType.EDITREP)){
			repDesignComponent.saveRepClearCache(rep_id);
		}else{
			throw new ReportException("权限不足!");
		}
		return JSON_SUCCESS;
	}
	/**
	 * 保存报表备注
	 * @return
	 */
	public String saveReportRemark() throws Exception{
		if (BaseDataControl.getRole().hasFunc(FuncType.EDITREMARK)){
			repDesignComponent.saveReportRemark(rep_id, mycube_remark);
		}else{
			throw new ReportException("权限不足!");
		}
		return JSON_SUCCESS;
	}
	/**
	 * 查询对应模板配置的值警戒配置
	 * 参数模板ID template_id
	 * 返回 List<MeaWarn> 
	 * @return
	 */
	public String queryCubeWarn() throws Exception{
		getRoot().setRecords(repDesignComponent.queryCubeWarn(template_id));
		return JSON_RECORDS;
	}
	/**
	 * 保存模板的值警戒配置
	 * @return
	 * @throws Exception
	 */
	public String saveCubeWarn() throws Exception{
		Type type = new TypeToken<List<MeaWarn>>(){}.getType();
		List<MeaWarn> list=JsonHelper.toList(warnlist, type);
		repDesignComponent.saveCubeWarn(template_id, list);
		return JSON_SUCCESS;
	}
	/**
	 * 警戒的类型配置
	 * @return
	 */
	public String queryWarnRowTypes(){
		getRoot().setRecords(repDesignComponent.queryWarnRowTypes());
		return JSON_RECORDS;
	}
	/**
	 * 警戒的逻辑符号
	 * @return
	 */
	public String queryWarnOperators(){
		getRoot().setRecords(repDesignComponent.queryWarnOperators());
		return JSON_RECORDS;
	}
	/**
	 * 警戒的多项目运算符号
	 * @return
	 */
	public String queryWarnValueTypes(){
		getRoot().setRecords(repDesignComponent.queryWarnValueTypes());
		return JSON_RECORDS;
	}
	/**
	 * 保存列定义明细
	 * 参数 rep_id,gridrows=List<GridRow>
	 * 返回无
	 * @return
	 */
	public String saveCustomRows() throws Exception{
		List<RepDetailRow> list=JsonHelper.toList(gridrows, new TypeToken<List<RepDetailRow>>(){}.getType());
		repDesignComponent.saveCustomRows(rep_id, list);
		return JSON_SUCCESS;
	}
	/**
	 * 查询可编辑明细的列定义明细
	 * 参数rep_id 返回 List<RepDetailRow>
	 * @return
	 */
	public String queryCustomRows() throws Exception{
		getRoot().setRecords(repDesignComponent.queryCustomRows(rep_id));
		return JSON_RECORDS;
	}
	/**
	 * 查询编辑明细的列定义的 类型可选范围
	 * @return
	 */
	public String queryCustomRowTypes(){
		
		getRoot().setRecords(repDesignComponent.queryCustomRowTypes());
		return JSON_RECORDS;
	}
	/**
	 * 指标警戒值，比较符号列表
	 * @return
	 * @throws Exception 
	 */
	public String queryMeasureWarnTypes() throws Exception{
		throw new Exception("null");
		//return JSON_RECORDS;
	}
	/**
	 * 查询cube的维度配置清单
	 * 参数rep_id
	 * 返回List<Dimension>
	 * @return
	 */
	public String queryCubeDimensions() throws Exception{
		getRoot().setRecords(repDesignComponent.queryDimensionsByRepId(rep_id));
		return JSON_RECORDS;
	}
	/**
	 * 查询度量数据显示格式
	 * @return
	 */
	public String queryMeasuresDataType(){
		getRoot().setRecords(repDesignComponent.queryMeaDataType());
		return JSON_RECORDS;
	}
	/**
	 * 测试cube明细报表的SQL是否正确
	 * 返回 合计项目 的值
	 * @return
	 */
	public String testCubeDetail() throws Exception{
		getRoot().setSimpleObj(repDesignComponent.testCubeDetailSql(getParameter().getRepdefinedto(), rep_id));
		return JSON;
	}
	/**
	 * 保存cube明细报表
	 * 参数rep_id=cube_rep_id
	 * 返回rep_id
	 * @return
	 */
	public String saveCubeDetail()throws Exception{
		getRoot().setSimpleObj(repDesignComponent.saveCubeDetail(getParameter().getRepdefinedto(), rep_id));
		return JSON_SIMPLEOBJ;
	}
	/**
	 * 查询cube明细报表配置
	 * 参数 rep_id,mea_detail_id
	 * 
	 * @return
	 */
	public String queryCubeDetail() throws Exception{
		getRoot().setSimpleObj(repDesignComponent.queryCubeDetail(rep_id, mea_detail_id));
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 查询cube的设置清单字符串
	 * @return
	 * @throws Exception
	 */
	public String queryMyCubeConfig()throws Exception{
		getRoot().setSimpleObj(repDesignComponent.queryMyCubeConfig(query_id));
		return JSON_SIMPLEOBJ;
	}
	/**
	 * 查询一个操作员能使用的模板清单】
	 * 参数optr_id
	 * @return
	 * @throws Exception
	 */
	public String queryMyCubes()throws Exception{
		getRoot().setRecords(repDesignComponent.queryMyCubes(getOptr().getOptr_id(),rep_id));
		return JSON_RECORDS;
	}
	/**
	 * 参数mycube
	 * 保存为我的cube模板
	 */
	public String saveMyCube()throws Exception{
		repDesignComponent.saveMycube(query_id,getOptr().getOptr_id(), rep_id,mycube_name,mycube_remark,mycube_show);
		return JSON_SUCCESS;
	}
	/**
	 * 删除模板
	 * @return
	 * @throws Exception
	 */
	public String saveDeleteMyCube()throws Exception{
		repDesignComponent.deleteMyCube(getOptr().getOptr_id(), rep_id, mycube_name);
		return JSON_SUCCESS;
	}
	/**
	 * 更新我的模板的备注说明
	 * @return
	 * @throws Exception
	 */
	public String saveUpdateMyCubeRemark()throws Exception{
		repDesignComponent.updateMyCubeRemark(getOptr().getOptr_id(), rep_id, mycube_name, mycube_remark);
		return JSON_SUCCESS;
	}
	/**
	 * 设置我的模板首选显示
	 * @return
	 * @throws Exception
	 */
	public String saveMyCubeDefault()throws Exception{
		repDesignComponent.updateMyCubeDefault(getOptr().getOptr_id(), rep_id, mycube_name);
		return JSON_SUCCESS;
	}
	/**
	 * 查询报表配置
	 * @return
	 * @throws Exception
	 */
	public String queryCuleDefine()throws Exception{
		getRoot().setRecords(repDesignComponent.queryCuleDefine(rep_id));
		return JSON_RECORDS;
	}
	/**
	 * 查询维类型
	 */
	public String queryCubeDimensionTypes()throws Exception{
		getRoot().setRecords(repDesignComponent.queryCubeDimensionTypes());
		return JSON_RECORDS;
	}
	/**
	 * 查询维列表
	 * @return
	 * @throws Exception
	 */
	public String queryDimensions()throws Exception{
		getRoot().setRecords(repDesignComponent.queryDimensions(rep_id));
		return JSON_RECORDS;
	}
	/**
	 * 查询度量定义列值
	 * @return
	 * @throws Exception
	 */
	public String queryMeasures()throws Exception{
		getRoot().setRecords(repDesignComponent.queryMeasureTypes());
		return JSON_RECORDS;
	}
	/**
	 * 验证cube定义是否正确
	 * @return
	 * @throws Exception
	 */
	public String validateCube()throws Exception{
		getRoot().setSimpleObj(repDesignComponent.validateCube(rep_id, getParameter().getRepcube()));
		return JSON_SIMPLEOBJ;
	}
	/**
	 * 保存cube定义
	 * @return
	 * @throws Exception
	 */
	public String saveCuleDefine()throws Exception{
		repDesignComponent.saveCuleDefine(rep_id, getOptr().getOptr_id(), getParameter().getRepcube());
		return JSON_SUCCESS;
	}
	/**
	 * 显示组装后sql
	 * @return
	 * @throws Exception
	 */
	public String showSql()throws Exception{
		if(BaseDataControl.getRole().hasFunc(FuncType.SHOWSQL)){
			getRoot().setSimpleObj(repDesignComponent.showSql(query_id,this.getOptr()));
		}else{
			throw new ReportException("权限不足!");
		}
		return JSON_SIMPLEOBJ;
	}
	/**
	 * 创建OSD催缴模板
	 * @return
	 * @throws Exception
	 */
	public String createOsdsql()throws Exception{
		repDesignComponent.createOsdSql(query_id, title,getOptr().getOptr_id());
		return JSON_SUCCESS;
	}
	public String queryOsdsqltitle()throws Exception{
		getRoot().setSimpleObj(repDesignComponent.queryOsdSqlTitle(query_id));
		return JSON_SIMPLEOBJ;
	}
	/**
	 * 获取报表列导出配置
	 * @return
	 */
	public String getColExport() throws Exception{
		getRoot().setRecords(repDesignComponent.findColExcport(query_id, getOptr().getOptr_id()));
		return JSON_RECORDS;
	}
	
	/**
	 * 保存报表列个人配置
	 */
	public String saveColExeport() throws Exception{
		repDesignComponent.saveColExport(query_id, getOptr().getOptr_id(), cols);
		return JSON_SUCCESS;
	}
	/**
	 * 菜单分配角色关系
	 */
	public String getResourceRepRole() throws Exception{
		getRoot().setRecords(repDesignComponent.getRepRole(rep_id));
		return JSON_RECORDS;
	}
	public String[] getCols() {
		return cols;
	}

	public void setCols(String[] cols) {
		this.cols = cols;
	}

	/**
	 * 菜单分配角色保存
	 */
	public String saveResource2Role() throws Exception{
		if (StringHelper.isNotEmpty(rep_id)){
			getRoot().setSuccess(repDesignComponent.removeRoleResource(rep_id));//如果原来分配的有角色，先删除角色，在保存修改的
			//标记是否全部未选中
			if(true != clear ){
				getRoot().setSuccess(repDesignComponent.saveRoleResource(roleIds,rep_id));
			}
		}else{
			getRoot().setSuccess(false);
		}
		return JSON;
	}
	
	/**
	 * 查询所有可用查询条件列表
	 * @return
	 */
	public String queryAllKeyCon(){
		getRoot().setRecords(keyComponent.getAllKeyCon());
		return JSON_RECORDS;
	}

	/**
	 * 查询所有内存列转换关键字列表
	 * @return
	 */
	public String queryAllMemoryKeys(){
		getRoot().setRecords(keyComponent.getAllMemoryKeys());
		return JSON_RECORDS;
	}
	
	/**
	 * 查询报表扩展信息
	 * @return
	 * @throws Exception
	 */
	public String queryRepDefine() throws Exception{
		// 判断权限,只有数据权限是广电级和具有角色资源的 操作员能查询
		if (!BaseDataControl.getRole().hasFunc(FuncType.EDITREP))
			throw new ReportException("您无权增加或修改报表.");
		getRoot().setSimpleObj(repDesignComponent.findRepDefine(rep_id,optr));
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 查询报表扩展信息
	 * @return
	 * @throws Exception
	 */
	public String queryRepRemark() throws Exception{
		getRoot().setSimpleObj(repDesignComponent.findRepDefine(rep_id,optr).getRemark());
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 查询报表扩展信息
	 * @return
	 * @throws Exception
	 */
	public String queryRepDetail() throws Exception{
		getRoot().setSimpleObj(repDesignComponent.findRepDefineDetail(rep_id));
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 上传快逸模板,多文件上传
	 * @return
	 * @throws Exception 
	 */
	public String uploadQuieeRaq() throws Exception{
//		getRoot().setSimpleObj(fileComponent.saveQuieeRaq(rep_id, uploadQuieeRaqs));
//		return JSON_SIMPLEOBJ;
		String str="";
		try{
		 str = fileComponent.saveQuieeRaq(uploadQuieeNames, uploadQuieeRaqs);
		}catch(Exception e){
			str=e.getMessage();
		}
		return retrunNone(str);
	}
	/**
	 * 下载快逸模板
	 * @return
	 * @throws Exception 
	 * @throws Exception 
	 * @throws  
	 */
	public String downloadQuieeRaq() throws Exception{
		OutputStream out=null;
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType( "application/zip");
			response.setHeader("Content-disposition", "attachment; filename="+quiee_raq+".zip");
			
			out = response.getOutputStream();
			fileComponent.downloadQuieeRaq(quiee_raq,out);
			out.flush();
			return NONE;
		} catch (Exception e) {
			throw e;
		}finally{		
			try {
				out.close();
			} catch (Exception e) {}
		}
	}
	
	/**
	 * 增加一个我的报表收藏
	 */
	public String saveMyRep() throws Exception {
		repDesignComponent.saveMyRep(optr, rep_id);
		getRoot().setSuccess(true);
		return JSON_SUCCESS;
	}

	/**
	 * 删除一个我的报表收藏
	 */
	public String deleteMyRep() throws Exception {
		repDesignComponent.deleteMyRep(optr, rep_id);
		getRoot().setSuccess(true);
		return JSON_SUCCESS;
	}

	/**
	 * 查询所有报表列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryAllRep() throws Exception {
		getRoot().setRecords(repDesignComponent.queryAllRep(optr));
		return JSON_RECORDS;
	}

	/**
	 * 查询报表更新日志
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryLog() throws Exception {
		getRoot().setPage(repDesignComponent.queryLog(optr,rep_id, start, limit));
		return JSON_PAGE;
	}

	/**
	 * 查询我的报表列表
	 */
	public String queryMyReport() throws Exception {
		getRoot().setPage(repDesignComponent.queryMyRep(optr, start, limit));
		return JSON_PAGE;
	}
	/**
	 * 当天打开的报表
	 * @return
	 * @throws Exception
	 */
	public String queryDayOpen()throws Exception {
		getRoot().setPage(repDesignComponent.queryDayOpen(optr, start, limit));
		return JSON_PAGE;
	}

	/**
	 * 查询营业日报表列表
	 */
	public String queryBusinessRep() throws Exception {
		getRoot().setPage(
				repDesignComponent.queryBusinessRep(optr, start, limit));
		return JSON_PAGE;
	}

	/**
	 * 查询财务报表列表
	 */
	public String queryFinanceRep() throws Exception {
		getRoot().setPage(
				repDesignComponent.queryFinanceRep(optr, start, limit));
		return JSON_PAGE;
	}

	/**
	 * 测试SQL，成功则返回合计项字符串（','分隔）
	 * 
	 * @return
	 * @throws Exception
	 */
	public String testSql() throws Exception {
		
			getRoot().setOthers(repDesignComponent.testSql( repDefineDto));
			
		return JSON_OTHER;
	}

	/**
	 * 保存创建报表信息
	 */
	public String saveRepDesign() throws Exception {
			getRoot().setSimpleObj(repDesignComponent.saveRepDesign(repDefineDto,optr));
		return JSON;
	}
	/**
	 * 保存统计报表对应的明细报表
	 * @return
	 * @throws Exception
	 */
	public String saveRepDetail()throws Exception{
		getRoot().setSimpleObj(repDesignComponent.saveDetailsReport(repDefineDto,optr));
		return JSON_SIMPLEOBJ;
	}
	/**
	 * 查询报表类型
	 */
	public String queryRepType() throws Exception {
		getRoot().setRecords(repDesignComponent.queryRepType());
		return JSON_RECORDS;
	}

	/**
	 * 查询数据源
	 */
	public String queryDatabase() throws Exception {
		getRoot().setRecords(repDesignComponent.queryDatabase());
		return JSON_RECORDS;
	}

	/**
	 * 查询报表属性定义
	 */
	public String queryRepInfo() throws Exception {
		getRoot().setRecords(repDesignComponent.queryRepInfo());
		return JSON_RECORDS;
	}

	public RepDesignComponent getRepDesignComponent() {
		return repDesignComponent;
	}

	public void setRepDesignComponent(RepDesignComponent repDesignComponent) {
		this.repDesignComponent = repDesignComponent;
	}

	public RepDefineDto getRepDefineDto() {
		return repDefineDto;
	}

	public void setRepDefineDto(RepDefineDto repDefineDto) {
		this.repDefineDto = repDefineDto;
	}
	public File[] getUploadQuieeRaqs() {
		return uploadQuieeRaqs;
	}
	public void setUploadQuieeRaqs(File[] uploadQuieeRaqs) {
		this.uploadQuieeRaqs = uploadQuieeRaqs;
	}
	public String getQuiee_raq() {
		return quiee_raq;
	}
	public void setQuiee_raq(String quiee_raq) {
		this.quiee_raq = quiee_raq;
	}
	public FileComponent getFileComponent() {
		return fileComponent;
	}
	public void setFileComponent(FileComponent fileComponent) {
		this.fileComponent = fileComponent;
	}
	public void setUploadQuieeNames(String uploadQuieeNames) {
		this.uploadQuieeNames = uploadQuieeNames;
	}

	public KeyComponent getKeyComponent() {
		return keyComponent;
	}

	public void setKeyComponent(KeyComponent keyComponent) {
		this.keyComponent = keyComponent;
	}

	public String getUploadQuieeNames() {
		return uploadQuieeNames;
	}
	public boolean isClear() {
		return clear;
	}
	public void setClear(boolean clear) {
		this.clear = clear;
	}
	public String[] getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMycube_name() {
		return mycube_name;
	}
	public void setMycube_name(String mycube_name) {
		this.mycube_name = mycube_name;
	}
	public String getMycube_remark() {
		return mycube_remark;
	}
	public void setMycube_remark(String mycube_remark) {
		this.mycube_remark = mycube_remark;
	}
	public Boolean getMycube_show() {
		return mycube_show;
	}
	public void setMycube_show(Boolean mycube_show) {
		this.mycube_show = mycube_show;
	}
	public String getMea_detail_id() {
		return mea_detail_id;
	}
	public void setMea_detail_id(String mea_detail_id) {
		this.mea_detail_id = mea_detail_id;
	}
	public String getGridrows() {
		return gridrows;
	}
	public void setGridrows(String gridrows) {
		this.gridrows = gridrows;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getWarnlist() {
		return warnlist;
	}
	public void setWarnlist(String warnlist) {
		this.warnlist = warnlist;
	}
}
