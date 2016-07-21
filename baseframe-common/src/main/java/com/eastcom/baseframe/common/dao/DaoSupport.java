package com.eastcom.baseframe.common.dao;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.eastcom.baseframe.common.persistence.PageHelper;
import com.eastcom.baseframe.common.utils.Reflections;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DAO支持类实现
 * 
 * @author jared
 * @version 2014-01-05
 * 
 */
@SuppressWarnings("unchecked")
public abstract class DaoSupport<T extends IdEntity<T>> implements Dao<T> {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * SessionFactory
	 */

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected String BASE_QUERY_HQL = "";
	
	/**
	 * 实体类类型(由构造方法自动赋值)
	 */
	protected Class<?> entityClass;

	/**
	 * 当前类字段列表
	 */
	protected List<Field> entityFields;

	/**
	 * 构造方法，根据实例类自动获取实体类类型
	 */
	public DaoSupport() {
		entityClass = Reflections.getClassGenricType(getClass());
		BASE_QUERY_HQL = " from " + entityClass.getSimpleName() + " where 1=1 ";
	}

	/**
	 * 获取当前类型的属性列表
	 * 
	 * @return
	 */
	protected List<Field> getEntityFields() {
		if (entityFields == null) {
			entityFields = new LinkedList<Field>();
			Class<?> superClass = entityClass;
			while (superClass != null) {
				Field[] superFields = superClass.getDeclaredFields();
				for (Field f : superFields) {
					if (Modifier.isStatic(f.getModifiers())) {
						continue;
					}
					if (f.isAnnotationPresent(Transient.class)) {
						continue;
					}
					entityFields.add(f);
				}
				superClass = superClass.getSuperclass();
			}
		}
		return entityFields;
	}

	/**
	 * 获取 Session
	 */
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 强制与数据库同步
	 */
	public void flush() {
		getCurrentSession().flush();
	}

	/**
	 * 清除缓存数据
	 */
	public void clear() {
		getCurrentSession().clear();
	}

