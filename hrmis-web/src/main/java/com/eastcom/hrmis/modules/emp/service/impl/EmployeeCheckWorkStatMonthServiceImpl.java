package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.hrmis.modules.emp.cache.LegalHolidayCache;
import com.eastcom.hrmis.modules.emp.dao.EmployeeCheckWorkDao;
import com.eastcom.hrmis.modules.emp.dao.EmployeeCheckWorkStatMonthDao;
import com.eastcom.hrmis.modules.emp.dao.EmployeeDao;
import com.eastcom.hrmis.modules.emp.entity.Employee;
import com.eastcom.hrmis.modules.emp.entity.EmployeeCheckWork;
import com.eastcom.hrmis.modules.emp.entity.EmployeeCheckWorkStatMonth;
import com.eastcom.hrmis.modules.emp.entity.EmployeeDept;
import com.eastcom.hrmis.modules.emp.service.EmployeeCheckWorkStatMonthService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeCheckWorkStatMonthServiceImpl extends CrudServiceSupport<EmployeeCheckWorkStatMonthDao, EmployeeCheckWorkStatMonth> implements EmployeeCheckWorkStatMonthService {

	@Autowired
	private EmployeeCheckWorkDao employeeCheckWorkDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Override
	public void refreshCheckWorkStatMonth(String statMonth) throws Exception {
		Map<String, Object> reqParams = Maps.newHashMap();
		reqParams.put("auditStatus", 2);
		List<Employee> employees = employeeDao.find(reqParams);
		if (CollectionUtils.isNotEmpty(employees)) {
			for (Employee employee : employees) {
				refreshCheckWorkStatMonth(employee, statMonth);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public EmployeeCheckWorkStatMonth refreshCheckWorkStatMonth(Employee employee, String statMonth) throws Exception {
		//参数
		DecimalFormat decimalFormat = new DecimalFormat("#.000");
		EmployeeDept employeeDept = employee.getEmployeeDept();
		double deptMinute = employeeDept.getWorkTimer() * 60;
		double cqscDay = EmployeeCheckWorkStatMonth.getMaxCqscDayFromMonth(statMonth) * deptMinute; // 出勤最大时长
		//double jjrJbscDay = LegalHolidayCache.getLegalHolidaysByMonth(statMonth).size() * deptMinute; // 节假日加班时长
		double jjrJbscDay = LegalHolidayCache.getLegalHolidaysByDate(new Date()).size() * deptMinute; // 节假日加班时长
		double sundayJbscDay = EmployeeCheckWorkStatMonth.getMaxSundayCountFromMonth(statMonth) * deptMinute; // 星期六加班时长
		
		//入职时间与离职时间会影响出勤最大时长以及节假日加班时长
		Date statDate = DateUtils.parseDate(statMonth, "yyyy年-MM月");
		if (employee.getEnrtyDate() != null && employee.getEnrtyDate().getYear() == statDate.getYear() && employee.getEnrtyDate().getMonth() == statDate.getMonth()) {
			//入职日期与统计月份为同一个月的时候，需要注意
			double enrtyOther = DateUtils.getDistanceOfTwoDate(statDate, employee.getEnrtyDate());
			cqscDay -= enrtyOther * deptMinute;
			jjrJbscDay -= LegalHolidayCache.getCurrentMonthExcludeLegalHolidaysByEntryDate(employee.getEnrtyDate()).size() * deptMinute;
		}
		if (employee.getQuitCompanyDate() != null && employee.getQuitCompanyDate().getYear() == statDate.getYear() && employee.getQuitCompanyDate().getMonth() == statDate.getMonth()
				&& EmployeeCheckWorkStatMonth.getMaxCqscDayFromMonth(statMonth) >= employee.getQuitCompanyDate().getDate()) {
			//离职日期与统计月份为同一个月,并且当前日期已经超过了离职日期
			cqscDay -= (EmployeeCheckWorkStatMonth.getMaxCqscDayFromMonth(statMonth) - employee.getQuitCompanyDate().getDate() + 1) * deptMinute;
			jjrJbscDay -= LegalHolidayCache.getCurrentMonthExcludeLegalHolidaysByQuitCompanyDate(employee.getQuitCompanyDate()).size() * deptMinute;
		}
		
		int cdCsCount = 0; // 迟到次数
		double cdScDay = 0; // 迟到时长
		int ztCsCount = 0; // 早退次数
		double ztScDay = 0; // 早退时长
		int kgCsCount = 0; // 旷工次数
		double kgScDay = 0; // 旷工时长
		double jblXscDay = 0; // 加班总时长
		double stypeCqscDay = 0; // S类型出勤时长
		double vtypeCqscDay = 0; // V类型出勤时长
		double gtypeCqscDay = 0; // G类型出勤时长
		double ztypeCqscDay = 0; // Z类型出勤时长
		double zcxjScDay = 0; // 正常休假时长
		double bxjScDay = 0; // 补休假时长
		double shjScDay = 0; // 事假时长
		double bjScDay = 0; // 病假时长
		double hjScDay = 0; // 婚假时长
		double cjScDay = 0; // 产假时长
		double sjScDay = 0; // 丧假时长
		double nxjScDay = 0; // 年休假时长
		double gsjScDay = 0; // 工伤假时长
		
		//得到统计记录
		EmployeeCheckWorkStatMonth employeeCheckWorkStatMonth = findByEmployeeIdAndStatMonth(employee.getId(), statMonth);
		if (employeeCheckWorkStatMonth == null) {
			employeeCheckWorkStatMonth = new EmployeeCheckWorkStatMonth();
			employeeCheckWorkStatMonth.setEmployee(employee);
			employeeCheckWorkStatMonth.setStatMonth(statMonth);
		}
		//得到考勤记录
		List<EmployeeCheckWork> employeeCheckWorks = employeeCheckWorkDao.getCheckWorkMonthDate(employee.getId(), DateUtils.parseDate(statMonth, "yyyy年-MM月"));
		if (CollectionUtils.isNotEmpty(employeeCheckWorks)) {
			double temp = 0;
			for (EmployeeCheckWork employeeCheckWork : employeeCheckWorks) {
				switch(employeeCheckWork.getIsAtWork()){
					case 0:
						//休假
						temp = employeeCheckWork.getAtHolidayHour() * 60 + employeeCheckWork.getAtHolidayMinute();
						if (employeeCheckWork.getAtHolidayHour() == 0 || temp > deptMinute) {
							//请假小时数为0或者请假时间大于部门上班时间，默认一天
							temp = deptMinute;
						}
						
						cqscDay -= temp;
						
						if (LegalHolidayCache.isLegalHoliday(employeeCheckWork.getWorkDate())) {
							jjrJbscDay -= temp;
						}
						
						if ("星期日".equals(DateUtils.getWeekOfDate(employeeCheckWork.getWorkDate()))) {
							sundayJbscDay -= temp;
						}
						
						switch(employeeCheckWork.getAtHolidayType()){
							case 0://正常休假
								zcxjScDay += temp;
								break;
							case 1://补休
								bxjScDay += temp;
								break;
							case 2://事假
								shjScDay += temp;
								break;
							case 3://病假
								bjScDay += temp;
								break;
							case 4://婚假
								hjScDay += temp;
								break;
							case 5://产假
								cjScDay += temp;
								break;
							case 6://丧假
								sjScDay += temp;
								break;
							case 7://年休假
								nxjScDay += temp;
								break;
							case 8://工伤假
								gsjScDay += temp;
								break;
						}
						break;
					case 1:
						//上班
						switch(employeeCheckWork.getAtWorkStatus()){
							case 1:
								//迟到
								
								cdCsCount++;
								cdScDay += employeeCheckWork.getAtWorkStatusMinute();
								
								/*cqscDay -= employeeCheckWork.getAtWorkStatusMinute();
								
								if (LegalHolidayCache.isLegalHoliday(employeeCheckWork.getWorkDate())) {
									jjrJbscDay -= employeeCheckWork.getAtWorkStatusMinute();
								}*/
								break;
							case 2:
								//早退
								cqscDay--;
								
								ztCsCount++;
								ztScDay += employeeCheckWork.getAtWorkStatusMinute();
								
								/*cqscDay -= employeeCheckWork.getAtWorkStatusMinute();
										
								if (LegalHolidayCache.isLegalHoliday(employeeCheckWork.getWorkDate())) {
									jjrJbscDay -= employeeCheckWork.getAtWorkStatusMinute();
								}*/
								break;
						}
						//上班特殊类型统计
						switch(employeeCheckWork.getAtWorkSpecialStatus()){
							case 1:
								// V类型
								vtypeCqscDay += deptMinute;
								break;
							case 2:
								// G类型
								gtypeCqscDay += deptMinute;
								break;
							case 3:
								// Z类型
								ztypeCqscDay += deptMinute;
								break;
							case 4:
								// S类型
								stypeCqscDay += deptMinute;
								break;
						}
						break;
					case 2:
						//旷工
						temp = employeeCheckWork.getOutWorkHour() * 60 + employeeCheckWork.getOutWorkMinute();
						if (employeeCheckWork.getOutWorkHour() == 0 || temp > deptMinute) {
							//旷工时间大于部门上班时间，默认一天
							temp = deptMinute;
						}
						
						kgCsCount++;
						kgScDay += temp;
						
						cqscDay -= temp;
						
						if (LegalHolidayCache.isLegalHoliday(employeeCheckWork.getWorkDate())) {
							jjrJbscDay -= temp;
						}
						
						if ("星期日".equals(DateUtils.getWeekOfDate(employeeCheckWork.getWorkDate()))) {
							sundayJbscDay -= temp;
						}
						
						break;
				}
				
				//加班总时长
				if (employeeCheckWork.getHasOverTime() == 1) {
					temp = employeeCheckWork.getOverTimeHour() * 60 + employeeCheckWork.getOverTimeMinute();
					jblXscDay += temp;
					
					if (LegalHolidayCache.isLegalHoliday(employeeCheckWork.getWorkDate())) {
						jjrJbscDay += temp;
					}
					
					if ("星期日".equals(DateUtils.getWeekOfDate(employeeCheckWork.getWorkDate()))) {
						sundayJbscDay += temp;
					}
				}
			}
		}
		employeeCheckWorkStatMonth.setCqscDay(Double.parseDouble(decimalFormat.format(cqscDay / deptMinute)));
		employeeCheckWorkStatMonth.setJjrJbscDay(Double.parseDouble(decimalFormat.format(jjrJbscDay / deptMinute)));
		employeeCheckWorkStatMonth.setJblXscDay(Double.parseDouble(decimalFormat.format(jblXscDay / deptMinute)));
		employeeCheckWorkStatMonth.setStypeCqscDay(stypeCqscDay / deptMinute);
		employeeCheckWorkStatMonth.setVtypeCqscDay(vtypeCqscDay / deptMinute);
		employeeCheckWorkStatMonth.setGtypeCqscDay(gtypeCqscDay / deptMinute);
		employeeCheckWorkStatMonth.setZtypeCqscDay(ztypeCqscDay / deptMinute);
		employeeCheckWorkStatMonth.setZcxjScDay(Double.parseDouble(decimalFormat.format(zcxjScDay / deptMinute)));
		employeeCheckWorkStatMonth.setBxjScDay(Double.parseDouble(decimalFormat.format(bxjScDay / deptMinute)));
		employeeCheckWorkStatMonth.setShjScDay(Double.parseDouble(decimalFormat.format(shjScDay / deptMinute)));
		employeeCheckWorkStatMonth.setBjScDay(Double.parseDouble(decimalFormat.format(bjScDay / deptMinute)));
		employeeCheckWorkStatMonth.setHjScDay(Double.parseDouble(decimalFormat.format(hjScDay / deptMinute)));
		employeeCheckWorkStatMonth.setCjScDay(Double.parseDouble(decimalFormat.format(cjScDay / deptMinute)));
		employeeCheckWorkStatMonth.setSjScDay(Double.parseDouble(decimalFormat.format(sjScDay / deptMinute)));
		employeeCheckWorkStatMonth.setNxjScDay(Double.parseDouble(decimalFormat.format(nxjScDay / deptMinute)));
		employeeCheckWorkStatMonth.setGsjScDay(Double.parseDouble(decimalFormat.format(gsjScDay / deptMinute)));
		employeeCheckWorkStatMonth.setKgCsCount(kgCsCount);
		employeeCheckWorkStatMonth.setKgScDay(Double.parseDouble(decimalFormat.format(kgScDay / deptMinute)));
		employeeCheckWorkStatMonth.setCdCsCount(cdCsCount);
		employeeCheckWorkStatMonth.setCdScDay(cdScDay);
		employeeCheckWorkStatMonth.setZtCsCount(ztCsCount);
		employeeCheckWorkStatMonth.setZtScDay(ztScDay);
		
		// 法定未出勤的节假日天数
		double jjrWcqscDay = LegalHolidayCache.getLegalHolidaysByMonth(statMonth).size() - employeeCheckWorkStatMonth.getJjrJbscDay();
		if (employee.getEnrtyDate() != null && employee.getEnrtyDate().getYear() == statDate.getYear() && employee.getEnrtyDate().getMonth() == statDate.getMonth()) {
			jjrWcqscDay -= LegalHolidayCache.getCurrentMonthExcludeLegalHolidaysByEntryDate(employee.getEnrtyDate()).size() * deptMinute;
		}
		if (employee.getQuitCompanyDate() != null && employee.getQuitCompanyDate().getYear() == statDate.getYear() && employee.getQuitCompanyDate().getMonth() == statDate.getMonth()
				&& EmployeeCheckWorkStatMonth.getMaxCqscDayFromMonth(statMonth) >= employee.getQuitCompanyDate().getDate()) {
			jjrWcqscDay -= LegalHolidayCache.getCurrentMonthExcludeLegalHolidaysByQuitCompanyDate(employee.getQuitCompanyDate()).size() * deptMinute;
		}
		if (jjrWcqscDay <= 0) {
			jjrWcqscDay = 0;
		}
		employeeCheckWorkStatMonth.setJjrWcqscDay(jjrWcqscDay);
		
		//休息日加班天数
		double xxrJbscDay = 0.0; // 休息日加班时长（天） 
		double xxrJbscDay_z = 0.0; // 整数位
		double xxrJbscDay_y = 0.0; // 余数位
		
		//加班计算方式 1：正常计算，2：含年假计算，3：按小时计算，4：不计加班
		switch (employee.getOverTimeRate()) {
			case 1:
				// （出勤天数+法定未出勤的节假日天数+补休天数）/6天
				xxrJbscDay_z = Math.floor((employeeCheckWorkStatMonth.getCqscDay() + jjrWcqscDay + employeeCheckWorkStatMonth.getBxjScDay()) / 6);
				xxrJbscDay_y = (employeeCheckWorkStatMonth.getCqscDay() + jjrWcqscDay + employeeCheckWorkStatMonth.getBxjScDay()) % 6 >= 5.5 ? 0.5 : 0;
				xxrJbscDay = (xxrJbscDay_z + xxrJbscDay_y) >= 4 ? 4 : (xxrJbscDay_z + xxrJbscDay_y);
				break;
			case 2:
				// （出勤天数+法定未出勤的节假日天数+补休天数+年休天数）/6天
				xxrJbscDay_z = Math.floor((employeeCheckWorkStatMonth.getCqscDay() + jjrWcqscDay + employeeCheckWorkStatMonth.getBxjScDay() + employeeCheckWorkStatMonth.getNxjScDay()) / 6);
				xxrJbscDay_y = (employeeCheckWorkStatMonth.getCqscDay() + jjrWcqscDay + employeeCheckWorkStatMonth.getBxjScDay() + employeeCheckWorkStatMonth.getNxjScDay()) % 6 >= 5.5 ? 0.5 : 0;
				xxrJbscDay = (xxrJbscDay_z + xxrJbscDay_y) >= 4 ? 4 : (xxrJbscDay_z + xxrJbscDay_y);
				break;
			case 3:
				//单位小时
				xxrJbscDay_z = Math.floor( employeeCheckWorkStatMonth.getCqscDay() / 6 );
				xxrJbscDay_y = employeeCheckWorkStatMonth.getCqscDay() % 6 <= 5 ? 0 : 1;
				xxrJbscDay = ( xxrJbscDay_z + xxrJbscDay_y ) * 2;
				break;
			case 4:
				//交易会是不算休息日加班费的
				xxrJbscDay = 0.0;
				break;
			case 5:
				//按星期六加班
				xxrJbscDay = Double.parseDouble(decimalFormat.format(sundayJbscDay / deptMinute));
				break;
		}
		employeeCheckWorkStatMonth.setXxrJbscDay(xxrJbscDay);
		dao.saveOrUpdate(employeeCheckWorkStatMonth);
		return employeeCheckWorkStatMonth;
	}

	@Override
	@Transactional(readOnly = true)
	public EmployeeCheckWorkStatMonth findByEmployeeIdAndStatMonth(
			String employeeId, String statMonth) {
		return dao.findByEmployeeIdAndStatMonth(employeeId, statMonth);
	}

	@Override
	public void deleteByDeptId(String deptId) {
		String sqlString = " delete from t_employee_check_work_stat_month where employee_id in ( "
					+" select t1.id from t_employee t1,t_employee_dept t2 "
					+" where t1.employee_dept_id = t2.id and t2.id = ?) ";
		dao.executeBySql(sqlString, new Object[]{deptId});
	}

	@Override
	public void deleteByEmployeeId(String employeeId) {
		String sqlString = " delete from t_employee_check_work_stat_month where employee_id = ? ";
		dao.executeBySql(sqlString, new Object[]{employeeId});
	}

	@Override
	public List<Map<String, Object>> getEmployeeCheckWorkStatByDeptIdAndStatMonth(
			String deptId, String statMonth) {
		String sqlString = " select t1.NAME,t2.* from ("
				+ " select * from t_employee where AUDIT_STATUS = 2 and EMPLOYEE_DEPT_ID = ?"
				+ " ) t1"
				+ " left join ( select * from t_employee_check_work_stat_month where STAT_MONTH = ?) t2"
				+ " on t1.ID = t2.EMPLOYEE_ID ";
		List<Map<String, Object>> result = dao.findBySql(sqlString, new Object[]{deptId,statMonth},Map.class);
		return result;
	}

	@Override
	public List<Map<String, Object>> getDeptCheckWorkStatByStatMonth(String statMonth) {
		String sqlString = " select s1.ID,s1.NAME,s1.count as EMP_COUNT,s2.* from ( "
				+ " select t1.ID,t1.NAME,t2.count from t_employee_dept t1"
				+ " left join ("
				+ "     select EMPLOYEE_DEPT_ID,count(*) as COUNT from t_employee where AUDIT_STATUS = 2 group by EMPLOYEE_DEPT_ID"
				+ " ) t2"
				+ "   on t1.ID = t2.EMPLOYEE_DEPT_ID"
				+ " ) s1"
				+ " left join ("
				+ "    select t1.EMPLOYEE_DEPT_ID,t2.STAT_MONTH,sum(t2.CD_CS_COUNT) as CD_CS_COUNT,sum(t2.ZT_CS_COUNT) as ZT_CS_COUNT,sum(t2.KG_SC_DAY) as KG_SC_DAY,sum(t2.JBL_XSC_DAY) as JBL_XSC_DAY,sum(t2.ZCXJ_SC_DAY) as ZCXJ_SC_DAY,sum(t2.BXJ_SC_DAY) as BXJ_SC_DAY,sum(t2.NXJ_SC_DAY) as NXJ_SC_DAY,sum(t2.GSJ_SC_DAY) as GSJ_SC_DAY,sum(t2.SHJ_SC_DAY) as SHJ_SC_DAY,sum(t2.HJ_SC_DAY) as HJ_SC_DAY,sum(t2.BJ_SC_DAY) as BJ_SC_DAY,sum(t2.CJ_SC_DAY) as CJ_SC_DAY,sum(t2.SJ_SC_DAY) as SJ_SC_DAY from ("
				+ "        select * from t_employee where AUDIT_STATUS = 2"
				+ "   ) t1"
				+ "   left join ( select * from t_employee_check_work_stat_month where STAT_MONTH = ?) t2"
				+ "   on t1.ID = t2.EMPLOYEE_ID"
				+ "   group by t1.EMPLOYEE_DEPT_ID,t2.STAT_MONTH ) s2"
				+ " on s1.ID = s2.EMPLOYEE_DEPT_ID";
		List<Map<String, Object>> result = dao.findBySql(sqlString, new Object[]{statMonth},Map.class);
		return result;
	}

	@Override
	public EmployeeCheckWorkStatMonth findByEmployeeIdAndStatMonth(
			String employeeId, Date statDate) {
		return dao.findByEmployeeIdAndStatMonth(employeeId, statDate);
	}

}
