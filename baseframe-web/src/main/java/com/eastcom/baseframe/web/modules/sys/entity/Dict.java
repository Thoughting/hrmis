package com.eastcom.baseframe.web.modules.sys.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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

import com.eastcom.baseframe.common.entity.TreeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 字典Entity
 */
@Entity
@Table(name = "T_SYS_DICT")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dict extends TreeEntity<Dict> {

	private static final long serialVersionUID = 1L;
	private String type; // 类型
	private String code; // 编码
	private String name; // 名称
	private String description;// 描述

	public Dict() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public Dict getParent() {
		return parent;
	}

	public void setParent(Dict parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@OrderBy(value = "sort")
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Override
	public List<Dict> getChildren() {
		return this.children;
	}

	@Override
	public void setChildren(List<Dict> children) {
		this.children = children;
	}

	// 树图用到属性
	@Transient
	public String getState() {
		if (CollectionUtils.isEmpty(this.children)) {
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
	

}