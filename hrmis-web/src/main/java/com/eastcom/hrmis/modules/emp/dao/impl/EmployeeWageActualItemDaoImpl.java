package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeWageActualItemDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeWage;
import com.eastcom.hrmis.modules.emp.entity.EmployeeWageActualItem;
import com.eastcom.hrmis.modules.emp.entity.WageCountItem;
import com.google.common.collect.Maps;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class EmployeeWageActualItemDaoImpl extends DaoSupport<EmployeeWageActualItem> implements EmployeeWageActualItemDao {

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public EmployeeWageActualItem getByWageIdAndCountItemCode(
			String employeeWageId, String wageCountItemCode) {
		Map<String, Object> reqParam = Maps.newHashMap();
		reqParam.put("employeeWageId", employeeWageId);
		reqParam.put("wageCountItemCode", wageCountItemCode);
		return getUnique(reqParam);
	}

	@Override
	public EmployeeWageActualItem getWageActualItem(EmployeeWage employeeWage,
													WageCountItem wageCountItem, String wageCountItemCode) {
		EmployeeWageActualItem actualItem = getByWageIdAndCountItemCode(employeeWage.getId(),wageCountItemCode);
		if (actualItem == null) {
			actualItem = new EmployeeWageActualItem();
			actualItem.setEmployeeWage(employeeWage);
			actualItem.setWageCountItem(wageCountItem);
		}
		return actualItem;
	}
	
	@Override
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params) {
		super.buildQueryHql(hql, params);
		if (checkParamsKey(params, "employeeWageId")) {
			hql.append(" and employeeWage.id = :employeeWageId ");
		}
		if (checkParamsKey(params, "wageCountItemId")) {
			hql.append(" and wageCountItem.id = :wageCountItemId ");
		}
		if (checkParamsKey(params, "wageCountItemCode")) {
			hql.append(" and wageCountItem.code = :wageCountItemCode ");
		}
	}

}
