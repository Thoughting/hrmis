package com.eastcom.hrmis.modules.emp.entity;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.eastcom.baseframe.common.utils.DateUtils;
import com.google.common.collect.Maps;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * 员工考勤统计
 *
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_EMPLOYEE_CHECK_WORK_STAT_MONTH")
@DynamicInsert
@DynamicUpdate
public class EmployeeCheckWorkStatMonth extends IdEntity<EmployeeCheckWorkStatMonth> {

    /**
     *
     */
    private static final long serialVersionUID = -568790150095656422L;

    private Employee employee; //所属员工

    private String statMonth; // 统计月份
    private Double cqscDay = 0.0; // 出勤时长（天）
    private Double jjrWcqscDay = 0.0; // 法定未出勤的节假日天数
    private Double jjrJbscDay = 0.0; // 节假日加班时长（天）
    private Double xxrJbscDay = 0.0; // 休息日加班时长（天）
    private Double jblXscDay = 0.0; // 加班类总时长（天）
    private Double stypeCqscDay = 0.0; // S类型出勤时长（天）
    private Double vtypeCqscDay = 0.0; // V类型出勤时长（天）
    private Double gtypeCqscDay = 0.0; // G类型出勤时长（天）
    private Double ztypeCqscDay = 0.0; // Z类型出勤时长（天）
    private Double zcxjScDay = 0.0; // 正常休假时长（天）
    private Double bxjScDay = 0.0; // 补休假时长（天）
    private Double shjScDay = 0.0; // 事假时长（天）
    private Double bjScDay = 0.0; // 病假时长（天）
    private Double hjScDay = 0.0; // 婚假时长（天）
    private Double cjScDay = 0.0; // 产假时长（天）
    private Double sjScDay = 0.0; // 丧假时长（天）
    private Double nxjScDay = 0.0; // 年休假时长（天）
    private Double gsjScDay = 0.0; // 工伤假时长（天）
    private Integer kgCsCount = 0; // 旷工次数（次）
    private Double kgScDay = 0.0; // 旷工时长（天）
    private Integer cdCsCount = 0; // 迟到次数（次）
    private Double cdScDay = 0.0; // 迟到时长（分钟）
    private Integer ztCsCount = 0; // 早退次数（次）
    private Double ztScDay = 0.0; // 早退时长（分钟）

    public EmployeeCheckWorkStatMonth() {
    }

    public String getStatMonth() {
        return this.statMonth;
    }

    public void setStatMonth(String statMonth) {
        this.statMonth = statMonth;
    }

    public Double getCqscDay() {
        return this.cqscDay;
    }

    public void setCqscDay(Double cqscDay) {
        this.cqscDay = cqscDay;
    }

    public Double getJjrWcqscDay() {
        return jjrWcqscDay;
    }

    public void setJjrWcqscDay(Double jjrWcqscDay) {
        this.jjrWcqscDay = jjrWcqscDay;
    }

    public Double getJjrJbscDay() {
        return this.jjrJbscDay;
    }

    public void setJjrJbscDay(Double jjrJbscDay) {
        this.jjrJbscDay = jjrJbscDay;
    }

    public Double getXxrJbscDay() {
        return this.xxrJbscDay;
    }

    public void setXxrJbscDay(Double xxrJbscDay) {
        this.xxrJbscDay = xxrJbscDay;
    }

    public Double getJblXscDay() {
        return this.jblXscDay;
    }

    public void setJblXscDay(Double jblXscDay) {
        this.jblXscDay = jblXscDay;
    }

    public Double getStypeCqscDay() {
        return this.stypeCqscDay;
    }

    public void setStypeCqscDay(Double stypeCqscDay) {
        this.stypeCqscDay = stypeCqscDay;
    }

    public Double getVtypeCqscDay() {
        return this.vtypeCqscDay;
    }

    public void setVtypeCqscDay(Double vtypeCqscDay) {
        this.vtypeCqscDay = vtypeCqscDay;
    }

    public Double getGtypeCqscDay() {
        return this.gtypeCqscDay;
    }

    public void setGtypeCqscDay(Double gtypeCqscDay) {
        this.gtypeCqscDay = gtypeCqscDay;
    }

    public Double getZtypeCqscDay() {
        return this.ztypeCqscDay;
    }

    public void setZtypeCqscDay(Double ztypeCqscDay) {
        this.ztypeCqscDay = ztypeCqscDay;
    }

    public Double getZcxjScDay() {
        return this.zcxjScDay;
    }

