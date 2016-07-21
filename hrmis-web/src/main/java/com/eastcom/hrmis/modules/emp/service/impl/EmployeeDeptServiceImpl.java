package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeDao;
import com.eastcom.hrmis.modules.emp.dao.EmployeeDeptDao;
import com.eastcom.hrmis.modules.emp.entity.Employee;
import com.eastcom.hrmis.modules.emp.entity.EmployeeDept;
import com.eastcom.hrmis.modules.emp.service.EmployeeDeptService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeDeptServiceImpl extends CrudServiceSupport<EmployeeDeptDao, EmployeeDept> implements EmployeeDeptService{

	@Autowired
	private EmployeeDao employeeDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateAuth(String userId, List<Object> resources)
			throws Exception {
		dao.deleteAllDeptsAuth(userId);
		//更新权限
		if (CollectionUtils.isNotEmpty(resources)) {
			for (Object temp : resources) {
				String deptId = (String) ((Map<String, Object>) temp).get("id");
				dao.insertDeptAuth( deptId, userId);
			}
		}
	}

	@Override
	public List<EmployeeDept> getAuthDeptByUserId(String userId) {
		return dao.getAuthDeptByUserId(userId);
	}
	
	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		if (CollectionUtils.isNotEmpty(ids)) {
			for (Serializable id : ids) {
				EmployeeDept dept = get(id);
				if (dept != null && CollectionUtils.isNotEmpty(dept.getEmployees())) {
					List<Employee> employees = dept.getEmployees();
					for (Employee employee : employees) {
						employee.setEmployeeDept(null);
						employeeDao.saveOrUpdate(employee);
					}
				}
			}
		}
		super.deleteByIds(ids);
	}

	@Override
	@Transactional(readOnly = true)
	public int getEmployeeCountByDeptId(String id) {
		return dao.getEmployeeCountByDeptId(id);
	}

}
