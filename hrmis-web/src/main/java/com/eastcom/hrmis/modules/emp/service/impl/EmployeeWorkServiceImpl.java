package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeWorkDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeWork;
import com.eastcom.hrmis.modules.emp.service.EmployeeWorkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeWorkServiceImpl extends CrudServiceSupport<EmployeeWorkDao, EmployeeWork> implements EmployeeWorkService {

}
