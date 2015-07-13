package com.ycsoft.daos.core.generator;

import static com.ycsoft.daos.helper.StringHelper.format;
import static com.ycsoft.daos.helper.StringHelper.formatIgnoreType;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.config.Table;
import com.ycsoft.daos.helper.DateHelper;
import com.ycsoft.daos.helper.ListHelper;



/**
 * <p>生成Oracle数据库执行的相关sql类</p>
 */
@SuppressWarnings("unchecked")
public class OracleGenerator implements SQLGenerator{

	/**
	 * <p> 生成产生所有数据的SQL </P>
	 * @param tname 表名
	 */
	public String getEntityAll(String tname) {
		return formatIgnoreType("SELECT t.* FROM {0} t ",tname);
	}
	/**
	 * <p> 通过主键获得逐渐对象 </p>
	 * @return
	 */
	public String getEntityByKey(Table tb) {
		return formatIgnoreType("SELECT t.* FROM {0} t WHERE t.{1} = ?",tb.getTableName(),tb.getPrimaryKey());
	}

	/**
	 * <p> 分页命令 </p>
	 */
	public String getPage(String sql, int start, int limit) {
		StringBuffer sb = new StringBuffer(100);
		sb.append("SELECT * FROM (");
		sb.append("SELECT row_.*, ROWNUM rownum_");
		sb.append(" FROM ( {0} ) row_ ");
		sb.append(" WHERE rownum <= {1})");
		sb.append("WHERE rownum_ > {2} ");
		return formatIgnoreType(sb.toString(), sql,start+limit,start);
	}

	/**
	 * <p> oracle 采用count函数获取sql结果集的行数 </p>
	 */
	public String getPageCount(String sql) {
		return formatIgnoreType("SELECT count(*) FROM ( {0} )" , sql );
	}


	/**
	 * <p> 获得总记录数的sql </p>
	 * @param tname 表名
	 */
	public String getRows(String tname ){
		return formatIgnoreType("SELECT COUNT(*) FROM {0} t",tname);
	}

	/**
	 * <p> 获得oracle下的序列nextVal值 </p>
	 */
	public String getSeqNextVal(String sequenceName) {
		return formatIgnoreType("SELECT {0}.NEXTVAL FROM dual ",sequenceName);
	}

	/**
	 * <p> 获得删除的Sql语句 </p>
	 */
	public String getDelete(Table tb) {
		return formatIgnoreType("DELETE FROM {0} t WHERE t.{1} = ?",tb.getTableName(),tb.getPrimaryKey());
	}

	/**
	 * <p> 获得保存的SQL语句 </p>
	 */
	public String getSave(String tableName,Map<String , Object > maps){
		String sql = "INSERT INTO {0}({1}) values({2})" ;
		StringBuffer columns = new StringBuffer();
		StringBuffer values = new StringBuffer();
		Iterator<String> ite =  maps.keySet().iterator();
		while(ite.hasNext()){
			String key = ite.next() ;
			columns.append(key + ",");
			if (maps.get(key) instanceof Date) {
				values.append("to_date("+format("{0}",DateHelper.format((Date)maps.get(key))) + ",'yyyy-mm-dd hh24:mi:ss'),");
			}
			else
				values.append(format("{0}",maps.get(key)) + ",");
		}
		if(values.length()>0){
			values.deleteCharAt(values.length() - 1);
			columns.deleteCharAt(columns.length() - 1);
		}
		return formatIgnoreType(sql,tableName,columns,values);
	}

	/**
	 * <p> 获取更新的SQL </p>
	 */
	public String getUpdate(Table tb , List<String> columns) {
		String sql = "UPDATE {0} t SET {1} WHERE t.{2} = ?";
		String cols = ListHelper.parseListToStr(""," = ? ", columns );
		return formatIgnoreType(sql,tb.getTableName(),cols,tb.getPrimaryKey());
	}

	/**
	 * 获取更新SQL命令，包含参数的值
	 */
	public String getUpdate(Table tb, Map<String, Object> maps, Object key) {
		String sql = "UPDATE {0} t SET {1} WHERE t.{2} = " + format("{0}",key);
		StringBuffer where = new StringBuffer();
		Iterator ite = maps.keySet().iterator();
		Object [] objs = new Object[maps.keySet().toArray().length];
		int i = 0 ;
		while(ite.hasNext()){
			Object str = ite.next();
			where.append( str +" = {"+ i +"} ,");
			if (maps.get(str) instanceof Date)
				objs[i] = "to_date('" + DateHelper.format((Date)maps.get(str)) + "','yyyy-mm-dd hh24:mi:ss')";
			else
				objs[i] = format("{0}",maps.get(str)) + "";
			i ++ ;
		}
		if (where.length() > 0) {
			where.deleteCharAt(where.length() - 1);
		}
		return formatIgnoreType(
						sql,
						tb.getTableName(),
						formatIgnoreType(where.toString(),objs),
						tb.getPrimaryKey());
	}

