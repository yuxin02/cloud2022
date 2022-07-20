package com.wiseco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableHystrix
public class ConsumerFeignHystrixOrder80 {
    public static void main(String[] args){
        SpringApplication.run(ConsumerFeignHystrixOrder80.class, args);
        System.out.println("启动成功");
    }
}