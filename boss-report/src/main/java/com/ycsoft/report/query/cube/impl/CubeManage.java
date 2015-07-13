package com.ycsoft.report.query.cube.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.report.bean.RepCube;
import com.ycsoft.report.bean.RepDefine;
import com.ycsoft.report.dao.config.RepCubeDao;
import com.ycsoft.report.query.cube.CubeExec;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.DimensionRolap;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.cube.Measure;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;
import com.ycsoft.report.query.menory.MemoryCacheInit;
/**
 * cube管理
 */
public class CubeManage implements MemoryCacheInit {
	/**
	 * key=rep_id,value=一个完整CUBE配置
	 */
	private static Map<String,List<RepCube>> cubeMap=new HashMap<String,List<RepCube>>();
	/**
	 * key=rep_id,value= 一个完整的CUBE配置Map<Sreing,RepCube>(key=column_define维或指标,value=cube的一个字段配置关系)
	 */
	private static Map<String,Map<String,RepCube>> cubeMapBycolumn=new HashMap<String,Map<String,RepCube>>();
	
	private RepCubeDao repCubeDao;
	
	
	public static List<RepCube> getRepCubes(String rep_id){
		return cubeMap.get(rep_id);
	}
	/**
	 * 使用完整CUBE配置构造一个cube
	 * @param repcubes
	 * @return
	 * @throws ReportException
	 */
	public CubeExec createCube(List<RepCube> repcubes) throws ReportException{
		Cube cube=new Cube(repcubes);
		return cube;
	}
	/**
	 * 使用缓存的CUBE配置构造一个cube
	 * @param rep_id
	 * @return
	 * @throws ReportException
	 */
	public CubeExec getCube(String rep_id) throws ReportException{
		List<RepCube> list=cubeMap.get(rep_id);
		if(list!=null)
			return createCube(list);
		else 
			return null;
	}
	
