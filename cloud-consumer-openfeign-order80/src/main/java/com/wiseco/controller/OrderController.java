package com.wiseco.controller;

import com.wiseco.entities.CommonResult;
import com.wiseco.entities.Payment;
import com.wiseco.service.PaymentFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("consumer")
@Slf4j
public class OrderController {

    @Resource
    private PaymentFeignService paymentFeignService;

    @GetMapping(value = "/payment/get/{id}")
    public  CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        return paymentFeignService.getPaymentById(id);
    }

    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout() {
        return paymentFeignService.paymentFeignTimeout();
    }
}
