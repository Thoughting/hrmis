package com.eastcom.hrmis.modules.emp.entity;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

/**
 * 员工排班计划
 * 
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_EMPLOYEE_SET_WORK_PLAN")
@DynamicInsert @DynamicUpdate
public class EmployeeSetWorkPlan extends IdEntity<EmployeeSetWorkPlan> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5222890407681576070L;
	private Employee employee;
	private String workMonth;
	private Integer commitStatus; // 0：未提交，1：已提交
	private String commiter;
	private Date commitDate;
	private String remark;

	public String getWorkMonth() {
		return this.workMonth;
	}

	public void setWorkMonth(String workMonth) {
		this.workMonth = workMonth;
	}

	public Integer getCommitStatus() {
		return this.commitStatus;
	}

	public void setCommitStatus(Integer commitStatus) {
		this.commitStatus = commitStatus;
	}

	public String getCommiter() {
		return this.commiter;
	}

	public void setCommiter(String commiter) {
		this.commiter = commiter;
	}

	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
	public Date getCommitDate() {
		return this.commitDate;
	}

	public void setCommitDate(Date commitDate) {
		this.commitDate = commitDate;
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

	@Transient
	public String getCommitStatusDict(){
		if (this.commitStatus == 0) {
			return "未提交";
		}
		return "已提交";
	}
}
