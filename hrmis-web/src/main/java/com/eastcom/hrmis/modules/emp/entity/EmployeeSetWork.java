package com.eastcom.hrmis.modules.emp.entity;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.eastcom.baseframe.web.modules.sys.cache.DictCache;
import com.eastcom.hrmis.modules.emp.EmpConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

/**
 * 员工排班信息
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_EMPLOYEE_SET_WORK")
@DynamicInsert @DynamicUpdate
public class EmployeeSetWork extends IdEntity<EmployeeSetWork> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5592966202400886937L;
	private Employee employee;
	private Date workDate;
	private Integer isAtWork = 1; // 1:上班  0:休假
	private Integer atWorkTimer = 0;
	private Integer atWorkSpecialStatus = 0;
	private Integer atHolidayType = 0;
	private Integer atHolidayHour = 0;
	private Integer atHolidayMinute = 0;
	private Integer atHolidayTimer = 0; // 0:全天 1：上午 2：下午
	private String remark;

	public EmployeeSetWork() {
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getWorkDate() {
		return this.workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public Integer getIsAtWork() {
		return this.isAtWork;
	}

	public void setIsAtWork(Integer isAtWork) {
		this.isAtWork = isAtWork;
	}

	public Integer getAtWorkTimer() {
		return this.atWorkTimer;
	}

	public void setAtWorkTimer(Integer atWorkTimer) {
		this.atWorkTimer = atWorkTimer;
	}

	public Integer getAtWorkSpecialStatus() {
		return this.atWorkSpecialStatus;
	}

	public void setAtWorkSpecialStatus(Integer atWorkSpecialStatus) {
		this.atWorkSpecialStatus = atWorkSpecialStatus;
	}

	public Integer getAtHolidayType() {
		return this.atHolidayType;
	}

	public void setAtHolidayType(Integer atHolidayType) {
		this.atHolidayType = atHolidayType;
	}

	public Integer getAtHolidayHour() {
		return atHolidayHour;
	}

	public void setAtHolidayHour(Integer atHolidayHour) {
		this.atHolidayHour = atHolidayHour;
	}

	public Integer getAtHolidayMinute() {
		return atHolidayMinute;
	}

	public void setAtHolidayMinute(Integer atHolidayMinute) {
		this.atHolidayMinute = atHolidayMinute;
	}

	public Integer getAtHolidayTimer() {
		return atHolidayTimer;
	}

	public void setAtHolidayTimer(Integer atHolidayTimer) {
		this.atHolidayTimer = atHolidayTimer;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "ID")
	@JsonIgnore
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Transient
	public String getAtWorkTimerDict(){
		return DictCache.getDictValue(EmpConstant.DICT_AT_WORK_TIMER_TYPE, this.atWorkTimer);
	}
	
	@Transient
	public String getAtWorkSpecialStatusDict(){
		return DictCache.getDictValue(EmpConstant.DICT_AT_WORK_SPECIAL_STATUS, this.atWorkSpecialStatus);
	}
	
	@Transient
	public String getAtHolidayTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_AT_HOLIDAY_TYPE, this.atHolidayType);
	}
	
	@Transient
	public String getAtHolidayHourDict(){
		return DictCache.getDictValue(EmpConstant.DICT_HOUR_LIST_TYPE, this.atHolidayHour);
	}
	
	@Transient
	public String getAtHolidayMinuteDict(){
		return DictCache.getDictValue(EmpConstant.DICT_MINUTE_LIST_TYPE, this.atHolidayMinute);
	}
	
	@Transient
	public Object[] getGridCellDescribe(){
		String backgoundColor = "#FFFFFF";
		String describe = "";
		switch(this.isAtWork){
			case 0:
				//休假
				switch(this.atHolidayType){
					case 0://正常休假
						describe = "∥";
						backgoundColor = "#FFFF00";
						break;
					case 1://补休
						describe = "=";
						backgoundColor = "#FFCC33";
						break;
					case 2://事假
						describe = "<font size=\"4\">△</font>";
						backgoundColor = "#A8FFFF";
						break;
					case 3://病假
						describe = "<font size=\"4\">▽</font>";
						backgoundColor = "#99CC00";
						break;
					case 4:
						describe = "婚";
						backgoundColor = "#FF8080";
						break;
					case 5:
						describe = "产";
						backgoundColor = "#FF8AFF";
						break;
					case 6:
						describe = "丧";
						backgoundColor = "#CCCCCC";
						break;
					case 7:
						describe = "年";
						backgoundColor = "#00CCCC";
						break;
					case 8://工伤假
						describe = "工";
						backgoundColor = "#E7D98B";
						break;
				}
				if (this.atHolidayTimer != null){
					switch (this.atHolidayTimer){
						case 1:
							describe += "上";
							break;
						case 2:
							describe += "下";
							break;
					}
				}

				if (this.atHolidayHour != 0) {
					describe += "<br/>" + this.getAtHolidayHourDict();
				}
				if (this.atHolidayMinute != 0) {
					describe += "<br/>" + this.getAtHolidayMinuteDict();
				}
				break;
			case 1:
				//上班
				if (this.atWorkTimer != 0) {
					describe = this.getAtWorkTimerDict().substring(0, 1);
				}
				if (this.atWorkSpecialStatus != 0) {
					describe += this.getAtWorkSpecialStatusDict().substring(this.getAtWorkSpecialStatusDict().length() - 1,this.getAtWorkSpecialStatusDict().length());
				}
				break;
		}
		if (describe == "") {
			describe = "&nbsp;";
		}
		return new Object[]{backgoundColor,describe};
	}

}
