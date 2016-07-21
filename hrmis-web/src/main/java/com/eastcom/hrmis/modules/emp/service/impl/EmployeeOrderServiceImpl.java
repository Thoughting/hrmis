package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeOrderDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeOrder;
import com.eastcom.hrmis.modules.emp.service.EmployeeOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeOrderServiceImpl extends CrudServiceSupport<EmployeeOrderDao, EmployeeOrder> implements EmployeeOrderService{

}
