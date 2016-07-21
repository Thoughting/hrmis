package com.eastcom.hrmis.modules.emp.service.impl;

import com.eastcom.baseframe.common.service.CrudServiceSupport;
import com.eastcom.hrmis.modules.emp.dao.WageCountItemDao;
import com.eastcom.hrmis.modules.emp.dao.WageTemplateDao;
import com.eastcom.hrmis.modules.emp.entity.WageCountItem;
import com.eastcom.hrmis.modules.emp.entity.WageTemplate;
import com.eastcom.hrmis.modules.emp.service.WageTemplateService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Map;

@Service
@Transactional(value = "hrmisTransactionManager")
public class WageTemplateServiceImpl extends CrudServiceSupport<WageTemplateDao, WageTemplate> implements WageTemplateService {

	@Autowired
	private WageCountItemDao wageCountItemDao;
	
	@Override
	public void updateEffect(WageTemplate wageTemplate) {
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		
		//正常工作时间工资（元/月）
		if (wageTemplate.getWageCountItem().getCode().equals("001")) {
			//正常工作时间工资 影响 休息日（0041），综合加班（0046），节日（0042）
			//休息日（0041）
			WageCountItem wageCountItem = wageCountItemDao.getUnique("code", "0041");
			if (wageCountItem != null) {
				Map<String, Object> reqParam = Maps.newHashMap();
				reqParam.put("wagePlanId", wageTemplate.getWagePlan().getId());
				reqParam.put("postId", wageTemplate.getEmployeePost().getId());
				reqParam.put("wageCountItemCode", "0041");
				WageTemplate temp = dao.getUnique(reqParam);
				if (temp == null) {
					temp = new WageTemplate();
					temp.setWagePlan(wageTemplate.getWagePlan());
					temp.setWageCountItem(wageCountItem);
					temp.setEmployeePost(wageTemplate.getEmployeePost());
				}
				temp.setCount(Double.parseDouble(decimalFormat.format(wageTemplate.getCount() / 21.75 * 2)));
				dao.saveOrUpdate(temp);
			}
			//一环综合加班（0046）
			wageCountItem = wageCountItemDao.getUnique("code", "0046");
			if (wageCountItem != null) {
				Map<String, Object> reqParam = Maps.newHashMap();
				reqParam.put("wagePlanId", wageTemplate.getWagePlan().getId());
				reqParam.put("postId", wageTemplate.getEmployeePost().getId());
				reqParam.put("wageCountItemCode", "0046");
				WageTemplate temp = dao.getUnique(reqParam);
				if (temp == null) {
					temp = new WageTemplate();
					temp.setWagePlan(wageTemplate.getWagePlan());
					temp.setWageCountItem(wageCountItem);
					temp.setEmployeePost(wageTemplate.getEmployeePost());
				}
				temp.setCount(Double.parseDouble(decimalFormat.format(wageTemplate.getCount() / 21.75 / 8 * 1.5)));
				dao.saveOrUpdate(temp);
			}
			// 节日（0042）
			wageCountItem = wageCountItemDao.getUnique("code", "0042");
			if (wageCountItem != null) {
				Map<String, Object> reqParam = Maps.newHashMap();
				reqParam.put("wagePlanId", wageTemplate.getWagePlan().getId());
				reqParam.put("postId", wageTemplate.getEmployeePost().getId());
				reqParam.put("wageCountItemCode", "0042");
				WageTemplate temp = dao.getUnique(reqParam);
				if (temp == null) {
					temp = new WageTemplate();
					temp.setWagePlan(wageTemplate.getWagePlan());
					temp.setWageCountItem(wageCountItem);
					temp.setEmployeePost(wageTemplate.getEmployeePost());
				}
				temp.setCount(Double.parseDouble(decimalFormat.format(wageTemplate.getCount() / 21.75 * 3)));
				dao.saveOrUpdate(temp);
			}
			// 节日（元/小时）（0047）
			wageCountItem = wageCountItemDao.getUnique("code", "0047");
			if (wageCountItem != null) {
				Map<String, Object> reqParam = Maps.newHashMap();
				reqParam.put("wagePlanId", wageTemplate.getWagePlan().getId());
				reqParam.put("postId", wageTemplate.getEmployeePost().getId());
				reqParam.put("wageCountItemCode", "0047");
				WageTemplate temp = dao.getUnique(reqParam);
				if (temp == null) {
					temp = new WageTemplate();
					temp.setWagePlan(wageTemplate.getWagePlan());
					temp.setWageCountItem(wageCountItem);
					temp.setEmployeePost(wageTemplate.getEmployeePost());
				}
				temp.setCount(Double.parseDouble(decimalFormat.format(wageTemplate.getCount() / 21.75 / 8 * 3)));
				dao.saveOrUpdate(temp);
			}
		}
	}

}
