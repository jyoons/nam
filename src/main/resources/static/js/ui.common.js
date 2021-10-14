if(typeof console=="undefined"){ console = { log : function(){}, info : function(){}, warn : function(){}, error : function(){} }; }
(function(global, ui){
  'use strict';

  var _win = { h : 0, w : 0, scrolltop : 0, scrolldir : "" };
  var _evt = {
    winclick : function(e){
      var target = e.target.nodeName==="A"||e.target.nodeName==="BUTTON" ? e.target : e.currentTarget, $target = $.$(target), $relele, uipop;
      uipop = target.getAttribute("data-uipop");
      document.focusEl = target;

      if(uipop){
        $relele = url2el(target, null);
        if(uipop==0 || uipop==1){
          if(!$relele) $relele = $target.closest('[data-uipopset]');
          if($relele.length==0) $relele = null;
          if(!$relele && !window.parent.length) self.close();
          if($relele && uipop==0) $relele.uipop("close");
          if($relele && uipop==1) $relele.uipop("open");
        }else if(uipop!="" && uipop!=undefined){
          var url = target.getAttribute("href") || target.getAttribute("data-url");
          var _data = $jq.extend([], $target.data("uipop"));
          _data.unshift(url);
          $target.uipop({winpop:_data});
        }
        e.preventDefault();
      }
    },
    winresize : function(e, first){
      _win.h = _page.$win.height();
      _win.w = _page.$win.width();
    },
    winscroll : function(e, first){
      var curscrolltop = _page.$win.scrollTop(), _dir;
      _dir = _win.scrolltop>curscrolltop ? "up" : "down";
      if(_win.scrolldir!=_dir) _page.$body.replaceClass("(up|down)",_dir);
      _win.scrolltop = curscrolltop;
      _win.scrolldir = _dir;
    },
    doc : function($wrap){
      $wrap.findFilter(".calendar").each(function(){
        var $this= $(this);
        if(!$this.attr("id")){
          var $buttonText = $this.find(">.hd").text();
          var $ipt = $this.find("input[type='text']");
          if($ipt.length>1){
            $ipt.eq(0).datepicker({
              dateFormat : "yy-mm-dd",
              showOn: "button",
              showOtherMonths : true,
              showMonthAfterYear : true,
              showButtonPanel: true,
              changeYear : true,
              changeMonth : true,
              maxDate : "+1y",
              dayNamesMin : ['일','월','화','수','목','금','토'],
              monthNamesShort : ['01','02','03','04','05','06','07','08','09','10','11','12'],
              buttonText : $buttonText+" 검색시작일 달력으로 선택<span></span>",
              onClose: function( selectedDate ) {
                $ipt.eq(1).datepicker( "option", "minDate", selectedDate );
                $(this).removeClass("blank");
              }
            });
            $ipt.eq(1).datepicker({
              dateFormat : "yy-mm-dd",
              showOn: "button",
              showOtherMonths : true,
              showMonthAfterYear : true,
              showButtonPanel: true,
              changeYear : true,
              changeMonth : true,
              maxDate : "+1y",
              dayNamesMin : ['일','월','화','수','목','금','토'],
              monthNamesShort : ['01','02','03','04','05','06','07','08','09','10','11','12'],
              buttonText : $buttonText+"검색종료일 달력으로 선택<span></span>",
              onClose: function( selectedDate ) {
                $ipt.eq(0).datepicker( "option", "maxDate", selectedDate );
                $(this).removeClass("blank");
              }
            });
          }else{
            $ipt.eq(0).datepicker({
              dateFormat : "yy-mm-dd",
              showOn: "button",
              showOtherMonths : true,
              showMonthAfterYear : true,
              showButtonPanel: true,
              changeYear : true,
              changeMonth : true,
              yearRange : "1900 : 2026",
              maxDate : "+1y",
              dayNamesMin : ['일','월','화','수','목','금','토'],
              monthNamesShort : ['01','02','03','04','05','06','07','08','09','10','11','12'],
              buttonText : $buttonText+" 달력으로 선택<span></span>"
            });
          }
        }
      });

      $wrap.findFilter(".cont-variable").each(function(){
        var _ = this, $this = $.$(_), max = this.getAttribute("data-max") || 3;
        _.temp = $this.children().eq(0).clone();
        if(!_.temp.length) return;
        $this.off("click.variable", "[data-btn]").on("click.variable", "[data-btn]", function(){
          var _$p = $.$(this).parent().parent();
          if(this.getAttribute("data-btn")=="add"){
            if($this.children().length>=max) return;
            _$p.after(_.temp.clone());
          }else{
            _$p.remove();
          }
        });
      });
    },
    init : function($wrap, isReInit){
      var _ = this;
      if(!$wrap) $wrap = _page.$body;
      _.doc($wrap); //form event
      if(!isReInit){
        _page.$body.off("click.linkHandler").off("click.linkHandler", "a, button, area").on("click.linkHandler", "a, button, area", _.winclick).off("focus.appFocus blur.appFocus", "input:not([type='checkbox']):not([type='radio']),select,textarea");
        _page.$win.off("resize.layoutsc orientationChange.layoutsc").on("resize.layoutsc orientationChange.layoutsc", _.winresize).trigger("resize.layoutsc", true).off("scroll.layoutsc").on("scroll.layoutsc", _.winscroll).trigger("scroll.layoutsc");
        setTimeout(function(){ _page.$win.trigger("resize.layoutsc", true).trigger("scroll.layoutsc", true); }, 500);

        _page.$body.off("change.fakefile", "input[type='file']").on("change.fakefile", "input[type='file']", function(){
          this.parentNode.setAttribute("data-filename", this.value);
        }).trigger('change.fakefile');
      }
    }
  };
  var _page = {
    $win : $(window), $html : null, $body : null,
    layout : {
      init : function(){
        $(".aside .navi .toggle").each(function(){
          $.$(this).parent().click(function(e){
            $.$(this).parent().toggleClass("active");
            e.preventDefault();
          });
        });
      }
    },
    reInit : function($wrap){
      var _ = _page, isReInit = true;
      if(!_.$body) return;
      if(!$wrap) $wrap = _.$body, isReInit = false;
      if(_win.h==0) _win.h = _page.$win.height(), _win.w = _page.$win.width();

      $wrap.findFilter('[data-tab]').tab();
      _evt.init($wrap, isReInit);
    },
    init : function(){
      var _ = this;
      _.layout.init();
      _.reInit();
    }
  };

  $(document).ready(function(){
    _page.$html= $("html");
    _page.$body = $("body");
    _page.init();
  });

  //public
  global.win = _win;
  ui.reInit = _page.reInit;

})(this, this.ui = this.ui || {});