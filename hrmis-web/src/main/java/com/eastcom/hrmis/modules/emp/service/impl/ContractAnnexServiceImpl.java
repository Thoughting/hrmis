package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.ContractAnnexDao;
import com.eastcom.hrmis.modules.emp.entity.ContractAnnex;
import com.eastcom.hrmis.modules.emp.service.ContractAnnexService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "hrmisTransactionManager")
public class ContractAnnexServiceImpl extends CrudServiceSupport<ContractAnnexDao, ContractAnnex> implements ContractAnnexService{

}
