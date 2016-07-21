<%@page import="com.eastcom.hrmis.modules.emp.EmpConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<html>
<head>
<title>员工档案详情</title>
<%@include file="/pages/include/head.jsp"%>
<style type="text/css">
	.panelHeader{
		text-align: center;
	}
</style>
<script src="${ctxPlugins }/jquery.upload.js"></script>
<script type="text/javascript" src="${ctxScripts}/common/uploader.js"></script>
<script type="text/javascript">
var employeeId = "${employee.id}";
var saveFlag = "${saveFlag}";
var addOrUpdate = "${addOrUpdate}";
</script>
<script src="${ctxScripts}/modules/emp/archives/employeeMgr_detail.js"></script>
</head>

<body style="background: none;">
	<!-- 基本信息   -->
	<div class="easyui-panel" style="width:1113px;" title="基础信息" headerCls="panelHeader">
		<table class="table-content-form" style="background: none;">
			<tbody>
			<tr>
				<th>编号：</th>
				<td>${employee.code}&nbsp;</td>
				<th>姓名：</th>
				<td>${employee.name}&nbsp;</td>
				<th>性别：</th>
				<td style="border-right: 1px #afafaf solid">
					${employee.sexDict}&nbsp;
				</td>
				<td rowspan="6" width="115px;" align="center" style="padding-left: 0px;">
					<a id="headerImgBtn" href="javascript:void(0)" style="text-decoration:none;" title="点击头像上传">
						<img id="headerImg" width="150" height="200" alt="点击更换头像" />
					</a>
				</td>
			</tr>
			<tr>
				<th>身份证号：</th>
				<td>${employee.cardNo}&nbsp;</td>
				<th>身份证有效期：</th>
				<td>
					<c:choose>
						<c:when test="${employee.isCardNoLongTerm == 1}">
							长期
						</c:when>
						<c:otherwise>
							<fmt:formatDate value="${employee.cardNoValidDate}" pattern="yyyy-MM-dd"/>&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<th>出生日期：</th>
				<td style="border-right: 1px #afafaf solid">
					<fmt:formatDate value="${employee.birthDate}" pattern="yyyy-MM-dd"/>&nbsp;
					(${employee.age}岁)
				</td>
			</tr>
			<tr>
				<th>退休时间：</th>
				<td>
					<fmt:formatDate value="${employee.retireDate}" pattern="yyyy-MM-dd"/>&nbsp;
				</td>
				<th>入职时间：<font color="red">*</font></th>
				<td>
					<fmt:formatDate value="${employee.enrtyDate}" pattern="yyyy-MM-dd"/>&nbsp;
					(${employee.enrtyDateTypeDict})
				</td>
				<th>合同签订日期：</th>
				<td style="border-right: 1px #afafaf solid">
					<c:choose>
						<c:when test="${employee.contractSignDateType == 1}">
							首次签订
						</c:when>
						<c:when test="${employee.contractSignDateType == 2}">
							二次签订
						</c:when>
						<c:when test="${employee.contractSignDateType == 3}">
							三次签订
						</c:when>
						<c:otherwise>
							无
						</c:otherwise>
					</c:choose>
					<fmt:formatDate value="${employee.contractSignDate}" pattern="yyyy-MM-dd"/>
				</td>
			</tr>
			<tr>
				<th>合同期限：<font color="red">*</font></th>
				<td>
					<fmt:formatDate value="${employee.contractStartDate}" pattern="yyyy-MM-dd"/>
					至
					<c:choose>
						<c:when test="${employee.contractTermCond == 1}">
							法定终止条件出现
						</c:when>
						<c:otherwise>
							<fmt:formatDate value="${employee.contractEndDate}" pattern="yyyy-MM-dd"/>&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<th>第一阶段转正时间：<font color="red">*</font></th>
				<td>
					<fmt:formatDate value="${employee.regularDate}" pattern="yyyy-MM-dd"/>&nbsp;
				</td>
				<th>第二阶段转正时间：</th>
				<td style="border-right: 1px #afafaf solid">
					<fmt:formatDate value="${employee.regularDateTwo}" pattern="yyyy-MM-dd"/>&nbsp;
				</td>
			</tr>
			<tr>
				<th>部门：</th>
				<td>
					${employee.employeeDept.name}&nbsp;
				</td>
				<th>工作岗位类别：</th>
				<td>
					${employee.employeePost.typeDict}&nbsp;
				</td>
				<th>工作岗位：</th>
				<td style="border-right: 1px #afafaf solid">
					${employee.employeePost.name}&nbsp;
				</td>
			</tr>
			<tr>
				<th>组织管理级序：</th>
				<td>
					${employee.manageLevelDict}&nbsp;
				</td>
				<th>婚育状况：</th>
				<td>
					${employee.marryTypeDict}&nbsp;
				</td>
				<th>食宿情况：</th>
				<td style="border-right: 1px #afafaf solid">
					${employee.mealRoomTypeDict}&nbsp;
				</td>
			</tr>
			<tr>
				<th>学历：</th>
				<td>
					${employee.educationDict}
				</td>
				<th>专业：</th>
				<td>
					${employee.major}&nbsp;
				</td>
				<th>职业资格：</th>
				<td style="border-right: 1px #afafaf solid">
					${employee.jobCapacity}&nbsp;
				</td>
			</tr>
			<tr>
				<th>职称：</th>
				<td>
					${employee.jobTitle}&nbsp;
				</td>
				<th>驾驶证类型：</th>
				<td>
					${employee.driveLicenseTypeDict}&nbsp;
				</td>
				<th>驾驶证有效期：</th>
				<td colspan="2">
					<fmt:formatDate value="${employee.driveLicenseValidDate}" pattern="yyyy-MM-dd"/>&nbsp;
				</td>
			</tr>
			<tr>
				<th>驾驶证拿证日期：</th>
				<td>
					<fmt:formatDate value="${employee.driveLicenseGetDate}" pattern="yyyy-MM-dd"/>&nbsp;
				</td>
				<th>籍贯：</th>
				<td>
					${employee.nativePlace}&nbsp;
				</td>
				<th>通讯地址：</th>
				<td colspan="2">
					${employee.contactAddr}&nbsp;
				</td>
			</tr>
			<tr>
				<th>政治面貌：</th>
				<td>
					${employee.polityDict}&nbsp;
				</td>
				<th>户籍性质：</th>
				<td>
					${employee.nativePlaceTypeDict}&nbsp;
				</td>
				<th>户籍地址：</th>
				<td colspan="2">
					${employee.nativePlaceAddr}&nbsp;
				</td>
			</tr>
			<tr>
				<th>联系电话：</th>
				<td>
					${employee.telephone}&nbsp;
				</td>
				<th>紧急联系人：</th>
				<td>
					${employee.emergentName}&nbsp;
				</td>
				<th>紧急联系人电话：</th>
				<td colspan="2">
					${employee.emergentTelephone}&nbsp;
				</td>
			</tr>
			<tr>
				<th>银行卡类型：</th>
				<td>
					${employee.bankTypeDict}&nbsp;
				</td>
				<th>银行卡号：</th>
				<td>
					${employee.bankCard}&nbsp;
				</td>
				<th>用工形式：</th>
				<td colspan="2">
					${employee.laborTypeDict}&nbsp;
				</td>
			</tr>
			<tr>
				<th>普通薪资方案：</th>
				<td>
					${employee.wagePlan.name}&nbsp;
				</td>
				<th>加班计算方式：</th>
				<td>
					<c:choose>
						<c:when test="${employee.overTimeRate == 1}">
							正常计算
						</c:when>
						<c:when test="${employee.overTimeRate == 2}">
							含年假计算
						</c:when>
						<c:when test="${employee.overTimeRate == 3}">
							按小时计算
						</c:when>
						<c:when test="${employee.overTimeRate == 4}">
							不计加班
						</c:when>
						<c:when test="${employee.overTimeRate == 5}">
							按星期六加班
						</c:when>
						<c:otherwise>
							无
						</c:otherwise>
					</c:choose>
				</td>
				<th>绩效薪资方案：</th>
				<td colspan="2">
					<c:choose>
						<c:when test="${employee.performanceWageType == 0}">
							无
						</c:when>
						<c:otherwise>
							${employee.performanceWageTypeDict}&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<th>性格特征：</th>
				<td>
					${employee.characterRemark}&nbsp;
				</td>
				<th>民族：</th>
				<td>
					${employee.nation}&nbsp;
				</td>
				<th>风险协议：</th>
				<td colspan="2">
					${employee.hasRiskAgreementDict}&nbsp;
				</td>
			</tr>
			<tr>
				<th>提成协议：</th>
				<td>
					${employee.hasPercentAgreementDict}&nbsp;
				</td>
				<th>身高：</th>
				<td style="border-right: 1px #afafaf solid">
					${employee.height}&nbsp;
				</td>
				<th class="last-child"></th>
				<td class="last-chilld"></td>
			</tr>
			</tbody>
		</table>
	</div>
	<br />
	<!-- 合同或协议扫描件   -->
	<div class="easyui-panel" style="width:1113px;" title="合同或协议扫描件" headerCls="panelHeader">
		<table class="table-content-form" style="background: none;">
			<tbody>
			<tr>
				<th>种类：</th>
				<td>
					${employee.contractTypeDict}&nbsp;
				</td>
				<th>首页：</th>
				<td>
					<div id="homeAnnex_container">
					</div>
				</td>
				<th>尾页：</th>
				<td>
					<div id="finalAnnex_container">
					</div>
				</td>
			</tr>
			<tr>
				<th>是否有签收表：</th>
				<td>
					${employee.hasSignFormDict}&nbsp;
				</td>
				<th>签收表：</th>
				<td>
					<div id="signFormAnnex_container">
					</div>
				</td>
				<th class="last-child"></th>
				<td class="last-child"></td>
			</tr>
			</tbody>
		</table>
	</div>

	<br />
	<!-- 参保情况   -->
	<div class="easyui-panel" style="width: 1113px;" title="参保情况" headerCls="panelHeader">
		<table class="table-content-form" style="background: none;">
			<tbody>
			<tr>
				<th>是否参保：</th>
				<td>
					<c:choose>
						<c:when test="${employee.hasInsure == 1}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
					</c:choose>
				</td>
				<th>社保号：</th>
				<td>
					${employee.insureNo}&nbsp;
				</td>
				<th>起始时间：</th>
				<td>
					<fmt:formatDate value="${employee.insureDate}" pattern="yyyy-MM-dd"/>&nbsp;
				</td>
			</tr>
			<tr>
				<th>目前基数：</th>
				<td>
					${employee.insurePayBase}&nbsp;
				</td>
				<th>养老保险：</th>
				<td>
					<c:choose>
						<c:when test="${employee.hasPersionInsure == 1}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
					</c:choose>
				</td>
				<th>工伤保险：</th>
				<td>
					<c:choose>
						<c:when test="${employee.hasInjuryInsure == 1}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<th>生育保险：</th>
				<td>
					<c:choose>
						<c:when test="${employee.hasBirthInsure == 1}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
					</c:choose>
				</td>
				<th>医疗保险：</th>
				<td>
					<c:choose>
						<c:when test="${employee.hasMedicalInsure == 1}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
					</c:choose>
				</td>
				<th>重病保险：</th>
				<td>
					<c:choose>
						<c:when test="${employee.hasSeriousInsure == 1}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<th>不购承诺：</th>
				<td>
					<c:choose>
						<c:when test="${employee.hasNonPurchaseCommit == 1}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
					</c:choose>
				</td>
				<th>不购承诺附件：</th>
				<td>
					<div id="nonPurchaseCommitAnnex_container">
					</div>
				</td>
				<th class="last-child"></th>
				<td class="last-child"></td>
			</tr>
			<tr>
				<th>工伤宝：</th>
				<td>
					<c:choose>
						<c:when test="${employee.hasGsbInsure == 1}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
					</c:choose>
				</td>
				<th class="last-child"></th>
				<td class="last-child"></td>
				<th class="last-child"></th>
				<td class="last-child"></td>
			</tr>
			</tbody>
		</table>
	</div>

	<br />
	<!-- 公积金情况   -->
	<div class="easyui-panel" style="width: 1113px;" title="公积金情况" headerCls="panelHeader">
		<table class="table-content-form" style="background: none;">
			<tbody>
			<tr>
				<th>是否有公积金：</th>
				<td>
					<c:choose>
						<c:when test="${employee.hasPublicFund == 1}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
					</c:choose>
				</td>
				<th>目前缴费：</th>
				<td>
					${employee.publicFundPayBase}&nbsp;
				</td>
				<th>起始时间：</th>
				<td>
					<fmt:formatDate value="${employee.publicFundDate}" pattern="yyyy-MM-dd"/>&nbsp;
				</td>
			</tr>
			</tbody>
		</table>
	</div>

	<br />
	<!-- 离职情况   -->
	<div class="easyui-panel" style="width: 1113px;" title="离职情况" headerCls="panelHeader">
		<table class="table-content-form" style="background: none;">
			<tbody>
			<tr>
				<th>是否已离职：</th>
				<td>
					<c:choose>
						<c:when test="${employee.hasQuitCompany == 1}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
					</c:choose>
				</td>
				<th>离职种类：</th>
				<td>
					${employee.quitCompanyTypeDict}&nbsp;
				</td>
				<th>离职时间：</th>
				<td>
					<fmt:formatDate value="${employee.quitCompanyDate}" pattern="yyyy-MM-dd"/>&nbsp;
				</td>
			</tr>
			<tr>
				<th>离职原因：</th>
				<td colspan="5">
					${employee.quitCompanyResult}&nbsp;
				</td>
			</tr>
			</tbody>
		</table>
	</div>
	<br />
	<!-- 家庭主要成员 -->
	<div class="easyui-panel" style="width:1113px;height:250px;" title="家庭主要成员" headerCls="panelHeader">
		<table id="family_datagrid" class="easyui-datagrid" fit="true"
			   singleSelect="true" border="false" fitColumns="true">
			<thead>
			<tr>
				<th field="name" width="50">姓名</th>
				<th field="relationShipDict" width="50">关系</th>
				<th field="cardNo" width="50">身份证号码</th>
				<th field="company" width="50">单位</th>
				<th field="jobName" width="50">职务</th>
				<th field="telephone" width="50">联系电话</th>
			</tr>
			</thead>
		</table>
	</div>
	<br />
	<!-- 学习、培训简历（按时间顺序由近及远） -->
	<div class="easyui-panel" style="width: 1113px;height:250px;" title="学习、培训简历（按时间顺序由近及远）" headerCls="panelHeader">
		<table id="study_datagrid" class="easyui-datagrid" fit="true"
			   singleSelect="true" border="false" fitColumns="true">
			<thead>
			<tr>
				<th field="startDate" width="50">开始日期</th>
				<th field="endDate" width="50">结束日期</th>
				<th field="school" width="50">学校（机构）</th>
				<th field="major" width="50">专业或课题</th>
				<th field="educationTypeDict" width="50">学历</th>
				<th field="studyTypeDict" width="50">学习、培训方式</th>
			</tr>
			</thead>
		</table>
	</div>

	<br />
	<!-- 工作简历（按时间顺序由近及远） -->
	<div class="easyui-panel" style="width:1113px;height:250px;" title="工作简历（按时间顺序由近及远）" headerCls="panelHeader">
		<table id="work_datagrid" class="easyui-datagrid" fit="true"
			   singleSelect="true" border="false" fitColumns="true">
			<thead>
			<tr>
				<th field="startDate" width="50">开始日期</th>
				<th field="endDate" width="50">结束日期</th>
				<th field="company" width="50">工作单位</th>
				<th field="jobName" width="50">职务</th>
				<th field="witness" width="50">证明人</th>
				<th field="telephone" width="50">原单位电话</th>
			</tr>
			</thead>
		</table>
	</div>
	<br />

	<div class="easyui-panel" style="width: 1113px;">
		<table class="table-content-form" style="background: none;">
			<tbody>
			<tr>
				<th>是否有患病史：</th>
				<td>
					<c:choose>
						<c:when test="${employee.hasDiseaseHistory == 1}">
							有
						</c:when>
						<c:otherwise>
							无
						</c:otherwise>
					</c:choose>
				</td>
				<th>疾病：</th>
				<td>
					${employee.diseaseHistory}&nbsp;
				</td>
				<th>是否亲戚/朋友在本公司任职：</th>
				<td>
					<c:choose>
						<c:when test="${employee.hasFriendInCompany == 1}">
							有
						</c:when>
						<c:otherwise>
							无
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<th>亲戚/朋友所在项目：</th>
				<td>
					${employee.friendDept}&nbsp;
				</td>
				<th>亲戚/朋友所在姓名：</th>
				<td>
					${employee.friendName}&nbsp;
				</td>
				<th>亲戚/朋友所在职务：</th>
				<td>
					${employee.friendJobTitle}&nbsp;
				</td>
			</tr>
			<tr>
				<th>是否有劳动纠纷：</th>
				<td>
					<c:choose>
						<c:when test="${employee.hasLaborDispute == 1}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
					</c:choose>
				</td>
				<th>原因：</th>
				<td>
					${employee.laborDisputeResult}&nbsp;
				</td>
				<th>入职介绍人：</th>
				<td>
					${employee.enrtyIntorducer}&nbsp;
				</td>
			</tr>
			<tr>
				<th>入职介绍单位：</th>
				<td>
					${employee.enrtyIntorducerCompany}&nbsp;
				</td>
				<th class="last-child"></th>
				<td class="last-child"></td>
				<th class="last-child"></th>
				<td class="last-child"></td>
			</tr>
			</tbody>
		</table>
	</div>
	<br />

	<!-- 变更情况   -->
	<div class="easyui-panel" style="width: 1113px;" title="变更情况" headerCls="panelHeader">
		<table class="table-content-form" style="background: none;">
			<tbody>
			<tr>
				<th>职位变更备注：</th>
				<td style="width: 900px;">
					${employee.postChangeRemark}
				</td>
			</tr>
			<tr>
				<th>部门变更备注：</th>
				<td>
					${employee.deptChangeRemark}
				</td>
			</tr>
			<tr>
				<th>操作变更备注：</th>
				<td>
					${employee.operaChangeRemark}
				</td>
			</tr>
			<tr>
				<th>工资变更备注：</th>
				<td>
					${employee.wageChangeRemark}
				</td>
			</tr>
			</tbody>
		</table>
	</div>
	<br />

</body>
</html>