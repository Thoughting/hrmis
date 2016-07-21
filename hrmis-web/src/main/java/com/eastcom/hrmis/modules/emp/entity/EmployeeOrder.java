package com.eastcom.hrmis.modules.emp.entity;

import com.eastcom.baseframe.web.common.entity.DataEntity;
import com.eastcom.baseframe.web.modules.sys.cache.DictCache;
import com.eastcom.hrmis.modules.emp.EmpConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * 员工工单表
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_EMPLOYEE_ORDER")
@DynamicInsert @DynamicUpdate
public class EmployeeOrder extends DataEntity<EmployeeOrder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8996358089609725660L;
	
	private Employee employee;
	
	/**
	 * 0：驾驶证过期提醒
	 * 1：身份证过期提醒
	 * 2：介绍费提醒
	 * 3：转正提醒
	 * 4：合同到期提醒
	 * 5：档案配置审核提醒
	 * 6: 退休提醒
	 */
	private Integer type;
	private String content;
	private Integer status; // 0: 代办  1: 已办

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Transient
	public String getTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_AGENCY_ORDER_TYPE, this.type);
	}

	@Transient
	public String getStatusDict(){
		switch (this.status){
			case 0:
				return "待处理";
			case 1:
				return "已处理";
		}
		return null;
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
	public String getEmployeeName(){
		if (this.getEmployee() != null) {
			return this.getEmployee().getName();
		}
		return "";
	}

	@Transient
	public String getEmployeeDeptName(){
		if (this.getEmployee() != null) {
			return this.getEmployee().getEmployeeDeptName();
		}
		return "";
	}
}
