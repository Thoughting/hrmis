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
@Table(name = "T_EMPLOYEE_ANNEX")
@DynamicInsert
@DynamicUpdate
public class EmployeeAnnex extends IdEntity<EmployeeAnnex> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -406488299865276256L;
	private Employee employee;
	private String annexName;
	private byte[] annexContent;

	public EmployeeAnnex() {
	}

	public String getAnnexName() {
		return this.annexName;
	}

	public void setAnnexName(String annexName) {
		this.annexName = annexName;
	}

	@JsonIgnore
	public byte[] getAnnexContent() {
		return this.annexContent;
	}

	public void setAnnexContent(byte[] annexContent) {
		this.annexContent = annexContent;
	}

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
	
}
