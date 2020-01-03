package com.cas.sso.app1;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rovey
 */
@Configuration
public class ServletConfigure {

    /**
     * 代码注册servlet
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        // ServletName默认值为首字母小写，即myServlet1
        return new ServletRegistrationBean(new CasLogin(), "/*");
    }

//    /**
//     * 多个servlet就注册多个bean
//     * @return
//     */
//    @Bean
//    public ServletRegistrationBean servletRegistrationBean1() {
//        return new ServletRegistrationBean(new MyServlet(), "/servlet/myServlet");// ServletName默认值为首字母小写，即myServlet
//    }


}