package com.wiseco.service.impl;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.wiseco.dao.PaymentDao;
import com.wiseco.entities.Payment;
import com.wiseco.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Resource
    private PaymentDao paymentDao;

    @Override
    public int create(Payment payment) {
        return paymentDao.create(payment);
    }

    @Override
    public Payment getPaymentByID(Long id) {
        return paymentDao.getPaymentByID(id);
    }


    public String paymentInfoOK(Long id) {
        return "ThreadPool:" + Thread.currentThread().getName() + "\tpaymentInfoOK\tid=" + id;
    }

    /**
     * 常规方法，无容错处理
     *
     * @Override public String paymentInfoTimeout(Long id) {
     * int timeout = 3;
     * try {
     * TimeUnit.SECONDS.sleep(timeout);
     * } catch (InterruptedException ex) {
     * ex.printStackTrace();
     * }
     * return "ThreadPool:" + Thread.currentThread().getName() + "\tpaymentInfoTimeout\tid=" + id + "\tTimeout: " + timeout;
     * }
     */
    @Override
    @HystrixCommand(fallbackMethod = "paymentInfoTimeoutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    })
    public String paymentInfoTimeout(Long id) {
        int timeout = 3;
        System.out.println("start to sleep: " + timeout);
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
//        int a = 10 / 0;
        return "ThreadPool:" + Thread.currentThread().getName() + "\tpaymentInfoTimeout\tid=" + id + "\tTimeout: " + timeout;
    }

    public String paymentInfoTimeoutHandler(Long id) {
        return "ThreadPool:" + Thread.currentThread().getName() + "\tpaymentInfoTimeoutHandler\tid=" + id + "\t!!!TIMEOUT!!!";
    }


    @HystrixCommand(fallbackMethod = "paymentCircuitBreakerFallback",
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),              // 是否开启断路器
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), // 请求次数
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), // 时间窗口期
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),// 失败率达到多少后跳闸
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")// 超时处理
            })
    public String paymentCircuitBreaker(Integer id) {
        if (id < 0) {
            throw new RuntimeException("******id 不能负数");
        }
        //测试异常
//        int age = 10 / 0;
//        int second = 3;
//        try {
//            TimeUnit.SECONDS.sleep(second);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        String serialNumber = IdUtil.simpleUUID();
        return Thread.currentThread().getName() + "\t" + "调用成功，流水号: " + serialNumber;
    }

    /**
     * paymentCircuitBreaker 方法的 fallback，<br/>
     * 当断路器开启时，主逻辑熔断降级，该 fallback 方法就会替换原 paymentCircuitBreaker 方法，处理请求
     *
     * @param id
     * @return
     */
    public String paymentCircuitBreakerFallback(Integer id) {
        return Thread.currentThread().getName() + "\t" + "id 不能负数或超时或自身错误，请稍后再试，/(ㄒoㄒ)/~~   id: " + id;
    }

}
