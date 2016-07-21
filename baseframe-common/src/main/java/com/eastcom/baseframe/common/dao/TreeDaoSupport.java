package com.eastcom.baseframe.common.dao;

import com.eastcom.baseframe.common.entity.TreeEntity;

/**
 * Tree DAO支持类实现
 * @author wutingguang <br>
 */
public abstract class TreeDaoSupport<T extends TreeEntity<T>> extends DaoSupport<T> implements TreeDao<T> {

}
