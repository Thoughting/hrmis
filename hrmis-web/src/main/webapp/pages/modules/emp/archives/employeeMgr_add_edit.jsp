<%@page import="com.eastcom.baseframe.common.config.Global"%>
<%@page import="com.eastcom.hrmis.modules.emp.EmpConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<html>
<head>
<title>员工档案编辑</title>
<meta name="decorator" content="default" />

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
<script src="${ctxScripts}/modules/emp/archives/employeeMgr_add_edit.js"></script>
</head>

<body style="background: none;">
	<!-- 基本信息   -->
	<div class="easyui-panel" style="width:100%;" title="基础信息&nbsp;&nbsp;带*为必填" headerCls="panelHeader">
	    <table class="table-content-form" style="background: none;">
	    	<tbody>
	    		<tr>
	    			<th>编号：<font color="red">*</font></th>
					<td><input id="code" name="code" type="text"/></td>
					<th>姓名：<font color="red">*</font></th>
					<td><input id="name" name="name" type="text"/></td>
					<th>性别：</th>
					<td style="border-right: 1px #afafaf solid">
						<input type="radio" value="1" name="sex" checked="checked">男
						<input type="radio" value="0" name="sex">女
					</td>
					<td rowspan="6" width="115px;" align="center" style="padding-left: 0px;">
						<a id="headerImgBtn" href="javascript:void(0)" style="text-decoration:none;" title="点击头像上传">
							<img id="headerImg" width="150" height="200" alt="点击更换头像" />
						</a>
					</td>
		    	</tr>
	    		<tr>
	    			<th>身份证号：<font color="red">*</font></th>
					<td><input id="cardNo" name="cardNo" type="text"/></td>
					<th>身份证有效期：<font color="red">*</font></th>
					<td>
						长期
						<input type="radio" value="1" name="isCardNoLongTerm" checked="checked">是
						<input type="radio" value="0" name="isCardNoLongTerm">否
						&nbsp;
						<input id="cardNoValidDate" name="cardNoValidDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
					</td>
					<th>出生日期：<font color="red">*</font></th>
					<td style="border-right: 1px #afafaf solid">
						<input id="birthDate" name="birthDate" style="width: 85px;" type="text" disabled="disabled"/>
						<span id="age">25</span>岁
					</td>
	    		</tr>
		    	<tr>
					<th>退休时间：</th>
					<td>
						<input id="retireDate" name="retireDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
					</td>
					<th>入职时间：<font color="red">*</font></th>
					<td>
						<input id="enrtyDate" name="enrtyDate" onchange="Interactive.enrtyDateChangeHandler();" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
						<sys:dictselect id="enrtyDateType" name="enrtyDateType" dictType="<%=EmpConstant.DICT_MORNING_AFTERNOON_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
					</td>
					<th>合同签订日期：</th>
					<td style="border-right: 1px #afafaf solid">
						<select id="contractSignDateType" name="contractSignDateType" style='height:22px;'>
							<option value='1'>首次签订</option>
							<option value='2'>二次签订</option>
							<option value='3'>三次签订</option>
						</select>
						<input id="contractSignDate" name="contractSignDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
					</td>
		    	</tr>
				<tr>
					<th>合同期限：<font color="red">*</font></th>
					<td>
						<input id="contractStartDate" name="contractStartDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
						至
						<input id="contractEndDate" name="contractEndDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
						<input id="contractTermCond" name="contractTermCond" value="1" type="checkbox" />法定终止条件出现
					</td>
					<th>第一阶段转正时间：<font color="red">*</font></th>
					<td>
						<input id="regularDate" name="regularDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
					</td>
					<th>第二阶段转正时间：</th>
					<td style="border-right: 1px #afafaf solid">
						<input id="regularDateTwo" name="regularDateTwo" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
					</td>
				</tr>
		    	<tr>
		    		<th>部门：</th>
					<td>
						<select id="employeeDept" name="employeeDept" style='height:22px;'>
							<option value=''>--请选择--</option>
						</select>
					</td>
					<th>工作岗位类别：</th>
					<td><sys:dictselect id="employeePostType" name="employeePostType" value="0" dictType="<%=EmpConstant.DICT_EMPLOYEE_POST_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/></td>
		    		<th>工作岗位：</th>
					<td style="border-right: 1px #afafaf solid">
						<select id="employeePost" name="employeePost" style='height:22px;'>
							<option value=''>--请选择--</option>
						</select>
					</td>
				</tr>
		    	<tr>
		    		<th>组织管理级序：</th>
					<td><sys:dictselect id="manageLevel" name="manageLevel" dictType="<%=EmpConstant.DICT_MANAGE_LEVEL %>" needNull="false" attrHtml="style='height:22px;'"/></td>
		    		<th>婚育状况：</th>
					<td><sys:dictselect id="marryType" name="marryType" dictType="<%=EmpConstant.DICT_EMPLOYEE_MARRY_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/></td>
					<th>食宿情况</th>
					<td style="border-right: 1px #afafaf solid">
						<sys:dictselect id="mealRoomType" name="mealRoomType" dictType="<%=EmpConstant.DICT_EMPLOYEE_MEALROOM_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
					</td>
		    	</tr>
		    	<tr>
	    			<th>学历：</th>
					<td><sys:dictselect id="education" name="education" dictType="<%=EmpConstant.DICT_EMPLOYEE_EDUCATION_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/></td>
		    		<th>专业：</th>
					<td>
						<input id="major" name="major" type="text"/>
					</td>
					<th>职业资格：</th>
					<td colspan="2">
						<input id="jobCapacity" name="jobCapacity" type="text"/>
					</td>
		    	</tr>
		    	<tr>
					<th>职称：</th>
					<td>
						<input id="jobTitle" name="jobTitle" type="text"/>
					</td>
					<th>驾驶证类型：</th>
					<td><sys:dictselect id="driveLicenseType" name="driveLicenseType" dictType="<%=EmpConstant.DICT_EMPLOYEE_DRIVELICENSE_TYPE %>" needNull="true" attrHtml="style='height:22px;'"/></td>
					<th>驾驶证有效期：</th>
					<td colspan="2">
						<input id="driveLicenseValidDate" name="driveLicenseValidDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
					</td>
		    	</tr>
		    	<tr>
		    		<th>驾驶证拿证日期：</th>
					<td>
						<input id="driveLicenseGetDate" name="driveLicenseGetDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
					</td>
		    		<th>籍贯：</th>
					<td>
						<input id="nativePlace" name="nativePlace" type="text"/>
					</td>
					<th>通讯地址：</th>
					<td colspan="2">
						<input id="contactAddr" name="contactAddr" type="text"/>
					</td>
		    	</tr>
		    	<tr>
		    		<th>政治面貌：</th>
					<td><sys:dictselect id="polity" name="polity" dictType="<%=EmpConstant.DICT_EMPLOYEE_POLITY_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/></td>
					<th>户籍性质：</th>
					<td><sys:dictselect id="nativePlaceType" name="nativePlaceType" dictType="<%=EmpConstant.DICT_EMPLOYEE_NATIVE_PLACT_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/></td>
					<th>户籍地址：<font color="red">*</font></th>
					<td colspan="2">
						<input id="nativePlaceAddr" name="nativePlaceAddr" type="text"/>
					</td>
		    	</tr>
		     	<tr>
		     		<th>联系电话：<font color="red">*</font></th>
					<td>
						<input id="telephone" name="telephone" type="text"/>
					</td>
		     		<th>紧急联系人：</th>
					<td>
						<input id="emergentName" name="emergentName" type="text"/>
					</td>
		     		<th>紧急联系人电话：</th>
					<td colspan="2">
						<input id="emergentTelephone" name="emergentTelephone" type="text"/>
					</td>
				</tr>
				<tr>
					<th>银行卡类型：</th>
					<td>
						<sys:dictselect id="bankType" name="bankType" dictType="<%=EmpConstant.DICT_EMPLOYEE_BANK_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
					</td>
					<th>银行卡号：</th>
					<td>
						<input id="bankCard" name="bankCard" type="text"/>
					</td>
					<th>用工形式：</th>
					<td colspan="2">
						<sys:dictselect id="laborType" name="laborType" dictType="<%=EmpConstant.DICT_EMPLOYEE_LABOR_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
					</td>
				</tr>
				<tr>
					<th>普通薪资方案：</th>
					<td>
						<select id="wagePlan" name="wagePlan" style='height:22px;'>
							<option value=''>--请选择--</option>
						</select>
					</td>
					<th>加班计算方式：</th>
					<td>
						<select id="overTimeRate" name="overTimeRate" style='height:22px;'>
							<option value='1'>正常计算</option>
							<option value='2'>含年假计算</option>
							<option value='3'>按小时计算</option>
							<option value='4'>不计加班</option>
							<option value='5'>按星期日加班</option>
						</select>
					</td>
					<th>绩效薪资方案：</th>
					<td colspan="2">
						<sys:dictselect id="performanceWageType" name="performanceWageType" dictType="<%=EmpConstant.DICT_PERFORMANCE_WAGE_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
					</td>
				</tr>
				<tr>
					<th>性格特征：</th>
					<td>
						<input id="characterRemark" name="characterRemark" type="text"/>
					</td>
					<th>民族：</th>
					<td><input id="nation" name="nation" type="text"/></td>
					<th>风险协议：</th>
					<td colspan="2">
						<input type="radio" value="1" name="hasRiskAgreement" checked="checked">是
						<input type="radio" value="0" name="hasRiskAgreement">否
					</td>
				</tr>
				<tr>
					<th>提成协议：</th>
					<td>
						<input type="radio" value="1" name="hasPercentAgreement">是
						<input type="radio" value="0" name="hasPercentAgreement" checked="checked">否
					</td>
					<th>身高：</th>
					<td style="border-right: 1px #afafaf solid">
						<input id="height" name="height" type="text"/>
					</td>
					<th class="last-child"></th>
					<td class="last-chilld"></td>
				</tr>
			</tbody>
		</table>
    </div>
    <br />
	<!-- 合同或协议扫描件   -->
	<div class="easyui-panel" style="width:100%;" title="合同或协议扫描件" headerCls="panelHeader">
		<table class="table-content-form" style="background: none;">
			<tbody>
			<tr>
				<th>种类：</th>
				<td>
					<sys:dictselect id="contractType" name="contractType" dictType="<%=EmpConstant.DICT_EMPLOYEE_CONTRACT_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
				</td>
				<th>首页：</th>
				<td>
					<div id="homeAnnex_container">
					</div>
					<a id="contractHomeAnnex_uploadBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">附件上传</a>
				</td>
				<th>尾页：</th>
				<td>
					<div id="finalAnnex_container">
					</div>
					<a id="contractFinalAnnex_uploadBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">附件上传</a>
				</td>
			</tr>
			<tr>
				<th>是否有签收表：</th>
				<td>
					<input type="radio" value="1" name="hasSignForm" checked="checked">是
					<input type="radio" value="0" name="hasSignForm">否
				</td>
				<th>签收表：</th>
				<td>
					<div id="signFormAnnex_container">
					</div>
					<a id="signFormAnnex_uploadBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">附件上传</a>
				</td>
				<th class="last-child"></th>
				<td class="last-child"></td>
			</tr>
			</tbody>
		</table>
	</div>

	<br />
	<!-- 参保情况   -->
	<div class="easyui-panel" style="width:100%;" title="参保情况" headerCls="panelHeader">
		<table class="table-content-form" style="background: none;">
			<tbody>
			<tr>
				<th>是否参保：</th>
				<td>
					<input type="radio" value="1" name="hasInsure" checked="checked">是
					<input type="radio" value="0" name="hasInsure">否
				</td>
				<th>社保号：</th>
				<td>
					<input id="insureNo" name="insureNo" type="text"/>
				</td>
				<th>起始时间：</th>
				<td>
					<input id="insureDate" name="insureDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
				</td>
			</tr>
			<tr>
				<th>目前基数：</th>
				<td>
					<input id="insurePayBase" name="insurePayBase" type="text"/>
				</td>
				<th>养老保险：</th>
				<td>
					<input type="radio" value="1" name="hasPersionInsure" checked="checked">是
					<input type="radio" value="0" name="hasPersionInsure">否
				</td>
				<th>工伤保险：</th>
				<td>
					<input type="radio" value="1" name="hasInjuryInsure" checked="checked">是
					<input type="radio" value="0" name="hasInjuryInsure">否
				</td>
			</tr>
			<tr>
				<th>生育保险：</th>
				<td>
					<input type="radio" value="1" name="hasBirthInsure" checked="checked">是
					<input type="radio" value="0" name="hasBirthInsure">否
				</td>
				<th>医疗保险：</th>
				<td>
					<input type="radio" value="1" name="hasMedicalInsure" checked="checked">是
					<input type="radio" value="0" name="hasMedicalInsure">否
				</td>
				<th>重病保险：</th>
				<td>
					<input type="radio" value="1" name="hasSeriousInsure" checked="checked">是
					<input type="radio" value="0" name="hasSeriousInsure">否
				</td>
			</tr>
			<tr>
				<th>不购承诺：</th>
				<td>
					<input type="radio" value="1" name="hasNonPurchaseCommit">是
					<input type="radio" value="0" name="hasNonPurchaseCommit" checked="checked">否
				</td>
				<th>不购承诺附件：</th>
				<td>
					<div id="nonPurchaseCommitAnnex_container">
					</div>
					<a id="nonPurchaseCommitAnnex_uploadBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">附件上传</a>
				</td>
				<th class="last-child"></th>
				<td class="last-child"></td>
			</tr>
			<tr>
				<th>工伤宝：</th>
				<td>
					<input type="radio" value="1" name="hasGsbInsure">是
					<input type="radio" value="0" name="hasGsbInsure" checked="checked">否
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
	<div class="easyui-panel" style="width:100%;" title="公积金情况" headerCls="panelHeader">
		<table class="table-content-form" style="background: none;">
			<tbody>
			<tr>
				<th>是否有公积金：</th>
				<td>
					<input type="radio" value="1" name="hasPublicFund" checked="checked">是
					<input type="radio" value="0" name="hasPublicFund">否
				</td>
				<th>目前缴费：</th>
				<td>
					<input id="publicFundPayBase" name="publicFundPayBase" type="text"/>
				</td>
				<th>起始时间：</th>
				<td>
					<input id="publicFundDate" name="publicFundDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
				</td>
			</tr>
			</tbody>
		</table>
	</div>

	<br />
	<!-- 离职情况   -->
	<div class="easyui-panel" style="width:100%;" title="离职情况" headerCls="panelHeader">
		<table class="table-content-form" style="background: none;">
			<tbody>
			<tr>
				<th>是否已离职：</th>
				<td>
					<input type="radio" value="1" name="hasQuitCompany">是
					<input type="radio" value="0" name="hasQuitCompany" checked="checked">否
				</td>
				<th>离职种类：</th>
				<td>
					<sys:dictselect id="quitCompanyType" name="quitCompanyType" dictType="<%=EmpConstant.DICT_EMPLOYEE_QUITCOMPANY_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
				</td>
				<th>离职时间：</th>
				<td>
					<input id="quitCompanyDate" name="quitCompanyDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
				</td>
			</tr>
			<tr>
				<th>离职原因：</th>
				<td colspan="5">
					<input id="quitCompanyResult" name="quitCompanyResult" type="text" style="width: 500px;"/>
				</td>
			</tr>
			</tbody>
		</table>
	</div>
	<br />
    <!-- 家庭主要成员 -->
    <div class="easyui-panel" style="width:100%;height:250px;" title="家庭主要成员" headerCls="panelHeader">
	    <table id="family_datagrid" class="easyui-datagrid" fit="true"
			singleSelect="true" toolbar="#family_toolbar" border="false" fitColumns="true">
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
		<div id="family_toolbar">
			<a id="family_addBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" plain="true">新增</a>
			<a id="family_editBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" plain="true">编辑</a>
			<a id="family_delBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" plain="true">删除</a>
		</div>
		<!-- 新增 -->
		<div id="family_addAndEditPopWin" class="easyui-window" modal="true" collapsible="false" 
			 minimizable="false" maximizable="false" closed="true" resizable="false" title="家庭成员新增或修改"
			 style="width:330px;padding:10px;">
		    <form id="family_addEditForm" method="post">
		        <table cellpadding="5">
		        	<tr>
	                    <td>姓名<font color="red">*</font>:</td>
	                    <td><input id="family_ud_name" name="name" class="easyui-textbox" type="text" data-options="required:true"></input></td>
	                </tr>
	                <tr>
	                    <td>关系<font color="red">*</font>:</td>
	                    <td><sys:dictselect id="family_ud_relationShip" name="relationShip" dictType="<%=EmpConstant.DICT_EMPLOYEE_RELATIONSHIP_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/></td>
	                </tr>
	                <tr>
	                    <td>身份证号码<font color="red">*</font>:</td>
	                    <td><input id="family_ud_cardNo" name="cardNo" class="easyui-textbox" type="text"></input></td>
	                </tr>
	                <tr>
	                    <td>单位:</td>
	                    <td><input id="family_ud_company" name="company" class="easyui-textbox" type="text"></input></td>
	                </tr>
	                <tr>
	                    <td>职务:</td>
	                    <td><input id="family_ud_jobName" name="jobName" class="easyui-textbox" type="text"></input></td>
	                </tr>
	                <tr>
	                    <td>联系电话:</td>
	                    <td><input id="family_ud_telephone" name="telephone" class="easyui-textbox" type="text" data-options="required:true"></input></td>
	                </tr>
	                <tr>
	                    <td></td>
	                    <td align="right">
	                    	<a id="family_addEditSubmitBtn" href="javascript:void(0)" class="easyui-linkbutton">确定</a>
	                    	<a id="family_addEditCancleBtn" href="javascript:void(0)" class="easyui-linkbutton">取消</a>
	                    </td>
	                </tr>
	            </table>
		    </form>
		</div>
    </div>
    <br />
    <!-- 学习、培训简历（按时间顺序由近及远） -->
    <div class="easyui-panel" style="width:100%;height:250px;" title="学习、培训简历（按时间顺序由近及远）" headerCls="panelHeader">
	    <table id="study_datagrid" class="easyui-datagrid" fit="true"
			singleSelect="true" toolbar="#study_toolbar" border="false" fitColumns="true">
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
		<div id="study_toolbar">
			<a id="study_addBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" plain="true">新增</a>
			<a id="study_editBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" plain="true">编辑</a>
			<a id="study_delBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" plain="true">删除</a>
		</div>
		<!-- 新增 -->
		<div id="study_addAndEditPopWin" class="easyui-window" modal="true" collapsible="false" 
			 minimizable="false" maximizable="false" closed="true" resizable="false" title="学习经历新增或修改"
			 style="width:330px;padding:10px;">
		    <form id="study_addEditForm" method="post">
		        <table cellpadding="5">
	                <tr>
	                    <td>开始时间:</td>
	                    <td>
	                    	<input id="study_ud_startDate" name="startDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
	                    </td>
	                </tr>
	                <tr>
	                    <td>结束时间:</td>
	                    <td>
                    		<input id="study_ud_endDate" name="endDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
	                    </td>
	                </tr>
	                <tr>
	                    <td>学校（机构）:</td>
	                    <td><input id="study_ud_school" name="school" class="easyui-textbox" type="text" data-options="required:true"></input></td>
	                </tr>
	                <tr>
	                    <td>专业或课题:</td>
	                    <td><input id="study_ud_major" name="major" class="easyui-textbox" type="text" data-options="required:true"></input></td>
	                </tr>
	                <tr>
	                    <td>学历:</td>
	                    <td>
	                    	<sys:dictselect id="study_ud_educationType" name="educationType" dictType="<%=EmpConstant.DICT_EMPLOYEE_EDUCATION_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
	                    </td>
	                </tr>	
	                <tr>
	                    <td>学习培训方式:</td>
	                    <td>
                    		<sys:dictselect id="study_ud_studyType" name="studyType" dictType="<%=EmpConstant.DICT_EMPLOYEE_STUDY_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
	                    </td>
	                </tr>
	                <tr>
	                    <td></td>
	                    <td align="right">
	                    	<a id="study_addEditSubmitBtn" href="javascript:void(0)" class="easyui-linkbutton">确定</a>
	                    	<a id="study_addEditCancleBtn" href="javascript:void(0)" class="easyui-linkbutton">取消</a>
	                    </td>
	                </tr>
	            </table>
		    </form>
		</div>
    </div>
    
    <br />
    <!-- 工作简历（按时间顺序由近及远） -->
    <div class="easyui-panel" style="width:100%;height:250px;" title="工作简历（按时间顺序由近及远）" headerCls="panelHeader">
	    <table id="work_datagrid" class="easyui-datagrid" fit="true"
			singleSelect="true" toolbar="#work_toolbar" border="false" fitColumns="true">
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
		<div id="work_toolbar">
			<a id="work_addBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" plain="true">新增</a>
			<a id="work_editBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" plain="true">编辑</a>
			<a id="work_delBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" plain="true">删除</a>
		</div>
		<!-- 新增 -->
		<div id="work_addAndEditPopWin" class="easyui-window" modal="true" collapsible="false" 
			 minimizable="false" maximizable="false" closed="true" resizable="false" title="工作经历新增或修改"
			 style="width:330px;padding:10px;">
		    <form id="work_addEditForm" method="post">
		        <table cellpadding="5">
	                <tr>
	                    <td>开始时间:</td>
	                    <td>
	                    	<input id="work_ud_startDate" name="startDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
	                    </td>
	                </tr>
	                <tr>
	                    <td>结束时间:</td>
	                    <td>
                    		<input id="work_ud_endDate" name="endDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,dateFmt:'yyyy-MM-dd'})"/>
	                    </td>
	                </tr>
	                <tr>
	                    <td>工作单位:</td>
	                    <td><input id="work_ud_company" name="company" class="easyui-textbox" type="text" data-options="required:true"></input></td>
	                </tr>
	                <tr>
	                    <td>职务:</td>
	                    <td><input id="work_ud_jobName" name="jobName" class="easyui-textbox" type="text" data-options="required:true"></input></td>
	                </tr>
	                <tr>
	                    <td>证明人:</td>
	                    <td><input id="work_ud_witness" name="witness" class="easyui-textbox" type="text" data-options="required:true"></input></td>
	                </tr>
	                <tr>
	                    <td>原单位电话:</td>
	                    <td><input id="work_ud_telephone" name="telephone" class="easyui-textbox" type="text" data-options="required:true"></input></td>
	                </tr>
	                <tr>
	                    <td></td>
	                    <td align="right">
	                    	<a id="work_addEditSubmitBtn" href="javascript:void(0)" class="easyui-linkbutton">确定</a>
	                    	<a id="work_addEditCancleBtn" href="javascript:void(0)" class="easyui-linkbutton">取消</a>
	                    </td>
	                </tr>
	            </table>
		    </form>
		</div>
    </div>
    <br />
    
    <div class="easyui-panel" style="width:100%;">
    	<table class="table-content-form" style="background: none;">
	    	<tbody>
		    	<tr>
					<th>是否有患病史：</th>
					<td>
						<input type="radio" value="1" name="hasDiseaseHistory">是
						<input type="radio" value="0" name="hasDiseaseHistory" checked="checked">否
					</td>
		    		<th>疾病：</th>
					<td>
						<input id="diseaseHistory" name="diseaseHistory" type="text"/>
					</td>
					<th>是否亲戚/朋友在本公司任职：</th>
					<td>
						<input type="radio" value="1" name="hasFriendInCompany">是
						<input type="radio" value="0" name="hasFriendInCompany" checked="checked">否
					</td>
		    	</tr>
		    	<tr>
					<th>亲戚/朋友所在项目：</th>
					<td>
						<input id="friendDept" name="friendDept" type="text"/>
					</td>
		    		<th>亲戚/朋友姓名：</th>
					<td>
						<input id="friendName" name="friendName" type="text"/>
					</td>
					<th>亲戚/朋友所在职务：</th>
					<td>
						<input id="friendJobTitle" name="friendJobTitle" type="text"/>
					</td>
		    	</tr>
		    	<tr>
					<th>是否有劳动纠纷：</th>
					<td>
						<input type="radio" value="1" name="hasLaborDispute">是
						<input type="radio" value="0" name="hasLaborDispute" checked="checked">否
					</td>
		    		<th>原因：</th>
					<td>
						<input id="laborDisputeResult" name="laborDisputeResult" type="text"/>
					</td>
					<th>入职介绍人：</th>
					<td>
						<input id="enrtyIntorducer" name="enrtyIntorducer" type="text"/>
					</td>
		    	</tr>
		    	<tr>
					<th>入职介绍单位：</th>
					<td>
						<input id="enrtyIntorducerCompany" name="enrtyIntorducerCompany" type="text"/>
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
    <!-- 离职情况   -->
	<div class="easyui-panel" style="width:100%;" title="变更情况" headerCls="panelHeader">
	   <table class="table-content-form" style="background: none;">
	    	<tbody>
		    	<tr>
		    		<th>职位变更附件：</th>
					<td>
						<div id="postChangeAnnex_container">
						</div>
						<a id="postChangeAnnex_uploadBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">附件上传</a>
					</td>
					<th>职位变更备注：</th>
					<td>
						<textarea id="postChangeRemark" name="postChangeRemark" type="text" style="width: 500px;height:60px;"></textarea>
					</td>
		    	</tr>
		    	<tr>
		    		<th>部门变更附件：</th>
					<td>
						<div id="deptChangeAnnex_container">
						</div>
						<a id="deptChangeAnnex_uploadBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">附件上传</a>
					</td>
					<th>部门变更备注：</th>
					<td>
						<textarea id="deptChangeRemark" name="deptChangeRemark" style="width: 500px;height:60px;"></textarea>
					</td>
		    	</tr>
		    	<tr style="display: none;">
		    		<th>操作变更附件：</th>
					<td>
						<div id="operaChangeAnnex_container">
						</div>
						<a id="operaChangeAnnex_uploadBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">附件上传</a>
					</td>
					<th>操作变更备注：</th>
					<td>
						<textarea id="operaChangeRemark" name="operaChangeRemark" style="width: 500px;height:60px;"></textarea>
					</td>
		    	</tr>
		    	<tr>
		    		<th>工资变更附件：</th>
					<td>
						<div id="wageChangeAnnex_container">
						</div>
						<a id="wageChangeAnnex_uploadBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">附件上传</a>
					</td>
					<th>工资变更备注：</th>
					<td>
						<textarea id="wageChangeRemark" name="wageChangeRemark" style="width: 500px;height:60px;"></textarea>
					</td>
		    	</tr>
			</tbody>
		</table>
    </div>
    <br />
    <div class="easyui-panel" style="width:100%;border: none;">
    	<table class="table-content-form" style="background: none;border: none;">
	    	<tbody>
		    	<tr>
					<td align="center">
						<a id="saveBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">暂存</a>
						<a id="sendAuditBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">提交审核</a>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>