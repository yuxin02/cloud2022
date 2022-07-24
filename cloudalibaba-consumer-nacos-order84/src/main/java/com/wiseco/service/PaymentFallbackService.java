package com.wiseco.service;


import com.wiseco.entities.CommonResult;
import com.wiseco.entities.Payment;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Component
public class PaymentFallbackService implements PaymentService {
    @Override
    public CommonResult<Payment> paymentSQL(Long id) {
        return new CommonResult<>(44444, "服务降级返回,---PaymentFallbackService", new Payment(id, "errorSerial"));
    }
}
