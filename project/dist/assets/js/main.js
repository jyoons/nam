"use strict";

var mainUi = function (mainUi, $window) {
  mainUi.init = function () {
    mainUi.slide.init();
  };

  mainUi.slide = {
    init: function init() {
      $('body').append('<div class="dimmed"></div>');
      $('.popup-slide').slick({
        arrows: false,
        initialSlide: 1 // infinite: false,
        // slidesToShow: 3,
        // slidesToScroll: 1

      });
    }
  };
  mainUi.init();
  return mainUi;
}(window.mainUi || {}, $(window));