	/**
	 * 从一个cube中提取mycube
	 * @param cube
	 * @return
	 */
	public MyCube getMyCube(CubeExec cube){
		MyCube mycube=new MyCube();
		//指标
		for(Measure mea: cube.getMeasures()){
			if(mycube.getMealist()==null){
				mycube.setMealist(new ArrayList<String>());
			}
			mycube.getMealist().add(mea.getColumnCode());
		}
		//维度
		for(DimensionRolap rolap:cube.getDimensionRolaps()){
			if(rolap.isUsesign()){
				//维度清单
				if(mycube.getDimlist()==null){
					mycube.setDimlist(new ArrayList<String>());
				}
				//纵向维
				mycube.getDimlist().add(rolap.getId());
				if(rolap.isVerticalsign()){
					mycube.setVertdim(rolap.getId());
				}
				//维层级
				if(mycube.getDimlevelmap()==null){
					mycube.setDimlevelmap(new HashMap<String,Integer>());
				}
				mycube.getDimlevelmap().put(rolap.getId(), rolap.getLevel());
				//合计值
				if(mycube.getDimtotalmap()==null){
					mycube.setDimtotalmap(new HashMap<String,Integer[]>());
				}
				List<Integer> totals=new ArrayList<Integer>();
				for(int i=1;i<=rolap.getDim().getLevelNum();i++){
					if(rolap.isLevelTotal(i))
						totals.add(i);
				}
				mycube.getDimtotalmap().put(rolap.getId(), totals.toArray(new Integer[totals.size()]));
				//切片值
				if(mycube.getDimslicesmap()==null){
					mycube.setDimslicesmap(new HashMap<String,Map<Integer,String[]>>());
				}
				if(rolap.getSlices_level()!=null){
					Map<Integer,String[]> slicesmap=new HashMap<Integer,String[]>();
					slicesmap.put(rolap.getSlices_level(), rolap.getSlices_value());
					mycube.getDimslicesmap().put(rolap.getId(), slicesmap);
				}
				//自定义排序
				if(mycube.getDimsortmap()==null){
					mycube.setDimsortmap(new HashMap<String,Map<Integer,String[]>>() );
				}
				if(rolap.getLevel_sort_map()!=null&&rolap.getLevel_sort_map().size()>0){
					mycube.getDimsortmap().put(rolap.getId(), rolap.getLevel_sort_map());
				}
			}
		}
		return mycube;
	}
	/**
	 * 使用mycube设置cube
	 * 并判断mycube配置是否正确
	 * @param cube
	 * @param mycube
	 * @return
	 * @throws ReportException
	 */
	public void setCube(CubeExec cube,MyCube mycube) throws ReportException{
		//指标
		cube.selectMeasure(mycube.getMealist().toArray(new String[mycube.getMealist().size()]));
		//维度
		if(mycube.getDimlist()!=null){
			Dimension[] dims=new Dimension[mycube.getDimlist().size()];
			for(int i=0;i<mycube.getDimlist().size();i++){
				dims[i]=DimensionManage.getDimension(mycube.getDimlist().get(i));
				if(dims[i]==null)
					throw new ReportException(mycube.getDimlist().get(i)+":mycube.dimlist["+i+"] is undefined.");
			}
			cube.selectDimension(DimensionManage.getDimension(mycube.getVertdim()), dims);
		}
		//合计
		if(mycube.getDimtotalmap()!=null){
			for(String dim:mycube.getDimtotalmap().keySet())
			cube.configDimensionTotal(DimensionManage.getDimension(dim), mycube.getDimtotalmap().get(dim));
		}
		//切片
		if(mycube.getDimslicesmap()!=null){
			for(String dim:mycube.getDimslicesmap().keySet()){
				Map<Integer,String[]> map=mycube.getDimslicesmap().get(dim);
				for(Integer level:map.keySet()){
					cube.slicesDimension(DimensionManage.getDimension(dim), level, map.get(level));
				}
			}
		}
		//自定义排序
		if(mycube.getDimsortmap()!=null){
			for(String dim:mycube.getDimsortmap().keySet()){
				Map<Integer,String[]> map=mycube.getDimsortmap().get(dim);
				cube.sortDimension(DimensionManage.getDimension(dim), map);
			}
		}
		
		//层
		if(mycube.getDimlevelmap()!=null){
			for(String dim:mycube.getDimlevelmap().keySet()){
				Integer level=mycube.getDimlevelmap().get(dim);
				if(level==null||level<1||level>DimensionManage.getDimension(dim).getLevelNum())
					throw new ReportException("level is 0 or level <1 or level> levelNum");
				for(int i=cube.getDimensionRolap(DimensionManage.getDimension(dim)).getLevel();i>level;i--)
					cube.shrinkDimension(DimensionManage.getDimension(dim));
				for(int i=cube.getDimensionRolap(DimensionManage.getDimension(dim)).getLevel();i<level;i++)
					cube.expandDimension(DimensionManage.getDimension(dim));
			}
		}
	}
	/**
	 * 使用mycube创建cube
	 * @param rep_id
	 * @param mycube
	 * @return
	 * @throws ReportException
	 */
	public CubeExec createCube(String rep_id,MyCube mycube) throws ReportException{
		CubeExec cube= this.getCube(rep_id);
		this.setCube(cube, mycube);
		return cube;
	}
	
