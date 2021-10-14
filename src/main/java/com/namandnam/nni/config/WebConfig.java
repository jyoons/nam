
package com.namandnam.nni.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.namandnam.nni.common.utils.OSValidator;

@Configuration
@EnableAspectJAutoProxy
public class WebConfig implements WebMvcConfigurer {
	
	@Value("${env.file.path}")
	private String prefix_upload_path;
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 삭제예정 : 로컬에서 퍼블보는 용도.
        //registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        //registry.addResourceHandler("/html/**").addResourceLocations("classpath:/html/");
    	
    	String attachFilePath = "file:///"+prefix_upload_path;;
    	
    	if( !OSValidator.isWindows() ) {
			attachFilePath = "file:"+prefix_upload_path;
		}
    	
    	registry.addResourceHandler("/attach/**").addResourceLocations( attachFilePath );
    	
    	
    }

    @Autowired
    private CommonInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(deviceResolverHandlerInterceptor());

        registry.addInterceptor(interceptor).addPathPatterns("/**")
        		.excludePathPatterns("/upload/**").excludePathPatterns("/static/**")
        		.excludePathPatterns("/error").excludePathPatterns("/cgi-bin/**");

    }
    
//    @Bean
//    public ServletWebServerFactory factory() {
//        return new TomcatServletWebServerFactory() {
//            @Override
//            protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
//            	
//            	String docBase = new File("C://temp").getAbsolutePath();
//            	
//            	System.out
//						.println("WebConfig.factory().new TomcatServletWebServerFactory() {...}.getTomcatWebServer() :: " + docBase);
//            	
//                tomcat.addContext("/aaab", docBase  );
//              
//                return super.getTomcatWebServer(tomcat);
//            }
//        };      
//    }

    @Bean
    public ObjectMapper getBeanObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper;
    }

    @Bean
    public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
        return new DeviceResolverHandlerInterceptor();
    }
    
    @Bean
    // 메세지 엑세서 MessageSource 사용해도 되는데, 메소드가 더 많음
    public MessageSourceAccessor getMessageSourceAccessor(){
    	return new MessageSourceAccessor(messageSource());
    }
    
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        //WEB-INF 밑에 해당 폴더에서 properties를 찾는다.
        messageSource.setBasenames("messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(10); // reload messages every 10 seconds
        return messageSource;
    }

}
