<%@page import="com.eastcom.baseframe.common.utils.DateUtils"%>
<%@page import="com.eastcom.hrmis.modules.emp.EmpConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<html>
<head>
<title>考勤统计分析</title>
<meta name="decorator" content="default" />
<script src="${ctxPlugins}/echarts/2.2.7/echarts.js"></script>
<script src="${ctxScripts}/modules/emp/workcheck/checkWorkStatMgr.js"></script>
</head>

<body>
	<!-- 项目部门考勤统计 -->
    <div class="easyui-panel" style="width:100%;height:400px;">
	    <table id="dept_stat_datagrid" class="easyui-datagrid" fit="true" rownumbers="true" enable="false"
			singleSelect="true" toolbar="#toolbar" border="false" fitColumns="true">
			<thead>
				<tr>
					<th field="NAME" width="90" align="center" formatter="Interactive.deptNameFormatter">项目</th>
					<th field="EMP_COUNT" width="50" align="center" formatter="Interactive.nullFormatter">人数</th>
					<th field="CD_CS_COUNT" width="50" align="center" formatter="Interactive.nullFormatter"><a href="javascript:Interactive.refreshStatTypeChart('迟到（人次）','CD_CS_COUNT')">迟到（人次）</a></th>
					<th field="ZT_CS_COUNT" width="50" align="center" formatter="Interactive.nullFormatter"><a href="javascript:Interactive.refreshStatTypeChart('早退（人次）','ZT_CS_COUNT')">早退（人次）</a></th>
					<th field="KG_SC_DAY" width="50" align="center" formatter="Interactive.nullFormatter"><a href="javascript:Interactive.refreshStatTypeChart('旷工（天）','KG_SC_DAY')">旷工（天）</a></th>
					<th field="JBL_XSC_DAY" width="50" align="center" formatter="Interactive.nullFormatter"><a href="javascript:Interactive.refreshStatTypeChart('加班（天）','JBL_XSC_DAY')">加班（天）</a></th>
					<th field="ZCXJ_SC_DAY" width="50" align="center" formatter="Interactive.nullFormatter"><a href="javascript:Interactive.refreshStatTypeChart('休假（天）','ZCXJ_SC_DAY')">休假（天）</a></th>
					<th field="BXJ_SC_DAY" width="50" align="center" formatter="Interactive.nullFormatter"><a href="javascript:Interactive.refreshStatTypeChart('补休（天）','BXJ_SC_DAY')">补休（天）</a></th>
					<th field="SHJ_SC_DAY" width="50" align="center" formatter="Interactive.nullFormatter"><a href="javascript:Interactive.refreshStatTypeChart('事假（天）','SHJ_SC_DAY')">事假（天）</a></th>
					<th field="BJ_SC_DAY" width="50" align="center" formatter="Interactive.nullFormatter"><a href="javascript:Interactive.refreshStatTypeChart('病假（天）','BJ_SC_DAY')">病假（天）</a></th>
					<th field="HJ_SC_DAY" width="50" align="center" formatter="Interactive.nullFormatter"><a href="javascript:Interactive.refreshStatTypeChart('婚假（天）','HJ_SC_DAY')">婚假（天）</a></th>
					<th field="CJ_SC_DAY" width="50" align="center" formatter="Interactive.nullFormatter"><a href="javascript:Interactive.refreshStatTypeChart('产假（天）','CJ_SC_DAY')">产假（天）</a></th>
					<th field="SJ_SC_DAY" width="50" align="center" formatter="Interactive.nullFormatter"><a href="javascript:Interactive.refreshStatTypeChart('丧假（天）','SJ_SC_DAY')">丧假（天）</a></th>
					<th field="NXJ_SC_DAY" width="50" align="center" formatter="Interactive.nullFormatter"><a href="javascript:Interactive.refreshStatTypeChart('年休假（天）','NXJ_SC_DAY')">年休假（天）</a></th>
					<th field="GSJ_SC_DAY" width="50" align="center" formatter="Interactive.nullFormatter"><a href="javascript:Interactive.refreshStatTypeChart('工伤假（天）','GSJ_SC_DAY')">工伤假（天）</a></th>
				</tr>
			</thead>
		</table>
		<div id="toolbar">
			<table style="line-height:26px;padding: 10px;padding-bottom: 0px;">
				<tr>
					<td>考勤年月：</td>
					<td>
						<input id="statMonth" name="statMonth" value="<%=DateUtils.formatDate(new Date(), "yyyy年-MM月") %>" class="Wdate" style="width: 100px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy年-MM月',onpicked:function(){Component.$dept_stat_datagrid.datagrid('reload')}})"/>
					</td>
					<td>
						<shiro:hasPermission name="emp:checkworkstat:export">
							<a id="exportBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">导出</a>
						</shiro:hasPermission>
        			</td>
				</tr>
			</table>
		</div>
    </div>
   
    <!-- 统计图表 -->
   	<br />
    <div id="statChartContainer" class="easyui-panel" style="width:100%;" title="统计图表">
	    <div id="chart" style="height:300px"></div>
    </div>
    
    <!-- 员工考勤统计清单 -->
    <br />
    <div id="statEmployeesContainer" style="width:100%">
    	<div  class="easyui-panel" style="width:100%;height:400px;" title="员工考勤统计清单">
		    <table id="emp_stat_datagrid" class="easyui-datagrid" fit="true" rownumbers="true" enable="false"
				singleSelect="true" border="false" pagination="true" fitColumns="true">
				<thead>
					<tr>
						<th field="NAME" width="90" align="center">姓名</th>
						<th field="CD_CS_COUNT" width="50" align="center">迟到（人次）</th>
						<th field="ZT_CS_COUNT" width="50" align="center">早退（人次）</th>
						<th field="KG_SC_DAY" width="50" align="center">旷工（天）</th>
						<th field="JBL_XSC_DAY" width="50" align="center">加班（天）</th>
						<th field="ZCXJ_SC_DAY" width="50" align="center">休假（天）</th>
						<th field="BXJ_SC_DAY" width="50" align="center">补休（天）</th>
						<th field="SHJ_SC_DAY" width="50" align="center">事假（天）</th>
						<th field="BJ_SC_DAY" width="50" align="center">病假（天）</th>
						<th field="HJ_SC_DAY" width="50" align="center">婚假（天）</th>
						<th field="CJ_SC_DAY" width="50" align="center">产假（天）</th>
						<th field="SJ_SC_DAY" width="50" align="center">丧假（天）</th>
						<th field="NXJ_SC_DAY" width="50" align="center">年休假（天）</th>
						<th field="GSJ_SC_DAY" width="50" align="center">工伤假（天）</th>
					</tr>
				</thead>
			</table>
	    </div>
    </div>
    <br />
</body>
</html>