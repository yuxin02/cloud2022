package com.wiseco.controller;

import com.wiseco.services.IMessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class SendMessageController {

    @Resource
    private IMessageProvider messageProvider;


    @GetMapping(value = "/sendmsg")
    public String sendMessage() {
       return messageProvider.send();
    }
}
