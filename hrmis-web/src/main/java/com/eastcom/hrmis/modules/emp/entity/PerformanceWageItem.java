package com.eastcom.hrmis.modules.emp.entity;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 员工绩效工资项
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_PERFORMANCE_WAGE_ITEM")
@DynamicInsert
@DynamicUpdate
public class PerformanceWageItem extends IdEntity<PerformanceWageItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6773505097021840200L;
	
	private PerformanceWage performanceWage; // 所属员工绩效工资
	
	private String field; // 字段名
	private String value; // 字段值
	private String remark; // 备注

	public PerformanceWageItem() {
	}

	public String getField() {
		return this.field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "PERFORMANCE_WAGE_ID", referencedColumnName = "ID")
	@JsonIgnore
	public PerformanceWage getPerformanceWage() {
		return performanceWage;
	}

	public void setPerformanceWage(PerformanceWage performanceWage) {
		this.performanceWage = performanceWage;
	}

}
