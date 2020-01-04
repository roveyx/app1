package com.cas.sso.app1.controller;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author guoyx
 * @since 2020-01-03 00:06
 */
@Controller
@RequestMapping("/test")
public class Test {

    @RequestMapping("/aa")
    @ResponseBody
    public String aa(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("ddddddddddddddd");

        return "bbbbbb";
    }

    @RequestMapping("/loginCas")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // cas认证成功，获取用户信息
        AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();

        String userId = principal.getName();
        String url = request.getRequestURL().toString();
        Cookie[] cookies = request.getCookies();
        String ticket = null;
        for (Cookie cookie : cookies) {
            if ("JSESSIONID".equals(cookie.getName())) {
                ticket = cookie.getValue();
            }
        }

        System.out.println("------");
        System.out.println(ticket);
        System.out.println(url);
        System.out.println(userId);
        String urll = "http://localhost/sso/integrationConfig/loginCas?url=" + url + "&userId=" + userId + "&token=" + ticket;
        System.out.println(urll);

        response.sendRedirect("http://cas-app1.com:8081/app1/test/hello");
    }

    @RequestMapping("/out")
    public String loginOut(HttpSession session) {
        session.invalidate();
        //这个是直接退出，走的是默认退出方式
        return "redirect:http://cas-server.com:8090/cas/logout";
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello world";
    }
}
