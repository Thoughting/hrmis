package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeWageActualItemDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeWageActualItem;
import com.eastcom.hrmis.modules.emp.service.EmployeeWageActualItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeWageActualItemServiceImpl extends CrudServiceSupport<EmployeeWageActualItemDao, EmployeeWageActualItem> implements EmployeeWageActualItemService {

}
