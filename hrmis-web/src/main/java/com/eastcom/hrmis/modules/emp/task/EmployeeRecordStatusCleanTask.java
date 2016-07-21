package com.eastcom.hrmis.modules.emp.task;

import com.eastcom.baseframe.common.task.Task;
import com.eastcom.baseframe.common.task.TaskSuppport;
import com.eastcom.hrmis.modules.emp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 员工异常记录清理任务
 *
 * Created by wtg on 2016/6/19.
 */
@Component
public class EmployeeRecordStatusCleanTask  extends TaskSuppport implements Task {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 凌晨1点执行
     *
     */
    @Override
    @Scheduled(cron="0 0 1 * * ?")
    public void execute() {
        logger.info("员工异常记录清理：开始执行");

        employeeService.cleanAbnormalRecord();

        logger.info("员工异常记录清理：执行结束");
    }

}
