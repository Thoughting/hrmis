package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.TreeDaoSupport;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.hrmis.modules.emp.dao.WageCountItemDao;
import com.eastcom.hrmis.modules.emp.entity.WageCountItem;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class WageCountItemDaoImpl extends TreeDaoSupport<WageCountItem> implements WageCountItemDao{

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public void update(String id, String parentId, int sort) throws Exception {
		this.executeBySql(" update T_WAGE_COUNT_ITEM t set parent_id = ? , sort = ? where id = ?",new Object[]{parentId,sort,id});
	}
	
	@Override
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params) {
		super.buildQueryHql(hql, params);
		String parentId = (String) params.get("parentId");
		if (StringUtils.isNotBlank(parentId)) {
			if ("null".equals(parentId)) {
				hql.append(" and ( parent.id is null or parent.id = '' ) ");
				params.remove("parentId");
			} else if ("not_null".equals(parentId)) {
				hql.append(" and ( parent.id is not null and parent.id != '' )");
				params.remove("parentId");
			} else {
				hql.append(" and parent.id = :parentId ");
			}
		}
		
		if (checkParamsKey(params, "code")) {
			hql.append(" and code = :code ");
		}
		
		hql.append(" order by sort ");
	}
}
