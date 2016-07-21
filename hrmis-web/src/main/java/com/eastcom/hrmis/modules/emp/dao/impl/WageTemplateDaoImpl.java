package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.hrmis.modules.emp.dao.WageTemplateDao;
import com.eastcom.hrmis.modules.emp.entity.WageTemplate;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class WageTemplateDaoImpl extends DaoSupport<WageTemplate> implements WageTemplateDao{

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params) {
		super.buildQueryHql(hql, params);
		if (checkParamsKey(params, "wagePlanId")) {
			hql.append(" and wagePlan.id = :wagePlanId ");
		}
		if (checkParamsKey(params, "wageCountItemId")) {
			hql.append(" and wageCountItem.id = :wageCountItemId ");
		}
		if (checkParamsKey(params, "wageCountItemCode")) {
			hql.append(" and wageCountItem.code = :wageCountItemCode ");
		}
		if (checkParamsKey(params, "postId")) {
			hql.append(" and employeePost.id = :postId ");
		}
	}
}
