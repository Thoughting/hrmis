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
 * 员工工资项
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_EMPLOYEE_WAGE_ITEM")
@DynamicInsert
@DynamicUpdate
public class EmployeeWageItem extends IdEntity<EmployeeWageItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 848809825204683215L;
	
	private Employee employee; // 所属员工
	private WageCountItem wageCountItem; // 所属工资核算项
	
	private Double count;
	private String remark;

	public EmployeeWageItem() {
	}

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
	@JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "ID")
	@JsonIgnore
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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
