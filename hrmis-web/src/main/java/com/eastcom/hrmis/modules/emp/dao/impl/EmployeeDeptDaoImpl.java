package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.hrmis.modules.emp.dao.EmployeeDeptDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeDept;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeDeptDaoImpl extends DaoSupport<EmployeeDept> implements EmployeeDeptDao{

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int getEmployeeCountByDeptId(String id) {
		int count = 0;
		String sqlString = " select SUBSTRING(code,length(code) - 2,length(code)) as EMPLOYEE_CODE from t_employee where RECORD_STATUS = 1 and EMPLOYEE_DEPT_ID = ? order by EMPLOYEE_CODE desc ";
		SQLQuery sqlQuery = createSqlQuery(sqlString, new Object[]{id});
		List<String> temp = sqlQuery.list();
		if (CollectionUtils.isNotEmpty(temp)) {
			for (String string : temp) {
				try {
					if (count < Integer.parseInt(string)) {
						count = Integer.parseInt(string);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

//		String sqlString = " select count(*) from t_employee where RECORD_STATUS = 1 and EMPLOYEE_DEPT_ID = ? ";
//		SQLQuery sqlQuery = createSqlQuery(sqlString, new Object[]{id});
//		List<BigInteger> temp = sqlQuery.list();
//		if (CollectionUtils.isNotEmpty(temp)) {
//			count = temp.get(0).intValue();
//		}
		return count;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EmployeeDept> getAuthDeptByUserId(String id) {
		List<EmployeeDept> depts = Lists.newArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("select FK_EMPLOYEE_DEPT_ID from T_EMPLOYEE_DEPT_SYS_USER where FK_SYS_USER_ID = ?");
		SQLQuery query = createSqlQuery(sb.toString(), new Object[]{id});
		List<String> ids = query.list();
		if (CollectionUtils.isNotEmpty(ids)) {
			for (String temp : ids) {
				EmployeeDept dept = getById(temp);
				if (dept != null) {
					depts.add(dept);
				}
			}
		}
		return depts;
	}

	@Override
	public void deleteAllDeptsAuth(String id) {
		executeBySql(" delete from T_EMPLOYEE_DEPT_SYS_USER where FK_SYS_USER_ID = ?",new Object[]{id});		
	}

	@Override
	public void insertDeptAuth(String id, String userId) {
		executeBySql(" INSERT INTO T_EMPLOYEE_DEPT_SYS_USER(FK_EMPLOYEE_DEPT_ID, FK_SYS_USER_ID) VALUES(?,?)",new Object[]{id, userId});
	}
	
	@Override
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params) {
		super.buildQueryHql(hql, params);
		if (checkParamsKey(params, "code_exact")) {
			hql.append(" and code = :code_exact ");
		}
		if (checkParamsKey(params, "code")) {
			params.put("code", "%" + params.get("code") + "%");
			hql.append(" and code like :code ");
		}
		if (checkParamsKey(params, "name")) {
			params.put("name", "%" + params.get("name") + "%");
			hql.append(" and name like :name ");
		}
		if (checkParamsKey(params, "remark")) {
			params.put("remark", "%" + params.get("remark") + "%");
			hql.append(" and remark like :remark ");
		}
		if (checkParamsKey(params, "telephone")) {
			params.put("telephone", "%" + params.get("telephone") + "%");
			hql.append(" and telephone like :telephone ");
		}
		
		String deptLeaderId = (String) params.get("deptLeaderId");
		if (StringUtils.isNotBlank(deptLeaderId)) {
			if ("null".equals(deptLeaderId)) {
				params.remove("deptLeaderId");
				hql.append(" and deptLeader.id is null ");
			} else {
				hql.append(" and deptLeader.id = :deptLeaderId ");
			}
		}
		
		hql.append(" order by code ");
	}

}
