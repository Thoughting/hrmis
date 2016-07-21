package com.eastcom.baseframe.web.modules.sys.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.modules.sys.dao.ResourceDao;
import com.eastcom.baseframe.web.modules.sys.entity.Resource;

@Repository
public class ResourceDaoImpl extends DaoSupport<Resource> implements ResourceDao {

	@Override
	@javax.annotation.Resource(name = "defaultSessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public void update(String id, String parentId, int sort) throws Exception {
		this.executeBySql(
				" update T_SYS_RESOURCE t set parent_id = ? , sort = ? where id = ?",
				new Object[] { parentId, sort, id });
	}
	
	@Override
	public void update2(String id, String parentId, int sort, int level)
			throws Exception {
		this.executeBySql(" update t_sys_resource t set parent_id = ? , sort = ? , level=? where id = ?",
				new Object[]{ parentId, sort, level, id });
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Resource> findByUserId(String userId) throws Exception {
		List<Resource> resources = new ArrayList<Resource>();
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT DISTINCT a.ID FROM t_sys_resource a ");
		sb.append(" LEFT JOIN t_sys_resource p ON p.id = a.parent_id ");
		sb.append(" JOIN t_sys_role_resource rm ON rm.fk_resource_id = a.id ");
		sb.append(" JOIN t_sys_role r ON r.id = rm.fk_role_id ");
		sb.append(" JOIN t_sys_user_role ur ON ur.fk_role_id = r.id ");
		sb.append(" JOIN t_sys_user u ON u.id = ur.fk_user_id AND u.id = ? ");
		sb.append(" ORDER BY a.sort ");
		List result = createSqlQuery(sb.toString(), new Object[] { userId }).list();
		if (CollectionUtils.isNotEmpty(result)) {
			for (Object resourceId : result) {
				Resource resource = this.getById(resourceId.toString());
				resources.add(resource);
			}
		}
		return resources;
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
		hql.append(" order by sort ");
	}

	

}
