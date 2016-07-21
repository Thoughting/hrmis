package com.eastcom.baseframe.common.service;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.eastcom.baseframe.common.persistence.PageHelper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * crud service 接口
 * @author wutingguang <br>
 */
public interface CrudService<T extends IdEntity<T>> {
	
	public List<T> findAll();

	public T get(Serializable id);

	public void update(T t);

	public void update(String tableName, Map<String, String> columnMapper, Map<String, String> conditionMapper, Map<String, Object> propertyMap);
	
	public void delete(T t);

	public void deleteById(Serializable id);
	
	public void deleteByIds(Collection<? extends Serializable> ids);
	
	public void delete(List<T> list);

	public Serializable save(T t);

	public void save(String tableName, Map<String, String> columnMapper, Map<String, Object> propertyMap);
	
	public void saveOrUpdate(T t);
	
	public void saveOrUpdate(String tableName, Map<String, String> columnMapper, Map<String, String> conditionMapper, Map<String, Object> propertyMap);
	
	public void saveOrUpdate(String tableName, Map<String, String> columnMapper, Map<String, String> conditionMapper, List<Map<String, Object>> propertyMaps);

	public T getUnique(String uniqueProperty, Object value);
	
	public T getUnique(Map<String, Object> params);
	
	public List<T> findByIds(Collection<? extends Serializable> ids);
	
	public <E> PageHelper<E> find(Map<String, Object> params, int pageNo, int pageSize);
	
	public <E> List<E> find(Map<String, Object> params);
	
}
