package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeStudyDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeStudy;
import com.eastcom.hrmis.modules.emp.service.EmployeeStudyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeStudyServiceImpl extends CrudServiceSupport<EmployeeStudyDao, EmployeeStudy> implements EmployeeStudyService{

}
