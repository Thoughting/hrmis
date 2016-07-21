package com.eastcom.hrmis.modules.emp.task;

import com.eastcom.baseframe.common.task.Task;
import com.eastcom.baseframe.common.task.TaskSuppport;
import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.hrmis.modules.emp.entity.EmployeeCheckWork;
import com.eastcom.hrmis.modules.emp.entity.EmployeeSetWork;
import com.eastcom.hrmis.modules.emp.service.EmployeeCheckWorkService;
import com.eastcom.hrmis.modules.emp.service.EmployeeSetWorkService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 员工考勤同步计划任务
 *
 * Created by wtg on 2016/6/6.
 */
@Component
public class EmployeeCheckWorkSyncTask extends TaskSuppport implements Task {

    @Autowired
    private EmployeeCheckWorkService employeeCheckWorkService;

    @Autowired
    private EmployeeSetWorkService employeeSetWorkService;

    @Override
    @Scheduled(cron="0 0 1 * * ?")
    public void execute() {
        logger.info("员工考勤同步计划任务：开始执行");
        Map<String, Object> params = Maps.newHashMap();
        params.put("startWorkDate",DateUtils.addDays(new Date(),-3));
        params.put("endWorkDate",DateUtils.addDays(new Date(),-1));
        List<EmployeeSetWork> setWorks = employeeSetWorkService.find(params);
        if (CollectionUtils.isNotEmpty(setWorks)){
            for (EmployeeSetWork setWork: setWorks) {
                params = Maps.newHashMap();
                params.put("workDate",setWork.getWorkDate());
                params.put("employeeId",setWork.getEmployee().getId());
                EmployeeCheckWork checkWork = employeeCheckWorkService.getUnique(params);
                if (checkWork == null){
                    checkWork = new EmployeeCheckWork();
                    checkWork.setEmployee(setWork.getEmployee());
                    checkWork.setWorkDate(setWork.getWorkDate());
                    checkWork.setIsAtWork(setWork.getIsAtWork());
                    checkWork.setAtWorkTimer(setWork.getAtWorkTimer());
                    checkWork.setAtWorkSpecialStatus(setWork.getAtWorkSpecialStatus());
                    checkWork.setAtHolidayType(setWork.getAtHolidayType());
                    checkWork.setAtHolidayHour(setWork.getAtHolidayHour());
                    checkWork.setAtHolidayMinute(setWork.getAtHolidayMinute());
                    checkWork.setAtHolidayTimer(setWork.getAtHolidayTimer());
                    checkWork.setRemark(setWork.getRemark());
                    employeeCheckWorkService.saveOrUpdate(checkWork);
                }
            }
        }

        logger.info("员工考勤同步计划任务：执行结束");
    }
}
