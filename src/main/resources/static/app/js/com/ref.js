window.ref = {};

String.prototype.format = function() {
	 a = this;
	 for (k in arguments) {
	   a = a.replace("{" + k + "}", arguments[k])
	 }
	 return a
};

//window.onpopstate = function(event) {
//
//    var prevUrl = document.referrer;
//      //location.href= prevUrl;
//};


ref = (function (window, $) {
	'use strict';
	
	var param = {
			type : '',
			strkey : ''
	};
	var
		// 상수
		config = (function() {
			var constants = {
				'KEY_ENTER': 13 // 엔터키 코드
				
			};

			return {
				get: function(name) {
					return constants[name];
				}
			};
		})(),
		// 서버 사이드 메시지 프라퍼티를 저장하여 클라이언트 사이드에서 사용할 수 있도록 하기 위한 오브젝트로,
		// 필요한 메시지 프라퍼티를 messages 오브젝트에 수동 셋팅 필요함(scripts.ftl 파일 참조)
		messages = {}

	// 메시지 getter
	function setMessage(obj) {
		messages = obj;
	}

	// 메시지 setter
	function getMessage(code) {
		return messages[code];
	}

	// jQuery ajax 기본 옵션 설정
	// $.ajax() 사용 시 별도 지정 없을 경우 아래 옵션 사용
	function setGlobalAjaxOption() {
		$.ajaxSetup({
			method: 'POST',
			dataType: 'json',
			beforeSend: function() {
				//ui.loading.enable();
			},
			complete: function() {
				//ui.loading.disable();
			},
			error: function (jqXHR, textStatus, errorThrown) {
				//sui.loading.disable();
				alert(getMessage('처리중 오류'));
			}
		});
	}

	// url에서 해시 파라메터를 파싱하여 오브젝트로 반환
	function getParams() {
		var
			url = window.location.href,
			pos = url.indexOf('#!'),
			params = {},
			hashes = [],
			hash,
			i = 0,
			length = 0;
		if (pos < 0) {
			return params;
		}
		hashes = url.slice(pos + 3).split('&');
		length = hashes.length;
		for (; i < length; i++) {
			hash = hashes[i].split('=');
			params[hash[0]] = hash.length > 1 ? decodeURIComponent(hash[1]) : null;
		}
		return params;
	}

	// 인자로 전달받은 파라메터를 url 해시 파라메터에 추가
	// 같은 파라메터 명일 경우 기존 파라메터로 대체
	function setParams(params) {
		var
			key,
			currentParams = getParams();
		for (key in params) {
			if (!params.hasOwnProperty(key)) {
				continue;
			}
			currentParams[key] = params[key];
		}
		// url 해시 변경
		window.location.hash = '#!/' + $.param(currentParams).replace(/\+/g, '%20');
	}

	// 페이징 영역의 페이지 이동
	function goToPage(currentPageNo) {
		setParams({
			currentPageNo: currentPageNo
		});
		return false;
	}


	

	// 컨텍스트 경로(해시 포함)를 URL 인코딩 하여 반환
	function ref() {
		return encodeURIComponent(window.location.href.split(window.location.host)[1]);
	}

	// 리스트로 돌아가기
	function goToList(url) {
		// 리스트 페이지를 통한 접근이 아닌 직접 접근인 경우 리스트 초기 상태로 이동
		// (리스트를 통한 접근인 경우 'ref' 파라메터에 리스트 URL이 전달 됨)
		if (window.location.href.split('?').length <= 1 || window.location.href.indexOf('ref=') === -1) {
			window.location.href = url;
		} else {
			var
				i = 0,
				param = '',
				params = window.location.href.split('?')[1].split('&'),
				length = params.length;

			for (; i < length; i++) {
				if ('ref' === params[i].split('=')[0]) {
					window.location.href =  decodeURIComponent(params[i].split('=')[1]);
					break;
				}
			}
		}
	}
	
	// daum 우편번호
	function fnPostCode(){
		new daum.Postcode({
			oncomplete: function(data) {
				var fullAddr = data.roadAddress;
				var extraAddr = ''; // 조합형 주소 변수
				if (data.bname !== '') {
					extraAddr += data.bname;
				}
				// 건물명이 있을 경우 추가한다.
				if (data.buildingName !== '') {
					extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
				}
				// 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
				fullAddr += (extraAddr !== '' ? ' (' + extraAddr + ')' : '');

				// 우편번호와 주소 정보를 해당 필드에 넣는다.                
				$("#f-ipt-3").val(data.zonecode);
				$("#f-ipt-3-2").val(fullAddr);
				$("#f-ipt-3-3").focus();

			}
		}).open();
	}
	
	//3자리 단위마다 콤마 생성
	function fnAddCommas(x) {
	    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}
	 
	//모든 콤마 제거
	function fnRemoveCommas(x) {
	    if(!x || x.length == 0) return "";
	    else return x.split(",").join("");
	}
	
	
	
	function fnPCertification(){
		if(forena.param.type == 'ipin'){
			window.open('', 'popupIPIN2', 'width=450, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
			document.form_ipin.target = "popupIPIN2";
			document.form_ipin.action = "https://cert.vno.co.kr/ipin.cb";
			document.form_ipin.submit();
		}else{
			window.open('', 'popupChk', 'width=500, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
			document.form_chk.action = "https://nice.checkplus.co.kr/CheckPlusSafeModel/checkplus.cb";
			document.form_chk.target = "popupChk";
			document.form_chk.submit();
		}
		
	}
	
	function fnGoStep(v){
		
		if(v == 'join'){
			fnGoStep3th();
		}else if( v == 'pay'){
			opener.fnGo(v);
			window.close();
		}else if( v == 'auth'){
			opener.fnAuth(v);
			window.close();
		}else{ 
		//  id / pw find
			opener.fnFind(v);
			window.close();
		}
	}
	
	function go(url){
		location.href=url;
	}
	
	function validateEmail(email) {
  		//const regExp = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  		const regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;

  		return regExp.test(email);
	}

	// init
	(function() {
		setGlobalAjaxOption();
		$('#btnTopLogin').click(function(){
			location.href = '/user/login';
		});
		
		$('#btnTopLogOut').click(function(){
			location.href = '/user/logout';
		});
		
		$('#btnTerms').click(function(e){
			e.preventDefault();
			$("#emailTab").removeClass("active");
			$("#termsTab").addClass("active");
			$("#email-security").uipop("open");
		});
		
		$('#btnEmail').click(function(e){
			e.preventDefault();
			$("#emailTab").addClass("active");
			$("#termsTab").removeClass("active");
			$("#email-security").uipop("open");
		});
		
	})();

	return {
		config: config,
		param: param,
		setMessage: setMessage,
		getMessage: getMessage,
//		getParams: getParams,
//		setParams: setParams,
		goToPage: goToPage,
		ref: ref,
		goToList: goToList,
		fnPostCode: fnPostCode,
		fnAddCommas: fnAddCommas,
		fnPCertification : fnPCertification,
		fnGoStep : fnGoStep,
		fnRemoveCommas:fnRemoveCommas,
		go:go,
		validateEmail:validateEmail
		
	};
})(window, jQuery);