"use strict";

var uiCommon = function (uiCommon, $window) {
  uiCommon.init = function () {
    uiCommon.lnb.init();
    uiCommon.select.init();
  };

  uiCommon.lnb = {
    init: function init() {
      uiCommon.lnb.setDefault();
    },
    setDefault: function setDefault() {
      var el = $('.lnb');
      var elPos = el.attr('data-lnb');
      var elString = elPos.split(',');
      var targetD1 = elString[0];
      var targetD2 = elString[1];
      var targetD3 = elString[2]; // $('.lnb-1depth.select-wrap .select-list-wrap, .lnb-2depth.select-wrap .select-list-wrap').css({'display':'block'});

      $('.lnb-1depth.select-wrap .select-list-wrap .select-list__items.' + targetD1).addClass('on');
      $('.lnb-2depth.select-wrap .select-list-wrap .select-list.' + targetD1).css({
        'display': 'block'
      });
      $('.lnb-2depth.select-wrap .select-list__items').eq(targetD2).addClass('on');
      $('.lnb-3depth.select-wrap .select-list__items').eq(targetD3).addClass('on'); // $('.lnb-3depth.select-wrap .select-list-wrap .select-list .select-list__items').eq(targetD2).addClass('on');
    }
  };
  uiCommon.select = {
    init: function init() {
      uiCommon.select.setDefault();
      uiCommon.select.event();
    },
    setDefault: function setDefault() {
      var el = $('.select-wrap');
      $('.select-wrap').each(function (i) {
        var title = $('.select-wrap').eq(i).find('.select-list__items.on').children('a').text();
        $('.select-wrap').eq(i).find('.select-title').text(title);
      });
    },
    event: function event() {
      var target = $('.select-title');
      target.on('click', function () {
        var _this = $(this);

        if (_this.hasClass('is-active')) {
          close();
        } else {
          open();
        }

        function open() {
          console.log('open');
          target.removeClass('is-active');
          target.next('.select-list-wrap').slideUp(200);

          _this.addClass('is-active');

          _this.next('.select-list-wrap').slideDown(200);
        }

        function close() {
          console.log('close');
          target.removeClass('is-active');
          target.next('.select-list-wrap').slideUp(200);
        }
      });
    }
  };
  uiCommon.init();
  return uiCommon;
}(window.uiCommon || {}, $(window));