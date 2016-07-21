//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/emp/checkwork/list';
		Constant.$addEdit_form_url = basePath + '/api/emp/checkwork/addOrUpdate';
		Constant.$export_url = basePath + "/api/emp/checkwork/export";
		
		Constant.$check_edit_url = basePath + "/api/emp/checkwork/checkEdit";
		
		//部门下拉框
		Constant.$dept_combo_url = basePath + '/api/emp/dept/combo';
		//表格头
		Constant.$setwork_column_url = basePath + '/api/emp/checkwork/column';
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
				if (hasGridEdit == "false") {
					$.messager.alert("提示", "没有考勤编辑权限！");
					return;
				}
				var today = Common.parseISO8601(systemDate);
				var currentDay = Common.parseISO8601(field);
				if (isNaN(currentDay.getDate())) {
					return;
				}
				if (currentDay > today) {
					return;
				}
				
				var row = Component.$grid.datagrid("getData").rows[index];
				
				//判断是否有编辑权限，3个工作日，除去周六日和设置的节日
				// var canEdit = false;
				// $.ajax({
				// 	url : Constant.$check_edit_url,
				// 	type : 'post',
				// 	dataType : 'json',
				// 	async : false,
				// 	data : {date:field},
				// 	success : function(data) {
				// 		if (data.success) {
				// 			canEdit = true;
				// 		}
				// 	},
				// 	error : function(re, status, err) {
				// 		$.messager.alert("错误", re.responseText, "error");
				// 	}
				// });
				// if (!canEdit) {
				// 	$.messager.alert("提示", "不能编辑！");
				// 	return;
				// }
				
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
				$("#ud_outwork_radio").empty();
				$("#ud_outwork_radio").append('<input type="radio" value="2" name="isAtWork">');
				$("#ud_hasOverTime_radio").empty();
				$("#ud_hasOverTime_radio").append('<input type="radio" value="1" name="hasOverTime">是');
				$("#ud_hasOverTime_radio").append('<input type="radio" value="0" name="hasOverTime">否');
				
				var id = "";
				if (value == null) {
					$("input[name=isAtWork][value=1]").attr("checked",'checked');
					$("#ud_atWorkTimer").val(0);
					$("#ud_atWorkStatus").val(0);
					$("#ud_atWorkStatusMinute").val(0);
					$("#ud_atWorkSpecialStatus").val(0);
					$("#ud_atHolidayType").val(0);
					$("#ud_atHolidayHour").val(0);
					$("#ud_atHolidayMinute").val(0);
					$("input[name=atHolidayTimer][value=0]").prop("checked",true);
					$("#ud_outWorkHour").val(0);
					$("#ud_outWorkMinute").val(0);
					$("input[name=hasOverTime][value=0]").attr("checked",'checked');
					$("#ud_overTimeHour").val(0);
					$("#ud_overTimeMinute").val(0);
					$("#ud_remark").val('');
				} else {
					id = value.id;
					$("input[name=isAtWork][value=" + value.isAtWork + "]").attr("checked",true);
					$("#ud_atWorkTimer").val(value.atWorkTimer);
					$("#ud_atWorkStatus").val(value.atWorkStatus);
					$("#ud_atWorkStatusMinute").val(value.atWorkStatusMinute);
					$("#ud_atWorkSpecialStatus").val(value.atWorkSpecialStatus);
					$("#ud_atHolidayType").val(value.atHolidayType);
					$("#ud_atHolidayHour").val(value.atHolidayHour);
					$("#ud_atHolidayMinute").val(value.atHolidayMinute);
					$("input[name=atHolidayTimer][value=" + value.atHolidayTimer + "]").prop("checked",true);
					$("#ud_outWorkHour").val(value.outWorkHour);
					$("#ud_outWorkMinute").val(value.outWorkMinute);
					$("input[name=hasOverTime][value=" + value.hasOverTime + "]").attr("checked",true);
					$("#ud_overTimeHour").val(value.overTimeHour);
					$("#ud_overTimeMinute").val(value.overTimeMinute);
					$("#ud_remark").val(value.remark);
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
						var today = Common.parseISO8601(systemDate);
						var currentDay = Common.parseISO8601(workCells[i].field);
						if (isNaN(currentDay.getDate()) || currentDay > today) {
							continue;
						}
						workCells[i].formatter = Interactive.workTypeCellFormatter;
					}
					Component.$grid.datagrid({
						columns : data.model,
						frozenColumns : [[
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