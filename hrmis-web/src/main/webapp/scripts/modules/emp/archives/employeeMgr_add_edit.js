// 用户管理
$.namespace('emp.contract.contractTypeMgr');

//界面变量
Constant = {
	init : function(){
		//员工
		Constant.$delete_url = basePath + "/api/emp/info/delete";
		Constant.$addEdit_form_url = basePath + "/api/emp/info/addOrUpdate";
		Constant.$detail_url = basePath + "/api/emp/info/detail";
		
		Constant.$dept_combo_url = basePath + '/api/emp/dept/combo';
		Constant.$post_combo_url = basePath + '/api/emp/post/combo';
		Constant.$wageplan_combo_url = basePath + '/api/emp/wageplan/combo';
		
		Constant.$header_img_url = basePath + '/api/emp/info/headerImg';
		Constant.$header_upload_url = basePath + '/api/emp/info/headerUpload';
		
		//员工家庭成员
		Constant.$family_grid_list_url = basePath + '/api/emp/family/list';
		Constant.$family_addEdit_form_url = basePath + "/api/emp/family/addOrUpdate";
		Constant.$family_delete_url = basePath + "/api/emp/family/delete";
		
		//员工学习经历
		Constant.$study_grid_list_url = basePath + '/api/emp/study/list';
		Constant.$study_addEdit_form_url = basePath + "/api/emp/study/addOrUpdate";
		Constant.$study_delete_url = basePath + "/api/emp/study/delete";
		
		//员工工作经历
		Constant.$work_grid_list_url = basePath + '/api/emp/work/list';
		Constant.$work_addEdit_form_url = basePath + "/api/emp/work/addOrUpdate";
		Constant.$work_delete_url = basePath + "/api/emp/work/delete";
		
		//员工附件
		Constant.$employeeAnnexList_url = basePath + "/api/emp/info/employeeAnnexList";
		Constant.$employeeAnnexUpload_url = basePath + "/api/emp/info/employeeAnnexUpload";
		Constant.$employeeAnnexDownload_url = basePath + "/api/emp/info/employeeAnnexDownload";
		Constant.$employeeAnnexDelete_url = basePath + "/api/emp/info/employeeAnnexDelete";
		
		//员工审核
		Constant.$employeeAudit_url = basePath + "/api/emp/info/audit";
		
		//员工编号自动生成
		Constant.$employeeCodeAutoCreate_url = basePath + "/api/emp/dept/newEmpCode";
		
	}
}

