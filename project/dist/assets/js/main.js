"use strict";

var mainUi = function (mainUi, $window) {
  mainUi.init = function () {
    mainUi.mainPop.init();
  };

  mainUi.mainPop = {
    init: function init() {
      this.setDefault('.main-popup');
      this.slide('.popup-slide');
      this.event();
    },
    setDefault: function setDefault(elem) {
      $('body').addClass('scrollOff');
      $('body').append('<div class="dimmed"></div>');
      $(elem).addClass('is-active');
    },
    slide: function slide(elem) {
      $(elem).slick({
        speed: 200,
        arrows: false,
        initialSlide: 0,
        dots: true,
        adaptiveHeight: true,
        infinite: false
      });
    },
    event: function event() {
      var _this = this;

      $('.main-popup .popup-close').on('click', function () {
        _this.close('.main-popup');
      });
    },
    close: function close(elem) {
      $('body').removeClass('scrollOff');
      $(elem).removeClass('is-active');
      $('.dimmed').remove();
    }
  };
  mainUi.init();
  return mainUi;
}(window.mainUi || {}, $(window));