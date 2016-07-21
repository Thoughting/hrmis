package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.hrmis.modules.emp.dao.CompilationTableDao;
import com.eastcom.hrmis.modules.emp.entity.CompilationTable;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class CompilationTableDaoImpl extends DaoSupport<CompilationTable> implements CompilationTableDao {

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public void deleteAllDeptSets(String id) {
		executeBySql(" delete from t_compilation_table_dept_relation where COMPILATION_TABLE_ID = ?",new Object[]{id});
	}

	@Override
	public void insertDeptSet(String id, String deptId) {
		executeBySql(" INSERT INTO t_compilation_table_dept_relation(COMPILATION_TABLE_ID, EMPLOYEE_DEPT_ID) VALUES(?,?)",new Object[]{id, deptId});
	}

	@Override
	public void deleteAllPostSets(String id) {
		executeBySql(" delete from t_compilation_table_post_relation where COMPILATION_TABLE_ID = ?",new Object[]{id});
	}

	@Override
	public void insertPostSet(String id, String postId) {
		executeBySql(" INSERT INTO t_compilation_table_post_relation(COMPILATION_TABLE_ID, EMPLOYEE_POST_ID) VALUES(?,?)",new Object[]{id, postId});
	}
	
}
