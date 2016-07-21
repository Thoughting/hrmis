package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeDeptDao;
import com.eastcom.hrmis.modules.emp.dao.EmployeeDeptLeaderDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeDept;
import com.eastcom.hrmis.modules.emp.entity.EmployeeDeptLeader;
import com.eastcom.hrmis.modules.emp.service.EmployeeDeptLeaderService;
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
public class EmployeeDeptLeaderServiceImpl extends CrudServiceSupport<EmployeeDeptLeaderDao, EmployeeDeptLeader> implements EmployeeDeptLeaderService{

	@Autowired
	private EmployeeDeptDao employeeDeptDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public void saveOrUpdate(EmployeeDeptLeader deptLeader, List<Object> checkDeptNodes) {
		saveOrUpdate(deptLeader);
		if (deptLeader != null) {
			dao.deleteAllDepts(deptLeader.getId());
			if (CollectionUtils.isNotEmpty(checkDeptNodes)) {
				for (Object object : checkDeptNodes) {
					Map<String, Object> map = (Map<String, Object>) object;
					dao.insertDeptRelation(deptLeader.getId(), (String )map.get("id"));
				}
			}
		}
	}

	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		if (CollectionUtils.isNotEmpty(ids)) {
			for (Serializable id : ids) {
				EmployeeDeptLeader deptLeader = get(id);
				if (deptLeader != null && CollectionUtils.isNotEmpty(deptLeader.getDepts())) {
					List<EmployeeDept> depts = deptLeader.getDepts();
					for (EmployeeDept employeeDept : depts) {
						employeeDept.setDeptLeader(null);
						employeeDeptDao.saveOrUpdate(employeeDept);
					}
				}
			}
		}
		super.deleteByIds(ids);
	}
	
}
