package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.hrmis.modules.emp.cache.WageCountItemCache;
import com.eastcom.hrmis.modules.emp.dao.EmployeeWageActualItemDao;
import com.eastcom.hrmis.modules.emp.dao.EmployeeWageDao;
import com.eastcom.hrmis.modules.emp.entity.*;
import com.eastcom.hrmis.modules.emp.service.EmployeeCheckWorkStatMonthService;
import com.eastcom.hrmis.modules.emp.service.EmployeeWageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeWageServiceImpl extends CrudServiceSupport<EmployeeWageDao, EmployeeWage> implements EmployeeWageService {

	@Autowired
	private EmployeeWageActualItemDao actualItemDao;
	
	@Autowired
	private EmployeeCheckWorkStatMonthService checkWorkStatMonthService;
	
	@SuppressWarnings("deprecation")
	@Override
	public EmployeeWage realStatEmployeeWageInfo(EmployeeWage employeeWage, Map<String, Object> itemMap) throws Exception {
		if (employeeWage != null && employeeWage.getAuditStatus() != 2 && employeeWage.getEmployee() != null) {
			DecimalFormat decimalFormat = new DecimalFormat("#.00");
			
			//记薪日 当月扣减双休日
			int wageDays =  EmployeeCheckWorkStatMonth.getWageDays(DateUtils.parseDate(employeeWage.getWageDateStr(), "yyyy年-MM月"));
			
			//当前员工
			Employee employee = employeeWage.getEmployee();
			
			//得到员工考勤统计
			EmployeeCheckWorkStatMonth employeeCheckWorkStatMonth = checkWorkStatMonthService.findByEmployeeIdAndStatMonth(employeeWage.getEmployee().getId(), employeeWage.getWageDateStr());
			if (employeeCheckWorkStatMonth == null) {
				employeeCheckWorkStatMonth = checkWorkStatMonthService.refreshCheckWorkStatMonth(employee, employeeWage.getWageDateStr());
			}
			
			//扣减字段
			double deductionTotal = 0;
			String deductionCode = "102社保（元/月）,103公积金（元/月）,104收费返还金（户）,105工会费（元/月）,106住宿费（元/月）,107水电费（元/月）,108赔偿/其他（元/月）";
			//其他字段
			String otherCode = "019收入总额,020扣减总额,021实发总额,0211现金发放（元/月）,0212银行发放（元/月）";
			//收入总额
			double incomeTotal = 0;
			//0211现金发放（元/月）
			double xianjinff = 0;
			//102社保
			double shebao = 0;
			//103公积金
			double gongjijin = 0;
			//缺勤工资
			double queqingz = 0;
			
			for (Map.Entry<String, Object> entry : itemMap.entrySet()) {  
				String key = entry.getKey();
				double value = (double) entry.getValue();
				boolean calculation = true;
				WageCountItem wageCountItem = WageCountItemCache.getByCode(key);
				if (wageCountItem != null) {
					EmployeeWageActualItem actualItem = actualItemDao.getWageActualItem(employeeWage, wageCountItem, key);
					if ("001".equals(key)) {
						//正常工作时间工资（元/月）  001
						double temp = value / wageDays * employeeCheckWorkStatMonth.getNormalCqscDay();//正常工作时间工资÷当月计薪日（当月扣减双休日）×当月正常出勤天数
						value = temp > value ? value : temp; // 计算出来的“正常工作时间工资”不能超过设置“正常工作时间工资”
					} else if ("002".equals(key)) {
						//环卫津贴（元/天） 002
						value = value * employeeCheckWorkStatMonth.getCqscDay() >= 338 ? 338 : value * employeeCheckWorkStatMonth.getCqscDay();
					} else if ("023".equals(key)) {
						//环卫津贴（元/月） 023
						double temp = value / wageDays * employeeCheckWorkStatMonth.getNormalCqscDay();//正常工作时间工资÷当月计薪日（当月扣减双休日）×当月正常出勤天数
						value = temp > value ? value : temp; // 计算出来的不能超过设置值
					} else if ("003".equals(key)) {
						//高温津贴（元/天） 003  每年6～10月才有发放
						if (employeeWage.getWageDateStr().indexOf("06月") != -1 || employeeWage.getWageDateStr().indexOf("07月") != -1 || employeeWage.getWageDateStr().indexOf("08月") != -1 || employeeWage.getWageDateStr().indexOf("09月") != -1 || employeeWage.getWageDateStr().indexOf("10月") != -1) {
							value = value * employeeCheckWorkStatMonth.getCqscDay() >= 150 ? 150 : value * employeeCheckWorkStatMonth.getCqscDay();
						} else {
							value = 0;
						}
					} else if ("024".equals(key)) {
						//岗位津贴（元/天） 024  计算公式同环卫津贴(元/天)
						value = value * employeeCheckWorkStatMonth.getCqscDay() >= 338 ? 338 : value * employeeCheckWorkStatMonth.getCqscDay();
					} else if ("0041".equals(key)) {
						// 休息日（元/天）0041
						value = value * employeeCheckWorkStatMonth.getXxrJbscDay();
					} else if ("0042".equals(key)) {
						//节日（元/天）
						value = value * employeeCheckWorkStatMonth.getJjrJbscDay();
					}else if ("0045".equals(key)) {
						//年休假（元/天）
						value = value * 5 / 12;
					}else if ("0046".equals(key)) {
						//一环综合加班（元/小时）
						value = value * employeeCheckWorkStatMonth.getXxrJbscDay();
					}else if ("0047".equals(key) && employee.getEmployeeDept() != null) {
						//一环节日（元/小时）
						value = value * employeeCheckWorkStatMonth.getJjrJbscDay() * employee.getEmployeeDept().getWorkTimer();
					}else if ("005".equals(key)) {
						//绩效奖金（元/月）
						value = value / wageDays * employeeCheckWorkStatMonth.getCqscDay();
					}else if ("006".equals(key)) {
						//驻外交通补贴（元/月）
						value = value / wageDays * employeeCheckWorkStatMonth.getCqscDay();
					}else if ("007".equals(key)) {
						//职务补贴（元/月）
						value = value / wageDays * employeeCheckWorkStatMonth.getCqscDay();
					}else if ("008".equals(key)) {
						//通讯补贴（元/月）
						value = value / wageDays * employeeCheckWorkStatMonth.getCqscDay();
					}else if ("009".equals(key)) {
						//工龄补贴（元/月）
						//次月起给予补贴30元/月，逐年递增30元/年，最高150元封顶。
						//入职时间
						Date today = new Date();
						Date enrtyDate = employee.getEnrtyDate();
						if (today.getYear() == enrtyDate.getYear() && today.getMonth() == enrtyDate.getMonth()) {
							//入职不满一个月，不发放
							value = 0;
						} else {
							value = value * (employee.getWorkYear() + 1) >= 150 ? 150 : value * (employee.getWorkYear() + 1);
						}
					}else if ("010".equals(key)) {
						//伙食补贴（元/月）
						value = value / wageDays * employeeCheckWorkStatMonth.getCqscDay();
					}else if ("011".equals(key)) {
						//特殊岗位补贴（元/月）
						value = value / wageDays * employeeCheckWorkStatMonth.getCqscDay();
					}else if ("104".equals(key)) {
						//收费返还金（户）
						value = value * 2.4;
					}else if ("105".equals(key)) {
						//工会费（元/月）
						//1.在职的，无论当月出勤是否满勤（即无论是否请假，都要扣
						//2.对于当月入职的需当月首日入职方可扣减；
						//3.对于离职需当月末日离职的方可扣减，中途离职的不扣减。
						//4.扣减不折算，或扣或不扣。
						Date today = new Date();
						Date enrtyDate = employee.getEnrtyDate();
						//非当月1号入职，不扣减工会费
						if (today.getYear() == enrtyDate.getYear() && today.getMonth() == enrtyDate.getMonth()) {
							if (enrtyDate.getDate() != 1) {
								value = 0;
							}
						}
						Date quitCompanyDate = employee.getQuitCompanyDate();
						//非当月末日离职，不扣减工会费
						if (employee.getHasQuitCompany() == 1 && today.getYear() == quitCompanyDate.getYear() && today.getMonth() == quitCompanyDate.getMonth()) {
							if (quitCompanyDate.getDate() != DateUtils.getMonthMaxDay(today)) {
								value = 0;
							}
						}
					}else {
						calculation = false;
					}
					
					if (calculation) {
						actualItem.setCount(Double.parseDouble(decimalFormat.format(value)));
						actualItemDao.saveOrUpdate(actualItem);
					}

					if (actualItem != null && actualItem.getCount() != null) {
						value = actualItem.getCount();
					}
					
					if ("0211".equals(key)) {
						//现金发放
						xianjinff = value;
					}
					if ("102".equals(key)) {
						//社保
						shebao = value;
					}
					if ("103".equals(key)) {
						//公积金
						gongjijin = value;
					}
					
					//扣减总额
					if (deductionCode.indexOf(key) != -1) {
						deductionTotal += value;
					} else if (otherCode.indexOf(key) == -1){
						//不是扣减总额字段也不是其他字段，则是需要进行收入总额计算的字段
						incomeTotal += value;
					}
				}
			}
			
			//收入总额 019
			WageCountItem wageCountItem = WageCountItemCache.getByCode("019");
			EmployeeWageActualItem actualItem = actualItemDao.getWageActualItem(employeeWage, wageCountItem, "019");
			actualItem.setCount(Double.parseDouble(decimalFormat.format(incomeTotal)));
			actualItemDao.saveOrUpdate(actualItem);
			
			//个税101
			//缴税基数=收入总额-代扣社保-公积金
			double minSui = 0;
			double gjjBase = (incomeTotal - shebao - gongjijin - queqingz) / 100 - 35;
			Double[] arr = new Double[]{0.6 * gjjBase - 0,2.0 * gjjBase - 21,4.0 * gjjBase - 111,5.0 * gjjBase - 201,6.0 * gjjBase - 551,7.0 * gjjBase - 1101,9.0 * gjjBase - 2701};
			for (Double d : arr) {
				if (minSui < d) {
					minSui = d;
				}
			}
			minSui = Double.parseDouble(decimalFormat.format(minSui * 5));
			wageCountItem = WageCountItemCache.getByCode("101");
			actualItem = actualItemDao.getWageActualItem(employeeWage, wageCountItem, "101");
			actualItem.setCount(minSui);
			actualItemDao.saveOrUpdate(actualItem);
			
			//扣减总额020 须加上上面算出的个税
			deductionTotal = deductionTotal + minSui;
			wageCountItem = WageCountItemCache.getByCode("020");
			actualItem = actualItemDao.getWageActualItem(employeeWage, wageCountItem, "020");
			actualItem.setCount(Double.parseDouble(decimalFormat.format(deductionTotal)));
			actualItemDao.saveOrUpdate(actualItem);
			
			//银行发放0212
			wageCountItem = WageCountItemCache.getByCode("0212");
			actualItem = actualItemDao.getWageActualItem(employeeWage, wageCountItem, "0212");
			actualItem.setCount(Double.parseDouble(decimalFormat.format(incomeTotal - deductionTotal - xianjinff)));
			actualItemDao.saveOrUpdate(actualItem);
			
		}
		return get(employeeWage.getId());
	}

}
