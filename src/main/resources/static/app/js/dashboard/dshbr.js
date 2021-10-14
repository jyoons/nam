var pos = 0;
$(function(){
		
		init();
	});
	
	var $list = $('#js-list'); // 리스트 영역
	
	/**
	 * 초기화
	 */
	function init() {
		sliderDefaultSetting();
		fnAddEventListener();
	}

	/**
	 * 이벤트 등록
	 */
	function fnAddEventListener() {
		fnSearch();
		
		$("#leftArrow").click(function(){
			let length = $('ul.slider').children().length;
			if(pos > 0){
				pos--;
				$("#slider").css("transform", "translateX(calc(100% / " + length + " * -" + pos + "))");
			}
		});
		
		$("#rightArrow").click(function(){
			let length = $('ul.slider').children().length;
			if(pos < length - 1){
				pos++;
				$("#slider").css("transform", "translateX(calc(100% / " + length + " * -" + pos + "))");
			}
		});
		
	}

	function sliderDefaultSetting(){
		let length = $('ul.slider').children().length;
		$('.slider').css('width', 'calc(100% * ' + length + ')');
		$('.slider li').css('width', 'calc(100% / ' + length + ')');
	}
	
	/**
	 * 상세화면 이동
	 * @param {event} e 이벤트
	 */
	function fnGoView(idx) {
		var $frm = $('#listFrm');
	  	$('#list-strNo').val(idx);
	  	$frm.attr('method', 'get');
	  	$frm.attr('action', 'view');
	  	$frm.submit();
	}

	/**
	 * 리스트 폼 세팅
	 */
	function setForm() {
	  var searchFrm = $('#listFrm');
	  searchFrm.find('#list-currentPageNo').val( 1 );	  
	  //$("#excelDownloadButton").attr('onclick', 'location.href="/oprt/dscntprice/excelDownload?' + $('#listFrm').serialize() + '"')
	}
	
	function setButtonForm(idx){
		var searchFrm = $('#listFrm');
		console.log($('input[name="is_actv"]').eq(idx).val());
		console.log($('input[name="is_actv"]').eq(idx).is(':checked'));
		console.log($('textarea').eq(idx).text());
	    searchFrm.find('#list-srchIdx').val( $('input[name="is_actv"]').eq(idx).val() );
	    $('input[name="is_actv"]').eq(idx).is(':checked') ? searchFrm.find('#list-isActv').val('Y') : searchFrm.find('#list-isActv').val('N');
	    searchFrm.find('#list-req').val( $('textarea').eq(idx).val() );

	}

	function fnSearch() {
		var $strVal = $('#f-search');
		var $strKey = $('#f-sch-board');
		
		/////////////////
		$.ajax({
			type: 'POST',
		    url: 'list-data',
		    data: $('#listFrm').serialize(),
		    dataType: 'html',
		    
		    success: function(data) {
		    	$list.html(data);
		    	ui.reInit($('#js-list'));
		    	
		    	//history.pushState(null, null, 'index' + "?" + $('#listFrm').serialize());
		    
		      $('.paging a').click(function(e) {
		        e.preventDefault();
		        $('#listFrm #list-currentPageNo').val(this.rel);
		        fnSearch();
		      });
		      
		      $("#prcenter-add").off("click").click(function(e){
		    	  e.preventDefault();	  
		    	  ref.go("/prcenter/{{path}}/add");
		      });

		      
		      $("#prcenter-list-del").off("click").click(function(e){
		    	  e.preventDefault();
		    	  
		    	  var delChks = [];
		    	  $("input[name='delCheckBox-list']:checked").each( function(i){
		    			delChks.push( $(this).attr('data-add')  );  
		    	  });
		    	  
		    	  if( delChks.length > 0 ){
		    		  //fnDeleteList(delChks);
		    	  }
		    	  
		      });
		      
		    },
		    error: function(request, status, error) {
		    	//console.log('code:' + request.status + '\n' + 'error:' + error);
		    },
		  });
		
	}
	
	
	
	
	
	
	
	
	
	