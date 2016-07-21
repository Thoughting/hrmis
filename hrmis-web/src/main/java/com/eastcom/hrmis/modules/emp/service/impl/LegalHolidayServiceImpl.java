package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.LegalHolidayDao;
import com.eastcom.hrmis.modules.emp.entity.LegalHoliday;
import com.eastcom.hrmis.modules.emp.service.LegalHolidayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "hrmisTransactionManager")
public class LegalHolidayServiceImpl extends CrudServiceSupport<LegalHolidayDao, LegalHoliday> implements LegalHolidayService{

}
