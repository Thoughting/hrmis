package com.eastcom.hrmis.modules.emp.entity;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.eastcom.baseframe.web.modules.sys.cache.DictCache;
import com.eastcom.hrmis.modules.emp.EmpConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;


/**
 * 员工实发工资
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_EMPLOYEE_WAGE")
@DynamicInsert
@DynamicUpdate
public class EmployeeWage extends IdEntity<EmployeeWage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5810736975444250069L;
	
	private Employee employee; // 所属员工
	
	private String wageDateStr;
	private Integer auditStatus; // 审核状态 0 未提交审核 1 审核中 2 审核通过 3 审核不通过
	private String auditContent;
	private String remark;

	private List<EmployeeWageActualItem> wageItems = Lists.newArrayList(); // 员工实发工资项
	
	public EmployeeWage() {
	}

	public String getWageDateStr() {
		return this.wageDateStr;
	}

	public void setWageDateStr(String wageDateStr) {
		this.wageDateStr = wageDateStr;
	}

	public Integer getAuditStatus() {
		return this.auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditContent() {
		return this.auditContent;
	}

	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "employeeWage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<EmployeeWageActualItem> getWageItems() {
		return wageItems;
	}

	public void setWageItems(List<EmployeeWageActualItem> wageItems) {
		this.wageItems = wageItems;
	}

	/**
	 * 审核状态
	 * @return
	 */
	@Transient
	public String getAuditStatusDict(){
		return DictCache.getDictValue(EmpConstant.DICT_EMPLOYEE_AUDITSTATUS_TYPE, this.auditStatus);
	}
}
