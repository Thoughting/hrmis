package com.eastcom.baseframe.common.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页帮助类<br>
 * Description: <br>
 * Copyright: eastcom_sw Copyright (C) 2013 <br>
 * 
 * @author <a href="mailto:邮箱">hujian</a><br>
 * @e-mail: hujian@eastcom-sw.com<br>
 * 
 */
public class PageHelper<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int page;// 当前页
	private int rows;// 每页显示记录数,值为“-1”表示不进行分页查询
	private String sort;// 排序字段
	private String order;// asc/desc

	private Long count = 0L;// 总记录数，设置为“-1”表示不查询总数

	private List<T> list = new ArrayList<T>();

	public PageHelper() {
	}
	
	/**
	 * 构造方法
	 * 
	 * @param page
	 *            当前页码
	 * @param rows
	 *            分页大小
	 */
	public PageHelper(int page, int rows) {
		this(page, rows, 0);
	}

	/**
	 * 构造方法
	 * 
	 * @param page
	 *            当前页码
	 * @param rows
	 *            分页大小
	 * @param count
	 *            数据条数
	 */
	public PageHelper(int page, int rows, long count) {
		this(page, rows, count, new ArrayList<T>());
	}

	/**
	 * 构造方法
	 * 
	 * @param page
	 *            当前页码
	 * @param rows
	 *            分页大小
	 * @param count
	 *            数据条数
	 * @param list
	 *            本页数据对象列表
	 */
	public PageHelper(int page, int rows, long count, List<T> list) {
		this.setCount(count);
		this.setPage(page);
		this.setRows(rows);
		this.setList(list);
	}

	/**
	 * 构造方法
	 * @param list
	 *       数据对象列表
	 * @param count
	 *       数据条数
	 */
	public PageHelper(List<T> list, long count) {
		this.setList(list);
		this.setCount(count);
	}

	/**
	 * 分页是否有效
	 * 
	 * @return this.rows==-1
	 */
	@JsonIgnore
	public boolean isDisabled() {
		return this.rows == -1;
	}

	@JsonIgnore
	public boolean isNotCount() {
		return this.count == -1;
	}

	/**
	 * 获取 Hibernate FirstResult
	 */
	@JsonIgnore
	public int getFirstResult() {
		int firstResult = (getPage() - 1) * getRows();
		if (firstResult >= getCount()) {
			firstResult = 0;
		}
		return firstResult;
	}

	@JsonIgnore
	public int getLastResult() {
		int lastResult = getFirstResult() + getMaxResults();
		if (lastResult > getCount()) {
			lastResult = getCount().intValue();
		}
		return lastResult;
	}

	/**
	 * 获取 Hibernate MaxResults
	 */
	public int getMaxResults() {
		return getRows();
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "PageHelper [page=" + page + ", rows=" + rows + ", sort=" + sort + ", order="
				+ order + "]";
	}

}
