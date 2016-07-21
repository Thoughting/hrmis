package com.eastcom.hrmis.modules.emp.dao.impl;

import com.eastcom.baseframe.common.dao.DaoSupport;
import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.hrmis.modules.emp.cache.EmployeeDeptCache;
import com.eastcom.hrmis.modules.emp.dao.EmployeeDao;
import com.eastcom.hrmis.modules.emp.entity.Employee;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeDaoImpl extends DaoSupport<Employee> implements EmployeeDao{

	@Override
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getEmployeeDeptPostCount() {
		List<Map<String, Object>> result = Lists.newArrayList();
		String sqlString = " select EMPLOYEE_DEPT_ID,EMPLOYEE_POST_ID,count(*) from t_employee "
				+ " where RECORD_STATUS = 1 AND AUDIT_STATUS = 2 AND HAS_QUIT_COMPANY = 0 "
				+ " GROUP BY EMPLOYEE_DEPT_ID,EMPLOYEE_POST_ID ";
		SQLQuery sqlQuery = createSqlQuery(sqlString);
		List<Object[]> temp = sqlQuery.list();
		if (CollectionUtils.isNotEmpty(temp)) {
			for (Object[] objects : temp) {
				Map<String, Object> item = Maps.newHashMap();
				item.put("deptId", objects[0]);
				item.put("postId", objects[1]);
				item.put("count", objects[2]);
				result.add(item);
			}
		}
		return result;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Map<String, Object> getEmployeeStatByDeptIdAndDate(String deptId,
			Date date) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("statDate", DateUtils.formatDate(date, "yyyy年-MM月"));
		
		date.setDate(1);
		String startDate = DateUtils.formatDate(date);
		date.setMonth(date.getMonth() + 1);
		String endDate = DateUtils.formatDate(date);
		Object[] params = new Object[]{startDate,endDate};
		
		String deptSql = "";
		if (StringUtils.isNotBlank(deptId)) {
			deptSql = " and EMPLOYEE_DEPT_ID = ? ";
			params = new Object[]{deptId,startDate,endDate};
		}
		
		//入职人员数
		String enrtyCountSql = "select count(*) from ( select * from t_employee where RECORD_STATUS = 1 AND AUDIT_STATUS = 2 and HAS_QUIT_COMPANY = 0 " + deptSql
						+" ) t where t.enrty_date >= ? and t.enrty_date < ? ";
		//转正人员数
		String regularCountSql = "select count(*) from ( select * from t_employee where RECORD_STATUS = 1 AND AUDIT_STATUS = 2 and HAS_QUIT_COMPANY = 0 and IS_REGULAR = 1"  + deptSql
				+" ) t where t.regular_date >= ? and t.regular_date < ? ";
		//离职人员数
		String quitCompanyCountSql = "select count(*) from ( select * from t_employee where RECORD_STATUS = 1 AND AUDIT_STATUS = 2 and HAS_QUIT_COMPANY = 1" + deptSql
				+" ) t where t.quit_company_date >= ? and t.quit_company_date < ? ";
		//合同到期人数
		String contractEndCountSql = "select count(*) from ( select * from t_employee where RECORD_STATUS = 1 AND AUDIT_STATUS = 2 and HAS_QUIT_COMPANY = 0" + deptSql
				+" ) t where t.contract_end_date >= ? and t.contract_end_date < ? ";
		//参保人员数
		String insureCountSql = "select count(*) from ( select * from t_employee where RECORD_STATUS = 1 AND AUDIT_STATUS = 2 and HAS_QUIT_COMPANY = 0" + deptSql
				+" ) t where t.enrty_date >= ? and t.enrty_date < ? and HAS_PERSION_INSURE = 1 ";
		//未参保人员数
		String noInsureCountSql = "select count(*) from ( select * from t_employee where RECORD_STATUS = 1 AND AUDIT_STATUS = 2 and HAS_QUIT_COMPANY = 0" + deptSql
				+" ) t where t.enrty_date >= ? and t.enrty_date < ? and HAS_PERSION_INSURE = 0 ";
		//退休人员数
		String retireCountSql = "select count(*) from ( select * from t_employee where RECORD_STATUS = 1 AND AUDIT_STATUS = 2 and HAS_QUIT_COMPANY = 0" + deptSql
				+" ) t where t.retire_date >= ? and t.retire_date < ? ";
		
		map.put("enrtyCount", ((BigInteger )createSqlQuery(enrtyCountSql, params).uniqueResult()).intValue());
		map.put("regularCount", ((BigInteger )createSqlQuery(regularCountSql, params).uniqueResult()).intValue());
		map.put("quitCompanyCount", ((BigInteger )createSqlQuery(quitCompanyCountSql, params).uniqueResult()).intValue());
		map.put("contractEndCount", ((BigInteger )createSqlQuery(contractEndCountSql, params).uniqueResult()).intValue());
		map.put("insureCount", ((BigInteger )createSqlQuery(insureCountSql, params).uniqueResult()).intValue());
		map.put("noInsureCount", ((BigInteger )createSqlQuery(noInsureCountSql, params).uniqueResult()).intValue());
		map.put("retireCount", ((BigInteger )createSqlQuery(retireCountSql, params).uniqueResult()).intValue());
		return map;
	}

	@Override
	public void cleanAbnormalRecord() {
		executeBySql(" delete from t_employee where RECORD_STATUS = 2 ");
	}

	@Override
	protected void buildQueryHql(StringBuffer hql, Map<String, Object> params) {
		super.buildQueryHql(hql, params);
		//身份证精确查找
		if (checkParamsKey(params,"cardNo")){
			hql.append(" and cardNo = :cardNo");
		}
		if (checkParamsKey(params, "code")) {
			params.put("code", "%" + params.get("code") + "%");
			hql.append(" and code like :code ");
		}
		if (checkParamsKey(params, "name")) {
			params.put("name", "%" + params.get("name") + "%");
			hql.append(" and name like :name ");
		}
		if (checkParamsKey(params, "sex")) {
			params.put("sex", Integer.parseInt("" + params.get("sex")));
			hql.append(" and sex = :sex ");
		}
		if (checkParamsKey(params, "nativePlaceType")) {
			params.put("nativePlaceType", Integer.parseInt("" + params.get("nativePlaceType")));
			hql.append(" and nativePlaceType = :nativePlaceType ");
		}
		if (checkParamsKey(params, "nativePlaceAddr")) {
			params.put("nativePlaceAddr", "%" + params.get("nativePlaceAddr") + "%");
			hql.append(" and nativePlaceAddr like :nativePlaceAddr ");
		}
		
		if (checkParamsKey(params, "employeeDept")) {
			if (!"all".equals((String ) params.get("employeeDept"))) {
				hql.append(" and employeeDept.id = :employeeDept ");
			} else {
				params.remove("employeeDept");
			}
		} else {
			List<String> authDeptIds = EmployeeDeptCache.getAuthDeptIdsByLoginUser();
			if (CollectionUtils.isNotEmpty(authDeptIds)) {
				params.put("authDeptIds", authDeptIds);
				hql.append(" and employeeDept.id in ( :authDeptIds ) ");
			}
		}
		
		if (checkParamsKey(params, "employeePost")) {
			hql.append(" and employeePost.id = :employeePost ");
		}
		if (checkParamsKey(params, "employeePostType")) {
			params.put("employeePostType", Integer.parseInt("" + params.get("employeePostType")));
			hql.append(" and employeePost.type = :employeePostType ");
		}
		if (checkParamsKey(params, "wagePlan")) {
			hql.append(" and wagePlan.id = :wagePlan ");
		}
		if (checkParamsKey(params, "start_enrtyDate")) {
			params.put("start_enrtyDate", DateUtils.parseDate(params.get("start_enrtyDate")));
			hql.append(" and enrtyDate > :start_enrtyDate ");
		}
		if (checkParamsKey(params, "end_enrtyDate")) {
			params.put("end_enrtyDate", DateUtils.parseDate(params.get("end_enrtyDate")));
			hql.append(" and enrtyDate <= :end_enrtyDate ");
		}
		if (checkParamsKey(params, "start_regularDate")) {
			params.put("start_regularDate", DateUtils.parseDate(params.get("start_regularDate")));
			hql.append(" and regularDate > :start_regularDate ");
		}
		if (checkParamsKey(params, "end_regularDate")) {
			params.put("end_regularDate", DateUtils.parseDate(params.get("end_regularDate")));
			hql.append(" and regularDate <= :end_regularDate ");
		}
		if (checkParamsKey(params, "start_birthDate")) {
			params.put("start_birthDate", DateUtils.parseDate(params.get("start_birthDate")));
			hql.append(" and birthDate > :start_birthDate ");
		}
		if (checkParamsKey(params, "end_birthDate")) {
			params.put("end_birthDate", DateUtils.parseDate(params.get("end_birthDate")));
			hql.append(" and birthDate <= :end_birthDate ");
		}
		if (checkParamsKey(params, "laborType")) {
			params.put("laborType", Integer.parseInt("" + params.get("laborType")));
			hql.append(" and laborType = :laborType ");
		}
		if (checkParamsKey(params, "education")) {
			params.put("education", Integer.parseInt("" + params.get("education")));
			hql.append(" and education = :education ");
		}
		if (checkParamsKey(params, "contractType")) {
			params.put("contractType", Integer.parseInt("" + params.get("contractType")));
			hql.append(" and contractType = :contractType ");
		}
		if (checkParamsKey(params, "contractStartDate")) {
			params.put("contractStartDate", DateUtils.parseDate(params.get("contractStartDate")));
			hql.append(" and contractStartDate > :contractStartDate ");
		}
		if (checkParamsKey(params, "contractEndDate")) {
			params.put("contractEndDate", DateUtils.parseDate(params.get("contractEndDate")));
			hql.append(" and contractEndDate <= :contractEndDate ");
		}
		if (checkParamsKey(params, "start_retareDate")) {
			params.put("start_retareDate", DateUtils.parseDate(params.get("start_retareDate")));
			hql.append(" and retareDate > :start_retareDate ");
		}
		if (checkParamsKey(params, "end_retareDate")) {
			params.put("end_retareDate", DateUtils.parseDate(params.get("end_retareDate")));
			hql.append(" and retareDate <= :end_retareDate ");
		}
		if (checkParamsKey(params, "mealRoomType")) {
			params.put("mealRoomType", Integer.parseInt("" + params.get("mealRoomType")));
			hql.append(" and mealRoomType = :mealRoomType ");
		}
		if (checkParamsKey(params, "hasInsure")) {
			params.put("hasInsure", Integer.parseInt("" + params.get("hasInsure")));
			hql.append(" and hasInsure = :hasInsure ");
		}
		if (checkParamsKey(params, "insureNo")) {
			params.put("insureNo", "%" + params.get("insureNo") + "%");
			hql.append(" and insureNo like :insureNo ");
		}
		if (checkParamsKey(params, "start_insureDate")) {
			params.put("start_insureDate", DateUtils.parseDate(params.get("start_insureDate")));
			hql.append(" and insureDate > :start_insureDate ");
		}
		if (checkParamsKey(params, "end_insureDate")) {
			params.put("end_insureDate", DateUtils.parseDate(params.get("end_insureDate")));
			hql.append(" and insureDate <= :end_insureDate ");
		}
		if (checkParamsKey(params, "driveLicenseType")) {
			params.put("driveLicenseType", Integer.parseInt("" + params.get("driveLicenseType")));
			hql.append(" and driveLicenseType = :driveLicenseType ");
		}
		if (checkParamsKey(params, "hasPublicFund")) {
			params.put("hasPublicFund", Integer.parseInt("" + params.get("hasPublicFund")));
			hql.append(" and hasPublicFund = :hasPublicFund ");
		}
		if (checkParamsKey(params, "start_publicFundDate")) {
			params.put("start_publicFundDate", DateUtils.parseDate(params.get("start_publicFundDate")));
			hql.append(" and publicFundDate > :start_publicFundDate ");
		}
		if (checkParamsKey(params, "end_publicFundDate")) {
			params.put("end_publicFundDate", DateUtils.parseDate(params.get("end_publicFundDate")));
			hql.append(" and publicFundDate <= :end_publicFundDate ");
		}
		if (checkParamsKey(params, "hasQuitCompany")) {
			params.put("hasQuitCompany", Integer.parseInt("" + params.get("hasQuitCompany")));
			hql.append(" and ( hasQuitCompany = :hasQuitCompany or auditStatus = 1 ) ");
		}
		if (checkParamsKey(params, "start_quitCompanyDate")) {
			params.put("start_quitCompanyDate", DateUtils.parseDate(params.get("start_quitCompanyDate")));
			hql.append(" and ( quitCompanyDate is null or quitCompanyDate > :start_quitCompanyDate ) ");
		}
		if (checkParamsKey(params, "end_quitCompanyDate")) {
			params.put("end_quitCompanyDate", DateUtils.parseDate(params.get("end_quitCompanyDate")));
			hql.append(" and ( quitCompanyDate is null or quitCompanyDate <= :end_quitCompanyDate ) ");
		}
		if (checkParamsKey(params, "auditStatus")) {
			params.put("auditStatus", Integer.parseInt("" + params.get("auditStatus")));
			hql.append(" and auditStatus = :auditStatus ");
		}
		if (checkParamsKey(params, "performanceWageType")) {
			params.put("performanceWageType", Integer.parseInt("" + params.get("performanceWageType")));
			hql.append(" and performanceWageType = :performanceWageType ");
		}
		hql.append(" and recordStatus = 1 order by modifyDate desc");
	}

}
