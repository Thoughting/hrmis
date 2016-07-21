package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeDao;
import com.eastcom.hrmis.modules.emp.dao.EmployeePostDao;
import com.eastcom.hrmis.modules.emp.entity.Employee;
import com.eastcom.hrmis.modules.emp.entity.EmployeePost;
import com.eastcom.hrmis.modules.emp.service.EmployeePostService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeePostServiceImpl extends CrudServiceSupport<EmployeePostDao, EmployeePost> implements EmployeePostService {

	@Autowired
	private EmployeeDao employeeDao;
	
	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		if (CollectionUtils.isNotEmpty(ids)) {
			for (Serializable id : ids) {
				EmployeePost post = get(id);
				if (post != null && CollectionUtils.isNotEmpty(post.getEmployees())) {
					List<Employee> employees = post.getEmployees();
					for (Employee employee : employees) {
						employee.setEmployeePost(null);
						employeeDao.saveOrUpdate(employee);
					}
				}
			}
		}
		super.deleteByIds(ids);
	}
	
}
