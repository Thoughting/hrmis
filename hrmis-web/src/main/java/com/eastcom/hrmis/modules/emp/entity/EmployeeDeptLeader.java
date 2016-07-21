package com.eastcom.hrmis.modules.emp.entity;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

/**
 * 分管部门领导
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_EMPLOYEE_DEPT_LEADER")
@DynamicInsert @DynamicUpdate
public class EmployeeDeptLeader extends IdEntity<EmployeeDeptLeader> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3273272339901520463L;
	
	private String name; // 姓名
	private String remark; // 备注

	private List<EmployeeDept> depts = Lists.newArrayList(); // 分管部门清单
	
	public EmployeeDeptLeader() {
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
	@OneToMany(mappedBy = "deptLeader", fetch = FetchType.LAZY)
	public List<EmployeeDept> getDepts() {
		return depts;
	}

	public void setDepts(List<EmployeeDept> depts) {
		this.depts = depts;
	}

	@Transient
	public String getDeptStr(){
		String deptStr = "";
		if (CollectionUtils.isNotEmpty(this.depts)) {
			for (EmployeeDept employeeDept : depts) {
				deptStr += employeeDept.getName() + ",";
			}
			deptStr = deptStr.substring(0, deptStr.length() - 1);
		}
		return deptStr;
	}
}
