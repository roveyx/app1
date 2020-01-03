package com.cas.sso.app1;

import com.cas.sso.app1.configuration.EnableCasClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rovey
 */
@SpringBootApplication
@ServletComponentScan
@EnableCasClient
public class App1Application {

    public static void main(String[] args) {
        SpringApplication.run(App1Application.class, args);
    }



}
