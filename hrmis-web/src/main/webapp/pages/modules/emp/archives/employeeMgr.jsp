<%@page import="com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache"%>
<%@page import="com.eastcom.baseframe.common.utils.StringUtils"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.eastcom.hrmis.modules.emp.EmpConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<%
String employeeName = URLDecoder.decode(StringUtils.defaultString(request.getParameter("name"),"") , "UTF-8");
request.setAttribute("employeeName", employeeName);
%>
<html>
<head>
<title>员工档案管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxScripts}/common/uploader.js"></script>
<script src="${ctxScripts}/modules/emp/archives/employeeMgr.js"></script>
<script type="text/javascript">

var hasAnnexDelete = <%=SecurityCache.hasPermission("emp:baseinfomgr:annexDelete")%>;

$(function(){
	//展开高级查询条件
	$("#moreAdvancedInputBtn").click(function(){
		$("#moreAdvancedInputBtn").hide();
		$("#retractAdvancedInputBtn").show();
		$("tr[name='advancedSearchInput']").show();
		
		var c = $('#cc');
        var p = c.layout('panel','north');
        p.panel('resize', {height:250});
        c.layout('resize',{
            height: (c.height() + 70),
        });
        c.layout('resize');
	})
	//收起高级查询条件
	$("#retractAdvancedInputBtn").click(function(){
		$("#retractAdvancedInputBtn").hide();
		$("#moreAdvancedInputBtn").show();
		$("tr[name='advancedSearchInput']").hide();
		
		var c = $('#cc');
        var p = c.layout('panel','north'); 
        p.panel('resize', {height:140});
        c.layout('resize',{
            height: (c.height() - 70),
        });
        c.layout('resize');
	})
})
</script>

</head>

