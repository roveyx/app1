package com.cas.sso.app1.configuration;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: WangSaiChao
 * @date: 2018/8/6
 * @description: 自动化配置类
 */
@Configuration
@EnableConfigurationProperties(CasClientConfigurationProperties.class)
public class CasClientConfiguration {

    @Autowired
    CasClientConfigurationProperties configProps;

    /**
     * 配置登出过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterSingleRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SingleSignOutFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String,String>  initParameters = new HashMap<String, String>();
        initParameters.put("casServerUrlPrefix", configProps.getServerUrlPrefix());
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(0);
        return registration;
    }

    /**
     * 配置过滤验证器 这里用的是Cas30ProxyReceivingTicketValidationFilter
     * @return
     */
    @Bean
    public FilterRegistrationBean filterValidationRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String,String> initParameters = new HashMap<String, String>();
        initParameters.put("casServerUrlPrefix", configProps.getServerUrlPrefix());
        initParameters.put("serverName", configProps.getClientHostUrl());
        initParameters.put("useSession", "true");
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(2);
        return registration;
    }

    /**
     * 配置授权过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterAuthenticationRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ReAuthenticationFilter());


        Map<String,String>  initParameters = new HashMap<String, String>();
        initParameters.put("casServerLoginUrl", configProps.getServerLoginUrl());
        initParameters.put("serverName", configProps.getClientHostUrl());
        if(configProps.getIgnorePattern() != null && !"".equals(configProps.getIgnorePattern())){
            initParameters.put("ignorePattern", configProps.getIgnorePattern());
        }
        //自定义UrlPatternMatcherStrategy 验证规则
        if(configProps.getIgnoreUrlPatternType() != null && !"".equals(configProps.getIgnoreUrlPatternType())){
            initParameters.put("ignoreUrlPatternType", configProps.getIgnoreUrlPatternType());
        }
        if(configProps.getExceptPaths() != null && !"".equals(configProps.getExceptPaths())){
            System.out.println("我设置了过滤的地址是-----"+configProps.getExceptPaths());
            initParameters.put("exceptPaths", configProps.getExceptPaths());
        }
        // 设定匹配的路径
        // registration.addUrlPatterns("/*");
        // 设定加载的顺序
        registration.setOrder(3);
        registration.setInitParameters(initParameters);

        return registration;
    }

    /**
     * request wraper过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterWrapperRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpServletRequestWrapperFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        // 设定加载的顺序
        registration.setOrder(4);
        return registration;
    }

    /**
     * 添加监听器
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean<EventListener> singleSignOutListenerRegistration(){
        ServletListenerRegistrationBean<EventListener> registrationBean = new ServletListenerRegistrationBean<EventListener>();
        registrationBean.setListener(new SingleSignOutHttpSessionListener());
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
