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
 * 职务部门
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_EMPLOYEE_DEPT")
@DynamicInsert @DynamicUpdate
public class EmployeeDept extends IdEntity<EmployeeDept> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7539212622259468267L;
	
	private EmployeeDeptLeader deptLeader; // 分管部门领导
	
	private String code;
	private String name;
	private String telephone;
	private Double workTimer;
	private String remark;

	private List<Employee> employees = Lists.newArrayList(); // 部门员工
	
	public EmployeeDept() {
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Double getWorkTimer() {
		return this.workTimer;
	}

	public void setWorkTimer(Double workTimer) {
		this.workTimer = workTimer;
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
	@JoinColumn(name = "EMPLOYEE_DEPT_LEADER_ID", referencedColumnName = "ID")
	public EmployeeDeptLeader getDeptLeader() {
		return deptLeader;
	}

	public void setDeptLeader(EmployeeDeptLeader deptLeader) {
		this.deptLeader = deptLeader;
	}

	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "employeeDept", fetch = FetchType.LAZY)
	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
	/******************树图属性*****************/
	private boolean checked = false;
	@Transient
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	@Transient
	public String getText() {
		return name;
	}
	
}
