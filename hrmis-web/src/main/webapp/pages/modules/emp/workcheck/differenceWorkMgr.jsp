<%@page import="com.eastcom.baseframe.common.utils.DateUtils"%>
<%@page import="com.eastcom.hrmis.modules.emp.EmpConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<html>
<head>
<title>排班与考勤差异统计</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	var systemDate = "<%=DateUtils.formatDate(new Date())%>";
</script>
<script src="${ctxScripts}/modules/emp/workcheck/differenceWorkMgr.js"></script>
</head>

<body>
	<div class="easyui-layout" style="width: 100%; height: 650px;">
		<div data-options="region:'center'">
			<table id="datagrid" class="easyui-datagrid" fit="true" rownumbers="true" enable="false"
				singleSelect="true" toolbar="#toolbar" border="false" pagination="true" pageSize="20">
			</table>
			<div id="toolbar">
				<table style="line-height:26px;padding: 10px;padding-bottom: 0px;">
					<tr>
						<td>所属部门：</td>
						<td>
							<select id="employeeDept" name="employeeDept" style='height:22px;'>
								<option value=''>--请选择--</option>
							</select>
						</td>
						<td>排班年月：</td>
						<td>
							<input id="setWorkDate" name="setWorkDate" value="<%=DateUtils.formatDate(new Date(), "yyyy年-MM月") %>" class="Wdate" style="width: 100px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy年-MM月',onpicked:Interactive.refreshGridColumn})"/>
						</td>
						<td>
							<a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
	        				<shiro:hasPermission name="emp:setandcheckworkdiff:export">
	        					<a id="exportBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">导出</a>
	        				</shiro:hasPermission>
	        			</td>
					</tr>
				</table>
				<table style="line-height:26px;padding: 10px;padding-top: 0px;padding-bottom: 0px;">
					<tr>
						<td><div style="width: 40px;height: 20px;background-color: #FFFFFF;border: 1px solid #CCCCCC;"></div></td>
						<td>正常</td>
						<td>&nbsp;</td>
						<td><div style="width: 40px;height: 20px;background-color: #FFFF00;border: 1px solid #CCCCCC;"></div></td>
						<td>差异</td>
						<td>&nbsp;</td>
						<td><font color="red">注：有差异的单元格中分两行，上一行为排班，下一行为实际考勤</font></td>
					</tr>
				</table>
				<table style="line-height:26px;padding: 10px;padding-top: 0px;">
					<tr>
						<td colspan="23">
							全天出勤（含节假日加班）:空白，无需填写；加点：+具体时长；迟到:迟+具体时长；早退:早+具体时长；
						</td>
					</tr>
					<tr>
						<td>正常休假：∥</td>
						<td>&nbsp;</td>
						<td>补休：=</td>
						<td>&nbsp;</td>
						<td>事假：<font size="4">△</font></td>
						<td>&nbsp;</td>
						<td>病假：<font size="4">▽</font></td>
						<td>&nbsp;</td>
						<td>婚假：婚</td>
						<td>&nbsp;</td>
						<td>产假：产</td>
						<td>&nbsp;</td>
						<td>丧假：丧</td>
						<td>&nbsp;</td>
						<td>年休假：年</td>
						<td>&nbsp;</td>
						<td>工伤假：工</td>
						<td>&nbsp;</td>
						<td>旷工：<font size="4">×</font></td>
					</tr>
					<tr>
						<td colspan="23">
							上午休假：上　下午休假：下
						</td>
					</tr>
					<tr>
						<td colspan="23">
							满勤扫路、洒水：Ｓ　室外保洁Ｖ　高空作业：Ｇ　中空作业:Ｚ
						</td>
					</tr>
				</table>
			</div>
	</div>
</body>
</html>