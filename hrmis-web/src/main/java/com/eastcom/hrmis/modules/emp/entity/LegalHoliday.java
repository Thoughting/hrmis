package com.eastcom.hrmis.modules.emp.entity;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 法定节假日
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_LEGAL_HOLIDAY")
@DynamicInsert
@DynamicUpdate
public class LegalHoliday extends IdEntity<LegalHoliday> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7957554672994740514L;
	private String year;
	private Date holiday;
	private String remark;

	public LegalHoliday() {
	}

	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getHoliday() {
		return this.holiday;
	}

	public void setHoliday(Date holiday) {
		this.holiday = holiday;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
