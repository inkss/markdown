package com.szyink.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // 处理请求
public class HelloController {

    @ResponseBody // 返回给浏览器 SpringMVC 基础
    @RequestMapping("/hello") // 接受浏览器的 hello 请求
    public String hello(){
        return "Hello World!";
    }
}
