package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeDeptLeaderDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeDeptLeader;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class EmployeeDeptLeaderDaoImpl extends DaoSupport<EmployeeDeptLeader> implements EmployeeDeptLeaderDao{

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public void deleteAllDepts(String leaderId) {
		executeBySql(" UPDATE t_employee_dept SET EMPLOYEE_DEPT_LEADER_ID=null WHERE EMPLOYEE_DEPT_LEADER_ID=? ",new Object[]{leaderId});
	}

	@Override
	public void insertDeptRelation(String leaderId, String deptId) {
		executeBySql(" UPDATE t_employee_dept SET EMPLOYEE_DEPT_LEADER_ID=? WHERE ID=? ",new Object[]{leaderId,deptId});
	}
	
}
