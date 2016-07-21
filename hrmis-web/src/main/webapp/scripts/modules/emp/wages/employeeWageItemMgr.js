//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/emp/employeewageitem/list';
		Constant.$addEdit_form_url = basePath + '/api/emp/employeewageitem/addOrUpdate';
		Constant.$export_url = basePath + "/api/emp/employeewageitem/export";
		
		Constant.$wageplan_combo_url = basePath + '/api/emp/wageplan/combo';
		Constant.$wageplan_column_url = basePath + '/api/emp/wageplan/column';
		
		Constant.$dept_combo_url = basePath + '/api/emp/dept/combo';
		Constant.$post_combo_url = basePath + '/api/emp/post/combo';
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
			// Interactive.refreshGridColumn();
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
		
		//导出
		$("#exportBtn").linkbutton({
			onClick:function(){
				Common.openPostWindow2(Constant.$export_url,{
					wagePlanId : $("#wagePlanCombo").val(),
					employeePost : $("#employeePost").val(),
					employeeDept : $("#employeeDept").val()
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
			data : {id : $("#wagePlanCombo").val(),type : "配置"},
			success : function(data) {
				if (data.success) {
					for(var i = 0;i < data.model.length;i++){
						var cells = data.model[i];
						for(var j = 0;j < cells.length;j++){
							if (cells[j].showFormatter == 'editFormatter') {
								cells[j].formatter = Interactive.editFormatter;
							} else if (cells[j].showFormatter == 'nullFormatter') {
								cells[j].formatter = Interactive.nullFormatter;
							}
						}
					}
					Component.$grid.datagrid({
						url : Constant.$grid_list_url,
						onBeforeLoad : function(param){
							param.wagePlanId = $("#wagePlanCombo").val();
							param.employeePost = $("#employeePost").val();
							param.employeeDept = $("#employeeDept").val();
							param.name = $("#name").val();
						},
						columns : data.model,
						frozenColumns : [[
						   {field:"deptName",title:"部门",align:"center",width:"120"},
						   {field:"postName",title:"岗位",align:"center",width:"120"},
			               {field:"employeeName",title:"姓名",align:"center",width:"120"}
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
	nullFormatter : function(value,row,index){
		return "";
	},
	editFormatter : function(value,row,index){
		var employeeId = row.employeeId;
		var wageCountItemId = value.wageCountItemId;
		var showValue = 0;
		var employeeWageItemId = null;
		var backgroundColor = "#FFFFFF";
		if (value.model != null) {
			showValue = value.model.count;
			employeeWageItemId = value.model.id;
			backgroundColor = value.backgroundColor;
		}
		if (hasGridEdit) {
			return '<input type="text" style="width: 70px;text-align:center;background-color:' + backgroundColor + ';border: none;IME-MODE: disabled;" maxlength="8" onchange="Interactive.inputChangeHandler(\'' + employeeWageItemId +'\',\'' + employeeId + '\',\'' + wageCountItemId + '\',this.value)" value="' + showValue + '"/>';
		} else {
			return showValue;
		}
	},
	inputChangeHandler : function(employeeWageItemId,employeeId,wageCountItemId,value){
		$.ajax({
			url : Constant.$addEdit_form_url,
			type : 'post',
			dataType : 'json',
			data : {
				id : employeeWageItemId,
				employeeId : employeeId,
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