//界面组件
Component = {
	init : function(){
		
		//员工家庭成员
		Component.$family_addAndEditPopWin = $("#family_addAndEditPopWin");
		
		Component.$family_grid = $("#family_datagrid");
		Component.$family_grid.datagrid({
			url : Constant.$family_grid_list_url,
			onBeforeLoad : function(param){
				param.employeeId = employeeId;
			}
		});
		
		Component.$family_addEditForm = $('#family_addEditForm');
		Component.$family_addEditForm.form({
			url : Constant.$family_addEdit_form_url,
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
					Component.$family_grid.datagrid('reload');
					$.messager.alert("提示", "操作成功");
					Component.$family_addAndEditPopWin.window('close');
				} else {
					$.messager.alert('提示','操作失败!');
				}
			}
		});
		
		//员工学习经历
		Component.$study_addAndEditPopWin = $("#study_addAndEditPopWin");
		
		Component.$study_grid = $("#study_datagrid");
		Component.$study_grid.datagrid({
			url : Constant.$study_grid_list_url,
			onBeforeLoad : function(param){
				param.employeeId = employeeId;
			}
		});
		
		Component.$study_addEditForm = $('#study_addEditForm');
		Component.$study_addEditForm.form({
			url : Constant.$study_addEdit_form_url,
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
					Component.$study_grid.datagrid('reload');
					$.messager.alert("提示", "操作成功");
					Component.$study_addAndEditPopWin.window('close');
				} else {
					$.messager.alert('提示','操作失败!');
				}
			}
		});
		
		//员工工作经历
		Component.$work_addAndEditPopWin = $("#work_addAndEditPopWin");
		
		Component.$work_grid = $("#work_datagrid");
		Component.$work_grid.datagrid({
			url : Constant.$work_grid_list_url,
			onBeforeLoad : function(param){
				param.employeeId = employeeId;
			}
		});
		
		Component.$work_addEditForm = $('#work_addEditForm');
		Component.$work_addEditForm.form({
			url : Constant.$work_addEdit_form_url,
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
					Component.$work_grid.datagrid('reload');
					$.messager.alert("提示", "操作成功");
					Component.$work_addAndEditPopWin.window('close');
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
		//窗口关闭时候判断是否有点击过保存操作,如果没有,则须删除刚开始新建的对象
		window.onbeforeunload = function(){
			if (saveFlag == "0") {
				$.ajax({
					url : Constant.$delete_url,
					type : 'post',
					dataType : 'json',
					data : {deleteJson : JSON.stringify([{id:employeeId}])}
				});
			}
		}
		
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
					var firstVal = "";
					$.each(data.model, function(i, n){
						if (i == 0) {
							firstVal = n.id;
						}
						$("#employeeDept").append("<option value='" + n.id + "'>" + n.name + "</option>");
					});
					$("#employeeDept").val(firstVal);
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		});

		//部门下拉框变更自动生成员工编号
		$("#employeeDept").change(function(){
			Interactive.getNewEmployeeCode();
		})

		//得到工资方案下拉框
		$.ajax({
			url : Constant.$wageplan_combo_url,
			type : 'post',
			dataType : 'json',
			async : false,
			data : {},
			success : function(data) {
				if (data.success) {
					$("#wagePlan").empty();
					$.each(data.model, function(i, n){
						$("#wagePlan").append("<option value='" + n.id + "'>" + n.name + "</option>");
					});
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		});
		
		
		//得到岗位下拉框
		Interactive.getEmployeePost();
		$("#employeePostType").change(function(){
			Interactive.enrtyDateChangeHandler();
			Interactive.getEmployeePost();
		});
		
		//得到员工档案详情
		Interactive.getEmployeeDetail();
		
		//点击头像上传
		$("#headerImgBtn").click(function(){
			$.upload({
				url: Constant.$header_upload_url, 
				fileName: 'file',
				dataType: 'json',
				accept: 'image/*',
				params: {id:employeeId},
				onSend: function() {
					return true;
				},
				onComplate: function(data) {
					if (data.success) {
						Interactive.getHeadImg();
					} else {
						$.messager.alert("错误", data.msg, "error");
					}
				}
			});
		});
		Interactive.getHeadImg();
		
		//员工合同首页附件上传
		Interactive.employeeAnnexList("contractHome");
		$("#contractHomeAnnex_uploadBtn").linkbutton({
			onClick:function(){
				Interactive.employeeAnnexUpload("contractHome");
			}
		});
		
		//员工合同尾页附件上传
		Interactive.employeeAnnexList("contractFinal");
		$("#contractFinalAnnex_uploadBtn").linkbutton({
			onClick:function(){
				Interactive.employeeAnnexUpload("contractFinal");
			}
		});
		
		//员工签收表附件上传
		Interactive.employeeAnnexList("signForm");
		$("#signFormAnnex_uploadBtn").linkbutton({
			onClick:function(){
				Interactive.employeeAnnexUpload("signForm");
			}
		});
		
		//员工不购承诺附件上传
		Interactive.employeeAnnexList("nonPurchaseCommit");
		$("#nonPurchaseCommitAnnex_uploadBtn").linkbutton({
			onClick:function(){
				Interactive.employeeAnnexUpload("nonPurchaseCommit");
			}
		});
		
		//职位变更附件上传
		Interactive.employeeAnnexList("postChangeAnnex");
		$("#postChangeAnnex_uploadBtn").linkbutton({
			onClick:function(){
				Interactive.employeeAnnexUpload("postChangeAnnex");
			}
		});
		
		//部门变更附件上传
		Interactive.employeeAnnexList("deptChangeAnnex");
		$("#deptChangeAnnex_uploadBtn").linkbutton({
			onClick:function(){
				Interactive.employeeAnnexUpload("deptChangeAnnex");
			}
		});
		
		//操作变更附件上传
		Interactive.employeeAnnexList("operaChangeAnnex");
		$("#operaChangeAnnex_uploadBtn").linkbutton({
			onClick:function(){
				Interactive.employeeAnnexUpload("operaChangeAnnex");
			}
		});
		
		//工资变更附件上传
		Interactive.employeeAnnexList("wageChangeAnnex");
		$("#wageChangeAnnex_uploadBtn").linkbutton({
			onClick:function(){
				Interactive.employeeAnnexUpload("wageChangeAnnex");
			}
		});
		
		//身份证号码输入改变出生日期
		$("#cardNo").focusout(function(){
			var cardNo = $("#cardNo").val();
			if(idCardNoUtil.checkIdCardNo(cardNo)){
				$("#birthDate").val(Common.getBirthday(cardNo));
				Interactive.birthDateOnPicked();
			}else{
				$.messager.alert("提示", "身份证号格式错误");
			}
		})
		
		//身份证是否长期切换时间控件
		$("input[name='isCardNoLongTerm']").change(function(){
			Interactive.changeCardNoValidDateState();
		});
		
		//性别选择会更新退休时间
		$("input[name='sex']").change(function(){
			Interactive.birthDateOnPicked();
		});

		//合同种类变更
		$("#contractType").change(function(){
			Interactive.enrtyDateChangeHandler();
		})

		//合同——法定终止条件出现选中，清除合同结束时间
		$("input[name='contractTermCond']").change(function(){
			$("#contractEndDate").val('');
		});

		//是否参保影响其他保险项
		$("input[name='hasInsure']").change(function(){
			if ($("input[name='hasInsure'][value=1]").prop("checked")) {
				$("input[name=hasPersionInsure][value=1]").prop("checked",true);
				$("input[name=hasInjuryInsure][value=1]").prop("checked",true);
				$("input[name=hasBirthInsure][value=1]").prop("checked",true);
				$("input[name=hasMedicalInsure][value=1]").prop("checked",true);
				$("input[name=hasSeriousInsure][value=1]").prop("checked",true);
				
				$("input[name=hasNonPurchaseCommit][value=0]").prop("checked",true);
				$("input[name=hasGsbInsure][value=0]").prop("checked",true);
			} else {
				$("input[name=hasPersionInsure][value=0]").prop("checked",true);
				$("input[name=hasInjuryInsure][value=0]").prop("checked",true);
				$("input[name=hasBirthInsure][value=0]").prop("checked",true);
				$("input[name=hasMedicalInsure][value=0]").prop("checked",true);
				$("input[name=hasSeriousInsure][value=0]").prop("checked",true);
				
				$("input[name=hasNonPurchaseCommit][value=1]").prop("checked",true);
				$("input[name=hasGsbInsure][value=1]").prop("checked",true);
			
			}
		});
		
		//表单提交审核
		$("#sendAuditBtn").click(function(){
			//必填项检测
			if(!Interactive.validateForm()){
				return;
			}
			
			$.messager.progress({msg:'请稍后...'});
			$.ajax({
				url : Constant.$addEdit_form_url,
				type : 'post',
				dataType : 'json',
				data : {
					addOrUpdate : addOrUpdate,
					auditStatus : 1,
					id : employeeId,
					code : $("#code").val(),
					name : $("#name").val(),
					sex : $("input[name='sex']:checked").val(),
					age : Common.getAges($("#birthDate").val()),
					enrtyDate : $("#enrtyDate").val(),
					enrtyDateType : $("#enrtyDateType").val(),
					regularDate : $("#regularDate").val(),
					regularDateTwo : $("#regularDateTwo").val(),
					retireDate : $("#retireDate").val(),
					employeeDept : $("#employeeDept").val(),
					employeePost : $("#employeePost").val(),
					wagePlan : $("#wagePlan").val(),
					overTimeRate : $("#overTimeRate").val(),
					nation : $("#nation").val(),
					marryType : $("#marryType").val(),
					height : $("#height").val(),
					cardNo : $("#cardNo").val(),
					cardNoValidDate : $("#cardNoValidDate").val(),
					isCardNoLongTerm : $("input[name='isCardNoLongTerm']:checked").val(),
					birthDate : $("#birthDate").val(),
					education : $("#education").val(),
					manageLevel : $("#manageLevel").val(),
					jobTitle : $("#jobTitle").val(),
					jobCapacity : $("#jobCapacity").val(),
					polity : $("#polity").val(),
					driveLicenseType : $("#driveLicenseType").val(),
					driveLicenseGetDate : $("#driveLicenseGetDate").val(),
					driveLicenseValidDate : $("#driveLicenseValidDate").val(),
					major : $("#major").val(),
					nativePlaceType : $("#nativePlaceType").val(),
					nativePlaceAddr : $("#nativePlaceAddr").val(),
					nativePlace : $("#nativePlace").val(),
					contactAddr : $("#contactAddr").val(),
					telephone : $("#telephone").val(),
					emergentName : $("#emergentName").val(),
					emergentTelephone : $("#emergentTelephone").val(),
					mealRoomType : $("#mealRoomType").val(),
					performanceWageType : $("#performanceWageType").val(),
					laborType : $("#laborType").val(),
					contractStartDate : $("#contractStartDate").val(),
					contractEndDate : $("#contractEndDate").val(),
					contractSignDateType : $("#contractSignDateType").val(),
					contractSignDate : $("#contractSignDate").val(),
					contractTermCond : $("input[name='contractTermCond']").is(":checked") ? 1 : 0,
					nomalPayPlan : $("#nomalPayPlan").val(),
					performancePayPlan : $("#performancePayPlan").val(),
					hasRiskAgreement : $("input[name='hasRiskAgreement']:checked").val(),
					hasPercentAgreement : $("input[name='hasPercentAgreement']:checked").val(),
					bankType : $("#bankType").val(),
					bankCard : $("#bankCard").val(),
					characterRemark : $("#characterRemark").val(),
					hasLaborDispute : $("input[name='hasLaborDispute']:checked").val(),
					laborDisputeResult : $("#laborDisputeResult").val(),
					enrtyIntorducer : $("#enrtyIntorducer").val(),
					enrtyIntorducerCompany : $("#enrtyIntorducerCompany").val(),
					hasDiseaseHistory : $("input[name='hasDiseaseHistory']:checked").val(),
					diseaseHistory : $("#diseaseHistory").val(),
					hasFriendInCompany : $("input[name='hasFriendInCompany']:checked").val(),
					friendDept : $("#friendDept").val(),
					friendName : $("#friendName").val(),
					friendJobTitle : $("#friendJobTitle").val(),
					contractType : $("#contractType").val(),
					hasSignForm : $("input[name='hasSignForm']:checked").val(),
					hasInsure : $("input[name='hasInsure']:checked").val(),
					insureNo : $("#insureNo").val(),
					insureDate : $("#insureDate").val(),
					insurePayBase : $("#insurePayBase").val(),
					hasPersionInsure : $("input[name='hasPersionInsure']:checked").val(),
					hasInjuryInsure : $("input[name='hasInjuryInsure']:checked").val(),
					hasBirthInsure : $("input[name='hasBirthInsure']:checked").val(),
					hasMedicalInsure : $("input[name='hasMedicalInsure']:checked").val(),
					hasSeriousInsure : $("input[name='hasSeriousInsure']:checked").val(),
					hasGsbInsure : $("input[name='hasGsbInsure']:checked").val(),
					hasNonPurchaseCommit : $("input[name='hasNonPurchaseCommit']:checked").val(),
					hasPublicFund : $("input[name='hasPublicFund']:checked").val(),
					publicFundPayBase : $("#publicFundPayBase").val(),
					publicFundDate : $("#publicFundDate").val(),
					hasQuitCompany : $("input[name='hasQuitCompany']:checked").val(),
					quitCompanyType : $("#quitCompanyType").val(),
					quitCompanyDate : $("#quitCompanyDate").val(),
					quitCompanyResult : $("#quitCompanyResult").val(),
					postChangeRemark : $("#postChangeRemark").val(),
					deptChangeRemark : $("#deptChangeRemark").val(),
					operaChangeRemark : $("#operaChangeRemark").val(),
					wageChangeRemark : $("#wageChangeRemark").val()
				},
				success : function(data) {
					$.messager.progress('close');
					if (data.success) {
						saveFlag = "1";
						$.messager.confirm('提示', '审核提交成功,是否跳转至列表页', function(r){
							if (r){
								window.open(basePath + '/emp/archives/employeeMgr','_self');
							}
						});
					} else {
						$.messager.alert("错误", data.message, "error");
					}
				},
				error : function(re, status, err) {
					$.messager.progress('close');
					$.messager.alert("错误", re.responseText, "error");
				}
			});
		});
		
		//表单保存提交
		$("#saveBtn").on("click",function(){
			//必填项检测
			if(!Interactive.validateForm()){
				return;
			}
			
			$.messager.progress({msg:'请稍后...'});
			$.ajax({
				url : Constant.$addEdit_form_url,
				type : 'post',
				dataType : 'json',
				data : {
					addOrUpdate : addOrUpdate,
					id : employeeId,
					code : $("#code").val(),
					name : $("#name").val(),
					sex : $("input[name='sex']:checked").val(),
					age : Common.getAges($("#birthDate").val()),
					enrtyDate : $("#enrtyDate").val(),
					enrtyDateType : $("#enrtyDateType").val(),
					regularDate : $("#regularDate").val(),
					regularDateTwo : $("#regularDateTwo").val(),
					retireDate : $("#retireDate").val(),
					employeeDept : $("#employeeDept").val(),
					employeePost : $("#employeePost").val(),
					wagePlan : $("#wagePlan").val(),
					overTimeRate : $("#overTimeRate").val(),
					nation : $("#nation").val(),
					marryType : $("#marryType").val(),
					height : $("#height").val(),
					cardNo : $("#cardNo").val(),
					cardNoValidDate : $("#cardNoValidDate").val(),
					isCardNoLongTerm : $("input[name='isCardNoLongTerm']:checked").val(),
					birthDate : $("#birthDate").val(),
					education : $("#education").val(),
					manageLevel : $("#manageLevel").val(),
					jobTitle : $("#jobTitle").val(),
					jobCapacity : $("#jobCapacity").val(),
					polity : $("#polity").val(),
					driveLicenseType : $("#driveLicenseType").val(),
					driveLicenseGetDate : $("#driveLicenseGetDate").val(),
					driveLicenseValidDate : $("#driveLicenseValidDate").val(),
					major : $("#major").val(),
					nativePlaceType : $("#nativePlaceType").val(),
					nativePlaceAddr : $("#nativePlaceAddr").val(),
					nativePlace : $("#nativePlace").val(),
					contactAddr : $("#contactAddr").val(),
					telephone : $("#telephone").val(),
					emergentName : $("#emergentName").val(),
					emergentTelephone : $("#emergentTelephone").val(),
					mealRoomType : $("#mealRoomType").val(),
					performanceWageType : $("#performanceWageType").val(),
					laborType : $("#laborType").val(),
					contractStartDate : $("#contractStartDate").val(),
					contractEndDate : $("#contractEndDate").val(),
					contractSignDateType : $("#contractSignDateType").val(),
					contractSignDate : $("#contractSignDate").val(),
					contractTermCond : $("input[name='contractTermCond']").is(":checked") ? 1 : 0,
					nomalPayPlan : $("#nomalPayPlan").val(),
					performancePayPlan : $("#performancePayPlan").val(),
					hasRiskAgreement : $("input[name='hasRiskAgreement']:checked").val(),
					hasPercentAgreement : $("input[name='hasPercentAgreement']:checked").val(),
					bankType : $("#bankType").val(),
					bankCard : $("#bankCard").val(),
					characterRemark : $("#characterRemark").val(),
					hasLaborDispute : $("input[name='hasLaborDispute']:checked").val(),
					laborDisputeResult : $("#laborDisputeResult").val(),
					enrtyIntorducer : $("#enrtyIntorducer").val(),
					enrtyIntorducerCompany : $("#enrtyIntorducerCompany").val(),
					hasDiseaseHistory : $("input[name='hasDiseaseHistory']:checked").val(),
					diseaseHistory : $("#diseaseHistory").val(),
					hasFriendInCompany : $("input[name='hasFriendInCompany']:checked").val(),
					friendDept : $("#friendDept").val(),
					friendName : $("#friendName").val(),
					friendJobTitle : $("#friendJobTitle").val(),
					contractType : $("#contractType").val(),
					hasSignForm : $("input[name='hasSignForm']:checked").val(),
					hasInsure : $("input[name='hasInsure']:checked").val(),
					insureNo : $("#insureNo").val(),
					insureDate : $("#insureDate").val(),
					insurePayBase : $("#insurePayBase").val(),
					hasPersionInsure : $("input[name='hasPersionInsure']:checked").val(),
					hasInjuryInsure : $("input[name='hasInjuryInsure']:checked").val(),
					hasBirthInsure : $("input[name='hasBirthInsure']:checked").val(),
					hasMedicalInsure : $("input[name='hasMedicalInsure']:checked").val(),
					hasSeriousInsure : $("input[name='hasSeriousInsure']:checked").val(),
					hasGsbInsure : $("input[name='hasGsbInsure']:checked").val(),
					hasNonPurchaseCommit : $("input[name='hasNonPurchaseCommit']:checked").val(),
					hasPublicFund : $("input[name='hasPublicFund']:checked").val(),
					publicFundPayBase : $("#publicFundPayBase").val(),
					publicFundDate : $("#publicFundDate").val(),
					hasQuitCompany : $("input[name='hasQuitCompany']:checked").val(),
					quitCompanyType : $("#quitCompanyType").val(),
					quitCompanyDate : $("#quitCompanyDate").val(),
					quitCompanyResult : $("#quitCompanyResult").val(),
					postChangeRemark : $("#postChangeRemark").val(),
					deptChangeRemark : $("#deptChangeRemark").val(),
					operaChangeRemark : $("#operaChangeRemark").val(),
					wageChangeRemark : $("#wageChangeRemark").val()
				},
				success : function(data) {
					$.messager.progress('close');
					if (data.success) {
						saveFlag = "1";
						$.messager.confirm('提示', '保存成功,是否跳转至列表页', function(r){
							if (r){
								window.open(basePath + '/emp/archives/employeeMgr','_self');
							}
						});
					} else {
						$.messager.alert("错误", data.message, "error");
					}
				},
				error : function(re, status, err) {
					$.messager.progress('close');
					$.messager.alert("错误", re.responseText, "error");
				}
			});
		});
	},
	validateForm : function(){
		//验证表单
		//员工编号不能为空
		if (!Common.checkNotNull("员工编号",$("#code").val())) {
			return false;
		}
		//身份证号码
		if(!idCardNoUtil.checkIdCardNo($("#cardNo").val())){
			$.messager.alert("提示", "身份证号格式错误");
			return false;
		}
		
		//身份证有效期
		if ($("input[name='isCardNoLongTerm']:checked").val() == 0 && !Common.checkNotNull("身份证有效期",$.trim($("#cardNoValidDate").val()))) {
			return false;
		}
		//姓名
		if (!Common.checkNotNull("姓名",$("#name").val())) {
			return false;
		}
		//户籍地址
		if (!Common.checkNotNull("户籍地址",$("#nativePlaceAddr").val())) {
			return false;
		}
		//手机号码
		// if (!Common.checkPhone($("#telephone").val())) {
		// 	return false;
		// }
		//入职时间
		if (!Common.checkNotNull("入职时间",$("#enrtyDate").val())) {
			return false;
		}
		//第一阶段转正时间
		if (!Common.checkNotNull("第一阶段转正时间",$("#regularDate").val())) {
			return false;
		}
		//第二阶段转正时间
//		if (!Common.checkNotNull("第二阶段转正时间",$("#regularDateTwo").val())) {
//			return false;
//		}
		//合同期限
		if (!Common.checkNotNull("合同开始时间",$("#contractStartDate").val())) {
			return false;
		}
		if (!$("input[name='contractTermCond']").is(":checked") && !Common.checkNotNull("合同结束时间",$("#contractEndDate").val())) {
			return false;
		}
		//如果选择已离职，离职时间不能为空
		if ($("input[name='hasQuitCompany']:checked").val() == 1 && !Common.checkNotNull("离职时间",$("#quitCompanyDate").val())) {
			return false;
		}
		return true;
	},
	getEmployeeDetail:function(){
		//得到员工档案详情
		$.ajax({
			url : Constant.$detail_url,
			type : 'post',
			dataType : 'json',
			data : {id : employeeId},
			success : function(data) {
				if (data.success) {
					var employee = data.model;
					$("#code").val(employee.code);
					$("#name").val(employee.name);
					$("#age").empty();
					$("#age").append(employee.age);
					$("input[name=sex][value=" + employee.sex + "]").attr("checked",'checked');
					if (addOrUpdate == "update") {
						$("#enrtyDate").val(employee.enrtyDate);
						$("#enrtyDateType").val(employee.enrtyDateType);
						$("#regularDate").val(employee.regularDate);
						$("#regularDateTwo").val(employee.regularDateTwo);
					}
					
					$("#retireDate").val(employee.retireDate);
					if (employee.employeeDept != null) {
						$("#employeeDept").val(employee.employeeDept.id);
						// Interactive.getNewEmployeeCode();
					}
					Interactive.getNewEmployeeCode();

					if (employee.employeePost != null) {
						$("#employeePostType").val(employee.employeePost.type);
						
						//职务类别变更需要重新请求
						Interactive.getEmployeePost();
						$("#employeePost").val(employee.employeePost.id);
					}
					if (employee.wagePlan != null) {
						$("#wagePlan").val(employee.wagePlan.id);
					}
					$("#overTimeRate").val(employee.overTimeRate);
					
					$("#nation").val(employee.nation);
					$("#marryType").val(employee.marryType);
					$("#height").val(employee.height);
					$("#cardNo").val(employee.cardNo);
					if (addOrUpdate == "update") {
						$("#cardNoValidDate").val(employee.cardNoValidDate);
					}
					$("input[name=isCardNoLongTerm][value=" + employee.isCardNoLongTerm + "]").attr("checked",'checked');
					
					//身份证是否长期
					Interactive.changeCardNoValidDateState();
					
					$("#birthDate").val(employee.birthDate);
					$("#education").val(employee.education);
					$("#manageLevel").val(employee.manageLevel);
					$("#jobTitle").val(employee.jobTitle);
					$("#jobCapacity").val(employee.jobCapacity);
					$("#polity").val(employee.polity);
					$("#driveLicenseType").val(employee.driveLicenseType);
					if (addOrUpdate == "update") {
						$("#driveLicenseValidDate").val(employee.driveLicenseValidDate);
						$("#driveLicenseGetDate").val(employee.driveLicenseGetDate);
					}
					$("#major").val(employee.major);
					$("#nativePlaceType").val(employee.nativePlaceType);
					$("#nativePlaceAddr").val(employee.nativePlaceAddr);
					$("#nativePlace").val(employee.nativePlace);
					$("#contactAddr").val(employee.contactAddr);
					$("#telephone").val(employee.telephone);
					$("#emergentName").val(employee.emergentName);
					$("#emergentTelephone").val(employee.emergentTelephone);
					$("#mealRoomType").val(employee.mealRoomType);
					$("#performanceWageType").val(employee.performanceWageType);
					$("#laborType").val(employee.laborType);
					if (addOrUpdate == "update") {
						$("#contractStartDate").val(employee.contractStartDate);
						$("#contractEndDate").val(employee.contractEndDate);
						$("#contractSignDateType").val(employee.contractSignDateType);
						$("#contractSignDate").val(employee.contractSignDate);
					}
					if (employee.contractTermCond == 1) {
						$("input[name='contractTermCond']").attr("checked",true);
					}
					$("input[name=hasRiskAgreement][value=" + employee.hasRiskAgreement + "]").attr("checked",'checked');
					$("input[name=hasPercentAgreement][value=" + employee.hasPercentAgreement + "]").attr("checked",'checked');
					$("#bankType").val(employee.bankType);
					$("#bankCard").val(employee.bankCard);
					$("#characterRemark").val(employee.characterRemark);
					$("input[name=hasLaborDispute][value=" + employee.hasLaborDispute + "]").attr("checked",'checked');
					$("#laborDisputeResult").val(employee.laborDisputeResult);
					$("#enrtyIntorducer").val(employee.enrtyIntorducer);
					$("#enrtyIntorducerCompany").val(employee.enrtyIntorducerCompany);
					$("input[name=hasDiseaseHistory][value=" + employee.hasDiseaseHistory + "]").attr("checked",'checked');
					$("#diseaseHistory").val(employee.diseaseHistory);
					$("input[name=hasFriendInCompany][value=" + employee.hasFriendInCompany + "]").attr("checked",'checked');
					$("#friendDept").val(employee.friendDept);
					$("#friendName").val(employee.friendName);
					$("#friendJobTitle").val(employee.friendJobTitle);
					$("#contractType").val(employee.contractType);
					$("input[name=hasSignForm][value=" + employee.hasSignForm + "]").attr("checked",'checked');
					$("input[name=hasInsure][value=" + employee.hasInsure + "]").attr("checked",'checked');
					$("#insureNo").val(employee.insureNo);
					if (addOrUpdate == "update") {
						$("#insureDate").val(employee.insureDate);
					}
					$("#insurePayBase").val(employee.insurePayBase);
					$("input[name=hasPersionInsure][value=" + employee.hasPersionInsure + "]").attr("checked",'checked');
					$("input[name=hasInjuryInsure][value=" + employee.hasInjuryInsure + "]").attr("checked",'checked');
					$("input[name=hasBirthInsure][value=" + employee.hasBirthInsure + "]").attr("checked",'checked');
					$("input[name=hasMedicalInsure][value=" + employee.hasMedicalInsure + "]").attr("checked",'checked');
					$("input[name=hasSeriousInsure][value=" + employee.hasSeriousInsure + "]").attr("checked",'checked');
					$("input[name=hasGsbInsure][value=" + employee.hasGsbInsure + "]").attr("checked",'checked');
					$("input[name=hasNonPurchaseCommit][value=" + employee.hasNonPurchaseCommit + "]").attr("checked",'checked');
					$("input[name=hasPublicFund][value=" + employee.hasPublicFund + "]").attr("checked",'checked');
					$("#publicFundPayBase").val(employee.publicFundPayBase);
					if (addOrUpdate == "update") {
						$("#publicFundDate").val(employee.publicFundDate);
					}
					$("input[name=hasQuitCompany][value=" + employee.hasQuitCompany + "]").attr("checked",'checked');
					$("#quitCompanyType").val(employee.quitCompanyType);
					if (addOrUpdate == "update") {
						$("#quitCompanyDate").val(employee.quitCompanyDate);
					}
					$("#quitCompanyResult").val(employee.quitCompanyResult);
					$("#postChangeRemark").val(employee.postChangeRemark);
					$("#deptChangeRemark").val(employee.deptChangeRemark);
					$("#operaChangeRemark").val(employee.operaChangeRemark);
					$("#wageChangeRemark").val(employee.wageChangeRemark);
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		})
	},
	getEmployeePost:function(){
		//得到岗位下拉框
		$("#employeePost").empty();
		$.ajax({
			url : Constant.$post_combo_url,
			type : 'post',
			dataType : 'json',
			async : false,
			data : {type:$("#employeePostType").val()},
			success : function(data) {
				if (data.success) {
					$.each(data.model, function(i, n){
						$("#employeePost").append("<option value='" + n.id + "'>" + n.name + "</option>");
					});
					if (addOrUpdate == "add") {
						$("#employeePost").val("40288a195243f3c0015243fa1d9d0020");
					}
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		})
	},
	changeCardNoValidDateState:function(){
		if($("input[name='isCardNoLongTerm']:checked").val() == "0"){
			$("#cardNoValidDate").show();
		}else{
			$("#cardNoValidDate").hide();
		}
	},
	enrtyDateChangeHandler:function(){
		var day = Common.parseISO8601($("#enrtyDate").val());
		if (day == null || isNaN(day.getDate())) {
			return;
		}

		//合同开始时间以及合同结束时间
		$("#contractStartDate").val(Common.formatDate(day));
		switch($("#contractType").val()){
			case "0":
				//劳动合同
				day.setFullYear(day.getFullYear() + 3);
				break;
			case "1":
				//聘用协议
				day.setFullYear(day.getFullYear() + 1);
				break;
		}
		var retireDay = Common.parseISO8601($("#retireDate").val());
		//合同结束日期不能超过退休日期
		if (retireDay != null && retireDay <= day){
			$("#contractEndDate").val(Common.formatDate(retireDay));
		} else {
			$("#contractEndDate").val(Common.formatDate(day));
		}


		//第一阶段转正时间第二阶段转正时间
		var regulayDay = Common.parseISO8601($("#enrtyDate").val());
		var regulayDayTwo = Common.parseISO8601($("#enrtyDate").val());
		switch($("#employeePostType").val()){
			case "0":
				//生产操作岗位
				regulayDay.setMonth(regulayDay.getMonth() + 1);
				regulayDayTwo.setMonth(regulayDayTwo.getMonth() + 2);
				break;
			case "1":
				//管理技术岗位
				regulayDay.setMonth(regulayDay.getMonth() + 3);
				regulayDayTwo.setMonth(regulayDayTwo.getMonth() + 6);
				break;
		}
		$("#regularDate").val(Common.formatDate(regulayDay));
		$("#regularDateTwo").val(Common.formatDate(regulayDayTwo));
	},
	birthDateOnPicked:function(){
		//年龄
		$("#age").empty();
		$("#age").append(Common.getAges($("#birthDate").val()));
		//退休日期
		var dateStr = $("#birthDate").val();
		dateStr = dateStr.replace(/-/g,"/");
		var date = new Date(dateStr);
		if ($("input[name='sex']:checked").val() == "1") {
			date.setFullYear(date.getFullYear() + 60);
		} else {
			date.setFullYear(date.getFullYear() + 50);
		}
		$("#retireDate").val(date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate());
	},
	getHeadImg:function(){
		//得到用户头像
		$.ajax({
			url : Constant.$header_img_url,
			type : 'post',
			dataType : 'json',
			data : {id : employeeId},
			success : function(data) {
				$("#headerImg").attr("src",basePath + data.model);
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		})
	},
	employeeAnnexList : function(annexType){
		$.ajax({
			url : Constant.$employeeAnnexList_url,
			type : 'post',
			dataType : 'json',
			data : {id:employeeId ,employeeId : employeeId,annexType:annexType},
			success : function(data) {
				var result = "";
				if (data.message == "signForm") {
					//签到表
					$("#signFormAnnex_container").empty();
					if (data.model != null) {
						result += "<a href=\"javascript:Interactive.employeeAnnexDownload('signForm','')\">" + data.model + "</a>";
						result += "&nbsp;";
						result += "<a href=\"javascript:Interactive.employeeAnnexDelete('signForm','')\">删除</a>";
						result += "<br />";
						$("#signFormAnnex_container").append(result);
					}
				} else if (data.message == "nonPurchaseCommit"){
					//不购承诺
					$("#nonPurchaseCommitAnnex_container").empty();
					if (data.model != null) {
						result += "<a href=\"javascript:Interactive.employeeAnnexDownload('nonPurchaseCommit','')\">" + data.model + "</a>";
						result += "&nbsp;";
						result += "<a href=\"javascript:Interactive.employeeAnnexDelete('nonPurchaseCommit','')\">删除</a>";
						result += "<br />";
						$("#nonPurchaseCommitAnnex_container").append(result);
					}
				} else if (data.message == "contractHome"){
					//合同首页
					$("#homeAnnex_container").empty();
					if (data.model != null && data.model.length > 0) {
						for(var i = 0;i < data.model.length;i++){
							var model = data.model[i];
							result += "<a href=\"javascript:Interactive.employeeAnnexDownload('contractHome','" + model.id + "')\">" + model.annexName + "</a>";
							result += "&nbsp;";
							result += "<a href=\"javascript:Interactive.employeeAnnexDelete('contractHome','" + model.id + "')\">删除</a>";
							result += "<br />";
						}
						$("#homeAnnex_container").append(result);
					}
				} else if (data.message == "contractFinal"){
					//合同尾页
					$("#finalAnnex_container").empty();
					if (data.model != null && data.model.length > 0) {
						for(var i = 0;i < data.model.length;i++){
							var model = data.model[i];
							result += "<a href=\"javascript:Interactive.employeeAnnexDownload('contractFinal','" + model.id + "')\">" + model.annexName + "</a>";
							result += "&nbsp;";
							result += "<a href=\"javascript:Interactive.employeeAnnexDelete('contractFinal','" + model.id + "')\">删除</a>";
							result += "<br />";
						}
						$("#finalAnnex_container").append(result);
					}
				} else if (data.message == "postChangeAnnex"){
					//职位变更
					$("#postChangeAnnex_container").empty();
					if (data.model != null) {
						result += "<a href=\"javascript:Interactive.employeeAnnexDownload('postChangeAnnex','')\">" + data.model + "</a>";
						result += "&nbsp;";
						result += "<a href=\"javascript:Interactive.employeeAnnexDelete('postChangeAnnex','')\">删除</a>";
						result += "<br />";
						$("#postChangeAnnex_container").append(result);
					}
				} else if (data.message == "deptChangeAnnex"){
					//部门变更
					$("#deptChangeAnnex_container").empty();
					if (data.model != null) {
						result += "<a href=\"javascript:Interactive.employeeAnnexDownload('deptChangeAnnex','')\">" + data.model + "</a>";
						result += "&nbsp;";
						result += "<a href=\"javascript:Interactive.employeeAnnexDelete('deptChangeAnnex','')\">删除</a>";
						result += "<br />";
						$("#deptChangeAnnex_container").append(result);
					}
				} else if (data.message == "operaChangeAnnex"){
					//操作变更
					$("#operaChangeAnnex_container").empty();
					if (data.model != null) {
						result += "<a href=\"javascript:Interactive.employeeAnnexDownload('operaChangeAnnex','')\">" + data.model + "</a>";
						result += "&nbsp;";
						result += "<a href=\"javascript:Interactive.employeeAnnexDelete('operaChangeAnnex','')\">删除</a>";
						result += "<br />";
						$("#operaChangeAnnex_container").append(result);
					}
				} else if (data.message == "wageChangeAnnex"){
					//工资变更
					$("#wageChangeAnnex_container").empty();
					if (data.model != null) {
						result += "<a href=\"javascript:Interactive.employeeAnnexDownload('wageChangeAnnex','')\">" + data.model + "</a>";
						result += "&nbsp;";
						result += "<a href=\"javascript:Interactive.employeeAnnexDelete('wageChangeAnnex','')\">删除</a>";
						result += "<br />";
						$("#wageChangeAnnex_container").append(result);
					}
				}
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		})
	},
	employeeAnnexUpload : function(annexType){
		Page.upload.open(Constant.$employeeAnnexUpload_url + "?content=" + employeeId + "_" + annexType, function() {
			Interactive.employeeAnnexList(annexType);
		});
	},
	employeeAnnexDownload : function(annexType,annexId){
		window.open(Constant.$employeeAnnexDownload_url + "?id=" + employeeId + "&annexType=" + annexType + "&annexId=" + annexId);
	},
	employeeAnnexDelete : function(annexType,annexId){
		$.messager.confirm('提示', '确定删除？', function(r){
			if (r){
				$.ajax({
					url : Constant.$employeeAnnexDelete_url,
					type : 'post',
					dataType : 'json',
					data : {
						id : employeeId,
						annexType : annexType,
						annexId : annexId
					},
					success : function(data) {
						if (data.success) {
							Interactive.employeeAnnexList(data.model);
							$.messager.alert("提示", "删除成功");
						} else {
							$.messager.alert("错误", data.message, "error");
						}
					},
					error : function(re, status, err) {
						$.messager.alert("错误", re.responseText, "error");
					}
				});
			}
		});
	},
	getNewEmployeeCode : function(){
		$.ajax({
			url : Constant.$employeeCodeAutoCreate_url,
			type : 'post',
			dataType : 'json',
			data : {id:$("#employeeDept").val(),employeeId:employeeId},
			success : function(data) {
				if (data.success) {
					$("#code").val(data.model);
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		});
	}
}

//员工家庭成员
Interactive.EmployeeFamily = {
	init : function(){
		//新增
		$("#family_addBtn").linkbutton({
			onClick : function(){
				Component.$family_addEditForm.form({
					queryParams : {
						employeeId : employeeId
					}
				});
				Component.$family_addEditForm.form("reset");
				Component.$family_addAndEditPopWin.window('open');
				Component.$family_addAndEditPopWin.window('center');
			}
		});
		
		//编辑
		$("#family_editBtn").linkbutton({
			onClick : function(){
				var selectNode = Component.$family_grid.datagrid('getSelected');
				if (selectNode != null) {
					$("#family_ud_name").textbox("setValue",selectNode.name);
					$("#family_ud_relationShip").val(selectNode.relationShip);
					$("#family_ud_cardNo").textbox("setValue",selectNode.cardNo);
					$("#family_ud_company").textbox("setValue",selectNode.company);
					$("#family_ud_jobName").textbox("setValue",selectNode.jobName);
					$("#family_ud_telephone").textbox("setValue",selectNode.telephone);
					Component.$family_addEditForm.form({
						queryParams : {id : selectNode.id,employeeId : employeeId}
					});
					Component.$family_addAndEditPopWin.window('open');
					Component.$family_addAndEditPopWin.window('center');
				} else {
					$.messager.alert('提示','请选择需要操作的记录!');
				}
			}
		});
		
		//删除
		$("#family_delBtn").linkbutton({
			onClick : function(){
				var selectNodes = Component.$family_grid.datagrid('getSelections');
				if (selectNodes != null && selectNodes.length > 0) {
					$.messager.confirm('提示', '确定删除？', function(r){
						if (r){
							$.ajax({
								url : Constant.$family_delete_url,
								type : 'post',
								dataType : 'json',
								data : {deleteJson : JSON.stringify(selectNodes)},
								success : function(data) {
									if (data.success) {
										Component.$family_grid.datagrid('reload');
										$.messager.alert("提示", "删除成功");
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
		
		//编辑框提交
		$("#family_addEditSubmitBtn").on("click",function(){
			$.messager.progress({msg:'请稍后...'});
			Component.$family_addEditForm.form('submit');
		});
		
		//编辑框取消
		$("#family_addEditCancleBtn").on("click",function(){
			Component.$family_addAndEditPopWin.window('close');
		})
	}
}

//员工学习经历
Interactive.EmployeeStudy = {
	init : function(){
		//新增
		$("#study_addBtn").linkbutton({
			onClick : function(){
				Component.$study_addEditForm.form({
					queryParams : {
						employeeId : employeeId
					}
				});
				Component.$study_addEditForm.form("reset");
				Component.$study_addAndEditPopWin.window('open');
				Component.$study_addAndEditPopWin.window('center');
			}
		});
		
		//编辑
		$("#study_editBtn").linkbutton({
			onClick : function(){
				var selectNode = Component.$study_grid.datagrid('getSelected');
				if (selectNode != null) {
					$("#study_ud_startDate").val(selectNode.startDate);
					$("#study_ud_endDate").val(selectNode.endDate);
					$("#study_ud_school").textbox("setValue",selectNode.school);
					$("#study_ud_major").textbox("setValue",selectNode.major);
					$("#study_ud_educationType").val(selectNode.educationType);
					$("#study_ud_studyType").val(selectNode.studyType);
					Component.$study_addEditForm.form({
						queryParams : {id : selectNode.id,employeeId : employeeId}
					});
					Component.$study_addAndEditPopWin.window('open');
					Component.$study_addAndEditPopWin.window('center');
				} else {
					$.messager.alert('提示','请选择需要操作的记录!');
				}
			}
		});
		
		//删除
		$("#study_delBtn").linkbutton({
			onClick : function(){
				var selectNodes = Component.$study_grid.datagrid('getSelections');
				if (selectNodes != null && selectNodes.length > 0) {
					$.messager.confirm('提示', '确定删除？', function(r){
						if (r){
							$.ajax({
								url : Constant.$study_delete_url,
								type : 'post',
								dataType : 'json',
								data : {deleteJson : JSON.stringify(selectNodes)},
								success : function(data) {
									if (data.success) {
										Component.$study_grid.datagrid('reload');
										$.messager.alert("提示", "删除成功");
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
		
		//编辑框提交
		$("#study_addEditSubmitBtn").on("click",function(){
			$.messager.progress({msg:'请稍后...'});
			$('#study_addEditForm').form('submit');
		});
		
		//编辑框取消
		$("#study_addEditCancleBtn").on("click",function(){
			Component.$study_addAndEditPopWin.window('close');
		})
	}
}


//员工工作经历
Interactive.EmployeeWork = {
	init : function(){
		//新增
		$("#work_addBtn").linkbutton({
			onClick : function(){
				Component.$work_addEditForm.form({
					queryParams : {
						employeeId : employeeId
					}
				});
				Component.$work_addEditForm.form("reset");
				Component.$work_addAndEditPopWin.window('open');
				Component.$work_addAndEditPopWin.window('center');
			}
		});
		
		//编辑
		$("#work_editBtn").linkbutton({
			onClick : function(){
				var selectNode = Component.$work_grid.datagrid('getSelected');
				if (selectNode != null) {
					$("#work_ud_startDate").val(selectNode.startDate);
					$("#work_ud_endDate").val(selectNode.endDate);
					$("#work_ud_company").textbox("setValue",selectNode.company);
					$("#work_ud_jobName").textbox("setValue",selectNode.jobName);
					$("#work_ud_witness").textbox("setValue",selectNode.witness);
					$("#work_ud_telephone").textbox("setValue",selectNode.telephone);
					Component.$work_addEditForm.form({
						queryParams : {id : selectNode.id,employeeId : employeeId}
					});
					Component.$work_addAndEditPopWin.window('open');
					Component.$work_addAndEditPopWin.window('center');
				} else {
					$.messager.alert('提示','请选择需要操作的记录!');
				}
			}
		});
		
		//删除
		$("#work_delBtn").linkbutton({
			onClick : function(){
				var selectNodes = Component.$work_grid.datagrid('getSelections');
				if (selectNodes != null && selectNodes.length > 0) {
					$.messager.confirm('提示', '确定删除？', function(r){
						if (r){
							$.ajax({
								url : Constant.$work_delete_url,
								type : 'post',
								dataType : 'json',
								data : {deleteJson : JSON.stringify(selectNodes)},
								success : function(data) {
									if (data.success) {
										Component.$work_grid.datagrid('reload');
										$.messager.alert("提示", "删除成功");
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
		
		//编辑框提交
		$("#work_addEditSubmitBtn").on("click",function(){
			$.messager.progress({msg:'请稍后...'});
			$('#work_addEditForm').form('submit');
		});
		
		//编辑框取消
		$("#work_addEditCancleBtn").on("click",function(){
			Component.$work_addAndEditPopWin.window('close');
		})
	}
}

$(function() {
	Constant.init();
	Component.init();
	Interactive.init();
	
	Interactive.EmployeeFamily.init();
	Interactive.EmployeeStudy.init();
	Interactive.EmployeeWork.init();
});

var idCardNoUtil = {
	/*省,直辖市代码表*/
	provinceAndCitys : {
		11 : "北京",
		12 : "天津",
		13 : "河北",
		14 : "山西",
		15 : "内蒙古",
		21 : "辽宁",
		22 : "吉林",
		23 : "黑龙江",
		31 : "上海",
		32 : "江苏",
		33 : "浙江",
		34 : "安徽",
		35 : "福建",
		36 : "江西",
		37 : "山东",
		41 : "河南",
		42 : "湖北",
		43 : "湖南",
		44 : "广东",
		45 : "广西",
		46 : "海南",
		50 : "重庆",
		51 : "四川",
		52 : "贵州",
		53 : "云南",
		54 : "西藏",
		61 : "陕西",
		62 : "甘肃",
		63 : "青海",
		64 : "宁夏",
		65 : "新疆",
		71 : "台湾",
		81 : "香港",
		82 : "澳门",
		91 : "国外"
	},

	/*每位加权因子*/
	powers : [ "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9",
			"10", "5", "8", "4", "2" ],

	/*第18位校检码*/
	parityBit : [ "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" ],

	/*性别*/
	genders : {
		male : "男",
		female : "女"
	},

	/*校验地址码*/
	checkAddressCode : function(addressCode) {
		var check = /^[1-9]\d{5}$/.test(addressCode);
		if (!check)
			return false;
		if (idCardNoUtil.provinceAndCitys[parseInt(addressCode.substring(0, 2))]) {
			return true;
		} else {
			return false;
		}
	},

	/*校验日期码*/
	checkBirthDayCode : function(birDayCode) {
		var check = /^[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))$/
				.test(birDayCode);
		if (!check)
			return false;
		var yyyy = parseInt(birDayCode.substring(0, 4), 10);
		var mm = parseInt(birDayCode.substring(4, 6), 10);
		var dd = parseInt(birDayCode.substring(6), 10);
		var xdata = new Date(yyyy, mm - 1, dd);
		if (xdata > new Date()) {
			return false;//生日不能大于当前日期
		} else if ((xdata.getFullYear() == yyyy)
				&& (xdata.getMonth() == mm - 1) && (xdata.getDate() == dd)) {
			return true;
		} else {
			return false;
		}
	},

	/*计算校检码*/
	getParityBit : function(idCardNo) {
		var id17 = idCardNo.substring(0, 17);
		/*加权 */
		var power = 0;
		for (var i = 0; i < 17; i++) {
			power += parseInt(id17.charAt(i), 10)
					* parseInt(idCardNoUtil.powers[i]);
		}
		/*取模*/
		var mod = power % 11;
		return idCardNoUtil.parityBit[mod];
	},

	/*验证校检码*/
	checkParityBit : function(idCardNo) {
		var parityBit = idCardNo.charAt(17).toUpperCase();
		if (idCardNoUtil.getParityBit(idCardNo) == parityBit) {
			return true;
		} else {
			return false;
		}
	},

	/*校验15位或18位的身份证号码*/
	checkIdCardNo : function(idCardNo) {
		//15位和18位身份证号码的基本校验
		var check = /^\d{15}|(\d{17}(\d|x|X))$/.test(idCardNo);
		if (!check)
			return false;
		//判断长度为15位或18位  
		if (idCardNo.length == 15) {
			return idCardNoUtil.check15IdCardNo(idCardNo);
		} else if (idCardNo.length == 18) {
			return idCardNoUtil.check18IdCardNo(idCardNo);
		} else {
			return false;
		}
	},

	//校验15位的身份证号码
	check15IdCardNo : function(idCardNo) {
		//15位身份证号码的基本校验
		var check = /^[1-9]\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}$/
				.test(idCardNo);
		if (!check)
			return false;
		//校验地址码
		var addressCode = idCardNo.substring(0, 6);
		check = idCardNoUtil.checkAddressCode(addressCode);
		if (!check)
			return false;
		var birDayCode = '19' + idCardNo.substring(6, 12);
		//校验日期码
		return idCardNoUtil.checkBirthDayCode(birDayCode);
	},

	//校验18位的身份证号码
	check18IdCardNo : function(idCardNo) {
		//18位身份证号码的基本格式校验
		var check = /^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}(\d|x|X)$/
				.test(idCardNo);
		if (!check)
			return false;
		//校验地址码
		var addressCode = idCardNo.substring(0, 6);
		check = idCardNoUtil.checkAddressCode(addressCode);
		if (!check)
			return false;
		//校验日期码
		var birDayCode = idCardNo.substring(6, 14);
		check = idCardNoUtil.checkBirthDayCode(birDayCode);
		if (!check)
			return false;
		return true;
		//验证校检码   
		//return idCardNoUtil.checkParityBit(idCardNo);
	},

	formateDateCN : function(day) {
		var yyyy = day.substring(0, 4);
		var mm = day.substring(4, 6);
		var dd = day.substring(6);
		return yyyy + '-' + mm + '-' + dd;
	},

	//获取信息
	getIdCardInfo : function(idCardNo) {
		var idCardInfo = {
			gender : "", //性别
			birthday : "" // 出生日期(yyyy-mm-dd)
		};
		if (idCardNo.length == 15) {
			var aday = '19' + idCardNo.substring(6, 12);
			idCardInfo.birthday = idCardNoUtil.formateDateCN(aday);
			if (parseInt(idCardNo.charAt(14)) % 2 == 0) {
				idCardInfo.gender = idCardNoUtil.genders.female;
			} else {
				idCardInfo.gender = idCardNoUtil.genders.male;
			}
		} else if (idCardNo.length == 18) {
			var aday = idCardNo.substring(6, 14);
			idCardInfo.birthday = idCardNoUtil.formateDateCN(aday);
			if (parseInt(idCardNo.charAt(16)) % 2 == 0) {
				idCardInfo.gender = idCardNoUtil.genders.female;
			} else {
				idCardInfo.gender = idCardNoUtil.genders.male;
			}

		}
		return idCardInfo;
	},

	/*18位转15位*/
	getId15 : function(idCardNo) {
		if (idCardNo.length == 15) {
			return idCardNo;
		} else if (idCardNo.length == 18) {
			return idCardNo.substring(0, 6) + idCardNo.substring(8, 17);
		} else {
			return null;
		}
	},

	/*15位转18位*/
	getId18 : function(idCardNo) {
		if (idCardNo.length == 15) {
			var id17 = idCardNo.substring(0, 6) + '19' + idCardNo.substring(6);
			var parityBit = idCardNoUtil.getParityBit(id17);
			return id17 + parityBit;
		} else if (idCardNo.length == 18) {
			return idCardNo;
		} else {
			return null;
		}
	}
};