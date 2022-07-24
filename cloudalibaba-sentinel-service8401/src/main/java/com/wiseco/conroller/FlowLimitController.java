package com.wiseco.conroller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("sentinel")
@Slf4j
public class FlowLimitController {

    @GetMapping("/testA")
    public String testA() {
        return "*****testA*******";
    }

    @GetMapping("/testB")
    public String testB() {
        log.info(Thread.currentThread().getName() + "\t" + "*****testB*******");
        return "*****testB*******";
    }

    @GetMapping("/testD")
    public String testD() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        log.info(Thread.currentThread().getName() + "\t" + "*****testD*******");
        return "*****testD*******";
    }

    @GetMapping("/testE")
    public String testE() {
        if (new Random().nextInt(100) > 80) {
            int a = 1 / 0;
        }
        log.info(Thread.currentThread().getName() + "\t" + "*****testE*******");
        return "*****testE*******";
    }


    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey", blockHandler = "dealTestHotKey")
    public String testHotKey(@RequestParam(value = "p1", required = false) String p1,
                             @RequestParam(value = "p2", required = false) String p2) {
        return "****testHotKey******";
    }

    public String dealTestHotKey(String p1, String p2, BlockException exception) {
        return "****testHotKey******, BlockHandler";
    }
}
