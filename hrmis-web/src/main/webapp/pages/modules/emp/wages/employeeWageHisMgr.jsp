<%@page import="com.eastcom.baseframe.common.utils.DateUtils"%>
<%@page import="com.eastcom.hrmis.modules.emp.EmpConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<html>
<head>
<title>查询员工工资历史</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
var deptName = "${deptName}";
</script>
<script src="${ctxScripts}/modules/emp/wages/employeeWageHisMgr.js"></script>
</head>

<body>
	<div class="easyui-layout" style="width: 100%; height: 650px;">
		<div data-options="region:'north'" style="height:50px;">
			<table style="line-height:26px;padding: 10px;padding-bottom: 0px;">
				<tr>
					<td>查询时段：</td>
					<td>
						<input id="start_wageDate" name="start_wageDate" value="${start_wageDate }" class="Wdate" style="width: 100px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy年-MM月',maxDate:'#F{$dp.$D(\'end_wageDate\')}',onpicked:Interactive.refreshGridColumn})"/>
						~
						<input id="end_wageDate" name="end_wageDate" value="${end_wageDate }" class="Wdate" style="width: 100px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy年-MM月',maxDate:'<%=DateUtils.formatDate(new Date(), "yyyy年-MM月") %>',onpicked:Interactive.refreshGridColumn})"/>
					</td>
					<td>部门：</td>
					<td>
						<select id="employeeDept" name="employeeDept" style='height:22px;'>
							<option value=''>--请选择--</option>
						</select>
					</td>
					<td>姓名：</td>
					<td>
						<input id="employeeName" name="employeeName" value="${employeeName }" type="text" style="width: 80px;"/>
					</td>
					<td>
        				<a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center'">
			<div id="mainTab" class="easyui-tabs" fit="true" border="false" toolbar="#toolbar">
		      	<c:if test="${not empty tabs}">
		      		<c:forEach items="${tabs}" var="tab" varStatus="tabStatus">
		      			<div title="${tab.name }" style="overflow: hidden;">
		      				<table id="datagrid_${tabStatus.count }" class="easyui-datagrid" fit="true" rownumbers="true" enable="false"
								singleSelect="true" border="false" pagination="true" pageSize="20" toolbar="#toolbar_${tabStatus.count }">
							</table>
							<div id="toolbar_${tabStatus.count }">
								<shiro:hasPermission name="emp:wagehismgr:export">
									<a id="exportBtn_${tabStatus.count }" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true">导出</a>
								</shiro:hasPermission>
					    	</div>
							<script type="text/javascript">
							$(function(){
								//得到工资方案表格头
								$.ajax({
									url : basePath + '/api/emp/wageplan/column',
									type : 'post',
									dataType : 'json',
									data : {id : "${tab.id}",type:"统计"},
									success : function(data) {
										if (data.success) {
											for(var i = 0;i < data.model.length;i++){
												var cells = data.model[i];
												for(var j = 0;j < cells.length;j++){
													if (cells[j].showFormatter == 'valueFormatter') {
														cells[j].formatter = Interactive.valueFormatter;
													}
													//去掉单位
													var title = cells[j].title;
													if (title.indexOf("（") != -1) {
														cells[j].title = title.substr(0,title.indexOf("（"));
													}
												}
											}
											$("#datagrid_${tabStatus.count }").datagrid({
												url : basePath + "/api/emp/wagehis/list",
								    			onBeforeLoad : function(param){
								    				param.start_wageDate = $("#start_wageDate").val();
								    				param.end_wageDate = $("#end_wageDate").val();
								    				param.deptName = $("#employeeDept").find("option:selected").text();
								    				param.employeeName = $("#employeeName").val();
								    				param.wagePlanId = "${tab.id}";
								    			},
												columns : data.model,
												frozenColumns : [[
												   {field:"wageDate",title:"统计月份",align:"center",width:"120"},
												   {field:"deptName",title:"部门",align:"center",width:"120"},
												   {field:"postName",title:"岗位",align:"center",width:"120"},
									               {field:"employeeName",title:"姓名",align:"center",width:"100"},
									               {field:"bankCard",title:"银行卡号",align:"center",width:"100"}
									            ]],
											});
										} else {
											$.messager.alert("错误", data.msg, "error");
										}
									},
									error : function(re, status, err) {
										$.messager.alert("错误", re.responseText, "error");
									}
								});
								
								//导出
								$("#exportBtn_${tabStatus.count }").linkbutton({
									onClick:function(){
										Common.openPostWindow2(basePath + '/api/emp/wagehis/export',{
											start_wageDate : $("#start_wageDate").val(),
						    				end_wageDate : $("#end_wageDate").val(),
						    				deptName : $("#employeeDept").find("option:selected").text(),
						    				employeeName : $("#employeeName").val(),
						    				wagePlanId : "${tab.id}"
										});
									}
								});
							})
							</script>
		     			</div>
					</c:forEach>
		      	</c:if>
		    </div>
		</div>
	</div>
</body>
</html>