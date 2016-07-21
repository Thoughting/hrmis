package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeFamilyDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeFamily;
import com.eastcom.hrmis.modules.emp.service.EmployeeFamilyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeFamilyServiceImpl extends CrudServiceSupport<EmployeeFamilyDao, EmployeeFamily> implements EmployeeFamilyService{

}
