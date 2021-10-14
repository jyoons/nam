

$(function(){
	
	$('.numbersOnly').keyup(function () { 
	    this.value = this.value.replace(/[^0-9,]/g,'');
	});
});

//ime-mode
$.fn.fnSetImeMode = function(v) {
	$(this).css("ime-mode", v);
}

$.fn.fnKeyOnlyNumber = function() {
	$(this).fnSetImeMode("disabled");
	$(this).on("input", function(evt) {
		$(this).val($(this).val().replace(/[^0-9]/g, ''));
		evt = evt || window.event;
		var code = (evt.which) ? evt.which : evt.keyCode;
		if ( code != 46 && (code < 48 || code > 57) ) {
			if (window.event) {
				window.event.returnValue = false;
			} else {
				evt.preventDefault();
			}
		}
	});
}

$.fn.fnKeyNotKorean = function(msg) {
	var regexp = /[^a-zA-Z0-9.\-\_\@]/g;
	$(this).fnSetImeMode("inactive");
	$(this).on("input", function() {
		if (regexp.test($(this).val())) {
			if (msg) alert(msg);
			$(this).val( $(this).val().replace(regexp, '') );
		}
	});	
}

$.fn.fnKeySetPwd = function(msg) {
	var regexp = /[^a-zA-Z0-9.\-\_\@\`\!\@\#\$\)\(\*\&\^\%\+\=\-\_\"\']/g;
	$(this).fnSetImeMode("inactive");
	$(this).on("input", function() {
		if (regexp.test($(this).val())) {
			if (msg) alert(msg);
			$(this).val( $(this).val().replace(regexp, '') );
		}
	});	
}

$.fn.fnKeyOnyNum2 = function(msg) {
	var regexp = /[^0-9.]/g;
	$(this).fnSetImeMode("inactive");
	$(this).on("input", function() {
		if (regexp.test($(this).val())) {
			if (msg) alert(msg);
			$(this).val( $(this).val().replace(regexp, '') );
		}
	});	
}

$.fn.fnKeyOnyDate = function(msg) {
	var regexp = /[^0-9\-]/g;
	$(this).fnSetImeMode("inactive");
	$(this).on("input", function() {
		if (regexp.test($(this).val())) {
			if (msg) alert(msg);
			$(this).val( $(this).val().replace(regexp, '') );
		}
	});	
}

$.fn.fnKeyOnlyOrdNo = function() {
	var regexp = /[^A-Z0-9]/g;
	$(this).fnSetImeMode("inactive");
	$(this).on("input", function() {
		if (regexp.test($(this).val())) {
			$(this).val( $(this).val().replace(regexp, '') );
		}
	});	
}

$.fn.fnSetMaxlength = function(mxlen) {
	mxlen = mxlen || 10;
	$(this).attr("maxlength", mxlen);
}

$.fn.fnSetAttr = function(p, v) {
	$(this).attr(p, v);
}

$.fn.fnKeyOnlyPercentNumber = function() {
	var regexp = /[^0-9.%]/g;
	$(this).fnSetImeMode("disabled");
	$(this).on("input", function(e) {
		if (regexp.test($(this).val())) {
			$(this).val( $(this).val().replace(regexp, '') );
		} else {
			var v = e.target.value.slice(0, e.target.value.length - 1);
			if (v.includes('%')) {
				e.target.value = '%';
				//console.log("111");
			} else if (v.length >= 3 && v.length <= 4 && !v.includes('.')) {
				if (v == '100' || v == '10000') {
					e.target.value = v + '.00%';
				} else {
					e.target.value = v.slice(0, 2) + '.' + v.slice(2, 3) + '%';
					e.target.setSelectionRange(4, 4);
				}
				//console.log("222");
			} else if (v.length >= 5 & v.length <= 6) {
				var whole = v.slice(0, 2);
				var fraction = v.slice(3, 5);
				e.target.value = whole + '.' + fraction + '%';
				//console.log("333");
			} else {
				e.target.value = v + '%';
				e.target.setSelectionRange(e.target.value.length - 1, e.target.value.length - 1);
				//console.log("444");
			}
			
			e.target.value = e.target.value.replace('..', '.');
		}
	});
}




var $commUtil = {

	decodeValue : function ( value ){
		var decoded = $('<div/>').html(value).text();
		return decoded;
	},
	
	fnConfirmDialog : function (msg){
	
		var result = confirm(msg);
	
		return new Promise(function(resolve, reject) {
	
			if( result ){
				resolve({value:true});
			}else{
				resolve({value:false});
			}
		});	
	},
	
	fnJsValidation : function (validItems) {
		var i = 0;
		for ( var key in validItems) {
			
			console.log( key + " : " + $(key).prop("type") );
			
			if ( $(key).val() != undefined && ($(key).val().length < 1 || $(key).val().trim() == "") ) {
				
				alert(validItems[key]);
				$(key).focus();
				
				return false;
			}
			i++;
		}
		return true;
	},
	
	
	
	fnBlockUiStart : function () {
		$.blockUI({ css: { 
	        border: 'none', 
	        padding: '15px', 
	        backgroundColor: '#000', 
	        '-webkit-border-radius': '10px', 
	        '-moz-border-radius': '10px', 
	        opacity: .8, 
	        color: '#fff' 
	    }});
	},
	
	fnBlockUiSop : function () {
		$.unblockUI();
	},
	
	fnSrchInit : function (){
		var url = window.location.pathname;
		document.location.href = url;
	},
	
	fnSrchFrm : function (){
		goPage(1);
	},
	
	fnChkAll : function (){
		var isChecked = $("#checkboxCon:checked").length > 0;
		if( isChecked ){
			$("[name=delNo]").each(function(){
				this.checked = true;
			});
		}else{
			$("[name=delNo]").each(function(){
				this.checked = false;
			});
		}
	},
	
	addslashes : function (str) {
		str = str.replace(/\\/g, '\\\\');
		str = str.replace(/\'/g, '\\\'');
		str = str.replace(/\"/g, '\\"');
		str = str.replace(/\0/g, '\\0');
		//str = str.replace(/\n/g, "");//행바꿈제거
		//str = str.replace(/\r/g, "<br>");//엔터제거
	
		return str;
	},
	 
	stripslashes : function (str) {
		str = str.replace(/\\'/g, '\'');
		str = str.replace(/\\"/g, '"');
		str = str.replace(/\\0/g, '\0');
		str = str.replace(/\\\\/g, '\\');
		return str;
	},
	
	fnInputLenChk : function (obj) {
		var iTxt = $(obj).val();
		var iMaxLen = $(obj).prop("maxlength");
		var i = j = cnt = 0;
		for (i = 0; i < iTxt.length; i++) {
			var val = escape(iTxt.charAt(i)).length;
			if (val == 6) {
				j++;
			}
			j++;
			if (j <= iMaxLen) {
				cnt++;
			}
			if (j > iMaxLen) {
				$(obj).val(iTxt.substr(0, cnt));
			}
		}
	},
	
	isContinuedValue : function (value) {
		var intCnt1 = 0;
	    var intCnt2 = 0;
	    var temp0 = "";
	    var temp1 = "";
	    var temp2 = "";
	    
	    for (var i = 0; i < value.length-2; i++) {
	    	temp0 = value.charAt(i);
	        temp1 = value.charAt(i + 1);
	        temp2 = value.charAt(i + 2);
	        
	        if (temp0.charCodeAt(0) - temp1.charCodeAt(0) == 1
	        		 && temp1.charCodeAt(0) - temp2.charCodeAt(0) == 1 ) {
	        	intCnt1++;
	        }
	        if (temp0.charCodeAt(0) - temp1.charCodeAt(0) == -1
	        		&& temp1.charCodeAt(0) - temp2.charCodeAt(0) == -1 ) {
	        	intCnt2++;
	        }
	    }
	    return (intCnt1 > 0 || intCnt2 > 0);
	},
	
	fnChkPwd : function (str, uid) {
		var minLen = 6;
		var maxLen = 20;
		var pass = str;
		var message = "";
		var chk = 0;
		
		if (pass.length < minLen || pass.length > maxLen) {
			message = "비밀번호는 " + minLen + "자 이상 " + maxLen + "자 이하로 입력해주세요.";
		}
		if (pass.search(/₩s/) != -1) {
			message = "비밀번호는 공백업이 입력해주세요.";
		}
	
		// 비밀번호 문자열에 숫자 존재 여부 검사
		var pattern1 = /[0-9]/;  // 숫자
		if(pattern1.test(pass) == false) {
			message = "비밀번호에 숫자가 입력되지 않았습니다.\n숫자를 입력해주세요.";
		}
	
		// 비밀번호 문자열에 영문 소문자 존재 여부 검사
		var pattern2 = /[a-z]/;
		if(pattern2.test(pass) == false) {
			message = "비밀번호에 영문 소문자가 입력되지 않았습니다.\n영문 소문자를 입력해주세요.";
		}
	
		// 비밀번호 문자열에 영문 대문자 존재 여부 검사
		var pattern3 = /[A-Z]/;
		if(pattern3.test(pass) == false) {
			message = "비밀번호에 영문 대문자가 입력되지 않았습니다.\n영문 대문자를 입력해주세요.";
		}
	
		// 비밀번호 문자열에 특수문자 존재 여부 검사
		var pattern4 = /[~!@#$%^&*()_+|<>?:{}]/;  // 특수문자
		if(pattern4.test(pass) == false) {
			message = "비밀번호에 특수문자가 입력되지 않았습니다.\n특수문자를 입력해주세요.";
		}
		
		if (uid) {
			if (pass.search(uid) > -1) {
				message = "ID가 포함된 비밀번호는 사용하실 수 없습니다.";
			}
		}
	
		// 비밀번호 문자열 결과 출력
		if(message) {
			alert(message);
			return false;
		} else {
			return true;
		}
	},
	
	fnSetMailType : function (tgt, v, isLogin) {
	    isLogin = (isLogin === undefined) ? false : isLogin;
		if (v == '1') {
			if (!isLogin) {
	            $("#" + tgt).prop('readonly', false);
	            $("#" + tgt).val('');
	        }
			$("#"+tgt).focus();
		} else {
			if (isLogin) {
	            var emails = $("#"+tgt).val().split("@");
	            $("#"+tgt).val(emails[0]);
			} else {
	            $("#"+tgt).prop('readonly', true);
	            $("#"+tgt).val(v);
			}
		}
	},
	
	fnCheckContent : function (objId) {
		var sHTML = "";
		if (objId) {
			sHTML = CKEDITOR.instances[objId].getData();
			if(sHTML == "" || sHTML == "<p><br></p>" || sHTML  == "<p></p>") {
				sHTML = "";
			}
		}
		return sHTML;
	},
	
	fnCheckBoolContent : function (objId) {
		
		if (objId) {
			sHTML = CKEDITOR.instances[objId].getData();
			
			var replaceData = sHTML.replace(/&nbsp\;/gi,'').replace(/\n/gi,'').replace(/<br \/>/gi,'');
			
			var stIdx = replaceData.search("<body>");
			var edIdx = replaceData.search("</body>");
			
			if( edIdx-stIdx > 6){
				return false;
			}else{
				return true;
			}
		}
		
		return true;
	},
	
	fnUpdateContent : function (objId) {
		if (objId) {
			CKEDITOR.instances[objId].updateElement();
		}
	},
	
	fnSEResetContent : function (objId) {
		if (objId) {
			CKEDITOR.instances[objId].setData();
		}
	},
	
	fnEditorSetContent : function (objId, cont) {
		if (objId) {
			fnSEResetContent(objId);
			setTimeout(function() {
				CKEDITOR.instances[objId].document.getBody().setHtml(cont);
			}, 200);
		}	
	},
	
	readURL : function (input, tgt) {
		var objSel = $("#preview_"+tgt+"");
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        reader.onload = function(e) {
	        	objSel.attr('src', e.target.result);
	        }
	        reader.readAsDataURL(input.files[0]);
	    }
	},
	
	fnListPage : function () {
		location.reload();
	},
	
	fnShowProfile : function () {
		document.location.href="/manage/profile.do";
	},
	
	fnShowMyLog : function (id) {
		$("#sid").val(id);
		$("#topFrm").attr("action","/manage/logList.do");
		$("#topFrm").submit();
	},
	
	fnChkFileType : function (input) {
		var exts = ['gif','png','jpg','jpeg','doc','docx','xls','xlsx','hwp','pdf','ppt','pptx','zip','mp4'];
		if (input.files && input.files[0]) {
			var ext = $(input).val().split('.').pop().toLowerCase();
			if($.inArray(ext, exts) == -1) {
				alert("등록 할수 없는 파일명입니다.\n" + exts.join(",") + " 파일만 가능합니다.");
				$(input).val("");
				return;
			}
			
			var fileSize = input.files[0].size;
			var maxSize = 50 * 1024 * 1024;//50MB
		 	 
		    if(fileSize > maxSize){
		    	alert("첨부파일 사이즈는 50MB 이내로 등록 가능합니다. ");
		    	$(input).val("");
		    	return;
		    }
		}
	},
	
	isUrlValid : function (url) {
	    return /^(https?|s?ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(url);
	},
	
	isEmailValid : function (email) {
		return /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/i.test(email);
	}

};