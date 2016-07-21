package com.eastcom.hrmis.modules.emp.entity;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.eastcom.baseframe.common.utils.excel.ExcelColumn;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.List;

/**
 * 人员编制表
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_COMPILATION_TABLE")
@DynamicInsert @DynamicUpdate
public class CompilationTable extends IdEntity<CompilationTable> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2998339959832831496L;
	
	private String name; //名称
	private String remark; //备注
	
	private List<EmployeeDept> depts = Lists.newArrayList(); // 编制部门
	private List<EmployeePost> posts = Lists.newArrayList(); // 编制岗位
	private List<CompilationCount> counts = Lists.newArrayList(); // 编制数
	
	public CompilationTable() {
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

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_COMPILATION_TABLE_DEPT_RELATION", joinColumns = { @JoinColumn(name = "COMPILATION_TABLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "EMPLOYEE_DEPT_ID") })
	@OrderBy("code") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public List<EmployeeDept> getDepts() {
		return depts;
	}

	public void setDepts(List<EmployeeDept> depts) {
		this.depts = depts;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_COMPILATION_TABLE_POST_RELATION", joinColumns = { @JoinColumn(name = "COMPILATION_TABLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "EMPLOYEE_POST_ID") })
	@OrderBy("code") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public List<EmployeePost> getPosts() {
		return posts;
	}

	public void setPosts(List<EmployeePost> posts) {
		this.posts = posts;
	}

	@JsonIgnore
	@OneToMany(mappedBy = "compilationTable", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<CompilationCount> getCounts() {
		return counts;
	}

	public void setCounts(List<CompilationCount> counts) {
		this.counts = counts;
	}
	
	@Transient
	public String getDeptsDescribe(){
		String describe = "";
		if (CollectionUtils.isNotEmpty(this.depts)) {
			for (EmployeeDept employeeDept : depts) {
				describe += employeeDept.getName() + "<br />";
			}
		}
		return describe;
	}
	
	@Transient
	public String getPostsDescribe(){
		String describe = "";
		if (CollectionUtils.isNotEmpty(this.posts)) {
			for (EmployeePost employeePost : posts) {
				describe += employeePost.getName() + "<br />";
			}
		}
		return describe;
	}
	
	@Transient
	public List<ExcelColumn> toExcelColumns(){
		List<ExcelColumn> excelColumns = Lists.newArrayList();
		ExcelColumn column = null;
		column = new ExcelColumn("分管领导", "leaderName", 80);
		excelColumns.add(column);
		column = new ExcelColumn("项目部", "deptName", 120);
		excelColumns.add(column);
		if (CollectionUtils.isNotEmpty(this.posts)) {
			for (EmployeePost post : this.posts) {
				column = new ExcelColumn(post.getName(), "post", 80);
				column.getChildren().add(new ExcelColumn("定", post.getId() + "_DING", 50));
				column.getChildren().add(new ExcelColumn("在", post.getId() + "_ZAI", 50));
				column.getChildren().add(new ExcelColumn("缺", post.getId() + "_QUE", 50));
				excelColumns.add(column);
			}
		}
		return excelColumns;
	}
}