    public void setZcxjScDay(Double zcxjScDay) {
        this.zcxjScDay = zcxjScDay;
    }

    public Double getBxjScDay() {
        return this.bxjScDay;
    }

    public void setBxjScDay(Double bxjScDay) {
        this.bxjScDay = bxjScDay;
    }

    public Double getShjScDay() {
        return this.shjScDay;
    }

    public void setShjScDay(Double shjScDay) {
        this.shjScDay = shjScDay;
    }

    public Double getBjScDay() {
        return this.bjScDay;
    }

    public void setBjScDay(Double bjScDay) {
        this.bjScDay = bjScDay;
    }

    public Double getHjScDay() {
        return this.hjScDay;
    }

    public void setHjScDay(Double hjScDay) {
        this.hjScDay = hjScDay;
    }

    public Double getCjScDay() {
        return this.cjScDay;
    }

    public void setCjScDay(Double cjScDay) {
        this.cjScDay = cjScDay;
    }

    public Double getSjScDay() {
        return this.sjScDay;
    }

    public void setSjScDay(Double sjScDay) {
        this.sjScDay = sjScDay;
    }

    public Double getNxjScDay() {
        return this.nxjScDay;
    }

    public void setNxjScDay(Double nxjScDay) {
        this.nxjScDay = nxjScDay;
    }

    public Double getGsjScDay() {
        return gsjScDay;
    }

    public void setGsjScDay(Double gsjScDay) {
        this.gsjScDay = gsjScDay;
    }

    public Integer getKgCsCount() {
        return this.kgCsCount;
    }

    public void setKgCsCount(Integer kgCsCount) {
        this.kgCsCount = kgCsCount;
    }

    public Double getKgScDay() {
        return this.kgScDay;
    }

    public void setKgScDay(Double kgScDay) {
        this.kgScDay = kgScDay;
    }

    public Integer getCdCsCount() {
        return this.cdCsCount;
    }

    public void setCdCsCount(Integer cdCsCount) {
        this.cdCsCount = cdCsCount;
    }

    public Double getCdScDay() {
        return this.cdScDay;
    }

    public void setCdScDay(Double cdScDay) {
        this.cdScDay = cdScDay;
    }

    public Integer getZtCsCount() {
        return this.ztCsCount;
    }

    public void setZtCsCount(Integer ztCsCount) {
        this.ztCsCount = ztCsCount;
    }

    public Double getZtScDay() {
        return this.ztScDay;
    }

