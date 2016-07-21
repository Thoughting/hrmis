package com.eastcom.baseframe.web.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.baseframe.web.modules.sys.dao.LogLoginDao;
import com.eastcom.baseframe.web.modules.sys.entity.LogLogin;
import com.eastcom.baseframe.web.modules.sys.service.LogLoginService;

@Service
@Transactional(value = "defaultTransactionManager")
public class LogLoginServiceImpl extends CrudServiceSupport<LogLoginDao, LogLogin> 
	implements LogLoginService{

}
