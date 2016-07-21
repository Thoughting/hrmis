package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.EmployeeContractHomeAnnexDao;
import com.eastcom.hrmis.modules.emp.entity.EmployeeContractHomeAnnex;
import com.eastcom.hrmis.modules.emp.service.EmployeeContractHomeAnnexService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "hrmisTransactionManager")
public class EmployeeContractHomeAnnexServiceImpl extends CrudServiceSupport<EmployeeContractHomeAnnexDao, EmployeeContractHomeAnnex> implements EmployeeContractHomeAnnexService{

}