    public void setZtScDay(Double ztScDay) {
        this.ztScDay = ztScDay;
    }

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "ID")
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * 薪假  婚假+产假+丧假
     *
     * @return
     */
    @Transient
    public Double getXinjiaScDay() {
        return hjScDay + cjScDay + sjScDay;
    }

    /**
     * 旷工统计
     *
     * @return
     */
    @Transient
    public String getKgTj() {
        return "" + this.kgScDay;
    }

    /**
     * 迟到统计
     *
     * @return
     */
    @Transient
    public String getCdTj() {
        return cdCsCount + "次" + "<br/>" + cdScDay.intValue() + "m";
    }

    /**
     * 早退统计
     *
     * @return
     */
    @Transient
    public String getZtTj() {
        return ztCsCount + "次" + "<br/>" + ztScDay.intValue() + "m";
    }

    @Transient
    public Map<String, Object> toMap() {
        Map<String, Object> result = Maps.newHashMap();
        result.put("cqscDay", this.cqscDay);// 出勤时长（天）
        result.put("jjrJbscDay", this.jjrJbscDay);// 节假日加班时长（天）
        result.put("xxrJbscDayDesc", this.getXxrJbscDayDesc()); // 休息日加班时长（天）
        result.put("jblXscDay", this.jblXscDay); // 加班类总时长（天）
        result.put("stypeCqscDay", this.stypeCqscDay); // S类型出勤时长（天）
        result.put("vtypeCqscDay", this.vtypeCqscDay); // V类型出勤时长（天）
        result.put("gtypeCqscDay", this.gtypeCqscDay); // G类型出勤时长（天）
        result.put("ztypeCqscDay", this.ztypeCqscDay); // Z类型出勤时长（天）
        result.put("zcxjScDay", this.zcxjScDay); // 正常休假时长（天）
        result.put("bxjScDay", this.bxjScDay); // 补休假时长（天）
        result.put("shjScDay", this.shjScDay); // 事假时长（天）
        result.put("bjScDay", this.bjScDay); // 病假时长（天）
        result.put("hjScDay", this.hjScDay); // 婚假时长（天）
        result.put("cjScDay", this.cjScDay); // 产假时长（天）
        result.put("sjScDay", this.sjScDay); // 丧假时长（天）
        result.put("nxjScDay", this.nxjScDay); // 年休假时长（天）
        result.put("gsjScDay", this.gsjScDay); // 工伤假时长（天）
        result.put("kgCsCount", this.kgCsCount); // 旷工次数（次）
        result.put("kgScDay", this.kgScDay); // 旷工时长（天）
        result.put("cdCsCount", this.cdCsCount); // 迟到次数（次）
        result.put("cdScDay", this.cdScDay); // 迟到时长（天）
        result.put("ztCsCount", this.ztCsCount); // 早退次数（次）
        result.put("ztScDay", this.ztScDay); // 早退时长（天）
        result.put("kgTj", this.getKgTj());
        result.put("cdTj", this.getCdTj());
        result.put("ztTj", this.getZtTj());
        result.put("xinjiaScDay", this.getXinjiaScDay());
        return result;
    }

    /**
     * 得到当前统计月份全勤天数
     * 如果统计月份为当前月份，则为截止到当天的全勤天数
     *
     * @param statMonth
     * @return
     */
    @SuppressWarnings("deprecation")
    @Transient
    public static int getMaxCqscDayFromMonth(String statMonth) throws Exception {
        Date current = DateUtils.parseDate(statMonth, "yyyy年-MM月");
        Date today = new Date();
        if (current.getYear() == today.getYear() && current.getMonth() == today.getMonth()) {
            return (int) DateUtils.getDistanceOfTwoDate(current, today) + 1;
        } else {
            return DateUtils.getMonthMaxDay(current);
        }
    }

    /**
     * 得到当前统计月份星期六天数
     * 如果统计月份为当前月份，则为截止到当天的星期六天数
     *
     * @param statMonth
     * @return
     */
    @SuppressWarnings("deprecation")
    @Transient
    public static int getMaxSundayCountFromMonth(String statMonth) throws Exception {
        Date current = DateUtils.parseDate(statMonth, "yyyy年-MM月");
        Date today = new Date();
        int total = 0;
        if (current.getYear() == today.getYear() && current.getMonth() == today.getMonth()) {
            total = (int) DateUtils.getDistanceOfTwoDate(current, today) + 1;
        } else {
            total = DateUtils.getMonthMaxDay(current);
        }
        int count = 0;
        for (int i = 1; i <= total; i++) {
            current.setDate(i);
            if ("星期日".equals(DateUtils.getWeekOfDate(current))) {
                count++;
            }
        }
        return count;
    }

    /**
     * 得到计薪日（当前月份扣减双休日）
     *
     * @param date
     * @return
     */
    @SuppressWarnings("deprecation")
    @Transient
    public static int getWageDays(Date date) {
        int wageDays = 0;
        int maxDays = DateUtils.getMonthMaxDay(date);
        for (int i = 1; i <= maxDays; i++) {
            date.setDate(i);
            if (!"星期日".equals(DateUtils.getWeekOfDate(date)) && !"星期六".equals(DateUtils.getWeekOfDate(date))) {
                wageDays++;
            }
        }
        return wageDays;
    }

    /**
     * 当月正常出勤天数（重要）--员工工资计算经常会使用到该值
     * <p>
     * 出勤天数-休息日加班天数+法定带薪未出勤天数（包括节假日、补休、年休、工伤假、婚假、产假、护理假、丧家、病假、等）
     *
     * @return
     */
    @Transient
    public double getNormalCqscDay() {
        //加班计算方式 1：正常计算，2：含年假计算，3：按小时计算，4：不计加班
        if (employee.getOverTimeRate() == 3) {
            return this.getCqscDay() - xxrJbscDay / employee.getEmployeeDept().getWorkTimer() + (jjrWcqscDay + bxjScDay + nxjScDay + gsjScDay + hjScDay + cjScDay + sjScDay + bjScDay);
        }
        return this.getCqscDay() - xxrJbscDay + (jjrWcqscDay + bxjScDay + nxjScDay + gsjScDay + hjScDay + cjScDay + sjScDay + bjScDay);
    }

    @Transient
    public String getXxrJbscDayDesc() {
        //加班计算方式 1：正常计算，2：含年假计算，3：按小时计算，4：不计加班
        if (employee.getOverTimeRate() == 3) {
            return this.xxrJbscDay + "h";
        }
        return this.xxrJbscDay + "";
    }
}