<body>
	<div id="cc" class="easyui-layout" style="width:100%;height:650px;">
        <div data-options="region:'north'" style="height:140px;">
        	<table class="search-table" style="padding: 5px;background: none;">
       			<tr>
       				<td>编号：</td>
					<td><input id="code" name="code" type="text"/></td>
					<td>姓名：</td>
					<td><input id="name" name="name" type="text" value="${employeeName }"/></td>
					<td>性别：</td>
					<td>
						<select id="sex" name="sex" style='height:22px;'>
							<option value=''>--请选择--</option>
							<option value='1'>男</option>
							<option value='0'>女</option>
						</select>
					</td>
					<td>审核状态：</td>
					<td>
						<sys:dictselect id="auditStatus" name="auditStatus" dictType="<%=EmpConstant.DICT_EMPLOYEE_AUDITSTATUS_TYPE %>" needNull="true" attrHtml="style='height:22px;'"/>
					</td>
					<td>
						<span id="moreAdvancedInputBtn" class="icon-form-more">更多</span>
						<span id="retractAdvancedInputBtn" class="icon-form-retract" style="display: none;">收起</span>
					</td>
				</tr>
				<tr>
					<td>部门：</td>
					<td>
						<select id="employeeDept" name="employeeDept" style='height:22px;'>
							<option value=''>--请选择--</option>
						</select>
					</td>
					<td>工作岗位类别：</td>
					<td><sys:dictselect id="employeePostType" name="employeePostType" dictType="<%=EmpConstant.DICT_EMPLOYEE_POST_TYPE %>" needNull="true" attrHtml="style='height:22px;'"/></td>
					<td>工作岗位：</td>
					<td>
						<select id="employeePost" name="employeePost" style='height:22px;'>
							<option value=''>--请选择--</option>
						</select>
					</td>
					<td>学历：</td>
					<td>
						<sys:dictselect id="education" name="education" dictType="<%=EmpConstant.DICT_EMPLOYEE_EDUCATION_TYPE %>" needNull="true" attrHtml="style='height:22px;'"/>
					</td>
					<td>
					</td>
				</tr>
				<tr>
					<td>入职时间：</td>
					<td>
						<input id="start_enrtyDate" name="start_enrtyDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
						至
						<input id="end_enrtyDate" name="end_enrtyDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
					</td>
					<td>转正时间：</td>
					<td>
						<input id="start_regularDate" name="start_regularDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
						至
						<input id="end_regularDate" name="end_regularDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
					</td>
					<td>出生日期：</td>
					<td>
						<input id="start_birthDate" name="start_retareDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
						至
						<input id="end_birthDate" name="end_retareDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
					</td>
					<td>用工形式：</td>
					<td>
						<sys:dictselect id="laborType" name="laborType" dictType="<%=EmpConstant.DICT_EMPLOYEE_LABOR_TYPE %>" needNull="true" attrHtml="style='height:22px;'"/>
					</td>
					<td>
					</td>
				</tr>
				<tr name="advancedSearchInput" style="overflow: hidden;display: none;">
					<td>合同种类：</td>
					<td>
						<sys:dictselect id="contractType" name="contractType" dictType="<%=EmpConstant.DICT_EMPLOYEE_CONTRACT_TYPE %>" needNull="true" attrHtml="style='height:22px;'"/>
					</td>
					<td>合同期限：</td>
					<td>
						<input id="contractStartDate" name="contractStartDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
						至
						<input id="contractEndDate" name="contractEndDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
					</td>
					<td>退休时间</td>
					<td>
						<input id="start_retareDate" name="start_retareDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
						至
						<input id="end_retareDate" name="end_retareDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
					</td>
					<td>食宿情况</td>
					<td>
						<sys:dictselect id="mealRoomType" name="mealRoomType" dictType="<%=EmpConstant.DICT_EMPLOYEE_MEALROOM_TYPE %>" needNull="true" attrHtml="style='height:22px;'"/>
					</td>
					<td>
					</td>
				</tr>
				<tr name="advancedSearchInput" style="overflow: hidden;display: none;">
					<td>是否参保：</td>
					<td>
						<select id="hasInsure" name="hasInsure" style='height:22px;'>
							<option value=''>--请选择--</option>
							<option value='1'>是</option>
							<option value='0'>否</option>
						</select>
					</td>
					<td>社保号</td>
					<td><input id="insureNo" name="insureNo" type="text"/></td>
					<td>参保时间：</td>
					<td>
						<input id="start_insureDate" name="start_insureDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
						至
						<input id="end_insureDate" name="end_insureDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
					</td>
					<td>驾驶证类型：</td>
					<td>
						<sys:dictselect id="driveLicenseType" name="driveLicenseType" dictType="<%=EmpConstant.DICT_EMPLOYEE_DRIVELICENSE_TYPE %>" needNull="true" attrHtml="style='height:22px;'"/>
					</td>
					<td>
					</td>
				</tr>
				<tr name="advancedSearchInput" style="overflow: hidden;display: none;">
					<td>是否有公积金：</td>
					<td>
						<select id="hasPublicFund" name="hasPublicFund" style='height:22px;'>
							<option value=''>--请选择--</option>
							<option value='1'>是</option>
							<option value='0'>否</option>
						</select>
					</td>
					<td>公积金起始</td>
					<td>
						<input id="start_publicFundDate" name="start_publicFundDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
						至
						<input id="end_publicFundDate" name="end_publicFundDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
					</td>
					<td>离职时间：</td>
					<td>
						<input id="start_quitCompanyDate" name="start_quitCompanyDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
						至
						<input id="end_quitCompanyDate" name="end_quitCompanyDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
					</td>
					<td>是否离职：</td>
					<td>
						<input type="radio" value="1" name="hasQuitCompany">是
						<input type="radio" value="0" name="hasQuitCompany" checked="checked">否
					</td>
					<td>
					</td>
				</tr>
				<tr name="advancedSearchInput" style="overflow: hidden;display: none;">
					<td>户籍性质：</td>
					<td><sys:dictselect id="nativePlaceType" name="nativePlaceType" dictType="<%=EmpConstant.DICT_EMPLOYEE_NATIVE_PLACT_TYPE %>" needNull="true" attrHtml="style='height:22px;'"/></td>
					<td>户籍地址：</td>
					<td><input id="nativePlaceAddr" name="nativePlaceAddr" type="text"/></td>
				</tr>
				<tr>
					<td colspan="11" align="center">
						<a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						<shiro:hasPermission name="emp:baseinfomgr:add">
							<a id="addBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add">新增</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="emp:baseinfomgr:edit">
							<a id="editBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">编辑</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="emp:baseinfomgr:del">
							<a id="delBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove">删除</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="emp:baseinfomgr:annexUpload">
							<a id="uploadAnnexBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">附件上传</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="emp:baseinfomgr:export">
							<a id="exportBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">导出</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="emp:baseinfomgr:regular">
							<a id="regularBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">转正</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="emp:baseinfomgr:quitcompany">
							<a id="quitCompanyBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">离职</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="emp:baseinfomgr:audit">
							<a id="sendAuditBtn" href="javascript:void(0)" class="easyui-linkbutton">提交审核</a>
							<a id="auditBtn" href="javascript:void(0)" class="easyui-linkbutton">审核</a>
						</shiro:hasPermission>
					</td>
				</tr>
			</table>
        </div>
        <div data-options="region:'center',title:'查询结果'">
        	<sys:easyuigrid id="datagrid" name="员工档案表格" attrHtml="fit='true'
				toolbar='#toolbar' border='false' pagination='true' nowrap='false' pageSize='20' "/>
			
			<!-- 转正 -->
			<div id="regularPopWin" class="easyui-window" modal="true" collapsible="false" 
				 minimizable="false" maximizable="false" closed="true" resizable="false" title=" "
				 style="width:240px;height: 180px;" title="离职设置"> 
				 <form id="regular_form" method="post">
			        <table cellpadding="5">
			        	<tr>
			        		<td>是否转正：</td>
							<td>
								<input type="radio" value="1" name="ud_isRegular" checked="checked">是
								<input type="radio" value="0" name="ud_isRegular">否
							</td>
			        	</tr>
		                <tr>
		                    <td>第一阶段转正时间:</td>
		                    <td>
		                    	<input id="ud_regularDate" name="ud_regularDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
							</td>
		                </tr>
		                <tr>
		                    <td>第二阶段转正时间:</td>
		                    <td>
		                    	<input id="ud_regularDateTwo" name="ud_regularDateTwo" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
							</td>
		                </tr>
		                <tr>
		                    <td></td>
		                    <td align="right">
		                    	<a id="regular_submitBtn" href="javascript:void(0)" class="easyui-linkbutton">确定</a>
		                    </td>
		                </tr>
		            </table>
	            </form>
			</div>
			
			<!-- 离职 -->
			<div id="quitCompanyPopWin" class="easyui-window" modal="true" collapsible="false" 
				 minimizable="false" maximizable="false" closed="true" resizable="false" title=" "
				 style="width:300px;height: 300px;" title="离职设置"> 
				 <form id="quitCompany_form" method="post">
			        <table cellpadding="5">
			        	<tr>
			        		<td>是否离职：</td>
							<td>
								<input type="radio" value="1" name="ud_hasQuitCompany" checked="checked">是
								<input type="radio" value="0" name="ud_hasQuitCompany">否
							</td>
			        	</tr>
		                <tr>
		                    <td>离职种类:</td>
		                    <td>
		                		<sys:dictselect id="ud_quitCompanyType" name="ud_quitCompanyType" dictType="<%=EmpConstant.DICT_EMPLOYEE_QUITCOMPANY_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
							</td>
						</tr>
		                <tr>
		                    <td>离职时间:</td>
		                    <td>
		                    	<input id="ud_quitCompanyDate" name="ud_quitCompanyDate" class="Wdate" style="width: 85px;" type="text" onFocus="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
							</td>
		                </tr>
		                <tr>
		                    <td>离职原因:</td>
		                    <td>
		                    	<input id="ud_quitCompanyResult" name="ud_quitCompanyResult" class="easyui-textbox" data-options="multiline:true" style="height:60px"/>
							</td>
		                </tr>
		                <tr>
		                    <td></td>
		                    <td align="right">
		                    	<a id="quitCompany_submitBtn" href="javascript:void(0)" class="easyui-linkbutton">确定</a>
		                    </td>
		                </tr>
		            </table>
	            </form>
			</div>
			
			<!-- 档案审核   -->
			<div id="audit_PopWin" class="easyui-window" modal="true" collapsible="false" 
				 minimizable="false" maximizable="false" closed="true" resizable="false" title=" "
				 style="width:300px;height: 200px;" title="审核档案"> 
				 <form id="audit_form" method="post">
			        <table cellpadding="5">
			        	<tr>
		                    <td>审核内容:</td>
		                    <td>
		                    	<input id="ud_auditContent" name="ud_auditContent" class="easyui-textbox" data-options="multiline:true" style="height:60px"/>
							</td>
		                </tr>
			        	<tr>
			        		<td>是否通过：</td>
							<td>
								<input type="radio" value="2" name="ud_auditStatus">通过
								<input type="radio" value="3" name="ud_auditStatus" checked="checked">不通过
							</td>
			        	</tr>
		                <tr>
		                    <td></td>
		                    <td align="right">
		                    	<a id="audit_submitBtn" href="javascript:void(0)" class="easyui-linkbutton">确定</a>
		                    </td>
		                </tr>
		            </table>
	            </form>
			</div>
			
        </div>
    </div>
</body>
</html>