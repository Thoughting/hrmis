package com.eastcom.baseframe.web.common.dao;

import java.io.Serializable;
import java.util.Date;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.entity.DataEntity;
import com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache;

/**
 * data dao support
 * 动态处理createBy createDate updateBy updateDate 字段
 * 
 * @author wutingguang <br>
 */
public abstract class DataDaoSupport<T extends DataEntity<T>> extends DaoSupport<T> {

	@Override
	public Serializable save(T o) {
		if (StringUtils.isBlank(o.getCreateBy())) {
			o.setCreateBy(SecurityCache.getLoginUser().getName());
		}
		o.setCreateDate(new Date());
		o.setUpdateBy(o.getCreateBy());
		o.setUpdateDate(o.getCreateDate());
		return super.save(o);
	}

	@Override
	public void update(T o) {
		o.setUpdateBy(SecurityCache.getLoginUser().getName());
		o.setUpdateDate(new Date());
		super.update(o);
	}

	@Override
	public void saveOrUpdate(T o) {
		if (o.getCreateDate() == null) {
			if (StringUtils.isBlank(o.getCreateBy())) {
				o.setCreateBy(SecurityCache.getLoginUser().getName());
			}
			o.setCreateDate(new Date());
		}
		o.setUpdateBy(SecurityCache.getLoginUser().getName());
		o.setUpdateDate(new Date());
		super.saveOrUpdate(o);
	}
	
}
