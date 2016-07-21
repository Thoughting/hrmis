package com.eastcom.baseframe.web.modules.sys.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

/**
 * 资源Entity
 * 
 * @author ThinkGem
 * @version 2013-05-15
 */
@Entity
@Table(name = "T_SYS_RESOURCE")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Resource extends IdEntity<Resource> {

	public static final int TYPE_OPERATION = 0; //资源操作
	public static final int TYPE_MENU = 1; // 资源类型
	
	private static final long serialVersionUID = 1L;
	private Resource parent; // 父级资源点
	private String name; // 名称
	private String permission; // 权限标识
	private int type; // 类型
	private String href; // 链接
	private String icon; // 图标
	private int level; // 层级
	private int sort = 0; // 排序
	private String remarks; // 备注
	private Date createDate; // 创建时间

	private List<Resource> childList = Lists.newArrayList();// 拥有子资源点列表
	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表

	public Resource() {
		super();
		this.sort = 30;
	}

	public Resource(String id) {
		this();
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
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

	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@OrderBy(value = "sort")
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public List<Resource> getChildList() {
		return childList;
	}

	public void setChildList(List<Resource> childList) {
		this.childList = childList;
	}

	@ManyToMany(mappedBy = "resourceList", fetch = FetchType.LAZY )
	@OrderBy("id")
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@Transient
	public static void sortList(List<Resource> list, List<Resource> sourcelist,
			String parentId) {
		for (int i = 0; i < sourcelist.size(); i++) {
			Resource e = sourcelist.get(i);
			if (e.getParent() != null && e.getParent().getId() != null
					&& e.getParent().getId().equals(parentId)) {
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j = 0; j < sourcelist.size(); j++) {
					Resource child = sourcelist.get(j);
					if (child.getParent() != null
							&& child.getParent().getId() != null
							&& child.getParent().getId().equals(e.getId())) {
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}

	@Transient
	public static int levelCount(Resource resource,int level){
		if (resource.getParent() == null) {
			return level;
		}else{
			level++;
			levelCount(resource.getParent(),level);
		}
		return level;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	// 树图用到属性
	@Transient
	public String getState() {
		if (CollectionUtils.isEmpty(childList)) {
			return "open";
		}
		return "closed";
	}

	@Transient
	public String getParentId() {
		if (parent != null) {
			return parent.getId();
		}
		return null;
	}
	
	@Transient
	public List<Resource> getChildren() {
		return childList;
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
		if (StringUtils.isNotBlank(remarks)) {
			return name + " （" + remarks + "） ";
		}
		return name;
	}
	
	//当前位置信息
	private String namePath;
	@Transient
	public String getNamePath() {
		return namePath;
	}
	public void setNamePath(String namePath) {
		this.namePath = namePath;
	}
	
}