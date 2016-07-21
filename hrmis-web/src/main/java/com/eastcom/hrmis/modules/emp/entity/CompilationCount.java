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
 * 编制数
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_COMPILATION_COUNT")
@DynamicInsert @DynamicUpdate
public class CompilationCount extends IdEntity<CompilationCount> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 121289033550980295L;
	
	private CompilationTable compilationTable; // 所属编制表
	
	private EmployeeDept employeeDept; // 所属部门
	private EmployeePost employeePost; // 所属岗位
	
	private Integer count; // 编制数
	private String remark; // 备注

	public CompilationCount() {
	}

	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
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
	@JoinColumn(name = "COMPILATION_TABLE_ID", referencedColumnName = "ID")
	@NotFound(action = NotFoundAction.IGNORE)
	public CompilationTable getCompilationTable() {
		return compilationTable;
	}

	public void setCompilationTable(CompilationTable compilationTable) {
		this.compilationTable = compilationTable;
	}

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "EMPLOYEE_DEPT_ID", referencedColumnName = "ID")
	@NotFound(action = NotFoundAction.IGNORE)
	public EmployeeDept getEmployeeDept() {
		return employeeDept;
	}

	public void setEmployeeDept(EmployeeDept employeeDept) {
		this.employeeDept = employeeDept;
	}

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "EMPLOYEE_POST_ID", referencedColumnName = "ID")
	@NotFound(action = NotFoundAction.IGNORE)
	public EmployeePost getEmployeePost() {
		return employeePost;
	}

	public void setEmployeePost(EmployeePost employeePost) {
		this.employeePost = employeePost;
	}

}
