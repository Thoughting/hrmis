package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeContractFinalAnnexDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeContractFinalAnnex;
import com.eastcom.hrmis.modules.emp.service.EmployeeContractFinalAnnexService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeContractFinalAnnexServiceImpl extends CrudServiceSupport<EmployeeContractFinalAnnexDao, EmployeeContractFinalAnnex> implements EmployeeContractFinalAnnexService {

}
