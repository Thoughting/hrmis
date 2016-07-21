//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/emp/setwork/list';
		Constant.$addEdit_form_url = basePath + '/api/emp/setwork/addOrUpdate';
		Constant.$export_url = basePath + "/api/emp/setwork/export";
		
		//部门下拉框
		Constant.$dept_combo_url = basePath + '/api/emp/dept/combo';
		//表格头
		Constant.$setwork_column_url = basePath + '/api/emp/setwork/column';
		
		//提交排班
		Constant.$commit_url = basePath + "/api/emp/setwork/commit";
		//回滚排班
		Constant.$rollback_url = basePath + "/api/emp/setwork/rollback";
	}
}

//界面组件
Component = {
	init : function(){
		Component.$addAndEditPopWin = $("#addAndEditPopWin");
		
		Component.$grid = $("#datagrid");
		Component.$grid.datagrid({
			url : Constant.$grid_list_url,
			onBeforeLoad : function(param){
				param.employeeDept = $("#employeeDept").val();
				param.setWorkDate = $("#setWorkDate").val();
			},
			onDblClickCell : function(index,field,value){
				if (!hasGridEdit) {
					$.messager.alert("提示", "没有排班编辑权限！");
					return;
				}
				var today = Common.parseISO8601(systemDate);
				var currentDay = Common.parseISO8601(field);
				if (isNaN(currentDay.getDate())) {
					return;
				}
				
				var row = Component.$grid.datagrid("getData").rows[index];
				
				if (row.commitStatus == 1) {
					$.messager.alert("提示", "员工：" + row.workMonth + "排班已提交，不能修改！");
					return;
				}
				
				if (row.entryDate != null && row.entryDate > field) {
					$.messager.alert("提示", "员工：" + row.name + row.entryDate + "才入职！");
					return;
				}
				
				if (row.quitCompanyDate != null && row.quitCompanyDate <= field) {
					$.messager.alert("提示", "员工：" + row.name + row.quitCompanyDate + "已离职！");
					return;
				}
				
				$("#ud_name").empty();
				$("#ud_name").append(row.name);
				$("#ud_setWorkDate").val(field);
				Interactive.popDateChange();
				
				$("#ud_work_radio").empty();
				$("#ud_work_radio").append('<input type="radio" value="1" name="isAtWork" checked="checked">');
				$("#ud_holiday_radio").empty();
				$("#ud_holiday_radio").append('<input type="radio" value="0" name="isAtWork">');
				
				var id = "";
				if (value == null) {
					$("input[name=isAtWork][value=1]").attr("checked",'checked');
					$("#ud_atWorkTimer").val(0);
					$("#ud_atWorkSpecialStatus").val(0);
					$("#ud_atHolidayType").val(0);
					$("#ud_atHolidayHour").val(0);
					$("#ud_atHolidayMinute").val(0);
					$("input[name=atHolidayTimer][value=0]").prop("checked",true);
				} else {
					id = value.id;
					$("input[name=isAtWork][value=" + value.isAtWork + "]").attr("checked","checked");
					$("#ud_atWorkTimer").val(value.atWorkTimer);
					$("#ud_atWorkSpecialStatus").val(value.atWorkSpecialStatus);
					$("#ud_atHolidayType").val(value.atHolidayType);
					$("#ud_atHolidayHour").val(value.atHolidayHour);
					$("#ud_atHolidayMinute").val(value.atHolidayMinute);
					$("input[name=atHolidayTimer][value=" + value.atHolidayTimer + "]").prop("checked",true);
				}
				Component.$addEditForm.form({
					queryParams : {id : id,employeeId : row.employeeId}
				});
				Component.$addAndEditPopWin.window('open');
				Component.$addAndEditPopWin.window('center');
			}
		});
		
		Component.$addEditForm = $('#addEditForm');
		Component.$addEditForm.form({
			url : Constant.$addEdit_form_url,
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
					Component.$addAndEditPopWin.window('close');
				} else {
					$.messager.alert('提示','操作失败!');
				}
			}
		});
	}
}

