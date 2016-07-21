/**
 * 通用方法
 */
Common = {
	/**
	 * 得到Iframe形式的Content
	 * 
	 * @param iframeId
	 * @param iframeUrl
	 * @returns {String}
	 */
	getIframeContent : function(iframeId, iframeUrl) {
		var content = "<iframe name='" + iframeId
				+ "' width=\"100%\" height=\"100%\" frameborder=\"0\" src=\""
				+ iframeUrl + "\" style=\"width:100%;height:100%;\"></iframe>";
		return content;
	},
	parseISO8601 : function(dateStringInRange) {
		var isoExp = /^\s*(\d{4})-(\d\d)-(\d\d)\s*$/,
			date = new Date(NaN), month,
			parts = isoExp.exec(dateStringInRange);
		if(parts) {
			month = +parts[2];
			date.setFullYear(parts[1], month - 1, parts[3]);
			if(month != date.getMonth() + 1) {
				date.setTime(NaN);
			}
		}
		return date;
	},
	openPostWindow : function(url, data, name) {
		var tempForm = document.createElement("form");
		tempForm.id = "tempForm1";
		tempForm.method = "post";
		tempForm.action = url;
		tempForm.target = name;

		var hideInput = document.createElement("input");
		hideInput.type = "hidden";
		hideInput.name = "content";
		hideInput.value = data;
		tempForm.appendChild(hideInput);
		$(tempForm).submit(function() {
			window.open('about:blank',name,'height=400, width=400, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');
		});
		document.body.appendChild(tempForm);
		tempForm.submit();
		document.body.removeChild(tempForm);
	},
	openPostWindow2 : function(url,data){
		var tempForm = document.createElement("form");
		tempForm.id = "tempForm1";
		tempForm.method = "post";
		tempForm.action = url;
		tempForm.target = '_blank';		//在新窗口打开页面
		if(!!data){
			for(var key in data){
				var hideInput = document.createElement("input");
				hideInput.type = "hidden";
				hideInput.name = key;
				hideInput.value = data[key];
				tempForm.appendChild(hideInput);
			}
		}
		$(tempForm).submit(function(){
			window.open('about:blank',name,'height=400, width=400, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');
		});
		document.body.appendChild(tempForm);
		tempForm.submit();
		document.body.removeChild(tempForm);
	},
	openPostWindow3 : function(url,target,data){
		var tempForm = document.createElement("form");
		tempForm.id = "tempForm1";
		tempForm.method = "post";
		tempForm.action = url;
		tempForm.target = target;		//在新窗口打开页面
		if(!!data){
			for(var key in data){
				var hideInput = document.createElement("input");
				hideInput.type = "hidden";
				hideInput.name = key;
				hideInput.value = data[key];
				tempForm.appendChild(hideInput);
			}
		}
		$(tempForm).submit(function(){
			window.open('about:blank',name,'height=400, width=400, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');
		});
		document.body.appendChild(tempForm);
		tempForm.submit();
		document.body.removeChild(tempForm);
	},
	formatDate : function(now) {
		var year = now.getFullYear();
		var month = now.getMonth() + 1;
		if (now.getMonth() < 9){
			month = "0" + month;
		}
		var date = now.getDate();
		if (now.getDate() < 10){
			date = "0" + date;
		}
		return year + "-" + month + "-" + date;
	},
	formatDateTime : function(now) {
		var year = now.getFullYear();
		if (now.getMonth() < 9){
			month = "0" + month;
		}
		var date = now.getDate();
		if (now.getDate() < 10){
			date = "0" + date;
		}
		var hour = now.getHours();
		var minute = now.getMinutes();
		var second = now.getSeconds();
		return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
	},
	getAges : function(str) {
		var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
		if (r == null)
			return false;
		var d = new Date(r[1], r[3] - 1, r[4]);
		if (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3]
				&& d.getDate() == r[4]) {
			var Y = new Date().getFullYear();
			return Y - r[1];
		}
		return ("输入的日期格式错误！");
	},
	/**
	 * 验证非空字段
	 * @param field
	 * @param value
	 */
	checkNotNull : function(field,value) {
		if ($.trim(value).length == 0){
			$.messager.alert("提示", field + "不能为空");
			return false;
		}
		return true;
	},
	/**
	 * 验证手机号码
	 * @param value
	 * @returns {Boolean}
	 */
	checkPhone : function(value){
		var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
		if(!myreg.test(value)){ 
			$.messager.alert("提示",'请输入有效的手机号码！'); 
		    return false; 
		}
		return true;
	},
	/**
	 * 身份证精确判断
	 * @param code
	 * @returns {Boolean}
	 */
	identityCodeValid : function(code) {
		var city = {
			11 : "北京",
			12 : "天津",
			13 : "河北",
			14 : "山西",
			15 : "内蒙古",
			21 : "辽宁",
			22 : "吉林",
			23 : "黑龙江 ",
			31 : "上海",
			32 : "江苏",
			33 : "浙江",
			34 : "安徽",
			35 : "福建",
			36 : "江西",
			37 : "山东",
			41 : "河南",
			42 : "湖北 ",
			43 : "湖南",
			44 : "广东",
			45 : "广西",
			46 : "海南",
			50 : "重庆",
			51 : "四川",
			52 : "贵州",
			53 : "云南",
			54 : "西藏 ",
			61 : "陕西",
			62 : "甘肃",
			63 : "青海",
			64 : "宁夏",
			65 : "新疆",
			71 : "台湾",
			81 : "香港",
			82 : "澳门",
			91 : "国外 "
		};
		var tip = "";
		var pass = true;

		if (!code || !/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(code)) {
			tip = "身份证号格式错误";
			pass = false;
		}

		else if (!city[code.substr(0, 2)]) {
			tip = "地址编码错误";
			pass = false;
		} else {
			// 18位身份证需要验证最后一位校验位
			if (code.length == 18) {
				code = code.split('');
				// ∑(ai×Wi)(mod 11)
				// 加权因子
				var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8,
						4, 2 ];
				// 校验位
				var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
				var sum = 0;
				var ai = 0;
				var wi = 0;
				for (var i = 0; i < 17; i++) {
					ai = code[i];
					wi = factor[i];
					sum += ai * wi;
				}
				var last = parity[sum % 11];
				if (parity[sum % 11] != code[17]) {
					tip = "校验位错误";
					pass = false;
				}
			}
		}
		if (!pass)
			$.messager.alert("提示", tip);
		return pass;
	},
	/**
	 * 根据身份证号码得到出生日期
	 * 
	 * @param val
	 */
	getBirthday : function(val) {
		var birthdayValue;
		if (15 == val.length) { // 15位身份证号码
			birthdayValue = val.charAt(6) + val.charAt(7);
			if (parseInt(birthdayValue) < 10) {
				birthdayValue = '20' + birthdayValue;
			} else {
				birthdayValue = '19' + birthdayValue;
			}
			birthdayValue = birthdayValue + '-' + val.charAt(8) + val.charAt(9)
					+ '-' + val.charAt(10) + val.charAt(11);
		}
		if (18 == val.length) { // 18位身份证号码
			birthdayValue = val.charAt(6) + val.charAt(7) + val.charAt(8)
					+ val.charAt(9) + '-' + val.charAt(10) + val.charAt(11)
					+ '-' + val.charAt(12) + val.charAt(13);
		}
		return birthdayValue;
	}
}
