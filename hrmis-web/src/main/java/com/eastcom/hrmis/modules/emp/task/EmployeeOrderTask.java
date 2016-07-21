package com.eastcom.hrmis.modules.emp.task;

import com.eastcom.baseframe.common.task.Task;
import com.eastcom.baseframe.common.task.TaskSuppport;
import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.hrmis.modules.emp.entity.Employee;
import com.eastcom.hrmis.modules.emp.entity.EmployeeOrder;
import com.eastcom.hrmis.modules.emp.service.EmployeeOrderService;
import com.eastcom.hrmis.modules.emp.service.EmployeeService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统自动生成提醒代办工单
 * @author wutingguang <br>
 */
@Component
public class EmployeeOrderTask extends TaskSuppport implements Task {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeeOrderService orderService;
	
	/**
	 * 每天凌晨1点运行
	 * 
	 * 同一个员工同一个类型工单只能存在一个代办，不能重复提醒
	 * 
	 * 0：驾驶证过期提醒
	 * 1：身份证过期提醒
	 * 2：介绍费提醒
	 * 3：转正提醒
	 * 4：合同到期提醒
	 * 5：档案配置审核提醒
	 * 
	 */
	@Override
	@Scheduled(cron="0 0 1 * * ?")
	//@Scheduled(cron="0 0/5 * * * ?")
	public void execute() {
		System.out.println("---自动生成提醒代办工单--");
		//档案审核通过并且未离职员工
		Map<String, Object> reqParam = Maps.newHashMap();
		reqParam.put("auditStatus", 2);
		reqParam.put("hasQuitCompany", 0);
		reqParam.put("employeeDept", "all");
		List<Employee> employees = employeeService.find(reqParam);
		if (CollectionUtils.isNotEmpty(employees)) {
			//拿到所有的代办工单
			Map<String, Object> orderParam = Maps.newHashMap();
			orderParam.put("status", 0);
			orderParam.put("employeeDept", "all");
			List<EmployeeOrder> orders = orderService.find(orderParam);
			for (Employee employee : employees) {
				try {
					//驾驶证有效期一个月之后即将失效 （0：驾驶证过期提醒）
					if (employee.getDriveLicenseValidDate() != null && DateUtils.getDistanceOfTwoDate(new Date(), employee.getDriveLicenseValidDate()) < 30) {
						if (!hasOrderByEmployeeAndOrderType(orders, employee, 0)) {
							EmployeeOrder order = new EmployeeOrder();
							order.setEmployee(employee);
							order.setContent(employee.getName() + "驾驶证将在" + DateUtils.formatDate(employee.getDriveLicenseValidDate()) + "到期，请提醒" + employee.getName() + "处理。");
							order.setStatus(0);
							order.setType(0);
							order.setCreateBy("系统自动生成");
							orderService.save(order);
							logger.info(employee.getName() + " 创建工单:" + order.getTypeDict() + "成功！");
						}
					}
					//身份证不是长期有效，并且有效期一个月之后即将失效 （1：身份证过期提醒）
					if (employee.getIsCardNoLongTerm() == 0 && employee.getCardNoValidDate() != null && DateUtils.getDistanceOfTwoDate(new Date(), employee.getCardNoValidDate()) < 30) {
						if (!hasOrderByEmployeeAndOrderType(orders, employee, 1)) {
							EmployeeOrder order = new EmployeeOrder();
							order.setEmployee(employee);
							order.setContent(employee.getName() + "身份证将在" + DateUtils.formatDate(employee.getCardNoValidDate()) + "到期，请提醒" + employee.getName() + "处理。");
							order.setStatus(0);
							order.setType(1);
							order.setCreateBy("系统自动生成");
							orderService.save(order);
							logger.info(employee.getName() + " 创建工单:" + order.getTypeDict() + "成功！");
						}
					}
					//员工入职满3个月，并且有入职介绍人，该入职介绍人可获得介绍费 （2：介绍费提醒）
					if (StringUtils.isNotBlank(employee.getEnrtyIntorducer()) && "无".equals(employee.getEnrtyIntorducer()) && "空".equals(employee.getEnrtyIntorducer()) && employee.getEnrtyDate() != null && DateUtils.getDistanceOfTwoDate(employee.getEnrtyDate(), new Date()) > 90) {
						if (!hasOrderByEmployeeAndOrderType(orders, employee, 2)) {
							EmployeeOrder order = new EmployeeOrder();
							order.setEmployee(employee);
							order.setContent("被介绍人：" + employee.getName() + "已入职3个月，介绍人：" + employee.getEnrtyIntorducer()  + "可获得介绍费。");
							order.setStatus(0);
							order.setType(2);
							order.setCreateBy("系统自动生成");
							orderService.save(order);
							logger.info(employee.getName() + " 创建工单:" + order.getTypeDict() + "成功！");
						}
					}
					//员工转正提醒  管理人员入职3个月后提醒  其他人员入职1个月后提醒 （3：转正提醒）
					if (employee.getEmployeePost() != null) {
						if ((employee.getEmployeePost().getType() == 0 ) && employee.getIsRegular() == 0 && employee.getEnrtyDate() != null && DateUtils.getDistanceOfTwoDate(employee.getEnrtyDate(), new Date()) > 15
								|| (employee.getEmployeePost().getType() == 1 ) && employee.getIsRegular() == 0 && employee.getEnrtyDate() != null && DateUtils.getDistanceOfTwoDate(employee.getEnrtyDate(), new Date()) > 75 ) {
							if (!hasOrderByEmployeeAndOrderType(orders, employee, 3)) {
								EmployeeOrder order = new EmployeeOrder();
								order.setEmployee(employee);
								order.setContent(employee.getName() + "-" + DateUtils.formatDate(employee.getEnrtyDate()) + "入职，是否需要转正，如需请处理！");
								order.setStatus(0);
								order.setType(3);
								order.setCreateBy("系统自动生成");
								orderService.save(order);
								logger.info(employee.getName() + " 创建工单:" + order.getTypeDict() + "成功！");
							}
						}
					}
					//合同到期处理 （4：合同到期提醒）
					if (employee.getContractEndDate() != null && DateUtils.getDistanceOfTwoDate(new Date(), employee.getContractEndDate()) < 30) {
						if (!hasOrderByEmployeeAndOrderType(orders, employee, 4)) {
							EmployeeOrder order = new EmployeeOrder();
							order.setEmployee(employee);
							order.setContent(employee.getName() + "合同将在" + DateUtils.formatDate(employee.getContractEndDate()) + "到期，请及时处理！");
							order.setStatus(0);
							order.setType(4);
							order.setCreateBy("系统自动生成");
							orderService.save(order);
							logger.info(employee.getName() + " 创建工单:" + order.getTypeDict() + "成功！");
						}
					}
					//退休人员处理 （6：退休提醒）
					if (employee.getRetireDate() != null && DateUtils.getDistanceOfTwoDate(new Date(), employee.getRetireDate()) < 30) {
						if (!hasOrderByEmployeeAndOrderType(orders, employee, 6)) {
							EmployeeOrder order = new EmployeeOrder();
							order.setEmployee(employee);
							order.setContent(employee.getName() + "将在" + DateUtils.formatDate(employee.getRetireDate()) + "退休，请及时处理！");
							order.setStatus(0);
							order.setType(6);
							order.setCreateBy("系统自动生成");
							orderService.save(order);
							logger.info(employee.getName() + " 创建工单:" + order.getTypeDict() + "成功！");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 判断员工是否有某一类型的代办工单
	 * 
	 * @param orders
	 * @param employee
	 * @param orderType
	 * @return
	 */
	private boolean hasOrderByEmployeeAndOrderType(List<EmployeeOrder> orders, Employee employee, Integer orderType){
		if (CollectionUtils.isNotEmpty(orders) && employee != null) {
			for (EmployeeOrder employeeOrder : orders) {
				if (employeeOrder.getEmployee() != null && employeeOrder.getEmployee().getId().equals(employee.getId()) && employeeOrder.getType() == orderType) {
					return true;
				}
			}
		}
		return false;
	}
}
