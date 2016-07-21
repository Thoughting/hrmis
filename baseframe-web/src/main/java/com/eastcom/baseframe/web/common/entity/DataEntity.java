package com.eastcom.baseframe.web.common.entity;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 数据Entity类
 * 
 * 带有 createBy,createDate,updateBy,updateDate,remark 字段
 * 
 */
@MappedSuperclass
public abstract class DataEntity<T> extends IdEntity<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6794374272024524282L;

	protected String createBy; // 创建人
	protected Date createDate; // 创建时间
	protected String updateBy; // 更新人
	protected Date updateDate; // 更新时间
	protected String remark; // 备注

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
