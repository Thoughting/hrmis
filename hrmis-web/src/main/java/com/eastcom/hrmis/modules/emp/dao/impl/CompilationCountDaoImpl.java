package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.hrmis.modules.emp.dao.CompilationCountDao;
import com.eastcom.hrmis.modules.emp.entity.CompilationCount;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class CompilationCountDaoImpl extends DaoSupport<CompilationCount> implements CompilationCountDao {

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Map<String, Object>> getLeaderAndDeptName(String compilationId) {
		List<Map<String, Object>> result = Lists.newArrayList();
		String sqlString = " select t2.name as leaderName,t1.name as deptName,t1.id as deptId from ("
				+ " select * from t_employee_dept where EMPLOYEE_DEPT_LEADER_ID is not null and id in ("
				+ " select EMPLOYEE_DEPT_ID from t_compilation_table_dept_relation where COMPILATION_TABLE_ID = ? )"
				+ " ) t1"
				+ " left join t_employee_dept_leader t2"
				+ " on t1.employee_dept_leader_id = t2.id order by t2.name,t1.code";
		SQLQuery sqlQuery = createSqlQuery(sqlString, new Object[]{compilationId});
		List<Object[]> temp = sqlQuery.list();
		if (CollectionUtils.isNotEmpty(temp)) {
			for (Object[] objects : temp) {
				Map<String, Object> item = Maps.newHashMap();
				item.put("leaderName", objects[0]);
				item.put("deptName", objects[1]);
				item.put("deptId", objects[2]);
				result.add(item);
			}
		}
		return result;
	}
	
	@Override
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params) {
		super.buildQueryHql(hql, params);
		if (checkParamsKey(params, "compilationId")) {
			hql.append(" and compilationTable.id = :compilationId ");
		}
		if (checkParamsKey(params, "deptId")) {
			hql.append(" and employeeDept.id = :deptId ");
		}
		if (checkParamsKey(params, "postId")) {
			hql.append(" and employeePost.id = :postId ");
		}
	}
	
}
