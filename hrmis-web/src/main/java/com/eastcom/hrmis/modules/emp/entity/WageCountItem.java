package com.eastcom.hrmis.modules.emp.entity;

import com.eastcom.baseframe.common.entity.TreeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "T_WAGE_COUNT_ITEM")
@DynamicInsert @DynamicUpdate
public class WageCountItem extends TreeEntity<WageCountItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 829814371390048280L;
	private String code;
	private String name;
	private String remarks;

	private Integer showWidth;
	private String showFormatter;
	
	public WageCountItem() {
	}

	public String getCode() {
		return code;
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

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getShowWidth() {
		return showWidth;
	}

	public void setShowWidth(Integer showWidth) {
		this.showWidth = showWidth;
	}

	public String getShowFormatter() {
		return showFormatter;
	}

	public void setShowFormatter(String showFormatter) {
		this.showFormatter = showFormatter;
	}
	
	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public WageCountItem getParent() {
		return this.parent;
	}

	@Override
	public void setParent(WageCountItem parent) {
		this.parent = parent;
	}

	@Override
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy(value = "sort")
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	public List<WageCountItem> getChildren() {
		return this.children;
	}

	@Override
	public void setChildren(List<WageCountItem> children) {
		this.children = children;
	}

	@Transient
	public static void sortList(List<WageCountItem> list,
								List<WageCountItem> sourcelist, String parentId) {
		for (int i = 0; i < sourcelist.size(); i++) {
			WageCountItem e = sourcelist.get(i);
			if (e.getParent() != null && e.getParent().getId() != null
					&& e.getParent().getId().equals(parentId)) {
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j = 0; j < sourcelist.size(); j++) {
					WageCountItem child = sourcelist.get(j);
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

	// 树图用到属性
	@Transient
	public String getState() {
		/*
		 * if (CollectionUtils.isEmpty(children)) { return "open"; } return
		 * "closed";
		 */
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
