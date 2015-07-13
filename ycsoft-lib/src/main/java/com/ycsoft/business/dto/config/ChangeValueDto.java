/**
 * 记录异动信息的DTO
 */
package com.ycsoft.business.dto.config;

/**
 * @author YC-SOFT
 *
 */
public class ChangeValueDto {
	private String columnName; //修改的字段名
	private String oldValue;   //修改前的值
	private String newValue;   //修改后的值

	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
}
