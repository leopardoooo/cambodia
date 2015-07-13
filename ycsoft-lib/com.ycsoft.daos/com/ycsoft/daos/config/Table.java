package com.ycsoft.daos.config;

import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * <p>存放表相关信息</p>
 * <p> 配置的时候需要根据数据库的格式，如oracle的表名是大写，
 *     否则可能导致JavaBean与数据库的表对应不上的后果。
 *     JavaBean 属性的定义需要与数据库的中保持一致 ,格式如：user_login_name <p>
 */
public class Table {

    //默认的表明为该实体类的类名，
    private String tableName = "" ;

    //实体类对应的主键
    private String primaryKey = "" ;

    //序列名
    private String sequenceName = "";

    //存储所有的列
    private List<String> columns;

    public Table(){}

    public Table(POJO data){
    	this(data.tn(),data.pk(),data.sn());
    }

    public Table(
    			String tableName,
    			String primaryKey,
    			String sequenceName) {
		if (!tableName.equals(""))
			setTableName(tableName.trim());
		if (!primaryKey.equals(""))
			setPrimaryKey(primaryKey.trim());
		if (!"".equals(sequenceName))
			setSequenceName(sequenceName.trim());
	}

	public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	/**
	 * <p> 判断是否已配置主键 </p>
	 * @throws Exception
	 */
	public boolean isNotKey(){
		if(StringUtils.isEmpty(getPrimaryKey()) ||
					StringUtils.isBlank(getPrimaryKey())){
			return true ;
		}
		return false;
	}
	/**
	 * <p> 判断是否已配置序列名 </p>
	 * @throws Exception
	 */
	public boolean isNotSequence(){
		if(StringUtils.isEmpty(getSequenceName()) ||
					StringUtils.isBlank(getSequenceName())){
			return true ;
		}
		return false;
	}
}
