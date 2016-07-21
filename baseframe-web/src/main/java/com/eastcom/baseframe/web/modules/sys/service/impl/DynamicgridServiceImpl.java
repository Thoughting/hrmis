package com.eastcom.baseframe.web.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.baseframe.web.modules.sys.dao.DynamicgridDao;
import com.eastcom.baseframe.web.modules.sys.entity.Dynamicgrid;
import com.eastcom.baseframe.web.modules.sys.service.DynamicgridService;

@Service
@Transactional(value = "defaultTransactionManager")
public class DynamicgridServiceImpl extends CrudServiceSupport<DynamicgridDao, Dynamicgrid> implements DynamicgridService{

}
