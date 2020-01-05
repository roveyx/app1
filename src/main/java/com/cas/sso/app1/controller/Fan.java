package com.cas.sso.app1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guoyx
 * @since 2020-01-05 04:47
 */
@RestController
public class Fan {

    @RequestMapping("fanbin")
    public String aa(){
        // new
        return "fanbin";
    }
    @RequestMapping("laoguo")
    public String bb(){
        // new
        return "laoguo";
    }
    @RequestMapping("qzj")
    public String aaaa(){
        // new
        return "qzj";
    }
}
