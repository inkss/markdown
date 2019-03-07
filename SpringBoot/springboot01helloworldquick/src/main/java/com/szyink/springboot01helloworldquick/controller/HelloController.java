package com.szyink.springboot01helloworldquick.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello(){
        return "hello world,quick!";
    }
}

/*
@Controller 注解：表明该类是控制类
@RequestMapping 注解：处理请求
@ResponseBody 注解：返回数据给浏览器

>>>> REST API 一个请求，一个数据
>>>> 所以可能会有多个请求返回，每一个返回都需要 ResponseBody ，故可以把它放到类前面

>> 原：
@Controller
public class HelloController {

    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){}
}

>> 现：表明这个类的所有方法的返回数据都交给浏览器
@Controller
@ResponseBody
public class HelloController {
    ...
}

@RestController 注解：可以视为 Controller 和 ResponseBody

>>>> RestController 为组合注解，具体内容有：
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@ResponseBody

 */