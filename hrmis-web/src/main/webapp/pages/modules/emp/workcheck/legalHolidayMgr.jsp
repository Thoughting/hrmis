<%@page import="com.eastcom.baseframe.common.utils.DateUtils"%>
<%@page import="com.eastcom.hrmis.modules.emp.EmpConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>职务岗位管理</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/emp/workcheck/legalHolidayMgr.js"></script>
</head>
<body>
	<div class="easyui-layout" style="width:100%;height:650px;">
		<div data-options="region:'center'">
		    <table id="datagrid" class="easyui-datagrid" fit="true" singleSelect="true"
		            toolbar="#toolbar" border="false" fitColumns="true" pageSize="20">
		        <thead>
		            <tr>
		            	<th field="holiday" width="50">法定节假日期</th>
		                <th field="remark" width="50">备注</th>
		            </tr>
		        </thead>
		    </table>
		    <div id="toolbar">
				<table style="line-height:26px;padding: 10px;">
					<tr>
						<td>选择年份：</td>
						<td>
							<input id="year" name="year" value="<%=DateUtils.formatDate(new Date(), "yyyy年") %>" class="Wdate" style="width: 100px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy年',maxDate:'<%=DateUtils.formatDate(DateUtils.addYears(new Date(), 1), "yyyy年") %>',onpicked:Interactive.refreshGridColumn})"/>
						</td>
						<td>
							<a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							<shiro:hasPermission name="emp:legalholidaymgr:add">
								<a id="addBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add">新增</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="emp:legalholidaymgr:edit">
								<a id="editBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">编辑</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="emp:legalholidaymgr:del">
								<a id="delBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove">删除</a>
							</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</div>
		    <!-- 新增与编辑弹出框  -->
		    <div id="addAndEditPopWin" class="easyui-window" modal="true" collapsible="false" 
		    	 minimizable="false" maximizable="false" closed="true" resizable="false"
		    	 style="width:320px;padding:10px;" title="法定节假日管理">
		        <form id="addEditForm" method="post">
		            <table cellpadding="5">
		            	<tr>
		                    <td>节假日<font color="red">*</font>:</td>
		                    <td>
		                    	<input id="ud_holiday" name="holiday" class="Wdate" style="width: 100px;" type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd',minDate:'<%=DateUtils.formatDate(new Date(), "yyyy") %>-01-01',onpicked:Interactive.popDateChange})"/>
		                    </td>
		                </tr>
		                <tr>
		                    <td>备注:</td>
		                    <td><input id="ud_remark" name="remark" class="easyui-textbox" name="description" data-options="multiline:true" style="height:60px"></input></td>
		                </tr>
		                <tr>
		                    <td></td>
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