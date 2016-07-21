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
 * 职务岗位
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_EMPLOYEE_POST")
@DynamicInsert @DynamicUpdate
public class EmployeePost extends IdEntity<EmployeePost> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6871096535470304084L;

	/**
	 * 0 ： 生产岗位
	 * 1 ： 管理岗位
	 */
	private Integer type; 
	private String code;
	private String name;
	private String remark;

	private List<Employee> employees = Lists.newArrayList(); // 部门员工
	
	public EmployeePost() {
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "employeePost", fetch = FetchType.LAZY)
	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
	@Transient
	public String getTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_EMPLOYEE_POST_TYPE, this.type);
	}
	
	/******************树图属性*****************/
	private boolean checked = false;
	@Transient
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	@Transient
	public String getText() {
		return name;
	}
	
}
