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

@Entity
@Table(name = "T_EMPLOYEE_WAGE_HIS_ITEM")
@DynamicInsert
@DynamicUpdate
public class EmployeeWageHisItem extends IdEntity<EmployeeWageHisItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 848809825204683215L;
	
	private EmployeeWageHis employeeWageHis; // 所属员工历史工资
	private WageCountItem wageCountItem; // 所属工资核算项
	
	private Double count;
	private String remark;

	public Double getCount() {
		return this.count;
	}

	public void setCount(Double count) {
		this.count = count;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "EMPLOYEE_WAGE_HIS_ID", referencedColumnName = "ID")
	@JsonIgnore
	public EmployeeWageHis getEmployeeWageHis() {
		return employeeWageHis;
	}

	public void setEmployeeWageHis(EmployeeWageHis employeeWageHis) {
		this.employeeWageHis = employeeWageHis;
	}

	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "WAGE_COUNT_ITEM_ID", referencedColumnName = "ID")
	@JsonIgnore
	public WageCountItem getWageCountItem() {
		return wageCountItem;
	}

	public void setWageCountItem(WageCountItem wageCountItem) {
		this.wageCountItem = wageCountItem;
	}
}
