package com.agroup.store.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @Value("${test.hello:default value}")
    private String testHello;

    // http://127.0.0.1:8880/hello
    @RequestMapping("/hello") //四种HTTP请求都支持
    //@RequestMapping(
    //  value = "/hello", method = RequestMethod.GET)
    //@RequestMapping(
    //  value = "/user/1", method = RequestMethod.GET/DELETE)
    //第一个只支持GET restful
    public String hello(){
        return "Hello World! " + testHello;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "Hello World! Post, " + name;
    }
}
