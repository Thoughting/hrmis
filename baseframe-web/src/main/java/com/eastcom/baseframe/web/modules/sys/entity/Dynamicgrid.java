package com.eastcom.baseframe.web.modules.sys.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

@Entity
@javax.persistence.Table(name = "T_SYS_DYNAMIC_GRID")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dynamicgrid extends IdEntity<Dynamicgrid>{

	private static final long serialVersionUID = 1675829052797050514L;
	private String code; // 编号
	private String name; // 名称
	private Integer headDepth; // 标题头深度
	private String remarks; // 备注
	private List<DynamicgridItem> dynamicgridItems = Lists.newArrayList(); // 该表拥有的字段
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getHeadDepth() {
		return headDepth;
	}
	public void setHeadDepth(Integer headDepth) {
		this.headDepth = headDepth;
	}
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@OneToMany(mappedBy = "dynamicgrid", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore 
	public List<DynamicgridItem> getDynamicgridItems() {
		return dynamicgridItems;
	}
	
	public void setDynamicgridItems(List<DynamicgridItem> dynamicgridItems) {
		this.dynamicgridItems = dynamicgridItems;
	}

}
