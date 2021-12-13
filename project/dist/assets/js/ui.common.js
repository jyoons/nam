"use strict";

var uiCommon = function (uiCommon, $window) {
  uiCommon.init = function () {
    console.log('test test111');
  };

  uiCommon.init();
  return uiCommon;
}(window.uiCommon || {}, $(window));