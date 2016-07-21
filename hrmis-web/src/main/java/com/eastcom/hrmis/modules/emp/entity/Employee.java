package com.eastcom.hrmis.modules.emp.entity;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.eastcom.baseframe.common.utils.DateUtils;
import com.eastcom.baseframe.web.modules.sys.cache.DictCache;
import com.eastcom.hrmis.modules.emp.EmpConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 员工基本信息
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_EMPLOYEE")
@DynamicInsert
@DynamicUpdate
public class Employee extends IdEntity<Employee> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4094965622237902398L;
	
	private EmployeeDept employeeDept; // 所属部门
	private EmployeePost employeePost; // 所属岗位
	private WagePlan wagePlan; // 所属工资方案
	private Integer overTimeRate = 1; // 加班计算方式 1：正常计算，2：含年假计算，3：按小时计算，4：不计加班，5：按星期六加班
	
	private List<EmployeeFamily> families = Lists.newArrayList(); // 家庭成员
	private List<EmployeeStudy> studies = Lists.newArrayList(); // 学习经历
	private List<EmployeeWork> works = Lists.newArrayList(); // 工作经历
	
	private List<EmployeeAnnex> annexs = Lists.newArrayList(); // 员工附件清单
	private List<EmployeeContractHomeAnnex> homeAnnexs = Lists.newArrayList(); // 合同首页附件清单
	private List<EmployeeContractFinalAnnex> finalAnnexs = Lists.newArrayList(); // 合同尾页附件清单
	
	private List<EmployeeSetWorkPlan> setWorkPlans = Lists.newArrayList(); // 员工排班计划
	private List<EmployeeSetWork> setWorks = Lists.newArrayList(); // 员工排班信息
	
	private List<EmployeeWageItem> wageItems = Lists.newArrayList(); // 员工工资项
	
	private List<EmployeeOrder> agencyOrders = Lists.newArrayList(); // 员工代办工单清单
	
	private String code; // 编号
	private String name; // 姓名
	private byte[] headerImg; // 头像
	private String headerImgName; // 头像路径
	private Integer sex = 1; // 性别
	private String nation; // 民族
	private String height; // 身高
	private String cardNo; // 身份证号
	private Date cardNoValidDate = new Date(); // 身份证有效期
	private Integer isCardNoLongTerm = 0; // 身份证是否长期有效
	private Integer age; // 年龄
	private Date birthDate; // 出生日期
	private Integer polity = 1; // 政治面貌（字典）
	private Integer education = 1; // 学历（字典）
	private Integer manageLevel = 1; // 组织管理级序（字典）
	private String jobTitle; // 职称
	private String jobCapacity; // 职业资格
	private Integer driveLicenseType = 1; // 驾驶证类别
	private Date driveLicenseGetDate = new Date(); // 驾驶证拿证日期
	private Date driveLicenseValidDate = new Date(); // 驾驶证有效期
	private String major; // 专业
	private String nativePlace; // 籍贯
	private Integer nativePlaceType = 1; // 户籍性质（字典）
	private String nativePlaceAddr; // 户籍地址
	private String characterRemark; // 性格特征
	private String contactAddr; // 通讯地址
	private String telephone; // 本人手机号码
	private String emergentName; // 紧急人姓名
	private String emergentTelephone; // 紧急人手机号码
	private Integer marryType = 1; // 婚姻状况
	private Date enrtyDate = new Date(); // 入职时间
	private Integer enrtyDateType; // 入职时间类型
	private Date regularDate = new Date(); // 转正时间（第一阶段）
	private Date regularDateTwo = new Date(); // 转正时间（第二阶段）
	private Integer isRegular = 0; // 是否转正
	private Date regularHandleDate = new Date(); // 转正操作时间
	private Date retireDate; // 退休时间
	private Integer performanceWageType = 0; // 绩效工资类型
	private Integer laborType = 1; // 用工形式（字典）
	private Integer bankType = 1; // 银行卡类型（字典）
	private String bankCard; // 银行卡号
	private Integer mealRoomType = 0; // 食宿情况（字典）
	private Integer hasRiskAgreement; // 是否有风险协议
	private byte[] riskAgreement; // 风险协议附件
	private Integer hasPercentAgreement; // 是否有提成协议
	private byte[] percentAgreement; // 提成协议附件
	private Integer hasLaborDispute; // 是否与前公司有劳动纠纷
	private String laborDisputeResult; // 劳动纠纷原因
	private String enrtyIntorducerCompany; // 入职介绍单位
	private String enrtyIntorducer; // 入职介绍人
	private Integer hasDiseaseHistory; // 是否有患病史
	private String diseaseHistory; // 所患疾病
	private Integer hasFriendInCompany; // 是否有亲戚（朋友）在本公司任职
	private String friendDept; // 亲戚（朋友）所在项目部门
	private String friendName; // 亲戚（朋友）姓名
	private String friendJobTitle; // 亲戚（朋友）职务
	private Integer hasQuitCompany; // 是否离职
	private Date quitCompanyDate = new Date(); // 离职日期
	private Integer quitCompanyType; // 离职种类（字典）
	private String quitCompanyResult; // 离职原因
	private Integer contractType = 0; // 员工合同种类（字典）
	private Date contractStartDate = new Date(); // 合同开始时间
	private Date contractEndDate = new Date(); // 合同结束时间
	private Integer contractSignDateType = 1; // 合同签订时间类型
	private Date contractSignDate = new Date(); // 合同签订时间
	private Integer contractTermCond = 0; // 法定终止条件出现
	private Integer hasSignForm; // 是否有签收表
	private byte[] signForm; // 签收表附件
	private String signFormName; // 签收表附件名称
	private Integer hasInsure; // 是否参保
	private String insureNo; // 社保号
	private Date insureDate = new Date(); // 社保起始时间
	private Double insurePayBase; // 社保缴费基数
	private Integer hasPersionInsure; // 是否有养老保险
	private Integer hasInjuryInsure; // 是否有工伤保险
	private Integer hasBirthInsure; // 是否有生育保险
	private Integer hasMedicalInsure; // 是否有医疗保险
	private Integer hasSeriousInsure; // 是否有重病保险
	private Integer hasGsbInsure; // 是否有工伤宝
	private Integer hasNonPurchaseCommit; // 是否有不购承诺
	private byte[] nonPurchaseCommit; // 不购承诺附件
	private String nonPurchaseCommitName; // 不购承诺附件名称
	private Integer hasPublicFund; // 是否有公积金
	private Date publicFundDate = new Date(); // 公积金起始时间
	private Double publicFundPayBase; // 公积金缴费
	
	private String postChangeRemark; // 岗位变更描述
	private byte[] postChangeAnnex; // 岗位变更附件
	private String postChangeAnnexName; // 岗位变更附件名称
	private String deptChangeRemark; // 部门变更描述
	private byte[] deptChangeAnnex; // 部门变更附件
	private String deptChangeAnnexName; // 部门变更附件名称
	private String operaChangeRemark; // 操作变更描述
	private byte[] operaChangeAnnex; // 操作变更附件
	private String operaChangeAnnexName; // 操作变更附件名称
	private String wageChangeRemark; // 工资变更描述
	private byte[] wageChangeAnnex; // 工资变更附件
	private String wageChangeAnnexName; // 工资变更附件名称
	
	private Date createDate = new Date(); // 创建时间
	private String modifyer; // 修改人
	private Date modifyDate = new Date(); // 修改时间
	private Integer recordStatus = 2; // 记录状态  1正常 2正在修改 0已删除
	private Integer auditStatus = 0; // 审核状态 0 未提交审核 1 审核中 2 审核通过 3 审核不通过
	private String auditContent; // 审核内容
	private String remark; // 备注
	
	public Employee() {
		this.sex = 1;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public byte[] getHeaderImg() {
		return headerImg;
	}

	public void setHeaderImg(byte[] headerImg) {
		this.headerImg = headerImg;
	}
	
	public String getHeaderImgName() {
		return headerImgName;
	}

	public void setHeaderImgName(String headerImgName) {
		this.headerImgName = headerImgName;
	}
	
	public Integer getSex() {
		return this.sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getNation() {
		return this.nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getHeight() {
		return this.height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getCardNoValidDate() {
		return this.cardNoValidDate;
	}

	public void setCardNoValidDate(Date cardNoValidDate) {
		this.cardNoValidDate = cardNoValidDate;
	}

	public Integer getIsCardNoLongTerm() {
		return isCardNoLongTerm;
	}

	public void setIsCardNoLongTerm(Integer isCardNoLongTerm) {
		this.isCardNoLongTerm = isCardNoLongTerm;
	}
	
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Integer getPolity() {
		return this.polity;
	}

	public void setPolity(Integer polity) {
		this.polity = polity;
	}

	public Integer getEducation() {
		return this.education;
	}

	public void setEducation(Integer education) {
		this.education = education;
	}

	public Integer getManageLevel() {
		return manageLevel;
	}

	public void setManageLevel(Integer manageLevel) {
		this.manageLevel = manageLevel;
	}
	
	public String getJobTitle() {
		return this.jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getJobCapacity() {
		return this.jobCapacity;
	}

	public void setJobCapacity(String jobCapacity) {
		this.jobCapacity = jobCapacity;
	}

	public Integer getDriveLicenseType() {
		return driveLicenseType;
	}

	public void setDriveLicenseType(Integer driveLicenseType) {
		this.driveLicenseType = driveLicenseType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getDriveLicenseGetDate() {
		return driveLicenseGetDate;
	}

	public void setDriveLicenseGetDate(Date driveLicenseGetDate) {
		this.driveLicenseGetDate = driveLicenseGetDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getDriveLicenseValidDate() {
		return this.driveLicenseValidDate;
	}

	public void setDriveLicenseValidDate(Date driveLicenseValidDate) {
		this.driveLicenseValidDate = driveLicenseValidDate;
	}

	public String getMajor() {
		return this.major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getNativePlace() {
		return this.nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public Integer getNativePlaceType() {
		return this.nativePlaceType;
	}

	public void setNativePlaceType(Integer nativePlaceType) {
		this.nativePlaceType = nativePlaceType;
	}

	public String getNativePlaceAddr() {
		return this.nativePlaceAddr;
	}

	public void setNativePlaceAddr(String nativePlaceAddr) {
		this.nativePlaceAddr = nativePlaceAddr;
	}

	public String getCharacterRemark() {
		return this.characterRemark;
	}

	public void setCharacterRemark(String characterRemark) {
		this.characterRemark = characterRemark;
	}

	public String getContactAddr() {
		return this.contactAddr;
	}

	public void setContactAddr(String contactAddr) {
		this.contactAddr = contactAddr;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmergentName() {
		return this.emergentName;
	}

	public void setEmergentName(String emergentName) {
		this.emergentName = emergentName;
	}

	public String getEmergentTelephone() {
		return this.emergentTelephone;
	}

	public void setEmergentTelephone(String emergentTelephone) {
		this.emergentTelephone = emergentTelephone;
	}

	public Integer getMarryType() {
		return this.marryType;
	}

	public void setMarryType(Integer marryType) {
		this.marryType = marryType;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getEnrtyDate() {
		return this.enrtyDate;
	}

	public void setEnrtyDate(Date enrtyDate) {
		this.enrtyDate = enrtyDate;
	}

	public Integer getEnrtyDateType() {
		return enrtyDateType;
	}

	public void setEnrtyDateType(Integer enrtyDateType) {
		this.enrtyDateType = enrtyDateType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getRegularDate() {
		return this.regularDate;
	}

	public void setRegularDate(Date regularDate) {
		this.regularDate = regularDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getRegularDateTwo() {
		return regularDateTwo;
	}

	public void setRegularDateTwo(Date regularDateTwo) {
		this.regularDateTwo = regularDateTwo;
	}
	
	public Integer getIsRegular() {
		return isRegular;
	}

	public void setIsRegular(Integer isRegular) {
		this.isRegular = isRegular;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getRegularHandleDate() {
		return regularHandleDate;
	}

	public void setRegularHandleDate(Date regularHandleDate) {
		this.regularHandleDate = regularHandleDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getRetireDate() {
		return this.retireDate;
	}

	public void setRetireDate(Date retireDate) {
		this.retireDate = retireDate;
	}

	public Integer getPerformanceWageType() {
		return performanceWageType;
	}

	public void setPerformanceWageType(Integer performanceWageType) {
		this.performanceWageType = performanceWageType;
	}
	
	public Integer getLaborType() {
		return this.laborType;
	}

	public void setLaborType(Integer laborType) {
		this.laborType = laborType;
	}

	public Integer getBankType() {
		return bankType;
	}

	public void setBankType(Integer bankType) {
		this.bankType = bankType;
	}
	
	public String getBankCard() {
		return this.bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public Integer getMealRoomType() {
		return this.mealRoomType;
	}

	public void setMealRoomType(Integer mealRoomType) {
		this.mealRoomType = mealRoomType;
	}

	public Integer getHasRiskAgreement() {
		return this.hasRiskAgreement;
	}

	public void setHasRiskAgreement(Integer hasRiskAgreement) {
		this.hasRiskAgreement = hasRiskAgreement;
	}

	@JsonIgnore
	public byte[] getRiskAgreement() {
		return this.riskAgreement;
	}

	public void setRiskAgreement(byte[] riskAgreement) {
		this.riskAgreement = riskAgreement;
	}

	public Integer getHasPercentAgreement() {
		return this.hasPercentAgreement;
	}

	public void setHasPercentAgreement(Integer hasPercentAgreement) {
		this.hasPercentAgreement = hasPercentAgreement;
	}

	@JsonIgnore
	public byte[] getPercentAgreement() {
		return this.percentAgreement;
	}

	public void setPercentAgreement(byte[] percentAgreement) {
		this.percentAgreement = percentAgreement;
	}

	public Integer getHasLaborDispute() {
		return this.hasLaborDispute;
	}

	public void setHasLaborDispute(Integer hasLaborDispute) {
		this.hasLaborDispute = hasLaborDispute;
	}

	public String getLaborDisputeResult() {
		return this.laborDisputeResult;
	}

	public void setLaborDisputeResult(String laborDisputeResult) {
		this.laborDisputeResult = laborDisputeResult;
	}

	public String getEnrtyIntorducerCompany() {
		return enrtyIntorducerCompany;
	}

	public void setEnrtyIntorducerCompany(String enrtyIntorducerCompany) {
		this.enrtyIntorducerCompany = enrtyIntorducerCompany;
	}
	
	public String getEnrtyIntorducer() {
		return this.enrtyIntorducer;
	}

	public void setEnrtyIntorducer(String enrtyIntorducer) {
		this.enrtyIntorducer = enrtyIntorducer;
	}

	public Integer getHasDiseaseHistory() {
		return this.hasDiseaseHistory;
	}

	public void setHasDiseaseHistory(Integer hasDiseaseHistory) {
		this.hasDiseaseHistory = hasDiseaseHistory;
	}

	public String getDiseaseHistory() {
		return this.diseaseHistory;
	}

	public void setDiseaseHistory(String diseaseHistory) {
		this.diseaseHistory = diseaseHistory;
	}

	public Integer getHasFriendInCompany() {
		return this.hasFriendInCompany;
	}

	public void setHasFriendInCompany(Integer hasFriendInCompany) {
		this.hasFriendInCompany = hasFriendInCompany;
	}

	public String getFriendDept() {
		return this.friendDept;
	}

	public void setFriendDept(String friendDept) {
		this.friendDept = friendDept;
	}

	public String getFriendName() {
		return this.friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public String getFriendJobTitle() {
		return this.friendJobTitle;
	}

	public void setFriendJobTitle(String friendJobTitle) {
		this.friendJobTitle = friendJobTitle;
	}

	public Integer getHasQuitCompany() {
		return this.hasQuitCompany;
	}

	public void setHasQuitCompany(Integer hasQuitCompany) {
		this.hasQuitCompany = hasQuitCompany;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getQuitCompanyDate() {
		return this.quitCompanyDate;
	}

	public void setQuitCompanyDate(Date quitCompanyDate) {
		this.quitCompanyDate = quitCompanyDate;
	}

	public Integer getQuitCompanyType() {
		return this.quitCompanyType;
	}

	public void setQuitCompanyType(Integer quitCompanyType) {
		this.quitCompanyType = quitCompanyType;
	}

	public String getQuitCompanyResult() {
		return this.quitCompanyResult;
	}

	public void setQuitCompanyResult(String quitCompanyResult) {
		this.quitCompanyResult = quitCompanyResult;
	}

	public Integer getContractType() {
		return contractType;
	}

	public void setContractType(Integer contractType) {
		this.contractType = contractType;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public Integer getHasSignForm() {
		return hasSignForm;
	}

	public void setHasSignForm(Integer hasSignForm) {
		this.hasSignForm = hasSignForm;
	}

	@JsonIgnore
	public byte[] getSignForm() {
		return signForm;
	}

	public void setSignForm(byte[] signForm) {
		this.signForm = signForm;
	}
	
	public String getSignFormName() {
		return signFormName;
	}

	public void setSignFormName(String signFormName) {
		this.signFormName = signFormName;
	}
	
	public Integer getHasInsure() {
		return hasInsure;
	}

	public void setHasInsure(Integer hasInsure) {
		this.hasInsure = hasInsure;
	}
	
	public String getInsureNo() {
		return insureNo;
	}

	public void setInsureNo(String insureNo) {
		this.insureNo = insureNo;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getInsureDate() {
		return insureDate;
	}

	public void setInsureDate(Date insureDate) {
		this.insureDate = insureDate;
	}

	public Double getInsurePayBase() {
		return insurePayBase;
	}

	public void setInsurePayBase(Double insurePayBase) {
		this.insurePayBase = insurePayBase;
	}

	public Integer getHasPersionInsure() {
		return hasPersionInsure;
	}

	public void setHasPersionInsure(Integer hasPersionInsure) {
		this.hasPersionInsure = hasPersionInsure;
	}

	public Integer getHasInjuryInsure() {
		return hasInjuryInsure;
	}

	public void setHasInjuryInsure(Integer hasInjuryInsure) {
		this.hasInjuryInsure = hasInjuryInsure;
	}

	public Integer getHasBirthInsure() {
		return hasBirthInsure;
	}

	public void setHasBirthInsure(Integer hasBirthInsure) {
		this.hasBirthInsure = hasBirthInsure;
	}

	public Integer getHasMedicalInsure() {
		return hasMedicalInsure;
	}

	public void setHasMedicalInsure(Integer hasMedicalInsure) {
		this.hasMedicalInsure = hasMedicalInsure;
	}

	public Integer getHasSeriousInsure() {
		return hasSeriousInsure;
	}

	public void setHasSeriousInsure(Integer hasSeriousInsure) {
		this.hasSeriousInsure = hasSeriousInsure;
	}

	public Integer getHasGsbInsure() {
		return hasGsbInsure;
	}

	public void setHasGsbInsure(Integer hasGsbInsure) {
		this.hasGsbInsure = hasGsbInsure;
	}
	
	public Integer getHasNonPurchaseCommit() {
		return hasNonPurchaseCommit;
	}

	public void setHasNonPurchaseCommit(Integer hasNonPurchaseCommit) {
		this.hasNonPurchaseCommit = hasNonPurchaseCommit;
	}

	@JsonIgnore
	public byte[] getNonPurchaseCommit() {
		return nonPurchaseCommit;
	}

	public void setNonPurchaseCommit(byte[] nonPurchaseCommit) {
		this.nonPurchaseCommit = nonPurchaseCommit;
	}

	public String getNonPurchaseCommitName() {
		return nonPurchaseCommitName;
	}

	public void setNonPurchaseCommitName(String nonPurchaseCommitName) {
		this.nonPurchaseCommitName = nonPurchaseCommitName;
	}
	
	public Integer getHasPublicFund() {
		return hasPublicFund;
	}

	public void setHasPublicFund(Integer hasPublicFund) {
		this.hasPublicFund = hasPublicFund;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getPublicFundDate() {
		return publicFundDate;
	}

	public void setPublicFundDate(Date publicFundDate) {
		this.publicFundDate = publicFundDate;
	}

	public Double getPublicFundPayBase() {
		return publicFundPayBase;
	}

	public void setPublicFundPayBase(Double publicFundPayBase) {
		this.publicFundPayBase = publicFundPayBase;
	}

	public Integer getContractSignDateType() {
		return contractSignDateType;
	}

	public void setContractSignDateType(Integer contractSignDateType) {
		this.contractSignDateType = contractSignDateType;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getContractSignDate() {
		return contractSignDate;
	}

	public void setContractSignDate(Date contractSignDate) {
		this.contractSignDate = contractSignDate;
	}

	public Integer getContractTermCond() {
		return contractTermCond;
	}

	public void setContractTermCond(Integer contractTermCond) {
		this.contractTermCond = contractTermCond;
	}

	public String getPostChangeRemark() {
		return postChangeRemark;
	}

	public void setPostChangeRemark(String postChangeRemark) {
		this.postChangeRemark = postChangeRemark;
	}

	@JsonIgnore
	public byte[] getPostChangeAnnex() {
		return postChangeAnnex;
	}

	public void setPostChangeAnnex(byte[] postChangeAnnex) {
		this.postChangeAnnex = postChangeAnnex;
	}

	public String getDeptChangeRemark() {
		return deptChangeRemark;
	}

	public void setDeptChangeRemark(String deptChangeRemark) {
		this.deptChangeRemark = deptChangeRemark;
	}

	@JsonIgnore
	public byte[] getDeptChangeAnnex() {
		return deptChangeAnnex;
	}

	public void setDeptChangeAnnex(byte[] deptChangeAnnex) {
		this.deptChangeAnnex = deptChangeAnnex;
	}

	public String getOperaChangeRemark() {
		return operaChangeRemark;
	}

	public void setOperaChangeRemark(String operaChangeRemark) {
		this.operaChangeRemark = operaChangeRemark;
	}

	@JsonIgnore
	public byte[] getOperaChangeAnnex() {
		return operaChangeAnnex;
	}

	public void setOperaChangeAnnex(byte[] operaChangeAnnex) {
		this.operaChangeAnnex = operaChangeAnnex;
	}

	public String getWageChangeRemark() {
		return wageChangeRemark;
	}

	public void setWageChangeRemark(String wageChangeRemark) {
		this.wageChangeRemark = wageChangeRemark;
	}

	@JsonIgnore
	public byte[] getWageChangeAnnex() {
		return wageChangeAnnex;
	}

	public void setWageChangeAnnex(byte[] wageChangeAnnex) {
		this.wageChangeAnnex = wageChangeAnnex;
	}
	
	public String getPostChangeAnnexName() {
		return postChangeAnnexName;
	}

	public void setPostChangeAnnexName(String postChangeAnnexName) {
		this.postChangeAnnexName = postChangeAnnexName;
	}

	public String getDeptChangeAnnexName() {
		return deptChangeAnnexName;
	}

	public void setDeptChangeAnnexName(String deptChangeAnnexName) {
		this.deptChangeAnnexName = deptChangeAnnexName;
	}

	public String getOperaChangeAnnexName() {
		return operaChangeAnnexName;
	}

	public void setOperaChangeAnnexName(String operaChangeAnnexName) {
		this.operaChangeAnnexName = operaChangeAnnexName;
	}

	public String getWageChangeAnnexName() {
		return wageChangeAnnexName;
	}

	public void setWageChangeAnnexName(String wageChangeAnnexName) {
		this.wageChangeAnnexName = wageChangeAnnexName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getModifyer() {
		return modifyer;
	}

	public void setModifyer(String modifyer) {
		this.modifyer = modifyer;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	public String getAuditContent() {
		return auditContent;
	}

	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "EMPLOYEE_DEPT_ID", referencedColumnName = "id")
	public EmployeeDept getEmployeeDept() {
		return employeeDept;
	}

	public void setEmployeeDept(EmployeeDept employeeDept) {
		this.employeeDept = employeeDept;
	}
	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "EMPLOYEE_POST_ID", referencedColumnName = "id")
	public EmployeePost getEmployeePost() {
		return employeePost;
	}

	public void setEmployeePost(EmployeePost employeePost) {
		this.employeePost = employeePost;
	}
	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "WAGE_PLAN_ID", referencedColumnName = "id")
	public WagePlan getWagePlan() {
		return wagePlan;
	}

	public void setWagePlan(WagePlan wagePlan) {
		this.wagePlan = wagePlan;
	}
	
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	public List<EmployeeFamily> getFamilies() {
		return families;
	}

	public void setFamilies(List<EmployeeFamily> families) {
		this.families = families;
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<EmployeeStudy> getStudies() {
		return studies;
	}

	public void setStudies(List<EmployeeStudy> studies) {
		this.studies = studies;
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<EmployeeWork> getWorks() {
		return works;
	}

	public void setWorks(List<EmployeeWork> works) {
		this.works = works;
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<EmployeeAnnex> getAnnexs() {
		return annexs;
	}

	public void setAnnexs(List<EmployeeAnnex> annexs) {
		this.annexs = annexs;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<EmployeeContractHomeAnnex> getHomeAnnexs() {
		return homeAnnexs;
	}

	public void setHomeAnnexs(List<EmployeeContractHomeAnnex> homeAnnexs) {
		this.homeAnnexs = homeAnnexs;
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<EmployeeContractFinalAnnex> getFinalAnnexs() {
		return finalAnnexs;
	}

	public void setFinalAnnexs(List<EmployeeContractFinalAnnex> finalAnnexs) {
		this.finalAnnexs = finalAnnexs;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<EmployeeSetWorkPlan> getSetWorkPlans() {
		return setWorkPlans;
	}

	public void setSetWorkPlans(List<EmployeeSetWorkPlan> setWorkPlans) {
		this.setWorkPlans = setWorkPlans;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<EmployeeSetWork> getSetWorks() {
		return setWorks;
	}

	public void setSetWorks(List<EmployeeSetWork> setWorks) {
		this.setWorks = setWorks;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<EmployeeWageItem> getWageItems() {
		return wageItems;
	}

	public void setWageItems(List<EmployeeWageItem> wageItems) {
		this.wageItems = wageItems;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<EmployeeOrder> getAgencyOrders() {
		return agencyOrders;
	}

	public void setAgencyOrders(List<EmployeeOrder> agencyOrders) {
		this.agencyOrders = agencyOrders;
	}
	
	/********************非数据库字段********************/
	@Transient
	public String getEmployeeDeptName() {
		if (this.employeeDept != null) {
			return this.employeeDept.getName();
		}
		return null;
	}
	
	@Transient
	public String getEmployeePostTypeDict() {
		if (this.employeePost != null) {
			return this.employeePost.getTypeDict();
		}
		return null;
	}
	
	@Transient
	public String getEmployeePostName() {
		if (this.employeePost != null) {
			return this.employeePost.getName();
		}
		return null;
	}
	
	@Transient
	public String getPerformanceWageTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_PERFORMANCE_WAGE_TYPE, this.performanceWageType);
	}
	
	@Transient
	public String getLaborTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_EMPLOYEE_LABOR_TYPE, this.laborType);
	}
	
	@Transient
	public String getEducationDict(){
		return DictCache.getDictValue(EmpConstant.DICT_EMPLOYEE_EDUCATION_TYPE, this.education);
	}
	
	@Transient
	public String getMealRoomTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_EMPLOYEE_MEALROOM_TYPE, this.mealRoomType);
	}
	
	@Transient
	public String getNativePlaceTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_EMPLOYEE_NATIVE_PLACT_TYPE, this.nativePlaceType);
	}
	
	@Transient
	public String getContractTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_EMPLOYEE_CONTRACT_TYPE, this.contractType);
	}
	
	@Transient
	public String getDriveLicenseTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_EMPLOYEE_DRIVELICENSE_TYPE, this.driveLicenseType);
	}

	@Transient
	public String getEnrtyDateTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_MORNING_AFTERNOON_TYPE, this.enrtyDateType);
	}

	@Transient
	public String getIsRegularDict(){
		return DictCache.getYesOrNo(this.isRegular);
	}
	
	@Transient
	public String getSexDict(){
		return DictCache.getBoyOrGrid(this.sex);
	}

	@Transient
	public String getManageLevelDict(){
		return DictCache.getDictValue(EmpConstant.DICT_MANAGE_LEVEL, this.manageLevel);
	}

	@Transient
	public String getMarryTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_EMPLOYEE_MARRY_TYPE, this.marryType);
	}

	@Transient
	public String getPolityDict(){
		return DictCache.getDictValue(EmpConstant.DICT_EMPLOYEE_POLITY_TYPE, this.polity);
	}

	@Transient
	public String getBankTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_EMPLOYEE_BANK_TYPE, this.bankType);
	}

	@Transient
	public String getQuitCompanyTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_EMPLOYEE_QUITCOMPANY_TYPE, this.quitCompanyType);
	}

	@Transient
	public String getHasContractHomeAnnex(){
		if (this.homeAnnexs.size() > 0) {
			return "有";
		}
		return "无";
	}
	
	@Transient
	public String getHasContractFinalAnnex(){
		if (this.finalAnnexs.size() > 0) {
			return "有";
		}
		return "无";
	}
	
	@Transient
	public String getHasRiskAgreementDict(){
		return DictCache.getHaveOrNotHave(this.hasRiskAgreement == null ? 0 : this.hasRiskAgreement);
	}
	
	@Transient
	public String getHasPercentAgreementDict(){
		return DictCache.getHaveOrNotHave(this.hasPercentAgreement == null ? 0 : this.hasBirthInsure);
	}
	
	@Transient
	public String getHasSignFormDict(){
		return DictCache.getHaveOrNotHave(this.hasSignForm == null ? 0 : this.hasBirthInsure);
	}
	
	@Transient
	public String getHasPersionInsureDict(){
		return DictCache.getHaveOrNotHave(this.hasPersionInsure == null ? 0 : this.hasBirthInsure);
	}
	
	@Transient
	public String getHasInjuryInsureDict(){
		return DictCache.getHaveOrNotHave(this.hasInjuryInsure == null ? 0 : this.hasBirthInsure);
	}
	
	@Transient
	public String getHasBirthInsureDict(){
		return DictCache.getHaveOrNotHave(this.hasBirthInsure == null ? 0 : this.hasBirthInsure);
	}
	
	@Transient
	public String getHasMedicalInsureDict(){
		return DictCache.getHaveOrNotHave(this.hasMedicalInsure == null ? 0 : this.hasBirthInsure);
	}
	
	
	@Transient
	public String getHasSeriousInsureDict(){
		return DictCache.getHaveOrNotHave(this.hasSeriousInsure == null ? 0 : this.hasBirthInsure);
	}
	
	@Transient
	public String getHasGsbInsureDict(){
		return DictCache.getHaveOrNotHave(this.hasGsbInsure == null ? 0 : this.hasBirthInsure);
	}
	
	@Transient
	public String getHasNonPurchaseCommitDict(){
		return DictCache.getHaveOrNotHave(this.hasNonPurchaseCommit == null ? 0 : this.hasBirthInsure);
	}
	
	@Transient
	public String getHasPublicFundDict(){
		return DictCache.getHaveOrNotHave(this.hasPublicFund == null ? 0 : this.hasBirthInsure);
	}

	/**
	 * 工龄
	 * @return
	 */
	@Transient
	public Integer getWorkYear(){
		if (this.enrtyDate != null) {
			return (int) Math.floor(DateUtils.getDistanceOfTwoDate(this.enrtyDate, new Date()) / 365);
		}
		return 0;
	}
	
	/**
	 * 年假
	 * @return
	 */
	@Transient
	public Integer getAnnualVacation(){
		Integer d = getWorkYear();
		if (d >= 1 && d < 10) {
			return 5;
		} else if (d >= 10 && d < 20){
			return 10;
		} else if (d >= 20) {
			return 15;
		}
		return 0;
	}

	/**
	 * 审核状态
	 * @return
	 */
	@Transient
	public String getAuditStatusDict(){
		return DictCache.getDictValue(EmpConstant.DICT_EMPLOYEE_AUDITSTATUS_TYPE, this.auditStatus);
	}

	@Transient
	public String getCardNoValidDateDesc(){
		if (this.getIsCardNoLongTerm() == 1) {
			return "长期";
		}
		if (this.getCardNoValidDate() != null) {
			return DateUtils.formatDate(this.getCardNoValidDate());
		}
		return "";
	}

	public Integer getOverTimeRate() {
		return overTimeRate;
	}

	public void setOverTimeRate(Integer overTimeRate) {
		this.overTimeRate = overTimeRate;
	}

	@Transient
	public String getIsRetire(){
		if (this.getRetireDate() != null && DateUtils.getDistanceOfTwoDate(new Date(), this.getRetireDate()) <= 0) {
			return "是";
		}
		return "否";
	}
}
