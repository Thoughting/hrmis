package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeWageHisDao;
import com.eastcom.hrmis.modules.emp.dao.EmployeeWageHisItemDao;
import com.eastcom.hrmis.modules.emp.entity.*;
import com.eastcom.hrmis.modules.emp.service.EmployeeWageHisService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeWageHisServiceImpl extends CrudServiceSupport<EmployeeWageHisDao, EmployeeWageHis> implements EmployeeWageHisService {

	@Autowired
	private EmployeeWageHisItemDao wageHisItemDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<WagePlan> getWagePlanInfos(Map<String, Object> params) {
		Map<String, WagePlan> result = Maps.newHashMap();
		List<EmployeeWageHis> wageHis = dao.find(params);
		if (CollectionUtils.isNotEmpty(wageHis)) {
			for (EmployeeWageHis employeeWageHis : wageHis) {
				if (employeeWageHis.getWagePlan() != null && result.get(employeeWageHis.getWagePlan().getId()) == null) {
					result.put(employeeWageHis.getWagePlan().getId(), employeeWageHis.getWagePlan());
				}
			}
		}
		return Lists.newArrayList(result.values().iterator());
	}

	@Override
	public void cloneByWage(EmployeeWage wage) throws Exception {
		Map<String, Object> reqParams = Maps.newHashMap();
		reqParams.put("employeeName", wage.getEmployee().getName());
		reqParams.put("wageDate",wage.getWageDateStr());
		EmployeeWageHis wageHis = dao.getUnique(reqParams);
		if (wageHis == null) {
			wageHis = new EmployeeWageHis();
			wageHis.setEmployee(wage.getEmployee());
			wageHis.setWageDateStr(wage.getWageDateStr());
		}
		wageHis.setDeptName(wage.getEmployee().getEmployeeDeptName());
		wageHis.setPostName(wage.getEmployee().getEmployeePostName());
		wageHis.setWagePlan(wage.getEmployee().getWagePlan());
		wageHis.setBankCard(wage.getEmployee().getBankCard());
		dao.saveOrUpdate(wageHis);
		
		//清空所有的item项
		if (CollectionUtils.isNotEmpty(wageHis.getWageItems())) {
			wageHisItemDao.delete(wageHis.getWageItems());
			wageHis.setWageItems(null);
		}
		//更新item
		if (CollectionUtils.isNotEmpty(wage.getWageItems())) {
			System.out.println(wage.getWageItems().size());
			for (EmployeeWageActualItem actualItem : wage.getWageItems()) {
				EmployeeWageHisItem wageHisItem = new EmployeeWageHisItem();
				wageHisItem.setEmployeeWageHis(wageHis);
				wageHisItem.setWageCountItem(actualItem.getWageCountItem());
				wageHisItem.setCount(actualItem.getCount());
				wageHisItem.setRemark(actualItem.getRemark());
				wageHisItemDao.save(wageHisItem);
			}
		}
	}

}
