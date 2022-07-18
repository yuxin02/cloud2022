package com.wiseco.lb;

import cn.hutool.core.util.RandomUtil;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MyWeightLBImpl implements MyLoadBalancer {
    // 原子类
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * @description 判断时第几次访问
     */
    public final int getAndIncrement() {
        int current;
        String a = "current";
        int next = 0;
        do {
            current = atomicInteger.get();
            // 防止越界
            next = current >= Integer.MAX_VALUE ? 0 : current + 1;
        } while (!atomicInteger.compareAndSet(current, next));
        System.out.println("*****第几次访问，次数next: " + next);
        return next;
    }

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
//        int index = getAndIncrement() % serviceInstances.size();
        int r = RandomUtil.randomInt(100);
        if (r < 20) {
            return serviceInstances.get(0);
        }
        return serviceInstances.get(1);

    }
}
