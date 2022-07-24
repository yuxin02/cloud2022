package com.wiseco.myHandler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.wiseco.entities.CommonResult;
import com.wiseco.entities.Payment;

public class CustomBlockHandler {

    public static CommonResult handlerException1(BlockException exception) {
        return new CommonResult(44, "by custom resource -- global exception1");
    }

    public static CommonResult handlerException2(BlockException exception) {
        return new CommonResult(44, "by custom resource -- global exception2");
    }
}
