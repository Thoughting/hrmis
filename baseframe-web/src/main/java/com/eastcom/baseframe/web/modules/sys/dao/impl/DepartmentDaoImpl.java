package com.eastcom.baseframe.web.modules.sys.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.eastcom.baseframe.common.dao.TreeDaoSupport;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.modules.sys.dao.DepartmentDao;
import com.eastcom.baseframe.web.modules.sys.entity.Department;

@Repository
public class DepartmentDaoImpl extends TreeDaoSupport<Department> implements DepartmentDao {

	@Override
	@Resource(name="defaultSessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public void update(String id, String parentId, int sort) throws Exception {
		this.executeBySql(" update T_SYS_DEPARTMENT t set parent_id = ? , sort = ? where id = ?",new Object[]{parentId,sort,id});
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
		hql.append(" order by sort,code ");
	}
	
}
