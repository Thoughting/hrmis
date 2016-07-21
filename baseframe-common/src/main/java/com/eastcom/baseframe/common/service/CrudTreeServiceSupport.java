package com.eastcom.baseframe.common.service;

import com.eastcom.baseframe.common.dao.TreeDao;
import com.eastcom.baseframe.common.entity.TreeEntity;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public abstract class CrudTreeServiceSupport<D extends TreeDao<T>, T extends TreeEntity<T>> extends CrudServiceSupport<D, T> implements CrudTreeService<T> {

	/**
	 * 递归查询子节点
	 * @param children
	 */
	protected void initializeCascadeChildren(List<T> list){
		for (T t : list) {
			Hibernate.initialize(t.getChildren());
			if (CollectionUtils.isNotEmpty(t.getChildren())) {
				initializeCascadeChildren(t.getChildren());
			}
		}
	}
}
