package com.wiseco.conroller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.wiseco.service.PaymentHystrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("consumer")
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")// hystrix 全局fallback方法
public class OrderHystrixController {
    @Resource
    private PaymentHystrixService paymentHystrixService;

    @GetMapping("/payment/hystrix/ok/{id}")
    public String paymentInfoOK(@PathVariable("id") Integer id) {
        String result = paymentHystrixService.paymentInfoOK(id);
        log.info("*****Payment result: " + result);
        return result;
    }

    /**
     * @GetMapping("/payment/hystrix/timeout/{id}") public String paymentInfoTimeout(@PathVariable("id") Integer id) {
     * String result = paymentHystrixService.paymentInfoTimeOut(id);
     * log.info("*****Payment result: " + result);
     * return result;
     * }
     */


//    @HystrixCommand(fallbackMethod = "paymentTimeOutFallbackMethod", commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
//    })
    @HystrixCommand
    @GetMapping("/payment/hystrix/timeout/{id}")
    public String paymentInfoTimeOut(@PathVariable("id") Integer id) {
        int age = 10 / 0;
        String result = null;
        try {
            result = paymentHystrixService.paymentInfoTimeOut(id);
            log.info("*****Payment result: " + result);
        } catch (HystrixRuntimeException e) {
            log.error(e.getMessage());
            return "异常处理信息，请稍后再试，/(ㄒoㄒ)/~~";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "异常处理信息，请稍后再试，/(ㄒoㄒ)/~~";
        }
        return result;
    }

    /**
     * 超时访问，设置自身调用超时的峰值，峰值内正常运行，超过了峰值需要服务降级
     * 自动调用fallbackMethod 指定的方法
     * <br/>
     * 超时异常或者运行异常 都会进行服务降级
     */
    public String paymentTimeOutFallbackMethod(Integer id) {
        return "我是消费者80,对方支付系统繁忙请10秒钟后再试或者自己运行出错请检查自己,o(╥﹏╥)o";
    }

    /**
     * hystrix 全局fallback方法
     */
    public String payment_Global_FallbackMethod() {
        return "Global异常处理信息，请稍后再试，/(ㄒoㄒ)/~~";
    }

}
