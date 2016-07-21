<%@page import="com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache"%>
<%@page import="com.eastcom.baseframe.common.utils.DateUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<%@ include file="/pages/include/param.jsp"%>
<html>
<head>
<title>人员编制动态表</title>
<meta name="decorator" content="default" />
<script src="${ctxScripts}/modules/emp/archives/compilationTableShow.js"></script>
<script type="text/javascript">
var gridCount = '${gridCount}';
var hasGridEdit = <%=SecurityCache.hasPermission("emp:compilationtablemgr:edit")%>;

</script>
</head>

<body>
	<div class="easyui-layout" style="width:100%;height:650px;">
        <div data-options="region:'center',iconCls:'icon-ok'">
            <div id="mainTab" class="easyui-tabs" fit="true" border="false" toolbar="#toolbar">
            	<c:if test="${not empty compilationTables}">
					<c:forEach items="${compilationTables}" var="compilationTable" varStatus="compilationTableStatus">
						<div id="${compilationTable.id }" title="${compilationTable.name }" style="overflow: hidden;">
				        	<table style="line-height:26px;padding: 10px;">
								<tr>
									<td><span id="${compilationTable.id }_total">合计：定编xx人，在编yy人，缺编XXX人</span></td>
									<td>
										<shiro:hasPermission name="emp:compliationtableshow:export">
											<a href="javascript:Interactive.exportBtnClickHandler()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">导出</a>
										</shiro:hasPermission>
									</td>
								</tr>
							</table>
				        	<table id="datagrid_${compilationTableStatus.count }" class="easyui-datagrid" style="width:100%;height: 567px;"
								singleSelect="true" border="true" url="${ctx }/api/emp/compilationcount/list?id=${compilationTable.id}">
								<thead frozen="true">
									<tr>
										<th field="leaderName" width="80" align="center">分管领导</th>
										<th field="deptName" width="150" align="center">项目部</th>
									</tr>
								</thead>
								<thead>
						            <tr>
						                <c:if test="${not empty compilationTable.posts}">
						                	<c:forEach items="${compilationTable.posts}" var="post" varStatus="postStatus">
						                		<th colspan="3">${post.name }</th>
						                	</c:forEach>
						                </c:if>

										<th colspan="3">小计</th>

						            </tr>
						            <tr>
						            	<c:if test="${not empty compilationTable.posts}">
						                	<c:forEach items="${compilationTable.posts}" var="post" varStatus="postStatus">
						                		<th field="${post.id }_DING" width="50" align="center" formatter="Interactive.dingFormatter">定</th>
				                				<th field="${post.id }_ZAI" width="50" align="center">在</th>
				                				<th field="${post.id }_QUE" width="50" align="center" formatter="Interactive.queFormatter">缺</th>
						                	</c:forEach>
						                </c:if>

										<th field="dept_DING_TOTAL" width="50" align="center">定</th>
										<th field="dept_ZAI_TOTAL" width="50" align="center">在</th>
										<th field="dept_QUE_TOTAL" width="50" align="center">缺</th>

						            </tr>
						        </thead>
							</table>
				        </div>
					</c:forEach>
				</c:if>
		    </div>
        </div>
    </div>
</body>
</html>