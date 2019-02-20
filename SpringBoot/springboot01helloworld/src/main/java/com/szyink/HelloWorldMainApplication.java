package com.szyink;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplication 标注一个主程序类，说明这是一个 Spring Boot 应用
 */
@SpringBootApplication
public class HelloWorldMainApplication {

    public static void main(String[] args) {

        // 启动 Spring 应用
        SpringApplication.run(HelloWorldMainApplication.class, args);
    }
}
