package com.eastcom.hrmis.modules.emp.entity;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

/**
 * 员工实发工资-历史
 * 
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_EMPLOYEE_WAGE_HIS")
@DynamicInsert
@DynamicUpdate
public class EmployeeWageHis extends IdEntity<EmployeeWageHis> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3696230974688537925L;

	private Employee employee; // 所属员工
	private WagePlan wagePlan; // 所属工资计划
	
	private String wageDateStr; // 工资日期
	private String deptName; // 部门名字
	private String postName; // 岗位名字
	private String bankCard; // 银行卡号
	
	private List<EmployeeWageHisItem> wageItems = Lists.newArrayList(); // 员工工资项
	
	@ManyToOne
	@JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "ID")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@ManyToOne
	@JoinColumn(name = "WAGE_PLAN_ID", referencedColumnName = "ID")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public WagePlan getWagePlan() {
		return wagePlan;
	}

	public void setWagePlan(WagePlan wagePlan) {
		this.wagePlan = wagePlan;
	}
	
	public String getWageDateStr() {
		return wageDateStr;
	}

	public void setWageDateStr(String wageDateStr) {
		this.wageDateStr = wageDateStr;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "employeeWageHis", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<EmployeeWageHisItem> getWageItems() {
		return wageItems;
	}

	public void setWageItems(List<EmployeeWageHisItem> wageItems) {
		this.wageItems = wageItems;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

}
