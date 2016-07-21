package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeWageHisItemDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeWageHisItem;
import com.eastcom.hrmis.modules.emp.service.EmployeeWageHisItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeWageHisItemServiceImpl extends CrudServiceSupport<EmployeeWageHisItemDao, EmployeeWageHisItem> implements EmployeeWageHisItemService{

}
