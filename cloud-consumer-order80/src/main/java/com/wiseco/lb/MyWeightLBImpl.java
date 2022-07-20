package com.wiseco.lb;

import cn.hutool.core.util.RandomUtil;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
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

    public Object randomWeight(Map<String, Integer> serverMap) {
        int length = serverMap.size(); // 总个数
        int totalWeight = 0; // 总权重
        boolean sameWeight = true; // 权重是否都一样
        // TODO: 如果serverMap被其它线程修改了如何处理
        List<String> servers = new ArrayList<>(serverMap.keySet());

        for (int i = 0; i < length; i++) {
            int weight = serverMap.get(servers.get(i));
            totalWeight += weight; // 累计总权重
            if (sameWeight && i > 0 && weight != serverMap.get(servers.get(i - 1))) {
                sameWeight = false; // 计算所有权重是否一样
            }
        }

        if (totalWeight > 0 && !sameWeight) {
            // 如果权重不相同且权重大于0则按总权重数随机
            int offset = ThreadLocalRandom.current().nextInt(totalWeight);
            // 并确定随机值落在哪个片断上
            for (int i = 0; i < length; i++) {
                offset -= serverMap.get(servers.get(i));
                if (offset < 0) {
                    return servers.get(i);
                }
            }
        }
        // 如果权重相同或权重为0则均等随机
        return servers.get(ThreadLocalRandom.current().nextInt(length));
    }

    public static void main(String[] args) {
        Map<String, Integer> serverMap = new HashMap() {{
            put("192.168.1.100", 1);
            put("192.168.1.101", 4);
//        put("192.168.1.102", 4);
//        put("192.168.1.103", 1);
//        put("192.168.1.104", 1);
//        put("192.168.1.105", 3);
//        put("192.168.1.106", 1);
//        put("192.168.1.107", 2);
//        put("192.168.1.108", 1);
//        put("192.168.1.109", 1);
//        put("192.168.1.110", 1);
        }};

        for (int i = 0; i < 100; i++) {
            System.out.println(new MyWeightLBImpl().randomWeight(serverMap));
        }
    }
}
