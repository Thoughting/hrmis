package com.eastcom.baseframe.common.entity;

import com.google.common.collect.Lists;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.List;

/**
 * Tree形Entity类
 * @author wutingguang <br>
 */
@MappedSuperclass
public abstract class TreeEntity<T> extends IdEntity<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3464058192720289637L;

	protected T parent; // 父类
	protected List<T> children = Lists.newArrayList(); // 子类
	protected Integer sort = 20; // 排序
	
	/**
	 * 子类实现
	 * @return
	 */
	@Transient
	public abstract T getParent();
	public abstract void setParent(T parent);
	
	/**
	 * 子类实现
	 * @return
	 */
	@Transient
	public abstract List<T> getChildren();
	public abstract void setChildren(List<T> children);
	
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}
