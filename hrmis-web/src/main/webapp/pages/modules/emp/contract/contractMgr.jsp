<%@page import="com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache"%>
<%@page import="com.eastcom.hrmis.modules.emp.EmpConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<html>
<head>
<title>合同查询管理</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/emp/contract/contractMgr.js"></script>
<script type="text/javascript" src="${ctxScripts}/common/uploader.js"></script>
<script type="text/javascript">
var hasDelete = <%=SecurityCache.hasPermission("emp:contractmgr:annexDel")%>;
</script>
</head>

<body>
	<div class="easyui-layout" style="width: 100%; height: 650px;">
		<div data-options="region:'center'">
			<table id="datagrid" class="easyui-datagrid" fit="true"
				singleSelect="true" toolbar="#toolbar" border="false"
				fitColumns="true" pagination="true" pageSize="20">
				<thead>
					<tr>
						<th field="ck" checkbox="true"></th>
						<th field="typeDict" width="50">合同类型</th>
						<th field="code" width="50">合同编号</th>
						<th field="name" width="50">合同名称</th>
						<th field="keyDesr" width="50">关键字说明</th>
						<th field="annexs" width="100" formatter="Interactive.contractAnnexFormatter">附件清单</th>
						<th field="updateDate" width="50">最后修改时间</th>
						<th field="modifyUser" width="50">最后修改人</th>
					</tr>
				</thead>
			</table>
			<div id="toolbar">
				<table style="line-height:26px;padding: 10px;">
					<tr>
						<td>合同类型：</td>
						<td>
							<sys:dictselect id="contractType" name="contractType" dictType="<%=EmpConstant.DICT_CONTRACT_TYPE %>" needNull="true" attrHtml="style='height:22px;'"/>
						</td>
						<td>合同编号：</td>
						<td><input id="code" name="code" type="text" style="width: 120px;"/></td>
						<td>合同名称：</td>
						<td><input id="name" name="name" type="text" style="width: 120px;"/></td>
						<td>关键字：</td>
						<td><input id="keyDesr" name="keyDesr" type="text" style="width: 140px;"/></td>
						<td>
							<a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							
							<shiro:hasPermission name="emp:contractmgr:add">
								<a id="addBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add">新增</a>
		    				</shiro:hasPermission>
		    				<shiro:hasPermission name="emp:contractmgr:edit">
								<a id="editBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">编辑</a>
		    				</shiro:hasPermission>
		    				<shiro:hasPermission name="emp:contractmgr:annexUpload">
								<a id="uploadBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit">附件上传</a>
		    				</shiro:hasPermission>
		    				<shiro:hasPermission name="emp:contractmgr:del">
								<a id="delBtn" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove">删除</a>
		    				</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</div>
			<!-- 新增 -->
			<div id="addAndEditPopWin" class="easyui-window" modal="true" collapsible="false" 
				 minimizable="false" maximizable="false" closed="true" resizable="false" title="合同新增或修改"
				 style="width:330px;padding:10px;">
			    <form id="addEditForm" method="post">
			        <table cellpadding="5">
		                <tr>
		                    <td>合同类型<font color="red">*</font>:</td>
		                    <td><sys:dictselect id="ud_contractType" name="contractType" dictType="<%=EmpConstant.DICT_CONTRACT_TYPE %>" needNull="false" attrHtml="style='height:22px;'"/></td>
		                </tr>
		                <tr>
		                    <td>合同编号<font color="red">*</font>:</td>
		                    <td><input id="ud_code" class="easyui-textbox" type="text" name="code" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>合同名称<font color="red">*</font>:</td>
		                    <td><input id="ud_name" class="easyui-textbox" type="text" name="name" data-options="required:true"></input></td>
		                </tr>
		                <tr>
		                    <td>关键字说明:</td>
		                    <td><input id="ud_keyDesr" class="easyui-textbox" name="keyDesr" data-options="multiline:true" style="height:60px"></input></td>
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