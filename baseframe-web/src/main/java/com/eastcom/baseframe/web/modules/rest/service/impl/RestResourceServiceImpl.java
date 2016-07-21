package com.eastcom.baseframe.web.modules.rest.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.baseframe.web.modules.rest.dao.RestResourceDao;
import com.eastcom.baseframe.web.modules.rest.entity.RestResource;
import com.eastcom.baseframe.web.modules.rest.service.RestResourceService;

@Service
@Transactional(value = "defaultTransactionManager")
public class RestResourceServiceImpl extends CrudServiceSupport<RestResourceDao, RestResource> implements RestResourceService{

}
