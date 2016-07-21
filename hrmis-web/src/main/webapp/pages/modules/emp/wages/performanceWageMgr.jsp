<%@page import="com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache"%>
<%@page import="com.eastcom.baseframe.common.utils.DateUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<html>
<head>
<title>绩效工资表</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/emp/wages/performanceWageMgr.js"></script>
<script type="text/javascript">
var hasGridEdit = <%=SecurityCache.hasPermission("emp:performancewagemgr:edit")%>;

</script>
</head>

<body>
	<div class="easyui-layout" style="width:100%;height:670px;">
        <div data-options="region:'center',iconCls:'icon-ok'">
            <div id="mainTab" class="easyui-tabs" fit="true" border="false" toolbar="#toolbar">
		      	<div title="管理人员遵章绩效奖金表" style="overflow: hidden;">
		      		<table style="line-height:26px;padding: 10px;">
						<tr>
							<td>绩效工资年月：</td>
							<td>
								<input id="wageDate" name="wageDate" value="<%=DateUtils.formatDate(new Date(), "yyyy年-MM月") %>" class="Wdate" style="width: 100px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy年-MM月',maxDate:'<%=DateUtils.formatDate(new Date(), "yyyy年-MM月") %>',onpicked:Interactive.reloadData})"/>
							</td>
							<td>
								<shiro:hasPermission name="emp:performancewagemgr:export">
									<a href="javascript:Interactive.exportBtnClickHandler(1)" class="easyui-linkbutton" iconCls="icon-edit">导出</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</table>
		      		<sys:easyuigrid id="datagrid1" name="管理人员遵章绩效奖金表" attrHtml='singleSelect="true" 
		      			fitColumns="true" border="true" pagination="true" pageSize="20" style="width:100%;height: 587px;"'/>
		     	</div>
		        <div title="文员绩效考核等级表" style="overflow: hidden;">
		        	<table style="line-height:26px;padding: 10px;">
						<tr>
							<td>绩效工资年月：</td>
							<td>
								<input id="wageDate" name="wageDate" value="<%=DateUtils.formatDate(new Date(), "yyyy年-MM月") %>" class="Wdate" style="width: 100px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy年-MM月',maxDate:'<%=DateUtils.formatDate(new Date(), "yyyy年-MM月") %>',onpicked:Interactive.reloadData})"/>
							</td>
							<td>
								<shiro:hasPermission name="emp:performancewagemgr:export">
									<a href="javascript:javascript:Interactive.exportBtnClickHandler(2)" class="easyui-linkbutton" iconCls="icon-edit">导出</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</table>
		        	<sys:easyuigrid id="datagrid2" name="文员绩效考核等级表" attrHtml='singleSelect="true" 
		        		border="true" pagination="true" pageSize="20" style="width:100%;height: 587px;"'/>
		        </div>
		    </div>
        </div>
    </div>
</body>
</html>