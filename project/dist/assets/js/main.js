"use strict";

$(document).ready(function () {
  $('.wrap.main').animate({
    'opacity': 1
  }, 500);
  mainUi.init();
});
$(window).on('beforeunload', function () {
  $(window).scrollTop(0);
});

var mainUi = function (mainUi, $window) {
  mainUi.init = function () {
    $('.main-popup').length > 0 && mainUi.mainPop.init();
    $('.wrap.main').length > 0 && mainUi.headerCtr.init();
    $('.wrap.main').length > 0 && mainUi.mainVisual.init();
    $('.wrap.main').length > 0 && mainUi.scrollEv.init();
    $('.wrap.main').length > 0 && mainUi.boardCtr.init();
    $('.wrap.main').length > 0 && mainUi.mobile.init();
  };

  mainUi.headerCtr = {
    init: function init() {
      this.headerScr();
    },
    headerScr: function headerScr() {
      $window.on('scroll', function () {
        var scTop = $(this).scrollTop();
        $('.header').removeClass('is-scroll');

        if (scTop > $('.main-visual').height()) {
          $('.header').addClass('is-scroll');
        } else {
          $('.header').removeClass('is-scroll');
        }
      });
    }
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
  mainUi.mainVisual = {
    init: function init() {
      this.mainSlider(); // this.quickMenu();
    },
    mainSlider: function mainSlider() {
      var barAni = 18000,
          $el_length = $('.main-slider li').length,
          $bar = $('.progress-bar .bar'),
          timeW = $('.progress-bar').width();
      var index = $('.page-num__current').text();
      $('.main-slider').on({
        init: function init(event, slick) {
          $(this).siblings('.slider-util').find('.bar').css({
            'animation-duration': barAni / 1000 + 0.3 + 's'
          });
          $('.page-num__total').text($el_length);
          $bar.stop().animate({
            width: timeW
          }, barAni, "linear", function () {
            $(this).css({
              'width': '0'
            });
          });
        },
        beforeChange: function beforeChange(slick, currentSlide, nextSlide) {
          $bar.removeClass('bar-ani');
          $bar.stop().width(0);
        },
        afterChange: function afterChange(event, slick, currentSlide) {
          $('.page-num__current').text(currentSlide + 1);
          $bar.addClass('bar-ani');
          $('.video' + index).trigger('play');
          $bar.stop().width(0);
          $bar.stop().animate({
            width: timeW
          }, barAni, "linear", function () {
            $(this).css({
              'width': '0'
            });
          });
        }
      });
      $('.main-slider').not('.slick-initialized').slick({
        dots: false,
        prevArrow: $('.util-prev'),
        nextArrow: $('.util-next'),
        pauseOnHover: false,
        infinite: true,
        speed: 1000,
        fade: true,
        slidesToShow: 1,
        slidesToScroll: 1,
        autoplay: true,
        autoplaySpeed: barAni
      });
      $('.util-pause').on('click', function () {
        var index = $('.page-num__current').text(),
            w = $('.progress-bar .bar').width();
        $('.main-slider').slick('slickPause');
        $(this).removeClass('on').siblings('.util-play').addClass('on');
        $bar.width(w).stop();
        $('.video' + index).trigger('pause');
      });
      $('.util-play').on('click', function () {
        var index = $('.page-num__current').text(),
            w = $('.progress-bar .bar').width();
        $('.main-slider').slick('slickPlay');
        $(this).removeClass('on').siblings('.util-pause').addClass('on');
        $('.video' + index).trigger('play');
        $bar.stop().animate({
          width: timeW
        }, barAni, "linear", function () {
          $('.video' + index).trigger('pause');
          $('.video' + index).get(0).currentTime = 0;
          $(this).css({
            'width': '0'
          });
        });
      });
      $('.slick-arrow').on('click', function () {
        $('.util-play').removeClass('on').siblings('.util-pause').addClass('on');
        $('.video' + index).trigger('play');
      });
    }
  };
  mainUi.scrollEv = {
    init: function init() {
      this.scrollInit();
      this.scrollEv();
      this.sectionAni();
    },
    scrollInit: function scrollInit() {
      var sumHeight = 0,
          epilHeight = $('.main-epilogue').height(),
          h = 0,
          fakeHeight = 0;

      if ($window.width() < 767) {
        fakeHeight = 0;
      } else {
        fakeHeight = 6000;
      }

      if ($window.width() < 1025) {
        h = 800; // h = 300;
      } else {
        h = 1800; // h = 700;
      }

      setTimeout(function () {
        if ($window.width() > 767) {
          $('.quick-menu').animate({
            scrollTop: 800
          }, 1400);
        } else {
          $('.quick-menu').animate({
            'height': '10rem'
          }, 700);
        }
      }, 300);
      $('.main .section').each(function (index, item) {
        var posi = $(item).index() * $(window).height(),
            sec_h = $(window).height();

        if (index == 3) {
          posi = posi + fakeHeight;
          sumHeight += $(item).height();
        } else {
          sumHeight += sec_h;
        }

        $(item).css({
          'top': posi
        });
      });
      $('#wrap.main .container').css({
        'height': sumHeight + epilHeight + fakeHeight + h + 'px'
      });
    },
    scrollEv: function scrollEv() {
      var $mainVi = $('.main-visual'),
          $sec01 = $('.section-01'),
          $sec02 = $('.section-02'),
          $sec03 = $('.section-03'),
          $navi = $('.main-indi li'),
          epilHeight = $('.main-epilogue').height(),
          _top01 = $sec01.offset().top,
          _top02 = $sec02.offset().top,
          _top03 = $sec03.offset().top,
          timer;
      $navi.find('a').on('click', function () {
        var index = $(this).parent().index(),
            posi;
        $('.main-indi').removeClass('color-change');
        $navi.removeClass('on').eq(index).addClass('on');
        $sec02.find('.section-02-text').removeClass('active').css({
          'top': '80%',
          'opacity': 0,
          'z-index': -1
        });

        switch (index) {
          case 0:
            posi = 0;
            break;

          case 1:
            posi = _top01;
            $('.main-indi').addClass('color-change');
            break;

          case 2:
            posi = _top02;
            break;

          case 3:
            posi = _top03;
            $('.main-indi').addClass('color-change');
            break;
        }

        ;
        $(window).scrollTop(posi);
      });
      $window.on('scroll', function (e) {
        var scTop = $window.scrollTop(),
            c = 0,
            d = -50,
            // d = -70,
        sec01_value = scTop * (d - c) / _top01 + c,
            fakeHeight = 0,
            headerH = Number($('.container').css('padding-top').slice(0, -2)),
            h = 0;

        if ($window.width() < 767) {
          fakeHeight = 0;
        } else {
          fakeHeight = 6000;
        }

        if ($window.width() < 1025) {
          h = 800;
        } else {
          h = 1800;
        }

        if (!timer) {
          timer = setTimeout(function () {
            timer = null;
            var newHeight = $('.height-fix').outerHeight();
            $mainVi.css({
              'height': newHeight
            });
            $sec01.css({
              'height': newHeight
            });
            $sec02.css({
              'height': newHeight
            });
            $('#wrap.main .container').css({
              'height': newHeight * 3 + $sec03.height() + epilHeight + fakeHeight + h + 'px'
            }); // - Number($('.container').css('padding-top').slice(0,-2))
            // sec01_value > -50 && $mainVi.css('top', sec01_value + "%");

            $mainVi.css('top', sec01_value + "%"); //section01 ?????????

            if (scTop > _top01 && scTop < _top02) {
              scTop = scTop - $(window).height();
              var sec02_value = scTop * (d - c) / newHeight + c;
              $mainVi.css({
                'opacity': '0',
                'position': 'absolute'
              });
              $sec01.css({
                'position': 'fixed',
                'top': sec02_value + "%"
              });
            } else {
              $mainVi.css({
                'opacity': '1',
                'position': 'fixed'
              });
              $sec01.css({
                'position': 'absolute',
                // 'top' : _top01,
                'top': newHeight
              });
            } //section02 ?????????


            if (scTop > _top02 && scTop < _top03) {
              $sec02.css({
                'position': 'fixed',
                'top': 0
              });
              $mainVi.css({
                'opacity': '0',
                'position': 'absolute'
              });
              scTop = scTop - $(window).height() * 2 - fakeHeight;
              var sec03_value = scTop * (d - c) / newHeight + c;

              if ($(window).scrollTop() > _top02 + fakeHeight) {
                $sec02.css({
                  'top': sec03_value + "%"
                });
              }
            } else {
              $sec02.css({
                'position': 'absolute',
                // 'top' : _top02,
                'top': newHeight * 2
              });
            } //section03 ?????????


            if (scTop > _top03) {
              scTop = scTop - (newHeight * 3 + fakeHeight);
              var sec04_value = scTop * (d - c) / newHeight + c; // let sec04_value = scTop * ( d - c ) / (_top03 - fakeHeight - $sec03.height()  - epilHeight + headerH/2)  + c;
              // let sec04_value = scTop * ( d - c ) / ($sec03.height() - (newHeight- epilHeight)) + c; // -50%
              // let sec04_value = scTop * ( d - c ) / ($sec03.height() - (newHeight +  epilHeight)) + c;
              // console.log($sec03.height());
              // console.log(scTop);
              // console.log(sec04_value);

              $mainVi.css({
                'opacity': '0',
                'position': 'absolute'
              });
              $sec03.css({
                'position': 'fixed',
                'top': sec04_value + "%"
              });
              $('.main-epilogue').addClass('show');
            } else {
              $sec03.css({
                'position': 'absolute',
                // 'top' : _top03,
                'top': newHeight * 3 + fakeHeight
              });
              $('.main-epilogue').removeClass('show');
            }
          }, 0);
        }
      });
    },
    sectionAni: function sectionAni() {
      var $section = $('.main .section'),
          $sec01 = $('.section-01'),
          $sec02 = $('.section-02'),
          $sec03 = $('.section-03'),
          $navi = $('.main-indi li'),
          $secInfo = $('.section-02 .section-02-text'),
          $sec02_title = $('.section-02-title'),
          _top01 = $sec01.offset().top - 400,
          _top02 = $sec02.offset().top - 400,
          _top03 = $sec03.offset().top - 400,
          _sec02Posi01 = $sec02.offset().top + 1500,
          _sec02Posi02 = $sec02.offset().top + 3000,
          _sec02Posi03 = $sec02.offset().top + 4500,
          _count01 = new countUp.CountUp('count01', 5000),
          _count02 = new countUp.CountUp('count02', 5000),
          _count01Flag = false,
          _count02Flag = false,
          timer;

      $window.on('scroll', function () {
        var scTop = $window.scrollTop();

        if (!timer) {
          timer = setTimeout(function () {
            timer = null;

            if (scTop < _top01) {
              $('.main-indi').removeClass('color-change');
              $navi.removeClass('on').eq(0).addClass('on');
              $section.removeClass('active');
            } else if (scTop < _top02) {
              $('.main-indi').addClass('color-change');
              $navi.removeClass('on').eq(1).addClass('on');
              $secInfo.removeClass('active');
              $section.removeClass('active').eq(1).addClass('active');
              $sec02_title.removeClass('on');
              $secInfo.removeClass('active').eq(0).removeClass('active').stop().animate({
                'top': '-80%',
                'opacity': '0',
                'z-index': '-1'
              }, 200);
            } else if (scTop < _top03 && $window.width() > 767) {
              $('.main-indi').removeClass('color-change');
              $navi.removeClass('on').eq(2).addClass('on');
              $section.removeClass('active').eq(2).addClass('active');
              $sec02_title.removeClass('on').eq(0).addClass('on');

              if (scTop < _sec02Posi01) {
                //3614 ??????
                $secInfo.removeClass('active').eq(0).addClass('active').css('top', '-30%').stop().animate({
                  'top': '0',
                  'opacity': '1',
                  'z-index': '0'
                }, 200);
                $secInfo.eq(1).stop().animate({
                  'top': '80%',
                  'opacity': '0',
                  'z-index': '-1'
                }, 200);
                setTimeout(function () {
                  _count01Flag = false;

                  _count01.reset();
                }, 200);
              } else if (scTop < _sec02Posi02) {
                $secInfo.removeClass('active').eq(0).removeClass('active').stop().animate({
                  'top': '-80%',
                  'opacity': '0',
                  'z-index': '-1'
                }, 200);
                $secInfo.removeClass('active').eq(1).addClass('active').css('top', '30%').stop().animate({
                  'top': '0',
                  'opacity': '1',
                  'z-index': '0'
                }, 200);
                $secInfo.eq(2).stop().animate({
                  'top': '80%',
                  'opacity': '0',
                  'z-index': '-1'
                }, 200);

                if ($secInfo.eq(1).hasClass('active')) {
                  setTimeout(function () {
                    if (!_count01Flag) {
                      _count01.start();

                      _count01Flag = true;
                    }

                    _count02Flag = false;

                    _count02.reset();
                  }, 400);
                }
              } else if (scTop < _sec02Posi03) {
                $secInfo.removeClass('active').eq(1).removeClass('active').stop().animate({
                  'top': '-80%',
                  'opacity': '0',
                  'z-index': '-1'
                }, 200);
                $secInfo.removeClass('active').eq(2).addClass('active').css('top', '-30%').stop().animate({
                  'top': '0',
                  'opacity': '1',
                  'z-index': '0'
                }, 200);
                $secInfo.eq(3).stop().animate({
                  'top': '80%',
                  'opacity': '0',
                  'z-index': '-1'
                }, 200);

                if ($secInfo.eq(2).hasClass('active')) {
                  setTimeout(function () {
                    if (!_count02Flag) {
                      _count02.start();

                      _count02Flag = true;
                    }

                    _count01Flag = false;

                    _count01.reset();
                  }, 400);
                }
              } else if (_sec02Posi03 < scTop) {
                $sec02_title.removeClass('on').eq(1).addClass('on');
                setTimeout(function () {
                  _count02Flag = false;

                  _count02.reset();
                }, 200);
                $secInfo.removeClass('active').eq(0).removeClass('active').css({
                  'top': '-80%',
                  'opacity': '0',
                  'z-index': '-1'
                });
                $secInfo.removeClass('active').eq(1).removeClass('active').css({
                  'top': '-80%',
                  'opacity': '0',
                  'z-index': '-1'
                });
                $secInfo.removeClass('active').eq(2).removeClass('active').stop().animate({
                  'top': '-80%',
                  'opacity': '0',
                  'z-index': '-1'
                }, 200);

                if (!$secInfo.eq(3).hasClass('active')) {
                  $secInfo.removeClass('active').eq(3).addClass('active').css('top', '80%').stop().animate({
                    'top': '0',
                    'opacity': '1',
                    'z-index': '0'
                  }, 200);
                }
              }
            } else if (scTop < _top03 && $window.width() < 767) {
              // mobile section02
              $sec02_title = $('.mo-view .section-02-title'), $secInfo = $('.mo-view .section-02-text');
              _count01Flag = false, _count02Flag = false;
              $sec02_title.removeClass('on').eq(0).addClass('on');
              $secInfo.each(function () {
                if ($(this).hasClass('slick-current') === true) {
                  $(this).addClass('active').stop().animate({
                    'top': '0',
                    'opacity': '1',
                    'z-index': '0'
                  }, 200);
                }
              });
            } else {
              $('.main-indi').addClass('color-change');
              $navi.removeClass('on').eq(3).addClass('on');
              $section.removeClass('active').eq(3).addClass('active');
              $sec02_title.removeClass('on');
              $secInfo.removeClass('active').css({
                'top': '-80%',
                'opacity': '0',
                'z-index': '-1'
              });
            }
          }, 100);
        }
      });
    }
  };
  mainUi.boardCtr = {
    init: function init() {
      var $mainTab = $('.main-tab-list__item'),
          $mainTabConts = $('.main-layout__conts');
      $mainTab.on('click', function () {
        $(this).addClass('on').siblings().removeClass('on');
        var tabItem = $(this).attr('data-tab');
        $mainTabConts.find('.' + tabItem).addClass('on').siblings().removeClass('on');
      });
    }
  };
  mainUi.mobile = {
    init: function init() {
      if ($window.width() < 767) {
        this.moSlick();
        this.moQuick();
        $('.main .slick-arrow').css({
          'display': 'none'
        });
        $('.main .section-02 .mo-view').show();
        $('.main .section-02 .pc-view').hide();
      } else {
        $('.main .section-02 .pc-view').show();
        $('.main .section-02 .mo-view').hide();
      }
    },
    moQuick: function moQuick() {
      var $quickClick = $('.main-visual .mo-view'),
          $quickMenu = $('.main .quick-menu');
      $quickClick.on('click', function () {
        $(this).hide();
        $quickMenu.stop().animate({
          'height': '60rem'
        }, 500);
      });
      $('html').on('click', function (e) {
        if (!$(e.target).is('.quick-menu, .mo-view')) {
          $quickClick.show();
          $quickMenu.stop().animate({
            'height': '10rem'
          }, 500);
        }
      });
    },
    moSlick: function moSlick() {
      var $secInfo = $('.mo-view .section-02-text'),
          $sec02_title = $('.mo-view .section-02-title'),
          _count01 = new countUp.CountUp('count01-mo', 5000),
          _count02 = new countUp.CountUp('count02-mo', 5000),
          _count01Flag = false,
          _count02Flag = false;

      $('.mo-view-slider').not('.slick-initialized').slick({
        dots: true,
        arrows: false,
        pauseOnHover: false,
        infinite: false,
        speed: 1000,
        slidesToShow: 1,
        slidesToScroll: 1,
        autoplay: false
      });
      $('.mo-view-slider').on({
        afterChange: function afterChange(event, slick, currentSlide) {
          $secInfo.each(function () {
            if ($(this).hasClass('slick-current') === true) {
              $secInfo.removeClass('active');
              $(this).addClass('active').stop().animate({
                'top': '0',
                'opacity': '1',
                'z-index': '0'
              }, 100);

              if ($secInfo.eq(1).hasClass('active') === true) {
                setTimeout(function () {
                  if (!_count01Flag) {
                    _count01.start();

                    _count01Flag = true;
                  }

                  _count02Flag = false;

                  _count02.reset();
                }, 100);
              }

              if ($secInfo.eq(2).hasClass('active') === true) {
                setTimeout(function () {
                  if (!_count02Flag) {
                    _count02.start();

                    _count02Flag = true;
                  }

                  _count01Flag = false;

                  _count01.reset();
                }, 100);
              }

              if ($secInfo.eq(3).hasClass('active') === true) {
                $sec02_title.removeClass('on').eq(1).addClass('on');
              } else {
                $sec02_title.removeClass('on').eq(0).addClass('on');
              }
            } else {
              $(this).removeClass('active').stop().animate({
                'top': '10%',
                'opacity': '0',
                'z-index': '-1'
              }, 100);
            }
          });
        }
      });
    }
  };
  mainUi.init();
  return mainUi;
}(window.mainUi || {}, $(window));