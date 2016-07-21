package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeSetWorkPlanDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeSetWorkPlan;
import com.eastcom.hrmis.modules.emp.service.EmployeeSetWorkPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeSetWorkPlanServiceImpl extends CrudServiceSupport<EmployeeSetWorkPlanDao, EmployeeSetWorkPlan> implements EmployeeSetWorkPlanService{

}
