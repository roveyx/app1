package com.cas.sso.app1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author guoyx
 * @since 2020-01-03 00:06
 */
@Controller
@RequestMapping("/test")
public class Test {

    @RequestMapping("/aa")
    @ResponseBody
    public String hello() {
        System.out.println("ddddddddddddddd");
        return "bbbbbb";
    }

    @RequestMapping("/out")
    public String loginOut(HttpSession session){
        session.invalidate();
        //这个是直接退出，走的是默认退出方式
        return "redirect:http://localhost:8090/cas/logout";
    }

    @RequestMapping("/hello")
    public String hello2() {
        return "hello world";
    }
}
