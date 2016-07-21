package com.eastcom.hrmis.modules.emp.entity;
// default package
// Generated 2016-1-19 13:37:23 by Hibernate Tools 3.4.0.CR1

import com.eastcom.baseframe.common.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 员工工作经历
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_EMPLOYEE_WORK")
@DynamicInsert
@DynamicUpdate
public class EmployeeWork extends IdEntity<EmployeeWork> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2634879897379450642L;
	
	private Employee employee;
	
	private Date startDate;
	private Date endDate;
	private String company;
	private String jobName;
	private String witness;
	private String telephone;

	public EmployeeWork() {
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getJobName() {
		return this.jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getWitness() {
		return this.witness;
	}

	public void setWitness(String witness) {
		this.witness = witness;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
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

}
