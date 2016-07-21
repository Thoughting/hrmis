package com.eastcom.hrmis.modules.emp.task;

import com.eastcom.baseframe.common.task.Task;
import com.eastcom.baseframe.common.task.TaskSuppport;
import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.hrmis.modules.emp.service.EmployeeCheckWorkStatMonthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class EmployeeCheckWorkStatMonthTask extends TaskSuppport implements Task {

	@Autowired
	private EmployeeCheckWorkStatMonthService employeeCheckWorkStatMonthService;
	
	/**
	 * 每天凌晨1点运行
	 * 
	 */
	@Override
	@Scheduled(cron="0 0 1 * * ?")
	public void execute() {
		String statMonth = DateUtils.formatDate(new Date(), "yyyy年-MM月");
		try {
			employeeCheckWorkStatMonthService.refreshCheckWorkStatMonth(statMonth);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
