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
 * 工资数据模板
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_WAGE_TEMPLATE")
@DynamicInsert @DynamicUpdate
public class WageTemplate extends IdEntity<WageTemplate> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2359396730455933357L;
	
	private WagePlan wagePlan; // 所属工资方案
	private WageCountItem wageCountItem; // 所属工资核算项
	private EmployeePost employeePost; // 所属岗位
	
	private Double count;
	private String remark;

	public WageTemplate() {
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

	@JsonIgnore
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "WAGE_PLAN_ID", referencedColumnName = "ID")
	public WagePlan getWagePlan() {
		return wagePlan;
	}

	public void setWagePlan(WagePlan wagePlan) {
		this.wagePlan = wagePlan;
	}

	@JsonIgnore
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "WAGE_COUNT_ITEM_ID", referencedColumnName = "ID")
	public WageCountItem getWageCountItem() {
		return wageCountItem;
	}

	public void setWageCountItem(WageCountItem wageCountItem) {
		this.wageCountItem = wageCountItem;
	}

	@JsonIgnore
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "EMPLOYEE_POST_ID", referencedColumnName = "ID")
	public EmployeePost getEmployeePost() {
		return employeePost;
	}

	public void setEmployeePost(EmployeePost employeePost) {
		this.employeePost = employeePost;
	}

}
