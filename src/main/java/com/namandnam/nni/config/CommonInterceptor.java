package com.namandnam.nni.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CommonInterceptor extends HandlerInterceptorAdapter{

    private String mobileWebUrl;

    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object Handler) throws Exception{

        if(request.getMethod().toUpperCase().equals("GET")&&request.getRequestURI().indexOf('.')==-1&&!"/".equals(request.getRequestURI())){

            Device device=DeviceUtils.getCurrentDevice(request);

//            if(device.isMobile()||device.isTablet()){
//                String redirectUrl=null;
//                StringBuilder mobileUrl=new StringBuilder();
//                mobileUrl.append(mobileWebUrl);
//                mobileUrl.append(request.getRequestURI());
//
//                if(null!=request.getQueryString()&&!"".equals(request.getQueryString())){
//                    mobileUrl.append("?");
//                    mobileUrl.append(request.getQueryString());
//                }
//
//                redirectUrl=mobileUrl.toString();
//
//                URL url=new URL(redirectUrl);
//                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
//                connection.setRequestMethod("GET");
//                connection.connect();
//                int code=connection.getResponseCode();
//
//                if(code!=200){
//                    redirectUrl=mobileWebUrl;
//                }
//
//                response.sendRedirect(redirectUrl);
//            }else{
//
//            }

            log.debug("device {} context : {} uri : {} remote : {}  referer : {}",device,request.getContextPath(),request.getRequestURI(),request.getRemoteAddr(),request.getHeader("referer"));
        }

        
        //return true;
        return super.preHandle(request,response,Handler);
    }

    @Override
    public void postHandle(HttpServletRequest request,HttpServletResponse response,Object obj,ModelAndView mav) throws Exception{

        Device device=DeviceUtils.getCurrentDevice(request);

    }

}
