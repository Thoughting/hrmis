package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.hrmis.modules.emp.dao.WagePlanDao;
import com.eastcom.hrmis.modules.emp.entity.WagePlan;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;

@Repository
public class WagePlanDaoImpl extends DaoSupport<WagePlan> implements WagePlanDao {

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public void deleteAllWageCountSets(String id) {
		executeBySql(" delete from t_wage_plan_count_item_relation where WAGE_PLAN_ID = ?",new Object[]{id});
	}

	@Override
	public void insertWageCountSet(String id, String wageCountId) {
		executeBySql(" INSERT INTO t_wage_plan_count_item_relation(WAGE_PLAN_ID, WAGE_COUNT_ITEM_ID) VALUES(?,?)",new Object[]{id, wageCountId});
	}

	@Override
	public void deleteAllPostSets(String id) {
		executeBySql(" delete from t_wage_plan_post_relation where WAGE_PLAN_ID = ?",new Object[]{id});
	}

	@Override
	public void insertPostSet(String id, String postId) {
		executeBySql(" INSERT INTO t_wage_plan_post_relation(WAGE_PLAN_ID, EMPLOYEE_POST_ID) VALUES(?,?)",new Object[]{id, postId});
	}

	@Override
	public void deleteById(Serializable id) {
		executeBySql(" UPDATE t_employee set WAGE_PLAN_ID = null where WAGE_PLAN_ID = ? ",new Object[]{id});
		super.deleteById(id);
	}
	
}
