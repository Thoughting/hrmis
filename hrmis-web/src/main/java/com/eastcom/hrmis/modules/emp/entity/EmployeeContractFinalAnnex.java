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
 * 员工合同尾页附件清单
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_EMPLOYEE_CONTRACT_FINAL_ANNEX")
@DynamicInsert
@DynamicUpdate
public class EmployeeContractFinalAnnex extends IdEntity<EmployeeContractFinalAnnex> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3984191173766215819L;
	
	private Employee employee;
	private String annexName;
	private byte[] annexContent;

	public EmployeeContractFinalAnnex() {
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
