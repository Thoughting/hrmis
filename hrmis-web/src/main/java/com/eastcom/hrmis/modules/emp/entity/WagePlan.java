package com.eastcom.hrmis.modules.emp.entity;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.eastcom.baseframe.web.modules.sys.cache.DictCache;
import com.eastcom.hrmis.modules.emp.EmpConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.List;

/**
 * 工资方案
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_WAGE_PLAN")
@DynamicInsert @DynamicUpdate
public class WagePlan extends IdEntity<WagePlan> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8877352506544380695L;
	private String name; // 名称
	private Integer countType; // 核算类型
	private String remark; // 备注

	private List<WageCountItem> countItems = Lists.newArrayList(); // 核算项
	private List<EmployeePost> posts = Lists.newArrayList(); // 岗位
	
	public WagePlan() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCountType() {
		return this.countType;
	}

	public void setCountType(Integer countType) {
		this.countType = countType;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_WAGE_PLAN_COUNT_ITEM_RELATION", joinColumns = { @JoinColumn(name = "WAGE_PLAN_ID") }, inverseJoinColumns = { @JoinColumn(name = "WAGE_COUNT_ITEM_ID") })
	@OrderBy("sort") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public List<WageCountItem> getCountItems() {
		return countItems;
	}

	public void setCountItems(List<WageCountItem> countItems) {
		this.countItems = countItems;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_WAGE_PLAN_POST_RELATION", joinColumns = { @JoinColumn(name = "WAGE_PLAN_ID") }, inverseJoinColumns = { @JoinColumn(name = "EMPLOYEE_POST_ID") })
	@OrderBy("code") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public List<EmployeePost> getPosts() {
		return posts;
	}

	public void setPosts(List<EmployeePost> posts) {
		this.posts = posts;
	}

	@Transient
	public String getCountTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_WAGESCOUNT_TYPE, this.countType);
	}
	
	@Transient
	public String getWageCountItemsDescribe(){
		String describe = "";
		if (CollectionUtils.isNotEmpty(this.countItems)) {
			for (WageCountItem countItem : countItems) {
				describe += countItem.getName() + ",";
			}
			describe = describe.substring(0, describe.length() - 1);
		}
		return describe;
	}
	
	@Transient
	public String getPostsDescribe(){
		String describe = "";
		if (CollectionUtils.isNotEmpty(this.posts)) {
			for (EmployeePost employeePost : posts) {
				describe += employeePost.getName() + ",";
			}
			describe = describe.substring(0, describe.length() - 1);
		}
		return describe;
	}
}
