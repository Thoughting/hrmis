<%@page import="com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache"%>
<%@page import="com.eastcom.baseframe.common.utils.DateUtils"%>
<%@page import="com.eastcom.hrmis.modules.emp.EmpConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<html>
<head>
<title>员工工资核实</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/emp/wages/employeeWageCheckMgr.js"></script>
<script type="text/javascript">
var hasGridEdit = <%=SecurityCache.hasPermission("emp:wagecheckmgr:edit")%>;

</script>
</head>

<body>
	<div class="easyui-layout" style="width: 100%; height: 650px;">
		<div data-options="region:'center'">
			<table id="datagrid" class="easyui-datagrid" fit="true" rownumbers="true" enable="false"
				toolbar="#toolbar" border="false" pagination="true" pageSize="20">
			</table>
			<div id="toolbar">
				<table style="line-height:26px;padding: 10px;padding-bottom: 0px;">
					<tr>
						<td>部门：</td>
						<td>
							<select id="employeeDept" name="employeeDept" style='height:22px;width:150px;'>
								<option value=''>--请选择--</option>
							</select>
						</td>
						<td>工资年月：</td>
						<td>
							<input id="wageDate" name="wageDate" value="<%=DateUtils.formatDate(new Date(), "yyyy年-MM月") %>" class="Wdate" style="width: 100px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy年-MM月',maxDate:'<%=DateUtils.formatDate(new Date(), "yyyy年-MM月") %>'})"/>
						</td>
						<td>工资方案：</td>
						<td>
							<select id="wagePlanCombo" name="wagePlanCombo" style='height:22px;width:150px;'>
								<option value=''>--请选择--</option>
							</select>
						</td>
						<td>岗位：</td>
						<td>
							<select id="employeePost" name="employeePost" style='height:22px;'>
								<option value=''>--请选择--</option>
							</select>
						</td>
						<td>姓名：</td>
						<td>
							<input id="name" name="name" type="text" style="width: 80px;"/>
						</td>
						<td>
	        				<a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
	        				<shiro:hasPermission name="emp:wagecheckmgr:sendaudit">
	        					<a id="sendAuditBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">提交审核</a>
	        				</shiro:hasPermission>
							<shiro:hasPermission name="emp:wagecheckmgr:resetaudit">
								<a id="resetAuditBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">重新审核</a>
							</shiro:hasPermission>
        					<shiro:hasPermission name="emp:wagecheckmgr:audit">
	        					<a id="auditBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">审核</a>
	        				</shiro:hasPermission>
	        				<shiro:hasPermission name="emp:wagecheckmgr:export">
	        					<a id="exportBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">导出</a>
	        				</shiro:hasPermission>
	        			</td>
					</tr>
				</table>
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