	/**
	 * 生成维度配置清单
	 * 格式：
	 * 维度: 地区_县区_区域(横向维;层=地区,县区;合计=地区,县区;切片=(县区:台山,
	 * 广海),维度：年_月(横向维;层=月;合计=月;切片=(月:1月,2月)), 度量：
	 * 客户数_SUM
	 * @return
	 * @throws ReportException 
	 */
	public String getMyCubeConfig(String rep_id,MyCube mycube) throws ReportException{
		StringBuilder config=new StringBuilder();
		
		if(mycube.getDimlist()==null||mycube.getDimlist().size()==0)
			return config.toString();
		config.append("维:");
		for(String id:mycube.getDimlist()){
			//维
			Dimension dim=DimensionManage.getDimension(id);
			config.append(dim.getName()).append("(")
				.append(dim.getId().equals(mycube.getVertdim())?"纵":"横");
			//层
			if(dim.getLevelNum()>1){
				config.append(":");
				config.append(dim.getLevel(mycube.getDimlevelmap().get(id)).getName());
			}
			config.append("),");
		}//指标
		if(mycube.getMealist()!=null&&mycube.getMealist().size()>0){
			config.append("指标(");
			for(String mea:mycube.getMealist()){
				RepCube repcube= cubeMapBycolumn.get(rep_id).get(mea);
				if(repcube==null)
					throw new ReportException("该CUBE中指标("+mea+")不存在");
				config.append(repcube.getColumn_as()).append("_").append(repcube.getColumn_define()).append(",");
			}
			config.append(")");
		}
		//合计项目
		if(mycube.getDimtotalmap()!=null&&mycube.getDimtotalmap().size()>0){
			config.append("\n合计:");
			for(String id:mycube.getDimlist()){
				if(mycube.getDimtotalmap().get(id)!=null&&mycube.getDimtotalmap().get(id).length>0){
					Dimension dim=DimensionManage.getDimension(id);
					Integer[] levels=mycube.getDimtotalmap().get(id);
					for(int i=0;i<levels.length;i++){
						if(i>0)
							config.append(",");
						config.append(dim.getLevel(levels[i]).getName());
					}
				}
			}
		}
		//切片过滤	
		if(mycube.getDimslicesmap()!=null&&mycube.getDimslicesmap().size()>0){
			config.append("\n过滤:");
			for(String id:mycube.getDimlist()){
				if(mycube.getDimslicesmap().get(id)!=null&&mycube.getDimslicesmap().get(id).size()>0){
					Dimension dim=DimensionManage.getDimension(id);
					Map<Integer,String[]> map=mycube.getDimslicesmap().get(id);
					
					for(Integer level:map.keySet()){
						if(map.get(level)!=null&&map.get(level).length>0){
							config.append(dim.getLevel(level).getName()).append("=(");
							Map<String,String> valuemap=new HashMap<String,String>();
							for(String o:map.get(level))
								valuemap.put(o, null);
							for(QueryKeyValue vo: dim.getLevelValues(level)){
								if(valuemap.containsKey(vo.getId()))
									config.append(vo.getName()).append(",");
							}
							config.append("),");
						}
					}
				}
			}
		}
		//自定义排序
		if(mycube.getDimsortmap()!=null&&mycube.getDimsortmap().size()>0){
			config.append("\n排序:");
			for(String dimid:mycube.getDimsortmap().keySet()){
				Dimension dim=DimensionManage.getDimension(dimid);
				Map<Integer,String[]> map= mycube.getDimsortmap().get(dimid);
				for(Integer level:map.keySet()){
					config.append(dim.getLevel(level).getName()).append("(");
					Map<String,String> valuemap=new HashMap<String,String>();
					for(String o:map.get(level)){
						valuemap.put(o, null);
					}
					for(QueryKeyValue vo: dim.getLevelValues(level)){
						if(valuemap.containsKey(vo.getId()))
							config.append(vo.getName()).append(",");
					}
					config.append("),");
				}
			}
		}
		return config.toString();
	}
	
	/**
	 * 初始化cube配置缓存
	 * @throws ReportException 
	 */
	public String initAll() throws ReportException {
		try {
			List<RepCube> list=repCubeDao.findAll();
			for(RepCube o:list){
				if(DimensionType.measure.name().equals(o.getColumn_type()))
					o.setIsmea(true);
				else
					o.setIsmea(false);
			}
			cubeMap=CollectionHelper.converToMap(list, "rep_id");
			
			Map<String,Map<String,RepCube>> cubecodemap=new HashMap<String,Map<String,RepCube>>();
			
			for(Object rep_id:cubeMap.keySet().toArray()){
				cubecodemap.put(rep_id.toString(), CollectionHelper.converToMapSingle(cubeMap.get(rep_id.toString()),"column_code"));
			}
			cubeMapBycolumn=cubecodemap;
			
			
			StringBuilder buffer=new StringBuilder();
			
			for(RepDefine rep: repCubeDao.checkCubeDimDatabase()){
				buffer.append("报表_").append(rep.getRep_name()).append("(").append(rep.getRep_id()).append(")")
				.append("数据源与配置的维度_").append(rep.getRemark()).append("(").append(rep.getRep_info()).append(")数据源不一致;");
			}
			return buffer.toString();
		} catch (Exception e) {
			throw new ReportException("cube内存初始化错误",e);
		}
	}

	public void setRepCubeDao(RepCubeDao repCubeDao) {
		this.repCubeDao = repCubeDao;
	}
}