//界面交互
Interactive = {
	init : function(){
		//回车查询
		$("body").keydown(function(event) {
			var c = event.keyCode;
			if(c == 13){
				$("#searchBtn").click();
			}
		});
		
		//得到部门下拉框
		$.ajax({
			url : Constant.$dept_combo_url,
			type : 'post',
			dataType : 'json',
			async : false,
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
		
		//查询
		$("#searchBtn").linkbutton({
			onClick:function(){
				Component.$grid.datagrid('reload');
			}
		});

		//上下午休假单选变更影响休假时长4小时
		$("input[name='atHolidayTimer']").change(function(){
			if ($("input[name='atHolidayTimer']:checked").val() != "0") {
				$("#ud_atHolidayHour").val("4");
			} else {
				$("#ud_atHolidayHour").val("0");
			}
		});

		//提交排班
		$("#commitBtn").linkbutton({
			onClick:function(){
				var selectNodes = Component.$grid.datagrid('getSelections');
				if (selectNodes != null) {
					$.messager.confirm('提示', '确定提交排班吗？', function(r){
						if (r){
							$.ajax({
								url : Constant.$commit_url,
								type : 'post',
								dataType : 'json',
								data : {
									employees : JSON.stringify(selectNodes),
									setWorkDate : $("#setWorkDate").val()
								},
								success : function(data) {
									if (data.success) {
										Component.$grid.datagrid('reload');
										$.messager.alert("提示", "操作成功");
									} else {
										$.messager.alert("错误", data.msg, "error");
									}
								},
								error : function(re, status, err) {
									$.messager.alert("错误", re.responseText, "error");
								}
							});
						}
					});
				} else {
					$.messager.alert('提示','请选择需要操作的记录!');
				}
			}
		})
		
		//回滚排班
		$("#rollbackBtn").linkbutton({
			onClick:function(){
				var selectNodes = Component.$grid.datagrid('getSelections');
				if (selectNodes != null) {
					$.messager.confirm('提示', '确定回滚排班吗？', function(r){
						if (r){
							$.ajax({
								url : Constant.$rollback_url,
								type : 'post',
								dataType : 'json',
								data : {
									employees : JSON.stringify(selectNodes),
									setWorkDate : $("#setWorkDate").val()
								},
								success : function(data) {
									if (data.success) {
										Component.$grid.datagrid('reload');
										$.messager.alert("提示", "操作成功");
									} else {
										$.messager.alert("错误", data.msg, "error");
									}
								},
								error : function(re, status, err) {
									$.messager.alert("错误", re.responseText, "error");
								}
							});
						}
					});
				} else {
					$.messager.alert('提示','请选择需要操作的记录!');
				}
			}
		})
		
		//导出
		$("#exportBtn").linkbutton({
			onClick:function(){
				Common.openPostWindow2(Constant.$export_url,{
					employeeDept : $("#employeeDept").val(),
					setWorkDate : $("#setWorkDate").val()
				});
			}
		})
		
		//编辑框提交
		$("#addEditSubmitBtn").on("click",function(){
			$.messager.progress({msg:'请稍后...'});
			$('#addEditForm').form('submit');
		});
		
		//编辑框取消
		$("#addEditCancleBtn").on("click",function(){
			Component.$addAndEditPopWin.window('close');
		})
		
		Interactive.refreshGridColumn();
	},
	refreshGridColumn : function(){
		//得到排班表格表格头
		$.ajax({
			url : Constant.$setwork_column_url,
			type : 'post',
			dataType : 'json',
			data : {date : $("#setWorkDate").val()},
			success : function(data) {
				if (data.success) {
					var workCells = data.model[1];
					for(var i = 0;i < workCells.length;i++){
						workCells[i].formatter = Interactive.workTypeCellFormatter;
					}
					Component.$grid.datagrid({
						columns : data.model,
						frozenColumns : [[
						   {field:"ck",checkbox:true},
						   {field:"commitStatusDict",title:"提交状态",align:"center",width:"80"},
			               {field:"code",title:"编号",align:"center",width:"80"},
			               {field:"name",title:"姓名",align:"center",width:"80"}
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
	workTypeCellFormatter : function(value,row,index){
		var backgoundColor = "#FFF";
		var describe = "&nbsp;";
		if(value != null){
			if (typeof value == 'string') {
				describe = value;
			} else {
				backgoundColor = value.gridCellDescribe[0];
				describe = value.gridCellDescribe[1];
			}
		}
		return '<div style="border: 1px solid #CCCCCC;background-color:' + backgoundColor + '"><a style="text-decoration:none;">' + describe + '</a></div>';
	},
	popDateChange : function(){
		$("#ud_week").empty();
		$("#ud_week").append("星期" + "日一二三四五六".charAt(new Date($("#ud_setWorkDate").val()).getDay()));
	}
}

$(function() {
	Constant.init();
	Component.init();
	Interactive.init();
});