package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeDao;
import com.eastcom.hrmis.modules.emp.dao.EmployeeWageItemDao;
import com.eastcom.hrmis.modules.emp.dao.WageCountItemDao;
import com.eastcom.hrmis.modules.emp.dao.WageTemplateDao;
import com.eastcom.hrmis.modules.emp.entity.*;
import com.eastcom.hrmis.modules.emp.service.EmployeeWageItemService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeWageItemServiceImpl extends CrudServiceSupport<EmployeeWageItemDao, EmployeeWageItem> implements EmployeeWageItemService{

	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private WageTemplateDao wageTemplateDao;
	
	@Autowired
	private WageCountItemDao wageCountItemDao;
	
	@Override
	public void updateEffect(EmployeeWageItem wageItem) {
		// 正常工作时间工资（元/月）
		if (wageItem.getWageCountItem().getCode().equals("001")) {
			
			DecimalFormat decimalFormat = new DecimalFormat("#.00");
			
			//正常工作时间工资 影响 休息日（0041），综合加班（0046），节日（0042）
			//休息日（0041）
			WageCountItem wageCountItem = wageCountItemDao.getUnique("code", "0041");
			if (wageCountItem != null) {
				Map<String, Object> reqParam = Maps.newHashMap();
				reqParam.put("employeeId", wageItem.getEmployee().getId());
				reqParam.put("wageCountItemCode", "0041");
				EmployeeWageItem temp = dao.getUnique(reqParam);
				if (temp == null) {
					temp = new EmployeeWageItem();
					temp.setEmployee(wageItem.getEmployee());
					temp.setWageCountItem(wageCountItem);
				}
				temp.setCount(Double.parseDouble(decimalFormat.format(wageItem.getCount() / 21.75 * 2)));
				dao.saveOrUpdate(temp);
			}
			//一环综合加班（0046）
			wageCountItem = wageCountItemDao.getUnique("code", "0046");
			if (wageCountItem != null) {
				Map<String, Object> reqParam = Maps.newHashMap();
				reqParam.put("employeeId", wageItem.getEmployee().getId());
				reqParam.put("wageCountItemCode", "0046");
				EmployeeWageItem temp = dao.getUnique(reqParam);
				if (temp == null) {
					temp = new EmployeeWageItem();
					temp.setEmployee(wageItem.getEmployee());
					temp.setWageCountItem(wageCountItem);
				}
				temp.setCount(Double.parseDouble(decimalFormat.format(wageItem.getCount() / 21.75 / 8 * 1.5)));
				dao.saveOrUpdate(temp);
			}
			// 节日（0042）
			wageCountItem = wageCountItemDao.getUnique("code", "0042");
			if (wageCountItem != null) {
				Map<String, Object> reqParam = Maps.newHashMap();
				reqParam.put("employeeId", wageItem.getEmployee().getId());
				reqParam.put("wageCountItemCode", "0042");
				EmployeeWageItem temp = dao.getUnique(reqParam);
				if (temp == null) {
					temp = new EmployeeWageItem();
					temp.setEmployee(wageItem.getEmployee());
					temp.setWageCountItem(wageCountItem);
				}
				temp.setCount(Double.parseDouble(decimalFormat.format(wageItem.getCount() / 21.75 * 3)));
				dao.saveOrUpdate(temp);
			}
			// 一环节日（0047）
			wageCountItem = wageCountItemDao.getUnique("code", "0047");
			if (wageCountItem != null && wageItem.getEmployee().getEmployeeDept() != null) {
				Map<String, Object> reqParam = Maps.newHashMap();
				reqParam.put("employeeId", wageItem.getEmployee().getId());
				reqParam.put("wageCountItemCode", "0047");
				EmployeeWageItem temp = dao.getUnique(reqParam);
				if (temp == null) {
					temp = new EmployeeWageItem();
					temp.setEmployee(wageItem.getEmployee());
					temp.setWageCountItem(wageCountItem);
				}
				temp.setCount(Double.parseDouble(decimalFormat.format(wageItem.getCount() / 21.75 * 3 / 8 * wageItem.getEmployee().getEmployeeDept().getWorkTimer())));
				dao.saveOrUpdate(temp);
			}
		}
	}

	@Override
	public Map<String, Object> getEmployeeWageTemplate(Employee employee) {
		Map<String, Object> result = Maps.newHashMap();
		if (employee.getWagePlan() != null) {
			//得到工资方案
			WagePlan wagePlan = employee.getWagePlan();
			//得到工资方案工资核算模板值
			Map<String, Object> reqParam = Maps.newHashMap();
			reqParam.put("wagePlanId", employee.getWagePlan().getId());
			List<WageTemplate> wageTemplates = wageTemplateDao.find(reqParam);
			
			if (CollectionUtils.isNotEmpty(wagePlan.getCountItems())) {
				List<WageCountItem> countItems = wagePlan.getCountItems();
				for (WageCountItem wageCountItem : countItems) {
					String key = wageCountItem.getCode();
					double value = 0.0;
					
					//工资模板数值填充
					if (CollectionUtils.isNotEmpty(wageTemplates)) {
						for (WageTemplate wageTemplate : wageTemplates) {
							if (employee.getEmployeePost() != null && employee.getEmployeePost().getId().equals(wageTemplate.getEmployeePost().getId()) && wageCountItem.getId().equals(wageTemplate.getWageCountItem().getId())) {
								value = wageTemplate.getCount();
								break;
							}
						}
					}
					//员工工资项填充
					if (CollectionUtils.isNotEmpty(employee.getWageItems())) {
						List<EmployeeWageItem> wageItems = employee.getWageItems();
						for (EmployeeWageItem wageItem : wageItems) {
							if (wageCountItem.getId().equals(wageItem.getWageCountItem().getId())) {
								value = wageItem.getCount();
								break;
							}
						}
					}
					result.put(key, value);
				}
			}
		}
		return result;
	}

}
