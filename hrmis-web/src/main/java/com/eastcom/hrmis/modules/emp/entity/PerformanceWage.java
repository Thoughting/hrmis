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
 * 员工绩效工资
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_PERFORMANCE_WAGE")
@DynamicInsert
@DynamicUpdate
public class PerformanceWage extends IdEntity<PerformanceWage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2673828699055208440L;
	
	private Employee employee; // 所属员工
	
	private Integer type; // 绩效工资类型
	private String wageDateStr; // 绩效工资日期
	private String remark; // 备注

	private List<PerformanceWageItem> wageItems = Lists.newArrayList(); // 员工绩效工资项
	
	public PerformanceWage() {
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getWageDateStr() {
		return wageDateStr;
	}

	public void setWageDateStr(String wageDateStr) {
		this.wageDateStr = wageDateStr;
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

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "performanceWage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<PerformanceWageItem> getWageItems() {
		return wageItems;
	}

	public void setWageItems(List<PerformanceWageItem> wageItems) {
		this.wageItems = wageItems;
	}

}
