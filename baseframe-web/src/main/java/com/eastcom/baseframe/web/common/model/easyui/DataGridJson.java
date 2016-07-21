package com.eastcom.baseframe.web.common.model.easyui;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * DataGrid模型<br>
 * Description: <br>
 * Copyright: eastcom_sw Copyright (C) 2013 <br>
 * 
 * @author <a href="mailto:邮箱">hujian</a><br>
 * @e-mail: hujian@eastcom-sw.com<br>
 */
@SuppressWarnings("rawtypes")
public class DataGridJson implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long total = 0L;// 总记录数
	private List rows = new ArrayList();// 每行记录
	private List footer = new ArrayList();// 表格统计信息

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public List getFooter() {
		return footer;
	}

	public void setFooter(List footer) {
		this.footer = footer;
	}

}
