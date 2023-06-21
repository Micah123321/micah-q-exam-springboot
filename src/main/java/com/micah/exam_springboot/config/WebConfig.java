package com.micah.exam_springboot.config;

import com.micah.exam_springboot.interceptor.JwtAuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {


    /**
     * 注册拦截器token拦截器
     */
    private final JwtAuthenticationInterceptor jwtAuthenticationInterceptor;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //addPathPatterns 拦截路径 excludePathPatterns 排除路径
        registry.addInterceptor(jwtAuthenticationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/user/login") //登录
                .excludePathPatterns("/api/user/register") //注册
                .excludePathPatterns("/api/category/**") //分类
                .excludePathPatterns("/api/product/**") //
//                .excludePathPatterns("/api/wallet/**") //
                .excludePathPatterns("/doc.html")//文档地址
        ;
    }
    /**
     * 文件命名
     *
     * @return 文件
     */
    @Bean
    public TomcatServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((Connector connector) -> {
            connector.setProperty("relaxedPathChars", "\"<>[\\]^`{|}");
            connector.setProperty("relaxedQueryChars", "\"<>[\\]^`{|}");
        });
        return factory;
    }

    /**
     * 映射服务类
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //接口文档资源
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/");
//        System.out.println("file:" + path);
    }


}
