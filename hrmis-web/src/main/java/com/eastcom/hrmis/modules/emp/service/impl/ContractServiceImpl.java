package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.ContractDao;
import com.eastcom.hrmis.modules.emp.entity.Contract;
import com.eastcom.hrmis.modules.emp.service.ContractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "hrmisTransactionManager")
public class ContractServiceImpl extends CrudServiceSupport<ContractDao, Contract> implements ContractService{

}