	/**
	 * 清除指定对象的缓存
	 * 
	 * @param o
	 */
	public void evict(T o) {
		getCurrentSession().evict(o);
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public List<T> findAll() {
		return getCurrentSession().createCriteria(entityClass).list();
	}

	@Override
	public Serializable save(T o) {
		if (o != null) {
			return this.getCurrentSession().save(o);
		}
		return null;
	}

	@Override
	public void save(String tableName, Map<String, String> columnMapper,Map<String, Object> propertyMap) {
		Object[] os = makeInsertSqlAndParams(tableName, columnMapper, propertyMap);
		executeBySql((String )os[0], (Object[] )os[1]);
	}
	
	/**
	 * 更新
	 * 
	 * @param o
	 * @return
	 */
	public void update(T o) {
		if (o != null) {
			this.getCurrentSession().update(o);
		}
	}

	@Override
	public void update(String tableName, Map<String, String> columnMapper,
			Map<String, String> conditionMapper, Map<String, Object> propertyMap) {
		Object[] os = makeUpdateSqlAndParams(tableName, columnMapper, conditionMapper, propertyMap);
		executeBySql((String )os[0], (Object[] )os[1]);
	}
	
	@Override
	public void saveOrUpdate(T o) {
		if (o != null) {
			this.getCurrentSession().saveOrUpdate(o);
		}
	}

	@Override
	public void saveOrUpdate(String tableName,
			Map<String, String> columnMapper,
			Map<String, String> conditionMapper, Map<String, Object> propertyMap) {
		//先保存，保存失败，再更新
		try {
			save(tableName, columnMapper, propertyMap);
		} catch (Exception e) {
			update(tableName, columnMapper, conditionMapper, propertyMap);
		}
	}

	@Override
	public void saveOrUpdate(String tableName,
			Map<String, String> columnMapper,
			Map<String, String> conditionMapper,
			List<Map<String, Object>> propertyMaps) {
		if (CollectionUtils.isNotEmpty(propertyMaps)) {
			for (Map<String, Object> propertyMap : propertyMaps) {
				saveOrUpdate(tableName, columnMapper, conditionMapper, propertyMap);
			}
		}
	}
	
	@Override
	public T merge(T o) {
		if (o != null) {
			return (T) this.getCurrentSession().merge(o);
		}
		return o;
	}

	public void delete(List<T> list) {
		for (T o : list) {
			delete(o);
		}
	}

	public void delete(T o) {
		getCurrentSession().delete(o);
	}

	public void deleteById(Serializable id) {
		getCurrentSession().delete(getById(id));
	}

	public void deleteByIds(Collection<? extends Serializable> ids) {
		for (Serializable id : ids) {
			deleteById(id);
		}
	}

	public Long countAll() {
		return count(DetachedCriteria.forClass(entityClass).setProjection(Projections.rowCount()));
	}

	/**
	 * 获取实体
	 * 
	 * @param id
	 * @return
	 */
	public T getById(Serializable id) {
		return (T) getCurrentSession().get(entityClass, id);
	}

	public List<T> findByIds(Collection<? extends Serializable> ids) {
		List<T> list = new ArrayList<T>();
		for (Serializable id : ids) {
			list.add(getById(id));
		}
		return list;
	}

	// -------------- HQL Query --------------

	@Override
	public T getUnique(String uniqueProperty, Object value) {
		return (T) getCurrentSession().createCriteria(entityClass).add(Restrictions.eq(uniqueProperty, value)).uniqueResult();
	}

	/**
	 * 获取实体
	 * 
	 * @param hqlString
	 * @return
	 */
	public T get(String hqlString) {
		Query query = createQuery(hqlString);
		return (T) query.uniqueResult();
	}

	/**
	 * 获取实体
	 * 
	 * @param hqlString
	 * @param parameter
	 * @return
	 */

	public T get(String hqlString, Map<String, Object> parameter) {
		Query query = createQuery(hqlString, parameter);
		return (T) query.uniqueResult();
	}

	public T get(String hqlString, Object[] parameter) {
		Query query = createQuery(hqlString, parameter);
		return (T) query.uniqueResult();
	}

	/**
	 * HQL 分页查询
	 * 
	 * @param page
	 * @param hqlString
	 * @return
	 */

	public <E> PageHelper<E> find(PageHelper<E> page, String hqlString) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()) {
			String countQlString = "select count(*) " + removeSelect(removeOrders(hqlString));
			// page.setCount(Long.valueOf(createQuery(countQlString,
			// parameter).uniqueResult().toString()));
			Query query = createQuery(countQlString);
			List<Object> list = query.list();
			if (list.size() > 0) {
				page.setCount(Long.valueOf(list.get(0).toString()));
			} else {
				page.setCount(list.size());
			}
			if (page.getCount() < 1) {
				return page;
			}
		}
		// order by
		String hql = hqlString;
		if (StringUtils.isNotBlank(page.getSort())) {
			hql += " order by " + page.getSort();
			if (StringUtils.isNotBlank(page.getOrder())) {
				hql += " " + page.getOrder();
			}
		}
		Query query = createQuery(hql);
		// set page
		if (!page.isDisabled()) {
			query.setFirstResult(page.getFirstResult());
			query.setMaxResults(page.getMaxResults());
		}
		page.setList(query.list());
		return page;
	}

	/**
	 * HQL 分页查询
	 * 
	 * @param page
	 * @param hqlString
	 * @param parameter
	 * @return
	 */

	public <E> PageHelper<E> find(PageHelper<E> page, String hqlString, Map<String, Object> parameter) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()) {
			String countQlString = "select count(*) " + removeSelect(removeOrders(hqlString));
			// page.setCount(Long.valueOf(createQuery(countQlString,
			// parameter).uniqueResult().toString()));
			Query query = createQuery(countQlString, parameter);
			List<Object> list = query.list();
			if (list.size() > 0) {
				page.setCount(Long.valueOf(list.get(0).toString()));
			} else {
				page.setCount(list.size());
			}
			if (page.getCount() < 1) {
				return page;
			}
		}
		// order by
		String hql = hqlString;
		if (StringUtils.isNotBlank(page.getSort())) {
			hql += " order by " + page.getSort();
			if (StringUtils.isNotBlank(page.getOrder())) {
				hql += " " + page.getOrder();
			}
		}
		Query query = createQuery(hql, parameter);
		// set page
		if (!page.isDisabled()) {
			query.setFirstResult(page.getFirstResult());
			query.setMaxResults(page.getMaxResults());
		}
		page.setList(query.list());
		return page;
	}

	public <E> PageHelper<E> find(PageHelper<E> page, String hqlString, Object[] parameter) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()) {
			String countQlString = "select count(*) " + removeSelect(removeOrders(hqlString));
			// page.setCount(Long.valueOf(createQuery(countQlString,
			// parameter).uniqueResult().toString()));
			Query query = createQuery(countQlString, parameter);
			List<Object> list = query.list();
			if (list.size() > 0) {
				page.setCount(Long.valueOf(list.get(0).toString()));
			} else {
				page.setCount(list.size());
			}
			if (page.getCount() < 1) {
				return page;
			}
		}
		// order by
		String hql = hqlString;
		if (StringUtils.isNotBlank(page.getSort())) {
			hql += " order by " + page.getSort();
			if (StringUtils.isNotBlank(page.getOrder())) {
				hql += " " + page.getOrder();
			}
		}
		Query query = createQuery(hql, parameter);
		// set page
		if (!page.isDisabled()) {
			query.setFirstResult(page.getFirstResult());
			query.setMaxResults(page.getMaxResults());
		}
		page.setList(query.list());
		return page;
	}

	public <E> List<E> find(String hqlString, int firstResult, int maxResults) {
		Query query = createQuery(hqlString);
		// set page
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		return query.list();
	}

	public <E> List<E> find(String hqlString, Map<String, Object> parameter, int firstResult, int maxResults) {

		Query query = createQuery(hqlString, parameter);
		// set page
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		return query.list();
	}

	public <E> List<E> find(String hqlString, Object[] parameter, int firstResult, int maxResults) {

		Query query = createQuery(hqlString, parameter);
		// set page
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		return query.list();
	}

	/**
	 * HQL 查询
	 * 
	 * @param hqlString
	 * @return
	 */
	public <E> List<E> find(String hqlString) {
		Query query = createQuery(hqlString);
		return query.list();
	}

	/**
	 * HQL 查询
	 * 
	 * @param hqlString
	 * @param parameter
	 * @return
	 */
	public <E> List<E> find(String hqlString, Map<String, Object> parameter) {
		Query query = createQuery(hqlString, parameter);
		return query.list();
	}

	/**
	 * HQL 查询
	 * 
	 * @param hqlString
	 * @param parameter
	 * @return
	 */
	public <E> List<E> find(String hqlString, Object[] parameter) {
		Query query = createQuery(hqlString, parameter);
		return query.list();
	}

	@Override
	public Long count(String hqlString) {
		Query q = this.createQuery(hqlString);
		return (Long) q.uniqueResult();
	}

	@Override
	public Long count(String hqlString, Map<String, Object> parameter) {
		Query q = this.createQuery(hqlString, parameter);
		return (Long) q.uniqueResult();
	}

	public Long count(String hqlString, Object[] parameter) {
		Query q = this.createQuery(hqlString, parameter);
		return (Long) q.uniqueResult();
	}

	/**
	 * 更新
	 * 
	 * @param hqlString
	 * @return
	 */
	public int execute(String hqlString) {
		return createQuery(hqlString).executeUpdate();
	}

	/**
	 * 更新
	 * 
	 * @param hqlString
	 * @param parameter
	 * @return
	 */
	public int execute(String hqlString, Map<String, Object> parameter) {
		return createQuery(hqlString, parameter).executeUpdate();
	}

	/**
	 * 更新
	 * 
	 * @param hqlString
	 * @param parameter
	 * @return
	 */
	public int execute(String hqlString, Object[] parameter) {
		return createQuery(hqlString, parameter).executeUpdate();
	}

	/**
	 * 创建 HQL 查询对象
	 * 
	 * @param hqlString
	 * @param parameter
	 * @return
	 */
	protected Query createQuery(String hqlString) {
		Query query = getCurrentSession().createQuery(hqlString);
		return query;
	}

	/**
	 * 创建 HQL 查询对象
	 * 
	 * @param hqlString
	 * @param parameter
	 * @return
	 */
	protected Query createQuery(String hqlString, Map<String, Object> parameter) {
		Query query = getCurrentSession().createQuery(hqlString);
		setParameter(query, parameter);
		return query;
	}

	/**
	 * 创建 HQL 查询对象
	 * 
	 * @param hqlString
	 * @param parameter
	 * @return
	 */
	protected Query createQuery(String hqlString, Object[] parameter) {
		Query query = getCurrentSession().createQuery(hqlString);
		setParameter(query, parameter);
		return query;
	}

	// -------------- SQL Query --------------

	/**
	 * SQL 分页查询
	 * 
	 * @param page
	 * @param sqlString
	 * @return
	 */

	public <E> PageHelper<E> findBySql(PageHelper<E> page, String sqlString) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()) {
			String countSqlString = "select count(*) from (" + removeOrders(sqlString)+") t";
			// page.setCount(Long.valueOf(createSqlQuery(countSqlString,
			// parameter).uniqueResult().toString()));
			SQLQuery query = createSqlQuery(countSqlString);
			List<Object> list = query.list();
			if (list.size() > 0) {
				page.setCount(Long.valueOf(list.get(0).toString()));
			} else {
				page.setCount(list.size());
			}
			if (page.getCount() < 1) {
				return page;
			}
		}
		// order by
		String sql = sqlString;
		if (StringUtils.isNotBlank(page.getSort()) && !hasOrders(sql)) {
			sql += " order by " + page.getSort();
			if (StringUtils.isNotBlank(page.getOrder())) {
				sql += " " + page.getOrder();
			}
		}
		SQLQuery query = createSqlQuery(sql);
		// set page
		if (!page.isDisabled()) {
			query.setFirstResult(page.getFirstResult());
			query.setMaxResults(page.getMaxResults());
		}
		page.setList(query.list());
		return page;
	}

	/**
	 * SQL 分页查询
	 * 
	 * @param page
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public <E> PageHelper<E> findBySql(PageHelper<E> page, String sqlString, Map<String, Object> parameter) {
		return findBySql(page, sqlString, parameter, null);
	}

	/**
	 * SQL 分页查询
	 * 
	 * @param page
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public <E> PageHelper<E> findBySql(PageHelper<E> page, String sqlString, Object[] parameter) {
		return findBySql(page, sqlString, parameter, null);
	}

	/**
	 * SQL 分页查询
	 * 
	 * @param page
	 * @param sqlString
	 * @param resultClass
	 * @return
	 */

	public <E> PageHelper<E> findBySql(PageHelper<E> page, String sqlString, Class<?> resultClass) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()) {
			String countSqlString = "select count(*) from (" + removeOrders(sqlString)+") t";
			// page.setCount(Long.valueOf(createSqlQuery(countSqlString,
			// parameter).uniqueResult().toString()));
			SQLQuery query = createSqlQuery(countSqlString);
			List<Object> list = query.list();
			if (list.size() > 0) {
				page.setCount(Long.valueOf(list.get(0).toString()));
			} else {
				page.setCount(list.size());
			}
			if (page.getCount() < 1) {
				return page;
			}
		}
		// order by
		String sql = sqlString;
		if (StringUtils.isNotBlank(page.getSort()) && !hasOrders(sql)) {
			sql += " order by " + page.getSort();
			if (StringUtils.isNotBlank(page.getOrder())) {
				sql += " " + page.getOrder();
			}
		}
		SQLQuery query = createSqlQuery(sql);
		// set page
		if (!page.isDisabled()) {
			query.setFirstResult(page.getFirstResult());
			query.setMaxResults(page.getMaxResults());
		}
		setResultTransformer(query, resultClass);
		page.setList(query.list());
		return page;
	}

	/**
	 * SQL 分页查询
	 * 
	 * @param page
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */

	public <E> PageHelper<E> findBySql(PageHelper<E> page, String sqlString, Map<String, Object> parameter, Class<?> resultClass) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()) {
			String countSqlString = "select count(*) from (" + removeOrders(sqlString)+") t";
			// page.setCount(Long.valueOf(createSqlQuery(countSqlString,
			// parameter).uniqueResult().toString()));
			SQLQuery query = createSqlQuery(countSqlString, parameter);
			List<Object> list = query.list();
			if (list.size() > 0) {
				page.setCount(Long.valueOf(list.get(0).toString()));
			} else {
				page.setCount(list.size());
			}
			if (page.getCount() < 1) {
				return page;
			}
		}
		// order by
		String sql = sqlString;
		if (StringUtils.isNotBlank(page.getSort()) && !hasOrders(sql)) {
			sql += " order by " + page.getSort();
			if (StringUtils.isNotBlank(page.getOrder())) {
				sql += " " + page.getOrder();
			}
		}
		SQLQuery query = createSqlQuery(sql, parameter);
		// set page
		if (!page.isDisabled()) {
			query.setFirstResult(page.getFirstResult());
			query.setMaxResults(page.getMaxResults());
		}
		setResultTransformer(query, resultClass);
		page.setList(query.list());
		return page;
	}

	/**
	 * SQL 分页查询
	 * 
	 * @param page
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */

	public <E> PageHelper<E> findBySql(PageHelper<E> page, String sqlString, Object[] parameter, Class<?> resultClass) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()) {
			String countSqlString = "select count(*) from (" + removeOrders(sqlString)+") t";
			// page.setCount(Long.valueOf(createSqlQuery(countSqlString,
			// parameter).uniqueResult().toString()));
			SQLQuery query = createSqlQuery(countSqlString, parameter);
			List<Object> list = query.list();
			if (list.size() > 0) {
				page.setCount(Long.valueOf(list.get(0).toString()));
			} else {
				page.setCount(list.size());
			}
			if (page.getCount() < 1) {
				return page;
			}
		}
		// order by
		String sql = sqlString;
		if (StringUtils.isNotBlank(page.getSort()) && !hasOrders(sql)) {
			sql += " order by " + page.getSort();
			if (StringUtils.isNotBlank(page.getOrder())) {
				sql += " " + page.getOrder();
			}
		}
		SQLQuery query = createSqlQuery(sql, parameter);
		// set page
		if (!page.isDisabled()) {
			query.setFirstResult(page.getFirstResult());
			query.setMaxResults(page.getMaxResults());
		}
		setResultTransformer(query, resultClass);
		page.setList(query.list());
		return page;
	}

	public <E> List<E> findBySql(String sqlString, int firstResult, int maxResults) {
		Query query = createSqlQuery(sqlString);
		// set page
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		return query.list();
	}

	public <E> List<E> findBySql(String sqlString, Map<String, Object> parameter, int firstResult, int maxResults) {

		Query query = createSqlQuery(sqlString, parameter);
		// set page
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		return query.list();
	}

	public <E> List<E> findBySql(String sqlString, Object[] parameter, int firstResult, int maxResults) {

		Query query = createSqlQuery(sqlString, parameter);
		// set page
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		return query.list();
	}

	/**
	 * SQL 查询
	 * 
	 * @param sqlString
	 * @return
	 */

	public <E> List<E> findBySql(String sqlString) {
		SQLQuery query = createSqlQuery(sqlString);
		return query.list();
	}
	
	/**
	 * SQL 查询
	 * 
	 * @param sqlString
	 * @return
	 */

	public <E> List<E> findBySql(String sqlString,Class<?> resultClass) {
		SQLQuery query = createSqlQuery(sqlString);
		setResultTransformer(query, resultClass);
		return query.list();
	}

	/**
	 * SQL 查询
	 * 
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public <E> List<E> findBySql(String sqlString, Map<String, Object> parameter) {
		return findBySql(sqlString, parameter, null);
	}

	/**
	 * SQL 查询
	 * 
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public <E> List<E> findBySql(String sqlString, Object[] parameter) {
		return findBySql(sqlString, parameter, null);
	}

	/**
	 * SQL 查询
	 * 
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */

	public <E> List<E> findBySql(String sqlString, Map<String, Object> parameter, Class<?> resultClass) {
		SQLQuery query = createSqlQuery(sqlString, parameter);
		setResultTransformer(query, resultClass);
		return query.list();
	}

	/**
	 * SQL 查询
	 * 
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */

	public <E> List<E> findBySql(String sqlString, Object[] parameter, Class<?> resultClass) {
		SQLQuery query = createSqlQuery(sqlString, parameter);
		setResultTransformer(query, resultClass);
		return query.list();
	}

	public Long countBySql(String sqlString) {
		Query q = this.createSqlQuery(sqlString);
		return ((BigInteger) q.uniqueResult()).longValue();
	}

	public Long countBySql(String sqlString, Map<String, Object> parameter) {
		Query q = this.createSqlQuery(sqlString, parameter);
		return ((BigInteger) q.uniqueResult()).longValue();
	}

	public Long countBySql(String sqlString, Object[] parameter) {
		Query q = this.createSqlQuery(sqlString, parameter);
		return ((BigInteger) q.uniqueResult()).longValue();
	}

	@Override
	public int executeBySql(String sqlString) {
		SQLQuery query = createSqlQuery(sqlString);
		return query.executeUpdate();
	}

	@Override
	public int executeBySql(String sqlString, Map<String, Object> parameter) {
		SQLQuery query = createSqlQuery(sqlString, parameter);
		return query.executeUpdate();
	}

	public int executeBySql(String sqlString, Object[] parameter) {
		SQLQuery query = createSqlQuery(sqlString, parameter);
		return query.executeUpdate();
	}

	/**
	 * 创建 SQL 查询对象
	 * 
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	protected SQLQuery createSqlQuery(String sqlString) {
		SQLQuery query = getCurrentSession().createSQLQuery(sqlString);
		return query;
	}

	/**
	 * 创建 SQL 查询对象
	 * 
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	protected SQLQuery createSqlQuery(String sqlString, Map<String, Object> parameter) {
		SQLQuery query = getCurrentSession().createSQLQuery(sqlString);
		setParameter(query, parameter);
		return query;
	}

	/**
	 * 创建 SQL 查询对象
	 * 
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	protected SQLQuery createSqlQuery(String sqlString, Object[] parameter) {
		SQLQuery query = getCurrentSession().createSQLQuery(sqlString);
		setParameter(query, parameter);
		return query;
	}

	public <E> List<E> findByNamedQuery(String tableName) {
		return findByNamedQuery(tableName, null, null);
	}

	public <E> List<E> findByNamedQuery(String tableName, Map<String, Object> parameter) {
		return findByNamedQuery(tableName, parameter, null);
	}

	public <E> List<E> findByNamedQuery(String tableName, Map<String, Object> parameter, Class<?> resultClass) {
		SQLQuery q = createNamedQuery(tableName, parameter);
		setResultTransformer(q, resultClass);
		return q.list();
	}

	/**
	 * 创建根据数据库表名查询语句
	 * 
	 * @param tableName
	 *            表名
	 * @param parameter
	 *            参数
	 * @return
	 */
	protected SQLQuery createNamedQuery(String tableName, Map<String, Object> parameter) {
		SQLQuery query = getCurrentSession().createSQLQuery(getCurrentSession().getNamedQuery(tableName).getQueryString());
		setParameter(query, parameter);
		return query;
	}

	// -------------- Query Tools --------------

	/**
	 * 设置查询结果类型
	 * 
	 * @param query
	 * @param resultClass
	 */
	private void setResultTransformer(SQLQuery query, Class<?> resultClass) {
		if (resultClass != null) {
			if (resultClass == Map.class) {
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			} else if (resultClass == List.class) {
				query.setResultTransformer(Transformers.TO_LIST);
			} else {
				query.addEntity(resultClass);
			}
		}
	}

	/**
	 * 设置查询参数
	 * 
	 * @param query
	 * @param parameter
	 */
	private void setParameter(Query query, Map<String, Object> parameter) {
		if (parameter != null) {
			Set<String> keySet = parameter.keySet();
			for (String string : keySet) {
				Object value = parameter.get(string);
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同
				if (value instanceof Collection<?>) {
					query.setParameterList(string, (Collection<?>) value);
				} else if (value instanceof Object[]) {
					query.setParameterList(string, (Object[]) value);
				} else {
					query.setParameter(string, value);
				}
			}
		}
	}

	private void setParameter(Query query, Object[] parameter) {
		if (parameter != null && parameter.length > 0) {
			for (int i = 0; i < parameter.length; i++) {
				query.setParameter(i, parameter[i]);
			}
		}
	}

	/**
	 * 去除qlString的select子句。
	 * 
	 * @param hqlString
	 * @return
	 */
	private String removeSelect(String hqlString) {
		int beginPos = hqlString.toLowerCase().indexOf("from");
		return hqlString.substring(beginPos);
	}

	/**
	 * 去除hql的orderBy子句。
	 * 
	 * @param hqlString
	 * @return
	 */
	private String removeOrders(String hqlString) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hqlString);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private Boolean hasOrders(String hqlString) {
		Boolean flag = false;
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hqlString);
		while (m.find()) {
			flag = true;
		}
		return flag;
	}

	// -------------- Criteria --------------

	protected T get(DetachedCriteria detachedCriteria) {
		return get(detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}

	protected T get(DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getCurrentSession());
		criteria.setResultTransformer(resultTransformer);
		return (T) criteria.uniqueResult();
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @return
	 */
	protected PageHelper<T> find(PageHelper<T> page) {
		return find(page, createDetachedCriteria());
	}

	/**
	 * 使用检索标准对象分页查询
	 * 
	 * @param page
	 * @param detachedCriteria
	 * @return
	 */
	protected PageHelper<T> find(PageHelper<T> page, DetachedCriteria detachedCriteria) {
		return find(page, detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}

	/**
	 * 使用检索标准对象分页查询
	 * 
	 * @param page
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */

	protected PageHelper<T> find(PageHelper<T> page, DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()) {
			page.setCount(count(detachedCriteria));
			if (page.getCount() < 1) {
				return page;
			}
		}
		Criteria criteria = detachedCriteria.getExecutableCriteria(getCurrentSession());
		criteria.setResultTransformer(resultTransformer);
		// set page
		if (!page.isDisabled()) {
			criteria.setFirstResult(page.getFirstResult());
			criteria.setMaxResults(page.getMaxResults());
		}
		// order by
		if (StringUtils.isNotBlank(page.getSort())) {
			String[] fields = page.getSort().split(",");
			String[] orders = null;
			if (StringUtils.isNotBlank(page.getOrder()))
				orders = page.getOrder().split(",");
			for (int i = 0; i < fields.length; i++) {
				String f = fields[i];
				if (null != orders && orders.length > 0) {
					if ("DESC".equals(orders[i].toUpperCase())) {
						criteria.addOrder(Order.desc(f));
					} else {
						criteria.addOrder(Order.asc(f));
					}
				} else {
					criteria.addOrder(Order.asc(f));
				}
			}
		}

		page.setList(criteria.list());
		return page;

	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @return
	 */
	protected List<T> find(DetachedCriteria detachedCriteria, int firstResult, int maxResults) {
		return find(detachedCriteria, firstResult, maxResults, Criteria.DISTINCT_ROOT_ENTITY);
	}

	/**
	 * 使用检索标准对象分页查询
	 * 
	 * @param page
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */

	protected List<T> find(DetachedCriteria detachedCriteria, int firstResult, int maxResults, ResultTransformer resultTransformer) {

		Criteria criteria = detachedCriteria.getExecutableCriteria(getCurrentSession());
		criteria.setResultTransformer(resultTransformer);
		// set page
		if (firstResult > -1 || maxResults > -1) {
			criteria.setFirstResult(firstResult);
			criteria.setMaxResults(maxResults);
		}
		return criteria.list();
	}

	/**
	 * 使用检索标准对象查询
	 * 
	 * @param detachedCriteria
	 * @return
	 */
	protected <E> List<E> find(DetachedCriteria detachedCriteria) {
		return find(detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}

	/**
	 * 使用检索标准对象查询
	 * 
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */

	protected <E> List<E> find(DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getCurrentSession());
		criteria.setResultTransformer(resultTransformer);
		return criteria.list();
	}

	/**
	 * 使用检索标准对象查询记录数
	 * 
	 * @param detachedCriteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected Long count(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getCurrentSession());
		Long totalCount = 0L;
		try {
			// Get orders
			Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
			field.setAccessible(true);
			List orderEntrys = (List) field.get(criteria);
			// Remove orders
			field.set(criteria, new ArrayList());
			// Remove limit;
			criteria.setFirstResult(0);
			criteria.setMaxResults(0);
			// Get count
			criteria.setProjection(Projections.rowCount());
			totalCount = Long.valueOf(criteria.uniqueResult().toString());
			// Clean count
			criteria.setProjection(null);
			// Restore orders
			field.set(criteria, orderEntrys);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return totalCount;
	}

	/**
	 * 创建与会话无关的检索标准对象
	 * 
	 * @param criterions
	 *            Restrictions.eq("name", value);
	 * @return
	 */
	protected DetachedCriteria createDetachedCriteria(Criterion... criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(entityClass);
		for (Criterion c : criterions) {
			dc.add(c);
		}
		return dc;
	}

	/**
	 * 创建与会话无关的检索标准对象
	 * 
	 * @param criterions
	 *            Restrictions.eq("name", value);
	 * @return
	 */
	protected DetachedCriteria createDetachedCriteria(T o, Criterion... criterions) {
		return createDetachedCriteria(o, MatchMode.ANYWHERE, criterions);
	}

	/**
	 * 创建与会话无关的检索标准对象
	 * 
	 * @param criterions
	 *            Restrictions.eq("name", value);
	 * @return
	 */
	protected DetachedCriteria createDetachedCriteria(T o, MatchMode mode, Criterion... criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(o.getClass());
		for (Criterion c : criterions) {
			dc.add(c);
		}
		if (o != null) {
			Example example = Example.create(o);
			example.enableLike(mode);
			example.excludeNone();// 空的不做查询条件
			example.excludeZeroes();// 0不要查询
			example.ignoreCase();// 忽略大小写
			dc.add(example);
		}
		return dc;
	}

	/**
	 * 根据MAP封装成查询条件对象，指定MatchMode
	 * 
	 * @param param
	 * @param mode
	 * @return
	 */
	protected DetachedCriteria buildCriteriaByMap(Map<String, Object> param, MatchMode mode) {
		DetachedCriteria dc = DetachedCriteria.forClass(entityClass);
		if (param == null) {
			return dc;
		}
		for (Map.Entry<String, Object> entity : param.entrySet()) {
			Field field = Reflections.getDeclaredField(entityClass, entity.getKey());
			if (field == null || entity.getValue() == null || entity.getValue().toString().equals("")) {
				continue;
			}
			Annotation anno = field.getAnnotation(Transient.class);
			if (anno != null) {
				continue;
			}
			Object value = Reflections.convert(entity.getValue(), field.getType());
			if (value == null) {
				continue;
			}
			if (field.getType().equals(String.class)) {
				value = "%" + value + "%";
				dc.add(Restrictions.ilike(field.getName(), value));
			} else {
				dc.add(Restrictions.eq(field.getName(), value));
			}
		}
		//
		// for (java.lang.reflect.Field f : getEntityFields()) {
		// if (null == f)
		// break;
		// Object v = param.get(f.getName());
		// if (null != v) {
		// if (v instanceof String) {
		// if (!StringUtils.isEmpty(v.toString())) {
		// if (f.getType().equals(String.class))
		// dc.add(Restrictions.ilike(f.getName(), v.toString(), mode));
		// else {
		// try {
		// Class<?> type = f.getType();
		// if (type.isPrimitive()) {
		// if (boolean.class.equals(type))
		// type = Boolean.class;
		// else if (byte.class.equals(type))
		// type = Byte.class;
		// else if (char.class.equals(type))
		// type = Character.class;
		// else if (short.class.equals(type))
		// type = Short.class;
		// else if (int.class.equals(type))
		// type = Integer.class;
		// else if (long.class.equals(type))
		// type = Long.class;
		// else if (float.class.equals(type))
		// type = Float.class;
		// else if (double.class.equals(type))
		// type = Double.class;
		// }
		// Method m = type.getMethod("valueOf", String.class);
		// dc.add(Restrictions.eq(f.getName(), m.invoke(null, v.toString())));
		// } catch (SecurityException e) {
		// logger.warn(
		// "尝试转换[" + f.getDeclaringClass().getName() + "."
		// + f.getName() + "]的字符串值失败，访问权限不足", e);
		// } catch (IllegalArgumentException e) {
		// logger.warn(
		// "尝试转换[" + f.getDeclaringClass().getName() + "."
		// + f.getName() + "]的字符串值失败，传参错误，这个不应该会发生。", e);
		// } catch (NoSuchMethodException e) {
		// logger.warn(
		// "尝试转换[" + f.getDeclaringClass().getName() + "."
		// + f.getName() + "]的字符串值失败，该字段类型[" + f.getType()
		// + "]没有定义valueOf(String)的方法", e);
		// } catch (IllegalAccessException e) {
		// logger.warn(
		// "尝试转换[" + f.getDeclaringClass().getName() + "."
		// + f.getName() + "]的字符串值失败，没有权限调用valueOf(String)", e);
		// } catch (InvocationTargetException e) {
		// logger.warn(
		// "尝试转换[" + f.getDeclaringClass().getName() + "."
		// + f.getName()
		// + "]的字符串值失败，valueOf(String)方法不是一个静态方法。", e);
		// } catch (NullPointerException e) {
		// logger.warn(
		// "尝试转换[" + f.getDeclaringClass().getName() + "."
		// + f.getName()
		// + "]的字符串值失败，valueOf(String)方法不是一个静态方法。", e);
		// } catch (Throwable e) {
		// logger.warn(
		// "尝试转换[" + f.getDeclaringClass().getName() + "."
		// + f.getName() + "]的字符串值失败，未知错误", e);
		// }
		// }
		// }
		// } else {
		// dc.add(Restrictions.eq(f.getName(), v));
		// }
		// }
		// }
		return dc;
	}

	/**
	 * 根据MAP封装成查询条件对象
	 * 
	 * @param param
	 * @param mode
	 * @return
	 */
	protected DetachedCriteria buildCriteriaByMap(Map<String, Object> param) {
		return buildCriteriaByMap(param, MatchMode.ANYWHERE);
	}

	@Override
	public <E> PageHelper<E> find(Map<String, Object> params, int pageNo,
								  int pageSize) {
		StringBuffer hql = new StringBuffer(BASE_QUERY_HQL);
		buildQueryHql(hql , params);
		PageHelper<E> pageHelper = new PageHelper<E>();
	    pageHelper.setPage(pageNo);
	    pageHelper.setRows(pageSize);
		return find(pageHelper, hql.toString(), params);
	}

	@Override
	public <E> List<E> find(Map<String, Object> params) {
		StringBuffer hql = new StringBuffer(BASE_QUERY_HQL);
		buildQueryHql(hql, params);
		return createQuery(hql.toString(), params).list();
	}
	
	@Override
	public T getUnique(Map<String, Object> params) {
		List<T> list = find(params);
		if (CollectionUtils.isNotEmpty(list) && list.size() == 1) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 子类如想使用
	 * 
	 * PageHelper<E> find(Map<String, Object> params, int pageNo,int pageSize)
	 * List<E> find(Map<String, Object> params)
	 * 
	 * 需要重写该方法 buildHql
	 * 
	 * @param hql
	 * @param params
	 */
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params){
		
	}
	
	/**
	 * 判断Params是否有key值，且key值不为空,则返回true
	 * 否则将该空值key删除掉
	 * @param key
	 */
	protected boolean checkParamsKey(Map<String, Object> params,String key){
		Object value = params.get(key);
		if (value instanceof String) {
			if (StringUtils.isNotBlank((String) value)) {
				return true;
			}
		} else if (value != null){
			return true;
		}
		params.remove(key);
		return false;
	}

	/**
	 * 创建保存更新的sql语句
	 * 
	 * @param tableName
	 * @param map
	 * @param columnMapper
	 * @return
	 */
	protected Object[] makeInsertSqlAndParams(String tableName, Map<String, String> columnMapper,Map<String, Object> propertyMap){
		StringBuffer sbsql = new StringBuffer();
		List<Object> params = Lists.newArrayList();
		sbsql.append(" insert into " + tableName);
		
		//构建insert块
		String tmp_1 = "";
		String tmp_2 = "";
		for (String key : propertyMap.keySet()){
			if (columnMapper.get(key) != null) {
				tmp_1 += " " + columnMapper.get(key) + ",";
				tmp_2 += " ?,";
				params.add(propertyMap.get(key));
			}
		}
		tmp_1 = tmp_1.substring(0, tmp_1.length() - 1);
		tmp_2 = tmp_2.substring(0, tmp_2.length() - 1);
		
		sbsql.append(" ( " + tmp_1 + " ) values ( " + tmp_2 + " ) ");
		
		return new Object[]{sbsql.toString(),params.toArray()};
	}
	
	/**
	 * 创建保存更新的sql语句
	 * 
	 * @param tableName
	 * @param map
	 * @param columnMapper
	 * @return
	 */
	protected Object[] makeUpdateSqlAndParams(String tableName, Map<String, String> columnMapper,Map<String, String> conditionMapper,Map<String, Object> propertyMap){
		StringBuffer sbsql = new StringBuffer();
		List<Object> params = Lists.newArrayList();
		sbsql.append(" update " + tableName + " set ");
		
		//构建update块
		String tmp_1 = "";
		for (String key : propertyMap.keySet()){
			if (columnMapper.get(key) != null) {
				tmp_1 += " " + columnMapper.get(key) + " = ?,";
				params.add(propertyMap.get(key));
			}
		}
		tmp_1 = tmp_1.substring(0, tmp_1.length() - 1);
		
		sbsql.append(tmp_1);
		
		//构建where块
		String tmp_2 = " where ";
		for (String key : propertyMap.keySet()){
			if (conditionMapper.get(key) != null) {
				tmp_2 += " " + columnMapper.get(key) + " = ? and";
				params.add(propertyMap.get(key));
			}
		}
		tmp_2 = tmp_2.substring(0, tmp_2.length() - 3);
		
		sbsql.append(tmp_2);
		
		return new Object[]{sbsql.toString(),params.toArray()};
	}
	
}
