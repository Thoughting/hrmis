package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.PerformanceWageItemDao;
import com.eastcom.hrmis.modules.emp.entity.PerformanceWageItem;
import com.eastcom.hrmis.modules.emp.service.PerformanceWageItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "hrmisTransactionManager")
public class PerformanceWageItemServiceImpl extends CrudServiceSupport<PerformanceWageItemDao, PerformanceWageItem> implements PerformanceWageItemService{

}
