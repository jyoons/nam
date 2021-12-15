"use strict";

var uiCommon = function (uiCommon, $window) {
  uiCommon.init = function () {
    uiCommon.gnb.init();
  };

  uiCommon.gnb = {
    init: function init() {}
  };
  uiCommon.init();
  return uiCommon;
}(window.uiCommon || {}, $(window));