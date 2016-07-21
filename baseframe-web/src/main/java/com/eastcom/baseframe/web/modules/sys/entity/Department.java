package com.eastcom.baseframe.web.modules.sys.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

import com.eastcom.baseframe.common.entity.TreeEntity;
import com.eastcom.baseframe.web.modules.sys.SysConstant;
import com.eastcom.baseframe.web.modules.sys.cache.DictCache;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

/**
 * 机构Entity
 */
@Entity
@Table(name = "T_SYS_DEPARTMENT")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Department extends TreeEntity<Department> {

	private static final long serialVersionUID = 1L;
	private String code; 	// 机构编码
	private String name; 	// 机构名称
	private int type = 0; 	// 机构类型（0：公司；1：部门；2：小组）
	private String remarks; // 备注
	
	private List<User> userList = Lists.newArrayList();   // 拥有用户列表
	
	public Department(){
		super();
	}
	
	public Department(String id){
		this();
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PARENT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public Department getParent() {
		return parent;
	}

	public void setParent(Department parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_SYS_USER_DEPARTMENT", joinColumns = { @JoinColumn(name = "FK_DEPARTMENT_ID") }, inverseJoinColumns = { @JoinColumn(name = "FK_USER_ID") })
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnore
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	@OneToMany(mappedBy = "parent", fetch=FetchType.EAGER,cascade=CascadeType.REMOVE)
	@OrderBy(value="code") @Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	public List<Department> getChildren() {
		return children;
	}

	public void setChildren(List<Department> children) {
		this.children = children;
	}

	@Transient
	public static void sortList(List<Department> list, List<Department> sourcelist, String parentId){
		for (int i=0; i<sourcelist.size(); i++){
			Department e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					Department child = sourcelist.get(j);
					if (child.getParent()!=null && child.getParent().getId()!=null
							&& child.getParent().getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}

	@Transient
	public String getTypeDict(){
		return DictCache.getDictValue(SysConstant.DICT_SYS_DEPARTMENT_TYPE, this.type);
	}
	
	//树图用到属性
	@Transient
	public String getState() {
		/*if (CollectionUtils.isEmpty(children)) {
			return "open";
		}
		return "closed";*/
		return "open";
	}

	@Transient
	public String getParentId() {
		if (parent != null) {
			return parent.getId();
		}
		return null;
	}
	
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