"use strict";

var mainUi = function (mainUi, $window) {
  mainUi.init = function () {
    mainUi.slide.init();
  };

  mainUi.slide = {
    init: function init() {
      $('body').addClass('scrollOff');
      $('body').append('<div class="dimmed"></div>');
      $('.popup-slide').slick({
        //         autoplay: true,
        //   autoplaySpeed: 1000,
        speed: 200,
        arrows: false,
        initialSlide: 0,
        dots: true,
        adaptiveHeight: true,
        infinite: false // slidesToShow: 3,
        //  slidesToScroll: 1

      });
    }
  };
  mainUi.init();
  return mainUi;
}(window.mainUi || {}, $(window));