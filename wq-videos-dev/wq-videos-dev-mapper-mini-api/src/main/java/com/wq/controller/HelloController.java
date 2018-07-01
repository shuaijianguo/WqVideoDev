package com.wq.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wuqingvika on 2018/7/1.
 */
@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String sayHello(){
        return "hello springboot,hello wuqingvika!";
    }
}
