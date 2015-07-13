package com.ycsoft.report.web.action.query;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.report.component.query.KeyComponent;
import com.ycsoft.report.dto.RepKeyDto;
import com.ycsoft.report.query.cube.DimensionRolap;
import com.ycsoft.report.query.datarole.BaseDataControl;
import com.ycsoft.report.query.datarole.FuncType;
import com.ycsoft.report.query.key.Impl.ConKeyCheck;

/**
 * 查询组件管理
 * 
 * @author new
 * 
 */
public class KeyAction extends ReportAction {

	private KeyComponent keyComponent;

	/**
	 * 页面组件ajax取值初始化 查询页面组件选中的#countyid#的值;(若页面取不到值或取到多个值，则设置该值为空)
	 */
	private String countyid;
	/**
	 * 页面组件ajax取值初始化 查询界面选中的组件
	 */
	private String key;
	/**
	 * 页面组件ajax取值初始化 若页面取不到值或取到多个值，则设置该值为空
	 */
	private String key_value;

	private File uploadqueryfile;// 组件上传文件
	/**
	 * 查询警戒配置中的项目选择
	 * 参数 key=类型,key_value=template_id;
	 * 返回 List<WarnDimLevel> id=dim+'_'+level
	 * @return
	 * @throws Exception
	 */
	public String queryWarnDimLevels()throws Exception{
		
		getRoot().setRecords(keyComponent.queryWarnDimLevels(key, key_value));
		return JSON_RECORDS;
	}
	/**
	 * 图形类型
	 * @return
	 * @throws Exception
	 */
	public String queryGraphTypes()throws Exception{
		
		getRoot().setRecords(keyComponent.queryGraphTypes());
		return JSON_RECORDS;
	}
	/**
	 * 查询指标清单
	 * @return
	 * @throws Exception
	 */
	public String queryMeas()throws Exception{
		getRoot().setRecords(keyComponent.queryMeas(query_id));
		return JSON_RECORDS;
	}
	/**
	 * 查询cube的维度清单
	 * @return
	 * @throws Exception
	 */
	public String queryDims()throws Exception{
		getRoot().setRecords(keyComponent.queryDims(query_id));
		return JSON_RECORDS;
	}
	
	public String queryMeasAndDims()throws Exception{
	   List<DimensionRolap> list1 = keyComponent.queryDims(query_id);
	   List<ConKeyCheck> list2 = keyComponent.queryMeas(query_id);
	   
	  Map<String, Object> map = new HashMap<String,Object>();
	  map.put("dims", list1);
	  map.put("meas", list2);
	  getRoot().setOthers(map);
	  return JSON_OTHER;
	}
	/**
	 * 查询cube中维度合计配置清单
	 * @return
	 * @throws Exception
	 */
	public String queryDimTotals()throws Exception{
		getRoot().setRecords(keyComponent.queryDimTotal(query_id));
		return JSON;
	}
	/**
	 * 初始化维度过滤器
	 * @return
	 * @throws Exception
	 */
	public String queryDimSlices()throws Exception{
		getRoot().setOthers(keyComponent.queryCubeSlicesValue(query_id, key));
		getRoot().setRecords(keyComponent.queryLevels(key));
		getRoot().setSimpleObj(keyComponent.queryDimName(key));
		return JSON;
	}
	
	
	/**
	 * 初始化维排序器
	 * @return
	 * @throws Exception
	 */
	public String queryDimSort()throws Exception{
		getRoot().setOthers(keyComponent.queryCubeSortValue(query_id, key));
		getRoot().setRecords(keyComponent.queryLevels(key));
		getRoot().setSimpleObj(keyComponent.queryDimName(key));
		return JSON;
	}
	/**
	 * 获取一个维度的所有层
	 * @return
	 * @throws Exception
	 */
	public String queryLevels()throws Exception{
		getRoot().setRecords(keyComponent.queryLevels(key));
		return JSON_RECORDS;
	}
	/**
	 * 获取一个维度的层级取值
	 * @return
	 * @throws Exception
	 */
	public String queryLevelValues()throws Exception{
		getRoot().setRecords(keyComponent.queryLevelValues(key, Integer.parseInt(key_value)));
		return JSON_RECORDS;
	}
	public String uploadQueryFile() throws Exception {
		String str = "";

		PrintWriter out = null;
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			str = keyComponent.saveuploadfilekey(key, uploadqueryfile);
			out.print("{success:true,msg:'" + str + "'}");
			return NONE;
		} catch (Exception e) {
			str = e.getMessage();
			LoggerHelper.error(this.getClass(),e.getMessage() , e);
			out.print("{success:false,msg:'" + str + "'}");
			return NONE;
		} finally {
			if(out!=null){
				try{
					out.flush();
				}catch(Exception e){}
				try{
					out.close();
				}catch(Exception e){}
			}
		}
	}

	/**
	 * 单个页面组件ajax取值初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String initKeyValue() throws Exception {
		getRoot().setRecords(keyComponent.getKeyValue(countyid, key, key_value));
		return JSON_RECORDS;
	}

	/**
	 * 页面组件加载所有数据 需要初始化paramter:key,key_value,countyid,repkeys四个参数
	 * 
	 * @return
	 * @throws Exception
	 */
	public String loadAllKeyValue() throws Exception {

		getRoot().setOthers(keyComponent.getAllKeyValue(rep_id, countyid, key, key_value));
		return JSON;
	}

	/**
	 * 初始查询查询组件界面 返回组件列表，和组件的初始化值
	 * 
	 * @return
	 * @throws Exception
	 */
	public String initKeys() throws Exception {
		List<RepKeyDto> repkeys = keyComponent.getRepKeySameLineList(rep_id);
		getRoot().setRecords(repkeys);
		// getParameter().setRepkeys(repkeys);
		// getRoot().setOthers(keyComponent.getAllKeyValue(getParameter(),
		// getRep_role(),getSystem_optr_map()));
		getRoot().setSimpleObj(keyComponent.queryRepDefine(rep_id));
		//装入权限
		Map<String,Boolean> rolemap=new HashMap<String,Boolean>();
		rolemap.put(FuncType.EDITREP.name(), BaseDataControl.getRole().hasFunc(FuncType.EDITREP));
		getRoot().setOthers(rolemap);
		return JSON;
	}

	public KeyComponent getKeyComponent() {
		return keyComponent;
	}

	public void setKeyComponent(KeyComponent keyComponent) {
		this.keyComponent = keyComponent;
	}

	public String getCountyid() {
		return countyid;
	}

	public void setCountyid(String countyid) {
		this.countyid = countyid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey_value() {
		return key_value;
	}

	public void setKey_value(String key_value) {
		this.key_value = key_value;
	}

	public File getUploadqueryfile() {
		return uploadqueryfile;
	}

	public void setUploadqueryfile(File uploadqueryfile) {
		this.uploadqueryfile = uploadqueryfile;
	}
}
