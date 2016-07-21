package com.eastcom.baseframe.web.modules.sys.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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
import com.eastcom.baseframe.common.utils.excel.ExcelColumn;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

@Entity
@javax.persistence.Table(name = "T_SYS_DYNAMIC_GRID_ITEM")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DynamicgridItem extends TreeEntity<DynamicgridItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8367232806871488037L;

	private Dynamicgrid dynamicgrid;

	private String field; // 字段名
	private String title; // 显示头
	private Integer width = 50; // 头宽度
	private String align; // 对齐方式
	private String formatter; // 格式化函数
	private String remarks; // 备注

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getFormatter() {
		return formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@ManyToOne
	@JoinColumn(name = "FK_T_SYS_DYNAMIC_GRID_ID", referencedColumnName = "ID")
	@JsonIgnore
	public Dynamicgrid getDynamicgrid() {
		return dynamicgrid;
	}

	public void setDynamicgrid(Dynamicgrid dynamicgrid) {
		this.dynamicgrid = dynamicgrid;
	}
	
	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public DynamicgridItem getParent() {
		return this.parent;
	}

	@Override
	public void setParent(DynamicgridItem parent) {
		this.parent = parent;
	}

	@Override
	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@OrderBy(value = "sort")
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	public List<DynamicgridItem> getChildren() {
		return this.children;
	}

	@Override
	public void setChildren(List<DynamicgridItem> children) {
		this.children = children;
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
	public String getName() {
		return this.field;
	}
	
	@Transient
	public String getText() {
		return this.field;
	}
	
	@Transient
	public static void sortList(List<DynamicgridItem> list, List<DynamicgridItem> sourcelist, String parentId){
		for (int i=0; i<sourcelist.size(); i++){
			DynamicgridItem e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					DynamicgridItem childe = sourcelist.get(j);
					if (childe.getParent()!=null && childe.getParent().getId()!=null
							&& childe.getParent().getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 将动态表头转换成excel导出的表头
	 * @return
	 */
	@Transient
	public static List<ExcelColumn> toExcelColumns(List<DynamicgridItem> items){
		List<ExcelColumn> excelColumns = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(items)) {
			for (DynamicgridItem dynamicgridItem : items) {
				excelColumns.add(DynamicgridItem.toExcelColumn(dynamicgridItem));
			}
		}
		return excelColumns;
	}
	
	@Transient
	private static ExcelColumn toExcelColumn(DynamicgridItem item){
		ExcelColumn excelColumn = new ExcelColumn(item.getTitle(), item.getField(), item.getWidth());
		if (CollectionUtils.isNotEmpty(item.getChildren())) {
			List<ExcelColumn> childExcelColumns = Lists.newArrayList();
			for (DynamicgridItem child : item.getChildren()) {
				childExcelColumns.add(DynamicgridItem.toExcelColumn(child));
			}
			excelColumn.setChildren(childExcelColumns);
		}
		return excelColumn;
	}
	
}
