package com.eastcom.baseframe.common.service;

import com.eastcom.baseframe.common.dao.Dao;
import com.eastcom.baseframe.common.entity.IdEntity;
import com.eastcom.baseframe.common.persistence.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Transactional
public abstract class CrudServiceSupport<D extends Dao<T>, T extends IdEntity<T> > extends BaseService implements CrudService<T>{

	/**
	 * 持久层对象
	 */
	@Autowired
	protected D dao;
	
	@Override
	@Transactional(readOnly = true)
	public List<T> findAll() {
		return dao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public T get(Serializable id) {
		return dao.getById(id);
	}

	@Override
	public void update(T t) {
		dao.update(t);		
	}

	@Override
	public void delete(T t) {
		dao.delete(t);		
	}

	@Override
	public void deleteById(Serializable id) {
		dao.deleteById(id);		
	}
	
	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		dao.deleteByIds(ids);
	}
	
	@Override
	public void delete(List<T> list) {
		dao.delete(list);		
	}
	
	@Override
	public Serializable save(T t) {
		return dao.save(t);
	}

	@Override
	public void saveOrUpdate(T t) {
		dao.saveOrUpdate(t);		
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> findByIds(Collection<? extends Serializable> ids) {
		return dao.findByIds(ids);
	}

	@Override
	@Transactional(readOnly = true)
	public <E> PageHelper<E> find(Map<String, Object> params, int pageNo,
			int pageSize) {
		return dao.find(params, pageNo, pageSize);
	}

	@Override
	@Transactional(readOnly = true)
	public <E> List<E> find(Map<String, Object> params) {
		return dao.find(params);
	}

	@Override
	@Transactional(readOnly = true)
	public T getUnique(String uniqueProperty, Object value) {
		return dao.getUnique(uniqueProperty, value);
	}

	@Override
	@Transactional(readOnly = true)
	public T getUnique(Map<String, Object> params) {
		return dao.getUnique(params);
	}

	@Override
	public void update(String tableName, Map<String, String> columnMapper,
			Map<String, String> conditionMapper, Map<String, Object> propertyMap) {
		dao.update(tableName, columnMapper, conditionMapper, propertyMap);
	}

	@Override
	public void save(String tableName, Map<String, String> columnMapper,
			Map<String, Object> propertyMap) {
		dao.save(tableName, columnMapper, propertyMap);
	}

	@Override
	public void saveOrUpdate(String tableName,
			Map<String, String> columnMapper,
			Map<String, String> conditionMapper, Map<String, Object> propertyMap) {
		dao.saveOrUpdate(tableName, columnMapper, conditionMapper, propertyMap);
	}

	@Override
	public void saveOrUpdate(String tableName,
			Map<String, String> columnMapper,
			Map<String, String> conditionMapper,
			List<Map<String, Object>> propertyMaps) {
		dao.saveOrUpdate(tableName, columnMapper, conditionMapper, propertyMaps);
	}

}
