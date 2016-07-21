//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/emp/wage/list';
		Constant.$addEdit_form_url = basePath + '/api/emp/actualwageitem/addOrUpdate';
		Constant.$export_url = basePath + '/api/emp/wage/check_export';
		
		Constant.$wageplan_combo_url = basePath + '/api/emp/wageplan/combo';
		Constant.$wageplan_column_url = basePath + '/api/emp/wageplan/column';
		
		Constant.$dept_combo_url = basePath + '/api/emp/dept/combo';
		Constant.$post_combo_url = basePath + '/api/emp/post/combo';
		
		//审核
		Constant.$audit_url = basePath + "/api/emp/wage/audit";
	}
}

//界面组件
Component = {
	init : function(){
		Component.$addAndEditPopWin = $("#addAndEditPopWin");
		Component.$grid = $("#datagrid");
	}
}

//界面交互
Interactive = {
	init : function(){
		//查询
		$("#searchBtn").linkbutton({
			onClick:function(){
				Interactive.refreshGridColumn();
			}
		});
		
		//得到工资方案下拉框
		$.ajax({
			url : Constant.$wageplan_combo_url,
			type : 'post',
			dataType : 'json',
			async : false,
			data : {},
			success : function(data) {
				if (data.success) {
					$("#wagePlanCombo").empty();
					$.each(data.model, function(i, n){
						$("#wagePlanCombo").append("<option value='" + n.id + "'>" + n.name + "</option>");
					});
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		});
		
		$("#wagePlanCombo").change(function(){
			Interactive.getPostCombo();
		})
		
		//得到部门下拉框
		$.ajax({
			url : Constant.$dept_combo_url,
			type : 'post',
			dataType : 'json',
			data : {},
			success : function(data) {
				if (data.success) {
					$("#employeeDept").empty();
					$("#employeeDept").append("<option value=''>--请选择--</option>");
					$.each(data.model, function(i, n){
						$("#employeeDept").append("<option value='" + n.id + "'>" + n.name + "</option>");
					});
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		});
		
		//审核
		$("#audit_form").form({
			url : Constant.$audit_url,
			onSubmit : function(){
				var isValid = $(this).form('validate');
				if (!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			success: function(data){
				$.messager.progress('close');
				var data = eval('(' + data + ')');
				if (data.success) {
					Component.$grid.datagrid('reload');
					$.messager.alert("提示", "操作成功");
					$("#audit_PopWin").window('close');
				} else {
					$.messager.alert('提示','操作失败!');
				}
			}
		});
		$("#auditBtn").click(function(){
			var selectNodes = Component.$grid.datagrid('getSelections');
			if (selectNodes != null) {
				$("#audit_form").form({
					queryParams : {wages : JSON.stringify(selectNodes)}
				});
				$("#audit_PopWin").window('open');
				$("#audit_PopWin").window('center');
			} else {
				$.messager.alert('提示','请选择需要操作的记录!');
			}
		});
		//审核信息提交
		$("#audit_submitBtn").click(function(){
			$.messager.progress({msg:'请稍后...'});
			$('#audit_form').form('submit');
		});
		
		//提交审核
		$("#sendAuditBtn").click(function(){
			var selectNodes = Component.$grid.datagrid('getSelections');
			if (selectNodes != null && selectNodes.length > 0) {
				$.messager.confirm('提示', '确定提交审核吗？', function(r){
					if (r){
						$.messager.progress({msg:'请稍后...'});
						$.ajax({
							url : Constant.$audit_url,
							type : 'post',
							dataType : 'json',
							data : {
								wages : JSON.stringify(selectNodes),
								auditStatus : 1
							},
							success : function(data) {
								$.messager.progress('close');
								if (data.success) {
									Component.$grid.datagrid('reload');
								} else {
									$.messager.alert("错误", data.msg, "error");
								}
							},
							error : function(re, status, err) {
								$.messager.progress('close');
								$.messager.alert("错误", re.responseText, "error");
							}
						});
					}
				});
			} else {
				$.messager.alert('提示','请选择需要操作的记录!');
			}
		});
		
		//重新审核
		$("#resetAuditBtn").click(function(){
			var selectNodes = Component.$grid.datagrid('getSelections');
			if (selectNodes != null && selectNodes.length > 0) {
				$.messager.confirm('提示', '确定重新审核吗？', function(r){
					if (r){
						$.messager.progress({msg:'请稍后...'});
						$.ajax({
							url : Constant.$audit_url,
							type : 'post',
							dataType : 'json',
							data : {
								wages : JSON.stringify(selectNodes),
								auditStatus : 0
							},
							success : function(data) {
								$.messager.progress('close');
								if (data.success) {
									Component.$grid.datagrid('reload');
								} else {
									$.messager.alert("错误", data.msg, "error");
								}
							},
							error : function(re, status, err) {
								$.messager.progress('close');
								$.messager.alert("错误", re.responseText, "error");
							}
						});
					}
				});
			} else {
				$.messager.alert('提示','请选择需要操作的记录!');
			}
		});
		
		//导出
		$("#exportBtn").linkbutton({
			onClick:function(){
				Common.openPostWindow2(Constant.$export_url,{
					wageDateStr : $("#wageDate").val(),
    				wagePlanId : $("#wagePlanCombo").val(),
    				employeePost : $("#employeePost").val(),
    				employeeDept : $("#employeeDept").val(),
    				employeeName : $("#name").val()
				});
			}
		})
		
		Interactive.refreshGridColumn();
		Interactive.getPostCombo();
	},
	getPostCombo : function(){
		//得到岗位下拉框
		$.ajax({
			url : Constant.$post_combo_url,
			type : 'post',
			dataType : 'json',
			data : {wagePlanId : $("#wagePlanCombo").val()},
			success : function(data) {
				if (data.success) {
					$("#employeePost").empty();
					$("#employeePost").append("<option value=''>--请选择--</option>");
					$.each(data.model, function(i, n){
						$("#employeePost").append("<option value='" + n.id + "'>" + n.name + "</option>");
					});
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		})
	},
	refreshGridColumn : function(){
		//得到工资方案表格头
		$.ajax({
			url : Constant.$wageplan_column_url,
			type : 'post',
			dataType : 'json',
			data : {id : $("#wagePlanCombo").val(),type:"核算"},
			success : function(data) {
				if (data.success) {
					for(var i = 0;i < data.model.length;i++){
						var cells = data.model[i];
						for(var j = 0;j < cells.length;j++){
							if (cells[j].showFormatter == 'editFormatter') {
								cells[j].formatter = Interactive.editFormatter;
							} else if (cells[j].showFormatter == 'valueFormatter') {
								cells[j].formatter = Interactive.valueFormatter;
							}
							//去掉单位
							var title = cells[j].title;
							if (title.indexOf("（") != -1) {
								cells[j].title = title.substr(0,title.indexOf("（"));
							}
						}
					}
					Component.$grid.datagrid({
						url : Constant.$grid_list_url,
		    			onBeforeLoad : function(param){
		    				param.wageDateStr = $("#wageDate").val();
		    				param.wagePlanId = $("#wagePlanCombo").val();
		    				param.employeePost = $("#employeePost").val();
		    				param.employeeDept = $("#employeeDept").val();
		    				param.employeeName = $("#name").val();
		    			},
						columns : data.model,
						frozenColumns : [[
						   {field:"ck",checkbox:true},
	                  	   {field:"auditStatusDict",title:"审核状态",align:"center",width:"100",formatter:Interactive.auditFormatter,styler:Interactive.auditStyler},
	                  	   {field:"deptName",title:"部门",align:"center",width:"120"},
						   {field:"postName",title:"岗位",align:"center",width:"120"},
			               {field:"employeeName",title:"姓名",align:"center",width:"100"},
			            ]]
					});
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		});
	},
	auditStyler : function(value,row,index){
		switch(row.auditStatus){
		case 1:
			//审核中
			return 'background-color:#FFCC33;';
			break;
		case 2:
			//审核通过
			return 'background-color:#99CC00;';
			break;
		case 3:
			//审核不通过
			return 'background-color:#DF584C;';
			break;
		}
	},
	auditFormatter : function(value,row,index){
		if (row.auditContent == null) {
			return value;
		}
		return "<div title='" + row.auditContent + "'>" + value + "</div>";
	},
	valueFormatter : function(value,row,index){
		var showValue = 0;
		if (value.model != null) {
			showValue = value.model.count;
		}
		return showValue;
	},
	editFormatter : function(value,row,index){
		var employeeWageId = row.employeeWageId;
		var wageCountItemId = value.wageCountItemId;
		var showValue = 0;
		var employeeWageActualItemId = null;
		var backgroundColor = "#FFFFFF";
		if (value.model != null) {
			showValue = value.model.count;
			employeeWageActualItemId = value.model.id;
			backgroundColor = value.backgroundColor;
		}
		if (row.auditStatus == 1 || row.auditStatus == 2) {
			//审核中，审核通过，状态下不能编辑
			return showValue;
		}
		if (hasGridEdit) {
			return '<input type="text" style="width: 70px;text-align:center;background-color:' + backgroundColor + ';border: none;IME-MODE: disabled;" maxlength="8" onchange="Interactive.inputChangeHandler(\'' + employeeWageActualItemId +'\',\'' + employeeWageId + '\',\'' + wageCountItemId + '\',this.value)" value="' + showValue + '"/>';
		} else {
			return showValue;
		}
	},
	inputChangeHandler : function(employeeWageActualItemId,employeeWageId,wageCountItemId,value){
		$.ajax({
			url : Constant.$addEdit_form_url,
			type : 'post',
			dataType : 'json',
			data : {
				id : employeeWageActualItemId,
				employeeWageId : employeeWageId,
				wageCountItemId : wageCountItemId,
				count : value
			},
			success : function(data) {
				if (data.success) {
					Component.$grid.datagrid('reload');
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			}
		});
	}
}

$(function() {
	Constant.init();
	Component.init();
	Interactive.init();
});