package com.eastcom.hrmis.modules.emp.entity;
// default package
// Generated 2016-1-19 13:37:23 by Hibernate Tools 3.4.0.CR1

import com.eastcom.baseframe.common.entity.IdEntity;
import com.eastcom.baseframe.web.modules.sys.cache.DictCache;
import com.eastcom.hrmis.modules.emp.EmpConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

/**
 * 员工学习经历
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_EMPLOYEE_STUDY")
@DynamicInsert
@DynamicUpdate
public class EmployeeStudy extends IdEntity<EmployeeStudy> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4229380636824834008L;
	
	private Employee employee;
	
	private Date startDate;
	private Date endDate;
	private String school;
	private String major;
	private Integer educationType;
	private Integer studyType;

	public EmployeeStudy() {
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

	public String getSchool() {
		return this.school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getMajor() {
		return this.major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public Integer getEducationType() {
		return this.educationType;
	}

	public void setEducationType(Integer educationType) {
		this.educationType = educationType;
	}

	public Integer getStudyType() {
		return this.studyType;
	}

	public void setStudyType(Integer studyType) {
		this.studyType = studyType;
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
	public String getEducationTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_EMPLOYEE_EDUCATION_TYPE, this.educationType);
	}
	
	@Transient
	public String getStudyTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_EMPLOYEE_STUDY_TYPE, this.studyType);
	}
	
}
