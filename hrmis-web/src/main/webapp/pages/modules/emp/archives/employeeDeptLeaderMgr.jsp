<%@page import="com.eastcom.hrmis.modules.emp.EmpConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>分管部门领导设置</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/emp/archives/employeeDeptLeaderMgr.js"></script>
<script type="text/javascript">
var deptLeaderId = '';
</script>
</head>
<body>
	<div class="easyui-layout" style="width:100%;height:650px;">
		<div data-options="region:'center'">
		    <table id="datagrid" class="easyui-datagrid" fit="true" singleSelect="true"
		            toolbar="#toolbar" border="false" fitColumns="true">
		        <thead>
		            <tr>
		            	<th field="name" width="50">分管领导</th>
		            	<th field="deptStr" width="80">分管部门名称</th>
		                <th field="remark" width="50">备注</th>
		            </tr>
		        </thead>
		    </table>
		    <div id="toolbar">
				<table style="line-height:26px;">
					<tr>
						<td>
							<shiro:hasPermission name="emp:deptleader:add">
								<a id="addBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add">新增</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="emp:deptleader:edit">
								<a id="editBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">编辑</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="emp:deptleader:del">
								<a id="delBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove">删除</a>
							</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</div>
		    <!-- 新增与编辑弹出框  -->
		    <div id="addAndEditPopWin" class="easyui-window" modal="true" collapsible="false" 
		    	 minimizable="false" maximizable="false" closed="true" resizable="false"
		    	 style="width:360px;padding:10px;" title="分管领导管理">
		        <form id="addEditForm" method="post">
		            <table cellpadding="5">
		            	<tr>
		                    <td>分管领导<font color="red">*</font>:</td>
		                    <td><input id="ud_name" class="easyui-textbox" type="text" name="name" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>分管部门名称<font color="red">*</font>:</td>
		                    <td>
		                    	<div style="height: 130px;overflow: auto;border: 1px solid #CCCCCC;">
		                    		<ul id="deptTree" class="easyui-tree" cascadeCheck="false" animate="true" checkbox="true" height="100"></ul>
		                    	</div>
			        		</td>
		                </tr>
		                <tr>
		                    <td>备注:</td>
		                    <td><input id="ud_remark" class="easyui-textbox" name="remark" data-options="multiline:true" style="height:60px"></input></td>
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