var $noticeReg = {
	
	validItems : {'#frmTitle':'제목을 입력하세요'},
	
	setViewData : function(){
		$("#f-sch-sel option:contains(" + $("#category").val() + ")").prop("selected", true);
	},
	
	/**
	 * 초기화
	 */
	init : function() {
		$('#openDt').datepicker('setDate', 'now');
		this.fnAddEventListener();
	},

	fnAddEventListener : function () {

		$('#btnCancel').click(function(){
			$noticeReg.fnGoIndex();
		});
		
		$('#btnReg').click(function(e) {
			if ( $("#btnReg").text().trim() == '수정' ) {
				$noticeReg.updData();	
			} else {
				$noticeReg.regData();
			}
		});
 	},
	

	/**
	 * 파일 업로드 및 저장
	 */
	regData : function() {
		let targetUrl;
		if( $commUtil.fnJsValidation( $noticeReg.validItems) ){
			
			$commUtil.fnUpdateContent("frmCntnt");
			
			if( !$("#frmTitle").val() ) {
				alert("제목을 입력하세요.");
				$("#frmTitle").focus();
				return false;
			}
			
			$("#frmTitle").val($("#frmTitle").val().trim())
			
			if( $("#frmTitle").val().length > 200 ) {
				alert("제목 길이가 200자를 초과 하였습니다.");
				$("#frmTitle").val('');
				$("#frmTitle").focus();
				return false;
			}
			
			if( $commUtil.fnCheckBoolContent("frmCntnt") ) {
				alert("내용을 입력하세요.");
				CKEDITOR.instances["frmCntnt"].focus();
				return false;
			}
			
			if ( $("#frmFile").val() != null && $("#frmFile").val() != '' ) {
				var fileReg = /(.*?)\.(gif|png|jpg|jpeg|doc|docx|xls|xlsx|hwp|pdf|ppt|pptx|zip|mp4)$/;
			  	if(!$("#frmFile").val().toLowerCase().match(fileReg)) {
					alert("지원하는 파일 확장자가 아닙니다.");
					
					if ( /(MSIE|Trident)/.test(navigator.userAgent) ) {
						$("#frmFile").replaceWith( $("#frmFile").clone(true) );
						$("#frmFile").parent().attr("data-filename", "")
					} else {
					    $("#frmFile").val("");
					    $("#frmFile").parent().attr("data-filename", "")
					}
					return;
				}
			}
			
			if($("#replyIdx").val() != null && $("#replyIdx").val() != '') {
				targetUrl = 'addReply.act';
			} else {
				targetUrl = 'add.act';
			}

			$commUtil.fnConfirmDialog('등록 하시겠습니까?').then(function(response){
				if(response.value){
					$('#frm').ajaxForm({
					    url: targetUrl,
					    enctype: 'multipart/form-data',
					    beforeSubmit: function(data,form,option){
					    	
					    },
					    error : function(data){
					    	console.log(data);
					    },
					    success: function(data) {
					    	if (data.success) {
						    	location.reload();
						    	alert("저장되었습니다.");
						    	$noticeReg.fnGoIndex();
					    	} else {
					    		alert(data.message);
					    	}
					    }
					});
					$('#frm').submit();
				}
			});
		}
	},
	
	
	updData : function () {
		if( $commUtil.fnJsValidation($noticeReg.validItems) ){
			
			$commUtil.fnUpdateContent("frmCntnt");
			
			if( !$("#frmTitle").val() ) {
				alert("제목을 입력하세요.");
				$("#frmTitle").focus();
				return false;
			}
			
			$("#frmTitle").val($("#frmTitle").val().trim())
			
			if( $("#frmTitle").val().length > 200 ) {
				alert("제목 길이가 200자를 초과 하였습니다.");
				$("#frmTitle").val('');
				$("#frmTitle").focus();
				return false;
			}
			
			if( $commUtil.fnCheckBoolContent("frmCntnt") ) {
				alert("내용을 입력하세요.");
				CKEDITOR.instances["frmCntnt"].focus();
				return false;
			}
			
			if ( $("#frmFile").val() != null && $("#frmFile").val() != '' ) {
				var fileReg = /(.*?)\.(gif|png|jpg|jpeg|doc|docx|xls|xlsx|hwp|pdf|ppt|pptx|zip|mp4)$/;
			  	if(!$("#frmFile").val().toLowerCase().match(fileReg)) {
					alert("지원하는 파일 확장자가 아닙니다.");
					
					if ( /(MSIE|Trident)/.test(navigator.userAgent) ) {
						$("#frmFile").replaceWith( $("#frmFile").clone(true) );
						$("#frmFile").parent().attr("data-filename", "")
					} else {
					    $("#frmFile").val("");
					    $("#frmFile").parent().attr("data-filename", "")
					}
					return;
				}
			}

			$commUtil.fnConfirmDialog('수정 하시겠습니까?').then(function(response){
				if(response.value){
					$('#frm').ajaxForm({
					    url: "upd.act",
					    enctype: 'multipart/form-data',
					    beforeSubmit: function(data,form,option){
					    	
					    },
					    error : function(data){
					    	console.log(data);
					    },
					    success: function(data) {
					    	if (data.success) {
						    	location.reload();
						    	alert("저장되었습니다.");
						    	$noticeReg.fnGoIndex();
					    	} else {
					    		alert(data.message);
					    	}
					    }
					});
					$('#frm').submit();
				}
			});
		}
	},
	
	
	fnGoIndex : function (){
		ref.go("/community/board");
	}
	
};

$(function(){
		CKEDITOR.replace( 'frmCntnt' , {
			width:'100%',
			height:'200px',
			filebrowserImageUploadUrl: '/file/editorImageUpload.do?a=1'
		});
		CKEDITOR.on('dialogDefinition', function( ev ){
			var dialogName = ev.data.name;
			var dialogDefinition = ev.data.definition;
		
			switch (dialogName) {
				case 'image': //Image Properties dialog
					//dialogDefinition.removeContents('info');
					dialogDefinition.removeContents('Link');
					dialogDefinition.removeContents('advanced');
					break;
			}
		});
		
		if( $("#frmTitle").val() != null && $("#frmTitle").val() !='' ){
			$noticeReg.setViewData();	
		}
		
		$noticeReg.init();
	});

