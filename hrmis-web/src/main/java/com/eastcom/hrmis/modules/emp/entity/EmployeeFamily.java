package com.eastcom.hrmis.modules.emp.entity;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.eastcom.baseframe.web.modules.sys.cache.DictCache;
import com.eastcom.hrmis.modules.emp.EmpConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * 员工家庭成员
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_EMPLOYEE_FAMILY")
@DynamicInsert
@DynamicUpdate
public class EmployeeFamily extends IdEntity<EmployeeFamily> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3275381270785546235L;
	
	private Employee employee;
	
	private String name;
	private Integer relationShip;
	private String cardNo;
	private String company;
	private String jobName;
	private String telephone;

	public EmployeeFamily() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRelationShip() {
		return this.relationShip;
	}

	public void setRelationShip(Integer relationShip) {
		this.relationShip = relationShip;
	}

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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

	@Transient
	public String getRelationShipDict(){
		return DictCache.getDictValue(EmpConstant.DICT_EMPLOYEE_RELATIONSHIP_TYPE, this.relationShip);
	}
}
