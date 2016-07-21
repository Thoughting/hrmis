-- 入职人数
select count(*) from (
select * from t_employee where RECORD_STATUS = 1 AND AUDIT_STATUS = 2 and HAS_QUIT_COMPANY = 0
) t where t.enrty_date >= '2016-03-01' and t.enrty_date < '2016-04-01'
/
--转正人数
select count(*) from (
select * from t_employee where RECORD_STATUS = 1 AND AUDIT_STATUS = 2 and HAS_QUIT_COMPANY = 0 and IS_REGULAR = 1
) t where t.regular_date >= '2016-01-01' and t.regular_date < '2016-02-01'
/
-- 离职人数
select count(*) from (
select * from t_employee where RECORD_STATUS = 1 AND AUDIT_STATUS = 2 and HAS_QUIT_COMPANY = 1
) t where t.quit_company_date >= '2016-03-01' and t.quit_company_date < '2016-04-01'
/
--合同到期人数
select count(*) from (
select * from t_employee where RECORD_STATUS = 1 AND AUDIT_STATUS = 2 and HAS_QUIT_COMPANY = 0
) t where t.contract_end_date >= '2016-03-01' and t.contract_end_date < '2016-04-01'
/
--参保人员数
select count(*) from (
select * from t_employee where RECORD_STATUS = 1 AND AUDIT_STATUS = 2 and HAS_QUIT_COMPANY = 0
) t where t.enrty_date >= '2016-03-01' and t.enrty_date < '2016-04-01' and HAS_PERSION_INSURE = 1
/
--未参保人员数
select count(*) from (
select * from t_employee where RECORD_STATUS = 1 AND AUDIT_STATUS = 2 and HAS_QUIT_COMPANY = 0
) t where t.enrty_date >= '2016-03-01' and t.enrty_date < '2016-04-01' and HAS_PERSION_INSURE = 0
/
--退休人员数
select count(*) from (
select * from t_employee where RECORD_STATUS = 1 AND AUDIT_STATUS = 2 and HAS_QUIT_COMPANY = 0
) t where t.retire_date >= '2042-05-01' and t.retire_date < '2042-06-01'
