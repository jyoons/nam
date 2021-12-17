"use strict";

var uiCommon = function (uiCommon, $window) {
  uiCommon.init = function () {
    uiCommon.gnb.init();
    uiCommon.lnb.init();
    uiCommon.select.init();
    uiCommon.titImg.init();
  };

  uiCommon.titImg = {
    init: function init() {
      var h1 = $('.header').outerHeight();
      var h2 = $('.container-header').outerHeight();
      var h3 = Number($('.container-header').css('padding-bottom').replace(/[^-\d\.]/g, ''));
      $('.container-header-fixImg').css('top', h1 + h2 - h3 - 60);
    }
  };
  uiCommon.gnb = {
    init: function init() {
      uiCommon.gnb.setDefault('.gnb');
      uiCommon.gnb.event('.gnb', '.gnb-btn', '.gnb-close');
    },
    setDefault: function setDefault(elem) {
      var el = $(elem);
      var elPos = el.attr('data-gnb');
      var elString = elPos.split(',');
      $('.gnb-1depth__items').removeClass('on');
      $('.gnb-1depth__items.' + elString[0]).addClass('on');
      $('.gnb-1depth__items.' + elString[0]).find('.gnb-2depth__items').eq(elString[1]).addClass('on');
    },
    event: function event(elem, target1, target2) {
      var tl1 = gsap.timeline();
      var tl2 = gsap.timeline();
      $(target1).on('click', function () {
        uiCommon.gnb.open('.gnb');
      });
      $(target2).on('click', function () {
        uiCommon.gnb.close();
      });
      uiCommon.gnb.itemEvent('.gnb-1depth__items');
    },
    close: function close() {
      // $('.dimmed').remove();
      gsap.to('.pc_gnbImage', {
        duration: 0.4,
        left: '50%',
        ease: "power1"
      });
      gsap.to('.gnb', {
        duration: 0.6,
        right: '-100%',
        ease: "power2",
        delay: 0.3
      });
      $('.logo').removeClass('active-menu');
    },
    open: function open(elem) {
      // $('body').append("<div class='dimmed' onclick='uiCommon.gnb.close()'></div>");
      gsap.to(elem, {
        duration: 0.4,
        right: 0,
        ease: "power2"
      });
      gsap.to('.pc_gnbImage', {
        duration: 0.6,
        left: 0,
        ease: "power2",
        delay: 0.3
      });
      $('.logo').addClass('active-menu');
      gsap.to('.logo', {
        duration: 0.4,
        opacity: 1,
        ease: "power2",
        delay: 0.2
      });
    },
    itemEvent: function itemEvent(target) {
      $(target + '>a').on('click', function () {
        var _this = $(this);

        $(target).removeClass('on');

        _this.parents('.gnb-1depth__items').addClass('on');
      });
    }
  };
  uiCommon.lnb = {
    init: function init() {
      uiCommon.lnb.setDefault('.lnb');
    },
    setDefault: function setDefault(elem) {
      var el = $(elem);

      if (el.length > 0) {
        var elPos = el.attr('data-lnb');
        var elString = elPos.split(',');
        $('.lnb-1depth .select-list__items, .lnb-2depth .select-list__items').removeClass('on');
        $('.lnb-1depth .select-list-wrap .select-list__items.' + elString[0]).addClass('on');
        $('.lnb-2depth .select-list-wrap .select-list.' + elString[0]).css({
          'display': 'block'
        });
        $('.lnb-2depth .select-list__items').eq(elString[1]).addClass('on');

        if (elString[0] == 'business') {
          $('.lnb-3depth .select-list').eq(elString[2]).css({
            'display': 'block'
          });
          $('.lnb-3depth .select-list__items').eq(elString[2]).addClass('on');
        }
      }
    }
  };
  uiCommon.select = {
    init: function init() {
      uiCommon.select.setDefault('.select-wrap');
      uiCommon.select.event('.select-title', '.select-list__items');
    },
    setDefault: function setDefault(elem) {
      var el = $(elem);
      el.each(function (i) {
        var title = el.eq(i).find('.select-list__items.on').children('a').text();
        el.eq(i).find('.select-title').text(title);
      });
    },
    event: function event(elem1, elem2) {
      var target = $(elem1);
      var elem = $(elem2);
      target.on('click', function () {
        var _this = $(this);

        if (_this.hasClass('is-active')) {
          close();
        } else {
          open();
        }

        function open() {
          target.removeClass('is-active');
          target.next('.select-list-wrap').slideUp(200);

          _this.addClass('is-active');

          _this.next('.select-list-wrap').slideDown(200);
        }

        function close() {
          target.removeClass('is-active');
          target.next('.select-list-wrap').slideUp(200);
        }
      });
      elem.children('a').on('click', function () {
        var _this = $(this);

        _this.parents('.select-list-wrap').slideUp(200);

        _this.parents('.select-list-wrap').find(elem2).removeClass('on');

        _this.closest(elem2).addClass('on');

        _this.parents('.select-wrap').find('.select-title').removeClass('is-active').text($(this).text());
      });
    }
  };
  uiCommon.init();
  return uiCommon;
}(window.uiCommon || {}, $(window));