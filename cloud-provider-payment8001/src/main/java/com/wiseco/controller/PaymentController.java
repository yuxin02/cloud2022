package com.wiseco.controller;

import com.wiseco.entities.CommonResult;
import com.wiseco.entities.Payment;
import com.wiseco.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private DiscoveryClient discoveryClient;

    @PostMapping(value = "/payment/create")
    public CommonResult<Integer> create(@RequestBody Payment payment) {
        int result = paymentService.create(payment);
        log.info("****Create payment: " + payment);

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

    @GetMapping(value = "/payment/discovery")
    public Object discovery() {
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            log.info("**** service: " + service);
        }

        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info("**** instance: " + instance.getInstanceId() + "\t" +
                    instance.getHost() + "\t" +
                    instance.getPort() + "\t" +
                    instance.getUri());
        }

        return discoveryClient;

    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB() {
        return serverPort;
    }

    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return serverPort;
    }

    @GetMapping("/payment/zipkin")
    public String paymentZipkin() {
        return "Hi, I am from payment zipkin!";
    }
}
