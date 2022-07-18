package com.wiseco.controller;

import com.wiseco.entities.CommonResult;
import com.wiseco.entities.Payment;
import com.wiseco.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;

@Slf4j
@RestController
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @PostMapping(value = "/payment/create")
    public CommonResult<Integer> create(@RequestBody Payment payment) {
        int result = paymentService.create(payment);
        log.info("****Create payment: " + payment);
        log.info("****hahah : " + payment);

        if (result > 0) {
            return new CommonResult<>(200, "Success!" + serverPort, result);

        } else {
            return new CommonResult<>(444, "Failure!" + serverPort, null);
        }
    }

    @GetMapping(value = "/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentByID(id);
        log.info("****Query by id = " + id + ": " + payment);
        if (payment != null) {
            return new CommonResult<>(200, "Success!" + serverPort, payment);
        } else {
            return new CommonResult<>(444, "Failure!" + serverPort + " id = " + id, null);
        }
    }

    @RequestMapping(value = "/payment/consul")
    public String paymentConsul() {
        return "SpringCloud with consul: " + serverPort + "\t" + UUID.randomUUID();
    }
}