	/**
	 * <p> 获得表名,根据Oracle的特性，表名大写
	 *  因此强行调用<code> java.lang.String.toUpperCase() </code>大写表名 </p>
	 * <p> 这里也使用了oracle的函数<code>lower</code>小写列名 </p>
	 */
	public String getTableColums(String tname) {
		return format("SELECT lower(t.COLUMN_NAME) FROM user_tab_columns t WHERE t.TABLE_NAME = {0}",tname.toUpperCase());
	}

	/**
	 * <p> 根据Map获得查询实体类的SQL </p>
	 */
	public String getFindByMap(String tableName,
			Map<String, Serializable> params) {
		StringBuffer sql = new StringBuffer("SELECT * FROM {0} WHERE 1 = 1 ") ;
		Object [] os = params.keySet().toArray();
		for (Object _o : os)
			sql.append(" AND " + _o + " = ? ");
		return formatIgnoreType(sql.toString(), tableName);
	}

	public String and(Map params){
		String target = StringUtils.EMPTY;
		if(params == null) return target;
		Iterator<String> ite = params.keySet().iterator();
		String key = null ;
		while(ite.hasNext()){
			key = ite.next();
			if( params.get(key) != null ){
				target += format( key + "={0}", params.get(key)) + " and ";
			}
		}
		return StringHelper.delEndChar( target ,  4 ) ;
	}

	public String in(Object[] value) {
		String target = StringUtils.EMPTY;
		if(value == null ) return target;
		boolean flag = false;
		for (Object o : value) {
			if (o != null){
				target += format("{0},", o);
				flag = true;
			}
		}
		if (flag)
			return StringHelper.delEndChar( target ,  1 );
		else
			return "-111";
	}
	/**
	 * 处理where in 里面的数据。当where in 里面的参数值超过一定数量时，把它分开来处理。如oracle in
	 * 里面的参数超出1000条数据将报错，此方法可以处理之. 形式如：select * from table1 where 1=1 and (ID in
	 * (1,2,3,4,...,1000) or  ID in (1001,1002,...))
	 * 格式如："select * from r_card r where 1=1 and ("+getSqlGenerator().setWhereInArray("r.card_id",values)+")";
	 * @param name
	 *            参数类型  如  r.card_id 会组装成  r.card_id in ('1','2',....'1000') or r.card_id in ('1001',...'2000')
	 * @param value
	 *            参数值
	 * @return 返回 分割后的sql
	 */
	public String setWhereInArray(String name, Object[] value) {
		int inArrayNum = value.length % 1000 == 0 ? value.length
				/ 1000 : value.length / 1000 + 1;// 参数值一共能分割成多少组，记录总组数
		int m = 0;// 用来记录参数值数字的下标值
		int b = 0;// 用来记录参数值数组和sql片段，能分出多少个参数值片段就能分割成多少个sql片段
		int n = 1000;// 用来记录参数值数字下标值的步长，步长的长度等于需要分割的数量，如要1000
		String p[] = new String[inArrayNum];// 分割的参数值片段数组
		String arrySql[] = new String[inArrayNum];// 分割的sql片段数组
		for (int k = 0; k < value.length; k++) {
			if (b < inArrayNum) {
				p[b] = "";
				for (; m < n; m++) {
					if (m >= value.length) {
						break;
					}
					p[b] += "'" + value[m].toString().trim() + "',";
				}
				p[b] = p[b].substring(0, p[b].lastIndexOf(","));
				arrySql[b] =  " or " + name + " in ("+p[b]+")";
				b++;
				n += 1000;
			}
		}
		String sql ="";
		if(arrySql.length>0){
			arrySql[0] = StringHelper.delStartChar( arrySql[0] ,  3 );
			for (int q = 0; q < arrySql.length; q++) {
				sql += arrySql[q];
			}
		}
		return  sql;
	}

	public String or(Map<String, Object> params) {
		String target = StringUtils.EMPTY;
		if(params == null ) return target;
		Iterator<String> ite = params.keySet().iterator();
		String key = null ;
		while(ite.hasNext()){
			key = ite.next();
			if( params.get(key) != null ){
				target += format( key + "={0}", params.get(key)) + " or ";
			}
		}
		return StringHelper.delEndChar( target , 3);
	}
}
