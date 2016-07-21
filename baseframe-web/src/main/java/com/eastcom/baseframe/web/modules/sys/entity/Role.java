package com.eastcom.baseframe.web.modules.sys.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.eastcom.baseframe.web.modules.sys.SysConstant;
import com.eastcom.baseframe.web.modules.sys.cache.DictCache;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

/**
 * 角色Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
@Entity
@Table(name = "T_SYS_ROLE")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends IdEntity<Role> {
	
	private static final long serialVersionUID = 1L;
	private String name; 	// 角色名称
	private String nameCn; // 角色中文名
	private Integer level; // 角色级别
	private String remarks; // 备注
	private Date createDate; // 创建时间
	
	private List<User> userList = Lists.newArrayList(); // 拥有用户列表
	private List<Resource> resourceList = Lists.newArrayList(); // 拥有资源列表
	private List<Area> areaList = Lists.newArrayList(); // 拥有区域列表
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@ManyToMany(mappedBy = "roleList", fetch=FetchType.LAZY)
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	
	@Transient
	public int getUserCount(){
		return userList.size();
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_SYS_ROLE_RESOURCE", joinColumns = { @JoinColumn(name = "FK_ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "FK_RESOURCE_ID") })
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public List<Resource> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<Resource> resourceList) {
		this.resourceList = resourceList;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_SYS_ROLE_AREA", joinColumns = { @JoinColumn(name = "FK_ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "FK_AREA_ID") })
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public List<Area> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<Area> areaList) {
		this.areaList = areaList;
	}
	
	@Transient
	public int getResourceCount(){
		return resourceList.size();
	}
	
	@Transient
	public int getAreaCount(){
		return areaList.size();
	}
	
	private boolean checked = false;
	@Transient
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	/**
	 * 获取权限字符串列表
	 */
	@Transient
	@JsonIgnore
	public List<String> getPermissions() {
		List<String> permissions = Lists.newArrayList();
		for (Resource resource : resourceList) {
			if (resource.getPermission()!=null && !"".equals(resource.getPermission())){
				permissions.add(resource.getPermission());
			}
		}
		return permissions;
	}

	@Transient
	public String getLevelDict(){
		return DictCache.getDictValue(SysConstant.DICT_SYS_ROLE_LEVEL, this.level);
	}
}
