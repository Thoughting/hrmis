package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeAnnexDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeAnnex;
import com.eastcom.hrmis.modules.emp.service.EmployeeAnnexService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeAnnexServiceImpl extends CrudServiceSupport<EmployeeAnnexDao, EmployeeAnnex> implements EmployeeAnnexService {

}
