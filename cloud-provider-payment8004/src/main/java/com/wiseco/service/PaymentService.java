package com.wiseco.service;

import com.wiseco.entities.Payment;

public interface PaymentService {
    int create(Payment payment);

    Payment getPaymentByID(Long id);
}
