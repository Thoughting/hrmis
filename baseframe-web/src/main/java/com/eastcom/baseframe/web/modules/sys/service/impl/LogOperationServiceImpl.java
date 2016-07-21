package com.eastcom.baseframe.web.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.baseframe.web.modules.sys.dao.LogOperationDao;
import com.eastcom.baseframe.web.modules.sys.entity.LogOperation;
import com.eastcom.baseframe.web.modules.sys.service.LogOperationService;

@Service
@Transactional(value = "defaultTransactionManager")
public class LogOperationServiceImpl extends CrudServiceSupport<LogOperationDao, LogOperation> 
	implements LogOperationService{

}
