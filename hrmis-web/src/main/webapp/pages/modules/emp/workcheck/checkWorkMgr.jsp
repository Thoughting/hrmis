<%@page import="com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache"%>
<%@page import="com.eastcom.baseframe.common.utils.DateUtils"%>
<%@page import="com.eastcom.hrmis.modules.emp.EmpConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<html>
<head>
<title>考勤管理</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/emp/workcheck/checkWorkMgr.js"></script>
<script type="text/javascript">
var hasGridEdit = "<%=SecurityCache.hasPermission("emp:checkworkmgr:edit")%>";
var systemDate = "<%=DateUtils.formatDate(new Date())%>";
</script>
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
						<td>考勤年月：</td>
						<td>
							<input id="setWorkDate" name="setWorkDate" value="<%=DateUtils.formatDate(new Date(), "yyyy年-MM月") %>" class="Wdate" style="width: 100px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy年-MM月',maxDate:'<%=DateUtils.formatDate(new Date(), "yyyy年-MM月") %>',onpicked:Interactive.refreshGridColumn})"/>
						</td>
						<td>
							<a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
	        				<shiro:hasPermission name="emp:checkworkmgr:export">
	        					<a id="exportBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">导出</a>
	        				</shiro:hasPermission>
	        			</td>
					</tr>
				</table>
				<table style="line-height:26px;padding: 10px;padding-top: 0px;">
					<tr>
						<td colspan="23">
							全天出勤（含节假日加班）:空白，无需填写；加点：+具体时长；迟到:迟+具体时长；早退:早+具体时长；
						</td>
					</tr>
					<tr>
						<td><div style="width: 40px;height: 20px;background-color: #FFFF00;border: 1px solid #CCCCCC;"></div></td>
						<td>正常休假：∥</td>
						<td>&nbsp;</td>
						<td><div style="width: 40px;height: 20px;background-color: #FFCC33;border: 1px solid #CCCCCC;"></div></td>
						<td>补休：=</td>
						<td>&nbsp;</td>
						<td><div style="width: 40px;height: 20px;background-color: #A8FFFF;border: 1px solid #CCCCCC;"></div></td>
						<td>事假：<font size="4">△</font></td>
						<td>&nbsp;</td>
						<td><div style="width: 40px;height: 20px;background-color: #99CC00;border: 1px solid #CCCCCC;"></div></td>
						<td>病假：<font size="4">▽</font></td>
						<td>&nbsp;</td>
						<td><div style="width: 40px;height: 20px;background-color: #FF8080;border: 1px solid #CCCCCC;"></div></td>
						<td>婚假：婚</td>
						<td>&nbsp;</td>
						<td><div style="width: 40px;height: 20px;background-color: #FF8AFF;border: 1px solid #CCCCCC;"></div></td>
						<td>产假：产</td>
						<td>&nbsp;</td>
						<td><div style="width: 40px;height: 20px;background-color: #CCCCCC;border: 1px solid #CCCCCC;"></div></td>
						<td>丧假：丧</td>
						<td>&nbsp;</td>
						<td><div style="width: 40px;height: 20px;background-color: #00CCCC;border: 1px solid #CCCCCC;"></div></td>
						<td>年休假：年</td>
						<td>&nbsp;</td>
						<td><div style="width: 40px;height: 20px;background-color: #E7D98B;border: 1px solid #CCCCCC;"></div></td>
						<td>工伤假：工</td>
						<td>&nbsp;</td>
						<td><div style="width: 40px;height: 20px;background-color: #DA1D00;border: 1px solid #CCCCCC;"></div></td>
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
			<!-- 新增 -->
			<div id="addAndEditPopWin" class="easyui-window" modal="true" collapsible="false" 
				 minimizable="false" maximizable="false" closed="true" resizable="false" title="考勤设置"
				 style="width:450px;">
			    <form id="addEditForm" method="post">
			        <table class="table-content-form" cellpadding="5">
			        	<tr>
		                    <th>姓名:</th>
		                    <td><span id="ud_name"></span></td>
		                </tr>
		                <tr>
		                    <th>日期:</th>
		                    <td>
	                    		<input id="ud_setWorkDate" name="setWorkDate" class="Wdate" style="width: 100px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd',maxDate:'<%=DateUtils.formatDate(new Date(), "yyyy-MM-dd") %>',onpicked:Interactive.popDateChange})"/>
		                    	&nbsp;
		                    	<span id="ud_week"></span>
		                    </td>
		                </tr>
		                <tr>
		                    <th><span id="ud_work_radio"><input type="radio" value="1" name="isAtWork" checked="checked"></span></th>
		                    <td>
                    			<sys:dictselect id="ud_atWorkTimer" name="atWorkTimer" dictType="<%=EmpConstant.DICT_AT_WORK_TIMER_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
		                   		<sys:dictselect id="ud_atWorkSpecialStatus" name="atWorkSpecialStatus" dictType="<%=EmpConstant.DICT_AT_WORK_SPECIAL_STATUS %>" needNull="false" attrHtml="style='height:22px;'"/>
		                    	<sys:dictselect id="ud_atWorkStatus" name="atWorkStatus" dictType="<%=EmpConstant.DICT_AT_WORK_STATUS %>" needNull="false" attrHtml="style='height:22px;'"/>
		                		<sys:dictselect id="ud_atWorkStatusMinute" name="atWorkStatusMinute" dictType="<%=EmpConstant.DICT_MINUTE_LIST_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
		                    </td>
		                </tr>
		                <tr>
		                    <th><span id="ud_holiday_radio"><input type="radio" value="0" name="isAtWork" checked="checked"></span></th>
		                    <td>
		                    	<sys:dictselect id="ud_atHolidayType" name="atHolidayType" dictType="<%=EmpConstant.DICT_AT_HOLIDAY_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
		                    	<sys:dictselect id="ud_atHolidayHour" name="atHolidayHour" dictType="<%=EmpConstant.DICT_HOUR_LIST_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
		                   		<sys:dictselect id="ud_atHolidayMinute" name="atHolidayMinute" dictType="<%=EmpConstant.DICT_MINUTE_LIST_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
		                    </td>
		                </tr>
						<tr>
							<th></td>
							<td>
								<input type="radio" value="0" name="atHolidayTimer" checked="checked">全天休假
								<input type="radio" value="1" name="atHolidayTimer">上午休假
								<input type="radio" value="2" name="atHolidayTimer">下午休假
							</td>
						</tr>
		                <tr>
		                    <th><span id="ud_outwork_radio"><input type="radio" value="3" name="isAtWork" checked="checked"></span></th>
		                	<td>
		                		旷工&nbsp;&nbsp;
		                		<sys:dictselect id="ud_outWorkHour" name="outWorkHour" dictType="<%=EmpConstant.DICT_HOUR_LIST_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
		                   		<sys:dictselect id="ud_outWorkMinute" name="outWorkMinute" dictType="<%=EmpConstant.DICT_MINUTE_LIST_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
		                	</td>
		                </tr>
		                <tr>
		                    <th>加班</th>
		                    <td>
		                    	<span id="ud_hasOverTime_radio">
		                    		<input type="radio" value="1" name="hasOverTime">是
									<input type="radio" value="0" name="hasOverTime">否
								</span>
		                    	<sys:dictselect id="ud_overTimeHour" name="overTimeHour" dictType="<%=EmpConstant.DICT_HOUR_LIST_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
		                    	<sys:dictselect id="ud_overTimeMinute" name="overTimeMinute" dictType="<%=EmpConstant.DICT_MINUTE_LIST_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/>
		                    </td>
		                </tr>
		                <tr>
		                    <th>备注:</th>
		                    <td><input id="ud_remark" class="easyui-textbox" name="remark" data-options="multiline:true" style="width:350px;height:60px"></input></td>
		                </tr>
		                <tr>
		                    <th></th>
		                    <td align="right">
		                    	<a id="addEditSubmitBtn" href="javascript:void(0)" class="easyui-linkbutton">确定</a>
		                    	<a id="addEditCancleBtn" href="javascript:void(0)" class="easyui-linkbutton">取消</a>
		                    </td>
		                </tr>
		            </table>
			    </form>
			</div>
		</div>
	</div>
</body>
</html>